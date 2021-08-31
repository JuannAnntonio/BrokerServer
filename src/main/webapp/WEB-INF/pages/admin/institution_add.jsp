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
<html class="no-js" lang="es" ng-app="admin-institution-details">

<head>
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
                    <h1 class="page-header">Institutciones</h1>
                </div>
                <div id="page-controls" class="col-lg-12 btn-group" ng-controller="institution_edit">
                    <a id="edit_button" class="btn btn-default" ng-click="enableForm()"><i
                            class="fa fa-edit fa-fw"></i>Editar</a>
                    <a id="save_button" class="btn btn-default" ng-click="saveChanges()" style="display: none;"><i
                            class="fa fa-save fa-fw"></i>Guardar</a>
                </div>
            </div>
            <br>
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading center-text">
                            <h2 class="panel-title">Nueva institución</h2>
                        </div>
                        <div class="panel-body">
                            <form class="form-horizontal" id="institution-detail-form">
                                <div class="form-group">
                                    <label class="col-sm-2" control-label for="name">Nombre de institución:</label>
                                    <div class="col-sm-10">
                                        <input disabled type="text" autofocus required id="name" placeholder="Nombre"
                                            class="form-control">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-2" for="phone">Número telefónico:</label>
                                    <div class="col-sm-10">
                                        <input disabled type="text" required id="phone" placeholder="Número telefónico"
                                            class="form-control" required>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-2" for="key">Clave:</label>
                                    <div class="col-sm-10">
                                        <input disabled type="text" required id="key" placeholder="Clave"
                                            class="form-control" required>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-2" for="system_commission">Comisión del sistema:</label>
                                    <div class="col-sm-10">
                                        <input disabled type="text" required id="system_commission"
                                            placeholder="Comisión del sistema" class="form-control" required>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-2" for="system_commission">Nivel Fuera de Mercado:</label>
                                    <div class="col-sm-10">
                                        <input disabled type="text"
                                            oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*?)\..*/g, '$1');"
                                            required id="nivelFueraMercado"
                                            placeholder="Parámetro de Nivel Fuera de Mercado" class="form-control">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-2" for="active">Activo:
                                    </label>
                                    <div class="col-sm-10">
                                        <input disabled type="checkbox" required id="active" class="form-control" <c:if
                                            test="${institution.enabled != 0}">checked</c:if> data-toggle="toggle"
                                        data-on=" " data-off=" "
                                        data-onstyle="success" data-offstyle="danger">
                                    </div>
                                </div>
                                


                                <br>
                                <div class="form-group" id="multiselect_div" ng-controller="multiselect">
                                    <div class="col-sm-2">
                                        <button type="button" id="multiselect_move_up" class="btn btn-block" disabled><i
                                                class="glyphicon glyphicon-arrow-up"></i></button>
                                        <button type="button" id="multiselect_move_down" class="btn btn-block"
                                            disabled><i class="glyphicon glyphicon-arrow-down"></i></button>
                                    </div>
                                    <div class="col-sm-4 center-text">
                                        <select name="from[]" id="multiselect_to" class="form-control" size="8"
                                            multiple="multiple" disabled sort="false">
                                        </select>
                                        <label for="active">
                                            Bancos de trabajo preferidos
                                        </label>
                                    </div>
                                    <div class="col-sm-2">
                                        <button type="button" id="multiselect_rightAll" class="btn btn-block"
                                            disabled><i class="glyphicon glyphicon-backward"></i></button>
                                        <button type="button" id="multiselect_rightSelected" class="btn btn-block"
                                            disabled><i class="glyphicon glyphicon-chevron-left"></i></button>
                                        <button type="button" id="multiselect_leftSelected" class="btn btn-block"
                                            disabled><i class="glyphicon glyphicon-chevron-right"></i></button>
                                        <button type="button" id="multiselect_leftAll" class="btn btn-block" disabled><i
                                                class="glyphicon glyphicon-forward"></i></button>
                                    </div>
                                    <div class="col-sm-4">
                                        <select name="to[]" id="multiselect" class="form-control" size="8"
                                            multiple="multiple" disabled>
                                            <c:forEach items="${workbenches.inactiveWorkbenchs}" var="inst">
                                                <option value="${inst.idInstitution}"> ${inst.name} </option>
                                            </c:forEach>
                                        </select>
                                        <label for="active">
                                            Bancos de trabajo disponibles
                                        </label>
                                    </div>
                                </div>
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
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
    <script src="../resources/js/admin/admin-app-institution-add.js"></script>
    <link href="https://gitcdn.github.io/bootstrap-toggle/2.2.2/css/bootstrap-toggle.min.css" rel="stylesheet">
    <script src="https://gitcdn.github.io/bootstrap-toggle/2.2.2/js/bootstrap-toggle.min.js"></script>
<script src="../resources/js/toastr.js?v=<%= System.currentTimeMillis() %>""></script>
</body>

</html>