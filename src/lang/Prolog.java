package com.googlecode.prolog_cafe.lang;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.Writer;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
/**
 * Prolog engine.
 *
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.2
*/
public final class Prolog {
    private static final SymbolTerm NONE = SymbolTerm.intern("$none");

    /** Prolog thread */
    public PrologControl control;

    /** Argument registers */
    public Term areg1, areg2, areg3, areg4, areg5, areg6, areg7, areg8;
    public Term[] aregs;
    private static final Term[] NO_REGISTERS = {};

    /** Continuation goal register */
    public Operation cont;
    /** Choice point frame stack */
    public final ChoicePointStack stack;
    /** Trail stack */
    public final Trail trail;
    /** Cut pointer */
    public int B0;
    /** Class loader */
    public PrologClassLoader pcl;
    /** Internal Database */
    public InternalDatabase internalDB;

    /** Current time stamp of choice point frame */
    protected long CPFTimeStamp;

    /**
     * Exception level of continuation passing loop:
     * <li><code>0</code> for no exception,
     * <li><code>1</code> for <code>halt/0</code>,
     * <li><code>1+N</code> for <code>halt(N)</code>.
     * </ul>
     */
    public int halt;

    /** <font color="red">Not supported yet</font>. Prolog implementation flag: <code>bounded</code>. */
    protected boolean bounded = false;
    /** Prolog implementation flag: <code>max_integer</code>. */
    protected static final int maxInteger = Integer.MAX_VALUE;
    /** Prolog implementation flag: <code>min_integer</code>. */
    protected static final int minInteger = Integer.MIN_VALUE;
    /** Prolog implementation flag: <code>integer_rounding_function</code>. */
    protected String integerRoundingFunction = "down";
    /** <font color="red">Not supported yet</font>. Prolog implementation flag: <code>char_conversion</code>. */
    protected String charConversion;
    /** Prolog implementation flag: <code>debug</code>. */
    protected String debug;
    /** Prolog implementation flag: <code>max_arity</code>. */
    protected int maxArity = 255;
    /** Prolog implementation flag: <code>unknown</code>. */
    protected String unknown;
    /** <font color="red">Not supported yet</font>. Prolog implementation flag: <code>double_quotes</code>. */
    protected String doubleQuotes;

    /** Holds an exception term for <code>catch/3</code> and <code>throw/1</code>. */
    protected Term exception;

    /** Holds the start time as <code>long</code> for <code>statistics/2</code>. */
    protected long startRuntime;
    /** Holds the previous time as <code>long</code> for <code>statistics/2</code>. */
    protected long previousRuntime;

    /** Hashtable for creating a copy of term. */
    protected final IdentityHashMap<VariableTerm,VariableTerm> copyHash;

    /** The size of the pushback buffer used for creating input streams. */
    public static final int PUSHBACK_SIZE = 3;

    /** Hashtable for managing input and output streams. */
    protected HashtableOfTerm streamManager;

    /** Hashtable for managing internal databases. */
    protected final HashtableOfTerm hashManager;

    /** Name of the builtin package. */
    public static final String BUILTIN = "com.googlecode.prolog_cafe.builtin";

    /** Holds an atom <code>[]<code> (empty list). */
    public static final SymbolTerm Nil     = SymbolTerm.intern("[]");

    /* Some symbols for stream options */
    static final SymbolTerm SYM_MODE_1     = SymbolTerm.intern("mode", 1);
    static final SymbolTerm SYM_ALIAS_1    = SymbolTerm.intern("alias", 1);
    static final SymbolTerm SYM_TYPE_1     = SymbolTerm.intern("type", 1);
    static final SymbolTerm SYM_READ       = SymbolTerm.intern("read");
    static final SymbolTerm SYM_APPEND     = SymbolTerm.intern("append");
    static final SymbolTerm SYM_INPUT      = SymbolTerm.intern("input");
    static final SymbolTerm SYM_OUTPUT     = SymbolTerm.intern("output");
    static final SymbolTerm SYM_TEXT       = SymbolTerm.intern("text");

    public static enum Feature {
      /** Access to the local filesystem and console. */
      IO,

      /** Track the running time of evaluations */
      STATISTICS_RUNTIME;
    }
    protected final EnumSet<Feature> features = EnumSet.allOf(Feature.class);

    Prolog(PrologControl c) {
      control = c;
      trail = new Trail();
      stack = new ChoicePointStack(trail);
      copyHash = new IdentityHashMap<VariableTerm, VariableTerm>();
      hashManager = new HashtableOfTerm();
    }

    Prolog(PrologControl c, PrologMachineCopy pmc) {
      control = c;
      trail = new Trail();
      stack = new ChoicePointStack(trail);
      copyHash = new IdentityHashMap<VariableTerm, VariableTerm>();
      pcl = pmc.pcl;

      // During restore there is no need to copy terms. clause/2 inside of
      // builtins.pl copies the predicate when it reads from internalDB.
      hashManager = PrologMachineCopy.copyShallow(pmc.hashManager);
      internalDB = new InternalDatabase(this, pmc.internalDB, false);
    }

