package jm;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import jm.db.DeviceList;
import jm.db.Driver;
import jm.db.OperatingSystem;
import jm.db.Vendor;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.blobstore.BlobstoreServicePb.BlobstoreService;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.sun.jersey.api.view.Viewable;

@Path("/front/")
public class FrontRestService extends AppController {
	
	@GET
	@Path("/search")
	@Produces(MediaType.APPLICATION_JSON)
	public Response search(@Context HttpServletRequest httpRequest) {
		
		String vendorId = httpRequest.getParameter("vendorId");
		String type = httpRequest.getParameter("type");
		
		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();
		Key key = KeyFactory.stringToKey(vendorId);
		ArrayList<Device> result = (ArrayList<Device>) Device.findBy(pm, key, type);
		DeviceList deviceList = new DeviceList(result);
		return Response.ok().entity(deviceList).build();
	}
	
	@GET
	@Path("/index")
	@Produces(MediaType.APPLICATION_XHTML_XML)
	public Response addDevice() {
		Map<String, Object> map = new HashMap<String, Object>();

		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();
		map.put("vendors", Vendor.getAll(pm));				
		
		beforeRender(map);
		return Response.ok(new Viewable("/views/driver/search.jsp", map)).build();
	}		
	
	@GET
	@Path("/device/{id}")
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
	@Path("/driver/{id}")
	@Produces(MediaType.APPLICATION_XHTML_XML)
	public void downloadDriver(@PathParam("id") String id, @Context HttpServletResponse httpResponse) throws IOException {
		com.google.appengine.api.blobstore.BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
		BlobKey blobKey = new BlobKey(id);
		blobstoreService.serve(blobKey, httpResponse);
	}		
}
