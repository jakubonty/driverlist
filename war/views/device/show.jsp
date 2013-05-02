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
    <jsp:attribute name="header">
      <h1>Welcome</h1>
    </jsp:attribute>
    <jsp:attribute name="footer">
      <p id="copyright">Copyright 1927, Future Bits When There Be Bits Inc.</p>
    </jsp:attribute>
    <jsp:body>
    	<h1>${it.device.name}</h1>
        Operating system:
        <select name="os" id="os">
        <c:forEach var="item" items="${it.systems}">
  			<option value="${item.name}">${item.name}</option>           		           		           			         
        </c:forEach>
        </select>    	
    	<div id="drivers">

        </div>
     <script>
        $(document).ready(function() {
        	os = { "drivers": [
                <c:forEach var="item" items="${it.device.drivers}">
    				{"name": "${item.name}", "os": "${item.operatingSystem}"},           		           		           			         
    			</c:forEach>
    				]
        	};
        	var updateDrivers = function() {
        		var selectedOs = $('#os').val();
				$('#drivers').empty();
            	for(dat in os.drivers) {
            		if (os.drivers[dat].os == selectedOs) {
                		$('#drivers').append('<a href="device/'+os.drivers[dat].name+'">'+os.drivers[dat].name+'</a>');
            		}
            	}    		        	
        	};		     		
        	$('#os').change(updateDrivers);
        	updateDrivers.call(this);
        });
    </script>
    </jsp:body>
</t:page>