    /**
     * Initializes some local instances only once.
     * This <code>initOnce</code> method is invoked in the constructor
     * and initializes the following instances:
     * <ul>
     *   <li><code>copyHash</code>
     *   <li><code>streamManager</code>
     * </ul>
     */
  private void initOnce() {
    if (8 < maxArity)
      aregs = new Term[maxArity - 8];
    else
      aregs = NO_REGISTERS;

    if (pcl == null) pcl = new PrologClassLoader();
    if (internalDB == null) internalDB = new InternalDatabase();

    streamManager = new HashtableOfTerm();
  }

    /** Initializes this Prolog engine. */
    public void init() { 
	if (aregs == null)
	  initOnce();
	stack.init();
	trail.init();
	B0 = stack.top();
	CPFTimeStamp = Long.MIN_VALUE;

	// Creates an initial choice point frame.
	ChoicePointFrame initialFrame = ChoicePointFrame.S0(null);
	initialFrame.b0 = B0;
	initialFrame.bp = Failure.FAILURE;
	initialFrame.tr = trail.top();
	initialFrame.timeStamp = ++CPFTimeStamp;
	stack.push(initialFrame);

	halt = 0;

	charConversion  = "off";
	debug           = "off";
	unknown         = "error";
	doubleQuotes    = "codes";

	exception = NONE;
	startRuntime = features.contains(Feature.STATISTICS_RUNTIME)
	    ? System.currentTimeMillis()
	    : 0;
	previousRuntime = 0;
    }

    /** Ensure a feature is enabled, throwing if not. */
    public void requireFeature(Prolog.Feature f, Operation goal, Term arg) {
      if (!features.contains(f)) {
        throw new PermissionException(goal, "use", f.toString().toLowerCase(), arg, "disabled");
      }
    }

    /** Sets B0 to the top of the choice point stack.. */
    public void setB0()    { B0 = stack.top(); }

    /** Discards all choice points after the value of <code>i</code>. */
    public void cut(int i) { stack.cut(i); }

    /** Discards all choice points after the value of <code>B0</code>. */
    public void neckCut()  { stack.cut(B0); }

    /**
     * Returns a copy of term <code>t</code>. 
     * @param t a term to be copied. It must be dereferenced.
     */
    public Term copy(Term t) {
	copyHash.clear();
	return t.copy(this);
    }

    /** 
     * Do backtrak.
     * This method restores the value of <code>B0</code>
     * and returns the backtrak point in current choice point.
     */
    public Operation fail() {
	ChoicePointFrame top = stack.top;
	B0 = top.b0;     // restore B0
	return top.bp;   // execute next clause
    }

    /** 
     * Returns the <code>Predicate</code> object refered, respectively, 
     * <code>var</code>, <code>Int</code>, <code>flo</code>, 
     * <code>con</code>, <code>str</code>, or <code>lis</code>, 
     * depending on whether the dereferenced value of argument 
     * register <code>areg[1]</code> is a
     * variable, integer, float,
     * atom, compound term, or non-empty list, respectively.
     */
    public Operation switch_on_term(Operation var, 
				    Operation Int, 
				    Operation flo,
				    Operation con, 
				    Operation str, 
				    Operation lis) {
	Term arg1 = areg1.dereference();
	if (arg1.isVariable())
	    return var;
	if (arg1.isInteger())
	    return Int;
	if (arg1.isDouble())
	    return flo;
	if (arg1.isSymbol())
	    return con;
	if (arg1.isStructure())
	    return str;
	if (arg1.isList())
	    return lis;
	return var;
    }

    /**
     * If the dereferenced value of arugment register <code>areg[1]</code>
     * is an integer, float, atom, or compound term (except for non-empty list),
     * this returns the <code>Predicate</code> object to which its key is mapped
     * in hashtable <code>hash</code>.
     *
     * The key is calculated as follows:
     * <ul>
     *   <li>integer - itself
     *   <li>float - itself
     *   <li>atom - itself
     *   <li>compound term - functor/arity
     * </ul>
     *
     * If there is no mapping for the key of <code>areg[1]</code>, 
     * this returns <code>otherwise</code>.
     */
    public Operation switch_on_hash(HashMap<Term,Operation> hash, Operation otherwise) {
	Term arg1 = areg1.dereference();
	Term key;
	if (arg1.isInteger() || arg1.isDouble() || arg1.isSymbol()) {
	    key = arg1;
	} else if (arg1.isStructure()) {
	    key = ((StructureTerm) arg1).functor();
	} else {
	    throw new SystemException("Invalid argument in switch_on_hash");
	}
	Operation p = hash.get(key);
	if (p != null)
	    return p;
	else 
	    return otherwise;
    }

    /** Restores the argument registers and continuation goal register from the current choice point frame. */
    public void restore() {
      stack.top.restore(this);
    }

