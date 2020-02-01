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

public class CountryRegionControllerTests {
	
	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(),
			Charset.forName("utf8"));
	
	private static MockMvc mockMvc;
			
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	private static CityEntity city;
	
	private static CountryEntity country, country1;
	
	private static CountryRegionEntity region1, region2, regionA1, regionA2, regionI1, regionI2;
	
	private static List<UserAccountEntity> userAccounts = new ArrayList<>();
	
	private static List<CountryEntity> countries = new ArrayList<>();

	private static List<CountryRegionEntity> regions = new ArrayList<>();

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
			country = countryRepository.save(new CountryEntity("Country", "CN", 1));
			countries.add(country);
			country1 = countryRepository.save(new CountryEntity("OtherCountry", "OC", 1));
			countries.add(country1);
			region1 = countryRegionRepository.save(new CountryRegionEntity(country, "ROne"));
			regions.add(region1);
			region2 = countryRegionRepository.save(new CountryRegionEntity(country, "RTwo"));	
			regions.add(region2);
			regionA1 = new CountryRegionEntity();
			regionA1.setCountry(country1);
			regionA1.setCountryRegionName("RAOne");
			regionA1.setStatus(-1);
			countryRegionRepository.save(regionA1);
			regions.add(regionA1);
			regionA2 = new CountryRegionEntity();
			regionA2.setCountry(country);
			regionA2.setCountryRegionName("RATwo");
			regionA2.setStatus(-1);
			countryRegionRepository.save(regionA2);
			regions.add(regionA2);
			regionI1 = new CountryRegionEntity();
			regionI1.setCountry(country1);
			regionI1.setCountryRegionName("RIOne");
			regionI1.setStatus(0);
			countryRegionRepository.save(regionI1);
			regions.add(regionI1);
			regionI2 = new CountryRegionEntity();
			regionI2.setCountry(country);
			regionI2.setCountryRegionName("RITwo");
			regionI2.setStatus(0);
			countryRegionRepository.save(regionI2);
			regions.add(regionI2);
			city = cityRepository.save(new CityEntity(region1, "World city", 33.3, 34.5));
			cities.add(city);
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
			for (CountryRegionEntity creg : regions) {
				countryRegionRepository.delete(creg);	
			}
			regions.clear();
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
		public void countryRegionServiceNotFound() throws Exception { 
			logger.info("countryRegionServiceNotFound");
			mockMvc.perform(get("/jobster/regions/persons/")
				.header("Authorization", "Bearer " + token))
				.andExpect(status().isNotFound()); 
		}
		
		@Test 
		public void getAllRegions() throws Exception { 
			logger.info("getAllRegions");
			mockMvc.perform(get("/jobster/regions/getAll")
					.header("Authorization", "Bearer " + token)) 
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[1].countryRegionName", is("RATwo")));
		}
		
		@Test 
		public void getAllRegionsNotFound() throws Exception { 
			logger.info("getAllRegionsNotFound");
			for (CountryRegionEntity region : CountryRegionControllerTests.regions) {
				countryRegionRepository.delete(region);
			}
			CountryRegionControllerTests.regions.clear();
			mockMvc.perform(get("/jobster/regions/getAll")
					.header("Authorization", "Bearer " + token))
				.andExpect(content().string("[]"));
		}
			
		@Test
		public void getRegionById() throws Exception { 
			logger.info("getRegionById");
			mockMvc.perform(get("/jobster/regions/getById/" + CountryRegionControllerTests.regions.get(3).getId())
					.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(CountryRegionControllerTests.regions.get(3).getId().intValue())))
				.andExpect(jsonPath("$.countryRegionName", is("RATwo"))); 
		}

		@Test 
		public void getRegionByIdNotExists() throws Exception { 
			logger.info("getRegionByIdNotExists");
			mockMvc.perform(get("/jobster/regions/getById/" + (CountryRegionControllerTests.regions.get(5).getId()+5))
					.header("Authorization", "Bearer " + token))
				.andExpect(content().string(""));
		}
		
		@Test 
		public void getRegionByIdWrongId() throws Exception { 
			logger.info("getRegionByIdWrongId");
			String wrongId="test";
			mockMvc.perform(get("/jobster/regions/getById/" + wrongId)
					.header("Authorization", "Bearer " + token))
				.andExpect(status().isBadRequest());
		}

		@Test
		public void getRegionByName() throws Exception { 
			logger.info("getRegionByName");
			mockMvc.perform(get("/jobster/regions/getByName/" + CountryRegionControllerTests.regions.get(2).getCountryRegionName())
					.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].countryRegionName", is("RAOne"))); 
		}

		@Test 
		public void getRegionByNameNotExists() throws Exception { 
			logger.info("getRegionByNameNotExists");
			String testName="Alibukerki";
			mockMvc.perform(get("/jobster/region/getByName/" + testName)
					.header("Authorization", "Bearer " + token))
				.andExpect(content().string(""));
		}
		
		@Test 
		public void getAllRegionsArchived() throws Exception { 
			logger.info("getAllRegionsArchived");
			mockMvc.perform(get("/jobster/regions/archived")
					.header("Authorization", "Bearer " + token)) 
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[1].countryRegionName", is("RATwo")))
				.andExpect(jsonPath("$[0].countryRegionName", is("RAOne")));
		}
		
		@Test 
		public void getAllRegionsNotFoundArchived() throws Exception { 
			logger.info("getAllRegionsNotFoundArchived");
			for (CountryRegionEntity region : CountryRegionControllerTests.regions) {
				countryRegionRepository.delete(region);
			}
			CountryRegionControllerTests.regions.clear();
			mockMvc.perform(get("/jobster/regions/archived")
					.header("Authorization", "Bearer " + token))
				.andExpect(content().string("[]"));
		}

		@Test 
		public void getAllRegionsInactive() throws Exception { 
			logger.info("getAllRegionsInactive");
			mockMvc.perform(get("/jobster/regions/inactive")
					.header("Authorization", "Bearer " + token)) 
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[1].countryRegionName", is("RITwo")))
				.andExpect(jsonPath("$[0].countryRegionName", is("RIOne")));

		}
		
		@Test 
		public void getAllRegionsNotFoundInactive() throws Exception { 
			logger.info("getAllRegionsNotFoundInactive");
			for (CountryRegionEntity region : CountryRegionControllerTests.regions) {
				countryRegionRepository.delete(region);
			}
			CountryRegionControllerTests.regions.clear();
			mockMvc.perform(get("/jobster/regions/inactive")
					.header("Authorization", "Bearer " + token))
				.andExpect(content().string("[]"));
		}
		
		@Test 
		public void getAllRegionsActive() throws Exception { 
			logger.info("getAllRegionsActive");
			mockMvc.perform(get("/jobster/regions/active")
					.header("Authorization", "Bearer " + token)) 
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].countryRegionName", is("ROne")))
				.andExpect(jsonPath("$[1].countryRegionName", is("RTwo")));
		}
		
		@Test 
		public void getAllRegionsNotFoundActive() throws Exception { 
			logger.info("getAllRegionsNotFoundActive");
			for (CountryRegionEntity region : CountryRegionControllerTests.regions) {
				countryRegionRepository.delete(region);
			}
			CountryRegionControllerTests.regions.clear();
			mockMvc.perform(get("/jobster/regions/active")
					.header("Authorization", "Bearer " + token))
				.andExpect(content().string("[]"));
		}
