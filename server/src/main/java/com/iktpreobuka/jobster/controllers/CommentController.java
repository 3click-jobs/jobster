package com.iktpreobuka.jobster.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.jobster.controllers.util.RESTError;
import com.iktpreobuka.jobster.entities.ApplyContactEntity;
import com.iktpreobuka.jobster.entities.CommentEntity;
import com.iktpreobuka.jobster.entities.dto.AddCommentDTO;
import com.iktpreobuka.jobster.entities.dto.ShowCommentDTO;
import com.iktpreobuka.jobster.repositories.ApplyContactRepository;
import com.iktpreobuka.jobster.repositories.CommentRepository;
import com.iktpreobuka.jobster.repositories.UserAccountRepository;
import com.iktpreobuka.jobster.security.Views;
import com.iktpreobuka.jobster.services.ApplyContactDao;
import com.iktpreobuka.jobster.services.CommentDao;

@RestController
@RequestMapping(path = "/jobster/comment")
public class CommentController {

	@Autowired
	CommentRepository commentRepository;
	
	@Autowired
	UserAccountRepository userAccountRepository;
	
	@Autowired
	ApplyContactRepository applyContactRepository;
	
	@Autowired
	ApplyContactDao applyContactDao;
	
	@Autowired 
	CommentDao commentDao;
	
	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
	
	private String createErrorMessage(BindingResult result) { 
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
		}

