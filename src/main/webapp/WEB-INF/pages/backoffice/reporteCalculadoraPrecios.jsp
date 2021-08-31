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
<head ng-controller="backoffice_calculadora_precios_controller">
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
							<h2 class="panel-title">Reporte Calculadora de Precios</h2>
						</div>
						<div class="panel-body">
						
						<div class="row">
							
							<div class="col-md-4">
							</div>
							
							<div style="margin-bottom: 20px;" class="col-md-4 text-center">
							
								Filtro por Instrumento:<br /><br />
							
								<select class="select2-idiomas form-control col-md-6" id="instrumentos" name="instrumentos"  required>
	                                
	                                <option value="BI">BI</option>
	                                <option value="IM">IM</option>
	                                <option value="IQ">IQ</option>
	                                <option value="IS">IS</option>
					<option value="LD">LD</option>
	                                <option value="LF">LF</option> /*Modificacion EYS LF */
	                                <option value="M">M</option>
	                                <option value="S">S</option>
	                            </select><br /><br />
	                            
								<div style="display: none;" id="loading" class="progress">
								  <div class="progress-bar progress-bar-striped active" role="progressbar" aria-valuenow="100" aria-valuemin="0" aria-valuemax="100" style="width: 100%">
								    <span class="sr-only">45% Complete</span>
								  </div>
								</div>
							</div>
							
							<div class="col-md-4">
							</div>
							
						</div>

							<table width="100%"
								class="table table-striped table-bordered table-hover"
								id="backoffice_reporte_calculadora_precios_table">
								<thead>
									<tr>
										<th>Instrumento</th> 
										<th>Fecha Valuación</th>
										<th>DxV</th>
										<th>Tasa Cupón</th>
										<th>Tasa Valuación</th>
										<th>Tasa Mercado</th>
										<th>Valor Udi</th>
										<th>Inicio Cupón</th>
										<th>Fin de Cupón</th>
										<th>Periodo Cupón</th>
										<th>Cupones x Vencer</th>
										<th>Precio Sucio</th>
										<th>Detalle</th>
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
	<script src="../resources/js/backoffice/backoffice-reporteCalculadoraPrecios.js?v=<%= System.currentTimeMillis() %>"></script>
	<script src="../resources/js/backoffice/backoffice-app-dashboard.js?v=<%= System.currentTimeMillis() %>"></script>
	<script src="../resources/js/backoffice/functions.js?v=<%= System.currentTimeMillis() %>"></script>
    <script src="../resources/js/moment.js?v=<%= System.currentTimeMillis() %>""></script>
    <!--daterangepicker requires moment-->
    <script src="../resources/js/daterangepicker.js?v=<%= System.currentTimeMillis() %>""></script>
    <script src="../resources/js/toastr.js?v=<%= System.currentTimeMillis() %>""></script>


	<script>
	    
	    $(document).ready(function()
	    {
	    	console.log("[reporteCaulculadoraPrecios][ready]");

	    	getReporteCalculadoraPreciosClick('BI');
	
	        $('#instrumentos').on('change', function(){
	
	            console.log("[reporteCaulculadoraPrecios][instrumentos] change");
	            
	            var instrumentos = $("#instrumentos option:selected" ).val();
	            
	            console.log("[reporteCaulculadoraPrecios][instrumentos] " + $( "#instrumentos option:selected" ).val());
	
	            getReporteCalculadoraPreciosClick(instrumentos);
	
	        });
	
	    });
	</script>

</body>
</html>