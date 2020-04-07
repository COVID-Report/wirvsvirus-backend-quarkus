package de.wirvsvirus.testresult;

import de.wirvsvirus.testresult.tools.HashCalculator;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

@Path("/hashes")
public class HashResource {

    DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");

   @GET
    public String getHashString(@QueryParam("sampleId") String sampleId, @QueryParam("name")String name , @QueryParam("birthday") String birthday) {
        return quote(HashCalculator.calculcateId(sampleId, name, dateTimeFormatter.parseLocalDate(birthday)));
    }

    private String quote(String calculcateId) {
       return "\"" + calculcateId + "\"";
    }
}
