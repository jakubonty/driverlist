package jm;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import jm.db.Device;
import jm.db.Driver;
import jm.db.OperatingSystem;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class UserServlet extends AppServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.doPost(req, resp);
		RequestDispatcher rd = req.getRequestDispatcher("/driver.jsp");
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();

		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();

		if (actionName.equals("addDriver")) {
			String vendorId = req.getParameter("vendorId").toString();
			String type = req.getParameter("type").toString();
			String name = req.getParameter("name").toString();
			try {
				if (vendorId != null && !vendorId.isEmpty()) {
					//Vendor vendor = pm.getObjectById(Vendor.class, vendorId);
					//author, device, version, downloadURL, operatingSystem
					
					Driver driver = new Driver();

					//pm.makePersistent(device);
				}
			} finally {
				pm.close();
			}
		}

		resp.sendRedirect("/user/");
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.doGet(req, resp);
		RequestDispatcher rd = req.getRequestDispatcher("/driver.jsp");
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();

		PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();

		String action = req.getAttribute("action").toString();

		if (action.equals("add")) {

			try {
				/*
				 * if(content != null && !content.isEmpty()) {
				 * pm.makePersistent(message); }
				 */
			} finally {
				pm.close();
			}

		} else if (action.equals("delete")) {

		} else if (action.equals("update")) {

		}

		rd.forward(req, resp);
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
