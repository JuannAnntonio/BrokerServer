/**
 * Created on 08/11/16.
 */

var url = domain + "admin/rest/getInstitutionInfo";


$(document).ready(function () {
    var table = $('#institution_table').DataTable({
        dom: 'lBfrtip',
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
            {"data": "institutionName"},
            {"data": "activeUsers"},
            {"data": "activeBid"},
            {"data": "tradedLastDays"},
            {"data": "isActive"},
            {"data": "viewUsers"},
            {"data": "viewMatrix"}
        ],
        dataSrc: function (json) {
            var data = json.data;
            console.log("### DATA_ADMIN_INST:: ", data);
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

