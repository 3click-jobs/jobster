package com.iktpreobuka.jobster.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

@Configuration
@EnableResourceServer
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

private static final String RESOURCE_ID = "jobster";
	
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		resources.resourceId(RESOURCE_ID).stateless(false);
	}
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.
		anonymous().disable()
		.requestMatchers().antMatchers("/jobster/**")  //PROBA 2 ZA SVE PUTANJE
		.and().authorizeRequests()
		.antMatchers("/jobster/**").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')") //PROBA 2 ZA SVE PUTANJE
		.and().exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
	}
}	