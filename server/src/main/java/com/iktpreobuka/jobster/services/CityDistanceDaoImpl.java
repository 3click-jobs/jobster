package com.iktpreobuka.jobster.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iktpreobuka.jobster.entities.CityDistanceEntity;
import com.iktpreobuka.jobster.entities.CityEntity;
import com.iktpreobuka.jobster.entities.UserEntity;
import com.iktpreobuka.jobster.repositories.CityDistanceRepository;
import com.iktpreobuka.jobster.repositories.CityRepository;
import com.iktpreobuka.jobster.util.CalculateCityDistance;

@Service 
public class CityDistanceDaoImpl implements CityDistanceDao {

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private CityDistanceRepository cityDistanceRepository;
	
	

	public void addNewDistancesForCity(CityEntity city) throws Exception {
		if ( city == null ) {
			throw new Exception("City is null.");
		}
		List<CityDistanceEntity> distances = new ArrayList<>();
		try {
			List<CityEntity> cities = cityRepository.getByIdIsNot(city.getId());
			for( CityEntity ci : cities) {
				Double milesDistance = CalculateCityDistance.calculateCityDistance(city.getLatitude(), city.getLongitude(), ci.getLatitude(), ci.getLongitude(), "MI");
				CityDistanceEntity distance = new CityDistanceEntity(city, ci, milesDistance * 1.609344, milesDistance);
				cityDistanceRepository.save(distance);
				distances.add(distance);
			}
		} catch (Exception e) {
			throw new Exception("addNewDistancesForCity save failed.");
		}
	}

	public void addNewDistancesForCityWithLoggedUser(CityEntity city, UserEntity loggedUser) throws Exception {
		if ( city == null || loggedUser == null ) {
			throw new Exception("City and/or logged user is null.");
		}
		List<CityDistanceEntity> distances = new ArrayList<>();
		try {
			List<CityEntity> cities = cityRepository.getByIdIsNot(city.getId());
			for( CityEntity ci : cities) {
				Double milesDistance = CalculateCityDistance.calculateCityDistance(city.getLatitude(), city.getLongitude(), ci.getLatitude(), ci.getLongitude(), "MI");
				CityDistanceEntity distance = new CityDistanceEntity(city, ci, milesDistance * 1.609344, milesDistance, loggedUser.getId());
				cityDistanceRepository.save(distance);
				distances.add(distance);
			}
		} catch (Exception e) {
			throw new Exception("addNewDistancesForCity save failed.");
		}
	}

}