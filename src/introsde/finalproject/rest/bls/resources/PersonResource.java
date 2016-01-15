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
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.glassfish.jersey.client.ClientConfig;
import org.json.JSONObject;

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
    	return "{ \n \"error\" : \"Error in Business Logic Services, due to the exception: "+e+"\"}";
    }
	
	private String externalErrorMessage(String e){
    	return "{ \n \"error\" : \"Error in External services, due to the exception: "+e+"\"}";
    }
	
	/**
	 * 
	 * @param input
	 * @return -1 if input before today, 0 if input equals today , 1 if input after today
	 * @throws ParseException
	 */
	private int compareDateWithToday(String input) throws ParseException{
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = format.parse(input);
		Date todayDate = Calendar.getInstance().getTime();
		System.out.println("Compare date: "+input+" with today: "+todayDate+" = "+date.compareTo(todayDate));
		return date.compareTo(todayDate);
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
		return response.readEntity(ListMeasureType.class);
	}
	
	/**
	 * GET /person/{personId}/measure/{measureId}
	 * Return the measure with {measureId}
	 * @return MeasureType a measure
	 */
	@GET
	@Path("/measure/{measureId}")
	@Produces( MediaType.APPLICATION_JSON )
	public MeasureType getMeasureById(@PathParam("measureId") BigInteger measureId) {
		System.out.println("getMeasureById: Reading Measures for idPerson "+ this.idPerson +"...");
		Response response = service.path(path+"/measure").request().accept(mediaType).get(Response.class);
		System.out.println(response);
		ListMeasureType lMeasure = response.readEntity(ListMeasureType.class);
		 for(MeasureType m : lMeasure.getMeasure()){
			 if(m.getIdMeasure().compareTo(measureId) == 0){
				 return m; 
			 }
		 }
		 return null;
	}
	
	/**
	 * GET /person/{personId}/measure/{measureId}/check
	 *  
	 * Check if the target is achieved for the measure passed as param
	 * @throws ParseException 
	 * 
	 */
	@GET
	@Path("/measure/{measureId}/check")
	@Produces( MediaType.TEXT_PLAIN )
	public Boolean checkMeasureWithTarget(@PathParam("measureId") BigInteger measureId) throws ParseException {
		System.out.println("checkMeasureWithTarget: Checking measure "+ measureId +" for idPerson "+ this.idPerson +"...");
		MeasureType measure = getMeasureById(measureId);
		ListTargetType listTargets = readTargetsByMeasureDef(measure.getMeasureDefinition().getIdMeasureDef());
		Boolean result = false;
		if(listTargets.getTarget().size() > 0){
			for(TargetType target : listTargets.getTarget()){
				int count = Integer.compare(Integer.parseInt(measure.getValue()), target.getValue());
				String cond = target.getConditionTarget().replaceAll("\\s","");
				//conditionTarget is set and the target is not expired
				if (target.getConditionTarget() != null && 
						compareDateWithToday(target.getEndDateTarget()) >= 0 &&
						target.isAchieved() == false) {
					if( (cond.equals("<") && count <  0) || (cond.equals("<=") && count <= 0) ||
						(cond.equals("=") && count == 0) || (cond.equals(">") && count > 0) ||
						(cond.equals(">=") && count >= 0)){
						target.setAchieved(true);
						updateTarget(target);
						result = true;
					}else
						result = false;
				}
			}
		}
		return result;
	}

	private Integer updateTarget(TargetType target) {
		System.out.println("updateTarget: Update target "+ target.getIdTarget() +"...");
		Response response = service.path(path+"/target/"+target.getIdTarget()).request().accept(mediaType).put(Entity.entity(target, mediaType)); ;
		System.out.println(response);
		return response.readEntity(Integer.class);		
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
				//check if the reminder is expired
				if ( this.compareDateWithToday(re.getExpireReminder()) >= 0 ){
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
	@Produces( MediaType.TEXT_PLAIN )
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
        	 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
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
        	 
        	 
        	 System.out.println("Create reminder: " + quote_reminder.getCreateReminder());
        	 System.out.println("Expire reminder: " + quote_reminder.getExpireReminder());
        	 System.out.println("Relevance reminder: " + quote_reminder.getRelevanceLevel());
        	 System.out.println("text reminder: " + quote_reminder.getText());
        	 System.out.println("Object quote_reminder: " + quote_reminder.toString());
        	 
        	 
        	 
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
     * This method is used to get the current weather data for one location.
     * 
     * GET /person/{personId}/weather
     * 
     * @param city location and nation code for which get current weather data
     * @param metric type of units to use for measure
     * @param json type of return data
     * @return jsonWeather 
     */
    @GET
    @Path("/weather")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWeatherTest(@QueryParam("city") String city, @QueryParam("units") String metric,
    		@QueryParam("mode") String json) {
    	try{
    	//http://127.0.1.1:5700/sdelab/person/weather?city=Trento,it&units=metric&mode=json
    	ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		//WebTarget service_weather = client.target(getBaseURIWeatherTest());
		WebTarget service_weather = client.target("https://ss-serene-hamlet-9690.herokuapp.com/sdelab/services/weather")
				.queryParam("city", city)
				.queryParam("units", metric)
				.queryParam("mode", json);
				
        System.out.println("City: " + city);
        System.out.println("Metric: " + metric);
        System.out.println("mode: " + json);
		System.out.println("Service to string  adding path" + service_weather.toString());
		
		
		
		//this.service_weather = service_weather;
		
		System.out.println("Service to string  after this.service_weater adding path" + service_weather.toString());
		
    	//String path = "http://api.openweathermap.org/data/2.5/find?q=Trento,it&units=metric&mode=json&appid=a3dbf2f9a2ab9c24905f3ea44cb9e265";
    	
		Response response_weather = service_weather.request().accept(MediaType.APPLICATION_JSON).get(Response.class);
		System.out.println("response_weather: " + response_weather );
        //System.out.println(weather);
        System.out.println("Service_weather after adding path: " + service_weather.toString());
        
        
        String jsonWeather = response_weather.readEntity(String.class);
        JSONObject weather_data = new JSONObject(jsonWeather);
        
        System.out.println("Content of weather data: " + weather_data);
        String condition = weather_data.get("Condition").toString();
        String pressure = weather_data.get("Pressure").toString();
        String humidity = weather_data.get("Humidity").toString();
        String temp_min = weather_data.get("Temperature min").toString();
        String temp_max = weather_data.get("Temperature max").toString();
        
        
        String rain = "Rain";
        if(condition.equals(rain) != true){
        	System.out.println("Is not raining");
        	weather_data.put("Motivation", "Is not raining !!! Come on lets go outside to run !");
        }else{
        	System.out.println("Is raining");
        	weather_data.put("Motivation", "Doesn't matter if is raining...do some exercices at home !");
        }
        String weather_with_motivation = weather_data.toString();
        
        
        if(response_weather.getStatus() != 200){
        	System.out.println("Error in external service");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
       				.entity(externalErrorMessage(weather_with_motivation)).build();
            }else{
            	System.out.println("jsonWeather: " + weather_with_motivation );
            	return Response.ok(weather_with_motivation).build();
            }
    	}catch(Exception e){
    		System.out.print("Error Cath motivation");
    		return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
    				.entity(errorMessage(e)).build();
    	}
    }
	
	
	
    /**
     * This method is used to get weather forecast for 5 days 
     * with data every 3 hours by city name.
     * 
     * 
     * @param city location and nation code for which get forecast weather data
     * @param metric type of units to use for measure
     * @param json type of return data
     * @return jsonWeather
     */
    @GET
    @Path("/forecast")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getForeCast(@QueryParam("city") String city, @QueryParam("units") String metric,
    		@QueryParam("mode") String json) {
    	try{
    	//http://127.0.1.1:5700/sdelab/person/forecast?city=Trento,it&units=metric&mode=json
    	
    	//http://api.openweathermap.org/data/2.5/forecast?q=Trento,it&units=metric&mode=json&appid=2de143494c0b295cca9337e1e96b00e0
    	ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		//WebTarget service = client.target(getBaseURIForecast());
		WebTarget service_forecast = client.target("https://ss-serene-hamlet-9690.herokuapp.com/sdelab/services/weather")
				.queryParam("city", city)
				.queryParam("units", metric)
				.queryParam("mode", json);
		//this.service_forecast = service;
		
		
        System.out.println("Service to string" + service_forecast.toString());
        Response response_forecast = service_forecast.request().accept(MediaType.APPLICATION_JSON).get(Response.class);
        String jsonForecast = response_forecast.readEntity(String.class);
        
        
        
        JSONObject forecast_data = new JSONObject(jsonForecast);
        
        System.out.println("Content of weather data: " + forecast_data);
        String condition = forecast_data.get("Condition").toString();
        String pressure = forecast_data.get("Pressure").toString();
        String humidity = forecast_data.get("Humidity").toString();
        String temp_min = forecast_data.get("Temperature min").toString();
        String temp_max = forecast_data.get("Temperature max").toString();
        
        
        String rain = "Rain";
        if(condition.equals(rain) != true){
        	System.out.println("Tomorrow will not rain...");
        	forecast_data.put("Motivation", "Wowowow tomorrow will not rain !!! Be ready to do some workout !");
        }else{
        	System.out.println("Is raining");
        	forecast_data.put("Motivation", "Doesn't matter if tomorrow will rain...you will be able to do some exercices at home !");
        }
        String forecast_with_motivation = forecast_data.toString();
        System.out.println("Forecast with motivation: " + forecast_with_motivation);
        
        if(response_forecast.getStatus() != 200){
        	System.out.println("Error in external service");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
       				.entity(externalErrorMessage(forecast_with_motivation)).build();
            }else{
            	System.out.println("jsonGetRandom: " + forecast_with_motivation );
            	return Response.ok(forecast_with_motivation).build();
            }
    	}catch(Exception e){
    		System.out.print("Error Cath motivation");
    		return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
    				.entity(errorMessage(e)).build();
    	}
        
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
	 * GET /person/{personId}/target
	 * Return list of target for person with id=personId
	 * @return ListTargetType list of targets
	 */
	@GET
	@Path("/target")
	@Produces( MediaType.APPLICATION_JSON )
	public ListTargetType readTargets() {
		System.out.println("readTargets: Reading targets for idPerson "+ this.idPerson +"...");
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
	

	/**
	 * GET /person/{personId}/target/{measureDefinitionId}
	 * Return list of target for person with id = personId and
	 * referring to measureDefinition = measureDefId
	 * @return ListTargetType list of targets
	 */
	@GET
	@Path("/target/{measureDefId}")
	@Produces( MediaType.APPLICATION_JSON )
	public ListTargetType readTargetsByMeasureDef(@PathParam("measureDefId") BigInteger measureDefId) {
		System.out.println("readTargetsByMeasureDef: Reading targets for idPerson "+ this.idPerson +"...");
		Response response = service.path(path+"/target/"+measureDefId).request().accept(mediaType).get(Response.class);
		System.out.println(response);
		return response.readEntity(ListTargetType.class);
	}
}