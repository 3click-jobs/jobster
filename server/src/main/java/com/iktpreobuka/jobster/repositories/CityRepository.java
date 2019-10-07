package com.iktpreobuka.jobster.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.jobster.entities.CityEntity;
import com.iktpreobuka.jobster.entities.CountryRegionEntity;

public interface CityRepository extends CrudRepository<CityEntity, Integer> {

	public CityEntity getByCityName(String cityName);
	
	public CityEntity getByCityNameAndCountryRegionId(String cityName, String countryRegion);

	public List<CityEntity> getByIdIsNot(Integer id);

	public CityEntity getByCityNameAndCountryRegion(String city, CountryRegionEntity countryRegion);
}
