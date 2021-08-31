(function() {
	console.log("[admin_factory.js] entry");
	matrix_admin.factory('functions', function($http, $window) {
		console.log("[admin_factory.js][matrix_admin.factory]");
    return {
      loading: function() {
        console.log("[admin_factory.js] loading");

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
        console.log("[admin_factory.js] loading");

        $('#loader-wrapper').css('display','');
      },
      loadingEndWait: function() {
        console.log("[admin_factory.js] loading");

        $('#loader-wrapper').css('display','none');
      },
      loadingWaitTime: function(x) {
        console.log("[admin_factory.js] loading");

        $('#loader-wrapper').css('display','');
        setTimeout(function() {
          $('#loader-wrapper').css('display','none');
        }, x);
      },
      	   getAdmin_Matrix: function(institution){
			
	      console.log("[factory][getAdmin_Matrix]");
	
	      var url = '/sigmact_broker/admin/rest/getMatrix';
		  	return $http.get(url,{
	        params: { cache: false, institution:institution },
	        cache: false
	      });
	   },
			getAdmin_Matrix2: function(id){
			
	      console.log("[factory][getAdmin_Matrix2]");
	
	      var url = '/sigmact_broker/admin/rest/getMatrix2';
		  	return $http.get(url,{
	        params: { cache: false, id:id },
	        cache: false
	      });
	   },
	 };
  });
}).call(this);
