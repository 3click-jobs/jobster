
package com.iktpreobuka.jobster.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import com.iktpreobuka.jobster.entities.JobSeekEntity;

@Repository
public interface JobSeekRepository extends CrudRepository<JobSeekEntity, Integer> {

	public JobSeekEntity getById(@PathVariable Integer id);
	
	public List<JobSeekEntity> getAllByEmployeeId(@PathVariable Integer id);
	
	public List<JobSeekEntity> getAllByTypeId(@PathVariable Integer id);
	
	public List<JobSeekEntity> getAllByCityId(@PathVariable Integer id);

	public JobSeekEntity findByIdAndStatusLike(Integer id, int i);

}