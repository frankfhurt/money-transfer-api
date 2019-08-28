package com.moneytransfer.common.exception;

import javax.inject.Singleton;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.moneytransfer.common.ViolationResponse;

/**
 * 
 * @author Franklyn Vieira
 * @since 08/2019
 *
 */
@Singleton
@Provider
public class CustomViolationHandler implements ExceptionMapper<CustomViolationException> {

	@Override
	public Response toResponse(CustomViolationException e) {
		
		ViolationResponse response = ViolationResponse.builder()
										.description("Invalid request")
										.errors(e.getViolations())
									.build();

		return Response.status(Status.BAD_REQUEST).entity(response).build();
	}

}
