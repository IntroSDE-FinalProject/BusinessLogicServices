package introsde.finalproject.rest.bls.resources;

import java.lang.reflect.Executable;
import java.math.BigInteger;
import java.util.ArrayList;
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

import org.json.JSONObject;

import introsde.finalproject.rest.generated.DoctorType;
import introsde.finalproject.rest.generated.FamilyType;
import introsde.finalproject.rest.generated.ListMeasureType;
import introsde.finalproject.rest.generated.MeasureType;
import introsde.finalproject.rest.generated.PersonType;

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
    
    
    private String errorMessage(Exception e){
    	return "{ \n \"error\" : \"Error in Business Logic Services, due to the exception: "+e+"\"}";
    }
	
	private String externalErrorMessage(String e){
    	return "{ \n \"error\" : \"Error in External services, due to the exception: "+e+"\"}";
    }
    
	//Errors for objects
	private String externalErrorMessageForObjects(Exception e,String obj){
    	return "{ \n \"error\" : \"Error in External services, due to the exception: "+e+", for the object: "+obj+"\"}";
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
	@Path("/person/measures")
	@Produces(MediaType.APPLICATION_JSON)
	public Response visualizeData() {
		try{
		Response response_family = service.path(path).request().accept(mediaType).get(Response.class);
		System.out.println("Response family " + response_family );
		FamilyType y = response_family.readEntity(FamilyType.class);
		
		if(response_family.getStatus() != 200){
        	System.out.println("BLS Error response_family.getStatus() != 200  ");
         return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
    				.entity(externalErrorMessage(y.getPerson().getMeasurements().toString())).build();
         }else{
        	 return Response.ok(y.getPerson().getMeasurements()).build();
         }
		}catch(Exception e){
    		System.out.print("Error Cath motivation");
    		return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
    				.entity(errorMessage(e)).build();
    	}
	}
    
	/**
	 * GET /family/{familyId}/person/{personId}/alarm
	 * @return
	 */
	@GET
	@Path("/person/alarm")
	@Produces(MediaType.APPLICATION_JSON)
	public Response receiveAlarm() {
		try{
			Response response_family = service.path(path).request().accept(mediaType).get(Response.class);
			System.out.println("Response family " + response_family );
			FamilyType y = response_family.readEntity(FamilyType.class);
			
			if(response_family.getStatus() != 200){
	        	System.out.println("BLS Error response_family.getStatus() != 200  ");
	         return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	    				.entity(externalErrorMessage(y.getPerson().getMeasurements().toString())).build();
	         }else{
	        	 //System.out.println("getCurrentHealth: Reading CurrentHealth for idPerson "+ this.idPerson +"...");
	        	 
	        	 PersonType p = y.getPerson();
	        	 p.getIdPerson();
	        	 Response response_currentHealth = service.path("person/"+p.getIdPerson()+"/currentHealth").request().accept(mediaType).get(Response.class);
	        	 ListMeasureType listCurrenthHealth = response_currentHealth.readEntity(ListMeasureType.class);
	        	 List<MeasureType> listMeasures = listCurrenthHealth.getMeasure();
	        	 int size = listMeasures.size();
	        	 
	        	 List<Integer> z = new ArrayList<Integer>();
	        	 
	        	 int idMeasureDefinition;
	        	 int value;
	        	 boolean min_good = false;
	        	 boolean max_good = false;
	        	 String min_message = "";
	        	 String max_message = "";
	        	 
	        	 for(int b=0; b<listMeasures.size();b++){
	        		 idMeasureDefinition = listMeasures.get(b).getMeasureDefinition().getIdMeasureDef().intValue();
	        		 value =  Integer.parseInt(listMeasures.get(b).getValue());
	        		 if(idMeasureDefinition == 4 || idMeasureDefinition == 5){
	        		 if(idMeasureDefinition == 4 && value > 90){
	        			 System.out.println("Blood pressure min too hight..." + value);
	        			 z.add(value);
	        			 min_good = false;
	        			 min_message = "blood pressure min too hight...";
	        		 }if(idMeasureDefinition == 4 && value < 60){
	        			 System.out.println("Blood pressure min too low: " + value);
	        			 z.add(value);
	        			 min_good = false;
	        			 min_message = "blood pressure min too low...";
	        		 }if(idMeasureDefinition == 4 && (value > 60 && value < 90) ){
	        			 System.out.println("Blood pressure min is perfect: " + value);
	        			 z.add(value);
	        			 min_good = true;
	        			 min_message = "blood pressure min is perfect...";
	        		 }if(idMeasureDefinition == 5 && value > 130){
	        			 System.out.println("Blood pressure max too hight: " + value);
	        			 z.add(value);
	        			 max_good = false;
	        			 max_message = "blood pressure max too hight...";
	        		 }if(idMeasureDefinition == 5 && value < 80){
	        			 System.out.println("Blood pressure max too low: " + value);
	        			 z.add(value);
	        			 max_good = false;
	        			 max_message = "lood pressure max too low...";
	        		 }if(idMeasureDefinition == 5 && (value > 80 && value < 130) ){
	        			 System.out.println("Blood pressure max is perfect..." + value);
	        			 z.add(value);
	        			 max_good = true;
	        			 max_message = "blood pressure max is perfect...";
	        		 }
	        		 } 
	        	 }
	        	 
	        	 int min = z.get(0);
	        	 int max = z.get(1);
	        	 String pressure_message;
	        	 if(!min_good && !max_good){
	        		 pressure_message = "The blood pressure of assisted is good !!! " + min_message + " " + max_message;
	        		 System.out.println("The blood pressure is good... !!!" + min_message);
	        	 }else{
	        		 pressure_message = "The blood pressure is not good... !!!" + min_message + " " + max_message;
	        		 System.out.println("The blood pressure is not good... !!!");
	        	 }
	        	 
	        	 
	        	 JSONObject message_alarm = new JSONObject();
	        	 message_alarm.put("Blood pressure min", min);
	        	 message_alarm.put("Blood pressure max", max);
	        	 message_alarm.put("Message",pressure_message);
	        	 
	        	 
	        	 /*
	        	 ListMeasureType x = y.getPerson().getMeasurements();
	        	 List<MeasureType> measureList = x.getMeasure();
	        	 for(int i=0; i<measureList.size(); i++){
	        		 System.out.println(measureList.get(i));
	        	 }
	        	 */
	        	 
	        	 //return Response.ok(y.getPerson().getMeasurements()).build();
	        	 return Response.ok(message_alarm.toString()).build();
	         }
			}catch(Exception e){
	    		System.out.print("Error Cath motivation");
	    		return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	    				.entity(errorMessage(e)).build();
	    	}
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