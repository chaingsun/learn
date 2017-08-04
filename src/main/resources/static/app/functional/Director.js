/**
 * 
 * 工地.
 * 
 */
angular.module('app').config(function($stateProvider, $urlRouterProvider, $httpProvider) {

	$stateProvider.state('directors', {
		url : '/directors',
		views : {
			'' : {
				templateUrl : 'layout.html'
			},
			'content@directors' : {
				templateUrl : 'views/director/list.html',
				controller : function($scope, $rootScope, SweetAlert, DirectorService) {
					$scope.loadDirectors = function(page) {
						$scope.page = page || $scope.page || 1;
						DirectorService.findByPage({
							page : ($scope.page - 1),
							keyWord : $scope.keyWord
						}).then(function(result) {
							$scope.totalElements = result.totalElements;
							$scope.directors = result.content;
						});
					};
					$scope.loadDirectors();
					$scope.remove = function(director) {
						SweetAlert.swal({
							title : "确定删除?",
							type : "warning",
							showCancelButton : true,
							confirmButtonColor : "#DD6B55",
						}, function(isConfirm) {
							if (isConfirm) {
								DirectorService.remove(director.id).then(function(result) {
									if (result.success) {
										SweetAlert.success("删除成功!");
										$scope.loadDirectors();
									} else {
										SweetAlert.success("删除失败！", "该主管存在关联数据,不能删除.");
									}
								});
							}
						});
					};
				}
			}
		}
	}).state('directors.new', {
		url : '/new',
		views : {
			'' : {
				templateUrl : 'layout.html'
			},
			'content@directors' : {
				templateUrl : 'views/director/edit.html',
				controller : function($scope, $state, SweetAlert, DirectorService) {
					$scope.director = {
						status : 0
					};
					$scope.doSubmit = function() {
						var director = $scope.director;
						DirectorService.add(director).then(function(result) {
							if (result.success) {
								SweetAlert.success('添加成功!');
								$state.go('directors');
							} else {
								SweetAlert.error('添加失败!', result.errorMsg);
							}
						});
					

					};
				}
			}
		}
	}).state('directors.modify', {
		url : '/{id:[0-9]{1,10}}',
		views : {
			'content@directors' : {
				templateUrl : 'views/director/edit.html',
				controller : function($scope, $state, SweetAlert, DirectorService, $stateParams) {
					DirectorService.findById($stateParams.id).then(function(director) {
						director.confirmPwd = director.pwd;
						$scope.director = director;
					});

					$scope.doSubmit = function() {
						var director = $scope.director;
						if (director.pwd != director.confirmPwd) {
							SweetAlert.error('输入错误', '两次输入密码不一致.');
						} else {
							DirectorService.modify(director).then(function(result) {
								if (result.success) {
									SweetAlert.success('修改成功!');
									$state.go('directors');
								} else {
									SweetAlert.error('修改失败!', result.errorMsg);
								}
							});
						}

					};

				}

			}
		}
	});

});

angular.module('app').factory('DirectorService', function($rootScope, $resource, $q) {
	return {

		findAll : function() {
			var deferred = $q.defer();
			$resource('directors', {}).query(function(directors) {
				deferred.resolve(directors);
			});
			return deferred.promise;
		},
		findById : function(id) {
			var deferred = $q.defer();
			$resource('directors/:id', {
				id : id
			}).get(function(director) {
				deferred.resolve(director);
			});
			return deferred.promise;
		},
		remove : function(id) {
			var deferred = $q.defer();
			($resource('directors/:id', {
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
			$resource('directors', {}, {
				'update' : {
					method : 'PUT'
				}
			}).update(site, function(result) {
				deferred.resolve(result);
			});
			return deferred.promise;
		},
		add : function(director) {
			var deferred = $q.defer();
			$resource('directors', {}).save(director, function(response) {
				deferred.resolve(response);
			});
			return deferred.promise;
		},
		findByPage : function(params) {
			params = params || {};
			var deferred = $q.defer();
			$resource('directors/page', params).get(function(result) {
				deferred.resolve(result);
			});
			return deferred.promise;

		}

	};
});