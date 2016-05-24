package au.edu.unsw.soacourse.hrSystem;

import java.io.StringWriter;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/jobalerts")
public class DataServices {
	// Return the list of books for client applications/programs
		
		//get result from XML using XQuery
		@GET
		@Path("/get")
		@Produces({MediaType.APPLICATION_ATOM_XML, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
		public Response getCompanyProfile(
				@HeaderParam("SecurityKey") String SecurityKey, 
				@HeaderParam("ShortKey") String ShortKey, 
				@QueryParam("keyword") String keyword,
				@QueryParam("sort_by") String sort_by){
			
			//check the SecurityKey and ShortKey
			int ret_code = checkSecurity(SecurityKey, ShortKey, "PUT");
			if (ret_code!= 200) return Response.status(ret_code).build();

			StringWriter writer = new StringWriter();
	        String xQuery;
	        if (sort_by==null)
	        	xQuery = String.format("{for $v in /channel/title/text()  where contains($v, \"%s\") return { $v }", keyword);
	        else
	        	//TODO: consider the parameter for ordering but....how to know.....
	        	xQuery = String.format("<jobalerts>{for $v in /channel/title/text() where contains($v, \"%s\") order by $v return <jobtitle> { $v } </jobtitle>}</jobalerts> ", keyword);

	        System.out.println(xQuery);

	        try {
	        	//TODO: change the file path and name
	            DataServiceModule.getXMLResource("C:/cs9322-Prac/t.xml", xQuery, writer);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        String result =  writer.toString();

	        return Response.ok(result).build();
		}	
		
		//check authentication and authorization 
		private int checkSecurity(String SecurityKey, String ShortKey, String annotation){
			
			//For authentication
			//TODO: check correct code number (what number????)
			if(SecurityKey.equals("i-am-foundit")==false)
				return 401;
			//For authorization
			if(annotation.equals("GET")){
				if(ShortKey.equals("app-applicant")==false)
					return 403; //Forbidden
			}
			return 200;
		}
		
		
		
}
