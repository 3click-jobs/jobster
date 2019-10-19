package com.iktpreobuka.jobster.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.jobster.entities.ApplyContactEntity;


@Repository
public interface ApplyContactRepository extends CrudRepository<ApplyContactEntity, Integer>{

	
	ApplyContactEntity findByIdAndStatusLike(Integer id, Integer i);
}
