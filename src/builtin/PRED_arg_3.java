package com.googlecode.prolog_cafe.builtin;
import com.googlecode.prolog_cafe.exceptions.IllegalDomainException;
import com.googlecode.prolog_cafe.exceptions.IllegalTypeException;
import com.googlecode.prolog_cafe.exceptions.PInstantiationException;
import  com.googlecode.prolog_cafe.lang.*;
/**
 * <code>arg/3</code><br>
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
 */
public class PRED_arg_3 extends Predicate.P3 {
    public PRED_arg_3(Term a1, Term a2, Term a3, Operation cont) {
	arg1 = a1;
	arg2 = a2;
	arg3 = a3;
	this.cont = cont;
    }

    public Operation exec(Prolog engine) {
        engine.setB0();
	Term a1, a2, a3;
	a1 = arg1;
	a2 = arg2;
	a3 = arg3;

	Term[] args;
	int arity, argNo;

	a1 = a1.dereference();
	if(a1 instanceof VariableTerm)
	    throw new PInstantiationException(this, 1);
	else if(! (a1 instanceof IntegerTerm))
	    throw new IllegalTypeException(this, 1, "integer", a1);
	a2 = a2.dereference();
	if (a2 instanceof ListTerm) {
	    args = new Term[2];
	    args[0] = ((ListTerm)a2).car();
	    args[1] = ((ListTerm)a2).cdr();
	    arity = 2;
	} else if (a2 instanceof StructureTerm) {
	    args =  ((StructureTerm)a2).args();
	    arity = ((StructureTerm)a2).arity();
	} else if (a2 instanceof VariableTerm) {
	    throw new PInstantiationException(this, 2);
	} else {
	    throw new IllegalTypeException(this, 2, "compound", a2);
	}
	argNo = ((IntegerTerm)a1).intValue();
	if (argNo < 0)
	    throw new IllegalDomainException(this, 1, "not_less_than_zero", a1);
	if (argNo > arity || argNo < 1)
	    return engine.fail();
	if (! a3.unify(args[argNo-1], engine.trail)) 
	    return engine.fail();
	return cont;
    }
}
