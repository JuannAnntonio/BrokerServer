package mx.sigmact.broker.core.config;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;

import mx.sigmact.broker.pojo.RoleType;

/**
 * Created on 12/11/2016.
 */

@Configuration
@EnableWebSecurity
public class SigmactBrokerWebSecurity extends WebSecurityConfigurerAdapter{

	private static final Logger log = LoggerFactory.getLogger(SigmactBrokerWebSecurity.class);
	
    @Resource
    DataSource dataSource;

    @Resource
    Md5PasswordEncoder encoder;


    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {

		log.info("[SigmactBrokerWebSecurity][configure] auth");
    	
        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery(
                        "SELECT username, password, enabled from SIGMACT_BROKER.USER WHERE username = ?")
                .authoritiesByUsernameQuery(
                        "SELECT USER.username, ROLES.role as authorities FROM SIGMACT_BROKER.ROLES, SIGMACT_BROKER.USER WHERE USER.username = ? AND USER.id_user = ROLES.fk_id_user");

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{

		log.info("[SigmactBrokerWebSecurity][configure] http");
    	
        http.authorizeRequests()
                .expressionHandler(new DefaultWebSecurityExpressionHandler()) //TODO Posiblemente innecesario,checar
                .antMatchers("/").permitAll()
                .antMatchers("/resources/**").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/test/**").permitAll()
                .antMatchers("/services/getbanxicoudi").permitAll()//TODO restrict for admin only
                .antMatchers("/logout").hasAnyAuthority(RoleType.SYSTEMADMIN, RoleType.BACKOFFICE, RoleType.TRADE,RoleType.INSTITUTIONADMIN)
                .antMatchers("/mainMenu").hasAnyAuthority(RoleType.SYSTEMADMIN, RoleType.BACKOFFICE, RoleType.TRADE,RoleType.INSTITUTIONADMIN)
                .antMatchers("/admin/**").hasAuthority(RoleType.SYSTEMADMIN)
                .antMatchers("/backoffice/**").hasAuthority(RoleType.BACKOFFICE)
                .antMatchers("/trader/**").hasAuthority(RoleType.TRADE)
                .antMatchers("/instAdmin/**").hasAuthority(RoleType.INSTITUTIONADMIN)
                .and()
                .formLogin()
                    .loginPage("/login").defaultSuccessUrl("/mainMenu").loginProcessingUrl("/login_check").failureUrl("/login?error=1")
                .and()
                .logout()
                    .logoutUrl("/logout").permitAll()
                .and()
                    .logout().logoutSuccessUrl("/")
                .and()
                .rememberMe().key("myAppKey")

        ;
    }

    


}
