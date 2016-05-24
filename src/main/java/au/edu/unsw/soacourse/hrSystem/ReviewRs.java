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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import au.edu.unsw.soacourse.hrSystem.dao.ReviewDao;
import au.edu.unsw.soacourse.hrSystem.model.CompanyProfile;
import au.edu.unsw.soacourse.hrSystem.model.HypermediaLink;
import au.edu.unsw.soacourse.hrSystem.model.Review;

@Path("/review")
public class ReviewRs {
	ReviewDao ReviewDao = new ReviewDao();
	ApplicationRs applicationRs = new ApplicationRs();
	
		@Context
		UriInfo uriInfo;
		@Context 
		Request request;
		
		//get all reviews
		//TODO: consider response		
		@GET
		@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
		public Response getReviews(
				@HeaderParam("SecurityKey") String SecurityKey, 
				@HeaderParam("ShortKey") String ShortKey) {
			
		//check the SecurityKey and ShortKey
		int ret_code = checkSecurity(SecurityKey, ShortKey, "GET");
		if (ret_code!= 200) return Response.status(ret_code).build();
			
		List<Review> rs = ReviewDao.get();
		if(rs==null) {
			throw new RuntimeException("GET: reviews not found");
		}	
		
		//for hateoas for body
		for( Review r:rs){
			
		    HypermediaLink linkToSelf = new HypermediaLink();
		    String hrefRoot = javax.ws.rs.core.Link.fromResource(getClass()).build().getUri().toString();
	
		    String href = javax.ws.rs.core.Link.fromMethod(getClass(),"getReview").build().getUri().toString();
		    String kk = javax.ws.rs.core.Link.fromMethod(getClass(),"getReview").param("revID", r.getRevID()).build().getUri().toString();
		     
		    linkToSelf.setRel("get");
		    linkToSelf.setHref(hrefRoot+kk);
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
		//TODO: consider response		
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
			throw new RuntimeException("GET: review with" + revID +  " not found");
		}	
		return Response.ok(r).build();
		}

		//get specific application
		//TODO: consider response		
		@GET
		@Path("/getApplication")
		@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
		public Response getApplication(
				@HeaderParam("SecurityKey") String SecurityKey, 
				@HeaderParam("ShortKey") String ShortKey,
				@QueryParam("appID") String appID) {
		
		//check the SecurityKey and ShortKey
		int ret_code = checkSecurity(SecurityKey, ShortKey, "GET");
		if (ret_code!= 200) return Response.status(ret_code).build();
		
		System.out.println("here111111111");
		
		List<Review> as = ReviewDao.getApp(appID);
		if(as==null) {
			throw new RuntimeException("GET: applications with" + appID +  " not found");
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
		    String hrefRoot = javax.ws.rs.core.Link.fromResource(applicationRs.getClass()).build().getUri().toString();
		    System.out.println("hrefRoot111111111111111"+hrefRoot);
		    
		    String href = javax.ws.rs.core.Link.fromMethod(applicationRs.getClass(),"getApplication").build().getUri().toString();
		    String kk = javax.ws.rs.core.Link.fromMethod(applicationRs.getClass(),"getApplication").param("appID", a.getAppID()).build().getUri().toString();
		    System.out.println(kk);
		     
		    linkToSelf.setRel("get");
		    linkToSelf.setHref(hrefRoot+kk);
		    a.addHypermediaLink(linkToSelf);
		}

		//in order to response list, change the type into GenericEntity
		GenericEntity<List<Review>> entity = new GenericEntity<List<Review>>(as) {};
		
		return Response.ok(entity).build();
		}
		
/*		//For hatetoas for header
	    private Link[] getApplicationLinks(String appID) {
    	
	    	//TODO: check if getApplication of ApplicationRs has QueryParam --> otherwise error!!! 
	    	Link application = Link.fromMethod(ApplicationRs.class, "getApplication").rel("get").param("appID", appID).build();;

	    	return new Link[] {application};
	    }
		*/
		
