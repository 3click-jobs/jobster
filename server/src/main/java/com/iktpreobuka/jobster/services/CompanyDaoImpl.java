package com.iktpreobuka.jobster.services;

import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iktpreobuka.jobster.entities.CompanyEntity;
import com.iktpreobuka.jobster.entities.JobOfferEntity;
import com.iktpreobuka.jobster.entities.JobSeekEntity;
import com.iktpreobuka.jobster.entities.UserEntity;
import com.iktpreobuka.jobster.entities.dto.CompanyDTO;
import com.iktpreobuka.jobster.repositories.CityRepository;
import com.iktpreobuka.jobster.repositories.CompanyRepository;
import com.iktpreobuka.jobster.repositories.JobOfferRepository;
import com.iktpreobuka.jobster.repositories.JobSeekRepository;

@Service
public class CompanyDaoImpl implements CompanyDao {

	
	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private CityRepository cityRepository;
	
	@Autowired
	private JobSeekRepository jobSeekRepository;

	@Autowired
	private JobOfferRepository jobOfferRepository;


    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");


	@Override
	public UserEntity addNewCompany(UserEntity loggedUser, CompanyDTO newCompany) throws Exception {
		if (newCompany.getCompanyName() == null || newCompany.getCompanyId() == null || newCompany.getEmail() == null || newCompany.getMobilePhone() == null || newCompany.getCity() == null ) {
			throw new Exception("Some data is null.");
		}
		UserEntity temporaryUser = new CompanyEntity();
		try {
			temporaryUser = companyRepository.getByEmail(newCompany.getEmail());
		} catch (Exception e1) {
			throw new Exception("addNewCompany Exist user check failed.");
		}
		if (temporaryUser != null && (!((CompanyEntity) temporaryUser).getCompanyName().equals(newCompany.getCompanyName()) || !((CompanyEntity) temporaryUser).getCompanyId().equals(newCompany.getCompanyId()) || !temporaryUser.getEmail().equals(newCompany.getEmail()) || !temporaryUser.getMobilePhone().equals(newCompany.getMobilePhone()) || !temporaryUser.getCity().equals(cityRepository.getByCityName(newCompany.getCity())) )) {
			throw new Exception("Company exists, but import data not same as exist user data.");
		}
		UserEntity user = new CompanyEntity();
		try {
			try {
				((CompanyEntity) user).setCompanyName(newCompany.getCompanyName());
				((CompanyEntity) user).setCompanyId(newCompany.getCompanyId());
			    user.setCity(cityRepository.getByCityName(newCompany.getCity()));
			    user.setEmail(newCompany.getEmail());
			    user.setMobilePhone(newCompany.getMobilePhone());
			    user.setDetailsLink(newCompany.getDetailsLink());
			    user.setNumberOfRatings(0);
			    user.setRating(0.0);
				user.setStatusActive();
				user.setCreatedById(loggedUser.getId());
				companyRepository.save(user);
				temporaryUser = user;
			} catch (Exception e) {
				throw new Exception("addNewCompany save failed.");
			}
			return temporaryUser;
		} catch (Exception e) {
			throw new Exception("addNewCompany save failed.");
		}
	}
	
	public void modifyCompany(UserEntity loggedUser, CompanyEntity company, CompanyDTO updateCompany) throws Exception {
		if (updateCompany.getCompanyName() == null && updateCompany.getCompanyId() == null && updateCompany.getEmail() == null && updateCompany.getMobilePhone() == null && updateCompany.getCity() == null ) {
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
			if (updateCompany.getCity() != null && !cityRepository.getByCityName(updateCompany.getCity()).equals(company.getCity()) && !updateCompany.getCity().equals(" ") && !updateCompany.getMobilePhone().equals("")) {
				company.setCity(cityRepository.getByCityName(updateCompany.getCity()));
				i++;
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
