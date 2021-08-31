<%--
  Created by IntelliJ IDEA.
  User: Gerardo
  Date: 25/10/16
  Time: 5:37 PM
--%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authentication var="principal" property="principal" ></sec:authentication>
<nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0; width: 100%">
    <div class="navbar-header" style="margin-top: 30px">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="sr-only">Toggle navigation</span>
        </button>
        <span class="navbar-text">
            <h2><i class="fa fa-user fa-fw">${principal.username}</i></h2>
            ${institution}
        </span>
    </div>
    <div class="nav-center center-block image-centered" style="margin-top: 30px">
        <img class="" src="../resources/images/Logo_BB.png" alt="Bull and bear logo" height="68">
    </div>
    <ul class="nav navbar-top-links navbar-right" ng-controller="navigation_controller">
         
        <li>
            <a class="navbar-btn" ng-click="muteapp()">
                Sonido: <i ng-if="mut==true" class="fa fa-volume-off fa-fw"></i> <i ng-if="mut==false" class="fa fa-volume-up fa-fw"></i>

            </a>
        </li>
        
        <li>
            <a class="navbar-btn" ng-click="lockedApp()">
                Bloquear: <i ng-if="showDiv ==true" class="fa fa-lock fa-fw"></i> <i ng-if="showDiv ==false" class="fa fa-unlock fa-fw"></i>

            </a>
        </li>
        <li >
            <a class="navbar-btn" ng-click="logout()">
                Salir : <span class="glyphicon glyphicon-log-out"> </span>
            </a>
        </li>
        <form type="hidden">
            <input type="hidden"
                   name="${_csrf.parameterName}"
                   value="${_csrf.token}"/>
        </form>
        <input type="hidden"
                   id="principalName"
                   value="${principal.username}"/>
        <input type="hidden"
                   id="institutionName"
                   value="${institution}"/>
    </ul>
</nav>
<div id="lockedDiv">

</div>