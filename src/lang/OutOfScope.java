package com.googlecode.prolog_cafe.lang;
/**
 * A trail entry for out-of-scope flag.<br>
 * This <code>OutOfScope</code> class is used in 
 * subclasses of <code>BlockPredicate</code>.<br>
 * <font color="red">This document is under construction.</font>
 *
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
 */
public class OutOfScope implements Undoable {
    BlockPredicate p;

    public OutOfScope(BlockPredicate _p) {
	p = _p;
    }

    @Override
    public void undo() {
	p.outOfScope = false;
    }
}
