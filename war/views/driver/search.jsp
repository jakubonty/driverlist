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
        <form method="POST">
        	Vendor:
        	<select id="vendorId" name="vendorId">		
            <c:forEach var="item" items="${it.vendors}">
  					<option value="${item.key}">${item.name}</option>           		           		           			         
        	</c:forEach>
        	</select>
            Type:
			<jsp:include page="/views/shared/types.jsp" />      
            <input type="submit" name="submit" value="Odeslat" />
        </form>
        <div id="devices">
    	</div>
            <script>
        $(document).ready(function() {
        	var updateDevices = function() {
        		var vendorId = $('#vendorId').val();
        		var type = $('#type').val();
        		$.getJSON("search?vendorId="+vendorId+"&type="+type,
                    function(data) {
    			$('#devices').empty();
                for(dat in data.devices) {                            	
                    $('#devices').append('<a href="device/'+data.devices[dat].id+'">'+data.devices[dat].name+'</a>');
                }                
            	});
        		};
        	$('#vendorId').change(updateDevices);
        	$('#type').change(updateDevices);
        });
    </script>
    </jsp:body>
</t:page>