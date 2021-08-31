/**
 * Created by norberto on 15/02/2017.
 */
/*
 * slick grid formatters and renders
 */

/**
 * Slick grid formatter. This formats the offer accept cell.
 * @param {Number} row The row of the cell formatted.
 * @param {Number} cell The column of the cell formatted.
 * @param value The value of the cell.
 * @returns {string} A String with the content of the cell
 */
var button_accept_offer_formatter = function (row, cell, value, columnDef, dataContext) {
    var buttonStr = "";
    if (dataContext.has_market_offer && dataContext.mkt_offer_amount && dataContext.mkt_offer_rate) {
        if(dataContext.disabledRow)
            buttonStr = "<button class='btn btn-default btn-xs' style='font-size: 7px' disabled ng-click='aggress_offer(" + value + ",\"" + bidding_type.OFFER + "\")'><i  " +
            "class='fa fa-check-square fa-fw fa-2x' style='color:#088E4A'></i></button>";
        else
            buttonStr = "<button class='btn btn-default btn-xs' style='font-size: 7px' ng-click='aggress_offer(" + value + ",\"" + bidding_type.OFFER + "\")'><i  " +
            "class='fa fa-check-square fa-fw fa-2x' style='color:#088E4A'></i></button>";
    } else if (dataContext.in_aggression_offer && dataContext.mkt_offer_amount && dataContext.mkt_offer_rate) {
        buttonStr = "<button class='btn btn-default btn-xs' style='font-size: 7px' ng-click='cancel_aggression_offer(" + value + ")'><i " +
            "class='fa fa-times-circle fa-fw fa-2x' style='color:#CA390C'></i></button>";
    } else {
        buttonStr = "<button class='btn btn-default btn-xs' style='font-size: 7px' disabled><i class='fa fa-check-square fa-fw fa-2x' style='color:#afc0d6'></i></button>";
    }
    return buttonStr;
};
/**
 * Slick grid formatter. This formats the bid accept cell.
 * @param {Number} row The row of the cell formatted.
 * @param {Number} cell The column of the cell formatted.
 * @param value The value of the cell.
 * @returns {string} A String with the content of the cell
 */
var button_accept_bid_formatter = function (row, cell, value, columnDef, dataContext) {
    var buttonStr = "";
    if (dataContext.has_market_bid && dataContext.mkt_bid_amount && dataContext.mkt_bid_rate) {
        if(dataContext.disabledRow)
            buttonStr = "<button class='btn btn-default btn-xs' style='font-size: 7px' disabled ng-click='aggress_bid(" + value + ",\"" + bidding_type.OFFER + "\")'>" +
            "<i class='fa fa-check-square fa-fw fa-2x' style='color:#088E4A'></i></button>";
        else
            buttonStr = "<button class='btn btn-default btn-xs' style='font-size: 7px' ng-click='aggress_bid(" + value + ",\"" + bidding_type.OFFER + "\")'>" +
            "<i class='fa fa-check-square fa-fw fa-2x' style='color:#088E4A'></i></button>";
    } else if (dataContext.in_aggression_bid && dataContext.mkt_bid_amount && dataContext.mkt_bid_rate) {
        buttonStr = "<button class='btn btn-default btn-xs' style='font-size: 7px' ng-click='cancel_aggression_bid(" + value + ")'><i " +
            "class='fa fa-times-circle fa-fw fa-2x' style='color:#CA390C'></i></button>";
    } else {
        buttonStr = "<button class='btn btn-default btn-xs' style='font-size: 7px' disabled><i class='fa fa-check-square fa-fw fa-2x' style='color:#afc0d6'></i></button>";
    }
    return buttonStr;
};

/**
 * Slick grid formatter. This formats the button with the post button for offer.
 * @param {Number} row The row of the cell formatted.
 * @param {Number} cell The column of the cell formatted.
 * @param value The value of the cell.
 * @returns {string} A String with the content of the cell
 */
var button_post_offer_formatter = function (row, cell, value, columnDef, dataContext) {
    var buttonStr = ""
    if(dataContext.is_offer_aggressed){
        buttonStr= "<button class='btn btn-default btn-xs' style='font-size: 7px' disabled><i " +
            "class='fa fa-times-circle fa-fw fa-2x' style='color:#afc0d6''></i></button>";
    }else if (dataContext.has_posted_offer) {
        buttonStr = "<button class='btn btn-default btn-xs' style='font-size: 7px' ng-click='cancel_offer(" + value + ")'><i " +
            "class='fa fa-times-circle fa-fw fa-2x' style='color:#CA390C'></i></button>";
    } else {
        if(dataContext.disabledRow)
            buttonStr = "<button class='btn btn-default btn-xs' style='font-size: 7px' disabled ng-click='post_offer(" + value + ")' ><i " +
            "class='fa fa-pencil fa-fw fa-2x' style='color:#0E5681'></i></button>";
        else
            buttonStr = "<button class='btn btn-default btn-xs' style='font-size: 7px' ng-click='post_offer(" + value + ")' ><i " +
            "class='fa fa-pencil fa-fw fa-2x' style='color:#0E5681'></i></button>";
    }
    //console.log("===> OFFER BTN",dataContext.disabledRow, buttonStr)
    return buttonStr
};
/**
 * Slick grid formatter. This formats the button with the post button for bid.
 * @param {Number} row The row of the cell formatted.
 * @param {Number} cell The column of the cell formatted.
 * @param value The value of the cell.
 * @returns {string} A String with the content of the cell
 */
