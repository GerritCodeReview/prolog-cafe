import java.awt.*;
import java.awt.event.*;
/**
 * <code>PrologControlPanel</code><br>
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 */
public class PrologControlPanel extends Panel implements ActionListener {
    private PrologFrame prologFrame = null;
    private Button startButton;
    private Button nextButton;
    private Button abortButton;
    private Button quitButton;
    private TextField message;
    private int count = 0;
    private int state;
    public final int START  = 1;
    public final int WAIT   = 2;
    public final int NEXT   = 3;
    public final int DONE   = 4;
    public final int ERROR  = 5;
    public final int QUIT   = 6;

    public PrologControlPanel(PrologFrame _prologFrame) {
	prologFrame = _prologFrame;
	startButton = new Button("Start");
	startButton.addActionListener(this);
	nextButton = new Button("Next");
	nextButton.addActionListener(this);
	abortButton = new Button("Abort");
	abortButton.addActionListener(this);
	quitButton = new Button("Quit");
	quitButton.addActionListener(this);
	message = new TextField(20);
	message.setEditable(false);
	Panel p = new Panel();
	p.add(startButton);
	p.add(nextButton);
	p.add(abortButton);
	p.add(quitButton);
	setLayout(new BorderLayout());
	add("Center", p);
	add("South", message);
	validate();
	setState(START);
    }

    public void close() {
	if (prologFrame.prolog == null) 
	    return;
	prologFrame.prolog.stop();
	prologFrame.prolog = null;
    }

    public synchronized int getState() {
	return state;
    }

    private synchronized void setState(int s) {
	switch (s) {
	case START:
	    startButton.setEnabled(true);
	    startButton.requestFocus();
	    nextButton.setEnabled(false);
	    abortButton.setEnabled(false);
	    quitButton.setEnabled(true);
	    message.setText("");
	    break;
	case WAIT:
	    startButton.setEnabled(false);
	    nextButton.setEnabled(false);
	    abortButton.setEnabled(true);
	    abortButton.requestFocus();
	    quitButton.setEnabled(true);
	    message.setText("Wait");
	    break;
	case NEXT:
	    startButton.setEnabled(true);
	    nextButton.setEnabled(true);
	    nextButton.requestFocus();
	    abortButton.setEnabled(true);
	    quitButton.setEnabled(true);
	    message.setText("Count = " + count);
	    break;
	case DONE:
	    startButton.setEnabled(true);
	    startButton.requestFocus();
	    nextButton.setEnabled(false);
	    abortButton.setEnabled(false);
	    quitButton.setEnabled(true);
	    message.setText("Count = " + count + " (no more)");
	    break;
	case ERROR:
	    startButton.setEnabled(true);
	    nextButton.setEnabled(false);
	    abortButton.setEnabled(false);
	    quitButton.setEnabled(true);
	    quitButton.requestFocus();
	    message.setText("Error");
	    break;
	case QUIT:
	    startButton.setEnabled(false);
	    nextButton.setEnabled(false);
	    abortButton.setEnabled(false);
	    quitButton.setEnabled(false);
	    message.setText("Bye");
	    break;
	}
	state = s;
	repaint();
    }

    public synchronized void actionStart() {
	prologFrame.sendArgument();
	if (prologFrame.prolog == null) 
	    setState(ERROR);
	setState(WAIT);
	if ( prologFrame.prolog.call() ) {
	    prologFrame.receiveResult();
	    count = 1;
	    setState(NEXT);
	} else {
	    close();
	    setState(DONE);
	    count = 0;
	}
    }

    public synchronized void actionNext() {
	if (prologFrame.prolog == null) 
	    setState(ERROR);
	setState(WAIT);
	if ( prologFrame.prolog.redo() ) {
	    prologFrame.receiveResult();
	    count++;
	    setState(NEXT);
	} else {
	    close();
	    setState(DONE);
	    count = 0;
	}
    }

    public synchronized void actionAbort() {
	close();
	setState(START);
    }

    public synchronized void actionQuit() {
	close();
	setState(QUIT);
    }

    public void actionPerformed(ActionEvent e){
	String action = e.getActionCommand();
	if (action.equals("Start"))
	    actionStart();
	else if (action.equals("Next"))
	    actionNext();
	else if (action.equals("Abort"))
	    actionAbort();
	else if (action.equals("Quit"))
	    actionQuit();
	else
	    super.processEvent(e);
    }

    public void paint(Graphics g) {
	paintComponents(g);
    }
}


