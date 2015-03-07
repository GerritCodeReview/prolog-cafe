package com.googlecode.prolog_cafe.builtin;
import com.googlecode.prolog_cafe.exceptions.ExistenceException;
import com.googlecode.prolog_cafe.exceptions.IllegalDomainException;
import com.googlecode.prolog_cafe.exceptions.IllegalTypeException;
import com.googlecode.prolog_cafe.exceptions.InternalException;
import com.googlecode.prolog_cafe.exceptions.PInstantiationException;
import com.googlecode.prolog_cafe.exceptions.PermissionException;
import com.googlecode.prolog_cafe.lang.*;
import java.io.*;
/**
   <code>open/4</code><br>
   @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
   @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
   @version 1.0
*/
public class PRED_open_4 extends Predicate.P4 {
    private static final SymbolTerm SYM_TEXT        = SymbolTerm.intern("text");
    private static final SymbolTerm SYM_READ        = SymbolTerm.intern("read");
    private static final SymbolTerm SYM_WRITE       = SymbolTerm.intern("write");
    private static final SymbolTerm SYM_APPEND      = SymbolTerm.intern("append");
    private static final SymbolTerm SYM_INPUT       = SymbolTerm.intern("input");
    private static final SymbolTerm SYM_OUTPUT      = SymbolTerm.intern("output");
    private static final SymbolTerm SYM_ALIAS_1     = SymbolTerm.intern("alias", 1);
    private static final SymbolTerm SYM_MODE_1      = SymbolTerm.intern("mode", 1);
    private static final SymbolTerm SYM_TYPE_1      = SymbolTerm.intern("type", 1);
    private static final SymbolTerm SYM_FILE_NAME_1 = SymbolTerm.intern("file_name", 1);

    public PRED_open_4(Term a1, Term a2, Term a3, Term a4, Operation cont) {
        arg1 = a1;
        arg2 = a2;
        arg3 = a3;
        arg4 = a4;
        this.cont = cont;
    }

    public Operation exec(Prolog engine) {
        engine.requireFeature(Prolog.Feature.IO, this, arg1);
        engine.setB0();
	File file;
	Term alias = null;
	Term opts = Prolog.Nil;
	JavaObjectTerm streamObject;
        Term a1, a2, a3, a4;
        a1 = arg1;
        a2 = arg2;
        a3 = arg3;
        a4 = arg4;

	// stream
	a3 = a3.dereference();
	if (! a3.isVariable())
	    throw new IllegalTypeException(this, 3, "variable", a3);
	// source_sink
	a1 = a1.dereference();
	if (a1.isVariable())
	    throw new PInstantiationException(this, 1);
	if (! a1.isSymbol())
	    throw new IllegalDomainException(this, 1, "source_sink", a1);
	file = new File(((SymbolTerm) a1).name());
	// io_mode
	a2 = a2.dereference();
	if (a2.isVariable())
	    throw new PInstantiationException(this, 2);
	if (! a2.isSymbol()) 
	    throw new IllegalTypeException(this, 2, "atom", a2);
	try {
	    if (a2.equals(SYM_READ)) {
		if (! file.exists())
		    throw new ExistenceException(this, 1, "source_sink", a1, "");
		PushbackReader in = 
		    new PushbackReader(new BufferedReader(new FileReader(file)), engine.PUSHBACK_SIZE);
		streamObject = new JavaObjectTerm(in);
		opts = new ListTerm(SYM_INPUT, opts);
	    } else if (a2.equals(SYM_WRITE)) {
		PrintWriter out = 
		    new PrintWriter(new BufferedWriter(new FileWriter(file, false)));
		streamObject = new JavaObjectTerm(out);
		opts = new ListTerm(SYM_OUTPUT, opts);
	    } else if (a2.equals(SYM_APPEND)) {
		PrintWriter out = 
		    new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
		streamObject = new JavaObjectTerm(out);
		opts = new ListTerm(SYM_OUTPUT, opts);
	    } else {
		throw new IllegalDomainException(this, 2, "io_mode", a2);
	    }
	} catch (IOException e) {
	    throw new PermissionException(this, "open", "source_sink", a1, "");
	}
	if (engine.getStreamManager().containsKey(streamObject))
	    throw new InternalException("stream object is duplicated");
	// stream_options
	a4 = a4.dereference();
	Term tmp = a4;
	while (! tmp.isNil()) {
	    if (tmp.isVariable())
		throw new PInstantiationException(this, 4);
	    if (! tmp.isList())
		throw new IllegalTypeException(this, 4, "list", a4);
	    Term car = ((ListTerm) tmp).car().dereference();
	    if (car.isVariable())
		throw new PInstantiationException(this, 4);
	    if (car.isStructure()) {
		SymbolTerm functor = ((StructureTerm) car).functor();
		Term[] args = ((StructureTerm) car).args();
		if (functor.equals(SYM_ALIAS_1)) {
		    alias = args[0].dereference();
		    if (! alias.isSymbol())
			throw new IllegalDomainException(this, 4, "stream_option", car);
		    if (engine.getStreamManager().containsKey(alias))
			throw new PermissionException(this, "open", "source_sink", car, "");
		} else {
		    throw new IllegalDomainException(this, 4, "stream_option", car);
		}
	    } else {
		throw new IllegalDomainException(this, 4, "stream_option", car);
	    }
	    tmp = ((ListTerm) tmp).cdr().dereference();
	}
	opts = new ListTerm(new StructureTerm(SYM_TYPE_1, SYM_TEXT), opts);
	opts = new ListTerm(new StructureTerm(SYM_MODE_1, a2), opts);
	opts = new ListTerm(new StructureTerm(SYM_FILE_NAME_1, SymbolTerm.create(file.getAbsolutePath())), opts);
	if (alias != null) {
	    engine.getStreamManager().put(alias, streamObject);
	    opts = new ListTerm(new StructureTerm(SYM_ALIAS_1, alias), opts);
	}
	((VariableTerm)a3).bind(streamObject, engine.trail);
	engine.getStreamManager().put(streamObject, opts);
        return cont;
    }
}



