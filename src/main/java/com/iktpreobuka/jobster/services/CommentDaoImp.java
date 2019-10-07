package com.iktpreobuka.jobster.services;

import org.springframework.stereotype.Service;

import com.iktpreobuka.jobster.entities.CommentEntity;
import com.iktpreobuka.jobster.entities.dto.CommentOutputDTO;

@Service
public class CommentDaoImp implements CommentDao{

	@Override
	public CommentOutputDTO createDTO(CommentEntity comment) {
		return null;
	}
}
