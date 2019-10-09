package com.iktpreobuka.jobster.repositories;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.jobster.entities.CountryEntity;

public interface CountryRepository extends CrudRepository<CountryEntity, Integer> {
	
	public CountryEntity getByCountryName(String countryName);

	public CountryEntity getByCountryNameAndIso2Code(String countryName, String iso2Code);
	
	public CountryEntity getByIso2Code(String iso2Code);

	public CountryEntity findByCountryNameAndIso2Code(String country, String iso2Code);

	@SuppressWarnings("unchecked")
	public CountryEntity save(CountryEntity countryEntity);

}
