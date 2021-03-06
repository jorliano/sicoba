package br.com.clairtonluz.sicoba.config.security;

import br.com.clairtonluz.sicoba.filter.CsrfHeaderFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

import javax.sql.DataSource;

/**
 * Created by clairtonluz on 31/01/16.
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String API_GERENCIANET_NOTIFICATION = "/api/gerencianet/**/notification";
    private static final String API_UPDATE_MK_HOST = "/api/mikrotiks/**/host";
    private static final String API_TESTES = "/api/testes/**";

    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers("/", "/index.html", "/app/**", "/dist/**", "/bower_components/**",
                        API_GERENCIANET_NOTIFICATION, API_TESTES).permitAll()
                .antMatchers(HttpMethod.POST, API_UPDATE_MK_HOST).permitAll()
                .anyRequest().authenticated()
                .and().logout()
                .and()
                .addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class)
                .csrf().ignoringAntMatchers(API_GERENCIANET_NOTIFICATION, API_UPDATE_MK_HOST, API_TESTES)
                .csrfTokenRepository(csrfTokenRepository())
        ;

    }

    private CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-XSRF-TOKEN");
        return repository;
    }

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
                .passwordEncoder(passwordEncoder())
                .usersByUsernameQuery(
                        "select username,password, enabled from users where username=? and enabled = true")
                .authoritiesByUsernameQuery(
                        "select u.username, ur.role from user_roles ur INNER JOIN users u on u.id = ur.user_id where u.username=?");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder;
    }
}
