package com.iktpreobuka.jobster.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.jobster.entities.CountryEntity;
import com.iktpreobuka.jobster.entities.CountryRegionEntity;

@Repository
public interface CountryRegionRepository extends CrudRepository<CountryRegionEntity, Integer> {

	CountryRegionEntity getByCountryRegionNameAndCountry(String countryRegionName, Integer id);
	
	CountryRegionEntity getByCountryRegionNameAndCountry(String countryRegionName, CountryEntity country);

	@SuppressWarnings("unchecked")
	public CountryRegionEntity save(CountryRegionEntity countryRegionEntity);
	
	CountryRegionEntity getByCountryRegionName(String string);

	CountryRegionEntity getByCreatedById(Integer id);
	
}
