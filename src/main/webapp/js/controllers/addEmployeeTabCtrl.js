angular.module('ionicApp.controllers')

.controller('AddEmployeeTabCtrl', function($scope, $rootScope, $state, WebsocketClient, Employee, UserInfo, URL) {
	
	$scope.$watch('$viewContentLoaded', function(event) {
		updateTab();
	})
	
	$scope.$on("GeneralEvent", function(event, msg) {
		var functionKey = msg.functionKey;
		if (functionKey == 'onSessionConnected') {
			onSessionConnectedResponse(msg);
		}
	});
	
	$rootScope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams) {
		var url = toState.url;
		if (URL.getCtrByUrl(url) == 'AddEmployeeTabCtrl') {
			openTabResponse();
		}
	})
	
	var onSessionConnectedResponse = function(msg) {
		var stateAuth = msg.stateAuth;
		if (!stateAuth) {
			$state.go(URL.getLoginStateName());
		}
	}
	
	$scope.$on("AddEmployeeTabCtrl", function(event, msg) {
		var functionKey = msg.functionKey;
		if (functionKey == 'addEmployee') {
			addEmployeeResponse(msg);
		}
		else if(functionKey == 'openTab') {
			openTabResponse();
		}
		else if(functionKey == 'getEmployeeById') {
			getEmployeeByIdResponse(msg);
		}
	});
	
	$scope.addEmployee = function(employee) {
		var data = {
			functionKey: 'addEmployee',
			urlName: 'CreateEmployee',
			payLoad: JSON.stringify(employee),
			urlParameter: null
		}
		sendMsg(data);
	}
	$scope.clearEmployee = function(employee) {
		employee = Employee.clearEmployee(employee);
	}
	$scope.getEmployeeById = function(employee) {
		var id = employee.id;
		if(id == null) {
			alert("请输入要添加的员工ID");
			return;
		}
		var data = {
			functionKey: 'getEmployeeById',
			urlName: 'GetEmployeeById',
			payLoad: null,
			urlParameter: id
		}
		sendMsg(data);
	}
	
	var addEmployeeResponse = function(msg) {
		var responseCode = msg.responseCode;
		if (responseCode != WebsocketClient.getResponseOk()) {
			alert("添加员工失败");
		}
		else {
			alert("添加员工成功");
		}
	}
	
	var getEmployeeByIdResponse = function(msg) {
		var responseCode = msg.responseCode;
		if (responseCode != WebsocketClient.getResponseOk()) {
			alert("获取二维码失败");
			return;
		}
		var employee = msg.responsePayLoad;
		$scope.employee = employee;
		$scope.$apply();
	}
	
	var openTabResponse = function() {
		updateTab();
	}
	
	var sendMsg = function(msg) {
		msg.routeKey = 'AddEmployeeTabCtrl';
		$scope.$emit("MainCtrl", msg);
	}
	
	var updateTab = function() {
		$scope.employeeTemplete = Employee.getEmployeeTemplate();
		$scope.employee = Employee.getEmptyEmployee();
	}

});