package jp.ac.kobe_u.cs.prolog.builtin;
import  jp.ac.kobe_u.cs.prolog.lang.*;
/**
 * <code>raise_exception/1</code><br>
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
 */
public class PRED_raise_exception_1 extends Predicate.P1 {
    public PRED_raise_exception_1(Term a1, Operation cont) {
	arg1 = a1;
	this.cont = cont;
    }

    public Operation exec(Prolog engine) {
        engine.setB0();
	Term a1;
	a1 = arg1;

	a1 = a1.dereference();
	if (a1.isVariable())
	    throw new PInstantiationException(this, 1);
	throw new TermException(a1);
    }
}