//--------------------------- ADD REGION ----------------------------------
		
		@Test 
		public void addNewRegion() throws Exception {
			logger.info("addNewRegion");
			CountryRegionEntity region = new CountryRegionEntity();
	    	region.setCountryRegionName("Latifundija");
	    	region.setCountry(country);
			Gson gson = new Gson();
	    	String json = gson.toJson(region);
	    	mockMvc.perform(post("/jobster/regions/addNewRegion")
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON).content(json))
	    		.andExpect(status().isOk())
				.andExpect(content().contentType(contentType)) 
				.andExpect(jsonPath("$.countryRegionName", is("Latifundija")));
	    	countryRegionRepository.delete(countryRegionRepository.getByCountryRegionName("Latifundija"));
		}
		
		@Test 
		public void addNewRegionSameNameDiffCountry() throws Exception {
			logger.info("addNewRegionSameNameDiffCountry");
			CountryRegionEntity region = new CountryRegionEntity();
	    	region.setCountryRegionName("ROne");
	    	region.setCountry(country1);
			Gson gson = new Gson();
	    	String json = gson.toJson(region);
	    	mockMvc.perform(post("/jobster/regions/addNewRegion")
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON).content(json))
	    		.andExpect(status().isOk())
				.andExpect(content().contentType(contentType)) 
				.andExpect(jsonPath("$.countryRegionName", is("ROne")));
	    	countryRegionRepository.delete(countryRegionRepository.findByCountryRegionNameAndCountry("ROne", country1));
		}
		
		@Test 
		public void addNewRegionMarginalName() throws Exception {
			logger.info("addNewRegionMarginalName");
			CountryRegionEntity region = new CountryRegionEntity();
	    	region.setCountryRegionName("La");
	    	region.setCountry(country);
			Gson gson = new Gson();
	    	String json = gson.toJson(region);
	    	mockMvc.perform(post("/jobster/regions/addNewRegion")
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON).content(json))
	    		.andExpect(status().isOk())
				.andExpect(content().contentType(contentType)) 
				.andExpect(jsonPath("$.countryRegionName", is("La")));
	    	countryRegionRepository.delete(countryRegionRepository.getByCountryRegionName("La"));
		}
		
		@Test 
		public void addNewRegionMiddleSpaceName() throws Exception {
			logger.info("addNewRegionMiddleSpaceName");
			CountryRegionEntity region = new CountryRegionEntity();
	    	region.setCountryRegionName("La Tifundija");
	    	region.setCountry(country);
			Gson gson = new Gson();
	    	String json = gson.toJson(region);
	    	mockMvc.perform(post("/jobster/regions/addNewRegion")
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON).content(json))
	    		.andExpect(status().isOk())
				.andExpect(content().contentType(contentType)) 
				.andExpect(jsonPath("$.countryRegionName", is("La Tifundija")));
	    	countryRegionRepository.delete(countryRegionRepository.getByCountryRegionName("La Tifundija"));
		}
		
		@Test 
		public void addNewRegionNullName() throws Exception {
			logger.info("addNewRegionNullName");
			CountryRegionEntity region = new CountryRegionEntity();
	    	region.setCountryRegionName(null);
	    	region.setCountry(country);
			Gson gson = new Gson();
	    	String json = gson.toJson(region);
	    	mockMvc.perform(post("/jobster/regions/addNewRegion")
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON).content(json))
	    		.andExpect(status().isOk())
				.andExpect(content().contentType(contentType)) 
				.andExpect(jsonPath("$.countryRegionName").doesNotExist());
	    	countryRegionRepository.delete(countryRegionRepository.getByCountryRegionName(null));
		}
		
		@Test 
		public void addNewRegionAlredyExists() throws Exception {
			logger.info("addNewRegionAlredyExists");
			CountryRegionEntity region = new CountryRegionEntity();
	    	region.setCountryRegionName("ROne");
	    	region.setCountry(country);
			Gson gson = new Gson();
	    	String json = gson.toJson(region);
	    	mockMvc.perform(post("/jobster/regions/addNewRegion")
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON).content(json))
	    		.andExpect(status().isNotAcceptable())
	    		.andExpect(content().string("Region already exists."));
		}
