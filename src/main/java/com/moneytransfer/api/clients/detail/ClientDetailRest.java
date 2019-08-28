package com.moneytransfer.api.clients.detail;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.moneytransfer.api.BusinessService;

/**
 * 
 * @author Franklyn Vieira
 * @since 08/2019
 *
 */
@Path("/clients")
@Produces(MediaType.APPLICATION_JSON)
public class ClientDetailRest {

	BusinessService<ClientDetailRequest, ClientDetailResponse> businessService = new ClientDetailBusinessService();

	@GET
	@Path("{clientId}")
	public Response detail(@PathParam("clientId") Long clientId) {
		
		ClientDetailRequest request = new ClientDetailRequest(clientId);
		
		ClientDetailResponse response = businessService.execute(request);
		
		return Response.status(Status.OK).entity(response).build();
	}
}
