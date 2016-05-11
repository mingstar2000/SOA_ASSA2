package au.edu.unsw.soacourse.hrSystem.dao;
import java.sql.*;  
import java.util.List;

import au.edu.unsw.soacourse.hrSystem.model.UserProfile;
public class UserProfileDao {
	public static String dbAddr = "jdbc:sqlite:c:/cs9322-Prac/workspace/FoundITService/db/foundITServer.db";  
	
	public  UserProfile put(UserProfile userProfile){
	         StringBuilder sql = new StringBuilder();
	         sql.append("UPDATE  tb_userProfile SET name ='").append(userProfile.getName()).append("'");
	         sql.append(", email ='").append(userProfile.getEmail()).append("'");
	         sql.append(", addr ='").append(userProfile.getAddr()).append("'");
	         sql.append(", telNum ='").append(userProfile.getTelNum()).append("'");
	         sql.append(", curPos ='").append(userProfile.getCurPos()).append("'");
	         sql.append(", education ='").append(userProfile.getEducation()).append("'");
	         sql.append(", skills ='").append(userProfile.getSkills()).append("'");
	         sql.append(", experience ='").append(userProfile.getExp()).append("'");
	         sql.append(", perDsp ='").append(userProfile.getPerDsp()).append("'");
	         sql.append("WHERE userID = '").append(userProfile.getId()).append("';");
	         System.out.println("sql is "+sql.toString()+"\n");
	         
	        // ConnectDB.updateDB(dbAddr, sql.toString());
	         
	         try {
	        	 List<UserProfile> files  = ConnectDB.executeSQL(dbAddr,sql.toString(), new UserProfileParser());
	             if(files != null && !files.isEmpty())
	                 return files.get(0);
	         } catch (SQLException e) {
	             e.printStackTrace();
	         }
	         return null;
	 }
	
	public  boolean post(UserProfile userProfile){
	
        String sql = "INSERT into  tb_userProfile VALUES ('"
        		+ userProfile.getId() + "','"
        		+userProfile.getName() +"','"
        		+userProfile.getEmail() +"','"
        		+userProfile.getAddr() +"','"
        		+userProfile.getTelNum() +"','"
        		+userProfile.getCurPos() +"','"
        		+userProfile.getEducation() +"','"
        		+userProfile.getSkills() +"','"
        		+userProfile.getExp() +"','"
        		+userProfile.getPerDsp() +"');";

        System.out.println("sql is "+sql.toString()+"\n");
        
        try {
        	ConnectDB.executeSQL(dbAddr,sql, null);
            return  true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

}
	public  UserProfile get(String userID) {
         String sql = "SELECT * FROM tb_userProfile where userID='"+userID+"';";
         try {
             List<UserProfile> files  = ConnectDB.executeSQL(dbAddr,sql, new UserProfileParser());
             if(files != null && !files.isEmpty())
                 return files.get(0);
         } catch (SQLException e) {
             e.printStackTrace();
         }
	return null;
	}



}