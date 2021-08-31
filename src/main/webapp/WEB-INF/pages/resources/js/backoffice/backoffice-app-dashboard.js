var backoffice_dashboard = angular.module('backofficeDashboard', []);

var urlLogout = domain + "logout";

backoffice_dashboard.controller('backoffice_controller_header', ['$scope', '$http', '$window', 'functions', function ($scope, $http, $window, functions) {

    console.log("[backoffice_controller]");
    
    var data = {
            _csrf: $('input[name="_csrf"]').val()
        };
        
    
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

backoffice_dashboard.controller('backoffice_controller', ['$scope', '$http', '$window', 'functions', function ($scope, $http, $window, functions) {

    console.log("[backoffice_controller]");
    
    $scope.getParameterClick = function(id_parameter){

      functions.getParameter(id_parameter).then(function (response) {

          console.log("[backoffice_controller][getParameter]");
          console.log("[backoffice_controller][getParameter] id_parameter: " + id_parameter);
    	  

          console.log(response.data);
          
        if(response.data.id_parameter == "today"){

          $('#datepicker').daterangepicker({
              timePicker: false,
              startDate: response.data.value,
              endDate: response.data.value,
              locale:
              {
                  format: 'YYYY-MM-DD'
              }
          });

        } else {
            toastr["warning"]("Parámetro Incorrecto", "");
            functions.loadingEndWait();
        }
      }, function (response) {
        /*ERROR*/
        toastr["error"]("Inténtelo de nuevo más tarde", "");
        functions.loadingEndWait();

      });/*fin getPosicionPorInstrumentoObtenerInstrumentos*/
    
    };

    getParameterClick = $scope.getParameterClick;
    
    $scope.getReportePersonalizadoClick = function(start, end){

      functions.getReportePersonalizado(start, end).then(function (response) {

          console.log("[backoffice_controller][getReportePersonalizado]");

          console.log(response.data.data);
          
        if(response.data.data.length>0){

          console.log(response.data);

          $('#backoffice_dashboard_table').dataTable().fnClearTable();
          $('#backoffice_dashboard_table').dataTable().fnAddData(response.data.data);

        } else {
            toastr["warning"]("No hay Datos", "");
            $('#backoffice_dashboard_table').dataTable().fnClearTable();
            functions.loadingEndWait();
        }
      }, function (response) {
        /*ERROR*/
        toastr["error"]("Inténtelo de nuevo más tarde", "");
        functions.loadingEndWait();

      });/*fin getPosicionPorInstrumentoObtenerInstrumentos*/
    
    };

    getReportePersonalizadoClick = $scope.getReportePersonalizadoClick;
    
    
}]);


backoffice_dashboard.controller('backoffice_carta_confirmacion_controller', ['$scope', '$http', '$window', 'functions', function ($scope, $http, $window, functions) {

    console.log("[backoffice_carta_confirmacion_controller]");
    
    $scope.getParameterClick = function(id_parameter){

      functions.getParameter(id_parameter).then(function (response) {

          console.log("[backoffice_carta_confirmacion_controller][getParameter]");
          console.log("[backoffice_carta_confirmacion_controller][getParameter] id_parameter: " + id_parameter);
    	  

          console.log(response.data);
          
        if(response.data.id_parameter == "today"){

          $('#datepicker').daterangepicker({
              timePicker: false,
              startDate: response.data.value,
              endDate: response.data.value,
              locale:
              {
                  format: 'YYYY-MM-DD'
              }
          });

        } else {
            toastr["warning"]("Parámetro Incorrecto", "");
            functions.loadingEndWait();
        }
      }, function (response) {
        /*ERROR*/
        toastr["error"]("Inténtelo de nuevo más tarde", "");
        functions.loadingEndWait();

      });/*fin getPosicionPorInstrumentoObtenerInstrumentos*/
    
    };

    getParameterClick = $scope.getParameterClick;
    
    $scope.getReporteCartaConfirmacionClick = function(start, end){

      functions.getReporteCartaConfirmacion(start, end).then(function (response) {

          console.log("[backoffice_carta_confirmacion_controller][getReporteCartaConfirmacion]");

          console.log(response.data.data);
          
        if(response.data.data.length>0){

          console.log(response.data);

          $('#backoffice_reporte_carta_confirmacion_table').dataTable().fnClearTable();
          $('#backoffice_reporte_carta_confirmacion_table').dataTable().fnAddData(response.data.data);

        } else {
            toastr["warning"]("No hay Datos", "");
            $('#backoffice_reporte_carta_confirmacion_table').dataTable().fnClearTable();
            functions.loadingEndWait();
        }
      }, function (response) {
        /*ERROR*/
        toastr["error"]("Inténtelo de nuevo más tarde", "");
        functions.loadingEndWait();

      });/*fin getPosicionPorInstrumentoObtenerInstrumentos*/
    
    };

    getReporteCartaConfirmacionClick = $scope.getReporteCartaConfirmacionClick;
    
    
}]);


backoffice_dashboard.controller('backoffice_calculadora_precios_controller', ['$scope', '$http', '$window', 'functions', function ($scope, $http, $window, functions) {

    console.log("[backoffice_calculadora_precios_controller]");
    

    /*
    $scope.getParameterClick = function(id_parameter){

      functions.getParameter(id_parameter).then(function (response) {

          console.log("[backoffice_calculadora_precios_controller][getParameter]");
          console.log("[backoffice_calculadora_precios_controller][getParameter] id_parameter: " + id_parameter);
    	  

          console.log(response.data);
          
        if(response.data.id_parameter == "today"){

          $('#datepicker').daterangepicker({
              timePicker: false,
              startDate: response.data.value,
              endDate: response.data.value,
              locale:
              {
                  format: 'YYYY-MM-DD'
              }
          });

        } else {
            toastr["warning"]("Parámetro Incorrecto", "");
            functions.loadingEndWait();
        }
      }, function (response) {
        //ERROR
        toastr["error"]("Inténtelo de nuevo más tarde", "");
        functions.loadingEndWait();

      });//fin getPosicionPorInstrumentoObtenerInstrumentos
    
    };

    getParameterClick = $scope.getParameterClick;
    
    */
    
    $scope.getReporteCalculadoraPreciosClick = function(tv){
    	
    	$("#loading").css("display","");
    	
        console.log("[backoffice_calculadora_precios_controller][getReporteCalculadoraPreciosClick]");
        console.log("[backoffice_calculadora_precios_controller][getReporteCalculadoraPreciosClick] tv: " + tv);

      functions.getReporteCalculadoraPrecios(tv).then(function (response) {

          console.log("[backoffice_calculadora_precios_controller][getReporteCalculadoraPreciosClick]");

          console.log(response.data.data);
          
        if(response.data.data.length>0){

          console.log(response.data);

          $('#backoffice_reporte_calculadora_precios_table').dataTable().fnClearTable();
          $('#backoffice_reporte_calculadora_precios_table').dataTable().fnAddData(response.data.data);

        } else {
            toastr["warning"]("No hay Datos", "");
            $('#backoffice_reporte_calculadora_precios_table').dataTable().fnClearTable();
            functions.loadingEndWait();
        }

    	$("#loading").css("display","none");
      }, function (response) {
        /*ERROR*/
        toastr["error"]("Inténtelo de nuevo más tarde", "");
        functions.loadingEndWait();
    	$("#loading").css("display","none");

      });/*fin getPosicionPorInstrumentoObtenerInstrumentos*/
    
    };

    getReporteCalculadoraPreciosClick = $scope.getReporteCalculadoraPreciosClick;
    
    
}]);

backoffice_dashboard.controller('backoffice_calculadora_precios_detalles_controller', ['$scope', '$http', '$window', 'functions', function ($scope, $http, $window, functions) {

    console.log("[backoffice_calculadora_precios_detalles_controller]");
    
    $scope.getReporteCalculadoraPreciosDetallesClick = function(id_valmer){
    	
        console.log("[backoffice_calculadora_precios_controller][getReporteCalculadoraPreciosDetallesClick]");
        console.log("[backoffice_calculadora_precios_controller][getReporteCalculadoraPreciosDetallesClick] id_valmer: " + id_valmer);

      functions.getReporteCalculadoraPreciosDetalles(id_valmer).then(function (response) {

          console.log("[backoffice_calculadora_precios_detalles_controller][getReporteCalculadoraPreciosDetallesClick]");

          console.log(response.data.data);
          
        if(response.data.data.backOfficeReporteCalculadoraPreciosDetalles.length>0){

          console.log(response.data.data);
          
          $("#instrumento").html(response.data.data.instrumento);
          $("#fechaValuacion").html(response.data.data.fechaValuacion);
          $("#intereses").html(response.data.data.intereses);
          $("#precioLimpio").html(response.data.data.precioLimpio);
          $("#precioSucio").html(response.data.data.precioSucio);
          $("#valorUdi").html(response.data.data.valorUdi);
          
          console.log(response.data.data.backOfficeReporteCalculadoraPreciosDetalles);

          $('#backoffice_reporte_calculadora_precios_detalles_table').dataTable().fnClearTable();
          $('#backoffice_reporte_calculadora_precios_detalles_table').dataTable().fnAddData(response.data.data.backOfficeReporteCalculadoraPreciosDetalles);

        } else {
            toastr["warning"]("No hay Datos", "");
            $('#backoffice_reporte_calculadora_precios_detalles_table').dataTable().fnClearTable();
            functions.loadingEndWait();
        }

      }, function (response) {
        /*ERROR*/
        toastr["error"]("Inténtelo de nuevo más tarde", "");
        functions.loadingEndWait();

      });/*fin getPosicionPorInstrumentoObtenerInstrumentos*/
    
    };

    getReporteCalculadoraPreciosDetallesClick = $scope.getReporteCalculadoraPreciosDetallesClick;
    
}]);
