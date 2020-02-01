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
import com.iktpreobuka.jobster.enumerations.EUserRole;
import com.iktpreobuka.jobster.repositories.CityRepository;
import com.iktpreobuka.jobster.repositories.CountryRegionRepository;
import com.iktpreobuka.jobster.repositories.CountryRepository;
import com.iktpreobuka.jobster.repositories.UserAccountRepository;
import com.iktpreobuka.jobster.repositories.UserRepository;


@RunWith(SpringRunner.class) 
@SpringBootTest 
@WebAppConfiguration

public class CountryControllerTests {
	
	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(),
			Charset.forName("utf8"));
	
	private static MockMvc mockMvc;
			
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	private static CityEntity city;
	
	private static CountryEntity country, countryA, countryI;
	
	private static CountryRegionEntity region;
	
	private static List<UserAccountEntity> userAccounts = new ArrayList<>();
	
	private static List<CountryEntity> countries = new ArrayList<>();

	private static List<CountryRegionEntity> countryRegions = new ArrayList<>();

	private static List<CityEntity> cities = new ArrayList<>();
	
	private static List<UserEntity> users = new ArrayList<>();
	
	private String token;
	
	@Autowired
	private FilterChainProxy springSecurityFilterChain;
	
	@Autowired 
	private CityRepository cityRepository;
	
	@Autowired
	private CountryRegionRepository countryRegionRepository;

	@Autowired
	private CountryRepository countryRepository;
	
	@Autowired
	private UserAccountRepository userAccountRepository;
	
	@Autowired 
	private UserRepository userRepository;
	

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	private boolean dbInit = false;
	
	@Before
	public void setUp() throws Exception { 
		logger.info("DBsetUp");
		if(!dbInit) { mockMvc = MockMvcBuilders
				.webAppContextSetup(webApplicationContext)
				.addFilter(springSecurityFilterChain)
				.build(); 
			country = countryRepository.save(new CountryEntity("Active", "AC", 1));
			countries.add(country);
			region = countryRegionRepository.save(new CountryRegionEntity(country, "World region"));	
			countryRegions.add(region);
			city = cityRepository.save(new CityEntity(region, "World city", 33.3, 34.5));
			cities.add(city);
			countryA= countryRepository.save(new CountryEntity("Archived", "AR", -1));;	
			countries.add(countryA);
			countryI= countryRepository.save(new CountryEntity("Inactive", "IN", 0));;	
			countries.add(countryI);
			users.add(userRepository.save(new UserEntity(city, "0642345678", "Jobster@mail.com", "About Jobster")));
			userAccounts.add(userAccountRepository.save(new UserAccountEntity(users.get(0), EUserRole.ROLE_ADMIN, "Test1234", "{bcrypt}$2a$10$FZjQbu7AqcSp0ns.GAxkbu0eKVUtNFTZNdVWwPOtBATLF0Bs9wtW2", users.get(0).getId())));
			dbInit = true;
			// GET TOKEN
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
					    } 
			logger.info("DBsetUp ok");
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
	
	//---------------------------- GET ------------------------------------------------------
	
	@Test 
	public void countryServiceNotFound() throws Exception { 
		logger.info("countryServiceNotFound");
		mockMvc.perform(get("/jobster/countries/persons/")
			.header("Authorization", "Bearer " + token))
			.andExpect(status().isNotFound()); 
	}
	
	@Test 
	public void getAllCountries() throws Exception { 
		logger.info("getAllCountries");
		mockMvc.perform(get("/jobster/countries/getAll")
				.header("Authorization", "Bearer " + token)) 
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].countryName", is("Active")))
			.andExpect(jsonPath("$[1].countryName", is("Archived")))
			.andExpect(jsonPath("$[2].countryName", is("Inactive")));
	}
	
	@Test 
	public void getAllCountriesNotFound() throws Exception { 
		logger.info("getAllCountriesNotFound");
		for (CountryEntity country : CountryControllerTests.countries) {
			countryRepository.delete(country);
		}
		CountryControllerTests.countries.clear();
		mockMvc.perform(get("/jobster/countries/getAll")
				.header("Authorization", "Bearer " + token))
			.andExpect(content().string("[]"));
	}
		
	@Test
	public void getCountryById() throws Exception { 
		logger.info("getCountryById");
		mockMvc.perform(get("/jobster/countries/getById/" + CountryControllerTests.countries.get(0).getId())
				.header("Authorization", "Bearer " + token))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(CountryControllerTests.countries.get(0).getId().intValue())))
			.andExpect(jsonPath("$.countryName", is("Active"))); 
	}

	@Test 
	public void getCountryByIdNotExists() throws Exception { 
		logger.info("getCountryByIdNotExists");
		mockMvc.perform(get("/jobster/countries/getById/" + (CountryControllerTests.countries.get(0).getId()+5))
				.header("Authorization", "Bearer " + token))
			.andExpect(content().string("Country doesn't exists."));
	}
	
	@Test 
	public void getCountryByIdWrongId() throws Exception { 
		logger.info("getCountryByIdWrongId");
		String wrongId="test";
		mockMvc.perform(get("/jobster/countries/getById/" + wrongId)
				.header("Authorization", "Bearer " + token))
			.andExpect(status().isBadRequest());
	}

	@Test
	public void getCountryByName() throws Exception { 
		logger.info("getCountryByName");
		mockMvc.perform(get("/jobster/countries/getByName/" + CountryControllerTests.countries.get(0).getCountryName())
				.header("Authorization", "Bearer " + token))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.countryName", is(CountryControllerTests.countries.get(0).getCountryName().toString()))); 
	}

	@Test 
	public void getCountryByNameNotExists() throws Exception { 
		logger.info("getCountryByNameNotExists");
		String testName="Alibukerki";
		mockMvc.perform(get("/jobster/countries/getByName/" + testName)
				.header("Authorization", "Bearer " + token))
			.andExpect(content().string("Country doesn't exists."));
	}
	
	@Test 
	public void getCountryByNameWrongName() throws Exception { 
		logger.info("getCountryByNameWrongName");
		String wrongName="Alibukerki13";
		mockMvc.perform(get("/jobster/countries/getByName/" + wrongName)
				.header("Authorization", "Bearer " + token))
			.andExpect(status().isBadRequest());
	}
	
	@Test 
	public void getAllCountriesArchived() throws Exception { 
		logger.info("getAllCountriesArchived");
		mockMvc.perform(get("/jobster/countries/archived")
				.header("Authorization", "Bearer " + token)) 
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].status", is(-1)));
	}
	
	@Test 
	public void getAllCountriesNotFoundArchived() throws Exception { 
		logger.info("getAllCountriesNotFoundArchived");
		for (CountryEntity country : CountryControllerTests.countries) {
			countryRepository.delete(country);
		}
		CountryControllerTests.countries.clear();
		mockMvc.perform(get("/jobster/countries/archived")
				.header("Authorization", "Bearer " + token))
			.andExpect(content().string("[]"));
	}

	@Test 
	public void getAllCountriesInactive() throws Exception { 
		logger.info("getAllCountriesInactive");
		mockMvc.perform(get("/jobster/countries/inactive")
				.header("Authorization", "Bearer " + token)) 
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].status", is(0)));

	}
	
	@Test 
	public void getAllCountriesNotFoundInactive() throws Exception { 
		logger.info("getAllCitiesNotFoundInactive");
		for (CountryEntity country : CountryControllerTests.countries) {
			countryRepository.delete(country);
		}
		CountryControllerTests.countries.clear();
		mockMvc.perform(get("/jobster/countries/inactive")
				.header("Authorization", "Bearer " + token))
			.andExpect(content().string("[]"));
	}
	
	@Test 
	public void getAllCountriesActive() throws Exception { 
		logger.info("getAllCountriesActive");
		mockMvc.perform(get("/jobster/countries/active")
				.header("Authorization", "Bearer " + token)) 
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].status", is(1)));

	}
	
	@Test 
	public void getAllCountriesNotFoundActive() throws Exception { 
		logger.info("getAllCountriesNotFoundActive");
		for (CountryEntity country : CountryControllerTests.countries) {
			countryRepository.delete(country);
		}
		CountryControllerTests.countries.clear();
		mockMvc.perform(get("/jobster/countries/active")
				.header("Authorization", "Bearer " + token))
			.andExpect(content().string("[]"));
	}
	

	//--------------------ADD COUNTRY-------------------------
	
	@Test 
	public void addNewCountry() throws Exception {
		logger.info("addNewCountry");
		CountryEntity country = new CountryEntity();
    	country.setCountryName("Latifundija");
    	country.setIso2Code("LF");
		Gson gson = new Gson();
    	String json = gson.toJson(country);
    	mockMvc.perform(post("/jobster/countries/addNewCountry")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.countryName", is("Latifundija")));
    	countryRepository.delete(countryRepository.getByCountryName("Latifundija"));
	}
	
	public void addNewCountrySameIsoDiffName() throws Exception {
		logger.info("addNewCountrySameIsoDiffName");
		CountryEntity country = new CountryEntity();
    	country.setCountryName("Latifundija");
    	country.setIso2Code("AC");
		Gson gson = new Gson();
    	String json = gson.toJson(country);
    	mockMvc.perform(post("/jobster/countries/addNewCountry")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		.andExpect(status().isBadRequest());
	}
	
	public void addNewCountrySameNameDiffIso() throws Exception {
		logger.info("addNewCountrySameIsoDiffName");
		CountryEntity country = new CountryEntity();
    	country.setCountryName("Active");
    	country.setIso2Code("LF");
		Gson gson = new Gson();
    	String json = gson.toJson(country);
    	mockMvc.perform(post("/jobster/countries/addNewCountry")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		.andExpect(status().isBadRequest());
	}
	
	@Test 
	public void addNewCountryAlreadyExists() throws Exception {
		logger.info("addNewCountryAlreadyExists");
		CountryEntity country = new CountryEntity();
    	country.setCountryName("Active");
    	country.setIso2Code("AC");
		Gson gson = new Gson();
    	String json = gson.toJson(country);
    	mockMvc.perform(post("/jobster/countries/addNewCountry")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		.andExpect(status().isNotAcceptable())
    		.andExpect(content().string("Country already exists."));
	}
	
	//-------------------------------------- ADD COUNTRY NAME -----------------------------------------
	
	@Test 
	public void addNewCountryMarginalName() throws Exception {
		logger.info("addNewCountryMarginalName");
		CountryEntity country = new CountryEntity();
    	country.setCountryName("La");
    	country.setIso2Code("LF");
		Gson gson = new Gson();
    	String json = gson.toJson(country);
    	mockMvc.perform(post("/jobster/countries/addNewCountry")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.countryName", is("La")));
    	countryRepository.delete(countryRepository.getByCountryName("La"));
	}
	
	@Test 
	public void addNewCountrySpaceName() throws Exception {
		logger.info("addNewCountrySpaceName");
		CountryEntity country = new CountryEntity();
    	country.setCountryName("La Tifundija");
    	country.setIso2Code("LF");
		Gson gson = new Gson();
    	String json = gson.toJson(country);
    	mockMvc.perform(post("/jobster/countries/addNewCountry")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.countryName", is("La Tifundija")));
    	countryRepository.delete(countryRepository.getByCountryName("La Tifundija"));
	}
	
	
	@Test 
	public void addNewCountryTooShortName() throws Exception {
		logger.info("addNewCountryTooShortName");
		CountryEntity country = new CountryEntity();
    	country.setCountryName("L");
    	country.setIso2Code("LF");
		Gson gson = new Gson();
    	String json = gson.toJson(country);
    	mockMvc.perform(post("/jobster/countries/addNewCountry")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		.andExpect(status().isBadRequest());
	}
	
	@Test 
	public void addNewCountryNullName() throws Exception {
		logger.info("addNewCountryNullName");
		CountryEntity country = new CountryEntity();
    	country.setCountryName(null);
    	country.setIso2Code("LF");
		Gson gson = new Gson();
    	String json = gson.toJson(country);
    	mockMvc.perform(post("/jobster/countries/addNewCountry")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		.andExpect(status().isBadRequest());
	}
	

	@Test 
	public void addNewCountryEmptyName() throws Exception {
		logger.info("addNewCountryEmptyName");
		CountryEntity country = new CountryEntity();
    	country.setCountryName("");
    	country.setIso2Code("LF");
		Gson gson = new Gson();
    	String json = gson.toJson(country);
    	mockMvc.perform(post("/jobster/countries/addNewCountry")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		.andExpect(status().isBadRequest());
	}
	
	@Test 
	public void addNewCountryNoName() throws Exception {
		logger.info("addNewCountryNoName");
		CountryEntity country = new CountryEntity();
    	country.setIso2Code("LF");
		Gson gson = new Gson();
    	String json = gson.toJson(country);
    	mockMvc.perform(post("/jobster/countries/addNewCountry")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		.andExpect(status().isBadRequest());
	}
	
	@Test 
	public void addNewCountryWrongNameNumbers() throws Exception {
		logger.info("addNewCountryWrongNameNumbers");
		CountryEntity country = new CountryEntity();
		country.setCountryName("Lai23");
    	country.setIso2Code("LF");
		Gson gson = new Gson();
    	String json = gson.toJson(country);
    	mockMvc.perform(post("/jobster/countries/addNewCountry")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		.andExpect(status().isBadRequest());
	}
	
	@Test 
	public void addNewCountryWrongNameOtherChars() throws Exception {
		logger.info("addNewCountryWrongNameOtherChars");
		CountryEntity country = new CountryEntity();
		country.setCountryName("Laih$");
    	country.setIso2Code("LF");
		Gson gson = new Gson();
    	String json = gson.toJson(country);
    	mockMvc.perform(post("/jobster/countries/addNewCountry")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		.andExpect(status().isBadRequest());
	}
	
	@Test 
	public void addNewCountryWrongNameLeadingSpace() throws Exception {
		logger.info("addNewCountryWrongNameLeadingSpace");
		CountryEntity country = new CountryEntity();
		country.setCountryName(" Laih");
    	country.setIso2Code("LF");
		Gson gson = new Gson();
    	String json = gson.toJson(country);
    	mockMvc.perform(post("/jobster/countries/addNewCountry")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		.andExpect(status().isBadRequest());
	}
	
	@Test 
	public void addNewCountryWrongNameTrailingSpace() throws Exception {
		logger.info("addNewCountryWrongNameLeadingSpace");
		CountryEntity country = new CountryEntity();
		country.setCountryName("Laih ");
    	country.setIso2Code("LF");
		Gson gson = new Gson();
    	String json = gson.toJson(country);
    	mockMvc.perform(post("/jobster/countries/addNewCountry")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		.andExpect(status().isBadRequest());
	}
	
	@Test 
	public void addNewCountryWrongNamemultipleSpaces() throws Exception {
		logger.info("addNewCountryWrongNamemultipleSpaces");
		CountryEntity country = new CountryEntity();
		country.setCountryName("Laih  Tifundija");
    	country.setIso2Code("LF");
		Gson gson = new Gson();
    	String json = gson.toJson(country);
    	mockMvc.perform(post("/jobster/countries/addNewCountry")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		.andExpect(status().isBadRequest());
	}
	
	@Test 
	public void addNewCountryWrongNameLeadingWhitespace() throws Exception {
		logger.info("addNewCountryWrongNameLeadingWhitespace");
		CountryEntity country = new CountryEntity();
		country.setCountryName("	Laih");
    	country.setIso2Code("LF");
		Gson gson = new Gson();
    	String json = gson.toJson(country);
    	mockMvc.perform(post("/jobster/countries/addNewCountry")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		.andExpect(status().isBadRequest());
	}
	
	@Test 
	public void addNewCountryWrongNameTrailingWhitespace() throws Exception {
		logger.info("addNewCountryWrongNameTrailingWhitespace");
		CountryEntity country = new CountryEntity();
		country.setCountryName("Laih	");
    	country.setIso2Code("LF");
		Gson gson = new Gson();
    	String json = gson.toJson(country);
    	mockMvc.perform(post("/jobster/countries/addNewCountry")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		.andExpect(status().isBadRequest());
	}
	
	@Test 
	public void addNewCountryWrongNameMiddleWhitespace() throws Exception {
		logger.info("addNewCountryWrongNameMiddleWhitespace");
		CountryEntity country = new CountryEntity();
		country.setCountryName("Laih	Tifundija");
    	country.setIso2Code("LF");
		Gson gson = new Gson();
    	String json = gson.toJson(country);
    	mockMvc.perform(post("/jobster/countries/addNewCountry")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		.andExpect(status().isBadRequest());
	}
	
	//-------------------------------------- ADD COUNTRY ISOCODE -----------------------------------------
		
		@Test 
		public void addNewCountryTooShortIso2() throws Exception {
			logger.info("addNewCountryTooShortIso2");
			CountryEntity country = new CountryEntity();
	    	country.setCountryName("Latif");
	    	country.setIso2Code("L");
			Gson gson = new Gson();
	    	String json = gson.toJson(country);
	    	mockMvc.perform(post("/jobster/countries/addNewCountry")
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON).content(json))
	    		.andExpect(status().isBadRequest());
		}
		
		@Test 
		public void addNewCountryTooLongIso2() throws Exception {
			logger.info("addNewCountryTooLongIso2");
			CountryEntity country = new CountryEntity();
	    	country.setCountryName("Latif");
	    	country.setIso2Code("LAT");
			Gson gson = new Gson();
	    	String json = gson.toJson(country);
	    	mockMvc.perform(post("/jobster/countries/addNewCountry")
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON).content(json))
	    		.andExpect(status().isBadRequest());
		}
		
		@Test 
		public void addNewCountryNullIso2() throws Exception {
			logger.info("addNewCountryNullIso2");
			CountryEntity country = new CountryEntity();
	    	country.setCountryName("Latifundija");
	    	country.setIso2Code(null);
			Gson gson = new Gson();
	    	String json = gson.toJson(country);
	    	mockMvc.perform(post("/jobster/countries/addNewCountry")
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON).content(json))
	    		.andExpect(status().isBadRequest());
		}
		

		@Test 
		public void addNewCountryEmptyIso2() throws Exception {
			logger.info("addNewCountryEmptyIso2");
			CountryEntity country = new CountryEntity();
	    	country.setCountryName("Latifundija");
	    	country.setIso2Code("");
			Gson gson = new Gson();
	    	String json = gson.toJson(country);
	    	mockMvc.perform(post("/jobster/countries/addNewCountry")
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON).content(json))
	    		.andExpect(status().isBadRequest());
		}
		
		@Test 
		public void addNewCountryNoIso2() throws Exception {
			logger.info("addNewCountryNoIso2");
			CountryEntity country = new CountryEntity();
			country.setCountryName("Latifundija");
			Gson gson = new Gson();
	    	String json = gson.toJson(country);
	    	mockMvc.perform(post("/jobster/countries/addNewCountry")
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON).content(json))
	    		.andExpect(status().isBadRequest());
		}
		
		@Test 
		public void addNewCountryWrongIso2Numbers() throws Exception {
			logger.info("addNewCountryWrongIso2Numbers");
			CountryEntity country = new CountryEntity();
			country.setCountryName("Latifundija");
	    	country.setIso2Code("L1");
			Gson gson = new Gson();
	    	String json = gson.toJson(country);
	    	mockMvc.perform(post("/jobster/countries/addNewCountry")
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON).content(json))
	    		.andExpect(status().isBadRequest());
		}
		
		@Test 
		public void addNewCountryWrongIso2OtherChars() throws Exception {
			logger.info("addNewCountryWrongIso2OtherChars");
			CountryEntity country = new CountryEntity();
			country.setCountryName("Laixxx");
	    	country.setIso2Code("%D");
			Gson gson = new Gson();
	    	String json = gson.toJson(country);
	    	mockMvc.perform(post("/jobster/countries/addNewCountry")
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON).content(json))
	    		.andExpect(status().isBadRequest());
		}
		
		@Test 
		public void addNewCountryWrongIso2LeadingSpace() throws Exception {
			logger.info("addNewCountryWrongIso2LeadingSpace");
			CountryEntity country = new CountryEntity();
			country.setCountryName("Laih");
	    	country.setIso2Code(" L");
			Gson gson = new Gson();
	    	String json = gson.toJson(country);
	    	mockMvc.perform(post("/jobster/countries/addNewCountry")
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON).content(json))
	    		.andExpect(status().isBadRequest());
		}
		
		@Test 
		public void addNewCountryWrongIso2TrailingSpace() throws Exception {
			logger.info("addNewCountryWrongIso2TrailingSpace");
			CountryEntity country = new CountryEntity();
			country.setCountryName("Laih");
	    	country.setIso2Code("L ");
			Gson gson = new Gson();
	    	String json = gson.toJson(country);
	    	mockMvc.perform(post("/jobster/countries/addNewCountry")
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON).content(json))
	    		.andExpect(status().isBadRequest());
		}
		
		@Test 
		public void addNewCountryWrongIso2multipleSpaces() throws Exception {
			logger.info("addNewCountryWrongIso2multipleSpaces");
			CountryEntity country = new CountryEntity();
			country.setCountryName("LaihTifundija");
	    	country.setIso2Code("  ");
			Gson gson = new Gson();
	    	String json = gson.toJson(country);
	    	mockMvc.perform(post("/jobster/countries/addNewCountry")
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON).content(json))
	    		.andExpect(status().isBadRequest());
		}
		
		@Test 
		public void addNewCountryWrongIso2MiddleSpaces() throws Exception {
			logger.info("addNewCountryWrongIso2MiddleSpaces");
			CountryEntity country = new CountryEntity();
			country.setCountryName("LaihTifundija");
	    	country.setIso2Code("L F");
			Gson gson = new Gson();
	    	String json = gson.toJson(country);
	    	mockMvc.perform(post("/jobster/countries/addNewCountry")
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON).content(json))
	    		.andExpect(status().isBadRequest());
		}
		
		@Test 
		public void addNewCountryWrongIso2LeadingWhitespace() throws Exception {
			logger.info("addNewCountryWrongIso2LeadingWhitespace");
			CountryEntity country = new CountryEntity();
			country.setCountryName("Laih");
	    	country.setIso2Code("	L");
			Gson gson = new Gson();
	    	String json = gson.toJson(country);
	    	mockMvc.perform(post("/jobster/countries/addNewCountry")
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON).content(json))
	    		.andExpect(status().isBadRequest());
		}
		
		@Test 
		public void addNewCountryWrongIso2TrailingWhitespace() throws Exception {
			logger.info("addNewCountryWrongIso2TrailingWhitespace");
			CountryEntity country = new CountryEntity();
			country.setCountryName("Laih");
	    	country.setIso2Code("LF	");
			Gson gson = new Gson();
	    	String json = gson.toJson(country);
	    	mockMvc.perform(post("/jobster/countries/addNewCountry")
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON).content(json))
	    		.andExpect(status().isBadRequest());
		}
		
		@Test 
		public void addNewCountryWrongIso2MiddleWhitespace() throws Exception {
			logger.info("addNewCountryWrongIso2MiddleWhitespace");
			CountryEntity country = new CountryEntity();
			country.setCountryName("LaihTifundija");
	    	country.setIso2Code("L	F");
			Gson gson = new Gson();
	    	String json = gson.toJson(country);
	    	mockMvc.perform(post("/jobster/countries/addNewCountry")
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON).content(json))
	    		.andExpect(status().isBadRequest());
		}
		
		//-------------------------------------- MODIFY COUNTRY -----------------------------------------
		
		@Test 
		public void modifyCountry() throws Exception {
			logger.info("modifyCountry");
			CountryEntity country = new CountryEntity();
			country.setCountryName("LaihTifundija");
	    	country.setIso2Code("LF");
			Gson gson = new Gson();
	    	String json = gson.toJson(country);
	    	mockMvc.perform(put("/jobster/countries/modify/" + (CountryControllerTests.countries.get(0).getId()))
	    			.header("Authorization", "Bearer " + token)
	    			.contentType(MediaType.APPLICATION_JSON).content(json))
	        		.andExpect(status().isOk())
	    			.andExpect(content().contentType(contentType)) 
	    			.andExpect(jsonPath("$.countryName", is("LaihTifundija")));
	    	Integer Id = CountryControllerTests.countries.get(0).getId();
	    	CountryControllerTests.countries.remove(0);
	    	CountryControllerTests.countries.add(0,countryRepository.findByIdAndStatusLike(Id, 1));
		}
		
		@Test 
		public void modifyCountryMarginalName() throws Exception {
			logger.info("modifyCountryMarginalName");
			CountryEntity country = new CountryEntity();
			country.setCountryName("La");
	    	country.setIso2Code("LF");
			Gson gson = new Gson();
	    	String json = gson.toJson(country);
	    	mockMvc.perform(put("/jobster/countries/modify/" + (CountryControllerTests.countries.get(0).getId()))
	    			.header("Authorization", "Bearer " + token)
	    			.contentType(MediaType.APPLICATION_JSON).content(json))
	        		.andExpect(status().isOk())
	    			.andExpect(content().contentType(contentType)) 
	    			.andExpect(jsonPath("$.countryName", is("La")));
	    	Integer Id = CountryControllerTests.countries.get(0).getId();
	    	CountryControllerTests.countries.remove(0);
	    	CountryControllerTests.countries.add(0,countryRepository.findByIdAndStatusLike(Id, 1));
		}
		
		@Test 
		public void modifyCountrySpaceName() throws Exception {
			logger.info("modifyCountrySpacelName");
			CountryEntity country = new CountryEntity();
			country.setCountryName("La TI");
	    	country.setIso2Code("LF");
			Gson gson = new Gson();
	    	String json = gson.toJson(country);
	    	mockMvc.perform(put("/jobster/countries/modify/" + (CountryControllerTests.countries.get(0).getId()))
	    			.header("Authorization", "Bearer " + token)
	    			.contentType(MediaType.APPLICATION_JSON).content(json))
	        		.andExpect(status().isOk())
	    			.andExpect(content().contentType(contentType)) 
	    			.andExpect(jsonPath("$.countryName", is("La TI")));
	    	Integer Id = CountryControllerTests.countries.get(0).getId();
	    	CountryControllerTests.countries.remove(0);
	    	CountryControllerTests.countries.add(0,countryRepository.findByIdAndStatusLike(Id, 1));
		}
		
		
		//-------------------------------------- MODIFY COUNTRY NAME-----------------------------------------
		
				@Test 
				public void modifyCountryNullName() throws Exception {
					logger.info("modifyCountryNullName");
					CountryEntity country = new CountryEntity();
					country.setCountryName(null);
			    	country.setIso2Code("LF");
					Gson gson = new Gson();
			    	String json = gson.toJson(country);
			    	mockMvc.perform(put("/jobster/countries/modify/" + (CountryControllerTests.countries.get(0).getId()))
			    			.header("Authorization", "Bearer " + token)
			    			.contentType(MediaType.APPLICATION_JSON).content(json))
			    			.andExpect(status().isBadRequest());
				}
				
				@Test 
				public void modifyCountryNoName() throws Exception {
					logger.info("modifyCountryNoName");
					CountryEntity country = new CountryEntity();
			    	country.setIso2Code("LF");
					Gson gson = new Gson();
			    	String json = gson.toJson(country);
			    	mockMvc.perform(put("/jobster/countries/modify/" + (CountryControllerTests.countries.get(0).getId()))
			    			.header("Authorization", "Bearer " + token)
			    			.contentType(MediaType.APPLICATION_JSON).content(json))
			    			.andExpect(status().isBadRequest());
				}
				

				@Test 
				public void modifyCountryEmptyName() throws Exception {
					logger.info("modifyCountryEmptyName");
					CountryEntity country = new CountryEntity();
					country.setCountryName("");
			    	country.setIso2Code("LF");
					Gson gson = new Gson();
			    	String json = gson.toJson(country);
			    	mockMvc.perform(put("/jobster/countries/modify/" + (CountryControllerTests.countries.get(0).getId()))
			    			.header("Authorization", "Bearer " + token)
			    			.contentType(MediaType.APPLICATION_JSON).content(json))
			    			.andExpect(status().isBadRequest());
				}
				
				@Test 
				public void modifyCountryTooShortName() throws Exception {
					logger.info("modifyCountryTooShortName");
					CountryEntity country = new CountryEntity();
					country.setCountryName("D");
			    	country.setIso2Code("LF");
					Gson gson = new Gson();
			    	String json = gson.toJson(country);
			    	mockMvc.perform(put("/jobster/countries/modify/" + (CountryControllerTests.countries.get(0).getId()))
			    			.header("Authorization", "Bearer " + token)
			    			.contentType(MediaType.APPLICATION_JSON).content(json))
			    			.andExpect(status().isBadRequest());
				}
				
				@Test 
				public void modifyCountryWrongNameNumbers() throws Exception {
					logger.info("modifyCountryWrongNameNumbers");
					CountryEntity country = new CountryEntity();
					country.setCountryName("Ddd12");
			    	country.setIso2Code("LF");
					Gson gson = new Gson();
			    	String json = gson.toJson(country);
			    	mockMvc.perform(put("/jobster/countries/modify/" + (CountryControllerTests.countries.get(0).getId()))
			    			.header("Authorization", "Bearer " + token)
			    			.contentType(MediaType.APPLICATION_JSON).content(json))
			    			.andExpect(status().isBadRequest());
				}
				
				@Test 
				public void modifyCountryWrongNameOtherChars() throws Exception {
					logger.info("modifyCountryWrongNameOtherChars");
					CountryEntity country = new CountryEntity();
					country.setCountryName("Ddd!!");
			    	country.setIso2Code("LF");
					Gson gson = new Gson();
			    	String json = gson.toJson(country);
			    	mockMvc.perform(put("/jobster/countries/modify/" + (CountryControllerTests.countries.get(0).getId()))
			    			.header("Authorization", "Bearer " + token)
			    			.contentType(MediaType.APPLICATION_JSON).content(json))
			    			.andExpect(status().isBadRequest());
				}
				
				@Test 
				public void modifyCountryWrongNameLeadingSpace() throws Exception {
					logger.info("modifyCountryWrongNameLeadingSpace");
					CountryEntity country = new CountryEntity();
					country.setCountryName(" Ddd");
			    	country.setIso2Code("LF");
					Gson gson = new Gson();
			    	String json = gson.toJson(country);
			    	mockMvc.perform(put("/jobster/countries/modify/" + (CountryControllerTests.countries.get(0).getId()))
			    			.header("Authorization", "Bearer " + token)
			    			.contentType(MediaType.APPLICATION_JSON).content(json))
			    			.andExpect(status().isBadRequest());
				}
				
				@Test 
				public void modifyCountryWrongNameTrailingSpace() throws Exception {
					logger.info("modifyCountryWrongNameTrailingSpace");
					CountryEntity country = new CountryEntity();
					country.setCountryName("Ddd ");
			    	country.setIso2Code("LF");
					Gson gson = new Gson();
			    	String json = gson.toJson(country);
			    	mockMvc.perform(put("/jobster/countries/modify/" + (CountryControllerTests.countries.get(0).getId()))
			    			.header("Authorization", "Bearer " + token)
			    			.contentType(MediaType.APPLICATION_JSON).content(json))
			    			.andExpect(status().isBadRequest());
				}
				
				@Test 
				public void modifyCountryWrongNameMultipleSpaces() throws Exception {
					logger.info("modifyCountryWrongNameMultipleSpaces");
					CountryEntity country = new CountryEntity();
					country.setCountryName("Dd  d");
			    	country.setIso2Code("LF");
					Gson gson = new Gson();
			    	String json = gson.toJson(country);
			    	mockMvc.perform(put("/jobster/countries/modify/" + (CountryControllerTests.countries.get(0).getId()))
			    			.header("Authorization", "Bearer " + token)
			    			.contentType(MediaType.APPLICATION_JSON).content(json))
			    			.andExpect(status().isBadRequest());
				}
				
				@Test 
				public void modifyCountryWrongNameLeadingWhitespace() throws Exception {
					logger.info("modifyCountryWrongNameLeadingWhitespace");
					CountryEntity country = new CountryEntity();
					country.setCountryName("	Ddd");
			    	country.setIso2Code("LF");
					Gson gson = new Gson();
			    	String json = gson.toJson(country);
			    	mockMvc.perform(put("/jobster/countries/modify/" + (CountryControllerTests.countries.get(0).getId()))
			    			.header("Authorization", "Bearer " + token)
			    			.contentType(MediaType.APPLICATION_JSON).content(json))
			    			.andExpect(status().isBadRequest());
				}
				
				@Test 
				public void modifyCountryWrongNameTrailingWhitespace() throws Exception {
					logger.info("modifyCountryWrongNameTrailingWhitespace");
					CountryEntity country = new CountryEntity();
					country.setCountryName("Ddd	");
			    	country.setIso2Code("LF");
					Gson gson = new Gson();
			    	String json = gson.toJson(country);
			    	mockMvc.perform(put("/jobster/countries/modify/" + (CountryControllerTests.countries.get(0).getId()))
			    			.header("Authorization", "Bearer " + token)
			    			.contentType(MediaType.APPLICATION_JSON).content(json))
			    			.andExpect(status().isBadRequest());
				}
				
				@Test 
				public void modifyCountryWrongNameMiddleWhitespace() throws Exception {
					logger.info("modifyCountryWrongNameMiddleWhitespace");
					CountryEntity country = new CountryEntity();
					country.setCountryName("Dd 	d");
			    	country.setIso2Code("LF");
					Gson gson = new Gson();
			    	String json = gson.toJson(country);
			    	mockMvc.perform(put("/jobster/countries/modify/" + (CountryControllerTests.countries.get(0).getId()))
			    			.header("Authorization", "Bearer " + token)
			    			.contentType(MediaType.APPLICATION_JSON).content(json))
			    			.andExpect(status().isBadRequest());
				}
				
			//-------------------------------------- MODIFY COUNTRY NAME-----------------------------------------
				
				@Test 
				public void modifyCountryNullIso2() throws Exception {
					logger.info("modifyCountryNullIso2");
					CountryEntity country = new CountryEntity();
					country.setCountryName("Latis");
			    	country.setIso2Code(null);
					Gson gson = new Gson();
			    	String json = gson.toJson(country);
			    	mockMvc.perform(put("/jobster/countries/modify/" + (CountryControllerTests.countries.get(0).getId()))
			    			.header("Authorization", "Bearer " + token)
			    			.contentType(MediaType.APPLICATION_JSON).content(json))
			    			.andExpect(status().isBadRequest());
				}
				
				@Test 
				public void modifyCountryNoIso2() throws Exception {
					logger.info("modifyCountryNoIso2");
					CountryEntity country = new CountryEntity();
					country.setCountryName("Latis");
					Gson gson = new Gson();
			    	String json = gson.toJson(country);
			    	mockMvc.perform(put("/jobster/countries/modify/" + (CountryControllerTests.countries.get(0).getId()))
			    			.header("Authorization", "Bearer " + token)
			    			.contentType(MediaType.APPLICATION_JSON).content(json))
			    			.andExpect(status().isBadRequest());
				}
				

				@Test 
				public void modifyCountryEmptyIso2() throws Exception {
					logger.info("modifyCountryEmptyName");
					CountryEntity country = new CountryEntity();
					country.setCountryName("XXXXX");
			    	country.setIso2Code("");
					Gson gson = new Gson();
			    	String json = gson.toJson(country);
			    	mockMvc.perform(put("/jobster/countries/modify/" + (CountryControllerTests.countries.get(0).getId()))
			    			.header("Authorization", "Bearer " + token)
			    			.contentType(MediaType.APPLICATION_JSON).content(json))
			    			.andExpect(status().isBadRequest());
				}
				
				@Test 
				public void modifyCountryTooShortIso2() throws Exception {
					logger.info("modifyCountryTooShortIso2");
					CountryEntity country = new CountryEntity();
					country.setCountryName("Dxxxx");
			    	country.setIso2Code("L");
					Gson gson = new Gson();
			    	String json = gson.toJson(country);
			    	mockMvc.perform(put("/jobster/countries/modify/" + (CountryControllerTests.countries.get(0).getId()))
			    			.header("Authorization", "Bearer " + token)
			    			.contentType(MediaType.APPLICATION_JSON).content(json))
			    			.andExpect(status().isBadRequest());
				}
				
				@Test 
				public void modifyCountryTooLongIso2() throws Exception {
					logger.info("modifyCountryTooLongIso2");
					CountryEntity country = new CountryEntity();
					country.setCountryName("Dxxxx");
			    	country.setIso2Code("LONG");
					Gson gson = new Gson();
			    	String json = gson.toJson(country);
			    	mockMvc.perform(put("/jobster/countries/modify/" + (CountryControllerTests.countries.get(0).getId()))
			    			.header("Authorization", "Bearer " + token)
			    			.contentType(MediaType.APPLICATION_JSON).content(json))
			    			.andExpect(status().isBadRequest());
				}
				
				@Test 
				public void modifyCountryWrongIso2Numbers() throws Exception {
					logger.info("modifyCountryWrongIso2Numbers");
					CountryEntity country = new CountryEntity();
					country.setCountryName("Ddd");
			    	country.setIso2Code("L1");
					Gson gson = new Gson();
			    	String json = gson.toJson(country);
			    	mockMvc.perform(put("/jobster/countries/modify/" + (CountryControllerTests.countries.get(0).getId()))
			    			.header("Authorization", "Bearer " + token)
			    			.contentType(MediaType.APPLICATION_JSON).content(json))
			    			.andExpect(status().isBadRequest());
				}
				
				@Test 
				public void modifyCountryWrongIso2OtherChars() throws Exception {
					logger.info("modifyCountryWrongIso2OtherChars");
					CountryEntity country = new CountryEntity();
					country.setCountryName("Ddd");
			    	country.setIso2Code("L$");
					Gson gson = new Gson();
			    	String json = gson.toJson(country);
			    	mockMvc.perform(put("/jobster/countries/modify/" + (CountryControllerTests.countries.get(0).getId()))
			    			.header("Authorization", "Bearer " + token)
			    			.contentType(MediaType.APPLICATION_JSON).content(json))
			    			.andExpect(status().isBadRequest());
				}
				
				@Test 
				public void modifyCountryWrongIso2LeadingSpace() throws Exception {
					logger.info("modifyCountryWrongIso2LeadingSpace");
					CountryEntity country = new CountryEntity();
					country.setCountryName("Ddd");
			    	country.setIso2Code(" LF");
					Gson gson = new Gson();
			    	String json = gson.toJson(country);
			    	mockMvc.perform(put("/jobster/countries/modify/" + (CountryControllerTests.countries.get(0).getId()))
			    			.header("Authorization", "Bearer " + token)
			    			.contentType(MediaType.APPLICATION_JSON).content(json))
			    			.andExpect(status().isBadRequest());
				}
				
				@Test 
				public void modifyCountryWrongIso2TrailingSpace() throws Exception {
					logger.info("modifyCountryWrongIso2TrailingSpace");
					CountryEntity country = new CountryEntity();
					country.setCountryName("Ddd");
			    	country.setIso2Code("L ");
					Gson gson = new Gson();
			    	String json = gson.toJson(country);
			    	mockMvc.perform(put("/jobster/countries/modify/" + (CountryControllerTests.countries.get(0).getId()))
			    			.header("Authorization", "Bearer " + token)
			    			.contentType(MediaType.APPLICATION_JSON).content(json))
			    			.andExpect(status().isBadRequest());
				}
				
				@Test 
				public void modifyCountryWrongIso2MiddleSpace() throws Exception {
					logger.info("modifyCountryWrongIso2MiddleSpace");
					CountryEntity country = new CountryEntity();
					country.setCountryName("Dd d");
			    	country.setIso2Code("L F");
					Gson gson = new Gson();
			    	String json = gson.toJson(country);
			    	mockMvc.perform(put("/jobster/countries/modify/" + (CountryControllerTests.countries.get(0).getId()))
			    			.header("Authorization", "Bearer " + token)
			    			.contentType(MediaType.APPLICATION_JSON).content(json))
			    			.andExpect(status().isBadRequest());
				}
				
				@Test 
				public void modifyCountryWrongIso2LeadingWhitespace() throws Exception {
					logger.info("modifyCountryWrongIso2LeadingWhitespace");
					CountryEntity country = new CountryEntity();
					country.setCountryName("Ddd");
			    	country.setIso2Code("	LF");
					Gson gson = new Gson();
			    	String json = gson.toJson(country);
			    	mockMvc.perform(put("/jobster/countries/modify/" + (CountryControllerTests.countries.get(0).getId()))
			    			.header("Authorization", "Bearer " + token)
			    			.contentType(MediaType.APPLICATION_JSON).content(json))
			    			.andExpect(status().isBadRequest());
				}
				
				@Test 
				public void modifyCountryWrongIso2TrailingWhitespace() throws Exception {
					logger.info("modifyCountryWrongNameTrailingWhitespace");
					CountryEntity country = new CountryEntity();
					country.setCountryName("Ddd");
			    	country.setIso2Code("LF	");
					Gson gson = new Gson();
			    	String json = gson.toJson(country);
			    	mockMvc.perform(put("/jobster/countries/modify/" + (CountryControllerTests.countries.get(0).getId()))
			    			.header("Authorization", "Bearer " + token)
			    			.contentType(MediaType.APPLICATION_JSON).content(json))
			    			.andExpect(status().isBadRequest());
				}
				
				@Test 
				public void modifyCountryWrongIso2MiddleWhitespace() throws Exception {
					logger.info("modifyCountryWrongIso2MiddleWhitespace");
					CountryEntity country = new CountryEntity();
					country.setCountryName("Ddd");
			    	country.setIso2Code("L	F");
					Gson gson = new Gson();
			    	String json = gson.toJson(country);
			    	mockMvc.perform(put("/jobster/countries/modify/" + (CountryControllerTests.countries.get(0).getId()))
			    			.header("Authorization", "Bearer " + token)
			    			.contentType(MediaType.APPLICATION_JSON).content(json))
			    			.andExpect(status().isBadRequest());
				}
				
		//----------------------------- DELETE/UNDELETE CITY--------------------------------
				
				@Test 
				public void deleteCountry() throws Exception {
					logger.info("deleteCountry");
					CountryEntity country=CountryControllerTests.countries.get(0);
					country.setStatus(0);
					Gson gson = new Gson();
			    	String json = gson.toJson(city);
			    	mockMvc.perform(delete("/jobster/countries/delete/" + (CountryControllerTests.countries.get(0).getId()))
			    			.header("Authorization", "Bearer " + token)
			    			.contentType(MediaType.APPLICATION_JSON).content(json))
			    	.andExpect(status().isOk())
	    			.andExpect(content().contentType(contentType)) 
	    			.andExpect(jsonPath("$.countryName", is("Active")))
	    			.andExpect(jsonPath("$.status", is(0)));
			    	Integer Id = CountryControllerTests.countries.get(0).getId();
			    	CountryControllerTests.countries.remove(0);
			    	CountryControllerTests.countries.add(0,countryRepository.findByIdAndStatusLike(Id, 0));
				}
				
				@Test 
				public void undeleteCountry() throws Exception {
					logger.info("undeleteCountry");
					CountryEntity country=CountryControllerTests.countries.get(2);
					city.setStatus(1);
					Gson gson = new Gson();
			    	String json = gson.toJson(country);
			    	mockMvc.perform(put("/jobster/countries/undelete/" + (CountryControllerTests.countries.get(2).getId()))
			    			.header("Authorization", "Bearer " + token)
			    			.contentType(MediaType.APPLICATION_JSON).content(json))
			    	.andExpect(status().isOk())
	    			.andExpect(content().contentType(contentType)) 
	    			.andExpect(jsonPath("$.countryName", is("Inactive")))
	    			.andExpect(jsonPath("$.status", is(1)));
			    	Integer Id = CountryControllerTests.countries.get(2).getId();
			    	CountryControllerTests.countries.remove(2);
			    	CountryControllerTests.countries.add(2,countryRepository.findByIdAndStatusLike(Id, 1));
				}
				
//----------------------------- ARCHIVE/UNARCHIVE CITY--------------------------------
				
				@Test 
				public void unarchiveCountry() throws Exception {
					logger.info("unarchiveCountry");
					CountryEntity country=CountryControllerTests.countries.get(1);
					city.setStatus(1);
					Gson gson = new Gson();
			    	String json = gson.toJson(country);
			    	mockMvc.perform(put("/jobster/countries/unarchive/" + (CountryControllerTests.countries.get(1).getId()))
			    			.header("Authorization", "Bearer " + token)
			    			.contentType(MediaType.APPLICATION_JSON).content(json))
			    	.andExpect(status().isOk())
	    			.andExpect(content().contentType(contentType)) 
	    			.andExpect(jsonPath("$.countryName", is("Archived")))
	    			.andExpect(jsonPath("$.status", is(1)));
	    	Integer Id = CountryControllerTests.countries.get(1).getId();
	    	CountryControllerTests.countries.remove(1);
	    	CountryControllerTests.countries.add(1,countryRepository.findByIdAndStatusLike(Id, 1));
				}
				
				@Test 
				public void archiveCountry() throws Exception {
					logger.info("archiveCountry");
					CountryEntity country=CountryControllerTests.countries.get(2);
					city.setStatus(-1);
					Gson gson = new Gson();
			    	String json = gson.toJson(country);
			    	mockMvc.perform(put("/jobster/countries/archive/" + (CountryControllerTests.countries.get(2).getId()))
			    			.header("Authorization", "Bearer " + token)
			    			.contentType(MediaType.APPLICATION_JSON).content(json))
			    	.andExpect(status().isOk())
	    			.andExpect(content().contentType(contentType)) 
	    			.andExpect(jsonPath("$.countryName", is("Inactive")))
	    			.andExpect(jsonPath("$.status", is(-1)));
	    	Integer Id = CountryControllerTests.countries.get(2).getId();
	    	CountryControllerTests.countries.remove(2);
	    	CountryControllerTests.countries.add(2,countryRepository.findByIdAndStatusLike(Id, -1));
				}
				
				//-------------------------------------- EOF -----------------------------------------
}

