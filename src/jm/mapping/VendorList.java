package jm.mapping;

import java.util.ArrayList;
import java.util.List;

import jm.db.Vendor;

public class VendorList {

	private ArrayList<VendorMapping> vendors;
	
	public VendorList(List<Vendor> vendors) {
		this.vendors = new ArrayList<VendorMapping>();
		for(Vendor vendor : vendors) {
			this.vendors.add(new VendorMapping(vendor));
		}
	}

	public ArrayList<VendorMapping> getVendors() {
		return vendors;
	}

	public void setVendors(ArrayList<VendorMapping> vendors) {
		this.vendors = vendors;
	}
}
