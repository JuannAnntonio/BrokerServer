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
    if (dataContext.has_market_offer && dataContext.mkt_offer_amount && dataContext.mkt_offer_rate) {
        return "<button class='btn btn-default btn-xs' style='font-size: 7px'><i ng-click='aggress_offer(" + value + ",\"" + bidding_type.OFFER + "\")' " +
            "class='fa fa-check-square fa-fw fa-2x' style='color:#088E4A'></i></button>";
    } else if (dataContext.in_aggression_offer && dataContext.mkt_offer_amount && dataContext.mkt_offer_rate) {
        return "<button class='btn btn-default btn-xs' style='font-size: 7px' ng-click='cancel_aggression_offer(" + value + ")'><i " +
            "class='fa fa-times-circle fa-fw fa-2x' style='color:#CA390C'></i></button>";
    } else {
        return "<button class='btn btn-default btn-xs' style='font-size: 7px' disabled><i class='fa fa-check-square fa-fw fa-2x' style='color:#afc0d6'></i></button>";
    }
};
/**
 * Slick grid formatter. This formats the bid accept cell.
 * @param {Number} row The row of the cell formatted.
 * @param {Number} cell The column of the cell formatted.
 * @param value The value of the cell.
 * @returns {string} A String with the content of the cell
 */
var button_accept_bid_formatter = function (row, cell, value, columnDef, dataContext) {
    if (dataContext.has_market_bid && dataContext.mkt_bid_amount && dataContext.mkt_bid_rate) {
        return "<button class='btn btn-default btn-xs' style='font-size: 7px'><i ng-click='aggress_bid(" + value + ",\"" + bidding_type.OFFER + "\")' " +
            "class='fa fa-check-square fa-fw fa-2x' style='color:#088E4A'></i></button>";
    } else if (dataContext.in_aggression_bid && dataContext.mkt_bid_amount && dataContext.mkt_bid_rate) {
        return "<button class='btn btn-default btn-xs' style='font-size: 7px' ng-click='cancel_aggression_bid(" + value + ")'><i " +
            "class='fa fa-times-circle fa-fw fa-2x' style='color:#CA390C'></i></button>";
    } else {
        return "<button class='btn btn-default btn-xs' style='font-size: 7px' disabled><i class='fa fa-check-square fa-fw fa-2x' style='color:#afc0d6'></i></button>";
    }
};

/**
 * Slick grid formatter. This formats the button with the post button for offer.
 * @param {Number} row The row of the cell formatted.
 * @param {Number} cell The column of the cell formatted.
 * @param value The value of the cell.
 * @returns {string} A String with the content of the cell
 */
var button_post_offer_formatter = function (row, cell, value, columnDef, dataContext) {
    if(dataContext.is_offer_aggressed){
        return "<button class='btn btn-default btn-xs' style='font-size: 7px' disabled><i " +
            "class='fa fa-times-circle fa-fw fa-2x' style='color:#afc0d6''></i></button>";
    }else if (dataContext.has_posted_offer) {
        return "<button class='btn btn-default btn-xs' style='font-size: 7px' ng-click='cancel_offer(" + value + ")'><i " +
            "class='fa fa-times-circle fa-fw fa-2x' style='color:#CA390C'></i></button>";
    } else {
        return "<button class='btn btn-default btn-xs' style='font-size: 7px' ng-click='post_offer(" + value + ")' ><i " +
            "class='fa fa-pencil fa-fw fa-2x' style='color:#0E5681'></i></button>";
    }
};
/**
 * Slick grid formatter. This formats the button with the post button for bid.
 * @param {Number} row The row of the cell formatted.
 * @param {Number} cell The column of the cell formatted.
 * @param value The value of the cell.
 * @returns {string} A String with the content of the cell
 */
var button_post_bid_formatter = function (row, cell, value, columnDef, dataContext) {
    if(dataContext.is_bid_aggressed){
        return "<button class='btn btn-default btn-xs' style='font-size: 7px' disabled><i " +
            "class='fa fa-times-circle fa-fw fa-2x' style='color:#afc0d6''></i></button>";
    }else if (dataContext.has_posted_bid) {
        return "<button class='btn btn-default btn-xs' style='font-size: 7px' ng-click='cancel_bid(" + value + ")' ><i " +
            "class='fa fa-times-circle fa-fw fa-2x' style='color:#CA390C'></i></button>";
    } else {
        return "<button class='btn btn-default btn-xs' style='font-size: 7px' ng-click='post_bid(" + value + ")' ><i " +
            "class='fa fa-pencil fa-fw fa-2x' style='color:#0E5681'></i></button>";
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
 * Validates that the amount posted is a multiple of 10
 * @param value
 * @returns {*}
 */
var amountValidation = function (value) {
    if(value == ""){
        return {valid: true, msg: null};
    }
    if (value % 10 !== 0 || value == undefined || !value.length) {
        return {valid: false, msg: "El valor debe ser un multiplo de 10"};
    } else {
        return {valid: true, msg: null};
    }
};
