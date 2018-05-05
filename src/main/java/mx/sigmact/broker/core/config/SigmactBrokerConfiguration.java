package mx.sigmact.broker.core.config;

import mx.sigmact.broker.core.helper.AggressionHelper;
import mx.sigmact.broker.core.lib.DaoHelper;
import mx.sigmact.broker.core.lib.DirtyPriceCalculator;
import mx.sigmact.broker.core.lib.FundingRateClient;
import mx.sigmact.broker.core.lib.UdiClient;
import mx.sigmact.broker.core.service.ValmerServiceReader;
import mx.sigmact.broker.dao.*;
import mx.sigmact.broker.services.ExternalServices;
import mx.sigmact.broker.services.timers.TimerServicesHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

import java.text.SimpleDateFormat;

/**
 * Created on 09/11/16.
 */
@Configuration
@PropertySource({"classpath:sigmact_broker.properties",
        "classpath:META-INF/queries/admin.query.properties",
        "classpath:META-INF/queries/back_office.query.properties",
        "classpath:META-INF/queries/institution_admin.query.properties",
        "classpath:META-INF/queries/trade.query.properties"
        })
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class SigmactBrokerConfiguration {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfig() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public Md5PasswordEncoder encoder(){
        return new Md5PasswordEncoder();
    }


    @Bean
    public DirtyPriceCalculator dpc(){
        return new DirtyPriceCalculator();
    }

    @Bean
    public JdbcTemplateAdminDao jdbcTemplateAdminDao(){
        JdbcTemplateAdminDao jdbcTemplateAdminDao = new JdbcTemplateAdminDao();
        return jdbcTemplateAdminDao;
    }

    @Bean
    public UdiClient udiClient(){
        return new UdiClient();
    }

    @Bean
    public FundingRateClient fondingRateClient(){
        return new FundingRateClient();
    }

    @Bean
    public ValmerServiceReader vsr(){
        return new ValmerServiceReader();
    }

    @Bean
    public SimpleDateFormat formatTimeStamp(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf;
    }

    @Bean
    public SimpleDateFormat formatDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf;
    }

    @Bean
    public DaoHelper daoHelper(){
        DaoHelper daoHelper = new DaoHelper();
        return daoHelper;
    }

    @Bean
    public DirtyPriceCalculator dpCalc(){
        return new DirtyPriceCalculator();
    }

    @Bean
    public ExternalServices externalServices(){
        return new ExternalServices();
    }

    @Bean
    public TimerServicesHelper timerServicesHelper(){
        return new TimerServicesHelper();
    }

    @Bean
    public AggressionHelper aggressionHelper(){
        return  new AggressionHelper();
    }

    @Bean
    public TraderDao traderDao(){
        return new JdbcTraderDao();
    }

    @Bean
    BackOfficeDao backOfficeDao(){
        return new JdbcBackOfficeDao();
    }
}
