package com.lab;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

@Path("/amq")
@Produces("application/json")
@Consumes("application/json")
public class AMQResource {

    @Inject
    AMQProducerService producerService;

    @POST
    public Response sendMessage(String message) {
        System.out.println("Start message: " + message);
        producerService.sendMessage(message);
        return Response.status(201).build();

    }

}
