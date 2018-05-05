/**
 * Created on 21/11/2016.
 */
var url = domain +"admin/rest/getUsers";

var getUrlParameter = function getUrlParameter(sParam) {
    var sPageURL = decodeURIComponent(window.location.search.substring(1)),
        sURLVariables = sPageURL.split('&'),
        sParameterName,
        i;

    for (i = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split('=');

        if (sParameterName[0] === sParam) {
            return sParameterName[1] === undefined ? true : sParameterName[1];
        }
    }
};

$(document).ready(function() {
    var institutionName = getUrlParameter("institution");
    var data = {
        institution: getUrlParameter("institution")
    }
    $('#users_table').DataTable( {
        dom: 'lBfrtip',
        ajax: {
            url: url,
            data: data
        },
        columns: [
            { "data": "name" },
            { "data": "institution" },
            { "data": "profile" },
            { "data": "availableInstruments" },
            { "data": "active" },
            { "data": "viewDetails" }
        ],
        dataSrc : function (json) {
            var data = json.data;
            return data;
        },
        select: true,
        buttons: [
            {
                text: "<i class='fa fa-plus-circle fa-fw'></i>",
                action: function () {
                    window.location.href = 'addUser'; //relative to domain
                }
            }
        ],
        language: {
            "lengthMenu": "Mostrar _MENU_ resultados por pagina",
            "zeroRecords": "No se encontro nada - disculpa",
            "info": "Mostrando la página _PAGE_ de _PAGES_",
            "infoEmpty": "Ningun registro disponible",
            "infoFiltered": "(Filtrado de _MAX_ registros totales)",
            "search":"Buscar:",
            "paginate": {
                "first":      "Inicio",
                "last":       "Última",
                "next":       "Siguiente",
                "previous":   "Anterior"
            },
            "processing":     "Cargando..."
        }
    } );
} );
