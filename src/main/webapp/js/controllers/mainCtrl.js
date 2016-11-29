angular.module('ionicApp.controllers')

.controller('MainCtrl', function($scope, $rootScope, $ionicSideMenuDelegate, WebsocketClient, StoreInfo) {

	$scope.infoNum = 0;
	$scope.session = WebsocketClient.getSession();
	$scope.storeInfo = StoreInfo.getStoreInfo();
	
	$scope.$watch('$viewContentLoaded', function(event) {
		
	})
	
	$scope.$on("MainCtrl", function(event, msg) {
		if(checkSocketAlive()) {
			sendMsgToServer(msg);
		} 
		else {
			var responseMsg = {
				routeKey : msg.routeKey,
				functionKey : msg.functionKey,
				responsePayLoad : WebsocketClient.getWebSocketError()
			};
			$scope.$broadcast(responseMsg.routeKey, responseMsg);
		}
	})
	
	$scope.openChildrenTab = function(index) {
		var responseMsg = {
			routeKey : null,
			functionKey : 'openTab'
		};
				
		if (index == 1) {
			responseMsg.routeKey = 'CheckinTabCtrl';
		} 
		else if(index == 2) {
			responseMsg.routeKey = 'AddEmployeeTabCtrl';
		}
		else if(index == 3) {
			responseMsg.routeKey = 'ListEmployeeTabCtrl';
		}
		else if(index == 4) {
			responseMsg.routeKey = 'UpdataInfoTabCtrl';
		}
		else if(index == 5) {
			
		}
		$scope.$broadcast(responseMsg.routeKey, responseMsg);
	}
	
	$scope.session.client.onmessage = function() {
		handleManage(event);
	}
	
	$scope.session.client.onopen = function(event) {
		$scope.session.connected = true;
		getStoreInfo($scope.storeInfo.id);
	}

	$scope.session.client.onclose = function() {
		$scope.session.connected = false;
		$scope.session.client = null;
	}
	
  	$scope.toggleLeft = function() {
	 	 $ionicSideMenuDelegate.toggleLeft();
  	}
  	$scope.shouldHide = function() {
		  return true;
  	}
  	$scope.shouldShowInfoNum = function() {
  		if($scope.infoNum == 0) {
  			return false;
  		}
  		return true;
  	}
  	
  	var checkSocketAlive = function() {
  		return $scope.session.connected;
  	}
  	
  	var sendMsgToServer = function(msg) {
  		var jsonStr = JSON.stringify(msg);
  		$scope.session.client.send(jsonStr);
  	}
  	
  	var handleManage = function(event) {
		var msg = JSON.parse(event.data);
		var routeKey = msg.routeKey;
		if (routeKey != 'MainCtrl') {
			$scope.$broadcast(routeKey, msg);
		}
		else {
			var functionKey = msg.functionKey;
			if (functionKey == 'getStoreInfo') {
				getStoreInfoResponse(msg);
			}
		}
	}
  	
  	var getStoreInfo = function(id) {
  		var data = {
			functionKey: 'getStoreInfo',
			urlName: 'GetStoreById',
			payLoad: null,
			urlParameter: id
		}
		sendMsg(data);
  	}
  	
  	var getStoreInfoResponse = function(msg) {
  		var responseCode = msg.responseCode;
  		if (responseCode != WebsocketClient.getResponseOk()) {
  			return;
  		}
  		store = msg.responsePayLoad;
  		$scope.storeInfo.name = store.name;
  		$scope.storeInfo.address = store.address;
  		$scope.storeInfo.tel = store.tel;
  	}
  	
  	var sendMsg = function(msg) {
  		msg.routeKey = 'MainCtrl';
  		$scope.$emit("MainCtrl", msg);
  	}
})

.controller('HomeTabCtrl', function($scope) {
	$scope.$watch('$viewContentLoaded', function(event) {
		
	})
});