//----------------------------------- ADD NEW REGION WRONG NAME-------------------------------------------
		
		@Test 
		public void addNewRegionEmptyName() throws Exception {
			logger.info("addNewRegionEmptyName");
			CountryRegionEntity region = new CountryRegionEntity();
	    	region.setCountryRegionName("");
	    	region.setCountry(country);
			Gson gson = new Gson();
	    	String json = gson.toJson(region);
	    	mockMvc.perform(post("/jobster/regions/addNewRegion")
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isBadRequest());
		}
		
		@Test 
		public void addNewRegionNoName() throws Exception {
			logger.info("addNewRegionNoName");
			CountryRegionEntity region = new CountryRegionEntity();
	    	region.setCountry(country);
			Gson gson = new Gson();
	    	String json = gson.toJson(region);
	    	mockMvc.perform(post("/jobster/regions/addNewRegion")
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON).content(json))
	    		.andExpect(status().isOk())
	    		.andExpect(content().contentType(contentType)) 
	    		.andExpect(jsonPath("$.countryRegionName").doesNotExist());
	    	countryRegionRepository.delete(countryRegionRepository.getByCountryRegionName(null));

		}
		
		@Test 
		public void addNewRegionLeadingSpaceName() throws Exception {
			logger.info("addNewRegionLeadingSpaceName");
			CountryRegionEntity region = new CountryRegionEntity();
	    	region.setCountryRegionName(" TRT");
	    	region.setCountry(country);
			Gson gson = new Gson();
	    	String json = gson.toJson(region);
	    	mockMvc.perform(post("/jobster/regions/addNewRegion")
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isBadRequest());
		}
		
		@Test 
		public void addNewRegionTrailingSpaceName() throws Exception {
			logger.info("addNewRegionTrailingSpaceName");
			CountryRegionEntity region = new CountryRegionEntity();
	    	region.setCountryRegionName("TRT ");
	    	region.setCountry(country);
			Gson gson = new Gson();
	    	String json = gson.toJson(region);
	    	mockMvc.perform(post("/jobster/regions/addNewRegion")
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isBadRequest());
		}
		
		@Test 
		public void addNewRegionMultipleSpaceName() throws Exception {
			logger.info("addNewRegionMultipleSpaceName");
			CountryRegionEntity region = new CountryRegionEntity();
	    	region.setCountryRegionName("TR   rtT");
	    	region.setCountry(country);
			Gson gson = new Gson();
	    	String json = gson.toJson(region);
	    	mockMvc.perform(post("/jobster/regions/addNewRegion")
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isBadRequest());
		}
		
		@Test 
		public void addNewRegionLeadingWhiteSpaceName() throws Exception {
			logger.info("addNewRegionLeadingWhiteSpaceName");
			CountryRegionEntity region = new CountryRegionEntity();
	    	region.setCountryRegionName("	TRT");
	    	region.setCountry(country);
			Gson gson = new Gson();
	    	String json = gson.toJson(region);
	    	mockMvc.perform(post("/jobster/regions/addNewRegion")
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isBadRequest());
		}
		
		@Test 
		public void addNewRegionTrailingWhiteSpaceName() throws Exception {
			logger.info("addNewRegionTrailingWhiteSpaceName");
			CountryRegionEntity region = new CountryRegionEntity();
	    	region.setCountryRegionName("TRT	");
	    	region.setCountry(country);
			Gson gson = new Gson();
	    	String json = gson.toJson(region);
	    	mockMvc.perform(post("/jobster/regions/addNewRegion")
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isBadRequest());
		}
		
		@Test 
		public void addNewRegionMiddleWhiteSpaceName() throws Exception {
			logger.info("addNewRegionMiddleWhiteSpaceName");
			CountryRegionEntity region = new CountryRegionEntity();
	    	region.setCountryRegionName("TR	rtT");
	    	region.setCountry(country);
			Gson gson = new Gson();
	    	String json = gson.toJson(region);
	    	mockMvc.perform(post("/jobster/regions/addNewRegion")
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isBadRequest());
		}
		
