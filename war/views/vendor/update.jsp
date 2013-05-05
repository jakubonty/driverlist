<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:page>
    <jsp:body>
    	<h2>Update vendor</h2>
        <form class="form-general" method="POST">
        	<input type="text" name="name" placeholder="Vendor name" value="${it.vendor.name}" />
            <div><input class="btn btn-large btn-primary" type="submit"
				name="submit" value="Update" /></div>
        </form>
    </jsp:body>
</t:page>