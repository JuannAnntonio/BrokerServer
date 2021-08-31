<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!doctype html>
<html class="no-js" lang="es" ng-app="matrixadmin" >
<head ng-controller="admin_matrix_controller">
    <%@include file="components/component_header_meta_info.jsp" %>

    <%@include file="components/component_header_stylesheets.jsp" %>

    <%@include file="components/component_header_stylesheets_datatables.jsp" %>

    <%@include file="components/component_header_java_script.jsp" %>
</head>
<body>

<div id="wrapper">
    <%@include file="components/component_navigation_sidebar_admin.jsp" %>
    <div id="page-wrapper">
        <div class="row">
            <div class="col-lg-12">
                <h1 class="page-header">Instituciones</h1>
            </div>
        </div>
        <div class="row">
            <div class="col-lg-12">
                <div class="panel panel-default">
                    <div class="panel-heading center-text">
                        <h2 class="panel-title">Datos de instituciones <strong id="institution"></strong></h2>
                    </div>
                    <div class="panel-body">

                        <table width="100%" class="table table-striped table-bordered table-hover" id="institutionmat">
                        <thead>
                        <tr>
                        	<th>Id Comisión</th>
                        	<th>Id Institución</th>
                            <th>Institución</th>
                            <th>Instrumento</th>
                            <th>Comision</th>
                            <th>Id a Institución</th>
                            <th>A Institución</th>
                            <th>Ver/Editar matriz de comisiones</th>
                        </tr>
                        </thead>
                        <tbody></tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<%@include file="components/component_end_page_scripts.jsp" %>
<%@include file="components/component_end_page_scripts_datatables.jsp" %>
<script src="../resources/js/variables.js"></script>
<script src="../resources/js/admin/admin-matrix.js"></script>
<script src="../resources/js/admin/admin-app-matrix.js?v=<%= System.currentTimeMillis() %>"></script>
<script src="../resources/js/admin/functions.js?v=<%= System.currentTimeMillis() %>"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/angular-morris/1.3.0/angular-morris.min.js"></script>
<script src="../resources/js/toastr.js?v=<%= System.currentTimeMillis() %>""></script>
<script>
	    
	    $(document).ready(function()
	    {

	    	var $_GET = {};
	    	if(document.location.toString().indexOf('?') !== -1) {
	    		    	    var query = document.location
	    		    	                   .toString()
	    		    	                   // get the query string
	    		    	                   .replace(/^.*?\?/, '')
	    		    	                   // and remove any existing hash string (thanks, @vrijdenker)
	    		    	                   .replace(/#.*$/, '')
	    		    	                   .split('&');

	    		    	    for(var i=0, l=query.length; i<l; i++) {
	    		    	       var aux = decodeURIComponent(query[i]).split('=');
	    		    	       $_GET[aux[0]] = aux[1];
	    		    	    }
	    		    	}
	    		    	//get the 'index' query parameter

	    	
	    	console.log("[institutionmatrix][ready]");
	            
	            $("#institution").html($_GET['institution']);
	            
	            console.log("[institutionmatrix.jsp] institution: " + $_GET['institution']);
	
	            getAdminMatrixClick($_GET['institution']);
	
	        });
	</script>
</body>
</html>
