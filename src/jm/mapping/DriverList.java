package jm.mapping;

import java.util.ArrayList;
import java.util.List;

import jm.db.Driver;

public class DriverList {

	private ArrayList<DriverMapping> drivers;
	
	public DriverList(List<Driver> drivers) {
		this.drivers = new ArrayList<DriverMapping>();
		for(Driver driver : drivers) {
			this.drivers.add(new DriverMapping(driver));
		}
	}

	public ArrayList<DriverMapping> getDrivers() {
		return drivers;
	}

	public void setDrivers(ArrayList<DriverMapping> drivers) {
		this.drivers = drivers;
	}	
	
}
