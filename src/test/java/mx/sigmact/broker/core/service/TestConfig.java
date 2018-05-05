package mx.sigmact.broker.core.service;

import mx.sigmact.broker.core.lib.DaoHelper;
import mx.sigmact.broker.core.lib.DirtyPriceCalculator;
import mx.sigmact.broker.core.lib.FundingRateClient;
import mx.sigmact.broker.core.lib.UdiClient;
import mx.sigmact.broker.dao.JdbcAdminDao;
import mx.sigmact.broker.dao.JdbcTemplateAdminDao;
import mx.sigmact.broker.dao.JdbcTraderDao;
import mx.sigmact.broker.dao.TraderDao;
import mx.sigmact.broker.services.ExternalServices;
import mx.sigmact.broker.services.timers.TimerServicesHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.text.SimpleDateFormat;

/**
 * Created on 11/11/2016.
 */
@Configuration
@EnableJpaRepositories("mx.sigmact.broker.repositories")
@EnableTransactionManagement
@PropertySource({"classpath:sigmact_broker.properties",
        "classpath:META-INF/queries/admin.query.properties",
        "classpath:META-INF/queries/back_office.query.properties",
        "classpath:META-INF/queries/institution_admin.query.properties",
        "classpath:META-INF/queries/trade.query.properties"
})
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class TestConfig {

    @Value("${jndi.mysqlbase}")
    String jndiDatasource;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        dataSource.setUrl("jdbc:mysql://localhost:3306/SIGMACT_BROKER");
        dataSource.setUsername("root");
        dataSource.setPassword("y222kwwsd");
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setPackagesToScan("mx.sigmact.broker.model");
        emf.setDataSource(dataSource());
        emf.setJpaVendorAdapter(jpaVendorAdapter());
        return emf;
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter hjva = new HibernateJpaVendorAdapter();
        hjva.setDatabasePlatform("org.hibernate.dialect.MySQLDialect");
        hjva.setShowSql(false);
        hjva.setGenerateDdl(false);
        return hjva;
    }

    @Bean
    public PlatformTransactionManager transactionManager(){
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        //transactionManager.setDataSource(dataSource());
        return  transactionManager;
    }

    @Bean
    public JdbcAdminDao jdbcAdminDao(){
        JdbcAdminDao dao = new JdbcAdminDao();
        return dao;
    }



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
    public TraderDao traderDao(){
        return new JdbcTraderDao();
    }
}
