package com.googlecode.prolog_cafe.lang;

import com.googlecode.prolog_cafe.exceptions.EvaluationException;
import com.googlecode.prolog_cafe.exceptions.IllegalTypeException;

/**
 * Integer.<br>
 * The class <code>IntegerTerm</code> wraps a value of primitive type 
 * <code>int</code>. 
 * <pre>
 *   Term t = new IntegerTerm(100);
 *   int i = ((IntegerTerm)t).intValue();
 * </pre>
 *
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
 */
public class IntegerTerm extends NumberTerm {
    /** Holds an <code>int</code> value that this <code>IntegerTerm</code> represents. */
    protected int val;

    /** Constructs a new Prolog integer that represents the specified <code>int</code> value. */
    public IntegerTerm(int i) {	val = i; }

    /**
     * Returns the value of <code>val</code>.
     * @see #val
     */
    public int value() { return val; }

    @Override
    public int type() {
      return TYPE_INTEGER;
    }

    /* Term */
    @Override
    public boolean unify(Term t, Trail trail) {
	if (t instanceof VariableTerm)
	    return ((VariableTerm)t).unify(this, trail);
	if (! (t instanceof IntegerTerm))
	    return false;
	else 
	    return this.val == ((IntegerTerm)t).value();
    }

    /** 
     * @return the <code>boolean</code> whose value is
     * <code>convertible(Integer.class, type)</code>.
     * @see Term#convertible(Class, Class)
     */
    @Override
    public boolean convertible(Class<?> type) { return convertible(Integer.class, type); }

    @Override
    public String name() { return ""; }

    /** 
     * Returns a <code>java.lang.Integer</code> corresponds to this <code>IntegerTerm</code>
     * according to <em>Prolog Cafe interoperability with Java</em>.
     * @return a <code>java.lang.Integer</code> object equivalent to
     * this <code>IntegerTerm</code>.
     */
    @Override
    public Object toJava() { return Integer.valueOf(val); }

    /* Object */
    /** Returns a string representation of this <code>IntegerTerm</code>. */
    @Override
    public String toString() { return Integer.toString(this.val); }

    /**
     * Checks <em>term equality</em> of two terms.
     * The result is <code>true</code> if and only if the argument is an instance of
     * <code>IntegerTerm</code> and has the same <code>int</code> value as this object.
     * @param obj the object to compare with. This must be dereferenced.
     * @return <code>true</code> if the given object represents a Prolog integer 
     * equivalent to this <code>IntegerTerm</code>, false otherwise.
     * @see #compareTo
    */
    @Override
    public boolean equals(Object obj) {
	if (! (obj instanceof IntegerTerm))
	    return false;
	return this.val == ((IntegerTerm)obj).value();
    }

    @Override
    public int hashCode() { return this.val; }

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
    @Override
    public int compareTo(Term anotherTerm) { // anotherTerm must be dereferenced.
	if (anotherTerm instanceof VariableTerm || anotherTerm instanceof DoubleTerm)
	    return AFTER;
	if (! (anotherTerm instanceof IntegerTerm))
	    return BEFORE;
	int v = ((IntegerTerm)anotherTerm).value();
	if (this.val == v)
	    return EQUAL;
	if (this.val > v)
	    return AFTER;
	return BEFORE;
    }

    /* NumberTerm */
    @Override
    public int intValue() { return this.val; }

    @Override
    public long longValue() { return (long)(this.val); }

    @Override
    public double doubleValue() { return (double)(this.val); }

    @Override
    public int arithCompareTo(NumberTerm t) {
	if (t instanceof DoubleTerm)
	    return - (t.arithCompareTo(this));
	int v = t.intValue();
	if (this.val == v)
	    return EQUAL;
	if (this.val > v)
	    return AFTER;
	return BEFORE;
    }

    @Override
    public NumberTerm abs() { return new IntegerTerm(Math.abs(this.val)); }

    @Override
    public NumberTerm acos() { return new DoubleTerm(Math.acos(this.doubleValue())); }

    @Override
    public NumberTerm add(NumberTerm t) {
	if (t instanceof DoubleTerm)
	    return t.add(this);
	return new IntegerTerm(this.val + t.intValue());
    }

    /** 
     * @exception IllegalTypeException if the given argument
     * <code>NumberTerm</code> is a floating point number.
     */
    @Override
    public NumberTerm and(NumberTerm t) {
	if (t instanceof DoubleTerm)
	    throw new IllegalTypeException("integer", t);
	return new IntegerTerm(this.val & t.intValue());
    }

    @Override
    public NumberTerm asin() { return new DoubleTerm(Math.asin(this.doubleValue())); }

    @Override
    public NumberTerm atan() { return new DoubleTerm(Math.atan(this.doubleValue())); }

    @Override
    public NumberTerm ceil() { return this; }

    @Override
    public NumberTerm cos() { return new DoubleTerm(Math.cos(this.doubleValue())); }

    /** 
     * @exception EvaluationException if the given argument
     * <code>NumberTerm</code> represents <coe>0</code>.
     */
    @Override
    public NumberTerm divide(NumberTerm t) { 
	if (t.doubleValue() == 0)
	    throw new EvaluationException("zero_divisor");
	return new DoubleTerm(this.doubleValue() / t.doubleValue()); 
    }

    @Override
    public NumberTerm exp() { return new DoubleTerm(Math.exp(this.doubleValue())); }

