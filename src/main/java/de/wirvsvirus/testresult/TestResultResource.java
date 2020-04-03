package de.wirvsvirus.testresult;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.wirvsvirus.testresult.database.TestResult;
import de.wirvsvirus.testresult.exception.FalseInformedException;
import de.wirvsvirus.testresult.service.TestResultService;
import lombok.AllArgsConstructor;

@Path("/tests")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@AllArgsConstructor
public class TestResultResource {

    @Inject
    TestResultService service;

    @GET
    @Path("/{testId}")
    public TestResult getResult(@PathParam("testId") String testId) {
        return service.getTestResult(testId);
    }

    @POST
    @Path("/{testId}")
    @RolesAllowed("Post")
    public TestResult addTestResult(@PathParam("testId") String testId, TestResult testResult) throws FalseInformedException {
        testResult.setHash(testId);
        return service.updateTestResult(testResult);
    }
}