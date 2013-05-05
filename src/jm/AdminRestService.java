package jm;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import jm.common.FlashMessage;
import jm.db.Device;
import jm.db.OperatingSystem;
import jm.db.Vendor;
import jm.db.Driver;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.sun.jersey.api.view.Viewable;

@Path("/admin/")
public class AdminRestService extends AppController {		
	
	@GET
	@Path("/drivers/")
	@Produces(MediaType.APPLICATION_XHTML_XML)
	public Response listDrivers() {
		Map<String, Object> map = new HashMap<String, Object>();

		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();
		map.put("drivers", Driver.getAll(pm));

		return Response.ok(new Viewable("/views/driver/list.jsp", map)).build();
	}

	@GET
	@Path("/drivers/{id}/delete")
	@Produces(MediaType.APPLICATION_XHTML_XML)
	public Response deleteDriver(@PathParam("id") String id,
			@Context HttpServletRequest httpRequest) {
		HttpSession session = httpRequest.getSession(true);
		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();
		try {
			Driver driver = pm.getObjectById(Driver.class,
					KeyFactory.stringToKey(id));
			pm.deletePersistent(driver);
			showFlashMessage("Driver sucessfully deleted.", "success");
		} catch (Exception e) {
			session.setAttribute("flashMessage", new FlashMessage("Error.",
					"error"));
		}
		return Response.seeOther(URI.create("/admin/drivers")).build();
	}

