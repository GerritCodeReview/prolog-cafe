package com.googlecode.prolog_cafe.builtin;
import com.googlecode.prolog_cafe.exceptions.IllegalTypeException;
import com.googlecode.prolog_cafe.exceptions.PInstantiationException;
import com.googlecode.prolog_cafe.exceptions.RepresentationException;
import  com.googlecode.prolog_cafe.lang.*;
/**
 * <code>atom_codes/2</code><br>
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.1
 */
public class PRED_atom_codes_2 extends Predicate.P2 {
    public PRED_atom_codes_2(Term a1, Term a2, Operation cont) {
	arg1 = a1;
	arg2 = a2;
	this.cont = cont;
    }

    public Operation exec(Prolog engine) {
        engine.setB0();
	Term a1, a2;
	a1 = arg1;
	a2 = arg2;

	a1 = a1.dereference();
	a2 = a2.dereference();
	if (a1 instanceof VariableTerm) { // atom_codes(-Atom, +CharCodeList)
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
		if (! (car instanceof IntegerTerm)) 
		    throw new RepresentationException(this, 2, "character_code");
		// car is an integer
		int i = ((IntegerTerm)car).intValue();
		if (! Character.isDefined((char)i))
		    throw new RepresentationException(this, 2, "character_code");
		sb.append((char)i);
		x = ((ListTerm)x).cdr().dereference();
	    }
	    if (! a1.unify(SymbolTerm.create(sb.toString()), engine.trail))
		return engine.fail();
	    return cont;
	} else { // atom_codes(+Atom, ?CharCodeList)
	    if (! (a1 instanceof SymbolTerm))
		throw new IllegalTypeException(this, 1, "atom", a1);
	    char[] chars = ((SymbolTerm)a1).name().toCharArray();
	    Term x = Prolog.Nil;
	    for (int i=chars.length; i>0; i--) {
		x = new ListTerm(new IntegerTerm((int)chars[i-1]), x);
	    }
	    if(! a2.unify(x, engine.trail)) 
		return engine.fail();
	    return cont;
	}
    }
}
