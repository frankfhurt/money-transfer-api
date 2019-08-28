package com.moneytransfer.common;

import java.util.List;

import com.moneytransfer.api.RestRequest;

/**
 * 
 * @author Franklyn Vieira
 * @since 08/2019
 *
 */
public interface RequestValidator {

	List<Violation> validate(RestRequest request);
	
}
