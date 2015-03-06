package com.googlecode.prolog_cafe.builtin;
import com.googlecode.prolog_cafe.lang.*;
/**
   <code>'$set_prolog_impl_flag'/2</code><br>
   @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
   @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
   @version 1.0
*/
class PRED_$set_prolog_impl_flag_2 extends Predicate.P2 {
    private static final SymbolTerm CHAR_CONVERSION   = SymbolTerm.intern("char_conversion");
    private static final SymbolTerm DEBUG             = SymbolTerm.intern("debug");
    private static final SymbolTerm UNKNOWN           = SymbolTerm.intern("unknown");
    private static final SymbolTerm DOUBLE_QUOTES     = SymbolTerm.intern("double_quotes");

    public PRED_$set_prolog_impl_flag_2(Term a1, Term a2, Operation cont) {
        arg1 = a1;
        arg2 = a2;
        this.cont = cont;
    }

    public Operation exec(Prolog engine) {
        engine.setB0();
        Term a1, a2;
        a1 = arg1;
        a2 = arg2;
	a1 = a1.dereference();
	a2 = a2.dereference();

	if (a1.equals(CHAR_CONVERSION)) {
	    if (! a2.isSymbol())
		return engine.fail();
	    engine.setCharConversion(((SymbolTerm)a2).name());
	} else if (a1.equals(DEBUG)) {
	    if (! a2.isSymbol())
		return engine.fail();
	    engine.setDebug(((SymbolTerm)a2).name());
	} else if (a1.equals(UNKNOWN)) {
	    if (! a2.isSymbol())
		return engine.fail();
	    engine.setUnknown(((SymbolTerm)a2).name());
	} else if (a1.equals(DOUBLE_QUOTES)) {
	    if (! a2.isSymbol())
		return engine.fail();
	    engine.setDoubleQuotes(((SymbolTerm)a2).name());
	} else {
	    return engine.fail();
	}
        return cont;
    }
}
