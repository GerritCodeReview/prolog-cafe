package jp.ac.kobe_u.cs.prolog.builtin;
import  jp.ac.kobe_u.cs.prolog.lang.*;
/**
 * <code>'$end_sync'/1</code>
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
 */
class PRED_$end_sync_1 extends Predicate.P1 {
    public PRED_$end_sync_1(Term a1, Operation cont) {
	arg1 = a1;
	this.cont = cont;
    }

    public Operation exec(Prolog engine) {
        engine.setB0();
	Term a1;
	a1 = arg1;

	a1 = a1.dereference();
	if (! a1.isJavaObject())
	    throw new IllegalTypeException(this, 1, "java", a1);
	Object obj = ((JavaObjectTerm)a1).object();
	if (! (obj instanceof PRED_$begin_sync_2))
	    throw new SystemException("a1 must be an object of PRED_$begin_sync_2: " + this);
	PRED_$begin_sync_2 p = ((PRED_$begin_sync_2) obj);
	p.outOfScope = true;
	engine.trail.push(new OutOfScope(p));
	return cont;
    }
}
