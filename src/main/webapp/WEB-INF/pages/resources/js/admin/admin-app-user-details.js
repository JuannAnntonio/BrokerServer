/**
 * Script for users details
 * Created on 23/11/16.
 */

var urlSave = domain + "admin/rest/saveUserInfo";
var urlUpdatePassword = domain + "admin/rest/updatePassword";
var form;
var form_password;
var prev_name = null;

var admin_user_details = angular.module('admin-user-details', [], function () {
        form = $('#user-detail-form');
        form_password = $('#user-password-form');
        prev_name = $('input[id="username"]').val();
    }
);

var showPasswordForm = function () {
    $('#user-details-panel').slideUp(400);
    $('#user-change-password-panel').delay(400).slideDown();
    $('#edit_button').hide();
    $('#save_button').hide();
    $('#edit_password').hide();
    $('#save_password').show();
    $('#cancel_update_password').show();

};

var hidePasswordForm = function () {
    $('#user-change-password-panel').slideUp(400);
    $('#user-details-panel').delay(400).slideDown();
    $('#edit_button').show();
    $('#save_button').hide();
    $('#edit_password').show();
    $('#save_password').hide();
    $('#cancel_update_password').hide();
};

var isPasswordCorrect = function () {
    var result = false;
    var new_password = form_password.find('#type-new-password').val();
    var new_passord_retype = form_password.find('#retype-new-password').val();
    if (new_password === new_passord_retype) {
        result = true;
    }
    return result;
};

var disable_form = function () {
    form.find(":input").prop("disabled", true);
    $("#multiselect_div").find(":button").prop("disabled", true);
    $("#edit_button").show();
    $("#save_button").hide();
};

var enable_form = function () {
    form.find(":input").prop("disabled", false);
    $("#multiselect_div").find(":button").prop("disabled", false);
    $("#edit_button").hide();
    $("#save_button").show();
};

function isUserDataCorrect(data) {
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
    return result;
};

/*
 * Guarda los cambios de los detalles del usuario.
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
        _csrf: $('input[name="_csrf"]').val()
    };
    if(data.phone_number.length < 10 || data.phone_number.length > 16){
        var warn = $('#warn-message-config');
        warn.html("<strong>Error de telefono </strong> el número telefonico no debe ser de menos de 10 digitos y no" +
            " mayor de 14")
        warn.fadeIn(400).delay(5000).fadeOut();
    }else if (!isUserDataCorrect(data)) {
        $('#warn-message').fadeIn(400).delay(5000).fadeOut();
    } else {
        $.ajax({
            type: "POST",
            url: urlSave,
            data: data,
            success: function (data) {
                if (data) {
                    if (data.code === 200) {
                        prev_name = $('input[id="username"]').val();
                        disable_form();
                        $('#success-message').fadeIn(400).delay(5000).fadeOut();
                    }else if(data.code === 501 || data.code === 502){
                        var warn = $('#warn-message-config');
                        warn.html("<strong>Error de duplicado </strong> "+data.message)
                        warn.fadeIn(400).delay(5000).fadeOut();
                    }
                }
            },
            error: function () {
                $('#error-message').fadeIn(400).delay(5000).fadeOut();
            }
        })
    }
}

var updatePassword = function () {
    if (!$("#type-new-password").val()) {
        $('#warn-message-password').fadeIn(400).delay(5000).fadeOut();
        return;
    }
    if (isPasswordCorrect()) {
        var data = {
            username: prev_name,
            old_password: $("#type-current-password").val(),
            new_password: $("#type-new-password").val(),
            _csrf: $('input[name="_csrf"]').val()
        };
        $.ajax({
            type: "POST",
            url: urlUpdatePassword,
            data: data,
            success: function (data) {
                if (data) {
                    if (data.code === 200) {
                        $('#success-message-password').fadeIn(400).delay(5000).fadeOut();
                        hidePasswordForm();
                    } else if (data.code === 500) {
                        $('#warn-message-old-password').fadeIn(400).delay(5000).fadeOut();
                    }else if(data.code === 502 || data.code === 501){
                        var message = $('#warn-message-password-config');
                        message.html('<strong>Error</strong> '+data.message);
                        message.fadeIn(400).delay(5000).fadeOut();
                    }
                }
            },
            error: function () {
                $('#error-message-password').fadeIn(400).delay(5000).fadeOut();
            }
        })
    }
}


/*
 * Controllers
 *
 *
 * */

admin_user_details.controller('user_edit', function ($scope) {
        $scope.enableForm = function () {
            enable_form();
        };
        $scope.saveChanges = function () {
            saveDetails();
        };
        $scope.changePassword = function () {
            showPasswordForm();
        };
        $scope.saveChangesPassword = function () {
            updatePassword();
        };
        $scope.cancelChangePassword = function () {
            hidePasswordForm()
        };
    }
);

admin_user_details.controller('multiselect', [function () {
    angular.element(document).ready(function () {
        var message = $('#warn-message-password-correct');
        $('#multiselect').multiselect(
            {
                keepRenderingSort: true
            }
        );
        form_password.find('#type-new-password').blur(function () {
            if (!isPasswordCorrect()) {
                message.fadeIn(400);
            } else {
                message.fadeOut(400);
            }
        });
        form_password.find('#retype-new-password').blur(function () {
            if (!isPasswordCorrect()) {
                message.fadeIn(400);
            } else {
                message.fadeOut(400);
            }
        });
        form.find('#profile').change(function () {
            if (form.find('#profile').val() === "Trader") {
                $('#multiselect_div').slideDown();
            } else {
                $('#multiselect_div').slideUp();
                $('#multiselect_leftAll').click();
            }
        });
        if (form.find('#profile').val() === "Trader") {
            $('#multiselect_div').slideDown();
        } else {
            $('#multiselect_div').slideUp();
        }
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


