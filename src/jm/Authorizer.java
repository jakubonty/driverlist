package jm;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;

public class Authorizer implements SecurityContext {

	private String role;
	private Principal principal;
	
	public Authorizer(final User user, UserService userService) {
		if (user == null) {
			role = "";
		} else {
            if (user != null) {
                principal = new Principal() {
                    public String getName() {
                        return user.getEmail();
                    }
                };		
            }
    		if (userService.isUserAdmin())
    			role = "admin";
    		else
    			role = "user";
		}
	}
	
	@Override
	public String getAuthenticationScheme() {
		return SecurityContext.FORM_AUTH;
	}

	@Override
	public Principal getUserPrincipal() {
		return principal;
	}

	@Override
	public boolean isSecure() {
		return true;
	}

	@Override
	public boolean isUserInRole(String role) {
		return role.equals(this.role);
	}

}
