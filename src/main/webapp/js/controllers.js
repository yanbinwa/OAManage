angular.module('ionicApp.controllers', []);

//.controller('MainCtrl', function($scope, $rootScope, $ionicSideMenuDelegate, WebsocketClient, StoreInfo) {
//
//	$scope.infoNum = 0;
//	$scope.session = WebsocketClient.getSession();
//	$scope.storeInfo = StoreInfo.getStoreInfo();
//	
//	$scope.$watch('$viewContentLoaded', function(event) {
//		
//	})
//	
//	$scope.$on("MainCtrl", function(event, msg) {
//		if(checkSocketAlive()) {
//			sendMsgToServer(msg);
//		} 
//		else {
//			var responseMsg = {
//				routeKey : msg.routeKey,
//				functionKey : msg.functionKey,
//				responsePayLoad : WebsocketClient.getWebSocketError()
//			};
//			$scope.$broadcast(responseMsg.routeKey, responseMsg);
//		}
//	})
//	
//	$scope.openChildrenTab = function(index) {
//		var responseMsg = {
//			routeKey : null,
//			functionKey : 'openTab'
//		};
//				
//		if (index == 1) {
//			responseMsg.routeKey = 'CheckinTabCtrl';
//		} 
//		else if(index == 2) {
//			responseMsg.routeKey = 'AddEmployeeTabCtrl';
//		}
//		else if(index == 3) {
//			responseMsg.routeKey = 'ListEmployeeTabCtrl';
//		}
//		else if(index == 4) {
//			responseMsg.routeKey = 'UpdataInfoTabCtrl';
//		}
//		$scope.$broadcast(responseMsg.routeKey, responseMsg);
//	}
//	
//	$scope.session.client.onmessage = function() {
//		handleManage(event);
//	}
//	
//	$scope.session.client.onopen = function(event) {
//		$scope.session.connected = true;
//		getStoreInfo($scope.storeInfo.id);
//	}
//
//	$scope.session.client.onclose = function() {
//		$scope.session.connected = false;
//		$scope.session.client = null;
//	}
//	
//  	$scope.toggleLeft = function() {
//	 	 $ionicSideMenuDelegate.toggleLeft();
//  	}
//  	$scope.shouldHide = function() {
//		  return true;
//  	}
//  	$scope.shouldShowInfoNum = function() {
//  		if($scope.infoNum == 0) {
//  			return false;
//  		}
//  		return true;
//  	}
//  	
//  	var checkSocketAlive = function() {
//  		return $scope.session.connected;
//  	}
//  	
//  	var sendMsgToServer = function(msg) {
//  		var jsonStr = JSON.stringify(msg);
//  		$scope.session.client.send(jsonStr);
//  	}
//  	
//  	var handleManage = function(event) {
//		var msg = JSON.parse(event.data);
//		var routeKey = msg.routeKey;
//		if (routeKey != 'MainCtrl') {
//			$scope.$broadcast(routeKey, msg);
//		}
//		else {
//			var functionKey = msg.functionKey;
//			if (functionKey == 'getStoreInfo') {
//				getStoreInfoResponse(msg);
//			}
//		}
//	}
//  	
//  	var getStoreInfo = function(id) {
//  		var data = {
//			functionKey: 'getStoreInfo',
//			urlName: 'GetStoreById',
//			payLoad: null,
//			urlParameter: id
//		}
//		sendMsg(data);
//  	}
//  	
//  	var getStoreInfoResponse = function(msg) {
//  		var responseCode = msg.responseCode;
//  		if (responseCode != WebsocketClient.getResponseOk()) {
//  			return;
//  		}
//  		store = msg.responsePayLoad;
//  		$scope.storeInfo.name = store.name;
//  		$scope.storeInfo.address = store.address;
//  		$scope.storeInfo.tel = store.tel;
//  	}
//  	
//  	var sendMsg = function(msg) {
//  		msg.routeKey = 'MainCtrl';
//  		$scope.$emit("MainCtrl", msg);
//  	}
//})
//
//.controller('HomeTabCtrl', function($scope) {
//	$scope.$watch('$viewContentLoaded', function(event) {
//		
//	})
//})
//
//.controller('CheckinTabCtrl', function($scope, WebsocketClient) {
//	$scope.$watch('$viewContentLoaded', function(event) {
//		
//	})
//	$scope.$on("CheckinTabCtrl", function(event, msg) {
//		var functionKey = msg.functionKey;
//		if (functionKey == 'getORCodeKey') {
//			getORCodeKeyResponse(msg);
//		}
//		else if(functionKey == 'openTab') {
//			openTabResponse(msg);
//		}
//	});
//
//	$scope.getORCodeKey = function() {
//		var data = {
//			functionKey: 'getORCodeKey',
//			urlName: 'GetORCodeKey',
//			payLoad: null,
//			urlParameter: null
//		}
//		sendMsg(data);
//	}
//	
//	var getORCodeKeyResponse = function(msg) {
//		showORCode(msg);
//	}
//	
//	var openTabResponse = function(msg) {
//		getORCodeKey();
//	}
//	
//	var showORCode = function(msg) {
//		var responseCode = msg.responseCode;
//		if (responseCode != WebsocketClient.getResponseOk()) {
//			alert("获取二维码失败");
//			return;
//		}
//		var oRCodeKey = msg.responsePayLoad;
//		$('#code').empty();
//		$('#code').qrcode(oRCodeKey);
//		$scope.$apply();
//	}
//	
//	var sendMsg = function(msg) {
//		msg.routeKey = 'CheckinTabCtrl';
//		$scope.$emit("MainCtrl", msg);
//	}
//	
//	$scope.getORCodeKey();
//})
//
//.controller('AddEmployeeTabCtrl', function($scope, WebsocketClient, Employee) {
//	
//	$scope.$watch('$viewContentLoaded', function(event) {
//	})
//	
//	$scope.$on("AddEmployeeTabCtrl", function(event, msg) {
//		var functionKey = msg.functionKey;
//		if (functionKey == 'addEmployee') {
//			addEmployeeResponse(msg);
//		}
//		else if(functionKey == 'openTab') {
//			openTabResponse();
//		}
//		else if(functionKey == 'getEmployeeById') {
//			getEmployeeByIdResponse(msg);
//		}
//	});
//	$scope.addEmployee = function(employee) {
//		var data = {
//			functionKey: 'addEmployee',
//			urlName: 'CreateEmployee',
//			payLoad: JSON.stringify(employee),
//			urlParameter: null
//		}
//		sendMsg(data);
//	}
//	$scope.clearEmployee = function(employee) {
//		employee = Employee.clearEmployee(employee);
//	}
//	$scope.getEmployeeById = function(employee) {
//		var id = employee.id;
//		if(id == null) {
//			alert("请输入要添加的员工ID");
//			return;
//		}
//		var data = {
//			functionKey: 'getEmployeeById',
//			urlName: 'GetEmployeeById',
//			payLoad: null,
//			urlParameter: id
//		}
//		sendMsg(data);
//	}
//	
//	var addEmployeeResponse = function(msg) {
//		var responseCode = msg.responseCode;
//		if (responseCode != WebsocketClient.getResponseOk()) {
//			alert("添加员工失败");
//		}
//		else {
//			alert("添加员工成功");
//		}
//	}
//	
//	var getEmployeeByIdResponse = function(msg) {
//		var responseCode = msg.responseCode;
//		if (responseCode != WebsocketClient.getResponseOk()) {
//			alert("获取二维码失败");
//			return;
//		}
//		var employee = msg.responsePayLoad;
//		$scope.employee = employee;
//		$scope.$apply();
//	}
//	
//	var openTabResponse = function() {
//		updateTab();
//	}
//	
//	var sendMsg = function(msg) {
//		msg.routeKey = 'AddEmployeeTabCtrl';
//		$scope.$emit("MainCtrl", msg);
//	}
//	
//	var updateTab = function() {
//		$scope.employeeTemplete = Employee.getEmployeeTemplate();
//		$scope.employee = Employee.getEmptyEmployee();
//	}
//	
//	updateTab();
//})
//
//.controller('ListEmployeeTabCtrl', function($scope, $rootScope, WebsocketClient, Employee) {
//	
//	$scope.employees = [];
//	
//	var employeeUpdateTime = window.localStorage['employeeUpdateTime'];
//	
//	if(employeeUpdateTime == null) {
//		employeeUpdateTime = 0;
//		window.localStorage['employeeUpdateTime'] = employeeUpdateTime;
//	}
//	$scope.$watch('$viewContentLoaded', function(event) {
//		
//	})
//	
//	$scope.$on("ListEmployeeTabCtrl", function(event, msg) {
//		var functionKey = msg.functionKey;
//		if (functionKey == 'listEmployee') {
//			listEmployeeResponse(msg);
//		}
//		else if(functionKey == 'openTab') {
//			openTabResponse();
//		}
//	});
//	
//	var openTabResponse = function() {
//		listEmployee();
//	}
//	
//	var listEmployee = function() {
//		if ((new Date().getTime() - employeeUpdateTime) > Employee.getEmployeeExpireTime()) {
//			var data = {
//				functionKey: 'listEmployee',
//				urlName: 'GetAllEmployee',
//				payLoad: null,
//				urlParameter: null	
//			}
//			sendMsg(data);
//		}
//		else {
//			$scope.employees = window.localStorage['employees'];
//		}
//	}
//	
//	var sendMsg = function(msg) {
//		msg.routeKey = 'ListEmployeeTabCtrl';
//		$scope.$emit("MainCtrl", msg);
//	}
//	
//	var listEmployeeResponse = function(msg) {
//		var responseCode = msg.responseCode;
//		if (responseCode != WebsocketClient.getResponseOk()) {
//			alert("获取员工信息失败");
//			return;
//		}
//		var employees = msg.responsePayLoad;
//		if (employees == null) {
//			return;
//		}
//		if (employees instanceof Array) {
//			$scope.employees = employees;
//			window.localStorage['employees'] = angular.toJson($scope.employees);
//			window.localStorage['employeeUpdateTime'] = new Date().getTime();
//			$scope.$apply();
//		}
//		else
//		{
//			alert("获取非法员工信息");
//		}
//	}
//	
//	listEmployee();
//	
//})
//
//.controller('UpdataInfoTabCtrl', function($scope, StoreInfo) {
//	
//	$scope.storeInfo = StoreInfo.getStoreInfo();
//	$scope.isDisableEditTag = true;
//	$scope.changeEditButtonName = "进入编辑";
//	
//    $scope.$watch('$viewContentLoaded', function(event) {
//		
//	})
//	$scope.$on("UpdataInfoTabCtrl", function(event, msg) {
//		var functionKey = msg.functionKey;
//		if (functionKey == 'openTab') {
//			openTabResponse();
//		}
//		else if(functionKey == 'updataStoreInfo') {
//			
//		}
//	});
//	
//	$scope.isDisableEdit = function() {
//		return $scope.isDisableEditTag;
//	}
//	
//	$scope.changeEditTag = function() {
//		if ($scope.isDisableEditTag == true) {
//			$scope.changeEditButtonName = "退出编辑";
//			$scope.isDisableEditTag = false;
//		} 
//		else {
//			$scope.changeEditButtonName = "进入编辑";
//			$scope.isDisableEditTag = true;
//		}
//	}
//	
//	$scope.updataStoreInfo = function(storeInfo) {
//		
//		$scope.isDisableEditTag = true;
//		$scope.changeEditButtonName = "进入编辑";
//	}
//	
//	var openTabResponse = function() {
//		$scope.isDisableEditTag = true;
//		$scope.changeEditButtonName = "进入编辑";
//	}
//	
//})
//
//.controller('LoginTabCtrl', function($scope, $state, DateUtil) {
//    $scope.user = {};
//    $scope.user.auth = "普通用户";
//    $scope.login = function() {
//        alert("name is " + $scope.user.username + ", password is: " + $scope.user.password + ", role is: " + $scope.user.auth);
//        $state.go('app.main.home');
//    }
//    $scope.sign = function() {
//        $state.go('sign');
//    }
//})
//
//.controller('SignTabCtrl', function($scope, $state, User, Employee) {
//    
//    $scope.$on("SignTabCtrl", function(event, msg) {
//		var functionKey = msg.functionKey;
//		if (functionKey == 'openTab') {
//			openTabResponse();
//		}
//		else if(functionKey == 'userSign') {
//			userSignResponse();
//		}
//	});
//    
//    $scope.confirm = function() {
//        if(!vaildateUser($scope.user)) {
//        	return;
//        }
//        if(!vaildateEmployee($scope.employee)) {
//        	return;
//        }
//        if(!vaildateStoreLocate($scope.storeLocate)) {
//        	return;
//        }
//        var payLoad = {};
//        payLoad.user = $scope.user;
//        payLoad.employee = $scope.employee;
//        payLoad.storeId = 1;
//        userSign(payLoad);
//    }
//    
//    $scope.clear = function() {
//    	updateTab();
//	}
//    
//    var updateTab = function() {
//    	$scope.user = User.getUserTemplate();
//        $scope.employee = Employee.getEmployeeTemplate();
//        $scope.storeLocate = {
//        	provience: '上海',
//        	city: '上海',
//        	name: '徐汇店'
//        };
//    }
//    
//    var openTabResponse = function() {
//    	updateTab();
//    }
//    
//    var vaildateUser = function(user) {
//    	if(user.username == null || user.username == '') {
//    		alert("用户名不能为空");
//    		return false;
//    	}
//    	if(user.password == null || user.password == '')
//        {
//    	    alert("用户密码不能为空");	
//    	    return false;
//    	}
//    	if (user.password != user.confirmPassword)
//    	{
//    		alert("两次输入密码不一致");
//    		return false;
//    	}
//    	return true;
//    }
//    
//    var vaildateEmployee = function(employee) {
//    	if(employee.name == null || employee.name == '') {
//    		alert("请输入用户名");
//    		return false;
//    	}
//    	if(employee.birthday == null || employee.birthday == '') {
//    		alert("请输入出生日期");
//    		return false;
//    	}
//    	employee.birthday = DateUtil.getTimestampFromDateStr(employee.birthday);
//    	if(employee.age == null || employee.age == '') {
//    		alert("请输入年龄");
//    		return false;
//    	}
//    	if(employee.sex == null || employee.sex == '')
//        {
//    	    alert("请输入性别");	
//    	    return false;
//    	}
//    	if (employee.tel == null || employee.tel == '')
//    	{
//    		alert("请输入联系方式");
//    		return false;
//    	}
//    	if (employee.ID == null || employee.ID == '')
//    	{
//    		alert("请输入身份证号");
//    		return false;
//    	}
//    	return true;
//    }
//    
//    var vaildateStoreLocate = function(storeLocate) {
//    	if(storeLocate.provience == null || storeLocate.provience == '') {
//    		alert("请选择门店位于的省份");
//    		return false;
//    	}
//    	if(storeLocate.city == null || storeLocate.city == '')
//        {
//    	    alert("请选择门店位于的城市");	
//    	    return false;
//    	}
//    	if (storeLocate.name == null || storeLocate.name == '')
//    	{
//    		alert("请选择门店名称");
//    		return false;
//    	}
//    	return true;
//    }
//    
//    var sendMsg = function(msg) {
//		msg.routeKey = 'SignTabCtrl';
//		$scope.$emit("MainCtrl", msg);
//	}
//    
//    var userSign = function(payLoad) {
//    	var data = {
//			functionKey: 'userSign',
//			urlName: 'UserSign',
//			payLoad: JSON.stringify(payLoad),
//			urlParameter: null
//		}
//		sendMsg(data);
//    }
//    
//    var userSignResponse = function(event) {
//    	
//    }
//    
//    updateTab();
//    
//    $('#sign_birthday_input').datepicker({
//        'format': 'yyyy年m月d日',
//        'autoclose': true
//    });
//    
//    $scope.getBirthday = function() {
//    	$scope.employee.birthday = $('#sign_birthday_input').val();
//    }
//    
//});