package com.googlecode.prolog_cafe.compiler.am2j;

import com.googlecode.prolog_cafe.exceptions.IllegalDomainException;
import com.googlecode.prolog_cafe.exceptions.PInstantiationException;
import com.googlecode.prolog_cafe.exceptions.PrologException;
import com.googlecode.prolog_cafe.lang.Operation;
import com.googlecode.prolog_cafe.lang.Predicate;
import com.googlecode.prolog_cafe.lang.Prolog;
import com.googlecode.prolog_cafe.lang.SymbolTerm;
import com.googlecode.prolog_cafe.lang.Term;

import java.io.File;

/** {@code exists_directory(+Directory)} */
public class PRED_exists_directory_1 extends Predicate.P1 {
  public PRED_exists_directory_1(Term a1, Operation next) {
    arg1 = a1;
    cont = next;
  }

  @Override
  public Operation exec(Prolog engine) throws PrologException {
    engine.requireFeature(Prolog.Feature.IO, this, arg1);
    engine.setB0();

    Term a1 = arg1.dereference();
    if (a1.isVariable()) throw new PInstantiationException(this, 1);
    if (!a1.isSymbol()) throw new IllegalDomainException(this, 1, "directory", a1);

    File file = new File(a1.name());
    if (file.isDirectory())
      return cont;
    else
      return engine.fail();
  }
}
