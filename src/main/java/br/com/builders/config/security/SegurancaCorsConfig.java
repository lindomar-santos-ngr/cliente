package br.com.builders.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SegurancaCorsConfig extends WebSecurityConfigurerAdapter {

    private static final String[] URL_PUBLICAS = {
            "/api-docs",
            "/swagger-resources/configuration/ui",
            "/swagger-resources",
            "/swagger-resources/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/h2-console/**",
    };

    @Override
    public void configure(final WebSecurity web) {
        web.ignoring().antMatchers(URL_PUBLICAS);
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
               .authorizeRequests()
                .anyRequest().permitAll()
                .and().csrf().disable();
    }

    @Bean
    public GrantedAuthorityDefaults autorizacaoDefault() {
        return new GrantedAuthorityDefaults("");
    }

}
