package com.googlecode.prolog_cafe.builtin;

import com.googlecode.prolog_cafe.lang.IllegalDomainException;
import com.googlecode.prolog_cafe.lang.Operation;
import com.googlecode.prolog_cafe.lang.PInstantiationException;
import com.googlecode.prolog_cafe.lang.PermissionException;
import com.googlecode.prolog_cafe.lang.Predicate;
import com.googlecode.prolog_cafe.lang.Prolog;
import com.googlecode.prolog_cafe.lang.PrologException;
import com.googlecode.prolog_cafe.lang.SymbolTerm;
import com.googlecode.prolog_cafe.lang.Term;

import java.io.File;

/** {@code make_directory(+Dir)} */
public class PRED_make_directory_1 extends Predicate.P1 {
  public PRED_make_directory_1(Term a1, Operation next) {
    arg1 = a1;
    cont = next;
  }

  @Override
  public Operation exec(Prolog engine) throws PrologException {
    engine.requireFeature(Prolog.Feature.IO, this, arg1);
    engine.setB0();

    Term a1 = arg1.dereference();
    if (a1.isVariable()) throw new PInstantiationException(this, 1);
    if (!a1.isSymbol()) throw new IllegalDomainException(this, 1, "dir", a1);

    File file = new File(((SymbolTerm) a1).name());
    if (file.mkdir())
      return cont;
    else
      throw new PermissionException(this, "open", "dir", a1, "cannot create");
  }
}
