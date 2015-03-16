package com.googlecode.prolog_cafe.builtin;
import com.googlecode.prolog_cafe.exceptions.IllegalTypeException;
import com.googlecode.prolog_cafe.exceptions.PInstantiationException;
import com.googlecode.prolog_cafe.exceptions.RepresentationException;
import com.googlecode.prolog_cafe.exceptions.SyntaxException;
import  com.googlecode.prolog_cafe.lang.*;
/**
 * <code>number_codes/2</code><br>
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.1
 */
public class PRED_number_codes_2 extends Predicate.P2 {
    public PRED_number_codes_2(Term a1, Term a2, Operation cont) {
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
	if (Prolog.Nil.equals(a2))
	    throw new SyntaxException(this, 2, "character_code_list", a2, "");
	if (a1 instanceof VariableTerm) { // number_codes(-Number, +CharCodeList)
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
	    try {
		if (! a1.unify(new IntegerTerm(Integer.parseInt(sb.toString())), engine.trail))
		    return engine.fail();
		return cont;
	    } catch (NumberFormatException e) {}
	    try {
		if(! a1.unify(new DoubleTerm(Double.parseDouble(sb.toString())), engine.trail))
		    return engine.fail();
		return cont;
	    } catch (NumberFormatException e) {
		throw new SyntaxException(this, 2, "character_code_list", a2, "");
	    }
	} else if (((a1 instanceof IntegerTerm) || (a1 instanceof DoubleTerm))) { // number_codes(+Number, ?CharCodeList)
	    char[] chars = a1.toString().toCharArray();
	    Term y = Prolog.Nil;
	    for (int i=chars.length; i>0; i--) {
		y = new ListTerm(new IntegerTerm((int)chars[i-1]), y);
	    }
	    if (! a2.unify(y, engine.trail) ) 
		return engine.fail();
	    return cont;
	} else {
	    throw new IllegalTypeException(this, 1, "number", a1);
	}
    }
}
