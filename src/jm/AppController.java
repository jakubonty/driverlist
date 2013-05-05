package jm;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;

import jm.common.FlashMessage;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class AppController {
	@Context protected HttpServletRequest httpRequest;
	
    public void beforeRender(Map<String, Object> map) {
    	UserService userService = UserServiceFactory.getUserService();
    	User user = userService.getCurrentUser();
        map.put("user", user);
        if(user == null) {
        	map.put("url", userService.createLoginURL("/"));
        }
        else {
        	map.put("url", userService.createLogoutURL("/"));
        	map.put("admin", userService.isUserAdmin());
        }    	
    }
    
    protected void showFlashMessage(String message, String type) {
    	HttpSession session = httpRequest.getSession(true);
    	session.setAttribute("flashMessage", new FlashMessage(message,
				type));    	
    }
    
    public void checkUserSigned() {
    	UserService userService = UserServiceFactory.getUserService();
    	User user = userService.getCurrentUser();
    	if (user == null) {
    		
    	}
    }
}
