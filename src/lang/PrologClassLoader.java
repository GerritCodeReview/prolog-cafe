package com.googlecode.prolog_cafe.lang;
import java.io.Serializable;
/**
 * Prolog class loader.
 *
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.1
 */
public class PrologClassLoader extends ClassLoader implements Serializable {
    /** Initialize using the {@link ClassLoader#getSystemClassLoader()}.  */
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
     * Returns a <code>java.lang.Class</code> object associated with the predicate
     * class with the given arguments.
     * @param pkg package name
     * @param functor predicate name
     * @param arity predicate arity
     * @param resolve If <code>true</code> then resolve the class
     * @return a <code>java.lang.Class</code> object associated with the predicate
     * class that corresponds to <code>pkg:functor/arity</code>
     * if exists, otherwise throws <code>ClassNotFoundException</code>.
     * @exception ClassNotFoundException
     */
    public Class loadPredicateClass(String pkg,
				    String functor,
				    int arity,
				    boolean resolve) throws ClassNotFoundException {
	return loadClass(PredicateEncoder.encode(pkg, functor, arity), resolve);
    }

    /**
     * Check whether the predicate class for the given arguments is defined.
     * @param pkg package name
     * @param functor predicate name
     * @param arity predicate arity
     * @return <code>true</code> if the predicate <code>pkg:functor/arity</code>
     * is defined, otherwise <code>false</code>.
     */
    public boolean definedPredicate(String pkg,
				    String functor,
				    int arity) {
	String cname = PredicateEncoder.encode(pkg, functor, arity);
	return getResource(cname.replace('.', '/') + ".class") != null;
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
      Class<Predicate> clazz = loadPredicateClass(pkg, functor, arity, true);

      Class[] params = new Class[arity + 1];
      for (int i = 0; i < arity; i++)
        params[i] = Term.class;
      params[arity] = Operation.class;

      Object[] a = new Object[arity + 1];
      for (int i = 0; i < arity; i++)
        a[i] = args[i];
      a[arity] = cont;
      return (Predicate) clazz.getDeclaredConstructor(params).newInstance(a);
    } catch (Exception err) {
      SymbolTerm colon2 = SymbolTerm.makeSymbol(":", 2);
      SymbolTerm slash2 = SymbolTerm.makeSymbol("/", 2);
      Term[] fa = {SymbolTerm.makeSymbol(functor), new IntegerTerm(arity)};
      Term[] r = {SymbolTerm.makeSymbol(pkg), new StructureTerm(slash2, fa)};
      Term what = new StructureTerm(colon2, r);

      ExistenceException e2 = new ExistenceException("procedure", new StructureTerm(slash2, fa), err.toString());
      e2.initCause(err);
      throw e2;
    }
  }
}
