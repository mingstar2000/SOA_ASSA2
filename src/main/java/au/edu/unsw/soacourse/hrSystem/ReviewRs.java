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

import au.edu.unsw.soacourse.hrSystem.dao.ReviewDao;
import au.edu.unsw.soacourse.hrSystem.model.Review;

@Path("/Review")
public class ReviewRs {
	ReviewDao ReviewDao = new ReviewDao();
		@Context
		UriInfo uriInfo;
		@Context 
		Request request;
		
		//get all reviews
		//TODO: consider response		
		@GET
		@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
		public List<Review> getReviews() {
		List<Review> rs = ReviewDao.get();
		if(rs==null) {
			throw new RuntimeException("GET: reviews not found");
		}	
		return rs;
		}

		//get specific review
		//TODO: consider response		
		@GET
		@Path("{revID}")
		@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
		public Review getReview(@PathParam("revID") String revID) {
		Review r = ReviewDao.getReview(revID);
		if(r==null) {
			throw new RuntimeException("GET: review with" + revID +  " not found");
		}	
		return r;
		}

		//get specific application
		//TODO: consider response		
		@GET
		@Path("/Application/{appID}")
		@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
		public List<Review> getApplication(@PathParam("appID") String appID) {
		List<Review> as = ReviewDao.getApp(appID);
		if(as==null) {
			throw new RuntimeException("GET: applications with" + appID +  " not found");
		}	
		return as;
		}
		
		//get specific reviewer
		//TODO: consider response		
		@GET
		@Path("/Reviewer/{userID}")
		@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
		public List<Review> getReviewer(@PathParam("userID") String userID) {
		List<Review> rs = ReviewDao.getRev(userID);
		if(rs==null) {
			throw new RuntimeException("GET: reviewers with" + userID +  " not found");
		}	
		return rs;
		}
		
	    // create new review
		//TODO: consider response
		//TODO: precondition.....the results of auto-checks -> how to know????
		@POST
		@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
		@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
		public Review newReview(
				@FormParam("appID") String appid,
				@FormParam("userID") String userid,
				@FormParam("reStatus1") String reStatus1,
				@FormParam("reStatus2") String reStatus2,
				@FormParam("magStatus") String magStatus
		) throws IOException {

			//create id using maximum number of revid
			String revid = String.valueOf(Integer.valueOf(ReviewDao.max())+1);
			System.out.println(revid);

			Review r = new Review(revid,appid,userid,reStatus1,reStatus2,magStatus);

			ReviewDao.post(r);
			
	        return r;
		}

}
