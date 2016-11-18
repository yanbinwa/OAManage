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
		id: 1,
		storeId: -1,
		storeName: "无",
		leaderId: -1,
		leaderName: "无",
		name: '王彦彬',
		tel: '13222085556'
	};
	
	var EMPLOYEES_EXPIRE_TIME = 5000;
	
	return {
		getEmployeeTemplate: function() {
			var newEmployee = JSON.parse(JSON.stringify(employeeTemplate));
			return newEmployee;
		},
		getEmptyEmployee: function() {
			var emptyTemplate = {
				id: '',
				storeId: '',
				storeName: '',
				leaderId: '',
				leaderName: '',
				name: '',
				tel: ''
			};
			return emptyTemplate;
		},
		clearEmployee: function(employee) {
			employee.id = '';
			employee.storeId = '';
			employee.storeName = '';
			employee.leaderId = '';
			employee.leaderName = '';
			employee.name = '';
			employee.tel = '';
		},
		getEmployeeExpireTime: function() {
			return EMPLOYEES_EXPIRE_TIME;
		}
	};
});