package com.iktpreobuka.jobster.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iktpreobuka.jobster.entities.CommentEntity;
import com.iktpreobuka.jobster.repositories.CommentRepository;

@RestController
@RequestMapping(path = "/jobster/comment")
public class CommentController {

	@Autowired
	CommentRepository commentRepository;

	//@Secured("ROLE_ADMIN")
	//@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/all") // get all comments
	public ResponseEntity<?> getAll() {
		try {
			Iterable<CommentEntity> comments = commentRepository.findAll();
			return new ResponseEntity<Iterable<CommentEntity>>(comments, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	//@Secured("ROLE_ADMIN")
	//@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET) // get all active comments
	public ResponseEntity<?> getAllActive() {
		try {
			Iterable<CommentEntity> comments = commentRepository.findByStatusLike(1);
			return new ResponseEntity<Iterable<CommentEntity>>(comments, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	//@Secured("ROLE_ADMIN")
	//@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/inactive") // get all inactive comments
	public ResponseEntity<?> getAllInactive() {
		try {
			Iterable<CommentEntity> comments = commentRepository.findByStatusLike(0);
			return new ResponseEntity<Iterable<CommentEntity>>(comments, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	//@Secured("ROLE_ADMIN")
	//@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived") // get all archived comments
	public ResponseEntity<?> getAllArchived() {
		try {
			Iterable<CommentEntity> comments = commentRepository.findByStatusLike(-1);
			return new ResponseEntity<Iterable<CommentEntity>>(comments, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	//@Secured("ROLE_ADMIN")
	//@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/{id}") // get active by ID
	public ResponseEntity<?> getById(@PathVariable Integer id) {
		try {
			CommentEntity comment = commentRepository.findByIdAndStatusLike(id, 1);
			return new ResponseEntity<>(comment, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	//@Secured("ROLE_ADMIN")
	//@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/{id}") // get by ID from all
	public ResponseEntity<?> getByIdAll(@PathVariable Integer id) {
		try {
			CommentEntity comment = commentRepository.findById(id).orElse(null);
			if (comment == null)
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return new ResponseEntity<CommentEntity>(comment, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
