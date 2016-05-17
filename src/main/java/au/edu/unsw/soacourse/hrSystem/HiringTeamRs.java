package au.edu.unsw.soacourse.hrSystem;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import au.edu.unsw.soacourse.hrSystem.dao.HiringTeamDao;
import au.edu.unsw.soacourse.hrSystem.model.CompanyProfile;
import au.edu.unsw.soacourse.hrSystem.model.HiringTeam;
import au.edu.unsw.soacourse.hrSystem.model.Review;


//TODO:Find a bug: return value error -> no cmpID....??? 

@Path("/HiringTeam")
public class HiringTeamRs {
	// Return the list of books for client applications/programs
	HiringTeamDao hiringTeamDao = new HiringTeamDao();
		@Context
		UriInfo uriInfo;
		@Context 
		Request request;
		
		//get hiring team members of specific company
		//TODO: consider response	
		@GET
		@Path("{cmpID}")
		@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
		public Response getHiringTeam(
				@HeaderParam("SecurityKey") String SecurityKey, 
				@HeaderParam("ShortKey") String ShortKey, 
				@PathParam("cmpID") String cmpID) {

		//check the SecurityKey and ShortKey
		int ret_code = checkSecurity(SecurityKey, ShortKey, "GET");
		if (ret_code!= 200) return Response.status(ret_code).build();
			
		List<HiringTeam> hs = hiringTeamDao.get(cmpID);
		if(hs==null) {
			throw new RuntimeException("GET: hiringTeam with" + cmpID +  " not found");
		}

		GenericEntity<List<HiringTeam>> entity = new GenericEntity<List<HiringTeam>>(hs) {};
		
		return Response.ok(entity).build();		
		}

		//get specific hiring team member of specific company
		//TODO: consider response		
		@GET
		@Path("TeamMember/{cmpID}/{userID}")
		@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
		public Response getHiringTeam(
				@HeaderParam("SecurityKey") String SecurityKey, 
				@HeaderParam("ShortKey") String ShortKey,
				@PathParam("cmpID") String cmpID, @PathParam("userID") String userID) {
		
		System.out.println("company ID = " + cmpID + " and user ID = " + userID);	
		
		//check the SecurityKey and ShortKey
		int ret_code = checkSecurity(SecurityKey, ShortKey, "GET");
		if (ret_code!= 200) return Response.status(ret_code).build();
		
		HiringTeam h = hiringTeamDao.getMember(cmpID, userID);
		if(h==null) {
			throw new RuntimeException("GET: hiringTeam with" + cmpID + " and user ID = " + userID + " not found");
		}	
		return Response.ok(h).build();
		}

	    // update hiringTeam
		//TODO: consider response
		@PUT
		@Path("TeamMember/{cmpID}/{userID}")
		@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
		public Response putHiringTeam(
				@HeaderParam("SecurityKey") String SecurityKey, 
				@HeaderParam("ShortKey") String ShortKey,
				@PathParam("cmpID") String cmpID,
				@PathParam("userID") String userID,
				@FormParam("password") String password,
				@FormParam("name") String name,
				@FormParam("skills") String skills
		) throws IOException, URISyntaxException {
								
			//check the SecurityKey and ShortKey
			int ret_code = checkSecurity(SecurityKey, ShortKey, "PUT");
			if (ret_code!= 200) return Response.status(ret_code).build();
			
			HiringTeam h = new HiringTeam(userID, password,cmpID,name);
			if (skills!=null) h.setSkills(skills);

			//if the id doesn't exist, create new hiringTeam member
			if(hiringTeamDao.getMember(h.getCmpID(), h.getUserID()) == null){
				if (hiringTeamDao.post(h)==true){
					URL uri = new URL(uriInfo.getAbsolutePath().toURL()+"/TeamMember/"+cmpID+"/"+userID);
					return Response.seeOther(uri.toURI()).build();
				}
				else
					return Response.ok().build();
			}
			else {
				hiringTeamDao.put(h);
				return Response.ok(h).build();
			}
		}
		
		
	    // create new hiringTeam member
		//TODO: consider response
		@POST
		@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
		@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
		public Response newHiringTeam(
				@HeaderParam("SecurityKey") String SecurityKey, 
				@HeaderParam("ShortKey") String ShortKey,
				@FormParam("password") String password,
				@FormParam("cmpID") String cmpID,
				@FormParam("name") String name,
				@FormParam("skills") String skills
		) throws IOException, URISyntaxException {

			//check the SecurityKey and ShortKey
			int ret_code = checkSecurity(SecurityKey, ShortKey, "POST");
			if (ret_code!= 200) return Response.status(ret_code).build();
			
			//create userid using maximum number of userid of the cmpid
			String userID = String.valueOf(Integer.valueOf(hiringTeamDao.max(cmpID))+1);
			System.out.println(userID);

			HiringTeam h = new HiringTeam(userID,password,cmpID, name);	
			if (skills!=null) h.setSkills(skills);
			
			if (hiringTeamDao.post(h)==true){
				URL uri = new URL(uriInfo.getAbsolutePath().toURL()+"/TeamMember/"+cmpID+"/"+userID);
				return Response.seeOther(uri.toURI()).build();
			}
			else
				return Response.ok().build();
		
		}

		//delete all hiringTeam members of specific company
		@DELETE	
		@Path("{cmpID}")
		public Response deleteHirTeam(
				@HeaderParam("SecurityKey") String SecurityKey, 
				@HeaderParam("ShortKey") String ShortKey,
				@PathParam("cmpID") String cmpID) {
			
			//check the SecurityKey and ShortKey
			int ret_code = checkSecurity(SecurityKey, ShortKey, "DELETE");
			if (ret_code!= 200) return Response.status(ret_code).build();
			
			HiringTeam h = (HiringTeam) hiringTeamDao.delete(cmpID);
			//TODO: consider response
			if(h != null)
				throw new RuntimeException("DELETE: HiringTeam with cmpID =" + cmpID + " not found");
			
			return Response.ok().build();
		}

		//delete specific hiringTeam member
		@DELETE	
		@Path("TeamMember/{cmpID}/{userID}")
		public Response deleteHirTeamMember(
				@HeaderParam("SecurityKey") String SecurityKey, 
				@HeaderParam("ShortKey") String ShortKey,
				@PathParam("cmpID") String cmpID, 
				@PathParam("userID") String userID) {
			
			//check the SecurityKey and ShortKey
			int ret_code = checkSecurity(SecurityKey, ShortKey, "DELETE");
			if (ret_code!= 200) return Response.status(ret_code).build();
				
			HiringTeam h = (HiringTeam) hiringTeamDao.deleteMember(cmpID, userID);
			//TODO: consider response
			if(h != null)
				throw new RuntimeException("DELETE: HiringTeam with cmpID =" + cmpID + "and userID="+ userID + " not found");
			
			return Response.ok().build();
		}
		
		//check authentication and authorization 
		private int checkSecurity(String SecurityKey, String ShortKey, String annotation){
			
			//For authentication
			//TODO: check correct code number (what number????)
			if(SecurityKey.equals("i-am-foundit")==false)
				return 401;
			//For authorization
			if(annotation.equals("GET") | annotation.equals("POST") | annotation.equals("PUT") | annotation.equals("DELETE")){
				if(ShortKey.equals("app-manager")==false)
					return 403; //Forbidden
			}
			return 200;
		}
				
}
