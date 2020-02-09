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
import com.iktpreobuka.jobster.entities.JobTypeEntity;
import com.iktpreobuka.jobster.entities.UserAccountEntity;
import com.iktpreobuka.jobster.entities.UserEntity;
import com.iktpreobuka.jobster.entities.dto.JobTypeDTO;
import com.iktpreobuka.jobster.enumerations.EUserRole;
import com.iktpreobuka.jobster.repositories.CityRepository;
import com.iktpreobuka.jobster.repositories.CountryRegionRepository;
import com.iktpreobuka.jobster.repositories.CountryRepository;
import com.iktpreobuka.jobster.repositories.JobTypeRepository;
import com.iktpreobuka.jobster.repositories.UserAccountRepository;
import com.iktpreobuka.jobster.repositories.UserRepository;

//@Ignore
@RunWith(SpringRunner.class) 
@SpringBootTest
@WebAppConfiguration 
public class JobTypeControllerTest {

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(), 
			MediaType.APPLICATION_JSON.getSubtype(), 
			Charset.forName("utf8"));
	
	private static MockMvc mockMvc;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;
    
	@Autowired 
	private WebApplicationContext webApplicationContext;
	
	@Autowired 
	private UserRepository userRepository;
	
	@Autowired 
	private CityRepository cityRepository;
	
	@Autowired
	private CountryRegionRepository countryRegionRepository;

	@Autowired
	private CountryRepository countryRepository;
	
	@Autowired
	private UserAccountRepository userAccountRepository;
	
	@Autowired
	private JobTypeRepository jobTypeRepository;

	private String token;
	
	private boolean dbInit = false;
	
	private static CityEntity city;

	private static CountryEntity country;
	
	private static CountryRegionEntity region;
	
