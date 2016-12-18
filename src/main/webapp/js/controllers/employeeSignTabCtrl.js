angular.module('ionicApp.controllers')

.controller('EmployeeSignTabCtrl', function($scope, $rootScope, $state, User, Employee, DateUtil, WebsocketClient, URL) {
    
    $scope.$watch('$viewContentLoaded', function(event) {
		
	})
	
    $scope.$on("EmployeeSignTabCtrl", function(event, msg) {
		var functionKey = msg.functionKey;
		if (functionKey == 'openTab') {
			openTabResponse();
		}
		else if(functionKey == 'userSign') {
			userSignResponse(msg);
		}
		else if(functionKey == 'getProvinceList') {
			getProvinceListResponse(msg);
		}
		else if(functionKey == 'getCityList') {
			getCityListResponse(msg);
		}
		else if(functionKey == 'getAreaList') {
			getAreaListResponse(msg);
		}
		else if(functionKey == 'getStoreList') {
			getStoreListResponse(msg);
		}
	});
    
    $scope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams) {
    	var url = toState.url;
		if (URL.getCtrByUrl(url) == 'EmployeeSignTabCtrl') {
			openTabResponse();
		}
	});
	
	$scope.$on("GeneralEvent", function(event, msg) {
		var functionKey = msg.functionKey;
		if (functionKey == 'onSessionConnected') {
			onSessionConnectedResponse(msg);
		}
	});
    
    $scope.confirm = function() {
        if(!vaildateUser($scope.user)) {
        	return;
        }
        if(!vaildateEmployee($scope.employee)) {
        	return;
        }
        if($scope.storeId == -1) {
        	return;
        }
        var payLoad = {};
        payLoad.user = $scope.user;
        payLoad.employee = $scope.employee;
        payLoad.storeId = $scope.storeId;
        userSign(payLoad);
    }
    
    $scope.clear = function() {
    	updateTab(false);
	}
    
    var onSessionConnectedResponse = function(msg) {
    	openTabResponse();
    }
    
    var updateTab = function(tag) {
    	$scope.user = User.getUserTemplate('employee');
        $scope.employee = Employee.getEmployeeTemplate();
        $scope.storeId = -1;
        $scope.provinces = [];
        $scope.citys = [];
        $scope.areas = [];
        $scope.stores = [];
        $scope.location = {};
        if (tag == true)
        {
        	getProvinceList();
        }
    }
    
    var openTabResponse = function() {
    	updateTab(true);
    }
    
    var vaildateUser = function(user) {
    	if(user.username == null || user.username == '') {
    		alert("用户名不能为空");
    		return false;
    	}
    	if(user.password == null || user.password == '')
        {
    	    alert("用户密码不能为空");	
    	    return false;
    	}
    	if (user.password != user.confirmPassword)
    	{
    		alert("两次输入密码不一致");
    		return false;
    	}
    	return true;
    }
    
    var vaildateEmployee = function(employee) {
    	if(employee.name == null || employee.name == '') {
    		alert("请输入用户名");
    		return false;
    	}
    	var birthdayStr = $('#sign_birthday_input').val();
    	if(birthdayStr == null || birthdayStr == '') {
    		alert("请输入出生日期");
    		return false;
    	}
    	employee.birthday = DateUtil.getTimestampFromDateStr(birthdayStr);
    	if(employee.age == null || employee.age == '') {
    		alert("请输入年龄");
    		return false;
    	}
    	if(employee.sex == null || employee.sex == '')
        {
    	    alert("请输入性别");	
    	    return false;
    	}
    	if (employee.tel == null || employee.tel == '')
    	{
    		alert("请输入联系方式");
    		return false;
    	}
    	if (employee.ID == null || employee.ID == '')
    	{
    		alert("请输入身份证号");
    		return false;
    	}
    	return true;
    }
    
    var sendMsg = function(msg) {
		msg.routeKey = 'EmployeeSignTabCtrl';
		$scope.$emit("MainCtrl", msg);
	}
    
    var userSign = function(payLoad) {
    	var data = {
			functionKey: 'userSign',
			urlName: 'UserSign',
			payLoad: JSON.stringify(payLoad),
			urlParameter: null
		}
		sendMsg(data);
    }
    
    var userSignResponse = function(msg) {
    	var responseCode = msg.responseCode;
    	if (responseCode != WebsocketClient.getResponseOk()) {
			alert(msg.responsePayLoad);
			return;
		}
    	alert("您的申请已经成功提交，请等待审核");
    	updateTab(false);
    	$state.go(URL.getLoginStateName());
    }
    
    $('#sign_birthday_input').datepicker({
        'format': 'yyyy/m/d',
        'autoclose': true
    });
    
    /* -------------- chose store info ----------------- */
    
    $scope.selectProvince = function() {
    	if ($scope.location.province == null) {
    		return;
    	}
    	var province = JSON.parse($scope.location.province);
    	getCityList(province.id);
    }
    
    $scope.selectCity = function() {
    	if ($scope.location.city == null) {
    		return;
    	}
    	var city = JSON.parse($scope.location.city);
    	getAreaList(city.id);
    }
    
    $scope.selectArea = function() {
    	if ($scope.location.area == null) {
    		return;
    	}
    	var area = JSON.parse($scope.location.area);
    	getStoreList(area.id);
    }
    
    $scope.selectStore = function() {
    	if ($scope.location.store == null) {
    		return;
    	}
    	var store = JSON.parse($scope.location.store);
    	$scope.storeId = store.id;
    }
    
    var getProvinceList = function() {
    	var data = {
			functionKey: 'getProvinceList',
			urlName: 'GetStoreProvince',
			payLoad: null,
			urlParameter: null
		}
		sendMsg(data);
    }
    
    var getProvinceListResponse = function(msg) {
    	var responseCode = msg.responseCode;
    	if (responseCode != WebsocketClient.getResponseOk()) {
			alert("fail to get provinceList");
			return;
		}
    	var provinceList = msg.responsePayLoad;
    	var provinceEntrys = Object.entries(provinceList);
    	$scope.provinces = [];
    	for (var i = 0; i < provinceEntrys.length; i ++) {
    		var provinceEntry = provinceEntrys[i];
    		var province = {};
    		province.id = provinceEntry[0];
    		province.name = provinceEntry[1];
    		$scope.provinces.push(province);
    	}
    	$scope.location.province = {};
    	$scope.location.city = {};
    	$scope.location.area = {};
    	$scope.location.store = {};
    	$scope.citys = [];
    	$scope.areas = [];
    	$scope.stores = [];
    	$rootScope.$digest();
    }
    
    var getCityList = function(provinceId) {
    	var data = {
			functionKey: 'getCityList',
			urlName: 'GetStoreCityByProvinceId',
			payLoad: null,
			urlParameter: provinceId
		}
		sendMsg(data);
    }
    
    var getCityListResponse = function(msg) {
    	var responseCode = msg.responseCode;
    	if (responseCode != WebsocketClient.getResponseOk()) {
    		alert("fail to get cityList");
			return;
		}
    	var cityList = msg.responsePayLoad;
    	var cityEntrys = Object.entries(cityList);
    	$scope.citys = [];
    	for (var i = 0; i < cityEntrys.length; i ++) {
    		var cityEntry = cityEntrys[i];
    		var city = {};
    		city.id = cityEntry[0];
    		city.name = cityEntry[1];
    		$scope.citys.push(city);
    	}
    	$scope.location.city = {};
    	$scope.location.area = {};
    	$scope.location.store = {};
    	$scope.areas = [];
    	$scope.stores = [];
    	$rootScope.$digest();
    }
    
    var getAreaList = function(cityId) {
    	var data = {
			functionKey: 'getAreaList',
			urlName: 'GetStoreAreaByCityId',
			payLoad: null,
			urlParameter: cityId
		}
		sendMsg(data);
    }
    
    var getAreaListResponse = function(msg) {
    	var responseCode = msg.responseCode;
    	if (responseCode != WebsocketClient.getResponseOk()) {
    		alert("fail to get areaList");
			return;
		}
    	var areaList = msg.responsePayLoad;
    	var areaEntrys = Object.entries(areaList);
    	$scope.areas = [];
    	for (var i = 0; i < areaEntrys.length; i ++) {
    		var areaEntry = areaEntrys[i];
    		var area = {};
    		area.id = areaEntry[0];
    		area.name = areaEntry[1];
    		$scope.areas.push(area);
    	}
    	$scope.location.area = {};
    	$scope.location.store = {};
    	$scope.stores = [];
    	$rootScope.$digest();
    }
    
    var getStoreList = function(areaId) {
    	var data = {
			functionKey: 'getStoreList',
			urlName: 'GetStoreByAreaId',
			payLoad: null,
			urlParameter: areaId
		}
		sendMsg(data);
    }
    
    var getStoreListResponse = function(msg) {
    	var responseCode = msg.responseCode;
    	if (responseCode != WebsocketClient.getResponseOk()) {
    		alert("fail to get store");
			return;
		}
    	var storeList = msg.responsePayLoad;
    	var storeEntrys = Object.entries(storeList);
    	$scope.stores = [];
    	for (var i = 0; i < storeEntrys.length; i ++) {
    		var storeEntry = storeEntrys[i];
    		var store = {};
    		store.id = storeEntry[0];
    		store.name = storeEntry[1];
    		$scope.stores.push(store);
    	}
    	$scope.location.store = {};
    	$rootScope.$digest();
    }
    
    /* -------------- ----------------- */
    
});
