/*
 *    Licensed Materials - Property of IBM
 *    5725-I43 (C) Copyright IBM Corp. 2015. All Rights Reserved.
 *    US Government Users Restricted Rights - Use, duplication or
 *    disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
*/

package jon.javaadapter;

import java.util.List;
import java.util.logging.Logger;
import java.io.IOException;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.http.HttpHost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.ibm.json.java.JSONObject;
import com.worklight.adapters.rest.api.MFPServerOperationException;
import com.worklight.adapters.rest.api.WLServerAPI;
import com.worklight.adapters.rest.api.WLServerAPIProvider;

@Path("/users")
public class JavaAdapterResource {
	/*
	 * For more info on JAX-RS see https://jsr311.java.net/nonav/releases/1.1/index.html
	 */
	private static CloseableHttpClient client;
	private static HttpHost host;
	public static void init() {
	    client = HttpClients.createDefault();
	    host = new HttpHost("developer.ibm.com");
	}	
	
	
	//Define logger (Standard java.util.Logger)
	static Logger logger = Logger.getLogger(JavaAdapterResource.class.getName());

    //Define the server api to be able to perform server operations
    WLServerAPI api = WLServerAPIProvider.getWLServerAPI();

	/* Path for method: "<server address>/JonMobileFirst/adapters/JavaAdapter/users" */
	@GET
	@Produces("application/xml")
	public String hello(){
		//log message to server log
        logger.info("Logging info message...");
        
		return "<title>Hello from the Java REST adapter</title><body>Hello from the Java REST adapter</body>";
	}
		
	/* Path for method: "<server address>/JonMobileFirst/adapters/JavaAdapter/users/{username}" */
	@GET
	@Path("/{username}")
	public String helloUser(@PathParam("username") String name){
		return "Hello " + name;
	}
	
	/* Path for method: "<server address>/JonMobileFirst/adapters/JavaAdapter/users/helloUserQuery?name=value" */
	@GET
	@Path("/helloUserQuery")
	public String helloUserQuery(@QueryParam("username") String name){
		return "Hello " + name;
	}
	
	@GET
	@Path("/sendJsonString")
	public String sendJsonString(@QueryParam("content") String content) {
		return "{ jsonContent: " + "\"" + content + "\"" + "}";
	}
	
	/* Path for method: "<server address>/JonMobileFirst/adapters/JavaAdapter/users/prop" */
	@GET
	@Path("/prop")
	public Response getPropertyValue() throws MFPServerOperationException{
		
		//Get the value of the JNDI configuration property wl.analytics.url
		String analyticsURL = api.getConfigurationAPI().getMFPConfigurationProperty("wl.analytics.url");
		
		return Response.ok("Analytics URL is: "+analyticsURL).build();
	}
	
	/* Path for method: "<server address>/JonMobileFirst/adapters/JavaAdapter/users/{first}/{middle}/{last}?age=value" */
	@POST
	@Path("/{first}/{middle}/{last}")
	public String enterInfo(@PathParam("first") String first, @PathParam("middle") String middle, @PathParam("last") String last,
			@QueryParam("age") int age, @FormParam("height") String height, @HeaderParam("Date") String date){
		return first +" "+ middle + " " + last + "\n" +
				"Age: " + age + "\n" +
				"Height: " + height + "\n" +
				"Date: " + date;
	}
	
	/* Path for method: "<server address>/JonMobileFirst/adapters/JavaAdapter/users/newUsers" */
	@PUT
	@Path("/newUsers")
	public String newUsers(@FormParam("username") List<String> users){
		if(users!=null && users.size() != 0){
			String usersTemp = "";
			int index = 0;
			for (String user : users) {
				usersTemp += " " +user;
				if(index < users.size() - 1 && users.size() != 2) usersTemp += ",";
				if(++index == users.size() -1 && users.size() != 1) usersTemp += " and";
			}
			return "Hello" + usersTemp;
		}
		
		return "Hello";
	}
	
	/* Path for method: "<server address>/JonMobileFirst/adapters/JavaAdapter/users/helloUserBody" */
	@POST
	@Consumes("application/json")
	@Path("/helloUserBody")
	public String testUser(@HeaderParam("Content-Type") String type,  JSONObject json) throws IOException{
		return "Hello " + json.get("first") + " " + json.get("middle") + " " + json.get("last");
	}
		
}
