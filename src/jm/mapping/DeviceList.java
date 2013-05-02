package jm.mapping;

import java.util.ArrayList;
import java.util.List;

import jm.db.Device;

public class DeviceList {

	private ArrayList<DeviceMapping> devices;
	
	public DeviceList(ArrayList<Device> devices) {
				this.devices = new ArrayList<DeviceMapping>();
				for(Device device : devices) {
					this.devices.add(new DeviceMapping(device));
				}
	}
	
	public List<DeviceMapping> getDevices() {		
		return this.devices;
	}
	
}
