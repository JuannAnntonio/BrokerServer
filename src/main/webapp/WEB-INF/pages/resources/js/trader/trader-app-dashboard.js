var trader_dashboard = angular.module('traderDashboard', ['angular.morris', 'traderMarket.services']);


var urlLogout = domain + "logout";

var handleError = function (status) {

};

trader_dashboard.factory('TraderCache', function ($cacheFactory) {
    return $cacheFactory('trader_cache', {capacity: 15});
});

trader_dashboard.service("ticketService", function () {
    this.container = {data: undefined}
});
trader_dashboard.service("activityService", function () {
    this.container = {data: undefined}
});


trader_dashboard.controller('table_controller',
    ['$scope',
        '$http',
        '$interpolate',
        '$compile',
        'TraderCache',
        'TradeServices',
        'ticketService',
        'activityService',
        function ($scope, $http, $interpolate, $compile, cache, service, tickets, activity) {
            $scope.services = service;
            /**
             * SlickGrid asyncPostRender so angular directives can be compiled
             * with slickgrid formmaters
             * @param cellNode
             */
            function button_renderer(cellNode) {
                var interpolated = $interpolate($(cellNode).html())($scope);
                var linker = $compile(interpolated);
                var htmlElements = linker($scope);
                $(cellNode).empty();
                $(cellNode).append(htmlElements);
            }

            /**
             * Slickgrid variables
             */
            var $this = this;
            $scope.grid = {};
            $scope.grid.table = null;
            $scope.grid.data = [];
            $scope.grid.options = {
                editable: true,
                enableAddRow: false,
                enableCellNavigation: true,
                asyncEditorLoading: false,
                enableAsyncPostRender: true,
                enableColumnReorder: false
            };
            $scope.grid.columns = [
                {
                    id: "dxv",
                    name: "DxV",
                    field: "dxv",
                    sortable: false,
                    width: 45,
                    minWidth: 45,
                    maxWidth: 45
                },
                {
                    id: "instrument",
                    name: "Instrumento",
                    field: "instrument",
                    sortable: false,
                    cssClass: "cell-title",
                    width: 140,
                    minWidth: 140,
                    maxWidth: 140
                },
                {
                    id: "accept_bid",
                    name: "BT",
                    field: "id",
                    sortable: false,
                    formatter: button_accept_bid_formatter,
                    asyncPostRender: button_renderer,
                    width: 30,
                    minWidth: 30,
                    maxWidth: 30
                },
                {
                    id: "mkt_bid_amount",
                    name: "Monto",
                    field: "mkt_bid_amount",
                    sortable: false,
                    width: 60,
                    minWidth: 60,
                    maxWidth: 60//,
                    //formatter: mkt_cell_formatter
                },
                {
                    id: "mkt_bid_rate",
                    name: "Bid",
                    field: "mkt_bid_rate",
                    sortable: false,
                    width: 60,
                    minWidth: 60,
                    maxWidth: 60
                },
                {
                    id: "mkt_separator",
                    name: "",
                    field: "separator",
                    sortable: false,
                    width: 10,
                    minWidth: 10,
                    maxWidth: 10
                },
                {
                    id: "mkt_offer_rate",
                    name: "Oferta",
                    field: "mkt_offer_rate",
                    sortable: false,
                    width: 60,
                    minWidth: 60,
                    maxWidth: 60
                },
                {
                    id: "mkt_offer_amount",
                    name: "Monto",
                    field: "mkt_offer_amount",
                    sortable: false,
                    width: 60,
                    minWidth: 60,
                    maxWidth: 60
                },
                {
                    id: "accept_offer",
                    name: "BT",
                    field: "id",
                    sortable: false,
                    formatter: button_accept_offer_formatter,
                    asyncPostRender: button_renderer,
                    width: 30,
                    minWidth: 30,
                    maxWidth: 30
                },
                {
                    id: "post_bid",
                    name: "BT",
                    field: "id",
                    sortable: false,
                    formatter: button_post_bid_formatter,
                    asyncPostRender: button_renderer,
                    width: 30,
                    minWidth: 30,
                    maxWidth: 30
                },
                {
                    id: "pst_bid_amount",
                    field: "pst_bid_amount",
                    name: "Monto",
                    sortable: false,
                    editor: Slick.Editors.Integer,
                    validator: amountValidation,
                    width: 60,
                    minWidth: 60,
                    maxWidth: 60
                },
                {
                    id: "pst_bid_rate",
                    field: "pst_bid_rate",
                    name: "Bid",
                    sortable: false,
                    editor: Slick.Editors.Float,
                    width: 60,
                    minWidth: 60,
                    maxWidth: 60
                },
                {
                    id: "pst_separator",
                    name: "",
                    field: "separator",
                    sortable: false,
                    width: 10,
                    minWidth: 10,
                    maxWidth: 10
                },
                {
                    id: "pst_offer_rate",
                    field: "pst_offer_rate",
                    name: "Oferta",
                    sortable: false,
                    editor: Slick.Editors.Float,
                    width: 60,
                    minWidth: 60,
                    maxWidth: 60
                },
                {
                    id: "pst_offer_amount",
                    field: "pst_offer_amount",
                    name: "Monto",
                    sortable: false,
                    editor: Slick.Editors.Integer,
                    validator: amountValidation,
                    width: 60,
                    minWidth: 60,
                    maxWidth: 60
                },
                {
                    id: "post_offer",
                    name: "BT",
                    field: "id",
                    sortable: false,
                    formatter: button_post_offer_formatter,
                    asyncPostRender: button_renderer,
                    width: 30,
                    minWidth: 30,
                    maxWidth: 30
                },
                {
                    id: "cancel_mkt_add_post",
                    name: "BT",
                    sortable: false,
                    width: 30,
                    minWidth: 30,
                    maxWidth: 30//,
                    //formatter: button_post_offer_formatter,
                    //asyncPostRender: button_renderer
                }
            ];

            $scope.data = {
                instruments: undefined
            };

            /**
             * Funtion to build the table fields based on the data from the
             * server
             * @param data
             */
            $scope.build_table = function (data) {
                var i;
                for (i in data) {
                    var row = data[i];
                    var data_row = {
                        id: row.idVPV,
                        dxv: row.dxv,
                        separator: "-",
                        instrument: row.name,
                        is_bidding_on_market: false,
                        is_market_position_active: false,
                        is_my_aggression: false, //TODO change this variable name to be more descriptive
                        highlight: false,
                        hmkt_bid: false,
                        hmkt_offer: false,
                        row_index: i,
                        has_posted_bid: false,
                        has_posted_offer: false,
                        has_market_bid: false,
                        has_market_offer: false,
                        in_aggression_offer: false,
                        in_aggression_bid: false,
                        is_offer_aggressed: false,
                        is_bid_aggressed: false
                    };
                    $scope.grid.data.push(data_row);
                }
                $scope.grid.data_view = new Slick.Data.DataView();
                $scope.services.setDataView($scope.grid.data_view);
                $scope.grid.table = new Slick.Grid("#trader-table", $scope.grid.data_view, $scope.grid.columns, $scope.grid.options);
                $scope.grid.table.setSelectionModel(new Slick.CellSelectionModel());
                $scope.grid.data_view.onRowCountChanged.subscribe(function (e, args) {
                    $scope.grid.table.updateRowCount();
                    $scope.grid.table.render();
                });
                $scope.grid.data_view.onRowsChanged.subscribe(function (e, args) {
                    $scope.grid.table.invalidateRows(args.rows);
                    $scope.grid.table.render();
                });
                $scope.grid.data_view.setItems($scope.grid.data);
                $scope.grid.table.onValidationError.subscribe(function (e, args) {
                    var dialog = $("#error-message");
                    dialog.html("<p>" + args.validationResults.msg + "</p>");
                    dialog.dialog({
                        modal: true,
                        buttons: {
                            Ok: function () {
                                $(this).dialog("close");
                            }
                        },
                        position: {my: "center", at: "center", within: "#trader-table"}
                    });
                });
                $scope.grid.table.onCellChange.subscribe(function (e, cellData) {
                    highlight_new_position($scope.grid.table, cellData.row, cellData.cell, 7, $scope.grid.data_view);
                });
                $scope.grid.table.onBeforeEditCell.subscribe(function (e, cellData) {
                    if ((cellData.cell == 10 || cellData.cell == 11 || cellData.cell == 13 || cellData.cell == 14)
                        && !isCellEditable($scope.grid.table.getColumns(), cellData.cell, cellData.item)) {
                        return false;
                    }
                });
                $scope.services.connect(domain + "market", $scope.grid.data_view, $scope.grid.table, tickets, activity);
            };

            /**
             *
             */
            $scope.load_market_data = function (data_view, grid) {
                $http.get(urlMarketPostions).success(function (data) {
                    for (i in data) {
                        var position = data[i];
                        var row = data_view.getItemById(position.idVPV);
                        if (position.biddingType === bidding_type.BID) {
                            row.mkt_bid_amount = position.amount;
                            row.mkt_bid_rate = position.rate;
                        } else if (position.bidding_type === bidding_type.OFFER) {
                            row.mkt_offer_amount = position.amount;
                            row.mkt_offer_rate = position.rate;
                        }
                        data_view.updateItem(position.idVPV, row);
                        grid.render();
                    }
                }).error(function (data) {
                    alert(JSON.stringify(data));

                });
            };

            $this.updateTablePosition = function (position, timeout) {
                var row = $scope.grid.data_view.getItemById(position.instrumentId);
                if (position.biddingType === bidding_type.BID) {
                    row.mkt_bid_amount = position.amount;
                    row.mkt_bid_rate = position.rate;
                    row.hmkt_bid = true;
                } else if (position.biddingType === bidding_type.OFFER) {
                    row.mkt_offer_amount = position.amount;
                    row.mkt_offer_rate = position.rate;
                    row.hmkt_offer = true;
                }
                $scope.grid.data_view.updateItem(position.instrumentId, row);
                var i;
                for (i = 0; i < timeout; i++) {
                    setTimeout(function () {
                        row.highlight = true;
                        if (position.biddingType === bidding_type.BID) {
                            row.hmkt_bid = true;
                        } else if (position.biddingType === bidding_type.OFFER) {
                            row.hmkt_offer = true;
                        }
                        $scope.grid.data_view.updateItem(position.instrumentId, row);
                    }, 500);
                    setTimeout(function () {
                        row.highlight = false;
                        $scope.grid.data_view.updateItem(position.instrumentId, row);
                    }, 500);

                }
                row.highlight = false;
                row.hmkt_bid = false;
                row.hmkt_offer = false;
            };

            /**
             * Method to initialize the controller
             */
            $scope.init = function () {
                if (cache.get("instruments") !== undefined) {
                    $scope.data.instruments = cache.get("instruments");
                    $scope.build_table(cache.get("instruments"));
                    $scope.grid.table.onCellChange.subscribe(function (e, cellData) {
                    });
                } else {
                    $http.get(urlInstruments)
                        .success(function (data) {
                                cache.put("instruments", data);
                                $scope.build_table(data);

                            }
                        ).error(function (status) {
                            handleError(status);
                        }
                    );
                }
            };
            $scope.post_bid = $scope.services.postBid;
            $scope.post_offer = $scope.services.postOffer;
            $scope.cancel_bid = $scope.services.cancelBid;
            $scope.cancel_offer = $scope.services.cancelOffer;
            $scope.aggress_bid = $scope.services.aggressBid;
            $scope.aggress_offer = $scope.services.aggressOffer;
            $scope.cancel_aggression_offer = $scope.services.cancelAggressionOffer;
            $scope.cancel_aggression_bid = $scope.services.cancelAggressionBid;

            $scope.init();
        }]);

