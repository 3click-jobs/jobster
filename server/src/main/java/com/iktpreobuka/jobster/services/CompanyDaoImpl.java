package com.iktpreobuka.jobster.services;

import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iktpreobuka.jobster.entities.CityEntity;
import com.iktpreobuka.jobster.entities.CompanyEntity;
import com.iktpreobuka.jobster.entities.CountryEntity;
import com.iktpreobuka.jobster.entities.CountryRegionEntity;
import com.iktpreobuka.jobster.entities.JobOfferEntity;
import com.iktpreobuka.jobster.entities.JobSeekEntity;
import com.iktpreobuka.jobster.entities.UserEntity;
import com.iktpreobuka.jobster.entities.dto.CompanyDTO;
import com.iktpreobuka.jobster.repositories.CityRepository;
import com.iktpreobuka.jobster.repositories.CompanyRepository;
import com.iktpreobuka.jobster.repositories.CountryRegionRepository;
import com.iktpreobuka.jobster.repositories.CountryRepository;
import com.iktpreobuka.jobster.repositories.JobOfferRepository;
import com.iktpreobuka.jobster.repositories.JobSeekRepository;

@Service
public class CompanyDaoImpl implements CompanyDao {

	
	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private CityRepository cityRepository;
	
	@Autowired
	private CountryRepository countryRepository;
	
	@Autowired
	private CountryRegionRepository countryRegionRepository;
	
	@Autowired
	private CityDao cityDao;
	
	@Autowired
	private JobSeekRepository jobSeekRepository;

	@Autowired
	private JobOfferRepository jobOfferRepository;


    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	
	@Override
	public Iterable<CompanyEntity> findCompanyByStatusLike(Integer status) throws Exception {
		return companyRepository.findByStatusLike(status);
	}

	@Override
	public UserEntity addNewCompany(CompanyDTO newCompany) throws Exception {
		if ( newCompany.getCompanyName() == null || newCompany.getCompanyRegistrationNumber() == null || newCompany.getAccessRole() == null || newCompany.getEmail() == null || newCompany.getMobilePhone() == null || (newCompany.getCity() == null && newCompany.getCountry() == null && newCompany.getIso2Code() == null && newCompany.getLatitude() == null && newCompany.getLongitude() == null) || newCompany.getUsername() == null || newCompany.getPassword() == null || newCompany.getConfirmedPassword() == null ) {
			throw new Exception("Some data is null.");
		}
		logger.info("addNewCompany validation Ok.");
		UserEntity user = new CompanyEntity();
		CityEntity city = new CityEntity();
		CountryEntity country = new CountryEntity();
		CountryRegionEntity countryRegion = new CountryRegionEntity();
		try {
			country = countryRepository.findByCountryNameIgnoreCase(newCompany.getCountry());
		} catch (Exception e1) {
			throw new Exception("CountryRepository failed.");
		}
		logger.info("CountryRepository Ok.");
		if (country != null && !country.getIso2Code().equalsIgnoreCase(newCompany.getIso2Code())) {
			throw new Exception("Wrong country name or ISO country code.");
		}
		Boolean newCountryRegion = false;
		Boolean newCountry = false;
		Boolean newCity = false;
		try {
			if(country != null) {
				logger.info("Country founded.");
				countryRegion = countryRegionRepository.findByCountryRegionNameAndCountry(newCompany.getCountryRegion(), country);
				if (countryRegion != null) {
					logger.info("CountryRegion founded.");
					city = cityRepository.findByCityNameAndRegion(newCompany.getCity(), countryRegion);
					logger.info("City founded.");
				} else {
					city = null;
					newCountryRegion = true;
				}
			} else {
				city = null;
				newCountry = true;
				newCountryRegion = true;
			}
			if( city == null) {
				city = cityDao.addNewCity(newCompany.getCity(), newCompany.getLongitude(), newCompany.getLatitude(), newCompany.getCountryRegion(), newCompany.getCountry(), newCompany.getIso2Code());
				newCity = true;
				logger.info("City created.");
			}
		} catch (Exception e2) {
			throw new Exception(e2.getLocalizedMessage());
		}
		try {
			((CompanyEntity) user).setCompanyName(newCompany.getCompanyName());
			((CompanyEntity) user).setCompanyRegistrationNumber(newCompany.getCompanyRegistrationNumber());
		    user.setCity(city);
		    user.setEmail(newCompany.getEmail());
		    user.setMobilePhone(newCompany.getMobilePhone());
		    user.setAbout(newCompany.getAbout());
		    user.setNumberOfRatings(0);
		    user.setRating(0.0);
			user.setStatusActive();
			companyRepository.save(user);
			logger.info("User created.");
			//user = companyRepository.findByEmailAndStatusLike(newCompany.getEmail(), 1);
			user.setCreatedById(user.getId());
			companyRepository.save(user);
			logger.info("User CreatedById added.");
			if(newCity == true) {
				city.setCreatedById(user.getId());
				cityRepository.save(city);
				logger.info("City CreatedById added.");
				if(newCountryRegion == true) {
					if(newCountry == true) {
						country = countryRepository.findByCountryNameAndIso2Code(newCompany.getCountry(), newCompany.getIso2Code());
						country.setCreatedById(user.getId());
						countryRepository.save(country);
						logger.info("Country CreatedById added.");
					}
					countryRegion = countryRegionRepository.findByCountryRegionNameAndCountry(newCompany.getCountryRegion(), country);
					countryRegion.setCreatedById(user.getId());
					countryRegionRepository.save(countryRegion);
					logger.info("CountryRegion CreatedById added.");
				}
			}
			logger.info("addNewCompany finished.");
		} catch (Exception e) {
			throw new Exception("addNewCompany save failed." + e.getLocalizedMessage());
		}
		return user;
	}
	
