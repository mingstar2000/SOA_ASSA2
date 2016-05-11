package au.edu.unsw.soacourse.hrSystem;

import java.io.IOException;
import java.util.ArrayList;
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



import au.edu.unsw.soacourse.hrSystem.dao.CompanyProfileDao;
import au.edu.unsw.soacourse.hrSystem.model.CompanyProfile;

@Path("/companyProfile")
public class CompanyProfileRs {
	// Return the list of books for client applications/programs
	 CompanyProfileDao companyProfileDao = new CompanyProfileDao();
		@Context
		UriInfo uriInfo;
		@Context
		Request request;
		
		//get specific company profile
		//TODO: consider response		
		@GET
		@Path("{cmpID}")
		@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
		public CompanyProfile getUserProfile(@PathParam("cmpID") String cmpID) {
			CompanyProfile c = companyProfileDao.get(cmpID);
		if(c==null) {
			throw new RuntimeException("GET: companyProfile with" + cmpID +  " not found");
		}
		return c;
		}

	    // update company profile
		//TODO: consider response
		//TODO: when the id doesn't exist, create new company profile
		@PUT
		@Path("{cmpID}")
		@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
		public Response putCompanyProfile(CompanyProfile c) {
			return putAndGetResponse(c);
			//TODO: Fix here so that it returns the updated book
		}
		private Response putAndGetResponse(CompanyProfile c) {
			Response res;
			if(companyProfileDao.get(c.getId()) != null) {
				res = Response.noContent().build();
			} else {
				res = Response.created(uriInfo.getAbsolutePath()).build();
			}
			companyProfileDao.put(c);
			return res;
		}
		
	    // create new company profile
		//TODO: consider response
		@POST
		@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
		public void newCompanyProfile(
				@FormParam("name") String name,
				@FormParam("email") String email,
				@FormParam("addr") String addr,
				@FormParam("telNum") String telNum,
				@FormParam("indType") String indType,
				@FormParam("webSite") String webSite,
				@FormParam("cmpDsp") String cmpDsp
		) throws IOException {
			CompanyProfile c = new CompanyProfile(name,email);
			if (addr!=null) c.setAddr(addr);
			if (telNum!=null) c.setTelNum(telNum);
			if (indType!=null) c.setIndType(indType);
			if (webSite!=null) c.setWebSite(webSite);
			if (cmpDsp!=null) c.setCmpDsp(cmpDsp);
			companyProfileDao.post(c);
		}

		//delete specific company profile
		@DELETE	
		@Path("{cmpID}")
		public void deleteCompany(@PathParam("cmpID") String cmpID) {
			
			CompanyProfile c = (CompanyProfile) companyProfileDao.delete(cmpID);
			//TODO: consider response
			if(c != null)
				throw new RuntimeException("DELETE: companyProfile with" + cmpID +  " not found");
		}
}