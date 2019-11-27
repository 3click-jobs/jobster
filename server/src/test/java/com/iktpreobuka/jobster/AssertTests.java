package com.iktpreobuka.jobster;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.iktpreobuka.jobster.controllers.CompanyController;
import com.iktpreobuka.jobster.controllers.PersonController;
import com.iktpreobuka.jobster.controllers.UserAccountController;
import com.iktpreobuka.jobster.controllers.UserController;

@RunWith (SpringRunner.class)
@SpringBootTest
public class AssertTests {

	@Autowired
	private	UserController userController;
	
	@Autowired
	private	CompanyController companyController;
	
	@Autowired
	private	PersonController personController;

	@Autowired
	private	UserAccountController userAccountController;

	
	@Test
	public void contexLoadsUser() throws Exception {
		assertThat(userController).isNotNull();
	}
	
	@Test
	public void contexLoadsCompany() throws Exception {
		assertThat(companyController).isNotNull();
	}
	
	@Test
	public void contexLoadsPerson() throws Exception {
		assertThat(personController).isNotNull();
	}

	@Test
	public void contexLoadsUserAccount() throws Exception {
		assertThat(userAccountController).isNotNull();
	}

}
