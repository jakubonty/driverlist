package jm.db;

import java.util.ArrayList;
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
public class Vendor {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key id;
	
	@Persistent
	private String name;

    @Persistent(mappedBy = "vendor")
    private List<Device> devices;
	
	public List<Device> getDevices() {
		return devices;
	}

	public void setDevices(List<Device> devices) {
		this.devices = devices;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	
	
	public static List<Vendor> getAll(PersistenceManager pm) {
		Query query = pm.newQuery(Vendor.class);
		List<Vendor> result = (List<Vendor>) query.execute();
		return result;
	}
	
	public Key getId() {
		return id;
	}
	public void setId(Key id) {
		this.id = id;
	}	
	
	public String getKey() {
		 return KeyFactory.keyToString(id);		
	}
	
	public void addDevice(Device device) {
		if (this.devices == null) {
			this.devices = new ArrayList<Device>();			
		}		
		this.devices.add(device);
	}
}
