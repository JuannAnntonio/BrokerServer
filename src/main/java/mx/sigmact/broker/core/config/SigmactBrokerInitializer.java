package mx.sigmact.broker.core.config;

import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
/**
 * CIntantiates the web application
 */

@Order(1)
public class SigmactBrokerInitializer extends AbstractAnnotationConfigDispatcherServletInitializer{



    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{DataSourceConfiguration.class, WebSocketConfig.class ,SigmactBrokerConfiguration.class,SigmactBrokerWebSecurity.class, SchedulingConfiguration.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{SigmactBrokerWebConfiguration.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

}
