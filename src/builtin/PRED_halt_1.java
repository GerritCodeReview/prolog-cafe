package com.googlecode.prolog_cafe.builtin;
import com.googlecode.prolog_cafe.exceptions.IllegalTypeException;
import com.googlecode.prolog_cafe.exceptions.PInstantiationException;
import com.googlecode.prolog_cafe.lang.*;
/**
 * <code>halt/1</code><br>
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
*/
public class PRED_halt_1 extends Predicate.P1 {
    public PRED_halt_1(Term a1, Operation cont) {
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
	if (! (a1 instanceof IntegerTerm))
	    throw new IllegalTypeException(this, 1, "integer", a1);
	engine.halt = 1 + ((IntegerTerm)a1).intValue();
        return cont;
    }
}
