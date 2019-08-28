package com.moneytransfer.common.exception;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Singleton;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.moneytransfer.common.Violation;
import com.moneytransfer.common.ViolationResponse;

/**
 * 
 * @author Franklyn Vieira
 * @since 08/2019
 *
 */
@Singleton
@Provider
public class ConstraintViolationHandler implements ExceptionMapper<ConstraintViolationException> {

	@Override
	public Response toResponse(ConstraintViolationException e) {
		List<Violation> errors = e.getConstraintViolations()
									.stream()
									.map(temp -> new Violation(temp.getPropertyPath().toString(), temp.getMessage()))
									.collect(Collectors.toList());
		
		ViolationResponse response = ViolationResponse.builder()
													.description("Invalid request")
													.errors(errors)
												.build();

		return Response.status(Status.BAD_REQUEST).entity(response).build();
	}

}
