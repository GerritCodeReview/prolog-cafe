package com.googlecode.prolog_cafe.exceptions;

/** Thrown if a goal runs too long. */
public class ReductionLimitException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ReductionLimitException(int limit) {
		super(String.format("exceeded reduction limit of %d", limit));
	}
}
