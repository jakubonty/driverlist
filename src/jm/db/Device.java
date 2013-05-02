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

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;

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
		Filter vendorFilter = new FilterPredicate("vendor", FilterOperator.EQUAL, vendor);		
		
		Filter typeFilter = new FilterPredicate("type", FilterOperator.EQUAL, type);
		
		Filter filter = CompositeFilterOperator.and(vendorFilter, typeFilter);		
		
		Query q = pm.newQuery(Device.class);
		q.setFilter("vendor == vendorParam && type == typeParam");
		//q.setFilter("");
		q.declareParameters(Vendor.class.getSimpleName() + " vendorParam, String typeParam");
		//q.declareParameters("");
		List<Device> result = (List<Device>) q.execute(vendor, type);
		//System.out.println("Count: "+ result.size());
		//return null;
		return new ArrayList<Device>(result);
	}	
	
}
