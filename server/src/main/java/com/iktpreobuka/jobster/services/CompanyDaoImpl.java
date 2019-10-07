package com.iktpreobuka.jobster.services;

import java.text.SimpleDateFormat;

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


	@Override
	public UserEntity addNewCompany(UserEntity loggedUser, CompanyDTO newCompany) throws Exception {
		if (newCompany.getCompanyName() == null || newCompany.getCompanyId() == null || newCompany.getEmail() == null || newCompany.getMobilePhone() == null || newCompany.getCity() == null || newCompany.getCountry() == null || newCompany.getIso2Code() == null || newCompany.getLatitude() == null || newCompany.getLongitude() == null ) {
			throw new Exception("Some data is null.");
		}
		UserEntity user = new CompanyEntity();
		CityEntity city = new CityEntity();
		try {
			CountryEntity country = countryRepository.getByCountryNameAndIso2Code(newCompany.getCountry(), newCompany.getIso2Code());
			if(country != null) {
				CountryRegionEntity countryRegion = countryRegionRepository.getByCountryRegionNameAndCountry(newCompany.getCountryRegion(), country);
				if (countryRegion != null) {
					city = cityRepository.getByCityNameAndCountryRegion(newCompany.getCity(), countryRegion);
				}
			}
			if( city == null) {
				city = cityDao.addNewCity(newCompany.getCity(), newCompany.getLongitude(), newCompany.getLatitude(), newCompany.getCountryRegion(), newCompany.getCountry(), newCompany.getIso2Code(), loggedUser);
			}
			((CompanyEntity) user).setCompanyName(newCompany.getCompanyName());
			((CompanyEntity) user).setCompanyId(newCompany.getCompanyId());
		    user.setCity(city);
		    user.setEmail(newCompany.getEmail());
		    user.setMobilePhone(newCompany.getMobilePhone());
		    user.setDetailsLink(newCompany.getDetailsLink());
		    user.setNumberOfRatings(0);
		    user.setRating(0.0);
			user.setStatusActive();
			user.setCreatedById(loggedUser.getId());
			companyRepository.save(user);
		} catch (Exception e) {
			throw new Exception("addNewCompany save failed.");
		}
		return user;
	}
	
	public void modifyCompany(UserEntity loggedUser, CompanyEntity company, CompanyDTO updateCompany) throws Exception {
		if (updateCompany.getCompanyName() == null && updateCompany.getCompanyId() == null && updateCompany.getEmail() == null && updateCompany.getMobilePhone() == null && (updateCompany.getCity() == null || updateCompany.getCountry() == null || updateCompany.getIso2Code() == null || updateCompany.getLatitude() == null || updateCompany.getLongitude() == null) ) {
			throw new Exception("All data is null.");
		}
		try {
			Integer i = 0;
			if (updateCompany.getCompanyName() != null && !updateCompany.getCompanyName().equals(" ") && !updateCompany.getCompanyName().equals("") && !updateCompany.getCompanyName().equals(company.getCompanyName())) {
				company.setCompanyName(updateCompany.getCompanyName());
				i++;
			}
			if (updateCompany.getCompanyId() != null && !updateCompany.getCompanyId().equals(company.getCompanyId()) && !updateCompany.getCompanyId().equals(" ") && !updateCompany.getCompanyId().equals("")) {
				company.setCompanyId(updateCompany.getCompanyId());
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
			if (updateCompany.getCity() != null && !updateCompany.getCity().equals(" ") && !updateCompany.getCity().equals("") && updateCompany.getCountry() != null && !updateCompany.getCountry().equals(" ") && !updateCompany.getCountry().equals("") && updateCompany.getIso2Code() != null && !updateCompany.getIso2Code().equals(" ") && !updateCompany.getIso2Code().equals("") ) {
				CityEntity city = new CityEntity();
				CountryRegionEntity countryRegion = new CountryRegionEntity();
				CountryEntity country = countryRepository.getByCountryNameAndIso2Code(updateCompany.getCountry(), updateCompany.getIso2Code());
				if(country != null) {
					countryRegion = countryRegionRepository.getByCountryRegionNameAndCountry(updateCompany.getCountryRegion(), country);
					if (countryRegion != null) {
						city = cityRepository.getByCityNameAndCountryRegion(updateCompany.getCity(), countryRegion);
					}
				}
				if( city == null) {
					city = cityDao.addNewCity(updateCompany.getCity(), updateCompany.getLongitude(), updateCompany.getLatitude(), updateCompany.getCountryRegion(), updateCompany.getCountry(), updateCompany.getIso2Code(), loggedUser);
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
			throw new Exception("modifyCompany faild on saving.");
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
			throw new Exception("deleteCompany failed on saving.");
		}
	}

	public void undeleteCompany(UserEntity loggedUser, CompanyEntity company) throws Exception {
		try {
			company.setStatusActive();
			company.setUpdatedById(loggedUser.getId());
			companyRepository.save(company);
		} catch (Exception e) {
			throw new Exception("undeleteCompany failed on saving.");
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
			throw new Exception("ArchiveCompany failed on saving.");
		}		
	}

}
