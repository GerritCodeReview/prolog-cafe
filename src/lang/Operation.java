package com.googlecode.prolog_cafe.lang;

import com.googlecode.prolog_cafe.exceptions.PrologException;

/**
 * Superclass any single step operation in the Prolog interpreter.
 * <p>
 * Most implementations should sublass {@link Predicate} instead to gain the
 * common {@link Predicate#cont} field to store the next step of the program.
 *
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
 */
public abstract class Operation {
  /**
   * Executes this operation and returns a continuation goal.
   *
   * @param engine current Prolog engine
   * @exception PrologException if a Prolog exception is raised.
   * @see Prolog
   */
  public abstract Operation exec(Prolog engine) throws PrologException;
}