	public void modifyCompany(UserEntity loggedUser, CompanyEntity company, CompanyDTO updateCompany) throws Exception {
		if (updateCompany.getCompanyName() == null && updateCompany.getCompanyRegistrationNumber() == null && updateCompany.getEmail() == null && updateCompany.getMobilePhone() == null && (updateCompany.getCity() == null || updateCompany.getCountry() == null || updateCompany.getIso2Code() == null || updateCompany.getLatitude() == null || updateCompany.getLongitude() == null) && updateCompany.getAbout() == null ) {
			throw new Exception("All data is null.");
		}
		try {
			Integer i = 0;
			if (updateCompany.getCompanyName() != null && !updateCompany.getCompanyName().equals(" ") && !updateCompany.getCompanyName().equals("") && !updateCompany.getCompanyName().equals(company.getCompanyName())) {
				company.setCompanyName(updateCompany.getCompanyName());
				i++;
			}
			if (updateCompany.getCompanyRegistrationNumber() != null && !updateCompany.getCompanyRegistrationNumber().equals(company.getCompanyRegistrationNumber()) && !updateCompany.getCompanyRegistrationNumber().equals(" ") && !updateCompany.getCompanyRegistrationNumber().equals("")) {
				company.setCompanyRegistrationNumber(updateCompany.getCompanyRegistrationNumber());
				i++;
			}
			if (updateCompany.getEmail() != null && !updateCompany.getEmail().equals(company.getEmail()) && !updateCompany.getEmail().equals(" ") && !updateCompany.getEmail().equals("")) {
				company.setEmail(updateCompany.getEmail());
				i++;
			}
			if (updateCompany.getMobilePhone() != null && !updateCompany.getMobilePhone().equals(company.getMobilePhone()) && !updateCompany.getMobilePhone().equals(" ") && !updateCompany.getMobilePhone().equals("")) {
				company.setMobilePhone(updateCompany.getMobilePhone());
				i++;
			}
			if (updateCompany.getAbout() != null && !updateCompany.getAbout().equals(company.getAbout()) && !updateCompany.getAbout().equals(" ") && !updateCompany.getAbout().equals("")) {
				company.setAbout(updateCompany.getAbout());
				i++;
			}
			if (updateCompany.getCity() != null && !updateCompany.getCity().equals(" ") && !updateCompany.getCity().equals("") && updateCompany.getCountry() != null && !updateCompany.getCountry().equals(" ") && !updateCompany.getCountry().equals("") && updateCompany.getIso2Code() != null && !updateCompany.getIso2Code().equals(" ") && !updateCompany.getIso2Code().equals("") ) {
				CityEntity city = new CityEntity();
				CountryRegionEntity countryRegion = new CountryRegionEntity();
				CountryEntity country = countryRepository.findByCountryNameAndIso2Code(updateCompany.getCountry(), updateCompany.getIso2Code());
				if(country != null) {
					countryRegion = countryRegionRepository.findByCountryRegionNameAndCountry(updateCompany.getCountryRegion(), country);
					if (countryRegion != null) {
						city = cityRepository.findByCityNameAndRegion(updateCompany.getCity(), countryRegion);
					} else {
						city = null;
					}
				} else {
					city = null;
				}
				if( city == null) {
					city = cityDao.addNewCityWithLoggedUser(updateCompany.getCity(), updateCompany.getLongitude(), updateCompany.getLatitude(), updateCompany.getCountryRegion(), updateCompany.getCountry(), updateCompany.getIso2Code(), loggedUser);
				}
				if(!city.equals(company.getCity())) {
					company.setCity(city);
					i++;
				}
			}
			if (i>0) {
				company.setUpdatedById(loggedUser.getId());
				companyRepository.save(company);
			}
		} catch (Exception e) {
			throw new Exception("modifyCompany faild on saving." + e.getLocalizedMessage());
		}
	}

