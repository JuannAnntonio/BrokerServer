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
<head ng-controller="backoffice_calculadora_precios_detalles_controller">
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
							<h2 class="panel-title">Reporte Calculadora de Precios Detalles</h2>
						</div>
						<div class="panel-body">

							<div class="row">
	
                                <div class="col-md-12">

                                    <div class="row">
                                        <div style="height: 100px; overflow-y: none; margin: .66667rem;" class="col-md-12 text-left"> 

                                            <div class="col-md-6">
                                            	<div style="border-bottom: 1px solid black !important;" >
	                                                <div style="display: inline-block;">Instrumento:</div>
	                                                <div style="display: inline-block;" id="instrumento"></div>
                                            	</div>
                                            </div>
                                            
                                            <div class="col-md-6">
                                                 <div style="border-bottom: 1px solid black !important;" >
	                                                <div style="display: inline-block;">Fecha Valuación:</div>
	                                                <div style="display: inline-block;" id="fechaValuacion"></div>
                                            	</div>
                                            </div>
                                            
                                            <div class="col-md-6">
                                                 <div style="border-bottom: 1px solid black !important;" >
	                                                <div style="display: inline-block;">Precio Sucio:</div>
	                                                <div style="display: inline-block;" id="precioSucio"></div>
                                            	</div>
                                            </div>
                                            
                                            <div class="col-md-6">
                                                <div style="border-bottom: 1px solid black !important;" >
	                                                <div style="display: inline-block;">Precio Limpio:</div>
	                                                <div style="display: inline-block;" id="precioLimpio"></div>
                                            	</div>
                                            </div>
                                            
                                            <div class="col-md-6">
                                                 <div style="border-bottom: 1px solid black !important;" >
	                                                <div style="display: inline-block;">Intereses:</div>
	                                                <div style="display: inline-block;" id="intereses"></div>
                                            	</div>
                                            </div>
                                            
                                            <div class="col-md-6">
                                                <div style="border-bottom: 1px solid black !important;" >
	                                                <div style="display: inline-block;">Valor Udi:</div>
	                                                <div style="display: inline-block;" id="valorUdi"></div>
                                            	</div>
                                            </div>

                                        </div>
                                    </div>

                                </div>

                            </div>

							<table width="100%"
								class="table table-striped table-bordered table-hover"
								id="backoffice_reporte_calculadora_precios_detalles_table">
								<thead>
									<tr>
										<th>Fecha Inicio</th> 
										<th>Fecha Fin</th>
										<th>DvX</th>
										<th>Periodo Cupón</th>
										<th>Intereses</th>
										<th>Valor Presente</th>
										<th>Suma Valor Presente</th>
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
	<script src="../resources/js/backoffice/backoffice-reporteCalculadoraPreciosDetalles.js?v=<%= System.currentTimeMillis() %>"></script>
	<script src="../resources/js/backoffice/backoffice-app-dashboard.js?v=<%= System.currentTimeMillis() %>"></script>
	<script src="../resources/js/backoffice/functions.js?v=<%= System.currentTimeMillis() %>"></script>
    <script src="../resources/js/moment.js?v=<%= System.currentTimeMillis() %>""></script>
    <!--daterangepicker requires moment-->
    <script src="../resources/js/daterangepicker.js?v=<%= System.currentTimeMillis() %>""></script>
    <script src="../resources/js/toastr.js?v=<%= System.currentTimeMillis() %>""></script>

	<script>
	    
	    $(document).ready(function()
	    {
	    	console.log("[reporteCaulculadoraPreciosDetalles][ready]");

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
	    	console.log($_GET['id_valmer']);

	    	getReporteCalculadoraPreciosDetallesClick($_GET['id_valmer']);

	    });
	</script>

</body>
</html>
