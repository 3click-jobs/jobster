package com.iktpreobuka.jobster.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;


import com.iktpreobuka.jobster.entities.CityEntity;
import com.iktpreobuka.jobster.entities.UserEntity;
import com.iktpreobuka.jobster.entities.dto.POSTCityDTO;

public interface CityDao {

	public CityEntity addNewCity(String cityName, Double longitude, Double latitude, String countryRegionName, String countryName, String iso2Code) throws Exception;

	public CityEntity addNewCityWithLoggedUser(String cityName, Double longitude, Double latitude, String countryRegionName, String countryName, String iso2Code, UserEntity loggedUser) throws Exception;

	Iterable<CityEntity> findCityByStatusLike(Integer status) throws Exception;
	//mrzim ovo govno od githuba

	void deleteCity(UserEntity loggedUser, CityEntity city) throws Exception;

	void undeleteCity(UserEntity loggedUser, CityEntity city) throws Exception;

	void archiveCity(UserEntity loggedUser, CityEntity city) throws Exception;

	public void unarchiveCity(UserEntity loggedUser, CityEntity city) throws Exception;
  
  CityEntity modifyCityWithLoggedUser(CityEntity city, POSTCityDTO updateCity, UserEntity loggedUser) throws Exception;

//pagination:
	public Page<CityEntity> findAll(int page, int pageSize, Direction direction,String sortBy);

}