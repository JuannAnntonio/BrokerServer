package mx.sigmact.broker.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * Created on 11/11/2016.
 */
@Configuration
@EnableJpaRepositories("mx.sigmact.broker.repositories")
@EnableTransactionManagement
public class DataSourceConfiguration {

    @Value("${jndi.mysqlbase}")
    String jndiDatasource;

    @Bean
    public DataSource dataSource() {
        return (new JndiDataSourceLookup()).getDataSource(jndiDatasource);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource());
        emf.setPackagesToScan("mx.sigmact.broker.model");
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
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf){
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        //transactionManager.setDataSource(dataSource());
        return  transactionManager;
    }

}
