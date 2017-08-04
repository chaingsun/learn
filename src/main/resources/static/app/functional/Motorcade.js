/**
 * 
 * 司机.
 * 
 */
angular.module('app').config(function($stateProvider, $urlRouterProvider, $httpProvider) {

	$stateProvider.state('motorcades', {
		url : '/motorcades',
		views : {
			'' : {
				templateUrl : 'views/motorcade/layout.html',
				controller : function($scope, $rootScope, SweetAlert, $state, $uibModal, MotorcadeService) {

					var loadMotorcades = function() {
						MotorcadeService.findAll().then(function(motorcades) {
							$scope.motorcades = motorcades;
						});
					};
					loadMotorcades();
					var openModal = function(motorcade) {
						$uibModal.open({
							templateUrl : 'views/motorcade/edit.html',
							resolve : {
								motorcade : function() {
									return motorcade;
								}
							},
							controller : function($scope, motorcade, $uibModalInstance) {
								console.log(motorcade);
								$scope.motorcade = motorcade;
								$scope.ok = function() {
									$uibModalInstance.close($scope.motorcade);
								};

								$scope.cancel = function() {
									$uibModalInstance.dismiss(false);
								};
							}
						}).result.then(function(motorcade) {
							if (motorcade) {
								MotorcadeService[motorcade.id ? 'modify' : 'add'](motorcade).then(function(result) {
									$state.reload();
								});
							}
						}, function() {
						});

					};
					$scope.edit = function(motorcade, $event) {
						openModal({
							assign : motorcade.assign,
							id : motorcade.id,
							name : motorcade.name
						});
						$event.stopPropagation();
					};
					$scope.remove = function(motorcade, $event) {
						$event.stopPropagation();
						SweetAlert.swal({
							title : "确定删除?",
							type : "warning",
							showCancelButton : true,
							confirmButtonColor : "#DD6B55",
						}, function(isConfirm) {
							if (isConfirm) {
								MotorcadeService.remove(motorcade.id).then(function(result) {
									if (result.success) {
										$state.go('motorcades');
										loadMotorcades();
									} else {
										SweetAlert.success("删除失败！", "该车队存在关联数据,不能删除.");
									}
								});
							}
						});
					};
					$scope.add = function() {
						openModal({
							assign : false
						});
					};

				}
			}
		}
	}).state('motorcades.drivers', {
		url : '/{motorcadeId:[0-9]{1,10}}/drivers',
		views : {
			'driver' : {
				templateUrl : 'views/motorcade/driver/list.html',
				controller : function($scope, $rootScope, SweetAlert, $stateParams, $uibModal, DriverService, MotorcadeService) {
					MotorcadeService.findById($stateParams.motorcadeId).then(function(motorcade) {
						$scope.motorcade = motorcade;
					});
					var loadDrivers = function() {
						MotorcadeService.findDrivers($stateParams.motorcadeId).then(function(drivers) {
							$scope.drivers = drivers;
						});
					};
					loadDrivers();

					$scope.remove = function(driver) {
						SweetAlert.swal({
							title : "确定删除?",
							type : "warning",
							showCancelButton : true,
							confirmButtonColor : "#DD6B55",
						}, function(isConfirm) {
							if (isConfirm) {
								DriverService.remove(driver.id).then(function(result) {
									if (result.success) {
										SweetAlert.success("删除成功!");
										loadDrivers();
									} else {
										SweetAlert.success("删除失败！", "该司机存在关联数据,不能删除.");
									}
								});
							}
						});
					};

				}
			}
		}
	}).state('motorcades.drivers.new', {
		url : '/new',
		views : {
			'driver@motorcades' : {
				templateUrl : 'views/motorcade/driver/edit.html',
				controller : function($scope, $rootScope, SweetAlert, $state, $stateParams, DriverService, MotorcadeService) {
					$scope.driver = {
						captain : false,
						status : 1,
						motorcade : {
							id : $stateParams.motorcadeId
						}
					};
					MotorcadeService.findById($stateParams.motorcadeId).then(function(motorcade) {
						$scope.driver.motorcade.name = motorcade.name;
					});
					$scope.doSubmit = function() {
						var driver = $scope.driver;
						DriverService.add(driver).then(function(result) {
							if (result.success) {
								SweetAlert.success('添加成功!');
								$state.go('motorcades.drivers');
							} else {
								SweetAlert.error('添加失败!', result.errorMsg);
							}
						});

					};
				}
			}
		}
	}).state('motorcades.drivers.modify', {
		url : '/{id:[0-9]{1,10}}',
		views : {
			'driver@motorcades' : {
				templateUrl : 'views/motorcade/driver/edit.html',
				controller : function($scope, $rootScope, SweetAlert, $stateParams, $state, DriverService, MotorcadeService) {
					DriverService.findById($stateParams.id).then(function(driver) {
						driver.confirmPwd = driver.pwd;
						$scope.driver = driver;
					});
					$scope.doSubmit = function() {
						var driver = $scope.driver;
						if (driver.pwd != driver.confirmPwd) {
							SweetAlert.error('输入错误', '两次输入密码不一致.');
						} else {
							DriverService.modify(driver).then(function(result) {
								if (result.success) {
									SweetAlert.success('修改成功!');
									$state.go('motorcades.drivers');
								} else {
									SweetAlert.error('修改失败!', result.errorMsg);
								}
							});
						}

					};
				}
			}
		}
	}).state('motorcades.drivers.statistics', {
		url : '/{id:[0-9]{1,10}}/statistics',
		views : {
			'driver@motorcades' : {
				templateUrl : 'views/statistics.html',
				controller : 'StatisticCtrl'
			}
		}
	});

});

