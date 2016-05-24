package au.edu.unsw.soacourse.hrSystem.dao;

import java.sql.SQLException;
import java.util.List;

import au.edu.unsw.soacourse.hrSystem.model.Application;

public class ApplicationDao {
public static String dbAddr = "jdbc:sqlite:/Users/zhangyun/git/FoundITService/db/foundITServer.db";  
	
	public  Application update(Application application){
	         String sql = "UPDATE  tb_application SET appID ='"+
	        		 application.getAppId()+"', userID ='"+
	        		 application.getUserID()+"',cvLetter = '"+
	        		 application.getCvLetter()+"',autoStatus = '"+
	        		 application.getAutoStatus()+"',jobID = '"+
	        		 application.getJobID()+"' WHERE appID = '"+
	        		 application.getAppId()+"';";

	         System.out.println("update sql is "+sql+"\n");
	         
	         try {
	        	 List<Application> files  = ConnectDB.executeSQL(dbAddr,sql.toString(), new ApplicationParser());
	             if(files != null && !files.isEmpty())
	                 return files.get(0);
	         } catch (SQLException e) {
	             e.printStackTrace();
	         }
	         return null;
	 }
	
	public  boolean insert(Application application){
		
	
        String sql = "INSERT into  tb_application VALUES ('"
        		+ application.getAppId()+ "','"
        		+application.getUserID()+"','"
        		+application.getCvLetter()+"','"
        		+application.getAutoStatus()+"','"
        		+application.getJobID()
        		+"');";

        System.out.println("insert sql is "+sql.toString()+"\n");
        
        try {
        	ConnectDB.executeSQL(dbAddr,sql, null);
            return  true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

}
	public  Application selectByApp(String appID) {
         String sql = "SELECT * FROM tb_application where appID='"+appID+"';";
         System.out.println("sql is "+sql +"\n");
         try {
             List<Application> files  = ConnectDB.executeSQL(dbAddr,sql, new ApplicationParser());
             
             if(files != null && !files.isEmpty())
                 return files.get(0);
         } catch (SQLException e) {
             e.printStackTrace();
         }
	return null;
	}
	public  List<Application> selectByJob(String jobID) {
        String sql = "SELECT * FROM tb_application where jobID='"+jobID+"';";
        System.out.println("sql is "+sql +"\n");
        try {
            List<Application> files  = ConnectDB.executeSQL(dbAddr,sql, new ApplicationParser());
            
            if(files != null && !files.isEmpty())
                return files;
        } catch (SQLException e) {
            e.printStackTrace();
        }
	return null;
	}
	public  List<Application> selectByUser(String userID) {
        String sql = "SELECT * FROM tb_application where userID='"+userID+"';";
        System.out.println("sql is "+sql +"\n");
        try {
            List<Application> files  = ConnectDB.executeSQL(dbAddr,sql, new ApplicationParser());
            
            if(files != null && !files.isEmpty())
                return files;
        } catch (SQLException e) {
            e.printStackTrace();
        }
	return null;
	}
	
	public  List<Application> select(String appID,String jobID,String userID) {
		String sql = "SELECT * FROM tb_application where 1=1 ";
        
        if (appID!= null) 
        {
        	sql =sql +" AND appID = '"+ appID+"'";
        }
        if (jobID!= null) 
        {
        	sql =sql +" AND jobID = '"+ jobID+"'";
        }
        if (userID!= null) 
        {
        	sql =sql +" AND userID = '"+ userID+"'";
        }
        sql = sql +";";
		System.out.println("sql from tb_application select is "+sql+"\n");

        try {
            List<Application> files  = ConnectDB.executeSQL(dbAddr,sql, new ApplicationParser());
            
            if(files != null && !files.isEmpty())
                return files;
        } catch (SQLException e) {
            e.printStackTrace();
        }
	return null;
	}
	public String createID() {
		String sql = "SELECT max(appID) from tb_application;";
		try {
            List<String> IDs  = ConnectDB.executeSQL(dbAddr,sql,new MaxIDParser() );
            if(IDs != null && !IDs.isEmpty())
                return IDs.get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
		return null;
	}





}
