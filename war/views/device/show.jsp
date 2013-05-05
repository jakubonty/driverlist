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
    				"data": "${item.data.keyString}", "version": "${item.version}",
    				"author": "${item.authorEmail}"},           		           		           			         
    			</c:forEach>
    				]
        	};
        	var updateDrivers = function() {
        		var selectedOs = $('#os').val();
				$('#drivers').empty();				
            	for(dat in os.drivers) {
            		if (os.drivers[dat].os == selectedOs) {
                		$('#drivers').append('<h4>'+os.drivers[dat].name+'</h4><p>Author: '+os.drivers[dat].author+'<br />Version: '+os.drivers[dat].version+'<br/><a href="/dl/driver/'+os.drivers[dat].data+'">Download .. </a></p>');
            		}
            	}    		        	
        	};		     		
        	$('#os').change(updateDrivers);
        	updateDrivers.call(this);
        });
    </script>
    </jsp:body>
</t:page>