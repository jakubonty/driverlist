package jm.mapping;

import java.util.Date;

import com.google.appengine.api.users.User;

import jm.config.Config;
import jm.db.Device;
import jm.db.Driver;

public class DriverMapping {

	private User author;
	
	private String name;		
	
	private Device device;	
	
	private String version;	
	
	private Date created;
	
	private String operatingSystem;	
	
	private String id;
	
	public DriverMapping(Driver driver) {
		this.author = driver.getAuthor();
		this.name = driver.getName();
		this.version = driver.getVersion();
		this.created = driver.getCreated();
		this.id = driver.getKey();
		this.device = driver.getDevice();
		this.operatingSystem = driver.getOperatingSystem();
	}

	public String getAuthor() {
		if (author == null) return "none";
		return author.getEmail();
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDevice() {
		return device.getKey();
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getOperatingSystem() {
		return operatingSystem;
	}

	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}	
	
	public String getDownloadUrl() {
		return Config.HOST_URL + "drivers/" + id + "/download";
	}
	
}
