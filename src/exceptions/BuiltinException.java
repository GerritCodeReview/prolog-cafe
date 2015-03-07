package com.googlecode.prolog_cafe.exceptions;

import com.googlecode.prolog_cafe.lang.Operation;
import com.googlecode.prolog_cafe.lang.Term;

/**
 * Builtin exception.<br>
 *
 *
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
 */
public class BuiltinException extends PrologException {
    Term message;

    /** Holds the goal in which this exception occurs. */
    public Operation goal = null;

    /** Holds the arity of goal in which this exception occurs. */
    public int argNo = 0;

    /** Constructs a new <code>BuiltinException</code>. */
    public BuiltinException(){}

    /** Constructs a new <code>BuiltinException</code> with a given message term. */
    public BuiltinException(Term _message){
	message = _message;
    }

    public Term getMessageTerm() {
	return message;
    }
}
