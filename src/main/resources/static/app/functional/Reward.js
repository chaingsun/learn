/**
 * 
 * 物料.
 * 
 */
angular.module('app').config(function($stateProvider, $urlRouterProvider, $httpProvider) {

	$stateProvider.state('rewards', {
		url : '/rewards',
		views : {
			'' : {
				templateUrl : 'layout.html'
			},
			'content@rewards' : {
				templateUrl : 'views/reward/list.html',
				controller : function($scope, SweetAlert, $uibModal, $state, DestinationService, ConstructionSiteService, RewardService) {
					$scope.checkedDestination = {
						id : 0
					};
					$scope.checkedSite = {
						id : 0
					};
					var loadRewards = function() {
						$scope.result = null;
						if ($scope.checkedDestination.id && $scope.checkedSite.id) {
							if ($scope.rewards) {
								$scope.rewards.length = 0;
							}
							RewardService.findBySiteAndDestination($scope.checkedSite.id, $scope.checkedDestination.id).then(function(rewards) {
								for (var i = 0; i < rewards.length; i++) {
									var reward = rewards[i];
									if (reward.reward) {
										reward.reward = reward.reward / 100;
									}
								}
								$scope.rewards = rewards;
							});
						}
					};
					$scope.$watch('checkedDestination.id', function(dest) {
						loadRewards();
					});
					$scope.$watch('checkedSite.id', function(site) {
						loadRewards();
					});
					ConstructionSiteService.findAll().then(function(sites) {
						$scope.sites = sites;
					});
					DestinationService.findAll().then(function(destinations) {
						$scope.destinations = destinations;
					});
					$scope.doSubmit = function() {
						var rewards = $scope.rewards, temp = new Array;
						for (var i = 0; i < rewards.length; i++) {
							var reward = rewards[i];
							temp.push({
								id : reward.id,
								reward : reward.reward ? parseInt(reward.reward * 100) : undefined
							});
						}
						RewardService.modify(temp).then(function(result) {
							$scope.result = result;
						});
					};
					$scope.add = function() {
						openModal({});
					};
					$scope.edit = function(destination, $event) {
						openModal({
							id : destination.id,
							name : destination.name
						});
						$event.stopPropagation();
					};
					$scope.remove = function(destination, $event) {
						$event.stopPropagation();
						SweetAlert.swal({
							title : "确定删除?",
							type : "warning",
							showCancelButton : true,
							confirmButtonColor : "#DD6B55",
						}, function(isConfirm) {
							if (isConfirm) {
								DestinationService.remove(destination.id).then(function(result) {
									if (result.success) {
										$state.reload();
									} else {
										SweetAlert.success("删除失败！", "该物料存在关联数据,不能删除.");
									}
								});
							}
						});
					};
					var openModal = function(destination) {
						$uibModal.open({
							templateUrl : 'views/reward/edit.html',
							resolve : {
								destination : function() {
									return destination;
								}
							},
							controller : function($scope, destination, $uibModalInstance) {
								$scope.destination = destination;
								$scope.ok = function() {
									$uibModalInstance.close($scope.destination);
								};
								$scope.cancel = function() {
									$uibModalInstance.dismiss(false);
								};
							}
						}).result.then(function(destination) {
							if (destination) {
								DestinationService[destination.id ? 'modify' : 'add'](destination).then(function(result) {
									$state.reload();
								});
							}
						}, function() {
						});

					};

				}
			}
		}
	});

});

angular.module('app').factory('RewardService', function($rootScope, $resource, $q) {
	return {
		modify : function(rewards) {
			var deferred = $q.defer();
			$resource('rewards', {}, {
				'update' : {
					method : 'PUT'
				}
			}).update(rewards, function(result) {
				deferred.resolve(result);
			});
			return deferred.promise;
		},
		findBySiteAndDestination : function(site, destination) {
			var deferred = $q.defer();
			$resource('rewards/site/:site/destination/:destination', {
				destination : destination,
				site : site
			}).query(function(destinations) {
				deferred.resolve(destinations);
			});
			return deferred.promise;
		}
	};
});

angular.module('app').factory('DestinationService', function($rootScope, $resource, $q) {
	return {
		remove : function(id) {
			var deferred = $q.defer();
			($resource('destinations/:id', {
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
		modify : function(destination) {
			var deferred = $q.defer();
			$resource('destinations', {}, {
				'update' : {
					method : 'PUT'
				}
			}).update(destination, function(result) {
				deferred.resolve(result);
			});
			return deferred.promise;
		},
		add : function(destination) {
			var deferred = $q.defer();
			$resource('destinations', {}).save(destination, function(response) {
				deferred.resolve(response);
			});
			return deferred.promise;
		},
		findAll : function(params) {
			params = params || {};
			var deferred = $q.defer();
			$resource('destinations', params).query(function(destinations) {
				deferred.resolve(destinations);
			});
			return deferred.promise;
		}
	};
});