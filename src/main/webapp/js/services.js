angular.module('ionicApp.services', [])

.factory('WebsocketClient', function() {

	//var SERVER_URL = "ws://localhost:8080/OAManage/websocket/websocketSpring";
	var SERVER_URL = "wss://localhost:8443/OAManage/websocket/websocketSpring";
	var WEBSOCKET_ERROR = 400;
	var RESPONSE_OK = 200;
	
	var session = new Object();
	session.client = null;
	session.connected = false;

	if ('WebSocket' in window) {
		session.client = new WebSocket(SERVER_URL);
	}

	return {
		getSession: function() {
			return session;
		},
		getServerUrl: function() {
			return SERVER_URL;
		},
		getWebSocketError: function() {
			return WEBSOCKET_ERROR;
		},
		getResponseOk: function() {
			return RESPONSE_OK;
		}
	};
})

.factory('StoreInfo', function() {
	var STORE_ID = 1;
	
	var storeInfo = {
		id: STORE_ID,
		name: 'CISCO',
		address: '上海市徐汇区宜山路926号新思大楼',
		tel: '13222085556'
	};
	return {
		getStoreInfo: function() {
			return storeInfo;
		}
	};
})

.factory('Employee', function() {
	
	var employeeTemplate = {
		sex: '男',
	    storeId: -1
	};
	
	var EMPLOYEES_EXPIRE_TIME = 5000;
	
	return {
		getEmployeeTemplate: function() {
			var newEmployee = JSON.parse(JSON.stringify(employeeTemplate));
			return newEmployee;
		},
		getEmptyEmployee: function() {
			var emptyTemplate = {
				sex: '男',
				storeId: '',
				name: '',
				tel: ''
			};
			return emptyTemplate;
		},
		clearEmployee: function(employee) {
			employee.storeId = -1;
			employee.name = '';
			employee.sex = '';
			employee.tel = '';
			employee.ID = '';
		},
		getEmployeeExpireTime: function() {
			return EMPLOYEES_EXPIRE_TIME;
		}
	};
})

.factory('User', function() {
	var userTemplate = {
		authType: '普通用户',
		userType: 'employee'
	};
	
	return {
		getUserTemplate: function() {
			var newUser = JSON.parse(JSON.stringify(userTemplate));
			return newUser;
		},
		clearUser: function(user) {
			user.name = '';
			user.authType = '普通用户';
			user.password = '';
			user.comfirmPassword = '';
		}
	}
})

.factory('DateUtil', function() {
	return {
		getTimestampFromDateStr: function(dateStr) {
			return new Date(dateStr).getTime();
		},
		getDateStrFromTimestamp: function(timestamp) {
			var date = new Date(timestamp);
			var year = date.getFullYear();
			var month = date.getUTCMonth();
			var day = date.getDate();
			return year + "/" + month + "/" + day;
		}
	}
});