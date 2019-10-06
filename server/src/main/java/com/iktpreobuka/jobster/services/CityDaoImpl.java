package com.iktpreobuka.jobster.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iktpreobuka.jobster.entities.CityEntity;
import com.iktpreobuka.jobster.repositories.CityRepository;

@Service 
public class CityDaoImpl implements CityDao {

	@Autowired
	private CityRepository cityRepository;
	
	
	public CityEntity addNewCity(String cityName, Double longitude, Double latitude, String countryRegionName, String countryName) throws Exception {
		CityEntity city = new CityEntity();
		
		//OVDE IDE KOD
		
		cityRepository.save(city);
		return city;
	}

}
