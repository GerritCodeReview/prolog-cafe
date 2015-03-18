package com.googlecode.prolog_cafe.lang;
/**
 * The superclass of classes for term structures.
 * The subclasses of <code>Term</code> must override
 * the <code>unify</code> method.
 *
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
 */
public abstract class Term implements Comparable<Term> {

    /** Holds an integer value <code>0</code>. */
    public static final int EQUAL  =  0;
    /** Holds an integer value <code>1</code>. */
    public static final int AFTER  =  1;
    /** Holds an integer value <code>-1</code>. */
    public static final int BEFORE = -1;

    public static final int TYPE_VARIABLE = 0;
    public static final int TYPE_INTEGER = 1;
    public static final int TYPE_DOUBLE = 2;
    public static final int TYPE_SYMBOL = 3;
    public static final int TYPE_STRUCTURE = 4;
    public static final int TYPE_LIST = 5;
    public static final int TYPE_CLOSURE = 6;
    public static final int TYPE_JAVA_OBJECT = 7;

    /** One of the {@code TYPE_*} constants from {@code Term}. */
    public abstract int type();

    /**
     * Checks whether the argument term is unified with this one.
     * @param t the term to be unified with.
     * @param trail Trail Stack.
     * @return <code>true</code> if succeeds, otherwise <code>false</code>.
     */
    abstract public boolean unify(Term t, Trail trail);

    /** @return the name of this Term, if {@link #isStructure()}. */
    public abstract String name();

    /** @return the arity of this Term, if {@link #isStructure()}. */
	public int arity() { return 0; }

    /** @return get the nth argument of {@link #isStructure()} or {@link #isList()}. */
    public Term arg(int nth) { throw new ArrayIndexOutOfBoundsException(nth); }

    /**
     * Check whether this object is convertible with the given Java class type.
     * @param type the Java class type to compare with.
     * @return <code>true</code> if this is convertible with
     * <code>type</code>. Otherwise <code>false</code>.
     * @see #convertible(Class, Class)
     */
    public boolean convertible(Class<?> type) { return convertible(getClass(), type); }

    /**
     * Returns a copy of this object.
     * @param engine the engine.
     *
     */
    protected Term copy(Prolog engine) { return this; }

    /** Returns the dereference value of this term. */
    public Term    dereference() { return this; }

    /**
     * Check whether this term is a ground term.
     * @return <code>true</code> if ground, otherwise <code>false</code>.
     */
    public boolean isGround() { return true; }

    /**
     * Returns a Java object that corresponds to this term
     * if defined in <em>Prolog Cafe interoperability with Java</em>.
     * Otherwise, returns <code>this</code>.
     * @return a Java object if defined in <em>Prolog Cafe interoperability with Java</em>,
     * otherwise <code>this</code>.
     */
    public Object  toJava() {
	return this;
    }

    /** Returns a quoted string representation of this term. */
    public String  toQuotedString() { return this.toString(); }

    /**
     * Check whether there is a widening conversion from <code>from</code> to <code>to</code>.
     */
    protected static boolean convertible(Class<?> from, Class<?> to) {
	if (from == null)
	    return ! to.isPrimitive();
	if (to.isAssignableFrom(from)) {
	    return true;
	} else if (to.isPrimitive()) {
	    if (from.equals(Boolean.class)) {
		return to.equals(Boolean.TYPE);
	    } else if (from.equals(Byte.class)) {
		return to.equals(Byte.TYPE)
		    || to.equals(Short.TYPE)
		    || to.equals(Integer.TYPE)
		    || to.equals(Long.TYPE)
		    || to.equals(Float.TYPE)
		    || to.equals(Double.TYPE);
	    } else if (from.equals(Short.class)) {
		return to.equals(Short.TYPE)
		    || to.equals(Integer.TYPE)
		    || to.equals(Long.TYPE)
		    || to.equals(Float.TYPE)
		    || to.equals(Double.TYPE);
	    } else if (from.equals(Character.class)) {
		return to.equals(Character.TYPE)
		    || to.equals(Integer.TYPE)
		    || to.equals(Long.TYPE)
		    || to.equals(Float.TYPE)
		    || to.equals(Double.TYPE);
	    } else if (from.equals(Integer.class)) {
		return to.equals(Integer.TYPE)
		    || to.equals(Long.TYPE)
		    || to.equals(Float.TYPE)
		    || to.equals(Double.TYPE);
	    } else if (from.equals(Long.class)) {
		return to.equals(Long.TYPE)
		    || to.equals(Float.TYPE)
		    || to.equals(Double.TYPE);
	    } else if (from.equals(Float.class)) {
		return to.equals(Float.TYPE)
		    || to.equals(Double.TYPE);
	    } else if (from.equals(Double.class)) {
		return to.equals(Double.TYPE);
	    }
	}
	return false;
    }

    /** Checks whether a given object is an instance of Prolog term. */
    public static boolean instanceOfTerm(Object obj) {
	return obj instanceof VariableTerm ||
	    obj instanceof IntegerTerm ||
	    obj instanceof DoubleTerm ||
	    obj instanceof SymbolTerm ||
	    obj instanceof ListTerm ||
	    obj instanceof StructureTerm ||
	    obj instanceof JavaObjectTerm ||
	    obj instanceof ClosureTerm;
    }
}
