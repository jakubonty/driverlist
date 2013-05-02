package jm;

import java.util.Map;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

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
    
    public void checkUserSigned() {
    	UserService userService = UserServiceFactory.getUserService();
    	User user = userService.getCurrentUser();
    	if (user == null) {
    		
    	}
    }
}