//----------------------------------------MODIFY REGION-----------------------------------------
		
		@Test 
		public void modifyRegionName() throws Exception {
			logger.info("modifyRegionName");
			CountryRegionEntity region = new CountryRegionEntity();
			region.setCountryRegionName("LaihTifundija");
	    	region.setCountry(country);
	    	region.setStatus(1);
			Gson gson = new Gson();
	    	String json = gson.toJson(region);
	    	mockMvc.perform(put("/jobster/regions/modify/" + (CountryRegionControllerTests.regions.get(0).getId()))
	    			.header("Authorization", "Bearer " + token)
	    			.contentType(MediaType.APPLICATION_JSON).content(json))
	        		.andExpect(status().isOk())
	    			.andExpect(content().contentType(contentType)) 
	    			.andExpect(jsonPath("$.countryRegionName", is("LaihTifundija")));
	    	Integer Id = CountryRegionControllerTests.regions.get(0).getId();
	    	CountryRegionControllerTests.regions.remove(0);
	    	CountryRegionControllerTests.regions.add(0,countryRegionRepository.findByIdAndStatusLike(Id, 1));
		}
		
		@Test 
		public void modifyRegionMarginalName() throws Exception {
			logger.info("modifyRegionMarginalName");
			CountryRegionEntity region = new CountryRegionEntity();
			region.setCountryRegionName("La");
	    	region.setCountry(country);
	    	region.setStatus(1);
			Gson gson = new Gson();
	    	String json = gson.toJson(region);
	    	mockMvc.perform(put("/jobster/regions/modify/" + (CountryRegionControllerTests.regions.get(0).getId()))
	    			.header("Authorization", "Bearer " + token)
	    			.contentType(MediaType.APPLICATION_JSON).content(json))
	        		.andExpect(status().isOk())
	    			.andExpect(content().contentType(contentType)) 
	    			.andExpect(jsonPath("$.countryRegionName", is("La")));
	    	Integer Id = CountryRegionControllerTests.regions.get(0).getId();
	    	CountryRegionControllerTests.regions.remove(0);
	    	CountryRegionControllerTests.regions.add(0,countryRegionRepository.findByIdAndStatusLike(Id, 1));
		}
		
		@Test 
		public void modifyRegionNullName() throws Exception {
			logger.info("modifyRegionNullName");
			CountryRegionEntity region = new CountryRegionEntity();
			region.setCountryRegionName(null);
	    	region.setCountry(country);
	    	region.setStatus(1);
			Gson gson = new Gson();
	    	String json = gson.toJson(region);
	    	mockMvc.perform(put("/jobster/regions/modify/" + (CountryRegionControllerTests.regions.get(0).getId()))
	    			.header("Authorization", "Bearer " + token)
	    			.contentType(MediaType.APPLICATION_JSON).content(json))
	        		.andExpect(status().isOk())
	    			.andExpect(content().contentType(contentType)) 
	    			.andExpect(jsonPath("$.countryRegionName").doesNotExist());
	    	Integer Id = CountryRegionControllerTests.regions.get(0).getId();
	    	CountryRegionControllerTests.regions.remove(0);
	    	CountryRegionControllerTests.regions.add(0,countryRegionRepository.findByIdAndStatusLike(Id, 1));
		}
		
		@Test 
		public void modifyRegionNoName() throws Exception {
			logger.info("modifyRegionNoName");
			CountryRegionEntity region = new CountryRegionEntity();
	    	region.setCountry(country);
	    	region.setStatus(1);
			Gson gson = new Gson();
	    	String json = gson.toJson(region);
	    	mockMvc.perform(put("/jobster/regions/modify/" + (CountryRegionControllerTests.regions.get(0).getId()))
	    			.header("Authorization", "Bearer " + token)
	    			.contentType(MediaType.APPLICATION_JSON).content(json))
	        		.andExpect(status().isOk())
	    			.andExpect(content().contentType(contentType)) 
	    			.andExpect(jsonPath("$.countryRegionName").doesNotExist());
	    	Integer Id = CountryRegionControllerTests.regions.get(0).getId();
	    	CountryRegionControllerTests.regions.remove(0);
	    	CountryRegionControllerTests.regions.add(0,countryRegionRepository.findByIdAndStatusLike(Id, 1));
		}
		
		@Test 
		public void modifyRegionCountry() throws Exception {
			logger.info("modifyRegionCountry");
			CountryRegionEntity region = new CountryRegionEntity();
			region.setCountryRegionName("ROne");
	    	region.setCountry(country1);
			Gson gson = new Gson();
	    	String json = gson.toJson(region);
	    	mockMvc.perform(put("/jobster/regions/modify/" + (CountryRegionControllerTests.regions.get(0).getId()))
	    			.header("Authorization", "Bearer " + token)
	    			.contentType(MediaType.APPLICATION_JSON).content(json))
	        		.andExpect(status().isOk())
	    			.andExpect(content().contentType(contentType)) 
	    			.andExpect(jsonPath("$.country.countryName", is("OtherCountry")));
	    	Integer Id = CountryRegionControllerTests.regions.get(0).getId();
	    	CountryRegionControllerTests.regions.remove(0);
	    	CountryRegionControllerTests.regions.add(0,countryRegionRepository.findByIdAndStatusLike(Id, 1));
		}
		
		@Test 
		public void modifyRegionCountryAndName() throws Exception {
			logger.info("modifyRegionCountryAndName");
			CountryRegionEntity region = new CountryRegionEntity();
			region.setCountryRegionName("LAT");
	    	region.setCountry(country1);
			Gson gson = new Gson();
	    	String json = gson.toJson(region);
	    	mockMvc.perform(put("/jobster/regions/modify/" + (CountryRegionControllerTests.regions.get(0).getId()))
	    			.header("Authorization", "Bearer " + token)
	    			.contentType(MediaType.APPLICATION_JSON).content(json))
	        		.andExpect(status().isOk())
	    			.andExpect(content().contentType(contentType)) 
	    			.andExpect(jsonPath("$.countryRegionName", is("LAT")))
	    			.andExpect(jsonPath("$.country.countryName", is("OtherCountry")));
	    	Integer Id = CountryRegionControllerTests.regions.get(0).getId();
	    	CountryRegionControllerTests.regions.remove(0);
	    	CountryRegionControllerTests.regions.add(0,countryRegionRepository.findByIdAndStatusLike(Id, 1));
		}
		
