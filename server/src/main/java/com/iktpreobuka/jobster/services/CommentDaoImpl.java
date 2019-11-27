package com.iktpreobuka.jobster.services;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iktpreobuka.jobster.entities.CommentEntity;
import com.iktpreobuka.jobster.entities.UserEntity;
import com.iktpreobuka.jobster.entities.dto.ShowCommentDTO;
import com.iktpreobuka.jobster.repositories.CompanyRepository;
import com.iktpreobuka.jobster.repositories.PersonRepository;
import com.iktpreobuka.jobster.repositories.UserRepository;

@Service
public class CommentDaoImpl implements CommentDao {
	
	@Autowired
	PersonRepository personRepository;
	
	@Autowired
	CompanyRepository companyRepository;
	
	@Autowired
	UserRepository userRepository;
	
	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	@Override
	public ShowCommentDTO fromCommentToDTO(CommentEntity comment) {
		logger.info("---------------- Transfering from comment to outputDTO.");
		ShowCommentDTO outputComment = new ShowCommentDTO();
		outputComment.setBeginningDate(comment.getApplication().getOffer().getBeginningDate());
		outputComment.setCityName(comment.getApplication().getOffer().getCity().getCityName());
		outputComment.setCommentContent(comment.getCommentContent());
		outputComment.setCommentDate(comment.getCommentDate());
		outputComment.setCommentTitle(comment.getCommentTitle());
		outputComment.setEndDate(comment.getApplication().getOffer().getEndDate());
		outputComment.setPrice(comment.getApplication().getOffer().getPrice());
		outputComment.setRating(comment.getRating());
		outputComment.setType(comment.getApplication().getOffer().getType().getJobTypeName());
		//comment.getCreatedById()
		if(personRepository.getByIdAndStatusLike(comment.getCreatedById(), 1) != null) {
			logger.info("---------------- User who commented is a person.");
			outputComment.setCommentPosterName(personRepository.getByIdAndStatusLike(comment.getCreatedById(), 1).getFirstName() +
					" " + personRepository.getByIdAndStatusLike(comment.getCreatedById(), 1).getLastName());
		}
		if(companyRepository.getByIdAndStatusLike(comment.getCreatedById(), 1) != null) {
			logger.info("---------------- User who commented is a company.");
			outputComment.setCommentPosterName(companyRepository.getByIdAndStatusLike(comment.getCreatedById(), 1).getCompanyName());
		}
		
		logger.info("---------------- OutputDTO returned");
		return outputComment;
	}
	
	
	@Override
	public ArrayList<ShowCommentDTO> fromCommentsToDTOs(Iterable<CommentEntity> comments) {
		logger.info("---------------- Transfering from comments to commentDTOs started.");
		ArrayList<ShowCommentDTO> dtos = new ArrayList<ShowCommentDTO>();
		ShowCommentDTO dto = new ShowCommentDTO();
		logger.info("---------------- Begin for-each.");
		for (CommentEntity comm : comments) {
			dto = fromCommentToDTO(comm);
			dtos.add(dto);
		}
		logger.info("---------------- Finished OK.");
		logger.info("---------------- OutputDTO returned.");
		return dtos;
		
		
	}
	@Override
	public void updateReceiverRating(Integer commReceiverId, Integer rating) {
		UserEntity user = userRepository.getByIdAndStatusLike(commReceiverId, 1);
		logger.info("---------------- Updating rating .... Found user receiving the comment.");
		Integer numberOfRatings = user.getNumberOfRatings();
		Double totalRating = user.getRating()*numberOfRatings;
		totalRating += rating;
		numberOfRatings++;
		user.setNumberOfRatings(numberOfRatings);
		user.setRating(totalRating/numberOfRatings);
		logger.info("---------------- Updating rating .... updated ratings saved to user. Saving user into the repo...");
		userRepository.save(user);		
	}



}
