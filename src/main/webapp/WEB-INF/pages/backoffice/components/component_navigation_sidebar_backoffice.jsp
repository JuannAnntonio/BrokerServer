    <nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="sr-only">Toggle navigation</span>
            </button>
            <a class="navbar-brand" href="dashboard">
                <img src="../resources/images/Boton_BB.png" height=68>
            </a>

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
            </form></ul>
        <div class="navbar-default sidebar" role="navigation">
            <div class="sidebar-nav navbar-collapse">
                <ul class="nav" id="side-menu">
                    <li>
                        <a href="dashboard"><i class="fa fa-tachometer fa-fw"></i>Inicio</a>
                    </li>
                </ul>
            </div>
            <!-- /.sidebar-collapse -->
        </div>
        <!-- /.navbar-static-side -->
    </nav>