angular.module('app').factory('DriverService', function($rootScope, $resource, $q) {
	return {
		findById : function(id) {
			var deferred = $q.defer();
			$resource('drivers/:id', {
				id : id
			}).get(function(motorcade) {
				deferred.resolve(motorcade);
			});
			return deferred.promise;
		},
		remove : function(id) {
			var deferred = $q.defer();
			($resource('drivers/:id', {
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
		modify : function(driver) {
			var deferred = $q.defer();
			$resource('drivers', {}, {
				'update' : {
					method : 'PUT'
				}
			}).update(driver, function(result) {
				deferred.resolve(result);
			});
			return deferred.promise;
		},
		add : function(driver) {
			var deferred = $q.defer();
			$resource('drivers', {}).save(driver, function(response) {
				deferred.resolve(response);
			});
			return deferred.promise;
		}
	};
});

angular.module('app').factory('MotorcadeService', function($rootScope, $resource, $q) {
	return {
		findDrivers : function(id) {
			var deferred = $q.defer();
			$resource('motorcades/:id/drivers', {
				id : id
			}).query(function(drivers) {
				deferred.resolve(drivers);
			});
			return deferred.promise;
		},
		findById : function(id) {
			var deferred = $q.defer();
			$resource('motorcades/:id', {
				id : id
			}).get(function(motorcade) {
				deferred.resolve(motorcade);
			});
			return deferred.promise;
		},
		remove : function(id) {
			var deferred = $q.defer();
			($resource('motorcades/:id', {
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
		modify : function(motorcade) {
			var deferred = $q.defer();
			$resource('motorcades', {}, {
				'update' : {
					method : 'PUT'
				}
			}).update(motorcade, function(result) {
				deferred.resolve(result);
			});
			return deferred.promise;
		},
		add : function(motorcade) {
			var deferred = $q.defer();
			$resource('motorcades', {}).save(motorcade, function(response) {
				deferred.resolve(response);
			});
			return deferred.promise;
		},
		findAll : function() {
			var deferred = $q.defer();
			$resource('motorcades', {}).query(function(motorcades) {
				deferred.resolve(motorcades);
			});
			return deferred.promise;
		}
	};
});