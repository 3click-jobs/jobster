package com.iktpreobuka.jobster.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.jobster.entities.ApplyContactEntity;
import com.iktpreobuka.jobster.entities.CommentEntity;

@Repository
public interface CommentRepository extends PagingAndSortingRepository<CommentEntity,Integer>{

	Iterable<CommentEntity> findByStatusLike(Integer i);

	CommentEntity findByIdAndStatusLike(Integer id, Integer i);

	Iterable<CommentEntity> findByCommentReceiverAndStatusLike(Integer id, int i);

	Iterable<CommentEntity> findByApplicationAndStatusLike(ApplyContactEntity application, int i);

//pagination:
	Page<CommentEntity> findByStatusLike(int i, Pageable pageable);
	Page<CommentEntity> findByCommentReceiverAndStatusLike(Integer id, int i, Pageable pageable);
	Page<CommentEntity> findByApplicationAndStatusLike(ApplyContactEntity application, int i, Pageable pageable);

	
	
}
