angular.module('ionicApp.controllers')

.controller('CheckinTabCtrl', function($scope, $state, WebsocketClient, UserInfo) {
	$scope.$watch('$viewContentLoaded', function(event) {

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
			$state.go('login');
		}
	}
	
	$scope.$on("CheckinTabCtrl", function(event, msg) {
		var functionKey = msg.functionKey;
		if (functionKey == 'getORCodeKey') {
			getORCodeKeyResponse(msg);
		}
		else if(functionKey == 'openTab') {
			openTabResponse(msg);
		}
	});

	$scope.getORCodeKey = function() {
		getORCodeKey();
	}
	
	var getORCodeKey = function() {
		var data = {
			functionKey: 'getORCodeKey',
			urlName: 'GetORCodeKey',
			payLoad: null,
			urlParameter: null
		}
		sendMsg(data);
	}
	
	var getORCodeKeyResponse = function(msg) {
		showORCode(msg);
	}
	
	var openTabResponse = function(msg) {
		getORCodeKey();
	}
	
	var showORCode = function(msg) {
		var responseCode = msg.responseCode;
		if (responseCode != WebsocketClient.getResponseOk()) {
			alert("获取二维码失败");
			return;
		}
		var oRCodeKey = msg.responsePayLoad;
		$('#code').empty();
		$('#code').qrcode(oRCodeKey);
		$scope.$apply();
	}
	
	var sendMsg = function(msg) {
		msg.routeKey = 'CheckinTabCtrl';
		$scope.$emit("MainCtrl", msg);
	}
	
	getORCodeKey();
});