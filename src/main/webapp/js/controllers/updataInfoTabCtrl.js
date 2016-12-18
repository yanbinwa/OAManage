angular.module('ionicApp.controllers')

.controller('UpdataInfoTabCtrl', function($scope, $rootScope, $state, StoreInfo, UserInfo, URL) {
	
    $scope.$watch('$viewContentLoaded', function(event) {
    	openTabResponse();
	})
	
	$scope.$on("GeneralEvent", function(event, msg) {
		var functionKey = msg.functionKey;
		if (functionKey == 'onSessionConnected') {
			onSessionConnectedResponse(msg);
		}
	});
    
	var onSessionConnectedResponse = function(msg) {
		var stateAuth = msg.stateAuth;
		if (!stateAuth) {
			$state.go(URL.getLoginStateName());
		}
	}
	
	$scope.storeInfo = StoreInfo.getStoreInfo();
	$scope.isDisableEditTag = true;
	$scope.changeEditButtonName = "进入编辑";
	
    $scope.$watch('$viewContentLoaded', function(event) {
		
	})
	$scope.$on("UpdataInfoTabCtrl", function(event, msg) {
		var functionKey = msg.functionKey;
		if (functionKey == 'openTab') {
			openTabResponse();
		}
		else if(functionKey == 'updataStoreInfo') {
			
		}
	});
	
	$scope.isDisableEdit = function() {
		return $scope.isDisableEditTag;
	}
	
	$scope.changeEditTag = function() {
		if ($scope.isDisableEditTag == true) {
			$scope.changeEditButtonName = "退出编辑";
			$scope.isDisableEditTag = false;
		} 
		else {
			$scope.changeEditButtonName = "进入编辑";
			$scope.isDisableEditTag = true;
		}
	}
	
	$scope.updataStoreInfo = function(storeInfo) {
		
		$scope.isDisableEditTag = true;
		$scope.changeEditButtonName = "进入编辑";
	}
	
	var openTabResponse = function() {
		$scope.isDisableEditTag = true;
		$scope.changeEditButtonName = "进入编辑";
	}
	
});