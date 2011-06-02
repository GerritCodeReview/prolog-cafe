package com.googlecode.prolog_cafe.compiler;

/** Indicates compiling did not succeed. */
public class CompileException extends Exception {
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
