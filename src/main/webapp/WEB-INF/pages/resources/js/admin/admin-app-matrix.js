var matrix_admin = angular.module('matrixadmin', []);

var urlLogout = domain + "logout";
console.log("[admin_app_matrix] entry");

matrix_admin.controller('navigation_controller', ['$scope', '$http', '$window', function ($scope, $http, $window) {
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
}]),

matrix_admin.controller('admin_matrix_controller', ['$scope', '$http', '$window', 'functions', function ($scope, $http, $window, functions) {
		console.log("[admin_matrix_controller]");
		
		$scope.getAdminMatrixClick = function(institution){
			
    	//$("#loading").css("display","");
    	
        console.log("[Admin_matrix_controller][getAdminMatrixClick]");
        console.log("[Admin_matrix_controller][getAdminMatrixClick] institution: " + institution);

      functions.getAdmin_Matrix(institution).then(function (response) {

          console.log("[matrix_admin_controller][getAdminMatrixClick] Response");

          console.log(response.data.data);
          
        if(response.data.data.length>0){

          console.log(response.data);

		$('#institutionmat').dataTable().fnClearTable();
		$('#institutionmat').dataTable().fnAddData(response.data.data);		
		  
        } else {
            toastr["warning"]("No hay Datos", "");
            $('#institutionmat').dataTable().fnClearTable();
            functions.loadingEndWait();
        }

    	$("#loading").css("display","none");
      }, function (response) {
        /*ERROR*/
        toastr["error"]("Inténtelo de nuevo más tarde", "");
        functions.loadingEndWait();
    	$("#loading").css("display","none");

      });
    
    };

    getAdminMatrixClick = $scope.getAdminMatrixClick;
    
    
}]);
