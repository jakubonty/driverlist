package jm.mapping;

import java.util.ArrayList;
import java.util.List;

import jm.db.OperatingSystem;

public class OperatingSystemList {

	private ArrayList<OperatingSystemMapping> osList;
	
	public OperatingSystemList(List<OperatingSystem> osList) {
		this.osList = new ArrayList<OperatingSystemMapping>();
		for(OperatingSystem system: osList) {
			this.osList.add(new OperatingSystemMapping(system));
		}
	}

	public ArrayList<OperatingSystemMapping> getOperatingSystems() {
		return osList;
	}

	public void setOperatingSytemst(ArrayList<OperatingSystemMapping> osList) {
		this.osList = osList;
	}	
	
}
