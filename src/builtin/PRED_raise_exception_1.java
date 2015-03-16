package com.googlecode.prolog_cafe.builtin;
import com.googlecode.prolog_cafe.exceptions.PInstantiationException;
import com.googlecode.prolog_cafe.exceptions.TermException;
import com.googlecode.prolog_cafe.lang.Operation;
import com.googlecode.prolog_cafe.lang.Predicate;
import com.googlecode.prolog_cafe.lang.Prolog;
import com.googlecode.prolog_cafe.lang.Term;
import com.googlecode.prolog_cafe.lang.VariableTerm;
/**
 * <code>raise_exception/1</code><br>
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
 */
public class PRED_raise_exception_1 extends Predicate.P1 {
    public PRED_raise_exception_1(Term a1, Operation cont) {
	arg1 = a1;
	this.cont = cont;
    }

    @Override
    public Operation exec(Prolog engine) {
        engine.setB0();
	Term a1;
	a1 = arg1;

	a1 = a1.dereference();
	if (a1 instanceof VariableTerm)
	    throw new PInstantiationException(this, 1);
	throw new TermException(a1);
    }
}
