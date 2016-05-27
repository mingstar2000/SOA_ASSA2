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
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
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

		@PUT
		@Path("{appID}")
		@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
		public Response putApplication(
				@HeaderParam("SecurityKey") String SecurityKey, 
				@HeaderParam("ShortKey") String ShortKey, 
				@PathParam("appID") String appID,
				@FormParam("cvLetter") String cvLetter,
				@FormParam("autoStatus") String autoStatus)
				throws MalformedURLException {
			//security check
			int ret_code = SecurityRs.checkSecurity(SecurityKey, ShortKey, "PUT");
			int ret_code1 = SecurityRs.checkSecurityM(SecurityKey, ShortKey, "PUT");
			if ((ret_code != 200) && (ret_code1!= 200))return Response.status(ret_code1).build();
			
			Application b = new Application();
			b.setAppId(appID);
			b.setCvLetter(cvLetter);
			b.setAutoStatus(autoStatus);

			System.out.println("测试text is" + b.getCvLetter());
			return putAndGetResponse(b);
			// TODO: Fix here so that it returns the updated book
		}

		private Response putAndGetResponse(Application b)
				throws MalformedURLException {
			Response res;
			System.out.println("inside putAndGetResponseID is " + b.getAppId()
					+ "\n");
			List<Application> applications = new ArrayList<Application>();

			applications = (List<Application>) applicationDao.select(b.getAppId(),
					null, null);
			if (applications == null) {
				applicationDao.insert(b);

				String url = uriInfo.getAbsolutePath().toURL().toString()
						+ "/search/?appID=" + b.getAppId().toString();
				res = Response.ok(b).link(url, "self").build();

			} else if (applications.get(0).getAutoStatus().equals(ConstParam.AUTOSTAOPEN)) {
				Application head = applications.get(0);
				System.out.println("jobID="+head.getJobID()+"userID="+head.getUserID()+"cv="+head.getCvLetter());
				if(b.getJobID() == null || b.getJobID().isEmpty() ) { 
					b.setJobID(head.getJobID());
					}
				if(b.getUserID() == null || b.getUserID().isEmpty() ) {
					b.setUserID(head.getUserID());
				}
				if (b.getCvLetter() == null|| b.getCvLetter().isEmpty() ) {
					b.setCvLetter(head.getCvLetter());
				}
				System.out.println("jobID="+b.getJobID()+"userID="+b.getUserID()+"cv="+b.getCvLetter());
				applicationDao.update(b);
				String url = uriInfo.getAbsolutePath().toURL().toString()
						+ "/search/?appID=" + b.getAppId().toString();
				res = Response.ok(b).link(url, "self").build();
			} else {
				res = Response.noContent().build();
				ResponseBuilder builder = Response.status(Status.BAD_REQUEST);
				builder.type("text/html");
				builder.entity("<h3>The application appID= "
						+ applications.get(0).getAppId() + " Not Found</h3>");
				throw new WebApplicationException(builder.build());
			}

			return res;
		}

		@POST
		@Path("/new")
		@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
		public Response postApplication(
				@HeaderParam("SecurityKey") String SecurityKey, 
				@HeaderParam("ShortKey") String ShortKey, 
				@FormParam("userID") String userID,
				@FormParam("cvLetter") String cvLetter,
				@FormParam("autoStatus") String autoStatus,
				@FormParam("jobID") String jobID) throws IOException {
			
			int ret_code = SecurityRs.checkSecurity(SecurityKey, ShortKey, "POST");
			if (ret_code!= 200) return Response.status(ret_code).build();
			
			System.out.println("开始了！");
			if (jobID == null || userID == null) {
				System.out
						.println("POST: application jobID or userID should not be null");
				throw new RuntimeException(
						"POST: application jobID or userID should not be null");
			}
			Application b = new Application(userID, cvLetter, autoStatus, jobID);

			String maxID = applicationDao.createID();
			System.out.println("the max id of application is" + maxID + "\n");
			if (maxID == null) {
				maxID = "0";
			}
			Integer newID = Integer.parseInt(maxID) + 1;
			b.setAppId(newID.toString());
			b.setAutoStatus(ConstParam.AUTOSTAOPEN);
			if (applicationDao.select(null, jobID, userID) != null) {
				System.out.println("You have alreay applied this job before");
				ResponseBuilder builder = Response.status(Status.BAD_REQUEST);
				builder.type("text/html");
				builder.entity("<h3>You have alreay applied this job before </h3>");
				throw new WebApplicationException(builder.build());

			}
			applicationDao.insert(b);
			return Response.ok("post success").build();

		}

		@DELETE
		@Path("{appID}")
		public Response deleteApplication(
				@HeaderParam("SecurityKey") String SecurityKey, 
				@HeaderParam("ShortKey") String ShortKey, 
				@PathParam("appID") String appID) {
			int ret_code = SecurityRs.checkSecurity(SecurityKey, ShortKey, "PUT");
			if (ret_code!= 200) return Response.status(ret_code).build();
			Application c = applicationDao.selectByApp(appID);

			if (c == null) {
				ResponseBuilder builder = Response.status(Status.BAD_REQUEST);
				builder.type("text/html");
				builder.entity("<h3>application appID can not match</h3>");
				throw new WebApplicationException(builder.build());
			}
			c.setAutoStatus(ConstParam.AUTOSTACHIV);
			applicationDao.update(c);
			return Response.ok("delete success!").build();

			// TODO: consider response

		}
		


}
