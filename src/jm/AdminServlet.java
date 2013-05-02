package jm;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import jm.db.Device;
import jm.db.OperatingSystem;
import jm.db.Vendor;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class AdminServlet extends AppServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.doGet(req, resp);
		RequestDispatcher rd = req.getRequestDispatcher("/admin/default.jsp");
		;
		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();

		if (actionName.equals("addDriver")) {
			rd = req.getRequestDispatcher("/views/driver/new.jsp");
		} else if (actionName.equals("addVendor")) {
			rd = req.getRequestDispatcher("/views/vendor/new.jsp");
		} else if (actionName.equals("addOperationSystem")) {
			rd = req.getRequestDispatcher("/views/OS/new.jsp");
		} else if (actionName.equals("addDevice")) {
			rd = req.getRequestDispatcher("/views/device/new.jsp");
		} else if (actionName.equals("listVendors")) {
			rd = req.getRequestDispatcher("/views/vendor/list.jsp");
			/*
			 * DatastoreService datastore =
			 * DatastoreServiceFactory.getDatastoreService(); Query query = new
			 * Query("Vendor"); List<Entity> result =
			 * datastore.prepare(query).asList
			 * (FetchOptions.Builder.withDefaults()); List<Vendor> vendors = new
			 * LinkedList<Vendor>(); for(Entity entity : result) {
			 * vendors.add(new Vendor(entity)); }
			 */
			req.setAttribute("vendors", Vendor.getAll(pm));

		}
		rd.forward(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.doPost(req, resp);
		RequestDispatcher rd = req.getRequestDispatcher("/driver.jsp");
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();

		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();

		if (actionName.equals("addVendor")) {


		} else if (actionName.equals("addOperatingSystem")) {
			String operatingSystem = req.getParameter("operatingSystem")
					.toString();
			try {
				if (operatingSystem != null && !operatingSystem.isEmpty()) {
					OperatingSystem os = new OperatingSystem();
					os.setName(operatingSystem);
					pm.makePersistent(os);
				}
			} finally {
				pm.close();
			}

		} else if (actionName.equals("addDevice")) {
			String vendorId = req.getParameter("vendorId").toString();
			String type = req.getParameter("type").toString();
			String name = req.getParameter("name").toString();
			try {
				if (vendorId != null && !vendorId.isEmpty()) {
					Vendor vendor = pm.getObjectById(Vendor.class, vendorId);

					Device device = new Device();
					device.setName(name);
					device.setType(type);
					device.setVendor(vendor);

					pm.makePersistent(device);
				}
			} finally {
				pm.close();
			}
		} else if (actionName.equals("update")) {

		}

		resp.sendRedirect("/admin/");
		// rd.forward(req, resp);
		/*
		 * Query query = new Query("Message").addSort("date",
		 * Query.SortDirection.DESCENDING); List<Entity> result =
		 * datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
		 * 
		 * List<Message> messages = new LinkedList<Message>(); for(Entity e :
		 * result) { Message m = new Message(); m.setUser((User)
		 * e.getProperty("user")); m.setDate((Date) e.getProperty("date"));
		 * m.setMessage((String) e.getProperty("message")); messages.add(m); }
		 */
	}
}
