angular.module('OAManage.services', [])

.factory('WebsocketClient', function() {

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
		}
	};
})

.factory('Actions', function() {

	var actions = [{
		name: '打卡',
		url: '#/tab/dash'
	}, {
		name: '添加员工',
		url: '#/tab/addEmployee'
	}, {
		name: '查询本店员工',
		url: '#/tab/showEmployee'
	}, {
		name: '更新本店信息',
		url: '#/tab/update'
	}];

	function getActionByName(actionName) {
		for(var i = 0; i < actions.length; i ++) {
			var action = actions[i];
			if (action.name == actionName) {
				return action;
			}
		}
		return actions[0];
	}

	return {
		getActions: function() {
			return actions;
		},
		getActionByName: function(actionName) {
			return getActionByName(actionName);
		}
	};

});