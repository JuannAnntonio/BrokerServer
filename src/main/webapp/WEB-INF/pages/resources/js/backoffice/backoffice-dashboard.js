/**
 * Created on 08/11/16.
 */

var url = domain + "backoffice/rest/getDashboard";


$(document).ready(function () {
    var table = $('#backoffice_dashboard_table').DataTable({
        dom: 'lfrtip',
        select: true,
        buttons: [
            {
                text: "<i class='fa fa-plus-circle fa-fw'></i>",
                action: function () {
                    window.location.href = 'addInstitution'; //relative to domain
                }
            }
        ],
        ajax: url,
        columns: [
            {"data": "date"},
            {"data": "transactionType"},
            {"data": "instrument"},
            {"data": "buyer"},
            {"data": "seller"},
            {"data": "workbench"},
            {"data": "wbBuyPrice"},
            {"data": "wbSellPrice"},
            {"data": "amount"},
            {"data": "wbAmount"},
            {"data": "titles"},
            {"data": "rate"},
            {"data": "surcharge"},
            {"data": "systemCommission"}
        ],
        dataSrc: function (json) {
            var data = json.data;
            return data;
        },
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

