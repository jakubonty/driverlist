package jm.mapping;

import jm.db.OperatingSystem;

public class OperatingSystemMapping {
	
	private String name;
	private String id;
	
	public OperatingSystemMapping(OperatingSystem os) {
		this.name = os.getName();
		this.id = os.getKey();
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
	
}
