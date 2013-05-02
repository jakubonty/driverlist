package jm;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import jm.db.Device;
import jm.db.OperatingSystem;
import jm.db.Vendor;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.sun.jersey.api.view.Viewable;

@Path("/admin/")
public class AdminRestService extends AppController {

	@GET
	@Path("/vendor/new")
	@Produces(MediaType.APPLICATION_JSON)
	public Response newVendor() {
		Map<String, Object> map = new HashMap<String, Object>();

		beforeRender(map);
		return Response.ok(new Viewable("/views/vendor/new.jsp", map)).build();
	}

	@POST
	@Path("/vendor/new")
	@Produces(MediaType.APPLICATION_XHTML_XML)
	public Response addVendor(@FormParam("name") String vendorName, @Context HttpServletRequest httpRequest) throws URISyntaxException {
		HttpSession session= httpRequest.getSession(true);
		Map<String, Object> map = new HashMap<String, Object>();

		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();
		
		Vendor vendor = new Vendor();
		vendor.setName(vendorName);
		try {
			if (vendor.getName() != null && !vendor.getName().isEmpty()) {
				pm.makePersistent(vendor);
				session.setAttribute("flashMessage", new FlashMessage("Driver was successfully added.", "success"));
			} else {
				session.setAttribute("flashMessage", new FlashMessage("Error.", "error"));
			}
		} finally {
			pm.close();
		}

		beforeRender(map);
		return Response.seeOther(new URI("/test/")).build();
	}
	
	@GET
	@Path("/operating-system/new")
	@Produces(MediaType.APPLICATION_JSON)
	public Response newOperatingSystem() {
		Map<String, Object> map = new HashMap<String, Object>();

		beforeRender(map);
		return Response.ok(new Viewable("/views/OS/new.jsp", map)).build();
	}

	@POST
	@Path("/operating-system/new")
	@Produces(MediaType.APPLICATION_XHTML_XML)
	public Response addOperatingSystem(@FormParam("name") String osName, @Context HttpServletRequest httpRequest) throws URISyntaxException {
		HttpSession session= httpRequest.getSession(true);
		Map<String, Object> map = new HashMap<String, Object>();

		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();
		
		OperatingSystem os = new OperatingSystem();
		os.setName(osName);
		try {
			if (os.getName() != null && !os.getName().isEmpty()) {
				pm.makePersistent(os);
				session.setAttribute("flashMessage", new FlashMessage("Driver was successfully added.", "success"));
			} else {
				session.setAttribute("flashMessage", new FlashMessage("Error.", "error"));
			}
		} finally {
			pm.close();
		}

		beforeRender(map);
		return Response.seeOther(new URI("/test/")).build();
	}
	
	@GET
	@Path("/device/new")
	@Produces(MediaType.APPLICATION_JSON)
	public Response newDevice() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();
		map.put("vendors", Vendor.getAll(pm));

		beforeRender(map);
		return Response.ok(new Viewable("/views/device/new.jsp", map)).build();
	}

	@POST
	@Path("/device/new")
	@Produces(MediaType.APPLICATION_XHTML_XML)
	public Response addDevice(@FormParam("name") String deviceName,
													@FormParam("vendorId") String vendorId,
													@FormParam("type") String type,
													@FormParam("description") String description,
													@Context HttpServletRequest httpRequest
			) throws URISyntaxException {
		HttpSession session= httpRequest.getSession(true);
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
				session.setAttribute("flashMessage", new FlashMessage("Driver was successfully added.", "success"));
			} else {
				session.setAttribute("flashMessage", new FlashMessage("Error.", "error"));
			}
		} finally {
			pm.close();
		}

		beforeRender(map);
		return Response.seeOther(new URI("/rest/front/index")).build();
	}			
}
