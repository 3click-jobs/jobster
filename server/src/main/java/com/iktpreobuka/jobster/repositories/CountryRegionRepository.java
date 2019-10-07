package com.iktpreobuka.jobster.repositories;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.jobster.entities.CountryEntity;
import com.iktpreobuka.jobster.entities.CountryRegionEntity;

public interface CountryRegionRepository extends CrudRepository<CountryRegionEntity, Integer> {

	CountryRegionEntity getByCountryRegionNameAndCountry(String countryRegionName, Integer id);
	
	CountryRegionEntity getByCountryRegionNameAndCountry(String countryRegionName, CountryEntity country);

}
