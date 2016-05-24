package au.edu.unsw.soacourse.hrSystem.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Review {

    private String revID = "";
    private String appID = "";
    private String userID = "";
    private String comment = "";
    private String reStatus = "";
    private String magStatus = "";

    @XmlElements(@XmlElement(name="link", type=HypermediaLink.class))
    private List<HypermediaLink> links = new ArrayList<HypermediaLink>();
 
    public List<HypermediaLink> getLinks() {
		return links;
	}

    public Review(){
    	
    }

    public Review (String revid, String appid, String userid, String reStatus){
        this.revID = revid;
        this.appID = appid;
        this.userID = userid;
        this.reStatus = reStatus;
    }

    public Review (String revid, String appid, String userid, String comment, String reStatus, String magStatus){
        this.revID = revid;
        this.appID = appid;
        this.userID = userid;
        this.comment = comment;
        this.reStatus = reStatus;
        this.magStatus = magStatus;
    }
    
    public String getRevID() {
        return revID;
    }
    public void setRevID(String revid) {
        this.revID= revid;
    }
    public String getAppID() {
        return appID;
    }
    public void setAppID(String appid) {
        this.appID = appid;
    }
    public String getUserID() {
        return userID;
    }
    public void setUserID(String userid) {
        this.userID = userid;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public String getReStatus() {
        return reStatus;
    }
    public void setReStatus(String reStatus) {
        this.reStatus = reStatus;
    }
    public String getMagStatus() {
        return magStatus;
    }
    public void setMagStatus(String magStatus) {
        this.magStatus = magStatus;
    }
    
	public void addHypermediaLink(HypermediaLink linkToSelf) {
		links.add(linkToSelf);
		}
	
}