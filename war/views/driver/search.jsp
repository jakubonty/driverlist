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
        		$.getJSON("/dl/front/search?vendorId="+vendorId+"&type="+type,
                    function(data) {
    			$('#devices').empty();
                for(dat in data.devices) {                            	
                    $('#devices').append('<a href="/dl/front/device/'+data.devices[dat].id+'"><h4>'+data.devices[dat].name+'</h4></a><p>'+data.devices[dat].description+'</p>');
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