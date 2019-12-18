package service.rest;


import service.api.UpperCaseService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/services")
public class RestService {

    private UpperCaseService service;

    public RestService(UpperCaseService service) {
        this.service = service;
    }

    @GET
    @Path("/{message}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response upperCaseMsg(@PathParam("message") String msg) {
        return Response.status(200).entity(service.toUpper(msg)).build();
    }

}
