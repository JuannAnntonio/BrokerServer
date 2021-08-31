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
<html class="no-js" lang="es" ng-app="admin-user-add">
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
                <h1 class="page-header">Agregar usuario</h1>
            </div>
            <div id="page-controls" class="col-lg-12 btn-group" ng-controller="user_edit">
                <a id="edit_button" class="btn btn-default" ng-click="enableForm()" style="display: none;"><i
                        class="fa fa-edit fa-fw"></i>Editar</a>
                <a id="save_button" class="btn btn-default" ng-click="saveChanges()"><i
                        class="fa fa-save fa-fw"></i>Guardar</a>
            </div>
        </div>
        <br>
        <div class="row">
            <div class="col-lg-12">
                <div class="panel panel-default">
                    <div class="panel-heading center-text">
                        <h2 class="panel-title">Datos de usuario</h2>
                    </div>
                    <div class="panel-body">
                        <form class="form-horizontal" id="new-user-form">
                            <div class="form-group">
                                <label class="col-sm-3" control-label for="username">Nombre de usuario:</label>
                                <div class="col-sm-9">
                                    <input disabled type="text" autofocus required id="username"
                                           placeholder="Nombre de usuario" class="form-control">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3" for="password">Contraseña:</label>
                                <div class="col-sm-9">
                                    <input disabled type="password" required id="password"
                                           placeholder="Contraseña"
                                           class="form-control">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3" for="password-confirmation">Vuelve a escribir la contraseña:</label>
                                <div class="col-sm-9">
                                    <input disabled type="password" required id="password-confirmation"
                                           placeholder="Contraseña"
                                           class="form-control">
                                </div>
                                <div id="warn-message-password" class="alert alert-warning" style="display: none;">
                                    <strong>Los campos de la contraseña no coinciden</strong>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3" for="profile">Perfil:</label>
                                <div class="col-sm-9">
                                    <select disabled required id="profile" class="form-control">
                                        <c:forEach items="${roles}" var="rol">
                                            <option value="${rol}"> ${rol} </option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3" for="email">Correo electrónico:</label>
                                <div class="col-sm-9">
                                    <input disabled type="text" required id="email"
                                           placeholder="Correo electrónico"
                                           class="form-control">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3" for="phone">Número telefónico:</label>
                                <div class="col-sm-9">
                                    <input disabled type="text" required id="phone"
                                           placeholder="Número telefónico"
                                           class="form-control">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3" for="institution">Institución:</label>
                                <div class="col-sm-9">
                                    <select disabled required id="institution" class="form-control">
                                        <c:forEach items="${institutions}" var="inst">
                                            <option value="${inst.idInstitution}"> ${inst.name} </option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group checkbox">
                                <label class="col-sm-3" for="active">Activo:
                                </label>
                                <div class="col-sm-9">
                                    <input disabled type="checkbox" required id="active" class="form-control"
                                           <c:if test="${user.enabled != 0}">checked</c:if> data-toggle="toggle"
                                           data-on=" " data-off=" "
                                           data-onstyle="success" data-offstyle="danger">
                                </div>
                            </div>
                            <br>
                            <div class="form-group" id="multiselect_div" ng-controller="multiselect" style="display: none;">
                                <div class="col-sm-2">
                                    <button type="button" id="multiselect_move_up" class="btn btn-block" disabled><i
                                            class="glyphicon glyphicon-arrow-up"></i></button>
                                    <button type="button" id="multiselect_move_down" class="btn btn-block" disabled><i
                                            class="glyphicon glyphicon-arrow-down"></i></button>
                                </div>
                                <div class="col-sm-4">
                                    <select name="from[]" id="multiselect_to" class="form-control" size="8"
                                            multiple="multiple" disabled>
                                    </select>
                                    <label for="multiselect_to">
                                        Tipos de valor preferidos
                                    </label>
                                </div>
                                <div class="col-sm-2">
                                    <button type="button" id="multiselect_rightAll" class="btn btn-block" disabled><i
                                            class="glyphicon glyphicon-backward"></i></button>
                                    <button type="button" id="multiselect_rightSelected" class="btn btn-block" disabled>
                                        <i class="glyphicon glyphicon-chevron-left"></i></button>
                                    <button type="button" id="multiselect_leftSelected" class="btn btn-block" disabled>
                                        <i class="glyphicon glyphicon-chevron-right"></i></button>
                                    <button type="button" id="multiselect_leftAll" class="btn btn-block" disabled><i
                                            class="glyphicon glyphicon-forward"></i></button>
                                </div>

                                <div class="col-sm-4">
                                    <select name="to[]" id="multiselect" class="form-control" size="8"
                                            multiple="multiple" disabled>
                                        <c:forEach items="${instruments}" var="inst">
                                            <option value="${inst.idInstrument}"> ${inst.tv} </option>
                                        </c:forEach>
                                    </select>
                                    <label for="multiselect">
                                        Tipo de valor disponible
                                    </label>
                                </div>
                            </div>
                            <input type="hidden"
                                   name="${_csrf.parameterName}"
                                   value="${_csrf.token}"/>
                        </form>
                        <div id="success-message" class="alert alert-success" style="display: none;">
                            <strong>¡Exito!</strong> Tu usuario ha sido registrado.
                        </div>
                        <div id="warn-message" class="alert alert-warning" style="display: none;">
                            <strong>Revisa los campos</strong> Algunos datos pueden ser incorrectos.
                        </div>
                        <div id="error-message" class="alert alert-danger" style="display: none;">
                            <strong>Ocurrio un error</strong> Consulta con el administrador del sistema.
                        </div>
                        <div id="error-message-config" class="alert alert-danger" style="display: none;">
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<%@include file="components/component_end_page_scripts.jsp" %>
<script src="../resources/js/variables.js"></script>
<script src="../resources/js/multiselect.js"></script>
<script src="../resources/js/admin/admin-app-user-add.js"></script>
<link href="https://gitcdn.github.io/bootstrap-toggle/2.2.2/css/bootstrap-toggle.min.css" rel="stylesheet">
<script src="https://gitcdn.github.io/bootstrap-toggle/2.2.2/js/bootstrap-toggle.min.js"></script>
<script src="../resources/js/toastr.js?v=<%= System.currentTimeMillis() %>""></script>
</body>
</html>