trader_dashboard.controller('movements_controller',
    ['$scope',
        '$http',
        'TraderCache',
        'ticketService',
        'activityService',
        function ($scope, $http, cache, tickets, activity) {
            $scope.data = {
                activity: undefined,
                tickets: undefined
            };
            $scope.data.tickets = tickets.container;
            $scope.data.activity = activity.container;

            $scope.load_activities = function (refresh) {
                if (cache.get('activities') !== undefined && !refresh) {
                    $scope.data.activity = cache.get('activities');
                } else {
                    $http.get(urlActivity)
                        .success(function (data) {//TODO delete unused parameters
                                //$scope.data.activity = data;
                                activity.container.data = data;
                            }
                        ).error(function (status) {
                            handleError(status);
                        }
                    );
                }
            };

            $scope.load_tickets = function (refresh) {
                if (cache.get('tickets') !== undefined && !refresh) {
                    $scope.data.activity = cache.get('tickets');
                } else {
                    $http.get(urlTicket)
                        .success(function (data) {//TODO delete unused parameters
                                //$scope.data.tickets = data;
                                tickets.container.data = data;
                                cache.put('tickets', data);
                            }
                        ).error(function (status) {
                            handleError(status);
                        }
                    );
                }
            };
            $scope.load_activities(true);
            $scope.load_tickets(true);
        }]);

