/**
 * 
 * 运输记录.
 * 
 */
angular.module('app').config(function($stateProvider, $urlRouterProvider, $httpProvider) {

	$stateProvider.state('transports', {
		url : '/transports',
		views : {
			'' : {
				templateUrl : 'layout.html'
			},
			'content@transports' : {
				templateUrl : 'views/transport/list.html',
				controller : function($scope, $filter, ConstructionSiteService, TransportService) {
					ConstructionSiteService.findAll().then(function(sites) {
						$scope.sites = sites;
					});
					$scope.open = function($event) {
						$event.preventDefault();
						$event.stopPropagation();
						$scope.opened = true;
					};
					$scope.example = {
						constructionSite : {},
						driver : {}
					};

					$scope.$watch('example.time', function(newTime, oldTime) {
						if (!newTime && !oldTime) {

						} else {
							$scope.loadTransports();
						}
					});
					$scope.loadTransports = function(page) {
						var example = $scope.example;
						$scope.page = page || $scope.page || 1;
						var dateFormatter = $filter('date');
						TransportService.findByPage({
							page : ($scope.page - 1),
							'constructionSite.id' : (example.constructionSite || {}).id,
							time : example.time ? dateFormatter(example.time, 'yyyy-MM-dd') : undefined,
							'driver.name' : example.driver.name
						}).then(function(result) {
							$scope.totalElements = result.totalElements;
							$scope.transports = result.content;
						});
					};
					$scope.loadTransports();
				}
			}
		}
	}).state('transports.view', {
		url : '/{id:[0-9]{1,10}}/view',
		views : {
			'content@transports' : {
				templateUrl : 'views/transport/view.html',
				controller : function($scope, $state, SweetAlert, $timeout, DestinationService, TransportService, $stateParams) {
					var id = $stateParams.id;
					$scope.transport = {};
					$scope.destinate = function() {
						var transport = $scope.transport, destination = (transport || {}).destination;
						if (transport.id && destination) {
							TransportService.destinate(transport.id, destination.id).then(function(result) {
								$scope.result = result;
								transport.reward = result.data;
								$timeout(function() {
									$scope.result = {};
								}, 1000);

							});
						}
					};
					DestinationService.findAll().then(function(destinations) {
						$scope.destinations = destinations;
					});
					TransportService.findById(id).then(function(transport) {
						$scope.transport = transport;
					});
					TransportService.findBehaviors(id).then(function(behaviors) {
						$scope.behaviors = behaviors;
					});

				}

			}
		}
	});

});

angular.module('app').factory('TransportService', function($rootScope, $resource, $q) {
	return {
		destinate : function(id, destination) {
			var deferred = $q.defer();
			$resource('transports/:id/destination/:destination', {
				destination : destination,
				id : id
			}, {
				'update' : {
					method : 'PUT'
				}
			}).update(destination, function(result) {
				deferred.resolve(result);
			});
			return deferred.promise;
		},
		findBehaviors : function(id) {
			var deferred = $q.defer();
			$resource('transports/:id/behaviors', {
				id : id
			}).query(function(behaviors) {
				deferred.resolve(behaviors);
			});
			return deferred.promise;
		},
		findById : function(id) {
			var deferred = $q.defer();
			$resource('transports/:id', {
				id : id
			}).get(function(transport) {
				deferred.resolve(transport);
			});
			return deferred.promise;
		},

		findStatisticsByYearsAndMonths : function(years, months, driver) {
			var url = 'transports/statistics?t=' + (+new Date());
			for (var i = 0; i < months.length; i++) {
				url += "&months=" + months[i];
			}
			for (var i = 0; i < years.length; i++) {
				url += "&years=" + years[i];
			}
			if (driver) {
				url += "&driver=" + driver;
			}
			var deferred = $q.defer();
			$resource(url, {}).query(function(result) {
				deferred.resolve(result);
			});
			return deferred.promise;

		},
		findByPage : function(params) {
			params = params || {};
			var deferred = $q.defer();
			$resource('transports/page', params).get(function(result) {
				deferred.resolve(result);
			});
			return deferred.promise;

		}

	};
});