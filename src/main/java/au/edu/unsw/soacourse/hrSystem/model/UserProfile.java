package au.edu.unsw.soacourse.hrSystem.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.Path;
import javax.xml.bind.annotation.*;

import org.glassfish.jersey.linking.InjectLink;

import au.edu.unsw.soacourse.hrSystem.UserProfileRs;

import java.util.Collection;

@XmlRootElement
@Path(value = "http://localhost:8080/FoundITService/userProfile/")
public class UserProfile {

    private String userID = "";
    private String name = "";
    private String email = "";
    private String addr;
    private String telNum;
    private String curPos;
    private String education;
    private String skills;
    private String experience;
    private String perDsp;
    
    //@XmlElements(@XmlElement(name="link", type=HypermediaLink.class))
    @XmlElements(@XmlElement(name="link", type=HypermediaLink.class))
    private List<HypermediaLink> links = new ArrayList<HypermediaLink>();

    public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getExperience() {
		return experience;
	}
	public void setExperience(String experience) {
		this.experience = experience;
	}


	public UserProfile(){

    }
    public UserProfile (String name, String email){

    	userID = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
    }
    public UserProfile (String id, String name, String email,String addr,String telNum,String curPos,String education,String skills,String experience,String perDsp){
        this.userID = id;
        this.name = name;
        this.email = email;
        this.addr = addr;
        this.telNum = telNum;
        this.curPos = curPos;
        this.education = education;
        this.skills = skills;
        this.experience = experience;
        this.perDsp = perDsp;
       

    }
    public String getId() {
        return userID;
    }
    public void setId(String id) {
        this.userID = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getAddr() {
        return addr;
    }
    public void setAddr(String addr) {
        this.addr = addr;
    }
    public String getTelNum() {
        return telNum;
    }
    public void setTelNum(String telNum) {
        this.telNum = telNum;
    }

    public String getCurPos() {
        return curPos;
    }
    public void setCurPos(String curPos) {
        this.curPos = curPos;
    }
    public String getEducation() {
        return education;
    }
    public void setEducation(String education) {
        this.education = education;
    }
    public String getSkills() {
        return skills;
    }
    public void setSkills(String skills) {
        this.skills = skills;
    }
    public String getExp() {
        return experience;
    }
    public void setExp(String experience) {
        this.experience = experience;
    }
    public String getPerDsp() {
        return perDsp;
    }
    public void setPerDsp(String perDsp) {
        this.perDsp = perDsp;
    }
	public void addHypermediaLink(HypermediaLink linkToSelf) {
		System.out.println("add 一个 link");
		links.add(linkToSelf);
		}

}