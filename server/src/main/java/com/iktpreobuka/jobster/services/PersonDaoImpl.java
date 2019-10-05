package com.iktpreobuka.jobster.services;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iktpreobuka.jobster.entities.JobOfferEntity;
import com.iktpreobuka.jobster.entities.JobSeekEntity;
import com.iktpreobuka.jobster.entities.PersonEntity;
import com.iktpreobuka.jobster.entities.UserEntity;
import com.iktpreobuka.jobster.entities.dto.PersonDTO;
import com.iktpreobuka.jobster.enumerations.EGender;
import com.iktpreobuka.jobster.repositories.CityRepository;
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
	private JobSeekRepository jobSeekRepository;

	@Autowired
	private JobOfferRepository jobOfferRepository;


    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");


	@Override
	public UserEntity addNewPerson(UserEntity loggedUser, PersonDTO newPerson) throws Exception {
			if (newPerson.getFirstName() == null || newPerson.getLastName() == null || newPerson.getGender() == null || newPerson.getBirthDate() == null || newPerson.getEmail() == null || newPerson.getMobilePhone() == null || newPerson.getCity() == null ) {
				throw new Exception("Some data is null.");
			}
			UserEntity temporaryUser = new PersonEntity();
			try {
				temporaryUser = personRepository.getByEmail(newPerson.getEmail());
			} catch (Exception e1) {
				throw new Exception("addNewPerson Exist user check failed.");
			}
			if (temporaryUser != null && (!((PersonEntity) temporaryUser).getFirstName().equals(newPerson.getFirstName()) || !((PersonEntity) temporaryUser).getLastName().equals(newPerson.getLastName()) || !((PersonEntity) temporaryUser).getGender().toString().equals(newPerson.getGender()) || !((PersonEntity) temporaryUser).getBirthDate().equals(formatter.parse(newPerson.getBirthDate())) || !temporaryUser.getEmail().equals(newPerson.getEmail()) || !temporaryUser.getMobilePhone().equals(newPerson.getMobilePhone()) || !temporaryUser.getCity().equals(cityRepository.getByCityName(newPerson.getCity())) )) {
				throw new Exception("User exists, but import data not same as exist user data.");
			}
			UserEntity user = new PersonEntity();
		try {
			try {
				((PersonEntity) user).setFirstName(newPerson.getFirstName());
				((PersonEntity) user).setLastName(newPerson.getLastName());
				((PersonEntity) user).setGender(EGender.valueOf(newPerson.getGender()));
			    Date bithDate = formatter.parse(newPerson.getBirthDate());
			    ((PersonEntity) user).setBirthDate(bithDate);
			    user.setCity(cityRepository.getByCityName(newPerson.getCity()));
			    user.setEmail(newPerson.getEmail());
			    user.setMobilePhone(newPerson.getMobilePhone());
			    user.setDetailsLink(newPerson.getDetailsLink());
			    user.setNumberOfRatings(0);
			    user.setRating(0.0);
				user.setStatusActive();
				user.setCreatedById(loggedUser.getId());
				personRepository.save(user);
				temporaryUser = user;
			} catch (Exception e) {
				throw new Exception("addNewPerson save failed.");
			}
			return temporaryUser;
		} catch (Exception e) {
			throw new Exception("addNewPerson save failed.");
		}
	}

	@Override
	public void modifyPerson(UserEntity loggedUser, PersonEntity person, PersonDTO updatePerson) throws Exception {
		if (updatePerson.getFirstName() == null && updatePerson.getLastName() == null && updatePerson.getGender() == null && updatePerson.getBirthDate() == null && updatePerson.getEmail() == null && updatePerson.getMobilePhone() == null && updatePerson.getCity() == null ) {
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
			if (updatePerson.getCity() != null && !cityRepository.getByCityName(updatePerson.getCity()).equals(person.getCity()) && !updatePerson.getCity().equals(" ") && !updatePerson.getMobilePhone().equals("")) {
				person.setCity(cityRepository.getByCityName(updatePerson.getCity()));
				i++;
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
