angular.module('OAManage', ['ionic', 'OAManage.controllers', 'OAManage.services'])

.config(function($stateProvider, $urlRouterProvider) {
	$stateProvider
		.state('app', {
			url: "/app",
			abstract: true,
			templateUrl: "templates/app.html"
		})
		.state('app.main', {
			url: "/main",
			Views: {
				'main': {
					templateUrl: "templates/main.html"
				}
			}
		})
		.state('app.home', {
			url: "/home",
			Views: {
				'home-tab': {
					templateUrl: "templates/home.html",
					controller: "HomeTabCtrl"
				}
			}
		})
		.state('app.checkin', {
			url: "/checkin",
			Views: {
				'checkin-tab': {
					templateUrl: "templates/checkin.html",
					controller: 'CheckinTabCtrl'
				}
			}
		});

	$urlRouterProvider.otherwise("/app/main/home");
});