package com.googlecode.prolog_cafe.builtin;
import com.googlecode.prolog_cafe.exceptions.IllegalTypeException;
import com.googlecode.prolog_cafe.lang.*;
/**
 <code>'$cut'/1</code><br>
 @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 @version 1.0
*/
public class PRED_$cut_1 extends Predicate.P1 {
    public PRED_$cut_1(Term a1, Operation cont) {
        arg1 = a1;
        this.cont = cont;
    }

    public Operation exec(Prolog engine) {
	//        engine.setB0(); 
        Term a1;
        a1 = arg1;
        a1 = a1.dereference();
        if (! (a1 instanceof IntegerTerm)) {
            throw new IllegalTypeException("integer", a1);
        } else {
            engine.cut(((IntegerTerm) a1).intValue());
        }
        return cont;
    }
}
