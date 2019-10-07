package com.iktpreobuka.jobster.services;

import com.iktpreobuka.jobster.entities.CityEntity;
import com.iktpreobuka.jobster.entities.UserEntity;

public interface CityDao {

	public CityEntity addNewCity(String cityName, Double longitude, Double latitude, String countryRegionName, String countryName, String iso2Code, UserEntity loggedUser) throws Exception;

}