package com.googlecode.prolog_cafe.builtin;
import com.googlecode.prolog_cafe.exceptions.IllegalDomainException;
import com.googlecode.prolog_cafe.exceptions.IllegalTypeException;
import com.googlecode.prolog_cafe.exceptions.PInstantiationException;
import com.googlecode.prolog_cafe.lang.IntegerTerm;
import com.googlecode.prolog_cafe.lang.Operation;
import com.googlecode.prolog_cafe.lang.Predicate;
import com.googlecode.prolog_cafe.lang.Prolog;
import com.googlecode.prolog_cafe.lang.SymbolTerm;
import com.googlecode.prolog_cafe.lang.Term;
import com.googlecode.prolog_cafe.lang.VariableTerm;
/**
 * <code>atom_lengt/2</code><br>
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
*/
public class PRED_atom_length_2 extends Predicate.P2 {
    public PRED_atom_length_2(Term a1, Term a2, Operation cont) {
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
	int length;

	a1 = a1.dereference();
	a2 = a2.dereference();

	if (a1 instanceof VariableTerm)
	    throw new PInstantiationException(this, 1);
	if (! (a1 instanceof SymbolTerm))
	    throw new IllegalTypeException(this, 1, "atom", a1);
	length = ((SymbolTerm)a1).name().length();
	if (a2 instanceof VariableTerm) {
	    if (! a2.unify(new IntegerTerm(length), engine.trail))
		return engine.fail();
	} else if (a2 instanceof IntegerTerm) {
	    int n = ((IntegerTerm)a2).intValue();
	    if (n < 0)
		throw new IllegalDomainException(this, 2, "not_less_than_zero", a2);
	    if (length != n)
		return engine.fail();
	} else {
	    throw new IllegalTypeException(this, 1, "integer", a2);
	}
        return cont;
    }
}
