package mx.sigmact.broker.services.rest;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.RestTemplateXhrTransport;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

public class StompClient {

//	private static String URL_ws = "ws://localhost/sigmact_broker/market";
	private static String URL_http = "http://localhost/sigmact_broker/market";
//	private static String URL_http = "http://bbfixed-beta.com/sigmact_broker/market";
//	private static String URL_http = "https://bbfixed.net/sigmact_broker/market";

	private static final Logger log = LoggerFactory.getLogger(StompClient.class);

	public StompSession connect() {

		try {

			List<Transport> transports = new ArrayList<Transport>(2);
			transports.add(new WebSocketTransport(new StandardWebSocketClient()));
			transports.add(new RestTemplateXhrTransport());
			SockJsClient sockJsClient = new SockJsClient(transports);
			
			WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
			stompClient.setMessageConverter(new MappingJackson2MessageConverter());
			
			WebSocketHttpHeaders headers_socket = new WebSocketHttpHeaders();
			
			StompHeaders stompHeader = new StompHeaders();
			//stompHeader.setId("5");
			//stompHeader.set("user-name", "Manlio");
			//stompHeader.setLogin("bbfixed_t01");
			//stompHeader.setPasscode("Passedg9");
			//stompHeader.set("username", "Manlio");
			//stompHeader.setSession("Manlio");
			
			
			StompSessionHandler sessionHandler = new MyStompSessionHandler();
			//sessionHandler.handleFrame(stompHeader, null);

			return stompClient.connect(URL_http, headers_socket, stompHeader, sessionHandler).get(1,
					TimeUnit.SECONDS);

		} catch (Exception e) {

			log.info("[RESTAdmin][doGetSocket] ERROR: " + e.getMessage());

		} 
		
		return null;
	}

}
