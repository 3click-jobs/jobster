package com.iktpreobuka.jobster.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iktpreobuka.jobster.entities.CountryEntity;
import com.iktpreobuka.jobster.entities.CountryRegionEntity;
import com.iktpreobuka.jobster.entities.UserEntity;
import com.iktpreobuka.jobster.repositories.CountryRegionRepository;

@Service 
public class CountryRegionDaoImpl implements CountryRegionDao {


	@Autowired
	private CountryRegionRepository countryRegionRepository;
	
	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
	
	@Override
	public Iterable<CountryRegionEntity> findRegionByStatusLike(Integer status) throws Exception {
		return countryRegionRepository.findByStatusLike(status);
	}


	public CountryRegionEntity addNewCountryRegion(String countryRegionName, CountryEntity country) throws Exception {
		if (country == null ) {
			throw new Exception("Country is null.");
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
				countryRegion = countryRegionRepository.save(countryRegion);
			}
		} catch (Exception e) {
			throw new Exception("addNewCountryRegion(countryRegionName, country, loggedUser) save failed.");
		}
		return countryRegion;
	}
	
	public CountryRegionEntity addNewCountryRegionWithLoggedUser(String countryRegionName, CountryEntity country, UserEntity loggedUser) throws Exception {
		if (country == null || loggedUser == null ) {
			throw new Exception("Country name and/or logged user is null.");
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
				countryRegion = countryRegionRepository.save(countryRegion);
			}
		} catch (Exception e) {
			throw new Exception("addNewCountryRegion(countryRegionName, country, loggedUser) save failed.");
		}
		return countryRegion;
	}
	
	public CountryRegionEntity addNullCountryRegion(CountryEntity country) throws Exception {
		if ( country == null ) {
			throw new Exception("Country is null.");
		}
		CountryRegionEntity countryRegion = new CountryRegionEntity();
		try {
			countryRegion = countryRegionRepository.getByCountryRegionNameAndCountry(null, country);
			if (countryRegion == null) {
				countryRegion = new CountryRegionEntity();
				countryRegion.setCountryRegionName(null);
				countryRegion.setCountry(country);
				countryRegion.setStatusActive();
				countryRegion = countryRegionRepository.save(countryRegion);
			}
		} catch (Exception e) {
			throw new Exception("addNewCountryRegion(country, loggedUser) save failed.");
		}
		return countryRegion;
	}

	public CountryRegionEntity addNullCountryRegionWithLoggedUser(CountryEntity country, UserEntity loggedUser) throws Exception {
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
				countryRegion = countryRegionRepository.save(countryRegion);
			}
		} catch (Exception e) {
			throw new Exception("addNewCountryRegion(country, loggedUser) save failed.");
		}
		return countryRegion;
	}
	
	@Override
	public void archiveRegion(UserEntity loggedUser, CountryRegionEntity region) throws Exception {
		try {
			region.setStatusArchived();
			region.setUpdatedById(loggedUser.getId());
			region = countryRegionRepository.save(region);
			logger.info("archiveRegion finished.");
		} catch (Exception e) {
			throw new Exception("ArchiveRegion failed on saving.");
		}				
	}
	
	@Override
	public void unarchiveRegion(UserEntity loggedUser, CountryRegionEntity region) throws Exception {
		try {
			region.setStatusArchived();
			region.setUpdatedById(loggedUser.getId());
			region = countryRegionRepository.save(region);
			logger.info("archiveCity finished.");
		} catch (Exception e) {
			throw new Exception("ArchiveCity failed on saving.");
		}				
	}

	@Override
	public void undeleteRegion(UserEntity loggedUser, CountryRegionEntity region) throws Exception {
		try {
			region.setStatusActive();
			region.setUpdatedById(loggedUser.getId());
			region = countryRegionRepository.save(region);
			logger.info("undeleteRegion finished.");
		} catch (Exception e) {
			throw new Exception("UndeleteRegion failed on saving.");
		}		
	}
	
	@Override
	public void deleteRegion(UserEntity loggedUser, CountryRegionEntity region) throws Exception {
		try {
			region.setStatusInactive();
			region.setUpdatedById(loggedUser.getId());
			region = countryRegionRepository.save(region);
			logger.info("deleteCountry finished.");
		} catch (Exception e) {
			throw new Exception("DeleteCountry failed on saving.");
		}
	}
}