	//@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/all") // get all comments
	public ResponseEntity<?> getAll(Principal principal) {
		logger.info("################ /jobster/comment/all/getAll started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<CommentEntity> comments = commentRepository.findAll();
			logger.info("---------------- Found comments - OK.");
			logger.info("---------------- Comments to DTOs service starting ");
			ArrayList<ShowCommentDTO> commentDTOs = commentDao.fromCommentsToDTOs(comments);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<ArrayList<ShowCommentDTO>>(commentDTOs, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET) // get all active comments
	public ResponseEntity<?> getAllActive(Principal principal) {
		logger.info("################ /jobster/comment/getAllActive started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<CommentEntity> comments = commentRepository.findByStatusLike(1);
			logger.info("---------------- Found comments - OK.");
			logger.info("---------------- Comments to DTOs service starting ");
			ArrayList<ShowCommentDTO> commentDTOs = commentDao.fromCommentsToDTOs(comments);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<ArrayList<ShowCommentDTO>>(commentDTOs, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	//@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/inactive") // get all inactive comments
	public ResponseEntity<?> getAllInactive(Principal principal) {
		logger.info("################ /jobster/comment/inactive/getAllInactive started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<CommentEntity> comments = commentRepository.findByStatusLike(0);
			logger.info("---------------- Found comments - OK.");
			logger.info("---------------- Comments to DTOs service starting ");
			ArrayList<ShowCommentDTO> commentDTOs = commentDao.fromCommentsToDTOs(comments);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<ArrayList<ShowCommentDTO>>(commentDTOs, HttpStatus.OK);

		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	//@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived") // get all archived comments
	public ResponseEntity<?> getAllArchived(Principal principal) {
		logger.info("################ /jobster/comment/archived/getAllArchived started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<CommentEntity> comments = commentRepository.findByStatusLike(-1);
			logger.info("---------------- Found comments - OK.");
			logger.info("---------------- Comments to DTOs service starting ");
			ArrayList<ShowCommentDTO> commentDTOs = commentDao.fromCommentsToDTOs(comments);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<ArrayList<ShowCommentDTO>>(commentDTOs, HttpStatus.OK);

		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	//@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "{id}") // get active by ID
	public ResponseEntity<?> getById(@PathVariable Integer id,Principal principal) {
		logger.info("################ /jobster/comment/getById started.");
		logger.info("Logged username: " + principal.getName());
		try {
			CommentEntity comment = commentRepository.findByIdAndStatusLike(id, 1);
			if (comment == null) {
				logger.info("++++++++++++++++ Active comment not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
			logger.info("---------------- Found comment - OK.");
			logger.info("---------------- Comment to DTO service starting ");
			ShowCommentDTO commentDTO = commentDao.fromCommentToDTO(comment);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(commentDTO, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	//@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/all/{id}/getByIdFromAll") // get by ID from all
	public ResponseEntity<?> getByIdAll(@PathVariable Integer id,Principal principal) {
		logger.info("################ /jobster/comment/getByIdFromAll started.");
		logger.info("Logged username: " + principal.getName());
		try {
			CommentEntity comment = commentRepository.findById(id).orElse(null);
			if (comment == null) {
				logger.info("++++++++++++++++ Comment not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
			logger.info("---------------- Found comment - OK.");
			logger.info("---------------- Comment to DTO service starting ");
			ShowCommentDTO commentDTO = commentDao.fromCommentToDTO(comment);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(commentDTO, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	//@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNewComment(@Valid @RequestBody AddCommentDTO newComment, Principal principal,BindingResult result){
		logger.info("################ /jobster/comment/{id}/addNewComment started.");
		logger.info("Logged username: " + principal.getName());
		if (result.hasErrors()) { 
			logger.error("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (newComment == null) {
			logger.error("---------------- New comment is null.");
	        return new ResponseEntity<>("New comment cannot be null", HttpStatus.BAD_REQUEST);
	      }
		if(newComment.getApplyId() == null) {
			logger.error("---------------- Apply ID null.");
	        return new ResponseEntity<>("Please provide applyJob id", HttpStatus.BAD_REQUEST);
		}
		if (newComment.getCommentTitle() == null || newComment.getCommentContent() == null || newComment.getRating() == null) {
			logger.error("---------------- One or more attributes are null");
			return new ResponseEntity<>("All attributes must be specified.", HttpStatus.BAD_REQUEST);
		}
		
		CommentEntity comment = new CommentEntity();
		
		try {

			ApplyContactEntity application = applyContactRepository.findByIdAndStatusLike(newComment.getApplyId(), 1);
			if(application == null) {
				logger.info("---------------- ApplyContact not found.");
				return new ResponseEntity<>("ApplyContact not found.", HttpStatus.NOT_FOUND);
			}
			if(!application.getCommentable()) {
				logger.info("---------------- Commenting not available at the moment.");
				return new ResponseEntity<>("Commenting not available at the moment.", HttpStatus.BAD_REQUEST);
			}
			logger.info("---------------- Application Found");
			
			Integer posterId = userAccountRepository.findByUsernameAndStatusLike(principal.getName(), 1).getUser().getId();
			if(!(posterId == application.getOffer().getEmployer().getId() || posterId == application.getSeek().getEmployee().getId())) {
				logger.error("---------------- "+ principal.getName() + " cannot comment on this job apply.");
				return new ResponseEntity<>(principal.getName() + " cannot comment on this job apply.", HttpStatus.FORBIDDEN);
			}
			
			logger.info("---------------- Comment oster Id found");
			
			Integer commReceiverId = applyContactDao.otherPartyFromApply(posterId, application);
			if(commReceiverId == null || userAccountRepository.findByIdAndStatusLike(commReceiverId, 1)==null) {
				logger.error("---------------- other party not found.");
				return new ResponseEntity<>(" other party not found", HttpStatus.BAD_REQUEST);
			}
			
			logger.info("---------------- Comment receiver Id found");
			
			comment.setApplication(application);
			comment.setCommentContent(newComment.getCommentContent());
			comment.setCommentDate(new Date());
			comment.setCommentTitle(newComment.getCommentTitle());
			comment.setCreatedById(posterId);
			comment.setRating(newComment.getRating());
			comment.setStatus(1);
			comment.setUpdatedById(null);
			comment.setCommentReceiver(commReceiverId);
			comment.setEdited(false);
			logger.info("---------------- Comment created!!!");
			commentRepository.save(comment);
			logger.info("---------------- Comment saved in DB!!!");

			return new ResponseEntity<>(comment, HttpStatus.OK);			
			
			
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
}
