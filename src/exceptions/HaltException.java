package com.googlecode.prolog_cafe.exceptions;

public class HaltException extends SystemException {
  private static final long serialVersionUID = 1L;

  private final int status;

  public HaltException(int status) {
    super("halt(" + status + ")");
    this.status = status;
  }

  public int getStatus() {
    return status;
  }
}
