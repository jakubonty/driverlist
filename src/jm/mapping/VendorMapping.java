package jm.mapping;

import jm.db.Vendor;

public class VendorMapping {

	private String name;
	private String id;	
	
	public VendorMapping(Vendor vendor) {
		this.name = vendor.getName();
		this.id = vendor.getKey();
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
