package au.edu.unsw.soacourse.hrSystem;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import au.edu.unsw.soacourse.hrSystem.dao.ApplicationDao;
import au.edu.unsw.soacourse.hrSystem.model.Application;
import au.edu.unsw.soacourse.hrSystem.model.HypermediaLink;
import au.edu.unsw.soacourse.hrSystem.model.JobPosting;

@Path("/application")
public class ApplicationRs {
	
	ApplicationDao applicationDao = new ApplicationDao();
		@Context
		UriInfo uriInfo;
		@Context
		Request request;
		
		@GET
		@Path("/search")
		@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
		public Response getApplication(
				@HeaderParam("SecurityKey") String SecurityKey, 
				@HeaderParam("ShortKey") String ShortKey, 
				@QueryParam("appID") String appID,
				@QueryParam("jobID") String jobID,
				@QueryParam("userID") String userID) {
			//check the SecurityKey and ShortKey
			System.out.println("start get in applicationRs");
			int ret_code = SecurityRs.checkSecurity(SecurityKey, ShortKey, "GET");
			int ret_code1 = SecurityRs.checkSecurityM(SecurityKey, ShortKey, "GET");
			if ((ret_code != 200) && (ret_code1!= 200))return Response.status(ret_code1).build();
			System.out.println("start get in applicationRs");
			List<Application> applications = new ArrayList<Application>();
			applications = (List<Application>) applicationDao.select(appID, jobID,
					userID);
			
			if (applications == null) {
				System.out.println("nothing了！");
				ResponseBuilder builder = Response.status(Status.BAD_REQUEST);
				builder.type("text/html");
				builder.entity("<h3>The jobPsoting appID= " + appID + ",jobID="
						+ jobID + ",userID=" + userID + " Not Found</h3>");
				throw new WebApplicationException(builder.build());
			}
			
			for (Application ap : applications) {

				HypermediaLink linkToSelf = new HypermediaLink();
				String href = javax.ws.rs.core.Link
						.fromMethod(getClass(), "getApplication").build().getUri()
						.toString();
				String hrefRoot = javax.ws.rs.core.Link.fromResource(getClass())
						.build().getUri().toString();
				linkToSelf.setRel("get");
				linkToSelf.setHref("http://localhost:8080/FoundITService"
						+ hrefRoot + href + "?appID=" + ap.getAppId());
				System.out.println("test the hypermedia!" + linkToSelf.getHref());
				ap.addHypermediaLink(linkToSelf);

				System.out.println("still ok?");
			}
			System.out.println("查看text" + applications.get(0).getCvLetter());
			GenericEntity<List<Application>> entity = new GenericEntity<List<Application>>(applications) {};

			return Response.ok(entity).build();

		}


		/*@GET
		@Path("/get")
		@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
		public Application getApplication(@QueryParam("appID") String appID) {
			Application b = applicationDao.selectByApp(appID);
		if(b==null) {
			throw new RuntimeException("GET: application by appID with" + appID +  " not found");
		}
		System.out.println("查看text"+ b.getCvLetter());
		return b;
		

		}

		@GET
		@Path("/jobID/{job_id}")
		@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
		public List<Application> getApplicationsbyJob(@PathParam("jobID") String jobID) {
			List<Application> applications = new ArrayList<Application>();
			applications = (List<Application>) applicationDao.selectByJob(jobID);
		if(applications==null) {
			throw new RuntimeException("GET: application by jobID with" + jobID +  " not found");
		}
		return applications;

		}
		
		@GET
		@Path("/userID/")
		@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
		public List<Application> getApplicationsbyUser(@QueryParam("userID") String userID) {
			List<Application> applications = new ArrayList<Application>();
			applications = (List<Application>) applicationDao.selectByUser(userID);
		if(applications==null) {
			throw new RuntimeException("GET: application by userID with" + userID +  " not found");
		}
		return applications;

		}
		*/
		@PUT
		@Path("{appID}")
		@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
		public Response putApplication(@PathParam("appID") String appID,Application b) throws MalformedURLException {
			b.setAppId(appID);
			System.out.println("测试text is" + b.getCvLetter());
			return putAndGetResponse(b);
			//TODO: Fix here so that it returns the updated book
		}
		private Response putAndGetResponse(Application b) throws MalformedURLException {
			Response res;
			System.out.println("inside putAndGetResponseID is "+b.getAppId()+"\n");
			List<Application> applications = new ArrayList<Application>();
			
			applications = (List<Application>)applicationDao.select(b.getAppId(), null, null);
			if (applications == null){
				applicationDao.insert(b);

				String url =uriInfo.getAbsolutePath().toURL().toString()+"/search/?appID="+b.getAppId().toString();
				res = Response.ok(b).link(url, "self").build(); 
				
			}
			 else if(applicationDao.selectByApp(b.getAppId()).getAutoStatus().equals(ConstParam.AUTOSTAOPEN) ) {

				 applicationDao.update(b);
				 String url =uriInfo.getAbsolutePath().toURL().toString()+"/search/?appID="+b.getAppId().toString();
				 res = Response.ok(b).link(url, "self").build(); 
			}
			else{
				res = Response.noContent().build();
				ResponseBuilder builder = Response.status(Status.BAD_REQUEST); 
				builder.type("text/html"); 
				builder.entity("<h3>The application appID= " + applications.get(0).getAppId() + " Not Found</h3>"); 
				throw new WebApplicationException(builder.build()); 
			}
			
			return res;
		}
		
		
		@POST
		@Path("/new")
		@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
		public void postApplication(
				@FormParam("userID") String userID,
				@FormParam("cvLetter") String cvLetter,
				@FormParam("autoStatus") String autoStatus,
				@FormParam("jobID") String jobID
		) throws IOException {
			System.out.println("开始了！");
			if(jobID==null || userID==null){
				throw new RuntimeException("POST: application jobID or userID should not be null");
			}
			Application b = new Application(userID,cvLetter,autoStatus,jobID);

			String maxID = applicationDao.createID();
			System.out.println("the max id of application is"+maxID+"\n");
			if (maxID == null) { maxID = "0";}
			Integer newID = Integer.parseInt(maxID)+1;
			b.setAppId(newID.toString());
			b.setAutoStatus(ConstParam.AUTOSTAOPEN);
			applicationDao.insert(b);

		}
		@DELETE	
		@Path("{appID}")
		public void deleteApplication(@PathParam("appID") String appID) {
			
			Application c = applicationDao.selectByApp(appID);
			
			if(c == null)
				{
				ResponseBuilder builder = Response.status(Status.BAD_REQUEST );
				builder.type("text/html");
				builder.entity("<h3>application appID can not match</h3>");
				throw new WebApplicationException(builder.build());
				}
			c.setAutoStatus(ConstParam.AUTOSTACHIV);
			applicationDao.update(c);
			
			//TODO: consider response
			
		}

}
