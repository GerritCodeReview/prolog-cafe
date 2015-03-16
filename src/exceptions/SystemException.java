package com.googlecode.prolog_cafe.exceptions;

import com.googlecode.prolog_cafe.lang.StructureTerm;
import com.googlecode.prolog_cafe.lang.SymbolTerm;
import com.googlecode.prolog_cafe.lang.Term;

/**
 * System error.<br>
 *
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
 */
public class SystemException extends PrologException {
    private static final long serialVersionUID = 1L;

    /** A functor symbol of <code>system_error/1</code>. */
    public static final SymbolTerm SYSTEM_ERROR = SymbolTerm.intern("system_error", 1);

    /** Holds a message. */
    public String message;

    /** Constructs a new <code>SystemException</code> with a message. */
    public SystemException(String _message) {
	message = _message;
    }

    /** Returns a term representation of this <code>SystemException</code>:
     * <code>system_error(message)</code>.
     */
    @Override
    public Term getMessageTerm() {
	Term[] args = {SymbolTerm.create(message)};
	return new StructureTerm(SYSTEM_ERROR, args);
    }

    /** Returns a string representation of this <code>SystemException</code>. */
    @Override
    public String toString() {
	String s = "{SYSTEM ERROR: " + message;
	s += "}";
	return s;
    }
}
