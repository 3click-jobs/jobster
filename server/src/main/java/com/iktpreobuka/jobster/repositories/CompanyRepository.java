package com.iktpreobuka.jobster.repositories;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.jobster.entities.CompanyEntity;
import com.iktpreobuka.jobster.entities.UserEntity;
import com.iktpreobuka.jobster.entities.dto.CompanyDTO;

public interface CompanyRepository extends CrudRepository<CompanyEntity, Integer> {
	
	public CompanyEntity getById(Integer id);
	public Optional<CompanyEntity> findById(Integer id);
	public CompanyEntity findByIdAndStatusLike(Integer id, Integer status);
	public void save(@Valid CompanyDTO newUser);
	public void save(UserEntity user);
	public Iterable<CompanyEntity> findByStatusLike(Integer status);
	public CompanyEntity getByEmailAndStatusLike(String email, Integer status);
	public CompanyEntity getByEmail(String email);
	public CompanyEntity getByMobilePhoneAndStatusLike(String mobilePhone, Integer status);
	public CompanyEntity getByMobilePhone(String mobilePhone);
	public CompanyEntity getByIdAndStatusLike(Integer userId, Integer status);

}