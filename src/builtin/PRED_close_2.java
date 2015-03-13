package com.googlecode.prolog_cafe.builtin;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.PushbackReader;

import com.googlecode.prolog_cafe.exceptions.ExistenceException;
import com.googlecode.prolog_cafe.exceptions.IllegalDomainException;
import com.googlecode.prolog_cafe.exceptions.IllegalTypeException;
import com.googlecode.prolog_cafe.exceptions.PInstantiationException;
import com.googlecode.prolog_cafe.exceptions.SystemException;
import com.googlecode.prolog_cafe.exceptions.TermException;
import com.googlecode.prolog_cafe.lang.HashtableOfTerm;
import com.googlecode.prolog_cafe.lang.JavaObjectTerm;
import com.googlecode.prolog_cafe.lang.ListTerm;
import com.googlecode.prolog_cafe.lang.Operation;
import com.googlecode.prolog_cafe.lang.Predicate;
import com.googlecode.prolog_cafe.lang.Prolog;
import com.googlecode.prolog_cafe.lang.StructureTerm;
import com.googlecode.prolog_cafe.lang.SymbolTerm;
import com.googlecode.prolog_cafe.lang.Term;
import com.googlecode.prolog_cafe.lang.VariableTerm;
/**
 * <code>close/2</code><br>
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
*/
public class PRED_close_2 extends Predicate.P2 {
    private static final SymbolTerm SYM_ALIAS_1 = SymbolTerm.intern("alias", 1);
    private static final SymbolTerm SYM_FORCE_1 = SymbolTerm.intern("force", 1);
    private static final SymbolTerm SYM_TRUE    = SymbolTerm.intern("true");
    private static final SymbolTerm SYM_FALSE   = SymbolTerm.intern("false");

    public PRED_close_2(Term a1, Term a2, Operation cont) {
        arg1 = a1;
        arg2 = a2;
        this.cont = cont;
    }

    public Operation exec(Prolog engine) {
        engine.requireFeature(Prolog.Feature.IO, this, arg1);
        engine.setB0();
        Term a1, a2;
        a1 = arg1;
        a2 = arg2;

	boolean forceFlag = false;
	Object stream = null;

	// close options
	a2 = a2.dereference();
	Term tmp = a2;
	while (! Prolog.Nil.equals(tmp)) {
	    if (tmp instanceof VariableTerm)
		throw new PInstantiationException(this, 2);
	    if (! (tmp instanceof ListTerm))
		throw new IllegalTypeException(this, 2, "list", a2);
	    Term car = ((ListTerm) tmp).car().dereference();
	    if (car instanceof VariableTerm)
		throw new PInstantiationException(this, 2);
	    if (car instanceof StructureTerm) {
		SymbolTerm functor = ((StructureTerm) car).functor();
		Term[] args = ((StructureTerm) car).args();
		if (functor.equals(SYM_FORCE_1)) {
		    Term bool = args[0].dereference();
		    if (bool.equals(SYM_TRUE))
			forceFlag = true;
		    else if (bool.equals(SYM_FALSE))
			forceFlag = false;
		    else 
			throw new IllegalDomainException(this, 2, "close_option", car);
		} else {
		    throw new IllegalDomainException(this, 2, "close_option", car);
		}
	    } else {
		throw new IllegalDomainException(this, 2, "close_option", car);
	    }
	    tmp = ((ListTerm) tmp).cdr().dereference();
	}
	//stream
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
	if (stream instanceof PushbackReader) {
	    PushbackReader in = (PushbackReader) stream;
	    try {
		in.close();
	    } catch (IOException e) {
		throw new TermException(new JavaObjectTerm(e));
	    }
	} else if (stream instanceof PrintWriter) {
	    PrintWriter out = (PrintWriter) stream;
	    if (out.checkError()) {
		if (! forceFlag)
		    throw new SystemException("output stream error");
	    }
	    out.flush();
	    out.close();
	} else {
	    throw new IllegalDomainException(this, 1, "stream_or_alias", a1);
	}
	// delete associated entries from the stream manager
	HashtableOfTerm streamManager = engine.getStreamManager();
	if (a1 instanceof SymbolTerm) {
	    streamManager.remove(engine.getStreamManager().get(a1));
	    streamManager.remove(a1);
	} else if (a1 instanceof JavaObjectTerm) {
	    Term tmp2 = streamManager.get(a1);
	    while (! Prolog.Nil.equals(tmp2)) {
		Term car = ((ListTerm) tmp2).car().dereference();
		if (car instanceof StructureTerm) {
		    SymbolTerm functor = ((StructureTerm) car).functor();
		    Term[] args = ((StructureTerm) car).args();
		    if (functor.equals(SYM_ALIAS_1)) {
			Term alias = args[0].dereference();
			streamManager.remove(alias);
		    }
		}
		tmp2 = ((ListTerm) tmp2).cdr().dereference();
	    }
	    streamManager.remove(a1);
	} else {
	    throw new IllegalDomainException(this, 1, "stream_or_alias", a1);
	}
        return cont;
    }
}
