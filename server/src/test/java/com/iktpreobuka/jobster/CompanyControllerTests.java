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
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.iktpreobuka.jobster.entities.CityEntity;
import com.iktpreobuka.jobster.entities.CompanyEntity;
import com.iktpreobuka.jobster.entities.CountryEntity;
import com.iktpreobuka.jobster.entities.CountryRegionEntity;
import com.iktpreobuka.jobster.entities.dto.CompanyDTO;
import com.iktpreobuka.jobster.repositories.CityRepository;
import com.iktpreobuka.jobster.repositories.CompanyRepository;
import com.iktpreobuka.jobster.repositories.CountryRegionRepository;
import com.iktpreobuka.jobster.repositories.CountryRepository;
import com.iktpreobuka.jobster.repositories.UserAccountRepository;

@RunWith(SpringRunner.class) 
@SpringBootTest 
@WebAppConfiguration 
@FixMethodOrder(MethodSorters.DEFAULT)
public class CompanyControllerTests {
 
	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(), 
			MediaType.APPLICATION_JSON.getSubtype(), 
			Charset.forName("utf8"));
	
	private static MockMvc mockMvc;
	
	@Autowired 
	private WebApplicationContext webApplicationContext;
	
	private static StringBuilder output = new StringBuilder("");
	
	private static CityEntity city;
	
	private static CountryEntity country;
	
	private static CountryRegionEntity region;
	
	private static List<CompanyEntity> companies = new ArrayList<>();
	
	@Autowired 
	private CompanyRepository companyRepository;
	
	@Autowired 
	private CityRepository cityRepository;
	
	@Autowired
	private CountryRegionRepository countryRegionRepository;

	@Autowired
	private CountryRepository countryRepository;
	
	@Autowired
	private UserAccountRepository userAccountRepository;
	

	private boolean dbInit = false;

	@Before
	public void setUp() throws Exception { 
		if(!dbInit) { mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build(); 
		country = countryRepository.save(new CountryEntity("Serbia", "SER", 1));
		region = countryRegionRepository.save(new CountryRegionEntity(country, "Vojvodina", 1));	
		city = cityRepository.save(new CityEntity(region, "Novi Sad", 33.3, 34.5, 1)); 
		CompanyControllerTests.companies.add(companyRepository.save(new CompanyEntity(city, "0644565858", "lidl@mail.com", "About Lidl", 1, "Lidl", "1231231231231")));
		CompanyControllerTests.companies.add(companyRepository.save(new CompanyEntity(city, "0644575858", "lidla@mail.com", "About Lidl", 1, "Lidla", "1241231231231")));
		CompanyControllerTests.companies.add(companyRepository.save(new CompanyEntity(city, "0644585858", "lidle@mail.com", "About Lidl", 1, "Lidle", "1251231231231")));
		dbInit = true;
		} 
	}
	
	@After
	public void tearDown() throws Exception {
		if(dbInit) {
		for (CompanyEntity cmpny : CompanyControllerTests.companies) {
			companyRepository.delete(cmpny);
		}
		CompanyControllerTests.companies.clear();
		cityRepository.delete(city); 
		countryRegionRepository.delete(region);	
		countryRepository.delete(country);
		dbInit = false;
		}
	}

		
	@Test 
	public void companyServiceNotFound() throws Exception { 
		output.append("a");
		mockMvc.perform(get("/companiestttttt/")).andExpect(status().isNotFound()); 
		}
	
	@Test 
	public void companyServiceFound() throws Exception { 
		output.append("b");
		mockMvc.perform(get("/jobster/users/companies/")).andExpect(status().isOk()); 
		}

	@Test 
	public void readSingleCompany() throws Exception { 
		output.append("c");
		mockMvc.perform(get("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId()))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id",
				is(CompanyControllerTests.companies.get(0).getId().intValue()))); }

	@Test 
	public void readAllCompanies() throws Exception { 
		output.append("d");
		mockMvc.perform(get("/jobster/users/companies/")) 
		.andExpect(status().isOk()) 
		.andExpect(content().contentType(contentType)) 
		.andDo(MockMvcResultHandlers.print());
	}

	@Test 
	public void createCompany() throws Exception {
		output.append("g");
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setMobilePhone("0638795858");
        companyDTO.setEmail("lidlo@mail.com");
        companyDTO.setDetailsLink("About Lidlo");
        companyDTO.setCompanyName("Lidlo");
        companyDTO.setCompanyId("9988776655443");
        companyDTO.setCity("Vrbas");
        companyDTO.setCountry("Serbia");
        companyDTO.setAccessRole("ROLE_USER");
        companyDTO.setIso2Code("SER");
        companyDTO.setCountryRegion("Vojvodina");
        companyDTO.setLongitude(43.1);
        companyDTO.setLatitude(43.1);
        companyDTO.setUsername("Testa12345678");
        companyDTO.setPassword("Test12345678");
        companyDTO.setConfirmedPassword("Test12345678");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        CompanyControllerTests.mockMvc.perform(post("/jobster/users/companies/").contentType(
                    MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isOk())
		.andExpect(content().contentType(contentType)) 
		.andExpect(jsonPath("$.username", is("Testa12345678")));
		CompanyControllerTests.companies.add(companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1)/*.save(new CompanyEntity(/*CompanyControllerTests.cities.get(0)*//*city, "0638795858", "lidlo@mail.com", "About Lidlo", 1, "Lidlo", "9988776655443"))*/);
		userAccountRepository.delete(userAccountRepository.getByUser(companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1)));
		}

	@Test 
	//@Transactional
	public void updateCompany() throws Exception {
		output.append("f");
		
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setCompanyName("Lidlt");
        companyDTO.setEmail("lidltrr@mail.com");
        
        Gson gson = new Gson();
        String json = gson.toJson(companyDTO);

        mockMvc.perform(put("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId())
        		.contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isOk())
		.andExpect(content().contentType(contentType)) 
		.andExpect(jsonPath("$.companyName", is("Lidlt"))) 
		.andExpect(jsonPath("$.email", is("lidltrr@mail.com")));
		//CompanyControllerTests.companies.get(0).setCompanyName(companyDTO.getCompanyName());
		//CompanyControllerTests.companies.get(0).setEmail(companyDTO.getEmail());
		CompanyControllerTests.companies.remove(0);
		CompanyControllerTests.companies.add(companyRepository.getByEmailAndStatusLike(companyDTO.getEmail(), 1));
		}
	
	@Test 
	public void deleteCompany() throws Exception {
		output.append("e");
		mockMvc.perform(delete("/jobster/users/companies/" + CompanyControllerTests.companies.get(0).getId())) 
		.andExpect(status().isOk()) 
		.andExpect(content().contentType(contentType)) 
		.andExpect(jsonPath("$.id", 
				is(CompanyControllerTests.companies.get(0).getId().intValue())));
		CompanyControllerTests.companies.get(0).setStatusInactive();
		companyRepository.deleteById(CompanyControllerTests.companies.get(0).getId());
		CompanyControllerTests.companies.remove(CompanyControllerTests.companies.get(0));
		}
	
}
