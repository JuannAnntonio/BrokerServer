/**
 * Created by norberto on 15/02/2017.
 */

var updatePostPosition = function (data_view, data) {
    var row = data_view.getItemById(data.instrumentId);
    if (data.biddingType === bidding_type.BID) {
        row.pst_bid_amount = data.amount;
        row.pst_bid_rate = data.rate;
    } else if (data.bidding_type === bidding_type.OFFER) {
        row.pst_offer_rate = data.rate;
        row.pst_offer_amount = data.amount;
    }
    data_view.updateItem(data.instrumentId, row);
};

var enableAggressionButton = function (data_view, id, bidding_position) {
    var row = data_view.getItemById(id);
    if (bidding_position === bidding_type.BID) {
        row.has_market_bid = true;
    } else if (bidding_position === bidding_type.OFFER) {
        row.has_market_offer = true;
    }
    data_view.updateItem(id, row);
};

/**
 * Updates a new position in the table
 * @param data_view (DataView) The SlickGrid DataView
 * @param position {Object} A MArketPosition send from the server.
 * @param grid {SlicGrid} The SlickGrid table.
 */
var updateTablePosition = function (data_view, position, grid, highlight, clear_aggression) {
    var row = data_view.getItemById(position.instrumentId);
    var cells = [0, 0];
    if (highlight) {
        var animate = true;
    }
    if (position.biddingType === bidding_type.BID) {
        if (clear_aggression) {
            row.in_aggression_bid = false;
        }
        if (position.amount && position.rate) {
            row.mkt_bid_amount = position.amount;
            row.mkt_bid_rate = position.rate;
            row.hmkt_bid = true;
            cells[0] = 3;
            cells[1] = 4;
        } else {
            animate = false;
            row.mkt_bid_amount = "";
            row.mkt_bid_rate = "";
            row.has_posted_bid = false;
            row.has_market_bid = false;
        }
    } else if (position.biddingType === bidding_type.OFFER) {
        if (clear_aggression) {
            row.in_aggresion_offer = false;
        }
        if (position.amount && position.rate) {
            row.mkt_offer_amount = position.amount;
            row.mkt_offer_rate = position.rate;
            row.hmkt_offer = true;
            cells[0] = 6;
            cells[1] = 7;
        } else {
            animate = false;
            row.mkt_offer_amount = "";
            row.mkt_offer_rate = "";
            row.has_posted_offer = false;
            row.has_market_offer = false;
        }
    }
    data_view.updateItem(position.instrumentId, row);
    if (animate) {
        row.highlight = true;
        highlight_new_position_row(grid, row.row_index, cells[0], 11, row);
        highlight_new_position_row(grid, row.row_index, cells[1], 11, row);
    }
    //TODO REMOVE as this does not update the item the flags for animation must be set before each animate
    row.highlight = false;
    row.hmkt_bid = false;
    row.hmkt_offer = false;
};

var getCells = function (position) {
    var cells = [0, 0];
    if (position.biddingType === bidding_type.BID) {
        cells[0] = 3;
        cells[1] = 4;
    } else if (position.biddingType === bidding_type.OFFER) {
        cells[0] = 6;
        cells[1] = 7;
    }
    return cells;
};

var cancelPosition = function (data_view, grid, message) {
    var row = data_view.getItemById(message.instrumentId);
    if (message.biddingType == bidding_type.BID) {
        row.has_posted_bid = false;
        row.pst_bid_amount = "";
        row.pst_bid_rate = "";
    } else if (message.biddingType == bidding_type.OFFER) {
        row.has_posted_offer = false;
        row.pst_offer_amount = "";
        row.pst_offer_rate = "";
    }
    data_view.updateItem(message.instrumentId, row);
};

var cancelAggressionEnable = function (grid, data_view, position, timeout) {
    var cells = getCells(position);
    var row = data_view.getItemById(position.instrumentId);
    row.highlight = true;
    if (position.biddingType === bidding_type.BID) {
        row.in_aggression_bid = true;
        row.hmkt_bid = true;
    } else if (position.biddingType === bidding_type.OFFER) {
        row.in_aggression_offer = true;
        row.hmkt_offer = true;
    }
    data_view.updateItem(position.instrumentId, row);
    highlight_new_position_row(grid, row.row_index, cells[1], timeout, row);
    highlight_new_position_row(grid, row.row_index, cells[0], timeout, row);
};

var lockAggression = function (data_view, position, grid, timeout) {
    var cells = getCells(position);
    var row = data_view.getItemById(position.instrumentId);
    row.highlight = true;
    if (position.biddingType === bidding_type.BID) {
        row.has_market_bid = false;
        row.hmkt_bid = true;
        row.is_bid_aggressed = true;
    } else if (position.biddingType === bidding_type.OFFER) {
        row.has_market_offer = false;
        row.hmkt_offer = true;
        row.is_offer_aggressed = true;
    }
    data_view.updateItem(position.instrumentId, row);
    highlight_new_position_row(grid, row.row_index, cells[1], timeout, row);
    highlight_new_position_row(grid, row.row_index, cells[0], timeout, row);
};