//	private static JobTypeEntity jobType;
	
	private static List<UserEntity> users = new ArrayList<>();
	
	private static List<UserAccountEntity> userAccounts = new ArrayList<>();
	
	private static List<CountryEntity> countries = new ArrayList<>();

	private static List<CountryRegionEntity> countryRegions = new ArrayList<>();

	private static List<CityEntity> cities = new ArrayList<>();
	
	private static List<JobTypeEntity> jobTypes = new ArrayList<>();

	
	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
	
	
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
			city = cityRepository.save(new CityEntity(region, "World city", 33.3, 34.5));
			cities.add(city);
			users.add(userRepository.save(new UserEntity(city, "0642345678", "Jobster@mail.com", "About Jobster")));
			userAccounts.add(userAccountRepository.save(new UserAccountEntity(users.get(0), EUserRole.ROLE_ADMIN, "Test1234", "{bcrypt}$2a$10$FZjQbu7AqcSp0ns.GAxkbu0eKVUtNFTZNdVWwPOtBATLF0Bs9wtW2", users.get(0).getId())));
			jobTypes.add(jobTypeRepository.save(new JobTypeEntity("Zidar", 1, users.get(0).getId())));
			jobTypes.add(jobTypeRepository.save(new JobTypeEntity("Tesar", 1, users.get(0).getId())));
			jobTypes.add(jobTypeRepository.save(new JobTypeEntity("Mesar", 1, users.get(0).getId())));
			jobTypes.add(jobTypeRepository.save(new JobTypeEntity("Klesar", 0, users.get(0).getId())));
			jobTypes.add(jobTypeRepository.save(new JobTypeEntity("Pisar", -1, users.get(0).getId())));
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

			logger.info("DBsetUp ok");
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
			for (JobTypeEntity jtp : jobTypes) {
				jobTypeRepository.delete(jtp);
			}
			jobTypes.clear();	
			dbInit = false;
			token = null;
			logger.info("DBtearDown ok");
		}
	}

	@Test
	public void jobTypeServiceNotFound() throws Exception { 
		logger.info("jobTypeServiceNotFound");
		mockMvc.perform(get("/jobster/jobtypes/readalljobtypes/")
			.header("Authorization", "Bearer " + token))
			.andExpect(status().is4xxClientError());
	}
	
	@Test 
	public void jobTypeServiceFound() throws Exception { 
		logger.info("jobTypeServiceFound");
		mockMvc.perform(get("/jobster/jobtypes/")
			.header("Authorization", "Bearer " + token))
			.andExpect(status().isOk()); 
		}

	@Test 
	public void readAllJobTypes() throws Exception {  
		logger.info("readAllJobTypes");
		mockMvc.perform(get("/jobster/jobtypes/")
			.header("Authorization", "Bearer " + token)) 
			.andExpect(status().isOk()) 
			.andExpect(content().contentType(contentType));
		}

	@Test 
	public void readAllJobTypesNotFound() throws Exception {  
		logger.info("readAllJobTypesNotFound");		
		for (JobTypeEntity jtp : jobTypes) {
			jobTypeRepository.delete(jtp);
		}
		jobTypes.clear();
		mockMvc.perform(get("/jobster/jobtypes/")
			.header("Authorization", "Bearer " + token))
			.andExpect(status().isNotFound()); 
		}
	
	@Test 
	public void readAllDeletedJobTypes() throws Exception {  
		logger.info("readAllDeletedJobTypes");
		mockMvc.perform(get("/jobster/jobtypes/deleted/")
			.header("Authorization", "Bearer " + token)) 
			.andExpect(status().isOk()) 
			.andExpect(content().contentType(contentType));
		}

	@Test
	public void readAllDeletedJobTypesNotFound() throws Exception {  
		logger.info("readAllDeletedJobTypesNotFound");		
		for (JobTypeEntity jtp : jobTypes) {
			jobTypeRepository.delete(jtp);
		}
		jobTypes.clear();
		mockMvc.perform(get("/jobster/jobtypes/deleted/")
			.header("Authorization", "Bearer " + token))
			.andExpect(status().isNotFound()); 
		}
	
	@Test
	public void readAllArchivedJobTypes() throws Exception {  
		logger.info("readAllArchivedJobTypes");
		mockMvc.perform(get("/jobster/jobtypes/archived/")
			.header("Authorization", "Bearer " + token)) 
			.andExpect(status().isOk()) 
			.andExpect(content().contentType(contentType));
		}

	@Test
	public void readAllArchivedJobTypesNotFound() throws Exception {  
		logger.info("readAllArchivedJobTypesNotFound");		
		for (JobTypeEntity jtp : jobTypes) {
			jobTypeRepository.delete(jtp);
		}
		jobTypes.clear();
		mockMvc.perform(get("/jobster/jobtypes/archived/")
			.header("Authorization", "Bearer " + token))
			.andExpect(status().isNotFound()); 
		}
	
	@Test
	public void createJobType() throws Exception {
		logger.info("createJobType");
        JobTypeDTO jobTypeDTO = new JobTypeDTO();
        jobTypeDTO.setJobTypeName("Maser");
        
        Gson gson = new Gson();
        String json = gson.toJson(jobTypeDTO);

        mockMvc.perform(post("/jobster/jobtypes/")
    		.contentType(MediaType.APPLICATION_JSON).content(json)
			.header("Authorization", "Bearer " + token))
        	//.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.jobTypeName", is("Maser")));
        jobTypes.add(jobTypeRepository.getByJobTypeNameAndStatusLike(jobTypeDTO.getJobTypeName(), 1));
		}
	
	@Test 
	public void createJobTypeMarginalValueJobTypeName() throws Exception {
		logger.info("createJobTypeMarginalValueJobTypeName");
        JobTypeDTO jobTypeDTO = new JobTypeDTO();
        jobTypeDTO.setJobTypeName("It");
        
        Gson gson = new Gson();
        String json = gson.toJson(jobTypeDTO);

        mockMvc.perform(post("/jobster/jobtypes/")
    		.contentType(MediaType.APPLICATION_JSON).content(json)
			.header("Authorization", "Bearer " + token))
        	//.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.jobTypeName", is("It")));
        jobTypes.add(jobTypeRepository.getByJobTypeNameAndStatusLike(jobTypeDTO.getJobTypeName(), 1));
		}

	@Test 
	public void createJobTypeValidationErrorJobTypeNameOneChar() throws Exception {
		logger.info("createJobTypeValidationErrorJobTypeNameOneChar");
        JobTypeDTO jobTypeDTO = new JobTypeDTO();
        jobTypeDTO.setJobTypeName("J");
        
        Gson gson = new Gson();
        String json = gson.toJson(jobTypeDTO);

        mockMvc.perform(post("/jobster/jobtypes/")
    		.contentType(MediaType.APPLICATION_JSON).content(json)
			.header("Authorization", "Bearer " + token))
	        .andExpect(status().isBadRequest());
		}
	
	@Test 
	public void createJobTypeValidationErrorContainsNotOnlyCorrectChars() throws Exception {
		logger.info("createJobTypeValidationErrorContainsNotOnlyCorrectChars");
        JobTypeDTO jobTypeDTO = new JobTypeDTO();
        jobTypeDTO.setJobTypeName("Maser+ka");
        
        Gson gson = new Gson();
        String json = gson.toJson(jobTypeDTO);

        mockMvc.perform(post("/jobster/jobtypes/")
    		.contentType(MediaType.APPLICATION_JSON).content(json)
			.header("Authorization", "Bearer " + token))
	        .andExpect(status().isBadRequest());
		}
	
	@Test 
	public void createNullJobType() throws Exception {
		logger.info("createNullJobType");
        JobTypeDTO jobTypeDTO = null;
        
        Gson gson = new Gson();
        String json = gson.toJson(jobTypeDTO);

        mockMvc.perform(post("/jobster/jobtypes/")
    		.contentType(MediaType.APPLICATION_JSON).content(json)
			.header("Authorization", "Bearer " + token))
	        .andExpect(status().isBadRequest());
		}
	
	@Test 
	public void createJobTypeNullJobTypeName() throws Exception {
		logger.info("createJobTypeNullJobTypeName");
        JobTypeDTO jobTypeDTO = new JobTypeDTO();
        jobTypeDTO.setJobTypeName(null);
        
        Gson gson = new Gson();
        String json = gson.toJson(jobTypeDTO);

        mockMvc.perform(post("/jobster/jobtypes/")
    		.contentType(MediaType.APPLICATION_JSON).content(json)
			.header("Authorization", "Bearer " + token))
	        .andExpect(status().isBadRequest());
		}
	
	@Test 
	public void createJobTypeBlankJobTypeName() throws Exception {
		logger.info("createJobTypeBlankJobTypeName");
        JobTypeDTO jobTypeDTO = new JobTypeDTO();
        jobTypeDTO.setJobTypeName("");
        
        Gson gson = new Gson();
        String json = gson.toJson(jobTypeDTO);

        mockMvc.perform(post("/jobster/jobtypes/")
    		.contentType(MediaType.APPLICATION_JSON).content(json)
			.header("Authorization", "Bearer " + token))
	        .andExpect(status().isBadRequest());
		}
	
	@Test 
	public void createJobTypeSpaceJobTypeName() throws Exception {
		logger.info("createJobTypeSpaceJobTypeName");
        JobTypeDTO jobTypeDTO = new JobTypeDTO();
        jobTypeDTO.setJobTypeName(" ");
        
        Gson gson = new Gson();
        String json = gson.toJson(jobTypeDTO);

        mockMvc.perform(post("/jobster/jobtypes/")
    		.contentType(MediaType.APPLICATION_JSON).content(json)
			.header("Authorization", "Bearer " + token))
	        .andExpect(status().isBadRequest());
		}
	
	@Test 
	public void createJobTypeNameAlreadyExists() throws Exception {
		logger.info("createJobTypeNameAlreadyExists");
        JobTypeDTO jobTypeDTO = new JobTypeDTO();
        jobTypeDTO.setJobTypeName("Mesar");
        
        Gson gson = new Gson();
        String json = gson.toJson(jobTypeDTO);

        mockMvc.perform(post("/jobster/jobtypes/")
    		.contentType(MediaType.APPLICATION_JSON).content(json)
			.header("Authorization", "Bearer " + token))
	        .andExpect(status().isNotAcceptable());
		}
	
	@Test 
	public void updateJobTypeWhichNotExists() throws Exception {
			logger.info("updateJobTypeWhichNotExists");
	        JobTypeDTO jobTypeDTO = new JobTypeDTO();
	        jobTypeDTO.setJobTypeName("Maser");
        
        	Gson gson = new Gson();
        	String json = gson.toJson(jobTypeDTO);

			mockMvc.perform(put("/jobster/jobtypes/" + (jobTypes.get(4).getId()+1))
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON).content(json))
    			//.andDo(MockMvcResultHandlers.print())
    			.andExpect(status().isNotFound());
	}	
	
	@Test 
	public void updateJobTypeName() throws Exception {
		logger.info("updateJobTypeName");		
        JobTypeDTO jobTypeDTO = new JobTypeDTO();
        jobTypeDTO.setJobTypeName("Maser");
        
        Gson gson = new Gson();
        String json = gson.toJson(jobTypeDTO);

		mockMvc.perform(put("/jobster/jobtypes/" + jobTypes.get(0).getId())
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
			//.andDo(MockMvcResultHandlers.print())
	        .andExpect(status().isOk())
			.andExpect(content().contentType(contentType))
			.andExpect(jsonPath("$.jobTypeName", is("Maser")));
		Integer Id = jobTypes.get(0).getId();
		jobTypes.remove(0);
		jobTypes.add(jobTypeRepository.findByIdAndStatusLike(Id, 1));
	}
	
	@Test 
	public void updateJobTypeNameMarginalValue() throws Exception {
		logger.info("updateJobTypeNameMarginalValue");		
        JobTypeDTO jobTypeDTO = new JobTypeDTO();
        jobTypeDTO.setJobTypeName("It");
        
        Gson gson = new Gson();
        String json = gson.toJson(jobTypeDTO);

		mockMvc.perform(put("/jobster/jobtypes/" + jobTypes.get(0).getId())
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
			//.andDo(MockMvcResultHandlers.print())
	        .andExpect(status().isOk())
			.andExpect(content().contentType(contentType))
			.andExpect(jsonPath("$.jobTypeName", is("It")));
		Integer Id = jobTypes.get(0).getId();
		jobTypes.remove(0);
		jobTypes.add(jobTypeRepository.findByIdAndStatusLike(Id, 1));
	}
	
	@Test 
	public void updateJobTypeValidationErrorJobTypeNameOneChar() throws Exception {
		logger.info("updateJobTypeValidationErrorJobTypeNameOneChar");		
        JobTypeDTO jobTypeDTO = new JobTypeDTO();
        jobTypeDTO.setJobTypeName("J");
        
        Gson gson = new Gson();
        String json = gson.toJson(jobTypeDTO);

		mockMvc.perform(put("/jobster/jobtypes/" + jobTypes.get(0).getId())
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}
	
	@Test 
	public void updateJobTypeValidationErrorContainsNotOnlyCorrectChars() throws Exception {
		logger.info("updateJobTypeValidationErrorContainsNotOnlyCorrectChars");		
        JobTypeDTO jobTypeDTO = new JobTypeDTO();
        jobTypeDTO.setJobTypeName("Maser+ka");
        
        Gson gson = new Gson();
        String json = gson.toJson(jobTypeDTO);

		mockMvc.perform(put("/jobster/jobtypes/" + jobTypes.get(0).getId())
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
        	.andExpect(status().isBadRequest());
	}
	
	@Test 
	public void updateNullJobType() throws Exception {
		logger.info("updateNullJobType");
        JobTypeDTO jobTypeDTO = null;
        
        Gson gson = new Gson();
        String json = gson.toJson(jobTypeDTO);

        mockMvc.perform(put("/jobster/jobtypes/" + jobTypes.get(0).getId())
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}
	
	@Test 
	public void updateJobTypeNullJobTypeName() throws Exception {
		logger.info("updateJobTypeNullJobTypeName");
        JobTypeDTO jobTypeDTO = new JobTypeDTO();
        jobTypeDTO.setJobTypeName(null);
        
        Gson gson = new Gson();
        String json = gson.toJson(jobTypeDTO);

        mockMvc.perform(put("/jobster/jobtypes/" + jobTypes.get(0).getId())
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test 
	public void updateJobTypeBlankJobTypeName() throws Exception {
		logger.info("updateJobTypeBlankJobTypeName");
        JobTypeDTO jobTypeDTO = new JobTypeDTO();
        jobTypeDTO.setJobTypeName("");
        
        Gson gson = new Gson();
        String json = gson.toJson(jobTypeDTO);

        mockMvc.perform(put("/jobster/jobtypes/" + jobTypes.get(0).getId())
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test 
	public void updateJobTypeSpaceJobTypeName() throws Exception {
		logger.info("updateJobTypeSpaceJobTypeName");
        JobTypeDTO jobTypeDTO = new JobTypeDTO();
        jobTypeDTO.setJobTypeName(" ");
        
        Gson gson = new Gson();
        String json = gson.toJson(jobTypeDTO);

        mockMvc.perform(put("/jobster/jobtypes/" + jobTypes.get(0).getId())
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test 
	public void updateJobTypeNameAlreadyExists() throws Exception {
		logger.info("updateJobTypeNameAlreadyExists");
        JobTypeDTO jobTypeDTO = new JobTypeDTO();
        jobTypeDTO.setJobTypeName("Mesar");
        
        Gson gson = new Gson();
        String json = gson.toJson(jobTypeDTO);

		mockMvc.perform(put("/jobster/jobtypes/" + jobTypes.get(0).getId())
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(json))
    		//.andDo(MockMvcResultHandlers.print())
    		.andExpect(status().isNotAcceptable());
	}
	
	@Test 
	public void archiveJobType() throws Exception {
		logger.info("archiveJobType");
        mockMvc.perform(put("/jobster/jobtypes/archive/" + jobTypes.get(0).getId())
    		.header("Authorization", "Bearer " + token))
	 		.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.id", is(jobTypes.get(0).getId().intValue())))
			.andExpect(jsonPath("$.status", is(-1)));
        jobTypes.get(0).setStatusArchived();
        jobTypeRepository.deleteById(jobTypes.get(0).getId());
        jobTypes.remove(jobTypes.get(0));
	}

	@Test 
	public void archiveJobTypeWhichNotExists() throws Exception {
		logger.info("archiveJobTypeWhichNotExists");
        mockMvc.perform(put("/jobster/jobtypes/archive/" + (jobTypes.get(4).getId()+1))
    		.header("Authorization", "Bearer " + token))
        	.andExpect(status().isNotFound());
	}	
	
	@Test 
	public void undeleteJobType() throws Exception {
		logger.info("undeleteJobType");
        mockMvc.perform(put("/jobster/jobtypes/undelete/" + jobTypes.get(3).getId())
    		.header("Authorization", "Bearer " + token))
        	//.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.jobTypeName", is(jobTypes.get(3).getJobTypeName())))
			.andExpect(jsonPath("$.status", is(1)));
        jobTypes.get(3).setStatusActive();
        jobTypeRepository.deleteById(jobTypes.get(3).getId());
        jobTypes.remove(jobTypes.get(3));
	}
	
	@Test
	public void undeleteJobTypeWhichNotExists() throws Exception {
		logger.info("undeleteJobTypeWhichNotExists");
        mockMvc.perform(put("/jobster/jobtypes/undelete/" + (jobTypes.get(4).getId()+1))
    		.header("Authorization", "Bearer " + token))
        	.andExpect(status().isNotFound());
	}	
	
	@Test 
	public void deleteJobType() throws Exception {
		logger.info("deleteJobType");
		mockMvc.perform(delete("/jobster/jobtypes/" + jobTypes.get(0).getId())
    		.header("Authorization", "Bearer " + token)) 
			.andExpect(status().isOk()) 
			.andExpect(content().contentType(contentType)) 
			.andExpect(jsonPath("$.id", is(jobTypes.get(0).getId().intValue())))
			.andExpect(jsonPath("$.status", is(0)));
		jobTypes.get(0).setStatusInactive();
		jobTypeRepository.deleteById(jobTypes.get(0).getId());
		jobTypes.remove(jobTypes.get(0));
	}
	
	@Test 
	public void deleteJobTypeWhichNotExists() throws Exception {
		logger.info("deleteJobTypeWhichNotExists");
        mockMvc.perform(delete("/jobster/jobtypes/" + (jobTypes.get(4).getId()+1))
    		.header("Authorization", "Bearer " + token))
        	.andExpect(status().isNotFound());
	}
	
}
