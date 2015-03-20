package com.googlecode.prolog_cafe.repl;

import com.googlecode.prolog_cafe.exceptions.HaltException;
import com.googlecode.prolog_cafe.lang.JavaObjectTerm;
import com.googlecode.prolog_cafe.lang.ListTerm;
import com.googlecode.prolog_cafe.lang.Prolog;
import com.googlecode.prolog_cafe.lang.StructureTerm;
import com.googlecode.prolog_cafe.lang.SymbolTerm;
import com.googlecode.prolog_cafe.lang.Term;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;
import java.io.PushbackReader;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.StringTokenizer;

/** Launches the meta-interpreter from the command line. */
public class PrologMain {
  private static final String COPYRIGHT = "Copyright(C) 1997-2009 M.Banbara and N.Tamura";
  private static final String HEADER = "Prolog Cafe (" + COPYRIGHT + ")";

  public static void main(String argv[]) {
    try {
      System.err.println(HEADER);

      BlockingPrologControl p = new BlockingPrologControl();
      p.configureUserIO(System.in, System.out, System.err);
      p.setMaxDatabaseSize(256);

      List<File> toLoad = new ArrayList<>(4);
      long reductionLimit = Long.MAX_VALUE;
      Term goal = null;
      for (int i = 0; i < argv.length; i++) {
        String arg = argv[i];
        if (arg.equals("--enable-io")) {
          p.setEnabled(Prolog.Feature.IO, true);
        } else if (arg.equals("--enable-statistics")) {
          p.setEnabled(Prolog.Feature.STATISTICS, true);
        } else if (arg.startsWith("--max-database-size=")) {
          String v = arg.substring(arg.indexOf('=') + 1);
          p.setMaxDatabaseSize(Integer.parseInt(v, 10));
        } else if (arg.startsWith("--reduction-limit=")) {
          String v = arg.substring(arg.indexOf('=') + 1);
          reductionLimit = Long.parseLong(v, 10);
        } else if (arg.equals("-f") && i + 1 < argv.length) {
          toLoad.add(new File(argv[++i]));
        } else if (arg.startsWith("-")) {
          usage();
          System.exit(1);
        } else if (i == argv.length - 1) {
          goal = parseAtomicGoal(arg);
        } else {
          usage();
          System.exit(1);
        }
      }

      initializePackages(p, goal);
      for (File file : toLoad) {
        try (FileReader src = new FileReader(file);
            BufferedReader buf = new BufferedReader(src);
            PushbackReader in = new PushbackReader(buf, Prolog.PUSHBACK_SIZE)) {
          Term path = SymbolTerm.create(file.getPath());
          if (!p.execute(Prolog.BUILTIN, "consult_stream",
              path, new JavaObjectTerm(in))) {
            System.err.println();
            System.err.flush();
            System.exit(1);
          }
        }
        System.err.println();
        System.err.flush();
      }

      if (goal == null) {
        System.err.println();
        System.err.flush();
        goal = new StructureTerm(SymbolTerm.intern(":", 2), new Term[]{
          SymbolTerm.intern(Prolog.BUILTIN),
          SymbolTerm.create("cafeteria")});
      }

      p.setReductionLimit(reductionLimit);
      p.execute(Prolog.BUILTIN, "call", goal);
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

  private static void initializePackages(BlockingPrologControl p, Term goal) {
    LinkedHashSet<String> set = new LinkedHashSet<>(3);
    set.add(Prolog.BUILTIN);
    set.add("user");
    if (goal != null) {
      set.add(goal.arg(1).name());
    }

    List<String> list = new ArrayList<>(set);
    Term done = SymbolTerm.intern("true");
    Term head = Prolog.Nil;
    for (int i = list.size() - 1; 0 <= i; i--) {
      head = new ListTerm(SymbolTerm.intern(list.get(i)), head);
    }
    p.execute(Prolog.BUILTIN, "initialization", head, done);
  }

  private static void usage() {
    PrintStream e = System.err;
    e.println("usage:  java -jar cafeteria.jar [options] [goal]");
    e.println();
    e.println("  --enable-io           : enable file system access");
    e.println("  --enable-statistics   : enable statistics/2");
    e.println("  --max-database-size=N : maximum entries in dynamic database");
    e.println("  --reduction-limit=N   : max reductions during execution");
    e.println();
    e.println("   -f source.pl         : load file.pl  (may be repeated)");
    e.println();
    e.println("  goal :          predicate or package:predicate");
    e.println("                  (if not specified, runs interactive loop)");
  }
}
