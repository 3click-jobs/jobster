package com.iktpreobuka.jobster;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.common.util.JacksonJsonParser;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import com.iktpreobuka.jobster.entities.CityEntity;
import com.iktpreobuka.jobster.entities.CountryEntity;
import com.iktpreobuka.jobster.entities.CountryRegionEntity;
import com.iktpreobuka.jobster.entities.UserAccountEntity;
import com.iktpreobuka.jobster.entities.UserEntity;
import com.iktpreobuka.jobster.enumerations.EUserRole;
import com.iktpreobuka.jobster.repositories.CityRepository;
import com.iktpreobuka.jobster.repositories.CountryRegionRepository;
import com.iktpreobuka.jobster.repositories.CountryRepository;
import com.iktpreobuka.jobster.repositories.UserAccountRepository;
import com.iktpreobuka.jobster.repositories.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
//@WebAppConfiguration 
public class HttpRequestTest {

	@LocalServerPort 
	private int port;
	
	@Autowired 
	private TestRestTemplate restTemplate;
	
	private static MockMvc mockMvc;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;
    
	@Autowired 
	private WebApplicationContext webApplicationContext;

	private static CityEntity city;

	private static CountryEntity country;
	
	private static CountryRegionEntity region;
	
	private static List<UserEntity> users = new ArrayList<>();
	
	private static List<UserAccountEntity> userAccounts = new ArrayList<>();
	
	private static List<CountryEntity> countries = new ArrayList<>();

	private static List<CountryRegionEntity> countryRegions = new ArrayList<>();

	private static List<CityEntity> cities = new ArrayList<>();
	
	private String token;

	@Autowired 
	private UserRepository userRepository;
	
	@Autowired 
	private CityRepository cityRepository;
	
	@Autowired
	private CountryRegionRepository countryRegionRepository;

	@Autowired
	private CountryRepository countryRepository;
	
	@Autowired
	private UserAccountRepository userAccountRepository;
		
	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	private boolean dbInit = false;

	@Before
	public void setUp() throws Exception { 
		logger.info("DBsetUp");
		if(!dbInit) { mockMvc = MockMvcBuilders
				.webAppContextSetup(webApplicationContext)
				.addFilter(springSecurityFilterChain)
				.build(); 
			country = countryRepository.save(new CountryEntity("World Union", "XX"));
			countries.add(country);
			region = countryRegionRepository.save(new CountryRegionEntity(country, "World region"));	
			countryRegions.add(region);
			city = cityRepository.save(new CityEntity(region, "World city", 33.3, 34.5));
			cities.add(city);
			users.add(userRepository.save(new UserEntity(city, "0642345678", "Jobster@mail.com", "About Jobster")));
			userAccounts.add(userAccountRepository.save(new UserAccountEntity(users.get(0), EUserRole.ROLE_ADMIN, "Test1234", "{bcrypt}$2a$10$FZjQbu7AqcSp0ns.GAxkbu0eKVUtNFTZNdVWwPOtBATLF0Bs9wtW2", users.get(0).getId())));
			dbInit = true;
			
			//GET TOKEN
			MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		    params.add("grant_type", "password");
		    params.add("client_id", "my-trusted-client");
		    params.add("username", "Test1234");
		    params.add("password", "admin");
		    ResultActions result 
		      = mockMvc.perform(post("/oauth/token")
		        .params(params)
		        .with(httpBasic("my-trusted-client","secret"))
		        .accept("application/json;charset=UTF-8"))
		        .andExpect(status().isOk())
		        .andExpect(content().contentType("application/json;charset=UTF-8"));
		    String resultString = result.andReturn().getResponse().getContentAsString();
		    JacksonJsonParser jsonParser = new JacksonJsonParser();
		    token = jsonParser.parseMap(resultString).get("access_token").toString();

			logger.info("DBsetUp ok");
		} 
	}
	
	@After
	public void tearDown() throws Exception {
		logger.info("DBtearDown");
		if(dbInit) {
			for (UserAccountEntity acc : userAccounts) {
				userAccountRepository.delete(acc);
			}
			userAccounts.clear();
			for (UserEntity usr : users) {
				userRepository.delete(usr);
			}
			users.clear();
			for (CityEntity cty : cities) {
				cityRepository.delete(cty); 
			}
			cities.clear();
			for (CountryRegionEntity creg : countryRegions) {
				countryRegionRepository.delete(creg);	
			}
			countryRegions.clear();
			for (CountryEntity cntry : countries) {
				countryRepository.delete(cntry);
			}
			countries.clear();	
			dbInit = false;
			token = null;
			logger.info("DBtearDown ok");
		}
	}
	
	@Test 
	public void greetingShouldReturnDefaultMessage() throws Exception {
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + token);
		HttpEntity<String> entity = new HttpEntity<>("paramters",headers);
		
		assertThat(this.restTemplate.exchange("http://localhost:" + port + "/jobster", HttpMethod.GET, entity, String.class).getBody()).contains("Jobster");
//		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/jobster", String.class)).contains("Jobster");
	}

}