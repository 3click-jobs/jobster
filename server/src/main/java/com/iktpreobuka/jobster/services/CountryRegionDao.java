package com.iktpreobuka.jobster.services;

import com.iktpreobuka.jobster.entities.CountryEntity;
import com.iktpreobuka.jobster.entities.CountryRegionEntity;
import com.iktpreobuka.jobster.entities.UserEntity;

public interface CountryRegionDao {

	public CountryRegionEntity addNewCountryRegion(String countryRegionName, CountryEntity country, UserEntity loggedUser) throws Exception;

	public CountryRegionEntity addNewCountryRegion(CountryEntity country, UserEntity loggedUser) throws Exception;

}