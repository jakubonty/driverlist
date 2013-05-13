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
    @GET
    @Produces(MediaType.TEXT_HTML)
	public Response addDevice() {
		Map<String, Object> map = new HashMap<String, Object>();

		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();
		map.put("vendors", Vendor.getAll(pm));				
		
		beforeRender(map);
		return Response.ok(new Viewable("/views/driver/search.jsp", map)).build();
	}			
}
