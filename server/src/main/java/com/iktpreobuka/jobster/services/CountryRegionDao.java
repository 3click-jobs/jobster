package com.iktpreobuka.jobster.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;

import com.iktpreobuka.jobster.entities.CountryEntity;
import com.iktpreobuka.jobster.entities.CountryRegionEntity;
import com.iktpreobuka.jobster.entities.UserEntity;

public interface CountryRegionDao {

	public CountryRegionEntity addNewCountryRegion(String countryRegionName, CountryEntity country) throws Exception;

	public CountryRegionEntity addNewCountryRegionWithLoggedUser(String countryRegionName, CountryEntity country, UserEntity loggedUser) throws Exception;

	public CountryRegionEntity addNullCountryRegion(CountryEntity country) throws Exception;

	public CountryRegionEntity addNullCountryRegionWithLoggedUser(CountryEntity country, UserEntity loggedUser) throws Exception;

	Iterable<CountryRegionEntity> findRegionByStatusLike(Integer status) throws Exception;

	public void archiveRegion(UserEntity loggedUser, CountryRegionEntity region) throws Exception;

	void unarchiveRegion(UserEntity loggedUser, CountryRegionEntity region) throws Exception;

	void undeleteRegion(UserEntity loggedUser, CountryRegionEntity region) throws Exception;

	void deleteRegion(UserEntity loggedUser, CountryRegionEntity region) throws Exception;

//pagination:
	public Page<CountryRegionEntity> findAll(int page, int pageSize, Direction direction,String sortBy);
/*
	public Page<CountryRegionEntity> findAll(int page, int pageSize);
	
	public Page<CountryRegionEntity> findAll(int page, int pageSize, Direction direction);
*/
}