    /** Creates a new choice point frame. */
    public Operation jtry0(Operation p, Operation next) { return finishjtry(p, next, ChoicePointFrame.S0(cont)); }
    public Operation jtry1(Operation p, Operation next) { return finishjtry(p, next, new ChoicePointFrame.S1(this)); }
    public Operation jtry2(Operation p, Operation next) { return finishjtry(p, next, new ChoicePointFrame.S2(this)); }
    public Operation jtry3(Operation p, Operation next) { return finishjtry(p, next, new ChoicePointFrame.S3(this)); }
    public Operation jtry4(Operation p, Operation next) { return finishjtry(p, next, new ChoicePointFrame.S4(this)); }
    public Operation jtry5(Operation p, Operation next) { return finishjtry(p, next, new ChoicePointFrame.S5(this)); }
    public Operation jtry6(Operation p, Operation next) { return finishjtry(p, next, new ChoicePointFrame.S6(this)); }
    public Operation jtry7(Operation p, Operation next) { return finishjtry(p, next, new ChoicePointFrame.S7(this)); }
    public Operation jtry8(Operation p, Operation next) { return finishjtry(p, next, new ChoicePointFrame.S8(this)); }
    public Operation jtry(int arity, Operation p, Operation next) {
	return finishjtry(p, next, new ChoicePointFrame.S9(arity, this));
    }

    private Operation finishjtry(Operation p, Operation next, ChoicePointFrame entry) {
      entry.b0 = B0;
      entry.bp = next;
      entry.tr = trail.top();
      entry.timeStamp = ++CPFTimeStamp;
      stack.push(entry);
      return p;
    }

    /** 
     * Resets all necessary information from the current choice point frame,
     * updates its next clause field to <code>next</code>,
     * and then returns <code>p</code>.
     */
    public Operation retry(Operation p, Operation next) {
	restore();
	ChoicePointFrame top = stack.top;
	trail.unwind(top.tr);
	top.bp = next;
	return p;
    }

    /** 
     * Resets all necessary information from the current choice point frame,
     * discard it, and then returns <code>p</code>.
     */
    public Operation trust(Operation p) {
	restore();
	trail.unwind(stack.top.tr);
	stack.delete();
	return p;
    }

    /** Returns the current time stamp of choice point frame. */
    public long    getCPFTimeStamp() { return CPFTimeStamp; }

    /** Returns the value of Prolog implementation flag: <code>bounded</code>. */
    public boolean isBounded() { return bounded; }

    /** Returns the value of Prolog implementation flag: <code>max_integer</code>. */
    public int getMaxInteger() { return maxInteger; }

    /** Returns the value of Prolog implementation flag: <code>min_integer</code>. */
    public int getMinInteger() { return minInteger; }

    /** Returns the value of Prolog implementation flag: <code>integer_rounding_function</code>. */
    public String getIntegerRoundingFunction() { return integerRoundingFunction; }

    /** Returns the value of Prolog implementation flag: <code>char_conversion</code>. */
    public String getCharConversion() { return charConversion; }
    /** Sets the value of Prolog implementation flag: <code>char_conversion</code>. */
    public void setCharConversion(String mode) { charConversion = mode;}

    /** Returns the value of Prolog implementation flag: <code>debug</code>. */
    public String getDebug() { return debug; }
    /** Sets the value of Prolog implementation flag: <code>debug</code>. */
    public void setDebug(String mode) { debug = mode;}

    /** Returns the value of Prolog implementation flag: <code>max_arity</code>. */
    public int getMaxArity() { return maxArity; }

    /** Returns the value of Prolog implementation flag: <code>unknown</code>. */
    public String getUnknown() { return unknown; }
    /** Sets the value of Prolog implementation flag: <code>unknown</code>. */
    public void setUnknown(String mode) { unknown = mode;}

    /** Returns the value of Prolog implementation flag: <code>double_quotes</code>. */
    public String getDoubleQuotes() { return doubleQuotes; }
    /** Sets the value of Prolog implementation flag: <code>double_quotes</code>. */
    public void setDoubleQuotes(String mode) { doubleQuotes = mode;}

    /** Returns the value of <code>exception</code>. This is used in <code>catch/3</code>. */
    public Term getException() { return exception; }
    /** Sets the value of <code>exception</code>. This is used in <code>throw/1</code>. */
    public void setException(Term t) { exception = t;}

    /** Returns the value of <code>startRuntime</code>. This is used in <code>statistics/2</code>. */
    public long getStartRuntime() { return startRuntime; }

    /** Returns the value of <code>previousRuntime</code>. This is used in <code>statistics/2</code>. */
    public long getPreviousRuntime() { return previousRuntime; }
    /** Sets the value of <code>previousRuntime</code>. This is used in <code>statistics/2</code>. */
    public void setPreviousRuntime(long t) { previousRuntime = t; }

    /** Returns the stream manager. */
    public HashtableOfTerm getStreamManager() { return streamManager; }

    /** Returns the hash manager. */
    public HashtableOfTerm getHashManager() { return hashManager; }
}
