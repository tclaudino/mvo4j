<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="row-fluid" ng-controller="contactTypeController">

	<h2>
		<p class="text-center">
			Contatos <a href="#searchModal" id="contactsHeaderButton"
				role="button" ng-class="" title="Search&nbsp;Contact"
				class="btn btn-inverse" data-toggle="modal"> <i
				class="icon-search"></i>
			</a>
		</p>
	</h2>
	<h4>
		<div ng-class="{'': state == 'list', 'none': state != 'list'}">
			<p class="text-center">Not found :&nbsp;{{page.totalContacts}}</p>
		</div>
	</h4>

	<div
		ng-class="{'alert badge-inverse': displaySearchMessage == true, 'none': displaySearchMessage == false}">
		<h4>
			<p class="messageToUser">
				<i class="icon-info-sign"></i>&nbsp;{{page.searchMessage}}
			</p>
		</h4>
		<a href="#" role="button" ng-click="resetSearch();"
			ng-class="{'': displaySearchMessage == true, 'none': displaySearchMessage == false}"
			title="reset" class="btn btn-inverse" data-toggle="modal"> <i
			class="icon-remove"></i> reset
		</a>
	</div>

	<div
		ng-class="{'alert badge-inverse': displayMessageToUser == true, 'none': displayMessageToUser == false}">
		<h4 class="displayInLine">
			<p class="messageToUser displayInLine">
				<i class="icon-info-sign"></i>&nbsp;{{page.actionMessage}}
			</p>
		</h4>
	</div>

	<div id="gridContainer">
		<table class="table table-bordered table-striped">
			<thead>
				<tr>
					<th scope="col">Nome</th>
					<th scope="col">Email</th>
					<th scope="col">Phone</th>
					<th scope="col"></th>
				</tr>
			</thead>
			<tbody>
				<tr ng-repeat="contact in page.source">
					<td class="tdContactsCentered">{{contact.name}}</td>
					<td class="tdContactsCentered">{{contact.email}}</td>
					<td class="tdContactsCentered">{{contact.phoneNumber}}</td>
					<td class="width15">
						<div class="text-center">
							<input type="hidden" value="{{contact.id}}" /> <a
								href="#updateContactsModal" ng-click="selectedContact(contact);"
								role="button" title="Update&nbsp;Contact"
								class="btn btn-inverse" data-toggle="modal"> <i
								class="icon-pencil"></i>
							</a> <a href="#deleteContactsModal"
								ng-click="selectedContact(contact);" role="button"
								title="Delete&nbsp;Contact" class="btn btn-inverse"
								data-toggle="modal"> <i class="icon-minus"></i>
							</a>
						</div>
					</td>
				</tr>
			</tbody>
		</table>

		<jsp:include page="../components/pagination.jsp" />

	</div>
	<div ng-class="text-center">
		<br /> <a href="#addContactsModal" role="button"
			ng-click="resetContact();" title="Create&nbsp;Contact"
			class="btn btn-inverse" data-toggle="modal"> <i class="icon-plus"></i>
			&nbsp;&nbsp;Create&nbsp;Contact
		</a>
	</div>

	<jsp:include page="../dialogs/searchDialog.jsp" />

</div>
<script src="<c:url value='/resources/js/controllers/contactType.js' />"></script>
