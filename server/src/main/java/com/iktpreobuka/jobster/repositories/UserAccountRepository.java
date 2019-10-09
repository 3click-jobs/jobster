package com.iktpreobuka.jobster.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.jobster.entities.CompanyEntity;
import com.iktpreobuka.jobster.entities.PersonEntity;
import com.iktpreobuka.jobster.entities.UserAccountEntity;
import com.iktpreobuka.jobster.entities.UserEntity;
import com.iktpreobuka.jobster.enumerations.EUserRole;


public interface UserAccountRepository extends CrudRepository<UserAccountEntity, Integer> {

	public UserAccountEntity getById(Integer id);
	public UserAccountEntity getByUsername(String username);
	public UserAccountEntity getByUser(UserEntity user);
	@Query("select ua.user from UserAccountEntity ua where ua.username=:username and ua.status=:status")
	public UserEntity findUserByUsernameAndStatusLike(String username, Integer status);
	public UserEntity findUserByIdAndStatusLike(Integer id, Integer status);
	public UserAccountEntity findByUserAndAccessRoleLikeAndStatusLike(UserEntity user, EUserRole eUserRole, Integer status);
	public Iterable<UserAccountEntity> findByStatusLike(Integer status);
	public UserAccountEntity findByIdAndStatusLike(Integer id, Integer status);
	public UserAccountEntity findByUserAndAccessRoleAndStatusLike(UserEntity user, EUserRole role, Integer status);
	public UserAccountEntity findByUsernameAndStatusLike(String name, Integer Status);
	public UserAccountEntity findByUserAndAccessRoleLike(UserEntity user, EUserRole role);
	public UserAccountEntity findByUserAndAccessRoleLike(CompanyEntity user, EUserRole role);
	public UserAccountEntity findByUserAndAccessRoleLike(PersonEntity user, EUserRole role);
	public void deleteByUser(Integer id);

}
