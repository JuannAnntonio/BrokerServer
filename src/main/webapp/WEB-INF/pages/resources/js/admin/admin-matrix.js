/**
 * Created on 08/11/16.
 */


$(document).ready(function () {
	console.log("[admin-matrix] entry");
    var table = $('#institutionmat').DataTable({
        dom: 'lBfrtip',
        select: true,
        buttons: [
            {
                text: "<i class='fa fa-plus-circle fa-fw'></i>",
                action: function () {
                    window.location.href = '#'; //relative to domain
                }
            }
        ],
        columns: [
            {"data": "idComision"},
            {"data": "idIntitution1"},
            {"data": "institutionName"},
            {"data": "instrument"},
            {"data": "comision"},
            {"data": "idIntitution2"},
            {"data": "institutionName2"},
            {"data": "boton"}
			
        ],
        language: {
            "lengthMenu": "Mostrar _MENU_ resultados por pagina",
            "zeroRecords": "No se encontro nada - disculpa",
            "info": "Mostrando la página _PAGE_ de _PAGES_",
            "infoEmpty": "Ningun registro disponible",
            "infoFiltered": "(Filtrado de _MAX_ registros totales)",
            "search": "Buscar:",
            "paginate": {
                "first": "Inicio",
                "last": "Última",
                "next": "Siguiente",
                "previous": "Anterior"
            },
            "processing": "Cargando...",
        }
    });
});

