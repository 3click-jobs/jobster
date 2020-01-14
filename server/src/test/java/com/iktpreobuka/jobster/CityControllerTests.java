package com.iktpreobuka.jobster;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
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
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
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
import com.iktpreobuka.jobster.entities.UserEntity;
import com.iktpreobuka.jobster.entities.dto.POSTCityDTO;
import com.iktpreobuka.jobster.enumerations.EUserRole;
import com.iktpreobuka.jobster.repositories.CityRepository;
import com.iktpreobuka.jobster.repositories.CountryRegionRepository;
import com.iktpreobuka.jobster.repositories.CountryRepository;
import com.iktpreobuka.jobster.repositories.PersonRepository;
import com.iktpreobuka.jobster.repositories.UserAccountRepository;
import com.iktpreobuka.jobster.repositories.UserRepository;


@RunWith(SpringRunner.class) 
@SpringBootTest 
@WebAppConfiguration

public class CityControllerTests {
	
	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(),
			Charset.forName("utf8"));
	
	private static MockMvc mockMvc;
			
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	private static CityEntity city, cityA, cityI;

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
	
	private static List<UserEntity> users = new ArrayList<>();
	
	private String token;
	
	@Autowired
	private FilterChainProxy springSecurityFilterChain;

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
	
	@Autowired 
	private UserRepository userRepository;
	
	/*@Autowired
	private CityDistanceRepository cityDistanceRepository;*/
	

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	private boolean dbInit = false;
	
	
	@SuppressWarnings("deprecation")
	@Before
	
	public void setUp() throws Exception { 
		logger.info("DBsetUp");
		if(!dbInit) { mockMvc = MockMvcBuilders
				.webAppContextSetup(webApplicationContext)
				.addFilter(springSecurityFilterChain)
				.build(); 
			country = countryRepository.save(new CountryEntity("World Union", "RS"));
			countries.add(country);
			region = countryRegionRepository.save(new CountryRegionEntity(country, "World region"));	
			countryRegions.add(region);
			city = cityRepository.save(new CityEntity(region, "World city", 33.3, 34.5));
			cities.add(city);
			cityA = new CityEntity();
			cityA.setCityName("Archived");
			cityA.setLongitude(34.3);
			cityA.setLatitude(35.5);
			cityA.setRegion(region);
			cityA.setStatus(-1);
			cityRepository.save(cityA);
			cities.add(cityA);
			cityI = new CityEntity();
			cityI.setCityName("Inactive");
			cityI.setLongitude(35.3);
			cityI.setLatitude(36.5);
			cityI.setRegion(region);
			cityI.setStatus(0);
			cityRepository.save(cityI);
			cities.add(cityI);
			users.add(userRepository.save(new UserEntity(city, "0642345678", "Jobster@mail.com", "About Jobster")));
			userAccounts.add(userAccountRepository.save(new UserAccountEntity(users.get(0), EUserRole.ROLE_ADMIN, "Test1234", "{bcrypt}$2a$10$FZjQbu7AqcSp0ns.GAxkbu0eKVUtNFTZNdVWwPOtBATLF0Bs9wtW2", users.get(0).getId())));
			
			dbInit = true;
			logger.info("DBsetUp ok1");
			
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

						logger.info("DBsetUp ok2");
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
	//@WithMockUser(username = "Test1234")
	public void cityServiceNotFound() throws Exception { 
		logger.info("cityServiceNotFound");
		mockMvc.perform(get("/jobster/cities/persons/readallpersons/")
			.header("Authorization", "Bearer " + token))
			.andExpect(status().isNotFound()); 
	}
	
	@Test 
	//@WithMockUser(username = "Test1234")
	public void getAllCities() throws Exception { 
		logger.info("getAllCities");
		mockMvc.perform(get("/jobster/cities/getAll")
				.header("Authorization", "Bearer " + token)) 
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[1].cityName", is(CityControllerTests.cities.get(1).getCityName())));
	}
	
	@Test 
	//@WithMockUser(username = "Test1234")
	public void getAllCitiesNotFound() throws Exception { 
		logger.info("readAllCitiesNotFound");
		for (CityEntity city : CityControllerTests.cities) {
			cityRepository.delete(city);
		}
		CityControllerTests.cities.clear();
		mockMvc.perform(get("/jobster/cities/getAll")
				.header("Authorization", "Bearer " + token))
			.andExpect(content().string("[]"));
	}
		
	@Test
	//@WithMockUser(username = "Test1234")
	public void getCityById() throws Exception { 
		logger.info("getCityById");
		mockMvc.perform(get("/jobster/cities/getById/" + CityControllerTests.cities.get(0).getId())
				.header("Authorization", "Bearer " + token))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(CityControllerTests.cities.get(0).getId().intValue()))); 
	}

	@Test 
	//@WithMockUser(username = "Test1234")
	public void getCityByIdNotExists() throws Exception { 
		logger.info("getCityByIdNotExists");
		mockMvc.perform(get("/jobster/cities/getById/" + (CityControllerTests.cities.get(0).getId()+5))
				.header("Authorization", "Bearer " + token))
			.andExpect(content().string(" City doesn`t exists."));
	}
	
	@Test 
	//@WithMockUser(username = "Test1234")
	public void getCityByIdWrongId() throws Exception { 
		logger.info("getCityByIdWrongId");
		String wrongId="test";
		mockMvc.perform(get("/jobster/cities/getById/" + wrongId)
				.header("Authorization", "Bearer " + token))
			.andExpect(status().isBadRequest());
	}

	@Test
	//@WithMockUser(username = "Test1234")
	public void getCityByName() throws Exception { 
		logger.info("getCityByName");
		mockMvc.perform(get("/jobster/cities/getByName/" + CityControllerTests.cities.get(0).getCityName())
				.header("Authorization", "Bearer " + token))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].cityName", is(CityControllerTests.cities.get(0).getCityName().toString()))); 
	}

	@Test 
	//@WithMockUser(username = "Test1234")
	public void getCityByNameNotExists() throws Exception { 
		logger.info("getCityByNameNotExists");
		String testName="Alibukerki";
		mockMvc.perform(get("/jobster/cities/getByName/" + testName)
				.header("Authorization", "Bearer " + token))
			.andExpect(content().string("No city with given name."));
	}
	
	@Test 
	//@WithMockUser(username = "Test1234")
	public void getCityByNameWrongName() throws Exception { 
		logger.info("getCityByNameWrongName");
		String wrongName="Alibukerki13";
		mockMvc.perform(get("/jobster/cities/getByName/" + wrongName)
				.header("Authorization", "Bearer " + token))
			.andExpect(status().isBadRequest());
	}
	
	@Test 
	//@WithMockUser(username = "Test1234")
	public void getAllCitiesArchived() throws Exception { 
		logger.info("getAllCitiesArchived");
		mockMvc.perform(get("/jobster/cities/archived")
				.header("Authorization", "Bearer " + token)) 
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].status", is(CityControllerTests.cities.get(1).getStatus().intValue())));
	}
	
	@Test 
	//@WithMockUser(username = "Test1234")
	public void getAllCitiesNotFoundArchived() throws Exception { 
		logger.info("getAllCitiesNotFoundArchived");
		for (CityEntity city : CityControllerTests.cities) {
			cityRepository.delete(city);
		}
		CityControllerTests.cities.clear();
		mockMvc.perform(get("/jobster/cities/archived")
				.header("Authorization", "Bearer " + token))
			.andExpect(content().string("[]"));
	}

	@Test 
	//@WithMockUser(username = "Test1234")
	public void getAllCitiesInactive() throws Exception { 
		logger.info("getAllCitiesInactive");
		mockMvc.perform(get("/jobster/cities/inactive")
				.header("Authorization", "Bearer " + token)) 
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].status", is(CityControllerTests.cities.get(2).getStatus().intValue())));

	}
	
	@Test 
	//@WithMockUser(username = "Test1234")
	public void getAllCitiesNotFoundInactive() throws Exception { 
		logger.info("getAllCitiesNotFoundInactive");
		for (CityEntity city : CityControllerTests.cities) {
			cityRepository.delete(city);
		}
		CityControllerTests.cities.clear();
		mockMvc.perform(get("/jobster/cities/inactive")
				.header("Authorization", "Bearer " + token))
			.andExpect(content().string("[]"));
	}
	
	@Test 
	//@WithMockUser(username = "Test1234")
	public void getAllCitiesActive() throws Exception { 
		logger.info("getAllCitiesActive");
		mockMvc.perform(get("/jobster/cities/active")
				.header("Authorization", "Bearer " + token)) 
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].status", is(CityControllerTests.cities.get(0).getStatus().intValue())));

	}
	
	@Test 
	//@WithMockUser(username = "Test1234")
	public void getAllCitiesNotFoundActive() throws Exception { 
		logger.info("getAllCitiesNotFoundActive");
		for (CityEntity city : CityControllerTests.cities) {
			cityRepository.delete(city);
		}
		CityControllerTests.cities.clear();
		mockMvc.perform(get("/jobster/cities/active")
				.header("Authorization", "Bearer " + token))
			.andExpect(content().string("[]"));
	}

	//--------------------ADD CITY-------------------------
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCity() throws Exception {
		logger.info("addNewCity");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol");
    	cityDTO.setCountry("Latifundija");
    	cityDTO.setIso2Code("LF");
    	cityDTO.setRegion("LFRegion");
    	cityDTO.setLongitude(43.1);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.cityName", is("LFCapitol")));
    	cityRepository.delete(cityRepository.getByCityName("LFCapitol"));
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCitySameCoordsDiffName() throws Exception {
		logger.info("addNewCitySameCoordsDiffName");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("City");
    	cityDTO.setCountry("World Union");
    	cityDTO.setIso2Code("RS");
    	cityDTO.setRegion("World region");
    	cityDTO.setLongitude(33.3);
    	cityDTO.setLatitude(34.5);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
    	.andDo(MockMvcResultHandlers.print())
        		.andExpect(status().isOk())
    			.andExpect(content().contentType(contentType)) 
    			.andExpect(jsonPath("$.cityName", is("City")));
    	cityRepository.delete(cityRepository.getByCityName("City"));
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityAlreadyExists() throws Exception {
		logger.info("addNewCityAlreadyExists");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("World city");
    	cityDTO.setCountry("World Union");
    	cityDTO.setIso2Code("RS");
    	cityDTO.setRegion("World region");
    	cityDTO.setLongitude(33.3);
    	cityDTO.setLatitude(34.5);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		.andExpect(content().string("City already exists."));
	}
	
	//------------------- ADD CITY REGION-----------------------------------
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityNullRegion() throws Exception {
		logger.info("addNewCityNullRegion");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol");
    	cityDTO.setCountry("Latifundija");
    	cityDTO.setIso2Code("LF");
    	cityDTO.setRegion(null);
    	cityDTO.setLongitude(43.1);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.cityName", is("LFCapitol")));
    	cityRepository.delete(cityRepository.getByCityName("LFCapitol"));
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityMarginalRegion() throws Exception {
		logger.info("addNewCityMarginalRegion");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol");
    	cityDTO.setCountry("Latifundija");
    	cityDTO.setIso2Code("LF");
    	cityDTO.setRegion("RC");
    	cityDTO.setLongitude(43.1);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.cityName", is("LFCapitol")));
    	cityRepository.delete(cityRepository.getByCityName("LFCapitol"));
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityEmptyRegion() throws Exception {
		logger.info("addNewCityEmptyRegion");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol");
    	cityDTO.setCountry("Latifundija");
    	cityDTO.setIso2Code("LF");
    	cityDTO.setRegion("");
    	cityDTO.setLongitude(43.1);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		.andExpect(status().isBadRequest());
	}


	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityNoRegion() throws Exception {
		logger.info("addNewCityNoRegion");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol");
    	cityDTO.setCountry("Latifundija");
    	cityDTO.setIso2Code("LF");
    	cityDTO.setLongitude(43.1);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		.andExpect(status().isOk())
    		.andExpect(content().contentType(contentType)) 
    		.andExpect(jsonPath("$.cityName", is("LFCapitol")));
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityWrongRegionNumbers() throws Exception {
		logger.info("addNewCityWrongRegionNumbers");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol");
    	cityDTO.setCountry("Latifundija");
    	cityDTO.setIso2Code("LF");
    	cityDTO.setRegion("region12");
    	cityDTO.setLongitude(43.1);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityWrongRegionOtherChars() throws Exception {
		logger.info("addNewCityWrongRegionOtherChars");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol");
    	cityDTO.setCountry("Latifundija");
    	cityDTO.setIso2Code("LF");
    	cityDTO.setRegion("region%%");
    	cityDTO.setLongitude(43.1);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityWrongRegionLeadingSpace() throws Exception {
		logger.info("addNewCityWrongRegionLeadingSpace");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol");
    	cityDTO.setCountry("Latifundija");
    	cityDTO.setIso2Code("LF");
    	cityDTO.setRegion(" region");
    	cityDTO.setLongitude(43.1);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityWrongRegionMultipleSpaces() throws Exception {
		logger.info("addNewCityWrongRegionMultipleSpaces");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol");
    	cityDTO.setCountry("Latifundija");
    	cityDTO.setIso2Code("LF");
    	cityDTO.setRegion("reg  ion");
    	cityDTO.setLongitude(43.1);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityWrongRegionTrailingSpace() throws Exception {
		logger.info("addNewCityWrongRegionTrailingSpace");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol");
    	cityDTO.setCountry("Latifundija");
    	cityDTO.setIso2Code("LF");
    	cityDTO.setRegion("region ");
    	cityDTO.setLongitude(43.1);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityWrongRegionLeadingWhitespace() throws Exception {
		logger.info("addNewCityWrongRegionLeadingWhitespace");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol");
    	cityDTO.setCountry("Latifundija");
    	cityDTO.setIso2Code("LF");
    	cityDTO.setRegion("	region");
    	cityDTO.setLongitude(43.1);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityWrongRegionMiddleWhitespace() throws Exception {
		logger.info("addNewCityWrongRegionMiddleWhitespace");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol");
    	cityDTO.setCountry("Latifundija");
    	cityDTO.setIso2Code("LF");
    	cityDTO.setRegion("reg	ion");
    	cityDTO.setLongitude(43.1);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityWrongRegionTrailingWhitespace() throws Exception {
		logger.info("addNewCityWrongRegionTrailingWhitespace");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol");
    	cityDTO.setCountry("Latifundija");
    	cityDTO.setIso2Code("LF");
    	cityDTO.setRegion("region	");
    	cityDTO.setLongitude(43.1);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityWrongRegionTooShort() throws Exception {
		logger.info("addNewCityWrongRegionTooShort");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol");
    	cityDTO.setCountry("Latifundija");
    	cityDTO.setIso2Code("LF");
    	cityDTO.setRegion("K");
    	cityDTO.setLongitude(43.1);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		.andExpect(status().isBadRequest());
	}
	
	//-----------------------------ADD CITY NAME----------------------------------------
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityNoName() throws Exception {
		logger.info("addNewCityNoName");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCountry("Latifundija");
    	cityDTO.setIso2Code("LF");
    	cityDTO.setRegion(null);
    	cityDTO.setLongitude(43.1);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityWrongNameNumbers() throws Exception {
		logger.info("addNewCityWrongNameNumbers");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol22");
    	cityDTO.setCountry("Latifundija");
    	cityDTO.setIso2Code("LF");
    	cityDTO.setRegion(null);
    	cityDTO.setLongitude(43.1);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityWrongNameOtherChars() throws Exception {
		logger.info("addNewCityWrongNameOtherChars");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol@");
    	cityDTO.setCountry("Latifundija");
    	cityDTO.setIso2Code("LF");
    	cityDTO.setRegion(null);
    	cityDTO.setLongitude(43.1);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityWrongNameLeadingSpace() throws Exception {
		logger.info("addNewCityWrongNameLeadingSpace");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName(" LFCapitol");
    	cityDTO.setCountry("Latifundija");
    	cityDTO.setIso2Code("LF");
    	cityDTO.setRegion(null);
    	cityDTO.setLongitude(43.1);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityWrongNameTrailingSpace() throws Exception {
		logger.info("addNewCityWrongNameTrailingSpace");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol ");
    	cityDTO.setCountry("Latifundija");
    	cityDTO.setIso2Code("LF");
    	cityDTO.setRegion(null);
    	cityDTO.setLongitude(43.1);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityWrongNameMultipleSpaces() throws Exception {
		logger.info("addNewCityWrongNameMultipleSpaces");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCap  itol");
    	cityDTO.setCountry("Latifundija");
    	cityDTO.setIso2Code("LF");
    	cityDTO.setRegion(null);
    	cityDTO.setLongitude(43.1);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityWrongNameLeadingWhitespace() throws Exception {
		logger.info("addNewCityWrongNameLeadingWhitespace");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("	LFCapitol");
    	cityDTO.setCountry("Latifundija");
    	cityDTO.setIso2Code("LF");
    	cityDTO.setRegion(null);
    	cityDTO.setLongitude(43.1);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityWrongNameTrailingWhitespace() throws Exception {
		logger.info("addNewCityWrongNameTrailingWhitespace");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol	");
    	cityDTO.setCountry("Latifundija");
    	cityDTO.setIso2Code("LF");
    	cityDTO.setRegion(null);
    	cityDTO.setLongitude(43.1);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityWrongNameMiddleWhitespace() throws Exception {
		logger.info("addNewCityWrongNameMiddleWhitespace");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapi	tol	");
    	cityDTO.setCountry("Latifundija");
    	cityDTO.setIso2Code("LF");
    	cityDTO.setRegion(null);
    	cityDTO.setLongitude(43.1);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		.andExpect(status().isBadRequest());
	}
	

	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityWrongNameTooShort() throws Exception {
		logger.info("addNewCityWrongNameTooShort");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("L");
    	cityDTO.setCountry("Latifundija");
    	cityDTO.setIso2Code("LF");
    	cityDTO.setRegion(null);
    	cityDTO.setLongitude(43.1);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		.andExpect(status().isBadRequest());
	}

	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityMarginalName() throws Exception {
		logger.info("addNewCityMarginalName");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LC");
    	cityDTO.setCountry("Latifundija");
    	cityDTO.setIso2Code("LF");
    	cityDTO.setRegion(null);
    	cityDTO.setLongitude(43.1);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isOk())
    			.andExpect(content().contentType(contentType)) 
    			.andExpect(jsonPath("$.cityName", is("LC")));
        	cityRepository.delete(cityRepository.getByCityName("LC"));
	}

	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityEmptyName() throws Exception {
		logger.info("addNewCityEmptyName");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("");
    	cityDTO.setCountry("Latifundija");
    	cityDTO.setIso2Code("LF");
    	cityDTO.setRegion(null);
    	cityDTO.setLongitude(43.1);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		//.andDo(MockMvcResultHandlers.print())
    		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityNullName() throws Exception {
		logger.info("addNewCityNullName");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName(null);
    	cityDTO.setCountry("Latifundija");
    	cityDTO.setIso2Code("LF");
    	cityDTO.setRegion("LFRegion");
    	cityDTO.setLongitude(43.1);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		//.andDo(MockMvcResultHandlers.print())
    		.andExpect(status().isBadRequest());
	}
	
	//-------------------------------- ADD CITY COUNTRY -----------------------------
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityEmptyCountry() throws Exception {
		logger.info("addNewCityEmptyCountry");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol");
    	cityDTO.setCountry("");
    	cityDTO.setIso2Code("LF");
    	cityDTO.setRegion("LFRegion");
    	cityDTO.setLongitude(43.1);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityMarginalCountry() throws Exception {
		logger.info("addNewCityMarginalCountry");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol");
    	cityDTO.setCountry("Co");
    	cityDTO.setIso2Code("LF");
    	cityDTO.setRegion("LFRegion");
    	cityDTO.setLongitude(43.1);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isOk())
        		.andExpect(status().isOk())
    			.andExpect(content().contentType(contentType)) 
    			.andExpect(jsonPath("$.region.country.countryName", is("Co")));
        	cityRepository.delete(cityRepository.getByCityName("LFCapitol"));
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityWrongCountryNumbers() throws Exception {
		logger.info("addNewCityWrongCountryNumbers");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol");
    	cityDTO.setCountry("Latifundija44");
    	cityDTO.setIso2Code("LF");
    	cityDTO.setRegion("LFRegion");
    	cityDTO.setLongitude(43.1);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityWrongCountryOtherChars() throws Exception {
		logger.info("addNewCityWrongCountryOtherChars");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol");
    	cityDTO.setCountry("Latifundija**");
    	cityDTO.setIso2Code("LF");
    	cityDTO.setRegion("LFRegion");
    	cityDTO.setLongitude(43.1);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityWrongCountryTooShort() throws Exception {
		logger.info("addNewCityWrongCountryTooShort");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol");
    	cityDTO.setCountry("L");
    	cityDTO.setIso2Code("LF");
    	cityDTO.setRegion("LFRegion");
    	cityDTO.setLongitude(43.1);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityNullCountry() throws Exception {
		logger.info("addNewCityNullCountry");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol");
    	cityDTO.setCountry(null);
    	cityDTO.setIso2Code("LF");
    	cityDTO.setRegion("LFRegion");
    	cityDTO.setLongitude(43.1);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isBadRequest());
	}
	
	
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityNoCountry() throws Exception {
		logger.info("addNewCityNoCountry");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol");
    	cityDTO.setIso2Code("LF");
    	cityDTO.setRegion("LFRegion");
    	cityDTO.setLongitude(43.1);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityWrongCountryLeadingSpace() throws Exception {
		logger.info("addNewCityWrongCountryLeadingSpace");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol");
    	cityDTO.setCountry(" Latifundija");
    	cityDTO.setIso2Code("LF");
    	cityDTO.setRegion("LFRegion");
    	cityDTO.setLongitude(43.1);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isBadRequest());
	}
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityWrongCountryTrailingSpace() throws Exception {
		logger.info("addNewCityWrongCountryTrailingSpace");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol");
    	cityDTO.setCountry("Latifundija ");
    	cityDTO.setIso2Code("LF");
    	cityDTO.setRegion("LFRegion");
    	cityDTO.setLongitude(43.1);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityWrongCountryMultipleSpaces() throws Exception {
		logger.info("addNewCityWrongCountryMultipleSpaces");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol");
    	cityDTO.setCountry("Latifu  ndija");
    	cityDTO.setIso2Code("LF");
    	cityDTO.setRegion("LFRegion");
    	cityDTO.setLongitude(43.1);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityWrongCountryLeadingWhitespace() throws Exception {
		logger.info("addNewCityWrongCountryLeadingWhitespace");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol");
    	cityDTO.setCountry("	Latifundija");
    	cityDTO.setIso2Code("LF");
    	cityDTO.setRegion("LFRegion");
    	cityDTO.setLongitude(43.1);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isBadRequest());
	}
	

	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityWrongCountryTrailingWhitespace() throws Exception {
		logger.info("addNewCityWrongCountryTrailingWhitespace");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol");
    	cityDTO.setCountry("Latifundija	");
    	cityDTO.setIso2Code("LF");
    	cityDTO.setRegion("LFRegion");
    	cityDTO.setLongitude(43.1);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityWrongCountryMiddleWhitespace() throws Exception {
		logger.info("addNewCityWrongCountryMiddleWhitespace");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol");
    	cityDTO.setCountry("Latifun	dija");
    	cityDTO.setIso2Code("LF");
    	cityDTO.setRegion("LFRegion");
    	cityDTO.setLongitude(43.1);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isBadRequest());
	}
	
	//------------------------------ADD CITY ISOCODE---------------------------------
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityEmptyIso2() throws Exception {
		logger.info("addNewCityEmptyIso2");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol");
    	cityDTO.setCountry("Latifundija");
    	cityDTO.setIso2Code("");
    	cityDTO.setRegion("LFRegion");
    	cityDTO.setLongitude(43.1);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityWrongIso2Numbers() throws Exception {
		logger.info("addNewCityWrongIso2Numbers");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol");
    	cityDTO.setCountry("Latifundija");
    	cityDTO.setIso2Code("L2");
    	cityDTO.setRegion("LFRegion");
    	cityDTO.setLongitude(43.1);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityWrongIso2OtherChars() throws Exception {
		logger.info("addNewCityWrongIso2OtherChars");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol");
    	cityDTO.setCountry("Latifundija");
    	cityDTO.setIso2Code("L%");
    	cityDTO.setRegion("LFRegion");
    	cityDTO.setLongitude(43.1);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityWrongIso2TooShort() throws Exception {
		logger.info("addNewCityWrongIso2TooShort");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol");
    	cityDTO.setCountry("Latifundija");
    	cityDTO.setIso2Code("L");
    	cityDTO.setRegion("LFRegion");
    	cityDTO.setLongitude(43.1);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityWrongIso2TooLong() throws Exception {
		logger.info("addNewCityWrongIso2TooLong");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol");
    	cityDTO.setCountry("Latifundija");
    	cityDTO.setIso2Code("LAT");
    	cityDTO.setRegion("LFRegion");
    	cityDTO.setLongitude(43.1);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityWrongIso2LeadingSpace() throws Exception {
		logger.info("addNewCityWrongIso2LeadingSpace");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol");
    	cityDTO.setCountry("Latifundija");
    	cityDTO.setIso2Code(" L");
    	cityDTO.setRegion("LFRegion");
    	cityDTO.setLongitude(43.1);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityWrongIso2TrailingSpace() throws Exception {
		logger.info("addNewCityWrongIso2TrailingSpace");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol");
    	cityDTO.setCountry("Latifundija");
    	cityDTO.setIso2Code("L ");
    	cityDTO.setRegion("LFRegion");
    	cityDTO.setLongitude(43.1);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityWrongIso2MiddleSpace() throws Exception {
		logger.info("addNewCityWrongIso2MiddleSpace");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol");
    	cityDTO.setCountry("Latifundija");
    	cityDTO.setIso2Code("L A");
    	cityDTO.setRegion("LFRegion");
    	cityDTO.setLongitude(43.1);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityWrongIso2MUltipleSpaces() throws Exception {
		logger.info("addNewCityWrongIso2MultipleSpaces");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol");
    	cityDTO.setCountry("Latifundija");
    	cityDTO.setIso2Code("L  A");
    	cityDTO.setRegion("LFRegion");
    	cityDTO.setLongitude(43.1);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityWrongIso2LeadingWhitespace() throws Exception {
		logger.info("addNewCityWrongIso2LeadingWhitespace");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol");
    	cityDTO.setCountry("Latifundija");
    	cityDTO.setIso2Code("	A");
    	cityDTO.setRegion("LFRegion");
    	cityDTO.setLongitude(43.1);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityWrongIso2TrailingWhitespace() throws Exception {
		logger.info("addNewCityWrongIso2TrailingWhitespace");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol");
    	cityDTO.setCountry("Latifundija");
    	cityDTO.setIso2Code("A	");
    	cityDTO.setRegion("LFRegion");
    	cityDTO.setLongitude(43.1);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityWrongIso2MiddleWhitespace() throws Exception {
		logger.info("addNewCityWrongIso2MiddleWhitespace");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol");
    	cityDTO.setCountry("Latifundija");
    	cityDTO.setIso2Code("A	L");
    	cityDTO.setRegion("LFRegion");
    	cityDTO.setLongitude(43.1);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityNullIso2() throws Exception {
		logger.info("addNewCityNullIso2");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol");
    	cityDTO.setCountry("Latifundija");
    	cityDTO.setIso2Code(null);
    	cityDTO.setRegion("LFRegion");
    	cityDTO.setLongitude(43.1);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityNoIso2() throws Exception {
		logger.info("addNewCityNoIso2");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol");
    	cityDTO.setCountry("Latifundija");
    	cityDTO.setRegion("LFRegion");
    	cityDTO.setLongitude(43.1);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isBadRequest());
	}
	//-------------------------ADD CITY LONGITUDE-----------------------------------------
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityWrongLongitudeMinus() throws Exception {
		logger.info("addNewCityWrongLongitudeMinus");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol");
    	cityDTO.setCountry("Latifundija");
    	cityDTO.setIso2Code("LF");
    	cityDTO.setRegion("LFRegion");
    	cityDTO.setLongitude(-200.3);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityWrongLongitudePlus() throws Exception {
		logger.info("addNewCityWrongLongitudePlus");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol");
    	cityDTO.setCountry("Latifundija");
    	cityDTO.setIso2Code("LF");
    	cityDTO.setRegion("LFRegion");
    	cityDTO.setLongitude(200.3);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityMarginalLongitudePlus() throws Exception {
		logger.info("addNewCityMarginalLongitudePlus");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol");
    	cityDTO.setCountry("Latifundija");
    	cityDTO.setIso2Code("LF");
    	cityDTO.setRegion("LFRegion");
    	cityDTO.setLongitude(180.0);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isOk())
    			.andExpect(content().contentType(contentType)) 
    			.andExpect(jsonPath("$.cityName", is("LFCapitol")));
        	cityRepository.delete(cityRepository.getByCityName("LFCapitol"));
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityMarginalLongitudeMinus() throws Exception {
		logger.info("addNewCityMarginalLongitudePlus");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol");
    	cityDTO.setCountry("Latifundija");
    	cityDTO.setIso2Code("LF");
    	cityDTO.setRegion("LFRegion");
    	cityDTO.setLongitude(-180.0);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isOk())
    			.andExpect(content().contentType(contentType)) 
    			.andExpect(jsonPath("$.cityName", is("LFCapitol")));
        	cityRepository.delete(cityRepository.getByCityName("LFCapitol"));
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityNullLongitude() throws Exception {
		logger.info("addNewCityNullLongitude");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol");
    	cityDTO.setCountry("Latifundija");
    	cityDTO.setIso2Code("LF");
    	cityDTO.setRegion("LFRegion");
    	cityDTO.setLongitude(null);
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityNoLongitude() throws Exception {
		logger.info("addNewCityNoLongitude");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol");
    	cityDTO.setCountry("Latifundija");
    	cityDTO.setRegion("LFRegion");
    	cityDTO.setLatitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isBadRequest());
	}
	//------------------------------------------------------------------

	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityWrongLatitudeMinus() throws Exception {
		logger.info("addNewCityWrongLatitudeMinus");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol");
    	cityDTO.setCountry("Latifundija");
    	cityDTO.setIso2Code("LF");
    	cityDTO.setRegion("LFRegion");
    	cityDTO.setLatitude(-200.3);
    	cityDTO.setLongitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityWrongLatitudePlus() throws Exception {
		logger.info("addNewCityWrongLatitudePlus");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol");
    	cityDTO.setCountry("Latifundija");
    	cityDTO.setIso2Code("LF");
    	cityDTO.setRegion("LFRegion");
    	cityDTO.setLatitude(200.3);
    	cityDTO.setLongitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityMarginalLatitudePlus() throws Exception {
		logger.info("addNewCityMarginalLatitudePlus");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol");
    	cityDTO.setCountry("Latifundija");
    	cityDTO.setIso2Code("LF");
    	cityDTO.setRegion("LFRegion");
    	cityDTO.setLatitude(90.0);
    	cityDTO.setLongitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isOk())
    			.andExpect(content().contentType(contentType)) 
    			.andExpect(jsonPath("$.cityName", is("LFCapitol")));
        	cityRepository.delete(cityRepository.getByCityName("LFCapitol"));
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityMarginalLatitudeMinus() throws Exception {
		logger.info("addNewCityMarginalLatitudePlus");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol");
    	cityDTO.setCountry("Latifundija");
    	cityDTO.setIso2Code("LF");
    	cityDTO.setRegion("LFRegion");
    	cityDTO.setLatitude(-90.0);
    	cityDTO.setLongitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isOk())
    			.andExpect(content().contentType(contentType)) 
    			.andExpect(jsonPath("$.cityName", is("LFCapitol")));
        	cityRepository.delete(cityRepository.getByCityName("LFCapitol"));
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityNullLatitude() throws Exception {
		logger.info("addNewCityNullLatitude");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol");
    	cityDTO.setCountry("Latifundija");
    	cityDTO.setIso2Code("LF");
    	cityDTO.setRegion("LFRegion");
    	cityDTO.setLatitude(null);
    	cityDTO.setLongitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityNoLatitude() throws Exception {
		logger.info("addNewCityNoLatitude");
		POSTCityDTO cityDTO = new POSTCityDTO();
    	cityDTO.setCityName("LFCapitol");
    	cityDTO.setCountry("Latifundija");
    	cityDTO.setRegion("LFRegion");
    	cityDTO.setLongitude(43.1);
		Gson gson = new Gson();
    	String json = gson.toJson(cityDTO);
    	mockMvc.perform(post("/jobster/cities/addNewCity")
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isBadRequest());
	}
	//----------------------------MODIFY CITY NAME--------------------------------------
	
	@Test //@WithMockUser(username = "Test1234")
	public void modifyCity() throws Exception {
		logger.info("modifyCity");
		POSTCityDTO city = new POSTCityDTO("NewName", 47.0, 45.0, region.getCountryRegionName(), country.getCountryName(), country.getIso2Code());
		Gson gson = new Gson();
    	String json = gson.toJson(city);
    	mockMvc.perform(put("/jobster/cities/modify/" + (CityControllerTests.cities.get(0).getId()))
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isOk())
    			.andExpect(content().contentType(contentType)) 
    			.andExpect(jsonPath("$.cityName", is("NewName")))
    			.andExpect(jsonPath("$.region.countryRegionName", is("World region")));
    	Integer Id = CityControllerTests.cities.get(0).getId();
		CityControllerTests.cities.remove(0);
		CityControllerTests.cities.add(0,cityRepository.findByIdAndStatusLike(Id, 1));
	}

	
	@Test //@WithMockUser(username = "Test1234")
	public void modifyCityMarginalName() throws Exception {
		logger.info("modifyCityMarginalName");
		POSTCityDTO city = new POSTCityDTO("NN", 47.0, 45.0, "NewRegion", "OldCountry", "OC");
		Gson gson = new Gson();
    	String json = gson.toJson(city);
    	mockMvc.perform(put("/jobster/cities/modify/" + (CityControllerTests.cities.get(0).getId()))
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isOk())
    			.andExpect(content().contentType(contentType)) 
    			.andExpect(jsonPath("$.cityName", is("NN")));
    	Integer Id = CityControllerTests.cities.get(0).getId();
		CityControllerTests.cities.remove(0);
		CityControllerTests.cities.add(0,cityRepository.findByIdAndStatusLike(Id, 1));
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void modifyCityNullName() throws Exception {
		logger.info("modifyCityNullName");
		POSTCityDTO city = new POSTCityDTO(null, 47.0, 45.0, "NewRegion", "OldCountry", "OC");
		Gson gson = new Gson();
    	String json = gson.toJson(city);
    	mockMvc.perform(put("/jobster/cities/modify/" + (CityControllerTests.cities.get(1).getId()))
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void modifyCityWrongNameNumbers() throws Exception {
		logger.info("modifyCityWrongNameNumbers");
		POSTCityDTO city = new POSTCityDTO("city23", 47.0, 45.0, "NewRegion", "OldCountry", "OC");
		Gson gson = new Gson();
    	String json = gson.toJson(city);
    	mockMvc.perform(put("/jobster/cities/modify/" + (CityControllerTests.cities.get(1).getId()))
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void modifyCityWrongNameOtherChars() throws Exception {
		logger.info("modifyCityWrongNameOtherChars");
		POSTCityDTO city = new POSTCityDTO("ci%%ty", 47.0, 45.0, "NewRegion", "OldCountry", "OC");
		Gson gson = new Gson();
    	String json = gson.toJson(city);
    	mockMvc.perform(put("/jobster/cities/modify/" + (CityControllerTests.cities.get(1).getId()))
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void modifyCityWrongNameTooShort() throws Exception {
		logger.info("modifyCityWrongNameTooShort");
		POSTCityDTO city = new POSTCityDTO("c", 47.0, 45.0, "NewRegion", "OldCountry", "OC");
		Gson gson = new Gson();
    	String json = gson.toJson(city);
    	mockMvc.perform(put("/jobster/cities/modify/" + (CityControllerTests.cities.get(1).getId()))
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void modifyCityWrongNameLeadingSpace() throws Exception {
		logger.info("modifyCityWrongNameLeadingSpace");
		POSTCityDTO city = new POSTCityDTO(" city", 47.0, 45.0, "NewRegion", "OldCountry", "OC");
		Gson gson = new Gson();
    	String json = gson.toJson(city);
    	mockMvc.perform(put("/jobster/cities/modify/" + (CityControllerTests.cities.get(1).getId()))
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void modifyCityWrongNameTrailingSpace() throws Exception {
		logger.info("modifyCityWrongNameTrailingSpace");
		POSTCityDTO city = new POSTCityDTO("city ", 47.0, 45.0, "NewRegion", "OldCountry", "OC");
		Gson gson = new Gson();
    	String json = gson.toJson(city);
    	mockMvc.perform(put("/jobster/cities/modify/" + (CityControllerTests.cities.get(1).getId()))
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void modifyCityNameMiddleSpace() throws Exception {
		logger.info("modifyCityNameMiddleSpace");
		POSTCityDTO city = new POSTCityDTO("ci ty", 47.0, 45.0, "NewRegion", "OldCountry", "OC");
		Gson gson = new Gson();
    	String json = gson.toJson(city);
    	mockMvc.perform(put("/jobster/cities/modify/" + (CityControllerTests.cities.get(0).getId()))
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType(contentType)) 
    			.andExpect(jsonPath("$.cityName", is("ci ty")));
    	Integer Id = CityControllerTests.cities.get(0).getId();
    	CityControllerTests.cities.remove(0);
    	CityControllerTests.cities.add(0,cityRepository.findByIdAndStatusLike(Id, 1));
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void modifyCityWrongNameMultipleSpaces() throws Exception {
		logger.info("modifyCityWrongNameMultipleSpaces");
		POSTCityDTO city = new POSTCityDTO("ci  ty ", 47.0, 45.0, "NewRegion", "OldCountry", "OC");
		Gson gson = new Gson();
    	String json = gson.toJson(city);
    	mockMvc.perform(put("/jobster/cities/modify/" + (CityControllerTests.cities.get(1).getId()))
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void modifyCityWrongNameLeadingWhitespace() throws Exception {
		logger.info("modifyCityWrongNameLeadingWhitespace");
		POSTCityDTO city = new POSTCityDTO("	city", 47.0, 45.0, "NewRegion", "OldCountry", "OC");
		Gson gson = new Gson();
    	String json = gson.toJson(city);
    	mockMvc.perform(put("/jobster/cities/modify/" + (CityControllerTests.cities.get(1).getId()))
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void modifyCityWrongNameMiddleWhitespace() throws Exception {
		logger.info("modifyCityWrongNameMiddleWhitespace");
		POSTCityDTO city = new POSTCityDTO("ci	ty", 47.0, 45.0, "NewRegion", "OldCountry", "OC");
		Gson gson = new Gson();
    	String json = gson.toJson(city);
    	mockMvc.perform(put("/jobster/cities/modify/" + (CityControllerTests.cities.get(1).getId()))
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void modifyCityWrongNameTrailingWhitespace() throws Exception {
		logger.info("modifyCityWrongNameTrailingWhitespace");
		POSTCityDTO city = new POSTCityDTO("city	", 47.0, 45.0, "NewRegion", "OldCountry", "OC");
		Gson gson = new Gson();
    	String json = gson.toJson(city);
    	mockMvc.perform(put("/jobster/cities/modify/" + (CityControllerTests.cities.get(1).getId()))
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void modifyCityEmptyName() throws Exception {
		logger.info("modifyCityEmptyName");
		POSTCityDTO city = new POSTCityDTO("", 47.0, 45.0, "NewRegion", "OldCountry", "OC");
		Gson gson = new Gson();
    	String json = gson.toJson(city);
    	mockMvc.perform(put("/jobster/cities/modify/" + (CityControllerTests.cities.get(1).getId()))
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isBadRequest());
	}
	
	//-------------------------------MODIFY CITY LONGITUDE------------------------------------------------

	@Test //@WithMockUser(username = "Test1234")
	public void modifyCityMarginalLongitudePlus() throws Exception {
		logger.info("modifyCityMarginalLongitudePlus");
		POSTCityDTO city = new POSTCityDTO("NN", 180.0, 45.0, "NewRegion", "OldCountry", "OC");
		Gson gson = new Gson();
    	String json = gson.toJson(city);
    	mockMvc.perform(put("/jobster/cities/modify/" + (CityControllerTests.cities.get(0).getId()))
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isOk())
    			.andExpect(content().contentType(contentType)) 
    			.andExpect(jsonPath("$.cityName", is("NN")))
    			.andExpect(jsonPath("$.longitude", is(180.0)));
    	Integer Id = CityControllerTests.cities.get(0).getId();
		CityControllerTests.cities.remove(0);
		CityControllerTests.cities.add(0,cityRepository.findByIdAndStatusLike(Id, 1));
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void modifyCityMarginalLongitudeMinus() throws Exception {
		logger.info("modifyCityMarginalLongitudeMinus");
		POSTCityDTO city = new POSTCityDTO("NN", -180.0, 45.0, "NewRegion", "OldCountry", "OC");
		Gson gson = new Gson();
    	String json = gson.toJson(city);
    	mockMvc.perform(put("/jobster/cities/modify/" + (CityControllerTests.cities.get(0).getId()))
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isOk())
    			.andExpect(content().contentType(contentType)) 
    			.andExpect(jsonPath("$.cityName", is("NN")))
    			.andExpect(jsonPath("$.longitude", is(-180.0)));
    	Integer Id = CityControllerTests.cities.get(0).getId();
		CityControllerTests.cities.remove(0);
		CityControllerTests.cities.add(0,cityRepository.findByIdAndStatusLike(Id, 1));
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void modifyCityNullLongitude() throws Exception {
		logger.info("modifyCityNullLongitude");
		POSTCityDTO city = new POSTCityDTO("NewName", null, 45.0, "NewRegion", "OldCountry", "OC");
		Gson gson = new Gson();
    	String json = gson.toJson(city);
    	mockMvc.perform(put("/jobster/cities/modify/" + (CityControllerTests.cities.get(1).getId()))
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void modifyCityWrongLongitudePlus() throws Exception {
		logger.info("modifyCityWrongLongitudePlus");
		POSTCityDTO city = new POSTCityDTO("city23", 252.0, 45.0, "NewRegion", "OldCountry", "OC");
		Gson gson = new Gson();
    	String json = gson.toJson(city);
    	mockMvc.perform(put("/jobster/cities/modify/" + (CityControllerTests.cities.get(1).getId()))
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isBadRequest());
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void modifyCityWrongLongitudeMinus() throws Exception {
		logger.info("modifyCityWrongLongitudeMinus");
		POSTCityDTO city = new POSTCityDTO("city23", -252.0, 45.0, "NewRegion", "OldCountry", "OC");
		Gson gson = new Gson();
    	String json = gson.toJson(city);
    	mockMvc.perform(put("/jobster/cities/modify/" + (CityControllerTests.cities.get(1).getId()))
    			.header("Authorization", "Bearer " + token)
    			.contentType(MediaType.APPLICATION_JSON).content(json))
        		.andExpect(status().isBadRequest());
	}
	

	
	//-------------------------------------------------------------------------------

		@Test //@WithMockUser(username = "Test1234")
		public void modifyCityMarginalLatitudePlus() throws Exception {
			logger.info("modifyCityMarginalLongitudePlus");
			POSTCityDTO city = new POSTCityDTO("NN", 180.0, 90.0, "NewRegion", "OldCountry", "OC");
			Gson gson = new Gson();
	    	String json = gson.toJson(city);
	    	mockMvc.perform(put("/jobster/cities/modify/" + (CityControllerTests.cities.get(0).getId()))
	    			.header("Authorization", "Bearer " + token)
	    			.contentType(MediaType.APPLICATION_JSON).content(json))
	        		.andExpect(status().isOk())
	    			.andExpect(content().contentType(contentType)) 
	    			.andExpect(jsonPath("$.cityName", is("NN")))
	    			.andExpect(jsonPath("$.latitude", is(90.0)));
	    	Integer Id = CityControllerTests.cities.get(0).getId();
			CityControllerTests.cities.remove(0);
			CityControllerTests.cities.add(0,cityRepository.findByIdAndStatusLike(Id, 1));
		}
		
		@Test //@WithMockUser(username = "Test1234")
		public void modifyCityMarginalLatitudeMinus() throws Exception {
			logger.info("modifyCityMarginalLatitudeMinus");
			POSTCityDTO city = new POSTCityDTO("NN", -180.0, -90.0, "NewRegion", "OldCountry", "OC");
			Gson gson = new Gson();
	    	String json = gson.toJson(city);
	    	mockMvc.perform(put("/jobster/cities/modify/" + (CityControllerTests.cities.get(0).getId()))
	    			.header("Authorization", "Bearer " + token)
	    			.contentType(MediaType.APPLICATION_JSON).content(json))
	        		.andExpect(status().isOk())
	    			.andExpect(content().contentType(contentType)) 
	    			.andExpect(jsonPath("$.cityName", is("NN")))
	    			.andExpect(jsonPath("$.latitude", is(-90.0)));
	    	Integer Id = CityControllerTests.cities.get(0).getId();
			CityControllerTests.cities.remove(0);
			CityControllerTests.cities.add(0,cityRepository.findByIdAndStatusLike(Id, 1));
		}
		
		@Test //@WithMockUser(username = "Test1234")
		public void modifyCityNullLatitude() throws Exception {
			logger.info("modifyCityNullLatitude");
			POSTCityDTO city = new POSTCityDTO("NewName", 45.0, null, "NewRegion", "OldCountry", "OC");
			Gson gson = new Gson();
	    	String json = gson.toJson(city);
	    	mockMvc.perform(put("/jobster/cities/modify/" + (CityControllerTests.cities.get(1).getId()))
	    			.header("Authorization", "Bearer " + token)
	    			.contentType(MediaType.APPLICATION_JSON).content(json))
	        		.andExpect(status().isBadRequest());
		}
		
		@Test //@WithMockUser(username = "Test1234")
		public void modifyCityWrongLatitudePlus() throws Exception {
			logger.info("modifyCityWrongLatitudePlus");
			POSTCityDTO city = new POSTCityDTO("city23", 45.0, 245.0, "NewRegion", "OldCountry", "OC");
			Gson gson = new Gson();
	    	String json = gson.toJson(city);
	    	mockMvc.perform(put("/jobster/cities/modify/" + (CityControllerTests.cities.get(1).getId()))
	    			.header("Authorization", "Bearer " + token)
	    			.contentType(MediaType.APPLICATION_JSON).content(json))
	        		.andExpect(status().isBadRequest());
		}
		
		@Test //@WithMockUser(username = "Test1234")
		public void modifyCityWrongLatitudeMinus() throws Exception {
			logger.info("modifyCityWrongLatitudeMinus");
			POSTCityDTO city = new POSTCityDTO("city23", 45.0, -245.0, "NewRegion", "OldCountry", "OC");
			Gson gson = new Gson();
	    	String json = gson.toJson(city);
	    	mockMvc.perform(put("/jobster/cities/modify/" + (CityControllerTests.cities.get(1).getId()))
	    			.header("Authorization", "Bearer " + token)
	    			.contentType(MediaType.APPLICATION_JSON).content(json))
	        		.andExpect(status().isBadRequest());
		}
		
		//-------------------------------------------------------------------------------
		
		@Test //@WithMockUser(username = "Test1234")
		public void modifyCityMarginalRegionName() throws Exception {
			logger.info("modifyCityMarginalRegionName");
			POSTCityDTO city = new POSTCityDTO("World city", 47.0, 45.0, "RC", "World Union", "RS");
			Gson gson = new Gson();
	    	String json = gson.toJson(city);
	    	mockMvc.perform(put("/jobster/cities/modify/" + (CityControllerTests.cities.get(0).getId()))
	    			.header("Authorization", "Bearer " + token)
	    			.contentType(MediaType.APPLICATION_JSON).content(json))
	        		.andExpect(status().isOk())
	    			.andExpect(content().contentType(contentType)) 
	    			.andExpect(jsonPath("$.cityName", is("World city")))
	    			.andExpect(jsonPath("$.region.countryRegionName", is("RC")));
	    	
	    	Integer Id = CityControllerTests.cities.get(0).getId();
			CityControllerTests.cities.remove(0);
			CityControllerTests.cities.add(0,cityRepository.findByIdAndStatusLike(Id, 1));
		}
		
		@Test //@WithMockUser(username = "Test1234")
		public void modifyCityNulllRegionName() throws Exception {
			logger.info("modifyCityNullRegionName");
			POSTCityDTO city = new POSTCityDTO("World city", 47.0, 45.0, null, "World Union", "RS");
			Gson gson = new Gson();
	    	String json = gson.toJson(city);
	    	mockMvc.perform(put("/jobster/cities/modify/" + (CityControllerTests.cities.get(0).getId()))
	    			.header("Authorization", "Bearer " + token)
	    			.contentType(MediaType.APPLICATION_JSON).content(json))
	    	.andDo(MockMvcResultHandlers.print())
	        		.andExpect(status().isOk())
	    			.andExpect(content().contentType(contentType)) 
	    			.andExpect(jsonPath("$.cityName", is("World city")));
	    	Integer Id = CityControllerTests.cities.get(0).getId();
			CityControllerTests.cities.remove(0);
			CityControllerTests.cities.add(0,cityRepository.findByIdAndStatusLike(Id, 1));
		}
		
		@Test //@WithMockUser(username = "Test1234")
		public void modifyCityWrongRegionNameNumbers() throws Exception {
			logger.info("modifyCityWrongRegionNameNumbers");
			POSTCityDTO city = new POSTCityDTO("World city", 47.0, 45.0, "RCA112", "World Union", "RS");
			Gson gson = new Gson();
	    	String json = gson.toJson(city);
	    	mockMvc.perform(put("/jobster/cities/modify/" + (CityControllerTests.cities.get(1).getId()))
	    			.header("Authorization", "Bearer " + token)
	    			.contentType(MediaType.APPLICATION_JSON).content(json))
	        		.andExpect(status().isBadRequest());
		}
		
		@Test //@WithMockUser(username = "Test1234")
		public void modifyCityWrongRegionNameOtherChars() throws Exception {
			logger.info("modifyCityWrongRegionNameOtherChars");
			POSTCityDTO city = new POSTCityDTO("World city", 47.0, 45.0, "RCA!", "World Union", "RS");
			Gson gson = new Gson();
	    	String json = gson.toJson(city);
	    	mockMvc.perform(put("/jobster/cities/modify/" + (CityControllerTests.cities.get(1).getId()))
	    			.header("Authorization", "Bearer " + token)
	    			.contentType(MediaType.APPLICATION_JSON).content(json))
	        		.andExpect(status().isBadRequest());
		}
		
		@Test //@WithMockUser(username = "Test1234")
		public void modifyCityWrongRegionNameLeadingSpace() throws Exception {
			logger.info("modifyCityWrongRegionNameLeadingSpace");
			POSTCityDTO city = new POSTCityDTO("World city", 47.0, 45.0, " RCA", "World Union", "RS");
			Gson gson = new Gson();
	    	String json = gson.toJson(city);
	    	mockMvc.perform(put("/jobster/cities/modify/" + (CityControllerTests.cities.get(1).getId()))
	    			.header("Authorization", "Bearer " + token)
	    			.contentType(MediaType.APPLICATION_JSON).content(json))
	        		.andExpect(status().isBadRequest());
		}
		
		@Test //@WithMockUser(username = "Test1234")
		public void modifyCityWrongRegionNameTrailingSpace() throws Exception {
			logger.info("modifyCityWrongRegionNameTrailingSpace");
			POSTCityDTO city = new POSTCityDTO("World city", 47.0, 45.0, "RCA ", "World Union", "RS");
			Gson gson = new Gson();
	    	String json = gson.toJson(city);
	    	mockMvc.perform(put("/jobster/cities/modify/" + (CityControllerTests.cities.get(1).getId()))
	    			.header("Authorization", "Bearer " + token)
	    			.contentType(MediaType.APPLICATION_JSON).content(json))
	        		.andExpect(status().isBadRequest());
		}
		
		@Test //@WithMockUser(username = "Test1234")
		public void modifyCityWrongRegionNameMultipleSpaces() throws Exception {
			logger.info("modifyCityWrongRegionNameMultipleSpaces");
			POSTCityDTO city = new POSTCityDTO("World city", 47.0, 45.0, "RC  AC ", "World Union", "RS");
			Gson gson = new Gson();
	    	String json = gson.toJson(city);
	    	mockMvc.perform(put("/jobster/cities/modify/" + (CityControllerTests.cities.get(1).getId()))
	    			.header("Authorization", "Bearer " + token)
	    			.contentType(MediaType.APPLICATION_JSON).content(json))
	        		.andExpect(status().isBadRequest());
		}
		
		@Test //@WithMockUser(username = "Test1234")
		public void modifyCityWrongRegionNameLeadingWhitespace() throws Exception {
			logger.info("modifyCityWrongRegionLeadingWhitespaces");
			POSTCityDTO city = new POSTCityDTO("World city", 47.0, 45.0, "	RCAC", "World Union", "RS");
			Gson gson = new Gson();
	    	String json = gson.toJson(city);
	    	mockMvc.perform(put("/jobster/cities/modify/" + (CityControllerTests.cities.get(1).getId()))
	    			.header("Authorization", "Bearer " + token)
	    			.contentType(MediaType.APPLICATION_JSON).content(json))
	        		.andExpect(status().isBadRequest());
		}
		
		@Test //@WithMockUser(username = "Test1234")
		public void modifyCityWrongRegionNameTrailingWhitespace() throws Exception {
			logger.info("modifyCityWrongRegionTrailingWhitespaces");
			POSTCityDTO city = new POSTCityDTO("World city", 47.0, 45.0, "RCAC	", "World Union", "RS");
			Gson gson = new Gson();
	    	String json = gson.toJson(city);
	    	mockMvc.perform(put("/jobster/cities/modify/" + (CityControllerTests.cities.get(1).getId()))
	    			.header("Authorization", "Bearer " + token)
	    			.contentType(MediaType.APPLICATION_JSON).content(json))
	        		.andExpect(status().isBadRequest());
		}
		
		@Test //@WithMockUser(username = "Test1234")
		public void modifyCityWrongRegionNameMiddleWhitespace() throws Exception {
			logger.info("modifyCityWrongRegionMiddleWhitespaces");
			POSTCityDTO city = new POSTCityDTO("World city", 47.0, 45.0, "RC	AC", "World Union", "RS");
			Gson gson = new Gson();
	    	String json = gson.toJson(city);
	    	mockMvc.perform(put("/jobster/cities/modify/" + (CityControllerTests.cities.get(1).getId()))
	    			.header("Authorization", "Bearer " + token)
	    			.contentType(MediaType.APPLICATION_JSON).content(json))
	        		.andExpect(status().isBadRequest());
		}
		
		@Test //@WithMockUser(username = "Test1234")
		public void modifyCityEmptyRegionName() throws Exception {
			logger.info("modifyCityEmptyRegionName");
			POSTCityDTO city = new POSTCityDTO("World city", 47.0, 45.0, "", "World Union", "RS");
			Gson gson = new Gson();
	    	String json = gson.toJson(city);
	    	mockMvc.perform(put("/jobster/cities/modify/" + (CityControllerTests.cities.get(1).getId()))
	    			.header("Authorization", "Bearer " + token)
	    			.contentType(MediaType.APPLICATION_JSON).content(json))
	        		.andExpect(status().isBadRequest());
		}
		
		//-------------------------------------------------------------------------------
		
				/*@Test //@WithMockUser(username = "Test1234")
				public void modifyCityMarginalCountryName() throws Exception {
					logger.info("modifyCityMarginalRegionName");
					POSTCityDTO city = new POSTCityDTO("World city", 47.0, 45.0, "RC", "World Union", "RS");
					Gson gson = new Gson();
			    	String json = gson.toJson(city);
			    	mockMvc.perform(put("/jobster/cities/modify/" + (CityControllerTests.cities.get(0).getId()))
			    			.header("Authorization", "Bearer " + token)
			    			.contentType(MediaType.APPLICATION_JSON).content(json))
			        		.andExpect(status().isOk())
			    			.andExpect(content().contentType(contentType)) 
			    			.andExpect(jsonPath("$.cityName", is("World city")))
			    			.andExpect(jsonPath("$.region.countryRegionName", is("RC")));
			    	
			    	Integer Id = CityControllerTests.cities.get(0).getId();
					CityControllerTests.cities.remove(0);
					CityControllerTests.cities.add(0,cityRepository.findByIdAndStatusLike(Id, 1));
				}
				
				@Test //@WithMockUser(username = "Test1234")
				public void modifyCityNulllRegionName() throws Exception {
					logger.info("modifyCityNullRegionName");
					POSTCityDTO city = new POSTCityDTO("World city", 47.0, 45.0, null, "World Union", "RS");
					Gson gson = new Gson();
			    	String json = gson.toJson(city);
			    	mockMvc.perform(put("/jobster/cities/modify/" + (CityControllerTests.cities.get(0).getId()))
			    			.header("Authorization", "Bearer " + token)
			    			.contentType(MediaType.APPLICATION_JSON).content(json))
			    	.andDo(MockMvcResultHandlers.print())
			        		.andExpect(status().isOk())
			    			.andExpect(content().contentType(contentType)) 
			    			.andExpect(jsonPath("$.cityName", is("World city")));
			    	Integer Id = CityControllerTests.cities.get(0).getId();
					CityControllerTests.cities.remove(0);
					CityControllerTests.cities.add(0,cityRepository.findByIdAndStatusLike(Id, 1));
				}
				
				@Test //@WithMockUser(username = "Test1234")
				public void modifyCityWrongRegionNameNumbers() throws Exception {
					logger.info("modifyCityWrongRegionNameNumbers");
					POSTCityDTO city = new POSTCityDTO("World city", 47.0, 45.0, "RCA112", "World Union", "RS");
					Gson gson = new Gson();
			    	String json = gson.toJson(city);
			    	mockMvc.perform(put("/jobster/cities/modify/" + (CityControllerTests.cities.get(1).getId()))
			    			.header("Authorization", "Bearer " + token)
			    			.contentType(MediaType.APPLICATION_JSON).content(json))
			        		.andExpect(status().isBadRequest());
				}
				
				@Test //@WithMockUser(username = "Test1234")
				public void modifyCityWrongRegionNameOtherChars() throws Exception {
					logger.info("modifyCityWrongRegionNameOtherChars");
					POSTCityDTO city = new POSTCityDTO("World city", 47.0, 45.0, "RCA!", "World Union", "RS");
					Gson gson = new Gson();
			    	String json = gson.toJson(city);
			    	mockMvc.perform(put("/jobster/cities/modify/" + (CityControllerTests.cities.get(1).getId()))
			    			.header("Authorization", "Bearer " + token)
			    			.contentType(MediaType.APPLICATION_JSON).content(json))
			        		.andExpect(status().isBadRequest());
				}
				
				@Test //@WithMockUser(username = "Test1234")
				public void modifyCityWrongRegionNameLeadingSpace() throws Exception {
					logger.info("modifyCityWrongRegionNameLeadingSpace");
					POSTCityDTO city = new POSTCityDTO("World city", 47.0, 45.0, " RCA", "World Union", "RS");
					Gson gson = new Gson();
			    	String json = gson.toJson(city);
			    	mockMvc.perform(put("/jobster/cities/modify/" + (CityControllerTests.cities.get(1).getId()))
			    			.header("Authorization", "Bearer " + token)
			    			.contentType(MediaType.APPLICATION_JSON).content(json))
			        		.andExpect(status().isBadRequest());
				}
				
				@Test //@WithMockUser(username = "Test1234")
				public void modifyCityWrongRegionNameTrailingSpace() throws Exception {
					logger.info("modifyCityWrongRegionNameTrailingSpace");
					POSTCityDTO city = new POSTCityDTO("World city", 47.0, 45.0, "RCA ", "World Union", "RS");
					Gson gson = new Gson();
			    	String json = gson.toJson(city);
			    	mockMvc.perform(put("/jobster/cities/modify/" + (CityControllerTests.cities.get(1).getId()))
			    			.header("Authorization", "Bearer " + token)
			    			.contentType(MediaType.APPLICATION_JSON).content(json))
			        		.andExpect(status().isBadRequest());
				}
				
				@Test //@WithMockUser(username = "Test1234")
				public void modifyCityWrongRegionNameMultipleSpaces() throws Exception {
					logger.info("modifyCityWrongRegionNameMultipleSpaces");
					POSTCityDTO city = new POSTCityDTO("World city", 47.0, 45.0, "RC  AC ", "World Union", "RS");
					Gson gson = new Gson();
			    	String json = gson.toJson(city);
			    	mockMvc.perform(put("/jobster/cities/modify/" + (CityControllerTests.cities.get(1).getId()))
			    			.header("Authorization", "Bearer " + token)
			    			.contentType(MediaType.APPLICATION_JSON).content(json))
			        		.andExpect(status().isBadRequest());
				}
				
				@Test //@WithMockUser(username = "Test1234")
				public void modifyCityWrongRegionNameLeadingWhitespace() throws Exception {
					logger.info("modifyCityWrongRegionLeadingWhitespaces");
					POSTCityDTO city = new POSTCityDTO("World city", 47.0, 45.0, "	RCAC", "World Union", "RS");
					Gson gson = new Gson();
			    	String json = gson.toJson(city);
			    	mockMvc.perform(put("/jobster/cities/modify/" + (CityControllerTests.cities.get(1).getId()))
			    			.header("Authorization", "Bearer " + token)
			    			.contentType(MediaType.APPLICATION_JSON).content(json))
			        		.andExpect(status().isBadRequest());
				}
				
				@Test //@WithMockUser(username = "Test1234")
				public void modifyCityWrongRegionNameTrailingWhitespace() throws Exception {
					logger.info("modifyCityWrongRegionTrailingWhitespaces");
					POSTCityDTO city = new POSTCityDTO("World city", 47.0, 45.0, "RCAC	", "World Union", "RS");
					Gson gson = new Gson();
			    	String json = gson.toJson(city);
			    	mockMvc.perform(put("/jobster/cities/modify/" + (CityControllerTests.cities.get(1).getId()))
			    			.header("Authorization", "Bearer " + token)
			    			.contentType(MediaType.APPLICATION_JSON).content(json))
			        		.andExpect(status().isBadRequest());
				}
				
				@Test //@WithMockUser(username = "Test1234")
				public void modifyCityWrongRegionNameMiddleWhitespace() throws Exception {
					logger.info("modifyCityWrongRegionMiddleWhitespaces");
					POSTCityDTO city = new POSTCityDTO("World city", 47.0, 45.0, "RC	AC", "World Union", "RS");
					Gson gson = new Gson();
			    	String json = gson.toJson(city);
			    	mockMvc.perform(put("/jobster/cities/modify/" + (CityControllerTests.cities.get(1).getId()))
			    			.header("Authorization", "Bearer " + token)
			    			.contentType(MediaType.APPLICATION_JSON).content(json))
			        		.andExpect(status().isBadRequest());
				}
				
				@Test //@WithMockUser(username = "Test1234")
				public void modifyCityEmptyRegionName() throws Exception {
					logger.info("modifyCityEmptyRegionName");
					POSTCityDTO city = new POSTCityDTO("World city", 47.0, 45.0, "", "World Union", "RS");
					Gson gson = new Gson();
			    	String json = gson.toJson(city);
			    	mockMvc.perform(put("/jobster/cities/modify/" + (CityControllerTests.cities.get(1).getId()))
			    			.header("Authorization", "Bearer " + token)
			    			.contentType(MediaType.APPLICATION_JSON).content(json))
			        		.andExpect(status().isBadRequest());
				}
				
				//.andDo(MockMvcResultHandlers.print())
				//-------------------------------------------------------------------------------

		}*/


}
