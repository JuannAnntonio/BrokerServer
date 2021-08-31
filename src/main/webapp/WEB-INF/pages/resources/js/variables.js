/**
 * Variables for the java script files
 * change the domain accordingly to the deployed host.
 * Version Control 2.0
 */
//prod->
//var domain = "https://bbfixed.com/sigmact_broker/";
//beta-> 
//var domain = "http://bbfixed-beta.com/sigmact_broker/";
//local -> 
var domain = "/sigmact_broker/";

//var domain = "http://216.55.178.98:9090/sigmact_broker/";
//var domain = "http://23.91.66.54:8080/sigmact_broker/";
//var domain = "http://sigmactdev.com:8080/sigmact_broker/"
//var domain = "http://devsigmact.com:8080/sigmact_broker/"
var urlLogout = domain + "logout";
var bidding_type = {
    BID: "Bid",
    OFFER: "Offer"
};
/*Trader services*/
var urlGraph = domain + "trader/rest/getTraderGraph";
var urlInstruments = domain + "trader/rest/getTraderInstruments";
var urlActivity = domain + "trader/rest/getTraderActivity";
var urlTicket = domain + "trader/rest/getTraderTickets";
var urlMarketPostions = domain + "trader/rest/marketPositions";