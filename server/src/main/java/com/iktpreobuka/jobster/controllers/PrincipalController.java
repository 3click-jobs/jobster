package com.iktpreobuka.jobster.controllers;

import java.security.Principal;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Iterables;
import com.iktpreobuka.jobster.controllers.util.RESTError;
import com.iktpreobuka.jobster.controllers.util.UserCustomValidator;
import com.iktpreobuka.jobster.entities.PersonEntity;
import com.iktpreobuka.jobster.entities.UserAccountEntity;
import com.iktpreobuka.jobster.entities.UserEntity;
import com.iktpreobuka.jobster.entities.dto.PersonDTO;
import com.iktpreobuka.jobster.enumerations.EUserRole;
import com.iktpreobuka.jobster.repositories.PersonRepository;
import com.iktpreobuka.jobster.repositories.UserAccountRepository;
import com.iktpreobuka.jobster.services.PersonDao;
import com.iktpreobuka.jobster.services.UserAccountDao;

@Controller
@RestController
@RequestMapping(value= "/jobster/users/principal")
public class PrincipalController {

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	@RequestMapping(method = RequestMethod.GET, value = "/nesto")
	public ResponseEntity<?> userNamePrincipal(Principal principal) {
		logger.info("metoda pocela");
		try {
			logger.info("Logged username: " + principal.getName());
		} catch (Exception e) {
			return new ResponseEntity<String>("Eror is" + " " + e.getMessage() + " " + e, HttpStatus.BAD_REQUEST);
		}
		logger.info("skoro zavrsla");
		return new ResponseEntity<String>("Metoda prosla " + principal.getName(), HttpStatus.OK);
	}
	
}
