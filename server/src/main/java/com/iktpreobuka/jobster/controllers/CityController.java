package com.iktpreobuka.jobster.controllers;
		

		
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iktpreobuka.jobster.controllers.util.RESTError;
import com.iktpreobuka.jobster.entities.CityEntity;
import com.iktpreobuka.jobster.entities.CountryRegionEntity;
import com.iktpreobuka.jobster.entities.UserEntity;
import com.iktpreobuka.jobster.entities.dto.POSTCityDTO;
import com.iktpreobuka.jobster.repositories.CityRepository;
import com.iktpreobuka.jobster.repositories.CountryRegionRepository;
import com.iktpreobuka.jobster.repositories.UserAccountRepository;
import com.iktpreobuka.jobster.services.CityDao;
import com.iktpreobuka.jobster.services.CityDistanceDao;
		

		
@Controller
		
@RestController
		
@RequestMapping(value= "/jobster/cities")
		

		
public class CityController {
		

		
	@Autowired 	
	private CityRepository cityRepository;
		
	@Autowired 
	private CityDao cityDao;
		
	//@Autowired 
	//private UserCustomValidator userValidator;
	
	@Autowired 
	private UserAccountRepository userAccountRepository;
	
	@Autowired 
	private CountryRegionRepository countryRegionRepository;
	
	@Autowired 
	private CityDistanceDao cityDistanceDao;
		

		
	/*@InitBinder
		
	protected void initBinder(final WebDataBinder binder) { 
		
		binder.addValidators(userValidator); 
		
		}*/
		

		

		
	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
		

		
	private String createErrorMessage(BindingResult result) { 
		
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
		
		}
	
	//@Secured("ROLE_ADMIN")
	
			//@JsonView(Views.Admin.class)
			
			@RequestMapping(method = RequestMethod.GET, value = "/byId/{id}")
			
			public ResponseEntity<?> getById(@PathVariable Integer id, Principal principal) {
			
				logger.info("################ /jobster/cities/getById started.");
			
				//logger.info("Logged username: " + principal.getName());
			
				try {
			
					CityEntity city= cityRepository.getById(id);
			
					logger.info("---------------- Finished OK.");
			
					return new ResponseEntity<CityEntity>(city, HttpStatus.OK);
			
				} catch(Exception e) {
			
					logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			
					return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
			
				}
			
			}
		

		
	//@Secured("ROLE_ADMIN")
		
		//@JsonView(Views.Admin.class)
		
		@RequestMapping(method = RequestMethod.GET)
		
		public ResponseEntity<?> getAll(Principal principal) {
		
			logger.info("################ /jobster/cities/getAll started.");
		
			//logger.info("Logged username: " + principal.getName());
		
			try {

				Iterable<CityEntity> cities= cityRepository.findAll();
		
				logger.info("---------------- Finished OK.");
		
				return new ResponseEntity<Iterable<CityEntity>>(cities, HttpStatus.OK);
		
			} catch(Exception e) {
		
				logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
		
				return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		
			}
		
		}
		
		//@Secured("ROLE_ADMIN")
		
		//@JsonView(Views.Admin.class)
		
		@RequestMapping(method = RequestMethod.GET, value = "/active")
		
		public ResponseEntity<?> getAllActive(Principal principal) {
		
			logger.info("################ /jobster/cities/getAllActive started.");
		
			//logger.info("Logged username: " + principal.getName());
		
			try {
		
				Iterable<CityEntity> cities= cityDao.findCityByStatusLike(1);
		
				logger.info("---------------- Finished OK.");
		
				return new ResponseEntity<Iterable<CityEntity>>(cities, HttpStatus.OK);
		
			} catch(Exception e) {
		
				logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
		
				return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		
			}
		
		}
		
		//@Secured("ROLE_ADMIN")
		
				//@JsonView(Views.Admin.class)
				
				@RequestMapping(method = RequestMethod.GET, value = "/inactive")
				
