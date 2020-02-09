package com.iktpreobuka.jobster;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

import com.google.gson.Gson;
import com.iktpreobuka.jobster.entities.CityEntity;
import com.iktpreobuka.jobster.entities.CountryEntity;
import com.iktpreobuka.jobster.entities.CountryRegionEntity;
import com.iktpreobuka.jobster.entities.UserAccountEntity;
import com.iktpreobuka.jobster.entities.UserEntity;
import com.iktpreobuka.jobster.entities.dto.UserAccountDTO;
import com.iktpreobuka.jobster.enumerations.EUserRole;
import com.iktpreobuka.jobster.repositories.CityRepository;
import com.iktpreobuka.jobster.repositories.CountryRegionRepository;
import com.iktpreobuka.jobster.repositories.CountryRepository;
import com.iktpreobuka.jobster.repositories.UserAccountRepository;
import com.iktpreobuka.jobster.repositories.UserRepository;

//@Ignore
@RunWith(SpringRunner.class) 
@SpringBootTest 
@WebAppConfiguration 
public class UserAccountControllerTests {
 
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

	private String token;


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
			UserAccountControllerTests.users.add(userRepository.save(new UserEntity(city, "0642345678", "Jobster@mail.com", "About Jobster")));
			UserAccountControllerTests.users.add(userRepository.save(new UserEntity(city, "0643456789", "Jobsters@mail.com", "About Jobsters")));
			UserAccountControllerTests.users.add(userRepository.save(new UserEntity(city, "0644567890", "Jobstery@mail.com", "About Jobstery")));
			user = new UserEntity(city, "0644567899", "Jobstery1@mail.com", "About Jobstery");
			user.setStatusInactive();
			UserAccountControllerTests.users.add(userRepository.save(user));
			user = new UserEntity(city, "0644567898", "Jobstery2@mail.com", "About Jobstery");
			user.setStatusArchived();
			UserAccountControllerTests.users.add(userRepository.save(user));
			UserAccountControllerTests.userAccounts.add(userAccountRepository.save(new UserAccountEntity(UserAccountControllerTests.users.get(0), EUserRole.ROLE_ADMIN, "Test1234", "{bcrypt}$2a$10$FZjQbu7AqcSp0ns.GAxkbu0eKVUtNFTZNdVWwPOtBATLF0Bs9wtW2", UserAccountControllerTests.users.get(0).getId())));
			UserAccountControllerTests.userAccounts.add(userAccountRepository.save(new UserAccountEntity(UserAccountControllerTests.users.get(1), EUserRole.ROLE_USER, "Test1235", "{bcrypt}$2a$10$FZjQbu7AqcSp0ns.GAxkbu0eKVUtNFTZNdVWwPOtBATLF0Bs9wtW2", UserAccountControllerTests.users.get(0).getId())));
			UserAccountControllerTests.userAccounts.add(userAccountRepository.save(new UserAccountEntity(UserAccountControllerTests.users.get(2), EUserRole.ROLE_USER, "Test1236", "{bcrypt}$2a$10$FZjQbu7AqcSp0ns.GAxkbu0eKVUtNFTZNdVWwPOtBATLF0Bs9wtW2", UserAccountControllerTests.users.get(0).getId())));
			userAccount = new UserAccountEntity(UserAccountControllerTests.users.get(3), EUserRole.ROLE_USER, "Test1237", "{bcrypt}$2a$10$FZjQbu7AqcSp0ns.GAxkbu0eKVUtNFTZNdVWwPOtBATLF0Bs9wtW2", UserAccountControllerTests.users.get(0).getId());
			userAccount.setStatusInactive();
			UserAccountControllerTests.userAccounts.add(userAccountRepository.save(userAccount));
			userAccount = new UserAccountEntity(UserAccountControllerTests.users.get(4), EUserRole.ROLE_USER, "Test1238", "{bcrypt}$2a$10$FZjQbu7AqcSp0ns.GAxkbu0eKVUtNFTZNdVWwPOtBATLF0Bs9wtW2", UserAccountControllerTests.users.get(0).getId());
			userAccount.setStatusArchived();
			UserAccountControllerTests.userAccounts.add(userAccountRepository.save(userAccount));
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
			for (UserAccountEntity acc : UserAccountControllerTests.userAccounts) {
				userAccountRepository.delete(acc);
			}
			UserAccountControllerTests.userAccounts.clear();
			for (UserEntity usr : UserAccountControllerTests.users) {
				userRepository.delete(usr);
			}
			UserAccountControllerTests.users.clear();
			for (CityEntity cty : UserAccountControllerTests.cities) {
				cityRepository.delete(cty); 
			}
			UserAccountControllerTests.cities.clear();
			for (CountryRegionEntity creg : UserAccountControllerTests.countryRegions) {
				countryRegionRepository.delete(creg);	
			}
			UserAccountControllerTests.countryRegions.clear();
			for (CountryEntity cntry : UserAccountControllerTests.countries) {
				countryRepository.delete(cntry);
			}
			UserAccountControllerTests.countries.clear();	
			dbInit = false;
			token = null;
			logger.info("DBtearDown ok");
		}
	}

	@Test
	public void userAccountServiceNotFound() throws Exception { 
        logger.info("userAccountServiceNotFound");
		mockMvc.perform(get("/jobster/accounts/readallaccounts/")
			.header("Authorization", "Bearer " + token))
			//.andDo(MockMvcResultHandlers.print())
			.andExpect(status().isBadRequest()); 
	}

	@Test
	public void userAccountServiceFound() throws Exception { 
        logger.info("userAccountServiceFound");
		mockMvc.perform(get("/jobster/accounts/")
			.header("Authorization", "Bearer " + token))
			.andExpect(status().isOk()); 
	}

	@Test
	public void readAllUserAccounts() throws Exception { 
        logger.info("readAllUserAccounts");
		mockMvc.perform(get("/jobster/accounts/")
			.header("Authorization", "Bearer " + token)) 
			.andExpect(status().isOk()) 
			.andExpect(content().contentType(contentType));
	}

	@Test
	public void readAllUserAccountsNotFound() throws Exception { 
        logger.info("readAllUserAccountsNotFound");
		for (UserAccountEntity acc : UserAccountControllerTests.userAccounts) {
			userAccountRepository.delete(acc);
		}
		UserAccountControllerTests.userAccounts.clear();
		mockMvc.perform(get("/jobster/accounts/")
			.header("Authorization", "Bearer " + token))
			.andExpect(status().isNotFound()); 
	}


	@Test
	public void readSingleUserAccount() throws Exception { 
        logger.info("readSingleUserAccount");
		mockMvc.perform(get("/jobster/accounts/" + UserAccountControllerTests.userAccounts.get(0).getId())
			.header("Authorization", "Bearer " + token))
			//.andDo(MockMvcResultHandlers.print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(UserAccountControllerTests.userAccounts.get(0).getId().intValue()))); 
	}

	@Test
	public void readSingleUserAccountWhichNotExists() throws Exception { 
        logger.info("readSingleUserAccountWhichNotExists");
		mockMvc.perform(get("/jobster/accounts/" + (UserAccountControllerTests.userAccounts.get(4).getId()+1))
			.header("Authorization", "Bearer " + token))
			.andExpect(status().isNotFound()); 
	}

	@Test
	public void readAllDeletedUserAccounts() throws Exception { 
        logger.info("readAllDeletedUserAccounts");
		mockMvc.perform(get("/jobster/accounts/deleted/")
			.header("Authorization", "Bearer " + token)) 
			.andExpect(status().isOk()) 
			.andExpect(content().contentType(contentType));
	}

	@Test
	public void readAllDeletedUserAccountsNotFound() throws Exception { 
        logger.info("readAllDeletedUserAccountsNotFound");
		for (UserAccountEntity acc : UserAccountControllerTests.userAccounts) {
			userAccountRepository.delete(acc);
		}
		UserAccountControllerTests.userAccounts.clear();
		mockMvc.perform(get("/jobster/accounts/deleted/")
			.header("Authorization", "Bearer " + token))
			.andExpect(status().isNotFound()); 
	}

	@Test
	public void readSingleDeletedUserAccount() throws Exception { 
        logger.info("readSingleDeletedUserAccount");
		Integer id = UserAccountControllerTests.userAccounts.get(3).getId();
		mockMvc.perform(get("/jobster/accounts/deleted/" + id)
			.header("Authorization", "Bearer " + token))
			//.andDo(MockMvcResultHandlers.print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(id))); 
	}

	@Test
	public void readSingleDeletedUserAccountWhichNotExists() throws Exception { 
        logger.info("readSingleDeletedUserAccountWhichNotExists");
		mockMvc.perform(get("/jobster/accounts/deleted/" + UserAccountControllerTests.userAccounts.get(0).getId())
			.header("Authorization", "Bearer " + token))
			.andExpect(status().isNotFound()); 
	}

	@Test
	public void readAllArchivedUserAccounts() throws Exception { 
        logger.info("readAllArchivedUserAccounts");
		mockMvc.perform(get("/jobster/accounts/archived/")
			.header("Authorization", "Bearer " + token)) 
			.andExpect(status().isOk()) 
			.andExpect(content().contentType(contentType));
	}

	@Test
	public void readAllArchivedUserAccountsNotFound() throws Exception { 
        logger.info("readAllArchivedUserAccountsNotFound");
		for (UserAccountEntity acc : UserAccountControllerTests.userAccounts) {
			userAccountRepository.delete(acc);
		}
		UserAccountControllerTests.userAccounts.clear();
		mockMvc.perform(get("/jobster/accounts/archived/")
			.header("Authorization", "Bearer " + token))
			.andExpect(status().isNotFound()); 
	}

	@Test
	public void readSingleArchivedUserAccount() throws Exception { 
        logger.info("readSingleArchivedUserAccount");
		Integer id = UserAccountControllerTests.userAccounts.get(4).getId();
		mockMvc.perform(get("/jobster/accounts/archived/" + id)
			.header("Authorization", "Bearer " + token))
			//.andDo(MockMvcResultHandlers.print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(id))); 
	}

	@Test
	public void readSingleArchivedUserAccountWhichNotExists() throws Exception { 
        logger.info("readSingleArchivedUserAccountWhichNotExists");
		mockMvc.perform(get("/jobster/accounts/archived/" + (UserAccountControllerTests.userAccounts.get(4).getId()+1))
			.header("Authorization", "Bearer " + token))
			.andExpect(status().isNotFound()); 
	}

	@Test
	public void createUserAccount() throws Exception {
        logger.info("createUserAccount");
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole("ROLE_ADMIN");
        userAccountDTO.setUsername("Test123");
        userAccountDTO.setPassword("Test123");
        userAccountDTO.setConfirmedPassword("Test123");
        userAccountDTO.setUserId(UserAccountControllerTests.users.get(1).getId().toString());
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
        	//.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
        	.andExpect(content().contentType(contentType)) 
        	.andExpect(jsonPath("$.username", is("Test123")));
        userAccountRepository.delete(userAccountRepository.getByUsername("Test123"));
	}

	@Test
	public void createUserMarginalValueUsernameMin() throws Exception {
        logger.info("createUserMarginalValueUsernameMin");
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole("ROLE_ADMIN");
        userAccountDTO.setUsername("Test5");
        userAccountDTO.setPassword("Test123");
        userAccountDTO.setConfirmedPassword("Test123");
        userAccountDTO.setUserId(UserAccountControllerTests.users.get(1).getId().toString());
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
        	//.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
        	.andExpect(content().contentType(contentType)) 
        	.andExpect(jsonPath("$.username", is("Test5")));
        userAccountRepository.delete(userAccountRepository.getByUsername("Test5"));
	}

	@Test
	public void createUserMarginalValueUsernameMax() throws Exception {
        logger.info("createUserMarginalValueUsernameMax");
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole("ROLE_ADMIN");
        userAccountDTO.setUsername("Test1234567891234567");
        userAccountDTO.setPassword("Test123");
        userAccountDTO.setConfirmedPassword("Test123");
        userAccountDTO.setUserId(UserAccountControllerTests.users.get(1).getId().toString());
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
        	//.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
        	.andExpect(content().contentType(contentType)) 
        	.andExpect(jsonPath("$.username", is("Test1234567891234567")));
        userAccountRepository.delete(userAccountRepository.getByUsername("Test1234567891234567"));
	}

	@Test
	public void createUserMarginalValuePasswordAndConfirmedPasswordMin() throws Exception {
        logger.info("createUserMarginalValuePasswordAndConfirmedPasswordMin");
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole("ROLE_ADMIN");
        userAccountDTO.setUsername("Test123");
        userAccountDTO.setPassword("Test5");
        userAccountDTO.setConfirmedPassword("Test5");
        userAccountDTO.setUserId(UserAccountControllerTests.users.get(1).getId().toString());
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
        	//.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
        	.andExpect(content().contentType(contentType)) 
        	.andExpect(jsonPath("$.username", is("Test123")));
        userAccountRepository.delete(userAccountRepository.getByUsername("Test123"));
	}

	@Test
	public void createUserValidationErrorUsernameToSmall() throws Exception {
        logger.info("createUserValidationErrorUsernameToSmall");
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole("ROLE_USER");
        userAccountDTO.setUsername("Test");
        userAccountDTO.setPassword("Test123");
        userAccountDTO.setConfirmedPassword("Test123");
        userAccountDTO.setUserId(UserAccountControllerTests.users.get(0).getId().toString());
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test
	public void createUserValidationErrorUsernameToLarge() throws Exception {
        logger.info("createUserValidationErrorUsernameToLarge");
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole("ROLE_USER");
        userAccountDTO.setUsername("TestTestTestTestTestTest");
        userAccountDTO.setPassword("Test123");
        userAccountDTO.setConfirmedPassword("Test123");
        userAccountDTO.setUserId(UserAccountControllerTests.users.get(0).getId().toString());
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test
	public void createUserValidationErrorRoleWrong() throws Exception {
        logger.info("createUserValidationErrorRoleWrong");
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole("AAAA");
        userAccountDTO.setUsername("Test123");
        userAccountDTO.setPassword("Test123");
        userAccountDTO.setConfirmedPassword("Test123");
        userAccountDTO.setUserId(UserAccountControllerTests.users.get(0).getId().toString());
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test
	public void createUserValidationErrorPasswordAndConfirmedPasswordToSmall() throws Exception {
        logger.info("createUserValidationErrorPasswordAndConfirmedPasswordToSmall");
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole("ROLE_USER");
        userAccountDTO.setUsername("Test123");
        userAccountDTO.setPassword("Test");
        userAccountDTO.setConfirmedPassword("Test");
        userAccountDTO.setUserId(UserAccountControllerTests.users.get(0).getId().toString()); 
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test
	public void createUserValidationErrorPasswordAndConfirmedPasswordContainUnsupportedChars() throws Exception {
        logger.info("createUserValidationErrorPasswordAndConfirmedPasswordContainUnsupportedChars");
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole("ROLE_USER");
        userAccountDTO.setUsername("Test123");
        userAccountDTO.setPassword("Te+st!12&3");
        userAccountDTO.setConfirmedPassword("Te+st!12&3");
        userAccountDTO.setUserId(UserAccountControllerTests.users.get(0).getId().toString());   
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test
	public void createNullUserAccount() throws Exception {
        logger.info("createNullUserAccount");
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test
	public void createUserAccountNullRole() throws Exception {
        logger.info("createUserAccountNullRole");
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole(null);
        userAccountDTO.setUsername("Test123");
        userAccountDTO.setPassword("Test123");
        userAccountDTO.setConfirmedPassword("Test123");
        userAccountDTO.setUserId(UserAccountControllerTests.users.get(0).getId().toString());
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test
	public void createUserAccountBlankRole() throws Exception {
        logger.info("createUserAccountBlankRole");
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole("");
        userAccountDTO.setUsername("Test123");
        userAccountDTO.setPassword("Test123");
        userAccountDTO.setConfirmedPassword("Test123");
        userAccountDTO.setUserId(UserAccountControllerTests.users.get(0).getId().toString());
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test
	public void createUserAccountSpaceRole() throws Exception {
        logger.info("createUserAccountSpaceRole");
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole(" ");
        userAccountDTO.setUsername("Test123");
        userAccountDTO.setPassword("Test123");
        userAccountDTO.setConfirmedPassword("Test123");
        userAccountDTO.setUserId(UserAccountControllerTests.users.get(0).getId().toString());
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test
	public void createUserAccountNullUsername() throws Exception {
        logger.info("createUserAccountNullUsername");
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole("ROLE_USER");
        userAccountDTO.setUsername(null);
        userAccountDTO.setPassword("Test123");
        userAccountDTO.setConfirmedPassword("Test123");
        userAccountDTO.setUserId(UserAccountControllerTests.users.get(0).getId().toString());
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test
	public void createUserAccountBlankUsername() throws Exception {
        logger.info("createUserAccountBlankUsername");
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole("ROLE_USER");
        userAccountDTO.setUsername("");
        userAccountDTO.setPassword("Test123");
        userAccountDTO.setConfirmedPassword("Test123");
        userAccountDTO.setUserId(UserAccountControllerTests.users.get(0).getId().toString());
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test
	public void createUserAccountSpaceUsername() throws Exception {
        logger.info("createUserAccountSpaceUsername");
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole("ROLE_USER");
        userAccountDTO.setUsername(" ");
        userAccountDTO.setPassword("Test123");
        userAccountDTO.setConfirmedPassword("Test123");
        userAccountDTO.setUserId(UserAccountControllerTests.users.get(0).getId().toString());
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test
	public void createUserAccountNullPassword() throws Exception {
        logger.info("createUserAccountNullPassword");
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole("ROLE_USER");
        userAccountDTO.setUsername("Test123");
        userAccountDTO.setPassword(null);
        userAccountDTO.setConfirmedPassword("Test123");
        userAccountDTO.setUserId(UserAccountControllerTests.users.get(0).getId().toString());
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test
	public void createUserAccountBlankPassword() throws Exception {
        logger.info("createUserAccountBlankPassword");
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole("ROLE_USER");
        userAccountDTO.setUsername("Test123");
        userAccountDTO.setPassword("");
        userAccountDTO.setConfirmedPassword("Test123");
        userAccountDTO.setUserId(UserAccountControllerTests.users.get(0).getId().toString());
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test
	public void createUserAccountSpacePassword() throws Exception {
        logger.info("createUserAccountSpacePassword");
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole("ROLE_USER");
        userAccountDTO.setUsername("Test123");
        userAccountDTO.setPassword(" ");
        userAccountDTO.setConfirmedPassword("Test123");
        userAccountDTO.setUserId(UserAccountControllerTests.users.get(0).getId().toString());
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test
	public void createUserAccountNullConfirmedPassword() throws Exception {
        logger.info("createUserAccountNullConfirmedPassword");
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole("ROLE_USER");
        userAccountDTO.setUsername("Test123");
        userAccountDTO.setPassword("Test123");
        userAccountDTO.setConfirmedPassword(null);
        userAccountDTO.setUserId(UserAccountControllerTests.users.get(0).getId().toString());
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test
	public void createUserAccountBlankConfirmedPassword() throws Exception {
        logger.info("createUserAccountBlankConfirmedPassword");
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole("ROLE_USER");
        userAccountDTO.setUsername("Test123");
        userAccountDTO.setPassword("Test123");
        userAccountDTO.setConfirmedPassword("");
        userAccountDTO.setUserId(UserAccountControllerTests.users.get(0).getId().toString());
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test
	public void createUserAccountSpaceConfirmedPassword() throws Exception {
        logger.info("createUserAccountSpaceConfirmedPassword");
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole("ROLE_USER");
        userAccountDTO.setUsername("Test123");
        userAccountDTO.setPassword("Test123");
        userAccountDTO.setConfirmedPassword(" ");
        userAccountDTO.setUserId(UserAccountControllerTests.users.get(0).getId().toString());
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test
	public void createUserAccountNullUserId() throws Exception {
        logger.info("createUserAccountNullUserId");
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole("ROLE_USER");
        userAccountDTO.setUsername("Test123");
        userAccountDTO.setPassword("Test123");
        userAccountDTO.setConfirmedPassword("Test123");
        userAccountDTO.setUserId(null);
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test
	public void createUserAccountBlankUserId() throws Exception {
        logger.info("createUserAccountBlankUserId");
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole("ROLE_USER");
        userAccountDTO.setUsername("Test123");
        userAccountDTO.setPassword("Test123");
        userAccountDTO.setConfirmedPassword("Test123");
        userAccountDTO.setUserId("");
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test
	public void createUserAccountSpaceUserId() throws Exception {
        logger.info("createUserAccountSpaceUserId");
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole("ROLE_USER");
        userAccountDTO.setUsername("Test123");
        userAccountDTO.setPassword("Test123");
        userAccountDTO.setConfirmedPassword("Test123");
        userAccountDTO.setUserId(" ");
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test
	public void createUserAccountForThatUserExistsWithThatAccessRole() throws Exception {
        logger.info("createUserAccountForThatUserExistsWithThatAccessRole");
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole("ROLE_ADMIN");
        userAccountDTO.setUsername("Test123");
        userAccountDTO.setPassword("Test123");
        userAccountDTO.setConfirmedPassword("Test123");
        userAccountDTO.setUserId(UserAccountControllerTests.users.get(0).getId().toString());
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isForbidden());
	}

	@Test
	public void createUserAccountWrongUserId() throws Exception {
        logger.info("createUserAccountWrongUserId");
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole("ROLE_USER");
        userAccountDTO.setUsername("Test123");
        userAccountDTO.setPassword("Test123");
        userAccountDTO.setConfirmedPassword("Test123");
        userAccountDTO.setUserId(Integer.toString(UserAccountControllerTests.users.get(4).getId()+1));
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isNotFound());
	}

	@Test
	public void createUserAccountUsernameAlreadyExists() throws Exception {
        logger.info("createUserAccountUsernameAlreadyExists");
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole("ROLE_USER");
        userAccountDTO.setUsername("Test1234");
        userAccountDTO.setPassword("Test123");
        userAccountDTO.setConfirmedPassword("Test123");
        userAccountDTO.setUserId(UserAccountControllerTests.users.get(0).getId().toString());   
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isNotAcceptable());
	}

	@Test
	public void updateUserAccountWhichNotExists() throws Exception {	
        logger.info("updateUserAccountWhichNotExists");
    	UserAccountDTO userAccountDTO = new UserAccountDTO();
    	userAccountDTO.setUsername("Test12");
    	Gson gson = new Gson();
    	String json = gson.toJson(userAccountDTO);
		mockMvc.perform(put("/jobster/accounts/" + (UserAccountControllerTests.userAccounts.get(4).getId()+1))
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
    		//.andDo(MockMvcResultHandlers.print())
    		.andExpect(status().isNotFound());
	}	

	@Test
	public void updateUserAccountUsernameMinMarginalValue() throws Exception {
        logger.info("updateUserAccountUsernameMinMarginalValue");
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setUsername("Test5");
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(put("/jobster/accounts/" + UserAccountControllerTests.userAccounts.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
        	//.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.username", is("Test5")));
		UserAccountControllerTests.userAccounts.remove(0);
		UserAccountControllerTests.userAccounts.add(userAccountRepository.findByUsernameAndStatusLike(userAccountDTO.getUsername(), 1));
	}

	@Test
	public void updateUserAccountUsernameMaxMarginalValue() throws Exception {	
        logger.info("updateUserAccountUsernameMaxMarginalValue");
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setUsername("Test1234567891234567");
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(put("/jobster/accounts/" + UserAccountControllerTests.userAccounts.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
        	//.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.username", is("Test1234567891234567")));
		UserAccountControllerTests.userAccounts.remove(0);
		UserAccountControllerTests.userAccounts.add(userAccountRepository.findByUsernameAndStatusLike(userAccountDTO.getUsername(), 1));
	}

	@Test
	public void updateUserAccountPasswordAndConfirmedPasswordMinMarginalValue() throws Exception {
        logger.info("updateUserAccountPasswordAndConfirmedPasswordMinMarginalValue");
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setPassword("Test5");
        userAccountDTO.setConfirmedPassword("Test5");
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(put("/jobster/accounts/" + UserAccountControllerTests.userAccounts.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
        	//.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
        	.andExpect(content().contentType(contentType)) 
        	.andExpect(jsonPath("$.username", is("Test1234")));
		UserAccountControllerTests.userAccounts.remove(0);
		UserAccountControllerTests.userAccounts.add(userAccountRepository.findByUsernameAndStatusLike("Test1234", 1));
	}

	@Test
	public void updateUserAccountValidationErrorUsernameToSmall() throws Exception {
        logger.info("updateUserAccountValidationErrorUsernameToSmall");
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setUsername("Test");
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(put("/jobster/accounts/" + UserAccountControllerTests.userAccounts.get(0).getId())
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test
	public void updateUserAccountValidationErrorUsernameToLarge() throws Exception {
        logger.info("updateUserAccountValidationErrorUsernameToLarge");
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setUsername("TestTestTestTestTestTest");
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(put("/jobster/accounts/" + UserAccountControllerTests.userAccounts.get(0).getId())
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test
	public void updateUserAccountValidationErrorRole() throws Exception {
        logger.info("updateUserAccountValidationErrorRole");
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole("AAAA");
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(put("/jobster/accounts/" + UserAccountControllerTests.userAccounts.get(0).getId())
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test
	public void updateUserAccountValidationErrorPasswordAndConfirmedPasswordToSmall() throws Exception {
        logger.info("updateUserAccountValidationErrorPasswordAndConfirmedPasswordToSmall");
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setPassword("Test");
        userAccountDTO.setConfirmedPassword("Test");
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(put("/jobster/accounts/" + UserAccountControllerTests.userAccounts.get(0).getId())
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test
	public void updateUserAccountValidationErrorPasswordAndConfirmedPasswordContainUnsupportedChars() throws Exception {
        logger.info("updateUserAccountValidationErrorPasswordAndConfirmedPasswordContainUnsupportedChars");
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setPassword("Te+st!12&3");
        userAccountDTO.setConfirmedPassword("Te+st!12&3");
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(put("/jobster/accounts/" + UserAccountControllerTests.userAccounts.get(0).getId())
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test
	public void updateNullUserAccount() throws Exception {
        logger.info("updateNullUserAccount");
		UserAccountDTO userAccountDTO = new UserAccountDTO();
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(put("/jobster/accounts/" + UserAccountControllerTests.userAccounts.get(0).getId())
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test
	public void updateUserAccountNullUsername() throws Exception {
        logger.info("updateUserAccountNullUsername");
		UserAccountDTO userAccountDTO = new UserAccountDTO();
		userAccountDTO.setUsername(null);
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(put("/jobster/accounts/" + UserAccountControllerTests.userAccounts.get(0).getId())
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test
	public void updateUserAccountBlankUsername() throws Exception {
        logger.info("updateUserAccountBlankUsername");
		UserAccountDTO userAccountDTO = new UserAccountDTO();
		userAccountDTO.setUsername("");
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(put("/jobster/accounts/" + UserAccountControllerTests.userAccounts.get(0).getId())
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test
	public void updateUserAccountSpaceUsername() throws Exception {
        logger.info("updateUserAccountSpaceUsername");
		UserAccountDTO userAccountDTO = new UserAccountDTO();
		userAccountDTO.setUsername(" ");
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(put("/jobster/accounts/" + UserAccountControllerTests.userAccounts.get(0).getId())
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test
	public void updateUserAccountNullRole() throws Exception {
        logger.info("updateUserAccountNullRole");
		UserAccountDTO userAccountDTO = new UserAccountDTO();
		userAccountDTO.setAccessRole(null);
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(put("/jobster/accounts/" + UserAccountControllerTests.userAccounts.get(0).getId())
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test
	public void updateUserAccountBlankRole() throws Exception {
        logger.info("updateUserAccountBlankRole");
		UserAccountDTO userAccountDTO = new UserAccountDTO();
		userAccountDTO.setAccessRole("");
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(put("/jobster/accounts/" + UserAccountControllerTests.userAccounts.get(0).getId())
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test
	public void updateUserAccountSpaceRole() throws Exception {
        logger.info("updateUserAccountSpaceRole");
		UserAccountDTO userAccountDTO = new UserAccountDTO();
		userAccountDTO.setAccessRole(" ");
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(put("/jobster/accounts/" + UserAccountControllerTests.userAccounts.get(0).getId())
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test
	public void updateUserAccountNullPassword() throws Exception {
        logger.info("updateUserAccountNullPassword");
		UserAccountDTO userAccountDTO = new UserAccountDTO();
		userAccountDTO.setPassword(null);
		userAccountDTO.setConfirmedPassword("Test123");
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(put("/jobster/accounts/" + UserAccountControllerTests.userAccounts.get(0).getId())
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test
	public void updateUserAccountBlankPassword() throws Exception {
        logger.info("updateUserAccountBlankPassword");
		UserAccountDTO userAccountDTO = new UserAccountDTO();
		userAccountDTO.setPassword("");
		userAccountDTO.setConfirmedPassword("Test123");
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(put("/jobster/accounts/" + UserAccountControllerTests.userAccounts.get(0).getId())
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test
	public void updateUserAccountSpacePassword() throws Exception {
        logger.info("updateUserAccountSpacePassword");
		UserAccountDTO userAccountDTO = new UserAccountDTO();
		userAccountDTO.setPassword(" ");
		userAccountDTO.setConfirmedPassword("Test123");
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(put("/jobster/accounts/" + UserAccountControllerTests.userAccounts.get(0).getId())
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test
	public void updateUserAccountNullConfirmedPassword() throws Exception {
        logger.info("updateUserAccountNullConfirmedPassword");
		UserAccountDTO userAccountDTO = new UserAccountDTO();
		userAccountDTO.setPassword("Test123");
		userAccountDTO.setConfirmedPassword(null);
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(put("/jobster/accounts/" + UserAccountControllerTests.userAccounts.get(0).getId())
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test
	public void updateUserAccountBlankConfirmedPassword() throws Exception {
        logger.info("updateUserAccountBlankConfirmedPassword");
		UserAccountDTO userAccountDTO = new UserAccountDTO();
		userAccountDTO.setPassword("Test123");
		userAccountDTO.setConfirmedPassword("");
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(put("/jobster/accounts/" + UserAccountControllerTests.userAccounts.get(0).getId())
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test
	public void updateUserAccountSpaceConfirmedPassword() throws Exception {
        logger.info("updateUserAccountSpaceConfirmedPassword");
		UserAccountDTO userAccountDTO = new UserAccountDTO();
		userAccountDTO.setPassword("Test123");
		userAccountDTO.setConfirmedPassword(" ");
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(put("/jobster/accounts/" + UserAccountControllerTests.userAccounts.get(0).getId())
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test
	public void updateUserAccountNullUserId() throws Exception {
        logger.info("updateUserAccountNullUserId");
		UserAccountDTO userAccountDTO = new UserAccountDTO();
		userAccountDTO.setUserId(null);
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(put("/jobster/accounts/" + UserAccountControllerTests.userAccounts.get(0).getId())
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test
	public void updateUserAccountBlankUserId() throws Exception {
        logger.info("updateUserAccountBlankUserId");
		UserAccountDTO userAccountDTO = new UserAccountDTO();
		userAccountDTO.setUserId("");
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(put("/jobster/accounts/" + UserAccountControllerTests.userAccounts.get(0).getId())
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test
	public void updateUserAccountSpaceUserId() throws Exception {
        logger.info("updateUserAccountSpaceUserId");
		UserAccountDTO userAccountDTO = new UserAccountDTO();
		userAccountDTO.setUserId(" ");
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(put("/jobster/accounts/" + UserAccountControllerTests.userAccounts.get(0).getId())
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test
	public void updateUserAccountUsernameAlreadyExists() throws Exception {
        logger.info("updateUserAccountUsernameAlreadyExists");
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setUsername("Test1234");
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(put("/jobster/accounts/" + UserAccountControllerTests.userAccounts.get(0).getId())
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
        	//.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isNotAcceptable());
	}

	@Test
	public void archiveUserAccount() throws Exception {
        logger.info("archiveUserAccount");
        mockMvc.perform(put("/jobster/accounts/archive/" + UserAccountControllerTests.userAccounts.get(1).getId())
    			.header("Authorization", "Bearer " + token))
			 	.andExpect(status().isOk())
				.andExpect(content().contentType(contentType)) 
				.andExpect(jsonPath("$.id", is(UserAccountControllerTests.userAccounts.get(1).getId().intValue())))
				.andExpect(jsonPath("$.status", is(-1)));
	        UserAccountControllerTests.userAccounts.get(1).setStatusArchived();
	        userAccountRepository.deleteById(UserAccountControllerTests.userAccounts.get(1).getId());
	        UserAccountControllerTests.userAccounts.remove(UserAccountControllerTests.userAccounts.get(1));
	}

	@Test
	public void archiveUserAccountWhichNotExists() throws Exception {
        logger.info("archiveUserAccountWhichNotExists");
        mockMvc.perform(put("/jobster/accounts/archive/" + (UserAccountControllerTests.userAccounts.get(4).getId()+1))
			.header("Authorization", "Bearer " + token))
        	.andExpect(status().isNotFound());
	}	

	@Test
	public void undeleteUserAccount() throws Exception {
        logger.info("undeleteUserAccount");
        mockMvc.perform(put("/jobster/accounts/undelete/" + UserAccountControllerTests.userAccounts.get(3).getId())
			.header("Authorization", "Bearer " + token))
        	//.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.username", is(UserAccountControllerTests.userAccounts.get(3).getUsername())))
			.andExpect(jsonPath("$.status", is(1)));
        UserAccountControllerTests.userAccounts.get(3).setStatusActive();
        userAccountRepository.deleteById(UserAccountControllerTests.userAccounts.get(3).getId());
        UserAccountControllerTests.userAccounts.remove(UserAccountControllerTests.userAccounts.get(3));
	}

	@Test
	public void undeleteUserAccountWhichNotExists() throws Exception {
        logger.info("undeleteUserAccountWhichNotExists");
        mockMvc.perform(put("/jobster/accounts/undelete/" + (UserAccountControllerTests.userAccounts.get(4).getId()+1))
			.header("Authorization", "Bearer " + token))
        	.andExpect(status().isNotFound());
	}	

	@Test
	public void deleteUserAccount() throws Exception {
        logger.info("deleteUserAccount");
		mockMvc.perform(delete("/jobster/accounts/" + UserAccountControllerTests.userAccounts.get(1).getId())
			.header("Authorization", "Bearer " + token)) 
			.andExpect(status().isOk()) 
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.id", is(UserAccountControllerTests.userAccounts.get(1).getId().intValue())))
			.andExpect(jsonPath("$.status", is(0)));
    	UserAccountControllerTests.userAccounts.get(1).setStatusInactive();
    	userAccountRepository.deleteById(UserAccountControllerTests.userAccounts.get(1).getId());
		UserAccountControllerTests.userAccounts.remove(UserAccountControllerTests.userAccounts.get(1));
	}

	@Test
	public void deleteUserAccountWhichNotExists() throws Exception {	
        logger.info("deleteUserAccountWhichNotExists");
        mockMvc.perform(delete("/jobster/accounts/" + (UserAccountControllerTests.userAccounts.get(4).getId()+1))
			.header("Authorization", "Bearer " + token))
        	.andExpect(status().isNotFound());
	}	

}
