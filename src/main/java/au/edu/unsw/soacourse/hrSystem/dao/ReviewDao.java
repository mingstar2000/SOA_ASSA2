package au.edu.unsw.soacourse.hrSystem.dao;
import java.sql.*;  
import java.util.List;

import au.edu.unsw.soacourse.hrSystem.model.Review;

//TODO: consider example value like 'Sunny's company' -> it can cause error because of "'" 
public class ReviewDao {
	public static String dbAddr = "jdbc:sqlite:c:/cs9322-Prac/workspace/FoundITService/db/foundITServer.db";  
	
	public  boolean post(Review review){
	
		
        String sql = "INSERT into  tb_reviews VALUES ('"
        		+ review.getRevID() + "','"
        		+review.getAppID() +"','"
        		+review.getUserID() +"','"
        		+review.getReStatus1() +"','"
        		+review.getReStatus2() +"','"
        		+review.getMagStatus() +"');";

        System.out.println("sql is "+sql.toString()+"\n");
        
        try {
        	ConnectDB.executeSQL(dbAddr,sql, null);
            return  true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

	}

	public  List<Review> get() {
        String sql = "SELECT * FROM tb_reviews;";
        System.out.println(sql);
        try {
            List<Review> files  = ConnectDB.executeSQL(dbAddr,sql, new ReviewParser());
            if(files != null && !files.isEmpty())
                return files;
        } catch (SQLException e) {
            e.printStackTrace();
        }
	return null;
	}
	
	public  Review getReview(String revID) {
        String sql = "SELECT * FROM tb_reviews where revID='"+revID+"';";
        System.out.println(sql);
        try {
            List<Review> files  = ConnectDB.executeSQL(dbAddr,sql, new ReviewParser());
            if(files != null && !files.isEmpty())
                return files.get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
	return null;
	}
	
	public  List<Review> getApp(String appID) {
        String sql = "SELECT * FROM tb_reviews where appID='"+appID+"';";
        System.out.println(sql);
        try {
            List<Review> files  = ConnectDB.executeSQL(dbAddr,sql, new ReviewParser());
            if(files != null && !files.isEmpty())
                return files;
        } catch (SQLException e) {
            e.printStackTrace();
        }
	return null;
	}

	public  List<Review> getRev(String userID) {
        String sql = "SELECT * FROM tb_reviews where userID='"+userID+"';";
        System.out.println(sql);
        try {
            List<Review> files  = ConnectDB.executeSQL(dbAddr,sql, new ReviewParser());
            if(files != null && !files.isEmpty())
                return files;
        } catch (SQLException e) {
            e.printStackTrace();
        }
	return null;
	}
	
	//TODO: if there is no data...
	public String max() {
        String sql = "SELECT MAX(revID) FROM tb_reviews;";
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


