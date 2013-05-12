package jm;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import jm.db.Device;
import jm.db.OperatingSystem;
import jm.db.Vendor;
import jm.mapping.DeviceList;
import jm.mapping.DeviceMapping;
import jm.mapping.DriverList;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.sun.jersey.api.view.Viewable;

@Path("/devices/")
public class DevicesResource extends AppController {

	@GET
	@Path("/search")
	@Produces(MediaType.APPLICATION_JSON)
	public Response search(@Context HttpServletRequest httpRequest) {

		String vendorId = httpRequest.getParameter("vendorId");
		String type = httpRequest.getParameter("type");

		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();
		Key key = KeyFactory.stringToKey(vendorId);
		ArrayList<Device> result = (ArrayList<Device>) Device.findBy(pm, key,
				type);
		DeviceList deviceList = new DeviceList(result);
		return Response.ok().entity(deviceList).build();
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_XHTML_XML)
	public Response showDevice(@PathParam("id") String id) {
		Map<String, Object> map = new HashMap<String, Object>();

		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();

		Key key = KeyFactory.stringToKey(id);
		Device device = pm.getObjectById(Device.class, key);

		map.put("systems", OperatingSystem.getAll(pm));
		map.put("device", device);

		beforeRender(map);
		return Response.ok(new Viewable("/views/device/show.jsp", map)).build();
	}

	@GET
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response showDeviceJSON(@PathParam("id") String id) {
		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();

		Key key = KeyFactory.stringToKey(id);
		Device device = pm.getObjectById(Device.class, key);

		return Response.ok(new DeviceMapping(device)).build();
	}

	@GET
	@Path("/{id}/drivers")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response showDeviceDriversJSON(@PathParam("id") String id) {
		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();

		Key key = KeyFactory.stringToKey(id);
		Device device = pm.getObjectById(Device.class, key);

		return Response.ok(new DriverList(device.getDrivers())).build();
	}

