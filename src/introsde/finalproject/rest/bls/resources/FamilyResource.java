package introsde.finalproject.rest.bls.resources;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import introsde.finalproject.rest.generated.DoctorType;
import introsde.finalproject.rest.generated.ListMeasureType;

@Stateless // only used if the the application is deployed in a Java EE container
@LocalBean // only used if the the application is deployed in a Java EE container
public class FamilyResource {
    @Context
    UriInfo uriInfo;
    @Context
    Request request;
    int idFamily;
    private WebTarget service = null;
    private String mediaType = null;
    private String path = null;

    public FamilyResource(UriInfo uriInfo, Request request,int id) {
        this.uriInfo = uriInfo;
        this.request = request;
        this.idFamily = id;
        this.path = "family/"+this.idFamily;
    }

    public FamilyResource(UriInfo uriInfo, Request request,int id, WebTarget service, String mediatype) {
        this.uriInfo = uriInfo;
        this.request = request;
        this.idFamily = id;
        this.service = service;
        this.mediaType = mediatype;
        this.path = "doctor/"+this.idFamily;
    }
    
    /*
     * visualizeData(idUser)
receiveAlarm(idUser) --> List
visualizeDailyActivities(idUser) --> List, List, List 
     */
    
    /**
	 * GET /family/{familyId}/person/{personId}
	 * @return
	 */
	@GET
	@Path("/person/{personId}")
	@Produces(MediaType.APPLICATION_JSON)
	public ListMeasureType visualizeData(@PathParam("personId") String personId) {
		System.out.println("visualizeData: Reading Data from person id "+personId +"...");
		
		//TODO finire
		return null;
	}
    
	/**
	 * GET /family/{familyId}/person/{personId}/alarm
	 * @return
	 */
	@GET
	@Path("/person/{personId}/alarm")
	@Produces(MediaType.APPLICATION_JSON)
	public ListMeasureType receiveAlarm(@PathParam("personId") String personId) {
		System.out.println("receiveAlarm: Check Data from person id "+personId +"...");
		
		//TODO finire
		return null;
	}
    
	/**
	 * GET /family/{familyId}/person/{personId}/activities
	 * @return
	 */
	@GET
	@Path("/person/{personId}/activities")
	@Produces(MediaType.APPLICATION_JSON)
	public ListMeasureType visualizeDailyActivities(@PathParam("personId") String personId) {
		
		
		//TODO finire
		return null;
	}
}