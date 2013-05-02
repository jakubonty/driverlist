package jm;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class AppServlet extends HttpServlet {
	
	protected String actionName;
	
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	UserService userService = UserServiceFactory.getUserService();
    	User user = userService.getCurrentUser();
        req.setAttribute("user", user);
        if(user == null) {
        	req.setAttribute("url", userService.createLoginURL("/"));
        }
        else {
        	req.setAttribute("url", userService.createLogoutURL("/"));
        }        
        Object o = req.getParameter("action");
        actionName = (o != null) ? o.toString() : "default";     
    }
    
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object o = req.getParameter("action");
        actionName = (o != null) ? o.toString() : "default";    
    }
}
