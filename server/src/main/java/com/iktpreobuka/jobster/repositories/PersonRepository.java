package com.iktpreobuka.jobster.repositories;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.jobster.entities.PersonEntity;
import com.iktpreobuka.jobster.entities.UserEntity;
import com.iktpreobuka.jobster.entities.dto.PersonDTO;


@Repository
public interface PersonRepository extends CrudRepository<PersonEntity, Integer> {

	public PersonEntity getById(Integer id);
	public Optional<PersonEntity> findById(Integer id);
	public PersonEntity findByIdAndStatusLike(Integer id, Integer status);
	public void save(@Valid PersonDTO newUser);
	public void save(UserEntity user);
	public Iterable<PersonEntity> findByStatusLike(Integer status);
	public PersonEntity getByEmailAndStatusLike(String email, Integer status);
	public PersonEntity getByEmail(String email);
	public PersonEntity getByMobilePhoneAndStatusLike(String mobilePhone, Integer status);
	public PersonEntity getByMobilePhone(String mobilePhone);
	public PersonEntity getByIdAndStatusLike(Integer userId, Integer status);

}

