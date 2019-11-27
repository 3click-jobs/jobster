package com.iktpreobuka.jobster.services;

import java.util.ArrayList;

import com.iktpreobuka.jobster.entities.CommentEntity;
import com.iktpreobuka.jobster.entities.dto.ShowCommentDTO;

public interface CommentDao {

	ArrayList<ShowCommentDTO> fromCommentsToDTOs(Iterable<CommentEntity> comments);

	ShowCommentDTO fromCommentToDTO(CommentEntity comment);

	void updateReceiverRating(Integer commReceiverId, Integer rating);

}
