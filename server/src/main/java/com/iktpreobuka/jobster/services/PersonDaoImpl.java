package com.iktpreobuka.jobster.services;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iktpreobuka.jobster.entities.CityEntity;
import com.iktpreobuka.jobster.entities.CompanyEntity;
import com.iktpreobuka.jobster.entities.CountryEntity;
import com.iktpreobuka.jobster.entities.CountryRegionEntity;
import com.iktpreobuka.jobster.entities.JobOfferEntity;
import com.iktpreobuka.jobster.entities.JobSeekEntity;
import com.iktpreobuka.jobster.entities.PersonEntity;
import com.iktpreobuka.jobster.entities.UserEntity;
import com.iktpreobuka.jobster.entities.dto.PersonDTO;
import com.iktpreobuka.jobster.enumerations.EGender;
import com.iktpreobuka.jobster.repositories.CityRepository;
import com.iktpreobuka.jobster.repositories.CountryRegionRepository;
import com.iktpreobuka.jobster.repositories.CountryRepository;
import com.iktpreobuka.jobster.repositories.JobOfferRepository;
import com.iktpreobuka.jobster.repositories.JobSeekRepository;
import com.iktpreobuka.jobster.repositories.PersonRepository;


@Service
public class PersonDaoImpl implements PersonDao {

	@Autowired
	private PersonRepository personRepository;

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
	public UserEntity addNewPerson(UserEntity loggedUser, PersonDTO newPerson) throws Exception {
			if (newPerson.getFirstName() == null || newPerson.getLastName() == null || newPerson.getGender() == null || newPerson.getBirthDate() == null || newPerson.getEmail() == null || newPerson.getMobilePhone() == null || newPerson.getCity() == null || newPerson.getCountry() == null || newPerson.getIso2Code() == null || newPerson.getLatitude() == null || newPerson.getLongitude() == null ) {
				throw new Exception("Some data is null.");
			}
			UserEntity user = new CompanyEntity();
			CityEntity city = new CityEntity();
			try {
				CountryEntity country = countryRepository.getByCountryNameAndIso2Code(newPerson.getCountry(), newPerson.getIso2Code());
				if(country != null) {
					CountryRegionEntity countryRegion = countryRegionRepository.getByCountryRegionNameAndCountry(newPerson.getCountryRegion(), country);
					if (countryRegion != null) {
						city = cityRepository.getByCityNameAndRegion(newPerson.getCity(), countryRegion);
					} else {
						city = null;
					}
				} else {
					city = null;
				}
				if( city == null) {
					city = cityDao.addNewCity(newPerson.getCity(), newPerson.getLongitude(), newPerson.getLatitude(), newPerson.getCountryRegion(), newPerson.getCountry(), newPerson.getIso2Code(), loggedUser);
				}
				((PersonEntity) user).setFirstName(newPerson.getFirstName());
				((PersonEntity) user).setLastName(newPerson.getLastName());
				((PersonEntity) user).setGender(EGender.valueOf(newPerson.getGender()));
			    Date bithDate = formatter.parse(newPerson.getBirthDate());
			    ((PersonEntity) user).setBirthDate(bithDate);
			    user.setCity(city);
			    user.setEmail(newPerson.getEmail());
			    user.setMobilePhone(newPerson.getMobilePhone());
			    user.setDetailsLink(newPerson.getDetailsLink());
			    user.setNumberOfRatings(0);
			    user.setRating(0.0);
				user.setStatusActive();
				user.setCreatedById(loggedUser.getId());
				personRepository.save(user);
			} catch (Exception e) {
				throw new Exception("addNewPerson save failed.");
			}
			return user;
	}

