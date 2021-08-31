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
                            <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.12.1/css/bootstrap-select.min.css" rel="stylesheet">

                            <link href="../resources/js/slickgrid/slick.grid.css" rel="stylesheet">
                            <link rel="stylesheet" href="https://code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css" type="text/css" />
                            <link rel="stylesheet" href="../resources/js/slickgrid/slick-custom-theme.css" type="text/css" />
                            <%@include file="components/component_header_java_script.jsp" %>
                                <link href="../resources/dist/css/brokerServer.css" rel="stylesheet">
                </head>
<style>
.alert-warning {
    color: #755e35;
    background-color: #fff3c1d1;
    border-color: #ffad02;
}
</style>
                <body style="overflow-x: scroll">
                    <div id="header" style="width: 100%; height: 200px">

                        <!--<iframe style="width: 100%; height: 23px; display: block; border: 0px; overflow: hidden;" src="https://es.dailyforex.com/forex-widget/widget/28524" height="23" width="100%" scrolling="no"></iframe> -->
                        <!-- TradingView Widget BEGIN -->
                        <div class="tradingview-widget-container">
                            <div class="tradingview-widget-container__widget"></div>
                            <script type="text/javascript" src="https://s3.tradingview.com/external-embedding/embed-widget-ticker-tape.js" async>
                                {
                                    "symbols": [{
                                        "proName": "OANDA:SPX500USD",
                                        "title": "S&P 500"
                                    }, {
                                        "proName": "OANDA:NAS100USD",
                                        "title": "Nasdaq 100"
                                    }, {
                                        "proName": "FX_IDC:EURUSD",
                                        "title": "EUR/USD"
                                    }, {
                                        "proName": "BITSTAMP:BTCUSD",
                                        "title": "BTC/USD"
                                    }, {
                                        "proName": "BITSTAMP:ETHUSD",
                                        "title": "ETH/USD"
                                    }, {
                                        "description": "",
                                        "proName": "OANDA:USDMXN"
                                    }],
                                    "colorTheme": "dark",
                                    "isTransparent": false,
                                    "displayMode": "adaptive",
                                    "locale": "es"
                                }
                            </script>
                        </div>
                        <!-- TradingView Widget END -->

                        <%@include file="components/component_navigation_sidebar_trader.jsp" %>
                    </div>
                    <div id="wrapper">
                        <div id="page-wrapper-trader" style="height: 100%">
                            <div class="panel panel-default" id="table-div" ng-controller="table_controller" style="width: 900px; float: left; height: 100%">

                                <div class="panel-heading center-text" style="padding: 4px">
                                    <button class="btn btn-danger" ng-click="cancel_all()">Limpiar operaciones <i class="glyphicon glyphicon-warning-sign"></i></button></h2>
                                </div>
                                <div id="warn-message" class="alert alert-warning" style="display: none;"></div>
                                <div id="danger-message" class="alert alert-danger" style="display: none;"></div>

                                <div class="panel-body" style="height: 100%">
                                    <div class="row">
                                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                                    </div>
                                    <div class="row" style="height: 100%">
                                        <audio id="myAudio">
									  <source src="../resources/sonido.mp3" type="audio/mpeg">
									</audio>
                                        <div id="trader-table" style="height: 100%"></div>
                                    </div>
                                </div>
                            </div>
                            <div style="display: flow-root; height: 100%; width: calc(100% - 920px); overflow-x: scroll">
                                <div id="instrument-statistics" class="row" style="max-height: 486px; max-width: calc(100% - 10px)" ng-controller="graph_controller">
                                    <!--<div id="instrument-statistics" class="panel panel-default" style="height: 486px; overflow-x: scroll;" ng-controller="graph_controller">-->
                                    <div class="panel panel-default">
                                        <div class="panel-heading center-text">
                                            <h2 class="panel-title">Desempeño del instrumento</h2>
                                        </div>
                                        <div class="panel-body">
                                            <div id="graph_form_div" class="btn-block">
                                                <form id="graph_form">
                                                    <div class="form-group">
                                                        <label class="col-sm-2 col-lg-2" control-label for="drop_down_graph_instrument">Instrumento: </label>
                                                        <select id="drop_down_graph_instrument" class="selectpicker col-sm-4 col-lg-4" data-live-search="true" ng-model="data.dropdown_value">
                                        <option data-ng-repeat="option in data.options" value="{{option.name}}">
                                            {{option.name}}
                                        </option>
                                    </select>

                                                        <button class="btn btn-default" ng-click="load_graph_data(1)" style="background-color:#F1592A; color: white">
                                        1D
                                    </button>
                                                        <button class="btn btn-default" ng-click="load_graph_data(7)" style="background-color:#F1592A; color: white">
                                        1S
                                    </button>
                                                        <button class="btn btn-default" ng-click="load_graph_data(30)" style="background-color:#F1592A; color: white">
                                        1M
                                    </button>
                                                        <button class="btn btn-default" ng-click="load_graph_data(90)" style="background-color:#F1592A; color: white">
                                        3M
                                    </button>
                                                        <button class="btn btn-default" ng-click="load_graph_data(180)" style="background-color:#F1592A; color: white">
                                        6M
                                    </button>
                                                        <button class="btn btn-default" ng-click="load_graph_data(365)" style="background-color:#F1592A; color: white">
                                        1A
                                    </button>
                                                        <button class="btn btn-default" ng-click="load_graph_data(730)" style="background-color:#F1592A; color: white">
                                        2A
                                    </button>
                                                        <button class="btn btn-default" ng-click="cleanGraph()" style="background-color:#F1592A; color: white"><i class="glyphicon glyphicon-erase"></i></button>
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
                                <div id="activity-summary" class="row" style="padding-left: 5px; max-width: 100%; overflow-x: scroll; max-height: 200px; max-height: calc(100% - 139px)" ng-controller="movements_controller">
                                    <div class="panel panel-default" style="height: 50%; overflow: scroll">
                                        <div class="panel-heading center-text">
                                            Resumen de actividades
                                        </div>
                                        <div class="panel-body wrapperPanel">
                                            <table id="table_activities" st-table="trader_activities" class="table table-bordered ">
                                                <thead>
                                                    <tr>
                                                        <th>
                                                            <h5>Instrumento</h5>
                                                        </th>
                                                        <th>
                                                            <h5>Posici&oacute;n</h5>
                                                        </th>
                                                        <th>
                                                            <h5>Tasa ponderada</h5>
                                                        </th>
                                                        <th>
                                                            <h5>P&L</h5>
                                                        </th>
                                                    </tr>
                                                </thead>
                                                <tbody class="table-striped">
                                                    <tr ng-repeat="activity in data.activity.data">
                                                        <th>{{activity.instrument}}</th>
                                                        <!--TODO check names-->
                                                        <th>{{activity.standing|number:2}}</th>
                                                        <th>{{activity.rate|number:4}}</th>
                                                        <th>{{activity.profitAndLoss|number:2}}</th>
                                                    </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>

                                    <div id="ticket-summary" class="panel panel-default" style="height: 50%; overflow: scroll">
                                        <div class="panel-heading center-text">
                                            Tickets
                                        </div>

                                        <div class="panel-body wrapperPanel">
                                            <table id="table_tickets" st-table="trader_tickets" class="table table-bordered">
                                                <thead>
                                                    <tr>
                                                        <th>
                                                            <h5>Fecha</h5>
                                                        </th>
                                                        <th>
                                                            <h5>Instrumento</h5>
                                                        </th>
                                                        <th>
                                                            <h5>Operaci&oacute;n</h5>
                                                        </th>
                                                        <th>
                                                            <h5>Contraparte</h5>
                                                        </th>
                                                        <th>
                                                            <h5>Monto Nominal</h5>
                                                        </th>
                                                        <th>
                                                            <h5>Tasa Negociada</h5>
                                                        </th>
                                                        <th>
                                                            <h5>Tasa Comisi&oacute;n</h5>
                                                        </th>
                                                        <th>
                                                            <h5>Tasa Liquidaci&oacute;n</h5>
                                                        </th>
                                                        <th>
                                                            <h5>Precio sucio</h5>
                                                        </th>
                                                        <th>
                                                            <h5>Monto Liquidaci&oacute;n</h5>
                                                        </th>
                                                        <th>
                                                            <h5>Fecha Liquidaci&oacute;n</h5>
                                                        </th>
                                                        <th>
                                                            <h5>D&iacute;as por Vencer</h5>
                                                        </th>
                                                        <th>
                                                            <h5>T&iacute;tulos</h5>
                                                        </th>
                                                    </tr>
                                                </thead>
                                                <tbody class="table-striped">
                                                    <tr ng-repeat="ticket in data.tickets.data">
                                                        <th>{{ticket.dateTime}}</th>
                                                        <!--TODO check names-->
                                                        <th>{{ticket.instrument}}</th>
                                                        <th>{{ticket.operation}}</th>
                                                        <th>{{ticket.counterpart}}</th>
                                                        <th>{{ticket.amount|number:2}}</th>
                                                        <th>{{ticket.negotiatedRate|number:4}}</th>
                                                        <th>{{ticket.commissionRate|number:4}}</th>
                                                        <th>{{ticket.agressionRate|number:4}}</th>
                                                        <th>{{ticket.price|number:8}}</th>
                                                        <th>{{ticket.realAmount|number:2}}</th>
                                                        <th>{{ticket.liquidationDate}}</th>
                                                        <th>{{ticket.dxv}}</th>
                                                        <th>{{ticket.titles|number:0}}</th>
                                                    </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>

                                </div>
                            </div>
                        </div>
                    </div>

                    <%@include file="components/component_end_page_scripts.jsp" %>
                        <%@include file="components/component_trader_table_end_page_scripts.jsp" %>

                </body>
                <script>
                    function setCookie(name, value, days) {
                        var expires = "";
                        if (days) {
                            var date = new Date();
                            date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
                            expires = "; expires=" + date.toUTCString();
                        }
                        document.cookie = name + "=" + (value || "") + expires + "; path=/";
                    }

                    function getCookie(name) {
                        console.log("[functions][getCookie]");
                        var name = name + "=";
                        var ca = document.cookie.split(';');
                        for (var i = 0; i < ca.length; i++) {
                            var c = ca[i];
                            while (c.charAt(0) == ' ') c = c.substring(1);
                            if (c.indexOf(name) == 0) return c.substring(name.length, c.length);
                        }
                        return "";
                    }
                    var mute = false;
                    var x = document.getElementById("myAudio");

                    function playAudio() {
                        x.play();
                    }

                    function muteAudio1() {
                        x.muted = true;
                    }

                    function muteAudio2() {
                        x.muted = false;
                    }
                </script>

                </html>