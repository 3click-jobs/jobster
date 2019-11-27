package com.iktpreobuka.jobster.services;

import com.iktpreobuka.jobster.entities.PersonEntity;
import com.iktpreobuka.jobster.entities.UserEntity;
import com.iktpreobuka.jobster.entities.dto.PersonDTO;


public interface PersonDao {

	public UserEntity addNewPerson(PersonDTO newPerson) throws Exception;

	public void modifyPerson(UserEntity loggedUser, PersonEntity person, PersonDTO updatePerson) throws Exception;

	public void deletePerson(UserEntity loggedUser, PersonEntity person) throws Exception;

	public void undeletePerson(UserEntity loggedUser, PersonEntity person) throws Exception;

	public void archivePerson(UserEntity loggedUser, PersonEntity person) throws Exception;

}
