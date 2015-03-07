package com.googlecode.prolog_cafe.lang;
import com.googlecode.prolog_cafe.exceptions.SystemException;

import java.util.LinkedList;
/**
 * Internal database for dynamic predicates.<br>
 *
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.1
 */
public class InternalDatabase {
    protected static final int DEFAULT_SIZE = 100000;

    /** Maximum size of enties. Initial size is <code>100000</code>. */
    protected int maxContents;

    /** An array of <code>Term</code> entries. */
    protected Term[] buffer;

    /* For GC */
    /** A list of reusable entry indices. */
    protected LinkedList<Integer> reusableIndices;

    /** the top index of this <code>InternalDatabase</code>. */
    protected int top;

    /** Constructs a new internal dababase. */
    public InternalDatabase() {
	this(DEFAULT_SIZE);
    }

    /** Constructs a new internal dababase with the given size. */
    public InternalDatabase(int n) {
	maxContents = n;
	buffer = new Term[Math.min(maxContents, DEFAULT_SIZE)];
	reusableIndices = new LinkedList<Integer>();
	top = -1;
    }

    InternalDatabase(Prolog engine, InternalDatabase src, boolean deepCopy) {
      maxContents = src.maxContents;
      buffer = new Term[src.buffer.length];
      reusableIndices = new LinkedList<Integer>(src.reusableIndices);
      top = src.top;

      if (deepCopy) {
        for (int i = 0; i <= top; i++) {
          Term s = src.buffer[i];
          if (s != null) {
            buffer[i] = s.copy(engine);
          }
        }
      } else if (0 <= top) {
        System.arraycopy(src.buffer, 0, buffer, 0, top + 1);
      }
    }

    /** Inserts an entry to this <code>InternalDatabase</code>. */
    public int insert(Term t) {
	try {
	    if (reusableIndices.isEmpty()) {
		buffer[++top] = t;
		return top;
	    } else {
		int i = reusableIndices.remove();
		buffer[i] = t;
		return i;
	    }
	} catch (ArrayIndexOutOfBoundsException e) {
	    if (maxContents <= buffer.length)
	      throw new SystemException("internal database capacity reached");
	    int len = buffer.length;
	    Term[] new_buffer = new Term[Math.min(len+10000, maxContents)];
	    for(int i=0; i<len; i++){
		new_buffer[i] = buffer[i];
	    }
	    buffer = new_buffer;
	    buffer[top] = t;
	    return top;
	}
    }

    /** Returns an entry with the given index from this <code>InternalDatabase</code>. */
    public Term get(int i) {
	return buffer[i];
    }

    /** Erases an entry with the given index from this <code>InternalDatabase</code>. */
    public Term erase(int i) {
	Term t = buffer[i];
	buffer[i] = null;
	reusableIndices.add(i);
	return t;
    }

    /** Tests if this has no entry. */
    private boolean empty() {
	return top == -1;
    }

    /** Shows the contents of this <code>InternalDatabase</code>. */
    public void show() {
	if (empty())
	    System.out.println("{internal database is empty!}");
	System.out.println("{reusable indices: " + reusableIndices.toString() + "}");
	for (int i=0; i<=top; i++) {
	    System.out.print("internal database[" + i + "]: ");
	    System.out.println(buffer[i]);
	}
    }
}
