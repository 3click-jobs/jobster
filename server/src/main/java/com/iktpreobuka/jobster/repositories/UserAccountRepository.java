package com.iktpreobuka.jobster.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.jobster.entities.CompanyEntity;
import com.iktpreobuka.jobster.entities.PersonEntity;
import com.iktpreobuka.jobster.entities.UserAccountEntity;
import com.iktpreobuka.jobster.entities.UserEntity;
import com.iktpreobuka.jobster.enumerations.EUserRole;


@Repository
public interface UserAccountRepository extends CrudRepository<UserAccountEntity, Integer> {

	public UserAccountEntity getById(Integer id);
	public UserAccountEntity getByUsername(String username);
	public UserAccountEntity getByUser(UserEntity user);
	@Query("select ua.user from UserAccountEntity ua where ua.username=:username and ua.status=:status")
	public UserEntity findUserByUsernameAndStatusLike(@Param("username") String username, @Param("status") Integer status);
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

	public UserEntity findUserByUsername(String username);
	@Query("select ue.id "
			+ "from UserAccountEntity ua "
			+ "join ua.user ue "
			+ "where ua.username=:username")
	public Integer getUserIdByUsername(@Param("username") String username); // pitati drakulica da li ce ovo da radi
	@SuppressWarnings("unchecked")
	public UserAccountEntity save(UserAccountEntity userAccountEntity);
	public UserAccountEntity findByUserAndStatusLike(CompanyEntity companyEntity, int i);
	public UserAccountEntity findByUserAndStatusLike(PersonEntity personEntity, int i);
	public UserAccountEntity findByUsernameAndStatusLikeAndAccessRoleLike(String name, int i, EUserRole roleAdmin);
	public UserAccountEntity findByUsername(String name);
  
//pagination:
	public Page<UserAccountEntity> findByStatusLike(int i, Pageable pageable);



}
