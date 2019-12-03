package com.iktpreobuka.jobster.services;

import com.iktpreobuka.jobster.entities.CountryEntity;
import com.iktpreobuka.jobster.entities.UserEntity;

public interface CountryDao {

	public CountryEntity addNewCountry(String countryName) throws Exception;

	public CountryEntity addNewCountryWithLoggedUser(String countryName, UserEntity loggedUser) throws Exception;

	public CountryEntity addNewCountryWithIso2Code(String countryName, String iso2Code) throws Exception;

	public CountryEntity addNewCountryWithIso2CodeAndLoggedUser(String countryName, String iso2Code, UserEntity loggedUser) throws Exception;

	public void archiveCountry(UserEntity loggedUser, CountryEntity country) throws Exception;

	void unarchiveCountry(UserEntity loggedUser, CountryEntity country) throws Exception;

	void undeleteCountry(UserEntity loggedUser, CountryEntity country) throws Exception;

	void deleteCountry(UserEntity loggedUser, CountryEntity country) throws Exception;

	Iterable<CountryEntity> findCountryByStatusLike(Integer status) throws Exception;

}