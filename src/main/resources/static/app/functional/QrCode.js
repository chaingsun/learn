/**
 * 
 * 工地.
 * 
 */
angular.module('app').config(function($stateProvider, $urlRouterProvider, $httpProvider) {

	$stateProvider.state('qrcodes', {
		url : '/qrcodes',
		views : {
			'' : {
				templateUrl : 'layout.html'
			},
			'content@qrcodes' : {
				templateUrl : 'views/qrcode/list.html',
				controller : function($scope, $rootScope, SweetAlert, ConstructionSiteService, ConstructionSiteQrCodeService) {
					$scope.checkedSite = {};
					$scope.loadQrCodes = function(site) {
						$scope.checkedSite = site;
						ConstructionSiteService.findQrCodes(site.id).then(function(qrCodes) {
							$scope.qrCodes = qrCodes;
						});
					};
					ConstructionSiteService.findAll().then(function(sites) {
						$scope.sites = sites;
						$scope.loadQrCodes(sites[0]);

					});
					$scope.remove = function(qrCode) {
						ConstructionSiteQrCodeService.remove(qrCode.id).then(function() {
							$scope.loadQrCodes($scope.checkedSite);
						});
					};
					$scope.add = function() {
						ConstructionSiteQrCodeService.add({
							constructionSite : $scope.checkedSite
						}).then(function() {
							$scope.loadQrCodes($scope.checkedSite);
						});
					};
				}
			}
		}
	});

});

angular.module('app').factory('ConstructionSiteQrCodeService', function($rootScope, $resource, $q) {
	return {

		remove : function(id) {
			var deferred = $q.defer();
			($resource('qrcodes/:id', {
				id : id
			}, {
				'delete' : {
					method : 'DELETE'
				}
			})['delete'])(function(response) {
				deferred.resolve(response);
			});
			return deferred.promise;
		},
		add : function(qrCode) {
			var deferred = $q.defer();
			$resource('qrcodes', {}).save(qrCode, function(response) {
				deferred.resolve(response);
			});
			return deferred.promise;

		}
	};
});