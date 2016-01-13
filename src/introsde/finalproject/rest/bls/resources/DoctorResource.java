package introsde.finalproject.rest.bls.resources;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import introsde.finalproject.rest.generated.DoctorType;
import introsde.finalproject.rest.generated.ListMeasureType;
import introsde.finalproject.rest.generated.ListPersonType;

@Stateless // only used if the the application is deployed in a Java EE container
@LocalBean // only used if the the application is deployed in a Java EE container
public class DoctorResource {
    @Context
    UriInfo uriInfo;
    @Context
    Request request;
    int idDoctor;
    private WebTarget service = null;
    private String mediaType = null;
    private String path = null;

    public DoctorResource(UriInfo uriInfo, Request request,int id) {
        this.uriInfo = uriInfo;
        this.request = request;
        this.idDoctor = id;
        this.path = "doctor/"+this.idDoctor;
    }

    public DoctorResource(UriInfo uriInfo, Request request,int id, WebTarget service, String mediatype) {
        this.uriInfo = uriInfo;
        this.request = request;
        this.idDoctor = id;
        this.service = service;
        this.mediaType = mediatype;
        this.path = "doctor/"+this.idDoctor;
    }

    
    /**
	 * GET /doctor/{doctorId}
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public DoctorType readDoctor() {
		System.out.println("readDoctor: Reading Doctor id "+this.idDoctor +"...");
		Response response = service.path(path).request().accept(mediaType).get(Response.class);
		DoctorType doctor = response.readEntity(DoctorType.class);
		return doctor;
	}
    
	/**
	 * DELETE /doctor/{doctorId}
	 */
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public void deletePerson() {
		System.out.println("deteteDoctor: Deleting doctor with id: "+ this.idDoctor);
		Response response = service.path(path).request(mediaType).delete(Response.class);
		System.out.println(response);
	}
    
	/**
	 * GET /doctor/{doctorId}/patients
	 * Returns the list of patients
	 * @return
	 */
	//TODO aggiugere wrapper in lds
	@GET
	@Path("/patients")
	@Produces( MediaType.APPLICATION_JSON )
	public ListPersonType getPatientList() {
		System.out.println("getPatientList: Reading list of patients for Doctor "+ this.idDoctor +"...");
		Response response = service.path(path+"/patients").request().accept(mediaType).get(Response.class);
		System.out.println(response);
		return response.readEntity(ListPersonType.class);
	}
}