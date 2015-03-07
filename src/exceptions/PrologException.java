package com.googlecode.prolog_cafe.exceptions;

import com.googlecode.prolog_cafe.lang.Term;

/**
 * The superclass of classes for Prolog exceptions.<br>
 *
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
 */
public abstract class PrologException extends RuntimeException {

    /** Constructs a new Prolog exception. */
    public PrologException() {}

    public PrologException(String s) {
      super(s);
    }

    /** Returns the message term of this object. */
    abstract public Term getMessageTerm();
}
