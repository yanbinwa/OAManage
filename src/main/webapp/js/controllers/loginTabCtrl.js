angular.module('ionicApp.controllers')

.controller('LoginTabCtrl', function($scope, $state, DateUtil) {
    $scope.user = {};
    $scope.user.auth = "普通用户";
    $scope.login = function() {
        alert("name is " + $scope.user.username + ", password is: " + $scope.user.password + ", role is: " + $scope.user.auth);
        $state.go('app.main.home');
    }
    $scope.sign = function() {
        $state.go('sign');
    }
});