var button_post_bid_formatter = function (row, cell, value, columnDef, dataContext) {
    var buttonStr = ""
    if(dataContext.is_bid_aggressed){
        buttonStr = "<button class='btn btn-default btn-xs' style='font-size: 7px' disabled><i " +
            "class='fa fa-times-circle fa-fw fa-2x' style='color:#afc0d6''></i></button>";
    }else if (dataContext.has_posted_bid) {
        buttonStr = "<button class='btn btn-default btn-xs' style='font-size: 7px' ng-click='cancel_bid(" + value + ")' ><i " +
            "class='fa fa-times-circle fa-fw fa-2x' style='color:#CA390C'></i></button>";
    } else {
        if(dataContext.disabledRow)
            buttonStr = "<button class='btn btn-default btn-xs' style='font-size: 7px' disabled ng-click='post_bid(" + value + ")' ><i " +
            "class='fa fa-pencil fa-fw fa-2x' style='color:#0E5681'></i></button>";
        else
            buttonStr = "<button class='btn btn-default btn-xs' style='font-size: 7px' ng-click='post_bid(" + value + ")' ><i " +
                "class='fa fa-pencil fa-fw fa-2x' style='color:#0E5681'></i></button>";
    }
    //console.log("===> BID BTN",dataContext.disabledRow, buttonStr)
    return buttonStr;
};
/**
 * Formatter to give format to the loaded data in the market
 * @param row {Number} The row being formatted starts at row 0
 * @param cell {Number} The cell number being formatted starts with cell 0
 * @param value The value associeted to display in this cell
 * @param columnDef The column definition with formatters and stuff like the id
 * @param dataContext Additional variables added while creating rows
 */
var mkt_cell_formatter = function (row, cell, value, columnDef, dataContext) {
    if (value === undefined) {
        value = 0
    }
    if (dataContext.highlight) {
        if (dataContext.hmkt_bid) {
            if (columnDef.id === "mkt_bid_amount" || columnDef.id === "mkt_bid_rate") {

                return "<span style='background-color: #e9f823' > " + value + "</span>";
            }
        } else if (dataContext.hmkt_offer) {
            if (columnDef.id === "mkt_offer_amount" || columnDef.id === "mkt_offer_rate") {
                return "<span style='background-color: #e9f823' > " + value + "</span>";
            }
        }
    } else {
        return value;
    }
};

/**
 * Formatter to give format to the loaded data in the market
 * @param row {Number} The row being formatted starts at row 0
 * @param cell {Number} The cell number being formatted starts with cell 0
 * @param value The value associeted to display in this cell
 * @param columnDef The column definition with formatters and stuff like the id
 * @param dataContext Additional variables added while creating rows
 */
var mkt_cell_bid_formatter_Detail = function (row, cell, value, columnDef, dataContext) {
    //if(value===undefined)return ''
	
	//console.log("[trader-slick][mkt_cell_bid_formatter_Detail]");
	
	//console.log("[trader-slick][mkt_cell_bid_formatter_Detail] dataContext: ");
	//console.log(dataContext);
	
	//console.log("[trader-slick][mkt_cell_bid_formatter_Detail] row: ");
	//console.log(row);
	
    var title = "<span>";
    if(dataContext.bidMarketDetail.length > 0) {
        title+= "Comisi\u00F3n "+formatFloat(dataContext.bidMarketDetail[0].surcharge,4)+" <br/>"
        title+= "Tasa Negociada "+formatFloat(dataContext.bidMarketDetail[0].rate,4)+" <br/>"
        title+= "Tasa Liquidaci\u00F3n "+formatFloat((dataContext.bidMarketDetail[0].rate+dataContext.bidMarketDetail[0].surcharge),4)+" <br/>"
    }
    title+="</span>"
    	
    var instrument = dataContext.instrument.split(" ");
	
	var result = "";
	
	if(instrument[0]=="M" || instrument[0]=="S" || instrument[0]=="BI"){
		//Columna Bid 1
		if(dataContext.has_posted_bid && dataContext.is_my_bid_active){
            result = "<span class=\"tip\"><label style='color:"+dataContext.highlightMyPositionTextBid+" '>" + ((value==undefined)?'':formatFloat(value,2)) +"</label>"+title+"</span>";
        } else if(dataContext.is_my_bid_active){
            result = "<span class=\"tip\"><label style='color: red'>" + ((value==undefined)?'':formatFloat(value,2)) +"</label>"+title+"</span>";
        } else
	    	result = "<span class=\"tip\">" + ((value==undefined)?'':formatFloat(value,2)) +title+"</span>";
		
	} else {

		if(dataContext.has_posted_bid && dataContext.is_my_bid_active)
			result = "<span class=\"tip\"><label style='color:"+dataContext.highlightMyPositionTextBid+" '>" + ((value==undefined)?'':formatFloat(value,4)) +"</label>"+title+"</span>";
        else if(dataContext.is_my_bid_active){
            result = "<span class=\"tip\"><label style='color: red'>" + ((value==undefined)?'':formatFloat(value,4)) +"</label>"+title+"</span>";
        } 
        else
	    	result = "<span class=\"tip\">" + ((value==undefined)?'':formatFloat(value,4)) +title+"</span>";
		
	}
    	
    return result;
  
};