//-------------------------------------------  MODIFY REGION WRONG NAME -------------------------------
		
		@Test 
		public void modifyRegionEmptyName() throws Exception {
			logger.info("modifyRegionEmptyName");
			CountryRegionEntity region = new CountryRegionEntity();
			region.setCountryRegionName("");
	    	region.setCountry(country);
	    	region.setStatus(1);
			Gson gson = new Gson();
	    	String json = gson.toJson(region);
	    	mockMvc.perform(put("/jobster/regions/modify/" + (CountryRegionControllerTests.regions.get(0).getId()))
	    			.header("Authorization", "Bearer " + token)
	    			.contentType(MediaType.APPLICATION_JSON).content(json))
	        		.andExpect(status().isBadRequest());
		}
		
		@Test 
		public void modifyRegionLeadingSpaceName() throws Exception {
			logger.info("modifyRegionLeadingSpaceName");
			CountryRegionEntity region = new CountryRegionEntity();
			region.setCountryRegionName(" Lat");
	    	region.setCountry(country);
	    	region.setStatus(1);
			Gson gson = new Gson();
	    	String json = gson.toJson(region);
	    	mockMvc.perform(put("/jobster/regions/modify/" + (CountryRegionControllerTests.regions.get(0).getId()))
	    			.header("Authorization", "Bearer " + token)
	    			.contentType(MediaType.APPLICATION_JSON).content(json))
	        		.andExpect(status().isBadRequest());
		}
		
		@Test 
		public void modifyRegionTrailingSpaceName() throws Exception {
			logger.info("modifyRegionTrailingSpaceName");
			CountryRegionEntity region = new CountryRegionEntity();
			region.setCountryRegionName("Lat ");
	    	region.setCountry(country);
	    	region.setStatus(1);
			Gson gson = new Gson();
	    	String json = gson.toJson(region);
	    	mockMvc.perform(put("/jobster/regions/modify/" + (CountryRegionControllerTests.regions.get(0).getId()))
	    			.header("Authorization", "Bearer " + token)
	    			.contentType(MediaType.APPLICATION_JSON).content(json))
	        		.andExpect(status().isBadRequest());
		}
		
		@Test 
		public void modifyRegionMultipleSpaceName() throws Exception {
			logger.info("modifyRegionMultipleSpaceName");
			CountryRegionEntity region = new CountryRegionEntity();
			region.setCountryRegionName("Lat   fund");
	    	region.setCountry(country);
	    	region.setStatus(1);
			Gson gson = new Gson();
	    	String json = gson.toJson(region);
	    	mockMvc.perform(put("/jobster/regions/modify/" + (CountryRegionControllerTests.regions.get(0).getId()))
	    			.header("Authorization", "Bearer " + token)
	    			.contentType(MediaType.APPLICATION_JSON).content(json))
	        		.andExpect(status().isBadRequest());
		}
		
		@Test 
		public void modifyRegionLeadingWhiteSpaceName() throws Exception {
			logger.info("modifyRegionLeadingWhiteSpaceName");
			CountryRegionEntity region = new CountryRegionEntity();
			region.setCountryRegionName("	Lat");
	    	region.setCountry(country);
	    	region.setStatus(1);
			Gson gson = new Gson();
	    	String json = gson.toJson(region);
	    	mockMvc.perform(put("/jobster/regions/modify/" + (CountryRegionControllerTests.regions.get(0).getId()))
	    			.header("Authorization", "Bearer " + token)
	    			.contentType(MediaType.APPLICATION_JSON).content(json))
	        		.andExpect(status().isBadRequest());
		}
		
		@Test 
		public void modifyRegionTrailingWhiteSpaceName() throws Exception {
			logger.info("modifyRegionTrailingWhiteSpaceName");
			CountryRegionEntity region = new CountryRegionEntity();
			region.setCountryRegionName("Lat	");
	    	region.setCountry(country);
	    	region.setStatus(1);
			Gson gson = new Gson();
	    	String json = gson.toJson(region);
	    	mockMvc.perform(put("/jobster/regions/modify/" + (CountryRegionControllerTests.regions.get(0).getId()))
	    			.header("Authorization", "Bearer " + token)
	    			.contentType(MediaType.APPLICATION_JSON).content(json))
	        		.andExpect(status().isBadRequest());
		}
		
		@Test 
		public void modifyRegionMiddleWhiteSpaceName() throws Exception {
			logger.info("modifyRegionMiddleWhiteSpaceName");
			CountryRegionEntity region = new CountryRegionEntity();
			region.setCountryRegionName("Lat	fund");
	    	region.setCountry(country);
	    	region.setStatus(1);
			Gson gson = new Gson();
	    	String json = gson.toJson(region);
	    	mockMvc.perform(put("/jobster/regions/modify/" + (CountryRegionControllerTests.regions.get(0).getId()))
	    			.header("Authorization", "Bearer " + token)
	    			.contentType(MediaType.APPLICATION_JSON).content(json))
	        		.andExpect(status().isBadRequest());
		}
		
