<%--
  Created by IntelliJ IDEA.
  User: Gerardo
  Date: 28/10/16
  Time: 12:03 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!doctype html>
<html class="no-js" lang="es">
<head>
    <%@include file="components/component_header_meta_info.jsp" %>

    <%@include file="components/component_header_stylesheets.jsp" %>

    <%@include file="components/component_header_stylesheets_datatables.jsp" %>

    <%@include file="components/component_header_java_script.jsp" %>
</head>
<body>
<div id="wrapper">
    <%@include file="components/component_navigation_sidebar_backoffice.jsp" %>
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
                        <h2 class="panel-title">BackOffice Data</h2>
                    </div>
                    <div class="panel-body">

                        <table width="100%" class="table table-striped table-bordered table-hover" id="backoffice_dashboard_table">
                        <thead>
                        <tr>
                            <th>Fecha</th>
                            <th>Tipo de transacció</th>
                            <th>Instrumento</th>
                            <th>Comprador</th>
                            <th>Vendedor</th>
                            <th>Workbench</th>
                            <th>Precio de compra</th>
                            <th>Precio de venta</th>
                            <th>Cantidad</th>
                            <th>Cantidad en wrokbench</th>
                            <th>Titulos</th>
                            <th>Razón</th>
                            <th>Sobrecargo</th>
                            <th>Comisión del sistema</th>
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
<script src="../resources/js/backoffice/backoffice-dashboard.js"></script>
</body>
</html>
