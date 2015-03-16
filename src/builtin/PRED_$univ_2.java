package com.googlecode.prolog_cafe.builtin;
import com.googlecode.prolog_cafe.exceptions.IllegalDomainException;
import com.googlecode.prolog_cafe.exceptions.IllegalTypeException;
import com.googlecode.prolog_cafe.exceptions.PInstantiationException;
import com.googlecode.prolog_cafe.lang.ClosureTerm;
import com.googlecode.prolog_cafe.lang.DoubleTerm;
import com.googlecode.prolog_cafe.lang.IntegerTerm;
import com.googlecode.prolog_cafe.lang.JavaObjectTerm;
import com.googlecode.prolog_cafe.lang.ListTerm;
import com.googlecode.prolog_cafe.lang.Operation;
import com.googlecode.prolog_cafe.lang.Predicate;
import com.googlecode.prolog_cafe.lang.Prolog;
import com.googlecode.prolog_cafe.lang.StructureTerm;
import com.googlecode.prolog_cafe.lang.SymbolTerm;
import com.googlecode.prolog_cafe.lang.Term;
import com.googlecode.prolog_cafe.lang.VariableTerm;
/**
   <code>'$univ'/2</code><br>
   @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
   @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
   @version 1.0
*/
public class PRED_$univ_2 extends Predicate.P2 {
    private static final SymbolTerm SYM_DOT = SymbolTerm.intern(".");
    private static final SymbolTerm SYM_NIL = Prolog.Nil;

    public PRED_$univ_2(Term a1, Term a2, Operation cont) {
        arg1 = a1;
        arg2 = a2;
        this.cont = cont;
    }

    @Override
    public String toString() {
        return "=..(" + arg1 + "," + arg2 + ")";
    }

    @Override
    public Operation exec(Prolog engine) {
        engine.setB0();
        Term a1, a2;
        a1 = arg1;
        a2 = arg2;

	a1 = a1.dereference();
	if (a1 instanceof SymbolTerm || ((a1 instanceof IntegerTerm) || (a1 instanceof DoubleTerm)) || a1 instanceof JavaObjectTerm || a1 instanceof ClosureTerm) {
	    if (! a2.unify(new ListTerm(a1, SYM_NIL), engine.trail))
		return engine.fail();
	} else if (a1 instanceof ListTerm) {
	    Term t = new ListTerm(((ListTerm)a1).cdr(), SYM_NIL);
	    t = new ListTerm(((ListTerm)a1).car(), t);
	    t = new ListTerm(SYM_DOT, t);
	    if (! a2.unify(t, engine.trail))
		return engine.fail();
	} else if (a1 instanceof StructureTerm) {
	    SymbolTerm sym = SymbolTerm.create(((StructureTerm)a1).functor().name());
	    Term[] args = ((StructureTerm)a1).args();
	    Term t = SYM_NIL;
	    for (int i=args.length; i>0; i--)
		t = new ListTerm(args[i-1], t);
	    if (! a2.unify(new ListTerm(sym, t), engine.trail))
		return engine.fail();
	} else if (a1 instanceof VariableTerm) {
	    a2 = a2.dereference();
	    if (a2 instanceof VariableTerm)
		throw new PInstantiationException(this, 2);
	    else if (a2.equals(SYM_NIL))
		throw new IllegalDomainException(this, 2, "non_empty_list", a2);
	    else if (! (a2 instanceof ListTerm))
		throw new IllegalTypeException(this, 2, "list", a2);
	    Term head = ((ListTerm)a2).car().dereference();
	    Term tail = ((ListTerm)a2).cdr().dereference();
	    if (head instanceof VariableTerm)
		throw new PInstantiationException(this, 2);
	    if (tail.equals(SYM_NIL)) {
		if (head instanceof SymbolTerm || ((head instanceof IntegerTerm) || (head instanceof DoubleTerm)) || head instanceof JavaObjectTerm || head instanceof ClosureTerm) {
		    if (! a1.unify(head, engine.trail))
			return engine.fail();
		    return cont;
		} else {
		    throw new IllegalTypeException(this, 2, "atomic", head);
		}
	    }
	    if (! (head instanceof SymbolTerm))
		throw new IllegalTypeException(this, 2, "atom", head);
	    Term x = tail;
	    while(! x.equals(SYM_NIL)) {
		if (x instanceof VariableTerm)
		    throw new PInstantiationException(this, 2);
		if (! (x instanceof ListTerm))
		    throw new IllegalTypeException(this, 2, "list", a2);
		x = ((ListTerm)x).cdr().dereference();
	    }
	    int n = ((ListTerm)a2).length() - 1;
	    SymbolTerm sym = SymbolTerm.create(((SymbolTerm)head).name(), n);
	    Term[] args = new Term[n];
	    for(int i=0; i<n; i++) {
		args[i] = ((ListTerm)tail).car().dereference();
		tail = ((ListTerm)tail).cdr().dereference();
	    }
	    if (! a1.unify(new StructureTerm(sym, args), engine.trail))
		return engine.fail();
	} else {
	    return engine.fail();
	}
        return cont;
    }
}
