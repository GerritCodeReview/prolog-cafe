import java.awt.*;
import jp.ac.kobe_u.cs.prolog.lang.*;
/**
 * <code>PrologFrame</code><br>
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 */
public abstract class PrologFrame extends Frame implements Runnable {
    PrologControlPanel control = null; 
    Thread frameThread = null;
    PrologControl prolog = null;

    public PrologFrame() {
	frameThread = new Thread(this);
	frameThread.start();
    }

    void sendArgument() {}

    void receiveResult() {}

    void aborted() {}

    void quit() {
	System.exit(0);
    }

    public void run() {
	try {
	    Thread.sleep(500);
	} catch (InterruptedException e) {}
	setVisible(true);
	while (Thread.currentThread() == frameThread) {
	    //	    repaint();
	    try {
		Thread.sleep(500);
	    } catch (InterruptedException e) {}
	    int state = control.getState();
	    if (state == control.ABORT) {
		aborted();
	    } else if (state == control.QUIT) {
		break;
	    }
	}
	control.close();
	quit();
    }
}
