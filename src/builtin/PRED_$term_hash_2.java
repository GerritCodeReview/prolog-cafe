package com.googlecode.prolog_cafe.builtin;
import  com.googlecode.prolog_cafe.lang.*;
/**
 * <code>'$term_hash'/2</code><br>
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.1
 */
public class PRED_$term_hash_2 extends Predicate.P2 {
    public PRED_$term_hash_2(Term a1, Term a2, Operation cont) {
	arg1 = a1;
	arg2 = a2;
	this.cont = cont;
    }

    @Override
    public Operation exec(Prolog engine) {
        engine.setB0();
	Term a1, a2;
        a1 = arg1;
        a2 = arg2;

	a1 = a1.dereference(); 
	if (! a2.unify(new IntegerTerm(a1.hashCode()), engine.trail))
	    return engine.fail();
	return cont;
    }
}
