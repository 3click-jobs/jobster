package com.iktpreobuka.jobster.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Iterables;
import com.iktpreobuka.jobster.controllers.util.RESTError;
import com.iktpreobuka.jobster.entities.ApplyContactEntity;
import com.iktpreobuka.jobster.entities.CommentEntity;
import com.iktpreobuka.jobster.entities.UserEntity;
import com.iktpreobuka.jobster.entities.dto.AddCommentDTO;
import com.iktpreobuka.jobster.entities.dto.ShowCommentDTO;
import com.iktpreobuka.jobster.enumerations.EUserRole;
import com.iktpreobuka.jobster.repositories.ApplyContactRepository;
import com.iktpreobuka.jobster.repositories.CommentRepository;
import com.iktpreobuka.jobster.repositories.UserAccountRepository;
import com.iktpreobuka.jobster.repositories.UserRepository;
import com.iktpreobuka.jobster.services.ApplyContactDao;
import com.iktpreobuka.jobster.services.CommentDao;
import com.iktpreobuka.jobster.services.EmailDao;

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
	UserRepository userRepository;

	@Autowired
	ApplyContactDao applyContactDao;

	@Autowired
	CommentDao commentDao;
	
	@Autowired
	EmailDao emailDao;

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
	}

	// @Secured("ROLE_ADMIN")
	//@JsonView(Views.Admin.class)
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
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	//@Secured("ROLE_ADMIN")
	//@JsonView(Views.Admin.class)
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
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// @Secured("ROLE_ADMIN")
	//@JsonView(Views.Admin.class)
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
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// @Secured("ROLE_ADMIN")
	//@JsonView(Views.Admin.class)
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
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// @Secured("ROLE_ADMIN")
	//@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/active/{id}") // get active by ID
	public ResponseEntity<?> getActiveById(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/comment/active/getActiveById started.");
		logger.info("Logged username: " + principal.getName());
		try {
			CommentEntity comment = commentRepository.findByIdAndStatusLike(id, 1);
			if (comment == null) {
				logger.info("++++++++++++++++ Active comment not found");
				return new ResponseEntity<>("Active comment not found.",HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found active comment - OK.");
			logger.info("---------------- Comment to DTO service starting ");
			ShowCommentDTO commentDTO = commentDao.fromCommentToDTO(comment);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<ShowCommentDTO>(commentDTO, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// @Secured("ROLE_ADMIN")
	//@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/inactive/{id}") // get inactive by ID
	public ResponseEntity<?> getInactiveById(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/comment/inactive/getInactiveById started.");
		logger.info("Logged username: " + principal.getName());
		try {
			CommentEntity comment = commentRepository.findByIdAndStatusLike(id, 0);
			if (comment == null) {
				logger.info("++++++++++++++++ Inactive comment not found");
				return new ResponseEntity<>("Inactive comment not found.",HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found inactive comment - OK.");
			logger.info("---------------- Comment to DTO service starting ");
			ShowCommentDTO commentDTO = commentDao.fromCommentToDTO(comment);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<ShowCommentDTO>(commentDTO, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// @Secured("ROLE_ADMIN")
	//@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived/{id}") // get archived by ID
	public ResponseEntity<?> getArchivedById(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/comment/archived/getArchivedById started.");
		logger.info("Logged username: " + principal.getName());
		try {
			CommentEntity comment = commentRepository.findByIdAndStatusLike(id, -1);
			if (comment == null) {
				logger.info("++++++++++++++++ archived comment not found");
				return new ResponseEntity<>("Archived comment not found.",HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found archivedcomment - OK.");
			logger.info("---------------- Comment to DTO service starting ");
			ShowCommentDTO commentDTO = commentDao.fromCommentToDTO(comment);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<ShowCommentDTO>(commentDTO, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// @Secured("ROLE_ADMIN")
	//@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/all/{id}") // get by ID from all
	public ResponseEntity<?> getByIdAll(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/comment/all/getByIdAll started.");
		logger.info("Logged username: " + principal.getName());
		try {
			CommentEntity comment = commentRepository.findById(id).orElse(null);
			if (comment == null) {
				logger.info("++++++++++++++++ Comment not found");
				return new ResponseEntity<>("Comment not found.",HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found comment - OK.");
			logger.info("---------------- Comment to DTO service starting ");
			ShowCommentDTO commentDTO = commentDao.fromCommentToDTO(comment);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<ShowCommentDTO>(commentDTO, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// @Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/user/{id}") // Get comments received by user
	public ResponseEntity<?> getUsersComment(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/comment/getUsersComment started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<CommentEntity> comments = commentRepository.findByCommentReceiverAndStatusLike(id, 1);
			if(Iterables.isEmpty(comments)) {
				logger.info("++++++++++++++++ User has no comments");
				return new ResponseEntity<>("User has no comments.",HttpStatus.NOT_FOUND);
			}
			
			logger.info("---------------- Found comments - OK.");
			logger.info("---------------- Comments to DTOs service starting ");
			ArrayList<ShowCommentDTO> commentDTOs = commentDao.fromCommentsToDTOs(comments);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<ArrayList<ShowCommentDTO>>(commentDTOs, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// @Secured("ROLE_ADMIN")
		// @JsonView(Views.Admin.class)
		@RequestMapping(method = RequestMethod.GET, value = "/apply/{id}") // Get comments by apply
		public ResponseEntity<?> getApplicationComment(@PathVariable Integer id, Principal principal) {
			logger.info("################ /jobster/comment/getApplicationComment started.");
			logger.info("Logged username: " + principal.getName());
			try {
				ApplyContactEntity application = applyContactRepository.findByIdAndStatusLike(id, 1);
				Iterable<CommentEntity> comments = commentRepository.findByApplicationAndStatusLike(application, 1);
				if(Iterables.isEmpty(comments)) {
					logger.info("++++++++++++++++ Application has no comments");
					return new ResponseEntity<>("Application has no comments.",HttpStatus.NOT_FOUND);
				}
				logger.info("---------------- Found comments - OK.");
				logger.info("---------------- Comments to DTOs service starting ");
				ArrayList<ShowCommentDTO> commentDTOs = commentDao.fromCommentsToDTOs(comments);
				logger.info("---------------- Finished OK.");
				return new ResponseEntity<ArrayList<ShowCommentDTO>>(commentDTOs, HttpStatus.OK);
			} catch (Exception e) {
				logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
				return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

	// @Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNewComment(@Valid @RequestBody AddCommentDTO newComment, Principal principal,
			BindingResult result) {
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
		if (newComment.getApplyId() == null) {
			logger.error("---------------- Apply ID null.");
			return new ResponseEntity<>("Please provide applyJob id", HttpStatus.BAD_REQUEST);
		}
		if (newComment.getCommentTitle() == null || newComment.getCommentContent() == null
				|| newComment.getRating() == null) {
			logger.error("---------------- One or more attributes are null");
			return new ResponseEntity<>("All attributes must be specified.", HttpStatus.BAD_REQUEST);
		}

		CommentEntity comment = new CommentEntity();

		try {

			ApplyContactEntity application = applyContactRepository.findByIdAndStatusLike(newComment.getApplyId(), 1);
			if (application == null) {
				logger.info("---------------- ApplyContact not found.");
				return new ResponseEntity<>("ApplyContact not found.", HttpStatus.NOT_FOUND);
			}
			if (!application.getCommentable()) {
				logger.info("---------------- Commenting not available at the moment.");
				return new ResponseEntity<>("Commenting not available at the moment.", HttpStatus.BAD_REQUEST);
			}
			logger.info("---------------- Application Found");

			Integer posterId = userAccountRepository.findByUsernameAndStatusLike(principal.getName(), 1).getUser()
					.getId();
			if (!(posterId == application.getOffer().getEmployer().getId()
					|| posterId == application.getSeek().getEmployee().getId())) {
				logger.error("---------------- " + principal.getName() + " cannot comment on this job apply.");
				return new ResponseEntity<>(principal.getName() + " cannot comment on this job apply.",
						HttpStatus.FORBIDDEN);
			}

			logger.info("---------------- Comment poster Id found");

			Integer commReceiverId = applyContactDao.otherPartyFromApply(posterId, application);
			if (commReceiverId == null || userRepository.getByIdAndStatusLike(commReceiverId, 1) == null) {
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
			
			logger.info("---------------- Starting update comment receiver rating!!!");
			commentDao.updateReceiverRating(commReceiverId, comment.getRating());
			logger.info("---------------- Comment receiver rating updated!!!");
			
			logger.info("---------------- Email sending service started...");
			emailDao.sendCommentAddedEmail(userRepository.getByIdAndStatusLike(commReceiverId, 1));
			logger.info("---------------- Email sent");
			
			return new ResponseEntity<>(comment, HttpStatus.OK);

		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	// @Secured("ROLE_ADMIN")
		//@JsonView(Views.Admin.class)
		@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
		public ResponseEntity<?> editComment(@PathVariable Integer id, @Valid @RequestBody AddCommentDTO updateComment, Principal principal, BindingResult result) {
			logger.info("################ /jobster/comment/{id}/editComment started.");
			logger.info("Logged user: " + principal.getName());
			if (result.hasErrors()) { 
				logger.info("---------------- Validation has errors - " + createErrorMessage(result));
				return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
				}
			if (updateComment == null) {
				logger.info("---------------- New comment data is null.");
		        return new ResponseEntity<>("New Comment data is null.", HttpStatus.BAD_REQUEST);
		      }
			if (updateComment.getCommentContent() == null && updateComment.getCommentTitle() == null && updateComment.getRating() == null ) {
				logger.info("---------------- All atributes are null.");
				return new ResponseEntity<>("All atributes are null", HttpStatus.BAD_REQUEST);
			}
			if ( ( updateComment.getCommentContent() != null && (updateComment.getCommentContent().equals(" ") || updateComment.getCommentContent().equals("") ) )
					|| ( updateComment.getCommentTitle() != null && (updateComment.getCommentTitle().equals(" ") || updateComment.getCommentTitle().equals("") ) )) {
				logger.info("---------------- Comment content or title is blank.");
				return new ResponseEntity<>("Comment content or title is blank", HttpStatus.BAD_REQUEST);
			}
			if ( ( updateComment.getCommentContent() != null && (updateComment.getCommentContent().equals(" ") || updateComment.getCommentContent().equals("") ) )
					|| ( updateComment.getCommentTitle() != null && (updateComment.getCommentTitle().equals(" ") || updateComment.getCommentTitle().equals("") ) )) {
				logger.info("---------------- Comment content or title is blank.");
				return new ResponseEntity<>("Comment content or title is blank", HttpStatus.BAD_REQUEST);
			}
			
			try {
				CommentEntity comm = commentRepository.findByIdAndStatusLike(id, 1);
				
				if (comm == null) {
					logger.info("---------------- Comment not found.");
			        return new ResponseEntity<>("Comment not found.", HttpStatus.NOT_FOUND);
			      }
				Integer puterId = userAccountRepository.findByUsernameAndStatusLike(principal.getName(), 1).getUser()
						.getId();
				if (!(puterId == comm.getCreatedById())) {
					logger.error("---------------- " + principal.getName() + " tried to edit comment which was not created by him");
					return new ResponseEntity<>(principal.getName() + " Cannot edit this comment ",
							HttpStatus.FORBIDDEN);
				}
				logger.info("Comment found ok.");
				
				if (!(updateComment.getCommentContent() == null || updateComment.getCommentContent().equals(" ") || updateComment.getCommentContent().equals("") )) {
					comm.setCommentContent(updateComment.getCommentContent());
				}
				if (!(updateComment.getCommentTitle() == null || updateComment.getCommentTitle().equals(" ") || updateComment.getCommentTitle().equals("") )) {
					comm.setCommentTitle(updateComment.getCommentTitle());
				}
				if (updateComment.getRating()!=null && updateComment.getRating()>0 && updateComment.getRating()<6) {
					comm.setRating(updateComment.getRating());
				}
				comm.setCommentDate(new Date());
				comm.setEdited(true);
				comm.setUpdatedById(puterId);
							
				logger.info("---------------- Comment edited successfuly !!!");
				commentRepository.save(comm);
				logger.info("---------------- Comment saved in DB!!!");

				logger.info("---------------- Starting update comment receiver rating!!!");
				commentDao.updateReceiverRating(comm.getCommentReceiver(), comm.getRating());
				logger.info("---------------- Comment receiver rating updated!!!");
				
				logger.info("---------------- Email sending service started...");
				emailDao.sendCommentEditedEmail(userRepository.getByIdAndStatusLike(comm.getCommentReceiver(), 1));
				logger.info("---------------- Email sent");

				return new ResponseEntity<>(comm, HttpStatus.OK);

			} catch (Exception e) {
				logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
				return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

	// @Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}") // Get comments received by user
	public ResponseEntity<?> deactivateComment(@PathVariable Integer id, Principal principal) {
		try {
			logger.info("################ /jobster/comment/deactivateComment started.");
			logger.info("Logged username: " + principal.getName());
			CommentEntity comment = commentRepository.findByIdAndStatusLike(id, 1);
			if (comment == null) {
				logger.error("++++++++++++++++ Active comment not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			UserEntity user = userAccountRepository.findByUsernameAndStatusLike(principal.getName(), 1).getUser();
			if (user == null) {
				logger.error("++++++++++++++++ User attempting to delete was not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

			if (userAccountRepository.findByUsernameAndStatusLike(principal.getName(), 1)
					.getAccessRole() != EUserRole.ROLE_ADMIN || comment.getCommentReceiver() != user.getId()
					|| comment.getCreatedById() != user.getId()) {
				logger.error("++++++++++++++++ User not allowed to delete the desired comment");
				return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
			}
			logger.info("---------------- User and comment found OK");
			comment.setStatus(0);
			comment.setUpdatedById(user.getId());
			logger.info("---------------- Comment status and updatedy updated!!!");
			commentRepository.save(comment);
			logger.info("---------------- Comment deactivated");
			
			logger.info("---------------- Email sending service started...");
			emailDao.sendCommentDeletedEmail(userRepository.getByIdAndStatusLike(comment.getCommentReceiver(), 1));
			logger.info("---------------- Email sent");
			
			return new ResponseEntity<CommentEntity>(comment, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// @Secured("ROLE_ADMIN")
		// @JsonView(Views.Admin.class)
		@RequestMapping(method = RequestMethod.PUT, value = "/undelete/{id}") //
		public ResponseEntity<?> reactivateComment(@PathVariable Integer id, Principal principal) {
			try {
				logger.info("################ /jobster/comment/undelete/{id} reactivateComment started.");
				logger.info("Logged username: " + principal.getName());
				CommentEntity comment = commentRepository.findByIdAndStatusLike(id, 0);
				if (comment == null) {
					logger.error("++++++++++++++++ Inactive comment not found");
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
				UserEntity user = userAccountRepository.findByUsernameAndStatusLike(principal.getName(), 1).getUser();
				if (user == null) {
					logger.error("++++++++++++++++ User attempting to undelete was not found");
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}

				if (userAccountRepository.findByUsernameAndStatusLike(principal.getName(), 1)
						.getAccessRole() != EUserRole.ROLE_ADMIN ) {
					logger.error("++++++++++++++++ User not allowed to undelete");
					return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
				}
				logger.info("---------------- User and comment found OK");
				comment.setStatus(1);
				comment.setUpdatedById(user.getId());
				logger.info("---------------- Comment status and updatedBy updated!!!");
				commentRepository.save(comment);
				logger.info("---------------- Comment activated!!!");
				
				logger.info("---------------- Email sending service started...");
				emailDao.sendCommentUndeleteEmail(userRepository.getByIdAndStatusLike(comment.getCommentReceiver(), 1));
				logger.info("---------------- Email sent");
				
				
				return new ResponseEntity<CommentEntity>(comment, HttpStatus.OK);
			} catch (Exception e) {
				logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
				return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}


	
	
	// @Secured("ROLE_ADMIN")
		// @JsonView(Views.Admin.class)
		@RequestMapping(method = RequestMethod.PUT, value = "/archive/{id}") // Get comments received by user
		public ResponseEntity<?> archiveComment(@PathVariable Integer id, Principal principal) {
			try {
				logger.info("################ /jobster/comment/archiveComment started.");
				logger.info("Logged username: " + principal.getName());
				CommentEntity comment = commentRepository.findById(id).orElse(null);
				if (comment == null || comment.getStatus() == -1) {
					logger.error("++++++++++++++++ Comment not found");
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
				UserEntity user = userAccountRepository.findByUsernameAndStatusLike(principal.getName(), 1).getUser();
				if (user == null) {
					logger.error("++++++++++++++++ User attempting to archive was not found");
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}

				if (userAccountRepository.findByUsernameAndStatusLike(principal.getName(), 1)
						.getAccessRole() != EUserRole.ROLE_ADMIN) {
					logger.error("++++++++++++++++ User not allowed to archive the desired comment");
					return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
				}
				logger.info("---------------- User and comment found OK");
				comment.setStatus(-1);
				comment.setUpdatedById(user.getId());
				logger.info("---------------- Comment status and updatedBy updated!!!");
				commentRepository.save(comment);
				logger.info("---------------- Comment archived!!!");
				return new ResponseEntity<CommentEntity>(comment, HttpStatus.OK);
			} catch (Exception e) {
				logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
				return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
//pagination:
		// @Secured("ROLE_ADMIN")
		//@JsonView(Views.Admin.class)
		@RequestMapping(method = RequestMethod.GET, value = "/allPaginated") // get all comments
		public ResponseEntity<?> getAllPAginated(
				@RequestParam Optional<Integer> page,
				@RequestParam Optional<Integer> pageSize,
				@RequestParam Optional<Sort.Direction> direction,
				@RequestParam Optional<String> sortBy, 
				Principal principal) {
			logger.info("################ /jobster/comment/all/getAllPaginated started.");
			logger.info("Logged username: " + principal.getName());
			try {
				Page<CommentEntity> commentsPage= commentDao.findAll(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("commentTitle"));// sortBy);
				Iterable<CommentEntity> comments = commentsPage.getContent();
				logger.info("---------------- Found comments - OK.");
				logger.info("---------------- Comments to DTOs service starting ");
				ArrayList<ShowCommentDTO> commentDTOs = commentDao.fromCommentsToDTOs(comments);
				logger.info("---------------- Finished OK.");
				return new ResponseEntity<ArrayList<ShowCommentDTO>>(commentDTOs, HttpStatus.OK);
			} catch (Exception e) {
				logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
				return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		//@Secured("ROLE_ADMIN")
		//@JsonView(Views.Admin.class)
		@RequestMapping(method = RequestMethod.GET, value="/activePaginated") // get all active comments
		public ResponseEntity<?> getAllActivePaginated(
				@RequestParam Optional<Integer> page,
				@RequestParam Optional<Integer> pageSize,
				@RequestParam Optional<Sort.Direction> direction,
				@RequestParam Optional<String> sortBy, 
				Principal principal) {
			logger.info("################ /jobster/comment/getAllActivePaginated started.");
			logger.info("Logged username: " + principal.getName());
			try {
				Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("commentTitle"));
				Page<CommentEntity> commentsPage= commentRepository.findByStatusLike(1,pageable);
				Iterable<CommentEntity> comments = commentsPage.getContent();
				logger.info("---------------- Found comments - OK.");
				logger.info("---------------- Comments to DTOs service starting ");
				ArrayList<ShowCommentDTO> commentDTOs = commentDao.fromCommentsToDTOs(comments);
				logger.info("---------------- Finished OK.");
				return new ResponseEntity<ArrayList<ShowCommentDTO>>(commentDTOs, HttpStatus.OK);
			} catch (Exception e) {
				logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
				return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		// @Secured("ROLE_ADMIN")
		//@JsonView(Views.Admin.class)
		@RequestMapping(method = RequestMethod.GET, value = "/inactivePaginated") // get all inactive comments
		public ResponseEntity<?> getAllInactivePaginated(
				@RequestParam Optional<Integer> page,
				@RequestParam Optional<Integer> pageSize,
				@RequestParam Optional<Sort.Direction> direction,
				@RequestParam Optional<String> sortBy, 
				Principal principal) {
			logger.info("################ /jobster/comment/inactive/getAllInactivePaginated started.");
			logger.info("Logged username: " + principal.getName());
			try {
				Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("commentTitle"));
				Page<CommentEntity> commentsPage= commentRepository.findByStatusLike(0,pageable);
				Iterable<CommentEntity> comments = commentsPage.getContent();
				logger.info("---------------- Found comments - OK.");
				logger.info("---------------- Comments to DTOs service starting ");
				ArrayList<ShowCommentDTO> commentDTOs = commentDao.fromCommentsToDTOs(comments);
				logger.info("---------------- Finished OK.");
				return new ResponseEntity<ArrayList<ShowCommentDTO>>(commentDTOs, HttpStatus.OK);

			} catch (Exception e) {
				logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
				return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		// @Secured("ROLE_ADMIN")
		//@JsonView(Views.Admin.class)
		@RequestMapping(method = RequestMethod.GET, value = "/archivedPaginated") // get all archived comments
		public ResponseEntity<?> getAllArchivedPaginated(
				@RequestParam Optional<Integer> page,
				@RequestParam Optional<Integer> pageSize,
				@RequestParam Optional<Sort.Direction> direction,
				@RequestParam Optional<String> sortBy, 
				Principal principal) {
			logger.info("################ /jobster/comment/archived/getAllArchivedPaginated started.");
			logger.info("Logged username: " + principal.getName());
			try {
				Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("commentTitle"));
				Page<CommentEntity> commentsPage= commentRepository.findByStatusLike(-1,pageable);
				Iterable<CommentEntity> comments = commentsPage.getContent();
				logger.info("---------------- Found comments - OK.");
				logger.info("---------------- Comments to DTOs service starting ");
				ArrayList<ShowCommentDTO> commentDTOs = commentDao.fromCommentsToDTOs(comments);
				logger.info("---------------- Finished OK.");
				return new ResponseEntity<ArrayList<ShowCommentDTO>>(commentDTOs, HttpStatus.OK);

			} catch (Exception e) {
				logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
				return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}


		// @Secured("ROLE_ADMIN")
		// @JsonView(Views.Admin.class)
		@RequestMapping(method = RequestMethod.GET, value = "/user/{id}/paginated") // Get comments received by user
		public ResponseEntity<?> getUsersCommentPaginated(
				@RequestParam Optional<Integer> page,
				@RequestParam Optional<Integer> pageSize,
				@RequestParam Optional<Sort.Direction> direction,
				@RequestParam Optional<String> sortBy, 
				@PathVariable Integer id,
				Principal principal) {
			logger.info("################ /jobster/comment/getUsersCommentPaginated started.");
			logger.info("Logged username: " + principal.getName());
			try {
				Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("commentTitle"));
				Page<CommentEntity> commentsPage= commentRepository.findByCommentReceiverAndStatusLike(id, 1, pageable);
				Iterable<CommentEntity> comments = commentsPage.getContent();
				if(Iterables.isEmpty(comments)) {
					logger.info("++++++++++++++++ User has no comments");
					return new ResponseEntity<>("User has no comments.",HttpStatus.NOT_FOUND);
				}
				
				logger.info("---------------- Found comments - OK.");
				logger.info("---------------- Comments to DTOs service starting ");
				ArrayList<ShowCommentDTO> commentDTOs = commentDao.fromCommentsToDTOs(comments);
				logger.info("---------------- Finished OK.");
				return new ResponseEntity<ArrayList<ShowCommentDTO>>(commentDTOs, HttpStatus.OK);
			} catch (Exception e) {
				logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
				return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		// @Secured("ROLE_ADMIN")
			// @JsonView(Views.Admin.class)
			@RequestMapping(method = RequestMethod.GET, value = "/apply/{id}/paginated") // Get comments by apply
			public ResponseEntity<?> getApplicationCommentPaginated(
					@RequestParam Optional<Integer> page,
					@RequestParam Optional<Integer> pageSize,
					@RequestParam Optional<Sort.Direction> direction,
					@RequestParam Optional<String> sortBy, 
					@PathVariable Integer id, 
					Principal principal) {
				logger.info("################ /jobster/comment/getApplicationCommentPaginated started.");
				logger.info("Logged username: " + principal.getName());
				try {
					ApplyContactEntity application = applyContactRepository.findByIdAndStatusLike(id, 1);
					Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("commentTitle"));
					Page<CommentEntity> commentsPage = commentRepository.findByApplicationAndStatusLike(application, 1, pageable);
					Iterable<CommentEntity> comments = commentsPage.getContent();
					if(Iterables.isEmpty(comments)) {
						logger.info("++++++++++++++++ Application has no comments");
						return new ResponseEntity<>("Application has no comments.",HttpStatus.NOT_FOUND);
					}
					logger.info("---------------- Found comments - OK.");
					logger.info("---------------- Comments to DTOs service starting ");
					ArrayList<ShowCommentDTO> commentDTOs = commentDao.fromCommentsToDTOs(comments);
					logger.info("---------------- Finished OK.");
					return new ResponseEntity<ArrayList<ShowCommentDTO>>(commentDTOs, HttpStatus.OK);
				} catch (Exception e) {
					logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
					return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
							HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}

}