				public ResponseEntity<?> getAllInactive(Principal principal) {
				
					logger.info("################ /jobster/cities/getAllInactive started.");
				
					//logger.info("Logged username: " + principal.getName());
				
					try {
				
						Iterable<CityEntity> cities= cityDao.findCityByStatusLike(0);
				
						logger.info("---------------- Finished OK.");
				
						return new ResponseEntity<Iterable<CityEntity>>(cities, HttpStatus.OK);
				
					} catch(Exception e) {
				
						logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
				
						return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
				
					}
				
				}
				
				//@Secured("ROLE_ADMIN")
				
				//@JsonView(Views.Admin.class)
				
				@RequestMapping(method = RequestMethod.GET, value = "/archived")
				
				public ResponseEntity<?> getAllArchived(Principal principal) {
				
					logger.info("################ /jobster/cities/getAllArchived started.");
				
					//logger.info("Logged username: " + principal.getName());
				
					try {
				
						Iterable<CityEntity> cities= cityDao.findCityByStatusLike(-1);
				
						logger.info("---------------- Finished OK.");
				
						return new ResponseEntity<Iterable<CityEntity>>(cities, HttpStatus.OK);
				
					} catch(Exception e) {
				
						logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
				
						return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
				
					}
				
				}
		

		
		//@Secured("ROLE_ADMIN")
		
		//@JsonView(Views.Admin.class)
		
		@RequestMapping(method = RequestMethod.GET, value = "/byName/{name}")
		
		public ResponseEntity<?> getByName(@PathVariable String name, Principal principal) {
		
			logger.info("################ /jobster/cities/getByName started.");
		
			//logger.info("Logged username: " + principal.getName());
		
			try {
		
				List<CityEntity> city= cityRepository.getByCityNameIgnoreCase(name);
		
				logger.info("---------------- Finished OK.");
		
				return new ResponseEntity<List<CityEntity>>(city, HttpStatus.OK);
		
			} catch(Exception e) {
		
				logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
		
				return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		
			}
		
		}
		
