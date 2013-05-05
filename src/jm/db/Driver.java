package jm.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;

@PersistenceCapable
public class Driver implements Serializable {
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key id;

	@Persistent
	private User author;
	
	@Persistent
	private String name;		
	
	@Persistent
	private Device device;	
	
	@Persistent
	private String version;	
	
	@Persistent
	private Date created;
	
	@Persistent
	private String operatingSystem;
	
	@Persistent
	private BlobKey data;
		
	public Driver() {
		created = new Date();		
	}	
	
	public BlobKey getData() {
		return data;
	}


	public void setData(BlobKey data) {
		this.data = data;
	}


	public Key getId() {
		return id;
	}


	public String getOperatingSystem() {
		return operatingSystem;
	}


	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
	}


	public void setId(Key id) {
		this.id = id;
	}


	public User getAuthor() {
		return author;
	}

	public String getAuthorEmail() {
		if (author == null)
			return "none";
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


	public Device getDevice() {
		return device;
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
	
	public String getKey() {
		 return KeyFactory.keyToString(id);		
	}	
	
	public static List<Driver> getAll(PersistenceManager pm) {
		Query query = pm.newQuery(Driver.class);
		query.setOrdering("name");
		List<Driver> result = (List<Driver>) query.execute();
		return result;
	}	
}
