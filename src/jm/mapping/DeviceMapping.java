package jm.mapping;

import jm.db.Device;

public class DeviceMapping {
	
	private String name;
	private String id;
	private String description;
	
	public DeviceMapping(Device device) {
		this.name = device.getName();
		this.id = device.getKey();
		this.description = device.getDescription();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}			
	
}
