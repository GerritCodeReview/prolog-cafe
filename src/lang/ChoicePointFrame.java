package com.googlecode.prolog_cafe.lang;

/**
 * Choice point frame.
 *
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
 */
public abstract class ChoicePointFrame {
    ChoicePointFrame prior;

    long timeStamp;
    Operation cont;  // continuation goal
    Operation bp;    // next clause
    int tr;          // trail pointer
    int b0;          // cut point

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

    public static final class S0 extends ChoicePointFrame {
    }

    public static final class S1 extends ChoicePointFrame {
      private final Term r1;

      public S1(Prolog engine) {
        this.r1 = engine.r1;
      }

      void restore(Prolog engine) {
        engine.cont = this.cont;
        engine.r1 = this.r1;
      }
    }

    public static final class S2 extends ChoicePointFrame {
      private final Term r1, r2;

      public S2(Prolog engine) {
        this.r1 = engine.r1;
        this.r2 = engine.r2;
      }

      void restore(Prolog engine) {
        engine.r1 = this.r1;
        engine.r2 = this.r2;
      }
    }

    public static final class S3 extends ChoicePointFrame {
      private final Term r1, r2, r3;

      public S3(Prolog engine) {
        this.r1 = engine.r1;
        this.r2 = engine.r2;
        this.r3 = engine.r3;
      }

      void restore(Prolog engine) {
        engine.cont = this.cont;
        engine.r1 = this.r1;
        engine.r2 = this.r2;
        engine.r3 = this.r3;
      }
    }

    public static final class S4 extends ChoicePointFrame {
      private final Term r1, r2, r3, r4;

      public S4(Prolog engine) {
        this.r1 = engine.r1;
        this.r2 = engine.r2;
        this.r3 = engine.r3;
        this.r4 = engine.r4;
      }

      void restore(Prolog engine) {
        engine.cont = this.cont;
        engine.r1 = this.r1;
        engine.r2 = this.r2;
        engine.r3 = this.r3;
        engine.r4 = this.r4;
      }
    }

    public static final class S5 extends ChoicePointFrame {
      private final Term r1, r2, r3, r4, r5;

      public S5(Prolog engine) {
        this.r1 = engine.r1;
        this.r2 = engine.r2;
        this.r3 = engine.r3;
        this.r4 = engine.r4;
        this.r5 = engine.r5;
      }

      void restore(Prolog engine) {
        engine.cont = this.cont;
        engine.r1 = this.r1;
        engine.r2 = this.r2;
        engine.r3 = this.r3;
        engine.r4 = this.r4;
        engine.r5 = this.r5;
      }
    }

    public static final class S6 extends ChoicePointFrame {
      private final Term r1, r2, r3, r4, r5, r6;

      public S6(Prolog engine) {
        this.r1 = engine.r1;
        this.r2 = engine.r2;
        this.r3 = engine.r3;
        this.r4 = engine.r4;
        this.r5 = engine.r5;
        this.r6 = engine.r6;
      }

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

    public static final class S7 extends ChoicePointFrame {
      private final Term r1, r2, r3, r4, r5, r6, r7;

      public S7(Prolog engine) {
        this.r1 = engine.r1;
        this.r2 = engine.r2;
        this.r3 = engine.r3;
        this.r4 = engine.r4;
        this.r5 = engine.r5;
        this.r6 = engine.r6;
        this.r7 = engine.r7;
      }

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

    public static class S8 extends ChoicePointFrame {
      private final Term r1, r2, r3, r4, r5, r6, r7, r8;

      public S8(Prolog engine) {
        this.r1 = engine.r1;
        this.r2 = engine.r2;
        this.r3 = engine.r3;
        this.r4 = engine.r4;
        this.r5 = engine.r5;
        this.r6 = engine.r6;
        this.r7 = engine.r7;
        this.r8 = engine.r8;
      }

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

    public static class S9 extends ChoicePointFrame {
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

    public static class S10 extends ChoicePointFrame {
      private final Term r1, r2, r3, r4, r5, r6, r7, r8, r9, r10;

      public S10(Prolog engine) {
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
