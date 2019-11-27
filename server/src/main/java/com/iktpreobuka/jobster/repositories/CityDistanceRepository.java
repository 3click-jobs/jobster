package com.iktpreobuka.jobster.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.jobster.entities.CityDistanceEntity;
import com.iktpreobuka.jobster.entities.CityEntity;

@Repository
public interface CityDistanceRepository extends CrudRepository<CityDistanceEntity, Integer> {

	public void deleteByFromCity(CityEntity fromCity);
	
}
