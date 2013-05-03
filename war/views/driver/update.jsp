<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ page
	import="com.google.appengine.api.blobstore.BlobstoreServiceFactory"%>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService"%>

<t:page>
	<jsp:body>
       	<h2>Update driver</h2>
        <form class="form-general" method="POST">
            <input type="text" name="name" value="${it.driver.name}"
				placeholder="Driver name" />
            <br />
            Version:
            <br />
        	<input type="text" name="version" value="${it.driver.version}" />
        	<br />
        	Operating system:
        	<br />
        	<select name="osId">
            <c:forEach var="item" items="${it.systems}">
  					<option value="${item.key}">${item.name}</option>           		           		           			         
        	</c:forEach>
        	</select>        	
        	<br />
            <input class="btn btn-large btn-primary" type="submit"
				name="submit" value="Update" />
        </form>
    </jsp:body>
</t:page>