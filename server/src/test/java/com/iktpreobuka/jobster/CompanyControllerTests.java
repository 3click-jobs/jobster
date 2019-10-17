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
import com.iktpreobuka.jobster.repositories.CityDistanceRepository;
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
	
	@Autowired
	private CityDistanceRepository cityDistanceRepository;
	

	private boolean dbInit = false;

	@Before
	public void setUp() throws Exception { 
		if(!dbInit) { mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build(); 
			country = countryRepository.save(new CountryEntity("World Union", "XXX", 1));
			region = countryRegionRepository.save(new CountryRegionEntity(country, "World region", 1));	
			regionNoName = countryRegionRepository.save(new CountryRegionEntity(country, null, 1));	
			cityWhitoutRegion = cityRepository.save(new CityEntity(regionNoName, "World city without", 33.3, 34.5, 1)); 
			city = cityRepository.save(new CityEntity(region, "World city", 33.3, 34.5, 1));
			CompanyControllerTests.companies.add(companyRepository.save(new CompanyEntity(city, "0642345678", "Jobster@mail.com", "About Jobster", 1, "Jobster", "11231231231231")));
			CompanyControllerTests.companies.add(companyRepository.save(new CompanyEntity(city, "0643456789", "Jobsters@mail.com", "About Jobsters", 1, "Jobsters", "11231231231232")));
			CompanyControllerTests.companies.add(companyRepository.save(new CompanyEntity(city, "0644567890", "Jobstery@mail.com", "About Jobstery", 1, "Jobstery", "11231231231233")));
			company = new CompanyEntity(city, "0644567899", "Jobstery1@mail.com", "About Jobstery", 1, "Jobsterya", "11231231231235");
			company.setStatusInactive();
			CompanyControllerTests.companies.add(companyRepository.save(company));
			company = new CompanyEntity(city, "0644567898", "Jobstery2@mail.com", "About Jobstery", 1, "Jobsteryb", "11231231231234");
			company.setStatusArchived();
			CompanyControllerTests.companies.add(companyRepository.save(company));
			userAccount = userAccountRepository.save(new UserAccountEntity(CompanyControllerTests.companies.get(1), EUserRole.ROLE_USER, "Test1234", "Test1234", CompanyControllerTests.companies.get(0).getId()));
			dbInit = true;
		} 
	}
	
	@After
	public void tearDown() throws Exception {
		if(dbInit) {
			if (userAccount != null)
				userAccountRepository.delete(userAccount);
			for (CompanyEntity cmpny : CompanyControllerTests.companies) {
				companyRepository.delete(cmpny);
			}
			CompanyControllerTests.companies.clear();
			cityDistanceRepository.deleteByFromCity(city);
			cityRepository.delete(city); 
			cityDistanceRepository.deleteByFromCity(cityWhitoutRegion);
			cityRepository.delete(cityWhitoutRegion); 
			countryRegionRepository.delete(region);	
			countryRegionRepository.delete(regionNoName);	
			countryRepository.delete(country);
			dbInit = false;
		}
	}

	@Test 
	public void companyServiceNotFound() throws Exception { 
		mockMvc.perform(get("/jobster/users/companies/readallcompanies/"))
			.andExpect(status().isNotFound()); 
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
			//.andDo(MockMvcResultHandlers.print());
		}

	@Test 
	public void readAllCompaniesNotFound() throws Exception { 
		
		userAccountRepository.delete(userAccount);
		userAccount = null;
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
			//.andDo(MockMvcResultHandlers.print());
		}

	@Test 
	public void readAllDeletedCompaniesNotFound() throws Exception { 
		
		/*CompanyRepository mockCompanyRepository = mock(CompanyRepository.class);
		when(mockCompanyRepository.findByStatusLike(0)).thenReturn(null);*/
		
		//when(companyDao.findCompanyByStatusLike(0)).thenReturn(null);

		userAccountRepository.delete(userAccount);
		userAccount = null;
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
			//.andDo(MockMvcResultHandlers.print());
		}

	@Test 
	public void readAllArchivedCompaniesNotFound() throws Exception { 
		
		userAccountRepository.delete(userAccount);
		userAccount = null;
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
	        .andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.username", is("Test123")));
		CompanyControllerTests.companies.add(companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1)/*.save(new CompanyEntity(/*CompanyControllerTests.cities.get(0)*//*city, "0638795858", "lidlo@mail.com", "About Lidlo", 1, "Lidlo", "9988776655443"))*/);
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
	        .andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.username", is("Test123")));
		CompanyControllerTests.companies.add(companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1)/*.save(new CompanyEntity(/*CompanyControllerTests.cities.get(0)*//*city, "0638795858", "lidlo@mail.com", "About Lidlo", 1, "Lidlo", "9988776655443"))*/);
		userAccountRepository.delete(userAccountRepository.getByUser(companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1)));
		}

	@Test 
	public void createCompanyValidationError() throws Exception {
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
	public void updateCompany() throws Exception {
		
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setCompanyName("Jobsty");
        companyDTO.setEmail("Jobsty@mail.com");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId())
        		.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.companyName", is("Jobsty"))) 
			.andExpect(jsonPath("$.email", is("Jobsty@mail.com")));
		CompanyControllerTests.companies.remove(0);
		CompanyControllerTests.companies.add(companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1));
		}

	@Test 
	public void shouldReturn404WhenTryPartialUpdateCompanyWhichNotExists() throws Exception {
		
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setCompanyName("Jobsty");
        companyDTO.setEmail("Jobsty@mail.com");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + (CompanyControllerTests.companies.get(4).getId()+1))
        		.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isNotFound());
		}	

	@Test 
	public void updateCompanyValidationError() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setCompanyName("Jobsty");
        companyDTO.setEmail("JobstyXmail");
        
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
	public void updateCompanyMobilePhoneAlreadyExists() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0642345678");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isNotAcceptable());
		}

	@Test 
	public void updateCompanyEmailAlreadyExists() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setEmail("Jobster@mail.com");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isNotAcceptable());
		}

	@Test 
	public void updateCompanyCompanyIdAlreadyExists() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setCompanyRegistrationNumber("11231231231232");

        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isNotAcceptable());
		}

	@Test 
	public void updateCompanyWrongAccessRole() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setAccessRole("ROLE_ADMIN");

        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isNotAcceptable());
		}

	@Test 
	public void updateCompanyUsernameAlreadyExists() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setUsername("Test1234");

        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()).contentType(
                    MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isNotAcceptable());
		}

	@Test 
	public void archiveCompany() throws Exception {
        mockMvc.perform(put("/jobster/users/companies/archive/" + CompanyControllerTests.companies.get(0).getId()))
	 		.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.id", 
				is(CompanyControllerTests.companies.get(0).getId().intValue())))
			.andExpect(jsonPath("$.status", is(-1)));
		CompanyControllerTests.companies.get(0).setStatusArchived();
		companyRepository.deleteById(CompanyControllerTests.companies.get(0).getId());
		CompanyControllerTests.companies.remove(CompanyControllerTests.companies.get(0));
		}

	@Test 
	public void shouldReturn404WhenTryArchiveCompanyWhichNotExists() throws Exception {
		
        mockMvc.perform(put("/jobster/users/companies/archive/" + (CompanyControllerTests.companies.get(4).getId()+1)))
        	.andExpect(status().isNotFound());
		}	

	/*@Test 
	public void shouldReturnForbiddenWhenCompanyTryToArchiveItself() throws Exception {
		output.append("f");
		
		CompanyEntity mockCompany = CompanyControllerTests.companies.get(0);
		when(mockUserRepository.getByIdAndStatusLike(CompanyControllerTests.companies.get(0).getId(), 1).thenReturn(Optional.of(mockCompany)));

        mockMvc.perform(put("/jobster/users/companies/archive/" + CompanyControllerTests.companies.get(0).getId()))
        	.andExpect(status().isForbidden());
		}	*/

	@Test 
	public void undeleteCompany() throws Exception {
        mockMvc.perform(put("/jobster/users/companies/undelete/" + CompanyControllerTests.companies.get(3).getId()))
	 		.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.id", 
				is(CompanyControllerTests.companies.get(3).getId().intValue())))
			.andExpect(jsonPath("$.status", is(1)));
		CompanyControllerTests.companies.get(3).setStatusActive();
		companyRepository.deleteById(CompanyControllerTests.companies.get(3).getId());
		CompanyControllerTests.companies.remove(CompanyControllerTests.companies.get(3));
		}

	@Test
	public void shouldReturn404WhenTryUndeleteCompanyWhichNotExists() throws Exception {
		
        mockMvc.perform(put("/jobster/users/companies/undelete/" + (CompanyControllerTests.companies.get(4).getId()+1)))
        	.andExpect(status().isNotFound());
		}	

	@Test 
	public void deleteCompany() throws Exception {
		mockMvc.perform(delete("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId())) 
			.andExpect(status().isOk()) 
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.id", 
				is(CompanyControllerTests.companies.get(0).getId().intValue())))
			.andExpect(jsonPath("$.status", is(0)));
		CompanyControllerTests.companies.get(0).setStatusInactive();
		companyRepository.deleteById(CompanyControllerTests.companies.get(0).getId());
		CompanyControllerTests.companies.remove(CompanyControllerTests.companies.get(0));
		}

	@Test 
	public void shouldReturn404WhenTryDeleteCompanyWhichNotExists() throws Exception {
		
        mockMvc.perform(delete("/jobster/users/companies/" + (CompanyControllerTests.companies.get(4).getId()+1)))
        	.andExpect(status().isNotFound());
		}	

	/*@Test 
	public void shouldReturnForbiddenWhenCompanyTryToDeleteItself() throws Exception {
		output.append("f");
		
		CompanyEntity mockCompany = CompanyControllerTests.companies.get(0);
		CompanyRepository mockCompanyRepository = Mockito.mock(CompanyRepository.class);
		Mockito.when(mockCompanyRepository.getByIdAndStatusLike(CompanyControllerTests.companies.get(0).getId(), 1).thenReturn(Optional.of(mockCompany)));

        mockMvc.perform(delete("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()))
        	.andExpect(status().isForbidden());
		}	*/

	
}
