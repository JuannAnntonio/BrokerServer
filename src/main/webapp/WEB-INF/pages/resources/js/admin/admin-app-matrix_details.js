var admin_matrix_details = angular.module('admin-matrix-details', [], function() {});

var urlSave = domain + "admin/rest/postMatrix";
var urlLogout = domain + "logout";

console.log("[admin-matrix-details] entry");

var disableForm = function() {
    $("#institution-detail-form").find(':input[id="comision"]').prop("disabled", true);
    $("#edit_button").show();
    $("#save_button").hide();
}

var lEnableForm = function() {
    $("#institution-detail-form").find(':input[id="comision"]').prop("disabled", false);
    $("#edit_button").hide();
    $("#save_button").show();
}

var prev_name = null;

function saveDetails() {
    var data = {
        id_comision: $("#id").val(),
        comision: $("#comision").val(),
        _csrf: $('input[name="_csrf"]').val()
    }
    $.ajax({
        type: "POST",
        url: urlSave,
        data: data,
        success: function(data) {
            if (data) {
                disableForm();
                prev_name = $('input[id="comision"]').val();

                toastr["success"]("Datos Guardados correctamente", "");

            }
        },
        error: function(jqXHR, textStatus, errorThrown) {

            toastr["error"]("Error: Contacte a su administrador", "");
        }
    })
}


admin_matrix_details.controller('institution_edit', function($scope) {
    console.log("[Admin-app-matrix_details][institution_edit] entry")
    $scope.enableForm = function() {
        prev_name = $('input[id="comision"]').val();
        lEnableForm();
    }
    $scope.saveChanges = function() {
        saveDetails();
    }

});



admin_matrix_details.controller('navigation_controller', ['$scope', '$http', '$window', function($scope, $http, $window) {
        var data = {
            _csrf: $('input[name="_csrf"]').val()
        }
        $scope.logout = function() {

            $http({
                method: "POST",
                url: urlLogout,
                params: data
            }).success(function() {
                $window.location.replace(domain);
            }).error(function(data, status) {
                alert("Error in logout status: " + status + " data:" + JSON.stringify(data));
            })
        }
    }]),

    admin_matrix_details.controller('admin_matrix_controller', ['$scope', '$http', '$window', 'functions', function($scope, $http, $window, functions) {
        console.log("[admin_matrix_controller]");

        $scope.getAdminMatrixClick = function(id) {
            console.log("[admin_matrix_controller]");

            //$("#loading").css("display","");

            console.log("[Admin_matrix_controller][getAdminMatrixClick]");
            console.log("[Admin_matrix_controller][getAdminMatrixClick] id: " + id);

            functions.getAdmin_Matrix2(id).then(function(response) {

                console.log("[matrix_admin_controller][getAdminMatrixClick] Response");

                console.log(response.data.data);

                if (response.data.data.length > 0) {

                    console.log('response.data: ', response.data);

                    data = response.data.data[0];
                    $("#id").val(data.idComision);
                    $("#inputName1").val(data.institutionName);
                    $("#instrument").val(data.instrument);
                    $("#comision").val(data.comision);
                    $("#name2").val(data.institutionName2);
                    console.log("data: ", data);
                    //Aqui se va a poner en donde se deben imprimir	
                    toastr["success"]("Datos Mostrados correctamente", "");

                } else {
                    toastr["warning"]("No hay Datos", "");
                    $('#institutionmat').dataTable().fnClearTable();
                    functions.loadingEndWait();
                }

                $("#loading").css("display", "none");
            }, function(response) {
                /*ERROR*/
                toastr["error"]("Inténtelo de nuevo más tarde", "");
                functions.loadingEndWait();
                $("#loading").css("display", "none");

            });

        };

        getAdminMatrixClick = $scope.getAdminMatrixClick;


    }]);