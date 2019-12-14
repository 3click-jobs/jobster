package com.iktpreobuka.jobster.repositories;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.jobster.entities.CityDistanceEntity;
import com.iktpreobuka.jobster.entities.CityEntity;

@Repository
public interface CityDistanceRepository extends CrudRepository<CityDistanceEntity, Integer> {

	public void deleteByFromCity(CityEntity fromCity);
	
	public void deleteByToCity(CityEntity cty);

	public Iterable<CityDistanceEntity> findByFromCity(CityEntity cty);

	public Iterable<CityDistanceEntity> findByFromCityLike(CityEntity cty);

	@Query("select d from CityDistanceEntity d join d.fromCity f where f.id=:fromCityId")
	public Iterable<CityDistanceEntity> findByFromCityIdLike(@Param("fromCityId") Integer fromCityId);

	
}
