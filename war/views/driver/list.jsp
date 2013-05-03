<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>

<t:page>
	<jsp:body>
    	<h2>Drivers</h2>
          	<div class="row-fluid">
        	<div class="span6">
        	<c:forEach var="item" items="${it.drivers}">
           		<h4>${item.name}</h4>
           		
           			<a href="/dl/admin/drivers/${item.key}/delete">Delete</a> | 
           			<a href="/dl/admin/drivers/${item.key}/update">Update</a>
        	</c:forEach>        	
    		</div>
    		<a href="/dl/user/drivers/new" class="btn btn-large btn-primary">Add driver</a>
    	</div>
    </jsp:body>
</t:page>