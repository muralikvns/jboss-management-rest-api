var app= angular.module('JbossDashBoard', []);
app.controller('JbossDashboardCtrl', function($scope, $http,$interval) {
    $http.get('http://localhost:8088/jboss/api/jboss-server-details').
        then(function(response) {
            console.log("jboss-all-server-details");
            
            $scope.jbossJsonResponse = response.data;
			$scope.jbossJsonResponseResult = $scope.jbossJsonResponse.result;
			$scope.now = new Date();
			console.log($scope.jbossJsonResponseResult);
            
        });
   
   
});
