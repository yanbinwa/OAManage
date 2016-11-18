angular.module('OAManage.controllers', [])

.controller('HomeCtrl', function($scope, Actions, $ionicSideMenuDelegate) {
	$scope.actions = Actions.getActions();
	$scope.activeAction = Actions.getActionByName('打卡');
	$scope.selectAction = function(action) {
		$scope.activeAction = action;
		$ionicSideMenuDelegate.toggleLeft(false);
	}
	$scope.toggleAction = function() {
		$ionicSideMenuDelegate.toggleLeft();
	}
})

.controller('DashCtrl', function($scope, WebsocketClient) {
	$scope.session = WebsocketClient.getSession();

	$scope.session.client.onmessage = function() {
		var oRCodeKey = event.data;
		$('#code').empty();
		$('#code').qrcode(oRCodeKey);
	}

	$scope.getORCodeKey = function() {
		data = {
			urlName: 'GetORCodeKey',
			payLoad: null,
			urlParameter: null
		}
		var jsonStr = JSON.stringify(data);
		if ($scope.session.connected == true) {
			$scope.session.client.send(jsonStr);
		}
		else {
			console.log('websocket disconnected');
		}
	}
});