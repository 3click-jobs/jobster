package com.iktpreobuka.jobster.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.jobster.entities.CommentEntity;

@Repository
public interface CommentRepository extends CrudRepository<CommentEntity,Integer>{

	Iterable<CommentEntity> findByStatusLike(Integer i);

	CommentEntity findByIdAndStatusLike(Integer id, Integer i);

	
	
}