	public void deleteCompany(UserEntity loggedUser, CompanyEntity company) throws Exception {
		try {
			for (JobOfferEntity jo : company.getJobOffers()) {
				if (jo.getStatus() == 1) {
					jo.setStatusInactive();
					jo.setUpdatedById(loggedUser.getId());
					jobOfferRepository.save(jo);
				}
			}
			for (JobSeekEntity js : company.getJobSeeks()) {
				if (js.getStatus() == 1) {
					js.setStatusInactive();
					js.setUpdatedById(loggedUser.getId());
					jobSeekRepository.save(js);
				}
			}
			company.setStatusInactive();
			company.setUpdatedById(loggedUser.getId());
			companyRepository.save(company);
		} catch (Exception e) {
			throw new Exception("deleteCompany failed on saving." + e.getLocalizedMessage());
		}
	}

	public void undeleteCompany(UserEntity loggedUser, CompanyEntity company) throws Exception {
		try {
			company.setStatusActive();
			company.setUpdatedById(loggedUser.getId());
			companyRepository.save(company);
		} catch (Exception e) {
			throw new Exception("undeleteCompany failed on saving." + e.getLocalizedMessage());
		}		
	}

	public void archiveCompany(UserEntity loggedUser, CompanyEntity company) throws Exception {
		try {
			for (JobOfferEntity jo : company.getJobOffers()) {
					jo.setStatusArchived();
					jo.setUpdatedById(loggedUser.getId());
					jobOfferRepository.save(jo);
			}
			for (JobSeekEntity js : company.getJobSeeks()) {
					js.setStatusArchived();
					js.setUpdatedById(loggedUser.getId());
					jobSeekRepository.save(js);
			}
			company.setStatusArchived();
			company.setUpdatedById(loggedUser.getId());
			companyRepository.save(company);
		} catch (Exception e) {
			throw new Exception("ArchiveCompany failed on saving." + e.getLocalizedMessage());
		}		
	}

}
