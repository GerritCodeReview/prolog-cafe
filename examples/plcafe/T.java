import jp.ac.kobe_u.cs.prolog.lang.*;
/**
 * A sample program for multi-thread execution.<br>
 * Usage: <br>
 * <pre>
 *       % plcafe -cp queens.jar T
 * </pre>
 *
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 */
public class T {
    public static void main(String args[]) {
	long t = System.currentTimeMillis();
	boolean r1 = true;
	boolean r2 = true;
	Term a1[] = {new IntegerTerm(10), new VariableTerm()};
	Term a2[] = {new IntegerTerm(8), new VariableTerm()};

	PrologControl e1 = new PrologControl();
	PrologControl e2 = new PrologControl();
	Term v1 = new VariableTerm();
	Term v2 = new VariableTerm();
	e1.setPredicate(new PRED_queens_2(), a1);
	e2.setPredicate(new PRED_queens_2(), a2);
	System.out.println("Start");
	e1.start();
	e2.start();
	while (r1 || r2) {
	    try {
		Thread.sleep(10);
	    } catch (InterruptedException e) {}
	    if (r1 && e1.ready()) {
		r1 = e1.next();
		if (r1) {
		    System.out.println("Success1 = " + a1[1]);
		    e1.cont();
		} else {
		    System.out.println("Fail1");
		}
	    } else if (r2 && e2.ready()) {
		r2 = e2.next();
		if (r2) {
		    System.out.println("Success2 = " + a2[1]);
		    e2.cont();
		} else {
		    System.out.println("Fail2");
		}
	    } else {
		System.out.println("Waiting");
		try {
		    Thread.sleep(100);
		} catch (InterruptedException e) {}
	    }
	}
	System.out.println("End");
	long t1 = System.currentTimeMillis();
	long t2 = t1 - t;
	System.out.println("time = " + t2 + "msec.");
	
    }
}
