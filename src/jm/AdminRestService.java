package jm;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.KeyFactory.Builder;
import com.sun.jersey.api.view.Viewable;

@Path("/admin/")
public class AdminRestService extends AppController {
	@GET
	@Path("/drivers/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listDrivers() {
		Map<String, Object> map = new HashMap<String, Object>();

		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();
		map.put("drivers", Driver.getAll(pm));

		return Response.ok(new Viewable("/views/driver/list.jsp", map)).build();
	}

	@GET
	@Path("/drivers/{id}/update")
	@Produces(MediaType.APPLICATION_JSON)
	public Response newDriver(@PathParam("id") String id) {
		Map<String, Object> map = new HashMap<String, Object>();

		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();
		map.put("systems", OperatingSystem.getAll(pm));
		map.put("devices", Device.getAll(pm));
		map.put("driver",
				pm.getObjectById(Driver.class, KeyFactory.stringToKey(id)));

		beforeRender(map);
		return Response.ok(new Viewable("/views/driver/update.jsp", map))
				.build();
	}

	@POST
	@Path("/drivers/{id}/update")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addDriver(@PathParam("id") String id,
			@FormParam("name") String driverName,
			@FormParam("osId") String osId,
			@FormParam("version") String version,
			@Context HttpServletRequest httpRequest) throws URISyntaxException {
		HttpSession session = httpRequest.getSession(true);

		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();

		Key key = KeyFactory.stringToKey(id);
		Driver driver = pm.getObjectById(Driver.class, key);
		key = KeyFactory.stringToKey(osId);
		OperatingSystem os = pm.getObjectById(OperatingSystem.class, key);
		driver.setName(driverName);
		driver.setVersion(version);
		driver.setOperatingSystem(os.getName());
		try {
			if (driver.getName() != null && !driver.getName().isEmpty()) {
				pm.makePersistent(driver);
				session.setAttribute("flashMessage", new FlashMessage(
						"Driver was successfully updated.", "success"));
			} else {
				session.setAttribute("flashMessage", new FlashMessage("Error.",
						"error"));
			}
		} finally {
			pm.close();
		}

		return Response.seeOther(new URI("/admin/drivers")).build();
	}

	@GET
	@Path("/vendors/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listVendors() {
		Map<String, Object> map = new HashMap<String, Object>();

		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();
		map.put("vendors", Vendor.getAll(pm));

		return Response.ok(new Viewable("/views/vendor/list.jsp", map)).build();
	}

	@GET
	@Path("/vendors/{id}/update")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateVendor(@PathParam("id") String id) {
		Map<String, Object> map = new HashMap<String, Object>();

		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();
		map.put("vendor",
				pm.getObjectById(Vendor.class, KeyFactory.stringToKey(id)));

		return Response.ok(new Viewable("/views/vendor/update.jsp", map))
				.build();
	}

	@POST
	@Path("/vendors/{id}/update")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateVendorSave(@PathParam("id") String id,
			@FormParam("name") String vendorName,
			@Context HttpServletRequest httpRequest) {
		HttpSession session = httpRequest.getSession(true);
		Map<String, Object> map = new HashMap<String, Object>();

		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();
		Vendor vendor = pm.getObjectById(Vendor.class,
				KeyFactory.stringToKey(id));
		vendor.setName(vendorName);

		pm.makePersistent(vendor);
		session.setAttribute("flashMessage", new FlashMessage(
				"Vendor sucessfully updated.", "success"));
		return Response.seeOther(URI.create("/admin/vendors")).build();
	}

	@GET
	@Path("/vendors/new")
	@Produces(MediaType.APPLICATION_JSON)
	public Response newVendor() {
		Map<String, Object> map = new HashMap<String, Object>();

		return Response.ok(new Viewable("/views/vendor/new.jsp", map)).build();
	}

	@POST
	@Path("/vendors/new")
	@Produces(MediaType.APPLICATION_XHTML_XML)
	public Response addVendor(@FormParam("name") String vendorName,
			@Context HttpServletRequest httpRequest) throws URISyntaxException {
		HttpSession session = httpRequest.getSession(true);
		Map<String, Object> map = new HashMap<String, Object>();

		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();

		Vendor vendor = new Vendor();
		vendor.setName(vendorName);
		try {
			if (vendor.getName() != null && !vendor.getName().isEmpty()) {
				pm.makePersistent(vendor);
				session.setAttribute("flashMessage", new FlashMessage(
						"Driver was successfully added.", "success"));
			} else {
				session.setAttribute("flashMessage", new FlashMessage("Error.",
						"error"));
			}
		} finally {
			pm.close();
		}

		return Response.seeOther(new URI("/")).build();
	}

	@GET
	@Path("/operating-systems/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listSystems() {
		Map<String, Object> map = new HashMap<String, Object>();

		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();
		map.put("systems", OperatingSystem.getAll(pm));

		return Response.ok(new Viewable("/views/OS/list.jsp", map)).build();
	}

	@GET
	@Path("/operating-systems/{id}/delete")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteOperatingSystem(@PathParam("id") String id,
			@Context HttpServletRequest httpRequest) {
		HttpSession session = httpRequest.getSession(true);
		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();
		try {
			Device device = pm.getObjectById(Device.class,
					KeyFactory.stringToKey(id));
			pm.deletePersistent(device);
			session.setAttribute("flashMessage", new FlashMessage(
					"Operating system sucessfully deleted.", "success"));
		} catch (Exception e) {
			session.setAttribute("flashMessage", new FlashMessage("Error.",
					"error"));
		}
		return Response.seeOther(URI.create("/admin/operating-systems")).build();
	}	
	
