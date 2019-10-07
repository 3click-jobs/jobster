package com.iktpreobuka.jobster.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iktpreobuka.jobster.entities.CountryEntity;
import com.iktpreobuka.jobster.entities.CountryRegionEntity;
import com.iktpreobuka.jobster.entities.UserEntity;
import com.iktpreobuka.jobster.repositories.CountryRegionRepository;

@Service 
public class CountryRegionDaoImpl implements CountryRegionDao {


	@Autowired
	private CountryRegionRepository countryRegionRepository;


	public CountryRegionEntity addNewCountryRegion(String countryRegionName, CountryEntity country, UserEntity loggedUser) throws Exception {
		if ( countryRegionName == null || country == null || loggedUser == null ) {
			throw new Exception("Country region name and/or country name and/or logged user is null.");
		}
		CountryRegionEntity countryRegion = new CountryRegionEntity();
		try {
			countryRegion = countryRegionRepository.getByCountryRegionNameAndCountry(countryRegionName, country);
			if (countryRegion == null) {
				countryRegion = new CountryRegionEntity();
				if ( countryRegionName != null ) {
					countryRegion.setCountryRegionName(countryRegionName);
				}
				countryRegion.setCountry(country);
				countryRegion.setStatusActive();
				countryRegion.setCreatedById(loggedUser.getId());
				countryRegionRepository.save(countryRegion);
			}
		} catch (Exception e) {
			throw new Exception("addNewCountryRegion(countryRegionName, country, loggedUser) save failed.");
		}
		return countryRegion;
	}
	
	public CountryRegionEntity addNewCountryRegion(CountryEntity country, UserEntity loggedUser) throws Exception {
		if ( country == null || loggedUser == null ) {
			throw new Exception("Country and/or logged user is null.");
		}
		CountryRegionEntity countryRegion = new CountryRegionEntity();
		try {
			countryRegion = countryRegionRepository.getByCountryRegionNameAndCountry(null, country);
			if (countryRegion == null) {
				countryRegion = new CountryRegionEntity();
				countryRegion.setCountryRegionName(null);
				countryRegion.setCountry(country);
				countryRegion.setStatusActive();
				countryRegion.setCreatedById(loggedUser.getId());
				countryRegionRepository.save(countryRegion);
			}
		} catch (Exception e) {
			throw new Exception("addNewCountryRegion(country, loggedUser) save failed.");
		}
		return countryRegion;
	}


}