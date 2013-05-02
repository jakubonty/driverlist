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
    	<h1>${it.device.name}</h1>
    	<p>
    		${it.device.description}
    	</p>
        Operating system:
        <select name="os" id="os">
        <c:forEach var="item" items="${it.systems}">
  			<option value="${item.name}">${item.name}</option>           		           		           			         
        </c:forEach>
        </select>    	
      	<div class="row-fluid">
        	<div id="drivers" class="span6">
    		</div>
    	</div>
     <script>
        $(document).ready(function() {
        	os = { "drivers": [
                <c:forEach var="item" items="${it.device.drivers}">
    				{"name": "${item.name}", "os": "${item.operatingSystem}", 
    				"data": "${item.data.keyString}", "version": "${item.version}"},           		           		           			         
    			</c:forEach>
    				]
        	};
        	var updateDrivers = function() {
        		var selectedOs = $('#os').val();
				$('#drivers').empty();				
            	for(dat in os.drivers) {
            		if (os.drivers[dat].os == selectedOs) {
                		$('#drivers').append('<h4>'+os.drivers[dat].name+'</h4><p>Version: '+os.drivers[dat].version+'<br/><a href="/rest/front/driver/'+os.drivers[dat].data+'">Download .. </a></p>');
            		}
            	}    		        	
        	};		     		
        	$('#os').change(updateDrivers);
        	updateDrivers.call(this);
        });
    </script>
    </jsp:body>
</t:page>