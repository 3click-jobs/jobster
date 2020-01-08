package com.iktpreobuka.jobster;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
	public void getCityByIdWrongName() throws Exception { 
		logger.info("getCityByIdWrongName");
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
	public void addNewCityEmptyRegion() throws Exception {
		logger.info("addNewCityNullRegion");
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
    		.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.cityName", is("LFCapitol")));
    	cityRepository.delete(cityRepository.getByCityName("LFCapitol"));
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
    	cityRepository.delete(cityRepository.getByCityName("LFCapitol"));
	}
	
	@Test //@WithMockUser(username = "Test1234")
	public void addNewCityWrongRegion() throws Exception {
		logger.info("addNewCityWrongRegion");
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
    		.andExpect(status().is5xxServerError());
	}
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
	public void addNewCityWrongName() throws Exception {
		logger.info("addNewCityWrongName");
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
	public void addNewCityWrongCountry() throws Exception {
		logger.info("addNewCityWrongCountry");
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
	//------------------------------------------------------------------
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
	public void addNewCityWrongIso2() throws Exception {
		logger.info("addNewCityWrongIso2");
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
	//------------------------------------------------------------------
	
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
	//------------------------------------------------------------------
	
	
}
