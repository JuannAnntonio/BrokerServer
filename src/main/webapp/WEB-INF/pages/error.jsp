<%--
  Created by IntelliJ IDEA.
  User: Gerardo
  Date: 15/10/16
  Time: 1:16 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!doctype html>
<html class="no-js" lang="es">
<head>
    <%@include file="component_header_meta_info.jsp" %>

    <%@include file="component_header_stylesheets.jsp" %>

    <%@include file="component_header_java_script.jsp" %>
</head>
<body class="login-body">
<div entityId="wrapper">
    <%@include file="component_navigation_login.jsp" %>
    <div class="row">
        <div class="col-sm-6 col-md-6 col-md-offset-3">
            <div class="login-panel panel panel-default">
                <div class="panel-heading center-text">
                    <h2 class="panel-title">Error pagina no encontrada</h2>
                </div>
                <div class="panel-body">
                </div>
            </div>

        </div>
    </div>

</div>

<!-- /#wrapper -->
<%@include file="component_end_page_scripts.jsp" %>
</body>
</html>
