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
import com.iktpreobuka.jobster.entities.CityEntity;
import com.iktpreobuka.jobster.entities.CountryEntity;
import com.iktpreobuka.jobster.entities.CountryRegionEntity;
import com.iktpreobuka.jobster.entities.UserEntity;
import com.iktpreobuka.jobster.entities.dto.POSTCityDTO;
import com.iktpreobuka.jobster.repositories.CityRepository;
import com.iktpreobuka.jobster.repositories.CountryRegionRepository;
import com.iktpreobuka.jobster.repositories.CountryRepository;
import com.iktpreobuka.jobster.repositories.UserAccountRepository;
import com.iktpreobuka.jobster.services.CityDao;
import com.iktpreobuka.jobster.services.CityDistanceDao;
import com.iktpreobuka.jobster.services.CountryDao;
import com.iktpreobuka.jobster.services.CountryRegionDao;

@Controller

@RestController

@RequestMapping(value = "/jobster/cities")

public class CityController {

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private CityDao cityDao;

	// @Autowired
	// private UserCustomValidator userValidator;

	@Autowired
	private UserAccountRepository userAccountRepository;

	@Autowired
	private CountryRegionRepository countryRegionRepository;

	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private CityDistanceDao cityDistanceDao;

	@Autowired
	private CountryDao countryDao;

	@Autowired
	private CountryRegionDao countryRegionDao;

