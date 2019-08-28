package com.moneytransfer.common;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * 
 * @author Franklyn Vieira
 * @since 08/2019
 *
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Violation {

	private String field;
	
	private String error;
	
}
