package com.iktpreobuka.jobster.repositories;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.jobster.entities.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Integer> {

	public UserEntity getById(Integer id);

	public UserEntity getByIdAndStatusLike(int parseInt, int i);

}
