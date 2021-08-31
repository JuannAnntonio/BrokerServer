/**
 * Created by norberto on 15/02/2017.
 */

var updatePostPosition = function (data_view, data) {

	console.log("[trader-table-behaviour][updatePostPosition]");
	
    var row = data_view.getItemById(data.instrumentId);

	console.log("data:");
	console.log(data);
	console.log("row:");
	console.log(row);
	
	console.log("data.biddingtype: ");
	console.log(data.biddingtype);

	console.log("bidding_type.OFFER: ");
	console.log(bidding_type.OFFER);
	
    if (data.biddingType === bidding_type.BID) {

    	console.log("BID");
    	
    	row.pst_bid_amount = data.amount;
        row.pst_bid_rate = data.rate;
        //row.has_posted_bid = true;
    
    } else if (data.biddingType === bidding_type.OFFER) {

    	console.log("OFFER");
    	
    	row.pst_offer_rate = data.rate;
        row.pst_offer_amount = data.amount;
        //row.has_posted_bid = true;
    
    }
    
    data_view.updateItem(data.instrumentId, row);
    
};

//profundidad mayor que puso postura y alguien le pegó a su postura
var updatePostPositionCancel211 = function (data_view, data, bool) {
	
	console.log("[trader-table-behaviour][updatePostPositionCancel211]");
	
    var row = data_view.getItemById(data.instrumentId);

	console.log("[trader-table-behaviour][updatePostPositionCancel211] data:");
	console.log(data);
	console.log("[trader-table-behaviour][updatePostPositionCancel211] row:");
	console.log(row);
	
	console.log("[trader-table-behaviour][updatePostPositionCancel211] data.biddingType: " + data.biddingType);
	console.log("[trader-table-behaviour][updatePostPositionCancel211] bidding_type.BID: " + bidding_type.BID);
	console.log("[trader-table-behaviour][updatePostPositionCancel211] bidding_type.OFFER: " + bidding_type.OFFER);
	
    if (data.biddingType === bidding_type.BID) {
    
    	console.log("BID");

    	row.pst_offer_rate = "";
        row.pst_offer_amount = "";
        row.has_posted_offer = bool;
        row.mkt_offer_amount = "";
        row.mkt_offer_rate = "";

        row.is_offer_aggressed = bool;
        row.in_aggresion_offer = bool;
        
    } else if (data.biddingType === bidding_type.OFFER) {

    	console.log("OFFER");
    	
    	row.pst_bid_amount = "";
        row.pst_bid_rate = "";
        row.has_posted_bid = bool;
        row.mkt_bid_amount = "";
        row.mkt_bid_rate = "";

        row.is_bid_aggressed = bool;
        row.in_aggression_bid = bool;
    
    }
    
    data_view.updateItem(data.instrumentId, row);
    
};

//profundidad mayor que le pega y sube
var updatePostPositionCancel212 = function (data_view, data, bool) {
	
	console.log("[trader-table-behaviour][updatePostPositionCancel212]");
	
    var row = data_view.getItemById(data.instrumentId);

	console.log("data:");
	console.log(data);
	console.log("row:");
	console.log(row);
	console.log("[trader-table-behaviour][updatePostPositionCancel212] data.biddingType: " + data.biddingType);
	console.log("[trader-table-behaviour][updatePostPositionCancel212] bidding_type.BID: " + bidding_type.BID);
	console.log("[trader-table-behaviour][updatePostPositionCancel212] bidding_type.OFFER: " + bidding_type.OFFER);
	console.log("[trader-table-behaviour][updatePostPositionCancel212] data.biddingType: " + typeof data.biddingType);
	console.log("[trader-table-behaviour][updatePostPositionCancel212] bidding_type.BID: " + typeof bidding_type.BID);
	console.log("[trader-table-behaviour][updatePostPositionCancel212] bidding_type.OFFER: " + typeof bidding_type.OFFER);

    if (data.biddingType === bidding_type.BID) {
    
    	console.log("BID");
    	
    	row.pst_bid_amount = data.amount;
        row.pst_bid_rate = data.rate;
        row.has_posted_bid = bool;
        row.mkt_offer_amount = "";
        row.mkt_offer_rate = "";
        
        row.is_offer_aggressed = false;
        row.in_aggresion_offer = false;
        
    } else if (data.biddingType === bidding_type.OFFER) {

    	console.log("OFFER");
    	
    	row.pst_offer_rate = data.rate;
        row.pst_offer_amount = data.amount;
        row.has_posted_offer = bool;
        row.mkt_bid_amount = "";
        row.mkt_bid_rate = "";
        
        row.is_bid_aggressed = false;
        row.in_aggression_bid = false;
    
    }
    
    data_view.updateItem(data.instrumentId, row);
    
};


