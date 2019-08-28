package com.moneytransfer.common;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

/**
 * 
 * @author Franklyn Vieira
 * @since 08/2019
 *
 */
@Getter
@Builder
public class ViolationResponse {

	private String description;
	
	private List<Violation> errors;

}
