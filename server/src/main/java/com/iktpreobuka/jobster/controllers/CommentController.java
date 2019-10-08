package com.iktpreobuka.jobster.controllers;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.jobster.controllers.util.RESTError;
import com.iktpreobuka.jobster.entities.CommentEntity;
import com.iktpreobuka.jobster.repositories.CommentRepository;
import com.iktpreobuka.jobster.security.Views;

@RestController
@RequestMapping(path = "/jobster/comment")
public class CommentController {

	@Autowired
	CommentRepository commentRepository;
	
	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	//@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/all") // get all comments
	public ResponseEntity<?> getAll(Principal principal) {
		logger.info("################ /jobster/comment/all started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<CommentEntity> comments = commentRepository.findAll();
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<CommentEntity>>(comments, HttpStatus.OK);

		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET) // get all active comments
	public ResponseEntity<?> getAllActive(Principal principal) {
		logger.info("################ /jobster/comment started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<CommentEntity> comments = commentRepository.findByStatusLike(1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<CommentEntity>>(comments, HttpStatus.OK);

		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	//@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/inactive") // get all inactive comments
	public ResponseEntity<?> getAllInactive(Principal principal) {
		logger.info("################ /jobster/comment/inactive started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<CommentEntity> comments = commentRepository.findByStatusLike(0);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<CommentEntity>>(comments, HttpStatus.OK);

		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	//@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived") // get all archived comments
	public ResponseEntity<?> getAllArchived(Principal principal) {
		logger.info("################ /jobster/comment/archived started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<CommentEntity> comments = commentRepository.findByStatusLike(-1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<CommentEntity>>(comments, HttpStatus.OK);

		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	//@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/{id}") // get active by ID
	public ResponseEntity<?> getById(@PathVariable Integer id,Principal principal) {
		logger.info("################ /jobster/comment/getActiveById started.");
		logger.info("Logged username: " + principal.getName());
		try {
			CommentEntity comment = commentRepository.findByIdAndStatusLike(id, 1);
			if (comment == null) {
				logger.info("++++++++++++++++ Active comment not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(comment, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	//@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/all/{id}") // get by ID from all
	public ResponseEntity<?> getByIdAll(@PathVariable Integer id,Principal principal) {
		logger.info("################ /jobster/comment/getByIdFromAll started.");
		logger.info("Logged username: " + principal.getName());
		try {
			CommentEntity comment = commentRepository.findById(id).orElse(null);
			if (comment == null) {
				logger.info("++++++++++++++++ Comment not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<CommentEntity>(comment, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
