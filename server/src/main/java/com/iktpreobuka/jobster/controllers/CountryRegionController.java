package com.iktpreobuka.jobster.controllers;

import java.security.Principal;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iktpreobuka.jobster.controllers.util.RESTError;
import com.iktpreobuka.jobster.entities.CountryEntity;
import com.iktpreobuka.jobster.entities.CountryRegionEntity;
import com.iktpreobuka.jobster.entities.UserAccountEntity;
import com.iktpreobuka.jobster.entities.UserEntity;
import com.iktpreobuka.jobster.repositories.CountryRegionRepository;
import com.iktpreobuka.jobster.repositories.CountryRepository;
import com.iktpreobuka.jobster.repositories.UserAccountRepository;
import com.iktpreobuka.jobster.services.CountryRegionDao;

@Controller
@RestController
@RequestMapping(value = "/jobster/regions")

public class CountryRegionController {

	
	@Autowired 	
	private CountryRegionRepository countryRegionRepository;
	
	@Autowired 	
	private CountryRepository countryRepository;
		
	@Autowired 
	private CountryRegionDao countryRegionDao;

	@Autowired
	private UserAccountRepository userAccountRepository;
  

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));

		}
	
	//@Secured("ROLE_ADMIN")
	//@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/getById/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/regions/getById started.");
		logger.info("Logged username: " + principal.getName());
		try {
			CountryRegionEntity region = countryRegionRepository.getById(id);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<CountryRegionEntity>(region, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	//@Secured("ROLE_ADMIN")
	//@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/getAll")
	public ResponseEntity<?> getAll(Principal principal) {
		logger.info("################ /jobster/regions/getAll started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<CountryRegionEntity> regions = countryRegionRepository.findAll();
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<CountryRegionEntity>>(regions, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// @Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/active")
	public ResponseEntity<?> getAllActive(Principal principal) {
		logger.info("################ /jobster/regions/getAllActive started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<CountryRegionEntity> regions = countryRegionDao.findRegionByStatusLike(1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<CountryRegionEntity>>(regions, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// @Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/inactive")
	public ResponseEntity<?> getAllInactive(Principal principal) {
		logger.info("################ /jobster/regions/getAllInactive started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<CountryRegionEntity> regions = countryRegionDao.findRegionByStatusLike(0);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<CountryRegionEntity>>(regions, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// @Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived")
	public ResponseEntity<?> getAllArchived(Principal principal) {
		logger.info("################ /jobster/regions/getAllArchived started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<CountryRegionEntity> regions = countryRegionDao.findRegionByStatusLike(-1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<CountryRegionEntity>>(regions, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

			
	//@Secured("ROLE_ADMIN")
	//@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/getByName/{name}")
	public ResponseEntity<?> getByNameAll(@PathVariable String name, Principal principal) {
		logger.info("################ /jobster/regions/getByName started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<CountryRegionEntity> regions = countryRegionRepository.getByCountryRegionNameIgnoreCase(name);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<CountryRegionEntity>>(regions, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// @Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.POST, value = "/addNewRegion")
	public ResponseEntity<?> addNewRegion(@Valid @RequestBody CountryRegionEntity newRegion, Principal principal,
			BindingResult result) {
		logger.info("################ /jobster/regions/addNewRegion started.");

		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		if (newRegion == null) {
			logger.info("---------------- New region is null.");
			return new ResponseEntity<>("New region is null.", HttpStatus.BAD_REQUEST);
		}

		if (newRegion.getCountry() == null ) {
			logger.info("---------------- Some atributes are null.");
			return new ResponseEntity<>("Some atributes are null", HttpStatus.BAD_REQUEST);
		}
		
		if (countryRegionRepository.existsByCountryRegionNameAndCountry(newRegion.getCountryRegionName(), countryRepository.getByCountryName(newRegion.getCountry().getCountryName()))) {
		logger.info("---------------- Region already exists.");
        return new ResponseEntity<>("Region already exists.", HttpStatus.NOT_ACCEPTABLE);
		}
		
		try {

			String countryRegionName=newRegion.getCountryRegionName();
			CountryEntity country= newRegion.getCountry();
			UserAccountEntity loggedUserAccount=userAccountRepository.getByUsername(principal.getName());
			UserEntity loggedUser=loggedUserAccount.getUser();
			countryRegionDao.addNewCountryRegionWithLoggedUser(countryRegionName, country, loggedUser);
			logger.info("New region created.");
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(newRegion, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(
					new RESTError(2, "Number format exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ This is an exception message: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// @Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/modify/{id}")

	public ResponseEntity<?> modifyRegion(@PathVariable Integer id, @Valid @RequestBody CountryRegionEntity updateRegion, Principal principal, BindingResult result) {
		logger.info("################ /jobster/regions/modify/{id} started.");
		try {
			CountryRegionEntity region=countryRegionRepository.getById(id);
			logger.info("Logged user: " + principal.getName());
		if (region == null) {
			logger.info("---------------- Region doesn't exists.");
	        return new ResponseEntity<>("Region doesn't exists.", HttpStatus.BAD_REQUEST);
	      }
		
		region.setCountryRegionName(updateRegion.getCountryRegionName());
		CountryEntity country=updateRegion.getCountry();
		region.setCountry((countryRepository.getByCountryNameIgnoreCase(country.getCountryName())));

		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		
		if ( updateRegion.getCountry() == null ) {
			logger.info("---------------- Some or all atributes are null.");
			return new ResponseEntity<>("Some or all atributes are null", HttpStatus.BAD_REQUEST);
		}
		countryRegionRepository.save(region);
		logger.info("Region updated.");
		logger.info("---------------- Finished OK.");
		return new ResponseEntity<>(updateRegion, HttpStatus.OK);
		}
		catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	// @Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/archive/{id}")
	public ResponseEntity<?> archive(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/regions/archive/{id} Archive started.");
		logger.info("Logged user: " + principal.getName());
		CountryRegionEntity region = new CountryRegionEntity();
		try {
			region = countryRegionRepository.getById(id);
			if (region == null || region.getStatus() == -1) {
				logger.info("---------------- Region not found.");
				return new ResponseEntity<>("Region not found.", HttpStatus.NOT_FOUND);
			}
			logger.info("Region for archiving identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			countryRegionDao.archiveRegion(loggedUser, region);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<CountryRegionEntity>(region, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(
					new RESTError(2, "Number format exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// @Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/unarchive/{id}")
	public ResponseEntity<?> unArchive(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/regions/unarchive/{id} Unarchive started.");
		logger.info("Logged user: " + principal.getName());
		CountryRegionEntity region = new CountryRegionEntity();
		try {
			region = countryRegionRepository.findByIdAndStatusLike(id, -1);
			if (region == null) {

				logger.info("---------------- Region not found.");
		        return new ResponseEntity<>("Region not found.", HttpStatus.NOT_FOUND);
		      }
			logger.info("Region for unarchiving identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			countryRegionDao.unarchiveRegion(loggedUser, region);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<CountryRegionEntity>(region, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(
					new RESTError(2, "Number format exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// @Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/undelete/{id}")
	public ResponseEntity<?> unDelete(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/regions/undelete/{id} unDelete started.");
		logger.info("Logged user: " + principal.getName());
		CountryRegionEntity region = new CountryRegionEntity();
		try {
			region = countryRegionRepository.findByIdAndStatusLike(id, 0);
			if (region == null) {
				logger.info("---------------- Region not found.");
				return new ResponseEntity<>("Region not found.", HttpStatus.NOT_FOUND);
			}
			logger.info("Region for undeleting identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			countryRegionDao.undeleteRegion(loggedUser, region);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<CountryRegionEntity>(region, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(
					new RESTError(2, "Number format exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	//@Secured("ROLE_ADMIN")
	//@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.DELETE, value = "delete/{id}")
	public ResponseEntity<?> deleteRegion(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/regions/{id} Delete started.");
		logger.info("Logged user: " + principal.getName());
		CountryRegionEntity region = new CountryRegionEntity();
		try {
			region = countryRegionRepository.findByIdAndStatusLike(id, 1);
			if (region == null) {
				logger.info("---------------- Region not found.");
				return new ResponseEntity<>("Region not found.", HttpStatus.NOT_FOUND);
			}
			logger.info("Region for deleting identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			countryRegionDao.deleteRegion(loggedUser, region);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<CountryRegionEntity>(region, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(
					new RESTError(2, "Number format exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// pagination:

	/*
	 * //@Secured("ROLE_ADMIN") //@JsonView(Views.Admin.class)
	 * 
	 * @RequestMapping(method = RequestMethod.GET, value = "/getAllPaginated2")
	 * public ResponseEntity<?> getAllPaginated2(
	 * 
	 * @RequestParam(defaultValue="0") int page,
	 * 
	 * @RequestParam(defaultValue="5") int pageSize, //Optional<Integer> pageSize,
	 * 
	 * @RequestParam(defaultValue="Sort.Direction.ASC")Sort.Direction direction, //
	 * Optional<Direction> direction,
	 * 
	 * @RequestParam (defaultValue="countryRegionName")String sortBy,
	 * //Optional<String> sortBy, Principal principal) {
	 * logger.info("################ /jobster/regions/getAllPaginated2 started.");
	 * logger.info("Logged username: " + principal.getName()); try {
	 * Page<CountryRegionEntity> regionsPage= countryRegionDao.findAll(page,
	 * pageSize, direction, sortBy); Iterable<CountryRegionEntity> regions =
	 * regionsPage.getContent(); logger.info("---------------- Finished OK.");
	 * return new ResponseEntity<Iterable<CountryRegionEntity>>(regions,
	 * HttpStatus.OK); } catch(Exception e) {
	 * logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
	 * return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+
	 * e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR); } }
	 * 
	 * //@Secured("ROLE_ADMIN") //@JsonView(Views.Admin.class)
	 * 
	 * @RequestMapping(method = RequestMethod.GET, value = "/getAllPaginated1")
	 * public ResponseEntity<?> getAllPaginated1(
	 * 
	 * @RequestParam(defaultValue="0") int page,
	 * 
	 * @RequestParam(defaultValue="2") int pageSize, //Optional<Integer> pageSize,
	 * // @RequestParam(defaultValue="Sort.Direction.ASC")Direction direction, //
	 * Optional<Direction> direction, // @RequestParam
	 * (defaultValue="countryRegionName")String sortBy, //Optional<String> sortBy,
	 * Principal principal) {
	 * logger.info("################ /jobster/regions/getAllPaginated started.");
	 * logger.info("Logged username: " + principal.getName()); try {
	 * Page<CountryRegionEntity> regionsPage= countryRegionDao.findAll(page,
	 * pageSize);// direction, sortBy); Iterable<CountryRegionEntity> regions =
	 * regionsPage.getContent(); logger.info("---------------- Finished OK.");
	 * return new ResponseEntity<Iterable<CountryRegionEntity>>(regions,
	 * HttpStatus.OK); } catch(Exception e) {
	 * logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
	 * return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+
	 * e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR); } }
	 */
	// @Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/getAllPaginated")
	public ResponseEntity<?> getAllPaginated(@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize, // Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, // Optional<Direction> direction,
			@RequestParam Optional<String> sortBy, // Optional<String> sortBy,
			Principal principal) {
		logger.info("################ /jobster/regions/getAllPaginated started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Page<CountryRegionEntity> regionsPage = countryRegionDao.findAll(page.orElse(0), pageSize.orElse(5),direction.orElse(Sort.Direction.ASC), sortBy.orElse("countryRegionName"));
			Iterable<CountryRegionEntity> regions = regionsPage.getContent();
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<CountryRegionEntity>>(regions, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// @Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/activePaginated")
	public ResponseEntity<?> getAllActivePaginated(
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, 
			@RequestParam Optional<String> sortBy,
			Principal principal) {
		logger.info("################ /jobster/regions/getAllActivePaginated started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("countryRegionName"));
			Page<CountryRegionEntity> countryRegionPage= countryRegionRepository.findCountryRegionByStatusLike(1,pageable);
			Iterable<CountryRegionEntity> regions = countryRegionPage.getContent();
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<CountryRegionEntity>>(regions, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// @Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/inactivePaginated")
	public ResponseEntity<?> getAllInactivePaginated(
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, 
			@RequestParam Optional<String> sortBy,
			Principal principal) {
		logger.info("################ /jobster/regions/getAllInactivePaginated started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("countryRegionName"));
			Page<CountryRegionEntity> countryRegionPage= countryRegionRepository.findCountryRegionByStatusLike(0,pageable);
			Iterable<CountryRegionEntity> regions = countryRegionPage.getContent();
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<CountryRegionEntity>>(regions, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// @Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archivedPaginated")
	public ResponseEntity<?> getAllArchivedPaginated(
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, 
			@RequestParam Optional<String> sortBy,
			Principal principal) {
		logger.info("################ /jobster/regions/getAllArchivedPaginated started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("countryRegionName"));
			Page<CountryRegionEntity> countryRegionPage= countryRegionRepository.findCountryRegionByStatusLike(-1,pageable);
			Iterable<CountryRegionEntity> regions = countryRegionPage.getContent();
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<CountryRegionEntity>>(regions, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// Vraca listu svih regiona sa datim imenom
	// @Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/AllPaginated/{name}")
	public ResponseEntity<?> getByNameAllPaginated(
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, 
			@RequestParam Optional<String> sortBy,
			@PathVariable String name,
			Principal principal) {
		logger.info("################ /jobster/regions/getByNameAllPaginated started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("countryRegionName"));
			Page<CountryRegionEntity> countryRegionPage= countryRegionRepository.getByCountryRegionNameIgnoreCase(name,pageable);
			Iterable<CountryRegionEntity> regions = countryRegionPage.getContent();
			
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<CountryRegionEntity>>(regions, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
