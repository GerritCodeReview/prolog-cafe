// Copyright 2011 Google Inc. All Rights Reserved.

package com.googlecode.prolog_cafe.lang;

/**
 * Choice point frame.
 *
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
 */
class ChoicePointFrame {
    ChoicePointFrame prior;

    long timeStamp;
    Operation cont;  // continuation goal
    Operation bp;    // next clause
    int tr;          // trail pointer
    int b0;          // cut point

    static ChoicePointFrame S0(Operation cont) {
      ChoicePointFrame r = new ChoicePointFrame();
      r.cont = cont;
      return r;
    }

    void restore(Prolog engine) {
      engine.cont = this.cont;
    }

    public String toString() {
      String t = " time:" + timeStamp + "\n" ;
      t = t + " cont:" + cont + "\n";
      t = t + " bp:" + bp + "\n";
      t = t + " tr:" + tr + "\n";
      t = t + " b0:" + b0 + "\n";
      return t;
    }

    static final class S1 extends ChoicePointFrame {
      private Term areg1;

      S1(Prolog engine) {
        this.cont = engine.cont;
        this.areg1 = engine.areg1;
      }

      void restore(Prolog engine) {
        engine.cont = this.cont;
        engine.areg1 = this.areg1;
      }
    }

    static final class S2 extends ChoicePointFrame {
      private Term areg1, areg2;

      S2(Prolog engine) {
        this.cont = engine.cont;
        this.areg1 = engine.areg1;
        this.areg2 = engine.areg2;
      }

      void restore(Prolog engine) {
        engine.cont = this.cont;
        engine.areg1 = this.areg1;
        engine.areg2 = this.areg2;
      }
    }

    static final class S3 extends ChoicePointFrame {
      private Term areg1, areg2, areg3;

      S3(Prolog engine) {
        this.cont = engine.cont;
        this.areg1 = engine.areg1;
        this.areg2 = engine.areg2;
        this.areg3 = engine.areg3;
      }

      void restore(Prolog engine) {
        engine.cont = this.cont;
        engine.areg1 = this.areg1;
        engine.areg2 = this.areg2;
        engine.areg3 = this.areg3;
      }
    }

    static final class S4 extends ChoicePointFrame {
      private Term areg1, areg2, areg3, areg4;

      S4(Prolog engine) {
        this.cont = engine.cont;
        this.areg1 = engine.areg1;
        this.areg2 = engine.areg2;
        this.areg3 = engine.areg3;
        this.areg4 = engine.areg4;
      }

      void restore(Prolog engine) {
        engine.cont = this.cont;
        engine.areg1 = this.areg1;
        engine.areg2 = this.areg2;
        engine.areg3 = this.areg3;
        engine.areg4 = this.areg4;
      }
    }

    static final class S5 extends ChoicePointFrame {
      private Term areg1, areg2, areg3, areg4, areg5;

      S5(Prolog engine) {
        this.cont = engine.cont;
        this.areg1 = engine.areg1;
        this.areg2 = engine.areg2;
        this.areg3 = engine.areg3;
        this.areg4 = engine.areg4;
        this.areg5 = engine.areg5;
      }

      void restore(Prolog engine) {
        engine.cont = this.cont;
        engine.areg1 = this.areg1;
        engine.areg2 = this.areg2;
        engine.areg3 = this.areg3;
        engine.areg4 = this.areg4;
        engine.areg5 = this.areg5;
      }
    }

    static final class S6 extends ChoicePointFrame {
      private Term areg1, areg2, areg3, areg4, areg5, areg6;

      S6(Prolog engine) {
        this.cont = engine.cont;
        this.areg1 = engine.areg1;
        this.areg2 = engine.areg2;
        this.areg3 = engine.areg3;
        this.areg4 = engine.areg4;
        this.areg5 = engine.areg5;
        this.areg6 = engine.areg6;
      }

      void restore(Prolog engine) {
        engine.cont = this.cont;
        engine.areg1 = this.areg1;
        engine.areg2 = this.areg2;
        engine.areg3 = this.areg3;
        engine.areg4 = this.areg4;
        engine.areg5 = this.areg5;
        engine.areg6 = this.areg6;
      }
    }

    static final class S7 extends ChoicePointFrame {
      private Term areg1, areg2, areg3, areg4, areg5, areg6, areg7;

      S7(Prolog engine) {
        this.cont = engine.cont;
        this.areg1 = engine.areg1;
        this.areg2 = engine.areg2;
        this.areg3 = engine.areg3;
        this.areg4 = engine.areg4;
        this.areg5 = engine.areg5;
        this.areg6 = engine.areg6;
        this.areg7 = engine.areg7;
      }

      void restore(Prolog engine) {
        engine.cont = this.cont;
        engine.areg1 = this.areg1;
        engine.areg2 = this.areg2;
        engine.areg3 = this.areg3;
        engine.areg4 = this.areg4;
        engine.areg5 = this.areg5;
        engine.areg6 = this.areg6;
        engine.areg7 = this.areg7;
      }
    }

    static class S8 extends ChoicePointFrame {
      private Term areg1, areg2, areg3, areg4, areg5, areg6, areg7, areg8;

      S8(Prolog engine) {
        this.cont = engine.cont;
        this.areg1 = engine.areg1;
        this.areg2 = engine.areg2;
        this.areg3 = engine.areg3;
        this.areg4 = engine.areg4;
        this.areg5 = engine.areg5;
        this.areg6 = engine.areg6;
        this.areg7 = engine.areg7;
        this.areg8 = engine.areg8;
      }

      void restore(Prolog engine) {
        engine.cont = this.cont;
        engine.areg1 = this.areg1;
        engine.areg2 = this.areg2;
        engine.areg3 = this.areg3;
        engine.areg4 = this.areg4;
        engine.areg5 = this.areg5;
        engine.areg6 = this.areg6;
        engine.areg7 = this.areg7;
        engine.areg8 = this.areg8;
      }
    }

    static final class S9 extends S8 {
      private Term[] aregs;

      S9(int arity, Prolog engine) {
        super(engine);
        aregs = new Term[arity - 8];
        System.arraycopy(engine.aregs, 0, aregs, 0, aregs.length);
      }

      void restore(Prolog engine) {
        System.arraycopy(aregs, 0, engine.aregs, 0, aregs.length);
        super.restore(engine);
      }
    }
}
