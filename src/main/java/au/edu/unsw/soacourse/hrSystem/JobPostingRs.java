package au.edu.unsw.soacourse.hrSystem;


import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
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
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import au.edu.unsw.soacourse.hrSystem.dao.ApplicationDao;
import au.edu.unsw.soacourse.hrSystem.dao.JobPostingDao;
import au.edu.unsw.soacourse.hrSystem.model.HypermediaLink;
import au.edu.unsw.soacourse.hrSystem.model.JobPosting;


@Path("/jobPosting")
public class JobPostingRs {


	
	JobPostingDao jobPostingDao = new JobPostingDao();
	ApplicationRs applicationRs = new ApplicationRs();
	CompanyProfileRs companyProfileRs = new CompanyProfileRs();
	ApplicationDao applicationDao = new ApplicationDao();
		@Context
		UriInfo uriInfo;
		@Context
		Request request;


		

		
		@GET
		@Path("/search")
		@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
		public Response getJob(
				@HeaderParam("SecurityKey") String SecurityKey, 
				@HeaderParam("ShortKey") String ShortKey,
				@QueryParam("jobID") String jobID,
				@QueryParam("cmpID") String cmpID,
				@QueryParam("name") String name,
				@QueryParam("salaryRate") String salaryRate,
				@QueryParam("posType") String posType,
				@QueryParam("location") String location,
				@QueryParam("status") String status,
				@QueryParam("jobDsp") String jobDsp) 
						throws MalformedURLException {
			System.out.println("查询开始了！"+SecurityKey+ShortKey);
			int ret_code = SecurityRs.checkSecurity(SecurityKey, ShortKey, "GET");
			int ret_code1 = SecurityRs.checkSecurityM(SecurityKey, ShortKey, "GET");
			System.out.println("security code"+ret_code+"retcode1"+ret_code1);
			if ((ret_code != 200) && (ret_code1!= 200))return Response.status(ret_code1).build();
			
			
			List<JobPosting> jobPostings = new ArrayList<JobPosting>();
			jobPostings = (List<JobPosting>) jobPostingDao.select(jobID, cmpID, name, salaryRate, posType, location, status, jobDsp);
			if(jobPostings==null) {
				
				System.out.println("nothing了！");
				ResponseBuilder builder = Response.status(Status.BAD_REQUEST); 
				builder.type("text/html"); 
				builder.entity("<h3>The jobPsoting jobID= " + jobID + ",cmpID="+cmpID+",name="+name+",salaryRate ="
						+salaryRate+",posType="+posType+",location="+location+",status="+status+" Not Found</h3>"); 
				throw new WebApplicationException(builder.build()); 
			}
			for( JobPosting jp:jobPostings){
				
			    HypermediaLink linkToSelf = new HypermediaLink();
			    HypermediaLink linkToNext = new HypermediaLink();
			    HypermediaLink linkToCmp = new HypermediaLink();
			    String hrefRoot = javax.ws.rs.core.Link.fromResource(applicationRs.getClass()).build().getUri().toString();
			    String baseUri = uriInfo.getBaseUri().toString();
			    if(baseUri.endsWith("/") == true) baseUri = baseUri.substring(0,baseUri.length()-1);
			    //next
			    //String href = javax.ws.rs.core.Link.fromMethod(applicationRs.getClass(),"postApplication").build().getUri().toString();
			    String kk = javax.ws.rs.core.Link.fromMethod(applicationRs.getClass(),"postApplication").param("jobID", jp.getJobID()).build().getUri().toString();
			     
			    linkToNext.setRel("post");
			    linkToNext.setHref(baseUri+hrefRoot+kk);
			    System.out.println("test the hypermedia1!"+ linkToNext.getHref());
			    jp.addHypermediaLink(linkToNext);
			    //company
			    String link = javax.ws.rs.core.Link.fromMethod(companyProfileRs.getClass(),"getCompanyProfile").build().getUri().toString();
			    String linkRoot = javax.ws.rs.core.Link.fromResource(companyProfileRs.getClass()).build().getUri().toString();
			     
			    linkToCmp.setRel("get");
			    linkToCmp.setHref(baseUri+linkRoot+link+"?cmpID="+jp.getCmpID().toString());
			    System.out.println("test the hypermedia2!"+ linkToCmp.getHref());
			   
			    jp.addHypermediaLink(linkToCmp);
			    //self
			    linkToSelf.setRel("self");
			    String url =uriInfo.getAbsolutePath().toURL().toString()+"?jobID="+jp.getJobID().toString();
			    linkToSelf.setHref(url);
			    jp.addHypermediaLink(linkToSelf);
			    
			    System.out.println("still ok?");
			}
			GenericEntity<List<JobPosting>> jobP = new GenericEntity<List<JobPosting>>(jobPostings){};
			return Response.ok(jobP).build() ;
		}
		
