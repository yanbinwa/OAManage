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
    })
    .state('app.main.addEmployee', {
      url: "/addEmployee",
      views: {
        'addEmployee-tab': {
          templateUrl: "templates/addEmployee.html",
          controller: 'AddEmployeeTabCtrl'
        }
      }
    })
    .state('app.main.listEmployee', {
      url: "/listEmployee",
      views: {
        'listEmployee-tab': {
          templateUrl: "templates/listEmployee.html",
          controller: 'ListEmployeeTabCtrl'
        }
      }
    })
    .state('app.main.updataInfo', {
      url: "/updataInfo",
      views: {
        'updataInfo-tab': {
          templateUrl: "templates/updataInfo.html",
          controller: 'UpdataInfoTabCtrl'
        }
      }
    })
    .state('app.main.userEmployee', {
      url: "/userEmployee",
      views: {
        'userEmployee-tab': {
          templateUrl: "templates/userEmployee.html",
          controller: 'UserEmployeeTabCtrl'
        }
      }
    })
    .state('app.main.userStore', {
      url: "/userStore",
      views: {
        'userStore-tab': {
          templateUrl: "templates/userStore.html",
          controller: 'UserStoreTabCtrl'
        }
      }
    })
    .state('employeeLogin', {
      url: "/employeeLogin",
      templateUrl: "templates/employeeLogin.html",	
      controller: 'EmployeeLoginTabCtrl'
    })
    .state('employeeSign', {
      url: "/employeeSign",
      templateUrl: "templates/employeeSign.html",	
      controller: 'EmployeeSignTabCtrl'
    })
    .state('storeSign', {
      url: "/storeSign",
      templateUrl: "templates/storeSign.html",	
      controller: 'StoreSignTabCtrl'
    });
  
  $urlRouterProvider.otherwise("/employeeLogin");
  
});