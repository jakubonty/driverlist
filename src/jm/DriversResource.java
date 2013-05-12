package jm;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import jm.db.Driver;
import jm.db.OperatingSystem;
import jm.mapping.DriverList;
import jm.mapping.DriverMapping;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.sun.jersey.api.view.Viewable;

@Path("/drivers/")
public class DriversResource extends AppController {

	private com.google.appengine.api.blobstore.BlobstoreService blobstoreService = BlobstoreServiceFactory
			.getBlobstoreService();

	@GET
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)	
	public Response listSystemsJSON() {
		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();
		return Response.ok(new DriverList(Driver.getAll(pm))).build();
	}	
	
	@GET
	@Path("/new")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({"user", "admin"})
	public Response newDriver() {
		Map<String, Object> map = new HashMap<String, Object>();

		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();
		map.put("systems", OperatingSystem.getAll(pm));
		map.put("devices", Device.getAll(pm));

		beforeRender(map);
		return Response.ok(new Viewable("/views/driver/new.jsp", map)).build();
	}

	@POST
	@Path("/new")
	@Produces(MediaType.APPLICATION_XHTML_XML)
	@RolesAllowed({"user", "admin"})
	public Response addDriver(@FormParam("name") String driverName,
			@FormParam("deviceId") String deviceId,
			@FormParam("osId") String osId,
			@FormParam("version") String version,
			@Context HttpServletRequest httpRequest,
			@Context HttpServletResponse httpResponse)
			throws URISyntaxException {
		boolean valid = true;		
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		
		
		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();

		driverName = httpRequest.getParameter("name");
		deviceId = httpRequest.getParameter("deviceId");
		osId = httpRequest.getParameter("osId");
		version = httpRequest.getParameter("version");
		Driver driver = new Driver();
		Device device = null;
		try {
			Key key = KeyFactory.stringToKey(deviceId);
			device = pm.getObjectById(Device.class, key);
			key = KeyFactory.stringToKey(osId);
			OperatingSystem os = pm.getObjectById(OperatingSystem.class, key);
			driver.setOperatingSystem(os.getName());
			driver.setOs(os.getId());
		} catch (Exception e) {
			showFlashMessage("Error.","error");
			valid = false;
		}
		driver.setName(driverName);
		driver.setVersion(version);
		if (driver.getName() == null || driver.getName().isEmpty()) {
			showFlashMessage("Missing driver name.", "error");
			valid = false;
		} else if (driver.getVersion() == null || driver.getVersion().isEmpty()) {
			showFlashMessage("Missing version.", "error");
			valid = false;
		}
		if (valid) {
			try {
				driver.setAuthor(user);
				Map<String, List<BlobKey>> blobs = blobstoreService
						.getUploads(httpRequest);
				BlobKey blobKey = null;
				if (blobs.get("driver") == null) {
					showFlashMessage("Missing file.", "error");
				} else {
					blobKey = blobs.get("driver").get(0);
					driver.setData(blobKey);
					device.addDriver(driver);				
					pm.makePersistent(device);
					pm.makePersistent(driver);
					showFlashMessage("Driver was successfully added.", "success");
				}
			} finally {
				pm.close();
			}
		}
		return Response.seeOther(new URI("/")).build();
	}	
	
	@GET
	@Path("/{id}")
	public Response downloadDriver(@PathParam("id") String id, @Context HttpServletResponse httpResponse) throws IOException {
		com.google.appengine.api.blobstore.BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
		BlobKey blobKey = new BlobKey(id);
		blobstoreService.serve(blobKey, httpResponse);
		return Response.created(null).status(HttpServletResponse.SC_OK).build();
	}
	
	@GET
	@Path("/{id}/download")
	public Response downloadDriverJSON(@PathParam("id") String id, @Context HttpServletResponse httpResponse) throws IOException {
		com.google.appengine.api.blobstore.BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
		BlobKey blobKey = new BlobKey(id);
		blobstoreService.serve(blobKey, httpResponse);
		return Response.created(null).status(HttpServletResponse.SC_OK).build();
	}		
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_XHTML_XML)
	@RolesAllowed({"admin"})
	public Response listDrivers() {
		Map<String, Object> map = new HashMap<String, Object>();

		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();
		map.put("drivers", Driver.getAll(pm));

		return Response.ok(new Viewable("/views/driver/list.jsp", map)).build();
	}

	
	@DELETE
	@Path("/{id}")
	@RolesAllowed({"admin"})
	public Response deleteDriverRest(@PathParam("id") String id) {
		try {
			deleteDriver(id);			
		} catch(Exception e) {
			Response.serverError().build();
		} 
		return Response.ok().build();
	}	
	
	@GET
	@Path("/{id}/delete")
	@Produces(MediaType.APPLICATION_XHTML_XML)
	@RolesAllowed({"admin"})
	public Response deleteDriver(@PathParam("id") String id,
			@Context HttpServletRequest httpRequest) {
		try {
			deleteDriver(id);
			showFlashMessage("Driver sucessfully deleted.", "success");
		} catch(Exception e) {
			showFlashMessage(e.getMessage(), "error");
		} 
		return Response.seeOther(URI.create("/drivers")).build();
	}
	
	private void deleteDriver(String id) throws Exception {
		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();
		try {
			Driver driver = pm.getObjectById(Driver.class,
					KeyFactory.stringToKey(id));
			pm.deletePersistent(driver);			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			pm.close();
		}		
	}

	@GET
	@Path("/{id}/update")
	@Produces(MediaType.APPLICATION_XHTML_XML)
	@RolesAllowed({"admin"})
	public Response newDriver(@PathParam("id") String id) {
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

	@PUT
	@Path("/{id}/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({"admin"})
	public Response updateDriver(@PathParam("id") String id, DriverMapping driver) throws URISyntaxException {

		try {
			updateDriver(id, driver.getOperatingSystem(), driver.getName(), driver.getVersion());
		} catch(Exception e) {
			return Response.serverError().build();
		}
		
		return Response.ok().build();
	}	
	
	@POST
	@Path("/{id}/update")
	@Produces(MediaType.APPLICATION_XHTML_XML)
	@RolesAllowed({"admin"})
	public Response updateDriver(@PathParam("id") String id,
			@FormParam("name") String driverName,
			@FormParam("osId") String osId,
			@FormParam("version") String version,
			@Context HttpServletRequest httpRequest) throws URISyntaxException {
		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();
		OperatingSystem os = null;
		try {
			Key key = KeyFactory.stringToKey(osId);
			os = pm.getObjectById(OperatingSystem.class, key);			
			updateDriver(id, os.getName(), driverName, version);
			showFlashMessage("Driver was successfully updated.", "success");
		} catch(Exception e) {
			showFlashMessage(e.getMessage(), "error");
		}
		
		return Response.seeOther(new URI("/drivers")).build();
	}	
	
	private void updateDriver(String id, String osName, String driverName, String version) throws Exception {
		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();

		Driver driver = null;		
		try {
			Key key = KeyFactory.stringToKey(id);
			driver = pm.getObjectById(Driver.class, key);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		driver.setName(driverName);
		driver.setVersion(version);
		driver.setOperatingSystem(osName);
		try {
			if (driver.getName() != null && !driver.getName().isEmpty()) {
				pm.makePersistent(driver);				
			} else {
				throw new Exception("Error.");
			}
		} finally {
			pm.close();
		}		
	}
	
}
