/**
 * Utility modules and functions for managing the bidding table
 * Created on 09/12/16.
 */

angular.module('traderMarket.services', [])
    .constant('sockJsProtocols', [])
    .factory('addAlert', [function() {
        return function(mensaje) {
            var dialog = $("#error-message");
            dialog.html("<p><i class='glyphicon glyphicon-warning-sign'></i> " + mensaje + "</p>");
            dialog.dialog({
                modal: true,
                buttons: {
                    Ok: function() {
                        $(this).dialog("close");
                    }
                },
                position: { my: "center", at: "center", within: "#trader-table" }
            });
        }
    }])
    .factory('StompClient', ['sockJsProtocols', '$q', function(sockJsProtocols, $q) {
        var stompClient;
        

        
        var wrappedSocket = {
            /**
             * Creates the new stomp cient. The client is inner to the module.
             */
        		        		
            init: function(url) {
            	
            	console.log("url: ");
            	console.log(url);
            	
            	console.log("sockJsProtocols: ");
            	console.log(sockJsProtocols);
            	
                if (sockJsProtocols.length > 0) {
                    stompClient = Stomp.over(new SockJS(url, null, { transports: sockJsProtocols }));
                } else {
                    stompClient = Stomp.over(new SockJS(url));
                }
            },
            /**
             * Connect returns the frame of the connection or the
             * @returns {String | Object} Returns a string with a message if the client was created or a JSON
             * with the frame response of the connetion.
             */
            connect: function() {

                console.log("[stomp-client][traderMarket.services][StompClient][connect]");

                return $q(function(resolve, reject) {
                    if (!stompClient) {
                        reject("STOMP client not created");
                    } else {
                        stompClient.connect({}, function(frame) {

                            console.log("[stomp-client][traderMarket.services][StompClient][connect] 2");
                        	
                        	console.log("frame:");
                        	console.log(frame);
                        	
                            resolve(frame);
                        }, function(error) {
                            reject("STOMP protocol error " + error);
                        });
                    }
                });
            },
            /**
             * Disconnects from the server
             */
            disconnect: function() {
                stompClient.disconnect();
            },
            /**
             *
             * @param {String} destination The channel for subscripion it uses the notify api because it never stops sending new
             * messages
             * @returns {Promise | Object} Returns the Object parsed from the channel as  many times as there are
             * messages on the channel.
             */
            subscribe: function(destination) {

                console.log("[stomp-client][traderMarket.services][StompClient][subscribe]");
                console.log("[stomp-client][traderMarket.services][StompClient][subscribe] destination: " + destination);

                var deferred = $q.defer();
                if (!stompClient) {
                    deferred.reject("STOMP client not created");
                } else {
                    stompClient.subscribe(destination, function(message) {

                        console.log("[stomp-client][traderMarket.services][StompClient][subscribe] message.body: " + message.body);
                    	
                        deferred.notify(JSON.parse(message.body));
                    });
                }
                return deferred.promise;
            },
            /**
             * Subscribes to a channel and resolves he first message after the first promise is resolved the
             * subscription expires.
             * @param destination
             * @returns {Object} A json message answer from the server
             */
            subscribeSingle: function(destination) {

                console.log("[stomp-client][traderMarket.services][StompClient][subscribeSingle]");

                return $q(function(resolve, reject) {
                    if (!stompClient) {
                        reject("STOMP client not created");
                    } else {
                        stompClient.subscribe(destination, function(message) {
                            resolve(JSON.parse(message.body));
                        });
                    }
                });
            },
            /**
             * Send a message to a channel
             * @param destination
             * @param headers
             * @param object
             */
            send: function(destination, headers, object) {
                stompClient.send(destination, headers, object);
            }
        };
        return wrappedSocket;
    }])
    /**
     * Create an object for market services.
     */
    .constant('BID', bidding_type.BID)
    .constant('OFFER', bidding_type.OFFER)
    .factory('TradeServices', ['StompClient', 'BID', 'OFFER', '$http', 'addAlert', function(stompClient, BID, OFFER, $http, addAlert) {

        return {
            date_view: undefined,
            getDataView: function() {
                return this.date_view;
            },
            setDataView: function(date_view) {
                this.date_view = date_view;
            },
            connect: function(url, data_view, grid, tickets, activity) {

                console.log("[stomp-client][traderMarket.services][TradeServices][connect]");


                stompClient.init(url);
                return stompClient.connect().then(function(frame) {
                	

                    console.log("[stomp-client][traderMarket.services][TradeServices][connect] 2");
                	
                    var suffix = frame.headers['queue-suffix'];
                    
                    console.log("suffix:");
                    console.log(suffix);

                    console.log("frame:");
                    console.log(frame);
                    
                    stompClient.subscribe("/market/announce").then(function() {
                            console.log("finished subscription")
                        },
                        function() {
                            console.log("error subscription")
                        },
                        function(marketMessage) {

                            console.log("[stomp-client][traderMarket.services][TradeServices][connect] subscribe('/market/announce')");
                            
                            console.log("marketMessage: ");
                            console.log(marketMessage);

                            if (marketMessage.code === 303) { // new position in market

                                console.log("[stomp-client.js][Code-303]");

                                if (marketMessage.message === "NEW") {
                                    //disabledRow(data_view, marketMessage.data, true)
                                    updateTablePosition(data_view, marketMessage.data, grid, true);
                                } else if (marketMessage.status === "CANCEL") {
                                    updateTablePosition(data_view, marketMessage.data, grid, false, false);
                                } else {
                                    updateTablePosition(data_view, marketMessage.data, grid, true);
                                }
                            } else if (marketMessage.code === 311) { //ENABLE AGGRESSION BUTTON
                                //disabledRow(data_view, marketMessage.data, false)
                                console.log("[stomp-client.js][Code-311]");
                                enableAggressionButton(data_view, marketMessage.data.instrumentId, marketMessage.data.biddingType);
                            } else if (marketMessage.code === 301) { // Lock table
                                //disabledRow(data_view, marketMessage.data, true)
                                console.log("[stomp-client.js][Code-301]");
                                lockAggression(data_view, marketMessage.data, grid, marketMessage.message);
                                //TODO DO not enable cancel in lock
                            } else if (marketMessage.code === 302) { // Unlock table aggression
                                //disabledRow(data_view, marketMessage.data, false)
                                console.log("[stomp-client.js][/market/announce][Code-302]");

                                //este playaudio es la versión final funcional
                                playAudio();

                                unlockAggression(data_view, marketMessage.data, grid);
                                
                                $http.get(urlActivity)
                                    .success(function(data) { //TODO delete unused parameters
                                        activity.container.data = data;
                                    }).error(function(status) {
                                        handleError(status);
                                    });
                                
                                $http.get(urlTicket)
                                    .success(function(data) { //TODO delete unused parameters
                                        tickets.container.data = data;
                                    }).error(function(status) {
                                        handleError(status);
                                    });
                                
                                console.log("[stomp-client.js][/market/announce][Code-302] Exit");

                            } else if (marketMessage.code === 309) { // Unlock table aggression
                                //disabledRow(data_view, marketMessage.data, false)
                                console.log("[stomp-client.js][/user/market/announcements][Code-309]");

                                unlockAggression(data_view, marketMessage.data, grid);
                                
                                $http.get(urlActivity)
                                    .success(function(data) { //TODO delete unused parameters
                                        activity.container.data = data;
                                    }).error(function(status) {
                                        handleError(status);
                                    });
                                
                                $http.get(urlTicket)
                                    .success(function(data) { //TODO delete unused parameters
                                        tickets.container.data = data;
                                    }).error(function(status) {
                                        handleError(status);
                                    });
                                
                                console.log("[stomp-client.js][/user/market/announcements][Code-309] Exit");

                            } else if (marketMessage.code === 320) { // Unlock table aggression
                                //disabledRow(data_view, marketMessage.data, false)
                                console.log("[stomp-client.js][Code-320]");
                                unlockAggression(data_view, marketMessage.data, grid);
                                $http.get(urlActivity)
                                    .success(function(data) { //TODO delete unused parameters
                                        activity.container.data = data;
                                    }).error(function(status) {
                                        handleError(status);
                                    });
                                $http.get(urlTicket)
                                    .success(function(data) { //TODO delete unused parameters
                                        tickets.container.data = data;
                                    }).error(function(status) {
                                        handleError(status);
                                    });
                                console.log("[stomp-client.js][Code-320] Exit");
                            } else if (marketMessage.code === 210) {

                                console.log("[stomp-client.js][Code-210]");

                                updatePostPosition(data_view, marketMessage.data);
                                
                                console.log("[stomp-client.js][Code-210]");
                            } else if (marketMessage.code === 211) {

                                console.log("[stomp-client.js][Code-211]");

                                updatePostPositionCancel211(data_view, marketMessage.data, false);
                                
                                console.log("[stomp-client.js][Code-211]");
                            } else if (marketMessage.code === 212) {

                                console.log("[stomp-client.js][Code-212]");

                                updatePostPositionCancel212(data_view, marketMessage.data, true);
                                
                                console.log("[stomp-client.js][Code-212]");
                            } 
                            //stompClient.unsubscribe("/market/announce");
                        });
                    stompClient.subscribe('/user/market/canceled').then(function() {}, function() {}, function(message) {
                        if (message.code === 201) {

                            console.log("[stomp-client.js][Code-201]");

                            cancelPosition(data_view, grid, message);
                            console.log("[stomp-client.js][Code-201]");
                        }
                        if (message.code === 501 && message.instrumentId != undefined && message.instrumentId != 0) {

                            console.log("[stomp-client.js][Code-501] 1");

                            cancelPosition(data_view, grid, message);
                            console.log("[stomp-client.js][Code-501-1]");
                        }
                        if (message.code === 501) {

                            console.log("[stomp-client.js][Code-501] 2");

                            for (var i = 0; i < grid.getDataLength(); i++) {
                                cancelPosition(data_view, grid, { instrumentId: grid.getDataItem(i).id, biddingType: bidding_type.BID });
                                cancelPosition(data_view, grid, { instrumentId: grid.getDataItem(i).id, biddingType: bidding_type.OFFER });
                                console.log("[stomp-client.js][Code-501-2]");
                            }
                        }
                    });

                    stompClient.subscribeSingle('/user/market/positions').then(function(list) {
                        console.log('/user/market/positions', list);
                        loadPositionsInMarket(list, data_view);
                    });
                    stompClient.subscribeSingle('/user/market/user_positions').then(function(list) {
                        loadPositionsForUser(list, data_view);
                    });
                    /*stompClient.subscribeSingle('/user/market/user_positions_detail').then(function (marketPositionDetail) {
                        loadPositionDetailForUser(marketPositionDetail,data_view);
                    });*/

                    stompClient.subscribe('/user/market/announcements').then(
                        function() {},
                        function() {},
                        function(data) {
                            if (data.code === 310) {
                                //agresor

                                // enable cancel aggression
                                console.log("[stomp-client.js][Code-310]");

                                cancelAggressionEnable(grid, data_view, data.data, data.message, { text: "#348900", background: "#a6e28d" });
                            }
                            if (data.code === 302) { // Unlock table aggression
                                //disabledRow(data_view, marketMessage.data, false)
                                console.log("[stomp-client.js][/user/market/announcements][Code-302]");

                                //este playaudio es la versión final funcional
                                playAudio();

                                unlockAggression(data_view, data.data, grid);
                                
                                $http.get(urlActivity)
                                    .success(function(data) { //TODO delete unused parameters
                                        activity.container.data = data;
                                    }).error(function(status) {
                                        handleError(status);
                                    });
                                
                                $http.get(urlTicket)
                                    .success(function(data) { //TODO delete unused parameters
                                        tickets.container.data = data;
                                    }).error(function(status) {
                                        handleError(status);
                                    });
                                
                                console.log("[stomp-client.js][/user/market/announcements][Code-302] Exit");

                            } 
                            if (data.code === 309) { // Unlock table aggression
                                //disabledRow(data_view, marketMessage.data, false)
                                console.log("[stomp-client.js][/user/market/announcements][Code-309]");


                                unlockAggression(data_view, data.data, grid);
                                
                                $http.get(urlActivity)
                                    .success(function(data) { //TODO delete unused parameters
                                        activity.container.data = data;
                                    }).error(function(status) {
                                        handleError(status);
                                    });
                                
                                $http.get(urlTicket)
                                    .success(function(data) { //TODO delete unused parameters
                                        tickets.container.data = data;
                                    }).error(function(status) {
                                        handleError(status);
                                    });
                                
                                console.log("[stomp-client.js][/user/market/announcements][Code-309] Exit");

                            } 
                            if (data.code === 315) {
                                console.log("[stomp-client.js][Code-315]");
                                //postulante o licitante

                                //hay que revisar que reciba el que postulo algún código de cancelación y si si poner una bandera.
                                //playAudio();

                                confirmedPostor(grid, data_view, data.data, data.message, { text: "#348900", background: "#a6e28d" });
                            } else if (data.code === 210) {

                                console.log("[stomp-client.js][Code-210]");

                                updatePostPosition(data_view, data.data);
                                
                                console.log("[stomp-client.js][Code-210]");
                            } else if (data.code === 211) {

                                console.log("[stomp-client.js][Code-211]");

                                updatePostPositionCancel211(data_view, data.data, false);
                                
                                console.log("[stomp-client.js][Code-211]");
                            } else if (data.code === 212) {

                                console.log("[stomp-client.js][Code-212]");

                                updatePostPositionCancel212(data_view, data.data, true);
                                
                                console.log("[stomp-client.js][Code-212]");
                            } else if (data.code === 213) {

                                console.log("[stomp-client.js][Code-213]");

                                updatePostPositionCleanCancel213(data_view, data.data, true);
                                
                                console.log("[stomp-client.js][Code-212]");
                            } else if (data.status === "Error") {
                                clearPositionButton(data_view, data.data.instrumentId, data.data.biddingType);
                            } else if (data.status === "Error aggression") {
                                messageErrorAggression(data);
                            }
                        });
                    stompClient.send('/BBBroker/getMarketPositions', {}, {});
                    stompClient.send('/BBBroker/getUserMarketPositions', {}, {});
                    
                    console.log("frame.headers['user-name']: ");
                    console.log(frame.headers['user-name']);
                    
                    return frame.headers['user-name'];
                });
            },
            disconnect: function() {
                stompClient.disconnect();
            },
            postBid: function(id) {

                console.log("[stomp-client.js][postBid]");
                var input = $("#monto").val();
                console.log("[stomp-client][stomp-client.js]");

                Slick.GlobalEditorLock.commitCurrentEdit();
                var row = this.services.getDataView().getItemById(id);

                console.log("POST_BID::", row);
                var principal_name = document.getElementById("principalName").value;
                console.log("### principal_name::", principal_name);

                var service = this.services;
                var marketPosition = {
                    instrumentId: row.id,
                    biddingType: BID,
                    rate: row.pst_bid_rate,
                    amount: row.pst_bid_amount,
                    userName: principal_name
                };
                //TODO manage aggression logic

                console.log("BID::", row.pst_bid_rate);
                console.log("NU_RANGO::", row.nuRango);
                console.log("MARCA_AGUA::", row.yield);

                if(row.instrument[0]=="M" || row.instrument[0]=="S" || row.instrument[0]=="BI"){
                    var limiteInferior = formatFloat(row.yield-row.nuRango,2);
                    var limiteSuperior = formatFloat(row.yield+row.nuRango,2);
                } else{
                    var limiteInferior = formatFloat(row.yield-row.nuRango,4);
                    var limiteSuperior = formatFloat(row.yield+row.nuRango,4);
                }
                
                var isLimite  = row.pst_bid_rate<limiteInferior || row.pst_bid_rate>limiteSuperior;

                console.log("LIMITE_SUPERIOR::", limiteSuperior);
                console.log("LIMITE_INFERIOR::", limiteInferior);
                console.log("IS_LIMITE::", isLimite);


               if (row.has_market_offer
                    && row.pst_bid_rate === row.mkt_offer_rate
                    && (row.pst_bid_amount <= row.mkt_offer_amount || row.pst_bid_amount > row.mkt_offer_amount)
                    && row.pst_bid_rate === row.pst_offer_rate) {

                    console.log("[stomp-client.js][postBid] 2da condición");

                    var msg = "Actualmente ya existe una postura con la misma tasa";
                    var warn = $('#warn-message');
                    warn.html(msg);
                    warn.fadeIn(400).delay(5000).fadeOut();
                } /*else if (row.has_market_offer && row.pst_bid_rate === row.mkt_offer_rate && row.pst_bid_amount > row.mkt_offer_amount) {
					console.log("row.has_market_offer",row.has_market_offer);
					console.log("row.pst_bid_rate",row.pst_bid_rate);
					console.log("row.mkt_offer_rate",row.mkt_offer_rate);
					console.log("row.pst_bid_amount",row.pst_bid_amount);
					console.log("row.mkt_offer_amount",row.mkt_offer_amount);
					
					
                    console.log("[stomp-client.js][postBid] 3era condición");

                    var msg = "Agrede la posición en el mercado primero";
                    var warn = $('#warn-message');
                    warn.html(msg);
                    warn.fadeIn(400).delay(5000).fadeOut();
                } */else if (row.has_market_offer && row.pst_bid_rate < row.mkt_offer_rate) {

                    console.log("[stomp-client.js][postBid] 4ta condición");

                    var msg = "El bid no puede ser menor a la oferta";
                    /*var warn = $('#danger-message');
                    warn.html(msg);
                    warn.fadeIn(400).delay(5000).fadeOut();
                     */
                    addAlert(msg);
                } else if (row.has_market_bid && row.pst_bid_rate > row.mkt_bid_rate && row.pst_bid_amount > 0) {

                    console.log("[stomp-client.js][postBid] 5ta condición");

                    var mensaje = "El bid no puede ser mayor al del mercado. Deseas entrar en profundidad&#63;";
                    var dialog = $("#error-message");
                    dialog.html("<p><i class='glyphicon glyphicon-question-sign'></i> " + mensaje + "</p>");
                    dialog.dialog({
                        modal: true,
                        buttons: {
                            Si: function() {
                                row.has_posted_bid = true;
                                service.getDataView().updateItem(row.id, row);
                                stompClient.send('/BBBroker/position', {}, JSON.stringify(marketPosition));
                                dialog.dialog("close");
                            },
                            No: function() {
                                dialog.dialog("close");
                            }
                        },
                        position: { my: "center", at: "center", within: "#trader-table" }
                    });
                } else if (isLimite) {
                    var mensaje = "La tasa esta fuera de valores de mercado. ¿Desea colocar la postura?";
                    var dialog = $("#error-message");
                    dialog.html("<p><i class='glyphicon glyphicon-question-sign'></i> " + mensaje + "</p>");
                    dialog.dialog({
                        modal: true,
                        buttons: {
                            Si: function () {
                                row.has_posted_bid = true;
                                service.getDataView().updateItem(row.id, row);
                                stompClient.send('/BBBroker/position', {}, JSON.stringify(marketPosition));
                                dialog.dialog("close");
                            },
                            No:function () {
                                dialog.dialog("close");
                            }
                        },
                        position: {my: "center", at: "center", within: "#trader-table"}
                    });
                } else if (row.has_market_offer
                    && row.pst_bid_rate === row.mkt_offer_rate
                    //&& row.pst_bid_amount <= row.mkt_offer_amount
                    && row.pst_bid_rate !== row.pst_offer_rate) {
                    
                    marketPosition.biddingType = OFFER;
                    stompClient.send('/BBBroker/aggress', {}, JSON.stringify(marketPosition));
                    
                    
                } else if (row.pst_bid_amount !== undefined && row.pst_bid_rate !== undefined &&
                    (input % 5 == 0 || input == undefined || !input.length) && input !== "0" && input !== "") {

                    console.log("[stomp-client.js][postBid] 6ta condición");
                    
                    console.log("[stomp-client.js] entra al if");
                    console.log("[stomp-client.js] pst_ bid: " + row.pst_bid_amount);
                    
                    row.has_posted_bid = true;
                    
                    this.services.getDataView().updateItem(row.id, row);
                    
                    stompClient.send('/BBBroker/position', {}, JSON.stringify(marketPosition));

                } else {

                    console.log("[stomp-client.js][postBid] 7ta condición");

                    var msg = undefined;
                    if (marketPosition.amount === undefined) {
                        msg = "El monto no esta definido";
                    } else {
                        msg = "El rendimiento no esta definido";
                    }
                    var warn = $('#warn-message');
                    warn.html(msg);
                    warn.fadeIn(400).delay(5000).fadeOut();

                }

            },
            postOffer: function(id) {

                var input = $("#monto").val();

                Slick.GlobalEditorLock.commitCurrentEdit();
                var row = this.services.getDataView().getItemById(id);

                row.disabledRow = true;
                this.services.getDataView().updateItem(id, row);

                var principal_name = document.getElementById("principalName").value;
                console.log("### principal_name::", principal_name);

                var service = this.services;
                var marketPosition = {
                    instrumentId: row.id,
                    biddingType: OFFER,
                    rate: row.pst_offer_rate,
                    amount: row.pst_offer_amount,
                    userName: principal_name
                };

                console.log("OFFER::", row.pst_offer_rate);
                console.log("NU_RANGO::", row.nuRango);
                console.log("MARCA_AGUA::", row.yield);

                if(row.instrument[0]=="M" || row.instrument[0]=="S" || row.instrument[0]=="BI"){
                    var limiteInferior = formatFloat(row.yield-row.nuRango,2);
                    var limiteSuperior = formatFloat(row.yield+row.nuRango,2);
                } else{
                    var limiteInferior = formatFloat(row.yield-row.nuRango,4);
                    var limiteSuperior = formatFloat(row.yield+row.nuRango,4);
                }
                
                var isLimite  = row.pst_offer_rate<limiteInferior || row.pst_offer_rate>limiteSuperior;

                console.log("LIMITE_SUPERIOR::", limiteSuperior);
                console.log("LIMITE_INFERIOR::", limiteInferior);
                console.log("IS_LIMITE::", isLimite);


                if (row.has_market_bid && row.mkt_bid_rate === row.pst_offer_rate
                    && row.pst_offer_amount <= row.mkt_bid_amount && row.pst_bid_rate === row.pst_offer_rate) {
                    var msg = "Actualmente ya existe una postura con la misma tasa";
                    var warn = $('#warn-message');
                    warn.html(msg);
                    warn.fadeIn(400).delay(5000).fadeOut();
                } /*else if (row.has_market_bid && row.pst_offer_rate === row.mkt_bid_rate && row.pst_offer_amount < row.mkt_bid_amount) {

                    console.log("[stomp-client.js][postBid] 3era condición");
					
					console.log("row.has_market_bid",row.has_market_bid);
					console.log("row.pst_offer_rate",row.pst_offer_rate);
					console.log("row.mkt_bid_rate",row.mkt_bid_rate);
					console.log("row.pst_offer_amount",row.pst_offer_amount);
					console.log("row.mkt_bid_amount",row.mkt_bid_amount);
					
                    var msg = "Agrede la posición en el mercado primero";
                    var warn = $('#warn-message');
                    warn.html(msg);
                    warn.fadeIn(400).delay(5000).fadeOut();
                } */else if (row.has_market_bid && row.pst_offer_rate > row.mkt_bid_rate) {

                    console.log("[stomp-client.js][postBid] 4ta condición");

                    var msg = "La oferta no puede ser mayor al bid";
                    /*var warn = $('#danger-message');
                    warn.html(msg);
                    warn.fadeIn(400).delay(5000).fadeOut();*/
                    addAlert(msg);
                } else if (row.has_market_offer && row.pst_offer_rate < row.mkt_offer_rate && row.pst_offer_amount > 0) {

                    console.log("[stomp-client.js][postBid] 5ta condición");

                    var mensaje = "La oferta no puede ser menor a la del mercado. Deseas entrar en profundidad&#63;";
                    var dialog = $("#error-message");
                    dialog.html("<p><i class='glyphicon glyphicon-question-sign'></i> " + mensaje + "</p>");
                    dialog.dialog({
                        modal: true,
                        buttons: {
                            Si: function() {
                                row.has_posted_offer = true;
                                service.getDataView().updateItem(row.id, row);
                                stompClient.send('/BBBroker/position', {}, JSON.stringify(marketPosition));
                                dialog.dialog("close");
                            },
                            No: function() {
                                dialog.dialog("close");
                            }
                        },
                        position: { my: "center", at: "center", within: "#trader-table" }
                    });
                } else if (isLimite) {
                    var mensaje = "La tasa esta fuera de valores de mercado. ¿Desea colocar la postura?";
                    var dialog = $("#error-message");
                        dialog.html("<p><i class='glyphicon glyphicon-question-sign'></i> " + mensaje + "</p>");
                        dialog.dialog({
                            modal: true,
                            buttons: {
                                Si: function () {
                                    row.has_posted_offer = true;
                                    service.getDataView().updateItem(row.id, row);
                                    stompClient.send('/BBBroker/position', {}, JSON.stringify(marketPosition));
                                    dialog.dialog("close");
                                },
                                No:function () {
                                    dialog.dialog("close");
                                }
                            },
                            position: {my: "center", at: "center", within: "#trader-table"}
                        });
                } else if (row.has_market_bid && row.mkt_bid_rate === row.pst_offer_rate
                    //&& row.pst_offer_amount <= row.mkt_bid_amount 
                    && row.pst_bid_rate !== row.pst_offer_rate) {
                    marketPosition.biddingType = BID;
                    
                    console.log("ELSE_IF -> 717");
                    
                    stompClient.send('/BBBroker/aggress', {}, JSON.stringify(marketPosition));
                } else if (row.pst_offer_amount !== undefined && row.pst_offer_rate !== undefined &&
                    (input % 5 == 0 || input == undefined || !input.length) && input !== "0" && input !== "") {

                    console.log("[stomp-client.js][postBid] 6ta condición");


                    row.has_posted_offer = true;
                    this.services.getDataView().updateItem(row.id, row);
                    stompClient.send('/BBBroker/position', {}, JSON.stringify(marketPosition));
                    return;

                } else {
                    var msg = undefined;
                    if (marketPosition.amount === undefined) {
                        msg = "El monto no esta definido";
                    } else {
                        msg = "El rendimiento no esta definido";
                    }
                    var warn = $('#warn-message');
                    warn.html(msg);
                    warn.fadeIn(400).delay(5000).fadeOut();
                }

                row.disabledRow = false;
                this.services.getDataView().updateItem(id, row);

            },
            cancelOffer: function(id) {

                console.log("[stomp-client][cancelOffer]");

                var marketPosition = {
                    instrumentId: id,
                    biddingType: OFFER
                };
                stompClient.send('/BBBroker/cancelPosition', {}, JSON.stringify(marketPosition));
            },
            cancelBid: function(id) {

                console.log("[stomp-client][cancelBid]");

                var marketPosition = {
                    instrumentId: id,
                    biddingType: BID
                };
                stompClient.send('/BBBroker/cancelPosition', {}, JSON.stringify(marketPosition));
            },
            cancelAll: function() {
                stompClient.send('/BBBroker/cancelAllPosition', {}, {});
            },
            cancelAggressionBid: function(id) {

                console.log("[stomp-client][cancelAggressionBid] tache de cancelar izquierda");

                var marketPosition = {
                    instrumentId: id,
                    biddingType: BID
                };
                stompClient.send('/BBBroker/cancelAggression', {}, JSON.stringify(marketPosition));
            },
            cancelAggressionOffer: function(id) {

                console.log("[stomp-client][cancelAggressionOffer] tache de cancelar derecha");

                var marketPosition = {
                    instrumentId: id,
                    biddingType: OFFER
                };
                stompClient.send('/BBBroker/cancelAggression', {}, JSON.stringify(marketPosition));
            },
            aggressBid: function(id) {

                console.log("[stomp-client][aggressBid]");

                var row = this.services.getDataView().getItemById(id);
                row.disabledRow = true;
                this.services.getDataView().updateItem(id, row);
                var marketAggressions = {
                    instrumentId: id,
                    biddingType: BID,
                    amount: row.mkt_bid_amount,
                    rate: row.mkt_bid_rate
                };

                if (row.has_posted_bid && row.mkt_bid_rate == row.pst_bid_rate) {
                    var msg = "Estas agrediendo una posición con el mismo rendimiento que el que ofreces";
                    var warn = $('#warn-message');
                    warn.html(msg);
                    warn.fadeIn(400).delay(5000).fadeOut();
                    console.log("[stomp-client][aggressBid][if-error]");

                } else {
                    console.log("[stomp-client][aggressBid][else-error]");

                    stompClient.send('/BBBroker/aggress', {}, JSON.stringify(marketAggressions));
                    return;
                }

                row.disabledRow = false;
                this.services.getDataView().updateItem(id, row);
            },
            aggressOffer: function(id) {
                var row = this.services.getDataView().getItemById(id);
                row.disabledRow = true;
                this.services.getDataView().updateItem(id, row);
                var marketAggression = {
                    instrumentId: id,
                    biddingType: OFFER,
                    amount: row.mkt_offer_amount,
                    rate: row.mkt_offer_rate
                };
                if (row.has_posted_offer && row.mkt_offer_rate == row.pst_offer_rate) {
                    var msg = "Estas agrediendo una posicón con el mismo rendimiento que el que ofreces";
                    var warn = $('#warn-message');
                    warn.html(msg);
                    warn.fadeIn(400).delay(5000).fadeOut();
                } else {
                    stompClient.send('/BBBroker/aggress', {}, JSON.stringify(marketAggression));
                    return;
                }
                row.disabledRow = false;
                this.services.getDataView().updateItem(id, row);
            }
        };
    }]);
