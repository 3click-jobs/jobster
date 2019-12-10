package com.iktpreobuka.jobster.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iktpreobuka.jobster.entities.CountryEntity;
import com.iktpreobuka.jobster.entities.UserEntity;
import com.iktpreobuka.jobster.repositories.CountryRepository;

@Service 
public class CountryDaoImpl implements CountryDao {


	@Autowired
	private CountryRepository countryRepository;
	
	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
	
	@Override
	public Iterable<CountryEntity> findCountryByStatusLike(Integer status) throws Exception {
		return countryRepository.findByStatusLike(status);
	}



	public CountryEntity addNewCountry(String countryName) throws Exception {
		if (countryName == null ) {
			throw new Exception("Country name is null.");
		}
		CountryEntity country = new CountryEntity();
		try {
			country = countryRepository.getByCountryName(countryName);
			if (country == null) {
				country = new CountryEntity();
				country.setCountryName(countryName);
				country.setStatusActive();
				countryRepository.save(country);
			}
		} catch (Exception e) {
			throw new Exception("addNewCountry(countryName, loggedUser) save failed.");
		}
		return country;
	}
	
	public CountryEntity addNewCountryWithLoggedUser(String countryName, UserEntity loggedUser) throws Exception {
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

	public CountryEntity addNewCountryWithIso2Code(String countryName, String iso2Code) throws Exception {
		if (countryName == null || iso2Code == null ) {
			throw new Exception("Country name and/or country iso2 code is null.");
		}
		CountryEntity country = new CountryEntity();
		try {
			country = countryRepository.getByCountryNameAndIso2Code(countryName, iso2Code);
			if (country == null) {
				country = new CountryEntity();
				country.setCountryName(countryName);
				country.setIso2Code(iso2Code);
				country.setStatusActive();
				countryRepository.save(country);
			}
		} catch (Exception e) {
			throw new Exception("addNewCountry(countryName, iso2Code, loggedUser) save failed.");
		}
		return country;
	}

	public CountryEntity addNewCountryWithIso2CodeAndLoggedUser(String countryName, String iso2Code, UserEntity loggedUser) throws Exception {
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
	
	@Override
	public void archiveCountry(UserEntity loggedUser, CountryEntity country) throws Exception {
		try {
			country.setStatusArchived();
			country.setUpdatedById(loggedUser.getId());
			countryRepository.save(country);
			logger.info("archiveCountry finished.");
		} catch (Exception e) {
			throw new Exception("ArchiveCountry failed on saving.");
		}				
	}
	
	@Override
	public void unarchiveCountry(UserEntity loggedUser, CountryEntity country) throws Exception {
		try {
			country.setStatusArchived();
			country.setUpdatedById(loggedUser.getId());
			countryRepository.save(country);
			logger.info("archiveCity finished.");
		} catch (Exception e) {
			throw new Exception("ArchiveCity failed on saving.");
		}				
	}
	
	@Override
	public void undeleteCountry(UserEntity loggedUser, CountryEntity country) throws Exception {
		try {
			country.setStatusActive();
			country.setUpdatedById(loggedUser.getId());
			countryRepository.save(country);
			logger.info("undeleteCountry finished.");
		} catch (Exception e) {
			throw new Exception("UndeleteCountry failed on saving.");
		}		
	}
	
	@Override
	public void deleteCountry(UserEntity loggedUser, CountryEntity country) throws Exception {
		try {
			country.setStatusInactive();
			country.setUpdatedById(loggedUser.getId());
			countryRepository.save(country);
			logger.info("deleteCountry finished.");
		} catch (Exception e) {
			throw new Exception("DeleteCountry failed on saving.");
		}
	}
}