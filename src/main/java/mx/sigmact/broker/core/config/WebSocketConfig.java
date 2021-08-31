package mx.sigmact.broker.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * Created on 08/12/16.
 */

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {


	private static final Logger log = LoggerFactory.getLogger(WebSocketConfig.class);
	
    /**
     * Configure the endpoint
     * @param registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

		log.info("[WebSocketConfig][registerStompEndpoints]");
    	
        registry.addEndpoint("/market")
        //.setAllowedOrigins("*")		
        .withSockJS();
        

    }

    /**
     * Configure the message for the server and for the clients
     * @param config
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {

		log.info("[WebSocketConfig][configureMessageBroker]");
    	
        config.setApplicationDestinationPrefixes("/BBBroker");
        config.enableSimpleBroker("/market");
        config.setUserDestinationPrefix("/user");
    }
}
