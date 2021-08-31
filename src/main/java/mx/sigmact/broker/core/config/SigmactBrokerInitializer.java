package mx.sigmact.broker.core.config;

import org.apache.log4j.Logger;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import mx.sigmact.broker.services.rest.RESTTrader;

import javax.servlet.Filter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * CIntantiates the web application
 */

@Order(1)
public class SigmactBrokerInitializer extends AbstractAnnotationConfigDispatcherServletInitializer{

	Logger log = Logger.getLogger(SigmactBrokerInitializer.class);
	
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
        servletContext.addListener(new SessionListener());
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {

    	log.info("[SigmactBrokerInitializer][getRootConfigClasses]");
    	
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

    @Override
    protected Filter[] getServletFilters() {
        return new Filter[] { new HiddenHttpMethodFilter() };
    }

}