//---------------------------------------- ARCHIVE/UNARCHIVE REGION--------------------------------------
		
		@Test 
		public void archiveRegion() throws Exception {
			logger.info("archiveRegion");
			CountryRegionEntity region=CountryRegionControllerTests.regions.get(1);
			region.setStatus(-1);
			Gson gson = new Gson();
	    	String json = gson.toJson(region);
	    	mockMvc.perform(put("/jobster/regions/archive/" + (CountryRegionControllerTests.regions.get(1).getId()))
	    			.header("Authorization", "Bearer " + token)
	    			.contentType(MediaType.APPLICATION_JSON).content(json))
	    	.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.countryRegionName", is("RTwo")))
			.andExpect(jsonPath("$.status", is(-1)));
	    	Integer Id = CountryRegionControllerTests.regions.get(1).getId();
	    	CountryRegionControllerTests.regions.remove(1);
	    	CountryRegionControllerTests.regions.add(1,countryRegionRepository.findByIdAndStatusLike(Id, -1));
		}
		
		@Test 
		public void unarchiveRegion() throws Exception {
			logger.info("unarchiveRegion");
			CountryRegionEntity region=CountryRegionControllerTests.regions.get(3);
			region.setStatus(1);
			Gson gson = new Gson();
	    	String json = gson.toJson(region);
	    	mockMvc.perform(put("/jobster/regions/unarchive/" + (CountryRegionControllerTests.regions.get(3).getId()))
	    			.header("Authorization", "Bearer " + token)
	    			.contentType(MediaType.APPLICATION_JSON).content(json))
	    	.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.countryRegionName", is("RATwo")))
			.andExpect(jsonPath("$.status", is(1)));
	Integer Id = CountryRegionControllerTests.regions.get(3).getId();
	CountryRegionControllerTests.regions.remove(3);
	CountryRegionControllerTests.regions.add(3,countryRegionRepository.findByIdAndStatusLike(Id, 1));
		}
		
