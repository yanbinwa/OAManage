angular.module('ionicApp.controllers', [])

.controller('MainCtrl', function($scope, $rootScope, $ionicSideMenuDelegate, WebsocketClient) {

	$scope.infoNum = 0;
	$scope.session = WebsocketClient.getSession();
	
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
				responsePayLoad : WebsocketClient.getWebSocketUnconnectionCode()
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
		$scope.$broadcast(responseMsg.routeKey, responseMsg);
	}
	
	$scope.session.client.onmessage = function() {
		handleManage(event);
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
		$scope.$broadcast(routeKey, msg);
	}
})

.controller('HomeTabCtrl', function($scope) {
	$scope.$watch('$viewContentLoaded', function(event) {
		
	})
})

.controller('CheckinTabCtrl', function($scope, WebsocketClient) {
	$scope.$watch('$viewContentLoaded', function(event) {
		
	})
	$scope.$on("CheckinTabCtrl", function(event, msg) {
		var functionKey = msg.functionKey;
		if (functionKey == 'getORCodeKey') {
			showORCode(msg);
		}
		else if(functionKey == 'openTab') {
			showORCode(msg);
		}
	});

	$scope.getORCodeKey = function() {
		var data = {
			functionKey: 'getORCodeKey',
			urlName: 'GetORCodeKey',
			payLoad: null,
			urlParameter: null
		}
		sendMsg(data);
	}
	
	var showORCode = function(msg) {
		var oRCodeKey = msg.responsePayLoad;
		if (oRCodeKey == WebsocketClient.getWebSocketUnconnectionCode()) {
			return;
		}
		$('#code').empty();
		$('#code').qrcode(oRCodeKey);
		$scope.$apply();
	}
	
	var sendMsg = function(msg) {
		msg.routeKey = 'CheckinTabCtrl';
		$scope.$emit("MainCtrl", msg);
	}
	
	$scope.getORCodeKey();
})

.controller('AddEmployeeTabCtrl', function($scope, WebsocketClient) {
	$scope.$watch('$viewContentLoaded', function(event) {
		
	})
	$scope.$on("AddEmployeeTabCtrl", function(event, msg) {
		var functionKey = msg.functionKey;
		if (functionKey == 'AddEmployee') {
			handleResponseMsg(msg);
		}
	});
	$scope.addEmployee = function(employee) {
		var data = {
			functionKey: 'AddEmployee',
			urlName: 'CreateEmployee',
			payLoad: JSON.stringify(employee),
			urlParameter: null
		}
		sendMsg(data);
		$scope.clearEmployee(employee);
	}
	
	$scope.clearEmployee = function(employee) {
		employee.id = "";
		employee.name = "";
		employee.tel = "";
	}
	
	var sendMsg = function(msg) {
		msg.routeKey = 'AddEmployeeTabCtrl';
		$scope.$emit("MainCtrl", msg);
	}
	var handleResponseMsg = function(msg) {
		var state = msg.responsePayLoad;
		if (state == WebsocketClient.getResponseOk()) {
			alert("添加员工成功");
		}
		else {
			alert("添加员工失败");
		}
	}
})

.controller('ListEmployeeTabCtrl', function($scope, $rootScope, WebsocketClient) {
	
	$scope.employees = [];
	
	var employeeUpdateTime = window.localStorage['employeeUpdateTime'];
	
	if(employeeUpdateTime == null) {
		employeeUpdateTime = 0;
		window.localStorage['employeeUpdateTime'] = employeeUpdateTime;
	}
	$scope.$watch('$viewContentLoaded', function(event) {
		
	})
	$scope.$on("ListEmployeeTabCtrl", function(event, msg) {
		var functionKey = msg.functionKey;
		if (functionKey == 'ListEmployee') {
			handleResponseMsg(msg);
		}
		else if(functionKey == 'openTab') {
			listEmployee();
		}
	});
	
	var listEmployee = function() {
		if ((new Date().getTime() - employeeUpdateTime) > WebsocketClient.getEmployeeExpireTime()) {
			var data = {
				functionKey: 'ListEmployee',
				urlName: 'GetAllEmployee',
				payLoad: null,
				urlParameter: null	
			}
			sendMsg(data);
		}
		else {
			$scope.employees = window.localStorage['employees'];
		}
	}
	
	var sendMsg = function(msg) {
		msg.routeKey = 'ListEmployeeTabCtrl';
		$scope.$emit("MainCtrl", msg);
	}
	
	var handleResponseMsg = function(msg) {
		var employees = msg.responsePayLoad;
		if (employees == null) {
			return;
		}
		if (employees instanceof Array) {
			$scope.employees = employees;
			window.localStorage['employees'] = angular.toJson($scope.employees);
			window.localStorage['employeeUpdateTime'] = new Date().getTime();
			$scope.$apply();
		}
		else
		{
			alert("非法员工信息");
		}
	}
	
	listEmployee();
	
})

.controller('UpdataInfoTabCtrl', function($scope, StoreInfo) {
	
	$scope.storeInfo = StoreInfo.getStoreInfo();
	$scope.isDisableEditTag = true;
	$scope.changeEditButtonName = "进入编辑";
	
    $scope.$watch('$viewContentLoaded', function(event) {
		
	})
	$scope.$on("UpdataInfoTabCtrl", function(event, msg) {
		var functionKey = msg.functionKey;
		if (functionKey == 'openTab') {
			$scope.isDisableEditTag = true;
			$scope.changeEditButtonName = "进入编辑";
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
	
});