angular.module('ionicApp.controllers')

.controller('HomeTabCtrl', function($scope, $state, UserInfo) {
	$scope.$watch('$viewContentLoaded', function(event) {
		if(!UserInfo.isUserLogin()) {
			$state.go('login');
		}
	})
});