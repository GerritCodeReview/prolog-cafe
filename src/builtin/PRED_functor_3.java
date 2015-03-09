package com.googlecode.prolog_cafe.builtin;
import com.googlecode.prolog_cafe.exceptions.IllegalDomainException;
import com.googlecode.prolog_cafe.exceptions.IllegalTypeException;
import com.googlecode.prolog_cafe.exceptions.PInstantiationException;
import  com.googlecode.prolog_cafe.lang.*;
/**
 * <code>functor/3</code><br>
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
 */
public class PRED_functor_3 extends Predicate.P3 {
    private static final SymbolTerm SYM_DOT = SymbolTerm.intern(".");

    public PRED_functor_3(Term a1, Term a2, Term a3, Operation cont) {
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

	// functor(?X,+Y,+Z)
	a1 = a1.dereference();
	if (a1.isVariable()) {
	    a2 = a2.dereference();
	    if (a2.isVariable())
		throw new PInstantiationException(this, 2);
	    if (!a2.isSymbol() &&  !a2.isNumber() && !a2.isJavaObject() && !a2.isClosure())
		throw new IllegalTypeException(this, 2, "atomic", a2);
	    a3 = a3.dereference();
	    if (a3.isVariable())
		throw new PInstantiationException(this, 3);
	    if (! a3.isInteger())
		throw new IllegalTypeException(this, 3, "integer", a3);
	    int n = ((IntegerTerm)a3).intValue();
	    if (n < 0)
		throw new IllegalDomainException(this, 3, "not_less_than_zero", a3);
	    if (n == 0) {
		if(! a1.unify(a2, engine.trail))
		    return engine.fail();
		return cont;
	    }
	    if (! a2.isSymbol())
		throw new IllegalTypeException(this, 2, "atom", a2);
	    if (n == 2  &&  a2.equals(SYM_DOT)) {
		Term t = new ListTerm(new VariableTerm(engine), new VariableTerm(engine));
		if(! a1.unify(t, engine.trail))
		    return engine.fail();
		return cont;
	    }
	    Term[] args = new Term[n];
	    for(int i=0; i<n; i++)
		args[i] = new VariableTerm(engine);
	    SymbolTerm sym = SymbolTerm.create(((SymbolTerm)a2).name(), n);
	    if(! a1.unify(new StructureTerm(sym, args), engine.trail))
		return engine.fail();
	    return cont;
	}
	// functor(+X,?Y,?Z)
	Term functor;
	IntegerTerm arity;
	if (a1.isSymbol() || a1.isNumber() || a1.isJavaObject() || a1.isClosure()) {
	    functor = a1;
	    arity   = new IntegerTerm(0);
	} else if (a1.isList()) {
	    functor = SYM_DOT;
	    arity   = new IntegerTerm(2);
	} else if (a1.isStructure()) {
	    functor = SymbolTerm.create(((StructureTerm)a1).name());
	    arity   = new IntegerTerm(((StructureTerm)a1).arity());
	} else {
	    return engine.fail();
	}
	if(! a2.unify(functor, engine.trail)) 
	    return engine.fail();
	if(! a3.unify(arity, engine.trail)) 
	    return engine.fail();
	return cont;
    }
}
