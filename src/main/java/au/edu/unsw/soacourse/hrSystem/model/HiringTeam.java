package au.edu.unsw.soacourse.hrSystem.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class HiringTeam {

    private String userID = "";
    private String password = "";
    private String cmpID = "";
    private String name = "";
    private String skills;
    
    public HiringTeam(){
    	
    }

    public HiringTeam (String userID, String password, String cmpID, String name){

    	this.userID = userID;
    	this.password = password;
    	this.cmpID = cmpID;
        this.name = name;
    }
    
    public HiringTeam (String userID, String password, String cmpID, String name, String skills){

    	this.userID = userID;
    	this.password = password;
    	this.cmpID = cmpID;
        this.name = name;
        this.skills = skills;
    }
    
    public String getUserID() {
        return userID;
    }
    public void setUserID(String userid) {
        this.userID= userid;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getCmpID() {
        return cmpID;
    }
    public void setCmpID(String cmpID) {
        this.cmpID = cmpID;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSkills() {
        return skills;
    }
    public void setSkills(String skills) {
        this.skills = skills;
    }
}