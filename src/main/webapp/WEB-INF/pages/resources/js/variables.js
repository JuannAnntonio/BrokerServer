/**
 * Variables for the java script files
 * change the domain accordingly to the deployed host.
 */
var domain = "http://localhost:8081/sigmact_broker/";/*cambie el puerto 80*/
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