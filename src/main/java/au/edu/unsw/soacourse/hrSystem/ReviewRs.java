package au.edu.unsw.soacourse.hrSystem;

import java.io.IOException;
import java.net.MalformedURLException;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import au.edu.unsw.soacourse.hrSystem.dao.ApplicationDao;
import au.edu.unsw.soacourse.hrSystem.dao.ReviewDao;
import au.edu.unsw.soacourse.hrSystem.model.Application;
import au.edu.unsw.soacourse.hrSystem.model.CompanyProfile;
import au.edu.unsw.soacourse.hrSystem.model.HypermediaLink;
import au.edu.unsw.soacourse.hrSystem.model.Review;

@Path("/review")
public class ReviewRs {
	ReviewDao ReviewDao = new ReviewDao();
	ApplicationRs applicationRs = new ApplicationRs();
	ApplicationDao applicationDao = new ApplicationDao();
	
		@Context
		UriInfo uriInfo;
		@Context 
		Request request;
		
		//get all reviews
		@GET
		@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
		public Response getReviews(
				@HeaderParam("SecurityKey") String SecurityKey, 
				@HeaderParam("ShortKey") String ShortKey) throws MalformedURLException {
			
		//check the SecurityKey and ShortKey
		int ret_code = checkSecurity(SecurityKey, ShortKey, "GET");
		if (ret_code!= 200) return Response.status(ret_code).build();
			
		List<Review> rs = ReviewDao.get();
		if(rs==null) {
			System.out.println("Reviews Not Found");
			ResponseBuilder builder = Response.status(Status.NOT_FOUND); 
			builder.type("text/html"); builder.entity("Reviews Not Found"); 
			throw new WebApplicationException(builder.build()); 
		}	
			
		//for hateoas for body
		for( Review r:rs){
			
		    HypermediaLink linkToSelf = new HypermediaLink();
		    String base_uri = uriInfo.getBaseUri().toString();
		    if (base_uri.endsWith("/")==true) base_uri = base_uri.substring(0, base_uri.length()-1);
		    String resource_uri = javax.ws.rs.core.Link.fromResource(getClass()).build().getUri().toString();
		    String method_uri = javax.ws.rs.core.Link.fromMethod(getClass(),"getReview").param("revID", r.getRevID()).build().getUri().toString();
		     
		    linkToSelf.setRel("get");
		    linkToSelf.setHref(base_uri+resource_uri+method_uri);
		    System.out.println("test the hypermedia!"+ linkToSelf.getHref());
		    r.addHypermediaLink(linkToSelf);
		}

		//in order to response list, change the type into GenericEntity
		GenericEntity<List<Review>> entity = new GenericEntity<List<Review>>(rs) {};
		
		return Response.ok(entity).build();
			
		//for header
		//return Response.ok(entity).links(getReviewlLinks(rs)).build();

		}
/*		//For hatetoas for header
		@SuppressWarnings("null")
		private Link[] getReviewlLinks(List<Review> rs) {
	    	
			Link[] review = null;
				
			//for(int i=0; i<rs.size(); i++)
			//	review[i] = Link.fromMethod(ReviewRs.class, "getReview").rel("get").param("revID", rs.get(0).getRevID()).build();

    		return review;
	    }*/
   
		//get specific review
		@GET
		@Path("/get")
		@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
		public Response getReview(
				@HeaderParam("SecurityKey") String SecurityKey, 
				@HeaderParam("ShortKey") String ShortKey,
				@QueryParam("revID") String revID) {
			
		//check the SecurityKey and ShortKey
		int ret_code = checkSecurity(SecurityKey, ShortKey, "GET");
		if (ret_code!= 200) return Response.status(ret_code).build();
		
		Review r = ReviewDao.getReview(revID);
		if(r==null) {
			System.out.println("Review Not Found");
			ResponseBuilder builder = Response.status(Status.NOT_FOUND); 
			builder.type("text/html"); builder.entity("Review Not Found"); 
			throw new WebApplicationException(builder.build()); 
		}	
		return Response.ok(r).build();
		}

