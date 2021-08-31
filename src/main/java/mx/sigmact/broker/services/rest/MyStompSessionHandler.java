package mx.sigmact.broker.services.rest;

import java.lang.reflect.Type;

import org.apache.log4j.Logger;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import mx.sigmact.broker.pojo.MarketMessage;

public class MyStompSessionHandler extends StompSessionHandlerAdapter {

	private Logger logger = Logger.getLogger(MyStompSessionHandler.class);

	@Override
	public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
		logger.info("[MyStompSessionHandler][afterConnected] New session established : " + session.getSessionId());

	}

	@Override
	public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload,
			Throwable exception) {
		logger.error("[MyStompSessionHandler][handleException] Got an exception: ", exception);
	}

	@Override
	public Type getPayloadType(StompHeaders headers) {

		logger.info("[MyStompSessionHandler][getPayloadType]");

		return MarketMessage.class;
	}

	@Override
	public void handleFrame(StompHeaders headers, Object payload) {

		logger.info("[MyStompSessionHandler][handleFrame]");

		// Message msg = (Message) payload;
		logger.info("[MyStompSessionHandler][handleFrame] Received : " + payload.toString());
	}

}
