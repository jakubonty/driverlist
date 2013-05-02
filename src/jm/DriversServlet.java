package jm;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class DriversServlet extends AppServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	super.doGet(req, resp);
    	RequestDispatcher rd = req.getRequestDispatcher("/driver.jsp");       
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        
        PersistenceManager pm = jm.db.PMF.get().getPersistenceManager();          
        
        if (actionName.equals("add")) {                  
            
            try {
            	/*
            	if(content != null && !content.isEmpty()) {
            		pm.makePersistent(message);
            	}*/
            }
            finally {
                pm.close();
            }        	
        	
        } else
        if (actionName.equals("delete")) {
        	        	
        } else
        if (actionName.equals("update")) {
        	        	
        }
        
        
        rd.forward(req, resp);
        /*
        Query query = new Query("Message").addSort("date", Query.SortDirection.DESCENDING);
        List<Entity> result = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
        
        List<Message> messages = new LinkedList<Message>();
        for(Entity e : result) {
        	Message m = new Message();
        	m.setUser((User) e.getProperty("user"));
        	m.setDate((Date) e.getProperty("date"));
        	m.setMessage((String) e.getProperty("message"));
        	messages.add(m);
        }
        */
    }
}
