package com.googlecode.prolog_cafe.exceptions;

import com.googlecode.prolog_cafe.lang.IntegerTerm;
import com.googlecode.prolog_cafe.lang.JavaObjectTerm;
import com.googlecode.prolog_cafe.lang.Operation;
import com.googlecode.prolog_cafe.lang.StructureTerm;
import com.googlecode.prolog_cafe.lang.SymbolTerm;
import com.googlecode.prolog_cafe.lang.Term;

/**
 * Evaluation error.<br>
 * There will be an evaluation error when the operands
 * of an evaluable functor are such that the operation
 * has an exceptional value.
 *
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
 */
public class EvaluationException extends BuiltinException {
    /** A functor symbol of <code>evaluation_error/3</code>. */
    public static final SymbolTerm EVALUATION_ERROR = SymbolTerm.intern("evaluation_error", 3);

    /* errorType ::= float_overflow | int_overflow | undefined | underflow | zero_devisor */
    /** Holds a string representation of error type. */
    public String errorType;

    /** Constructs a new <code>EvaluationException</code> with an error type. */
    public EvaluationException(String _errorType) {
	errorType    = _errorType;
    }

    /** Constructs a new <code>EvaluationException</code> with the given arguments. */
    public EvaluationException(Operation _goal, int _argNo, String _errorType) {
	this.goal    = _goal;
	this.argNo   = _argNo;
	errorType    = _errorType;
    }

    /** Returns a term representation of this <code>EvaluationException</code>:
     * <code>evaluation_error(goal,argNo,errorType)</code>.
     */
    @Override
    public Term getMessageTerm() {
	Term[] args = {
	    new JavaObjectTerm(goal), 
	    new IntegerTerm(argNo), 
	    SymbolTerm.create(errorType)};
	return new StructureTerm(EVALUATION_ERROR, args);
    }

    /** Returns a string representation of this <code>EvaluationException</code>. */
    @Override
    public String toString() {
	String s = "{EVALUATION ERROR: " + goal.toString();
	if (argNo > 0)
	    s += " - arg " + argNo;
	s += ", found " + errorType;
	s += "}";
	return s;
    }
}
