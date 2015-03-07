package com.googlecode.prolog_cafe.exceptions;

import com.googlecode.prolog_cafe.lang.StructureTerm;
import com.googlecode.prolog_cafe.lang.SymbolTerm;
import com.googlecode.prolog_cafe.lang.Term;

/**
 * Internal error.<br>
 *
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
 */
public class InternalException extends PrologException {
    /** A functor symbol of <code>internal_error/1</code>. */
    public static final SymbolTerm INTERNAL_ERROR = SymbolTerm.intern("internal_error", 1);

    /** Holds a message. */
    public String message;

    /** Constructs a new <code>InternalException</code> with a message. */
    public InternalException(String _message) {
	message = _message;
    }

    /** Returns a term representation of this <code>InternalException</code>:
     * <code>internal_error(message)</code>.
     */
    public Term getMessageTerm() {
	Term[] args = {SymbolTerm.create(message)};
	return new StructureTerm(INTERNAL_ERROR, args);
    }

    /** Returns a string representation of this <code>InternalException</code>. */
    public String toString() {
	String s = "{INTERNAL ERROR: " + message;
	s += "}";
	return s;
    }
}
