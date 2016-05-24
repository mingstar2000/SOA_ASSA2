package au.edu.unsw.soacourse.hrSystem;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
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
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.Link.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import au.edu.unsw.soacourse.hrSystem.dao.ApplicationDao;
import au.edu.unsw.soacourse.hrSystem.dao.UserProfileDao;
import au.edu.unsw.soacourse.hrSystem.model.Application;
import au.edu.unsw.soacourse.hrSystem.model.HypermediaLink;
import au.edu.unsw.soacourse.hrSystem.model.JobPosting;
import au.edu.unsw.soacourse.hrSystem.model.UserProfile;
@Path("/userProfile")
public class UserProfileRs {
	// Return the list of books for client applications/programs
	 UserProfileDao userProfileDao = new UserProfileDao();
	 ApplicationRs applicationRs = new ApplicationRs();
		@Context
		UriInfo uriInfo;
		@Context
		Request request;
		
		@GET
		@Path("/{ProfileID}")
		@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON,MediaType.APPLICATION_ATOM_XML})
		public Response getUserProfile(
				@HeaderParam("SecurityKey") String SecurityKey, 
				@HeaderParam("ShortKey") String ShortKey, 
				@PathParam("ProfileID") String userID) throws IOException {
			
			//check the SecurityKey and ShortKey
			int ret_code = SecurityRs.checkSecurity(SecurityKey, ShortKey, "GET");
			if (ret_code!= 200) return Response.status(ret_code).build();
			
			UserProfile b = userProfileDao.get(userID);
			System.out.println("let's start!");
			if(b==null) {
				ResponseBuilder builder = Response.status(Status.BAD_REQUEST); 
				builder.type("text/html"); 
				builder.entity("<h3>The user Profile ID " + userID + " Not Found</h3>"); 
				throw new WebApplicationException(builder.build()); 	
			}
			
			System.out.println("test the hypermedia!");
		    HypermediaLink linkToSelf = new HypermediaLink();
		    String hrefRoot = javax.ws.rs.core.Link.fromResource(applicationRs.getClass()).build().getUri().toString();
		    String href = javax.ws.rs.core.Link.fromMethod(applicationRs.getClass(),"getApplication").build().getUri().toString();
		    linkToSelf.setHref(hrefRoot+href+ "/"+b.getId());		    
		    linkToSelf.setRel("self");
		    b.addHypermediaLink(linkToSelf);
		    System.out.println("still ok?");
		   
		    ResponseBuilder builder = Response.ok(b); 
			return builder.build();
			}
			
		@PUT
		@Path("{ProfileID}")
		@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
		public Response putUserProfile(@PathParam("ProfileID") String ProfileID,UserProfile b) throws MalformedURLException {
			b.setId(ProfileID);
			System.out.println("put 开始了");
			return putAndGetResponse(b);
			//TODO: Fix here so that it returns the updated book
		}
		private Response putAndGetResponse(UserProfile b) throws MalformedURLException  {
			Response res;
			System.out.println("b.name is "+b.getId()+"\n");
			if(userProfileDao.get(b.getId()) != null) {
				System.out.println("1111111");
				userProfileDao.put(b);
				res = Response.ok(uriInfo.getAbsolutePath().toURL().toString()).build();
			} else {
				res =  Response.status(404).build();
			}
			System.out.println(res);
			return res;
		}
		@POST
		@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON,MediaType.APPLICATION_FORM_URLENCODED})
		public Response newUserProfile(
				@FormParam("name") String name,
				@FormParam("email") String email,
				@FormParam("addr") String addr,
				@FormParam("telNum") String telNum,
				@FormParam("curPos") String curPos,
				@FormParam("education") String education,
				@FormParam("skills") String skills,
				@FormParam("experience") String experience,
				@FormParam("perDsp") String perDsp
		) throws IOException, URISyntaxException {
			UserProfile b = new UserProfile(name,email);
			if (addr!=null) b.setAddr(addr);
			if (telNum!=null) b.setTelNum(telNum);
			if (curPos!=null) b.setCurPos(curPos);
			if (education!=null) b.setEducation(education);
			if (skills!=null) b.setSkills(skills);
			if (experience!=null) b.setExp(experience);
			if (perDsp!=null) b.setPerDsp(perDsp);
			String maxID = userProfileDao.create();
			System.out.println("the max id is"+maxID+"\n");
			Integer newID = Integer.parseInt(maxID)+1;
			b.setId(newID.toString());
			userProfileDao.post(b);
			URL uri = new URL(uriInfo.getAbsolutePath().toURL() +"/"+ newID.toString());
			//return Response.seeOther(uri.toURI()).build();
			return Response.ok(b).link(uri.toString(), "self").build();

		}
		//delete specific user profile
		@DELETE	
		@Path("{userID}")
		public void deleteUser(@PathParam("userID") String userID) {
			
			
			UserProfile c = userProfileDao.get(userID);
			
			if(c == null)
				{
				ResponseBuilder builder = Response.status(Status.BAD_REQUEST );
				builder.type("text/html");
				builder.entity("<h3>UserProfile userID can not find</h3>");
				throw new WebApplicationException(builder.build());

				}
		
			userProfileDao.delete(userID);

		}
			
}
