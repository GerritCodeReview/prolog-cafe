package jp.ac.kobe_u.cs.prolog.builtin;
import jp.ac.kobe_u.cs.prolog.lang.*;
import java.util.Hashtable;
import java.util.Enumeration;
/**
   <code>hash_keys/2</code><br>
   @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
   @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
   @version 1.0
*/
public class PRED_hash_keys_2 extends Predicate.P2 {
    private static final SymbolTerm SYM_NIL = SymbolTerm.makeSymbol("[]");

    public PRED_hash_keys_2(Term a1, Term a2, Operation cont) {
        arg1 = a1;
        arg2 = a2;
        this.cont = cont;
    }

    public String toString() {
        return "hash_keys(" + arg1 + "," + arg2 + ")";
    }

    public Operation exec(Prolog engine) {
        engine.setB0();
        Term a1, a2;
        a1 = arg1;
        a2 = arg2;

	Object hash = null;

	a1 = a1.dereference();
	if (a1.isVariable()) {
	    throw new PInstantiationException(this, 1);
	} else if (a1.isSymbol()) {
	    if (! engine.getHashManager().containsKey(a1))
		throw new ExistenceException(this, 1, "hash", a1, "");
	    hash = ((JavaObjectTerm) engine.getHashManager().get(a1)).object();
	} else if (a1.isJavaObject()) {
	    hash = ((JavaObjectTerm) a1).object();
	} else {
	    throw new IllegalDomainException(this, 1, "hash_or_alias", a1);
	}
	if (! (hash instanceof HashtableOfTerm))
	    throw new InternalException(this + ": Hash is not HashtableOfTerm");
	Term keys = SYM_NIL;
	for (Term t : ((HashtableOfTerm) hash).keySet())
	    keys = new ListTerm(t, keys);
	if (! a2.unify(keys, engine.trail))
	    return engine.fail();
        return cont;
    }
}
