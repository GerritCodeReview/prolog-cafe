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

    void restore(Prolog engine) {
      engine.cont = this.cont;
    }

    @Override
    public String toString() {
      String t = " time:" + timeStamp + "\n" ;
      t = t + " cont:" + cont + "\n";
      t = t + " bp:" + bp + "\n";
      t = t + " tr:" + tr + "\n";
      t = t + " b0:" + b0 + "\n";
      return t;
    }

    static final class S1 extends ChoicePointFrame {
      private final Term r1;

      S1(Prolog engine) {
        this.r1 = engine.r1;
      }

      @Override
      void restore(Prolog engine) {
        engine.cont = this.cont;
        engine.r1 = this.r1;
      }
    }

    static final class S2 extends ChoicePointFrame {
      private final Term r1, r2;

      S2(Prolog engine) {
        this.r1 = engine.r1;
        this.r2 = engine.r2;
      }

      @Override
      void restore(Prolog engine) {
        engine.cont = this.cont;
        engine.r1 = this.r1;
        engine.r2 = this.r2;
      }
    }

    static final class S3 extends ChoicePointFrame {
      private final Term r1, r2, r3;

      S3(Prolog engine) {
        this.r1 = engine.r1;
        this.r2 = engine.r2;
        this.r3 = engine.r3;
      }

      @Override
      void restore(Prolog engine) {
        engine.cont = this.cont;
        engine.r1 = this.r1;
        engine.r2 = this.r2;
        engine.r3 = this.r3;
      }
    }

    static final class S4 extends ChoicePointFrame {
      private final Term r1, r2, r3, r4;

      S4(Prolog engine) {
        this.r1 = engine.r1;
        this.r2 = engine.r2;
        this.r3 = engine.r3;
        this.r4 = engine.r4;
      }

      @Override
      void restore(Prolog engine) {
        engine.cont = this.cont;
        engine.r1 = this.r1;
        engine.r2 = this.r2;
        engine.r3 = this.r3;
        engine.r4 = this.r4;
      }
    }

    static final class S5 extends ChoicePointFrame {
      private final Term r1, r2, r3, r4, r5;

      S5(Prolog engine) {
        this.r1 = engine.r1;
        this.r2 = engine.r2;
        this.r3 = engine.r3;
        this.r4 = engine.r4;
        this.r5 = engine.r5;
      }

      @Override
      void restore(Prolog engine) {
        engine.cont = this.cont;
        engine.r1 = this.r1;
        engine.r2 = this.r2;
        engine.r3 = this.r3;
        engine.r4 = this.r4;
        engine.r5 = this.r5;
      }
    }

    static final class S6 extends ChoicePointFrame {
      private final Term r1, r2, r3, r4, r5, r6;

      S6(Prolog engine) {
        this.r1 = engine.r1;
        this.r2 = engine.r2;
        this.r3 = engine.r3;
        this.r4 = engine.r4;
        this.r5 = engine.r5;
        this.r6 = engine.r6;
      }

      @Override
      void restore(Prolog engine) {
        engine.cont = this.cont;
        engine.r1 = this.r1;
        engine.r2 = this.r2;
        engine.r3 = this.r3;
        engine.r4 = this.r4;
        engine.r5 = this.r5;
        engine.r6 = this.r6;
      }
    }

    static final class S7 extends ChoicePointFrame {
      private final Term r1, r2, r3, r4, r5, r6, r7;

      S7(Prolog engine) {
        this.r1 = engine.r1;
        this.r2 = engine.r2;
        this.r3 = engine.r3;
        this.r4 = engine.r4;
        this.r5 = engine.r5;
        this.r6 = engine.r6;
        this.r7 = engine.r7;
      }

      @Override
      void restore(Prolog engine) {
        engine.cont = this.cont;
        engine.r1 = this.r1;
        engine.r2 = this.r2;
        engine.r3 = this.r3;
        engine.r4 = this.r4;
        engine.r5 = this.r5;
        engine.r6 = this.r6;
        engine.r7 = this.r7;
      }
    }

    static class S8 extends ChoicePointFrame {
      private final Term r1, r2, r3, r4, r5, r6, r7, r8;

      S8(Prolog engine) {
        this.r1 = engine.r1;
        this.r2 = engine.r2;
        this.r3 = engine.r3;
        this.r4 = engine.r4;
        this.r5 = engine.r5;
        this.r6 = engine.r6;
        this.r7 = engine.r7;
        this.r8 = engine.r8;
      }

      @Override
      void restore(Prolog engine) {
        engine.cont = this.cont;
        engine.r1 = this.r1;
        engine.r2 = this.r2;
        engine.r3 = this.r3;
        engine.r4 = this.r4;
        engine.r5 = this.r5;
        engine.r6 = this.r6;
        engine.r7 = this.r7;
        engine.r8 = this.r8;
      }
    }

    static class S9 extends ChoicePointFrame {
      private final Term r1, r2, r3, r4, r5, r6, r7, r8, r9;

      S9(Prolog engine) {
        this.r1 = engine.r1;
        this.r2 = engine.r2;
        this.r3 = engine.r3;
        this.r4 = engine.r4;
        this.r5 = engine.r5;
        this.r6 = engine.r6;
        this.r7 = engine.r7;
        this.r8 = engine.r8;
        this.r9 = engine.r9;
      }

      @Override
      void restore(Prolog engine) {
        engine.cont = this.cont;
        engine.r1 = this.r1;
        engine.r2 = this.r2;
        engine.r3 = this.r3;
        engine.r4 = this.r4;
        engine.r5 = this.r5;
        engine.r6 = this.r6;
        engine.r7 = this.r7;
        engine.r8 = this.r8;
        engine.r9 = this.r9;
      }
    }

    static class S10 extends ChoicePointFrame {
      private final Term r1, r2, r3, r4, r5, r6, r7, r8, r9, r10;

      S10(Prolog engine) {
        this.r1 = engine.r1;
        this.r2 = engine.r2;
        this.r3 = engine.r3;
        this.r4 = engine.r4;
        this.r5 = engine.r5;
        this.r6 = engine.r6;
        this.r7 = engine.r7;
        this.r8 = engine.r8;
        this.r9 = engine.r9;
        this.r10 = engine.r10;
      }

      @Override
      void restore(Prolog engine) {
        engine.cont = this.cont;
        engine.r1 = this.r1;
        engine.r2 = this.r2;
        engine.r3 = this.r3;
        engine.r4 = this.r4;
        engine.r5 = this.r5;
        engine.r6 = this.r6;
        engine.r7 = this.r7;
        engine.r8 = this.r8;
        engine.r9 = this.r9;
        engine.r10 = this.r10;
      }
    }
}
