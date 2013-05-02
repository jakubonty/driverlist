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
    	<h2>Find device</h2>
        <form class="form-general" method="POST">
        	Vendor:
        	<br/>
        	<select id="vendorId" name="vendorId">		
            <c:forEach var="item" items="${it.vendors}">
  					<option value="${item.key}">${item.name}</option>           		           		           			         
        	</c:forEach>
        	</select>
        	<br/>
            Type:
            <br/>
			<jsp:include page="/views/shared/types.jsp" />		
        </form>
        <h3>Found devices:</h3>        
      	<div class="row-fluid">
        	<div id="devices" class="span6">
    		</div>
    	</div>
            <script>
        $(document).ready(function() {
        	var updateDevices = function() {
        		var vendorId = $('#vendorId').val();
        		var type = $('#type').val();
        		$.getJSON("/rest/front/search?vendorId="+vendorId+"&type="+type,
                    function(data) {
    			$('#devices').empty();
                for(dat in data.devices) {                            	
                    $('#devices').append('<a href="/rest/front/device/'+data.devices[dat].id+'"><h4>'+data.devices[dat].name+'</h4></a><p>'+data.devices[dat].description+'</p>');
                }                
            	});
        		};
        	$('#vendorId').change(updateDevices);
        	$('#type').change(updateDevices);
        	updateDevices.call(this);
        });
    </script>
    </jsp:body>
</t:page>