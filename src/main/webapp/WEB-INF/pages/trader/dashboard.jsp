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
<html class="no-js" lang="es" ng-app="traderDashboard">
<head>
    <%@include file="components/component_header_meta_info.jsp" %>

    <%@include file="components/component_header_stylesheets.jsp" %>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.12.1/css/bootstrap-select.min.css"
          rel="stylesheet">
    <link href="../resources/js/slickgrid/slick.grid.css" rel="stylesheet">
    <link rel="stylesheet" href="http://code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css" type="text/css"/>
    <link rel="stylesheet" href="../resources/js/slickgrid/slick-custom-theme.css" type="text/css"/>

    <%@include file="components/component_header_java_script.jsp" %>
</head>
<body>
<div id="wrapper">
    <%@include file="components/component_navigation_sidebar_trader.jsp" %>
    <div id="page-wrapper-trader" style="width: 1740px; min-width: 1740px">
        <div class="row">
            <div class="col-lg-12">
                <div class="col-lg-6" id="table-div" ng-controller="table_controller" style="padding: 0;">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                    <div id="trader-table" style="height: 2300px; font-size: 10px;"></div>
                </div>
                <div class="col-lg-6" style="padding-left: 20px">
                    <div id="instrument-statistics" class="row" ng-controller="graph_controller">
                        <div id="warn-message" class="alert alert-warning"
                             style="display: none;"></div>
                        <div class="panel panel-default">
                            <div class="panel-heading center-text">
                                <h2 class="panel-title">Desempeño del instrumento</h2>
                            </div>
                            <div class="panel-body">
                                <div id="graph_form_div" class="btn-block">
                                    <form id="graph_form">
                                        <div class="form-group">
                                            <label class="col-sm-2 col-lg-2" control-label
                                                   for="drop_down_graph_instrument">Instrumento: </label>
                                            <select id="drop_down_graph_instrument"
                                                    class="selectpicker col-sm-4 col-lg-4"
                                                    data-live-search="true"
                                                    ng-model="data.dropdown_value">
                                                <option data-ng-repeat="option in data.options" value="{{option.name}}">
                                                    {{option.name}}
                                                </option>
                                            </select>

                                            <button class="btn btn-default" ng-click="load_graph_data(1)"
                                                    style="background-color:#F1592A; color: white">
                                                1D
                                            </button>
                                            <button class="btn btn-default" ng-click="load_graph_data(7)"
                                                    style="background-color:#F1592A; color: white">
                                                1S
                                            </button>
                                            <button class="btn btn-default" ng-click="load_graph_data(30)"
                                                    style="background-color:#F1592A; color: white">
                                                1M
                                            </button>
                                            <button class="btn btn-default" ng-click="load_graph_data(90)"
                                                    style="background-color:#F1592A; color: white">
                                                3M
                                            </button>
                                            <button class="btn btn-default" ng-click="load_graph_data(180)"
                                                    style="background-color:#F1592A; color: white">
                                                6M
                                            </button>
                                            <button class="btn btn-default" ng-click="load_graph_data(365)"
                                                    style="background-color:#F1592A; color: white">
                                                1A
                                            </button>
                                            <button class="btn btn-default" ng-click="load_graph_data(730)"
                                                    style="background-color:#F1592A; color: white">
                                                2A
                                            </button>
                                        </div>
                                    </form>
                                    <!--line-data="data.graph_data"-->
                                    <!--
                                         line-chart
                                         line-data=data.graph_data
                                         line-xkey='date'
                                         line-ykeys='["yield"]'
                                         line-labels='[data.dropdown_value]'
                                         line-colors='["#FFB253"]'
                                         point-size='1'
                                         style="background-color: black">-->
                                    <div id="line-chart" style="background-color: black">

                                    </div>
                                    <div id="error-message" title="Error de entrada de datos">

                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div id="activity-summary" class="row" ng-controller="movements_controller">
                        <div class="panel panel-default">
                            <div class="panel-heading center-text">
                                Resumen de actividades
                            </div>
                        </div>
                        <div class="panel-body" style="padding: 0">
                            <table id="table_activities" st-table="trader_activities" class="table table-bordered ">
                                <thead>
                                <tr>
                                    <th><h5>Instrumento</h5></th>
                                    <th><h5>Postura</h5></th>
                                    <th><h5>Tasa ponderada</h5></th>
                                    <th><h5>P&L</h5></th>
                                </tr>
                                </thead>
                                <tbody class="table-striped">
                                <tr ng-repeat="activity in data.activity.data">
                                    <th>{{activity.instrument}}</th><!--TODO check names-->
                                    <th>{{activity.standing}}</th>
                                    <th>{{activity.rate}}</th>
                                    <th>{{activity.profitAndLoss}}</th>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                        <div id="ticket-summary" class="panel panel-default">
                            <div class="panel-heading center-text">
                                Tickets
                            </div>
                        </div>
                        <div class="panel-body" style="padding: 0">
                            <table id="table_tickets" st-table="trader_tickets" class="table table-bordered ">
                                <thead>
                                <tr>
                                    <th><h5>Fecha</h5></th>
                                    <th><h5>Instrumento</h5></th>
                                    <th><h5>Comprador</h5></th>
                                    <th><h5>Vendedor</h5></th>
                                    <th><h5>Tasa</h5></th>
                                    <th><h5>Monto</h5></th>
                                    <th><h5>Monto Real</h5></th>
                                    <th><h5>Titulos</h5></th>
                                    <th><h5>Precio sucio</h5></th>
                                </tr>
                                </thead>
                                <tbody class="table-striped">
                                <tr ng-repeat="ticket in data.tickets.data">
                                    <th>{{ticket.dateTime}}</th><!--TODO check names-->
                                    <th>{{ticket.instrument}}</th>
                                    <th>{{ticket.buyer}}</th>
                                    <th>{{ticket.seller}}</th>
                                    <th>{{ticket.rate}}</th>
                                    <th>{{ticket.amount}}</th>
                                    <th>{{ticket.realAmount}}</th>
                                    <th>{{ticket.titles}}</th>
                                    <th>{{ticket.price}}</th>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">

        </div>
    </div>
</div>
<%@include file="components/component_end_page_scripts.jsp" %>
<%@include file="components/component_trader_table_end_page_scripts.jsp" %>

</body>
</html>
