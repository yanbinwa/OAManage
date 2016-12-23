angular.module('ionicApp.controllers')

.controller('CheckinTabCtrl', function($scope, $rootScope, $state, WebsocketClient, UserInfo, URL) {
	
	$scope.$watch('$viewContentLoaded', function(event) {
		openTabResponse();
	})
	
	$scope.$on("GeneralEvent", function(event, msg) {
		var functionKey = msg.functionKey;
		if (functionKey == 'onSessionConnected') {
			onSessionConnectedResponse(msg);
		}
	});
	
	$rootScope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams) {
		var url = toState.url;
		if (URL.getCtrByUrl(url) == 'CheckinTabCtrl') {
			openTabResponse();
		}
	})
	
	var onSessionConnectedResponse = function(msg) {
		var stateAuth = msg.stateAuth;
		if (!stateAuth) {
			$state.go(URL.getLoginStateName());
		}
		else {
			openTabResponse();
		}
	}
	
	$scope.$on("CheckinTabCtrl", function(event, msg) {
		var functionKey = msg.functionKey;
		// fetch the ORCode
		if (functionKey == 'getORCodeKey') {
			getORCodeKeyResponse(msg);
		}
		else if(functionKey == 'openTab') {
			openTabResponse();
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
	
	var openTabResponse = function() {
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
		var store = UserInfo.getUserInfo().store;
		if (store == null) {
			alert("can not get store from UserInfo");
			return;
		}
		oRCodeKey = oRCodeKey + "_" + store.id;
		$('#code').empty();
		$('#code').qrcode(oRCodeKey);
		$scope.$digest();
	}
	
	var sendMsg = function(msg) {
		msg.routeKey = 'CheckinTabCtrl';
		$scope.$emit("MainCtrl", msg);
	}
	
});