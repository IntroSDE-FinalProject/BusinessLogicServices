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
import introsde.finalproject.rest.generated.MeasureType;

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
        this.path = "family/"+this.idFamily;
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
	public ListMeasureType visualizeData() {
		
		
		Response response_family = service.path(path).request().accept(mediaType).get(Response.class);
		System.out.println("Response family" + response_family);
		ListMeasureType y = response_family.readEntity(ListMeasureType.class);
		List<MeasureType> familyList = y.getMeasure();
		for(int i=0; i< familyList.size(); i++){
			System.out.println(familyList.get(i).toString());
		}
		
		//path /person/{personId}/measure to retrieve a list of measure for a person
		String path_person_list = "person/"+personId+"/measure";
		Response response_person_measure = service.path(path_person_list).request().accept(mediaType).get(Response.class);
		System.out.println("Response " + response_person_measure);
		
		ListMeasureType x = response_person_measure.readEntity(ListMeasureType.class);
		List<MeasureType> person_measure_list = x.getMeasure();
		for(int i=0; i< person_measure_list.size(); i++){
			System.out.println("List of person measure: " + person_measure_list.get(i).getMeasureDefinition());
		}
		
		
		
		
		
		
		
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