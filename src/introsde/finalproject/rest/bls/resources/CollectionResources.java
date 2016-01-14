package introsde.finalproject.rest.bls.resources;

import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import javax.ejb.*;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.glassfish.jersey.client.ClientConfig;

import introsde.finalproject.rest.generated.ListMeasureDefinitionType;
import introsde.finalproject.rest.generated.ListMeasureType;
import introsde.finalproject.rest.generated.ListTargetType;
import introsde.finalproject.rest.generated.MeasureDefinitionType;
import introsde.finalproject.rest.generated.MeasureType;
import introsde.finalproject.rest.generated.ReminderType;

@Stateless // will work only inside a Java EE application
@LocalBean // will work only inside a Java EE application
@Path("/")
public class CollectionResources {

    // Allows to insert contextual objects into the class,
    // e.g. ServletContext, Request, Response, UriInfo
    @Context
    UriInfo uriInfo;
    @Context
    Request request;
    
    
    private static String uriServer = "https://ss-serene-hamlet-9690.herokuapp.com/sdelab"; //StorageService
   // private static String uriServer = "http://127.0.1.1:5777/sdelab/";
	private static String mediaType = MediaType.APPLICATION_JSON;
	
	private Client client = null;
	private WebTarget service = null;
	private ClientConfig clientConfig = null;
	
	public CollectionResources() throws MalformedURLException{
		clientConfig = new ClientConfig();
		client = ClientBuilder.newClient(clientConfig);
		service = client.target(getBaseURI(uriServer));
	}
	
	private static URI getBaseURI(String uriServer) {
		return UriBuilder.fromUri(uriServer).build();
	}
    
	public void reloadUri(){
		service = null;
		service = client.target(getBaseURI(uriServer));
	}
	

	 @Path("person/{personId}")
	 public PersonResource getPerson(@PathParam("personId") int id) {
		 return new PersonResource(uriInfo, request, id, service, mediaType);
	 }
	
	 @Path("doctor/{doctorId}")
	 public DoctorResource getDoctor(@PathParam("doctorId") int id) {
		 return new DoctorResource(uriInfo, request, id, service, mediaType);
	 }
	 
	 @Path("family/{familyId}")
	 public FamilyResource getFamily(@PathParam("familyId") int id) {
		 return new FamilyResource(uriInfo, request, id, service, mediaType);
	 }
	 
	 /**
	  * GET /measureDefinition
	  * Return the measureDefinition object associated with a specific id
	  * @return {@link MeasureDefinitionType} 
	  */
	 @GET
	 @Path("/measureDefinition")
	 @Produces( MediaType.APPLICATION_JSON )
	 public MeasureDefinitionType readTargets(@QueryParam("measure") BigInteger measureId) {
		 System.out.println("readTargets: Readind Measure Definition");
		 Response response = service.path("/measureDefinition").request().accept(mediaType).get(Response.class);
		 System.out.println(response);
		 ListMeasureDefinitionType ld = response.readEntity(ListMeasureDefinitionType.class);
		 MeasureDefinitionType result = null;
		 for(MeasureDefinitionType de : ld.getMeasureType()){
			 if(de.getIdMeasureDef().compareTo(measureId) == 0){
				 result = de;
			 }
		 }
		 return result;
	 }
}