package com.googlecode.prolog_cafe.builtin;
import com.googlecode.prolog_cafe.exceptions.IllegalTypeException;
import com.googlecode.prolog_cafe.exceptions.PInstantiationException;
import com.googlecode.prolog_cafe.exceptions.SyntaxException;
import  com.googlecode.prolog_cafe.lang.*;
/**
 * <code>number_chars/2</code><br>
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
 */
public class PRED_number_chars_2 extends Predicate.P2 {
    public PRED_number_chars_2(Term a1, Term a2, Operation cont) {
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
	if (a2.isNil())
	    throw new SyntaxException(this, 2, "character_code_list", a2, "");
	if (a1.isVariable()) { // number_chars(-Number, +CharList)
	    if (a2.isVariable()) {
		throw new PInstantiationException(this, 2);
	    } else if (! a2.isList()) {
		throw new IllegalTypeException(this, 2, "list", a2);
	    }
	    StringBuffer sb = new StringBuffer();
	    Term x = a2;
	    while(! x.isNil()) {
		if (x.isVariable())
		    throw new PInstantiationException(this, 2);
		if (! x.isList())
		    throw new IllegalTypeException(this, 2, "list", a2);
		Term car = ((ListTerm)x).car().dereference();
		if (car.isVariable())
		    throw new PInstantiationException(this, 2);
		if (! car.isSymbol() || ((SymbolTerm)car).name().length() != 1)
		    throw new IllegalTypeException(this, 2, "character", a2);
		sb.append(((SymbolTerm)car).name());
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
	} else if (a1.isNumber()) { // number_chars(+Number, ?CharList)
	    String s = a1.toString();
	    Term y = Prolog.Nil;
	    for (int i=s.length(); i>0; i--) {
		y = new ListTerm(SymbolTerm.create(s.substring(i-1,i)), y);
	    }
	    if (! a2.unify(y, engine.trail) ) 
		return engine.fail();
	    return cont;
	} else {
	    throw new IllegalTypeException(this, 1, "number", a1);
	}
    }
}