	@Override
	public void modifyPerson(UserEntity loggedUser, PersonEntity person, PersonDTO updatePerson) throws Exception {
		if (updatePerson.getFirstName() == null && updatePerson.getLastName() == null && updatePerson.getGender() == null && updatePerson.getBirthDate() == null && updatePerson.getEmail() == null && updatePerson.getMobilePhone() == null && ( updatePerson.getCity() == null || updatePerson.getCountry() == null || updatePerson.getIso2Code() == null || updatePerson.getLatitude() == null || updatePerson.getLongitude() == null) ) {
			throw new Exception("All data is null.");
		}
		try {
			Integer i = 0;
			if (updatePerson.getFirstName() != null && !updatePerson.getFirstName().equals(" ") && !updatePerson.getFirstName().equals("") && !updatePerson.getFirstName().equals(person.getFirstName())) {
				person.setFirstName(updatePerson.getFirstName());
				i++;
			}
			if (updatePerson.getLastName() != null && !updatePerson.getLastName().equals(person.getLastName()) && !updatePerson.getLastName().equals(" ") && !updatePerson.getLastName().equals("")) {
				person.setLastName(updatePerson.getLastName());
				i++;
			}
			if (updatePerson.getGender() != null && EGender.valueOf(updatePerson.getGender()) != person.getGender() && (EGender.valueOf(updatePerson.getGender()) == EGender.GENDER_FEMALE || EGender.valueOf(updatePerson.getGender()) == EGender.GENDER_MALE)) {
				person.setGender(EGender.valueOf(updatePerson.getGender()));
				i++;
			}
			if (updatePerson.getBirthDate() != null && !formatter.parse(updatePerson.getBirthDate()).equals(person.getBirthDate()) && !updatePerson.getBirthDate().equals(" ") && !updatePerson.getBirthDate().equals("")) {
			    Date employmentDate = formatter.parse(updatePerson.getBirthDate());
			    person.setBirthDate(employmentDate);
				i++;
			}
			if (updatePerson.getEmail() != null && !updatePerson.getEmail().equals(person.getEmail()) && !updatePerson.getEmail().equals(" ") && !updatePerson.getEmail().equals("")) {
				person.setEmail(updatePerson.getEmail());
				i++;
			}
			if (updatePerson.getMobilePhone() != null && !updatePerson.getMobilePhone().equals(person.getMobilePhone()) && !updatePerson.getMobilePhone().equals(" ") && !updatePerson.getMobilePhone().equals("")) {
				person.setMobilePhone(updatePerson.getMobilePhone());
				i++;
			}
			if (updatePerson.getCity() != null && !updatePerson.getCity().equals(" ") && !updatePerson.getCity().equals("") && updatePerson.getCountry() != null && !updatePerson.getCountry().equals(" ") && !updatePerson.getCountry().equals("") && updatePerson.getIso2Code() != null && !updatePerson.getIso2Code().equals(" ") && !updatePerson.getIso2Code().equals("") ) {
				CityEntity city = new CityEntity();
				CountryRegionEntity countryRegion = new CountryRegionEntity();
				CountryEntity country = countryRepository.getByCountryNameAndIso2Code(updatePerson.getCountry(), updatePerson.getIso2Code());
				if(country != null) {
					countryRegion = countryRegionRepository.getByCountryRegionNameAndCountry(updatePerson.getCountryRegion(), country);
					if (countryRegion != null) {
						city = cityRepository.getByCityNameAndRegion(updatePerson.getCity(), countryRegion);
					} else {
						city = null;
					}
				} else {
					city = null;
				}
				if( city == null) {
					city = cityDao.addNewCity(updatePerson.getCity(), updatePerson.getLongitude(), updatePerson.getLatitude(), updatePerson.getCountryRegion(), updatePerson.getCountry(), updatePerson.getIso2Code(), loggedUser);
				}
				if(!city.equals(person.getCity())) {
					person.setCity(city);
					i++;
				}
			}
			if (i>0) {
				person.setUpdatedById(loggedUser.getId());
				personRepository.save(person);
			}
		} catch (Exception e) {
			throw new Exception("modifyPerson faild on saving.");
		}
	}

	@Override
	public void deletePerson(UserEntity loggedUser, PersonEntity person) throws Exception {
		try {
			for (JobOfferEntity jo : person.getJobOffers()) {
				if (jo.getStatus() == 1) {
					jo.setStatusInactive();
					jo.setUpdatedById(loggedUser.getId());
					jobOfferRepository.save(jo);
				}
			}
			for (JobSeekEntity js : person.getJobSeeks()) {
				if (js.getStatus() == 1) {
					js.setStatusInactive();
					js.setUpdatedById(loggedUser.getId());
					jobSeekRepository.save(js);
				}
			}
			person.setStatusInactive();
			person.setUpdatedById(loggedUser.getId());
			personRepository.save(person);
		} catch (Exception e) {
			throw new Exception("deletePerson failed on saving.");
		}
	}
	
	@Override
	public void undeletePerson(UserEntity loggedUser, PersonEntity person) throws Exception {
		try {
			person.setStatusActive();
			person.setUpdatedById(loggedUser.getId());
			personRepository.save(person);
		} catch (Exception e) {
			throw new Exception("undeletePerson failed on saving.");
		}		
	}
	
	@Override
	public void archivePerson(UserEntity loggedUser, PersonEntity person) throws Exception {
		try {
			for (JobOfferEntity jo : person.getJobOffers()) {
					jo.setStatusArchived();
					jo.setUpdatedById(loggedUser.getId());
					jobOfferRepository.save(jo);
			}
			for (JobSeekEntity js : person.getJobSeeks()) {
					js.setStatusArchived();
					js.setUpdatedById(loggedUser.getId());
					jobSeekRepository.save(js);
			}
			person.setStatusArchived();
			person.setUpdatedById(loggedUser.getId());
			personRepository.save(person);
		} catch (Exception e) {
			throw new Exception("ArchivePerson failed on saving.");
		}		
	}


}
