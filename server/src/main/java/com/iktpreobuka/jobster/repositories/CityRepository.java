
package com.iktpreobuka.jobster.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.jobster.entities.CityEntity;
import com.iktpreobuka.jobster.entities.CountryRegionEntity;

@Repository
public interface CityRepository extends CrudRepository<CityEntity, Integer> {

	public CityEntity getByCityName(String cityName);
	
	public List<CityEntity> getByCityNameIgnoreCase(String cityName);
	
	public CityEntity getByCityNameAndRegionId(String cityName, String countryRegion);

	public List<CityEntity> getByIdIsNot(Integer id);

	public CityEntity getByCityNameAndRegion(String city, CountryRegionEntity countryRegion);

	@SuppressWarnings("unchecked")
	public CityEntity save(CityEntity cityEntity);
	
	public CityEntity getByCreatedById(Integer id);

	public CityEntity getById(Integer id);

	public boolean existsByCityNameIgnoreCase(String cityName);

	public boolean existsByLongitude(Double longitude);

	public boolean existsByLatitude(Double latitude);

	public boolean existsByRegion(CountryRegionEntity region);

	/*public Iterable<CityEntity> getAllBySTATUS_ACTIVE();

	public Iterable<CityEntity> getAllBySTATUS_INACTIVE();

	public Iterable<CityEntity> getAllBySTATUS_ARCHIVED();*/

	public Iterable<CityEntity> findByStatusLike(Integer status);
}

