package com.googlecode.prolog_cafe.lang;
import java.util.HashMap;
/**
 * <code>Hashtable&lt;Term,Term&gt;</code>.<br>
 * <font color="red">This document is under construction.</font>
 *
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
 */
public class HashtableOfTerm extends HashMap<Term,Term> {
    private static final long serialVersionUID = 1L;

    public HashtableOfTerm() { 
	super(); 
    }
    public HashtableOfTerm(int initialCapacity) { 
	super(initialCapacity);
    }
    public HashtableOfTerm(int initialCapacity, float loadFactor) {
	super(initialCapacity, loadFactor);
    }
}