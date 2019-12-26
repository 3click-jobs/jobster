package com.iktpreobuka.jobster.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.jobster.entities.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Integer> {

	public UserEntity getById(Integer id);

	public UserEntity getByIdAndStatusLike(int parseInt, int i);

}
