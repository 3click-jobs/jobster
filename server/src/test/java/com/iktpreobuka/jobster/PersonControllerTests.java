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
import java.util.Date;
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
import com.iktpreobuka.jobster.entities.PersonEntity;
import com.iktpreobuka.jobster.entities.UserAccountEntity;
import com.iktpreobuka.jobster.entities.dto.PersonDTO;
import com.iktpreobuka.jobster.enumerations.EGender;
import com.iktpreobuka.jobster.enumerations.EUserRole;
import com.iktpreobuka.jobster.repositories.CityRepository;
import com.iktpreobuka.jobster.repositories.CountryRegionRepository;
import com.iktpreobuka.jobster.repositories.CountryRepository;
import com.iktpreobuka.jobster.repositories.PersonRepository;
import com.iktpreobuka.jobster.repositories.UserAccountRepository;

@RunWith(SpringRunner.class) 
@SpringBootTest 
@WebAppConfiguration 
//@WithMockUser(username = "Test1234", roles = { "EUserRole.ROLE_USER" })

public class PersonControllerTests {
 

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
	
	private static PersonEntity person;

	private static CountryRegionEntity regionNoName;

	private static UserAccountEntity userAccount;
	
	private static List<PersonEntity> persons = new ArrayList<>();
	
	private static List<UserAccountEntity> userAccounts = new ArrayList<>();
	
	private static List<CountryEntity> countries = new ArrayList<>();

	private static List<CountryRegionEntity> countryRegions = new ArrayList<>();

	private static List<CityEntity> cities = new ArrayList<>();

	@Autowired 
	private PersonRepository personRepository;
	
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
	
	private String token;


