package jp.ac.kobe_u.cs.prolog.builtin;
import jp.ac.kobe_u.cs.prolog.lang.*;
/**
 <code>'$get_level'/1</code><br>
 @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 @version 1.0
*/
public class PRED_$get_level_1 extends Predicate {

    public Term arg1;

    public PRED_$get_level_1(Term a1, Operation cont) {
        arg1 = a1;
        this.cont = cont;
    }

    public String toString() {
        return "$get_level(" + arg1 + ")";
    }

    public Operation exec(Prolog engine) {
	//        engine.setB0(); 
	Term a1;
        a1 = arg1;
        if (! a1.unify(new IntegerTerm(engine.B0), engine.trail)) {
            return engine.fail();
        }
        return cont;
    }
}
