package com.googlecode.prolog_cafe.lang;
import com.googlecode.prolog_cafe.exceptions.InternalException;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentHashMap;
/**
 * Atom.<br>
 * The <code>SymbolTerm</code> class represents a Prolog atom.<br>
 *
 * <pre>
 *   Term t = SymbolTerm.makeSymbol("kobe");
 *   String name = ((SymbolTerm)t).name();
 * </pre>
 *
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
 */
public abstract class SymbolTerm extends Term {
    /** Symbol table. */
    private static final ConcurrentHashMap<Key, InternRef> SYMBOL_TABLE =
      new ConcurrentHashMap<Key, InternRef>();

    private static final ReferenceQueue<Interned> DEAD = new ReferenceQueue<Interned>();

    private static final class Key {
      final String name;
      final int arity;

      Key(String n, int a) {
        name = n;
        arity = a;
      }

      @Override
      public int hashCode() {
        return name.hashCode();
      }

      @Override
      public boolean equals(Object other) {
        Key k = (Key) other;
        return arity == k.arity && name.equals(k.name);
      }
    }

    private static final class InternRef extends WeakReference<Interned> {
      final Key key;

      InternRef(Key key, Interned sym) {
        super(sym, DEAD);
        this.key = key;
      }
    }

    private static final class Dynamic extends SymbolTerm {
      Dynamic(String name, int arity) {
        super(name, arity);
      }
    }

    private static final class Interned extends SymbolTerm {
      Interned(String name, int arity) {
        super(name, arity);
      }
    }

    private static final SymbolTerm colon2 = intern(":", 2);

    /** Returns a Prolog atom for the given character. */
    public static SymbolTerm create(char c) {
      if (0 <= c && c <= 127)
        return intern(Character.toString(c), 0);
      else
        return create(Character.toString(c));
    }

    /** Returns a Prolog atom for the given name. */
    public static SymbolTerm create(String _name) {
      return new Dynamic(_name, 0);
    }

    /** Returns a Prolog atom for the given name. */
    public static SymbolTerm create(String _name, int arity) {
      // For a non-zero arity try to reuse the term, its probable this is a
      // structure term and those are more commonly declared in code
      // to be a type of object the code manipulates, therefore also very
      // likely to already be in the cache.
      if (arity != 0)
        return softReuse(_name, arity);

      return new Dynamic(_name, 0);
    }

    /** Returns a Prolog functor for the given name and arity. */
    public static StructureTerm create(String pkg, String name, int arity) {
      // This is likely a specific function that exists in code, so try to reuse
      // the symbols that are involved in the term.
      return new StructureTerm(colon2, softReuse(pkg, 0), softReuse(name, arity));
    }

    /** Returns a Prolog atom for the given name. */
    public static SymbolTerm intern(String _name) {
      return intern(_name, 0);
    }

    /** Returns a Prolog functor for the given name and arity. */
    public static SymbolTerm intern(String _name, int _arity) {
      _name = _name.intern();
      Key key = new Key(_name, _arity);

      Reference<? extends Interned> ref = SYMBOL_TABLE.get(key);
      if (ref != null) {
        Interned sym = ref.get();
        if (sym != null)
          return sym;
        SYMBOL_TABLE.remove(key, ref);
        ref.enqueue();
      }

      gc();

      Interned sym = new Interned(_name, _arity);
      InternRef nref = new InternRef(key, sym);
      InternRef oref = SYMBOL_TABLE.putIfAbsent(key, nref);
      if (oref != null) {
        SymbolTerm osym = oref.get();
        if (osym != null)
          return osym;
      }
      return sym;
    }

    static void gc() {
      Reference<? extends Interned> ref;
      while ((ref = DEAD.poll()) != null) {
        SYMBOL_TABLE.remove(((InternRef) ref).key, ref);
      }
    }

