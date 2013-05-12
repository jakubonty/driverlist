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

import jm.db.OperatingSystem;
import jm.mapping.OperatingSystemList;
import jm.mapping.OperatingSystemMapping;

import com.google.appengine.api.datastore.KeyFactory;
import com.sun.jersey.api.view.Viewable;

@Path("/operating-systems/")
public class OSResource extends AppController {

	@GET
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)	
	public Response listSystemsJSON() {
		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();
		return Response.ok(new OperatingSystemList(OperatingSystem.getAll(pm))).build();
	}

	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_XHTML_XML)
	@RolesAllowed({"admin"})
	public Response listSystems() {
		Map<String, Object> map = new HashMap<String, Object>();

		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();
		map.put("systems", OperatingSystem.getAll(pm));

		return Response.ok(new Viewable("/views/OS/list.jsp", map)).build();
	}	

	@DELETE
	@Path("/{id}/delete")
	@Produces(MediaType.APPLICATION_XHTML_XML)
	@RolesAllowed({"admin"})
	public Response deleteOperatingSystemRest(@PathParam("id") String id) {
		try {
			deleteOperatingSystem(id);
		} catch(Exception e) {
			Response.serverError().build();			
		}
		return Response.ok().build();
	}	
	
	@GET
	@Path("/{id}/delete")
	@Produces(MediaType.APPLICATION_XHTML_XML)
	@RolesAllowed({"admin"})
	public Response deleteOperatingSystem(@PathParam("id") String id,
			@Context HttpServletRequest httpRequest) {
		try {
			deleteOperatingSystem(id);
			showFlashMessage("Operating system sucessfully deleted.", "success");
		} catch(Exception e) {
			showFlashMessage(e.getMessage(), "error");			
		}
		return Response.seeOther(URI.create("/operating-systems"))
				.build();
	}
	
	private void deleteOperatingSystem(String id) throws Exception {
		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();
		try {
			OperatingSystem os = pm.getObjectById(OperatingSystem.class,
					KeyFactory.stringToKey(id));
			pm.deletePersistent(os);			
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

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({"admin"})
	public Response updateOperatingSystemSave(@PathParam("id") String id, OperatingSystemMapping os) {		
		try {
			updateOperatingSystem(id, os.getName());
		} catch(Exception e) {
			Response.serverError().build();
		}
		return Response.seeOther(URI.create("/operating-systems"))
				.build();
	}	
	
	@POST
	@Path("/{id}/update")
	@Produces(MediaType.APPLICATION_XHTML_XML)
	@RolesAllowed({"admin"})
	public Response updateOperatingSystemSave(@PathParam("id") String id,
			@FormParam("name") String osName,
			@Context HttpServletRequest httpRequest) {
		
		try {
			updateOperatingSystem(id, osName);
			showFlashMessage("Operating system sucessfully updated.", "success");
		} catch(Exception e) {
			showFlashMessage(e.getMessage(), "error");
		}

		return Response.seeOther(URI.create("/operating-systems"))
				.build();
	}
	
	private void updateOperatingSystem(String id, String osName) throws Exception {
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
		} else {
			throw new Exception("Error.");
		}		
	}

	@GET
	@Path("/new")
	@Produces(MediaType.APPLICATION_XHTML_XML)
	@RolesAllowed({"admin"})
	public Response newOperatingSystem() {
		Map<String, Object> map = new HashMap<String, Object>();

		return Response.ok(new Viewable("/views/OS/new.jsp", map)).build();
	}

	@PUT
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({"admin"})
	public Response addOperatingSystem(OperatingSystemMapping os) throws URISyntaxException {
		try {
			addOS(os.getName());
		} catch(Exception e) {
			return Response.serverError().build();
		}
		
		return Response.ok().build();
	}	
	
	@POST
	@Path("/new")
	@Produces(MediaType.APPLICATION_XHTML_XML)
	@RolesAllowed({"admin"})
	public Response addOperatingSystem(@FormParam("name") String osName,
			@Context HttpServletRequest httpRequest) throws URISyntaxException {

		try {
			addOS(osName);
			showFlashMessage("Operating system was successfully added.", "success");
		} catch(Exception e) {
			showFlashMessage(e.getMessage(), "error");
		}
		
		return Response.seeOther(new URI("/operating-systems/")).build();
	}
	
	private void addOS(String osName) throws Exception {
		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();

		OperatingSystem os = new OperatingSystem();
		os.setName(osName);
		try {
			if (os.getName() != null && !os.getName().isEmpty()) {
				pm.makePersistent(os);				
			} else {
				throw new Exception("Missing name.");
			}
		} finally {
			pm.close();
		}		
	}
	
}
