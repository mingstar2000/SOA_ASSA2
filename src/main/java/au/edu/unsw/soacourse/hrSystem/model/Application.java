package au.edu.unsw.soacourse.hrSystem.model;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.Path;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import org.glassfish.jersey.linking.InjectLink;
@XmlRootElement
@Path(value = "http://localhost:8080/FoundITService/application/")

public class Application {
	private String appID = "";
    private String userID = "";
    private String cvLetter = "";
    private String autoStatus = "0";
    private String jobID = "";
    @XmlElements(@XmlElement(name="link", type=HypermediaLink.class))
    private List<HypermediaLink> links = new ArrayList<HypermediaLink>();

    public List<HypermediaLink> getLinks() {
		return links;
	}
	public void setLinks(List<HypermediaLink> links) {
		this.links = links;
	}
	public Application(){
    }
    public Application(String userID,String cvLetter, String autoStatus,String jobID){
    	this.userID = userID;
    	this.cvLetter = cvLetter;
    	this.autoStatus = autoStatus;
    	this.jobID = jobID;
    }
    public Application(String appID, String userID,String cvLetter, String autoStatus,String jobID){
    	this.appID = appID;
    	this.userID = userID;
    	this.cvLetter = cvLetter;
    	this.autoStatus = autoStatus;
    	this.jobID = jobID;
    }
	public String getAppId() {
        return appID;
    }
    public void setAppId(String appID) {
        this.appID = appID;
    }
    public String getUserID() {
        return userID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }
    public String getCvLetter() {
        return cvLetter;
    }
    public void setCvLetter(String cvLetter) {
        this.cvLetter = cvLetter;
    }
    public String getAutoStatus() {
        return autoStatus;
    }
    public void setAutoStatus(String autoStatus) {
        this.autoStatus = autoStatus;
    }
    public String getJobID() {
        return jobID;
    }
    public void setJobID(String jobID) {
        this.jobID = jobID;
    }
	public void addHypermediaLink(HypermediaLink linkToSelf) {
		System.out.println("add 一个 link");
		links.add(linkToSelf);
		}
}




