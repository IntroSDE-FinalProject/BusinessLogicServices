package introsde.finalproject.rest.bls.resources;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
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
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import introsde.finalproject.rest.generated.*;

/**
 *
 */
@Stateless // only used if the the application is deployed in a Java EE container
@LocalBean // only used if the the application is deployed in a Java EE container
public class PersonResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	int idPerson;
	private WebTarget service = null;
	private String mediaType = null;
	private String path = null;

	public PersonResource(UriInfo uriInfo, Request request,int id) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.idPerson = id;
		this.path = "person/"+this.idPerson;
	}

	public PersonResource(UriInfo uriInfo, Request request,int id, WebTarget service, String mediatype) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.idPerson = id;
		this.service = service;
		this.mediaType = mediatype;
		this.path = "person/"+this.idPerson;
	}
	
	
	private String errorMessage(Exception e){
    	return "{ \n \"error\" : \"Error in Storage Services, due to the exception: "+e+"\"}";
    }
	
	private String externalErrorMessage(String e){
    	return "{ \n \"error\" : \"Error in External services, due to the exception: "+e+"\"}";
    }
	

	//********************PERSON********************

	/**
	 * GET /person/{personId}
	 * Return Person with {personId}
	 * @return PersonType person
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public PersonType readPerson() {
		System.out.println("readPerson: Reading Person...");
		Response response = service.path(path).request().accept(mediaType).get(Response.class);
		if (response.getStatus() == 200 || response.getStatus() == 202) {
			PersonType u = response.readEntity(PersonType.class);
			return u;
		}else{
			return null;
		}
	}

	/**
	 * DELETE /person/{personId}
	 * Delete Person with {personId}
	 */
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public void deletePerson() {
		System.out.println("detetePerson: Deleting person with id: "+ this.idPerson);
		Response response = service.path(path).request(mediaType).delete(Response.class);
		System.out.println(response);
	}

	//********************MEASURE********************

	/**
	 * GET /person/{personId}/currentHealth
	 * Return the list of the most last measurement for each MeasureDefinition
	 * @return ListMeasureType list of measures
	 */
	@GET
	@Path("/currentHealth")
	@Produces( MediaType.APPLICATION_JSON )
	public ListMeasureType getCurrentHealth() {
		System.out.println("getCurrentHealth: Reading CurrentHealth for idPerson "+ this.idPerson +"...");
		Response response = service.path(path+"/currentHealth").request().accept(mediaType).get(Response.class);
		System.out.println(response);
		return response.readEntity(ListMeasureType.class);
	}

	/**
	 * GET /person/{personId}/measure
	 * Return the history of all measurement
	 * @return ListMeasureType list of measures
	 */
	@GET
	@Path("/measure")
	@Produces( MediaType.APPLICATION_JSON )
	public ListMeasureType visualizeMeasure() {
		System.out.println("visualizeMeasure: Reading Measures for idPerson "+ this.idPerson +"...");
		Response response = service.path(path+"/measure").request().accept(mediaType).get(Response.class);
		System.out.println(response);
		//ListMeasureType x = response.readEntity(ListMeasureType.class);
		//x.getMeasure().get(0).getTimestamp();
		return response.readEntity(ListMeasureType.class);
	}

	//***********************REMINDER***********************

	/**
	 * GET /person/{personId}/reminder
	 * Return all not expired reminder and ordered by relevanceLevel 
	 * @return ListReminderType list of reminders 
	 * @throws ParseException 
	 */
	@GET
	@Path("/reminder")
	@Produces( MediaType.APPLICATION_JSON )
	public ListReminderType visualizeReminder() throws ParseException {
		System.out.println("visualizeReminder: Reading reminders for idPerson "+ this.idPerson +"...");
		
		//send and read request
		Response response = service.path(path+"/reminder").request().accept(mediaType).get(Response.class);
		System.out.println(response);
		ListReminderType list = response.readEntity(ListReminderType.class);
		
		//iterates on the list of reminder and checks if they are expired
		ListReminderType returnList = new ListReminderType();
		if(list.getReminder().size() > 0){
			for(ReminderType re : list.getReminder()){
				//read a String and convert to Date
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Date date = format.parse(re.getExpireReminder());
				Date todayDate = Calendar.getInstance().getTime();
				System.out.println(date.compareTo(todayDate));
				if ( date.compareTo(todayDate) >= 0 ){
					returnList.getReminder().add(re);
				}
			}
			
			//sort the list of reminder based on the relevanceLevel 
			Collections.sort(returnList.getReminder(), new Comparator<ReminderType>() {
				@Override
				public int compare(final ReminderType object1, final ReminderType object2) {
					return object1.getRelevanceLevel().compareTo(object2.getRelevanceLevel());
				}
			} );
			return returnList;
		} else
			return returnList;
	}


	/**
	 * GET /person/{personId}/suggestion
	 * @return
	 */
	@GET
	@Path("/motivation")
	@Produces( MediaType.APPLICATION_JSON )
	public Response getMotivation(){
		try{
		System.out.println("visualizeSuggestion: Reading suggestion for idPerson "+ this.idPerson +"...");
		String path_motivation = "services/quote";
        System.out.println("Service to string" + service.toString());
        Response response_motivation = service.path(path_motivation).request().accept(MediaType.TEXT_PLAIN_TYPE).get(Response.class);
        System.out.print("Response: " + response_motivation);
	
        //AS ---> Response.status(Response.Status.INTERNAL_SERVER_ERROR)
		//.entity("Error AS").build();
        
        String motivation_quote = response_motivation.readEntity(String.class);
        if(response_motivation.getStatus() != 200){
        	System.out.println("SS Error response_motivation.getStatus() != 200  ");
         return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
    				.entity(externalErrorMessage(motivation_quote)).build();
         
         }else{
        	 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd");
             Calendar cal_created = Calendar.getInstance();
             //System.out.println(dateFormat.format(cal.getTime()));
             String date_created = dateFormat.format(cal_created.getTime());
             //System.out.println(date_created.getClass().getName());
             
             int years = cal_created.get(Calendar.YEAR);
             int days = cal_created.get(Calendar.DAY_OF_MONTH);
             int month = cal_created.get(Calendar.MONTH);
             
             //Calendar for expired date with the days setted to 5 days after respect to the creation
             Calendar cal_expired =  cal_created;
             //update a date
             int days_expired = days+5;
             cal_expired.set(years, month, days_expired);
             String date_expired = dateFormat.format(cal_expired.getTime());
             
             int relevance_value = 3;
             BigInteger relevance = BigInteger.valueOf(relevance_value);
        	 
        	 ReminderType quote_reminder = new ReminderType();
        	 quote_reminder.setAutocreate(true);
        	 quote_reminder.setCreateReminder(date_created);
        	 quote_reminder.setExpireReminder(date_expired);
        	 quote_reminder.setRelevanceLevel(relevance);
        	 quote_reminder.setText(motivation_quote);
        	 insertNewReminder(quote_reminder);
        	 
        	 return Response.ok(motivation_quote).build();
        	 
         }
        }catch(Exception e){
        	System.out.println("BLS Error catch response_motivation.getStatus() != 200  ");
        	return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
			.entity(errorMessage(e)).build();
        }
	
	}
	
	
	
	/**
	 * GET /person/{personId}/suggestion
	 * @return
	 */
	@GET
	@Path("/weather")
	@Produces( MediaType.APPLICATION_JSON )
	public ReminderType  getWeather(){
		System.out.println("visualizeSuggestion: Reading suggestion for idPerson "+ this.idPerson +"...");

		//TODO da finire

		return null;
	}
	
	
	
	/**
	 * GET /person/{personId}/suggestion
	 * @return
	 */
	@GET
	@Path("/forecast")
	@Produces( MediaType.APPLICATION_JSON )
	public ReminderType  getForecast(){
		System.out.println("visualizeSuggestion: Reading suggestion for idPerson "+ this.idPerson +"...");

		//TODO da finire

		return null;
	}
	
	
	
	
	
	

	/**
	 * POST /person/{personId}/reminder
	 * Insert a new reminder for person {personId}
	 * @return 
	 */
	@POST
	@Path("/reminder")
	@Produces( MediaType.APPLICATION_JSON )
	@Consumes({MediaType.APPLICATION_JSON ,  MediaType.APPLICATION_XML})
	public Response insertNewReminder(ReminderType reminder){
		System.out.println("insert New Reminder for person "+ this.idPerson);
		Response response = service.path(path+"/reminder").request(mediaType)
				.post(Entity.entity(reminder, mediaType), Response.class);
		System.out.println(response);
		
		//TODO exception handler
		return null;
	}


	//********************TARGET********************

	/**
	 * GET /person/{personId}/target/check
	 * 	checkTarget(Measure) --> Boo
	 * 
	 * Check if the target is achieved for the measure passed as param
	 * 
	 */
	@GET
	@Path("/target/check")
	@Produces( MediaType.APPLICATION_JSON )
	public Boolean checkTarget() {
		System.out.println("checkTarget: Checking targets for idPerson "+ this.idPerson +"...");

		//TODO da fare

		return null;
	}

	/**
	 * GET /person/{personId}/target
	 * Return list of target for person with id=personId
	 * @return ListTargetType list of targets
	 */
	@GET
	@Path("/target")
	@Produces( MediaType.APPLICATION_JSON )
	public ListTargetType readTargets() {
		System.out.println("readTargets: Readind targets for idPerson "+ this.idPerson +"...");
		Response response = service.path(path+"/target").request().accept(mediaType).get(Response.class);
		System.out.println(response);
		return response.readEntity(ListTargetType.class);
	}

	/**
	 * POST /person/{personId}/target
	 * Insert a new target for person {personId} 
	 */
	@POST
	@Path("/target")
	@Produces( MediaType.TEXT_PLAIN )
	@Consumes({MediaType.APPLICATION_JSON ,  MediaType.APPLICATION_XML})
	public Response insertNewTarget(TargetType target){
		System.out.println("insertNewTarget: New Target... ");
		Response response = service.path(path+"/target").request(mediaType)
				.post(Entity.entity(target, mediaType), Response.class);
		System.out.println(response);
		
		//TODO exception handler
		return null;	
	}

}