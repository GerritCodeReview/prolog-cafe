package com.googlecode.prolog_cafe.builtin;
import com.googlecode.prolog_cafe.exceptions.IllegalTypeException;
import com.googlecode.prolog_cafe.exceptions.JavaException;
import com.googlecode.prolog_cafe.exceptions.PInstantiationException;
import com.googlecode.prolog_cafe.lang.ListTerm;
import com.googlecode.prolog_cafe.lang.Operation;
import com.googlecode.prolog_cafe.lang.Predicate;
import com.googlecode.prolog_cafe.lang.Prolog;
import com.googlecode.prolog_cafe.lang.Term;
import com.googlecode.prolog_cafe.lang.VariableTerm;

import java.util.Arrays;
/**
 * <code>sort/2</code><br>
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.1
 */
public class PRED_sort_2 extends Predicate.P2 {
    public PRED_sort_2(Term a1, Term a2, Operation cont) {
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
	int len;
	Term tmp, tmp2;
	Term[] list;

	a1 = a1.dereference();
	if (a1 instanceof VariableTerm) {
	    throw new PInstantiationException(this, 1);
	} else if (a1.equals(Prolog.Nil)) {
	    if (! a2.unify(Prolog.Nil, engine.trail))
		return engine.fail();
	    return cont;
	} else if (! (a1 instanceof ListTerm)) {
	    throw new IllegalTypeException(this, 1, "list", a1);
	}
	len = ((ListTerm)a1).length();
	list = new Term[len];
	tmp = a1;
	for (int i=0; i<len; i++) {
	    if (! (tmp instanceof ListTerm))
		throw new IllegalTypeException(this, 1, "list", a1);
	    list[i] = ((ListTerm)tmp).car().dereference();
	    tmp = ((ListTerm)tmp).cdr().dereference();
	}
	if (! tmp.equals(Prolog.Nil))
	    throw new PInstantiationException(this, 1);
	try {
	    Arrays.sort(list);
	} catch (ClassCastException e) {
	    throw new JavaException(this, 1, e);
	}
	tmp = Prolog.Nil;
	tmp2 = null;
	for (int i=list.length-1; i>=0; i--) {
	    if (! list[i].equals(tmp2))
		tmp = new ListTerm(list[i], tmp);
	    tmp2 = list[i];
	}
	if(! a2.unify(tmp, engine.trail)) 
	    return engine.fail();
	return cont;
    }
}
