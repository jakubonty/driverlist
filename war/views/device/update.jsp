<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:page>
    <jsp:body>
    	<h2>Update device</h2>
        <form class="form-general" method="POST">
        	Device name:
        	<br/>
            <input type="text" name="name" value="${it.device.name}" placeholder="Device name"/>
        	<br/>
        	Device type:
        	<br/>
        	<jsp:include page="/views/shared/types.jsp" />
        	<br/>
        	Description:
        	<br/>
        	<textarea name="description">${it.device.description}</textarea>
        	<br/>
            <input type="submit" name="submit" value="Send" />
        </form>
    </jsp:body>
</t:page>