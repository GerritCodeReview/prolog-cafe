package com.googlecode.prolog_cafe.builtin;
import com.googlecode.prolog_cafe.exceptions.ExistenceException;
import com.googlecode.prolog_cafe.exceptions.IllegalDomainException;
import com.googlecode.prolog_cafe.exceptions.PInstantiationException;
import com.googlecode.prolog_cafe.exceptions.PermissionException;
import com.googlecode.prolog_cafe.exceptions.RepresentationException;
import com.googlecode.prolog_cafe.exceptions.TermException;
import  com.googlecode.prolog_cafe.lang.*;
import java.io.*;
/**
 * <code>read_line/2</code><br>
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
 */
class PRED_read_line_2 extends Predicate.P2 {
    public PRED_read_line_2(Term a1, Term a2, Operation cont) {
	arg1 = a1;
	arg2 = a2;
	this.cont = cont;
    }

    public Operation exec(Prolog engine) {
        engine.setB0();
        Term a1, a2;
        a1 = arg1;
        a2 = arg2;
	Object stream = null;
	String line;
	char[] chars;
	Term t;

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
	if (! (stream instanceof PushbackReader))
	    throw new PermissionException(this, "input", "stream", a1, "");
	// read line
	try {
	    line = (new BufferedReader((PushbackReader)stream)).readLine();
	    if (line == null) { // end_of_stream
		if(! a2.unify(new IntegerTerm(-1), engine.trail))
		    return engine.fail();
		return cont;
	    }
	    chars = line.toCharArray();
	    t = Prolog.Nil;
	    for (int i=chars.length; i>0; i--) {
		if (! Character.isDefined((int)chars[i-1]))
		    throw new RepresentationException(this, 0, "character");
		t = new ListTerm(new IntegerTerm((int)chars[i-1]), t);
	    }
	    if(! a2.unify(t, engine.trail))
		return engine.fail();
	    return cont;
	} catch (IOException e) {
	    throw new TermException(new JavaObjectTerm(e));
	}
    }
}
