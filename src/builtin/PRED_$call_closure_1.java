package com.googlecode.prolog_cafe.builtin;
import com.googlecode.prolog_cafe.lang.ClosureTerm;
import com.googlecode.prolog_cafe.lang.Operation;
import com.googlecode.prolog_cafe.lang.Predicate;
import com.googlecode.prolog_cafe.lang.Prolog;
import com.googlecode.prolog_cafe.lang.Term;
/**
 * <code>'$call_closure'/1</code><br>
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
 */
class PRED_$call_closure_1 extends Predicate.P1 {
    public PRED_$call_closure_1(Term a1, Operation cont) {
	arg1 = a1;
	this.cont = cont;
    }

    @Override
    public Operation exec(Prolog engine) {
        engine.setB0();
	Term a1;
	Predicate code;

	// a1 must be closure
	a1 = arg1.dereference();

	if (! (a1 instanceof ClosureTerm))
	    return engine.fail();
	code = ((ClosureTerm) a1).getCode();
	code.cont = this.cont;
	return code;
    }
}
