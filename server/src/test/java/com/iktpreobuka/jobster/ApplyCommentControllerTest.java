package com.iktpreobuka.jobster;

import java.nio.charset.Charset;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class) 
@SpringBootTest 
@WebAppConfiguration 
public class ApplyCommentControllerTest {
	
	private static MockMvc mockMvc;
	
	@Autowired 
	private WebApplicationContext webApplicationContext;
	
	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(), 
			MediaType.APPLICATION_JSON.getSubtype(), 
			Charset.forName("utf8"));
	


}
