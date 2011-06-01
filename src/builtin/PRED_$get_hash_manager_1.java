package jp.ac.kobe_u.cs.prolog.builtin;
import jp.ac.kobe_u.cs.prolog.lang.*;
/**
   <code>'$get_hash_manager'/1</code><br>
   @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
   @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
   @version 1.0
*/
class PRED_$get_hash_manager_1 extends Predicate {

    public Term arg1;

    public PRED_$get_hash_manager_1(Term a1, Operation cont) {
        arg1 = a1;
        this.cont = cont;
    }

    public String toString() {
        return "$get_hash_manager(" + arg1 + ")";
    }

    public Operation exec(Prolog engine) {
        engine.setB0();
        Term a1;
        a1 = arg1;

	a1 = a1.dereference();
	if (! a1.isVariable())
	    throw new IllegalTypeException(this, 1, "variable", a1);
	if (! a1.unify(new JavaObjectTerm(engine.getHashManager()), engine.trail))
	    return engine.fail();
        return cont;
    }
}
