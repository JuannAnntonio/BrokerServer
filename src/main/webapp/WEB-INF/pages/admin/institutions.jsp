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
<html class="no-js" lang="es" ng-app="admin-institutions">
<head>
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
                        <h2 class="panel-title">Datos de instituciones</h2>
                    </div>
                    <div class="panel-body">

                        <table width="100%" class="table table-striped table-bordered table-hover" id="institution_table">
                        <thead>
                        <tr>
                            <th>Nombre</th>
                            <th>Usuarios activos</th>
                            <th>Posturas activas</th>
                            <th>Monto operado los últimos 90 días</th>
                            <th>Activo</th>
                            <th>Ver usuarios</th>
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
<script src="../resources/js/admin/admin-institutions.js"></script>
<script src="../resources/js/admin/admin-app-institutions.js"></script>
<script src="../resources/js/toastr.js?v=<%= System.currentTimeMillis() %>""></script>
</body>
</html>
