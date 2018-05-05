<%--
  Created by IntelliJ IDEA.
  User: Gerardo
  Date: 25/10/16
  Time: 5:37 PM
--%>
<nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0">
    <div class="navbar-header" style="margin-top: 30px">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="sr-only">Toggle navigation</span>
        </button>
        <a class="navbar-brand" href="preferences">
            <h1><i class="fa fa-user fa-fw"></i></h1>
        </a>
    </div>
    <div class="nav-center center-block image-centered" style="margin-top: 30px">
        <img class="" src="../resources/images/Logo_BB.png" alt="Bull and bear logo" height="68">
    </div>
    <ul class="nav navbar-top-links navbar-right" ng-controller="navigation_controller">
        <li>
            <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                Bloquear: <i class="fa fa-unlock fa-fw"></i>
            </a>
        </li>
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
</nav>