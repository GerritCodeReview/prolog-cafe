package com.googlecode.prolog_cafe.exceptions;

/**
 * Thrown if a goal exceeds the configured reduction limit.
 *
 * @see com.googlecode.prolog_cafe.lang.PrologControl#setReductionLimit(long)
 */
public class ReductionLimitException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public ReductionLimitException(long limit) {
    super(String.format("exceeded reduction limit of %d", limit));
  }
}
