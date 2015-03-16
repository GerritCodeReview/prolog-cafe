package com.googlecode.prolog_cafe.builtin;
import com.googlecode.prolog_cafe.exceptions.BuiltinException;
import com.googlecode.prolog_cafe.exceptions.IllegalTypeException;
import com.googlecode.prolog_cafe.exceptions.JavaException;
import com.googlecode.prolog_cafe.exceptions.PInstantiationException;
import com.googlecode.prolog_cafe.lang.*;
import java.util.Arrays;
/**
 * <code>keysort/2</code><br>
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.1
 */
public class PRED_keysort_2 extends Predicate.P2 {
    private static final SymbolTerm SYM_HYPHEN_2 = SymbolTerm.intern("-", 2);

    public PRED_keysort_2(Term a1, Term a2, Operation cont) {
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
	Term tmp;
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
	    if (list[i] instanceof VariableTerm)
		throw new PInstantiationException(this, 1);
	    if (! (list[i] instanceof StructureTerm))
		throw new IllegalTypeException(this, 1, "key_value_pair", a1);
	    if (! ((StructureTerm) list[i]).functor().equals(SYM_HYPHEN_2))
		throw new IllegalTypeException(this, 1, "key_value_pair", a1);
	    tmp = ((ListTerm)tmp).cdr().dereference();
	}
	if (! tmp.equals(Prolog.Nil))
	    throw new PInstantiationException(this, 1);
	try {
	    Arrays.sort(list, new KeySortComparator());
	} catch (BuiltinException e) {
	    e.goal = this; e.argNo = 1; throw e;
	} catch (ClassCastException e1) {
	    throw new JavaException(this, 1, e1);
	}
	tmp = Prolog.Nil;
	for (int i=list.length-1; i>=0; i--) {
	    tmp = new ListTerm(list[i], tmp);
	}
	if(! a2.unify(tmp, engine.trail)) 
	    return engine.fail();
	return cont;
    }
}

class KeySortComparator implements java.util.Comparator<Term> {
    @Override
    public int compare(Term t1, Term t2) {
	Term arg1 = ((StructureTerm)t1).args()[0].dereference();
	Term arg2 = ((StructureTerm)t2).args()[0].dereference();
	return arg1.compareTo(arg2);
    }
}
