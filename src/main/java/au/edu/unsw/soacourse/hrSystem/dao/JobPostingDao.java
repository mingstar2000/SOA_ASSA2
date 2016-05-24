package au.edu.unsw.soacourse.hrSystem.dao;

import java.sql.SQLException;
import java.util.List;

import au.edu.unsw.soacourse.hrSystem.model.Application;
import au.edu.unsw.soacourse.hrSystem.model.JobPosting;;

public class JobPostingDao {


public static String dbAddr = "jdbc:sqlite:c:/cs9322-Prac/workspace/FoundITService/db/foundITServer.db";  
	
	public  JobPosting update(JobPosting jobPosting){
	         String sql = "UPDATE  tb_jobPosting SET  cmpID ='"+
	        		 jobPosting.getCmpID()+"',name = '"+
	        		 jobPosting.getName()+"',salaryRate = '"+
	        		 jobPosting.getSalaryRate()+"',posType = '"+
	        		 jobPosting.getPosType()+"',location = '"+
	        		 jobPosting.getLocation()+"',status = '"+
	        		 jobPosting.getStatus()+"',jobDsp = '"+
	        		 jobPosting.getJobDsp()+"' WHERE jobID = '"+
	        		 jobPosting.getJobID()+"';";

	         System.out.println("update sql is "+sql+"\n");
	         
	        // ConnectDB.updateDB(dbAddr, sql.toString());
	         
	         try {
	        	 List<JobPosting> files  = ConnectDB.executeSQL(dbAddr,sql.toString(), new JobPostingParser());
	             if(files != null && !files.isEmpty())
	                 return files.get(0);
	         } catch (SQLException e) {
	             e.printStackTrace();
	         }
	         return null;
	 }
	
	public  boolean insert(JobPosting jobPosting){
		
	
        String sql = "INSERT into  tb_jobPosting VALUES ('"+
        		 jobPosting.getJobID()+"','"+
        		 jobPosting.getCmpID()+"','"+
        		 jobPosting.getName()+"','"+
        		 jobPosting.getSalaryRate()+"', '"+
        		 jobPosting.getPosType()+"','"+
        		 jobPosting.getLocation()+"', '"+
        		 jobPosting.getStatus()+"','"+
        		 jobPosting.getJobDsp()+"');";

        System.out.println("insert sql is "+sql.toString()+"\n");
        
        try {
        	ConnectDB.executeSQL(dbAddr,sql, null);
            return  true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

}

	public  List<JobPosting> select(String jobID,String cmpID,String name,String salaryRate,String posType,String location,String status, String jobDsp) {
        String sql = "SELECT * FROM tb_jobPosting where 1=1 ";
        
        if (jobID!= null) 
        {
        	sql =sql +" AND jobID = '"+ jobID+"'";
        }
        if (cmpID!= null) 
        {
        	sql =sql +"AND cmpID = '"+ cmpID+"'";
        }
        if (name!= null) 
        {
        	sql =sql +"AND name = '"+ name+"'";
        }if (salaryRate!= null) 
        {
        	sql =sql +" AND salaryRate = '"+ salaryRate+"'"; 
        	
        	
        }
        if(posType != null)
        {
        	sql =sql +" AND posType = '"+ posType+"'";
        }
        if(location != null)
        {
        	sql =sql +" AND location = '"+ location+"'";
        }
        if(status != null)
        {
        	sql =sql +"AND status = '"+ status+"'";
        }
        if(jobDsp != null)
        {
        	sql =sql +"AND jobDsp like '%"+ jobDsp+"%'";
        }
        		 
        		sql = sql +";";
        		System.out.println("sql from tb_jobPosting select is "+sql+"\n");
        try {
            List<JobPosting> files  = ConnectDB.executeSQL(dbAddr,sql, new JobPostingParser());
            System.out.println("file from tb_jobPosting select is ");
            if(files != null && !files.isEmpty())
                return files;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("nothing returened  from tb_jobPosting select ");
	return null;
	}

	public String createID() {
		String sql = "SELECT max(jobID) from tb_jobPosting;";
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