	@GET
	@Path("/operating-systems/{id}/update")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateOperatingSystem(@PathParam("id") String id) {
		Map<String, Object> map = new HashMap<String, Object>();

		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();
		map.put("os",
				pm.getObjectById(OperatingSystem.class,
						KeyFactory.stringToKey(id)));

		return Response.ok(new Viewable("/views/OS/update.jsp", map)).build();
	}

	@POST
	@Path("/operating-systems/{id}/update")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateOperatingSystemSave(@PathParam("id") String id,
			@FormParam("name") String osName,
			@Context HttpServletRequest httpRequest) {
		HttpSession session = httpRequest.getSession(true);
		Map<String, Object> map = new HashMap<String, Object>();

		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();
		OperatingSystem os = pm.getObjectById(OperatingSystem.class,
				KeyFactory.stringToKey(id));
		os.setName(osName);

		pm.makePersistent(os);
		session.setAttribute("flashMessage", new FlashMessage(
				"Operating system sucessfully updated.", "success"));
		return Response.seeOther(URI.create("/admin/operating-systems"))
				.build();
	}

	@GET
	@Path("/operating-systems/new")
	@Produces(MediaType.APPLICATION_JSON)
	public Response newOperatingSystem() {
		Map<String, Object> map = new HashMap<String, Object>();

		return Response.ok(new Viewable("/views/OS/new.jsp", map)).build();
	}

	@POST
	@Path("/operating-systems/new")
	@Produces(MediaType.APPLICATION_XHTML_XML)
	public Response addOperatingSystem(@FormParam("name") String osName,
			@Context HttpServletRequest httpRequest) throws URISyntaxException {
		HttpSession session = httpRequest.getSession(true);
		Map<String, Object> map = new HashMap<String, Object>();

		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();

		OperatingSystem os = new OperatingSystem();
		os.setName(osName);
		try {
			if (os.getName() != null && !os.getName().isEmpty()) {
				pm.makePersistent(os);
				session.setAttribute("flashMessage", new FlashMessage(
						"Driver was successfully added.", "success"));
			} else {
				session.setAttribute("flashMessage", new FlashMessage("Error.",
						"error"));
			}
		} finally {
			pm.close();
		}

		beforeRender(map);
		return Response.seeOther(new URI("/test/")).build();
	}

	@GET
	@Path("/devices/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listDevices() {
		Map<String, Object> map = new HashMap<String, Object>();

		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();
		map.put("devices", Device.getAll(pm));

		beforeRender(map);
		return Response.ok(new Viewable("/views/device/list.jsp", map)).build();
	}

	@GET
	@Path("/devices/new")
	@Produces(MediaType.APPLICATION_JSON)
	public Response newDevice() {
		Map<String, Object> map = new HashMap<String, Object>();

		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();
		map.put("vendors", Vendor.getAll(pm));

		beforeRender(map);
		return Response.ok(new Viewable("/views/device/new.jsp", map)).build();
	}

	@GET
	@Path("/devices/{id}/delete")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteDevice(@PathParam("id") String id,
			@Context HttpServletRequest httpRequest) {
		HttpSession session = httpRequest.getSession(true);
		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();
		try {
			Device device = pm.getObjectById(Device.class,
					KeyFactory.stringToKey(id));
			pm.deletePersistent(device);
			session.setAttribute("flashMessage", new FlashMessage(
					"Device sucessfully deleted.", "success"));
		} catch (Exception e) {
			session.setAttribute("flashMessage", new FlashMessage("Error.",
					"error"));
		}
		return Response.seeOther(URI.create("/admin/devices")).build();
	}

	@GET
	@Path("/devices/{id}/update")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateDevice(@PathParam("id") String id) {
		Map<String, Object> map = new HashMap<String, Object>();

		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();
		map.put("device",
				pm.getObjectById(Device.class, KeyFactory.stringToKey(id)));
		map.put("vendors", Vendor.getAll(pm));

		return Response.ok(new Viewable("/views/device/update.jsp", map))
				.build();
	}

	@POST
	@Path("/devices/{id}/update")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateDeviceSave(@PathParam("id") String id,
			@Context HttpServletRequest httpRequest,
			@FormParam("name") String deviceName,
			@FormParam("vendorId") String vendorId,
			@FormParam("type") String type,
			@FormParam("description") String description) {
		HttpSession session = httpRequest.getSession(true);
		Map<String, Object> map = new HashMap<String, Object>();

		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();
		Device device = pm.getObjectById(Device.class,
				KeyFactory.stringToKey(id));

		device.setName(deviceName);
		device.setType(type);
		device.setDescription(description);

		pm.makePersistent(device);

		session.setAttribute("flashMessage", new FlashMessage(
				"Device sucessfully updated.", "success"));

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
		HttpSession session = httpRequest.getSession(true);
		Map<String, Object> map = new HashMap<String, Object>();

		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();

		Device device = new Device();
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
				session.setAttribute("flashMessage", new FlashMessage(
						"Driver was successfully added.", "success"));
			} else {
				session.setAttribute("flashMessage", new FlashMessage("Error.",
						"error"));
			}
		} finally {
			pm.close();
		}

		beforeRender(map);
		return Response.seeOther(new URI("/")).build();
	}
}
