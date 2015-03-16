package com.googlecode.prolog_cafe.exceptions;

import com.googlecode.prolog_cafe.lang.JavaObjectTerm;
import com.googlecode.prolog_cafe.lang.Operation;
import com.googlecode.prolog_cafe.lang.StructureTerm;
import com.googlecode.prolog_cafe.lang.SymbolTerm;
import com.googlecode.prolog_cafe.lang.Term;

/**
 * Permission error.<br>
 * There will be a permission error when it is not permitted
 * to perform a specific operation.
 *
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
 */
public class PermissionException extends BuiltinException {
    private static final long serialVersionUID = 1L;

    /** A functor symbol of <code>permission_error/5</code>. */
    public static final SymbolTerm PERMISSION_ERROR = SymbolTerm.intern("permission_error", 5);

    /* operation ::= access | create | input | modify | open | output | reposition | new */
    /** Holds a string representation of operation. */
    public String operation;

    /*
      permissionType ::= binary_stream | flag | operator | past_end_of_stream
                         private_procedure | static_procedure | source_sink
			 stream | text_stream
    */
    /** Holds a string representation of permission type. */
    public String permissionType;

    /** Holds the argument or one of its components which caused the error. */
    public Term culprit;

    /** Holds a string representation of detail message. */
    public String msg;

    /** Constructs a new <code>PermissionException</code>
     * with the given arguments. */
    public PermissionException(Operation _goal, 
			       String _operation, 
			       String _permissionType, 
			       Term _culprit, 
			       String _message) {
	this.goal = _goal;
	operation = _operation;
	permissionType   = _permissionType;
	culprit   = _culprit;
	msg       = _message;
    }

    /** Returns a term representation of this <code>PermissionException</code>:
     * <code>permission_error(goal,argNo,operation,permissionType,culprit,message)</code>.
     */
    @Override
    public Term getMessageTerm() {
	Term[] args = {
	    new JavaObjectTerm(goal), 
	    SymbolTerm.create(operation),
	    SymbolTerm.create(permissionType),
	    culprit,
	    SymbolTerm.create(msg)};
	return new StructureTerm(PERMISSION_ERROR, args);
    }

    /** Returns a string representation of this <code>PermissionException</code>. */
    @Override
    public String toString() {
	String s = "{PERMISSION ERROR: " + goal.toString();
	s += " - can not " + operation + " " + permissionType + " " + culprit.toString();
	s += ": " + msg;
	s += "}";
	return s;
    }
}
