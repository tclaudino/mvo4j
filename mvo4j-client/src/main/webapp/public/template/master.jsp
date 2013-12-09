<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!doctype html>
<html lang="pt-BR" id="ng-app" ng-app="">
<head>
<%-- 	    <title><spring:message code="project.title" /></title> --%>
<title>MVO - Case Test</title>
<link href="<c:url value='/resources/css/bootstrap.min.css'  />"
	rel="stylesheet" />
<link
	href="<c:url value='/resources/css/bootstrap-responsive.min.css'  />"
	rel="stylesheet" />
<link href="<c:url value='/resources/css/style.css'  />"
	rel="stylesheet" />
<script src="<c:url value='/resources/js/jquery-1.9.1.min.js' />"></script>
<script src="<c:url value='/resources/js/angular.min.js' />"></script>
</head>
<body>
	<div class="container">

		<div class="header">
			<tiles:insertAttribute name="header" />
		</div>

		<div class="content">
			<div id="loadingModal" class="modal hide fade in centering"
				role="dialog" aria-labelledby="deleteContactsModalLabel"
				aria-hidden="true">
				<div id="divLoadingIcon" class="text-center">
					<div class="icon-align-center loading"></div>
				</div>
			</div>
			<tiles:insertAttribute name="body" />
		</div>

		<div class="footer">
			<tiles:insertAttribute name="footer" />
		</div>

		<!--[if IE]>
	            <script src="<c:url value='/resources/js/bootstrap.min.ie.js' />"></script>
	        <![endif]-->
		<!--[if !IE]><!-->
		<script src="<c:url value='/resources/js/bootstrap.min.js' />"></script>
		<!--<![endif]-->
	</div>
</body>
</html>