package com.googlecode.prolog_cafe.builtin;
import com.googlecode.prolog_cafe.exceptions.IllegalTypeException;
import com.googlecode.prolog_cafe.exceptions.SystemException;
import  com.googlecode.prolog_cafe.lang.*;
/**
 * <code>'$end_exception'/1<code><br>
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
 */
class PRED_$end_exception_1 extends Predicate.P1 {
    public PRED_$end_exception_1(Term a1, Operation cont) {
	arg1 = a1;
	this.cont = cont;
    }

    public Operation exec(Prolog engine) {
        engine.setB0();
	Term a1;
	a1 = arg1;

	a1 = a1.dereference();
	if (! (a1 instanceof JavaObjectTerm))
	    throw new IllegalTypeException(this, 1, "java", a1);
	Object obj = ((JavaObjectTerm)a1).object();
	if (! (obj instanceof PRED_$begin_exception_1))
	    throw new SystemException("a1 must be an object of PRED_$begin_exception_1: " + this.toString());
	PRED_$begin_exception_1 p = ((PRED_$begin_exception_1) obj);
	p.outOfScope = true;
	engine.trail.push(new OutOfScope(p));
	return cont;
    }
}