/**
 * Formatter to give format to the loaded data in the market
 * @param row {Number} The row being formatted starts at row 0
 * @param cell {Number} The cell number being formatted starts with cell 0
 * @param value The value associeted to display in this cell
 * @param columnDef The column definition with formatters and stuff like the id
 * @param dataContext Additional variables added while creating rows
 */
var mkt_cell_bid_formatter_DetailClick = function (row, cell, value, columnDef, dataContext) {
    //if(value===undefined)return ''
    var title = "<span>";
    for (i in dataContext.bidMarketDetail) {
        title+= "Monto "+formatNumber(dataContext.bidMarketDetail[i].amount)+", Tasa Negociada "+formatFloat(dataContext.bidMarketDetail[i].rate,4)+" <br/>"
    }
    title+="</span>"
    	//Columna de monto 1
    if(dataContext.has_posted_bid && dataContext.is_my_bid_active){
        return "<span class=\"tip\"><label style='color:"+dataContext.highlightMyPositionTextBid+" '>" + ((value==undefined)?'':value) +"</label>"+title+"</span>";
    } else if(dataContext.is_my_bid_active){
        return "<span class=\"tip\"><label style='color: red'>" + ((value==undefined)?'':value) +"</label>"+title+"</span>";
    } else
        return "<span class=\"tip\">"+ ((value==undefined)?'':value) + title+"</span>";
    //return "<span class=\"tip\"><label style='color: #0d6aad'>" + ((value==undefined)?'':value) +"</label>"+title+"</span>";

};

/**
 * Formatter to give format to the loaded data in the market
 * @param row {Number} The row being formatted starts at row 0
 * @param cell {Number} The cell number being formatted starts with cell 0
 * @param value The value associeted to display in this cell
 * @param columnDef The column definition with formatters and stuff like the id
 * @param dataContext Additional variables added while creating rows
 */
var mkt_cell_offer_formatter_Detail = function (row, cell, value, columnDef, dataContext) {
	
	//console.log("[trader-slick][mkt_cell_offer_formatter_Detail]");
	
	//console.log("[trader-slick][mkt_cell_offer_formatter_Detail] dataContext: ");
	//console.log(dataContext);
	
	//console.log("[trader-slick][mkt_cell_offer_formatter_Detail] row: ");
	//console.log(row);
	
    //if(value===undefined)return ''
    var title = "<span>";
    if(dataContext.offerMarketDetail.length >0 ) {
        title+= "Comisi\u00F3n "+formatFloat(dataContext.offerMarketDetail[0].surcharge,4)+" <br/>"
        title+= "Tasa Negociada "+formatFloat(dataContext.offerMarketDetail[0].rate,4)+" <br/>"
        title+= "Tasa Liquidaci\u00F3n "+formatFloat((dataContext.offerMarketDetail[0].rate-dataContext.offerMarketDetail[0].surcharge),4)+" <br/>"

    }
    title+="</span>"
    
    var instrument = dataContext.instrument.split(" ");
	
	var result = "";
	
	if(instrument[0]=="M" || instrument[0]=="S" || instrument[0]=="BI"){
	    //Columna de offer
		if(dataContext.has_posted_offer && dataContext.is_my_offer_active)
			result = "<span class=\"tip\"><label style='color:"+dataContext.highlightMyPositionTextOffer+" '>" + ((value==undefined)?'': formatFloat(value,2)) +"</label>"+title+"</span>";
        else if(dataContext.is_my_offer_active){
            result = "<span class=\"tip\"><label style='color: red'>" + ((value==undefined)?'':formatFloat(value,2)) +"</label>"+title+"</span>";
        } else
	    	result = "<span class=\"tip\">" + ((value==undefined)?'': formatFloat(value,2)) +title+"</span>";

	} else {

		if(dataContext.has_posted_offer && dataContext.is_my_offer_active)
			result = "<span class=\"tip\"><label style='color:"+dataContext.highlightMyPositionTextOffer+" '>" + ((value==undefined)?'': formatFloat(value,4)) +"</label>"+title+"</span>";
        else if(dataContext.is_my_offer_active){
            result = "<span class=\"tip\"><label style='color: red'>" + ((value==undefined)?'':formatFloat(value,4)) +"</label>"+title+"</span>";
        } else
	    	result = "<span class=\"tip\">" + ((value==undefined)?'': formatFloat(value,4)) +title+"</span>";

	}

	return result;

};


