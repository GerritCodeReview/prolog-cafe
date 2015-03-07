package com.googlecode.prolog_cafe.builtin;
import com.googlecode.prolog_cafe.exceptions.IllegalTypeException;
import com.googlecode.prolog_cafe.exceptions.PInstantiationException;
import com.googlecode.prolog_cafe.exceptions.RepresentationException;
import com.googlecode.prolog_cafe.lang.*;
/**
   <code>'$get_instances'/2</code><br>
   @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
   @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
   @version 1.1
*/
class PRED_$get_instances_2 extends Predicate.P2 {
    private static final SymbolTerm COMMA = SymbolTerm.intern(",", 2);

    public PRED_$get_instances_2(Term a1, Term a2, Operation cont) {
        arg1 = a1;
        arg2 = a2;
        this.cont = cont;
    }

    public Operation exec(Prolog engine) {
        engine.setB0();
        Term a1, a2;
        a1 = arg1;
        a2 = arg2;
	int idx;

	a1 = a1.dereference();
	if (a1.isNil())
	    return engine.fail();
	if (! a1.isList())
	    throw new IllegalTypeException(this, 1, "list", a1);
	Term x = Prolog.Nil;
	Term tmp = a1;
	while(! tmp.isNil()) {
	    if (! tmp.isList())
		throw new IllegalTypeException(this, 1, "list", a1);
	    Term car = ((ListTerm)tmp).car().dereference();
	    if (car.isVariable())
		throw new PInstantiationException(this, 1);
	    if (! car.isInteger()) 
		throw new RepresentationException(this, 1, "integer");
	    // car is an integer
	    int i = ((IntegerTerm)car).intValue();
	    Term e = engine.internalDB.get(i);
	    if (e != null) {
		x = new ListTerm(new StructureTerm(COMMA, e, car), x);
	    }
	    tmp = ((ListTerm)tmp).cdr().dereference();
	}
	if (! a2.unify(x, engine.trail))
	    return engine.fail();
	return cont;
    }
}
