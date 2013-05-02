<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<!-- The HTML 4.01 Transitional DOCTYPE declaration-->
<!-- above set at the top of the file will set     -->
<!-- the browser's rendering engine into           -->
<!-- "Quirks Mode". Replacing this declaration     -->
<!-- with a "Standards Mode" doctype is supported, -->
<!-- but may lead to some differences in layout.   -->
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:page>
    <jsp:body>
    	<h2>New vendor</h2>
        <form class="form-general" method="POST">
        	<input type="text" name="name" placeholder="Vendor name" />
            <input type="hidden" name="action" value="addVendor">
            <div><input type="submit" name="submit" value="Odeslat"></div>
        </form>
    </jsp:body>
</t:page>