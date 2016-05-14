package au.edu.unsw.soacourse.hrSystem.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Review {

    private String revID = "";
    private String appID = "";
    private String userID = "";
    private String reStatus1 = "";
    private String reStatus2 = "";
    private String magStatus = "";
    
    public Review(){
    	
    }

    public Review (String revid, String appid, String userid, String reStatus1, String reStatus2, String magStatus){
        this.revID = revid;
        this.appID = appid;
        this.userID = userid;
        this.reStatus1 = reStatus1;
        this.reStatus2 = reStatus2;
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
    public String getReStatus1() {
        return reStatus1;
    }
    public void setReStatus1(String reStatus1) {
        this.reStatus1 = reStatus1;
    }
    public String getReStatus2() {
        return reStatus2;
    }
    public void setReStatus2(String reStatus2) {
        this.reStatus2 = reStatus2;
    }
    public String getMagStatus() {
        return magStatus;
    }
    public void setMagStatus(String magStatus) {
        this.magStatus = magStatus;
    }
}