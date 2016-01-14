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

	//********************PERSON********************

	/**
	 * GET /person/{personId}
	 * @return
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
	 * @return
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
	 * @return
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
	 * @return
	 * @throws ParseException 
	 */
	@GET
	@Path("/reminder")
	@Produces( MediaType.APPLICATION_JSON )
	public ListReminderType visualizeReminder() throws ParseException {
		System.out.println("visualizeReminder: Reading reminders for idPerson "+ this.idPerson +"...");
		Response response = service.path(path+"/reminder").request().accept(mediaType).get(Response.class);
		System.out.println(response);
		ListReminderType list = response.readEntity(ListReminderType.class);

		ListReminderType returnList = new ListReminderType();
		if(list.getReminder().size() > 0){
			for(ReminderType re : list.getReminder()){
				System.out.println("sono qui");
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Date date = format.parse(re.getExpireReminder());
				Date todayDate = Calendar.getInstance().getTime();
				System.out.println(date.compareTo(todayDate));
				if ( date.compareTo(todayDate) >= 0 ){
					returnList.getReminder().add(re);
				}
			}

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
	@Path("/suggestion")
	@Produces( MediaType.APPLICATION_JSON )
	public ReminderType  visualizeSuggestion(){
		System.out.println("visualizeSuggestion: Reading suggestion for idPerson "+ this.idPerson +"...");

		//TODO da finire

		return null;
	}

	/**
	 * POST /person/{personId}/reminder
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
		return response;
	}


	//********************TARGET********************

	/**
	 * 	checkTarget(Measure) --> Boo
	 */
	@POST
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
	 *  
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
		return response;	
	}

}