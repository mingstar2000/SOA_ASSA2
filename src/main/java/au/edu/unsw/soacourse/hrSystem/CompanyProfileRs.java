package au.edu.unsw.soacourse.hrSystem;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Properties;

import javax.swing.text.Document;
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
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import au.edu.unsw.soacourse.hrSystem.dao.CompanyProfileDao;
import au.edu.unsw.soacourse.hrSystem.model.CompanyProfile;
import au.edu.unsw.soacourse.hrSystem.model.HypermediaLink;


//TODO: check SecurityKey and ShortKey
//  1. check if SecurityKey is 'i-am-foundit'
//  2. check if ShortKey is proper for the resource
//          e.g, for company profile, ShortKey should be 'app-manager'
//          e.g, for user profile, ShortKey should be 'app-candidate'
//          e.g, for review, ShortKey should be 'app-reviewer'

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
		@Path("/get")
		@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
		public Response getCompanyProfile(
				@HeaderParam("SecurityKey") String SecurityKey, 
				@HeaderParam("ShortKey") String ShortKey, 
				@QueryParam("cmpID") String cmpID) {
		
			//check the SecurityKey and ShortKey
			int ret_code = checkSecurity(SecurityKey, ShortKey, "GET");
			if (ret_code!= 200) return Response.status(ret_code).build();
			
			CompanyProfile c = companyProfileDao.get(cmpID);
			if(c==null) {
				//TODO: what method is proper? and....which type? html/text/xml????
				//throw new RuntimeException("GET: companyProfile with" + cmpID +  " not found");
				ResponseBuilder builder = Response.status(400); 
				builder.type("text/html"); 
				builder.entity("<h3>The Compnay Profile ID " + cmpID + " Not Found</h3>"); 
				throw new WebApplicationException(builder.build()); 		
			}			
			
			 HypermediaLink linkToSelf = new HypermediaLink();
			    String hrefRoot = javax.ws.rs.core.Link.fromResource(getClass()).build().getUri().toString();
			    String href = javax.ws.rs.core.Link.fromMethod(getClass(),"getCompanyProfile").build().getUri().toString();
			    linkToSelf.setHref(hrefRoot+href+ "/"+c.getId());		    
			    linkToSelf.setRel("self");
			    c.addHypermediaLink(linkToSelf);
			
			    ResponseBuilder builder = Response.ok(c); 
				return builder.build();
		}
/*		//For hatetoas for header
	    private Link[] getCompanyProfilelLinks(String cmpID) {

	    	Link putCompanyprofile = Link.fromMethod(CompanyProfileRs.class, "putCompanyProfile").rel("update").param("cmpID", cmpID).build();
	    	Link delCompanyprofile = Link.fromMethod(CompanyProfileRs.class, "delCompanyProfile").rel("delete").param("cmpID", cmpID).build();
	        
	    	return new Link[] {putCompanyprofile, delCompanyprofile};
	    }*/

	    // update company profile
		//TODO: consider response
		@PUT
		@Path("/update")
		@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
		public Response putCompanyProfile(
				@HeaderParam("SecurityKey") String SecurityKey, 
				@HeaderParam("ShortKey") String ShortKey,
				@QueryParam("cmpID") String cmpid,
				@FormParam("name") String name,
				@FormParam("email") String email,
				@FormParam("addr") String addr,
				@FormParam("telNum") String telNum,
				@FormParam("indType") String indType,
				@FormParam("webSite") String webSite,
				@FormParam("cmpDsp") String cmpDsp
		) throws IOException, URISyntaxException {
						

			//check the SecurityKey and ShortKey
			int ret_code = checkSecurity(SecurityKey, ShortKey, "PUT");
			if (ret_code!= 200) return Response.status(ret_code).build();
			
			CompanyProfile c = new CompanyProfile(cmpid, name,email);
			if (addr!=null) c.setAddr(addr);
			if (telNum!=null) c.setTelNum(telNum);
			if (indType!=null) c.setIndType(indType);
			if (webSite!=null) c.setWebSite(webSite);
			if (cmpDsp!=null) c.setCmpDsp(cmpDsp);

			//if the id doesn't exist, create new company profile
			if(companyProfileDao.get(c.getId()) == null){
				if (companyProfileDao.post(c)==true){
					URL uri = new URL(uriInfo.getAbsolutePath().toURL() +"/"+ cmpid);
					return Response.seeOther(uri.toURI()).build();
				}
				else
					return Response.ok("ok").build();
			}
			else {
				companyProfileDao.put(c);
				return Response.ok(c).build();
			}
		}
		
	    // create new company profile
		@POST
		@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
		@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
		public Response newCompanyProfile(
				@HeaderParam("SecurityKey") String SecurityKey, 
				@HeaderParam("ShortKey") String ShortKey,
				@FormParam("name") String name,
				@FormParam("email") String email,
				@FormParam("addr") String addr,
				@FormParam("telNum") String telNum,
				@FormParam("indType") String indType,
				@FormParam("webSite") String webSite,
				@FormParam("cmpDsp") String cmpDsp
		) throws MalformedURLException, URISyntaxException, UnknownHostException {

			
			//check the SecurityKey and ShortKey
			int ret_code = checkSecurity(SecurityKey, ShortKey, "POST");
			if (ret_code!= 200) return Response.status(ret_code).build();
		
			//create id using maximum number of cmpid
			String cmpid = companyProfileDao.max();
			if (cmpid == null) cmpid = "1";
			else cmpid = String.valueOf(Integer.valueOf(cmpid)+1);

			CompanyProfile c = new CompanyProfile(cmpid,name,email);
			
			if (addr!=null) c.setAddr(addr);
			if (telNum!=null) c.setTelNum(telNum);
			if (indType!=null) c.setIndType(indType);
			if (webSite!=null) c.setWebSite(webSite);
			if (cmpDsp!=null) c.setCmpDsp(cmpDsp);
			
			if (companyProfileDao.post(c)==true){
				URL uri = new URL(uriInfo.getAbsolutePath().toURL() +"/get?cmpID="+ cmpid);
				return Response.seeOther(uri.toURI()).build();
			}
			else
				//TODO: consider the response
				return Response.ok("ok").build();
		}

		//delete specific company profile
		//TODO: consider the response....void or a message?
		@DELETE	
		@Path("/delete")
		public Response delCompanyProfile(
				@HeaderParam("SecurityKey") String SecurityKey, 
				@HeaderParam("ShortKey") String ShortKey,
				@QueryParam("cmpID") String cmpID) {
						
			//check the SecurityKey and ShortKey
			int ret_code = checkSecurity(SecurityKey, ShortKey, "DELETE");
			if (ret_code!= 200) return Response.status(ret_code).build();
			
			CompanyProfile c = (CompanyProfile) companyProfileDao.delete(cmpID);
			if(c != null)
				throw new RuntimeException("DELETE: companyProfile with" + cmpID +  " not found");
				
		return Response.ok("ok").build();
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