    @Override
    public NumberTerm floatIntPart() { throw new IllegalTypeException("float", this); }

    @Override
    public NumberTerm floatFractPart() { throw new IllegalTypeException("float", this); }

    @Override
    public NumberTerm floor() { return this; }

    /** 
     * @exception IllegalTypeException if the given argument
     * <code>NumberTerm</code> is a floating point number.
     * @exception EvaluationException if the given argument
     * <code>NumberTerm</code> represents <coe>0</code>.
     */
    @Override
    public NumberTerm intDivide(NumberTerm t) {
	if (t instanceof DoubleTerm)
	    throw new IllegalTypeException("integer", t);
	if (t.intValue() == 0)
	    throw new EvaluationException("zero_divisor");
	return new IntegerTerm((int) (this.val / t.intValue()));
    }

    /** 
     * @exception EvaluationException if this object represents <coe>0</code>.
     */
    @Override
    public NumberTerm log() { 
	if (this.val == 0)
	    throw new EvaluationException("undefined");
	return new DoubleTerm(Math.log(this.doubleValue())); 
    }

    @Override
    public NumberTerm max(NumberTerm t) {
	if (t instanceof DoubleTerm) 
	    return t.max(this);
	return new IntegerTerm(Math.max(this.val, t.intValue()));
    }

    @Override
    public NumberTerm min(NumberTerm t) {
	if (t instanceof DoubleTerm) 
	    return t.min(this);
	return new IntegerTerm(Math.min(this.val, t.intValue()));
    }

    /** 
     * @exception IllegalTypeException if the given argument
     * <code>NumberTerm</code> is a floating point number.
     * @exception EvaluationException if the given argument
     * <code>NumberTerm</code> represents <coe>0</code>.
     */
    @Override
    public NumberTerm mod(NumberTerm t) {
	if (t instanceof DoubleTerm)
	    throw new IllegalTypeException("integer", t);
	if (t.intValue() == 0)
	    throw new EvaluationException("zero_divisor");
	return new IntegerTerm(this.val % t.intValue());
    }

    @Override
    public NumberTerm multiply(NumberTerm t) {
	if (t instanceof DoubleTerm)
	    return t.multiply(this);
	return new IntegerTerm(this.val * t.intValue());
    }

    @Override
    public NumberTerm negate() { return new IntegerTerm(- this.val); }

    @Override
    public NumberTerm not() { return new IntegerTerm(~ this.val); }

    /** 
     * @exception IllegalTypeException if the given argument
     * <code>NumberTerm</code> is a floating point number.
     */
    @Override
    public NumberTerm or(NumberTerm t) {
	if (t instanceof DoubleTerm)
	    throw new IllegalTypeException("integer", t);
	return new IntegerTerm(this.val | t.intValue());
    }

    @Override
    public NumberTerm pow(NumberTerm t) { return new DoubleTerm(Math.pow(this.doubleValue(), t.doubleValue())); }

    @Override
    public NumberTerm rint() { return new DoubleTerm(this.doubleValue()); }

    @Override
    public NumberTerm round() { return this; }

    /** 
     * @exception IllegalTypeException if the given argument
     * <code>NumberTerm</code> is a floating point number.
     */
    @Override
    public NumberTerm shiftLeft(NumberTerm t) {
	if (t instanceof DoubleTerm)
	    throw new IllegalTypeException("integer", t);
	return new IntegerTerm(this.val << t.intValue());
    }

    /** 
     * @exception IllegalTypeException if the given argument
     * <code>NumberTerm</code> is a floating point number.
     */
    @Override
    public NumberTerm shiftRight(NumberTerm t) {
	if (t instanceof DoubleTerm)
	    throw new IllegalTypeException("integer", t);
	return new IntegerTerm(this.val >> t.intValue());
    }

    @Override
    public NumberTerm signum() {return new IntegerTerm((int) Math.signum((double) this.val)); }

    @Override
    public NumberTerm sin() { return new DoubleTerm(Math.sin(this.doubleValue())); }

    /** 
     * @exception EvaluationException if this object represents
     * an integer less than <coe>0</code>.
     */
    @Override
    public NumberTerm sqrt() { 
	if (this.val < 0)
	    throw new EvaluationException("undefined");
	return new DoubleTerm(Math.sqrt(this.doubleValue())); 
    }

    @Override
    public NumberTerm subtract(NumberTerm t) {
	if (t instanceof DoubleTerm)
	    return new DoubleTerm(this.doubleValue() - t.doubleValue());
	return new IntegerTerm(this.val - t.intValue());
    }

    @Override
    public NumberTerm tan() { return new DoubleTerm(Math.tan(this.doubleValue())); }

    @Override
    public NumberTerm toDegrees() { return new DoubleTerm(Math.toDegrees(this.doubleValue())); }

    @Override
    public NumberTerm toFloat() { return new DoubleTerm((double) this.val); }

    @Override
    public NumberTerm toRadians() { return new DoubleTerm(Math.toRadians(this.doubleValue())); }

    @Override
    public NumberTerm truncate() { return this; }

    /** 
     * @exception IllegalTypeException if the given argument
     * <code>NumberTerm</code> is a floating point number.
     */
    @Override
    public NumberTerm xor(NumberTerm t) {
	if (t instanceof DoubleTerm)
	    throw new IllegalTypeException("integer", t);
	return new IntegerTerm(this.val ^ t.intValue());
    }
}
