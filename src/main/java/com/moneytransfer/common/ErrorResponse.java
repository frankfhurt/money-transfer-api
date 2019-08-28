package com.moneytransfer.common;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;

/**
 * 
 * @author Franklyn Vieira
 * @since 08/2019
 *
 */
@AllArgsConstructor
public class ErrorResponse {

	@JsonProperty
	private String message;
	
}
