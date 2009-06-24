import java.awt.*;
import java.util.Vector;
import jp.ac.kobe_u.cs.prolog.lang.*;
/**
 * Knight Tour.<br>
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 */
public class Knight extends PrologFrame {
    private KnightPanel knightPanel;
    private TextField N_Field;
    private int N = 5;

    public Knight() {
	setSize(500, 400);
	setLayout(new BorderLayout());
	add("North", new Label("Knight Tour", Label.CENTER));
	knightPanel = new KnightPanel();
	add("Center", knightPanel);
	Panel p = new Panel();
	add("South", p);
	p.add(new Label("N="));
	N_Field = new TextField(Integer.toString(N), 2);
	p.add(N_Field);
	control = new PrologControlPanel(this);
	p.add(control);
	validate();
    }

    Term arg1 = null;
    Term arg2 = null;
    Predicate knight = null;

    public void sendArgument() {
	try {
	    N = Integer.parseInt(N_Field.getText());
	} catch (NumberFormatException e) {
	    N = 1;
	    N_Field.setText(Integer.toString(N));
	}
	this.prolog = new PrologControl();
	arg1 = new IntegerTerm(N);
	arg2 = new VariableTerm();
	Term[] args = {arg1,arg2};
	knight = new PRED_knight_tour_applet_2();
	prolog.setPredicate(knight, args);
	knightPanel.setKnight(N, null);
    }

    public void receiveResult(){
	Vector v = (Vector)(arg2.toJava());
	if(v == null) return;
	if(v.size() != N*N) return;
	int knight[][] = new int[N][N];
	try {
	    for (int i = 0; i < N; i++) {
		for (int j = 0; j < N; j++) {
		    knight[i][j]= ((Integer)(v.elementAt(i*N+j))).intValue();
		}
	    }
	    knightPanel.setKnight(N, knight);
	} catch (NumberFormatException e) {}
    }

    public static void main(String args[]) {
	new Knight();
    }
}

class KnightPanel extends Panel {
    private int N = 0;
    private int knightI[] = null;
    private int knightJ[] = null;
    private int w;
    private int h;
    private int cellSize;
    private int x0;
    private int y0;

    public void setKnight(int n, int k[][]) {
	if (n < 1) {
	    N = 0;
	    return;
	}
	N = n;
	if (k == null) {
	    knightI = null;
	    knightJ = null;
	    repaint();
	    return;
	}
	knightI = new int[N*N];
	knightJ = new int[N*N];
	for (int i = 0; i < N; i++) {
	    for (int j = 0; j < N; j++) {
		int s = k[i][j] - 1;
		knightI[s] = i;
		knightJ[s] = j;
	    }
	}
	repaint();
    }

    private int xpos(int j) {
	return x0+j*cellSize;
    }

    private int ypos(int i) {
	return y0+i*cellSize;
    }

    public void paint(Graphics g) {
	if (N < 1)
	    return;
	w = getSize().width;
	h = getSize().height;
	cellSize = Math.min(w, h) / N;
	x0 = (w - N * cellSize) / 2;
	y0 = (h - N * cellSize) / 2;
	g.setColor(Color.white);
	g.fillRect(x0, y0, N*cellSize, N*cellSize);
	g.setColor(Color.black);
	for (int i = 0; i <= N; i++)
	    g.drawLine(xpos(0), ypos(i), xpos(N), ypos(i));
	for (int j = 0; j <= N; j++)
	    g.drawLine(xpos(j), ypos(0), xpos(j), ypos(N));
	if (knightI == null || knightJ == null)
	    return;
	int hs = cellSize / 2;
	Font font = g.getFont();
	FontMetrics fontMetrics = g.getFontMetrics();
	g.setColor(Color.gray);
	for (int s = 0; s < N*N; s++) {
	    String str = Integer.toString(s + 1);
	    int x = xpos(knightJ[s])+hs;
	    int y = ypos(knightI[s])+hs;
	    x = x - fontMetrics.stringWidth(str) / 2;
	    y = y + fontMetrics.getAscent() / 2;
	    g.drawString(str, x, y);
	}
	g.setColor(Color.blue);
	for (int s = 0; s < N*N - 1; s++)
	    g.drawLine(xpos(knightJ[s  ])+hs, ypos(knightI[s  ])+hs,
		       xpos(knightJ[s+1])+hs, ypos(knightI[s+1])+hs);
    }
}
