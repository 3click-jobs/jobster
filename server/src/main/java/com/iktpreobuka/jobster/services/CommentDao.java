package com.iktpreobuka.jobster.services;

import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;

import com.iktpreobuka.jobster.entities.CommentEntity;
import com.iktpreobuka.jobster.entities.dto.ShowCommentDTO;

public interface CommentDao {

	ArrayList<ShowCommentDTO> fromCommentsToDTOs(Iterable<CommentEntity> comments);

	ShowCommentDTO fromCommentToDTO(CommentEntity comment);

	void updateReceiverRating(Integer commReceiverId, Integer rating);

	Page<CommentEntity>  findAll(int page, int pageSize, Direction direction,String sortBy);

}
