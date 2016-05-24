package au.edu.unsw.soacourse.hrSystem.model;

import javax.xml.bind.annotation.XmlElements;

import javax.xml.bind.annotation.XmlRootElement;

import org.glassfish.jersey.linking.InjectLink;

import au.edu.unsw.soacourse.hrSystem.UserProfileRs;

@XmlRootElement
public class HypermediaLink {
	   public HypermediaLink(String href, String rel) {
		super();
		this.setHref(href);
		this.setRel(rel);
	}
	public HypermediaLink() {
		// TODO Auto-generated constructor stub
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getRel() {
		return rel;
	}
	public void setRel(String rel) {
		this.rel = rel;
	}
	@InjectLink(resource=UserProfileRs.class)
	private String href;
	private String rel;
  

}