		//get specific application
		@GET
		@Path("/getApplication")
		@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
		public Response getApplication(
				@HeaderParam("SecurityKey") String SecurityKey, 
				@HeaderParam("ShortKey") String ShortKey,
				@QueryParam("appID") String appID) throws MalformedURLException {
		
		//check the SecurityKey and ShortKey
		int ret_code = checkSecurity(SecurityKey, ShortKey, "GET");
		if (ret_code!= 200) return Response.status(ret_code).build();
		
		List<Review> as = ReviewDao.getApp(appID);
		if(as==null) {
			System.out.println("Reviews Not Found");
			ResponseBuilder builder = Response.status(Status.NOT_FOUND); 
			builder.type("text/html"); builder.entity("Reviews with " + appID +" Not Found"); 
			throw new WebApplicationException(builder.build()); 
		}	
		System.out.println("here22222");

		if (ShortKey.equals("app_reviwer")){
			//in order to response list, change the type into GenericEntity
			GenericEntity<List<Review>> entity = new GenericEntity<List<Review>>(as) {};
			return Response.ok(entity).build();
		}
		
		System.out.println("here3333");

		//if Shortkey is app_manager, for hateoas, add next uri (update application status for finalizing)
		//for hateoas for body
		for(Review a:as){
			
		    HypermediaLink linkToSelf = new HypermediaLink();
		    
		    String base_uri = uriInfo.getBaseUri().toString();
		    if (base_uri.endsWith("/")==true) base_uri = base_uri.substring(0, base_uri.length()-1);
		    String resource_uri = javax.ws.rs.core.Link.fromResource(applicationRs.getClass()).build().getUri().toString();
		    String method_uri = javax.ws.rs.core.Link.fromMethod(applicationRs.getClass(),"getApplication").param("appID", a.getAppID()).build().getUri().toString();
		     
		    linkToSelf.setRel("get");
		    linkToSelf.setHref(base_uri+resource_uri+method_uri);
		    a.addHypermediaLink(linkToSelf);
		}

		//in order to response list, change the type into GenericEntity
		GenericEntity<List<Review>> entity = new GenericEntity<List<Review>>(as) {};
		
		return Response.ok(entity).build();
		}
		
		//get specific reviewer
		@GET
		@Path("/getReviewer")
		@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
		public Response getReviewer(
				@HeaderParam("SecurityKey") String SecurityKey, 
				@HeaderParam("ShortKey") String ShortKey,
				@QueryParam("userID") String userID) {
			
		//check the SecurityKey and ShortKey
		int ret_code = checkSecurity(SecurityKey, ShortKey, "GET");
		if (ret_code!= 200) return Response.status(ret_code).build();
			
		List<Review> rs = ReviewDao.getRev(userID);
		if(rs==null) {
			System.out.println("Reviewer Not Found");
			ResponseBuilder builder = Response.status(Status.NOT_FOUND); 
			builder.type("text/html"); builder.entity("Reviewer Not Found"); 
			throw new WebApplicationException(builder.build()); 
		}	
		
		//for hateoas for body
		for(Review r:rs){

		    HypermediaLink linkToSelf = new HypermediaLink();
		    String base_uri = uriInfo.getBaseUri().toString();
		    if (base_uri.endsWith("/")==true) base_uri = base_uri.substring(0, base_uri.length()-1);
		    String resource_uri = javax.ws.rs.core.Link.fromResource(applicationRs.getClass()).build().getUri().toString();
		    String method_uri = javax.ws.rs.core.Link.fromMethod(applicationRs.getClass(),"getApplication").param("appID", r.getAppID()).build().getUri().toString();
		     
		    linkToSelf.setRel("get");
		    linkToSelf.setHref(base_uri+resource_uri+method_uri);
		    r.addHypermediaLink(linkToSelf);
		}

		//in order to response list, change the type into GenericEntity
		GenericEntity<List<Review>> entity = new GenericEntity<List<Review>>(rs) {};
		
		return Response.ok(entity).build();
		}
		
