package com.iktpreobuka.jobster.repositories;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.jobster.entities.CityEntity;

public interface CityRepository extends CrudRepository<CityEntity, Integer> {

	public CityEntity getByCityName(String cityName);
}
