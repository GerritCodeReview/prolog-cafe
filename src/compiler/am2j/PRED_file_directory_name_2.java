package com.googlecode.prolog_cafe.compiler.am2j;

import com.googlecode.prolog_cafe.exceptions.IllegalDomainException;
import com.googlecode.prolog_cafe.exceptions.PInstantiationException;
import com.googlecode.prolog_cafe.exceptions.PermissionException;
import com.googlecode.prolog_cafe.exceptions.PrologException;
import com.googlecode.prolog_cafe.lang.Operation;
import com.googlecode.prolog_cafe.lang.Predicate;
import com.googlecode.prolog_cafe.lang.Prolog;
import com.googlecode.prolog_cafe.lang.SymbolTerm;
import com.googlecode.prolog_cafe.lang.Term;

import java.io.File;

/** {@code file_directory_name(+File, -Directory)} */
public class PRED_file_directory_name_2 extends Predicate.P2 {
  public PRED_file_directory_name_2(Term a1, Term a2, Operation next) {
    arg1 = a1;
    arg2 = a2;
    cont = next;
  }

  @Override
  public Operation exec(Prolog engine) throws PrologException {
    engine.requireFeature(Prolog.Feature.IO, this, arg1);
    engine.setB0();

    Term a1 = arg1.dereference();
    if (a1.isVariable()) throw new PInstantiationException(this, 1);
    if (!a1.isSymbol()) throw new IllegalDomainException(this, 1, "file", a1);

    File file = new File(a1.name());
    File dir = file.getParentFile();
    if (dir == null) throw new IllegalDomainException(this, 1, "file", a1);

    Term a2 = arg2.dereference();
    if (a2.unify(SymbolTerm.create(dir.getPath()), engine.trail))
      return cont;
    else
      return engine.fail();
  }
}
