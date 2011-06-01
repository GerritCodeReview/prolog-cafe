package jp.ac.kobe_u.cs.prolog.builtin;
import jp.ac.kobe_u.cs.prolog.lang.*;
/**
   <code>'$insert'/2</code><br>
   @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
   @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
   @version 1.1
*/
class PRED_$insert_2 extends Predicate {
    public Term arg1, arg2;

    public PRED_$insert_2(Term a1, Term a2, Operation cont) {
        arg1 = a1;
        arg2 = a2;
        this.cont = cont;
    }

    public String toString() {
        return "$insert(" + arg1 + "," + arg2 + ")";
    }

    public Operation exec(Prolog engine) {
        engine.setB0();
        Term a1, a2;
        a1 = arg1;
        a2 = arg2;
	int idx;

	a2 = a2.dereference();
	if (! a2.isVariable())
	    throw new IllegalTypeException(this, 2, "variable", a2);
	a1 = a1.dereference();
	idx = engine.internalDB.insert(a1);
	if (! a2.unify(new IntegerTerm(idx), engine.trail))
	    return engine.fail();
	return cont;
    }
}
