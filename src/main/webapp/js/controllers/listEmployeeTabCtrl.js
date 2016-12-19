angular.module('ionicApp.controllers')

.controller('ListEmployeeTabCtrl', function($scope, $rootScope, $state, WebsocketClient, UserInfo, URL, DateUtil) {

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
		if (URL.getCtrByUrl(url) == 'ListEmployeeTabCtrl') {
			openTabResponse();
		}
	})
	
	var onSessionConnectedResponse = function(msg) {
		var stateAuth = msg.stateAuth;
		if (!stateAuth) {
			$state.go(URL.getLoginStateName());
		}
	}
	
	$scope.$on("ListEmployeeTabCtrl", function(event, msg) {
		var functionKey = msg.functionKey;
		if (functionKey == 'getEmployees') {
			getEmployeesResponse(msg);
		}
	});
	
	var sendMsg = function(msg) {
		msg.routeKey = 'ListEmployeeTabCtrl';
		$scope.$emit("MainCtrl", msg);
	}
	
	var openTabResponse = function() {
		updateTab(true);
	}
	
	var onSessionConnectedResponse = function() {
		openTabResponse();
	}
	
	var updateTab = function(tag) {
		$scope.employeeBriefList = [];
		if (tag == true) {
			getEmployees();
		}
	}
	
	var getEmployees = function() {
		var store = UserInfo.getUserInfo().store;
		if (store == null) {
			alert("can not get store from UserInfo");
			return;
		}
		var data = {
			functionKey: 'getEmployees',
			urlName: 'GetEmployeeInfoByStoreId',
			payLoad: null,
			urlParameter: store.id
		}
		sendMsg(data);
	}
	
	var getEmployeesResponse = function(msg) {
		var responseCode = msg.responseCode;
    	if (responseCode != WebsocketClient.getResponseOk()) {
			alert(msg.responsePayLoad);
			return;
		}
    	var employeeInfoListObj = msg.responsePayLoad;
    	var employeeInfoMapList = Object.entries(employeeInfoListObj);
    	for (var i = 0; i < employeeInfoMapList.length; i ++) {
    		var employeeInfoMap = employeeInfoMapList[0];
    		var employeeId =  employeeInfoMap[0];
    		var employeeInfo = employeeInfoMap[1];
    		var employee = employeeInfo.employee;
    		var employeeDynamicInfo = employeeInfo.employeeDynamicInfo;
    		var isCheckin = isEmployeeCheckin(employeeDynamicInfo.checkinStatus);
    		var checkTime = '';
    		if (isCheckin) {
    			checkTime = getEmployeeCheckinTime(employeeDynamicInfo.checkinTime);
    		}
    		var employeeBrief = {};
    		employeeBrief.id = employee.id;
    		employeeBrief.name = employee.name;
    		employeeBrief.isCheckin = isCheckin;
    		employeeBrief.checkinTime = checkTime;
    		$scope.employeeBriefList.push(employeeBrief);
    	}
    	$rootScope.$digest();
	}
	
	var isEmployeeCheckin = function(checkinStatus) {
		return checkinStatus == 'checkin';
	}
	
	var getEmployeeCheckinTime = function(checkinTime) {
		return DateUtil.getTimeStrFromTimestamp(checkinTime);
	}
		
});