package com.googlecode.prolog_cafe.repl;

import com.googlecode.prolog_cafe.exceptions.HaltException;
import com.googlecode.prolog_cafe.lang.ListTerm;
import com.googlecode.prolog_cafe.lang.Prolog;
import com.googlecode.prolog_cafe.lang.StructureTerm;
import com.googlecode.prolog_cafe.lang.SymbolTerm;
import com.googlecode.prolog_cafe.lang.Term;

import java.util.EnumSet;
import java.util.StringTokenizer;

/** Launches the meta-interpreter from the command line. */
public class PrologMain {
  private static final String COPYRIGHT = "Copyright(C) 1997-2009 M.Banbara and N.Tamura";
  private static final String HEADER = "Prolog Cafe (" + COPYRIGHT + ")";

  public static void main(String argv[]) {
    try {
      System.err.println(HEADER);
      if (argv.length != 1) {
        usage();
        System.exit(1);
      }

      Term packages = new ListTerm(
        SymbolTerm.intern(Prolog.BUILTIN),
        new ListTerm(SymbolTerm.intern("user"), Prolog.Nil));

      Term goal = parseAtomicGoal(argv[0]);
      if (goal == null) {
        usage();
        System.exit(1);
      }

      BlockingPrologControl p = new BlockingPrologControl();
      p.setEnabled(EnumSet.allOf(Prolog.Feature.class), true);
      p.configureUserIO(System.in, System.out, System.err);
      p.setPredicate(Prolog.BUILTIN, "initialization", packages, goal);
      for (boolean r = p.call(); r; r = p.redo()) {
      }
    } catch (HaltException e) {
      System.exit(e.getStatus());
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Returns a term for given string representation of atom goal, or
   * <code>null</code> if parsing fails.
   *
   * @param s a string representation of initial goal (ex. foge:main).
   * @return a term which corresponds to a given string, or <code>null</code> if
   *         parsing fails.
   */
  private static Term parseAtomicGoal(String s) {
    StringTokenizer st = new StringTokenizer(s, ":");
    int i = st.countTokens();
    if (i == 1) {
      Term[] args = {
          SymbolTerm.intern("user"),
          SymbolTerm.create(st.nextToken())};
      return new StructureTerm(SymbolTerm.intern(":", 2), args);
    } else if (i == 2) {
      Term[] args = {
          SymbolTerm.create(st.nextToken()),
          SymbolTerm.create(st.nextToken())};
      return new StructureTerm(SymbolTerm.intern(":", 2), args);
    } else {
      return null;
    }
  }

  private static void usage() {
    System.err.println("Usage:");
    System.err.println("  java -jar cafeteria.jar package:predicate");
    System.err.println("  java -jar cafeteria.jar predicate");
    System.err.println("    package:        package name\n");
    System.err.println("    predicate:      predicate name (only atom)");
  }
}