//limpiar el botón cancel
var updatePostPositionCleanCancel213 = function (data_view, data, bool) {
	
	console.log("[trader-table-behaviour][updatePostPositionCleanCancel213]");
	
  var row = data_view.getItemById(data.instrumentId);

	console.log("data:");
	console.log(data);
	console.log("row:");
	console.log(row);
	console.log("[trader-table-behaviour][updatePostPositionCleanCancel213] data.biddingType: " + data.biddingType);
	console.log("[trader-table-behaviour][updatePostPositionCleanCancel213] bidding_type.BID: " + bidding_type.BID);
	console.log("[trader-table-behaviour][updatePostPositionCleanCancel213] bidding_type.OFFER: " + bidding_type.OFFER);
	console.log("[trader-table-behaviour][updatePostPositionCleanCancel213] data.biddingType: " + typeof data.biddingType);
	console.log("[trader-table-behaviour][updatePostPositionCleanCancel213] bidding_type.BID: " + typeof bidding_type.BID);
	console.log("[trader-table-behaviour][updatePostPositionCleanCancel213] bidding_type.OFFER: " + typeof bidding_type.OFFER);

  if (data.biddingType === bidding_type.BID) {
  
  	console.log("BID");
  	
  	row.pst_bid_amount = "";
	  row.pst_bid_rate = "";
	  row.has_posted_bid = false;
	  row.mkt_offer_amount = "";
	  row.mkt_offer_rate = "";
      
      row.is_offer_aggressed = false;
      row.in_aggresion_offer = false;
      
  } else if (data.biddingType === bidding_type.OFFER) {

  	console.log("OFFER");
  	
  	row.pst_offer_rate = "";
      row.pst_offer_amount = "";
      row.has_posted_offer = false;
      row.mkt_bid_amount = "";
      row.mkt_bid_rate = "";
      
      row.is_bid_aggressed = false;
      row.in_aggression_bid = false;
  
  }
  
  data_view.updateItem(data.instrumentId, row);
  
};

