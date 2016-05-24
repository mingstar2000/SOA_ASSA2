package au.edu.unsw.soacourse.hrSystem.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CompanyProfile {
	
    private String cmpID = "";
    private String name = "";
    private String email = "";
    private String addr;
    private String telNum;
    private String indType;
    private String webSite;
    private String cmpDsp;

    @XmlElements(@XmlElement(name="link", type=HypermediaLink.class))
    private List<HypermediaLink> links = new ArrayList<HypermediaLink>();

    
	public CompanyProfile(){
    	
    }

    public List<HypermediaLink> getLinks() {
		return links;
	}

	public CompanyProfile (String cmpid, String name, String email){

    	this.cmpID = cmpid;
        this.name = name;
        this.email = email;
    }
    
    public CompanyProfile (String id, String name, String email,String addr,String telNum,String indType,String webSite,String cmpDsp){
        this.cmpID = id;
        this.name = name;
        this.email = email;
        this.addr = addr;
        this.telNum = telNum;
        this.indType = indType;
        this.webSite = webSite;
        this.cmpDsp = cmpDsp;
    }
    
    public String getId() {
        return cmpID;
    }
    public void setId(String cmpid) {
        this.cmpID= cmpid;
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

	public void addHypermediaLink(HypermediaLink linkToSelf) {
		links.add(linkToSelf);
		}
	
	
}