package au.edu.unsw.soacourse.hrSystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import au.edu.unsw.soacourse.hrSystem.dao.HiringTeamDao;
import au.edu.unsw.soacourse.hrSystem.model.CompanyProfile;
import au.edu.unsw.soacourse.hrSystem.model.HiringTeam;


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
		public List<HiringTeam> getHiringTeam(@PathParam("cmpID") String cmpID) {
		
		List<HiringTeam> hs = hiringTeamDao.get(cmpID);
		if(hs==null) {
			throw new RuntimeException("GET: hiringTeam with" + cmpID +  " not found");
		}
		return hs;
		}

		//get specific hiring team member of specific company
		//TODO: consider response		
		@GET
		@Path("TeamMember/{cmpID}/{userID}")
		@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
		public HiringTeam getHiringTeam(@PathParam("cmpID") String cmpID, @PathParam("userID") String userID) {
		
		System.out.println("company ID = " + cmpID + " and user ID = " + userID);	
			
		HiringTeam h = hiringTeamDao.getMember(cmpID, userID);
		if(h==null) {
			throw new RuntimeException("GET: hiringTeam with" + cmpID + " and user ID = " + userID + " not found");
		}	
		return h;
		}

	    // update hiringTeam
		//TODO: consider response
		@PUT
		@Path("TeamMember/{cmpID}/{userID}")
		@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
		public HiringTeam putHiringTeam(
				@PathParam("cmpID") String cmpID,
				@PathParam("userID") String userID,
				@FormParam("password") String password,
				@FormParam("name") String name,
				@FormParam("skills") String skills
		) throws IOException {
								
			HiringTeam h = new HiringTeam(userID, password,cmpID,name);
			if (skills!=null) h.setSkills(skills);

			//if the id doesn't exist, create new hiringTeam member
			if(hiringTeamDao.getMember(h.getCmpID(), h.getUserID()) == null)
				hiringTeamDao.post(h);
			else
				hiringTeamDao.put(h);
		
		return h;
		}
		
		
	    // create new company profile
		//TODO: consider response
		@POST
		@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
		@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
		public HiringTeam newHiringTeam(
				@FormParam("password") String password,
				@FormParam("cmpID") String cmpID,
				@FormParam("name") String name,
				@FormParam("skills") String skills
		) throws IOException {

			//create userid using maximum number of userid of the cmpid
			String userID = String.valueOf(Integer.valueOf(hiringTeamDao.max(cmpID))+1);
			System.out.println(userID);

			HiringTeam h = new HiringTeam(userID,password,cmpID, name);	
			if (skills!=null) h.setSkills(skills);
			
			hiringTeamDao.post(h);
			
	        return h;
		}

		//delete all hiringTeam members of specific company
		@DELETE	
		@Path("{cmpID}")
		public void deleteHirTeam(@PathParam("cmpID") String cmpID) {
			
			HiringTeam h = (HiringTeam) hiringTeamDao.delete(cmpID);
			//TODO: consider response
			if(h != null)
				throw new RuntimeException("DELETE: HiringTeam with cmpID =" + cmpID + " not found");
		}

		//delete specific hiringTeam member
		@DELETE	
		@Path("TeamMember/{cmpID}/{userID}")
		public void deleteHirTeamMember(@PathParam("cmpID") String cmpID, @PathParam("userID") String userID) {
			
			HiringTeam h = (HiringTeam) hiringTeamDao.deleteMember(cmpID, userID);
			//TODO: consider response
			if(h != null)
				throw new RuntimeException("DELETE: HiringTeam with cmpID =" + cmpID + "and userID="+ userID + " not found");
		}
}
