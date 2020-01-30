package com.iktpreobuka.jobster.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.jobster.entities.CountryEntity;

@Repository
//public interface CountryRepository extends CrudRepository<CountryEntity, Integer> {
//pagination:
public interface CountryRepository extends PagingAndSortingRepository<CountryEntity, Integer> {

	
	public CountryEntity getByCountryName(String countryName);

	public CountryEntity getByCountryNameAndIso2Code(String countryName, String iso2Code);
	
	public CountryEntity getByIso2Code(String iso2Code);

	public CountryEntity findByCountryNameAndIso2Code(String country, String iso2Code);

	@SuppressWarnings("unchecked")
	public CountryEntity save(CountryEntity countryEntity);


	public CountryEntity getByCreatedById(Integer id);

	public CountryEntity getById(Integer id);

	public CountryEntity getByCountryNameIgnoreCase(String name);

	public boolean existsByCountryNameIgnoreCase(String countryName);

	public boolean existsByIso2Code(String iso2Code);

	public CountryEntity findByIdAndStatusLike(Integer id, int i);

	public Iterable<CountryEntity> findByStatusLike(Integer status);


	public boolean existsByIso2CodeIgnoreCase(String iso2Code);
	public CountryEntity findByCountryNameIgnoreCase(String country);

	//public Page<CountryEntity> findCountryByStatusLike(int i, PageRequest pageRequest);
	public Page<CountryEntity> findCountryByStatusLike(int i, Pageable pageable);


	

}
