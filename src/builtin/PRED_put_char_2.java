package com.googlecode.prolog_cafe.builtin;
import com.googlecode.prolog_cafe.exceptions.ExistenceException;
import com.googlecode.prolog_cafe.exceptions.IllegalDomainException;
import com.googlecode.prolog_cafe.exceptions.IllegalTypeException;
import com.googlecode.prolog_cafe.exceptions.PInstantiationException;
import com.googlecode.prolog_cafe.exceptions.PermissionException;
import com.googlecode.prolog_cafe.exceptions.RepresentationException;
import com.googlecode.prolog_cafe.lang.JavaObjectTerm;
import com.googlecode.prolog_cafe.lang.Operation;
import com.googlecode.prolog_cafe.lang.Predicate;
import com.googlecode.prolog_cafe.lang.Prolog;
import com.googlecode.prolog_cafe.lang.SymbolTerm;
import com.googlecode.prolog_cafe.lang.Term;
import com.googlecode.prolog_cafe.lang.VariableTerm;

import java.io.PrintWriter;
/**
   <code>put_char/2</code><br>
   @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
   @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
   @version 1.0
*/
public class PRED_put_char_2 extends Predicate.P2 {
    public PRED_put_char_2(Term a1, Term a2, Operation cont) {
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
	String str;
	char c;
	Object stream = null;

	// Char
	a2 = a2.dereference(); 
	if (a2 instanceof VariableTerm)
	    throw new PInstantiationException(this, 2);
	if (! (a2 instanceof SymbolTerm))
	    throw new IllegalTypeException(this, 2, "character", a2);
	// S_or_a
	a1 = a1.dereference(); 
	if (a1 instanceof VariableTerm) {
	    throw new PInstantiationException(this, 1);
	} else if (a1 instanceof SymbolTerm) {
	    if (! engine.getStreamManager().containsKey(a1))
		throw new ExistenceException(this, 1, "stream", a1, "");
	    stream = ((JavaObjectTerm) engine.getStreamManager().get(a1)).object();
	} else if (a1 instanceof JavaObjectTerm) {
	    stream = ((JavaObjectTerm) a1).object();
	} else {
	    throw new IllegalDomainException(this, 1, "stream_or_alias", a1);
	}
	if (! (stream instanceof PrintWriter))
	    throw new PermissionException(this, "output", "stream", a1, "");
	// print single character
	str = ((SymbolTerm)a2).name();
	if (str.length() != 1)
	    throw new IllegalTypeException(this, 2, "character", a2);
	c = str.charAt(0);
	if (! Character.isDefined(c))
	    throw new RepresentationException(this, 2, "character");
	((PrintWriter) stream).print(c);
	return cont;
    }
}
