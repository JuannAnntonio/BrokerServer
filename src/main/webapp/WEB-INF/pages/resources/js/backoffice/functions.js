(function() {
	backoffice_dashboard.factory('functions', function($http, $window) {
    return {
      loading: function() {
        console.log("[factory.js] loading");

        $('#loader-wrapper').css('display','');
        $window.onload = function(e) {
          //your magic here
          $('#loader-wrapper').css('display','none');
        }

        setTimeout(function() {
          $('#loader-wrapper').css('display','none');
        }, 4000);
      },
      loadingWait: function() {
        console.log("[factory.js] loading");

        $('#loader-wrapper').css('display','');
      },
      loadingEndWait: function() {
        console.log("[factory.js] loading");

        $('#loader-wrapper').css('display','none');
      },
      loadingWaitTime: function(x) {
        console.log("[factory.js] loading");

        $('#loader-wrapper').css('display','');
        setTimeout(function() {
          $('#loader-wrapper').css('display','none');
        }, x);
      },
      getParameter: function(id_parameter){

	      console.log("[factory][getParameter]");
	
	      var url = '/sigmact_broker/parameter/rest/getParameter';
		  	return $http.get(url,{
	        params: { cache: false, id_parameter_value:id_parameter },
	        cache: false
	      });
	
	   },
	   getReportePersonalizado: function(start, end){
	
	      console.log("[factory][getReportePersonalizado]");
	
	      var url = '/sigmact_broker/backoffice/rest/getDashboard';
		  	return $http.get(url,{
	        params: { cache: false, start:start, end:end },
	        cache: false
	      });
	
	   },
	   getReporteCartaConfirmacion: function(start, end){
			
	      console.log("[factory][getReporteCartaConfirmacion]");
	
	      var url = '/sigmact_broker/backoffice/rest/getReporteCartaConfirmacion';
		  	return $http.get(url,{
	        params: { cache: false, start:start, end:end },
	        cache: false
	      });
	
	   },
	   getReporteCalculadoraPrecios: function(tv){
			
	      console.log("[factory][getReporteCalculadoraPrecios]");
	
	      var url = '/sigmact_broker/backoffice/rest/getReporteCalculadoraPrecios';
		  	return $http.get(url,{
	        params: { cache: false, tv:tv },
	        cache: false
	      });
	
	   },
	   getReporteCalculadoraPreciosDetalles: function(id_valmer){
			
		      console.log("[factory][getReporteCalculadoraPreciosDetalles]");
		
		      var url = '/sigmact_broker/backoffice/rest/getReporteCalculadoraPreciosDetalles';
			  	return $http.get(url,{
		        params: { cache: false, id_valmer:id_valmer },
		        cache: false
		      });
		
		   },
	 };
  });

}).call(this);
