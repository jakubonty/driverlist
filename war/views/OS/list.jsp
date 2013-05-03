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

<t:page>
	<jsp:body>
    	<h2>Operating systems</h2>
          	<div class="row-fluid">
        	<div class="span6">
        	<c:forEach var="item" items="${it.systems}">
           		<h4>${item.name}</h4>
           		
           			<a href="/dl/admin/operating-systems/${item.key}/delete">Delete</a> | 
           			<a href="/dl/admin/operating-systems/${item.key}/update">Update</a>
        	</c:forEach>        	
    		</div>
    		<a href="/dl/admin/operating-systems/new" class="btn btn-large btn-primary">Add operating system</a>
    		</div>    		
    </jsp:body>
</t:page>