package com.navarrop.rest.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

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

    @Path("/user")
    @GET
    @Produces("application/json")
    @Consumes("application/json")
    String user(@Context SecurityContext securityContext, String message);
}