		//get specific reviewer
		//TODO: consider response		
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
			throw new RuntimeException("GET: reviewers with" + userID +  " not found");
		}	
		
		//for hateoas for body
		for(Review r:rs){
			
		    HypermediaLink linkToSelf = new HypermediaLink();
		    String hrefRoot = javax.ws.rs.core.Link.fromResource(applicationRs.getClass()).build().getUri().toString();
	
		    String href = javax.ws.rs.core.Link.fromMethod(applicationRs.getClass(),"getApplication").build().getUri().toString();
		    String kk = javax.ws.rs.core.Link.fromMethod(applicationRs.getClass(),"getApplication").param("appID", r.getAppID()).build().getUri().toString();
		     
		    linkToSelf.setRel("get");
		    linkToSelf.setHref(hrefRoot+kk);
		    r.addHypermediaLink(linkToSelf);
		}

		//in order to response list, change the type into GenericEntity
		GenericEntity<List<Review>> entity = new GenericEntity<List<Review>>(rs) {};
		
		return Response.ok(entity).build();
		}
		
		// update Review by reviewer
		//TODO: consider response
		@PUT
		@Path("/update")
		@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
		public Response putReview(
				@HeaderParam("SecurityKey") String SecurityKey, 
				@HeaderParam("ShortKey") String ShortKey,
				@QueryParam("revID") String revid,
				@FormParam("appID") String appid,
				@FormParam("userID") String userid,
				@FormParam("reStatus1") String reStatus1,
				@FormParam("reStatus2") String reStatus2,
				@FormParam("magStatus") String magStatus
		) throws IOException, URISyntaxException {
						
			//check the SecurityKey and ShortKey
			int ret_code = checkSecurity(SecurityKey, ShortKey, "PUT");
			if (ret_code!= 200) return Response.status(ret_code).build();
			
			Review r = new Review(revid,appid,userid,reStatus1,reStatus2,magStatus);

			//if the revid doesn't exist, create new Review 
			//TODO: basically it's impossible there is no existing revid
			//      and also the reviewer can't create a review because that is responsible for manager
			//      so.....consider what to do then
			if(ReviewDao.getRev(r.getRevID()) == null){
				if (ReviewDao.post(r)==true){
					URL uri = new URL(uriInfo.getAbsolutePath().toURL() +"/"+ revid);
					return Response.seeOther(uri.toURI()).build();
				}
				else
					return Response.ok().build();
			}
			else {
				//TODO: in this case, it is needed to check the review status
				//      if the status is not open (that means the review already has done), return error~~
				// if (r.getStatus().equals("0") == false)
				//	return error~~~
				
				ReviewDao.put(r);
				return Response.ok(r).build();
			}
		}
		
	    // create new review
		//TODO: consider response
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
			
			//TODO: check precondition.....the results of auto-checks 
			//  -> check the value of 'autoStaus' of tb_application using the given appID
			//  -> if the value is 1 (pass), then can go, otherwise return error
			//Application a = ApplicationDao.selectByApp(appID);
			//if (a.getAutoStatus.equals("1") == false)
			// 	return Response.ok().build();
			
			//create id using maximum number of revid
			String revid = ReviewDao.max();
			if (revid == null) revid = "1";
			else revid = String.valueOf(Integer.valueOf(revid)+1);
			
			Review r = new Review(revid,appid,userid,comment,reStatus,magStatus);

			if (ReviewDao.post(r)==true){
				URL uri = new URL(uriInfo.getAbsolutePath().toURL() +"/get?revID="+ revid);
				return Response.seeOther(uri.toURI()).build();
			}
			else
				//TODO: consider the response
				return Response.ok().build();
		}

		//check authentication and authorization 
		private int checkSecurity(String SecurityKey, String ShortKey, String annotation){
			
			//For authentication
			//TODO: check correct code number (what number????)
			if(SecurityKey.equals("i-am-foundit")==false)
				return 401;
			//For authorization
			if(annotation.equals("GET")){
				if(ShortKey.equals("app-manager")==false & ShortKey.equals("app-reviwer")==false)
					return 403; //Forbidden
			}
			if(annotation.equals("POST")){
				if(ShortKey.equals("app-manager")==false)
					return 403; //Forbidden
			}
			if(annotation.equals("PUT")){
				if(ShortKey.equals("app-reviwer")==false)
					return 403; //Forbidden
			}
			return 200;
		}
		
}
