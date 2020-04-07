package de.wirvsvirus.testresult;


import de.wirvsvirus.testresult.exception.FalseInformedException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


@Provider
public class TestResultExceptionMapper implements ExceptionMapper<FalseInformedException> {

    @Override
    public Response toResponse(FalseInformedException ex) {
        return Response.status(Response.Status.CONFLICT).entity(ex.getErrorResult()).build();
    }
}
