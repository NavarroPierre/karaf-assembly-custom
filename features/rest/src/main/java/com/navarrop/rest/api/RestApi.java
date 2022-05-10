package com.navarrop.rest.api;

import javax.ws.rs.*;

public interface RestApi {

    @Path("/timestamp")
    @GET
    @Produces("application/json")
    long getTimestamp();

    @Path("/echo")
    @POST
    @Produces("application/json")
    @Consumes("application/json")
    String echo(String message);

}
