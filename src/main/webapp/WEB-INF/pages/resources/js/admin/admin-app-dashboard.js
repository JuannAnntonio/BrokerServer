/**
 * Created on 16/11/16.
 */

var url = domain + "admin/rest/getDashboardInfo";

var admin_dashboard = angular.module('admin_dashboard', ['angular.morris']);
admin_dashboard.controller('graph_controller', function ($scope, $http) {
    $http.get(url);
    $.getJSON(url, function (data) {
            $scope.lastQuarterTradedAmount = data.lastQuarterTradedAmount;
            $scope.lastQuarterActivityStandings = data.lastQuarterActivityStandings;
        });
    }
);

admin_dashboard.controller('navigation_controller', ['$scope', '$http', '$window', function ($scope, $http, $window) {
    var data = {
        _csrf: $('input[name="_csrf"]').val()
    }
    $scope.logout = function () {
        $http({
            method: "POST",
            url: urlLogout,
            params: data
        }).success(function () {
            $window.location.replace(domain);
        }).error(function (data, status) {
            alert("Error in logout status: " + status + " data:" + JSON.stringify(data));
        })
    }
}]);


