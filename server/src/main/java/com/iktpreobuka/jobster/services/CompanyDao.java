package com.iktpreobuka.jobster.services;

import com.iktpreobuka.jobster.entities.CompanyEntity;
import com.iktpreobuka.jobster.entities.UserEntity;
import com.iktpreobuka.jobster.entities.dto.CompanyDTO;

public interface CompanyDao {
	
	public UserEntity addNewCompany(UserEntity loggedUser, CompanyDTO newCompany) throws Exception;

	public void modifyCompany(UserEntity loggedUser, CompanyEntity company, CompanyDTO updateCompany) throws Exception;

	public void deleteCompany(UserEntity loggedUser, CompanyEntity company) throws Exception;

	public void undeleteCompany(UserEntity loggedUser, CompanyEntity company) throws Exception;

	public void archiveCompany(UserEntity loggedUser, CompanyEntity company) throws Exception;


}
