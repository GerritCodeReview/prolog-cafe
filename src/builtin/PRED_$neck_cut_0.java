package com.googlecode.prolog_cafe.builtin;
import com.googlecode.prolog_cafe.lang.*;
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

    @Override
    public Operation exec(Prolog engine) {
	//        engine.setB0(); 
        engine.neckCut();
        return cont;
    }
}
