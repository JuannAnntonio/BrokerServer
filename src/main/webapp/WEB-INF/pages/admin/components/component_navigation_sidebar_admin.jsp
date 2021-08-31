<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authentication var="principal" property="principal" ></sec:authentication>
    <nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0; width: 100%">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="sr-only">Toggle navigation</span>
            </button>
            <a class="navbar-brand" href="preferences">
                <h2><i class="fa fa-user fa-fw">${principal.username}</i></h2>
            </a>

        </div>
        <div class="nav-center center-block image-centered" style="margin-top: 30px">
            <img class="" src="../resources/images/Logo_BB.png" alt="Bull and bear logo" height="68">
        </div>
        <ul class="nav navbar-top-links navbar-right" ng-controller="navigation_controller">
            <li class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" ng-click="logout()">
                    Salir : <span class="glyphicon glyphicon-log-out"> </span>
                </a>
            </li>
            <form type="hidden">
                <input type="hidden"
                       name="${_csrf.parameterName}"
                       value="${_csrf.token}"/>
            </form>
        </ul>
        <div class="navbar-default sidebar" role="navigation">
            <div class="sidebar-nav navbar-collapse">
                <ul class="nav" id="side-menu">
                    <li>
                        <a href="dashboard"><i class="fa fa-tachometer fa-fw"></i>Inicio</a>
                    </li>
                    <li>
                        <a href="institutions"><i class="fa fa-institution fa-fw"></i>Instituciones</a>
                    </li>
                    <li>
                        <a href="priceprovider"><i class="fa fa-dollar fa-fw"></i>Proveedores de Precios</a>
                    </li>
                    <li>
                        <a href="users"><i class="fa fa-users fa-fw"></i>Usuarios</a>
                    </li>
                    <li>
                        <a href="#"><i class="fa fa-folder-open fa-fw"></i> Reportes<span class="fa arrow"></span></a>
                        <ul class="nav nav-second-level">
                            <li>
                                <a href="report/ticket">Ticket</a>
                            </li>
                            <li>
                                <a href="report/report">Reportes</a>
                            </li>
                        </ul>
                    </li>
                </ul>
            </div>
            <!-- /.sidebar-collapse -->
        </div>
        <!-- /.navbar-static-side -->
    </nav>