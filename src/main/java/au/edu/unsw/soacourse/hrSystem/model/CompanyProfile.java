package au.edu.unsw.soacourse.hrSystem.model;

import java.util.UUID;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CompanyProfile {

    private String cmpid = "";
    private String name = "";
    private String email = "";
    private String addr;
    private String telNum;
    private String indType;
    private String webSite;
    private String cmpDsp;

    public CompanyProfile(){

    }
    public CompanyProfile (String name, String email){

    	//TODO: change according to our rule (use max number)
    	this.cmpid = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
    }
    
    public CompanyProfile (String id, String name, String email,String addr,String telNum,String indType,String webSite,String cmpDsp){
        this.cmpid = id;
        this.name = name;
        this.email = email;
        this.addr = addr;
        this.telNum = telNum;
        this.indType = indType;
        this.webSite = webSite;
        this.cmpDsp = cmpDsp;
    }
    
    public String getId() {
        return cmpid;
    }
    public void setId(String cmpid) {
        this.cmpid= cmpid;
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
    public String getIndType() {
        return indType;
    }
    public void setIndType(String indType) {
        this.indType = indType;
    }
    public String getWebSite() {
        return webSite;
    }
    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }
    public String getCmpDsp() {
        return cmpDsp;
    }
    public void setCmpDsp(String cmpDsp) {
        this.cmpDsp = cmpDsp;
    }
}