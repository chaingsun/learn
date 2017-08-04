/**
 * 
* 工地.
 * 
 */
angular.module('app').config(function($stateProvider, $urlRouterProvider, $httpProvider) {

	$stateProvider.state('sites', {
		url : '/sites',
		views : {
			'' : {
				templateUrl : 'layout.html'
			},
			'content@sites' : {
				templateUrl : 'views/constructionsite/list.html',
				controller : function($scope, $rootScope, SweetAlert, ConstructionSiteService) {
					var loadSites = function() {
						ConstructionSiteService.findAll().then(function(sites) {
							$scope.sites = sites;
						});

					};
					loadSites();
					$scope.remove = function(site) {
						SweetAlert.swal({
							title : "确定删除?",
							type : "warning",
							showCancelButton : true,
							confirmButtonColor : "#DD6B55",
						}, function(isConfirm) {
							if (isConfirm) {
								ConstructionSiteService.remove(site.id).then(function(result) {
									if (result.success) {
										SweetAlert.success("删除成功!");
										loadSites();
									} else {
										SweetAlert.success("删除失败！", "该工地存在关联数据，不能删除.");
									}
								});
							}
						});
					};
				}
			}
		}
	}).state('sites.new', {
		url : '/new',
		views : {
			'' : {
				templateUrl : 'layout.html'
			},
			'content@sites' : {
				templateUrl : 'views/constructionsite/edit.html',
				controller : function($scope, $state, SweetAlert, DirectorService, ConstructionSiteService) {
					$scope.site = {};
					DirectorService.findAll().then(function(directors) {
						$scope.directors = directors;
					});
					$scope.doSubmit = function() {
						ConstructionSiteService.add($scope.site).then(function(result) {
							if (result.success) {
								SweetAlert.success('添加成功!');
								$state.go('sites');
							} else {
								SweetAlert.error('添加失败!', result.msg);
							}
						});
					};
				}
			}
		}
	}).state('sites.modify', {
		url : '/{id:[0-9]{1,10}}',
		views : {

			'content@sites' : {
				templateUrl : 'views/constructionsite/edit.html',
				controller : function($scope, $state, SweetAlert, DirectorService, ConstructionSiteService, $stateParams) {

					$scope.site = {};
					DirectorService.findAll().then(function(directors) {
						$scope.directors = directors;
					});
					$scope.doSubmit = function() {
						ConstructionSiteService.modify($scope.site).then(function(result) {
							if (result.success) {
								SweetAlert.success('修改成功!');
								$state.go('sites');
							} else {
								SweetAlert.error('修改失败!', result.msg);
							}
						});
					};
					ConstructionSiteService.findById($stateParams.id).then(function(site) {
						$scope.site = site;
					});
				}

			}
		}
	});

});

angular.module('app').factory('ConstructionSiteService', function($rootScope, $resource, $q) {
	return {

		findById : function(id) {
			var deferred = $q.defer();
			$resource('constructionsites/:id', {
				id : id
			}).get(function(site) {
				deferred.resolve(site);
			});
			return deferred.promise;
		},
		remove : function(id) {
			var deferred = $q.defer();
			($resource('constructionsites/:id', {
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
		modify : function(site) {
			var deferred = $q.defer();
			$resource('constructionsites', {}, {
				'update' : {
					method : 'PUT'
				}
			}).update(site, function(result) {
				deferred.resolve(result);
			});
			return deferred.promise;
		},
		add : function(site) {
			var deferred = $q.defer();
			$resource('constructionsites', {}).save(site, function(response) {
				deferred.resolve(response);
			});
			return deferred.promise;
		},
		findQrCodes : function(id) {
			var deferred = $q.defer();
			$resource('constructionsites/:id/qrcodes', {
				id : id
			}).query(function(qrCodes) {
				deferred.resolve(qrCodes);
			});
			return deferred.promise;
		},
		findAll : function() {
			var deferred = $q.defer();
			$resource('constructionsites', {}).query(function(sites) {
				deferred.resolve(sites);
			});
			return deferred.promise;
		}

	};
});