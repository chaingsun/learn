/**
 * 
 * 物料.
 * 
 */
angular.module('app').config(function($stateProvider, $urlRouterProvider, $httpProvider) {

	$stateProvider.state('materiels', {
		url : '/materiels',
		views : {
			'' : {
				templateUrl : 'layout.html'
			},
			'content@materiels' : {
				templateUrl : 'views/materiel/list.html',
				controller : function($scope, SweetAlert, $uibModal, $state, ConstructionSiteService, MaterielService) {

					$scope.sites = new Array;

					MaterielService.findAll().then(function(materiels) {
						$scope.materiels = materiels;
					});
					$scope.checkedMateriel = {};
					$scope.loadMaterielAmounts = function(materiel) {
						$scope.checkedMateriel = materiel || $scope.checkedMateriel;
						if ($scope.checkedMateriel) {
							ConstructionSiteService.findAll().then(function(sites) {
								$scope.sites.length = 0;
								for (var i = 0; i < sites.length; i++) {
									var site = sites[i];
									$scope.sites.push(site);
									site.currentYear = new Date().getFullYear();
									site.amounts = new Array;
									$scope.loadSiteMaterielAmounts(site, site.currentYear);
								}
							});
						}
					};
					$scope.loadSiteMaterielAmounts = function(site, year) {
						if ($scope.checkedMateriel) {
							site.amounts = new Array;
							site.currentYear = year;
							MaterielService.findByMaterielAndSiteAndYearOrderByMonthAsc($scope.checkedMateriel.id, site.id, site.currentYear).then(function(amounts) {
								site.amounts = amounts;
							});
						}
					};

					$scope.add = function() {
						openModal({});
					};
					$scope.edit = function(materiel, $event) {
						openModal({
							unit : materiel.unit,
							id : materiel.id,
							name : materiel.name
						});
						$event.stopPropagation();
					};
					$scope.remove = function(materiel, $event) {
						$event.stopPropagation();
						SweetAlert.swal({
							title : "确定删除?",
							type : "warning",
							showCancelButton : true,
							confirmButtonColor : "#DD6B55",
						}, function(isConfirm) {
							if (isConfirm) {
								MaterielService.remove(materiel.id).then(function(result) {
									if (result.success) {
										$state.reload();
									} else {
										SweetAlert.success("删除失败！", "该物料存在关联数据,不能删除.");
									}
								});
							}
						});
					};
					var openModal = function(materiel) {
						$uibModal.open({
							templateUrl : 'views/materiel/edit.html',
							resolve : {
								materiel : function() {
									return materiel;
								}
							},
							controller : function($scope, materiel, $uibModalInstance) {
								$scope.materiel = materiel;
								$scope.ok = function() {
									$uibModalInstance.close($scope.materiel);
								};
								$scope.cancel = function() {
									$uibModalInstance.dismiss(false);
								};
							}
						}).result.then(function(materiel) {
							if (materiel) {
								MaterielService[materiel.id ? 'modify' : 'add'](materiel).then(function(result) {
									$state.reload();
								});
							}
						}, function() {
						});

					};

					$scope.doSubmit = function(site) {
						MaterielService.modifyMaterielAmounts(site.amounts).then(function(result) {
							if (result.success) {
								SweetAlert.success('操作成功.', '修改工地[' + site.name + ']物料[' + $scope.checkedMateriel.name + ']' + site.currentYear + '的各个月数据量成功.');
							} else {

							}
						});
					};

				}
			}
		}
	});

});

angular.module('app').factory('MaterielService', function($rootScope, $resource, $q) {
	return {

		modifyMaterielAmounts : function(materielAmounts) {
			var deferred = $q.defer();
			$resource('materielamounts', {}, {
				'update' : {
					method : 'PUT'
				}
			}).update(materielAmounts, function(result) {
				deferred.resolve(result);
			});
			return deferred.promise;
		},
		findByMaterielAndSiteAndYearOrderByMonthAsc : function(id, site, year) {
			var deferred = $q.defer();
			$resource('materiels/:id/site/:site/year/:year/amounts', {
				id : id,
				site : site,
				year : year
			}).query(function(amounts) {
				deferred.resolve(amounts);
			});
			return deferred.promise;
		},
		remove : function(id) {
			var deferred = $q.defer();
			($resource('materiels/:id', {
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
		modify : function(materiel) {
			var deferred = $q.defer();
			$resource('materiels', {}, {
				'update' : {
					method : 'PUT'
				}
			}).update(materiel, function(result) {
				deferred.resolve(result);
			});
			return deferred.promise;
		},
		add : function(materiel) {
			var deferred = $q.defer();
			$resource('materiels', {}).save(materiel, function(response) {
				deferred.resolve(response);
			});
			return deferred.promise;
		},
		findAll : function(params) {
			params = params || {};
			var deferred = $q.defer();
			$resource('materiels', params).query(function(materiels) {
				deferred.resolve(materiels);
			});
			return deferred.promise;
		}

	};
});