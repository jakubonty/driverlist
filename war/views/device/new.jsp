<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:page>
    <jsp:body>
    	<h2>New device</h2>
        <form class="form-general" method="POST">
        	Device name:
        	<br/>
            <input type="text" name="name" value="" placeholder="Device name"/>
            <br/>
        	Vendor:
        	<br/>
        	<select name="vendorId">		
            <c:forEach var="item" items="${it.vendors}">
  					<option value="${item.key}">${item.name}</option>           		           		           			         
        	</c:forEach>
        	</select>
        	<br/>
        	Device type:
        	<br/>
        	<jsp:include page="/views/shared/types.jsp" />
        	<br/>
        	Description:
        	<br/>
        	<textarea name="description"></textarea>
        	<br/>
            <input class="btn btn-large btn-primary" type="submit"
				name="submit" value="Create" />
        </form>
    </jsp:body>
</t:page>