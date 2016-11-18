angular.module('OAManage.services', [])

.factory('WebsocketClient', function() {

	var session = new Object();
	session.client = null;
	session.connected = false;

//	if ('WebSocket' in window) {
//		session.client = new WebSocket("ws://localhost:8080/OAManage/websocket/websocketSpring");
//	}

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
		}
	};
});