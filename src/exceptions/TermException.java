package com.googlecode.prolog_cafe.exceptions;

import com.googlecode.prolog_cafe.lang.Term;

/**
 * User-defined exception.<br>
 *
 * This <code>TermException</code> is used to implement
 * built-in predicate <code>throw/1</code>.
 *
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
 */
public class TermException extends PrologException {
    private static final long serialVersionUID = 1L;

    /** Message as term. */
    Term message;

    /** Constructs a new <code>TermException</code>. */
    public TermException(){}

    /** Constructs a new <code>TermException</code> with a given message term. */
    public TermException(Term _message){
	super(_message.toString());
	message = _message;
    }

    @Override
    public Term getMessageTerm() {
	return message;
    }
}
