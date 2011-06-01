package jp.ac.kobe_u.cs.prolog.lang;

/**
 * Superclass of any predicate's implementation class.
 * <p>
 * Concrete implementations must provide a definition of the Operation's
 * {@link Operation#exec(Prolog)} method. On a successful execution the
 * method must return {@link #cont}.
 * <p>
 * To be compatible with the {@code am2j} compiler implementations must
 * define a constructor matching the signature:
 * <pre>
 *   public ClassName(Term arg1, Term arg2, ..., Operation cont) {
 *     // save arg1, arg2
 *     this.cont = cont;
 *   }
 * </pre>
 */
public abstract class Predicate extends Operation {
  /**
   * Holds the continuation goal of this predicate.
   * <p>
   * In a Prolog program this is the next operation to perform if this operation
   * was executed successfully.
   */
  public Operation cont;

  public static abstract class P1 extends Predicate {
    protected Term arg1;
  }

  public static abstract class P2 extends Predicate {
    protected Term arg1;
    protected Term arg2;
  }

  public static abstract class P3 extends Predicate {
    protected Term arg1;
    protected Term arg2;
    protected Term arg3;
  }

  public static abstract class P4 extends Predicate {
    protected Term arg1;
    protected Term arg2;
    protected Term arg3;
    protected Term arg4;
  }
}
