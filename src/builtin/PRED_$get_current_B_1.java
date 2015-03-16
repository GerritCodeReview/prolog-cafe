package com.googlecode.prolog_cafe.builtin;
import com.googlecode.prolog_cafe.lang.IntegerTerm;
import com.googlecode.prolog_cafe.lang.Operation;
import com.googlecode.prolog_cafe.lang.Predicate;
import com.googlecode.prolog_cafe.lang.Prolog;
import com.googlecode.prolog_cafe.lang.Term;
/**
 * <code>'$get_current_B'/1</code><br>
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
 */
class PRED_$get_current_B_1 extends Predicate.P1 {
    public PRED_$get_current_B_1(Term a1, Operation cont) {
	arg1 = a1;
	this.cont = cont;
    }

    @Override
    public Operation exec(Prolog engine) {
        engine.setB0();
	Term a1;
	a1 = arg1;
	if (! a1.unify(new IntegerTerm(engine.stack.top()), engine.trail)) 
	    return engine.fail();
	return cont;
    }
}
