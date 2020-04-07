package de.wirvsvirus.testresult;

import de.wirvsvirus.testresult.database.TestResult;
import de.wirvsvirus.testresult.exception.FalseInformedException;
import de.wirvsvirus.testresult.service.TestResultService;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/tests")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TestResultResource {

    @Inject
    private TestResultService service;

    @GET
    @Path("/{testId}")
    public TestResult getResult(@PathParam("testId") String testId) {
        return service.getTestResult(testId);
    }

    @POST
    @Path("/{testId}")
    @RolesAllowed("Post")
    public TestResult addTestResult(@PathParam("testId") String testId, TestResult testResult) throws FalseInformedException {
        testResult.setId(testId);
        return service.updateTestResult(testResult);
    }
}