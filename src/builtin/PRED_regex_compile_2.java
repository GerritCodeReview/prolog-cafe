package com.googlecode.prolog_cafe.builtin;
import com.googlecode.prolog_cafe.exceptions.IllegalTypeException;
import com.googlecode.prolog_cafe.exceptions.PInstantiationException;
import com.googlecode.prolog_cafe.lang.JavaObjectTerm;
import com.googlecode.prolog_cafe.lang.Operation;
import com.googlecode.prolog_cafe.lang.Predicate;
import com.googlecode.prolog_cafe.lang.Prolog;
import com.googlecode.prolog_cafe.lang.SymbolTerm;
import com.googlecode.prolog_cafe.lang.Term;
import com.googlecode.prolog_cafe.lang.VariableTerm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <code>regex_compile/2</code><br>
 *
 * <pre>
 *   'regex_compile'(+regex string, -Pattern object)
 * </pre>
 */
public class PRED_regex_compile_2 extends Predicate.P2 {

  public PRED_regex_compile_2(Term a1, Term a2, Operation cont) {
        arg1 = a1;
        arg2 = a2;
        this.cont = cont;
  }

  @Override
  public Operation exec(Prolog engine) {
      engine.setB0();
      Term a1 = arg1.dereference();
      Term a2 = arg2.dereference();

      if (a1 instanceof VariableTerm) {
        throw new PInstantiationException(this, 1);
      }
      if (!(a1 instanceof SymbolTerm)) {
        throw new IllegalTypeException(this, 1, "atom", a1);
      }
      Pattern pattern = Pattern.compile(a1.name(), Pattern.MULTILINE);

      if (!a2.unify(new JavaObjectTerm(pattern), engine.trail)) {
        return engine.fail();
      }
      return cont;
  }
}