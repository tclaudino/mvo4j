<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div class="masthead">
	<h3 class="muted">Mvo4j Header</h3>

	<div class="navbar">
		<div class="navbar-inner">
			<div class="container">
				<ul class="nav" ng-controller="LocationController">
					<li
						ng-class="{'active': activeURL == 'home', '': activeURL != 'home'}">
						<a href="<c:url value="/home"/>" title="Home">
							<p>Home</p>
					</a>
					</li>
					<li
						ng-class="{'gray': activeURL == 'contactType', '': activeURL != 'contactType'}"><a
						title='contactType' href="<c:url value='/contactType/list'/>"><p>
								ContactType</p></a></li>
				</ul>
				<ul class="nav pull-right">
					<li><a href="<c:url value='/logout' />" title='Logout'><p
								class="displayInLine">Logout &nbsp;(${user.name})</p></a></li>
				</ul>
			</div>
		</div>
	</div>
</div>
