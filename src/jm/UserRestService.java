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
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import jm.common.FlashMessage;
import jm.db.Device;
import jm.db.Driver;
import jm.db.OperatingSystem;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.sun.jersey.api.view.Viewable;

@Path("/user/")
public class UserRestService extends AppController {
	private com.google.appengine.api.blobstore.BlobstoreService blobstoreService = BlobstoreServiceFactory
			.getBlobstoreService();

	@GET
	@Path("/drivers/new")
	@Produces(MediaType.APPLICATION_JSON)
	public Response newDriver() {
		Map<String, Object> map = new HashMap<String, Object>();

		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();
		map.put("systems", OperatingSystem.getAll(pm));
		map.put("devices", Device.getAll(pm));

		beforeRender(map);
		return Response.ok(new Viewable("/views/driver/new.jsp", map)).build();
	}

	@POST
	@Path("/drivers/new")
	@Produces(MediaType.APPLICATION_XHTML_XML)
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
}
