package com.googlecode.prolog_cafe.builtin;
import  com.googlecode.prolog_cafe.lang.*;
/**
 * <code>current_input/1</code><br>
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
 */
public class PRED_current_input_1 extends Predicate.P1 {
    public PRED_current_input_1(Term a1, Operation cont) {
	arg1 = a1;
	this.cont = cont;
    }

    public Operation exec(Prolog engine) {
        engine.requireFeature(Prolog.Feature.IO, this, arg1);
        engine.setB0();
	Term a1;
	a1 = arg1;
	a1 = a1.dereference();
	if (a1.isVariable()) {
	    ((VariableTerm)a1).bind(new JavaObjectTerm(engine.getCurrentInput()), engine.trail);
	} else if (a1.isJavaObject()) {
	    if (! a1.unify(new JavaObjectTerm(engine.getCurrentInput()), engine.trail)) 
		return engine.fail();
	} else {
	    throw new IllegalDomainException(this,1,"stream",a1);
	}
	return cont;
    }
}