	@SuppressWarnings("deprecation")
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
			PersonControllerTests.persons.add(personRepository.save(new PersonEntity(city, "0642345678", "Jobster@mail.com", "About Jobster", "Jobster", "Jobster", EGender.GENDER_MALE, new Date(1985, 10, 22))));
			PersonControllerTests.persons.add(personRepository.save(new PersonEntity(city, "0643456789", "Jobsters@mail.com", "About Jobsters", "Jobsters", "Jobsters", EGender.GENDER_FEMALE, new Date(1985, 10, 22))));
			PersonControllerTests.persons.add(personRepository.save(new PersonEntity(city, "0644567890", "Jobstery@mail.com", "About Jobstery", "Jobstery", "Jobstery", EGender.GENDER_MALE, new Date(1985, 10, 22))));
			person = new PersonEntity(city, "0644567899", "Jobstery1@mail.com", "About Jobstery", "Jobsterya", "Jobsterya", EGender.GENDER_FEMALE, new Date(1985, 10, 22));
			person.setStatusInactive();
			PersonControllerTests.persons.add(personRepository.save(person));
			person = new PersonEntity(city, "0644567898", "Jobstery2@mail.com", "About Jobstery", "Jobsteryb", "Jobsteryb", EGender.GENDER_MALE, new Date(1985, 10, 22));
			person.setStatusArchived();
			PersonControllerTests.persons.add(personRepository.save(person));
			PersonControllerTests.userAccounts.add(userAccountRepository.save(new UserAccountEntity(PersonControllerTests.persons.get(0), EUserRole.ROLE_ADMIN, "Test1234", "{bcrypt}$2a$10$FZjQbu7AqcSp0ns.GAxkbu0eKVUtNFTZNdVWwPOtBATLF0Bs9wtW2", PersonControllerTests.persons.get(0).getId())));
			PersonControllerTests.userAccounts.add(userAccountRepository.save(new UserAccountEntity(PersonControllerTests.persons.get(1), EUserRole.ROLE_USER, "Test1235", "{bcrypt}$2a$10$FZjQbu7AqcSp0ns.GAxkbu0eKVUtNFTZNdVWwPOtBATLF0Bs9wtW2", PersonControllerTests.persons.get(0).getId())));
			PersonControllerTests.userAccounts.add(userAccountRepository.save(new UserAccountEntity(PersonControllerTests.persons.get(2), EUserRole.ROLE_USER, "Test1236", "{bcrypt}$2a$10$FZjQbu7AqcSp0ns.GAxkbu0eKVUtNFTZNdVWwPOtBATLF0Bs9wtW2", PersonControllerTests.persons.get(0).getId())));
			userAccount = new UserAccountEntity(PersonControllerTests.persons.get(3), EUserRole.ROLE_USER, "Test1237", "{bcrypt}$2a$10$FZjQbu7AqcSp0ns.GAxkbu0eKVUtNFTZNdVWwPOtBATLF0Bs9wtW2", PersonControllerTests.persons.get(0).getId());
			userAccount.setStatusInactive();
			PersonControllerTests.userAccounts.add(userAccountRepository.save(userAccount));
			userAccount = new UserAccountEntity(PersonControllerTests.persons.get(4), EUserRole.ROLE_USER, "Test1238", "{bcrypt}$2a$10$FZjQbu7AqcSp0ns.GAxkbu0eKVUtNFTZNdVWwPOtBATLF0Bs9wtW2", PersonControllerTests.persons.get(0).getId());
			userAccount.setStatusArchived();
			PersonControllerTests.userAccounts.add(userAccountRepository.save(userAccount));
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
			for (UserAccountEntity acc : PersonControllerTests.userAccounts) {
				userAccountRepository.delete(acc);
			}
			PersonControllerTests.userAccounts.clear();
			for (PersonEntity prsn : PersonControllerTests.persons) {
				personRepository.delete(prsn);
			}
			PersonControllerTests.persons.clear();
			for (CityEntity cty : PersonControllerTests.cities) {
				/*Iterable<CityDistanceEntity> lcde = cityDistanceRepository.findByFromCity(cty);
				for (CityDistanceEntity cde : lcde) {
					cityDistanceRepository.delete(cde);
				}*/
				cityRepository.delete(cty); 
			}
			PersonControllerTests.cities.clear();
			for (CountryRegionEntity creg : PersonControllerTests.countryRegions) {
				countryRegionRepository.delete(creg);	
			}
			PersonControllerTests.countryRegions.clear();
			for (CountryEntity cntry : PersonControllerTests.countries) {
				countryRepository.delete(cntry);
			}
			PersonControllerTests.countries.clear();	
			dbInit = false;
			token = null;
			logger.info("DBtearDown ok");
		}
	}

	@Test //@WithMockUser(username = "Test1234")
	public void personServiceNotFound() throws Exception { 
		logger.info("personServiceNotFound");
		mockMvc.perform(get("/jobster/users/persons/readallpersons/")
			.header("Authorization", "Bearer " + token))
			//.andDo(MockMvcResultHandlers.print())
			.andExpect(status().isBadRequest()); 
	}

	@Test //@WithMockUser(username = "Test1234")
	public void personServiceFound() throws Exception { 
		logger.info("personServiceFound");
		mockMvc.perform(get("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token))
			.andExpect(status().isOk()); 
	}

	@Test //@WithMockUser(username = "Test1234")
	public void readAllPersons() throws Exception { 
		logger.info("readAllPersons");
		mockMvc.perform(get("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)) 
			.andExpect(status().isOk()) 
			.andExpect(content().contentType(contentType));
	}

	@Test //@WithMockUser(username = "Test1234")
	public void readAllPersonsNotFound() throws Exception { 
		logger.info("readAllPersonsNotFound");
		for (UserAccountEntity acc : PersonControllerTests.userAccounts) {
			userAccountRepository.delete(acc);
		}
		PersonControllerTests.userAccounts.clear();
		for (PersonEntity prsn : PersonControllerTests.persons) {
			personRepository.delete(prsn);
		}
		PersonControllerTests.persons.clear();
		mockMvc.perform(get("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token))
			.andExpect(status().isNotFound()); 
	}

	@Test //@WithMockUser(username = "Test1234")
	public void readSinglePerson() throws Exception { 
		logger.info("readSinglePerson");
		Integer id = PersonControllerTests.persons.get(1).getId();
		mockMvc.perform(get("/jobster/users/persons/" + id)
			.header("Authorization", "Bearer " + token))
			//.andDo(MockMvcResultHandlers.print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(id))); 
	}

	@Test //@WithMockUser(username = "Test1234")
	public void readSinglePersonWhichNotExists() throws Exception { 
		logger.info("readSinglePersonWhichNotExists");
		mockMvc.perform(get("/jobster/users/persons/" + (PersonControllerTests.persons.get(4).getId()+1))
			.header("Authorization", "Bearer " + token))
			.andExpect(status().isNotFound()); 
	}

	@Test //@WithMockUser(username = "Test1234")
	public void readAllDeletedPersons() throws Exception { 
		logger.info("readAllDeletedPersons");
		mockMvc.perform(get("/jobster/users/persons/deleted/")
			.header("Authorization", "Bearer " + token)) 
			.andExpect(status().isOk()) 
			.andExpect(content().contentType(contentType));
	}

	@Test //@WithMockUser(username = "Test1234")
	public void readAllDeletedPersonsNotFound() throws Exception { 
		logger.info("readAllDeletedPersonsNotFound");
		for (UserAccountEntity acc : PersonControllerTests.userAccounts) {
			userAccountRepository.delete(acc);
		}
		PersonControllerTests.userAccounts.clear();
		for (PersonEntity prsn : PersonControllerTests.persons) {
			personRepository.delete(prsn);
		}
		PersonControllerTests.persons.clear();		
		mockMvc.perform(get("/jobster/users/persons/deleted/")
			.header("Authorization", "Bearer " + token))
			.andExpect(status().isNotFound()); 
	}

	@Test //@WithMockUser(username = "Test1234")
	public void readSingleDeletedPerson() throws Exception { 
		logger.info("readSingleDeletedPerson");
		Integer id = PersonControllerTests.persons.get(3).getId();
		mockMvc.perform(get("/jobster/users/persons/deleted/" + id)
			.header("Authorization", "Bearer " + token))
			//.andDo(MockMvcResultHandlers.print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(id))); 
	}

	@Test //@WithMockUser(username = "Test1234")
	public void readSingleDeletedPersonWhichNotExists() throws Exception { 
		logger.info("readSingleDeletedPersonWhichNotExists");
		mockMvc.perform(get("/jobster/users/persons/deleted/" + PersonControllerTests.persons.get(1).getId())
			.header("Authorization", "Bearer " + token))
			.andExpect(status().isNotFound()); 
	}

	@Test //@WithMockUser(username = "Test1234")
	public void readAllArchivedPersons() throws Exception { 
		logger.info("readAllArchivedPersons");
		mockMvc.perform(get("/jobster/users/persons/archived/")
			.header("Authorization", "Bearer " + token)) 
			.andExpect(status().isOk()) 
			.andExpect(content().contentType(contentType));
	}

	@Test //@WithMockUser(username = "Test1234")
	public void readAllArchivedPersonsNotFound() throws Exception { 
		logger.info("readAllArchivedPersonsNotFound");
		for (UserAccountEntity acc : PersonControllerTests.userAccounts) {
			userAccountRepository.delete(acc);
		}
		PersonControllerTests.userAccounts.clear();
		for (PersonEntity cmpny : PersonControllerTests.persons) {
			personRepository.delete(cmpny);
		}
		PersonControllerTests.persons.clear();
		mockMvc.perform(get("/jobster/users/persons/archived/")
			.header("Authorization", "Bearer " + token))
			.andExpect(status().isNotFound()); 
	}

	@Test //@WithMockUser(username = "Test1234")
	public void readSingleArchivedPerson() throws Exception { 
		logger.info("readSingleArchivedPerson");
		Integer id = PersonControllerTests.persons.get(4).getId();
		mockMvc.perform(get("/jobster/users/persons/archived/" + id)
			.header("Authorization", "Bearer " + token))
			//.andDo(MockMvcResultHandlers.print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(id))); 
	}

	@Test //@WithMockUser(username = "Test1234")
	public void readSingleArchivedPersonWhichNotExists() throws Exception { 
		logger.info("readSingleArchivedPersonWhichNotExists");
		mockMvc.perform(get("/jobster/users/persons/archived/" + (PersonControllerTests.persons.get(4).getId()+1))
			.header("Authorization", "Bearer " + token))
			.andExpect(status().isNotFound()); 
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPerson() throws Exception {
		logger.info("createPerson");
		PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
		Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		//.andDo(MockMvcResultHandlers.print())
    		.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.user.email", is("Jobsteries@mail.com")));
		PersonControllerTests.persons.add(personRepository.getByEmailAndStatusLike(personDTO.getEmail(), 1));
		userAccountRepository.delete(userAccountRepository.getByUser(personRepository.getByEmailAndStatusLike(personDTO.getEmail(), 1)));
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonWithoutCountryRegion() throws Exception {
		logger.info("createPersonWithoutCountryRegion");
		PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setCity("City without");
    	personDTO.setCountry("World Union");
//    	personDTO.setCountryRegion(null);
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	
    	mockMvc.perform(post("/jobster/users/persons/")
    		.contentType(MediaType.APPLICATION_JSON).content(json)
			.header("Authorization", "Bearer " + token))
//    		.andDo(MockMvcResultHandlers.print())
    		.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.user.email", is("Jobsteries@mail.com")));
		PersonControllerTests.persons.add(personRepository.getByEmailAndStatusLike(personDTO.getEmail(), 1));
		userAccountRepository.delete(userAccountRepository.getByUser(personRepository.getByEmailAndStatusLike(personDTO.getEmail(), 1)));
//    	PersonControllerTests.countryRegions.add(countryRegionRepository.getByCountryRegionName(""));	
    	PersonControllerTests.cities.add(cityRepository.getByCityName("City without"));	
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonMarginalValueFirstName() throws Exception {
		logger.info("createPersonMarginalValueFirstName");
		PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jo");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		//.andDo(MockMvcResultHandlers.print())
    		.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.user.firstName", is("Jo")));
		PersonControllerTests.persons.add(personRepository.getByEmailAndStatusLike(personDTO.getEmail(), 1));
		userAccountRepository.delete(userAccountRepository.getByUser(personRepository.getByEmailAndStatusLike(personDTO.getEmail(), 1)));
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonMarginalValueLastName() throws Exception {
		logger.info("createPersonMarginalValueLastName");
		PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jo");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		//.andDo(MockMvcResultHandlers.print())
    		.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.user.lastName", is("Jo")));
		PersonControllerTests.persons.add(personRepository.getByEmailAndStatusLike(personDTO.getEmail(), 1));
		userAccountRepository.delete(userAccountRepository.getByUser(personRepository.getByEmailAndStatusLike(personDTO.getEmail(), 1)));
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonMarginalValueEmail() throws Exception {
		logger.info("createPersonMarginalValueEmail");
		PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("JobsteriesJobsteriesJobsteriesJobsteriese@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		//.andDo(MockMvcResultHandlers.print())
    		.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.user.email", is("JobsteriesJobsteriesJobsteriesJobsteriese@mail.com")));
		PersonControllerTests.persons.add(personRepository.getByEmailAndStatusLike(personDTO.getEmail(), 1));
		userAccountRepository.delete(userAccountRepository.getByUser(personRepository.getByEmailAndStatusLike(personDTO.getEmail(), 1)));
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonMarginalValueAbout() throws Exception {
		logger.info("createPersonMarginalValueAbout");
		PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("AboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaABABABABABABABA");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		//.andDo(MockMvcResultHandlers.print())
    		.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.user.about", is("AboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaABABABABABABABA")));
		PersonControllerTests.persons.add(personRepository.getByEmailAndStatusLike(personDTO.getEmail(), 1));
		userAccountRepository.delete(userAccountRepository.getByUser(personRepository.getByEmailAndStatusLike(personDTO.getEmail(), 1)));
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonMarginalValueCity() throws Exception {
		logger.info("createpersonMarginalValueCity");
		PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
		personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setCity("Wo");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
    		//.andDo(MockMvcResultHandlers.print())
    		.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.user.firstName", is("Jobsteries")));
    	person = personRepository.getByEmailAndStatusLike(personDTO.getEmail(), 1);
    	PersonControllerTests.persons.add(person);
    	PersonControllerTests.cities.add(cityRepository.getByCityName("Wo"));	
		PersonControllerTests.userAccounts.add(userAccountRepository.getByUser(personRepository.getByEmailAndStatusLike(personDTO.getEmail(), 1)));
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonMarginalValueCountryName() throws Exception {
		logger.info("createpersonMarginalValueCountryName");	
		PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
		personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setCity("World cityX");
    	personDTO.setCountry("Wo");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("YY");
    	personDTO.setCountryRegion("World regionX");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		//.andDo(MockMvcResultHandlers.print())
    		.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.user.firstName", is("Jobsteries")));
    	person = personRepository.getByEmailAndStatusLike(personDTO.getEmail(), 1);
    	PersonControllerTests.persons.add(person);
    	PersonControllerTests.countries.add(countryRepository.getByCountryName("Wo"));	
    	PersonControllerTests.cities.add(cityRepository.getByCityName("World cityX"));	
    	PersonControllerTests.countryRegions.add(countryRegionRepository.getByCountryRegionName("World regionX"));		
		PersonControllerTests.userAccounts.add(userAccountRepository.getByUser(personRepository.getByEmailAndStatusLike(personDTO.getEmail(), 1)));
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonMarginalValueIso2CodeMin() throws Exception {
		logger.info("createpersonMarginalValueIso2CodeMin");
		PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
		personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setCity("World cityX");
    	personDTO.setCountry("World UnionX");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XY");
    	personDTO.setCountryRegion("World regionX");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
   		mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		//.andDo(MockMvcResultHandlers.print())
    		.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.user.firstName", is("Jobsteries")));
    	person = personRepository.getByEmailAndStatusLike(personDTO.getEmail(), 1);
    	PersonControllerTests.persons.add(person);
    	PersonControllerTests.countries.add(countryRepository.getByCountryName("World UnionX"));	
    	PersonControllerTests.cities.add(cityRepository.getByCityName("World cityX"));	
    	PersonControllerTests.countryRegions.add(countryRegionRepository.getByCountryRegionName("World regionX"));		
		PersonControllerTests.userAccounts.add(userAccountRepository.getByUser(personRepository.getByEmailAndStatusLike(personDTO.getEmail(), 1)));
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonMarginalValueCountryRegionName() throws Exception {
		logger.info("createpersonMarginalValueCountryRegionName");
		PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
		personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setCity("World cityX");
    	personDTO.setCountry("World UnionX");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("YY");
    	personDTO.setCountryRegion("Wo");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		//.andDo(MockMvcResultHandlers.print())
    		.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.user.firstName", is("Jobsteries")));
    	person = personRepository.getByEmailAndStatusLike(personDTO.getEmail(), 1);
    	PersonControllerTests.persons.add(person);
    	PersonControllerTests.countries.add(countryRepository.getByCountryName("World UnionX"));	
    	PersonControllerTests.cities.add(cityRepository.getByCityName("World cityX"));	
    	PersonControllerTests.countryRegions.add(countryRegionRepository.getByCountryRegionName("Wo"));		
		PersonControllerTests.userAccounts.add(userAccountRepository.getByUser(personRepository.getByEmailAndStatusLike(personDTO.getEmail(), 1)));
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonMarginalValueLongitudeMin() throws Exception {
		logger.info("createpersonMarginalValueLongitudeMin");
		PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
		personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setCity("World cityX");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(-180.0);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		//.andDo(MockMvcResultHandlers.print())
    		.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.user.firstName", is("Jobsteries")));
    	person = personRepository.getByEmailAndStatusLike(personDTO.getEmail(), 1);
    	PersonControllerTests.persons.add(person);
    	PersonControllerTests.cities.add(cityRepository.getByCityName("World cityX"));	
		PersonControllerTests.userAccounts.add(userAccountRepository.getByUser(personRepository.getByEmailAndStatusLike(personDTO.getEmail(), 1)));
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonMarginalValueLongitudeMax() throws Exception {
		logger.info("createpersonMarginalValueLongitudeMax");
		PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
		personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setCity("World cityX");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(180.0);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		//.andDo(MockMvcResultHandlers.print())
    		.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.user.firstName", is("Jobsteries")));
    	person = personRepository.getByEmailAndStatusLike(personDTO.getEmail(), 1);
    	PersonControllerTests.persons.add(person);
    	PersonControllerTests.cities.add(cityRepository.getByCityName("World cityX"));	
		PersonControllerTests.userAccounts.add(userAccountRepository.getByUser(personRepository.getByEmailAndStatusLike(personDTO.getEmail(), 1)));
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonMarginalValueLatitudeMin() throws Exception {
		logger.info("createpersonMarginalValueLatitudeMin");
		PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
		personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setCity("World cityX");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(-90.0);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		//.andDo(MockMvcResultHandlers.print())
    		.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.user.firstName", is("Jobsteries")));
    	person = personRepository.getByEmailAndStatusLike(personDTO.getEmail(), 1);
    	PersonControllerTests.persons.add(person);
    	PersonControllerTests.cities.add(cityRepository.getByCityName("World cityX"));	
		PersonControllerTests.userAccounts.add(userAccountRepository.getByUser(personRepository.getByEmailAndStatusLike(personDTO.getEmail(), 1)));
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonMarginalValueLatitudeMax() throws Exception {
		logger.info("createpersonMarginalValueLatitudeMax");
		PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
		personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setCity("World cityX");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(90.0);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		//.andDo(MockMvcResultHandlers.print())
    		.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.user.firstName", is("Jobsteries")));
    	person = personRepository.getByEmailAndStatusLike(personDTO.getEmail(), 1);
    	PersonControllerTests.persons.add(person);
    	PersonControllerTests.cities.add(cityRepository.getByCityName("World cityX"));	
		PersonControllerTests.userAccounts.add(userAccountRepository.getByUser(personRepository.getByEmailAndStatusLike(personDTO.getEmail(), 1)));
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonMarginalValueUsernameMin() throws Exception {
		logger.info("createPersonMarginalValueUsernameMin");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test5");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
    		//.andDo(MockMvcResultHandlers.print())
    		.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.username", is("Test5")));
		PersonControllerTests.persons.add(personRepository.getByEmailAndStatusLike(personDTO.getEmail(), 1));
		userAccountRepository.delete(userAccountRepository.getByUser(personRepository.getByEmailAndStatusLike(personDTO.getEmail(), 1)));
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonMarginalValueUsernameMax() throws Exception {
		logger.info("createPersonMarginalValueUsernameMax");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test1234567891234567");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		//.andDo(MockMvcResultHandlers.print())
    		.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.username", is("Test1234567891234567")));
		PersonControllerTests.persons.add(personRepository.getByEmailAndStatusLike(personDTO.getEmail(), 1));
		userAccountRepository.delete(userAccountRepository.getByUser(personRepository.getByEmailAndStatusLike(personDTO.getEmail(), 1)));
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonMarginalValuePasswordAndConfirmedPasswordMin() throws Exception {
		logger.info("createPersonMarginalValuePasswordAndConfirmedPasswordMin");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test12");
    	personDTO.setPassword("Test5");
    	personDTO.setConfirmedPassword("Test5");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		//.andDo(MockMvcResultHandlers.print())
    		.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.username", is("Test12")));
		PersonControllerTests.persons.add(personRepository.getByEmailAndStatusLike(personDTO.getEmail(), 1));
		userAccountRepository.delete(userAccountRepository.getByUser(personRepository.getByEmailAndStatusLike(personDTO.getEmail(), 1)));
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonValidationErrorPersonFirstNameOneChar() throws Exception {
		logger.info("createPersonValidationErrorPersonFirstNameOneChar");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
        personDTO.setFirstName("J");
        personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonValidationErrorpersonLastNameOneChar() throws Exception {
		logger.info("createPersonValidationErrorpersonLastNameOneChar");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
        personDTO.setFirstName("Jobsteries");
        personDTO.setLastName("J");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonValidationErrorPersonFirstNameContainsNotOnlyLetters() throws Exception {
		logger.info("createPersonValidationErrorPersonFirstNameContainsNotOnlyLetters");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("J&o#3!:d");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonValidationErrorPersonLastNameContainsNotOnlyLetters() throws Exception {
		logger.info("createPersonValidationErrorPersonLastNameContainsNotOnlyLetters");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("J&o#3!:d");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonValidationErrorBirthDateNotFormatDDmmYYYY() throws Exception {
		logger.info("createPersonValidationErrorBirthDateNotFormatDDmmYYYY");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("1985.22.10");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonValidationErrorEmailWrongRegex() throws Exception {
		logger.info("createPersonValidationErrorEmailWrongRegex");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonValidationErrorPhoneRegex() throws Exception {
		logger.info("createPersonValidationErrorPhoneRegex");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("064gf56789--01");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonValidationErrorEmailToLong() throws Exception {
		logger.info("createPersonValidationErrorEmailToLong");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("JobsteriesJobsteriesJobsteriesJobsteriesJobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void createPersonValidationErrorCityNameMin() throws Exception {
		logger.info("createPersonValidationErrorCityNameMin");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
   		personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setCity("");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonValidationErrorCityNameRegex() throws Exception {
		logger.info("createPersonValidationErrorCityNameRegex");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setCity("World city1");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonValidationErrorCountryNameMin() throws Exception {
		logger.info("createPersonValidationErrorCountryNameMin");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setCity("World city");
    	personDTO.setCountry("");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonValidationErrorCountryNameRegex() throws Exception {
		logger.info("createPersonValidationErrorCountryNameRegex");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union1");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonValidationErrorIso2CodeMin() throws Exception {
		logger.info("createPersonValidationErrorIso2CodeMin");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("X");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonValidationErrorIso2CodeMax() throws Exception {
		logger.info("createPersonValidationErrorIso2CodeMax");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XXX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonValidationErrorIso2CodeRegex() throws Exception {
		logger.info("createPersonValidationErrorIso2CodeRegex");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("X1");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonValidationErrorCountryRegionRegex() throws Exception {
		logger.info("createPersonValidationErrorCountryRegionRegex");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region1");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonValidationErrorLongitudeToSmall() throws Exception {
		logger.info("createPersonValidationErrorLongitudeToSmall");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region1");
    	personDTO.setLongitude(-181.0);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonValidationErrorLongitudeToLarge() throws Exception {
		logger.info("createPersonValidationErrorLongitudeToLarge");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region1");
    	personDTO.setLongitude(181.0);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonValidationErrorLatitudeToSmall() throws Exception {
		logger.info("createPersonValidationErrorLatitudeToSmall");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region1");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(-91.0);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonValidationErrorLatitudeToLarge() throws Exception {
		logger.info("createPersonValidationErrorLatitudeToLarge");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region1");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(91.0);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonValidationErrorAboutToLarge() throws Exception {
		logger.info("createPersonValidationErrorAboutToLarge");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries, About Jobsteries, About Jobsteries, About Jobsteries, About Jobsteries, About Jobsteries, About Jobsteries, About Jobsteries, About Jobsteries, About Jobsteries, About Jobsteries, About Jobsteries, About Jobsteries, About Jobsteries, About Jobsteries, About Jobsteries.");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region1");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonValidationErrorUsernameToSmall() throws Exception {
		logger.info("createPersonValidationErrorUsernameToSmall");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region1");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonValidationErrorUsernameToLarge() throws Exception {
		logger.info("createPersonValidationErrorUsernameToLarge");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region1");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("TestTestTestTestTestTest");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonValidationErrorRoleWrong() throws Exception {
		logger.info("createPersonValidationErrorRoleWrong");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("AAAA");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonValidationErrorGenderWrong() throws Exception {
		logger.info("createPersonValidationErrorGenderWrong");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setGender("AAAA");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void createPersonValidationErrorPasswordAndConfirmedPasswordToSmall() throws Exception {
		logger.info("createPersonValidationErrorPasswordAndConfirmedPasswordToSmall");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region1");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test");
    	personDTO.setConfirmedPassword("Test");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonValidationErrorPasswordAndConfirmedPasswordContainUnsupportedChars() throws Exception {
		logger.info("createPersonValidationErrorPasswordAndConfirmedPasswordContainUnsupportedChars");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region1");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Te+st!12&3");
    	personDTO.setConfirmedPassword("Te+st!12&3");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createNullPerson() throws Exception {
		logger.info("createNullPerson");
    	PersonDTO personDTO = new PersonDTO();
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonNullEmail() throws Exception {
		logger.info("createPersonNullEmail");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail(null);
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonBlankEmail() throws Exception {
		logger.info("createPersonBlankEmail");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonSpaceEmail() throws Exception {
		logger.info("createPersonSpaceEmail");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail(" ");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonNullPhone() throws Exception {
		logger.info("createPersonNullPhone");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone(null);
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonBlankPhone() throws Exception {
		logger.info("createPersonBlankPhone");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonSpacePhone() throws Exception {
		logger.info("createPersonSpacePhone");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone(" ");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonNullFirstName() throws Exception {
		logger.info("createPersonNullFirstName");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName(null);
    	personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonBlankFirstName() throws Exception {
		logger.info("createPersonBlankFirstName");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonSpaceFirstName() throws Exception {
		logger.info("createPersonSpaceFirstName");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName(" ");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonNullLastName() throws Exception {
		logger.info("createPersonNullLastName");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName(null);
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonBlankLastName() throws Exception {
		logger.info("createPersonBlankLastName");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonSpaceLastName() throws Exception {
		logger.info("createPersonSpaceLastName");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName(" ");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonNullBirthDate() throws Exception {
		logger.info("createPersonNullBirthDate");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonBlankBirthDate() throws Exception {
		logger.info("createPersonBlankBirthDate");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonSpaceBirthDate() throws Exception {
		logger.info("createPersonSpaceBirthDate");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate(" ");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonNullCity() throws Exception {
		logger.info("createPersonNullCity");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setCity(null);
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonBlankCity() throws Exception {
		logger.info("createPersonBlankCity");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setCity("");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonSpaceCity() throws Exception {
		logger.info("createPersonSpaceCity");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setCity(" ");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonNullLongitude() throws Exception {
		logger.info("createPersonNullLongitude");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(null);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonNullLatitude() throws Exception {
		logger.info("createPersonNullLatitude");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(null);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonNullCountry() throws Exception {
		logger.info("createPersonNullCountry");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setCity("World city");
    	personDTO.setCountry(null);
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonBlankCountry() throws Exception {
		logger.info("createPersonBlankCountry");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setCity("World city");
    	personDTO.setCountry("");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonSpaceCountry() throws Exception {
		logger.info("createPersonSpaceCountry");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setCity("World city");
    	personDTO.setCountry(" ");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonNullRole() throws Exception {
		logger.info("createNullPerson");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole(null);
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonBlankRole() throws Exception {
		logger.info("createPersonBlankRole");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonSpaceRole() throws Exception {
		logger.info("createPersonSpaceRole");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole(" ");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonNullGender() throws Exception {
		logger.info("createPersonNullGender");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setGender(null);
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonBlankGender() throws Exception {
		logger.info("createPersonBlankGender");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setGender("");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonSpaceGender() throws Exception {
		logger.info("createPersonSpaceGender");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setGender(" ");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void createPersonNullIso2Code() throws Exception {
		logger.info("createPersonNullIso2Code");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code(null);
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonBlankIso2Code() throws Exception {
		logger.info("createPersonBlankIso2Code");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonSpaceIso2Code() throws Exception {
		logger.info("createPersonSpaceIso2Code");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code(" ");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonNullUsername() throws Exception {
		logger.info("createPersonNullUsername");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername(null);
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonBlankUsername() throws Exception {
		logger.info("createPersonBlankUsername");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonSpaceUsername() throws Exception {
		logger.info("createPersonSpaceUsername");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername(" ");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonNullPassword() throws Exception {
		logger.info("createPersonNullPassword");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword(null);
    	personDTO.setConfirmedPassword("Test123");  
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonBlankPassword() throws Exception {
		logger.info("createPersonBlankPassword");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
     	personDTO.setFirstName("Jobsteries");
     	personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonSpacePassword() throws Exception {
		logger.info("createPersonSpacePassword");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword(" ");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonNullConfirmedPassword() throws Exception {
		logger.info("createPersonNullConfirmedPassword");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword(null);
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonBlankConfirmedPassword() throws Exception {
		logger.info("createPersonBlankConfirmedPassword");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonSpaceConfirmedPassword() throws Exception {
		logger.info("createPersonSpaceConfirmedPassword");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword(" ");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonMobilePhoneAlreadyExists() throws Exception {
		logger.info("createPersonMobilePhoneAlreadyExists");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0642345678");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isNotAcceptable());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonEmailAlreadyExists() throws Exception {
		logger.info("createPersonEmailAlreadyExists");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobster@mail.com");
    	personDTO.setAbout("About Jobsteries");
     	personDTO.setFirstName("Jobsteries");
     	personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isNotAcceptable());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonWrongAccessRole() throws Exception {
		logger.info("createPersonWrongAccessRole");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_ADMIN");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isNotAcceptable());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void createPersonWrongGender() throws Exception {
		logger.info("createPersonWrongGender");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setGender("GENDER_CHILD");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test123");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void createPersonUsernameAlreadyExists() throws Exception {
		logger.info("createPersonUsernameAlreadyExists");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("0645678901");
    	personDTO.setEmail("Jobsteries@mail.com");
    	personDTO.setAbout("About Jobsteries");
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setGender("GENDER_MALE");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	personDTO.setUsername("Test1234");
    	personDTO.setPassword("Test123");
    	personDTO.setConfirmedPassword("Test123");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(post("/jobster/users/persons/")
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isNotAcceptable());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonWhichNotExists() throws Exception {
		logger.info("updatePersonWhichNotExists");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setFirstName("Jobsteries");
    	personDTO.setLastName("Jobsteries");
    	personDTO.setBirthDate("22-10-1985");
    	personDTO.setEmail("Jobsty@mail.com");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
		mockMvc.perform(put("/jobster/users/persons/" + (PersonControllerTests.persons.get(4).getId()+1))
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
    		//.andDo(MockMvcResultHandlers.print())
    		.andExpect(status().isNotFound());
	}	

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonFirstName() throws Exception {
		logger.info("updatePersonFirstName");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setFirstName("Jobsty");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
		mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
			//.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
			.andExpect(content().contentType(contentType))
			.andExpect(jsonPath("$.firstName", is("Jobsty")));
		Integer Id = PersonControllerTests.persons.get(0).getId();
		PersonControllerTests.persons.remove(0);
		PersonControllerTests.persons.add(personRepository.getByIdAndStatusLike(Id, 1));
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonFirstNameMarginalValue() throws Exception {
		logger.info("updatePersonFirstNameMarginalValue");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setFirstName("Jo");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
		mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
			//.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.firstName", is("Jo")));
		Integer Id = PersonControllerTests.persons.get(0).getId();
		PersonControllerTests.persons.remove(0);
		PersonControllerTests.persons.add(personRepository.getByIdAndStatusLike(Id, 1));
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonLastName() throws Exception {
		logger.info("updatePersonLastName");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setLastName("Jobsty");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
		mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
			//.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
			.andExpect(content().contentType(contentType))
			.andExpect(jsonPath("$.lastName", is("Jobsty")));
		Integer Id = PersonControllerTests.persons.get(0).getId();
		PersonControllerTests.persons.remove(0);
		PersonControllerTests.persons.add(personRepository.getByIdAndStatusLike(Id, 1));
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonLastNameMarginalValue() throws Exception {
		logger.info("updatePersonLastNameMarginalValue");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setLastName("Jo");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
		mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
			//.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.lastName", is("Jo")));
		Integer Id = PersonControllerTests.persons.get(0).getId();
		PersonControllerTests.persons.remove(0);
		PersonControllerTests.persons.add(personRepository.getByIdAndStatusLike(Id, 1));
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonEmail() throws Exception {
		logger.info("updatePersonEmail");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setEmail("Jobsty@mail.com");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
		mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
			//.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
        	.andExpect(content().contentType(contentType)) 
        	.andExpect(jsonPath("$.email", is("Jobsty@mail.com")));
		Integer Id = PersonControllerTests.persons.get(0).getId();
		PersonControllerTests.persons.remove(0);
		PersonControllerTests.persons.add(personRepository.getByIdAndStatusLike(Id, 1));
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonEmailMarginalValue() throws Exception {	
		logger.info("updatePersonEmailMarginalValue");
   		PersonDTO personDTO = new PersonDTO();
    	personDTO.setEmail("JobsteriesJobsteriesJobsteriesJobsteriese@mail.com");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
		mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
			//.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
        	.andExpect(content().contentType(contentType)) 
        	.andExpect(jsonPath("$.email", is("JobsteriesJobsteriesJobsteriesJobsteriese@mail.com")));
		Integer Id = PersonControllerTests.persons.get(0).getId();
		PersonControllerTests.persons.remove(0);
		PersonControllerTests.persons.add(personRepository.getByIdAndStatusLike(Id, 1));
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonAboutMarginalValue() throws Exception {
		logger.info("updatePersonAboutMarginalValue");
		PersonDTO personDTO = new PersonDTO();
    	personDTO.setAbout("AboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaABABABABABABABA");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
		mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
			//.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
        	.andExpect(content().contentType(contentType)) 
        	.andExpect(jsonPath("$.about", is("AboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaABABABABABABABA")));
		Integer Id = PersonControllerTests.persons.get(0).getId();
		PersonControllerTests.persons.remove(0);
		PersonControllerTests.persons.add(personRepository.getByIdAndStatusLike(Id, 1));
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonCityMarginalValue() throws Exception {
		logger.info("updatepersonCityMarginalValue");	
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setCity("Wo");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			//.andDo(MockMvcResultHandlers.print())
			.andExpect(jsonPath("$.id", is(persons.get(0).getId())));
		Integer Id = PersonControllerTests.persons.get(0).getId();
		PersonControllerTests.persons.remove(0);
		person = personRepository.getByIdAndStatusLike(Id, 1);
    	PersonControllerTests.persons.add(person);
    	PersonControllerTests.cities.add(cityRepository.getByCityName("Wo"));	
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonCountryMarginalValue() throws Exception {
		logger.info("updatepersonCountryMarginalValue");
		PersonDTO personDTO = new PersonDTO();
    	personDTO.setCity("World cityX");
    	personDTO.setCountry("Wo");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("YY");
    	personDTO.setCountryRegion("World regionX");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			//.andDo(MockMvcResultHandlers.print())
			.andExpect(jsonPath("$.id", is(persons.get(0).getId())));
		Integer Id = PersonControllerTests.persons.get(0).getId();
		PersonControllerTests.persons.remove(0);
		person = personRepository.getByIdAndStatusLike(Id, 1);
    	PersonControllerTests.persons.add(person);
    	PersonControllerTests.countries.add(countryRepository.getByCountryName("Wo"));	
    	PersonControllerTests.cities.add(cityRepository.getByCityName("World cityX"));	
    	PersonControllerTests.countryRegions.add(countryRegionRepository.getByCountryRegionName("World regionX"));		
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonIso2CodeMarginalValue() throws Exception {
		logger.info("updatepersonIso2CodeMarginalValue");
		PersonDTO personDTO = new PersonDTO();
    	personDTO.setCity("World cityX");
    	personDTO.setCountry("World UnionX");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XY");
    	personDTO.setCountryRegion("World regionX");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			//.andDo(MockMvcResultHandlers.print())
			.andExpect(jsonPath("$.id", is(persons.get(0).getId())));
		Integer Id = PersonControllerTests.persons.get(0).getId();
		PersonControllerTests.persons.remove(0);
		person = personRepository.getByIdAndStatusLike(Id, 1);
    	PersonControllerTests.persons.add(person);
    	PersonControllerTests.countries.add(countryRepository.getByCountryName("World UnionX"));	
    	PersonControllerTests.cities.add(cityRepository.getByCityName("World cityX"));	
    	PersonControllerTests.countryRegions.add(countryRegionRepository.getByCountryRegionName("World regionX"));		
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonLongitudeMinMarginalValue() throws Exception {
		logger.info("updatepersonLongitudeMinMarginalValue");
		PersonDTO personDTO = new PersonDTO();
    	personDTO.setCity("World cityX");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(-180.0);
    	personDTO.setLatitude(43.1);
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			//.andDo(MockMvcResultHandlers.print())
			.andExpect(jsonPath("$.id", is(persons.get(0).getId())));
		Integer Id = PersonControllerTests.persons.get(0).getId();
		PersonControllerTests.persons.remove(0);
		person = personRepository.getByIdAndStatusLike(Id, 1);
    	PersonControllerTests.persons.add(person);
    	PersonControllerTests.cities.add(cityRepository.getByCityName("World cityX"));	
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonLongitudeMaxMarginalValue() throws Exception {
		logger.info("updatepersonLongitudeMaxMarginalValue");
		PersonDTO personDTO = new PersonDTO();
    	personDTO.setCity("World cityX");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(180.0);
    	personDTO.setLatitude(43.1);
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			//.andDo(MockMvcResultHandlers.print())
			.andExpect(jsonPath("$.id", is(persons.get(0).getId())));
		Integer Id = PersonControllerTests.persons.get(0).getId();
		PersonControllerTests.persons.remove(0);
		person = personRepository.getByIdAndStatusLike(Id, 1);
    	PersonControllerTests.persons.add(person);
    	PersonControllerTests.cities.add(cityRepository.getByCityName("World cityX"));	
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonLatitudeMinMarginalValue() throws Exception {
		logger.info("updatepersonLatitudeMinMarginalValue");
		PersonDTO personDTO = new PersonDTO();
    	personDTO.setCity("World cityX");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(-90.0);
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			//.andDo(MockMvcResultHandlers.print())
			.andExpect(jsonPath("$.id", is(persons.get(0).getId())));
		Integer Id = PersonControllerTests.persons.get(0).getId();
		PersonControllerTests.persons.remove(0);
		person = personRepository.getByIdAndStatusLike(Id, 1);
    	PersonControllerTests.persons.add(person);
    	PersonControllerTests.cities.add(cityRepository.getByCityName("World cityX"));	
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonLatitudeMaxMarginalValue() throws Exception {
		logger.info("updatepersonLatitudeMaxMarginalValue");
		PersonDTO personDTO = new PersonDTO();
    	personDTO.setCity("World cityX");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(90.0);
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			//.andDo(MockMvcResultHandlers.print())
			.andExpect(jsonPath("$.id", is(persons.get(0).getId())));
		Integer Id = PersonControllerTests.persons.get(0).getId();
		PersonControllerTests.persons.remove(0);
		person = personRepository.getByIdAndStatusLike(Id, 1);
    	PersonControllerTests.persons.add(person);
    	PersonControllerTests.cities.add(cityRepository.getByCityName("World cityX"));	
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonUsernameMinMarginalValue() throws Exception {	
		logger.info("updatePersonUsernameMinMarginalValue");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setUsername("Test5");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
		mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
    		//.andDo(MockMvcResultHandlers.print())
    		.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.id", is(PersonControllerTests.persons.get(0).getId())));
		PersonControllerTests.userAccounts.remove(0);
		PersonControllerTests.userAccounts.add(userAccountRepository.findByUserAndStatusLike(PersonControllerTests.persons.get(0), 1));
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonUsernameMaxMarginalValue() throws Exception {
		logger.info("updatePersonUsernameMaxMarginalValue");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setUsername("Test1234567891234567");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
		mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
    		//.andDo(MockMvcResultHandlers.print())
    		.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.id", is(PersonControllerTests.persons.get(0).getId())));
		PersonControllerTests.userAccounts.remove(0);
		PersonControllerTests.userAccounts.add(userAccountRepository.findByUserAndStatusLike(PersonControllerTests.persons.get(0), 1));
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonPasswordAndConfirmedPasswordMinMarginalValue() throws Exception {
		logger.info("updatePersonPasswordAndConfirmedPasswordMinMarginalValue");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setPassword("Test5");
    	personDTO.setConfirmedPassword("Test5");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
    		//.andDo(MockMvcResultHandlers.print())
    		.andExpect(status().isOk())
    		.andExpect(content().contentType(contentType)) 
    		.andExpect(jsonPath("$.id", is(PersonControllerTests.persons.get(0).getId())));
		PersonControllerTests.userAccounts.remove(0);
		PersonControllerTests.userAccounts.add(userAccountRepository.findByUserAndStatusLike(PersonControllerTests.persons.get(0), 1));
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonValidationErrorFirstNameOneChar() throws Exception {
		logger.info("updatePersonValidationErrorFirstNameOneChar");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setFirstName("J");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonValidationErrorFirstNameContainsNotOnlyLetters() throws Exception {
		logger.info("updatePersonValidationErrorFirstNameContainsNotOnlyLetters");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setFirstName("J&o#3!:d");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonValidationErrorLastNameOneChar() throws Exception {
		logger.info("updatePersonValidationErrorLastNameOneChar");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setLastName("J");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonValidationErrorLastNameContainsNotOnlyLetters() throws Exception {
		logger.info("updatePersonValidationErrorLastNameContainsNotOnlyLetters");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setLastName("J&o#3!:d");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonValidationErrorBirthDateNotFormatDDMMYYYY() throws Exception {
		logger.info("updatePersonValidationErrorBirthDateNotFormatDDMMYYYY");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setBirthDate("1985.10.22");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonValidationErrorEmailWrongRegex() throws Exception {
		logger.info("updatePersonValidationErrorEmailWrongRegex");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setEmail("Jobsty@mail");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonValidationErrorPhoneWrongRegex() throws Exception {
		logger.info("updatePersonValidationErrorPhoneWrongRegex");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setMobilePhone("064gf56789--01");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonValidationErrorEmailToLong() throws Exception {
		logger.info("updatePersonValidationErrorEmailToLong");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setEmail("JobsteriesJobsteriesJobsteriesJobsteriesJobsteries@mail.com");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonValidationErrorAboutToLong() throws Exception {
		logger.info("updatePersonValidationErrorAboutToLong");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setAbout("About Jobsteries....About Jobsteries....About Jobsteries....About Jobsteries....About Jobsteries....About Jobsteries....About Jobsteries....About Jobsteries....About Jobsteries....About Jobsteries....About Jobsteries....About Jobsteries....About Jobsteries");
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonValidationErrorCityNameMin() throws Exception {
		logger.info("updatePersonValidationErrorCityNameMin");
    	PersonDTO personDTO = new PersonDTO();
    	personDTO.setCity("");
    	personDTO.setCountry("World Union");
    	personDTO.setAccessRole("ROLE_USER");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
    	Gson gson = new Gson();
    	String json = gson.toJson(personDTO);
    	mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
    		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonValidationErrorCityNameRegex() throws Exception {
		logger.info("updatePersonValidationErrorCityNameRegex");
        PersonDTO personDTO = new PersonDTO();
        personDTO.setCity("World city1");
        personDTO.setCountry("World Union");
        personDTO.setAccessRole("ROLE_USER");
        personDTO.setIso2Code("XX");
        personDTO.setCountryRegion("World region");
        personDTO.setLongitude(43.1);
        personDTO.setLatitude(43.1);
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonValidationErrorCountryNameMin() throws Exception {
		logger.info("updatePersonValidationErrorCountryNameMin");
        PersonDTO personDTO = new PersonDTO();
        personDTO.setCity("World city");
        personDTO.setCountry("");
        personDTO.setAccessRole("ROLE_USER");
        personDTO.setIso2Code("XX");
        personDTO.setCountryRegion("World region");
        personDTO.setLongitude(43.1);
        personDTO.setLatitude(43.1);
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonValidationErrorCountryNameRegex() throws Exception {
		logger.info("updatePersonValidationErrorCountryNameRegex");
        PersonDTO personDTO = new PersonDTO();
        personDTO.setCity("World city");
        personDTO.setCountry("World Union1");
        personDTO.setAccessRole("ROLE_USER");
        personDTO.setIso2Code("XX");
        personDTO.setCountryRegion("World region");
        personDTO.setLongitude(43.1);
        personDTO.setLatitude(43.1);
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonValidationErrorIso2CodeMin() throws Exception {
		logger.info("updatePersonValidationErrorIso2CodeMin");
        PersonDTO personDTO = new PersonDTO();
        personDTO.setCity("World city");
        personDTO.setCountry("World Union");
        personDTO.setAccessRole("ROLE_USER");
        personDTO.setIso2Code("X");
        personDTO.setCountryRegion("World region");
        personDTO.setLongitude(43.1);
        personDTO.setLatitude(43.1);
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonValidationErrorIso2CodeMax() throws Exception {
		logger.info("updatePersonValidationErrorIso2CodeMax");
        PersonDTO personDTO = new PersonDTO();
        personDTO.setCity("World city");
        personDTO.setCountry("World Union");
        personDTO.setAccessRole("ROLE_USER");
        personDTO.setIso2Code("XXX");
        personDTO.setCountryRegion("World region");
        personDTO.setLongitude(43.1);
        personDTO.setLatitude(43.1);
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonValidationErrorIso2CodeRegex() throws Exception {
		logger.info("updatePersonValidationErrorIso2CodeRegex");
        PersonDTO personDTO = new PersonDTO();
        personDTO.setCity("World city");
        personDTO.setCountry("World Union");
        personDTO.setAccessRole("ROLE_USER");
        personDTO.setIso2Code("X1");
        personDTO.setCountryRegion("World region");
        personDTO.setLongitude(43.1);
        personDTO.setLatitude(43.1);
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonValidationErrorCountryRegionRegex() throws Exception {
		logger.info("updatePersonValidationErrorCountryRegionRegex");
        PersonDTO personDTO = new PersonDTO();
        personDTO.setCity("World city");
        personDTO.setCountry("World Union");
        personDTO.setAccessRole("ROLE_USER");
        personDTO.setIso2Code("XX");
        personDTO.setCountryRegion("World region1");
        personDTO.setLongitude(43.1);
        personDTO.setLatitude(43.1);
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonValidationErrorLongitudeToSmall() throws Exception {
		logger.info("updatePersonValidationErrorLongitudeToSmall");
        PersonDTO personDTO = new PersonDTO();
        personDTO.setCity("World city");
        personDTO.setCountry("World Union");
        personDTO.setAccessRole("ROLE_USER");
        personDTO.setIso2Code("XX");
        personDTO.setCountryRegion("World region");
        personDTO.setLongitude(-181.0);
        personDTO.setLatitude(43.1);
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonValidationErrorLongitudeToLarge() throws Exception {
		logger.info("updatePersonValidationErrorLongitudeToLarge");
        PersonDTO personDTO = new PersonDTO();
        personDTO.setCity("World city");
        personDTO.setCountry("World Union");
        personDTO.setAccessRole("ROLE_USER");
        personDTO.setIso2Code("XX");
        personDTO.setCountryRegion("World region");
        personDTO.setLongitude(181.0);
        personDTO.setLatitude(43.1);
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonValidationErrorLatitudeToSmall() throws Exception {
		logger.info("updatePersonValidationErrorLatitudeToSmall");
        PersonDTO personDTO = new PersonDTO();
        personDTO.setCity("World city");
        personDTO.setCountry("World Union");
        personDTO.setAccessRole("ROLE_USER");
        personDTO.setIso2Code("XX");
        personDTO.setCountryRegion("World region");
        personDTO.setLongitude(43.1);
        personDTO.setLatitude(-91.0);
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonValidationErrorLatitudeToLarge() throws Exception {
		logger.info("updatePersonValidationErrorLatitudeToLarge");
        PersonDTO personDTO = new PersonDTO();
        personDTO.setCity("World city");
        personDTO.setCountry("World Union");
        personDTO.setAccessRole("ROLE_USER");
        personDTO.setIso2Code("XX");
        personDTO.setCountryRegion("World region");
        personDTO.setLongitude(43.1);
        personDTO.setLatitude(91.0);
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonValidationErrorAboutToLarge() throws Exception {
		logger.info("updatePersonValidationErrorAboutToLarge");
        PersonDTO personDTO = new PersonDTO();
        personDTO.setAbout("About Jobsteries, About Jobsteries, About Jobsteries, About Jobsteries, About Jobsteries, About Jobsteries, About Jobsteries, About Jobsteries, About Jobsteries, About Jobsteries, About Jobsteries, About Jobsteries, About Jobsteries, About Jobsteries, About Jobsteries, About Jobsteries.");
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonValidationErrorUsernameToSmall() throws Exception {
		logger.info("updatePersonValidationErrorUsernameToSmall");
        PersonDTO personDTO = new PersonDTO();
        personDTO.setUsername("Test");
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonValidationErrorUsernameToLarge() throws Exception {
		logger.info("updatePersonValidationErrorUsernameToLarge");
        PersonDTO personDTO = new PersonDTO();
        personDTO.setUsername("TestTestTestTestTestTest");
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonValidationErrorRole() throws Exception {
		logger.info("updatePersonValidationErrorRole");
        PersonDTO personDTO = new PersonDTO();
        personDTO.setAccessRole("AAAA");
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonValidationErrorGender() throws Exception {
		logger.info("updatePersonValidationErrorGender");
        PersonDTO personDTO = new PersonDTO();
        personDTO.setGender("AAAA");
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonValidationErrorPasswordAndConfirmedPasswordToSmall() throws Exception {
		logger.info("updatePersonValidationErrorPasswordAndConfirmedPasswordToSmall");
        PersonDTO personDTO = new PersonDTO();
        personDTO.setPassword("Test");
        personDTO.setConfirmedPassword("Test");
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonValidationErrorPasswordAndConfirmedPasswordContainUnsupportedChars() throws Exception {
		logger.info("updatePersonValidationErrorPasswordAndConfirmedPasswordContainUnsupportedChars");
        PersonDTO personDTO = new PersonDTO();
        personDTO.setPassword("Te+st!12&3");
        personDTO.setConfirmedPassword("Te+st!12&3");
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updateNullPerson() throws Exception {
		logger.info("updateNullPerson");
		PersonDTO personDTO = new PersonDTO();
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonNullEmail() throws Exception {
		logger.info("updatePersonNullEmail");
		PersonDTO personDTO = new PersonDTO();
		personDTO.setEmail(null);
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonBlankEmail() throws Exception {
		logger.info("updatePersonBlankEmail");
		PersonDTO personDTO = new PersonDTO();
		personDTO.setEmail("");
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonSpaceEmail() throws Exception {
		logger.info("updatePersonSpaceEmail");
		PersonDTO personDTO = new PersonDTO();
		personDTO.setEmail(" ");
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonNullPhone() throws Exception {
		logger.info("updatePersonNullPhone");
		PersonDTO personDTO = new PersonDTO();
		personDTO.setMobilePhone(null);
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonBlankPhone() throws Exception {
		logger.info("updatePersonBlankPhone");
		PersonDTO personDTO = new PersonDTO();
		personDTO.setMobilePhone("");
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonSpacePhone() throws Exception {
		logger.info("updatePersonSpacePhone");
		PersonDTO personDTO = new PersonDTO();
		personDTO.setMobilePhone(" ");
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonNullFirstName() throws Exception {
		logger.info("updatePersonNullFirstName");
		PersonDTO personDTO = new PersonDTO();
		personDTO.setFirstName(null);
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonBlankFirstName() throws Exception {
		logger.info("updatePersonBlankFirstName");
		PersonDTO personDTO = new PersonDTO();
		personDTO.setFirstName("");
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonSpaceFirstName() throws Exception {
		logger.info("updatePersonSpaceFirstName");
		PersonDTO personDTO = new PersonDTO();
		personDTO.setFirstName(" ");
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonNullLastName() throws Exception {
		logger.info("updatePersonNullLastName");
		PersonDTO personDTO = new PersonDTO();
		personDTO.setLastName(null);
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonBlankLastName() throws Exception {
		logger.info("updatePersonBlankLastName");
		PersonDTO personDTO = new PersonDTO();
		personDTO.setLastName("");
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonSpaceLastName() throws Exception {
		logger.info("updatePersonSpaceLastName");
		PersonDTO personDTO = new PersonDTO();
		personDTO.setLastName(" ");
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonNullBirthDate() throws Exception {
		logger.info("updatePersonNullBirthDate");
		PersonDTO personDTO = new PersonDTO();
		personDTO.setBirthDate(null);
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonBlankBirthDate() throws Exception {
		logger.info("updatePersonBlankBirthDate");
		PersonDTO personDTO = new PersonDTO();
		personDTO.setBirthDate("");
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonSpaceBirthDate() throws Exception {
		logger.info("updatePersonSpaceBirthDate");
		PersonDTO personDTO = new PersonDTO();
		personDTO.setBirthDate(" ");
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonNullCity() throws Exception {
		logger.info("updatePersonNullCity");
		PersonDTO personDTO = new PersonDTO();
		personDTO.setCity(null);
    	personDTO.setCountry("World Union");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonBlankCity() throws Exception {
		logger.info("updatePersonBlankCity");
		PersonDTO personDTO = new PersonDTO();
		personDTO.setCity("");
    	personDTO.setCountry("World Union");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonSpaceCity() throws Exception {
		logger.info("updatePersonSpaceCity");
		PersonDTO personDTO = new PersonDTO();
		personDTO.setCity(" ");
    	personDTO.setCountry("World Union");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
		mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
        	//.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isBadRequest());
		}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonNullLongitude() throws Exception {
		logger.info("updatePersonNullLongitude");
		PersonDTO personDTO = new PersonDTO();
		personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(null);
    	personDTO.setLatitude(43.1);
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonNullLatitude() throws Exception {
		logger.info("updatePersonNullLatitude");
		PersonDTO personDTO = new PersonDTO();
		personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(null);
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonNullCountry() throws Exception {
		logger.info("updatePersonNullCountry");
		PersonDTO personDTO = new PersonDTO();
		personDTO.setCity("World city");
    	personDTO.setCountry(null);
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);    
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonBlankCountry() throws Exception {
		logger.info("updatePersonBlankCountry");
		PersonDTO personDTO = new PersonDTO();
		personDTO.setCity("World city");
    	personDTO.setCountry("");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonSpaceCountry() throws Exception {
		logger.info("updatePersonSpaceCountry");
		PersonDTO personDTO = new PersonDTO();
		personDTO.setCity("World city");
    	personDTO.setCountry(" ");
    	personDTO.setIso2Code("XX");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
		mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
        	//.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonNullIso2Code() throws Exception {
		logger.info("updatePersonNullIso2Code");
		PersonDTO personDTO = new PersonDTO();
		personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setIso2Code(null);
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonBlankIso2Code() throws Exception {
		logger.info("updatePersonBlankIso2Code");
		PersonDTO personDTO = new PersonDTO();
		personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setIso2Code("");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonSpaceIso2Code() throws Exception {
		logger.info("updatePersonSpaceIso2Code");
		PersonDTO personDTO = new PersonDTO();
		personDTO.setCity("World city");
    	personDTO.setCountry("World Union");
    	personDTO.setIso2Code(" ");
    	personDTO.setCountryRegion("World region");
    	personDTO.setLongitude(43.1);
    	personDTO.setLatitude(43.1);
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonNullUsername() throws Exception {
		logger.info("updatePersonNullUsername");
		PersonDTO personDTO = new PersonDTO();
		personDTO.setUsername(null);
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonBlankUsername() throws Exception {
		logger.info("updatePersonBlankUsername");
		PersonDTO personDTO = new PersonDTO();
		personDTO.setUsername("");
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonSpaceUsername() throws Exception {
		logger.info("updatePersonSpaceUsername");
		PersonDTO personDTO = new PersonDTO();
		personDTO.setUsername(" ");
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonNullRole() throws Exception {
		logger.info("updatePersonNullRole");
		PersonDTO personDTO = new PersonDTO();
		personDTO.setAccessRole(null);
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonBlankRole() throws Exception {
		logger.info("updatePersonBlankRole");
		PersonDTO personDTO = new PersonDTO();
		personDTO.setAccessRole("");
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonSpaceRole() throws Exception {
		logger.info("updatePersonSpaceRole");
		PersonDTO personDTO = new PersonDTO();
		personDTO.setAccessRole(" ");
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonNullGender() throws Exception {
		logger.info("updatePersonNullGender");
		PersonDTO personDTO = new PersonDTO();
		personDTO.setGender(null);
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonBlankGender() throws Exception {
		logger.info("updatePersonBlankGender");
		PersonDTO personDTO = new PersonDTO();
		personDTO.setGender("");
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonSpaceGender() throws Exception {
		logger.info("updatePersonSpaceGender");
		PersonDTO personDTO = new PersonDTO();
		personDTO.setGender(" ");
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonNullPassword() throws Exception {
		logger.info("updatePersonNullPassword");
		PersonDTO personDTO = new PersonDTO();
		personDTO.setPassword(null);
		personDTO.setConfirmedPassword("Test123");
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonBlankPassword() throws Exception {
		logger.info("updatePersonBlankPassword");
		PersonDTO personDTO = new PersonDTO();
		personDTO.setPassword("");
		personDTO.setConfirmedPassword("Test123");
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonSpacePassword() throws Exception {
		logger.info("updatePersonSpacePassword");
		PersonDTO personDTO = new PersonDTO();
		personDTO.setPassword(" ");
		personDTO.setConfirmedPassword("Test123");
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonNullConfirmedPassword() throws Exception {
		logger.info("updatePersonNullConfirmedPassword");
		PersonDTO personDTO = new PersonDTO();
		personDTO.setPassword("Test123");
		personDTO.setConfirmedPassword(null);
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonBlankConfirmedPassword() throws Exception {
		logger.info("updatePersonBlankConfirmedPassword");
		PersonDTO personDTO = new PersonDTO();
		personDTO.setPassword("Test123");
		personDTO.setConfirmedPassword("");
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonSpaceConfirmedPassword() throws Exception {
		logger.info("updatePersonSpaceConfirmedPassword");
		PersonDTO personDTO = new PersonDTO();
		personDTO.setPassword("Test123");
		personDTO.setConfirmedPassword(" ");
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
        mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
        	.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonMobilePhoneAlreadyExists() throws Exception {
		logger.info("updatePersonMobilePhoneAlreadyExists");
        PersonDTO personDTO = new PersonDTO();
        personDTO.setMobilePhone("0642345678");
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
		mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
        	//.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isNotAcceptable());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonEmailAlreadyExists() throws Exception {
		logger.info("updatePersonEmailAlreadyExists");
        PersonDTO personDTO = new PersonDTO();
        personDTO.setEmail("Jobster@mail.com");
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
		mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
        	//.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isNotAcceptable());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonWrongAccessRole() throws Exception {
		logger.info("updatePersonWrongAccessRole");
        PersonDTO personDTO = new PersonDTO();
        personDTO.setAccessRole("ROLE_ADMIN");
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
		mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
        	//.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isNotAcceptable());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonWrongGender() throws Exception {
		logger.info("updatePersonWrongGender");
        PersonDTO personDTO = new PersonDTO();
        personDTO.setGender("GENDER_CHILD");
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
		mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
        	//.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void updatePersonUsernameAlreadyExists() throws Exception {
		logger.info("updatePersonUsernameAlreadyExists");
        PersonDTO personDTO = new PersonDTO();
        personDTO.setUsername("Test1234");
        Gson gson = new Gson();
        String json = gson.toJson(personDTO);
		mockMvc.perform(put("/jobster/users/persons/" + PersonControllerTests.persons.get(0).getId())
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
        	//.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isNotAcceptable());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void archivePerson() throws Exception {
		logger.info("archivePerson");
		Integer id = PersonControllerTests.persons.get(1).getId();
        mockMvc.perform(put("/jobster/users/persons/archive/" + id)
			.header("Authorization", "Bearer " + token))
	 		.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.user.id", is(id)))
			.andExpect(jsonPath("$.user.status", is(-1)));
        PersonControllerTests.userAccounts.get(1).setStatusArchived();
        userAccountRepository.deleteById(PersonControllerTests.userAccounts.get(1).getId());
		PersonControllerTests.userAccounts.remove(PersonControllerTests.userAccounts.get(1));
		PersonControllerTests.persons.get(1).setStatusArchived();
		personRepository.deleteById(PersonControllerTests.persons.get(1).getId());
		PersonControllerTests.persons.remove(PersonControllerTests.persons.get(1));
	}

	@Test //@WithMockUser(username = "Test1234")
	public void archivePersonWhichNotExists() throws Exception {
		logger.info("archivePersonWhichNotExists");
        mockMvc.perform(put("/jobster/users/persons/archive/" + (PersonControllerTests.persons.get(4).getId()+1))
			.header("Authorization", "Bearer " + token))
        	.andExpect(status().isNotFound());
	}	

	@Test //@WithMockUser(username = "Test1234")
	public void undeletePerson() throws Exception {
		logger.info("undeletePerson");
//		Integer id = PersonControllerTests.persons.get(3).getId();
        mockMvc.perform(put("/jobster/users/persons/undelete/" + PersonControllerTests.persons.get(3).getId())
			.header("Authorization", "Bearer " + token))
        	//.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.id", is(PersonControllerTests.persons.get(3).getId().intValue())))
			.andExpect(jsonPath("$.status", is(1)));
        PersonControllerTests.userAccounts.get(3).setStatusActive();
        userAccountRepository.deleteById(PersonControllerTests.userAccounts.get(3).getId());
		PersonControllerTests.userAccounts.remove(PersonControllerTests.userAccounts.get(3));
		PersonControllerTests.persons.get(3).setStatusActive();
		personRepository.deleteById(PersonControllerTests.persons.get(3).getId());
		PersonControllerTests.persons.remove(PersonControllerTests.persons.get(3));
	}

	@Test
	public void undeletePersonWhichNotExists() throws Exception {
		logger.info("undeletePersonWhichNotExists");
        mockMvc.perform(put("/jobster/users/persons/undelete/" + (PersonControllerTests.persons.get(4).getId()+1))
			.header("Authorization", "Bearer " + token))
        	.andExpect(status().isNotFound());
	}	

	@Test //@WithMockUser(username = "Test1234")
	public void deletePerson() throws Exception {
		logger.info("deletePerson");
//		Integer id = PersonControllerTests.persons.get(1).getId();
		mockMvc.perform(delete("/jobster/users/persons/" + PersonControllerTests.persons.get(1).getId())
			.header("Authorization", "Bearer " + token)) 
			.andExpect(status().isOk()) 
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.user.id", is(PersonControllerTests.persons.get(1).getId().intValue())))
			.andExpect(jsonPath("$.user.status", is(0)));
        PersonControllerTests.userAccounts.get(1).setStatusInactive();
        userAccountRepository.deleteById(PersonControllerTests.userAccounts.get(1).getId());
		PersonControllerTests.userAccounts.remove(PersonControllerTests.userAccounts.get(1));
		PersonControllerTests.persons.get(1).setStatusInactive();
		personRepository.deleteById(PersonControllerTests.persons.get(1).getId());
		PersonControllerTests.persons.remove(PersonControllerTests.persons.get(1));
	}

	@Test //@WithMockUser(username = "Test1234")
	public void deletePersonWhichNotExists() throws Exception {
		logger.info("deletePersonWhichNotExists");
        mockMvc.perform(delete("/jobster/users/persons/" + (PersonControllerTests.persons.get(4).getId()+1))
    		.header("Authorization", "Bearer " + token))
        	.andExpect(status().isNotFound());
	}

}