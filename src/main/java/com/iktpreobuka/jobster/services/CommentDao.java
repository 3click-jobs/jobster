package com.iktpreobuka.jobster.services;

import com.iktpreobuka.jobster.entities.CommentEntity;
import com.iktpreobuka.jobster.entities.dto.CommentOutputDTO;

public interface CommentDao {

	CommentOutputDTO createDTO(CommentEntity comment);

}
