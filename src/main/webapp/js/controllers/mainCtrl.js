angular.module('ionicApp.controllers')

.controller('MainCtrl', function($scope, $state, $rootScope, $ionicSideMenuDelegate, $timeout, $ionicModal, WebsocketClient, StoreInfo, Notification, UserInfo) {

    $scope.$watch('$viewContentLoaded', function(event) {

	})
	
	$scope.infoNum = Notification.getNotificationsSize();
	$scope.session = WebsocketClient.getSession(onOpen, onClose, onMessage);
	$scope.notifications = Notification.getNotifications();
	
	$scope.$on("MainCtrl", function(event, msg) {
		if(WebsocketClient.checkSocketAlive()) {
			sendMsgToServer(msg);
		} 
		else {
			reConnectSession(1);
			if(WebsocketClient.checkSocketAlive()) {
				sendMsgToServer(msg);
			}
			else {
				var responseMsg = {
					routeKey : msg.routeKey,
					functionKey : msg.functionKey,
					responseCode : WebsocketClient.getWebSocketError(),
					responsePayLoad : null
				};
				handleManage(responseMsg);
			}
		}
	})
	
	$scope.$on("MainCtrlNotify", function(event, notify) {
		var routeKey = notify.routeKey;
		if (routeKey == "MainCtrlNotify") {
			if (notify.functionKey == "reConnectSession") {
				reConnectSessionResponse();
			}
		}
		else {
			
		}
	})
	
  	var sendMsg = function(msg) {
  		msg.routeKey = 'MainCtrl';
  		$scope.$emit("MainCtrl", msg);
  	}
  	
  	var sendNotify = function(notify) {
  		notify.routeKey = 'MainCtrlNotify';
  		$scope.$emit("MainCtrlNotify", notify);
  	}
	
	/* --------- WebSocket client function ---------- */
	
  	var handleManage = function(event) {
		var msg = JSON.parse(event.data);
		var routeKey = msg.routeKey;
		if (routeKey != 'MainCtrl') {
			$scope.$broadcast(routeKey, msg);
		}
		else {
			var functionKey = msg.functionKey;
			if(functionKey == 'getNotifyMessage') {
				getNotifyMessageResponse(msg);
			}
			else if(functionKey == 'getSessionId') {
				getSessionIdResponse(msg);
			}
			else if(functionKey == 'handleVerifyNotify') {
				handleVerifyNotifyResponse(msg);
			}
		}
	}
	
    function onOpen() {
		alert('websocket connected');
		WebsocketClient.sessionOnOpen();
		onSessionConnected();
	}
	
	function onMessage(event) {
		handleManage(event);
	}
	
	function onClose() {
		WebsocketClient.sessionOnClose();
	}
	
	var onSessionConnected = function() {
		getSessionId();
	}
	
  	var sendMsgToServer = function(msg) {
  		var jsonStr = JSON.stringify(msg);
  		WebsocketClient.sendMsg(jsonStr);
  	}
	
	/* ---------------------------------------------- */
	
	
	/* --------- Get Session Id ---------- */
	
	var getSessionId = function() {
		var sessionId = WebsocketClient.getSessionId();
		var data = {
			functionKey: 'getSessionId',
			urlName: 'GetSessionId',
			payLoad: sessionId,
			urlParameter: null
		}
		sendMsg(data);
	}
	
	var getSessionIdResponse = function(msg) {
		var responseCode = msg.responseCode;
		var stateAuth = false;
  		if (responseCode != WebsocketClient.getResponseOk()) {
  			return;
  		}
  		var sessionId = msg.responsePayLoad;
  		if (sessionId == WebsocketClient.getSessionId()) {
  			StoreInfo.loadStoreInfo(WebsocketClient.getStorageKey());
  			UserInfo.loadUserInfo(WebsocketClient.getStorageKey());
  			stateAuth = true;
  		}
  		else {
  			WebsocketClient.setSessionId(sessionId);
  			WebsocketClient.saveSessionId(WebsocketClient.getStorageKey());
  		}
  		
  		//send sessionOpenMsg to other states
  		var responseMsg = {
			routeKey : 'GeneralEvent',
			functionKey : 'onSessionConnected',
			stateAuth: stateAuth
		};
  		$scope.$broadcast(responseMsg.routeKey, responseMsg);
	}
	
	/* ---------------------------------------------- */

	
	/* --------- ReConnect Session ---------- */
	
	var reConnectSession = function(timeout) {
		if (WebsocketClient.isReconnecting())
		{
			return;
		}
		WebsocketClient.startReconnect();
		$timeout(function() {
			var data = {
				functionKey: 'reConnectSession',
				stateName: null
			}
			sendNotify(data);
		}, timeout);
	}
	
	var reConnectSessionResponse = function(notify) {
		$scope.session = WebsocketClient.getSession(onOpen, onClose, onMessage);
	}
	
	/* ---------------------------------------------- */
	
	
	/* --------- Handle Notification ---------- */

	$scope.checkBoxContext = {};
	$scope.checkBoxContext.isSelected = false;
	$scope.currentNotification = null;
	
  	var getNotifyMessageResponse = function(msg) {
  		var responseCode = msg.responseCode;
  		if (responseCode != WebsocketClient.getResponseOk()) {
  			return;
  		}
  		var payLoad = msg.responsePayLoad;
  		if (payLoad.type == "UserSignVerify") {
  			handleUserSignVerifyNotification(payLoad);
  		}
  	}
  	
  	var handleUserSignVerifyNotification = function(payLoad) {
  		var notification = payLoad;
  		Notification.addNotification(payLoad);
  		$scope.infoNum = Notification.getNotificationsSize();
  		$rootScope.$digest();
  	}
  	
  	var getSelectedNotify = function() {
  		var notifications = [];
  		if ($scope.checkBoxContext.isSelected) {
  			notifications = Notification.getNotifications();
  		}
  		else {
  			for(var i = 0; i < $scope.notifications.length; i ++) {
  				if($scope.notifications[i].isSelected == true) {
  					notifications.push($scope.notifications[i]);
  				}
  			}
  		}
  		return notifications;
  	}
  	
  	$scope.selectedAllNotify = function() {
  		for(var i = 0; i < $scope.notifications.length; i ++) {
			$scope.notifications[i].isSelected = $scope.checkBoxContext.isSelected;
		}
  	}
  	
  	$scope.confirmSelectedNotify = function() {
  		var notifications = getSelectedNotify();
  		if (notifications.length == 0) {
  			alert("当前没有信息被选中");
  			return;
  		}
  		handleVerifyNotify(notifications);
  		for(var i = 0; i < notifications.length; i++) {
  			removeNotification(notifications[i]);
  		}
  	}
  	
  	$scope.clearSelectedNotify = function() {
  		var notifications = getSelectedNotify();
  		for(var i = 0; i < notifications.length; i++) {
  			removeNotification(notifications[i]);
  		}
  	}
  	
  	$scope.getNotifyTitle = function(notification) {
  		if (Notification.isEmployeeSignNotify(notification)) {
  			return "员工申请认证";
  		}
  		else if(Notification.isStoreSignNotify(notification)) {
  			return "门店申请认证";
  		}
  	}
  	
  	$ionicModal.fromTemplateUrl('templates/employeeSignVerify.html', function(modal) {
  	    $scope.employeeSignVerifyModal = modal;
  	}, {
  	    scope: $scope,
  	    animation: 'slide-in-up'
  	});
  	
  	$ionicModal.fromTemplateUrl('templates/storeSignVerify.html', function(modal) {
  	    $scope.storeSignVerifyModal = modal;
  	}, {
  	    scope: $scope,
  	    animation: 'slide-in-up'
  	});
  	
  	$scope.verifyNotify = function(notification) {
  		$scope.notification = notification;
  		openVerifyNotify(notification);
  	}
  	
  	$scope.cancelVerifyNotify = function(notification) {
  		closeVerifyNotify(notification);
  	}
  	
  	$scope.confirmVerifyNotify = function(notification) {
  		var notifications = [];
  		notifications.push(notification);
  		handleVerifyNotify(notifications);
  		$scope.currentNotification = notification;
  	}
  	
  	var openVerifyNotify = function(notification) {
  		if (Notification.isEmployeeSignNotify(notification)) {
  			$scope.employeeSignVerifyModal.show();
  		}
  		else if(Notification.isStoreSignNotify(notification)) {
  			$scope.storeSignVerifyModal.show();
  		}
  	}
  	
  	var closeVerifyNotify = function(notification) {
  		if (Notification.isEmployeeSignNotify(notification)) {
  			$scope.employeeSignVerifyModal.hide();
  		}
  		else if(Notification.isStoreSignNotify(notification)) {
  			$scope.storeSignVerifyModal.hide();
  		}
  	}
  	
  	var removeNotification = function(notification) {
  		Notification.removeNotification(notification);
  		$scope.infoNum = Notification.getNotificationsSize();
  		$rootScope.$digest();
  	}
  	
  	var handleVerifyNotify = function(notifications) {
  		var users = [];
  		for(var i = 0; i < notifications.length; i ++) {
  			users.push(notifications[i].user);
  		}
  		var data = {
			functionKey: 'handleVerifyNotify',
			urlName: 'VerifyUserSign',
			payLoad: JSON.stringify(users),
			urlParameter: null
		}
		sendMsg(data);
  	}
  	
  	var handleVerifyNotifyResponse = function(msg) {
  		var responseCode = msg.responseCode;
  		if (responseCode != WebsocketClient.getResponseOk()) {
  			alert("认证用户时出现错误");
  		}
  		else {
  			alert("认证用户时出现成功");
  			if ($scope.currentNotification != null) {
  				removeNotification($scope.currentNotification);
  				closeVerifyNotify($scope.currentNotification);
  				$scope.currentNotification = null;
  			}
  		}
  	}
  	
  	/* ---------------------------------------------- */

  	
  	/* --------- Other ---------- */
  	
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
 	
  	$scope.isAdminUser = function() {
  		return UserInfo.isAdminUser();
  	}
  	
  	$scope.isNormalUser = function() {
  		return UserInfo.isNormalUser();
  	}
  	
  	/* ---------------------------------------------- */
});