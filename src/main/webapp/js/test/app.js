angular.module('todo', ['ionic'])

.controller('TodoCtrl', function($scope, $ionicModal) {
	$scope.tasks = [
		{ title: '菜鸟教程' },
    	{ title: 'www.runoob.com' },
    	{ title: '菜鸟教程' },
    	{ title: 'www.runoob.com' }
	];

	//ionicModal is pop-ups window
	$ionicModal.fromTemplateUrl('new-task.html', function(modal) {
		$scope.taskModal = modal;
	}, {
		scope: $scope,
		animation: 'slide-in-up'
	});

	$scope.newTask = function() {
		$scope.taskModal.show();
	};

	$scope.createTask = function(task) {
		$scope.tasks.push({
			title : task.title
		});
		$scope.taskModal.hide();
		task.title = "";
	};

	$scope.closeNewTask = function() {
		$scope.taskModal.hide();
	};

});