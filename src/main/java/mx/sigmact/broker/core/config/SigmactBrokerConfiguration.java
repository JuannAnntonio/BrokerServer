package mx.sigmact.broker.core.config;

import java.text.SimpleDateFormat;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

import mx.sigmact.broker.core.helper.AggressionHelper;
import mx.sigmact.broker.core.lib.DaoHelper;
import mx.sigmact.broker.core.lib.DirtyPriceCalculator;
import mx.sigmact.broker.core.lib.FundingRateClient;
import mx.sigmact.broker.core.lib.UdiClient;
import mx.sigmact.broker.core.service.ValmerServiceReader;
import mx.sigmact.broker.dao.AggressionDao;
import mx.sigmact.broker.dao.BackOfficeDao;
import mx.sigmact.broker.dao.Coupon_bondeDao;
import mx.sigmact.broker.dao.Coupon_bondeFDao;
import mx.sigmact.broker.dao.FondeoDao;
import mx.sigmact.broker.dao.InstrumentDao;
import mx.sigmact.broker.dao.JdbcAggressionDao;
import mx.sigmact.broker.dao.JdbcBackOfficeDao;
import mx.sigmact.broker.dao.JdbcCoupon_bonde;
import mx.sigmact.broker.dao.JdbcCoupon_bondeF;
import mx.sigmact.broker.dao.JdbcFondeoDao;
import mx.sigmact.broker.dao.JdbcInstrumentDao;
import mx.sigmact.broker.dao.JdbcParameterDao;
import mx.sigmact.broker.dao.JdbcTemplateAdminDao;
import mx.sigmact.broker.dao.JdbcTraderDao;
import mx.sigmact.broker.dao.JdbcUdiDao;
import mx.sigmact.broker.dao.JdbcUtilDao;
import mx.sigmact.broker.dao.ParameterDao;
import mx.sigmact.broker.dao.StandingDAO;
import mx.sigmact.broker.dao.StandingDAOImpl;
import mx.sigmact.broker.dao.TraderDao;
import mx.sigmact.broker.dao.UdiDao;
import mx.sigmact.broker.dao.UtilDao;
import mx.sigmact.broker.services.ExternalServices;
import mx.sigmact.broker.services.timers.TimerServicesHelper;

/**
 * Created on 09/11/16.
 */
@Configuration
@PropertySource({"classpath:sigmact_broker.properties",
    "classpath:META-INF/queries/admin.query.properties",
    "classpath:META-INF/queries/aggression.query.properties",
    "classpath:META-INF/queries/back_office.query.properties",
    "classpath:META-INF/queries/back_office2.query.properties",
    "classpath:META-INF/queries/institution_admin.query.properties",
    "classpath:META-INF/queries/fondeo.query.properties",
    "classpath:META-INF/queries/coupon_bondes.query.properties",
    "classpath:META-INF/queries/coupon_bondesf.query.properties",
    "classpath:META-INF/queries/instrument.query.properties",
    "classpath:META-INF/queries/standing.query.properties",
    "classpath:META-INF/queries/parameter.query.properties",
    "classpath:META-INF/queries/trade.query.properties",
    "classpath:META-INF/queries/udi.query.properties"
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
    public StandingDAO standingDao(){
        return new StandingDAOImpl();
    }
    
    @Bean
    public TraderDao traderDao(){
        return new JdbcTraderDao();
    }

    @Bean
    public ParameterDao parameterDao(){
        return new JdbcParameterDao();
    }

    @Bean
    public FondeoDao fondeoDao(){
        return new JdbcFondeoDao();
    }

    @Bean
    public BackOfficeDao backOfficeDao(){
        return new JdbcBackOfficeDao();
    }

    @Bean
    public Coupon_bondeDao coupon_bondeDao(){
        return new JdbcCoupon_bonde();
    }

    /*Inicia Modificacion LF EYS */
    @Bean
    public Coupon_bondeFDao coupon_bondeFDao(){
        return new JdbcCoupon_bondeF();
    }
    /*Termina Modificacion LF EYS */

    @Bean
    public UtilDao utilDao(){
        return new JdbcUtilDao();
    }

    @Bean
    public InstrumentDao instrumentDao(){
        return new JdbcInstrumentDao();
    }

    @Bean
    public AggressionDao aggressionDao(){
        return new JdbcAggressionDao();
    }

    @Bean
    public UdiDao udiDao(){
        return new JdbcUdiDao();
    }
}
