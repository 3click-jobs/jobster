package com.iktpreobuka.jobster;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
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

public class CityControllerTests {
	
	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(),
			Charset.forName("utf8"));
	
	private static MockMvc mockMvc;
			
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
	
	
	@SuppressWarnings("deprecation")
	@Before
	
	public void setUp() throws Exception { 
		logger.info("DBsetUp");
		if(!dbInit) { mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build(); 
			country = countryRepository.save(new CountryEntity("World Union", "RS"));
			countries.add(country);
			region = countryRegionRepository.save(new CountryRegionEntity(country, "World region"));	
			countryRegions.add(region);
			city = cityRepository.save(new CityEntity(region, "World city", 33.3, 34.5));
			cities.add(city);
			
			dbInit = true;
			logger.info("DBsetUp ok");
		} 
	}
	/*public void setUp() throws Exception { 
		logger.info("DBsetUp");
		if(!dbInit) { mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build(); 
			country = countryRepository.save(new CountryEntity("World Union", "RS"));
			countries.add(country);
			region = countryRegionRepository.save(new CountryRegionEntity(country, "World region"));	
			countryRegions.add(region);
			regionNoName = countryRegionRepository.save(new CountryRegionEntity(country, null));	
			countryRegions.add(regionNoName);
			cityWhitoutRegion = cityRepository.save(new CityEntity(regionNoName, "World city without", 33.8, 34.9)); 
			cities.add(cityWhitoutRegion);
			city = cityRepository.save(new CityEntity(region, "World city", 33.3, 34.5));
			cities.add(city);
			
			dbInit = true;
			logger.info("DBsetUp ok");
		} 
	}*/
	
	@After
	public void tearDown() throws Exception {
		logger.info("DBtearDown");
		if(dbInit) {
			for (CityEntity cty : CityControllerTests.cities) {
				/*Iterable<CityDistanceEntity> lcde = cityDistanceRepository.findByFromCity(cty);
				for (CityDistanceEntity cde : lcde) {
					cityDistanceRepository.delete(cde);
				}*/
				cityRepository.delete(cty); 
			}
			CityControllerTests.cities.clear();
			for (CountryRegionEntity creg : CityControllerTests.countryRegions) {
				countryRegionRepository.delete(creg);	
			}
			CityControllerTests.countryRegions.clear();
			for (CountryEntity cntry : CityControllerTests.countries) {
				countryRepository.delete(cntry);
			}
			CityControllerTests.countries.clear();	
			dbInit = false;
			logger.info("DBtearDown ok");
		}
	}
	
	@Test @WithMockUser(username = "admin")
	public void cityServiceNotFound() throws Exception { 
		logger.info("cityServiceNotFound");
		mockMvc.perform(get("/jobster/cities/persons/readallpersons/"))
			//.andDo(MockMvcResultHandlers.print())
			.andExpect(status().isNotFound()); 
	}
	
	@Test 
	@WithMockUser(username = "admin")
	public void getAllCities() throws Exception { 
		logger.info("getAllCities");
		mockMvc.perform(get("/jobster/cities/getAll")) 
			.andExpect(status().isOk()) 
			.andExpect(content().contentType(contentType))
			.andDo(MockMvcResultHandlers.print());
	}
	
	@Test 
	@WithMockUser(username = "admin")
	public void readAllCitiesNotFound() throws Exception { 
		logger.info("readAllCitiesNotFound");
		for (CityEntity city : CityControllerTests.cities) {
			cityRepository.delete(city);
		}
		CityControllerTests.cities.clear();
		mockMvc.perform(get("/jobster/cities/getAll"))
			.andExpect(status().isNotFound());
	}

	@Test
	@WithMockUser(username = "admin")
	public void readSingleCity() throws Exception { 
		logger.info("readSingleCity");
		mockMvc.perform(get("/jobster/cities/getById/" + CityControllerTests.cities.get(0).getId()))
			//.andDo(MockMvcResultHandlers.print())
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json;charset=UTF-8"))
			.andExpect(jsonPath("$.id", is(CityControllerTests.cities.get(0).getId().intValue()))); 
	}

	@Test 
	@WithMockUser(username = "admin")
	public void readCityNotExists() throws Exception { 
		logger.info("readCityNotExists");
		for (CityEntity city : CityControllerTests.cities) {
			cityRepository.delete(city);
		}
		CityControllerTests.cities.clear();
		mockMvc.perform(get("/jobster/cities/getById/" + (CityControllerTests.cities.get(0).getId())))
			.andExpect(status().isNotFound()); 
	}


}
