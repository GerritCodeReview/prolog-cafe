import java.awt.*;
import jp.ac.kobe_u.cs.prolog.lang.*;
/**
 * <code>Peg</code><br>
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 */
public class Peg extends PrologFrame {
    private PegPanel pegPanel;
    private TextField N_Field;
    private TextField I_Field;
    private TextField J_Field;
    private int N  = 5;
    private int I0 = 5;
    private int J0 = 3;

    public Peg() {
	setSize(500, 400);
	setBackground(Color.lightGray);
	setLayout(new BorderLayout());
	add("North", new Label("Peg", Label.CENTER));
	pegPanel = new PegPanel();
	add("Center", pegPanel);
	Panel p = new Panel();
	add("South", p);
	p.add(new Label("N="));
	N_Field = new TextField(Integer.toString(N), 2);
	p.add(N_Field);
	p.add(new Label("(I, J)=("));
	I_Field = new TextField(Integer.toString(I0), 1);
	p.add(I_Field);
	p.add(new Label(","));
	J_Field = new TextField(Integer.toString(J0), 1);
	p.add(J_Field);
	p.add(new Label(")"));
	control = new PrologControlPanel(this);
	p.add(control);
	validate(); 
    }

    Term a1 = null;
    Term a2 = null;
    Term a3 = null;
    Term a4 = null;
    Predicate peg = null;

    void sendArgument() {
	try {
	    N =  Integer.parseInt(N_Field.getText());
	    I0 = Integer.parseInt(I_Field.getText());
	    J0 = Integer.parseInt(J_Field.getText());
	} catch (NumberFormatException e) {
	    N = 1;
	    N_Field.setText(Integer.toString(N));
	    I0 = 1;
	    I_Field.setText(Integer.toString(I0));
	    J0 = 1;
	    J_Field.setText(Integer.toString(J0));
	}
	this.prolog = new PrologControl();
	a1 = new IntegerTerm(N);
	a2 = new IntegerTerm(I0);
	a3 = new IntegerTerm(J0);
	a4 = new VariableTerm();
	Term[] args = {a1,a2,a3,a4};
	peg = new PRED_peg_game_4();
	prolog.setPredicate(peg, args);
    }

    void receiveResult() {
	Term result = a4.dereference();
	if (! result.isList()) {
	    System.out.println("Invalid result!");
	    return;
	}
	int len = ((ListTerm)result).length();
	int moves[][] = new int[len][4];	
	Term elm;
	Term[] args;
	try {
	    for (int i=0; i<len; i++) {
		elm = ((ListTerm)result).car().dereference();
		args = ((StructureTerm)elm).args();
		moves[i][0] = ((IntegerTerm)(args[0].dereference())).intValue();
		moves[i][1] = ((IntegerTerm)(args[1].dereference())).intValue();
		moves[i][2] = ((IntegerTerm)(args[2].dereference())).intValue();
		moves[i][3] = ((IntegerTerm)(args[3].dereference())).intValue();
		result = ((ListTerm)result).cdr().dereference();
	    }
	    pegPanel.setSol(N, I0, J0, moves);
	} catch (Exception e) {
	    System.out.println("are?");
	}
	pegPanel.start();	// start animation
    }

    public static void main(String args[]) {
	new Peg();
    }
}

class PegPanel extends Panel implements Runnable {
    private Thread pegAnimator = null;
    private int N;
    private int I0;
    private int J0;
    private Color peg[][] = null;
    private int moves[][] = null;
    private int cellSize;
    private int x0;
    private int y0;
    private int wait = 300;

    public synchronized void start() {
	stop();
	pegAnimator = new Thread(this);
	pegAnimator.start();
    }

    public synchronized void stop() {
	pegAnimator = null;
    }

    public synchronized void setSol(int n, int i0, int j0, int m[][]) {
	N = n;
	I0 = i0;
	J0 = j0;
	moves = m;
	peg = null;
	repaint();
    }

    public void paint(Graphics g) {
	int m, n;
	if (peg == null)
	    return;
	/* this.g = g; */
	int w = getSize().width;
	int h = getSize().height;
	cellSize = Math.min(w, h) / N;
	x0 = (w - N * cellSize) / 2;
	y0 = (h - N * cellSize) / 2;
	for (int i = 1; i <= N; ++i)
	    for (int j = 1; j <= i; ++j) {
		g.setColor(peg[i][j]);
		m = i-1;
		n = j-1;
		int x = x0 + (N-1-m)*cellSize/2 + n*cellSize;
		int y = y0 + m*cellSize;
		g.fillOval(x+cellSize/4, y+cellSize/4, cellSize/2, cellSize/2);
	    }
    }

    private void sleep(int t) {
	try {
	    Thread.sleep(t);
	} catch (InterruptedException e) {}
    }

    public void run() {
	if (moves == null)
	    return;
	peg = new Color[N+1][N+1];
	for (int i = 1; i <= N; ++i)
	    for (int j = 1; j <= i; ++j)
		peg[i][j] = Color.blue;
	peg[I0][J0] = Color.white;
	repaint();
	for (int k = 0; k < moves.length; ++k) {
	    sleep(wait);
	    int i0 = moves[k][0];
	    int j0 = moves[k][1];
	    int di = moves[k][2];
	    int dj = moves[k][3];
	    peg[i0][j0] = Color.red;
	    repaint();
	    sleep(wait);
	    peg[i0][j0] = Color.white;
	    peg[i0+di][j0+dj] = Color.white;
	    peg[i0+2*di][j0+2*dj] = Color.blue;
	    repaint();
	    sleep(wait);
	}
    }
}
