package jp.ac.kobe_u.cs.prolog.builtin;
import  jp.ac.kobe_u.cs.prolog.lang.*;
/**
 * <code>'$set_exception'/1</code><br>
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
 */
class PRED_$set_exception_1 extends Predicate.P1 {
    public PRED_$set_exception_1(Term a1, Operation cont) {
	arg1 = a1;
	this.cont = cont;
    }

    public String toString() { return "$set_exception(" + arg1 + ")"; }

    public Operation exec(Prolog engine) {
        engine.setB0();
	Term a1;
	a1 = arg1;

	a1 = a1.dereference();
	engine.setException(a1);
	return cont;
    }
}