/**
 * Formatter to give format to the loaded data in the market
 * @param row {Number} The row being formatted starts at row 0
 * @param cell {Number} The cell number being formatted starts with cell 0
 * @param value The value associeted to display in this cell
 * @param columnDef The column definition with formatters and stuff like the id
 * @param dataContext Additional variables added while creating rows
 */
var mkt_cell_offer_formatter_DetailClick = function (row, cell, value, columnDef, dataContext) {
    //if(value===undefined)return ''
    var title = "<span>";
    for (i in dataContext.offerMarketDetail) {
        title+= "Monto "+formatNumber(dataContext.offerMarketDetail[i].amount)+" Tasa Negociada "+formatFloat(dataContext.offerMarketDetail[i].rate,4)+" <br/>"
    }
    title+="</span>"
    //Columna monto 2	
    if(dataContext.has_posted_offer && dataContext.is_my_offer_active)
        return "<span class=\"tip\"><label style='color:"+dataContext.highlightMyPositionTextOffer+" '>" + ((value==undefined)?'':value) +"</label>"+title+"</span>";
    else if(dataContext.is_my_offer_active){
            return "<span class=\"tip\"><label style='color: red'>" + ((value==undefined)?'':value) +"</label>"+title+"</span>";
    } else
        return "<span class=\"tip\">" + ((value==undefined)?'':value) +title+"</span>";

};


/**
 * Validates that the amount posted is a multiple of 5
 * @param value
 * @returns {*}
 */
var amountValidation = function (value) {
	
    if(value == ""){
        return {valid: false, msg: "Tu monto debe ser mayor a 0"};
    }
	if (value == 0 || value == undefined || !value.length) {
        return {valid: false, msg: "Tu monto debe ser mayor a 0"};
    }
    if (value % 5 !== 0 || value == undefined || !value.length) {
        return {valid: false, msg: "El valor debe ser un multiplo de 5"};
    } else {
        return {valid: true, msg: null};
    }
};



/**
 * Formatter to give format to the loaded rate
 * @param row {Number} The row being formatted starts at row 0
 * @param cell {Number} The cell number being formatted starts with cell 0
 * @param value The value associeted to display in this cell
 */
var bid_cell_formatter = function (row, cell, value, columnDef, dataContext) {

	/*
	console.log("[trader-slickgrid-formatters-validators.js][bid_cell_formatter]");

	console.log("row:");
	console.log(row);
	console.log("cell:");
	console.log(cell);
	console.log("value:");
	console.log(value);
	console.log("dataContext:");
	console.log(dataContext);
	*/
	
	var instrument = dataContext.instrument.split(" ");
	
	var result = "";
	
	if(instrument[0]=="M" || instrument[0]=="S" || instrument[0]=="BI"){
		
		result = (value===undefined || value ==="" ?"<span class='placeholderPost'>"+formatFloat(dataContext.yield,2) + "</span>":"<span>"+formatFloat(value,2)+"</span>") ;
		
	} else {

		result = (value===undefined || value ==="" ?"<span class='placeholderPost'>"+formatFloat(dataContext.yield,4) + "</span>":"<span>"+formatFloat(value,4)+"</span>") ;
		
	}
	
    //NuDecimal = (value - int(value)).toString().length;
    //return (value===undefined || value ==="" ?"<span class='placeholderPost'>"+formatFloat(dataContext.yield,NuDecimal) + "</span>":"<span>"+formatFloat(value,NuDecimal)+"</span>") ;
    return result;
};

var formatFloat = function formattedNumber(amount, decimal) {
    return amount.toLocaleString('es-MX', {minimumFractionDigits: decimal, maximumFractionDigits: decimal})
}

var formatNumber = function formattedNumber(amount) {
    return amount.toLocaleString('es-MX', {minimumFractionDigits: 0, maximumFractionDigits: 0})
}