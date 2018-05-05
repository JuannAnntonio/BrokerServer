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
<html class="no-js" lang="es" ng-app="admin_dashboard">
<head>
    <%@include file="components/component_header_meta_info.jsp" %>

    <%@include file="components/component_header_stylesheets.jsp" %>

    <%@include file="components/component_header_java_script.jsp" %>
</head>
<body ng-controller="graph_controller">
<div id="wrapper">
    <%@include file="components/component_navigation_sidebar_admin.jsp" %>
    <div id="page-wrapper">
        <div class="row">
            <div class="col-lg-12">
                <h1 class="page-header">Datos de uso</h1>
            </div>
        </div>
        <div class="row">
            <div class="col-lg-12">
                <div class="panel panel-default">
                    <div class="panel-heading center-text">
                        <h2 class="panel-title">Monto manejado el último cuarto</h2>
                    </div>
                    <div class="panel-body">
                        <div line-chart
                             line-data="lastQuarterTradedAmount"
                             line-xkey='y'
                             line-ykeys='["a", "b"]'
                             line-labels='["Standing amount", "Aggresion amount"]'
                             line-colors='["#31C0BE", "#c7254e"]'>
                        </div>
                    </div>
                </div>
                <div class="panel panel-default">
                    <div class="panel-heading center-text">
                        <h2 class="panel-title">Actividades realizadas el último cuarto</h2>
                    </div>
                    <div class="panel-body">
                        <div line-chart line-data='lastQuarterActivityStandings'
                             line-xkey='y'
                             line-ykeys='["a", "b"]'
                             line-labels='["Standings count", "Aggression count"]'
                             line-colors='["#31C0BE", "#c7254e"]'>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<%@include file="components/component_end_page_scripts.jsp" %>
<script src="https://cdnjs.cloudflare.com/ajax/libs/angular-morris/1.3.0/angular-morris.min.js"></script>
<script src="../resources/js/variables.js"></script>
<script src="../resources/js/admin/admin-app-dashboard.js"></script>
</body>
</html>
