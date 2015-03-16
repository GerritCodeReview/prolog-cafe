package com.googlecode.prolog_cafe.builtin;
import com.googlecode.prolog_cafe.exceptions.IllegalTypeException;
import com.googlecode.prolog_cafe.exceptions.PInstantiationException;
import com.googlecode.prolog_cafe.lang.ListTerm;
import com.googlecode.prolog_cafe.lang.Operation;
import com.googlecode.prolog_cafe.lang.Predicate;
import com.googlecode.prolog_cafe.lang.Prolog;
import com.googlecode.prolog_cafe.lang.SymbolTerm;
import com.googlecode.prolog_cafe.lang.Term;
import com.googlecode.prolog_cafe.lang.VariableTerm;
/**
 * <code>atom_chars/2</code><br>
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.1
 */
public class PRED_atom_chars_2 extends Predicate.P2 {
    public PRED_atom_chars_2(Term a1, Term a2, Operation cont) {
	arg1 = a1;
	arg2 = a2;
	this.cont = cont;
    }

    @Override
    public Operation exec(Prolog engine) {
        engine.setB0();
	Term a1, a2;
	a1 = arg1;
	a2 = arg2;

	a1 = a1.dereference();
	a2 = a2.dereference();
	if (a1 instanceof VariableTerm) { // atom_chars(-Atom, +CharList)
	    if (Prolog.Nil.equals(a2)) {
		if (! a1.unify(SymbolTerm.intern(""), engine.trail))
		    return engine.fail();
		return cont;
	    } else if (a2 instanceof VariableTerm) {
		throw new PInstantiationException(this, 2);
	    } else if (! (a2 instanceof ListTerm)) {
		throw new IllegalTypeException(this, 2, "list", a2);
	    }
	    StringBuffer sb = new StringBuffer();
	    Term x = a2;
	    while(! Prolog.Nil.equals(x)) {
		if (x instanceof VariableTerm)
		    throw new PInstantiationException(this, 2);
		if (! (x instanceof ListTerm))
		    throw new IllegalTypeException(this, 2, "list", a2);
		Term car = ((ListTerm)x).car().dereference();
		if (car instanceof VariableTerm)
		    throw new PInstantiationException(this, 2);
		if (! (car instanceof SymbolTerm) || ((SymbolTerm)car).name().length() != 1)
		    throw new IllegalTypeException(this, 2, "character", a2);
		sb.append(((SymbolTerm)car).name());
		x = ((ListTerm)x).cdr().dereference();
	    }
	    if (! a1.unify(SymbolTerm.create(sb.toString()), engine.trail))
		return engine.fail();
	    return cont;
	} else if (Prolog.Nil.equals(a2) || a2 instanceof VariableTerm || a2 instanceof ListTerm) { // atom_chars(+Atom, ?CharList)
	    if (! (a1 instanceof SymbolTerm))
		throw new IllegalTypeException(this, 1, "atom", a1);
	    String s = ((SymbolTerm)a1).name();
	    Term x = Prolog.Nil;
	    for (int i=s.length(); i>0; i--) {
		x = new ListTerm(SymbolTerm.create(s.substring(i-1,i)), x);
	    }
	    if(! a2.unify(x, engine.trail)) 
		return engine.fail();
	    return cont;
	} else {
	    return engine.fail();
	}
    }
}