	/*
	 * @InitBinder
	 * 
	 * protected void initBinder(final WebDataBinder binder) {
	 * 
	 * binder.addValidators(userValidator);
	 * 
	 * }
	 */

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	private String createErrorMessage(BindingResult result) {

		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));

	}

	// @Secured("ROLE_ADMIN")

	// @JsonView(Views.Admin.class)

	@RequestMapping(method = RequestMethod.GET, value = "/getById/{id}")

	public ResponseEntity<?> getById(@PathVariable Integer id, Principal principal) {

		logger.info("################ /jobster/cities/getById started.");

		logger.info("Logged username: " + principal.getName());

		try {
			if (!(cityRepository.existsById(id))) {
				return new ResponseEntity<>("City doesn`t exists.", HttpStatus.BAD_REQUEST);
			}

			CityEntity city = cityRepository.getById(id);

			logger.info("---------------- Finished OK.");

			return new ResponseEntity<CityEntity>(city, HttpStatus.OK);

		} catch (Exception e) {

			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());

			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	// @Secured("ROLE_ADMIN")

	// @JsonView(Views.Admin.class)

	@RequestMapping(method = RequestMethod.GET, value="/getAll")

	public ResponseEntity<?> getAll(Principal principal) {

		logger.info("################ /jobster/cities/getAll started.");

		logger.info("Logged username: " + principal.getName());

		try {

			Iterable<CityEntity> cities = cityRepository.findAll();

			logger.info("---------------- Finished OK.");

			return new ResponseEntity<Iterable<CityEntity>>(cities, HttpStatus.OK);

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

		logger.info("################ /jobster/cities/getAllActive started.");

		logger.info("Logged username: " + principal.getName());

		try {

			Iterable<CityEntity> cities = cityDao.findCityByStatusLike(1);

			logger.info("---------------- Finished OK.");

			return new ResponseEntity<Iterable<CityEntity>>(cities, HttpStatus.OK);

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

		logger.info("################ /jobster/cities/getAllInactive started.");

		logger.info("Logged username: " + principal.getName());

		try {

			Iterable<CityEntity> cities = cityDao.findCityByStatusLike(0);

			logger.info("---------------- Finished OK.");

			return new ResponseEntity<Iterable<CityEntity>>(cities, HttpStatus.OK);

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

		logger.info("################ /jobster/cities/getAllArchived started.");

		logger.info("Logged username: " + principal.getName());

		try {

			Iterable<CityEntity> cities = cityDao.findCityByStatusLike(-1);

			logger.info("---------------- Finished OK.");

			return new ResponseEntity<Iterable<CityEntity>>(cities, HttpStatus.OK);

		} catch (Exception e) {

			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());

			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	// @Secured("ROLE_ADMIN")

	// @JsonView(Views.Admin.class)

	@RequestMapping(method = RequestMethod.GET, value = "/getByName/{name}")

	public ResponseEntity<?> getByName(@PathVariable String name, Principal principal) {

		logger.info("################ /jobster/cities/getByName started.");

		logger.info("Logged username: " + principal.getName());

		try {

			List<CityEntity> city = cityRepository.getByCityNameIgnoreCase(name);
			if (city.isEmpty()) {
				return new ResponseEntity<>("No city with given name.", HttpStatus.BAD_REQUEST);
			}

			logger.info("---------------- Finished OK.");

			return new ResponseEntity<List<CityEntity>>(city, HttpStatus.OK);

		} catch (Exception e) {

			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());

			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	// @Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.POST, value = "/addNewCity")
	public ResponseEntity<?> addNewCity(@Valid @RequestBody POSTCityDTO newCity, Principal principal,
			BindingResult result) {
		logger.info("################ /jobster/cities/addNewCity started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) {
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		if (newCity == null) {
			logger.info("---------------- New city is null.");
			return new ResponseEntity<>("New city is null.", HttpStatus.BAD_REQUEST);
		}
		if (newCity.getCityName() == null || newCity.getLongitude() == null || newCity.getLatitude() == null
				|| newCity.getCountry() == null) {
			logger.info("---------------- Some atributes are null.");
			return new ResponseEntity<>("Some atributes are null", HttpStatus.BAD_REQUEST);
		}

		try {
			if (cityRepository.existsByCityNameIgnoreCase(newCity.getCityName())
					&& (cityRepository.existsByLongitude(newCity.getLongitude())
							&& cityRepository.existsByLatitude(newCity.getLatitude()))) {
				logger.info("---------------- City already exists.");
				return new ResponseEntity<>("City already exists.", HttpStatus.NOT_ACCEPTABLE);
			}
			if (!(countryRepository.existsByCountryNameIgnoreCase(newCity.getCountry()))) {
				String countryName = newCity.getCountry();
				String iso2Code = newCity.getIso2Code();
				CountryEntity country = countryDao.addNewCountryWithIso2Code(countryName, iso2Code);
				if (!(countryRegionRepository.existsByCountryRegionNameIgnoreCase(newCity.getCountry()))) {
					String countryRegionName = newCity.getRegion();
					countryRegionDao.addNewCountryRegion(countryRegionName, country);
				}
			}

			CityEntity city = new CityEntity();
			city.setCityName(newCity.getCityName());
			city.setLatitude(newCity.getLatitude());
			city.setLongitude(newCity.getLongitude());
			city.setStatusActive();
			CountryEntity country = countryRepository.getByCountryName(newCity.getCountry());
			CountryRegionEntity region = countryRegionRepository.getByCountryRegionNameAndCountry(newCity.getRegion(),
					country);
			if (region == null) {
				city.setRegion(null);
			}
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
	public ResponseEntity<?> modifyCity(@PathVariable Integer id, @Valid @RequestBody POSTCityDTO updateCity,
			Principal principal, BindingResult result) {
		logger.info("################ /jobster/cities/modify/{id} started.");
		try {
			CityEntity city = cityRepository.getById(id);
			
			if (city == null) {
				logger.info("---------------- City doesn't exists.");
				return new ResponseEntity<>("City doesn't exists.", HttpStatus.BAD_REQUEST);
			}
			
			logger.info("Logged user: " + principal.getName());
			
			
			if (updateCity.getCityName() == null || updateCity.getLongitude() == null
					|| updateCity.getLatitude() == null) {
				logger.info("---------------- Some or all atributes are null.");
				return new ResponseEntity<>("Some or all atributes are null.", HttpStatus.BAD_REQUEST);
			}
			if(updateCity.getRegion()==null) {
				return new ResponseEntity<>("Country Region needs update.", HttpStatus.BAD_REQUEST);
			}
			
			CountryRegionEntity regionN=countryRegionRepository.getByCountryRegionName(city.getRegion().getCountryRegionName());
			if(!(regionN.getCountryRegionName().equalsIgnoreCase(updateCity.getRegion()))) {
				return new ResponseEntity<>("Country Region needs update.", HttpStatus.BAD_REQUEST);
			}
			
			CountryEntity countryN= countryRepository.findByCountryNameIgnoreCase(city.getRegion().getCountry().getCountryName());
			if(!(countryN.getIso2Code().equalsIgnoreCase(updateCity.getIso2Code()))) {
				return new ResponseEntity<>("Country needs update.", HttpStatus.BAD_REQUEST);
			}
			
			CountryEntity countryI= countryRepository.getByIso2Code(city.getRegion().getCountry().getIso2Code());
			if(!(countryI.getCountryName().equalsIgnoreCase(updateCity.getCountry()))) {
				return new ResponseEntity<>("Country needs update.", HttpStatus.BAD_REQUEST);
			}
			
			

			city.setCityName(updateCity.getCityName());
			city.setLongitude(updateCity.getLongitude());
			city.setLatitude(updateCity.getLatitude());
			CountryEntity country = countryRepository.getByCountryName(updateCity.getCountry());
			CountryRegionEntity region = countryRegionRepository
					.getByCountryRegionNameAndCountry(updateCity.getRegion(), country);
			city.setRegion(region);

			if (result.hasErrors()) {
				logger.info("---------------- Validation has errors - " + createErrorMessage(result));
				return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
			}

			
			cityRepository.save(city);
			logger.info("City updated.");
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(city, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// @Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/archive/{id}")
	public ResponseEntity<?> archive(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/cities/archive/{id}/archive started.");
		logger.info("Logged user: " + principal.getName());
		CityEntity city = new CityEntity();
		try {
			city = cityRepository.getById(id);
			if (city == null || city.getStatus() == -1) {
				logger.info("---------------- City not found.");
				return new ResponseEntity<>("City not found.", HttpStatus.NOT_FOUND);
			}
			logger.info("City for archiving identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(),1);
			logger.info("Logged user identified.");
			cityDao.archiveCity(loggedUser, city);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<CityEntity>(city, HttpStatus.OK);
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
	@RequestMapping(method = RequestMethod.DELETE, value = "/delete/{id}")
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

	// @Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/allPaginated")
	public ResponseEntity<?> getAllPaginated(@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize, @RequestParam Optional<Sort.Direction> direction,
			@RequestParam Optional<String> sortBy, Principal principal) {
		logger.info("################ /jobster/cities/getAllPaginated started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Page<CityEntity> citiesPage = cityDao.findAll(page.orElse(0), pageSize.orElse(5),
					direction.orElse(Sort.Direction.ASC), sortBy.orElse("cityName"));
			Iterable<CityEntity> cities = citiesPage.getContent();
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<CityEntity>>(cities, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	// @Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/activePaginated")
	public ResponseEntity<?> getAllActivePaginated(@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize, @RequestParam Optional<Sort.Direction> direction,
			@RequestParam Optional<String> sortBy, Principal principal) {
		logger.info("################ /jobster/cities/getAllActivePaginated started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC),
					sortBy.orElse("cityName"));
			Page<CityEntity> citiesPage = cityRepository.findCityByStatusLike(1, pageable);
			Iterable<CityEntity> cities = citiesPage.getContent();
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<CityEntity>>(cities, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	// @Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/inactivePaginated")
	public ResponseEntity<?> getAllInactivePaginated(@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize, @RequestParam Optional<Sort.Direction> direction,
			@RequestParam Optional<String> sortBy, Principal principal) {
		logger.info("################ /jobster/cities/getAllinactivePaginated started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC),
					sortBy.orElse("cityName"));
			Page<CityEntity> citiesPage = cityRepository.findCityByStatusLike(0, pageable);
			Iterable<CityEntity> cities = citiesPage.getContent();
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<CityEntity>>(cities, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	// @Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archivedPaginated")
	public ResponseEntity<?> getAllArchivedPaginated(@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize, @RequestParam Optional<Sort.Direction> direction,
			@RequestParam Optional<String> sortBy, Principal principal) {
		logger.info("################ /jobster/cities/getAllArchivedPaginated started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC),
					sortBy.orElse("cityName"));
			Page<CityEntity> citiesPage = cityRepository.findCityByStatusLike(-1, pageable);
			Iterable<CityEntity> cities = citiesPage.getContent();
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<CityEntity>>(cities, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	// @Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/byNamePaginated/{name}")
	public ResponseEntity<?> getByNamePaginated(@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize, @RequestParam Optional<Sort.Direction> direction,
			@RequestParam Optional<String> sortBy, @PathVariable String name, Principal principal) {
		logger.info("################ /jobster/cities/getByName started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC),
					sortBy.orElse("cityName"));
			Page<CityEntity> cityPage = cityRepository.getByCityNameIgnoreCase(name,pageable);
			Iterable<CityEntity> city = cityPage.getContent();
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<CityEntity>>(city, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

}