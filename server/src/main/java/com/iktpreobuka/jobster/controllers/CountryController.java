package com.iktpreobuka.jobster.controllers;

import java.security.Principal;
import java.util.List;
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
import org.springframework.data.domain.Sort.Direction;
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
import com.iktpreobuka.jobster.entities.UserEntity;
import com.iktpreobuka.jobster.entities.dto.POSTCountryDTO;
import com.iktpreobuka.jobster.repositories.CountryRepository;
import com.iktpreobuka.jobster.repositories.UserAccountRepository;
import com.iktpreobuka.jobster.services.CountryDao;

@Controller
@RestController
@RequestMapping(value= "/jobster/countries")

public class CountryController {
	
	@Autowired 	
	private CountryRepository countryRepository;
	
	@Autowired 	
	private UserAccountRepository userAccountRepository;
		
	@Autowired 
	private CountryDao countryDao;
		
	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
		
	private String createErrorMessage(BindingResult result) { 
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
		}
	
	//@Secured("ROLE_ADMIN")
	//@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/getById/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/countries/getById started.");
		logger.info("Logged username: " + principal.getName());
		try {
			CountryEntity country= countryRepository.getById(id);
			if (country==null) {
				return new ResponseEntity<>("Country doesn't exists.", HttpStatus.BAD_REQUEST);
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<CountryEntity>(country, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	//@Secured("ROLE_ADMIN")
	//@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/getAll")
	public ResponseEntity<?> getAll(Principal principal) {
			logger.info("################ /jobster/countries/getAll started.");
			logger.info("Logged username: " + principal.getName());
			try {
				Iterable<CountryEntity> countries= countryRepository.findAllByOrderByCountryName();
				logger.info("---------------- Finished OK.");
				return new ResponseEntity<Iterable<CountryEntity>>(countries, HttpStatus.OK);
				} catch(Exception e) {
					logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
					return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
				}
	}
	
	//@Secured("ROLE_ADMIN")
	//@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/active")
	public ResponseEntity<?> getAllActive(Principal principal) {
			logger.info("################ /jobster/countries/getAllActive started.");
			logger.info("Logged username: " + principal.getName());
			try {
				Iterable<CountryEntity> countries= countryDao.findCountryByStatusLike(1);
				logger.info("---------------- Finished OK.");
				return new ResponseEntity<Iterable<CountryEntity>>(countries, HttpStatus.OK);
			} catch(Exception e) {
				logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
				return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
			}
	}
			
	//@Secured("ROLE_ADMIN")
	//@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/inactive")
	public ResponseEntity<?> getAllInactive(Principal principal) {
		logger.info("################ /jobster/countries/getAllInactive started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<CountryEntity> countries= countryDao.findCountryByStatusLike(0);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<CountryEntity>>(countries, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
					
	//@Secured("ROLE_ADMIN")
	//@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived")
	public ResponseEntity<?> getAllArchived(Principal principal) {
		logger.info("################ /jobster/countries/getAllArchived started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<CountryEntity> countries= countryDao.findCountryByStatusLike(-1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<CountryEntity>>(countries, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
			
	//@Secured("ROLE_ADMIN")
	//@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/getByName/{name}")
	public ResponseEntity<?> getByName(@PathVariable String name, Principal principal) {
		logger.info("################ /jobster/countries/getByName started.");
		logger.info("Logged username: " + principal.getName());
		try {
			CountryEntity country= countryRepository.getByCountryNameIgnoreCase(name);
			if (country==null) {
				return new ResponseEntity<>("Country doesn't exists.", HttpStatus.BAD_REQUEST);
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<CountryEntity>(country, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//@Secured("ROLE_ADMIN")
	//@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.POST, value = "/addNewCountry")
	public ResponseEntity<?> addNewCountry(@Valid @RequestBody POSTCountryDTO newCountry, Principal principal, BindingResult result) {
		logger.info("################ /jobster/countries/addNewCountry started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
		}
		if (newCountry == null) {
			logger.info("---------------- New country is null.");
			return new ResponseEntity<>("New country is null.", HttpStatus.BAD_REQUEST);
		}
		if (newCountry.getCountryName() == null || newCountry.getIso2Code() == null ) {
			logger.info("---------------- Some atributes are null.");
			return new ResponseEntity<>("Some atributes are null", HttpStatus.BAD_REQUEST);
		}
		try {
			if (countryRepository.existsByCountryNameIgnoreCase(newCountry.getCountryName()) || countryRepository.existsByIso2CodeIgnoreCase(newCountry.getIso2Code())) {
				logger.info("---------------- Country already exists.");
		        return new ResponseEntity<>("Country already exists.", HttpStatus.NOT_ACCEPTABLE);
			}
			
			String countryName=newCountry.getCountryName();
			String iso2Code=newCountry.getIso2Code();
			CountryEntity country=countryDao.addNewCountryWithIso2Code(countryName, iso2Code);
			logger.info("New country created.");
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(country, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ This is an exception message: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//@Secured("ROLE_ADMIN")
	//@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/modify/{id}")
	public ResponseEntity<?> modifyCountry(@PathVariable Integer id, @Valid @RequestBody POSTCountryDTO updateCountry, Principal principal, BindingResult result) {
		logger.info("################ /jobster/countries/modify/{id} started.");
		try {
			CountryEntity country=countryRepository.getById(id);
		logger.info("Logged user: " + principal.getName());
		if (country == null) {
			logger.info("---------------- Country doesn't exists.");
	        return new ResponseEntity<>("Country doesn't exists.", HttpStatus.BAD_REQUEST);
	      }
		
		country.setCountryName(updateCountry.getCountryName());
		country.setIso2Code(updateCountry.getIso2Code());

		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		
		if (updateCountry.getCountryName() == null || updateCountry.getIso2Code() == null ) {
			logger.info("---------------- Some or all atributes are null.");
			return new ResponseEntity<>("Some or all atributes are null", HttpStatus.BAD_REQUEST);
		}
		countryRepository.save(country);
		logger.info("Country updated.");
		logger.info("---------------- Finished OK.");
		return new ResponseEntity<>(country, HttpStatus.OK);
		}
		catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	
		}
	}
	
	//@Secured("ROLE_ADMIN")
	//@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/archive/{id}")
	public ResponseEntity<?> archive(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/countries/archive/{id}/archive started.");
		logger.info("Logged user: " + principal.getName());
		CountryEntity country = new CountryEntity();
		try {
			country = countryRepository.getById(id);
			if (country == null || country.getStatus() == -1) {
				logger.info("---------------- Country not found.");
		        return new ResponseEntity<>("Country not found.", HttpStatus.NOT_FOUND);
		      }
			logger.info("Country for archiving identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user: " + loggedUser);
			logger.info("Logged user identified.");
			countryDao.archiveCountry(country);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<CountryEntity>(country, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//@Secured("ROLE_ADMIN")
	//@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/unarchive/{id}")
	public ResponseEntity<?> unArchive(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/countries/unarchive/{id} Unarchive started.");
		logger.info("Logged user: " + principal.getName());
		CountryEntity country = new CountryEntity();
		try {
			country = countryRepository.findByIdAndStatusLike(id, -1);
			if (country == null) {
				logger.info("---------------- Country not found.");
		        return new ResponseEntity<>("Country not found.", HttpStatus.NOT_FOUND);
		      }
			logger.info("Country for unarchiving identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user: " + loggedUser);
			logger.info("Logged user identified.");
			
			countryDao.unarchiveCountry(country);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<CountryEntity>(country, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	//@Secured("ROLE_ADMIN")
	//@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/undelete/{id}")
	public ResponseEntity<?> unDelete(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/countries/undelete/{id} unDelete started.");
		logger.info("Logged user: " + principal.getName());
		CountryEntity country = new CountryEntity();
		try {
			country = countryRepository.findByIdAndStatusLike(id, 0);
			if (country == null) {
				logger.info("---------------- Country not found.");
		        return new ResponseEntity<>("Country not found.", HttpStatus.NOT_FOUND);
		      }
			logger.info("Country for undeleting identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			logger.info("Logged user: " + loggedUser);
			countryDao.undeleteCountry(country);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<CountryEntity>(country, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//@Secured("ROLE_ADMIN")
	//@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.DELETE, value = "/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/countries/{id} Delete started.");
		//logger.info("Logged user: " + principal.getName());
		CountryEntity country = new CountryEntity();
		try {
			country = countryRepository.findByIdAndStatusLike(id, 1);
			if (country == null) {
				logger.info("---------------- Country not found.");
		        return new ResponseEntity<>("Country not found.", HttpStatus.NOT_FOUND);
		      }
			logger.info("Country for deleting identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user: " + loggedUser);
			logger.info("Logged user identified.");
			countryDao.deleteCountry(country);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<CountryEntity>(country, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	
	
//************************************ pagination:	
/*version1:
	//@Secured("ROLE_ADMIN")
	//@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/getAllPaginated")
	public ResponseEntity<?> getAllPaginated(@RequestParam(defaultValue="0") int page, Principal principal) {
			logger.info("################ /jobster/countries/getAllPaginated started.");
			//logger.info("Logged username: " + principal.getName());
			try {
				Page<CountryEntity> countriesPage= countryDao.findAll(page);
				Iterable<CountryEntity> countries = countriesPage.getContent();
				logger.info("---------------- Finished OK.");
				return new ResponseEntity<Iterable<CountryEntity>>(countries, HttpStatus.OK);
				} catch(Exception e) {
					logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
					return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
				}
	}
	
*/
	
	
//version2:
	//@Secured("ROLE_ADMIN")
	//@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/getAllPaginated")
	public ResponseEntity<?> getAllPaginated(
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, 
			@RequestParam Optional<String> sortBy,
			Principal principal) {
			logger.info("################ /jobster/countries/getAllPaginated started.");
			//logger.info("Logged username: " + principal.getName());
			try {
				Page<CountryEntity> countriesPage= countryDao.findAll(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("countryName"));
				Iterable<CountryEntity> countries = countriesPage.getContent();
				logger.info("---------------- Finished OK.");
				return new ResponseEntity<Iterable<CountryEntity>>(countries, HttpStatus.OK);
				} catch(Exception e) {
					logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
					return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
				}
	}
	
	
	//@Secured("ROLE_ADMIN")
	//@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/activePaginated")
	public ResponseEntity<?> getAllActivePaginated(
					@RequestParam Optional<Integer> page,
					@RequestParam Optional<Integer> pageSize,
					@RequestParam Optional<Sort.Direction> direction, 
					@RequestParam Optional<String> sortBy,
					Principal principal) {
			logger.info("################ /jobster/countries/getAllActivePaginated started.");
			//logger.info("Logged username: " + principal.getName());
			try {
				Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("countryName"));
			//	Page<CountryEntity> countriesPage= countryDao.findCountryByStatusLike(1,page.orElse(0), pageSize.orElse(4), direction.orElse(Sort.Direction.ASC), sortBy.orElse("countryName"));
				Page<CountryEntity> countriesPage= countryRepository.findCountryByStatusLike(1,pageable);

				Iterable<CountryEntity> countries = countriesPage.getContent();
				logger.info("---------------- Finished OK.");
				return new ResponseEntity<Iterable<CountryEntity>>(countries, HttpStatus.OK);
			} catch(Exception e) {
				logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
				return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
			}
	}
	
	//@Secured("ROLE_ADMIN")
	//@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/inactivePaginated")
	public ResponseEntity<?> getAllInactivePaginated(
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, 
			@RequestParam Optional<String> sortBy,
			Principal principal) {
		logger.info("################ /jobster/countries/getAllInactivePaginated started.");
		//logger.info("Logged username: " + principal.getName());
		try {
			Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("countryName"));
			Page<CountryEntity> countriesPage= countryRepository.findCountryByStatusLike(0,pageable);
			Iterable<CountryEntity> countries = countriesPage.getContent();

			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<CountryEntity>>(countries, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	//@Secured("ROLE_ADMIN")
	//@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archivedPaginated")
	public ResponseEntity<?> getAllArchivedPaginated(
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, 
			@RequestParam Optional<String> sortBy,
			Principal principal) {
		logger.info("################ /jobster/countries/getAllArchivedPaginated started.");
		//logger.info("Logged username: " + principal.getName());
		try {
			Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("countryName"));
			Page<CountryEntity> countriesPage= countryRepository.findCountryByStatusLike(-1,pageable);
			Iterable<CountryEntity> countries = countriesPage.getContent();
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<CountryEntity>>(countries, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
			
	
//**********************************************************
}
