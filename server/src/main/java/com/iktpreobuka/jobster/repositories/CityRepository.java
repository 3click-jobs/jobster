
package com.iktpreobuka.jobster.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.jobster.entities.CityEntity;
import com.iktpreobuka.jobster.entities.CountryRegionEntity;

@Repository
public interface CityRepository extends PagingAndSortingRepository<CityEntity, Integer> {

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

	public Iterable<CityEntity> findByStatusLike(Integer status);

	public CityEntity findByIdAndStatusLike(Integer id, int i);


	public Iterable<CityEntity> findByStatus(Integer status);

	public Iterable<CityEntity> findCityByStatus(int i);

	public Iterable<CityEntity> getByStatus(int i);

	public CityEntity findByCityNameAndRegion(String city, CountryRegionEntity countryRegion);
	
//pagination:
	public Page<CityEntity> findCityByStatusLike(int i, Pageable pageable);
	public Page<CityEntity> getByCityNameIgnoreCase(String name, Pageable pageable);


	public CityEntity findByCityNameAndStatusLike(String cityName, int i);

}

