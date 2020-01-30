package com.iktpreobuka.jobster.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;

import com.iktpreobuka.jobster.entities.CountryEntity;
import com.iktpreobuka.jobster.entities.UserEntity;

public interface CountryDao {

	public CountryEntity addNewCountry(String countryName) throws Exception;

	public CountryEntity addNewCountryWithLoggedUser(String countryName, UserEntity loggedUser) throws Exception;

	public CountryEntity addNewCountryWithIso2Code(String countryName, String iso2Code) throws Exception;

	public CountryEntity addNewCountryWithIso2CodeAndLoggedUser(String countryName, String iso2Code, UserEntity loggedUser) throws Exception;

	void undeleteCountry(CountryEntity country) throws Exception;

	void deleteCountry(CountryEntity country) throws Exception;

	Iterable<CountryEntity> findCountryByStatusLike(Integer status) throws Exception;

	public void archiveCountry(CountryEntity country) throws Exception;

	void archiveCountryWithLoggedUser(UserEntity loggedUser, CountryEntity country) throws Exception;

	public void unarchiveCountry(CountryEntity country) throws Exception;

	void unarchiveCountryWithLoggedUser(UserEntity loggedUser, CountryEntity country) throws Exception;

	void undeleteCountryWithLoggedUser(UserEntity loggedUser, CountryEntity country) throws Exception;

	void deleteCountryWIthLoggedUser(UserEntity loggedUser, CountryEntity country) throws Exception;
	
	
	
//pagination:
/*version1:
	public Page<CountryEntity> findAll(int page);
*/
	public Page<CountryEntity> findAll(int page, int pageSize, Direction direction,String sortBy);

//	public Page<CountryEntity> findCountryByStatusLike(int i, int page, int pageSize, Direction direction,String sortBy);

}