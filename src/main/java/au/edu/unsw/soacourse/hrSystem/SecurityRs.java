package au.edu.unsw.soacourse.hrSystem;

public class SecurityRs {
	
	//check authentication and authorization 
			public static int checkSecurity(String SecurityKey, String ShortKey, String annotation){
				
				//For authentication
				//TODO: check correct code number (what number????)
				if(SecurityKey.equals("i-am-foundit")==false)
					return 401;
				//For authorization
				if(annotation.equals("GET") | annotation.equals("POST") | annotation.equals("PUT") | annotation.equals("DELETE")){
					if(ShortKey.equals("app-candidate")==false)
						return 403; //Forbidden
				}
				return 200;
			}

	
	

}