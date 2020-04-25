package de.wirvsvirus.testresult;

import de.wirvsvirus.testresult.database.TestResult;
import de.wirvsvirus.testresult.exception.FalseInformedException;
import de.wirvsvirus.testresult.service.TestResultService;
import org.eclipse.microprofile.metrics.annotation.Counted;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/tests")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TestResultResource {

    @Inject
    protected TestResultService service;

    @GET
    @Path("/{testId}")
    @Counted(name = "getResult", description = "How many times a result has been requested.")
    public TestResult getResult(@PathParam("testId") String testId) {
        return service.getTestResult(testId);
    }

    @POST
    @Path("/{testId}")
    @RolesAllowed("Post")
    @Counted(name = "postResult", description = "How many times a result has updated requested.")
    public TestResult addTestResult(@PathParam("testId") String testId, TestResult testResult) throws FalseInformedException {
        testResult.setId(testId);
        return service.updateTestResult(testResult);
    }
}