/**
 * Utility modules and functions for managing the bidding table
 * Created on 09/12/16.
 */

angular.module('traderMarket.services', [])
    .constant('sockJsProtocols', [])
    .factory('StompClient', ['sockJsProtocols', '$q', function (sockJsProtocols, $q) {
        var stompClient;
        var wrappedSocket = {
            /**
             * Creates the new stomp cient. The client is inner to the module.
             */
            init: function (url) {
                if (sockJsProtocols.length > 0) {
                    stompClient = Stomp.over(new SockJS(url, null, {transports: sockJsProtocols}));
                }
                else {
                    stompClient = Stomp.over(new SockJS(url));
                }
            },
            /**
             * Connect returns the frame of the connection or the
             * @returns {String | Object} Returns a string with a message if the client was created or a JSON
             * with the frame response of the connetion.
             */
            connect: function () {
                return $q(function (resolve, reject) {
                    if (!stompClient) {
                        reject("STOMP client not created");
                    } else {
                        stompClient.connect({}, function (frame) {
                            resolve(frame);
                        }, function (error) {
                            reject("STOMP protocol error " + error);
                        });
                    }
                });
            },
            /**
             * Disconnects from the server
             */
            disconnect: function () {
                stompClient.disconnect();
            },
            /**
             *
             * @param {String} destination The channel for subscripion it uses the notify api because it never stops sending new
             * messages
             * @returns {Promise | Object} Returns the Object parsed from the channel as  many times as there are
             * messages on the channel.
             */
            subscribe: function (destination) {
                var deferred = $q.defer();
                if (!stompClient) {
                    deferred.reject("STOMP client not created");
                } else {
                    stompClient.subscribe(destination, function (message) {
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
            subscribeSingle: function (destination) {
                return $q(function (resolve, reject) {
                    if (!stompClient) {
                        reject("STOMP client not created");
                    } else {
                        stompClient.subscribe(destination, function (message) {
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
            send: function (destination, headers, object) {
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
    .factory('TradeServices', ['StompClient', 'BID', 'OFFER', '$http', function (stompClient, BID, OFFER, $http) {
        return {
            date_view: undefined,
            getDataView: function () {
                return this.date_view;
            },
            setDataView: function (date_view) {
                this.date_view = date_view;
            },
            connect: function (url, data_view, grid, tickets, activity) {
                stompClient.init(url);
                return stompClient.connect().then(function (frame) {
                    var suffix = frame.headers['queue-suffix'];
                    stompClient.subscribe("/market/announce").then(function () {
                            console.log("finished subscription")
                        },
                        function () {
                            console.log("error subscription")
                        },
                        function (marketMessage) {
                            if (marketMessage.code === 303) { // new position in market
                                if (marketMessage.message === "NEW") {
                                    updateTablePosition(data_view, marketMessage.data, grid, true);
                                } else {
                                    updateTablePosition(data_view, marketMessage.data, grid, false);
                                }
                            } else if (marketMessage.code === 311) { //ENABLE AGGRESSION BUTTON
                                enableAggressionButton(data_view, marketMessage.data.instrumentId, marketMessage.data.biddingType);
                            } else if (marketMessage.code === 301) { // Lock table
                                lockAggression(data_view, marketMessage.data, grid, marketMessage.message);
                                //TODO DO not enable cancel in lock
                            } else if (marketMessage.code === 302) { // Unlock table aggression
                                unlockAggression(data_view, marketMessage.data, grid);
                                $http.get(urlActivity)
                                    .success(function (data) {//TODO delete unused parameters
                                            activity.container.data = data;
                                        }
                                    ).error(function (status) {
                                        handleError(status);
                                    }
                                );
                                $http.get(urlTicket)
                                    .success(function (data) {//TODO delete unused parameters
                                            tickets.container.data = data;
                                        }
                                    ).error(function (status) {
                                        handleError(status);
                                    }
                                );
                            }
                        });
                    stompClient.subscribe('/user/market/canceled').then(function () {
                    }, function () {
                    }, function (message) {
                        if (message.code === 201) {
                            cancelPosition(data_view, grid, message);
                        }
                    });
                    stompClient.subscribeSingle('/user/market/positions').then(function (list) {
                        loadPositionsInMarket(list, data_view);
                    });
                    stompClient.subscribeSingle('/user/market/user_positions').then(function (list) {
                        loadPositionsForUser(list, data_view);
                    });

                    stompClient.subscribe('/user/market/announcements').then(
                        function () {
                        },
                        function () {
                        },
                        function (data) {
                            if (data.code === 310) {// enable cancel aggression
                                cancelAggressionEnable(grid, data_view, data.data, data.message);
                            }else if(data.code === 210){
                                updatePostPosition(data_view, data.data);
                            } else if (data.status === "Error") {
                                clearPositionButton(data_view, data.data.instrumentId, data.data.biddingType);
                            } else {
                            }
                        });
                    stompClient.send('/BBBroker/getMarketPositions', {}, {});
                    stompClient.send('/BBBroker/getUserMarketPositions', {}, {});
                    return frame.headers['user-name'];
                });
            },
            disconnect: function () {
                stompClient.disconnect();
            },
            postBid: function (id) {
                Slick.GlobalEditorLock.commitCurrentEdit();
                var row = this.services.getDataView().getItemById(id);
                var marketPosition = {
                    instrumentId: row.id,
                    biddingType: BID,
                    rate: row.pst_bid_rate,
                    amount: row.pst_bid_amount
                };
                //TODO manage aggression logic
                if (row.has_market_offer
                    && row.pst_bid_rate === row.mkt_offer_rate
                    && row.pst_bid_amount <= row.mkt_offer_amount
                    && row.pst_bid_rate !== row.pst_offer_rate) {
                    marketPosition.biddingType = OFFER;
                    stompClient.send('/BBBroker/aggress', {}, JSON.stringify(marketPosition));
                } else if (row.has_market_offer
                    && row.pst_bid_rate === row.mkt_offer_rate
                    && row.pst_bid_amount <= row.mkt_offer_amount
                    && row.pst_bid_rate === row.pst_offer_rate) {
                    var msg = "Cancela tu posición primero";
                    var warn = $('#warn-message');
                    warn.html(msg);
                    warn.fadeIn(400).delay(5000).fadeOut();
                }
                    else if (row.has_market_offer && row.pst_bid_rate === row.mkt_offer_rate && row.pst_bid_amount > row.mkt_offer_amount) {
                    var msg = "Agrede la posición en el mercado primero";
                    var warn = $('#warn-message');
                    warn.html(msg);
                    warn.fadeIn(400).delay(5000).fadeOut();
                } else if (row.has_market_offer && row.pst_bid_rate < row.mkt_offer_rate) {
                    var msg = "El bid no puede ser menor a la oferta";
                    var warn = $('#warn-message');
                    warn.html(msg);
                    warn.fadeIn(400).delay(5000).fadeOut();
                } else if (row.pst_bid_amount !== undefined && row.pst_bid_rate !== undefined) {
                    stompClient.send('/BBBroker/position', {}, JSON.stringify(marketPosition));
                    row.has_posted_bid = true;
                    this.services.getDataView().updateItem(row.id, row);
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

            },
            postOffer: function (id) {
                Slick.GlobalEditorLock.commitCurrentEdit();
                var row = this.services.getDataView().getItemById(id);
                var marketPosition = {
                    instrumentId: row.id,
                    biddingType: OFFER,
                    rate: row.pst_offer_rate,
                    amount: row.pst_offer_amount
                };
                if (row.has_market_bid && row.mkt_bid_rate === row.pst_offer_rate
                    && row.pst_offer_amount <= row.mkt_bid_amount && row.pst_bid_rate !== row.pst_offer_rate) {
                    marketPosition.biddingType = BID;
                    stompClient.send('/BBBroker/aggress', {}, JSON.stringify(marketPosition));
                }else if (row.has_market_bid && row.mkt_bid_rate === row.pst_offer_rate
                    && row.pst_offer_amount <= row.mkt_bid_amount && row.pst_bid_rate === row.pst_offer_rate) {
                    var msg = "Cancela tu posición primero";
                    var warn = $('#warn-message');
                    warn.html(msg);
                    warn.fadeIn(400).delay(5000).fadeOut();
                } else if (row.has_market_bid && row.pst_offer_rate === row.mkt_bid_rate && row.pst_offer_amount > row.mkt_bid_amount) {
                    var msg = "Agrede la posición en el mercado primero";
                    var warn = $('#warn-message');
                    warn.html(msg);
                    warn.fadeIn(400).delay(5000).fadeOut();
                } else if (row.has_market_bid && row.pst_offer_rate > row.mkt_bid_rate) {
                    var msg = "La oferta no puede ser mayor al bid";
                    var warn = $('#warn-message');
                    warn.html(msg);
                    warn.fadeIn(400).delay(5000).fadeOut();
                } else if (row.pst_offer_amount !== undefined && row.pst_offer_rate !== undefined) {
                    stompClient.send('/BBBroker/position', {}, JSON.stringify(marketPosition));
                    row.has_posted_offer = true;
                    this.services.getDataView().updateItem(row.id, row);
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
            },
            cancelOffer: function (id) {
                var marketPosition = {
                    instrumentId: id,
                    biddingType: OFFER
                };
                stompClient.send('/BBBroker/cancelPosition', {}, JSON.stringify(marketPosition));
            },
            cancelBid: function (id) {
                var marketPosition = {
                    instrumentId: id,
                    biddingType: BID
                };
                stompClient.send('/BBBroker/cancelPosition', {}, JSON.stringify(marketPosition));
            },
            cancelAggressionBid: function (id) {
                var marketPosition = {
                    instrumentId: id,
                    biddingType: BID
                };
                stompClient.send('/BBBroker/cancelAggression', {}, JSON.stringify(marketPosition));
            },
            cancelAggressionOffer: function (id) {
                var marketPosition = {
                    instrumentId: id,
                    biddingType: OFFER
                };
                stompClient.send('/BBBroker/cancelAggression', {}, JSON.stringify(marketPosition));
            },
            aggressBid: function (id) {
                var row = this.services.getDataView().getItemById(id);
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
                } else {
                    stompClient.send('/BBBroker/aggress', {}, JSON.stringify(marketAggressions));
                }
            },
            aggressOffer: function (id) {
                var row = this.services.getDataView().getItemById(id);
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
                }
            }
        };
    }]);