trader_dashboard.controller('graph_controller', ['$scope', '$http', 'TraderCache', function ($scope, $http, cache) {
    $scope.data = {
        dropdown_value: null,
        options: null,
        graph_data: null,
        insruments: null
    };
    $scope.load_graph_data = function (days) {
        var ldays = parseInt(days);
        var data = {days: ldays, instrument: $scope.data.dropdown_value};
        $http({url: urlGraph, method: "GET", params: data})
            .success(function (data) {
                $scope.data.graph_data = data.list;
                $scope.data.min = data.maxYield;
                $scope.data.max = data.minYield;
                $("#line-chart").empty();
                Morris.Line({
                    element: 'line-chart',
                    data: $scope.data.graph_data,
                    xkey: 'date',
                    ykeys: ['yield'],
                    labels: [$scope.data.dropdown_value],
                    lineColors: ["#FFB253"],
                    pointSize: 1,
                    ymin: $scope.data.min,
                    ymax: $scope.data.max,
                    yLabelFormat: function(y_element){
                        var s = y_element.toString();
                        var period = s.indexOf('.');
                        return s.substring(0,period+4);
                    }
                });
            }).error(function () {
            //TODO handle error
        })
    };
    if (cache.get("instruments") !== undefined) {
        $scope.data.options = cache.get("instruments");
    } else {
        $http.get(urlInstruments)
            .success(function (data) {
                $scope.data.options = data;
                cache.put("instruments", data);
            }).error(function () {
            //TODO handle error
        });
    }
    $scope.$watch(function () {
        $('#drop_down_graph_instrument').selectpicker('refresh');
    });
}]);

trader_dashboard.controller('navigation_controller', ['$scope', '$http', '$window', 'TraderCache', function ($scope, $http, $window) {
    var data = {
        _csrf: $('input[name="_csrf"]').val()
    };
    $scope.logout = function () {
        $http({
            method: "POST",
            url: urlLogout,
            params: data
        }).success(function () {
            $window.location.replace(domain);
        }).error(function (data, status) {
            alert("Error in logout status: " + status + " data:" + JSON.stringify(data));
        })
    }
}]);


trader_dashboard.directive('signalLastElement', function () {
    return {
        restrict: 'A',
        scope: true,
        link: function (scope) {
            if (scope.$last) {
                scope.$emit('lastElement')
            }

        }

    }
});
