
package com.iktpreobuka.jobster.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.jobster.entities.CityEntity;
import com.iktpreobuka.jobster.entities.CountryRegionEntity;

@Repository
public interface CityRepository extends CrudRepository<CityEntity, Integer> {

	public CityEntity getByCityName(String cityName);
	
	public CityEntity getByCityNameAndRegionId(String cityName, String countryRegion);

	public List<CityEntity> getByIdIsNot(Integer id);

	public CityEntity getByCityNameAndRegion(String city, CountryRegionEntity countryRegion);

	@SuppressWarnings("unchecked")
	public CityEntity save(CityEntity cityEntity);
	
	public CityEntity getByCreatedById(Integer id);

	//public Iterable<CityEntity> findByCityNameIgnoreCase();

	//public CityEntity getByCityNameIgnoreCase(String name);
}

