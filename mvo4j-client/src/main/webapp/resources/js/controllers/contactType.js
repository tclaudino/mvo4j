function contactTypeController($scope, $http) {

    $scope.getContactList = function () {

    	var url = "";

        $scope.startDialogAjaxRequest();

        var config = {params: {page: $scope.pageToGet}};

        $http.get(url, config)
            .success(function (data) {
                $scope.finishAjaxCallOnSuccess(data, null, false);
            })
            .error(function () {
                $scope.displayCreateContactButton = false;
            });
    }

    $scope.startDialogAjaxRequest = function () {
        $scope.displayValidationError = false;
        $("#loadingModal").modal('show');
    }

    $scope.finishAjaxCallOnSuccess = function (data) {
        $scope.populateTable(data);
        $("#loadingModal").modal('hide');
    }

    $scope.populateTable = function (data) {
        if (data.pagesCount > 0) {

            $scope.page = {source: data.contacts, currentPage: $scope.pageToGet, pagesCount: data.pagesCount, totalContacts : data.totalContacts};

        }
        $scope.displayCreateContactButton = true;
        $scope.displayMessageToUser = false;
        $scope.displaySearchButton = true;
        $scope.page.actionMessage = data.actionMessage;
    }



}