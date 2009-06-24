import java.awt.*;
import jp.ac.kobe_u.cs.prolog.lang.*;
/**
 * <code>Pentomino</code><br>
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 */
public class Pentomino extends PrologFrame {
    private PentominoPanel pentominoPanel;
    private TextField M_Field;
    private int M = 4;

    public Pentomino() {
	setSize(500, 400);
        setBackground(Color.lightGray);
	setLayout(new BorderLayout());
	add("North", new Label("Pentominos Solver", Label.CENTER));
	pentominoPanel = new PentominoPanel();
	add("Center", pentominoPanel);
	Panel p = new Panel();
	add("South", p);
	p.add(new Label("Rows="));
	M_Field = new TextField("4", 2);
	p.add(M_Field);
	control = new PrologControlPanel(this);
	p.add(control);
	validate();
    }

    Term a1 = null;
    Term a2 = null;
    Predicate pentomino = null;

    void sendArgument() {
	try {
	    M = Integer.parseInt(M_Field.getText());
	} catch (NumberFormatException e) {
	    M = 4;
	    M_Field.setText(Integer.toString(M));
	}
	this.prolog = new PrologControl();
	a1 = new IntegerTerm(M);
	a2 = new VariableTerm();
	Term[] args = {a1,a2};
	pentomino = new PRED_pentomino_applet_2();
	prolog.setPredicate(pentomino, args);
    }

    void receiveResult() {
	Term result = a2.dereference();
	if ( !result.isList() ) {
	    System.out.println("Invalid result!");
	    return;
	}
	int Row, Col;
	if (M == 8) {
	    Row = Col = 8;
	} 
	else if (3 <= M && M <= 6) {
	    Row = M;
	    Col = (int)(60/M);
	} else {
	    Row = 4;
	    Col = 15;
	}
	char pentomino[][] = new char[Row][Col];
	Term elm;
	try {
	    for (int i = 0; i < Row; i++) {
		for (int j = 0; j < Col; j++) {
		    elm = ((ListTerm)result).car().dereference();
		    pentomino[i][j] = elm.toString().toCharArray()[0];
		    result = ((ListTerm)result).cdr().dereference();
		}
	    }
	} catch (Exception e) {
	    System.out.println("are?");
	}
	pentominoPanel.setPentomino(Row, Col, pentomino);
    }

    public static void main(String args[]) {
	new Pentomino();
    }
}


class PentominoPanel extends Panel {
    private int M;
    private int N;
    private Color pentomino[][] = null;

    public void setPentomino(int m, int n, char p[][]) {
	M = m;
	N = n;
	pentomino = new Color[M][N];
	for (int i = 0; i < M; i++) {
	    for (int j = 0; j < N; j++) {
		Color c = Color.white;
		switch (p[i][j]) {
		  case 'F': c = Color.blue; break;
		  case 'I': c = Color.green; break;
		  case 'L': c = Color.red; break;
		  case 'N': c = Color.cyan; break;
		  case 'P': c = Color.magenta; break;
		  case 'T': c = Color.yellow; break;
		  case 'U': c = Color.orange; break;
		  case 'V': c = Color.pink; break;
		  case 'W': c = Color.black; break;
		  case 'X': c = Color.gray; break;
		  case 'Y': c = new Color(64,128,128); break;
		  case 'Z': c = new Color(128,64,128); break;
		}
		pentomino[i][j] = c;
	    }
	}
	repaint();
    }

    public void paint(Graphics g) {
	if (pentomino == null)
	  return;
	int w = getSize().width;
	int h = getSize().height;
	w = w - 10;
	h = h - 10;
	int cellSize = Math.min(w/N, h/M);
	int x0 = (w - N * cellSize) / 2 + 5;
	int y0 = (h - M * cellSize) / 2 + 5;
	for (int i = 0; i < M; i++)
	  for (int j = 0; j < N; j++) {
	      g.setColor(pentomino[i][j]);
	      g.fillRect(x0+j*cellSize, y0+i*cellSize, cellSize, cellSize);
	  }
    }
}
