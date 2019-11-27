package com.iktpreobuka.jobster.services;

import com.iktpreobuka.jobster.entities.CityEntity;
import com.iktpreobuka.jobster.entities.UserEntity;

public interface CityDistanceDao {


	public void addNewDistancesForCity(CityEntity city) throws Exception;

	public void addNewDistancesForCityWithLoggedUser(CityEntity city, UserEntity loggedUser) throws Exception;


}