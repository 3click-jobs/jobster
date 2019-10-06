package com.iktpreobuka.jobster.services;

import com.iktpreobuka.jobster.entities.CityEntity;

public interface CityDao {

	public CityEntity addNewCity(String cityName, Double longitude, Double latitude, String countryRegionName, String countryName) throws Exception;

}
