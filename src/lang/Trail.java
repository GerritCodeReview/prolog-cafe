package com.googlecode.prolog_cafe.lang;
/**
 * Trail stack.<br>
 * The class <code>Trail</code> represents a trail stack.<br>
 * Entries pushed to this trail stack must implement
 * the <code>Undoable</code> interface.
 * @see Undoable
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
 */
public final class Trail {
    /** An array of <code>Undoable</code> entries. */
    private Undoable[] buffer;

    /** the top index of this <code>Trail</code>. */
    private int top;

    /** Current timestamp of the top of {@link ChoicePointStack}. */
    long timeStamp;

    /** Constructs a new trail stack. */
    public Trail() {
	this(20000);
    }

    /** Constructs a new trail stack with the given size. */
    public Trail(int n) {
	buffer = new Undoable[n];
	top = -1;
    }

    /** Discards all entries. */
    public void init() { deleteAll(); }

    /** Pushs an entry to this <code>Trail</code>. */
    public void push(Undoable t) {
	try {
	    buffer[++top] = t;
	} catch (ArrayIndexOutOfBoundsException e) {
	    int len = buffer.length;
	    Undoable[] new_buffer = new Undoable[len+20000];
		System.arraycopy(buffer, 0, new_buffer, 0, len);
	    buffer = new_buffer;
	    buffer[top] = t;
	}
    }

    /** Pops an entry from this <code>Trail</code>. */
    public Undoable pop() {
	Undoable t = buffer[top];
	buffer[top--] = null;
	return t;
    }

    /** Discards all entries. */
    protected void deleteAll() {
	while (! empty()) {
	    buffer[top--] = null;
	}	
    }

    /** Tests if this stack has no entry. */
    public boolean empty() {
	return top == -1;
    }

    /** Current allocation of the trail storage array.  */
    public int max() { return buffer.length; }

    /** Returns the value of <code>top</code>. 
     * @see #top
     */
    public int top() { return top; }

    /** Unwinds all entries after the value of <code>i</code>. */
    public void unwind(int i) {
	Undoable t;
	while (top > i) {
	    t = pop();
	    t.undo();
	}
    }
}

