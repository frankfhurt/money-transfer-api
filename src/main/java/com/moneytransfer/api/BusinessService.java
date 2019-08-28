package com.moneytransfer.api;

/**
 * 
 * @author Franklyn Vieira
 * @since 08/2019
 *
 */
public interface BusinessService<I extends RestRequest, O> {

	O execute(I request);
	
}
