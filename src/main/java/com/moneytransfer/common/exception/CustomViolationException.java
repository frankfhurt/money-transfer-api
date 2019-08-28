package com.moneytransfer.common.exception;

import java.util.List;

import com.moneytransfer.common.Violation;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author Franklyn Vieira
 * @since 08/2019
 *
 */
@Getter
@Setter
public class CustomViolationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private List<Violation> violations;

	public CustomViolationException(List<Violation> violations) {
		this.violations = violations;
	}
}