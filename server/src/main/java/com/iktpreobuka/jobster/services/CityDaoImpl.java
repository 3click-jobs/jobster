package com.iktpreobuka.jobster.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iktpreobuka.jobster.entities.CityEntity;
import com.iktpreobuka.jobster.entities.CountryEntity;
import com.iktpreobuka.jobster.entities.CountryRegionEntity;
import com.iktpreobuka.jobster.entities.UserEntity;
import com.iktpreobuka.jobster.repositories.CityRepository;
import com.iktpreobuka.jobster.repositories.CountryRegionRepository;
import com.iktpreobuka.jobster.repositories.CountryRepository;

@Service 
public class CityDaoImpl implements CityDao {

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private CountryRegionRepository countryRegionRepository;

	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private CountryDao countryDao;

	@Autowired
	private CountryRegionDao countryRegionDao;

	@Autowired
	private CityDistanceDao cityDistanceDao;
	
	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
	
	@Override
	public Iterable<CityEntity> findCityByStatusLike(Integer status) throws Exception {
		return cityRepository.findByStatusLike(status);
	}

	

	public CityEntity addNewCity(String cityName, Double longitude, Double latitude, String countryRegionName, String countryName, String iso2Code) throws Exception {
		if ( cityName == null || longitude == null || latitude == null || countryName == null || iso2Code == null ) {
			throw new Exception("Some data is null.");
		}
		logger.info("addNewCity validation Ok.");
		CityEntity city = new CityEntity();
		try {
			CountryEntity country = countryRepository.getByIso2Code(iso2Code);
			if( country == null ) {
				country = countryDao.addNewCountryWithIso2Code(countryName, iso2Code);
			}
			CountryRegionEntity region = countryRegionRepository.getByCountryRegionNameAndCountry(countryRegionName, country);
			if( region == null) {
				if ( countryRegionName == null ) {
					region = countryRegionDao.addNullCountryRegion(country);
				} else {
					region = countryRegionDao.addNewCountryRegion(countryRegionName, country);
				}
			}
			city.setCityName(cityName);
			city.setLongitude(longitude);
			city.setLatitude(latitude);
			city.setRegion(region);
			city.setStatusActive();
			cityRepository.save(city);
			logger.info("City added.");
			cityDistanceDao.addNewDistancesForCity(city);
			logger.info("City distances added.");
		} catch (Exception e) {
			throw new Exception("addNewCity failed on saving.");
		}
		return city;
	}

	public CityEntity addNewCityWithLoggedUser(String cityName, Double longitude, Double latitude, String countryRegionName, String countryName, String iso2Code, UserEntity loggedUser) throws Exception {
		if ( cityName == null || longitude == null || latitude == null || countryName == null || iso2Code == null || loggedUser == null ) {
			throw new Exception("Some data is null.");
		}
		logger.info("addNewCity validation Ok.");
		CityEntity city = new CityEntity();
		try {
			CountryEntity country = countryRepository.getByIso2Code(iso2Code);
			if( country == null ) {
				country = countryDao.addNewCountryWithIso2CodeAndLoggedUser(countryName, iso2Code, loggedUser);
			}
			CountryRegionEntity region = countryRegionRepository.getByCountryRegionNameAndCountry(countryRegionName, country);
			if( region == null) {
				if ( countryRegionName == null ) {
					region = countryRegionDao.addNullCountryRegionWithLoggedUser(country, loggedUser);
				} else {
					region = countryRegionDao.addNewCountryRegionWithLoggedUser(countryRegionName, country, loggedUser);
				}
			}
			city.setCityName(cityName);
			city.setLongitude(longitude);
			city.setLatitude(latitude);
			city.setRegion(region);
			city.setStatusActive();
			city.setCreatedById(loggedUser.getId());
			cityRepository.save(city);
			logger.info("City added.");
			cityDistanceDao.addNewDistancesForCityWithLoggedUser(city, loggedUser);
			logger.info("City distances added.");
		} catch (Exception e) {
			throw new Exception("addNewCity failed on saving.");
		}
		return city;
	}



}