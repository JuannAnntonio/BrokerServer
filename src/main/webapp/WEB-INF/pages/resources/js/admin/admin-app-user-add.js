/**
 * Script for users details
 * Created on 23/11/16.
 */

var urlSave = domain + "admin/rest/saveUserInfo";
var form;

var admin_user_add = angular.module('admin-user-add', [], function () {
    form = $('#new-user-form');
});


var isPasswordCorrect = function () {
    var result = false;
    var password = form.find('#password').val();
    var password_confirm = form.find('#password-confirmation').val();
    if (password === password_confirm) {
        result = true;
    }
    return result;
}

var disable_form = function () {
    form.find(":input").prop("disabled", true);
    $("#multiselect_div").find(":button").prop("disabled", true);
    $("#edit_button").show();
    $("#save_button").hide();
}

var enable_form = function () {
    form.find(":input").prop("disabled", false);
    $("#multiselect_div").find(":button").prop("disabled", false);
    $("#edit_button").hide();
    $("#save_button").show();
}

var isUserDataCorrect = function (data) {
    var result = true;
    if (data.profile === "Trader") {
        if (!data.name || !data.email || !data.phone_number || !data.institution ||
            typeof data.active == 'undefined' ||
            data.instruments.length == 0
        ) {
            result = false;
        }
    } else {
        if (!data.name || !data.email || !data.phone_number || !data.institution ||
            typeof data.active == 'undefined'
        ) {
            result = false;
        }
    }
    if (!isPasswordCorrect()) {
        result = false;
    }
    return result;
}

var prev_name = null;

/*
 *Guarda el nuevo usuario
 * 
 */

function saveDetails() {
    var active_value = false;
    if ($('#active').is(":checked")) {
        active_value = true;
    }
    var instruments = [];
    $("#multiselect_to").find("option").each(function () {
        instruments.push(parseInt($(this).val()));
    });
    var data = {
        previous_name: prev_name,
        name: $("#username").val(),
        email: $("#email").val(),
        phone_number: $("#phone").val(),
        institution: $("#institution").val(),
        profile: $("#profile").val(),
        active: active_value,
        instruments: instruments,
        password: $('#password').val(),
        _csrf: $('input[name="_csrf"]').val()
    };
    if(data.phone_number.length < 10 || data.phone_number.length > 16){
        var error = $('#error-message-config');
        error.html("<strong>Error de telefono </strong> el número telefonico no debe ser de menos de 10 digitos y no" +
            " mayor de 14")
        error.fadeIn(400).delay(5000).fadeOut();
    }else if (!isUserDataCorrect(data)) {
        $('#warn-message').fadeIn(400).delay(5000).fadeOut();
    } else {
        $.ajax({
            type: "POST",
            url: urlSave,
            data: data,
            success: function (data) {
                if (data.code === 200) {
                    prev_name = $('input[id="username"]').val();
                    disable_form();
                    $('#success-message').fadeIn(400).delay(5000).fadeOut();
                }else if(data.code === 501 || data.code === 502){
                    var error = $('#error-message-config');
                    error.html("<strong>Error de duplicado </strong> "+data.message)
                    error.fadeIn(400).delay(5000).fadeOut();
                }
            },
            error: function () {
                $('#error-message').fadeIn(400).delay(5000).fadeOut();
            }
        })
    }
}


admin_user_add.controller('user_edit', function ($scope) {
        $scope.enableForm = function () {
            enable_form();
        }
        $scope.saveChanges = function () {
            saveDetails();
        }
    }
);

admin_user_add.controller('multiselect', [function () {
    angular.element(document).ready(function () {
        var message = $('#warn-message-password');
        var multiselect = $('#multiselect').multiselect(
            {
                keepRenderingSort: true
            }
        );
        form.find('#password').blur(function () {
            if (!isPasswordCorrect()) {
                message.fadeIn(400);
            } else {
                message.fadeOut(400);
            }
        });
        form.find('#password-confirmation').blur(function () {
            if (!isPasswordCorrect()) {
                message.fadeIn(400);
            } else {
                message.fadeOut(400);
            }
        });
        form.find('#profile').change(function () {
            if(form.find('#profile').val() === "Trader"){
                $('#multiselect_div').slideDown();
            }else{
                $('#multiselect_div').slideUp();
            }
        });
        enable_form();
    });
}]);

admin_user_add.controller('navigation_controller', ['$scope', '$http', '$window', function ($scope, $http, $window) {
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


