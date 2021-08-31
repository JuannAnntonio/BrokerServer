/**
 * Script for institution details
 * Created on 23/11/16.
 */

var admin_user_details = angular.module('admin-institutions', [], function () {
    }
);

admin_user_details.controller('navigation_controller', ['$scope', '$http', '$window', function ($scope, $http, $window) {
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


