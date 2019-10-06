package com.iktpreobuka.jobster.repositories;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.jobster.entities.CountryEntity;

public interface CountryRepository extends CrudRepository<CountryEntity, Integer> {
	
	public CountryEntity getByCountryName(String countryName);

}
