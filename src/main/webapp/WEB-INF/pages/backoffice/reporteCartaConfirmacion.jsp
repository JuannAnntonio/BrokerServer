<%--
  Created by IntelliJ IDEA.
  User: Gerardo
  Date: 28/10/16
  Time: 12:03 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="java.util.Date" %>  
<!doctype html>
<html class="no-js" lang="es" ng-app="backofficeDashboard">
<head ng-controller="backoffice_carta_confirmacion_controller">
<%@include file="components/component_header_meta_info.jsp"%>

<%@include file="components/component_header_stylesheets.jsp"%>

<%@include file="components/component_header_stylesheets_datatables.jsp"%>

<%@include file="components/component_header_java_script.jsp"%>
</head>
<body>
	<div id="wrapper">
		<%@include
			file="components/component_navigation_sidebar_backoffice.jsp"%>
		<div id="page-wrapper">
			<div class="row">
				<div class="col-lg-12">
					<h1 class="page-header">BackOffice</h1>
				</div>
			</div>
			<div class="row">
				<div class="col-lg-12">
					<div class="panel panel-default">
						<div class="panel-heading center-text">
							<h2 class="panel-title">Reporte Operaciones</h2>
						</div>
						<div class="panel-body">

							<div style="margin-bottom: 10px;" class="row">
								
	                            <div class="col-md-12">
		                            <div style="margin-top: 0px; !important"  class="input-group input-group-md">
		                                <input type="text" class="form-control" placeholder="Select date" id="datepicker">
									    <span class="input-group-addon" id="sizing-addon1">
		                            		<i class="fal fa-calendar"></i>
		                            	</span>
									</div>
	                            </div>
	                            
	                        </div>

							<table width="100%"
								class="table table-striped table-bordered table-hover"
								id="backoffice_reporte_carta_confirmacion_table">
								<thead>
									<tr>
										<th>ID Operación</th> 
										<th>Fecha Operación</th>
										<th>Hora Operación</th>
										<th>Instrumento</th>
										<th>Tipo Operacion</th>
										<th>Contraparte</th>
										<th>Monto Nominal</th>
										<th>Tasa Negociada</th>
										<th>Tasa Comisión</th>
										<th>Tasa Liquidación</th>
										<th>Precio Sucio</th>
										<th>Monto Liquidación</th>
										<th>Fecha Liquidación</th>
										<th>Días por Vencer</th>
										<th>Títulos</th>
										<th>Operador</th>
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
	
	<%@include file="components/component_end_page_scripts.jsp"%>
	<%@include file="components/component_end_page_scripts_datatables.jsp"%>
	<script src="../resources/js/variables.js?v=<%= System.currentTimeMillis() %>"></script>
	<script src="../resources/js/backoffice/backoffice-reporteCartaConfirmacion.js?v=<%= System.currentTimeMillis() %>"></script>
	<script src="../resources/js/backoffice/backoffice-app-dashboard.js?v=<%= System.currentTimeMillis() %>"></script>
	<script src="../resources/js/backoffice/functions.js?v=<%= System.currentTimeMillis() %>"></script>
    <script src="../resources/js/moment.js?v=<%= System.currentTimeMillis() %>""></script>
    <!--daterangepicker requires moment-->
    <script src="../resources/js/daterangepicker.js?v=<%= System.currentTimeMillis() %>""></script>
    <script src="../resources/js/toastr.js?v=<%= System.currentTimeMillis() %>""></script>

	<script>
	    
	    $(document).ready(function()
	    {
	    	console.log("[dashboard][ready]");
	    	
	    	getParameterClick("today");
	
	        $('#datepicker').on('change', function(){
	
	            console.log("[datepicker] change");
	
	            var arrayDatePicker = $('#datepicker').val().split(" - ");
	            var start = arrayDatePicker[0];
	            var end = arrayDatePicker[1];
	            

	            console.log("[datepicker] change start: " + start);
	            console.log("[datepicker] change end: " + end);
	
	            getReporteCartaConfirmacionClick(start, end);
	
	        });
	
	    });
	</script>
</body>
</html>
