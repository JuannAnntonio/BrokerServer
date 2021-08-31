<%--
  Created by IntelliJ IDEA.
  User: JJuan
  Date: 18/06/18
  Time: 15:39
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
    <%@include file="components/component_navigation_sidebar_admin.jsp" %>
    <div id="page-wrapper">
        <div class="row">
            <div class="col-lg-12">
                <h1 class="page-header">Proveedor de Precios</h1>
            </div>
        </div>
        <div class="row">

        </div>
    </div>
</div>
<%@include file="components/component_end_page_scripts.jsp" %>
<script src="https://cdnjs.cloudflare.com/ajax/libs/angular-morris/1.3.0/angular-morris.min.js"></script>
<script src="../resources/js/variables.js"></script>
<script src="../resources/js/toastr.js?v=<%= System.currentTimeMillis() %>""></script>
<script src="../resources/js/admin/admin-app-dashboard.js"></script>
</body>
</html>
