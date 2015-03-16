package com.googlecode.prolog_cafe.builtin;
import com.googlecode.prolog_cafe.lang.IntegerTerm;
import com.googlecode.prolog_cafe.lang.Operation;
import com.googlecode.prolog_cafe.lang.Predicate;
import com.googlecode.prolog_cafe.lang.Prolog;
import com.googlecode.prolog_cafe.lang.SymbolTerm;
import com.googlecode.prolog_cafe.lang.Term;

public final class PRED_$compiled_predicate_3 extends Predicate.P3 {
    public PRED_$compiled_predicate_3(Term a1, Term a2, Term a3, Operation cont) {
        this.arg1 = a1;
        this.arg2 = a2;
        this.arg3 = a3;
        this.cont = cont;
    }

    @Override
    public Operation exec(Prolog engine) {
        engine.setB0();
        Term a1 = arg1.dereference();
        Term a2 = arg2.dereference();
        Term a3 = arg3.dereference();
        if(! engine.pcl.definedPredicate(((SymbolTerm)a1).name(), ((SymbolTerm)a2).name(), ((IntegerTerm)a3).intValue()))
          return engine.fail();
        return cont;
    }
}
