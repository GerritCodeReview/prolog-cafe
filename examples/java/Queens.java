import java.awt.*;
import jp.ac.kobe_u.cs.prolog.lang.*;
/**
 * <code>Queens</code><br>
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 */
public class Queens extends PrologFrame {
    private QueensPanel queensPanel;
    private TextField N_Field;
    private int N = 8;
  
    public Queens() {
	setSize(500,400);
        setBackground(Color.lightGray);
	setLayout(new BorderLayout());
	add("North", new Label("N-Queens Solver", Label.CENTER));
	queensPanel = new QueensPanel(N);
	add("Center", queensPanel);
	Panel p = new Panel();
	add("South", p);
	p.add(new Label("N="));
	N_Field = new TextField(Integer.toString(N), 2);
	p.add(N_Field);
	control = new PrologControlPanel(this);
	p.add(control);
	validate();
    }

    Term a1 = null;
    Term a2 = null;
    Predicate nqueens = null;
    
    void sendArgument() {
	try {
	    N = Integer.parseInt(N_Field.getText());
	} catch (NumberFormatException e) {
	    N = 1;
	    N_Field.setText(Integer.toString(N));
	}
	queensPanel.setN(N);
	this.prolog = new PrologControl();
	a1 = new IntegerTerm(N);
	a2 = new VariableTerm();
	Term[] args = {a1,a2};
	nqueens = new PRED_queens_2();
	prolog.setPredicate(nqueens, args);
    }

    void receiveResult() {
	Term result = a2.dereference();
	if ( !result.isList() || ((ListTerm)result).length() != N ){
	    System.out.println("Invalid result!");
	    return;
	}
	int queens[] = new int[N];
	Term elm;
	try {
	    for (int i = 0; i < N; i++) {
		elm = ((ListTerm)result).car().dereference();
		queens[i] = ((IntegerTerm)elm).intValue() - 1;
		result = ((ListTerm)result).cdr();
		/* System.out.println(queens[i]); */
	    }
	} catch (Exception e){}
	queensPanel.setQueens(queens);
    }

    public static void main(String args[]) {
	new Queens();
    }
}

class QueensPanel extends Panel {
    private int N = 0;
    private int queens[] = null;
    private int cellSize = 0;
    private Image boardImage = null;
    private int boardN = 0;

    public QueensPanel(int n) {
	setN(n);
    }

    public void setN(int n) {
	if (n > 0) {
	    N = n;
	    setQueens(null);
	    repaint();
	}
    }

    public void setQueens(int q[]) {
	queens = q;
	repaint();
    }

    private void drawBoard(Graphics g, int x0, int y0) {
	int size = N * cellSize;
	if (boardImage == null || N != boardN
	    || boardImage.getWidth(null) != size
	    || boardImage.getHeight(null) != size) {
	    boardImage = createImage(size, size);
	    Graphics b = boardImage.getGraphics();
	    b.setColor(Color.white);
	    b.fillRect(0, 0, size, size);
	    b.setColor(Color.gray);
	    for (int i = 0; i < N; i++)
		for (int j = 0; j < N; j++)
		    if ((i + j) % 2 == 1) {
			b.fillRect(i*cellSize, j*cellSize, cellSize, cellSize);
		    }
	    boardN = N;
	}
	g.drawImage(boardImage, x0, y0, null);
    }
	    
    public void paint(Graphics g) {
	if (N < 1)
	    return;
	int w = getSize().width;
	int h = getSize().height;
	cellSize = Math.min(w, h) / N;
	int x0 = (w - N * cellSize) / 2;
	int y0 = (h - N * cellSize) / 2;
	drawBoard(g, x0, y0);
	if (queens == null || N != queens.length)
	    return;
	int d = (8*cellSize)/10;
	g.setColor(Color.orange);
	for (int i = 0; i < N; i++) {
	    int j = queens[i];
	    int x = x0 + i * cellSize + (cellSize - d) / 2;
	    int y = y0 + j * cellSize + (cellSize - d) / 2;
	    g.fillArc(x, y, d, d, 0, 360);
	}
    }
}