//------------------------------------- DELETE/UNDELETE REGION -----------------------------------
		
		@Test 
		public void deleteRegion() throws Exception {
			logger.info("deleteRegion");
			CountryRegionEntity region=CountryRegionControllerTests.regions.get(1);
			region.setStatus(0);
			Gson gson = new Gson();
	    	String json = gson.toJson(region);
	    	mockMvc.perform(delete("/jobster/regions/delete/" + (CountryRegionControllerTests.regions.get(1).getId()))
	    			.header("Authorization", "Bearer " + token)
	    			.contentType(MediaType.APPLICATION_JSON).content(json))
	    	.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.countryRegionName", is("RTwo")))
			.andExpect(jsonPath("$.status", is(0)));
	Integer Id = CountryRegionControllerTests.regions.get(1).getId();
	CountryRegionControllerTests.regions.remove(1);
	CountryRegionControllerTests.regions.add(1,countryRegionRepository.findByIdAndStatusLike(Id, 0));
		}
		
		@Test 
		public void undeleteRegion() throws Exception {
			logger.info("undeleteRegion");
			CountryRegionEntity region=CountryRegionControllerTests.regions.get(4);
			region.setStatus(1);
			Gson gson = new Gson();
	    	String json = gson.toJson(region);
	    	mockMvc.perform(put("/jobster/regions/undelete/" + (CountryRegionControllerTests.regions.get(4).getId()))
	    			.header("Authorization", "Bearer " + token)
	    			.contentType(MediaType.APPLICATION_JSON).content(json))
	    	.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.countryRegionName", is("RIOne")))
			.andExpect(jsonPath("$.status", is(1)));
	Integer Id = CountryRegionControllerTests.regions.get(4).getId();
	CountryRegionControllerTests.regions.remove(4);
	CountryRegionControllerTests.regions.add(4,countryRegionRepository.findByIdAndStatusLike(Id, 1));
		}
}
