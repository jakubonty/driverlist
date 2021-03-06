package jm.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@PersistenceCapable
public class Device {
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key id;	
	@Persistent
	private String name;
	@Persistent
	private String description;	
	
	@Persistent
	private Vendor vendor;	
	
	@Persistent
	private String type;	
	
	@Persistent
	private Date created;

    @Persistent(mappedBy = "device")
    private List<Driver> drivers;	
	
    public Device() {
    	created = new Date();    	
    }
    
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Key getId() {
		return id;
	}

	public void setId(Key id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
	
	public void addDriver(Driver driver) {
		if (this.drivers == null) {
			this.drivers = new ArrayList<Driver>();			
		}		
		this.drivers.add(driver);
	}	
	
	public static List<Device> getAll(PersistenceManager pm) {
		Query query = pm.newQuery(Device.class);
		query.setOrdering("name");
		List<Device> result = (List<Device>) query.execute();
		return result;
	}

	public List<Driver> getDrivers() {
		return drivers;
	}

	public void setDrivers(List<Driver> drivers) {
		this.drivers = drivers;
	}
	
	public String getKey() {
		 return KeyFactory.keyToString(id);		
	}	
	
	public static List<Device> findBy(PersistenceManager pm, Key vendor, String type) {	
		Query q = pm.newQuery(Device.class);
		q.setOrdering("name");
		q.setFilter("vendor == vendorParam && type == typeParam");
		q.declareParameters(Vendor.class.getSimpleName() + " vendorParam, String typeParam");
		List<Device> result = (List<Device>) q.execute(vendor, type);
		return new ArrayList<Device>(result);
	}	
	
}
