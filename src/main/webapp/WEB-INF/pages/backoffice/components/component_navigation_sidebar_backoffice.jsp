<div style="margin-bottom: 0px;" class="tradingview-widget-container">
    <div class="tradingview-widget-container__widget"></div>
    <script type="text/javascript" src="https://s3.tradingview.com/external-embedding/embed-widget-ticker-tape.js" async>
    {
    "symbols": [
        {
        "title": "S&P 500",
        "proName": "OANDA:SPX500USD"
        },
        {
        "title": "Nasdaq 100",
        "proName": "OANDA:NAS100USD"
        },
        {
        "title": "EUR/USD",
        "proName": "FX_IDC:EURUSD"
        },
        {
        "title": "BTC/USD",
        "proName": "BITSTAMP:BTCUSD"
        },
        {
        "title": "ETH/USD",
        "proName": "BITSTAMP:ETHUSD"
        }
    ],
    "colorTheme": "dark",
    "isTransparent": false,
    "displayMode": "adaptive",
    "locale": "es"
    }
    </script>
</div>
<nav class="navbar navbar-default navbar-static-top" role="navigation"
	style="margin-bottom: 0">
	<div class="navbar-header">
		<button style="float: left; border: 0 !important;" type="button"
			class="navbar-toggle" data-toggle="collapse"
			data-target=".navbar-collapse">
			<i class="far fa-bars" style="color: white;"></i>
		</button>
		
		<div class="hidden-xs nav-center center-block"
			style="margin-top: 30px; margin: 40px auto; position: absolute; left: 50%; margin-left: -175px !important; display: block;">
			<img class="hidden-xs" src="../resources/images/Logo_BB.png"
				alt="Bull and bear logo" height="68"> 
		</div>
		<div class="hidden-sm hidden-md hidden-lgnav-center center-block"
			style="margin-top: 30px; margin: 40px auto; position: absolute; left: 50%; margin-left: -54px !important; display: block;">
			<img
				class="hidden-sm hidden-md hidden-lg"
				src="../resources/images/Boton_BB.png" height=68>
		</div>

	</div>
	<ul ng-controller="backoffice_controller_header" style="position: absolute; top: 0px; right: 0px;" class="nav navbar-top-links">
		<li style="cursor: pointer;" class="dropdown"><a class="dropdown-toggle"
			data-toggle="dropdown" ng-click="logout()"> Salir : <span
				class="glyphicon glyphicon-log-out"> </span>
		</a></li>
		<form type="hidden">
			<input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}" />
		</form>
	</ul>
	<div class="navbar-default sidebar" role="navigation">
		<div class="sidebar-nav navbar-collapse">
			<ul class="nav" id="side-menu">
			
                <li>
					<a href="reporteCartaConfirmacion"><i style="padding-right: 10px;" class="fa fa-tachometer fa-fw"></i>Reporte Operaciones</a>
				</li>
				<li class="">
					
					<a href="#"><i class="fa fa-folder-open fa-fw"></i> Otros Reportes<span class="fa arrow"></span></a>
					
					<ul class="nav nav-second-level collapse in" aria-expanded="true" style="">
                        
                        <li>
		                	<a href="dashboard"><i style="padding-right: 10px;" class="fa fa-tachometer fa-fw"></i>Reporte Personalizado</a>	
		                </li>
		                
						
                         <li ng-if='${idInstitution}==5'>
                             <a href="reporteCalculadoraPrecios"><i style="padding-right: 10px;" class="fa fa-tachometer fa-fw"></i>Reporte Calculadora de Precios</a>
                         </li>
                        
                     </ul>
				</li>
			</ul>
		</div>
		<!-- /.sidebar-collapse -->
	</div>
	<!-- /.navbar-static-side -->
</nav>
