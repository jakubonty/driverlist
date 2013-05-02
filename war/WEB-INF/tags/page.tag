<%@tag description="Overall Page template" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="header" fragment="true" %>
<%@attribute name="footer" fragment="true" %>
<html>
  <head>
  	<script src="//ajax.googleapis.com/ajax/libs/jquery/2.0.0/jquery.min.js"></script>
  </head>
  <body>
    <div id="pageheader">
      	Header
     <c:choose>
        <c:when test="${it.user != null}">
        	Jste přihlášen jako ${it.user.nickname}. <a href="${it.url}">Odhlásit se.</a>
        </c:when>
        <c:otherwise>
        	Nejste přihlášen. <a href="${it.url}">Přihlásit se.</a>
        </c:otherwise>
	  </c:choose>
      <jsp:invoke fragment="header"/>
        <c:if test="${it.admin}"> 
        	<jsp:include page="/views/menu/admin_menu.jsp" />
        </c:if>            
    </div>
    <div id="body">
      <jsp:doBody/>
    </div>
    <div id="pagefooter">
    	Footer
      <jsp:invoke fragment="footer"/>
    </div>
  </body>
</html>