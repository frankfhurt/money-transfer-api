package com.moneytransfer.common.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.moneytransfer.common.ErrorResponse;

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
public class CustomWebApplicationException extends WebApplicationException {

	private static final long serialVersionUID = 1L;

	public CustomWebApplicationException(Status status, ErrorResponse errorResponse) {
		super(Response.status(status).entity(errorResponse).build());
	}
}