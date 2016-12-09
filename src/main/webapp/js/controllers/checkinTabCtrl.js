angular.module('ionicApp.controllers')

.controller('CheckinTabCtrl', function($scope, $state, WebsocketClient, UserInfo, StoreInfo) {
	
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
		// fetch the ORCode
		if (functionKey == 'getORCodeKey') {
			getORCodeKeyResponse(msg);
		}
		else if(functionKey == 'openTab') {
			openTabResponse(msg);
		}
		// push the ORCode
		else if(functionKey == 'oRCodeKeyUpdate') {
			oRCodeKeyUpdateResponse(msg);
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
		var responseCode = msg.responseCode;
		if (responseCode != WebsocketClient.getResponseOk()) {
			alert("获取二维码失败");
			return;
		}
		showORCode(msg.responsePayLoad);
	}
	
	var openTabResponse = function(msg) {
		getORCodeKey();
	}
	
	var oRCodeKeyUpdateResponse = function(msg) {
		var responseCode = msg.responseCode;
		if (responseCode != WebsocketClient.getResponseOk()) {
			return;
		}
		showORCode(msg.responsePayLoad);
	}
	
	var showORCode = function(oRCodeKey) {
		oRCodeKey = oRCodeKey + "_" + StoreInfo.getStoreInfo().id;
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