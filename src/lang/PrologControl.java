package com.googlecode.prolog_cafe.lang;

import java.util.Set;

/**
 * Tracks current evaluation goal and results.
 * <p>
 * On success/1 or fail/1 the corresponding methods in this class are invoked,
 * allowing the implementation to message the results to the application. During
 * any reduction loops the {@link #isEngineStopped()} method is consulted to
 * determine if the loop should terminate.
 *
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.2
 */
public abstract class PrologControl {
    /** Holds a Prolog engine. */
    protected Prolog engine;

    /** Holds a Prolog goal to be executed. */
    protected Operation code;

    /** Constructs a new <code>PrologControl</code>. */
    public PrologControl() {
      engine = new Prolog(this);
    }

    /** Constructs a new <code>PrologControl</code>. */
    public PrologControl(PrologMachineCopy pmc) {
      engine = new Prolog(this, pmc);
    }

    public boolean isEnabled(Prolog.Feature f) {
      return engine.features.contains(f);
    }

    public void setEnabled(Prolog.Feature f, boolean on) {
      if (on)
        engine.features.add(f);
      else
        engine.features.remove(f);
    }

    public void setEnabled(Set<Prolog.Feature> f, boolean on) {
      if (on)
        engine.features.addAll(f);
      else
        engine.features.removeAll(f);
    }

    public int getMaxDatabaseSize() {
      if (engine.internalDB != null)
        return engine.internalDB.maxContents;
      return InternalDatabase.DEFAULT_SIZE;
    }
    public void setMaxDatabaseSize(int size) {
      if (engine.aregs != null)
        throw new IllegalStateException("Prolog already initialized");
      if (engine.internalDB != null)
        engine.internalDB.maxContents = size;
      else
        engine.internalDB = new InternalDatabase(size);
    }

    public PrologClassLoader getPrologClassLoader() {
      if (engine.pcl == null)
        engine.pcl = new PrologClassLoader();
      return engine.pcl;
    }
    public void setPrologClassLoader(PrologClassLoader cl) {
      if (engine.aregs != null)
        throw new IllegalStateException("Prolog already initialized");
      engine.pcl = cl;
    }

    public int getMaxArity() { return engine.getMaxArity(); }
    public void setMaxArity(int max) {
      if (max < 8)
        throw new IllegalStateException("invalid arity " + max);
      if (engine.aregs != null)
        throw new IllegalStateException("Prolog already initialized");
      engine.maxArity = max;
    }

    /** Sets a goal and its arguments to this Prolog thread. 
     * An initial continuation goal (a <code>Success</code> object)
     * is set to the <code>cont</code> field of goal <code>p</code> as continuation.
     */
    public void setPredicate(Predicate p) {
      p.cont = Success.SUCCESS;
      code = p;
    }

    /** Sets a goal <code>call(t)</code> to this Prolog thread. 
     * An initial continuation goal (a <code>Success</code> object)
     * is set to the <code>cont</code> field of goal <code>p</code> as continuation.
     */
    public void setPredicate(String pkg, String functor, Term... args) {
      setPredicate(getPrologClassLoader().predicate(pkg, functor, args));
    }

    /** Sets a goal <code>call(t)</code> to this Prolog thread.
     * An initial continuation goal (a <code>Success</code> object)
     * is set to the <code>cont</code> field of <code>call(t)</code> as continuation.
     */
    public void setPredicate(Term t)  {
      setPredicate(Prolog.BUILTIN, "call", t);
    }

    /**
     * Is invoked when the system succeeds to find a solution.<br>
     *
     * This method is invoked from the initial continuation goal
     * (a <code>Success</code> object).
     */
    protected abstract void success();

    /** Is invoked after failure of all trials. */
    protected abstract void fail();

    /**
     * Check if evaluation should continue.
     * <p>
     * This method gets invoked on every predicate reduction. If the control class
     * wants to halt execution (for example sufficient results were obtained, or a
     * limit on running time or reduction count has been reached) the method must
     * return true to stop execution.
     *
     * @return true if the engine is no longer supposed to execute; false if
     *         another predicate reduction can take place.
     */
    public abstract boolean isEngineStopped();

    /**
     * Execute the predicate on the current thread.
     * <p>
     * This method does not return until {@link #isEngineStopped()} returns true.
     * Implementors of the class are expected to invoke this method to perform
     * evaluation, and terminate out of the loop at the proper time based on an
     * invocation to {@link #success()} or {@link #fail()}.
     *
     * @throws PrologException
     */
    protected void executePredicate() throws PrologException {
      Prolog engine = this.engine;
      Operation code = this.code;
      try {
        engine.init();

        do {
          if (isEngineStopped()) return;
          code = code.exec(engine);
        } while (engine.halt == 0);

        if (engine.halt != 1) {
            throw new HaltException(engine.halt - 1);
        }
      } finally {
        this.code = code;
        SymbolTerm.gc();
      }
    }

    /** @param err stack trace to print (or log). */
    public void printStackTrace(Throwable err) {
      err.printStackTrace();
    }
}
