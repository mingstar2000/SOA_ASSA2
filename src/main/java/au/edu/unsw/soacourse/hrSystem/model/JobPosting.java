package au.edu.unsw.soacourse.hrSystem.model;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class JobPosting {
	private String jobID = "";
    private String cmpID = "";
    private String name = "";
    private String salaryRate = "";
    private String posType = "";
//    @XmlElements(@XmlElement(name="link", type=HypermediaLink.class))
    private List<HypermediaLink> links = new ArrayList<HypermediaLink>();
    public JobPosting(){
    	
    }
    public JobPosting(String cmpID, String name,
			String salaryRate, String posType, String location, String status,
			String jobDsp) {
		super();
		this.cmpID = cmpID;
		this.name = name;
		this.salaryRate = salaryRate;
		this.posType = posType;
		this.location = location;
		this.status = status;
		this.jobDsp = jobDsp;
	}
    public JobPosting(String jobID,String cmpID, String name,
			String salaryRate, String posType, String location, String status,
			String jobDsp) {
		super();
		this.jobID = jobID;
		this.cmpID = cmpID;
		this.name = name;
		this.salaryRate = salaryRate;
		this.posType = posType;
		this.location = location;
		this.status = status;
		this.jobDsp = jobDsp;
	}

	public String getJobID() {
		return jobID;
	}
	public void setJobID(String jobID) {
		this.jobID = jobID;
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
	public String getSalaryRate() {
		return salaryRate;
	}
	public void setSalaryRate(String salaryRate) {
		this.salaryRate = salaryRate;
	}
	public String getPosType() {
		return posType;
	}
	public void setPosType(String posType) {
		this.posType = posType;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getJobDsp() {
		return jobDsp;
	}
	public void setJobDsp(String jobDsp) {
		this.jobDsp = jobDsp;
	}
	public void addHypermediaLink(HypermediaLink linkToSelf) {
		System.out.println("add 一个 link");
		links.add(linkToSelf);
		}
	
	
	public List<HypermediaLink> getLinks() {
		return links;
	}
	public void setLinks(List<HypermediaLink> links) {
		this.links = links;
	}


	private String location = "";
    private String status = "";
    private String jobDsp = "";

}