		// update Review by reviewer
		@PUT
		@Path("/update")
		@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
		public Response putReview(
				@HeaderParam("SecurityKey") String SecurityKey, 
				@HeaderParam("ShortKey") String ShortKey,
				@QueryParam("revID") String revid,
				@FormParam("appID") String appid,
				@FormParam("userID") String userid,
				@FormParam("comment") String comment,
				@FormParam("reStatus") String reStatus,
				@FormParam("magStatus") String magStatus
		) throws IOException, URISyntaxException {
						
			//check the SecurityKey and ShortKey
			int ret_code = checkSecurity(SecurityKey, ShortKey, "PUT");
			if (ret_code!= 200) return Response.status(ret_code).build();
			
			Review r = new Review(revid,appid,userid,comment,reStatus,magStatus);

			//if the revid doesn't exist, create new Review 
			//TODO: basically it's impossible there is no existing revid
			//      and also the reviewer can't create a review because that is responsible for manager
			//      so.....consider what to do then
			if(ReviewDao.getRev(r.getRevID()) == null){

				// check the status of the applicant (if the status is not pass(1), then cannot create)
				if (checkStaus(appid).equals("1") == false){
					System.out.println("The result of auto-check of the applicant is not pass");
					ResponseBuilder builder = Response.status(Status.PRECONDITION_FAILED); 
					builder.type("text/html"); builder.entity("The result of auto-check of the applicant is not pass"); 
					throw new WebApplicationException(builder.build()); 					
				}

				//create id using maximum number of revid
				revid = ReviewDao.max();
				if (revid == null) revid = "1";
				else revid = String.valueOf(Integer.valueOf(revid)+1);
				r.setRevID(revid);
				
				if (ReviewDao.post(r)==true){
					//URL uri = new URL(uriInfo.getAbsolutePath().toURL() +"/"+ revid);
					URL uri = new URL(uriInfo.getBaseUri().toURL() +"review/get?revID="+ revid);
					System.out.println(uri.toString());
					return Response.seeOther(uri.toURI()).build();
				}
				else{
					System.out.println("Create new review failed");
					ResponseBuilder builder = Response.status(Status.NOT_IMPLEMENTED); 
					builder.type("text/html"); builder.entity("Create new review failed"); 
					throw new WebApplicationException(builder.build()); 
				}
			}
			else {
				// check the status of the applicant (if the status is not pass(1), then cannot update)
				if (checkStaus(appid).equals("1") == false){
					System.out.println("review update failed becuase of status");
					ResponseBuilder builder = Response.status(Status.NOT_MODIFIED); 
					builder.type("text/html"); builder.entity("review update failed becuase of status"); 
					throw new WebApplicationException(builder.build()); 					
				}
				
				if (ReviewDao.put(r) == null){
					System.out.println("review update failed");
					ResponseBuilder builder = Response.status(Status.NOT_MODIFIED); 
					builder.type("text/html"); builder.entity("review update failed"); 
					throw new WebApplicationException(builder.build()); 					
				}
				return Response.ok(r).build();
			}
		}
		
	    // create new review
		@POST
		@Path("/new")
		@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
		@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
		public Response newReview(
				@HeaderParam("SecurityKey") String SecurityKey, 
				@HeaderParam("ShortKey") String ShortKey,
				@FormParam("appID") String appid,
				@FormParam("userID") String userid,
				@FormParam("comment") String comment,
				@FormParam("reStatus") String reStatus,
				@FormParam("magStatus") String magStatus
		) throws IOException, URISyntaxException {

			//check the SecurityKey and ShortKey
			int ret_code = checkSecurity(SecurityKey, ShortKey, "POST");
			if (ret_code!= 200) return Response.status(ret_code).build();
			
			// check the status of the applicant (if the status is not pass(1), then cannot create)
			if (checkStaus(appid).equals("1") == false){
				System.out.println("The result of auto-check of the applicant is not pass");
				ResponseBuilder builder = Response.status(Status.PRECONDITION_FAILED); 
				builder.type("text/html"); builder.entity("The result of auto-check of the applicant is not pass"); 
				throw new WebApplicationException(builder.build()); 					
			}
			
			//create id using maximum number of revid
			String revid = ReviewDao.max();
			if (revid == null) revid = "1";
			else revid = String.valueOf(Integer.valueOf(revid)+1);
			
			Review r = new Review(revid,appid,userid,comment,reStatus,magStatus);

			if (ReviewDao.post(r)==true){
				URL uri = new URL(uriInfo.getBaseUri().toURL() +"review/get?revID="+ revid);
				System.out.println(uri.toString());
				return Response.seeOther(uri.toURI()).build();
			}
			else{
				System.out.println("Create new review failed");
				ResponseBuilder builder = Response.status(Status.NOT_IMPLEMENTED); 
				builder.type("text/html"); builder.entity("Create new review failed"); 
				throw new WebApplicationException(builder.build()); 
			}
		}

		//check the status of the application
		private String checkStaus(String appid){
		
			Application a = applicationDao.selectByApp(appid);
			System.out.println("application autostatus = " + a.getAutoStatus());
			
			return a.getAutoStatus();
		}
		
		//check authentication and authorization 
		private int checkSecurity(String SecurityKey, String ShortKey, String annotation){
			
			//For authentication
			if(SecurityKey.equals("i-am-foundit")==false)
				return 401;
			//For authorization
			if(annotation.equals("GET")){
				if(ShortKey.equals("app-manager")==false & ShortKey.equals("app-reviewer")==false)
					return 403; //Forbidden
			}
			if(annotation.equals("POST")){
				if(ShortKey.equals("app-manager")==false)
					return 403; //Forbidden
			}
			if(annotation.equals("PUT")){
				if(ShortKey.equals("app-reviewer")==false)
					return 403; //Forbidden
			}
			return 200;
		}
		
}