	@GET
	@Path("/drivers/{id}/update")
	@Produces(MediaType.APPLICATION_XHTML_XML)
	public Response newDriver(@PathParam("id") String id) {
		HttpSession session = httpRequest.getSession(true);
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();
			map.put("systems", OperatingSystem.getAll(pm));
			map.put("devices", Device.getAll(pm));
			map.put("driver",
				pm.getObjectById(Driver.class, KeyFactory.stringToKey(id)));
		} catch(Exception e) {
			showFlashMessage("Error.", "error");
		}
		return Response.ok(new Viewable("/views/driver/update.jsp", map))
				.build();
	}

	@POST
	@Path("/drivers/{id}/update")
	@Produces(MediaType.APPLICATION_XHTML_XML)
	public Response addDriver(@PathParam("id") String id,
			@FormParam("name") String driverName,
			@FormParam("osId") String osId,
			@FormParam("version") String version,
			@Context HttpServletRequest httpRequest) throws URISyntaxException {

		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();

		Driver driver = null;
		OperatingSystem os = null;
		try {
			Key key = KeyFactory.stringToKey(id);
			driver = pm.getObjectById(Driver.class, key);
			key = KeyFactory.stringToKey(osId);
			os = pm.getObjectById(OperatingSystem.class, key);
		} catch (Exception e) {
			showFlashMessage("Error.", "error");
		}
		driver.setName(driverName);
		driver.setVersion(version);
		driver.setOperatingSystem(os.getName());
		try {
			if (driver.getName() != null && !driver.getName().isEmpty()) {
				pm.makePersistent(driver);
				showFlashMessage("Driver was successfully updated.", "success");
			} else {
				showFlashMessage("Error.", "error");
			}
		} finally {
			pm.close();
		}
		return Response.seeOther(new URI("/admin/drivers")).build();
	}

	@GET
	@Path("/vendors/")
	@Produces(MediaType.APPLICATION_XHTML_XML)
	public Response listVendors() {
		Map<String, Object> map = new HashMap<String, Object>();

		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();
		map.put("vendors", Vendor.getAll(pm));

		return Response.ok(new Viewable("/views/vendor/list.jsp", map)).build();
	}

	@GET
	@Path("/vendors/{id}/delete")
	@Produces(MediaType.APPLICATION_XHTML_XML)
	public Response deleteVendor(@PathParam("id") String id,
			@Context HttpServletRequest httpRequest) {
		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();
		try {
			Vendor vendor = pm.getObjectById(Vendor.class,
					KeyFactory.stringToKey(id));
			pm.deletePersistent(vendor);
			showFlashMessage("Vendor sucessfully deleted.", "success");
		} catch (Exception e) {
			showFlashMessage("Error.", "error");
		} finally {
			pm.close();
		}
		return Response.seeOther(URI.create("/admin/vendors")).build();
	}

	@GET
	@Path("/vendors/{id}/update")
	@Produces(MediaType.APPLICATION_XHTML_XML)
	public Response updateVendor(@PathParam("id") String id,
			@Context HttpServletRequest httpRequest) {		
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();
			map.put("vendor",
					pm.getObjectById(Vendor.class, KeyFactory.stringToKey(id)));
		} catch (Exception e) {
			showFlashMessage("Error.", "error");
		}

		return Response.ok(new Viewable("/views/vendor/update.jsp", map))
				.build();
	}

	@POST
	@Path("/vendors/{id}/update")
	@Produces(MediaType.APPLICATION_XHTML_XML)
	public Response updateVendorSave(@PathParam("id") String id,
			@FormParam("name") String vendorName,
			@Context HttpServletRequest httpRequest) {
		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();
		Vendor vendor = null;
		try {
			vendor = pm.getObjectById(Vendor.class, KeyFactory.stringToKey(id));
			if (vendorName == null || vendorName.isEmpty()) {
				showFlashMessage("Vendor name missing.", "error");
			} else {
				vendor.setName(vendorName);
				pm.makePersistent(vendor);
				showFlashMessage("Vendor sucessfully updated.", "success");
			}
		} catch (Exception e) {
			showFlashMessage("Error.", "error");
		} finally {
			pm.close();
		}

		return Response.seeOther(URI.create("/admin/vendors")).build();
	}

	@GET
	@Path("/vendors/new")
	@Produces(MediaType.APPLICATION_XHTML_XML)
	public Response newVendor() {
		Map<String, Object> map = new HashMap<String, Object>();

		return Response.ok(new Viewable("/views/vendor/new.jsp", map)).build();
	}

	@POST
	@Path("/vendors/new")
	@Produces(MediaType.APPLICATION_XHTML_XML)
	public Response addVendor(@FormParam("name") String vendorName,
			@Context HttpServletRequest httpRequest) throws URISyntaxException {
		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();

		Vendor vendor = new Vendor();
		vendor.setName(vendorName);
		try {
			if (vendor.getName() != null && !vendor.getName().isEmpty()) {
				pm.makePersistent(vendor);
				showFlashMessage("Vendor was successfully added.", "success");
			} else {
				showFlashMessage("Error.", "error");
			}
		} finally {
			pm.close();
		}

		return Response.seeOther(new URI("/admin/vendors/")).build();
	}

	@GET
	@Path("/operating-systems/")
	@Produces(MediaType.APPLICATION_XHTML_XML)
	public Response listSystems() {
		Map<String, Object> map = new HashMap<String, Object>();

		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();
		map.put("systems", OperatingSystem.getAll(pm));

		return Response.ok(new Viewable("/views/OS/list.jsp", map)).build();
	}

	@GET
	@Path("/operating-systems/{id}/delete")
	@Produces(MediaType.APPLICATION_XHTML_XML)
	public Response deleteOperatingSystem(@PathParam("id") String id,
			@Context HttpServletRequest httpRequest) {
		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();
		try {
			OperatingSystem os = pm.getObjectById(OperatingSystem.class,
					KeyFactory.stringToKey(id));
			pm.deletePersistent(os);
			showFlashMessage("Operating system sucessfully deleted.", "success");
		} catch (Exception e) {
			showFlashMessage("Error.", "error");
		} finally {
			pm.close();
		}
		return Response.seeOther(URI.create("/admin/operating-systems"))
				.build();
	}

	@GET
	@Path("/operating-systems/{id}/update")
	@Produces(MediaType.APPLICATION_XHTML_XML)
	public Response updateOperatingSystem(@PathParam("id") String id,
			@Context HttpServletRequest httpRequest) {
		Map<String, Object> map = new HashMap<String, Object>();

		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();
		try {
			map.put("os",
					pm.getObjectById(OperatingSystem.class,
							KeyFactory.stringToKey(id)));
		} catch (Exception e) {
			showFlashMessage("Error.", "error");
		}

		return Response.ok(new Viewable("/views/OS/update.jsp", map)).build();
	}

	@POST
	@Path("/operating-systems/{id}/update")
	@Produces(MediaType.APPLICATION_XHTML_XML)
	public Response updateOperatingSystemSave(@PathParam("id") String id,
			@FormParam("name") String osName,
			@Context HttpServletRequest httpRequest) {
		boolean valid = true;

		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();

		OperatingSystem os = null;
		try {
			os = pm.getObjectById(OperatingSystem.class,
					KeyFactory.stringToKey(id));
			os.setName(osName);
		} catch (Exception e) {
			valid = false;
		}
		if (valid) {
			pm.makePersistent(os);
			showFlashMessage("Operating system sucessfully updated.", "success");
		} else {
			showFlashMessage("Error.", "error");
		}
		return Response.seeOther(URI.create("/admin/operating-systems"))
				.build();
	}

	@GET
	@Path("/operating-systems/new")
	@Produces(MediaType.APPLICATION_XHTML_XML)
	public Response newOperatingSystem() {
		Map<String, Object> map = new HashMap<String, Object>();

		return Response.ok(new Viewable("/views/OS/new.jsp", map)).build();
	}

	@POST
	@Path("/operating-systems/new")
	@Produces(MediaType.APPLICATION_XHTML_XML)
	public Response addOperatingSystem(@FormParam("name") String osName,
			@Context HttpServletRequest httpRequest) throws URISyntaxException {
		Map<String, Object> map = new HashMap<String, Object>();

		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();

		OperatingSystem os = new OperatingSystem();
		os.setName(osName);
		try {
			if (os.getName() != null && !os.getName().isEmpty()) {
				pm.makePersistent(os);
				showFlashMessage("Operating system was successfully added.", "success");
			} else {
				showFlashMessage("Error.", "error");
			}
		} finally {
			pm.close();
		}

		beforeRender(map);
		return Response.seeOther(new URI("/admin/operating-systems/")).build();
	}

	@GET
	@Path("/devices/")
	@Produces(MediaType.APPLICATION_XHTML_XML)
	public Response listDevices() {
		Map<String, Object> map = new HashMap<String, Object>();

		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();
		map.put("devices", Device.getAll(pm));

		beforeRender(map);
		return Response.ok(new Viewable("/views/device/list.jsp", map)).build();
	}

	@GET
	@Path("/devices/new")
	@Produces(MediaType.APPLICATION_XHTML_XML)
	public Response newDevice() {
		Map<String, Object> map = new HashMap<String, Object>();

		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();
		map.put("vendors", Vendor.getAll(pm));

		beforeRender(map);
		return Response.ok(new Viewable("/views/device/new.jsp", map)).build();
	}

	@GET
	@Path("/devices/{id}/delete")
	@Produces(MediaType.APPLICATION_XHTML_XML)
	public Response deleteDevice(@PathParam("id") String id,
			@Context HttpServletRequest httpRequest) {
		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();
		try {
			Device device = pm.getObjectById(Device.class,
					KeyFactory.stringToKey(id));
			pm.deletePersistent(device);
			showFlashMessage("Device sucessfully deleted.", "success");
		} catch (Exception e) {
			showFlashMessage("Error.", "error");
		}
		return Response.seeOther(URI.create("/admin/devices")).build();
	}

	@GET
	@Path("/devices/{id}/update")
	@Produces(MediaType.APPLICATION_XHTML_XML)
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

	@POST
	@Path("/devices/{id}/update")
	@Produces(MediaType.APPLICATION_XHTML_XML)
	public Response updateDeviceSave(@PathParam("id") String id,
			@Context HttpServletRequest httpRequest,
			@FormParam("name") String deviceName,
			@FormParam("vendorId") String vendorId,
			@FormParam("type") String type,
			@FormParam("description") String description) {
		boolean valid = true;

		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();

		Device device = null;
		try {
			device = pm.getObjectById(Device.class, KeyFactory.stringToKey(id));
		} catch (Exception e) {
			showFlashMessage("Error.", "error");
			valid = false;
		}

		if (valid) {
			if (deviceName == null || deviceName.isEmpty()) {
				showFlashMessage("Missing device name.", "error");
			} else if (type == null || type.isEmpty()) {
				showFlashMessage("Missing type.", "error");
			} else if (description == null || description.isEmpty()) {
				showFlashMessage("Missing description.", "error");
			} else {
				device.setName(deviceName);
				device.setType(type);
				device.setDescription(description);
				pm.makePersistent(device);
			}
		}

		showFlashMessage("Device sucessfully updated.", "success");

		return Response.seeOther(URI.create("/admin/devices")).build();
	}

	@POST
	@Path("/devices/new")
	@Produces(MediaType.APPLICATION_XHTML_XML)
	public Response addDevice(@FormParam("name") String deviceName,
			@FormParam("vendorId") String vendorId,
			@FormParam("type") String type,
			@FormParam("description") String description,
			@Context HttpServletRequest httpRequest) throws URISyntaxException {

		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();

		Device device = new Device();
		if (deviceName == null || deviceName.isEmpty()) {
			showFlashMessage("Missing device name.", "error");
		} else if (type == null || type.isEmpty()) {
			showFlashMessage("Missing type.", "error");
		} else if (description == null || description.isEmpty()) {
			showFlashMessage("Missing description.", "error");
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
					showFlashMessage("Device was successfully added.", "success");
				} else {
					showFlashMessage("Error.", "error");
				}
			} finally {
				pm.close();
			}
		}
		return Response.seeOther(new URI("/admin/devices/")).build();
	}
}
