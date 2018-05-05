package mx.sigmact.broker.core.config;

import org.springframework.core.annotation.Order;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * Created on 11/11/2016.
 */
@Order(2)
public class SigmactBrokerSecurityInitializer extends AbstractSecurityWebApplicationInitializer {

}
