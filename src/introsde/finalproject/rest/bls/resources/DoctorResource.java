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

    private String errorMessage(Exception e){
    	return "{ \n \"error\" : \"Error in Business Logic Services, due to the exception: "+e+"\"}";
    }
	
	private String externalErrorMessage(String e){
    	return "{ \n \"error\" : \"Error in External services, due to the exception: "+e+"\"}";
    }
    
   
    
    
    /**
	 * GET /doctor/{doctorId}
	 * Return Doctor with {doctorId}
	 * @return DoctorType a doctor
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response readDoctor() {
		try{
		System.out.println("readDoctor: Reading Doctor id "+this.idDoctor +"...");
		Response response = service.path(path).request().accept(mediaType).get(Response.class);
		
		DoctorType doctor = response.readEntity(DoctorType.class);
		
		if(response.getStatus() != 200){
	    	System.out.println("SS Error response.getStatus() != 200  ");
	     return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(externalErrorMessage(doctor.toString())).build();
	     
	     }else{
	    	 return Response.ok(doctor).build();
	     }
	    }catch(Exception e){
	    	System.out.println("BLS Error catch response.getStatus() != 200  ");
	    	return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
			.entity(errorMessage(e)).build();
	    }
		
	}
    
	/**
	 * DELETE /doctor/{doctorId}
	 * Delete Doctor with {doctorId}
	 */
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteDoctor() {
		try{
		System.out.println("deteteDoctor: Deleting doctor with id: "+ this.idDoctor);
		Response response = service.path(path).request(mediaType).delete(Response.class);
		
		if(response.getStatus() != 204){
	    	System.out.println("SS Error response.getStatus() != 200  ");
	     return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(externalErrorMessage(response.toString())).build();
	     }else{
	    	 return Response.ok(response.readEntity(String.class)).build();
	     }
	    }catch(Exception e){
	    	System.out.println("BLS Error catch response.getStatus() != 200  ");
	    	return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
			.entity(errorMessage(e)).build();
	    }
	
	}
    
	/**
	 * GET /doctor/{doctorId}/patients
	 * Returns the list of patients
	 * @return ListPersonType list of person
	 */
	@GET
	@Path("/patients")
	@Produces( MediaType.APPLICATION_JSON )
	public Response getPatientList() {
		try{
		System.out.println("getPatientList: Reading list of patients for Doctor "+ this.idDoctor +"...");
		Response response = service.path(path+"/patients").request().accept(mediaType).get(Response.class);
		System.out.println(response);
		
		if(response.getStatus() != 200){
	    	System.out.println("SS Error response.getStatus() != 200  ");
	     return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(externalErrorMessage(response.toString())).build();
	     }else{
	    	 return Response.ok(response.readEntity(ListPersonType.class)).build();
	     }
	    }catch(Exception e){
	    	System.out.println("BLS Error catch response.getStatus() != 200  ");
	    	return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
			.entity(errorMessage(e)).build();
	    }
	}
}