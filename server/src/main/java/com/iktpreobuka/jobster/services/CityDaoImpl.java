package com.iktpreobuka.jobster.services;

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
	
	
	public CityEntity addNewCity(String cityName, Double longitude, Double latitude, String countryRegionName, String countryName, String iso2Code, UserEntity loggedUser) throws Exception {
		if ( cityName == null || longitude == null || latitude == null || countryName == null || iso2Code == null || loggedUser == null ) {
			throw new Exception("Some data is null.");
		}
		CityEntity city = new CityEntity();
		try {
			CountryEntity country = countryRepository.getByIso2Code(iso2Code);
			if( country == null) {
				country = countryDao.addNewCountryWithIso2Code(countryName, iso2Code, loggedUser);
			}
			CountryRegionEntity region = countryRegionRepository.getByCountryRegionNameAndCountry(countryRegionName, country.getId());
			if( region == null) {
				if ( countryRegionName == null ) {
					region = countryRegionDao.addNewCountryRegion(country, loggedUser);
				} else {
					region = countryRegionDao.addNewCountryRegion(countryRegionName, country, loggedUser);
				}
			}
			city.setCityName(cityName);
			city.setLongitude(longitude);
			city.setLatitude(latitude);
			city.setRegion(region);
			city.setStatusActive();
			city.setCreatedById(loggedUser.getId());
			cityRepository.save(city);
			cityDistanceDao.addNewDistancesForCity(city, loggedUser);
		} catch (Exception e) {
			throw new Exception("addNewCity failed on saving.");
		}
		return city;
	}

}