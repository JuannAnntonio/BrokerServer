
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!doctype html>
<html class="no-js" lang="es" ng-app="admin-matrix-details">
<head ng-controller="admin_matrix_controller">
    <%@include file="components/component_header_meta_info.jsp" %>

    <%@include file="components/component_header_stylesheets.jsp" %>

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
            <div id="page-controls" class="col-lg-12 btn-group" ng-controller="institution_edit">
                <a id="edit_button" class="btn btn-default" ng-click="enableForm()"><i class="fa fa-edit fa-fw"></i>Editar</a>
                <a id="save_button" class="btn btn-default" ng-click="saveChanges()" style="display: none;"><i
                        class="fa fa-save fa-fw"></i>Guardar</a>
            </div>
        </div>
        <br>
        <div class="row">
            <div class="col-lg-12">
                <div class="panel panel-default">
                    <div class="panel-heading center-text">
                        <h2 class="panel-title">Datos de la relación</h2>
                    </div>
                    <div class="panel-body">
                        <form class="form-horizontal" id="institution-detail-form">
                        <div class="form-group">
                                <div class="col-sm-9">
                                    <input disabled type="hidden" required id="id"
                                           placeholder="id"
                                           class="form-control" value="">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3" control-label for="name1">Nombre de institución:</label>
                                <div class="col-sm-9">
                                    <input disabled type="text" autofocus required id="inputName1"
                                           placeholder="Nombre" class="form-control"
                                           />
                                </div>
                            </div>                            <div class="form-group">
                                <label class="col-sm-3" for="instrument">Instrumento:</label>
                                <div class="col-sm-9">
                                    <input disabled type="text" required id="instrument"
                                           placeholder="Instrumento"
                                           class="form-control" value="">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3" for="Comision">Comision:</label>
                                <div class="col-sm-9">
                                    <input disabled type="text" required id="comision"
                                           placeholder="Comision"
                                           class="form-control" value="" required>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3" for="name2">Institución emparejada:</label>
                                <div class="col-sm-9">
                                    <input disabled type="text" required id="name2"
                                           placeholder=""
                                           class="form-control" value="">
                                </div>
                            </div>
                            
                            <input type="hidden"
                                   name="${_csrf.parameterName}"
                                   value="${_csrf.token}"/>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<%@include file="components/component_end_page_scripts.jsp" %>
<script src="../resources/js/variables.js"></script>
<script src="../resources/js/multiselect.js"></script>
<script src="../resources/js/admin/admin-app-matrix_details.js"></script>
<link href="https://gitcdn.github.io/bootstrap-toggle/2.2.2/css/bootstrap-toggle.min.css" rel="stylesheet">
<script src="../resources/js/admin/admin-app-matrix.js?v=<%= System.currentTimeMillis() %>"></script>
<script src="../resources/js/admin/functions.js?v=<%= System.currentTimeMillis() %>"></script>
<script src="../resources/js/admin/functions2.js?v=<%= System.currentTimeMillis() %>"></script>
<script src="https://gitcdn.github.io/bootstrap-toggle/2.2.2/js/bootstrap-toggle.min.js"></script>
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

	    	
	    	console.log("[institution_details][ready]");
	            
	            $("#id").html($_GET['id']);
	            
	            console.log("[institution_details.jsp] id: " + $_GET['id']);
	
	            getAdminMatrixClick($_GET['id']);
	            
	            console.log("[institution_details.jsp] entrao al get");
	
	        });
	</script>

</body>
</html>
