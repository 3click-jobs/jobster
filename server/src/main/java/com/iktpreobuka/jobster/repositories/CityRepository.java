package com.iktpreobuka.jobster.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.jobster.entities.CityEntity;
import com.iktpreobuka.jobster.entities.CountryRegionEntity;

public interface CityRepository extends CrudRepository<CityEntity, Integer> {

	public CityEntity getByCityNameIgnoreCase(String cityName);
	
	public CityEntity getByCityNameAndRegionId(String cityName, String countryRegion);

	public List<CityEntity> getByIdIsNot(Integer id);

	public CityEntity getByCityNameAndRegion(String city, CountryRegionEntity countryRegion);

	public Iterable<CityEntity> findByCityNameIgnoreCase();

	public boolean existsByCityNameIgnoreCase(String cityName);

	public boolean existsByLongitude(Double longitude);

	public boolean existsByLatitude(Double latitude);

	public boolean existsByRegion(CountryRegionEntity region);
}
