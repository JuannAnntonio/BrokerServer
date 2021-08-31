/**
 * Script for institution details
 * Created on 23/11/16.
 */

var urlSave = domain + "admin/rest/saveInstitutionInfo";

var admin_user_details = angular.module('admin-institution-details', [], function () {
    }
);

var disableForm = function () {
    $("#institution-detail-form").find(":input").prop("disabled", true);
    $("#multiselect_div").find(":button").prop("disabled", true);
    $("#edit_button").show();
    $("#save_button").hide();
}

var lEnableForm = function () {
    $("#institution-detail-form").find(":input").prop("disabled", false);
    $("#multiselect_div").find(":button").prop("disabled", false);
    $("#edit_button").hide();
    $("#save_button").show();
}

var prev_name = null;

function saveDetails() {
    var active_value = false;
    if ($('#active').is(":checked")) {
        active_value = true;
    }
    var workbenches = new Array();
    $("#multiselect_to").find("option").each(function()
    {
        workbenches.push(parseInt($(this).val()));
    });
    var nivelFueraMercado = document.getElementById('nivelFueraMercado').value;
    var radios = document.getElementsByName('radios');
    var opcion = 0;
    for (var i = 0, length = radios.length; i < length; i++) {
        if (radios[i].checked) {
            opcion = radios[i].value;
            break;
        }
    }
    var data = {
        previous_name: prev_name,
        name: $("#name").val(),
        phone_number: $("#phone").val(),
        key: $("#key").val(),
        system_commission: $("#system_commission").val(),
        active: active_value,
        workbenches: workbenches,
        nivelFueraMercado: nivelFueraMercado,
        opcionRadio: opcion,
        _csrf: $('input[name="_csrf"]').val()
    }
    $.ajax({
        type: "POST",
        url: urlSave,
        data: data,
        success: function (data) {
            if (data) {
                disableForm();
                prev_name = $('input[id="name"]').val();
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert("Error: "+errorThrown+", text: "+textStatus+"jqXHR: "+jqXHR);
        }
    })
}


admin_user_details.controller('institution_edit', function ($scope) {
        $scope.enableForm = function () {
            prev_name = $('input[id="name"]').val();
            lEnableForm();
        }
        $scope.saveChanges = function () {
            saveDetails();
        }

    }
);

admin_user_details.controller('multiselect', [function() {
    angular.element(document).ready(function () {
        var multiselect = $('#multiselect').multiselect(
            {
                keepRenderingSort: true
            }
        );

    });
}]);

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

