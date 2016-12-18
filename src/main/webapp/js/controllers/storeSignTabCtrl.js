angular.module('ionicApp.controllers')

.controller('StoreSignTabCtrl', function($scope, $rootScope, $state, User, Store, DateUtil, WebsocketClient, URL) {
    
	var scope = $rootScope;
	
    $scope.$watch('$viewContentLoaded', function(event) {
    	
	})
	
    $scope.$on("StoreSignTabCtrl", function(event, msg) {
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
	});
    
    $scope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams) {
    	var url = toState.url;
		if (URL.getCtrByUrl(url) == 'StoreSignTabCtrl') {
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
        if(!vaildateStore($scope.store)) {
        	return;
        }
        var payLoad = {};
        payLoad.user = $scope.user;
        payLoad.store = $scope.store;
        payLoad.store.provinceId = JSON.parse($scope.location.province).id;
        payLoad.store.cityId = JSON.parse($scope.location.city).id;
        payLoad.store.areaId = JSON.parse($scope.location.area).id;
       
        userSign(payLoad);
    }
    
    $scope.clear = function() {
    	updateTab(false);
	}
    
    var updateTab = function(tag) {
    	$scope.user = User.getUserTemplate('store');
        $scope.store = Store.getStoreTemplate();
        $scope.provinces = [];
        $scope.citys = [];
        $scope.areas = [];
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
    
    var vaildateStore = function(store) {
    	if(store.name == null || store.name == '') {
    		alert("请输入门店名称");
    		return false;
    	}
    	if (store.tel == null || store.tel == '')
    	{
    		alert("请输入联系方式");
    		return false;
    	}
    	if ($scope.location.province == null)
    	{
    		alert("请选择所在省市区");
    		return false;
    	}
    	if ($scope.location.city == null)
    	{
    		alert("请选择所在省市区");
    		return false;
    	}
    	if ($scope.location.area == null)
    	{
    		alert("请选择所在省市区");
    		return false;
    	}
    	if (store.address == null || store.address == '')
    	{
    		alert("请输入门店详细地址");
    		return false;
    	}
    	return true;
    }
    
    var sendMsg = function(msg) {
		msg.routeKey = 'StoreSignTabCtrl';
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
    	$state.go(URL.getSignStateName());
    }
    
    var onSessionConnectedResponse = function(msg) {
    	openTabResponse();
    }
    
    /*  Location query  */
    
    $scope.selectProvince = function() {
    	if ($scope.location.province == null) {
    		return;
    	}
    	$scope.store.province = JSON.parse($scope.location.province);
    	getCityList($scope.store.province.id);
    }
    
    $scope.selectCity = function() {
    	if ($scope.location.city == null) {
    		return;
    	}
    	$scope.store.city = JSON.parse($scope.location.city);
    	getAreaList($scope.store.city.id);
    }
    
    $scope.selectArea = function() {
    	$scope.store.area = JSON.parse($scope.location.area);
    }
    
    var getProvinceList = function() {
    	var data = {
			functionKey: 'getProvinceList',
			urlName: 'GetProvinceList',
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
    	scope.$digest();
    }
    
    var getCityList = function(provinceId) {
    	var data = {
			functionKey: 'getCityList',
			urlName: 'GetCityList',
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
    	scope.$digest();
    }
    
    var getAreaList = function(cityId) {
    	var data = {
			functionKey: 'getAreaList',
			urlName: 'GetAreaList',
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
    	scope.$digest();
    }
    
});
