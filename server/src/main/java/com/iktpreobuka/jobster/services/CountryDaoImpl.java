package com.iktpreobuka.jobster.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
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
			country = countryRepository.findByCountryNameIgnoreCase(countryName);
			if (country == null) {
				country = new CountryEntity();
				country.setCountryName(countryName);
				country.setStatusActive();
				country = countryRepository.save(country);
			}
		} catch (Exception e) {
			throw new Exception("addNewCountry( countryName )save failed.");
		}
		return country;
	}
	
	public CountryEntity addNewCountryWithLoggedUser(String countryName, UserEntity loggedUser) throws Exception {
		if (countryName == null || loggedUser == null ) {
			throw new Exception("Country name and/or logged user is null.");
		}
		CountryEntity country = new CountryEntity();
		try {
			country = countryRepository.findByCountryNameIgnoreCase(countryName);
			if (country == null) {
				country = new CountryEntity();
				country.setCountryName(countryName);
				country.setStatusActive();
				country.setCreatedById(loggedUser.getId());
				country = countryRepository.save(country);
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
			country = countryRepository.findByCountryNameIgnoreCase(countryName);
		} catch (Exception e1) {
			throw new Exception("CountryRepository failed.");
		}
		logger.info("CountryRepository Ok.");
		if (country != null && !country.getIso2Code().equalsIgnoreCase(iso2Code)) {
			throw new Exception("Wrong country name or ISO country code.");
		}
		try {
			if (country == null) {
				country = new CountryEntity();
				country.setCountryName(countryName);
				country.setIso2Code(iso2Code);
				country.setStatusActive();
				country = countryRepository.save(country);
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
			country = countryRepository.findByCountryNameIgnoreCase(countryName);
		} catch (Exception e1) {
			throw new Exception("CountryRepository failed.");
		}
		logger.info("CountryRepository Ok.");
		if (country != null && !country.getIso2Code().equalsIgnoreCase(iso2Code)) {
			throw new Exception("Wrong country name or ISO country code.");
		}
		try {
			if (country == null) {
				country = new CountryEntity();
				country.setCountryName(countryName);
				country.setIso2Code(iso2Code);
				country.setStatusActive();
				country.setCreatedById(loggedUser.getId());
				country = countryRepository.save(country);
			}
		} catch (Exception e) {
			throw new Exception("addNewCountry(countryName, iso2Code, loggedUser) save failed.");
		}
		return country;
	}
	
	@Override
	public void archiveCountryWithLoggedUser(UserEntity loggedUser, CountryEntity country) throws Exception {
		try {
			country.setStatusArchived();
			country.setUpdatedById(loggedUser.getId());
			country = countryRepository.save(country);
			logger.info("archiveCountry finished.");
		} catch (Exception e) {
			throw new Exception("ArchiveCountry failed on saving.");
		}				
	}
	
	@Override
	public void archiveCountry(CountryEntity country) throws Exception {
		try {
			country.setStatusArchived();
			countryRepository.save(country);
			logger.info("archiveCountry finished.");
		} catch (Exception e) {
			throw new Exception("ArchiveCountry failed on saving.");
		}				
	}
	
	@Override
	public void unarchiveCountryWithLoggedUser(UserEntity loggedUser, CountryEntity country) throws Exception {
		try {
			country.setStatusActive();
			country.setUpdatedById(loggedUser.getId());

			countryRepository.save(country);
			logger.info("unArchiveCountry finished.");
		} catch (Exception e) {
			throw new Exception("unArchiveCountry failed on saving.");
		}				
	}
	
	@Override
	public void unarchiveCountry(CountryEntity country) throws Exception {
		try {
			country.setStatusActive();
			countryRepository.save(country);
			logger.info("unArchiveCountry finished.");

			country = countryRepository.save(country);
			logger.info("archiveCity finished.");

		} catch (Exception e) {
			throw new Exception("unArchiveCountry failed on saving.");
		}				
	}
	
	@Override
	public void undeleteCountryWithLoggedUser(UserEntity loggedUser, CountryEntity country) throws Exception {
		try {
			country.setStatusActive();
			country.setUpdatedById(loggedUser.getId());

			countryRepository.save(country);
			logger.info("unDeleteCountry finished.");

			country = countryRepository.save(country);
			logger.info("undeleteCountry finished.");

		} catch (Exception e) {
			throw new Exception("unDeleteCountry failed on saving.");
		}		
	}
	
	@Override
	public void undeleteCountry(CountryEntity country) throws Exception {
		try {
			country.setStatusActive();
			countryRepository.save(country);
			logger.info("unDeleteCountry finished.");
		} catch (Exception e) {
			throw new Exception("unDeleteCountry failed on saving.");
		}		
	}
	
	@Override
	public void deleteCountryWIthLoggedUser(UserEntity loggedUser, CountryEntity country) throws Exception {
		try {
			country.setStatusInactive();
			country.setUpdatedById(loggedUser.getId());
			country = countryRepository.save(country);
			logger.info("deleteCountry finished.");
		} catch (Exception e) {
			throw new Exception("DeleteCountry failed on saving.");
		}
	}
	
	@Override
	public void deleteCountry(CountryEntity country) throws Exception {
		try {
			country.setStatusInactive();
			countryRepository.save(country);
			logger.info("deleteCountry finished.");
		} catch (Exception e) {
			throw new Exception("DeleteCountry failed on saving.");
		}
	}


//pagination:
	
/*version1:
	@SuppressWarnings("deprecation")
	@Override
	public Page<CountryEntity> findAll(int pageNum) {
		return countryRepository.findAll(new PageRequest(pageNum, 4));
	}
*/

	@Override
	public Page<CountryEntity> findAll(int page, int pageSize, Direction direction, String sortBy) {
		return countryRepository.findAll(PageRequest.of(page, pageSize, direction, sortBy));
	}


/*
	@Override
	public Page<CountryEntity> findCountryByStatusLike(int i, int page, int pageSize, Direction direction, String sortBy) {
		return countryRepository.findCountryByStatusLike(i, new PageRequest(page, pageSize, direction, sortBy));
	}
	*/
}