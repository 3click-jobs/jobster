package com.iktpreobuka.jobster.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iktpreobuka.jobster.entities.CountryEntity;
import com.iktpreobuka.jobster.entities.UserEntity;
import com.iktpreobuka.jobster.repositories.CountryRepository;

@Service 
public class CountryDaoImpl implements CountryDao {


	@Autowired
	private CountryRepository countryRepository;


	public CountryEntity addNewCountry(String countryName, UserEntity loggedUser) throws Exception {
		if (countryName == null || loggedUser == null ) {
			throw new Exception("Country name and/or logged user is null.");
		}
		CountryEntity country = new CountryEntity();
		try {
			country = countryRepository.getByCountryName(countryName);
			if (country == null) {
				country = new CountryEntity();
				country.setCountryName(countryName);
				country.setStatusActive();
				country.setCreatedById(loggedUser.getId());
				countryRepository.save(country);
			}
		} catch (Exception e) {
			throw new Exception("addNewCountry(countryName, loggedUser) save failed.");
		}
		return country;
	}

	public CountryEntity addNewCountryWithIso2Code(String countryName, String iso2Code, UserEntity loggedUser) throws Exception {
		if (countryName == null || iso2Code == null || loggedUser == null ) {
			throw new Exception("Country name and/or country iso2 code and/or logged user is null.");
		}
		CountryEntity country = new CountryEntity();
		try {
			country = countryRepository.getByCountryNameAndIso2Code(countryName, iso2Code);
			if (country == null) {
				country = new CountryEntity();
				country.setCountryName(countryName);
				country.setIso2Code(iso2Code);
				country.setStatusActive();
				country.setCreatedById(loggedUser.getId());
				countryRepository.save(country);
			}
		} catch (Exception e) {
			throw new Exception("addNewCountry(countryName, iso2Code, loggedUser) save failed.");
		}
		return country;
	}


}