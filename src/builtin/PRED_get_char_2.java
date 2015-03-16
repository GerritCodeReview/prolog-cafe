package com.googlecode.prolog_cafe.builtin;
import com.googlecode.prolog_cafe.exceptions.ExistenceException;
import com.googlecode.prolog_cafe.exceptions.IllegalDomainException;
import com.googlecode.prolog_cafe.exceptions.IllegalTypeException;
import com.googlecode.prolog_cafe.exceptions.PInstantiationException;
import com.googlecode.prolog_cafe.exceptions.PermissionException;
import com.googlecode.prolog_cafe.exceptions.RepresentationException;
import com.googlecode.prolog_cafe.exceptions.TermException;
import com.googlecode.prolog_cafe.lang.*;
import java.io.*;
/**
 * <code>get_char/2</code><br>
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
*/
public class PRED_get_char_2 extends Predicate.P2 {
    private static final SymbolTerm SYM_EOF = SymbolTerm.intern("end_of_file");

    public PRED_get_char_2(Term a1, Term a2, Operation cont) {
        arg1 = a1;
        arg2 = a2;
        this.cont = cont;
    }

    boolean inCharacter(Term t) {
	if (! (t instanceof SymbolTerm))
	    return false;
	if (t.equals(SYM_EOF))
	    return true;
	return ((SymbolTerm)t).name().length() == 1;
    }

    @Override
    public Operation exec(Prolog engine) {
        engine.setB0();
        Term a1, a2;
        a1 = arg1;
        a2 = arg2;
	Object stream = null;

	// Char
	a2 = a2.dereference(); 
	if (! (a2 instanceof VariableTerm) && ! inCharacter(a2))
	    throw new IllegalTypeException(this, 2, "in_character", a2);
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
	// read single character
	try {
	    int c = ((PushbackReader)stream).read();
	    if (c < 0) { // EOF
		if (! a2.unify(SYM_EOF, engine.trail))
		    return engine.fail();
		return cont;
	    } 
	    if (! Character.isDefined(c))
		throw new RepresentationException(this, 0, "character");
	    if (! a2.unify(SymbolTerm.create((char)c), engine.trail))
		return engine.fail();
	    return cont;
	} catch (IOException e) {
	    throw new TermException(new JavaObjectTerm(e));
	}
    }
}