		@PUT
		@Path("{jobID}")
		@Consumes({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
		public Response putJob(
				@HeaderParam("SecurityKey") String SecurityKey, 
				@HeaderParam("ShortKey") String ShortKey,
				@PathParam("jobID") String jobID,
				@FormParam("cmpID") String cmpID,
				@FormParam("name") String name,
				@FormParam("salaryRate") String salaryRate,
				@FormParam("posType") String posType,
				@FormParam("location") String location,
				@FormParam("status") String status,
				@FormParam("jobDsp") String jobDsp) throws MalformedURLException, URISyntaxException {
			
			int ret_code = SecurityRs.checkSecurity(SecurityKey, ShortKey, "PUT");
			int ret_code1 = SecurityRs.checkSecurityM(SecurityKey, ShortKey, "PUT");
			if ((ret_code != 200) && (ret_code1!= 200))return Response.status(ret_code1).build();
			
			JobPosting b =new JobPosting();
			b.setJobID(jobID);
			b.setJobDsp(jobDsp);
			b.setLocation(location);
			b.setName(name);
			b.setPosType(posType);
			b.setSalaryRate(salaryRate);
			b.setStatus(status);
			return putAndGetResponse(b);
			//TODO: Fix here so that it returns the updated book
		}

		private Response putAndGetResponse(JobPosting b) throws MalformedURLException, URISyntaxException {
			ResponseBuilder res;
			System.out.println("inside putAndGetResponseID is "+b.getJobID()+"\n");

			List<JobPosting> jobPostings = new ArrayList<JobPosting>();
			
			jobPostings = (List<JobPosting>)jobPostingDao.select(b.getJobID(), null, null, null, null, null, null, null);
		
			//System.out.println("job is  "+jobPostings.get(0).getJobID()+jobPostings.get(0).getStatus()+jobPostings.get(0).getName());
			if ( jobPostings == null){
				jobPostingDao.insert(b);
				String url =uriInfo.getAbsolutePath().toURL().toString()+"/search/?jobID="+b.getJobID().toString();
				res = Response.ok(b).link(url, "self"); 
			
			} else if (jobPostings.get(0).getStatus().equals(ConstParam.STAOPEN) ) {
				JobPosting jb = jobPostings.get(0);
				if(b.getCmpID() == null || b.getCmpID().isEmpty())
				{
					b.setCmpID(jb.getCmpID());
				}
				if(b.getJobDsp()== null || b.getJobDsp().isEmpty())
				{
					b.setJobDsp(jb.getJobDsp());
				}
				if(b.getLocation() == null || b.getLocation().isEmpty())
				{
					b.setLocation(jb.getLocation());
				}
				if(b.getName() == null || b.getName().isEmpty())
				{
					b.setName(jb.getName());
				}
				if(b.getPosType() == null || b.getPosType().isEmpty())
				{
					b.setPosType(jb.getPosType());
				}
				if(b.getSalaryRate() == null || b.getSalaryRate().isEmpty())
				{
					b.setSalaryRate(jb.getSalaryRate());
				}
				if(b.getStatus() == null || b.getStatus().isEmpty())
				{
					b.setStatus(jb.getStatus());
				}
				jobPostingDao.update(b);
				String url =uriInfo.getAbsolutePath().toURL().toString()+"/search/?jobID="+jobPostings.get(0).getJobID().toString();
				res = Response.ok(b).link(url, "self"); 
			} else {
				System.out.println("GET: application's status is "+ConstParam.STAOPEN+"\n");
				System.out.println("GET: application's status is "+jobPostingDao.select(b.getJobID(), null, null, null, null, null, null,null).get(0).getStatus()+"\n");
			
				ResponseBuilder builder = Response.status(Status.BAD_REQUEST); 
				builder.type("text/html"); 
				builder.entity("<h3>The jobPsoting jobID= " + jobPostings.get(0).getJobID() + " Not Found</h3>"); 
				throw new WebApplicationException(builder.build()); 
			}
			
			return res.build();
		}
		
		
		@POST
		@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
		public Response postJob(
				@HeaderParam("SecurityKey") String SecurityKey, 
				@HeaderParam("ShortKey") String ShortKey,
				@FormParam("cmpID") String cmpID,
				@FormParam("name") String name,
				@FormParam("salaryRate") String salaryRate,
				@FormParam("posType") String posType,
				@FormParam("location") String location,
				@FormParam("status") String status,
				@FormParam("jobDsp") String jobDsp
		) throws IOException {
			System.out.println("开始了！");
			System.out.println("cmpID ="+cmpID);
			System.out.println("name ="+name);
			
			int ret_code1 = SecurityRs.checkSecurityM(SecurityKey, ShortKey, "POST");
			if (ret_code1 != 200)return Response.status(ret_code1).build();
			if(cmpID==null || name==null){
				ResponseBuilder builder = Response.status(Status.BAD_REQUEST );
				builder.type("text/html");
				builder.entity("<h3>jobPosting cmpID or name should not be null</h3>");
				throw new WebApplicationException(builder.build());
			}
			JobPosting b = new JobPosting(cmpID,name,salaryRate, posType,location, status, jobDsp);

			String maxID = jobPostingDao.createID();
			System.out.println("the max id of application is"+maxID+"\n");
			if (maxID == null) { maxID = "0";}
			Integer newID = Integer.parseInt(maxID)+1;
			b.setJobID(newID.toString());
			b.setStatus(ConstParam.STAOPEN);
			jobPostingDao.insert(b);
			String url =uriInfo.getAbsolutePath().toURL().toString()+"/search/?jobID="+newID.toString();
			ResponseBuilder builder = Response.ok(b).link(url, "self"); 
			return builder.build();

		}
		@DELETE	
		@Path("{jobID}")
		public Response deleteApplication(
				@HeaderParam("SecurityKey") String SecurityKey, 
				@HeaderParam("ShortKey") String ShortKey,
				@PathParam("jobID") String jobID) {
			int ret_code1 = SecurityRs.checkSecurityM(SecurityKey, ShortKey, "POST");
			if (ret_code1 != 200)return Response.status(ret_code1).build();
			JobPosting c = jobPostingDao.select(jobID, null, null, null, null, null, null,null).get(0);
			
			if(c == null)
				{
				ResponseBuilder builder = Response.status(Status.BAD_REQUEST );
				builder.type("text/html");
				builder.entity("<h3>jobPosting jobID should not be null</h3>");
				throw new WebApplicationException(builder.build());
				}
			c.setStatus(ConstParam.STACLOSE);
			jobPostingDao.update(c);
			return Response.ok("delete job sucessfully").build();
		}
}