var enableAggressionButton = function (data_view, id, bidding_position) {
    var row = data_view.getItemById(id);
    row.disabledRow = false
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
var updateTablePosition = function (data_view, position, grid, highlight, habilitado) {
    console.log('updateTablePosition_35');

    var principalName = document.getElementById("principalName").value;
    var institutionName = document.getElementById("institutionName").value;

    console.log("POSITION::", position);
    console.log('principalName',principalName);
    console.log('institutionName',institutionName);
    console.log("::: ", position.username != principalName && institutionName==position.institucion);    
    
    var row = data_view.getItemById(position.instrumentId);
    if(row==undefined) return
    row.disabledRow = habilitado==undefined ?true:false;
    var cells = [0, 0];
    var color=undefined
    if (highlight) {
        var animate = true;
    }

    if (position.biddingType === bidding_type.BID) {
        color={text :row.highlightPositionTextBid,background: row.highlightPositionBid, time:2500}
        if (position.amount && position.rate) {
            row.mkt_bid_amount = position.amount;
            row.mkt_bid_rate = position.rate;
            row.hmkt_bid = true;
            /*if(row.mkt_bid_rate && row.pst_bid_rate && (row.mkt_bid_rate === row.pst_bid_rate)) row.is_my_bid_active = true
            else row.is_my_bid_active = false*/

            if(position.username != principalName && institutionName==position.institucion) row.is_my_bid_active = true
            else row.is_my_bid_active = false

            cells[0] = 3;
            cells[1] = 4;
        } else {
            animate = false;
            row.mkt_bid_amount = "";
            row.mkt_bid_rate = "";
            row.has_posted_bid = false;
            row.has_market_bid = false;
        }
        if(position.blockDetailList)
            row.bidMarketDetail = position.blockDetailList;
        if(row.has_posted_bid )//&& row.is_my_bid_active)
            color={text :row.highlightMyPositionTextBid,background: row.highlightMyPositionBid, time:2500}


    } else if (position.biddingType === bidding_type.OFFER) {
        color={text :row.highlightPositionTextOffer,background: row.highlightPositionOffer, time:2500}
        if (position.amount && position.rate) {
            row.mkt_offer_amount = position.amount;
            row.mkt_offer_rate = position.rate;
            row.hmkt_offer = true;
            /*if(row.mkt_offer_rate && row.pst_offer_rate && (row.mkt_offer_rate === row.pst_offer_rate)) row.is_my_offer_active = true
            else row.is_my_offer_active = false*/
            
            if(position.username != principalName && institutionName==position.institucion) row.is_my_offer_active = true
            else row.is_my_offer_active = false
            
            cells[0] = 6;
            cells[1] = 7;
        } else {
            animate = false;
            row.mkt_offer_amount = "";
            row.mkt_offer_rate = "";
            row.has_posted_offer = false;
            row.has_market_offer = false;
        }
        if(position.blockDetailList)
            row.offerMarketDetail = position.blockDetailList;
        if(row.has_posted_offer )//&& row.is_my_offer_active)
            color={text :row.highlightMyPositionTextOffer,background: row.highlightMyPositionOffer, time:2500}

    }
    console.log("row.is_my_offer_active:", row.is_my_offer_active);
    console.log("row.is_my_bid_active:", row.is_my_bid_active);
    
    data_view.updateItem(position.instrumentId, row);

    if (animate) {
        row.highlight = true;
        highlight_new_position_row(grid, row.row_index, cells[0], 5, row,color);
        highlight_new_position_row(grid, row.row_index, cells[1], 5, row, color);
        //toolTipo_new_position_surcharge(grid, row.row_index, cell[1], row.surcharge)
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
    }
    if (message.biddingType == bidding_type.OFFER) {
        row.has_posted_offer = false;
        row.pst_offer_amount = "";
        row.pst_offer_rate = "";
    }
    data_view.updateItem(message.instrumentId, row);
};

var cancelAggressionEnable = function (grid, data_view, position, timeout,color) {
	
	console.log("[trader-table-behavior][cancelAggressionEnable]");
	
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
    highlight_new_position_row(grid, row.row_index, cells[1], timeout, row,color);
    highlight_new_position_row(grid, row.row_index, cells[0], timeout, row,color);
};

var messageErrorAggression = function(dat){
    //var msg = "No es posible agredir una postura de tu misma institución";
    var warn = $('#warn-message');
    warn.html(dat.message);
    warn.fadeIn(400).delay(5000).fadeOut();
};

var confirmedPostor = function (grid,data_view, position, timeout,color) {
    var cells = getCells(position);
    var row = data_view.getItemById(position.instrumentId);
    row.highlight = true;
    data_view.updateItem(position.instrumentId, row);
    highlight_new_position_row(grid, row.row_index, cells[1], timeout, row,color);
    highlight_new_position_row(grid, row.row_index, cells[0], timeout, row,color);
};

var lockAggression = function (data_view, position, grid, timeout) {
    var cells = getCells(position);
    var row = data_view.getItemById(position.instrumentId);
    row.highlight = true;
    row.disabledRow = true;
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
    updateTablePosition(data_view, position, grid, false, false)
    if (position.amount > 0) {
        if (position.biddingType == bidding_type.BID) {
            item.has_market_bid = true;
        } else if (position.biddingType == bidding_type.OFFER) {
            item.has_market_offer = true;
        }
    }else{
        item.mkt_bid_amount = "";
        item.mkt_bid_rate = "";
    }
    if (position.biddingType == bidding_type.BID) {
        item.is_bid_aggressed = false;
        item.in_aggression_bid = false;
    } else if (position.biddingType == bidding_type.OFFER) {
        item.is_offer_aggressed = false;
        item.in_aggresion_offer = false;
    }
    item.disabledRow = false;
    data_view.updateItem(position.instrumentId, item);
};


var loadPositionsInMarket = function (list, data_view) {
    console.log('loadPositionsInMarket_203');
    var i = 0;
    for (i in list) {
        var market_position = list[i];
        var id = market_position.instrumentId;
        var row = data_view.getItemById(id);
        //TODO Add variable for the market to activate the agress button and also add in formatter.
        //TODO Add renderer to the agression button
        if (market_position.biddingType === bidding_type.BID) {
            row.mkt_bid_amount = market_position.amount;
            row.bidMarketDetail = market_position.blockDetailList
            row.mkt_bid_rate = market_position.rate;
            row.has_market_bid = true;
            //if(row.mkt_bid_rate && row.pst_bid_rate && (row.mkt_bid_rate === row.pst_bid_rate)) row.is_my_bid_active = true
            //else row.is_my_bid_active = false
            row.is_my_bid_active = market_position.sameInstitution;
        } else if (market_position.biddingType === bidding_type.OFFER) {
            row.mkt_offer_amount = market_position.amount;
            row.offerMarketDetail = market_position.blockDetailList
            row.mkt_offer_rate = market_position.rate;
            row.has_market_offer = true;
            //if(row.mkt_offer_rate && row.pst_offer_rate && (row.mkt_offer_rate === row.pst_offer_rate)) row.is_my_offer_active = true
            //else row.is_my_offer_active = false
            row.is_my_offer_active = market_position.sameInstitution;
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
            if(row.mkt_bid_rate && row.pst_bid_rate && (row.mkt_bid_rate === row.pst_bid_rate)) row.is_my_bid_active = true
            else row.is_my_offer_active =false
        } else if (market_position.biddingType === bidding_type.OFFER) {
            row.pst_offer_amount = market_position.amount;
            row.pst_offer_rate = market_position.rate;
            row.has_posted_offer = true;
            if(row.mkt_offer_rate && row.pst_offer_rate && (row.mkt_offer_rate === row.pst_offer_rate)) row.is_my_offer_active = true
            else row.is_my_offer_active =false
        }
        data_view.updateItem(id, row)
    }


};
/*
var loadPositionDetailForUser = function(marketPositionDetail,data_view){

    var market_position = marketPositionDetail;
    var id = market_position.instrumentId;
    var row = data_view.getItemById(id);
    if (market_position.biddingType === bidding_type.BID) {
        row.bidMarketDetail = market_position.blockDetailList;
        row.has_posted_bid = true;
    } else if (market_position.biddingType === bidding_type.OFFER) {
        row.offerMarketDetail = market_position.blockDetailList;
        row.has_posted_offer = true;
    }
    data_view.updateItem(id, row)
}*/

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
 * @param color {String} Color for aggressor, otherwise is undefined.
 */
var highlight_cell = function (grid, row, cell, time,color) {
    if(color === undefined) {
        color = {background : "#e2a641", text : "#bf6314", time: 1000}
    }
    if(color.time === undefined)
        color.time = 1000
    var i;
    var l_cell = grid.getCellNode(row, cell);
    for (i = 0; i < time; i++) {
        if($(l_cell).find("label")!=undefined)  $(l_cell).find("label").css("color",color.text)
        $(l_cell).effect("highlight", {color: color.background}, color.time).css("color", color.text);
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
 * @param color {String} color for aggressor, otherwise is undefined.
 */
var highlight_new_position_row = function (grid, row, cell, time, row_data,color) {
    var col = grid.getColumns()[cell];
    if (row_data.highlight) {
        if (row_data.hmkt_bid) {
            if (col.id === "mkt_bid_amount" || col.id === "mkt_bid_rate") {
                highlight_cell(grid, row, cell, time,color);
            }
        } else if (row_data.hmkt_offer) {
            if (col.id === "mkt_offer_amount" || col.id === "mkt_offer_rate") {
                highlight_cell(grid, row, cell, time,color);
            }
        }
    }
};

var disabledRow= function(data_view, position,status){
    var id = position.instrumentId;
    var row = data_view.getItemById(id);
    row.disabledRow = status;
    console.log(id,row,status);
    /*var $input = $("<INPUT type=text class='editor-text' />")
    if(status && $input != undefined)
        $input.attr("disabled","disabled")
    if(!status && $input != undefined)
        $input.removeAttr("disabled")*/
    data_view.updateItem(position.instrumentId, row);
};
