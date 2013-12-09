<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<div class="text-center">
	<button href="#" class="btn btn-inverse"
		ng-class="{'btn-inverse': page.currentPage != 0, 'disabled': page.currentPage == 0}"
		ng-disabled="page.currentPage == 0" ng-click="changePage(0)"
		title='first'>first</button>
	<button href="#" class="btn btn-inverse"
		ng-class="{'btn-inverse': page.currentPage != 0, 'disabled': page.currentPage == 0}"
		ng-disabled="page.currentPage == 0" class="btn btn-inverse"
		ng-click="changePage(page.currentPage - 1)" title='back'>&lt;</button>
	<span>{{page.currentPage + 1}} de {{page.pagesCount}} </span>
	<button href="#" class="btn btn-inverse"
		ng-class="{'btn-inverse': page.pagesCount - 1 != page.currentPage, 'disabled': page.pagesCount - 1 == page.currentPage}"
		ng-click="changePage(page.currentPage + 1)"
		ng-disabled="page.pagesCount - 1 == page.currentPage" title='next'>&gt;</button>
	<button href="#" class="btn btn-inverse"
		ng-class="{'btn-inverse': page.pagesCount - 1 != page.currentPage, 'disabled': page.pagesCount - 1 == page.currentPage}"
		ng-disabled="page.pagesCount - 1 == page.currentPage"
		ng-click="changePage(page.pagesCount - 1)" title='last'>last</button>
</div>