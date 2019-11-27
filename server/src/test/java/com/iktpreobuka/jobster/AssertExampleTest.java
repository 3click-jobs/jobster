package com.iktpreobuka.jobster;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.iktpreobuka.jobster.controllers.UserController;

@RunWith (SpringRunner.class)
@SpringBootTest
public class AssertExampleTest {

	@Autowired
	private	UserController userController;
	
	@Test
	public void contexLoads() throws Exception {
		assertThat(userController).isNotNull();
	}
}
