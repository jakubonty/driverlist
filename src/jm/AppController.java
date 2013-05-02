package jm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.sun.jersey.api.view.Viewable;

public class AppController {
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
}
