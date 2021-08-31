/**
 * Created on 08/11/16.
 */

var url = domain + "backoffice/rest/getReporteCalculadoraPrecios";

$(document).ready(function() {

	console.log("[backoffice-reporteCalculadoraPrecios.js]");

	var table = $('#backoffice_reporte_calculadora_precios_table').DataTable({

        "pageLength": 25,   
        "ordering": true,
        responsive: true,
        dom: "<'row mb-3'<'col-sm-12 col-md-6 d-flex align-items-left justify-content-start'f><'col-sm-12 col-md-6 d-flex align-items-left justify-content-end'B>>" +
            "<'row'<'col-sm-12'tr>>" +
            "<'row'<'col-sm-12 col-md-5'i><'col-sm-12 col-md-7'p>>",
		select : true,
		buttons : [ {
			extend : 'pageLength',
			className : 'btn-outline-default'
		}, {
			extend : 'colvis',
			text : 'Column Visibility',
			titleAttr : 'Col visibility',
			className : 'btn-outline-default'
		}, {
			extend : 'collection',
			text : 'Export',
			buttons : [ { // meter librería jszip
				extend : 'excelHtml5',
				text : 'Excel',
				orientation : 'landscape',
				titleAttr : 'Generate Excel',
				className : 'btn-outline-default'
			}, {
				extend : 'csvHtml5',
				text : 'CSV',
				titleAttr : 'Generate CSV',
				className : 'btn-outline-default'
			}, {
				// se debe incluir libreria pdf maker
				extend : 'pdfHtml5',
				text : 'PDF',
				titleAttr : 'PDF',
				customize : function(doc) {
					// pageMargins [left, top, right, bottom]
					doc.pageMargins = [ 20, 20, 20, 20 ];
				},
				className : 'btn-outline-default'
			} ],
			className : 'btn-outline-default'

		}, {
			extend : 'copyHtml5',
			text : 'Copy',
			titleAttr : 'Copy to clipboard',
			className : 'btn-outline-default'
		}, {
			extend : 'print',
			text : '<i class="fal fa-print"></i>',
			titleAttr : 'Print Table',
			className : 'btn-outline-default'
		}

		],
		columns : [ {
			"data" : "instrumento"
		}, {
			"data" : "fechaValuacion"
		}, {
			"data" : "dxV"
		}, {
			"data" : "tasaCupon"
		}, {
			"data" : "tasaValuacion"
		}, {
			"data" : "tasaMercado"
		}, {
			"data" : "valorUdi"
		}, {
			"data" : "inicioCupon"
		}, {
			"data" : "finCupon"
		}, {
			"data" : "periodoCupon"
		}, {
			"data" : "cuponesxVencer"
		}, {
			"data" : "precioSucio"
		}, {
            "data": null,
			"render": function ( data, type, row, meta ) {
				
				console.log("[backoffice-reporteCalculadorPrecios][datatable] render");
				
				/*
				console.log("row:");
				console.log(row);
				console.log(row.id_valmer_Price_Vector);
				*/
				
                return '<center><a href=\"reporteCalculadoraPreciosDetalles?id_valmer='+row.id_valmer_Price_Vector+'\"><i class=\"fa fa-bars fa-fw\"></i></a></center>';
            }
				
		}],
		dataSrc : function(json) {
			
			console.log("[backoffice-dashboard][dataSrc]");
			
			var data = json.data;
			return data;
		},
		language : {
			"lengthMenu" : "Mostrar _MENU_ resultados por pagina",
			"zeroRecords" : "No se encontro nada - disculpa",
			"info" : "Mostrando la página _PAGE_ de _PAGES_",
			"infoEmpty" : "Ningun registro disponible",
			"infoFiltered" : "(Filtrado de _MAX_ registros totales)",
			"search" : "Buscar:",
			"paginate" : {
				"first" : "Inicio",
				"last" : "Última",
				"next" : "Siguiente",
				"previous" : "Anterior"
			},
			"processing" : "Cargando...",
		}
	});
});