	@GET
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response showDevicesJSON() {
		Map<String, Object> map = new HashMap<String, Object>();

		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();

		beforeRender(map);
		return Response.ok(new DeviceList(Device.getAll(pm))).build();
	}

	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_XHTML_XML)
	@RolesAllowed({ "admin" })
	public Response listDevices() {
		Map<String, Object> map = new HashMap<String, Object>();

		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();
		map.put("devices", Device.getAll(pm));

		return Response.ok(new Viewable("/views/device/list.jsp", map)).build();
	}

	@GET
	@Path("/new")
	@Produces(MediaType.APPLICATION_XHTML_XML)
	public Response newDevice() {
		Map<String, Object> map = new HashMap<String, Object>();

		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();
		map.put("vendors", Vendor.getAll(pm));

		return Response.ok(new Viewable("/views/device/new.jsp", map)).build();
	}

	@DELETE
	@Path("/{id}")
	@RolesAllowed({ "admin" })
	public Response deleteDeviceRest(@PathParam("id") String id) {

		try {
			deleteDevice(id);
		} catch (Exception e) {
			return Response.serverError().build();
		}
		return Response.ok().build();
	}

	@GET
	@Path("/{id}/delete")
	@Produces(MediaType.APPLICATION_XHTML_XML)
	@RolesAllowed({ "admin" })
	public Response deleteDevice(@PathParam("id") String id,
			@Context HttpServletRequest httpRequest) {

		try {
			deleteDevice(id);
			showFlashMessage("Device sucessfully deleted.", "success");
		} catch (Exception e) {
			showFlashMessage(e.getMessage(), "error");
		}
		return Response.seeOther(URI.create("/devices")).build();
	}

	private void deleteDevice(String id) throws Exception {
		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();
		try {
			Device device = pm.getObjectById(Device.class,
					KeyFactory.stringToKey(id));
			pm.deletePersistent(device);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			pm.close();
		}
	}

	@GET
	@Path("/{id}/update")
	@Produces(MediaType.APPLICATION_XHTML_XML)
	@RolesAllowed({ "admin" })
	public Response updateDevice(@PathParam("id") String id,
			@Context HttpServletRequest httpRequest) {
		Map<String, Object> map = new HashMap<String, Object>();

		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();
		try {
			map.put("device",
					pm.getObjectById(Device.class, KeyFactory.stringToKey(id)));
		} catch (Exception e) {
			showFlashMessage("Error.", "error");
		}
		map.put("vendors", Vendor.getAll(pm));

		return Response.ok(new Viewable("/views/device/update.jsp", map))
				.build();
	}

	@PUT
	@Path("/{id}/")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({ "admin" })
	public Response updateDeviceSave(@PathParam("id") String id, DeviceMapping device) {

		try {
			updateDevice(id, device.getName(), device.getType(), device.getDescription());
		} catch (Exception e) {
			return Response.serverError().build();
		}

		return Response.ok().build();
	}	
	
	@POST
	@Path("/{id}/update")
	@Produces(MediaType.APPLICATION_XHTML_XML)
	@RolesAllowed({ "admin" })
	public Response updateDeviceSave(@PathParam("id") String id,
			@Context HttpServletRequest httpRequest,
			@FormParam("name") String deviceName,
			@FormParam("vendorId") String vendorId,
			@FormParam("type") String type,
			@FormParam("description") String description) {

		try {
			updateDevice(vendorId, deviceName, type, description);
			showFlashMessage("Device sucessfully updated.", "success");
		} catch (Exception e) {
			showFlashMessage(e.getMessage(), "error");
		}

		return Response.seeOther(URI.create("/devices")).build();
	}

	private void updateDevice(String id, String deviceName, String type,
			String description) throws Exception {

		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();

		Device device = null;
		try {
			device = pm.getObjectById(Device.class, KeyFactory.stringToKey(id));
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}

		if (deviceName == null || deviceName.isEmpty()) {
			throw new Exception("Missing device name.");
		} else if (type == null || type.isEmpty()) {
			throw new Exception("Missing type.");
		} else if (description == null || description.isEmpty()) {
			throw new Exception("Missing description.");
		} else {
			try {
				device.setName(deviceName);
				device.setType(type);
				device.setDescription(description);
				pm.makePersistent(device);
			} catch (Exception e) {
				throw new Exception(e.getMessage());
			} finally {
				pm.close();
			}
		}

	}

	@POST
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({ "admin" })
	public Response addDevice(DeviceMapping device) throws URISyntaxException {
		try {
			addDevice(device.getName(), device.getType(), device.getDescription(), device.getVendorId());
		} catch(Exception e) {
			Response.serverError().build();
		}		
		return Response.ok().build();
	}	
	
	@POST
	@Path("/new")
	@Produces(MediaType.APPLICATION_XHTML_XML)
	@RolesAllowed({ "admin" })
	public Response addDevice(@FormParam("name") String deviceName,
			@FormParam("vendorId") String vendorId,
			@FormParam("type") String type,
			@FormParam("description") String description,
			@Context HttpServletRequest httpRequest) throws URISyntaxException {

		try {
			addDevice(deviceName, type, description, vendorId);
			showFlashMessage("Device was successfully added.",
					"success");
		} catch(Exception e) {
			showFlashMessage(e.getMessage(), "error");			
		}
		
		return Response.seeOther(new URI("/devices/")).build();
	}
	
	private void addDevice(String deviceName, String type, String description, String vendorId) throws Exception {

		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();

		Device device = new Device();
		if (deviceName == null || deviceName.isEmpty()) {
			throw new Exception("Missing device name.");
		} else if (type == null || type.isEmpty()) {
			throw new Exception("Missing type.");
		} else if (description == null || description.isEmpty()) {
			throw new Exception("Missing description.");
		} else {
			device.setName(deviceName);
			device.setDescription(description);
			device.setType(type);
			Key key = KeyFactory.stringToKey(vendorId);
			Vendor vendor = pm.getObjectById(Vendor.class, key);
			try {
				if (device.getName() != null && !device.getName().isEmpty()) {
					vendor.addDevice(device);
					pm.makePersistent(vendor);
					pm.makePersistent(device);					
				} else {
					throw new Exception("Error.");
				}
			} finally {
				pm.close();
			}
		}		
	}

}
