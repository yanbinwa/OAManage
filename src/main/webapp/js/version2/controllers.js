angular.module('OAManage.controllers', [])

.controller('AppCtrl', function($scope, $ionicSideMenuDelegate) {
	$scope.toggleLeft = function() {
		$ionicSideMenuDelegate.toggleLeft();
	}
})

.controller('HomeTabCtrl', function($scope) {

})

.controller('CheckinTabCtrl', function($scope, WebsocketClient) {

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