		//@Secured("ROLE_ADMIN")
		//@JsonView(Views.Admin.class)
		@RequestMapping(method = RequestMethod.POST, value = "/addNewCity")
			public ResponseEntity<?> addNewCity(@Valid @RequestBody POSTCityDTO newCity, Principal principal, BindingResult result) {
				logger.info("################ /jobster/cities/addNewCity started.");
				//logger.info("Logged user: " + principal.getName());
				if (result.hasErrors()) { 
					logger.info("---------------- Validation has errors - " + createErrorMessage(result));
					return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
				}
				if (newCity == null) {
					logger.info("---------------- New city is null.");
					return new ResponseEntity<>("New city is null.", HttpStatus.BAD_REQUEST);
				}
				if (newCity.getCityName() == null || newCity.getLongitude() == null || newCity.getLatitude() == null || newCity.getRegion() == null) {
					logger.info("---------------- Some atributes are null.");
					return new ResponseEntity<>("Some atributes are null", HttpStatus.BAD_REQUEST);
				}
				
				try {
					if (cityRepository.existsByCityNameIgnoreCase(newCity.getCityName()) &&
					   (cityRepository.existsByLongitude(newCity.getLongitude())&& cityRepository.existsByLatitude(newCity.getLatitude()))) {
					   logger.info("---------------- City already exists.");
					   return new ResponseEntity<>("City already exists.", HttpStatus.NOT_ACCEPTABLE);
				}
				
						CityEntity city=new CityEntity();
						city.setCityName(newCity.getCityName());
						city.setLatitude(newCity.getLatitude());
						city.setLongitude(newCity.getLongitude());
						city.setStatusActive();
						CountryRegionEntity region=countryRegionRepository.getById(newCity.getRegion());
						city.setRegion(region);

						cityRepository.save(city);
						logger.info("New city created.");
						logger.info("---------------- Calculate and add distance started.");
						cityDistanceDao.addNewDistancesForCity(city);
						logger.info("---------------- Calculate and add distance finished.");
						logger.info("---------------- Finished OK.");
						return new ResponseEntity<>(city, HttpStatus.OK);
				
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
				public ResponseEntity<?> modifyCity(@PathVariable Integer id, @Valid @RequestBody POSTCityDTO updateCity, Principal principal, BindingResult result) {
					logger.info("################ /jobster/cities/modify/{id} started.");
					try {
					CityEntity city=cityRepository.getById(id);
					//logger.info("Logged user: " + principal.getName());
					if (city == null) {
						logger.info("---------------- City doesn't exists.");
				        return new ResponseEntity<>("City doesn't exists.", HttpStatus.BAD_REQUEST);
				      }
					
					city.setCityName(updateCity.getCityName());
					city.setLongitude(updateCity.getLongitude());
					city.setLatitude(updateCity.getLatitude());
					CountryRegionEntity region=countryRegionRepository.getById(updateCity.getRegion());
					city.setRegion(region);
	
					if (result.hasErrors()) { 
						logger.info("---------------- Validation has errors - " + createErrorMessage(result));
						return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
						}
					
					if (updateCity.getCityName() == null || updateCity.getLongitude() == null ||  updateCity.getLatitude() == null || updateCity.getRegion() == null) {
						logger.info("---------------- Some or all atributes are null.");
						return new ResponseEntity<>("Some or all atributes are null", HttpStatus.BAD_REQUEST);
					}
					cityRepository.save(city);
					logger.info("City updated.");
					logger.info("---------------- Finished OK.");
					return new ResponseEntity<>(updateCity, HttpStatus.OK);
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
					logger.info("################ /jobster/cities/archive/{id}/archive started.");
					//logger.info("Logged user: " + principal.getName());
					CityEntity city = new CityEntity();
					try {
						city = cityRepository.getById(id);
						if (city == null || city.getStatus() == -1) {
							logger.info("---------------- City not found.");
					        return new ResponseEntity<>("City not found.", HttpStatus.NOT_FOUND);
					      }
						logger.info("City for archiving identified.");
						//UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
						//logger.info("Logged user identified.");
						//cityDao.archiveCity(loggedUser, city);
						logger.info("---------------- Finished OK.");
						return new ResponseEntity<CityEntity>(city, HttpStatus.OK);
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
					logger.info("################ /jobster/cities/unarchive/{id}/ Unarchive started.");
					logger.info("Logged user: " + principal.getName());
					CityEntity city = new CityEntity();
					try {
						city = cityRepository.findByIdAndStatusLike(id, -1);
						if (city == null) {
							logger.info("---------------- City not found.");
					        return new ResponseEntity<>("City not found.", HttpStatus.NOT_FOUND);
					      }
						logger.info("City for unarchiving identified.");
						UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
						logger.info("Logged user identified.");
						cityDao.unarchiveCity(loggedUser, city);
						logger.info("---------------- Finished OK.");
						return new ResponseEntity<CityEntity>(city, HttpStatus.OK);
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
					logger.info("################ /jobster/cities/undelete/{id}/unDelete started.");
					logger.info("Logged user: " + principal.getName());
					CityEntity city = new CityEntity();
					try {
						city = cityRepository.findByIdAndStatusLike(id, 0);
						if (city == null) {
							logger.info("---------------- City not found.");
					        return new ResponseEntity<>("City not found.", HttpStatus.NOT_FOUND);
					      }
						logger.info("City for undeleting identified.");
						UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
						logger.info("Logged user identified.");
						cityDao.undeleteCity(loggedUser, city);
						logger.info("---------------- Finished OK.");
						return new ResponseEntity<CityEntity>(city, HttpStatus.OK);
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
				@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
				public ResponseEntity<?> delete(@PathVariable Integer id, Principal principal) {
					logger.info("################ /jobster/cities/{id}/delete started.");
					logger.info("Logged user: " + principal.getName());
					CityEntity city = new CityEntity();
					try {
						city = cityRepository.findByIdAndStatusLike(id, 1);
						if (city == null) {
							logger.info("---------------- City not found.");
					        return new ResponseEntity<>("City not found.", HttpStatus.NOT_FOUND);
					      }
						logger.info("City for deleting identified.");
						UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
						logger.info("Logged user identified.");
						cityDao.deleteCity(loggedUser, city);
						logger.info("---------------- Finished OK.");
						return new ResponseEntity<CityEntity>(city, HttpStatus.OK);
					} catch (NumberFormatException e) {
						logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
						return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
					} catch (Exception e) {
						logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
						return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
					}
				}		
}