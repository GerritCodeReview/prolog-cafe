package com.googlecode.prolog_cafe.lang;

import static com.googlecode.prolog_cafe.lang.PredicateEncoder.encode;

import com.googlecode.prolog_cafe.exceptions.ExistenceException;

import java.lang.reflect.Constructor;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Prolog class loader.
 *
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.1
 */
public class PrologClassLoader extends ClassLoader {
  private final ConcurrentHashMap<Key, CacheEntry> predicateCache =
      new ConcurrentHashMap<Key, CacheEntry>();

  /** Initialize using the {@link ClassLoader#getSystemClassLoader()}. */
  public PrologClassLoader() {
  }

  /**
   * Initialize using a specific parent ClassLoader.
   *
   * @param parent source for all predicates in this context.
   */
  public PrologClassLoader(ClassLoader parent) {
    super(parent);
  }

  /**
   * Check whether the predicate class for the given arguments is defined.
   *
   * @param pkg package name
   * @param functor predicate name
   * @param arity predicate arity
   * @return <code>true</code> if the predicate <code>pkg:functor/arity</code>
   *         is defined, otherwise <code>false</code>.
   */
  public boolean definedPredicate(String pkg, String functor, int arity) {
    try {
      return findPredicate(pkg, functor, arity) instanceof ValidPredicate;
    } catch (ClassNotFoundException cnfe) {
      return false;
    }
  }

  /**
   * Allocate a predicate and configure it with the specified arguments.
   *
   * @param pkg package the predicate is in.
   * @param functor name of the predicate.
   * @param args arguments to pass. The arity is derived from the arguments.
   * @return the predicate encapsulating the logic and the arguments.
   */
  public Predicate predicate(String pkg, String functor, Term... args) {
    return predicate(pkg, functor, Success.SUCCESS, args);
  }

  /**
   * Allocate a predicate and configure it with the specified arguments.
   *
   * @param pkg package the predicate is in.
   * @param functor name of the predicate.
   * @param cont operation to execute if the predicate is successful. Usually
   *        this is {@link Success#SUCCESS}.
   * @param args arguments to pass. The arity is derived from the arguments.
   * @return the predicate encapsulating the logic and the arguments.
   */
  public Predicate predicate(String pkg, String functor, Operation cont, Term... args) {
    int arity = args.length;
    try {
      CacheEntry ent = findPredicate(pkg, functor, arity);
      if (ent instanceof ValidPredicate) {
        Object[] a = new Object[arity + 1];
        for (int i = 0; i < arity; i++)
          a[i] = args[i];
        a[arity] = cont;
        return ((ValidPredicate) ent).constructor.newInstance(a);
      }
    } catch (Exception cause) {
      ExistenceException err2 = new ExistenceException(
          "procedure",
          term(pkg, functor, arity),
          cause.toString());
      err2.initCause(cause);
      throw err2;
    }
    throw new ExistenceException("procedure", term(pkg, functor, arity), "NOT_FOUND");
  }

  private static StructureTerm term(String pkg, String functor, int arity) {
    return new StructureTerm(":",
      SymbolTerm.create(pkg),
      new StructureTerm("/",
        SymbolTerm.create(functor),
        new IntegerTerm(arity)));
  }

  private CacheEntry findPredicate(String pkg, String functor, int arity)
      throws ClassNotFoundException {
    Key key = new Key(pkg, functor, arity);
    CacheEntry entry = predicateCache.get(key);
    if (entry == null) {
      Class<?> clazz;
      try {
        clazz = Class.forName(
            encode(pkg, functor, arity),
            false /* avoid resolve */,
            this);
      } catch (ClassNotFoundException cnfe) {
        predicateCache.put(key, NotFound.INSTANCE);
        throw cnfe;
      }

      if (!Predicate.class.isAssignableFrom(clazz)) {
        predicateCache.put(key, NotFound.INSTANCE);
        throw new ClassNotFoundException(clazz.getName(),
          new ClassCastException("Does not extend " + Predicate.class));
      }

      Class[] params = new Class[arity + 1];
      for (int i = 0; i < arity; i++)
        params[i] = Term.class;
      params[arity] = Operation.class;

      Constructor<Predicate> cons;
      try {
        cons = (Constructor<Predicate>) clazz.getDeclaredConstructor(params);
      } catch (NoSuchMethodException e) {
        predicateCache.put(key, NotFound.INSTANCE);
        throw new ClassNotFoundException("Wrong constructor on " + clazz.getName(), e);

      } catch (SecurityException e) {
        predicateCache.put(key, NotFound.INSTANCE);
        throw new ClassNotFoundException("Constructor not visible " + clazz.getName(), e);
      }
      cons.setAccessible(true);

      try {
        Class.forName(clazz.getName(), true /* resolve now */, this);
      } catch (ClassNotFoundException e) {
        predicateCache.put(key, NotFound.INSTANCE);
        throw new ClassNotFoundException("Cannot initialize " + clazz.getName(), e);

      } catch (RuntimeException e) {
        predicateCache.put(key, NotFound.INSTANCE);
        throw new ClassNotFoundException("Cannot initialize " + clazz.getName(), e);

      } catch (LinkageError e) {
        predicateCache.put(key, NotFound.INSTANCE);
        throw new ClassNotFoundException("Cannot initialize " + clazz.getName(), e);
      }

      entry = new ValidPredicate(cons);
      predicateCache.put(key, entry);
    }
    return entry;
  }

  private static final class Key {
    final String pkg;
    final String functor;
    final int arity;

    Key(String pkg, String functor, int arity) {
      this.pkg = pkg;
      this.functor = functor;
      this.arity = arity;
    }

    @Override
    public int hashCode() {
      int h = pkg.hashCode();
      h = (h * 31) + functor.hashCode();
      h = (h * 31) + arity;
      return h;
    }

    @Override
    public boolean equals(Object other) {
      if (other instanceof Key) {
        Key b = (Key) other;
        return arity == b.arity && pkg.equals(b.pkg) && functor.equals(b.functor);
      }
      return false;
    }
  }

  private static abstract class CacheEntry {
  }

  private static class NotFound extends CacheEntry {
    static final NotFound INSTANCE = new NotFound();
  }

  private static class ValidPredicate extends CacheEntry {
    final Constructor<Predicate> constructor;

    ValidPredicate(Constructor<Predicate> c) {
      constructor = c;
    }
  }
}
