<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:page>
    <jsp:body>
    	<h2>New operating system</h2>
        <form class="form-general" method="POST">
        	<input type="text" name="name" placeholder="System name" />
            <div><input type="submit" name="submit" value="Send"></div>
        </form>
    </jsp:body>
</t:page>