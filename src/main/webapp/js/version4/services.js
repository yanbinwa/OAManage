angular.module('ionicApp.services', [])

.factory('WebsocketClient', function() {

	var SERVER_URL = "ws://localhost:8080/OAManage/websocket/websocketSpring";
	var WEBSOCKET_UNCONNECTION = 404;
	var RESPONSE_OK = 200;
	var EMPLOYEES_EXPIRE_TIME = 5000;
	
	var session = new Object();
	session.client = null;
	session.connected = false;

	if ('WebSocket' in window) {
		session.client = new WebSocket("ws://localhost:8080/OAManage/websocket/websocketSpring");
	}

	session.client.onopen = function(event) {
		session.connected = true;
	}

	session.client.onclose = function() {
		session.connected = false;
		session.client = null;
	}

	return {
		getSession: function() {
			return session;
		},
		getServerUrl: function() {
			return SERVER_URL;
		},
		getWebSocketUnconnectionCode: function() {
			return WEBSOCKET_UNCONNECTION;
		},
		getResponseOk: function() {
			return RESPONSE_OK;
		},
		getEmployeeExpireTime: function() {
			return EMPLOYEES_EXPIRE_TIME;
		}
	};
})

.factory('StoreInfo', function() {
	var STORE_ID = 1000;
	
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
});