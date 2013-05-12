package jm;

import java.net.URI;
import java.net.URISyntaxException;
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

import jm.db.Vendor;
import jm.mapping.VendorList;
import jm.mapping.VendorMapping;

import com.google.appengine.api.datastore.KeyFactory;
import com.sun.jersey.api.view.Viewable;

@Path("/vendors/")
public class VendorsResource extends AppController {
	
	@GET
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)	
	public Response listVendorsJSON() {
		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();
		return Response.ok(new VendorList(Vendor.getAll(pm))).build();
	}	
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_XHTML_XML)
	@RolesAllowed({"admin"})
	public Response listVendors() {
		Map<String, Object> map = new HashMap<String, Object>();

		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();
		map.put("vendors", Vendor.getAll(pm));

		return Response.ok(new Viewable("/views/vendor/list.jsp", map)).build();
	}

	@DELETE
	@Path("/{id}/delete")
	@Produces(MediaType.APPLICATION_XHTML_XML)
	@RolesAllowed({"admin"})
	public Response deleteVendorRest(@PathParam("id") String id,
			@Context HttpServletRequest httpRequest) {
		try {
			deleteVendor(id);
		} catch(Exception e) {
			return Response.serverError().build();
		} 
		return Response.ok().build();
	}
	
	@GET
	@Path("/{id}/delete")
	@Produces(MediaType.APPLICATION_XHTML_XML)
	@RolesAllowed({"admin"})
	public Response deleteVendor(@PathParam("id") String id,
			@Context HttpServletRequest httpRequest) {
		try {
			deleteVendor(id);
			showFlashMessage("Vendor sucessfully deleted.", "success");
		} catch(Exception e) {
			showFlashMessage(e.getMessage(), "error");
		}		
		return Response.seeOther(URI.create("/vendors")).build();
	}
	
	private void deleteVendor(String id) throws Exception {
		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();
		try {
			Vendor vendor = pm.getObjectById(Vendor.class,
					KeyFactory.stringToKey(id));
			pm.deletePersistent(vendor);			
		} catch (Exception e) {
			throw new Exception("Error.");			
		} finally {
			pm.close();
		}		
	}

	@GET
	@Path("/{id}/update")
	@Produces(MediaType.APPLICATION_XHTML_XML)
	@RolesAllowed({"admin"})
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
	

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({"admin"})
	public Response updateVendorSaveRest(@PathParam("id") String id, VendorMapping vendor) {
		try {
			updateVendor(id, vendor.getName());
		} catch(Exception e) {
			return Response.serverError().build();			
		} 
		return Response.ok().build();
	}	
	
	@POST
	@Path("/{id}/update")
	@Produces(MediaType.APPLICATION_XHTML_XML)
	@RolesAllowed({"admin"})
	public Response updateVendorSave(@PathParam("id") String id,
			@FormParam("name") String vendorName,
			@Context HttpServletRequest httpRequest) {

		try {
			updateVendor(id, vendorName);
			showFlashMessage("Vendor sucessfully updated.", "success");
		} catch(Exception e) {
			showFlashMessage(e.getMessage(),  "error");			
		} 

		return Response.seeOther(URI.create("/vendors")).build();
	}

	private void updateVendor(String id, String vendorName) throws Exception {
		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();
		try {			
			Vendor vendor = null;
			vendor = pm.getObjectById(Vendor.class, KeyFactory.stringToKey(id));
			if (vendorName == null || vendorName.isEmpty()) {
				throw new Exception("Vendor name missing.");
			} else {
				vendor.setName(vendorName);
				pm.makePersistent(vendor);				
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());			
		} finally {
			pm.close();
		}		
	}
	
	
	@GET
	@Path("/new")
	@Produces(MediaType.APPLICATION_XHTML_XML)
	@RolesAllowed({"admin"})
	public Response newVendor() {
		Map<String, Object> map = new HashMap<String, Object>();

		return Response.ok(new Viewable("/views/vendor/new.jsp", map)).build();
	}

	@POST
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addVendorRest(@FormParam("name") String vendorName, VendorMapping vendor){

		try {
			addVendor(vendor.getName());			
		} catch(Exception e) {
			return Response.serverError().build();
		}
		return Response.ok().build();
	}	
	
	@POST
	@Path("/new")
	@Produces(MediaType.APPLICATION_XHTML_XML)
	public Response addVendor(@FormParam("name") String vendorName,
			@Context HttpServletRequest httpRequest) throws URISyntaxException {

		try {
			addVendor(vendorName);
			showFlashMessage("Vendor was successfully added.", "success");
		} catch(Exception e) {
			
		}
		return Response.seeOther(new URI("/vendors/")).build();
	}
	
	private void addVendor(String vendorName) {
		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();

		Vendor vendor = new Vendor();
		vendor.setName(vendorName);
		try {
			if (vendor.getName() != null && !vendor.getName().isEmpty()) {
				pm.makePersistent(vendor);				
			} else {
				showFlashMessage("Error.", "error");
			}
		} finally {
			pm.close();
		}		
	}
}