var unlockAggression = function (data_view, position, grid) {
    var item = data_view.getItemById(position.instrumentId);
    updateTablePosition(data_view, position, grid, false, true);
    if (position.amount > 0) {
        if (position.biddingType == bidding_type.BID) {
            item.has_market_bid = true;
        } else if (position.biddingType == bidding_type.OFFER) {
            item.has_market_offer = true;
        }
    }
    if (position.biddingType == bidding_type.BID) {
        item.is_bid_aggressed = false;
    } else if (position.biddingType == bidding_type.OFFER) {
        item.is_offer_aggressed = false;
    }
    data_view.updateItem(position.instrumentId, item);
};


var loadPositionsInMarket = function (list, data_view) {
    var i = 0;
    for (i in list) {
        var market_position = list[i];
        var id = market_position.instrumentId;
        var row = data_view.getItemById(id);
        //TODO Add variable for the market to activate the agress button and also add in formatter.
        //TODO Add renderer to the agression button
        if (market_position.biddingType === bidding_type.BID) {
            row.mkt_bid_amount = market_position.amount;
            row.mkt_bid_rate = market_position.rate;
            row.has_market_bid = true;
        } else if (market_position.biddingType === bidding_type.OFFER) {
            row.mkt_offer_amount = market_position.amount;
            row.mkt_offer_rate = market_position.rate;
            row.has_market_offer = true;
        }
        data_view.updateItem(id, row)
    }


};

var clearPositionButton = function (data_view, id_instrument, bid_type) {
    var item = data_view.getItemById(id_instrument);
    if (bid_type === bidding_type.BID) {
        item.has_posted_bid = false;
    } else if (bid_type === bidding_type.OFFER) {
        item.has_posted_offer = false;
    }
    data_view.updateItem(id_instrument, item);
}


var loadPositionsForUser = function (list, data_view) {
    var i = 0;
    for (i in list) {
        var market_position = list[i];
        var id = market_position.instrumentId;
        var row = data_view.getItemById(id);
        //TODO Add variable for the market to activate the agress button and also add in formatter.
        //TODO Add renderer to the agression button
        if (market_position.biddingType === bidding_type.BID) {
            row.pst_bid_amount = market_position.amount;
            row.pst_bid_rate = market_position.rate;
            row.has_posted_bid = true;
        } else if (market_position.biddingType === bidding_type.OFFER) {
            row.pst_offer_amount = market_position.amount;
            row.pst_offer_rate = market_position.rate;
            row.has_posted_offer = true;
        }
        data_view.updateItem(id, row)
    }


};

/**
 * Checks if a cell can be edited this is used when there is a post in the market
 * @param cols
 * @param cell
 * @param item
 * @returns {boolean}
 */
var isCellEditable = function (cols, cell, item) {
    var ret_val = false;
    var col = cols[cell];
    if (col.id === "pst_offer_amount" || col.id === "pst_offer_rate") {
        if (!item.has_posted_offer) {
            ret_val = true;
        }
    } else if (col.id === "pst_bid_amount" || col.id === "pst_bid_rate") {
        if (!item.has_posted_bid) {
            ret_val = true;
        }
    }
    return ret_val;
};

/**
 * Highlights a cell from the table.
 * @param grid {SlickGrid} The table form SLickGrid.
 * @param row {Number} The row of the cell to highlight.
 * @param cell {Number} The column of the cell to highlight.
 * @param time {Number} The amount of time to highlight.
 */
var highlight_cell = function (grid, row, cell, time) {
    var i;
    var l_cell = grid.getCellNode(row, cell);
    for (i = 0; i < time; i++) {
        $(l_cell).effect("highlight", {color: "#FFC47C"}, 1000);
    }
};

/**
 * Higlights a new position in the market.
 * @param grid {SlickGrid} The table form SLickGrid.
 * @param row {Number} The row of the cell to highlight.
 * @param cell {Number} The column of the cell to highlight.
 * @param time {Number} The amount of time to highlight.
 * @param data_view {DataView} A slick grid DataView from the current grid.
 */
var highlight_new_position = function (grid, row, cell, time, data_view) {
    var col = grid.getColumns()[cell];
    var row_data = data_view.getItem(row);
    if (row_data.highlight) {
        if (row_data.hmkt_bid) {
            if (col.id === "mkt_bid_amount" || col.id === "mkt_bid_rate") {
                highlight_cell(grid, row, cell, time);
            }
        } else if (row_data.hmkt_offer) {
            if (col.id === "mkt_offer_amount" || col.id === "mkt_offer_rate") {
                highlight_cell(grid, row, cell, time);
            }
        }
    }
    row_data.highlight = false;
    row_data.hmkt_bid = false;
    row_data.hmkt_offer = false;
};

/**
 * Higlights a new position in the market.
 * @param grid {SlickGrid} The table form SLickGrid.
 * @param row {Number} The row of the cell to highlight.
 * @param cell {Number} The column of the cell to highlight.
 * @param time {Number} The amount of time to highlight.
 * @param row_data {Object} A slick grid Item from the DataView.
 */
var highlight_new_position_row = function (grid, row, cell, time, row_data) {
    var col = grid.getColumns()[cell];
    if (row_data.highlight) {
        if (row_data.hmkt_bid) {
            if (col.id === "mkt_bid_amount" || col.id === "mkt_bid_rate") {
                highlight_cell(grid, row, cell, time);
            }
        } else if (row_data.hmkt_offer) {
            if (col.id === "mkt_offer_amount" || col.id === "mkt_offer_rate") {
                highlight_cell(grid, row, cell, time);
            }
        }
    }
};