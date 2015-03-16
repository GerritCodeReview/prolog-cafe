package com.googlecode.prolog_cafe.builtin;
import com.googlecode.prolog_cafe.lang.IntegerTerm;
import com.googlecode.prolog_cafe.lang.Operation;
import com.googlecode.prolog_cafe.lang.Predicate;
import com.googlecode.prolog_cafe.lang.Prolog;
import com.googlecode.prolog_cafe.lang.SymbolTerm;
import com.googlecode.prolog_cafe.lang.Term;
import com.googlecode.prolog_cafe.lang.Token;
/**
 * <code>'$atom_type0'/2</code><br>
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
 */
class PRED_$atom_type0_2 extends Predicate.P2 {
    public PRED_$atom_type0_2(Term a1, Term a2, Operation cont){ 
	arg1 = a1;
	arg2 = a2;
	this.cont = cont;
    }

    @Override
    public Operation exec(Prolog engine) {
        engine.setB0();
	Term a1, a2;
	int type;
	a1 = arg1;
	a2 = arg2;

	a1 = a1.dereference();
	if(! (a1 instanceof SymbolTerm))
	    return engine.fail();
	type = Token.getStringType(((SymbolTerm)a1).name());
	if(! a2.unify(new IntegerTerm(type), engine.trail)) 
	    return engine.fail();
	return cont;
    }
}