    private static SymbolTerm softReuse(String _name, int _arity) {
      Key key = new Key(_name, _arity);
      Reference<? extends Interned> ref = SYMBOL_TABLE.get(key);
      if (ref != null) {
        Interned sym = ref.get();
        if (sym != null)
          return sym;
        SYMBOL_TABLE.remove(key, ref);
        ref.enqueue();
      }

      // If reuse wasn't possible, construct the term dynamically.
      return new Dynamic(_name, _arity);
    }

    /** Holds a string representation of this <code>SymbolTerm</code>. */
    protected final String name;

    /** Holds the arity of this <code>SymbolTerm</code>. */
    protected final int arity;

    /** Constructs a new Prolog atom (or functor) with the given symbol name and arity. */
    protected SymbolTerm(String _name, int _arity) {
	name  = _name;
	arity = _arity;
    }

    /** Returns the arity of this <code>SymbolTerm</code>.
     * @return the value of <code>arity</code>.
     * @see #arity
     */
    public int arity() { return arity; }

    /** Returns the string representation of this <code>SymbolTerm</code>.
     * @return the value of <code>name</code>.
     * @see #name
     */
    public String name() { return name; }

    /* Term */
    public boolean unify(Term t, Trail trail) {
      t = t.dereference();
      if (t instanceof VariableTerm) {
        ((VariableTerm) t).bind(this, trail);
        return true;
      }
      return eq(this, t);
    }

    @Override
    public int hashCode() {
      return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
      return obj instanceof Term && eq(this, (Term) obj);
    }

    private static boolean eq(SymbolTerm a, Term b0) {
      if (a == b0) {
        return true;
      } else if (b0 instanceof SymbolTerm && (a instanceof Dynamic || b0 instanceof Dynamic)) {
        SymbolTerm b = (SymbolTerm) b0;
        return a.arity == b.arity && a.name.equals(b.name);
      } else {
        return false;
      }
    }

    /**
     * @return the <code>boolean</code> whose value is
     * <code>convertible(String.class, type)</code>.
     * @see Term#convertible(Class, Class)
     */
    public boolean convertible(Class type) { return convertible(String.class, type); }

    /**
     * Returns a <code>java.lang.String</code> corresponds to this <code>SymbolTerm</code>
     * according to <em>Prolog Cafe interoperability with Java</em>.
     * @return a <code>java.lang.String</code> object equivalent to
     * this <code>SymbolTerm</code>.
     */
    public Object toJava() { return name; }

    public String toQuotedString() { return Token.toQuotedString(name); }

    /** Returns a string representation of this <code>SymbolTerm</code>. */
    public String toString() { return name; }

    /* Comparable */
    /**
     * Compares two terms in <em>Prolog standard order of terms</em>.<br>
     * It is noted that <code>t1.compareTo(t2) == 0</code> has the same
     * <code>boolean</code> value as <code>t1.equals(t2)</code>.
     * @param anotherTerm the term to compared with. It must be dereferenced.
     * @return the value <code>0</code> if two terms are identical;
     * a value less than <code>0</code> if this term is <em>before</em> the <code>anotherTerm</code>;
     * and a value greater than <code>0</code> if this term is <em>after</em> the <code>anotherTerm</code>.
     */
    public int compareTo(Term anotherTerm) { // anotherTerm must be dereferenced.
	if (anotherTerm instanceof VariableTerm || ((anotherTerm instanceof IntegerTerm) || (anotherTerm instanceof DoubleTerm)))
	    return AFTER;
	if (! (anotherTerm instanceof SymbolTerm))
	    return BEFORE;
	if (this == anotherTerm)
	    return EQUAL;
	int x = name.compareTo(((SymbolTerm)anotherTerm).name());
	if (x != 0)
	    return x;
	int y = this.arity - ((SymbolTerm)anotherTerm).arity();
	if (y != 0)
	    return y;
	throw new InternalException("SymbolTerm is not unique");
    }
}
