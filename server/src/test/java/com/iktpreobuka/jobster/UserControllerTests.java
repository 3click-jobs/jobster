package com.iktpreobuka.jobster;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
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
//@WithMockUser(username = "Test1234", roles = { "EUserRole.ROLE_USER" })
public class UserControllerTests {
 
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
			UserControllerTests.users.add(userRepository.save(new UserEntity(city, "0642345678", "Jobster@mail.com", "About Jobster")));
			UserControllerTests.users.add(userRepository.save(new UserEntity(city, "0643456789", "Jobsters@mail.com", "About Jobsters")));
			UserControllerTests.users.add(userRepository.save(new UserEntity(city, "0644567890", "Jobstery@mail.com", "About Jobstery")));
			user = new UserEntity(city, "0644567899", "Jobstery1@mail.com", "About Jobstery");
			user.setStatusInactive();
			UserControllerTests.users.add(userRepository.save(user));
			user = new UserEntity(city, "0644567898", "Jobstery2@mail.com", "About Jobstery");
			user.setStatusArchived();
			UserControllerTests.users.add(userRepository.save(user));
			UserControllerTests.userAccounts.add(userAccountRepository.save(new UserAccountEntity(UserControllerTests.users.get(0), EUserRole.ROLE_USER, "Test1234", "Test1234", UserControllerTests.users.get(0).getId())));
			UserControllerTests.userAccounts.add(userAccountRepository.save(new UserAccountEntity(UserControllerTests.users.get(1), EUserRole.ROLE_USER, "Test1235", "Test1234", UserControllerTests.users.get(0).getId())));
			UserControllerTests.userAccounts.add(userAccountRepository.save(new UserAccountEntity(UserControllerTests.users.get(2), EUserRole.ROLE_USER, "Test1236", "Test1234", UserControllerTests.users.get(0).getId())));
			userAccount = new UserAccountEntity(UserControllerTests.users.get(3), EUserRole.ROLE_USER, "Test1237", "Test1234", UserControllerTests.users.get(0).getId());
			userAccount.setStatusInactive();
			UserControllerTests.userAccounts.add(userAccountRepository.save(userAccount));
			userAccount = new UserAccountEntity(UserControllerTests.users.get(4), EUserRole.ROLE_USER, "Test1238", "Test1234", UserControllerTests.users.get(0).getId());
			userAccount.setStatusArchived();
			UserControllerTests.userAccounts.add(userAccountRepository.save(userAccount));
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
			for (UserAccountEntity acc : UserControllerTests.userAccounts) {
				userAccountRepository.delete(acc);
			}
			UserControllerTests.userAccounts.clear();
			for (UserEntity usr : UserControllerTests.users) {
				userRepository.delete(usr);
			}
			UserControllerTests.users.clear();
			for (CityEntity cty : UserControllerTests.cities) {
				/*Iterable<CityDistanceEntity> lcde = cityDistanceRepository.findByFromCity(cty);
				for (CityDistanceEntity cde : lcde) {
					cityDistanceRepository.delete(cde);
				}*/
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
			logger.info("DBtearDown ok");
		}
	}

	@Test @WithMockUser(username = "Test1234")
	public void userServiceNotFound() throws Exception { 
		mockMvc.perform(get("/jobster/users/users/readallusers/"))
			//.andDo(MockMvcResultHandlers.print())
			.andExpect(status().isNotFound());
	}

	@Test @WithMockUser(username = "Test1234")
	public void userServiceFound() throws Exception { 
		mockMvc.perform(get("/jobster/users/users/")).andExpect(status().isOk()); 
	}

	@Test @WithMockUser(username = "Test1234")
	public void readAllUsers() throws Exception { 
		mockMvc.perform(get("/jobster/users/users/")) 
			.andExpect(status().isOk()) 
			.andExpect(content().contentType(contentType));
		}

	@Test @WithMockUser(username = "Test1234")
	public void readAllUsersNotFound() throws Exception { 	
		for (UserAccountEntity acc : UserControllerTests.userAccounts) {
			userAccountRepository.delete(acc);
		}
		UserControllerTests.userAccounts.clear();
		for (UserEntity cmpny : UserControllerTests.users) {
			userRepository.delete(cmpny);
		}
		UserControllerTests.users.clear();
		mockMvc.perform(get("/jobster/users/users/"))
			.andExpect(status().isNotFound()); 
	}

}
