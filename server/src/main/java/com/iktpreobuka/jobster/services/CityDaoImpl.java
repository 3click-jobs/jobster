package com.iktpreobuka.jobster.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.iktpreobuka.jobster.entities.CityEntity;
import com.iktpreobuka.jobster.entities.CountryEntity;
import com.iktpreobuka.jobster.entities.CountryRegionEntity;
import com.iktpreobuka.jobster.entities.UserEntity;
import com.iktpreobuka.jobster.entities.dto.POSTCityDTO;
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
		CountryEntity country = new CountryEntity();
		try {
			country = countryRepository.getByIso2Code(iso2Code);
		} catch (Exception e1) {
			throw new Exception("CountryRepository failed.");
		}
		logger.info("CountryRepository Ok.");
		if (country != null && !country.getCountryName().equalsIgnoreCase(countryName)) {
			throw new Exception("Wrong country name or ISO country code.");
		}
		try {
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
			city = cityRepository.save(city);
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
			CountryEntity country = countryRepository.getByCountryNameIgnoreCase(countryName);
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
			city = cityRepository.save(city);
			logger.info("City added.");
			cityDistanceDao.addNewDistancesForCityWithLoggedUser(city, loggedUser);
			logger.info("City distances added.");
		} catch (Exception e) {
			throw new Exception("addNewCity failed on saving.");
		}
		return city;
	}
	
	@Override
	public  CityEntity modifyCityWithLoggedUser(CityEntity city, POSTCityDTO updateCity, UserEntity loggedUser) throws Exception {
		try {
			
		
		if(!(countryRepository.existsByCountryNameIgnoreCase(updateCity.getCountry()))) {
			String countryName=updateCity.getCountry();
			String iso2Code=updateCity.getIso2Code();
			countryDao.addNewCountryWithIso2CodeAndLoggedUser(countryName, iso2Code, loggedUser);
		}
		if(!(countryRegionRepository.existsByCountryRegionNameIgnoreCase(updateCity.getCountry()))) {
			CountryEntity country=countryRepository.getByCountryNameIgnoreCase(updateCity.getCountry());
			String countryRegionName=updateCity.getRegion();
			countryRegionDao.addNewCountryRegionWithLoggedUser(countryRegionName, country, loggedUser);
			}
		
		city.setCityName(updateCity.getCityName());
		city.setLongitude(updateCity.getLongitude());
		city.setLatitude(updateCity.getLatitude());
		CountryEntity country = countryRepository.getByCountryName(updateCity.getCountry());
		CountryRegionEntity region=countryRegionRepository.getByCountryRegionNameAndCountry(updateCity.getRegion(), country);
		city.setRegion(region);
		city.setUpdatedById(loggedUser.getId());
		} catch (Exception e) {
			throw new Exception("Modify City failed");
		}
		return city;
	}
	

	@Override
	public void deleteCity(UserEntity loggedUser, CityEntity city) throws Exception {
		try {
			city.setStatusInactive();
			city.setUpdatedById(loggedUser.getId());
			city = cityRepository.save(city);
			logger.info("deleteCity finished.");
		} catch (Exception e) {
			throw new Exception("DeleteCity failed on saving.");
		}
	}

	@Override
	public void undeleteCity(UserEntity loggedUser, CityEntity city) throws Exception {
		try {
			city.setStatusActive();
			city.setUpdatedById(loggedUser.getId());
			city = cityRepository.save(city);
			logger.info("undeleteCity finished.");
		} catch (Exception e) {
			throw new Exception("UndeleteCity failed on saving.");
		}		
	}
	
	@Override
	public void archiveCity(UserEntity loggedUser, CityEntity city) throws Exception {
		try {
			city.setStatusArchived();
			city.setUpdatedById(loggedUser.getId());
			city = cityRepository.save(city);
			logger.info("archiveCity finished.");
		} catch (Exception e) {
			throw new Exception("ArchiveCity failed on saving.");
		}				
	}
	
	@Override
	public void unarchiveCity(UserEntity loggedUser, CityEntity city) throws Exception {
		try {
			city.setStatusActive();
			city.setUpdatedById(loggedUser.getId());
			city = cityRepository.save(city);
			logger.info("unarchiveCity finished.");
		} catch (Exception e) {
			throw new Exception("UnarchiveCity failed on saving.");
		}		
	}


}