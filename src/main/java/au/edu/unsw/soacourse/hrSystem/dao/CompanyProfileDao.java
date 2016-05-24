package au.edu.unsw.soacourse.hrSystem.dao;
import java.sql.*;  
import java.util.List;

import au.edu.unsw.soacourse.hrSystem.model.CompanyProfile;
import au.edu.unsw.soacourse.hrSystem.model.UserProfile;

//TODO: consider example value like 'Sunny's company' -> it can cause error because of "'" 
public class CompanyProfileDao {
	public static String dbAddr = "jdbc:sqlite:c:/cs9322-Prac/workspace/FoundITService/db/foundITServer.db";  
	
	public  CompanyProfile put(CompanyProfile companyProfile){
	         StringBuilder sql = new StringBuilder();
	         sql.append("UPDATE  tb_companyProfile SET name ='").append(companyProfile.getName()).append("'");
	         sql.append(", email ='").append(companyProfile.getEmail()).append("'");
	         sql.append(", addr ='").append(companyProfile.getAddr()).append("'");
	         sql.append(", telNum ='").append(companyProfile.getTelNum()).append("'");
	         sql.append(", indType ='").append(companyProfile.getIndType()).append("'");
	         sql.append(", webSite ='").append(companyProfile.getWebSite()).append("'");
	         sql.append(", cmpDsp ='").append(companyProfile.getCmpDsp()).append("'");
	         sql.append(" WHERE cmpID = '").append(companyProfile.getId()).append("';");
	         System.out.println("sql is "+sql.toString()+"\n");
	         
	        // ConnectDB.updateDB(dbAddr, sql.toString());
	         
	         try {
	        	 List<CompanyProfile> files  = ConnectDB.executeSQL(dbAddr,sql.toString(), new CompanyProfileParser());
	             if(files != null && !files.isEmpty())
	                 return files.get(0);
	         } catch (SQLException e) {
	             e.printStackTrace();
	         }
	         return null;
	 }
	
	public  boolean post(CompanyProfile companyProfile){
	
		
        String sql = "INSERT into  tb_companyProfile VALUES ('"
        		+ companyProfile.getId() + "','"
        		+companyProfile.getName() +"','"
        		+companyProfile.getEmail() +"','"
        		+companyProfile.getAddr() +"','"
        		+companyProfile.getTelNum() +"','"
        		+companyProfile.getIndType() +"','"
        		+companyProfile.getWebSite() +"','"
        		+companyProfile.getCmpDsp() +"');";

        System.out.println("sql is "+sql.toString()+"\n");
        
        try {
        	ConnectDB.executeSQL(dbAddr,sql, null);
            return  true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

	}
	public  CompanyProfile get(String cmpID) {
        String sql = "SELECT * FROM tb_companyProfile where cmpID='"+cmpID+"';";
        System.out.println(sql);
        try {
            List<CompanyProfile> files  = ConnectDB.executeSQL(dbAddr,sql, new CompanyProfileParser());
            if(files != null && !files.isEmpty())
                return files.get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
	return null;
	}

	public  CompanyProfile delete(String cmpID) {
        String sql = "DELETE FROM tb_companyProfile where cmpID='"+cmpID+"';";
        try {
            List<CompanyProfile> files  = ConnectDB.executeSQL(dbAddr,sql, new CompanyProfileParser());
            if(files != null && !files.isEmpty())
                return files.get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
	return null;
	}

	//TODO: if there is no data...
	public String max() {
        String sql = "SELECT MAX(cmpID) FROM tb_companyProfile;";
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
