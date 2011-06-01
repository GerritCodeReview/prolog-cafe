package jp.ac.kobe_u.cs.prolog.builtin;
import  jp.ac.kobe_u.cs.prolog.lang.*;
/**
 * <code>'$erase'/1</code><br>
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.1
 */
class PRED_$erase_1 extends Predicate {
     Term arg1;

    public PRED_$erase_1(Term a1, Operation cont) {
	arg1 = a1;
	this.cont = cont;
    }

    public String toString() { return "$erase(" + arg1 + ")"; }

    public Operation exec(Prolog engine) {
        engine.setB0();
	Term a1 = arg1;
	int idx;

	a1 = a1.dereference();
	if (! a1.isInteger())
	    throw new IllegalTypeException(this, 1, "integer", a1);
	idx = ((IntegerTerm)a1).intValue();
	engine.internalDB.erase(idx);
	return cont;
    }
}
