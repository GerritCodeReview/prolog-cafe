package com.googlecode.prolog_cafe.exceptions;

import com.googlecode.prolog_cafe.lang.IntegerTerm;
import com.googlecode.prolog_cafe.lang.JavaObjectTerm;
import com.googlecode.prolog_cafe.lang.Operation;
import com.googlecode.prolog_cafe.lang.StructureTerm;
import com.googlecode.prolog_cafe.lang.SymbolTerm;
import com.googlecode.prolog_cafe.lang.Term;

/**
 * Type error.<br>
 * There will be a type error when the type of an argument or 
 * one of its components is incorrect, but not a variable.
 *
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
 */
public class IllegalTypeException extends BuiltinException {
    private static final long serialVersionUID = 1L;

    /** A functor symbol of <code>type_error/4</code>. */
    public static final SymbolTerm TYPE_ERROR = SymbolTerm.intern("type_error", 4);

    /*
      type ::= atom | atomic | byte | callable | character | compound | evaluable |
               in_byte | in_character | integer | list | number |
	       predicate_indicator | variable |
	       flaot | java
    */
    /** Holds a string representation of valid type. */
    public String type;

    /** Holds the argument or one of its components which caused the error. */
    public Term culprit;

    /** Constructs a new <code>IllegalTypeException</code> 
     * with a valid type and its culprit. */
    public IllegalTypeException(String _type, Term _culprit) {
	type    = _type;
	culprit = _culprit;
    }

    /** Constructs a new <code>IllegalTypeException</code> 
     * with the given arguments. */
    public IllegalTypeException(Operation _goal, int _argNo, String _type, Term _culprit) {
	this.goal    = _goal;
	this.argNo   = _argNo;
	type    = _type;
	culprit = _culprit;
    }

    /** Returns a term representation of this <code>IllegalTypeException</code>:
     * <code>type_error(goal,argNo,type,culprit)</code>.
     */
    @Override
    public Term getMessageTerm() {
	Term[] args = {
	    new JavaObjectTerm(goal), 
	    new IntegerTerm(argNo), 
	    SymbolTerm.create(type),
	    culprit};
	return new StructureTerm(TYPE_ERROR, args);
    }

    /** Returns a string representation of this <code>IllegalTypeException</code>. */
    @Override
    public String toString() {
	String s = "{TYPE ERROR: " + goal.toString();
	if (argNo > 0)
	    s += " - arg " + argNo;
	s += ": expected " + type;
	s += ", found " + culprit.toString();
	s += "}";
	return s;
    }
}
