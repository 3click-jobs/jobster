package com.iktpreobuka.jobster.repositories;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.jobster.entities.CompanyEntity;
import com.iktpreobuka.jobster.entities.UserEntity;
import com.iktpreobuka.jobster.entities.dto.CompanyDTO;

@Repository
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

	public CompanyEntity getByCompanyRegistrationNumber(String companyId);
	public UserEntity findByEmailAndStatusLike(String email, Integer status);
	
	public Page<CompanyEntity> findCompanyByStatusLike(int i, Pageable pageable);

}
