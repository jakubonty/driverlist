<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<!-- The HTML 4.01 Transitional DOCTYPE declaration-->
<!-- above set at the top of the file will set     -->
<!-- the browser's rendering engine into           -->
<!-- "Quirks Mode". Replacing this declaration     -->
<!-- with a "Standards Mode" doctype is supported, -->
<!-- but may lead to some differences in layout.   -->
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ page
	import="com.google.appengine.api.blobstore.BlobstoreServiceFactory"%>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService"%>

<%
	BlobstoreService blobstoreService = BlobstoreServiceFactory
			.getBlobstoreService();
	String uploadUrl = blobstoreService
			.createUploadUrl("/rest/user/driver/new");
	pageContext.setAttribute("uploadUrl", uploadUrl);
%>

<t:page>
	<jsp:body>
       	<h2>New driver</h2>
        <form class="form-general" action="${uploadUrl}" method="POST"
			enctype="multipart/form-data">
            <input type="text" name="name" value=""
				placeholder="Driver name" />
            <br />
            Version:
            <br />
        	<input type="text" name="version" value="1.0" />
        	<br />
        	Device:
        	<br />
        	<select name="deviceId">		
            <c:forEach var="item" items="${it.devices}">
  					<option value="${item.key}">${item.name}</option>           		           		           			         
        	</c:forEach>
        	</select>
        	<br />
        	Operating system:
        	<br />
        	<select name="osId">
            <c:forEach var="item" items="${it.systems}">
  					<option value="${item.key}">${item.name}</option>           		           		           			         
        	</c:forEach>
        	</select>
        	<br />
        	File:
        	<br />
        	<input type="file" name="driver" />        	
        	<br />
            <input class="btn btn-large btn-primary" type="submit"
				name="submit" value="Add driver" />
        </form>
    </jsp:body>
</t:page>