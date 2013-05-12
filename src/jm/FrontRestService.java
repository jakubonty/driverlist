package jm;

import java.util.HashMap;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import jm.db.Vendor;

import com.sun.jersey.api.view.Viewable;

@Path("/")
public class FrontRestService extends AppController {
	/*
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
	}*/
	
    @GET
    @Produces(MediaType.TEXT_HTML)
	public Response addDevice() {
		Map<String, Object> map = new HashMap<String, Object>();

		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();
		map.put("vendors", Vendor.getAll(pm));				
		
		beforeRender(map);
		return Response.ok(new Viewable("/views/driver/search.jsp", map)).build();
	}		
	/*
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
	@Path("/device/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response showDeviceJSON(@PathParam("id") String id) {
		Map<String, Object> map = new HashMap<String, Object>();

		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();
		
		Key key = KeyFactory.stringToKey(id);
		Device device = pm.getObjectById(Device.class, key);		
		
		map.put("systems", OperatingSystem.getAll(pm));	
		map.put("device", device);				
		
		beforeRender(map);
		return Response.ok(new DeviceMapping(device)).build();
	}
	
	@GET
	@Path("/devices/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response showDevicesJSON() {
		Map<String, Object> map = new HashMap<String, Object>();

		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();		
		
		beforeRender(map);
		return Response.ok(new DeviceList(Device.getAll(pm))).build();
	}	*/	
	/*
	@GET
	@Path("/driver/{id}")
	public Response downloadDriver(@PathParam("id") String id, @Context HttpServletResponse httpResponse) throws IOException {
		com.google.appengine.api.blobstore.BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
		BlobKey blobKey = new BlobKey(id);
		blobstoreService.serve(blobKey, httpResponse);
		return Response.created(null).status(HttpServletResponse.SC_OK).build();
	}
	
	@GET
	@Path("/driver/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)	
	public Response downloadDriverJSON(@PathParam("id") String id, @Context HttpServletResponse httpResponse) throws IOException {
		com.google.appengine.api.blobstore.BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
		BlobKey blobKey = new BlobKey(id);
		blobstoreService.serve(blobKey, httpResponse);
		return Response.created(null).status(HttpServletResponse.SC_OK).build();
	}*/	
}
