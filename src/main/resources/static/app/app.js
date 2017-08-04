(function() {
	angular.module('app', [ 'ui.router', 'ngSanitize', 'ngAnimate', 'ui.bootstrap', 'ui.bootstrap.tpls', 'ui.select', 'ui.bootstrap.pagination', 'ngResource' ]).run(
			function($rootScope, $templateCache, $state, $http, $stateParams) {
				$rootScope.$state = $state;
				$rootScope.Behaviors = {
					IN : '进场',
					DISCHARGE : '卸货',
					OUT : '出场'
				};
				$rootScope._self = angular.copy(_self);
				_self = null;
				$rootScope.$stateParams = $stateParams;
				$templateCache.put("layout.html", '<div ui-view="content"></div>');
				$rootScope.dateOptions = {
					formatYear : 'yy',
					startingDay : 1
				};
				$rootScope.logout = function() {
					$http({
						url : 'ops/logout',
						method : 'GET'
					}).then(function() {
						window.location = 'login.html';
					});
				};
			});
	angular.module('app').filter('propsFilter', function() {
		return function(items, props) {
			var out = [];

			if (angular.isArray(items)) {
				var keys = Object.keys(props);

				items.forEach(function(item) {
					var itemMatches = false;

					for (var i = 0; i < keys.length; i++) {
						var prop = keys[i];
						var text = props[prop].toLowerCase();
						if (item[prop].toString().toLowerCase().indexOf(text) !== -1) {
							itemMatches = true;
							break;
						}
					}

					if (itemMatches) {
						out.push(item);
					}
				});
			} else {
				out = items;
			}

			return out;
		};
	});
})();
