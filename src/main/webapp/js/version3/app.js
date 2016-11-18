angular.module('ionicApp', ['ionic', 'ionicApp.controllers', 'ionicApp.services'])

.config(function($stateProvider, $urlRouterProvider) {

  $stateProvider
    .state('app', {
      url: "/app",
      abstract: true,
      templateUrl: "templates/app.html"
    })
    .state('app.main', {
      url: "/main",
      views:{
        'main':{
          templateUrl: "templates/main.html"
        }
      }
    })
    .state('app.main.home', {
      url: "/home",
      views: {
        'home-tab': {
          templateUrl: "templates/home.html",
          controller: "HomeTabCtrl"
        }
      }
    })
   .state('app.main.checkin', {
      url: "/checkin",
      views: {
        'checkin-tab': {
          templateUrl: "templates/checkin.html",
          controller: 'CheckinTabCtrl'
        }
      }
    });
  
  $urlRouterProvider.otherwise("/app/main/home");
});