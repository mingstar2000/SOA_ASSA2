package au.edu.unsw.soacourse.hrSystem.dao;
import java.sql.*;  
import java.util.List;

import au.edu.unsw.soacourse.hrSystem.model.HiringTeam;

//TODO: consider example value like 'Sunny's company' -> it can cause error because of "'" 
public class HiringTeamDao {
	public static String dbAddr = "jdbc:sqlite:/Users/zhangyun/git/FoundITService/db/foundITServer.db";  
	
	public  HiringTeam put(HiringTeam hiringTeam, String old_userID){
	         StringBuilder sql = new StringBuilder();
	         sql.append("UPDATE  tb_hirTeam SET userID ='").append(hiringTeam.getUserID()).append("'");
	         sql.append(", password ='").append(hiringTeam.getPassword()).append("'");
	         sql.append(", cmpID ='").append(hiringTeam.getCmpID()).append("'");
	         sql.append(", name ='").append(hiringTeam.getName()).append("'");
	         sql.append(", skills ='").append(hiringTeam.getSkills()).append("'");
	         sql.append(" WHERE cmpID = '").append(hiringTeam.getCmpID()).append("' and ");
	         sql.append("userID = '").append(old_userID).append("';");
	         System.out.println("sql is "+sql.toString()+"\n");
	         
	         System.out.println(sql);
	         try {
	        	 List<HiringTeam> files  = ConnectDB.executeSQL(dbAddr,sql.toString(), new HiringTeamParser());
	             if(files != null && !files.isEmpty())
	                 return files.get(0);
	         } catch (SQLException e) {
	             e.printStackTrace();
	         }
	         return null;
	 }
	
	public  boolean post(HiringTeam hiringTeam){
	
        String sql = "INSERT into  tb_hirTeam VALUES ('"
        		+ hiringTeam.getUserID() + "','"
        		+hiringTeam.getPassword() +"','"
        		+hiringTeam.getCmpID() +"','"
        		+hiringTeam.getName() +"','"
        		+hiringTeam.getSkills() +"');";

        System.out.println("sql is "+sql.toString()+"\n");
        
        try {
        	ConnectDB.executeSQL(dbAddr,sql, null);
            return  true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
	}
	
	public  List<HiringTeam> get(String cmpID) {
		
		String sql = "SELECT * FROM tb_hirTeam where cmpID='"+cmpID+"';";

		System.out.println(sql);
        try {
            List<HiringTeam> files  = ConnectDB.executeSQL(dbAddr,sql, new HiringTeamParser());
            if(files != null && !files.isEmpty())
                return files;
        } catch (SQLException e) {
            e.printStackTrace();
        }
	return null;
	}

	public  HiringTeam getMember(String cmpID, String userID) {
		
		String sql = "SELECT * FROM tb_hirTeam where cmpID='"+cmpID+"' and userID='" +userID+"';";

		System.out.println(sql);
        try {
            List<HiringTeam> files  = ConnectDB.executeSQL(dbAddr,sql, new HiringTeamParser());
            if(files != null && !files.isEmpty())
                return files.get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
	return null;
	}

	public  HiringTeam delete(String cmpID) {
        String sql = "DELETE FROM tb_hirTeam where cmpID='"+cmpID + "';";
        try {
            List<HiringTeam> files  = ConnectDB.executeSQL(dbAddr,sql, new HiringTeamParser());
            if(files != null && !files.isEmpty())
                return files.get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
	return null;
	}

	public  HiringTeam deleteMember(String cmpID, String userID) {
        String sql = "DELETE FROM tb_hirTeam where cmpID='"+cmpID+"' and userID='"+userID+"';";
        try {
            List<HiringTeam> files  = ConnectDB.executeSQL(dbAddr,sql, new HiringTeamParser());
            if(files != null && !files.isEmpty())
                return files.get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
	return null;
	}
	
	//TODO: if there is no data...
	public String max(String cmpID) {
        String sql = "SELECT MAX(userID) FROM tb_hirTeam where cmpID='"+cmpID+"';";
        try {
        	System.out.println(sql);
            List<String> maxID  = ConnectDB.executeSQL(dbAddr,sql, new MaxIDParser());
            System.out.println(maxID.get(0));
            if(maxID != null && !maxID.isEmpty())
                return maxID.get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
	return null;
	}
}
