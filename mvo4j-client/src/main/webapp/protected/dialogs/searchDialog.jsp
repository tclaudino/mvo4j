<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<div id="searchModal" class="modal hide fade in centering"
	role="dialog" aria-labelledby="searchModalLabel"
	aria-hidden="true">
	<div class="modal-header">
		<h3 id="searchModalLabel" class="displayInLine">
			Search
		</h3>
	</div>
	<div class="modal-body">
		<form name="searchForm" novalidate>
			<label>Search for</label>

			<div>
				<div class="input-append">
					<input type="text" autofocus required ng-model="searchFor"
						name="searchFor"
						placeholder="<spring:message code='contact'/>&nbsp;Nome" />
				</div>
				<div class="input-append displayInLine">
					<label class="displayInLine"> <span
						class="alert alert-error"
						ng-show="displayValidationError && searchContactForm.searchFor.$error.required">
							<spring:message code="required" />
					</span>
					</label>
				</div>
			</div>
			<input type="submit" class="btn btn-inverse"
				ng-click="searchContact(searchContactForm, false);"
				value='<spring:message code="search"/>' />
			<button class="btn btn-inverse" data-dismiss="modal"
				ng-click="exit('#searchModal');" aria-hidden="true">
				<spring:message code="cancel" />
			</button>
		</form>
	</div>
	<span class="alert alert-error dialogErrorMessage"
		ng-show="errorOnSubmit"> <spring:message code="request.error" />
	</span>
</div>