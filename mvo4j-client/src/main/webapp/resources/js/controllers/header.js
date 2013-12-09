var postJson;
function LocationController($scope, $http, $location) {
	if ($location.$$absUrl.lastIndexOf('/contacts') > 0) {
		$scope.activeURL = 'contacts';
	} else {
		$scope.activeURL = 'home';
	}

	postJson = function postJson() {

		var param = {
			"dateIns" : new Date(),
			"acronym" : "T2",
			"id" : 2,
			"type" : "TESTE2"
		};

		var url = '/mvo4j-client/contactType.json';

		var config = {
			headers : {
				'Content-Type' : 'application/json; charset=UTF-8'
			}
		};

		$http.post(url, param, config)
	        .success(function (data) {
	            alert(data.type);
	        })
	        .error(function(data, status, headers, config) {
	            $scope.handleErrorInDialogs(status);
	        });

	}
}