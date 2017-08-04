/**
 * 
 */
angular.module('app').config(function($stateProvider, $urlRouterProvider, $compileProvider) {
	$compileProvider.debugInfoEnabled(true);
	$urlRouterProvider.otherwise('/statistics');
	$stateProvider.state('statistics', {
		url : '/statistics',
		views : {
			'' : {
				template : '<div class="content" ui-view="content"></div>'
			},
			'content@statistics' : {
				templateUrl : 'views/statistics.html',
				controller : 'StatisticCtrl'
			}
		}
	});
}).controller('StatisticCtrl', function($scope, MaterielService, TransportService, ConstructionSiteService, DriverService, $stateParams) {
	if ($stateParams.id) {
		DriverService.findById($stateParams.id).then(function(driver) {
			$scope.driver = driver;
		});
	}
	$scope.sites = new Array;
	$scope.materiels = new Array;
	ConstructionSiteService.findAll().then(function(sites) {
		$scope.sites = sites;
	});
	MaterielService.findAll().then(function(materiels) {
		$scope.materiels = materiels;
	});
	var times = new Array;
	$scope.statistic = {};

	$scope.groupByYear = function(item) {
		return item.year + '年';
	};
	for (var i = 0; i < 24; i++) {
		var now = new Date();
		now.setMonth(now.getMonth() - i);
		times.push({
			year : now.getFullYear(),
			month : now.getMonth() + 1
		});
	}
	$scope.times = times;
	var getMateriel = function(id) {
		var materiels = $scope.materiels;
		for (var i = 0; i < materiels.length; i++) {
			var materiel = materiels[i];
			if (materiel.id == id) {
				return materiel;
			}
		}
	};

	var loadStatistics = function(times) {
		if (times && times.length) {
			$scope.loading = true;
			var years = new Array, months = new Array;
			for (var i = 0; i < times.length; i++) {
				var time = times[i];
				years.push(time.year);
				months.push(time.month);
			}
			TransportService.findStatisticsByYearsAndMonths(years, months,$stateParams.id).then(function(statistics) {
				for (var i = 0; i < statistics.length; i++) {
					var statistic = statistics[i], totalPrice = statistic.totalPrice = ((statistic.totalPrice || 0) / 100), tranTimes = statistic.tranTimes, totalAmount = statistic.totalAmount;
					statistic.display = isDisplay(statistic);
					if (totalPrice) {
						statistic.avgPrice = totalPrice / tranTimes;
					}
					if (totalAmount) {
						statistic.avgAmount = totalAmount / tranTimes;
					}
				}
				$scope.statistics = statistics;
				$scope.loading = false;

			});
		} else {
			$scope.statistics = new Array;
		}

	};
	function isDisplay(statistic) {
		var sites = $scope.statistic.sites || [], materiels = $scope.statistic.materiels || [];
		var siteIndex = sites.length ? -1 : 0;
		for (var i = 0; i < sites.length; i++) {
			if (sites[i].id === statistic.site.id) {
				siteIndex = i;
				break;
			}
		}
		if (siteIndex == -1) {
			return false;
		}
		var materielIndex = materiels.length ? -1 : 0;
		for (var i = 0; i < materiels.length; i++) {
			if (materiels[i].id === statistic.materiel.id) {
				materielIndex = i;
				break;
			}
		}
		if (materielIndex == -1) {
			return false;
		}

		return true;
	}

	function filter() {
		var statistics = $scope.statistics || [];
		for (var i = 0; i < statistics.length; i++) {
			statistics[i].display = isDisplay(statistics[i]);
		}
	}

	$scope.$watch('statistic.materiels', function() {
		filter();
	}, true);
	$scope.$watch('statistic.sites', function() {
		filter();
	}, true);
	$scope.$watch('statistic.times', function(times, oldValue) {
		loadStatistics(times);
	}, true);

});