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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.iktpreobuka.jobster.entities.CityEntity;
import com.iktpreobuka.jobster.entities.CompanyEntity;
import com.iktpreobuka.jobster.entities.CountryEntity;
import com.iktpreobuka.jobster.entities.CountryRegionEntity;
import com.iktpreobuka.jobster.entities.UserAccountEntity;
import com.iktpreobuka.jobster.entities.dto.CompanyDTO;
import com.iktpreobuka.jobster.enumerations.EUserRole;
import com.iktpreobuka.jobster.repositories.CityRepository;
import com.iktpreobuka.jobster.repositories.CompanyRepository;
import com.iktpreobuka.jobster.repositories.CountryRegionRepository;
import com.iktpreobuka.jobster.repositories.CountryRepository;
import com.iktpreobuka.jobster.repositories.UserAccountRepository;

@RunWith(SpringRunner.class) 
@SpringBootTest 
@WebAppConfiguration 
public class CompanyControllerTests {
 
	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(), 
			MediaType.APPLICATION_JSON.getSubtype(), 
			Charset.forName("utf8"));
	
	private static MockMvc mockMvc;
	/*@Autowired 
	private MockMvc mockMvc;
	
	@MockBean 
	private CompanyDao mockCompanyDao;*/
	
	@Autowired 
	private WebApplicationContext webApplicationContext;
	
	private static CityEntity city;

	private static CityEntity cityWhitoutRegion;
	
	private static CountryEntity country;
	
	private static CountryRegionEntity region;
	
	private static CompanyEntity company;

	private static CountryRegionEntity regionNoName;

	private static UserAccountEntity userAccount;
	
	private static List<CompanyEntity> companies = new ArrayList<>();
	
	private static List<UserAccountEntity> userAccounts = new ArrayList<>();
	
	private static List<CountryEntity> countries = new ArrayList<>();

	private static List<CountryRegionEntity> countryRegions = new ArrayList<>();

	private static List<CityEntity> cities = new ArrayList<>();

	@Autowired 
	private CompanyRepository companyRepository;
	
	/*@Autowired
	private CompanyDao companyDao;*/
	
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
			CompanyControllerTests.companies.add(companyRepository.save(new CompanyEntity(city, "0642345678", "Jobster@mail.com", "About Jobster", "Jobster", "11231231231231")));
			CompanyControllerTests.companies.add(companyRepository.save(new CompanyEntity(city, "0643456789", "Jobsters@mail.com", "About Jobsters", "Jobsters", "11231231231232")));
			CompanyControllerTests.companies.add(companyRepository.save(new CompanyEntity(city, "0644567890", "Jobstery@mail.com", "About Jobstery", "Jobstery", "11231231231233")));
			company = new CompanyEntity(city, "0644567899", "Jobstery1@mail.com", "About Jobstery", "Jobsterya", "11231231231235");
			company.setStatusInactive();
			CompanyControllerTests.companies.add(companyRepository.save(company));
			company = new CompanyEntity(city, "0644567898", "Jobstery2@mail.com", "About Jobstery", "Jobsteryb", "11231231231234");
			company.setStatusArchived();
			CompanyControllerTests.companies.add(companyRepository.save(company));
			CompanyControllerTests.userAccounts.add(userAccountRepository.save(new UserAccountEntity(CompanyControllerTests.companies.get(0), EUserRole.ROLE_USER, "Test1234", "Test1234", CompanyControllerTests.companies.get(0).getId())));
			CompanyControllerTests.userAccounts.add(userAccountRepository.save(new UserAccountEntity(CompanyControllerTests.companies.get(1), EUserRole.ROLE_USER, "Test1235", "Test1234", CompanyControllerTests.companies.get(0).getId())));
			CompanyControllerTests.userAccounts.add(userAccountRepository.save(new UserAccountEntity(CompanyControllerTests.companies.get(2), EUserRole.ROLE_USER, "Test1236", "Test1234", CompanyControllerTests.companies.get(0).getId())));
			userAccount = new UserAccountEntity(CompanyControllerTests.companies.get(3), EUserRole.ROLE_USER, "Test1237", "Test1234", CompanyControllerTests.companies.get(0).getId());
			userAccount.setStatusInactive();
			CompanyControllerTests.userAccounts.add(userAccountRepository.save(userAccount));
			userAccount = new UserAccountEntity(CompanyControllerTests.companies.get(4), EUserRole.ROLE_USER, "Test1238", "Test1234", CompanyControllerTests.companies.get(0).getId());
			userAccount.setStatusArchived();
			CompanyControllerTests.userAccounts.add(userAccountRepository.save(userAccount));
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
			for (UserAccountEntity acc : CompanyControllerTests.userAccounts) {
				userAccountRepository.delete(acc);
			}
			CompanyControllerTests.userAccounts.clear();
			for (CompanyEntity cmpny : CompanyControllerTests.companies) {
				companyRepository.delete(cmpny);
			}
			CompanyControllerTests.companies.clear();
			for (CityEntity cty : CompanyControllerTests.cities) {
				/*Iterable<CityDistanceEntity> lcde = cityDistanceRepository.findByFromCity(cty);
				for (CityDistanceEntity cde : lcde) {
					cityDistanceRepository.delete(cde);
				}*/
				cityRepository.delete(cty); 
			}
			CompanyControllerTests.cities.clear();
			for (CountryRegionEntity creg : CompanyControllerTests.countryRegions) {
				countryRegionRepository.delete(creg);	
			}
			CompanyControllerTests.countryRegions.clear();
			for (CountryEntity cntry : CompanyControllerTests.countries) {
				countryRepository.delete(cntry);
			}
			CompanyControllerTests.countries.clear();	
			dbInit = false;
			logger.info("DBtearDown ok");
		}
	}

	@Test 
	public void companyServiceNotFound() throws Exception { 
		//ResultActions result = */
		mockMvc.perform(get("/jobster/users/companies/readallcompanies/"))
				//.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isNotFound()); 
        //logger.info("companyServiceNotFound: " + result.andReturn().getResponse().getContentAsString());
        //logger.info("companyServiceNotFound: " + result.toString());
		}

	@Test 
	public void companyServiceFound() throws Exception { 
		mockMvc.perform(get("/jobster/users/companies/")).andExpect(status().isOk()); 
		}

	@Test 
	public void readAllCompanies() throws Exception { 
		mockMvc.perform(get("/jobster/users/companies/")) 
			.andExpect(status().isOk()) 
			.andExpect(content().contentType(contentType));
		}

	@Test 
	public void readAllCompaniesNotFound() throws Exception { 
		
		for (UserAccountEntity acc : CompanyControllerTests.userAccounts) {
			userAccountRepository.delete(acc);
		}
		CompanyControllerTests.userAccounts.clear();
		for (CompanyEntity cmpny : CompanyControllerTests.companies) {
			companyRepository.delete(cmpny);
		}
		CompanyControllerTests.companies.clear();

		mockMvc.perform(get("/jobster/users/companies/"))
			.andExpect(status().isNotFound()); 
		}


	@Test 
	public void readSingleCompany() throws Exception { 
		mockMvc.perform(get("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()))
			//.andDo(MockMvcResultHandlers.print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id",
				is(CompanyControllerTests.companies.get(0).getId().intValue()))); }

	@Test 
	public void readSingleCompanyWhichNotExists() throws Exception { 

		mockMvc.perform(get("/jobster/users/companies/" + (CompanyControllerTests.companies.get(4).getId()+1)))
			.andExpect(status().isNotFound()); }

	@Test 
	public void readAllDeletedCompanies() throws Exception { 
		mockMvc.perform(get("/jobster/users/companies/deleted/")) 
			.andExpect(status().isOk()) 
			.andExpect(content().contentType(contentType));
		}

	@Test 
	public void readAllDeletedCompaniesNotFound() throws Exception { 
		
		//CompanyRepository mockCompanyRepository = mock(CompanyRepository.class);
		//when(mockCompanyRepository.findByStatusLike(0)).thenReturn(null);
		
		//when(companyDao.findCompanyByStatusLike(0)).thenReturn(null);

		//userAccountRepository.delete(userAccount);
		//userAccount = null;

		for (UserAccountEntity acc : CompanyControllerTests.userAccounts) {
			userAccountRepository.delete(acc);
		}
		CompanyControllerTests.userAccounts.clear();
		for (CompanyEntity cmpny : CompanyControllerTests.companies) {
			companyRepository.delete(cmpny);
		}
		CompanyControllerTests.companies.clear();
		
		mockMvc.perform(get("/jobster/users/companies/deleted/"))
			.andExpect(status().isNotFound()); 
		}


	@Test 
	public void readSingleDeletedCompany() throws Exception { 
		mockMvc.perform(get("/jobster/users/companies/deleted/" + CompanyControllerTests.companies.get(3).getId()))
			//.andDo(MockMvcResultHandlers.print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id",
				is(CompanyControllerTests.companies.get(3).getId().intValue()))); }

	@Test 
	public void readSingleDeletedCompanyWhichNotExists() throws Exception { 

		mockMvc.perform(get("/jobster/users/companies/deleted/" + CompanyControllerTests.companies.get(0).getId()))
			.andExpect(status().isNotFound()); }

	@Test 
	public void readAllArchivedCompanies() throws Exception { 
		mockMvc.perform(get("/jobster/users/companies/archived/")) 
			.andExpect(status().isOk()) 
			.andExpect(content().contentType(contentType));
		}

	@Test 
	public void readAllArchivedCompaniesNotFound() throws Exception { 
		
		//userAccountRepository.delete(userAccount);
		//userAccount = null;

		for (UserAccountEntity acc : CompanyControllerTests.userAccounts) {
			userAccountRepository.delete(acc);
		}
		CompanyControllerTests.userAccounts.clear();
		for (CompanyEntity cmpny : CompanyControllerTests.companies) {
			companyRepository.delete(cmpny);
		}
		CompanyControllerTests.companies.clear();

		mockMvc.perform(get("/jobster/users/companies/archived/"))
			.andExpect(status().isNotFound()); 
		}


	@Test 
	public void readSingleArchivedCompany() throws Exception { 
		mockMvc.perform(get("/jobster/users/companies/archived/" + CompanyControllerTests.companies.get(4).getId()))
			//.andDo(MockMvcResultHandlers.print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id",
				is(CompanyControllerTests.companies.get(4).getId().intValue()))); }

	@Test 
	public void readSingleArchivedCompanyWhichNotExists() throws Exception { 

		mockMvc.perform(get("/jobster/users/companies/archived/" + (CompanyControllerTests.companies.get(4).getId()+1)))
			.andExpect(status().isNotFound()); }

	@Test 
	public void createCompany() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
        	//.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.user.companyRegistrationNumber", is("11231231231236")));
		CompanyControllerTests.companies.add(companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1));
		userAccountRepository.delete(userAccountRepository.getByUser(companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1)));
		}

	@Test 
	public void createCompanyWithoutCountryRegion() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city without");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
        	//.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.user.companyRegistrationNumber", is("11231231231236")));
		CompanyControllerTests.companies.add(companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1));
		userAccountRepository.delete(userAccountRepository.getByUser(companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1)));
		}

	@Test 
	public void createCompanyMarginalValueCompanyName() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jo");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
        	//.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.user.companyName", is("Jo")));
		CompanyControllerTests.companies.add(companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1));
		userAccountRepository.delete(userAccountRepository.getByUser(companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1)));
		}

	@Test 
	public void createCompanyMarginalValueEmail() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("JobsteriesJobsteriesJobsteriesJobsteriese@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
        	//.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.user.email", is("JobsteriesJobsteriesJobsteriesJobsteriese@mail.com")));
		CompanyControllerTests.companies.add(companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1));
		userAccountRepository.delete(userAccountRepository.getByUser(companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1)));
		}

	@Test 
	public void createCompanyMarginalValueAbout() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("AboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaABABABABABABABA");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
        	//.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.user.about", is("AboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaABABABABABABABA")));
		CompanyControllerTests.companies.add(companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1));
		userAccountRepository.delete(userAccountRepository.getByUser(companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1)));
		}

	@Test 
	public void createCompanyMarginalValueCity() throws Exception {
		
		logger.info("createCompanyMarginalValueCity");

        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("Wo");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
        	//.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.user.companyName", is("Jobsteries")));
		//CompanyControllerTests.companies.add(companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1));
        company = companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1);
        CompanyControllerTests.companies.add(company);
        CompanyControllerTests.cities.add(cityRepository.getByCityName("Wo"));	
		//userAccountRepository.delete(userAccountRepository.getByUser(companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1)));
		CompanyControllerTests.userAccounts.add(userAccountRepository.getByUser(companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1)));
		}

	@Test 
	public void createCompanyMarginalValueCountryName() throws Exception {
		
		logger.info("createCompanyMarginalValueCountryName");
		
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries111");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World cityX");
        companyDTO.setCountry("Wo");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("YYY");
        companyDTO.setCountryRegion("World regionX");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
        	//.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.user.companyName", is("Jobsteries111")));
		//CompanyControllerTests.companies.add(companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1));
        company = companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1);
        CompanyControllerTests.companies.add(company);
        CompanyControllerTests.countries.add(countryRepository.getByCountryName("Wo"));	
        CompanyControllerTests.cities.add(cityRepository.getByCityName("World cityX"));	
        CompanyControllerTests.countryRegions.add(countryRegionRepository.getByCountryRegionName("World regionX"));		
		//userAccountRepository.delete(userAccountRepository.getByUser(companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1)));
		CompanyControllerTests.userAccounts.add(userAccountRepository.getByUser(companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1)));
		}

	@Test 
	public void createCompanyMarginalValueIso2CodeMin() throws Exception {
 
		logger.info("createCompanyMarginalValueIso2CodeMin");

		CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries222");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World cityX");
        companyDTO.setCountry("World UnionX");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XX");
        companyDTO.setCountryRegion("World regionX");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
        	//.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.user.companyName", is("Jobsteries222")));
		//CompanyControllerTests.companies.add(companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1));
        company = companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1);
        CompanyControllerTests.companies.add(company);
        CompanyControllerTests.countries.add(countryRepository.getByCountryName("World UnionX"));	
        CompanyControllerTests.cities.add(cityRepository.getByCityName("World cityX"));	
        CompanyControllerTests.countryRegions.add(countryRegionRepository.getByCountryRegionName("World regionX"));		
		//userAccountRepository.delete(userAccountRepository.getByUser(companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1)));
		CompanyControllerTests.userAccounts.add(userAccountRepository.getByUser(companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1)));
		}

	@Test 
	public void createCompanyMarginalValueCountryRegionName() throws Exception {

		logger.info("createCompanyMarginalValueCountryRegionName");

		CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries333");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World cityX");
        companyDTO.setCountry("World UnionX");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("YYY");
        companyDTO.setCountryRegion("Wo");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
        	//.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.user.companyName", is("Jobsteries333")));
		//CompanyControllerTests.companies.add(companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1));
        company = companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1);
        CompanyControllerTests.companies.add(company);
        CompanyControllerTests.countries.add(countryRepository.getByCountryName("World UnionX"));	
        CompanyControllerTests.cities.add(cityRepository.getByCityName("World cityX"));	
        CompanyControllerTests.countryRegions.add(countryRegionRepository.getByCountryRegionName("Wo"));		
		//userAccountRepository.delete(userAccountRepository.getByUser(companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1)));
		CompanyControllerTests.userAccounts.add(userAccountRepository.getByUser(companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1)));
		}

	@Test 
	public void createCompanyMarginalValueLongitudeMin() throws Exception {

		logger.info("createCompanyMarginalValueLongitudeMin");

		CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries444");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World cityX");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(-180.0);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
        	//.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.user.companyName", is("Jobsteries444")));
		//CompanyControllerTests.companies.add(companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1));
        company = companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1);
        CompanyControllerTests.companies.add(company);
        CompanyControllerTests.cities.add(cityRepository.getByCityName("World cityX"));	
		//userAccountRepository.delete(userAccountRepository.getByUser(companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1)));
		CompanyControllerTests.userAccounts.add(userAccountRepository.getByUser(companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1)));
		}

	@Test 
	public void createCompanyMarginalValueLongitudeMax() throws Exception {

		logger.info("createCompanyMarginalValueLongitudeMax");

		CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries555");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World cityX");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(180.0);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
        	//.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.user.companyName", is("Jobsteries555")));
		//CompanyControllerTests.companies.add(companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1));
        company = companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1);
        CompanyControllerTests.companies.add(company);
        CompanyControllerTests.cities.add(cityRepository.getByCityName("World cityX"));	
		//userAccountRepository.delete(userAccountRepository.getByUser(companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1)));
		CompanyControllerTests.userAccounts.add(userAccountRepository.getByUser(companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1)));
		}

	@Test 
	public void createCompanyMarginalValueLatitudeMin() throws Exception {

		logger.info("createCompanyMarginalValueLatitudeMin");

		CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries666");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World cityX");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(-90.0);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
        	//.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.user.companyName", is("Jobsteries666")));
		//CompanyControllerTests.companies.add(companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1));
        company = companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1);
        CompanyControllerTests.companies.add(company);
        CompanyControllerTests.cities.add(cityRepository.getByCityName("World cityX"));	
		//userAccountRepository.delete(userAccountRepository.getByUser(companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1)));
		CompanyControllerTests.userAccounts.add(userAccountRepository.getByUser(companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1)));
		}

	@Test 
	public void createCompanyMarginalValueLatitudeMax() throws Exception {

		logger.info("createCompanyMarginalValueLatitudeMax");

		CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries777");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World cityX");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(90.0);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
        	//.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.user.companyName", is("Jobsteries777")));
		//CompanyControllerTests.companies.add(companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1));
        company = companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1);
        CompanyControllerTests.companies.add(company);
        CompanyControllerTests.cities.add(cityRepository.getByCityName("World cityX"));	
		//userAccountRepository.delete(userAccountRepository.getByUser(companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1)));
		CompanyControllerTests.userAccounts.add(userAccountRepository.getByUser(companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1)));
		}

	@Test 
	public void createCompanyMarginalValueUsernameMin() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test5");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
        	//.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.username", is("Test5")));
		CompanyControllerTests.companies.add(companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1));
		userAccountRepository.delete(userAccountRepository.getByUser(companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1)));
		}

	@Test 
	public void createCompanyMarginalValueUsernameMax() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test1234567891234567");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
        	//.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.username", is("Test1234567891234567")));
		CompanyControllerTests.companies.add(companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1));
		userAccountRepository.delete(userAccountRepository.getByUser(companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1)));
		}

	@Test 
	public void createCompanyMarginalValuePasswordAndConfirmedPasswordMin() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test12");
        companyDTO.setPassword("Test5");
        companyDTO.setConfirmedPassword("Test5");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
        	//.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.username", is("Test12")));
		CompanyControllerTests.companies.add(companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1));
		userAccountRepository.delete(userAccountRepository.getByUser(companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1)));
		}

	@Test 
	public void createCompanyValidationErrorCompanyNameOneChar() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("J");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanyValidationErrorCompanyNameContainsNotOnlyLettersAndNumbers() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("J&o#3!:d");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanyValidationErrorRegistrationNumberToSmall() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("112312331236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanyValidationErrorRegistrationNumberToLarge() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("1123123123123656");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanyValidationErrorRegistrationNumberContainsLetter() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("112f1231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanyValidationErrorEmailWrongRegex() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanyValidationErrorPhoneRegex() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("064gf56789--01");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanyValidationErrorEmailToLong() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("JobsteriesJobsteriesJobsteriesJobsteriesJobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}
	
	@Test 
	public void createCompanyValidationErrorCityNameMin() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanyValidationErrorCityNameRegex() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city1");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanyValidationErrorCountryNameMin() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanyValidationErrorCountryNameRegex() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union1");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanyValidationErrorIso2CodeMin() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("X");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanyValidationErrorIso2CodeMax() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanyValidationErrorIso2CodeRegex() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("X1X");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanyValidationErrorCountryRegionRegex() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region1");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanyValidationErrorLongitudeToSmall() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region1");
        companyDTO.setLongitude(-181.0);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanyValidationErrorLongitudeToLarge() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region1");
        companyDTO.setLongitude(181.0);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanyValidationErrorLatitudeToSmall() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region1");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(-91.0);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanyValidationErrorLatitudeToLarge() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region1");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(91.0);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanyValidationErrorAboutToLarge() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries, About Jobsteries, About Jobsteries, About Jobsteries, About Jobsteries, About Jobsteries, About Jobsteries, About Jobsteries, About Jobsteries, About Jobsteries, About Jobsteries, About Jobsteries, About Jobsteries, About Jobsteries, About Jobsteries, About Jobsteries.");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region1");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanyValidationErrorUsernameToSmall() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region1");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanyValidationErrorUsernameToLarge() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region1");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("TestTestTestTestTestTest");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanyValidationErrorRoleWrong() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("AAAA");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanyValidationErrorPasswordAndConfirmedPasswordToSmall() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region1");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test");
        companyDTO.setConfirmedPassword("Test");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanyValidationErrorPasswordAndConfirmedPasswordContainUnsupportedChars() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region1");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Te+st!12&3");
        companyDTO.setConfirmedPassword("Te+st!12&3");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createNullCompany() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanyNullEmail() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
	companyDTO.setEmail(null);
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanyBlankEmail() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
	companyDTO.setEmail("");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanySpaceEmail() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
	companyDTO.setEmail(" ");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanyNullPhone() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone(null);
	companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanyBlankPhone() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("");
	companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanySpacePhone() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone(" ");
	companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanyNullCompanyName() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName(null);
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanyBlankCompanyName() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanySpaceCompanyName() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName(" ");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanyNullRegistrationNumber() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
	companyDTO.setCompanyRegistrationNumber(null);
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanyBlankRegistrationNumber() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
	companyDTO.setCompanyRegistrationNumber("");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanySpaceRegistrationNumber() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
	companyDTO.setCompanyRegistrationNumber(" ");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanyNullCity() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
	companyDTO.setCity(null);
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanyBlankCity() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
	companyDTO.setCity("");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanySpaceCity() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
	companyDTO.setCity(" ");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanyNullLongitude() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(null);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanyNullLatitude() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
	companyDTO.setLatitude(null);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanyNullCountry() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
	companyDTO.setCountry(null);
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanyBlankCountry() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
	companyDTO.setCountry("");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanySpaceCountry() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
	companyDTO.setCountry(" ");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanyNullRole() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
	companyDTO.setAccessRole(null);
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanyBlankRole() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
	companyDTO.setAccessRole("");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanySpaceRole() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
	companyDTO.setAccessRole(" ");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanyNullIso2Code() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
	companyDTO.setIso2Code(null);
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanyBlankIso2Code() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
	companyDTO.setIso2Code("");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanySpaceIso2Code() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
	companyDTO.setIso2Code(" ");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanyNullUsername() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
	companyDTO.setUsername(null);
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanyBlankUsername() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
	companyDTO.setUsername("");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanySpaceUsername() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
	companyDTO.setUsername(" ");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanyNullPassword() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
	companyDTO.setPassword(null);
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanyBlankPassword() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
	companyDTO.setPassword("");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanySpacePassword() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
	companyDTO.setPassword(" ");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanyNullConfirmedPassword() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
	companyDTO.setConfirmedPassword(null);
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanyBlankConfirmedPassword() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
	companyDTO.setConfirmedPassword("");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanySpaceConfirmedPassword() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
	companyDTO.setConfirmedPassword(" ");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void createCompanyMobilePhoneAlreadyExists() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0642345678");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isNotAcceptable());
		}

	@Test 
	public void createCompanyEmailAlreadyExists() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobster@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isNotAcceptable());
		}

	@Test 
	public void createCompanyCompanyIdAlreadyExists() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231232");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isNotAcceptable());
		}

	@Test 
	public void createCompanyWrongAccessRole() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_ADMIN");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test123");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isNotAcceptable());
		}

	@Test 
	public void createCompanyUsernameAlreadyExists() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0645678901");
        companyDTO.setEmail("Jobsteries@mail.com");
        companyDTO.setAbout("About Jobsteries");
        companyDTO.setCompanyName("Jobsteries");
        companyDTO.setCompanyRegistrationNumber("11231231231236");
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Test1234");
        companyDTO.setPassword("Test123");
        companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isNotAcceptable());
		}

	@Test 
	public void updateCompanyWhichNotExists() throws Exception {
		
        	CompanyDTO companyDTO = new CompanyDTO();
        	companyDTO.setCompanyName("Jobsty");
        	companyDTO.setEmail("Jobsty@mail.com");
        
        	Gson gson = new Gson();
        	String json = gson.toJson(companyDTO);

        	//ResultActions result =
			mockMvc.perform(put("/jobster/users/companies/" + (CompanyControllerTests.companies.get(4).getId()+1))
        			.contentType(MediaType.APPLICATION_JSON).content(json))
        			//.andDo(MockMvcResultHandlers.print())
        			.andExpect(status().isNotFound());
            //logger.info("updateCompanyWhichNotExists: " + result.andReturn().getResponse().getContentAsString());
            //logger.info("updateCompanyWhichNotExists: " + result.toString());
}	

	@Test 
	public void updateCompanyName() throws Exception {
		
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setCompanyName("Jobsty");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        //ResultActions result = 
		mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId())
        		.contentType(MediaType.APPLICATION_JSON).content(json))
			//.andDo(MockMvcResultHandlers.print())
	        .andExpect(status().isOk())
			.andExpect(content().contentType(contentType))
			.andExpect(jsonPath("$.user.companyName", is("Jobsty")));
        //logger.info("updateCompanyName: " + result.andReturn().getResponse().getContentAsString());
        //logger.info("updateCompanyName: " + result.toString());
		Integer Id = CompanyControllerTests.companies.get(0).getId();
		CompanyControllerTests.companies.remove(0);
		CompanyControllerTests.companies.add(companyRepository.getByIdAndStatusLike(Id, 1));
		}

	@Test 
	public void updateCompanyNameMarginalValue() throws Exception {
		
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setCompanyName("Jo");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        //ResultActions result = 
		mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId())
        		.contentType(MediaType.APPLICATION_JSON).content(json))
			//.andDo(MockMvcResultHandlers.print())
	        .andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.user.companyName", is("Jo")));
        //logger.info("updateCompanyNameMarginalValue: " + result.andReturn().getResponse().getContentAsString());
        //logger.info("updateCompanyNameMarginalValue: " + result.toString());
		Integer Id = CompanyControllerTests.companies.get(0).getId();
		CompanyControllerTests.companies.remove(0);
		CompanyControllerTests.companies.add(companyRepository.getByIdAndStatusLike(Id, 1));
		}

	@Test 
	public void updateCompanyEmail() throws Exception {
		
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setEmail("Jobsty@mail.com");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        //ResultActions result = 
		mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId())
        		.contentType(MediaType.APPLICATION_JSON).content(json))
				//.andDo(MockMvcResultHandlers.print())
	        	.andExpect(status().isOk())
	        	.andExpect(content().contentType(contentType)) 
	        	.andExpect(jsonPath("$.user.email", is("Jobsty@mail.com")));
        //logger.info("updateCompanyEmail: " + result.andReturn().getResponse().getContentAsString());
        //logger.info("updateCompanyEmail: " + result.toString());
		Integer Id = CompanyControllerTests.companies.get(0).getId();
		CompanyControllerTests.companies.remove(0);
		CompanyControllerTests.companies.add(companyRepository.getByIdAndStatusLike(Id, 1));
		}

	@Test 
	public void updateCompanyEmailMarginalValue() throws Exception {
		
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setEmail("JobsteriesJobsteriesJobsteriesJobsteriese@mail.com");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        //ResultActions result = 
		mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId())
        		.contentType(MediaType.APPLICATION_JSON).content(json))
				//.andDo(MockMvcResultHandlers.print())
	        	.andExpect(status().isOk())
	        	.andExpect(content().contentType(contentType)) 
	        	.andExpect(jsonPath("$.user.email", is("JobsteriesJobsteriesJobsteriesJobsteriese@mail.com")));
        //logger.info("updateCompanyEmailMarginalValue: " + result.andReturn().getResponse().getContentAsString());
        //logger.info("updateCompanyEmailMarginalValue: " + result.andReturn().toString());
		Integer Id = CompanyControllerTests.companies.get(0).getId();
		CompanyControllerTests.companies.remove(0);
		CompanyControllerTests.companies.add(companyRepository.getByIdAndStatusLike(Id, 1));
		}
	
	@Test 
	public void updateCompanyAboutMarginalValue() throws Exception {
		
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setAbout("AboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaABABABABABABABA");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        //ResultActions result = 
		mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId())
        		.contentType(MediaType.APPLICATION_JSON).content(json))
				//.andDo(MockMvcResultHandlers.print())
	        	.andExpect(status().isOk())
	        	.andExpect(content().contentType(contentType)) 
	        	.andExpect(jsonPath("$.user.about", is("AboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaAboutJobsteriesCompaABABABABABABABA")));
        //logger.info("updateCompanyAboutMarginalValue: " + result.andReturn().getResponse().getContentAsString());
        //logger.info("updateCompanyAboutMarginalValue: " + result.toString());
		Integer Id = CompanyControllerTests.companies.get(0).getId();
		CompanyControllerTests.companies.remove(0);
		CompanyControllerTests.companies.add(companyRepository.getByIdAndStatusLike(Id, 1));
		}

	@Test 
	public void updateCompanyCityMarginalValue() throws Exception {
		
		logger.info("updateCompanyCityMarginalValue");
		
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setCity("Wo");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId())
        		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			//.andDo(MockMvcResultHandlers.print())
			.andExpect(jsonPath("$.user.id", is(companies.get(0).getId())));
		Integer Id = CompanyControllerTests.companies.get(0).getId();
		CompanyControllerTests.companies.remove(0);
		company = companyRepository.getByIdAndStatusLike(Id, 1);
        CompanyControllerTests.companies.add(company);
        CompanyControllerTests.cities.add(cityRepository.getByCityName("Wo"));	
		}

	@Test 
	public void updateCompanyCountryMarginalValue() throws Exception {
		
		logger.info("updateCompanyCountryMarginalValue");

		CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setCity("World cityX");
        companyDTO.setCountry("Wo");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("YYY");
        companyDTO.setCountryRegion("World regionX");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId())
        		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			//.andDo(MockMvcResultHandlers.print())
			.andExpect(jsonPath("$.user.id", is(companies.get(0).getId())));
		Integer Id = CompanyControllerTests.companies.get(0).getId();
		CompanyControllerTests.companies.remove(0);
		company = companyRepository.getByIdAndStatusLike(Id, 1);
        CompanyControllerTests.companies.add(company);
        CompanyControllerTests.countries.add(countryRepository.getByCountryName("Wo"));	
        CompanyControllerTests.cities.add(cityRepository.getByCityName("World cityX"));	
        CompanyControllerTests.countryRegions.add(countryRegionRepository.getByCountryRegionName("World regionX"));		
		}

	@Test 
	public void updateCompanyIso2CodeMarginalValue() throws Exception {
		
		logger.info("updateCompanyIso2CodeMarginalValue");

		CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setCity("World cityX");
        companyDTO.setCountry("World UnionX");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XX");
        companyDTO.setCountryRegion("World regionX");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId())
        		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			//.andDo(MockMvcResultHandlers.print())
			.andExpect(jsonPath("$.user.id", is(companies.get(0).getId())));
		Integer Id = CompanyControllerTests.companies.get(0).getId();
		CompanyControllerTests.companies.remove(0);
		company = companyRepository.getByIdAndStatusLike(Id, 1);
        CompanyControllerTests.companies.add(company);
        CompanyControllerTests.countries.add(countryRepository.getByCountryName("World UnionX"));	
        CompanyControllerTests.cities.add(cityRepository.getByCityName("World cityX"));	
        CompanyControllerTests.countryRegions.add(countryRegionRepository.getByCountryRegionName("World regionX"));		
		}

	@Test 
	public void updateCompanyLongitudeMinMarginalValue() throws Exception {
		
		logger.info("updateCompanyLongitudeMinMarginalValue");

		CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setCity("World cityX");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(-180.0);
        companyDTO.setLatitude(43.1);
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId())
        		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			//.andDo(MockMvcResultHandlers.print())
			.andExpect(jsonPath("$.user.id", is(companies.get(0).getId())));
		Integer Id = CompanyControllerTests.companies.get(0).getId();
		CompanyControllerTests.companies.remove(0);
		company = companyRepository.getByIdAndStatusLike(Id, 1);
        CompanyControllerTests.companies.add(company);
        CompanyControllerTests.cities.add(cityRepository.getByCityName("World cityX"));	
		}

	@Test 
	public void updateCompanyLongitudeMaxMarginalValue() throws Exception {
		
		logger.info("updateCompanyLongitudeMaxMarginalValue");

		CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setCity("World cityX");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(180.0);
        companyDTO.setLatitude(43.1);
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId())
        		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			//.andDo(MockMvcResultHandlers.print())
			.andExpect(jsonPath("$.user.id", is(companies.get(0).getId())));
		Integer Id = CompanyControllerTests.companies.get(0).getId();
		CompanyControllerTests.companies.remove(0);
		company = companyRepository.getByIdAndStatusLike(Id, 1);
        CompanyControllerTests.companies.add(company);
        CompanyControllerTests.cities.add(cityRepository.getByCityName("World cityX"));	
		}

	@Test 
	public void updateCompanyLatitudeMinMarginalValue() throws Exception {
		
		logger.info("updateCompanyLatitudeMinMarginalValue");

		CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setCity("World cityX");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(-90.0);
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId())
        		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			//.andDo(MockMvcResultHandlers.print())
			.andExpect(jsonPath("$.user.id", is(companies.get(0).getId())));
		Integer Id = CompanyControllerTests.companies.get(0).getId();
		CompanyControllerTests.companies.remove(0);
		company = companyRepository.getByIdAndStatusLike(Id, 1);
        CompanyControllerTests.companies.add(company);
        CompanyControllerTests.cities.add(cityRepository.getByCityName("World cityX"));	
		}

	@Test 
	public void updateCompanyLatitudeMaxMarginalValue() throws Exception {
		
		logger.info("updateCompanyLatitudeMaxMarginalValue");

		CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setCity("World cityX");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(90.0);
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId())
        		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			//.andDo(MockMvcResultHandlers.print())
			.andExpect(jsonPath("$.user.id", is(companies.get(0).getId())));
		Integer Id = CompanyControllerTests.companies.get(0).getId();
		CompanyControllerTests.companies.remove(0);
		company = companyRepository.getByIdAndStatusLike(Id, 1);
        CompanyControllerTests.companies.add(company);
        CompanyControllerTests.cities.add(cityRepository.getByCityName("World cityX"));	
	}

	@Test 
	public void updateCompanyUsernameMinMarginalValue() throws Exception {
		
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setUsername("Test5");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);
        //logger.info(json.toString());

        //ResultActions result = 
		mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId())
        		.contentType(MediaType.APPLICATION_JSON).content(json))
        	//.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.username", is("Test5")));
        //logger.info("updateCompanyUsernameMinMarginalValue: " + result.andReturn().getResponse().getContentAsString());
        //logger.info("updateCompanyUsernameMinMarginalValue: " + result.toString());
		CompanyControllerTests.userAccounts.remove(0);
		CompanyControllerTests.userAccounts.add(userAccountRepository.findByUserAndStatusLike(CompanyControllerTests.companies.get(0), 1));
		}

	@Test 
	public void updateCompanyUsernameMaxMarginalValue() throws Exception {
		
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setUsername("Test1234567891234567");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        //ResultActions result = 
		mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId())
        		.contentType(MediaType.APPLICATION_JSON).content(json))
        	//.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.username", is("Test1234567891234567")));
        //logger.info("updateCompanyUsernameMaxMarginalValue: " + result.andReturn().getResponse().getContentAsString());
        //logger.info("updateCompanyUsernameMaxMarginalValue: " + result.toString());
		CompanyControllerTests.userAccounts.remove(0);
		CompanyControllerTests.userAccounts.add(userAccountRepository.findByUserAndStatusLike(CompanyControllerTests.companies.get(0), 1));
		}

	@Test 
	public void updateCompanyPasswordAndConfirmedPasswordMinMarginalValue() throws Exception {
		
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setPassword("Test5");
        companyDTO.setConfirmedPassword("Test5");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        //ResultActions result = 
        	mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId())
        		.contentType(MediaType.APPLICATION_JSON).content(json))
        		//.andDo(MockMvcResultHandlers.print())
        		.andExpect(status().isOk())
        		.andExpect(content().contentType(contentType)) 
        		.andExpect(jsonPath("$.username", is("Test1234")));
        //logger.info("updateCompanyPasswordAndConfirmedPasswordMinMarginalValue: " + result.andReturn().getResponse().getContentAsString());
        //logger.info("updateCompanyPasswordAndConfirmedPasswordMinMarginalValue: " + result.toString());
		CompanyControllerTests.userAccounts.remove(0);
		CompanyControllerTests.userAccounts.add(userAccountRepository.findByUserAndStatusLike(CompanyControllerTests.companies.get(0), 1));
		}

	@Test 
	public void updateCompanyValidationErrorCompanyNameOneChar() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setCompanyName("J");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanyValidationErrorCompanyNameContainsNotOnlyLettersAndNumbers() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setCompanyName("J&o#3!:d");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanyValidationErrorRegistrationNumberToSmall() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setCompanyRegistrationNumber("112312331236");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanyValidationErrorRegistrationNumberToLarge() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setCompanyRegistrationNumber("1123123123123656");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanyValidationErrorRegistrationNumberContainsLetter() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setCompanyRegistrationNumber("112f1231231236");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanyValidationErrorEmailWrongRegex() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setEmail("Jobsty@mail");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanyValidationErrorPhoneWrongRegex() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("064gf56789--01");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanyValidationErrorEmailToLong() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setEmail("JobsteriesJobsteriesJobsteriesJobsteriesJobsteries@mail.com");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}
	
	@Test 
	public void updateCompanyValidationErrorAboutToLong() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setAbout("About Jobsteries....About Jobsteries....About Jobsteries....About Jobsteries....About Jobsteries....About Jobsteries....About Jobsteries....About Jobsteries....About Jobsteries....About Jobsteries....About Jobsteries....About Jobsteries....About Jobsteries");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanyValidationErrorCityNameMin() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setCity("");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanyValidationErrorCityNameRegex() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setCity("World city1");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanyValidationErrorCountryNameMin() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setCity("World city");
        companyDTO.setCountry("");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}
	
	@Test 
	public void updateCompanyValidationErrorCountryNameRegex() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union1");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanyValidationErrorIso2CodeMin() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("X");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanyValidationErrorIso2CodeMax() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanyValidationErrorIso2CodeRegex() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("X1X");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanyValidationErrorCountryRegionRegex() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region1");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanyValidationErrorLongitudeToSmall() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(-181.0);
        companyDTO.setLatitude(43.1);
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanyValidationErrorLongitudeToLarge() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(181.0);
        companyDTO.setLatitude(43.1);
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanyValidationErrorLatitudeToSmall() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(-91.0);
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanyValidationErrorLatitudeToLarge() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setCity("World city");
        companyDTO.setCountry("World Union");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("XXX");
        companyDTO.setCountryRegion("World region");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(91.0);
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanyValidationErrorAboutToLarge() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setAbout("About Jobsteries, About Jobsteries, About Jobsteries, About Jobsteries, About Jobsteries, About Jobsteries, About Jobsteries, About Jobsteries, About Jobsteries, About Jobsteries, About Jobsteries, About Jobsteries, About Jobsteries, About Jobsteries, About Jobsteries, About Jobsteries.");
       
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanyValidationErrorUsernameToSmall() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setUsername("Test");
       
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanyValidationErrorUsernameToLarge() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setUsername("TestTestTestTestTestTest");
       
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanyValidationErrorRole() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setAccessRole("AAAA");
       
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanyValidationErrorPasswordAndConfirmedPasswordToSmall() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setPassword("Test");
        companyDTO.setConfirmedPassword("Test");
       
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanyValidationErrorPasswordAndConfirmedPasswordContainUnsupportedChars() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setPassword("Te+st!12&3");
        companyDTO.setConfirmedPassword("Te+st!12&3");
       
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateNullCompany() throws Exception {
		CompanyDTO companyDTO = new CompanyDTO();
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanyNullEmail() throws Exception {
		CompanyDTO companyDTO = new CompanyDTO();
		companyDTO.setEmail(null);
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanyBlankEmail() throws Exception {
		CompanyDTO companyDTO = new CompanyDTO();
		companyDTO.setEmail("");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanySpaceEmail() throws Exception {
		CompanyDTO companyDTO = new CompanyDTO();
		companyDTO.setEmail(" ");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanyNullPhone() throws Exception {
		CompanyDTO companyDTO = new CompanyDTO();
		companyDTO.setMobilePhone(null);
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanyBlankPhone() throws Exception {
		CompanyDTO companyDTO = new CompanyDTO();
		companyDTO.setMobilePhone("");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanySpacePhone() throws Exception {
		CompanyDTO companyDTO = new CompanyDTO();
		companyDTO.setMobilePhone(" ");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanyNullCompanyName() throws Exception {
		CompanyDTO companyDTO = new CompanyDTO();
		companyDTO.setCompanyName(null);
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanyBlankCompanyName() throws Exception {
		CompanyDTO companyDTO = new CompanyDTO();
		companyDTO.setCompanyName("");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanySpaceCompanyName() throws Exception {
		CompanyDTO companyDTO = new CompanyDTO();
		companyDTO.setCompanyName(" ");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanyNullRegistrationNumber() throws Exception {
		CompanyDTO companyDTO = new CompanyDTO();
		companyDTO.setCompanyRegistrationNumber(null);
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanyBlankRegistrationNumber() throws Exception {
		CompanyDTO companyDTO = new CompanyDTO();
		companyDTO.setCompanyRegistrationNumber("");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanySpaceRegistrationNumber() throws Exception {
		CompanyDTO companyDTO = new CompanyDTO();
		companyDTO.setCompanyRegistrationNumber(" ");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanyNullCity() throws Exception {
		CompanyDTO companyDTO = new CompanyDTO();
		companyDTO.setCity(null);
        	companyDTO.setCountry("World Union");
        	companyDTO.setIso2Code("XXX");
        	companyDTO.setCountryRegion("World region");
        	companyDTO.setLongitude(43.1);
        	companyDTO.setLatitude(43.1);
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanyBlankCity() throws Exception {
		CompanyDTO companyDTO = new CompanyDTO();
		companyDTO.setCity("");
        	companyDTO.setCountry("World Union");
        	companyDTO.setIso2Code("XXX");
        	companyDTO.setCountryRegion("World region");
        	companyDTO.setLongitude(43.1);
        	companyDTO.setLatitude(43.1);
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanySpaceCity() throws Exception {
		CompanyDTO companyDTO = new CompanyDTO();
		companyDTO.setCity(" ");
        	companyDTO.setCountry("World Union");
        	companyDTO.setIso2Code("XXX");
        	companyDTO.setCountryRegion("World region");
        	companyDTO.setLongitude(43.1);
        	companyDTO.setLatitude(43.1);
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        //ResultActions result = 
		mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
        		//.andDo(MockMvcResultHandlers.print())
        		.andExpect(status().isBadRequest());
        //logger.info("updateCompanySpaceCity: " + result.andReturn().getResponse().getContentAsString());
        //logger.info("updateCompanySpaceCity: " + result.toString());
		}

	@Test 
	public void updateCompanyNullLongitude() throws Exception {
		CompanyDTO companyDTO = new CompanyDTO();
		companyDTO.setCity("World city");
        	companyDTO.setCountry("World Union");
        	companyDTO.setIso2Code("XXX");
        	companyDTO.setCountryRegion("World region");
        	companyDTO.setLongitude(null);
        	companyDTO.setLatitude(43.1);
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanyNullLatitude() throws Exception {
		CompanyDTO companyDTO = new CompanyDTO();
		companyDTO.setCity("World city");
        	companyDTO.setCountry("World Union");
        	companyDTO.setIso2Code("XXX");
        	companyDTO.setCountryRegion("World region");
        	companyDTO.setLongitude(43.1);
        	companyDTO.setLatitude(null);
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanyNullCountry() throws Exception {
		CompanyDTO companyDTO = new CompanyDTO();
		companyDTO.setCity("World city");
        	companyDTO.setCountry(null);
        	companyDTO.setIso2Code("XXX");
        	companyDTO.setCountryRegion("World region");
        	companyDTO.setLongitude(43.1);
        	companyDTO.setLatitude(43.1);
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanyBlankCountry() throws Exception {
		CompanyDTO companyDTO = new CompanyDTO();
		companyDTO.setCity("World city");
        	companyDTO.setCountry("");
        	companyDTO.setIso2Code("XXX");
        	companyDTO.setCountryRegion("World region");
        	companyDTO.setLongitude(43.1);
        	companyDTO.setLatitude(43.1);
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanySpaceCountry() throws Exception {
		CompanyDTO companyDTO = new CompanyDTO();
		companyDTO.setCity("World city");
        	companyDTO.setCountry(" ");
        	companyDTO.setIso2Code("XXX");
        	companyDTO.setCountryRegion("World region");
        	companyDTO.setLongitude(43.1);
        	companyDTO.setLatitude(43.1);
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        //ResultActions result = 
		mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
        		//.andDo(MockMvcResultHandlers.print())
        		.andExpect(status().isBadRequest());
        //logger.info("updateCompanySpaceCountry: " + result.andReturn().getResponse().getContentAsString());
        //logger.info("updateCompanySpaceCountry: " + result.toString());
		}

	@Test 
	public void updateCompanyNullIso2Code() throws Exception {
		CompanyDTO companyDTO = new CompanyDTO();
		companyDTO.setCity("World city");
        	companyDTO.setCountry("World Union");
        	companyDTO.setIso2Code(null);
        	companyDTO.setCountryRegion("World region");
        	companyDTO.setLongitude(43.1);
        	companyDTO.setLatitude(43.1);
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanyBlankIso2Code() throws Exception {
		CompanyDTO companyDTO = new CompanyDTO();
		companyDTO.setCity("World city");
        	companyDTO.setCountry("World Union");
        	companyDTO.setIso2Code("");
        	companyDTO.setCountryRegion("World region");
        	companyDTO.setLongitude(43.1);
        	companyDTO.setLatitude(43.1);
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanySpaceIso2Code() throws Exception {
		CompanyDTO companyDTO = new CompanyDTO();
		companyDTO.setCity("World city");
        	companyDTO.setCountry("World Union");
        	companyDTO.setIso2Code(" ");
        	companyDTO.setCountryRegion("World region");
        	companyDTO.setLongitude(43.1);
        	companyDTO.setLatitude(43.1);
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanyNullUsername() throws Exception {
		CompanyDTO companyDTO = new CompanyDTO();
		companyDTO.setUsername(null);
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanyBlankUsername() throws Exception {
		CompanyDTO companyDTO = new CompanyDTO();
		companyDTO.setUsername("");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanySpaceUsername() throws Exception {
		CompanyDTO companyDTO = new CompanyDTO();
		companyDTO.setUsername(" ");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanyNullRole() throws Exception {
		CompanyDTO companyDTO = new CompanyDTO();
		companyDTO.setAccessRole(null);
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanyBlankRole() throws Exception {
		CompanyDTO companyDTO = new CompanyDTO();
		companyDTO.setAccessRole("");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanySpaceRole() throws Exception {
		CompanyDTO companyDTO = new CompanyDTO();
		companyDTO.setAccessRole(" ");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanyNullPassword() throws Exception {
		CompanyDTO companyDTO = new CompanyDTO();
		companyDTO.setPassword(null);
		companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanyBlankPassword() throws Exception {
		CompanyDTO companyDTO = new CompanyDTO();
		companyDTO.setPassword("");
		companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanySpacePassword() throws Exception {
		CompanyDTO companyDTO = new CompanyDTO();
		companyDTO.setPassword(" ");
		companyDTO.setConfirmedPassword("Test123");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanyNullConfirmedPassword() throws Exception {
		CompanyDTO companyDTO = new CompanyDTO();
		companyDTO.setPassword("Test123");
		companyDTO.setConfirmedPassword(null);
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanyBlankConfirmedPassword() throws Exception {
		CompanyDTO companyDTO = new CompanyDTO();
		companyDTO.setPassword("Test123");
		companyDTO.setConfirmedPassword("");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanySpaceConfirmedPassword() throws Exception {
		CompanyDTO companyDTO = new CompanyDTO();
		companyDTO.setPassword("Test123");
		companyDTO.setConfirmedPassword(" ");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
		}

	@Test 
	public void updateCompanyMobilePhoneAlreadyExists() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0642345678");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        //ResultActions result = 
		mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
        		//.andDo(MockMvcResultHandlers.print())
        		.andExpect(status().isNotAcceptable());
        //logger.info("updateCompanyMobilePhoneAlreadyExists: " + result.andReturn().getResponse().getContentAsString());
        //logger.info("updateCompanyMobilePhoneAlreadyExists: " + result.toString());
		}

	@Test 
	public void updateCompanyEmailAlreadyExists() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setEmail("Jobster@mail.com");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        //ResultActions result = 
		mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
        		//.andDo(MockMvcResultHandlers.print())
        		.andExpect(status().isNotAcceptable());
        //logger.info("updateCompanyEmailAlreadyExists: " + result.andReturn().getResponse().getContentAsString());
        //logger.info("updateCompanyEmailAlreadyExists: " + result.toString());
		}

	@Test 
	public void updateCompanyCompanyIdAlreadyExists() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setCompanyRegistrationNumber("11231231231232");

        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        //ResultActions result = 
		mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
        		//.andDo(MockMvcResultHandlers.print())
        		.andExpect(status().isNotAcceptable());
        //logger.info("updateCompanyCompanyIdAlreadyExists: " + result.andReturn().getResponse().getContentAsString());
        //logger.info("updateCompanyCompanyIdAlreadyExists: " + result.toString());
		}

	@Test 
	public void updateCompanyWrongAccessRole() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setAccessRole("ROLE_ADMIN");

        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        //ResultActions result = 
		mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
        		//.andDo(MockMvcResultHandlers.print())
        		.andExpect(status().isNotAcceptable());
        //logger.info("updateCompanyWrongAccessRole: " + result.andReturn().getResponse().getContentAsString());
        //logger.info("updateCompanyWrongAccessRole: " + result.toString());
		}

	@Test 
	public void updateCompanyUsernameAlreadyExists() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setUsername("Test1234");

        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        //ResultActions result = 
		mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
        		//.andDo(MockMvcResultHandlers.print())
        		.andExpect(status().isNotAcceptable());
        //logger.info("updateCompanyUsernameAlreadyExists: " + result.andReturn().getResponse().getContentAsString());
        //logger.info("updateCompanyUsernameAlreadyExists: " + result.toString());
		}

	@Test 
	public void archiveCompany() throws Exception {
        mockMvc.perform(put("/jobster/users/companies/archive/" + CompanyControllerTests.companies.get(0).getId()))
	 		.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.user.id", 
				is(CompanyControllerTests.companies.get(0).getId().intValue())))
			.andExpect(jsonPath("$.user.status", is(-1)));
        CompanyControllerTests.userAccounts.get(0).setStatusArchived();
        userAccountRepository.deleteById(CompanyControllerTests.userAccounts.get(0).getId());
		CompanyControllerTests.userAccounts.remove(CompanyControllerTests.userAccounts.get(0));
		CompanyControllerTests.companies.get(0).setStatusArchived();
		companyRepository.deleteById(CompanyControllerTests.companies.get(0).getId());
		CompanyControllerTests.companies.remove(CompanyControllerTests.companies.get(0));
		}

	@Test 
	public void archiveCompanyWhichNotExists() throws Exception {
		
        mockMvc.perform(put("/jobster/users/companies/archive/" + (CompanyControllerTests.companies.get(4).getId()+1)))
        	.andExpect(status().isNotFound());
		}	

	@Test 
	public void undeleteCompany() throws Exception {
        mockMvc.perform(put("/jobster/users/companies/undelete/" + CompanyControllerTests.companies.get(3).getId()))
        	//.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.companyName", 
				is(CompanyControllerTests.companies.get(3).getCompanyName())))
			.andExpect(jsonPath("$.status", is(1)));
        CompanyControllerTests.userAccounts.get(3).setStatusActive();
        userAccountRepository.deleteById(CompanyControllerTests.userAccounts.get(3).getId());
		CompanyControllerTests.userAccounts.remove(CompanyControllerTests.userAccounts.get(3));
		CompanyControllerTests.companies.get(3).setStatusActive();
		companyRepository.deleteById(CompanyControllerTests.companies.get(3).getId());
		CompanyControllerTests.companies.remove(CompanyControllerTests.companies.get(3));
		}

	@Test
	public void undeleteCompanyWhichNotExists() throws Exception {
		
        mockMvc.perform(put("/jobster/users/companies/undelete/" + (CompanyControllerTests.companies.get(4).getId()+1)))
        	.andExpect(status().isNotFound());
		}	

	@Test 
	public void deleteCompany() throws Exception {
		mockMvc.perform(delete("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId())) 
			.andExpect(status().isOk()) 
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.user.id", 
				is(CompanyControllerTests.companies.get(0).getId().intValue())))
			.andExpect(jsonPath("$.user.status", is(0)));
        CompanyControllerTests.userAccounts.get(0).setStatusInactive();
        userAccountRepository.deleteById(CompanyControllerTests.userAccounts.get(0).getId());
		CompanyControllerTests.userAccounts.remove(CompanyControllerTests.userAccounts.get(0));
		CompanyControllerTests.companies.get(0).setStatusInactive();
		companyRepository.deleteById(CompanyControllerTests.companies.get(0).getId());
		CompanyControllerTests.companies.remove(CompanyControllerTests.companies.get(0));
		}

	@Test 
	public void deleteCompanyWhichNotExists() throws Exception {
		
        mockMvc.perform(delete("/jobster/users/companies/" + (CompanyControllerTests.companies.get(4).getId()+1)))
        	.andExpect(status().isNotFound());
		}	

}
