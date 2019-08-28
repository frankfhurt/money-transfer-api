package com.moneytransfer.api.clients.create;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.moneytransfer.api.BusinessService;
import com.moneytransfer.common.ErrorResponse;

/**
 * 
 * @author Franklyn
 * @since 08/2019
 *
 */
@Path("/clients")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClientCreateRest {

	BusinessService<ClientCreateRequest, ClientCreateResponse> businessService;

	public ClientCreateRest() {
		super();
		this.businessService = new ClientCreateBusinessService();
	}
	
	public ClientCreateRest(BusinessService<ClientCreateRequest, ClientCreateResponse> businessService) {
		super();
		this.businessService = businessService;
	}

	@POST
	public Response create(@Valid @NotNull(message = "request cannot be null") ClientCreateRequest request) {
		try {
			ClientCreateResponse response = businessService.execute(request);
			return Response.status(Status.OK).entity(response).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(new ErrorResponse(e.getMessage())).build();
		}
	}
}