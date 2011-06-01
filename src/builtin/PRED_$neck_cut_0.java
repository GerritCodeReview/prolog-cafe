package jp.ac.kobe_u.cs.prolog.builtin;
import jp.ac.kobe_u.cs.prolog.lang.*;
/**
 <code>'$neck_cut'/0</code><br>
 @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 @version 1.0
*/
public class PRED_$neck_cut_0 extends Predicate {

    public PRED_$neck_cut_0(Operation cont) {
        this.cont = cont;
    }

    public String toString() {
        return "$neck_cut";
    }

    public Operation exec(Prolog engine) {
	//        engine.setB0(); 
        engine.neckCut();
        return cont;
    }
}
