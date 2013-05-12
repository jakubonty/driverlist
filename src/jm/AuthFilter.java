package jm;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

public class AuthFilter implements ContainerRequestFilter {
	@Context
	HttpServletRequest request;

	@Override
	public ContainerRequest filter(ContainerRequest containerRequest) {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		request.setAttribute("user", user);
		if (user == null) {
			request.setAttribute("url", userService.createLoginURL("/"));
		} else {
			request.setAttribute("url", userService.createLogoutURL("/"));
			request.setAttribute("admin", userService.isUserAdmin());
		}		
		containerRequest.setSecurityContext(new Authorizer(user, userService));
		/*
		HttpSession session = request.getSession(true);
		Response response = null;
		response = Response.seeOther(URI.create("/")).build();
		if ((containerRequest.getPath().startsWith("user/") || containerRequest.getPath().startsWith("admin/"))&& user == null) {
			session.setAttribute("flashMessage", new FlashMessage(
					"Please, log in first.", "error"));
			throw new WebApplicationException(response);
		} else if (containerRequest.getPath().startsWith("admin/") && !userService.isUserAdmin()) {
			session.setAttribute("flashMessage", new FlashMessage(
					"You need admin permission to do this action.", "error"));
			throw new WebApplicationException(response);			
		}*/
		return containerRequest;
	}
}