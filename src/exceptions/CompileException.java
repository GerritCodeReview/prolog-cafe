package com.googlecode.prolog_cafe.exceptions;

/** Indicates compiling did not succeed. */
public class CompileException extends Exception {
  private static final long serialVersionUID = 1L;

  public CompileException(String message) {
    super(message);
  }

  public CompileException(Throwable cause) {
    super(cause.getMessage(), cause);
  }

  public CompileException(String message, Throwable cause) {
    super(message, cause);
  }
}
