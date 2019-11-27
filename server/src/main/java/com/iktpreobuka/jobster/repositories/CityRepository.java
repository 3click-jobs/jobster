package com.iktpreobuka.jobster.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.jobster.entities.CityEntity;
import com.iktpreobuka.jobster.entities.CountryRegionEntity;

@Repository
public interface CityRepository extends CrudRepository<CityEntity, Integer> {

	
	// public Iterable<CityEntity> findByCityNameIgnoreCase(); 
	// Blanusa cemu ovo sluzi kad mu ne dajes ime grada, a i kako bi vratio listu ako das ime grada? 
	// Pretpostavljam da si hteo ovo sto sam u sledecj liniji otkucao.

	public Iterable<CityEntity> getAllByStatusLike(Integer status);
	
	public boolean existsByCityNameIgnoreCase(String cityName);

	public boolean existsByLongitude(Double longitude);

	public boolean existsByLatitude(Double latitude);

	public boolean existsByRegion(CountryRegionEntity region);
	
	public CityEntity getByCityName(String cityName);
	
	public CityEntity getByCityNameIgnoreCase(String cityName);
	
	public CityEntity getByCityNameAndRegionId(String cityName, String countryRegion);

	public List<CityEntity> getByIdIsNot(Integer id);

	public CityEntity getByCityNameAndRegion(String city, CountryRegionEntity countryRegion);

	@SuppressWarnings("unchecked")
	public CityEntity save(CityEntity cityEntity);
	
	public CityEntity getByCreatedById(Integer id);

	//public Iterable<CityEntity> findByCityNameIgnoreCase();

	//public CityEntity getByCityNameIgnoreCase(String name);
}
