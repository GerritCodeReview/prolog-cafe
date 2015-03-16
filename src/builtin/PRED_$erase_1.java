package com.googlecode.prolog_cafe.builtin;
import com.googlecode.prolog_cafe.exceptions.IllegalTypeException;
import com.googlecode.prolog_cafe.lang.IntegerTerm;
import com.googlecode.prolog_cafe.lang.Operation;
import com.googlecode.prolog_cafe.lang.Predicate;
import com.googlecode.prolog_cafe.lang.Prolog;
import com.googlecode.prolog_cafe.lang.Term;
/**
 * <code>'$erase'/1</code><br>
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.1
 */
class PRED_$erase_1 extends Predicate.P1 {
    public PRED_$erase_1(Term a1, Operation cont) {
	arg1 = a1;
	this.cont = cont;
    }

    @Override
    public Operation exec(Prolog engine) {
        engine.setB0();
	Term a1 = arg1;
	int idx;

	a1 = a1.dereference();
	if (! (a1 instanceof IntegerTerm))
	    throw new IllegalTypeException(this, 1, "integer", a1);
	idx = ((IntegerTerm)a1).intValue();
	engine.internalDB.erase(idx);
	return cont;
    }
}
