package com.googlecode.prolog_cafe.builtin;
import  com.googlecode.prolog_cafe.lang.*;
/**
 * <code>current_engine/1</code>
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
 */
public class PRED_current_engine_1 extends Predicate.P1 {
    public PRED_current_engine_1(Term a1, Operation cont) {
	arg1 = a1;
	this.cont = cont;
    }

    @Override
    public Operation exec(Prolog engine) {
        engine.setB0();
	Term a1;
	a1 = arg1;

	a1 = a1.dereference();
	if (! a1.unify(new JavaObjectTerm(engine), engine.trail))
	    return engine.fail();
	return cont;
    }
}
