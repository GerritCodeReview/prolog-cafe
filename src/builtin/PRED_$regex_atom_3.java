package com.googlecode.prolog_cafe.builtin;
import com.googlecode.prolog_cafe.lang.IllegalTypeException;
import com.googlecode.prolog_cafe.lang.JavaObjectTerm;
import com.googlecode.prolog_cafe.lang.Operation;
import com.googlecode.prolog_cafe.lang.PInstantiationException;
import com.googlecode.prolog_cafe.lang.Predicate;
import com.googlecode.prolog_cafe.lang.Prolog;
import com.googlecode.prolog_cafe.lang.SymbolTerm;
import com.googlecode.prolog_cafe.lang.Term;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <code>$regex_atom/3</code><br>
 * 
 * <pre>
 *   '$regex_atom'(+Pattern, +Chars, -Match)
 * </pre>
 */
public class PRED_$regex_atom_3 extends Predicate.P3 {

  static final Operation regex_check = new PRED_regex_check();
  static final Operation regex_next = new PRED_regex_next();
  static final Operation regex_empty = new PRED_regex_empty();

  public PRED_$regex_atom_3(Term a1, Term a2, Term a3, Operation cont) {
        arg1 = a1;
        arg2 = a2;
        arg3 = a3;
        this.cont = cont;
  }

  public Operation exec(Prolog engine) {
      engine.setB0();
      engine.cont = cont;
      Term a1 = arg1.dereference();
      Term a2 = arg2.dereference();

      if (a1.isVariable()) {
        throw new PInstantiationException(this, 1);
      }
      if (!a1.isSymbol()) {
        throw new IllegalTypeException(this, 1, "atom", a1);
      }
      Pattern pattern = Pattern.compile(a1.name());

      if (a2.isVariable()) {
        throw new PInstantiationException(this, 1);
      }
      if (!a2.isSymbol()) {
        throw new IllegalTypeException(this, 1, "atom", a2);
      }
      Matcher matcher = pattern.matcher(a2.name());

      if (!matcher.find()) {
        return engine.fail();
      }

      engine.areg1 = new JavaObjectTerm(matcher);
      engine.areg3 = arg3;
      return engine.jtry3(regex_check, regex_next);
  }

  private static final class PRED_regex_check extends Operation {
    @Override
    public Operation exec(Prolog engine) {
      Term a1 = engine.areg1;
      Term a3 = engine.areg3;
      Matcher matcher = (Matcher)((JavaObjectTerm)a1).object();

      String match = matcher.group();
      SymbolTerm matchSym = SymbolTerm.create(match);

      if (!a3.unify(matchSym, engine.trail)) {
        return engine.fail();
      }
      return engine.cont;
    }
  }

  private static final class PRED_regex_next extends Operation {
    @Override
    public Operation exec(Prolog engine) {
      return engine.trust(regex_empty);
    }
  }

  private static final class PRED_regex_empty extends Operation {
    @Override
    public Operation exec(Prolog engine) {
      Term a1 = engine.areg1;
      Matcher matcher = (Matcher)((JavaObjectTerm)a1).object();
      if (!matcher.find()) {
        return engine.fail();
      }

      return engine.jtry3(regex_check, regex_next);
    }
  }
}