package com.iktpreobuka.jobster;

import static org.hamcrest.CoreMatchers.is;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
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

@RunWith(SpringRunner.class) 
@SpringBootTest 
@WebAppConfiguration 
//@WithMockUser(username = "Test1234", roles = { "EUserRole.ROLE_USER" })
public class UserAccountControllerTests {
 
	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(), 
			MediaType.APPLICATION_JSON.getSubtype(), 
			Charset.forName("utf8"));
	
	private static MockMvc mockMvc;
	/*@Autowired 
	private MockMvc mockMvc;
	
	@MockBean 
	private UserDao mockUserDao;*/
	
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
	
	/*@Autowired
	private UserDao userDao;*/
	
	@Autowired 
	private CityRepository cityRepository;
	
	@Autowired
	private CountryRegionRepository countryRegionRepository;

	@Autowired
	private CountryRepository countryRepository;
	
	@Autowired
	private UserAccountRepository userAccountRepository;
	
	/*@Autowired
	private CityDistanceRepository cityDistanceRepository;*/
	
	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	private boolean dbInit = false;

	@Before
	public void setUp() throws Exception { 
		logger.info("DBsetUp");
		if(!dbInit) { mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build(); 
			country = countryRepository.save(new CountryEntity("World Union", "XXX"));
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
			UserAccountControllerTests.userAccounts.add(userAccountRepository.save(new UserAccountEntity(UserAccountControllerTests.users.get(0), EUserRole.ROLE_ADMIN, "Test1234", "Test1234", UserAccountControllerTests.users.get(0).getId())));
			UserAccountControllerTests.userAccounts.add(userAccountRepository.save(new UserAccountEntity(UserAccountControllerTests.users.get(1), EUserRole.ROLE_USER, "Test1235", "Test1234", UserAccountControllerTests.users.get(0).getId())));
			UserAccountControllerTests.userAccounts.add(userAccountRepository.save(new UserAccountEntity(UserAccountControllerTests.users.get(2), EUserRole.ROLE_USER, "Test1236", "Test1234", UserAccountControllerTests.users.get(0).getId())));
			userAccount = new UserAccountEntity(UserAccountControllerTests.users.get(3), EUserRole.ROLE_USER, "Test1237", "Test1234", UserAccountControllerTests.users.get(0).getId());
			userAccount.setStatusInactive();
			UserAccountControllerTests.userAccounts.add(userAccountRepository.save(userAccount));
			userAccount = new UserAccountEntity(UserAccountControllerTests.users.get(4), EUserRole.ROLE_USER, "Test1238", "Test1234", UserAccountControllerTests.users.get(0).getId());
			userAccount.setStatusArchived();
			UserAccountControllerTests.userAccounts.add(userAccountRepository.save(userAccount));
			dbInit = true;
			logger.info("DBsetUp ok");
		} 
	}
	
	@After
	public void tearDown() throws Exception {
		logger.info("DBtearDown");
		if(dbInit) {
			/*if (userAccount != null)
				userAccountRepository.delete(userAccount);*/
			for (UserAccountEntity acc : UserAccountControllerTests.userAccounts) {
				userAccountRepository.delete(acc);
			}
			UserAccountControllerTests.userAccounts.clear();
			for (UserEntity usr : UserAccountControllerTests.users) {
				userRepository.delete(usr);
			}
			UserAccountControllerTests.users.clear();
			for (CityEntity cty : UserAccountControllerTests.cities) {
				/*Iterable<CityDistanceEntity> lcde = cityDistanceRepository.findByFromCity(cty);
				for (CityDistanceEntity cde : lcde) {
					cityDistanceRepository.delete(cde);
				}*/
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
			logger.info("DBtearDown ok");
		}
	}

	@Test @WithMockUser(username = "Test1234")
	public void userAccountServiceNotFound() throws Exception { 
		mockMvc.perform(get("/jobster/accounts/readallaccounts/"))
			//.andDo(MockMvcResultHandlers.print())
			.andExpect(status().isNotFound()); 
	}

	@Test @WithMockUser(username = "Test1234")
	public void userAccountServiceFound() throws Exception { 
		mockMvc.perform(get("/jobster/accounts/"))
			.andExpect(status().isOk()); 
	}

	@Test @WithMockUser(username = "Test1234")
	public void readAllUserAccounts() throws Exception { 
		mockMvc.perform(get("/jobster/accounts/")) 
			.andExpect(status().isOk()) 
			.andExpect(content().contentType(contentType));
	}

	@Test @WithMockUser(username = "Test1234")
	public void readAllUserAccountsNotFound() throws Exception { 
		for (UserAccountEntity acc : UserAccountControllerTests.userAccounts) {
			userAccountRepository.delete(acc);
		}
		UserAccountControllerTests.userAccounts.clear();
		mockMvc.perform(get("/jobster/accounts/"))
			.andExpect(status().isNotFound()); 
	}


	@Test @WithMockUser(username = "Test1234")
	public void readSingleUserAccount() throws Exception { 
		mockMvc.perform(get("/jobster/accounts/" + UserAccountControllerTests.userAccounts.get(0).getId()))
			//.andDo(MockMvcResultHandlers.print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(UserAccountControllerTests.userAccounts.get(0).getId().intValue()))); 
	}

	@Test @WithMockUser(username = "Test1234")
	public void readSingleUserAccountWhichNotExists() throws Exception { 
		mockMvc.perform(get("/jobster/accounts/" + (UserAccountControllerTests.userAccounts.get(4).getId()+1)))
			.andExpect(status().isNotFound()); 
	}

	@Test @WithMockUser(username = "Test1234")
	public void readAllDeletedUserAccounts() throws Exception { 
		mockMvc.perform(get("/jobster/accounts/deleted/")) 
			.andExpect(status().isOk()) 
			.andExpect(content().contentType(contentType));
	}

	@Test @WithMockUser(username = "Test1234")
	public void readAllDeletedUserAccountsNotFound() throws Exception { 
		for (UserAccountEntity acc : UserAccountControllerTests.userAccounts) {
			userAccountRepository.delete(acc);
		}
		UserAccountControllerTests.userAccounts.clear();
		mockMvc.perform(get("/jobster/accounts/deleted/"))
			.andExpect(status().isNotFound()); 
	}

	@Test @WithMockUser(username = "Test1234")
	public void readSingleDeletedUserAccount() throws Exception { 
		mockMvc.perform(get("/jobster/accounts/deleted/" + UserAccountControllerTests.userAccounts.get(3).getId()))
			//.andDo(MockMvcResultHandlers.print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(UserAccountControllerTests.users.get(3).getId().intValue()))); 
	}

	@Test @WithMockUser(username = "Test1234")
	public void readSingleDeletedUserAccountWhichNotExists() throws Exception { 
		mockMvc.perform(get("/jobster/accounts/deleted/" + UserAccountControllerTests.userAccounts.get(0).getId()))
			.andExpect(status().isNotFound()); 
	}

	@Test @WithMockUser(username = "Test1234")
	public void readAllArchivedUserAccounts() throws Exception { 
		mockMvc.perform(get("/jobster/accounts/archived/")) 
			.andExpect(status().isOk()) 
			.andExpect(content().contentType(contentType));
	}

	@Test @WithMockUser(username = "Test1234")
	public void readAllArchivedUserAccountsNotFound() throws Exception { 
		for (UserAccountEntity acc : UserAccountControllerTests.userAccounts) {
			userAccountRepository.delete(acc);
		}
		UserAccountControllerTests.userAccounts.clear();
		mockMvc.perform(get("/jobster/accounts/archived/"))
			.andExpect(status().isNotFound()); 
	}

	@Test @WithMockUser(username = "Test1234")
	public void readSingleArchivedUserAccount() throws Exception { 
		mockMvc.perform(get("/jobster/accounts/archived/" + UserAccountControllerTests.userAccounts.get(4).getId()))
			//.andDo(MockMvcResultHandlers.print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(UserAccountControllerTests.users.get(4).getId().intValue()))); 
	}

	@Test @WithMockUser(username = "Test1234")
	public void readSingleArchivedUserAccountWhichNotExists() throws Exception { 
		mockMvc.perform(get("/jobster/accounts/archived/" + (UserAccountControllerTests.userAccounts.get(4).getId()+1)))
			.andExpect(status().isNotFound()); 
	}

	@Test @WithMockUser(username = "Test1234")
	public void createUserAccount() throws Exception {
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole("ROLE_USER");
        userAccountDTO.setUsername("Test123");
        userAccountDTO.setPassword("Test123");
        userAccountDTO.setConfirmedPassword("Test123");
        userAccountDTO.setUserId(UserAccountControllerTests.users.get(0).getId().toString());
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
		.contentType(MediaType.APPLICATION_JSON).content(json))
        	//.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
		.andExpect(content().contentType(contentType)) 
		.andExpect(jsonPath("$.username", is("Test123")));
	userAccountRepository.delete(userAccountRepository.getByUsername("Test123"));
	}

	@Test @WithMockUser(username = "Test1234")
	public void createUserMarginalValueUsernameMin() throws Exception {
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole("ROLE_USER");
        userAccountDTO.setUsername("Test5");
        userAccountDTO.setPassword("Test123");
        userAccountDTO.setConfirmedPassword("Test123");
        userAccountDTO.setUserId(UserAccountControllerTests.users.get(0).getId().toString());
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
		.contentType(MediaType.APPLICATION_JSON).content(json))
        	//.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
		.andExpect(content().contentType(contentType)) 
		.andExpect(jsonPath("$.username", is("Test5")));
	userAccountRepository.delete(userAccountRepository.getByUsername("Test5"));
	}

	@Test @WithMockUser(username = "Test1234")
	public void createUserMarginalValueUsernameMax() throws Exception {
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole("ROLE_USER");
        userAccountDTO.setUsername("Test1234567891234567");
        userAccountDTO.setPassword("Test123");
        userAccountDTO.setConfirmedPassword("Test123");
	userAccountDTO.setUserId(UserAccountControllerTests.users.get(0).getId().toString());
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
		.contentType(MediaType.APPLICATION_JSON).content(json))
        	//.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
		.andExpect(content().contentType(contentType)) 
		.andExpect(jsonPath("$.username", is("Test1234567891234567")));
	userAccountRepository.delete(userAccountRepository.getByUsername("Test1234567891234567"));
	}

	@Test @WithMockUser(username = "Test1234")
	public void createUserMarginalValuePasswordAndConfirmedPasswordMin() throws Exception {
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole("ROLE_USER");
        userAccountDTO.setUsername("Test123");
        userAccountDTO.setPassword("Test5");
        userAccountDTO.setConfirmedPassword("Test5");
	userAccountDTO.setUserId(UserAccountControllerTests.users.get(0).getId().toString());
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
		.contentType(MediaType.APPLICATION_JSON).content(json))
        	//.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
		.andExpect(content().contentType(contentType)) 
		.andExpect(jsonPath("$.username", is("Test123")));
	userAccountRepository.delete(userAccountRepository.getByUsername("Test123"));
	}

	@Test @WithMockUser(username = "Test1234")
	public void createUserValidationErrorUsernameToSmall() throws Exception {
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole("ROLE_USER");
        userAccountDTO.setUsername("Test");
        userAccountDTO.setPassword("Test123");
        userAccountDTO.setConfirmedPassword("Test123");
	userAccountDTO.setUserId(UserAccountControllerTests.users.get(0).getId().toString());
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test @WithMockUser(username = "Test1234")
	public void createUserValidationErrorUsernameToLarge() throws Exception {
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole("ROLE_USER");
        userAccountDTO.setUsername("TestTestTestTestTestTest");
        userAccountDTO.setPassword("Test123");
        userAccountDTO.setConfirmedPassword("Test123");
	userAccountDTO.setUserId(UserAccountControllerTests.users.get(0).getId().toString());
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test @WithMockUser(username = "Test1234")
	public void createUserValidationErrorRoleWrong() throws Exception {
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole("AAAA");
        userAccountDTO.setUsername("Test123");
        userAccountDTO.setPassword("Test123");
        userAccountDTO.setConfirmedPassword("Test123");
	userAccountDTO.setUserId(UserAccountControllerTests.users.get(0).getId().toString());
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test @WithMockUser(username = "Test1234")
	public void createUserValidationErrorPasswordAndConfirmedPasswordToSmall() throws Exception {
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole("ROLE_USER");
        userAccountDTO.setUsername("Test123");
        userAccountDTO.setPassword("Test");
        userAccountDTO.setConfirmedPassword("Test");
	userAccountDTO.setUserId(UserAccountControllerTests.users.get(0).getId().toString()); 
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test @WithMockUser(username = "Test1234")
	public void createUserValidationErrorPasswordAndConfirmedPasswordContainUnsupportedChars() throws Exception {
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole("ROLE_USER");
        userAccountDTO.setUsername("Test123");
        userAccountDTO.setPassword("Te+st!12&3");
        userAccountDTO.setConfirmedPassword("Te+st!12&3");
	userAccountDTO.setUserId(UserAccountControllerTests.users.get(0).getId().toString());   
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test @WithMockUser(username = "Test1234")
	public void createNullUserAccount() throws Exception {
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test @WithMockUser(username = "Test1234")
	public void createUserAccountNullRole() throws Exception {
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole(null);
        userAccountDTO.setUsername("Test123");
        userAccountDTO.setPassword("Test123");
        userAccountDTO.setConfirmedPassword("Test123");
	userAccountDTO.setUserId(UserAccountControllerTests.users.get(0).getId().toString());
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test @WithMockUser(username = "Test1234")
	public void createUserAccountBlankRole() throws Exception {
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole("");
        userAccountDTO.setUsername("Test123");
        userAccountDTO.setPassword("Test123");
        userAccountDTO.setConfirmedPassword("Test123");
	userAccountDTO.setUserId(UserAccountControllerTests.users.get(0).getId().toString());
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test @WithMockUser(username = "Test1234")
	public void createUserAccountSpaceRole() throws Exception {
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole(" ");
        userAccountDTO.setUsername("Test123");
        userAccountDTO.setPassword("Test123");
        userAccountDTO.setConfirmedPassword("Test123");
	userAccountDTO.setUserId(UserAccountControllerTests.users.get(0).getId().toString());
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test @WithMockUser(username = "Test1234")
	public void createUserAccountNullUsername() throws Exception {
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole("ROLE_USER");
        userAccountDTO.setUsername(null);
        userAccountDTO.setPassword("Test123");
        userAccountDTO.setConfirmedPassword("Test123");
	userAccountDTO.setUserId(UserAccountControllerTests.users.get(0).getId().toString());
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test @WithMockUser(username = "Test1234")
	public void createUserAccountBlankUsername() throws Exception {
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole("ROLE_USER");
        userAccountDTO.setUsername("");
        userAccountDTO.setPassword("Test123");
        userAccountDTO.setConfirmedPassword("Test123");
	userAccountDTO.setUserId(UserAccountControllerTests.users.get(0).getId().toString());
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test @WithMockUser(username = "Test1234")
	public void createUserAccountSpaceUsername() throws Exception {
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole("ROLE_USER");
        userAccountDTO.setUsername(" ");
        userAccountDTO.setPassword("Test123");
        userAccountDTO.setConfirmedPassword("Test123");
	userAccountDTO.setUserId(UserAccountControllerTests.users.get(0).getId().toString());
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test @WithMockUser(username = "Test1234")
	public void createUserAccountNullPassword() throws Exception {
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole("ROLE_USER");
        userAccountDTO.setUsername("Test123");
        userAccountDTO.setPassword(null);
        userAccountDTO.setConfirmedPassword("Test123");
	userAccountDTO.setUserId(UserAccountControllerTests.users.get(0).getId().toString());
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test @WithMockUser(username = "Test1234")
	public void createUserAccountBlankPassword() throws Exception {
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole("ROLE_USER");
        userAccountDTO.setUsername("Test123");
        userAccountDTO.setPassword("");
        userAccountDTO.setConfirmedPassword("Test123");
	userAccountDTO.setUserId(UserAccountControllerTests.users.get(0).getId().toString());
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test @WithMockUser(username = "Test1234")
	public void createUserAccountSpacePassword() throws Exception {
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole("ROLE_USER");
        userAccountDTO.setUsername("Test123");
        userAccountDTO.setPassword(" ");
        userAccountDTO.setConfirmedPassword("Test123");
	userAccountDTO.setUserId(UserAccountControllerTests.users.get(0).getId().toString());
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test @WithMockUser(username = "Test1234")
	public void createUserAccountNullConfirmedPassword() throws Exception {
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole("ROLE_USER");
        userAccountDTO.setUsername("Test123");
        userAccountDTO.setPassword("Test123");
        userAccountDTO.setConfirmedPassword(null);
	userAccountDTO.setUserId(UserAccountControllerTests.users.get(0).getId().toString());
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test @WithMockUser(username = "Test1234")
	public void createUserAccountBlankConfirmedPassword() throws Exception {
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole("ROLE_USER");
        userAccountDTO.setUsername("Test123");
        userAccountDTO.setPassword("Test123");
        userAccountDTO.setConfirmedPassword("");
	userAccountDTO.setUserId(UserAccountControllerTests.users.get(0).getId().toString());
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test @WithMockUser(username = "Test1234")
	public void createUserAccountSpaceConfirmedPassword() throws Exception {
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole("ROLE_USER");
        userAccountDTO.setUsername("Test123");
        userAccountDTO.setPassword("Test123");
        userAccountDTO.setConfirmedPassword(" ");
	userAccountDTO.setUserId(UserAccountControllerTests.users.get(0).getId().toString());
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test @WithMockUser(username = "Test1234")
	public void createUserAccountNullUserId() throws Exception {
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole("ROLE_USER");
        userAccountDTO.setUsername("Test123");
        userAccountDTO.setPassword("Test123");
        userAccountDTO.setConfirmedPassword("Test123");
	userAccountDTO.setUserId(null);
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test @WithMockUser(username = "Test1234")
	public void createUserAccountBlankUserId() throws Exception {
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole("ROLE_USER");
        userAccountDTO.setUsername("Test123");
        userAccountDTO.setPassword("Test123");
        userAccountDTO.setConfirmedPassword("Test123");
	userAccountDTO.setUserId("");
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test @WithMockUser(username = "Test1234")
	public void createUserAccountSpaceUserId() throws Exception {
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole("ROLE_USER");
        userAccountDTO.setUsername("Test123");
        userAccountDTO.setPassword("Test123");
        userAccountDTO.setConfirmedPassword("Test123");
	userAccountDTO.setUserId(" ");
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test @WithMockUser(username = "Test1234")
	public void createUserAccountForThatUserExistsWithThatAccessRole() throws Exception {
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole("ROLE_ADMIN");
        userAccountDTO.setUsername("Test123");
        userAccountDTO.setPassword("Test123");
        userAccountDTO.setConfirmedPassword("Test123");
	userAccountDTO.setUserId(UserAccountControllerTests.users.get(0).getId().toString());
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isForbidden());
	}

	@Test @WithMockUser(username = "Test1234")
	public void createUserAccountWrongUserId() throws Exception {
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole("ROLE_USER");
        userAccountDTO.setUsername("Test123");
        userAccountDTO.setPassword("Test123");
        userAccountDTO.setConfirmedPassword("Test123");
	userAccountDTO.setUserId(Integer.toString(UserAccountControllerTests.users.get(4).getId()+1));
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isNotFound());
	}

	@Test @WithMockUser(username = "Test1234")
	public void createUserAccountUsernameAlreadyExists() throws Exception {
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole("ROLE_USER");
        userAccountDTO.setUsername("Test1234");
        userAccountDTO.setPassword("Test123");
        userAccountDTO.setConfirmedPassword("Test123");
	userAccountDTO.setUserId(UserAccountControllerTests.users.get(0).getId().toString());   
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(post("/jobster/accounts/")
		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isNotAcceptable());
	}

	@Test @WithMockUser(username = "Test1234")
	public void updateUserAccountWhichNotExists() throws Exception {	
        	UserAccountDTO userAccountDTO = new UserAccountDTO();
		userAccountDTO.setUsername("Test12");
        	Gson gson = new Gson();
        	String json = gson.toJson(userAccountDTO);
		mockMvc.perform(put("/jobster/accounts/" + (UserAccountControllerTests.userAccounts.get(4).getId()+1))
        		.contentType(MediaType.APPLICATION_JSON).content(json))
        		//.andDo(MockMvcResultHandlers.print())
        		.andExpect(status().isNotFound());
	}	

	@Test @WithMockUser(username = "Test1234")
	public void updateUserAccountUsernameMinMarginalValue() throws Exception {
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setUsername("Test5");
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
	mockMvc.perform(put("/jobster/accounts/" + UserAccountControllerTests.userAccounts.get(0).getId())
        	.contentType(MediaType.APPLICATION_JSON).content(json))
        	//.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
		.andExpect(content().contentType(contentType)) 
		.andExpect(jsonPath("$.username", is("Test5")));
	UserAccountControllerTests.userAccounts.remove(0);
	UserAccountControllerTests.userAccounts.add(userAccountRepository.findByUsernameAndStatusLike(userAccountDTO.getUsername(), 1));
	}

	@Test @WithMockUser(username = "Test1234")
	public void updateUserAccountUsernameMaxMarginalValue() throws Exception {	
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setUsername("Test1234567891234567");
	Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
	mockMvc.perform(put("/jobster/accounts/" + UserAccountControllerTests.userAccounts.get(0).getId())
        	.contentType(MediaType.APPLICATION_JSON).content(json))
        	//.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
		.andExpect(content().contentType(contentType)) 
		.andExpect(jsonPath("$.username", is("Test1234567891234567")));
	UserAccountControllerTests.userAccounts.remove(0);
	UserAccountControllerTests.userAccounts.add(userAccountRepository.findByUsernameAndStatusLike(userAccountDTO.getUsername(), 1));
	}

	@Test @WithMockUser(username = "Test1234")
	public void updateUserAccountPasswordAndConfirmedPasswordMinMarginalValue() throws Exception {
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setPassword("Test5");
        userAccountDTO.setConfirmedPassword("Test5");
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(put("/jobster/accounts/" + UserAccountControllerTests.userAccounts.get(0).getId())
        	.contentType(MediaType.APPLICATION_JSON).content(json))
        	//.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
        	.andExpect(content().contentType(contentType)) 
        	.andExpect(jsonPath("$.username", is("Test1234")));
	UserAccountControllerTests.userAccounts.remove(0);
	UserAccountControllerTests.userAccounts.add(userAccountRepository.findByUsernameAndStatusLike("Test1234", 1));
	}

	@Test @WithMockUser(username = "Test1234")
	public void updateUserAccountValidationErrorUsernameToSmall() throws Exception {
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setUsername("Test");
	Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(put("/jobster/accounts/" + UserAccountControllerTests.userAccounts.get(0).getId())
		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test @WithMockUser(username = "Test1234")
	public void updateUserAccountValidationErrorUsernameToLarge() throws Exception {
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setUsername("TestTestTestTestTestTest");
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(put("/jobster/accounts/" + UserAccountControllerTests.userAccounts.get(0).getId())
		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test @WithMockUser(username = "Test1234")
	public void updateUserAccountValidationErrorRole() throws Exception {
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setAccessRole("AAAA");
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(put("/jobster/accounts/" + UserAccountControllerTests.userAccounts.get(0).getId())
		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test @WithMockUser(username = "Test1234")
	public void updateUserAccountValidationErrorPasswordAndConfirmedPasswordToSmall() throws Exception {
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setPassword("Test");
        userAccountDTO.setConfirmedPassword("Test");
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(put("/jobster/accounts/" + UserAccountControllerTests.userAccounts.get(0).getId())
		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test @WithMockUser(username = "Test1234")
	public void updateUserAccountValidationErrorPasswordAndConfirmedPasswordContainUnsupportedChars() throws Exception {
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setPassword("Te+st!12&3");
        userAccountDTO.setConfirmedPassword("Te+st!12&3");
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(put("/jobster/accounts/" + UserAccountControllerTests.userAccounts.get(0).getId())
		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test @WithMockUser(username = "Test1234")
	public void updateNullUserAccount() throws Exception {
		UserAccountDTO userAccountDTO = new UserAccountDTO();
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(put("/jobster/accounts/" + UserAccountControllerTests.userAccounts.get(0).getId())
		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test @WithMockUser(username = "Test1234")
	public void updateUserAccountNullUsername() throws Exception {
		UserAccountDTO userAccountDTO = new UserAccountDTO();
		userAccountDTO.setUsername(null);
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(put("/jobster/accounts/" + UserAccountControllerTests.userAccounts.get(0).getId())
		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test @WithMockUser(username = "Test1234")
	public void updateUserAccountBlankUsername() throws Exception {
		UserAccountDTO userAccountDTO = new UserAccountDTO();
		userAccountDTO.setUsername("");
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(put("/jobster/accounts/" + UserAccountControllerTests.userAccounts.get(0).getId())
		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test @WithMockUser(username = "Test1234")
	public void updateUserAccountSpaceUsername() throws Exception {
		UserAccountDTO userAccountDTO = new UserAccountDTO();
		userAccountDTO.setUsername(" ");
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(put("/jobster/accounts/" + UserAccountControllerTests.userAccounts.get(0).getId())
		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test @WithMockUser(username = "Test1234")
	public void updateUserAccountNullRole() throws Exception {
		UserAccountDTO userAccountDTO = new UserAccountDTO();
		userAccountDTO.setAccessRole(null);
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(put("/jobster/accounts/" + UserAccountControllerTests.userAccounts.get(0).getId())
		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test @WithMockUser(username = "Test1234")
	public void updateUserAccountBlankRole() throws Exception {
		UserAccountDTO userAccountDTO = new UserAccountDTO();
		userAccountDTO.setAccessRole("");
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(put("/jobster/accounts/" + UserAccountControllerTests.userAccounts.get(0).getId())
		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test @WithMockUser(username = "Test1234")
	public void updateUserAccountSpaceRole() throws Exception {
		UserAccountDTO userAccountDTO = new UserAccountDTO();
		userAccountDTO.setAccessRole(" ");
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(put("/jobster/accounts/" + UserAccountControllerTests.userAccounts.get(0).getId())
		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test @WithMockUser(username = "Test1234")
	public void updateUserAccountNullPassword() throws Exception {
		UserAccountDTO userAccountDTO = new UserAccountDTO();
		userAccountDTO.setPassword(null);
		userAccountDTO.setConfirmedPassword("Test123");
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(put("/jobster/accounts/" + UserAccountControllerTests.userAccounts.get(0).getId())
		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test @WithMockUser(username = "Test1234")
	public void updateUserAccountBlankPassword() throws Exception {
		UserAccountDTO userAccountDTO = new UserAccountDTO();
		userAccountDTO.setPassword("");
		userAccountDTO.setConfirmedPassword("Test123");
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(put("/jobster/accounts/" + UserAccountControllerTests.userAccounts.get(0).getId())
		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test @WithMockUser(username = "Test1234")
	public void updateUserAccountSpacePassword() throws Exception {
		UserAccountDTO userAccountDTO = new UserAccountDTO();
		userAccountDTO.setPassword(" ");
		userAccountDTO.setConfirmedPassword("Test123");
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(put("/jobster/accounts/" + UserAccountControllerTests.userAccounts.get(0).getId())
		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test @WithMockUser(username = "Test1234")
	public void updateUserAccountNullConfirmedPassword() throws Exception {
		UserAccountDTO userAccountDTO = new UserAccountDTO();
		userAccountDTO.setPassword("Test123");
		userAccountDTO.setConfirmedPassword(null);
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(put("/jobster/accounts/" + UserAccountControllerTests.userAccounts.get(0).getId())
		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test @WithMockUser(username = "Test1234")
	public void updateUserAccountBlankConfirmedPassword() throws Exception {
		UserAccountDTO userAccountDTO = new UserAccountDTO();
		userAccountDTO.setPassword("Test123");
		userAccountDTO.setConfirmedPassword("");
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(put("/jobster/accounts/" + UserAccountControllerTests.userAccounts.get(0).getId())
		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test @WithMockUser(username = "Test1234")
	public void updateUserAccountSpaceConfirmedPassword() throws Exception {
		UserAccountDTO userAccountDTO = new UserAccountDTO();
		userAccountDTO.setPassword("Test123");
		userAccountDTO.setConfirmedPassword(" ");
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(put("/jobster/accounts/" + UserAccountControllerTests.userAccounts.get(0).getId())
		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test @WithMockUser(username = "Test1234")
	public void updateUserAccountNullUserId() throws Exception {
		UserAccountDTO userAccountDTO = new UserAccountDTO();
		userAccountDTO.setUserId(null);
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(put("/jobster/accounts/" + UserAccountControllerTests.userAccounts.get(0).getId())
		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test @WithMockUser(username = "Test1234")
	public void updateUserAccountBlankUserId() throws Exception {
		UserAccountDTO userAccountDTO = new UserAccountDTO();
		userAccountDTO.setUserId("");
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(put("/jobster/accounts/" + UserAccountControllerTests.userAccounts.get(0).getId())
		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test @WithMockUser(username = "Test1234")
	public void updateUserAccountSpaceUserId() throws Exception {
		UserAccountDTO userAccountDTO = new UserAccountDTO();
		userAccountDTO.setUserId(" ");
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
        mockMvc.perform(put("/jobster/accounts/" + UserAccountControllerTests.userAccounts.get(0).getId())
		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test @WithMockUser(username = "Test1234")
	public void updateUserAccountUsernameAlreadyExists() throws Exception {
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setUsername("Test1234");
        Gson gson = new Gson();
        String json = gson.toJson(userAccountDTO);
	mockMvc.perform(put("/jobster/accounts/" + UserAccountControllerTests.userAccounts.get(0).getId())
		.contentType(MediaType.APPLICATION_JSON).content(json))
        	//.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isNotAcceptable());
	}

	@Test @WithMockUser(username = "Test1234")
	public void archiveUserAccount() throws Exception {
        mockMvc.perform(put("/jobster/accounts/archive/" + UserAccountControllerTests.userAccounts.get(0).getId()))
	 	.andExpect(status().isOk())
		.andExpect(content().contentType(contentType)) 
		.andExpect(jsonPath("$.id", is(UserAccountControllerTests.userAccounts.get(0).getId().intValue())))
		.andExpect(jsonPath("$.status", is(-1)));
        UserAccountControllerTests.userAccounts.get(0).setStatusArchived();
        userAccountRepository.deleteById(UserAccountControllerTests.userAccounts.get(0).getId());
	UserAccountControllerTests.userAccounts.remove(UserAccountControllerTests.userAccounts.get(0));
	}

	@Test @WithMockUser(username = "Test1234")
	public void archiveUserAccountWhichNotExists() throws Exception {
        mockMvc.perform(put("/jobster/accounts/archive/" + (UserAccountControllerTests.userAccounts.get(4).getId()+1)))
        	.andExpect(status().isNotFound());
	}	

	@Test @WithMockUser(username = "Test1234")
	public void undeleteUserAccount() throws Exception {
        mockMvc.perform(put("/jobster/accounts/undelete/" + UserAccountControllerTests.userAccounts.get(3).getId()))
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
        mockMvc.perform(put("/jobster/accounts/undelete/" + (UserAccountControllerTests.userAccounts.get(4).getId()+1)))
        	.andExpect(status().isNotFound());
	}	

	@Test @WithMockUser(username = "Test1234")
	public void deleteUserAccount() throws Exception {
		mockMvc.perform(delete("/jobster/accounts/" + UserAccountControllerTests.userAccounts.get(0).getId())) 
			.andExpect(status().isOk()) 
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.id", is(UserAccountControllerTests.userAccounts.get(0).getId().intValue())))
			.andExpect(jsonPath("$.status", is(0)));
        	UserAccountControllerTests.userAccounts.get(0).setStatusInactive();
        	userAccountRepository.deleteById(UserAccountControllerTests.userAccounts.get(0).getId());
		UserAccountControllerTests.userAccounts.remove(UserAccountControllerTests.userAccounts.get(0));
	}

	@Test @WithMockUser(username = "Test1234")
	public void deleteUserAccountWhichNotExists() throws Exception {	
        mockMvc.perform(delete("/jobster/accounts/" + (UserAccountControllerTests.userAccounts.get(4).getId()+1)))
        	.andExpect(status().isNotFound());
	}	

}
