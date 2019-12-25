package com.iktpreobuka.jobster.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenStoreUserApprovalHandler;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
@EnableWebSecurity
public class OAuth2SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private DataSource dataSource;
	
	@Value("${spring.queries.users-query}")
	private String usersQuery;
	
	@Value("${spring.queries.roles-query}")
	private String rolesQuery;
	
	@Autowired
	private ClientDetailsService clientDetailsService;
	
	
	
	@Autowired
	public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
	auth
	.jdbcAuthentication()
	.usersByUsernameQuery(usersQuery) 
	.authoritiesByUsernameQuery(rolesQuery)
	.passwordEncoder(passwordEncoder())
	.dataSource(dataSource);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	http
	.csrf().disable()
	.anonymous().disable()
	.authorizeRequests()
	.antMatchers("/oauth/token").permitAll();
	} 
	
	@Bean
	@Autowired
	public TokenStoreUserApprovalHandler userApprovalHandler(TokenStore tokenStore){
	TokenStoreUserApprovalHandler handler = new TokenStoreUserApprovalHandler();
	handler.setTokenStore(tokenStore);
	handler.setRequestFactory(new DefaultOAuth2RequestFactory(clientDetailsService));
	handler.setClientDetailsService(clientDetailsService);
	return handler;
	}
	
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
	return super.authenticationManagerBean();
	}
	
	@Bean
	public TokenStore tokenStore() {
	return new InMemoryTokenStore();
	}
	
	@Bean
	@Autowired
	public ApprovalStore approvalStore(TokenStore tokenStore) throws Exception {
	TokenApprovalStore store = new TokenApprovalStore();
	store.setTokenStore(tokenStore);
	return store;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	

}