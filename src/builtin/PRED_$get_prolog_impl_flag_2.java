package com.googlecode.prolog_cafe.builtin;
import com.googlecode.prolog_cafe.lang.*;
/**
   <code>'$get_prolog_impl_flag'/2</code><br>
   @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
   @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
   @version 1.0
*/
class PRED_$get_prolog_impl_flag_2 extends Predicate.P2 {
    private static final SymbolTerm DEBUG = SymbolTerm.intern("debug");
    private static final SymbolTerm MAX_ARITY = SymbolTerm.intern("max_arity");

    public PRED_$get_prolog_impl_flag_2(Term a1, Term a2, Operation cont) {
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
	a2 = a2.dereference();

	if (a1.equals(DEBUG)) {
	    if (! a2.unify(SymbolTerm.intern(engine.getDebug()), engine.trail))
		return engine.fail();
	} else if (a1.equals(MAX_ARITY)) {
	    if (! a2.unify(new IntegerTerm(Prolog.MAX_ARITY), engine.trail))
		return engine.fail();
	} else {
	    return engine.fail();
	}
        return cont;
    }
}
