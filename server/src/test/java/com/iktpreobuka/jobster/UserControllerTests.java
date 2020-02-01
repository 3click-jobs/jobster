package com.iktpreobuka.jobster;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
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
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.util.JacksonJsonParser;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
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
@SpringBootTest 
@WebAppConfiguration 

public class UserControllerTests {
 
	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(), 
			MediaType.APPLICATION_JSON.getSubtype(), 
			Charset.forName("utf8"));
	
	private static MockMvc mockMvc;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;
    
	@Autowired 
	private WebApplicationContext webApplicationContext;
	
	private static CityEntity city;

	private static CityEntity cityWhitoutRegion;
	
	private static CountryEntity country;
	
	private static CountryRegionEntity region;
	
	private static UserEntity user;

	private static CountryRegionEntity regionNoName;

	private static UserAccountEntity userAccount;
	
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
			regionNoName = countryRegionRepository.save(new CountryRegionEntity(country, null));	
			countryRegions.add(regionNoName);
			cityWhitoutRegion = cityRepository.save(new CityEntity(regionNoName, "World city without", 33.3, 34.5)); 
			cities.add(cityWhitoutRegion);
			city = cityRepository.save(new CityEntity(region, "World city", 33.3, 34.5));
			cities.add(city);
			UserControllerTests.users.add(userRepository.save(new UserEntity(city, "0642345678", "Jobster@mail.com", "About Jobster")));
			UserControllerTests.users.add(userRepository.save(new UserEntity(city, "0643456789", "Jobsters@mail.com", "About Jobsters")));
			UserControllerTests.users.add(userRepository.save(new UserEntity(city, "0644567890", "Jobstery@mail.com", "About Jobstery")));
			user = new UserEntity(city, "0644567899", "Jobstery1@mail.com", "About Jobstery");
			user.setStatusInactive();
			UserControllerTests.users.add(userRepository.save(user));
			user = new UserEntity(city, "0644567898", "Jobstery2@mail.com", "About Jobstery");
			user.setStatusArchived();
			UserControllerTests.users.add(userRepository.save(user));
			UserControllerTests.userAccounts.add(userAccountRepository.save(new UserAccountEntity(UserControllerTests.users.get(0), EUserRole.ROLE_USER, "Test1234", "{bcrypt}$2a$10$FZjQbu7AqcSp0ns.GAxkbu0eKVUtNFTZNdVWwPOtBATLF0Bs9wtW2", UserControllerTests.users.get(0).getId())));
			UserControllerTests.userAccounts.add(userAccountRepository.save(new UserAccountEntity(UserControllerTests.users.get(1), EUserRole.ROLE_ADMIN, "Test1235", "{bcrypt}$2a$10$FZjQbu7AqcSp0ns.GAxkbu0eKVUtNFTZNdVWwPOtBATLF0Bs9wtW2", UserControllerTests.users.get(0).getId())));
			UserControllerTests.userAccounts.add(userAccountRepository.save(new UserAccountEntity(UserControllerTests.users.get(2), EUserRole.ROLE_USER, "Test1236", "{bcrypt}$2a$10$FZjQbu7AqcSp0ns.GAxkbu0eKVUtNFTZNdVWwPOtBATLF0Bs9wtW2", UserControllerTests.users.get(0).getId())));
			userAccount = new UserAccountEntity(UserControllerTests.users.get(3), EUserRole.ROLE_USER, "Test1237", "{bcrypt}$2a$10$FZjQbu7AqcSp0ns.GAxkbu0eKVUtNFTZNdVWwPOtBATLF0Bs9wtW2", UserControllerTests.users.get(0).getId());
			userAccount.setStatusInactive();
			UserControllerTests.userAccounts.add(userAccountRepository.save(userAccount));
			userAccount = new UserAccountEntity(UserControllerTests.users.get(4), EUserRole.ROLE_USER, "Test1238", "{bcrypt}$2a$10$FZjQbu7AqcSp0ns.GAxkbu0eKVUtNFTZNdVWwPOtBATLF0Bs9wtW2", UserControllerTests.users.get(0).getId());
			userAccount.setStatusArchived();
			UserControllerTests.userAccounts.add(userAccountRepository.save(userAccount));
			dbInit = true;
			
			//GET TOKEN
			MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		    params.add("grant_type", "password");
		    params.add("client_id", "my-trusted-client");
		    params.add("username", "Test1235");
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
			for (UserAccountEntity acc : UserControllerTests.userAccounts) {
				userAccountRepository.delete(acc);
			}
			UserControllerTests.userAccounts.clear();
			for (UserEntity usr : UserControllerTests.users) {
				userRepository.delete(usr);
			}
			UserControllerTests.users.clear();
			for (CityEntity cty : UserControllerTests.cities) {
				cityRepository.delete(cty); 
			}
			UserControllerTests.cities.clear();
			for (CountryRegionEntity creg : UserControllerTests.countryRegions) {
				countryRegionRepository.delete(creg);	
			}
			UserControllerTests.countryRegions.clear();
			for (CountryEntity cntry : UserControllerTests.countries) {
				countryRepository.delete(cntry);
			}
			UserControllerTests.countries.clear();	
			dbInit = false;
			token = null;
			logger.info("DBtearDown ok");
		}
	}

	@Test
	public void userServiceNotFound() throws Exception { 
		mockMvc.perform(get("/jobster/users/users/readallusers/")
			//.andDo(MockMvcResultHandlers.print())
			.header("Authorization", "Bearer " + token))
			.andExpect(status().isNotFound());
	}

	@Test
	public void userServiceFound() throws Exception { 
		mockMvc.perform(get("/jobster/users/users/")
			.header("Authorization", "Bearer " + token))
			.andExpect(status().isOk()); 
	}

	@Test
	public void readAllUsers() throws Exception { 
		mockMvc.perform(get("/jobster/users/users/")
			.header("Authorization", "Bearer " + token)) 
			.andExpect(status().isOk()) 
			.andExpect(content().contentType(contentType));
		}

	@Test
	public void readAllUsersNotFound() throws Exception { 	
		for (UserAccountEntity acc : UserControllerTests.userAccounts) {
			userAccountRepository.delete(acc);
		}
		UserControllerTests.userAccounts.clear();
		for (UserEntity cmpny : UserControllerTests.users) {
			userRepository.delete(cmpny);
		}
		UserControllerTests.users.clear();
		mockMvc.perform(get("/jobster/users/users/")
			.header("Authorization", "Bearer " + token))
			.andExpect(status().isNotFound()); 
	}

}
