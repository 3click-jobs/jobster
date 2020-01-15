package com.iktpreobuka.jobster.services;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Iterables;
import com.iktpreobuka.jobster.entities.ApplyContactEntity;
import com.iktpreobuka.jobster.entities.JobOfferEntity;
import com.iktpreobuka.jobster.entities.JobSeekEntity;
import com.iktpreobuka.jobster.repositories.ApplyContactRepository;



@Service
public class ApplyContactDaoImp implements ApplyContactDao{
	
	@PersistenceContext
	EntityManager em;
	
	@Autowired
	ApplyContactRepository applyContactRepository;
	
	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
	
	//Vraca ID druge strane u apply
	@Override
	public Integer otherPartyFromApply(Integer idIn,ApplyContactEntity apply) {
		logger.info("get other party's ID from apply started");
		if(apply.getOffer().getEmployer().getId()==idIn) {
			return apply.getSeek().getEmployee().getId();}
		else if(apply.getSeek().getEmployee().getId()==idIn) {
			return apply.getOffer().getEmployer().getId();}
		else {return null;}
	}


	@SuppressWarnings("unchecked")
	@Override
	public Iterable<ApplyContactEntity> findByQueryForLoggedInUser(Integer loggedInUserId, 
			Boolean rejected, Boolean connected, Boolean expired,Boolean commentable) {
		logger.info("++++++++++++++++ Service for finding applicaitons for a logged in user by params started");
		String sql = "select a from ApplyContactEntity a where a.createdById = " + loggedInUserId;
		logger.info("++++++++++++++++ Basic query created");
		if(rejected !=null) {
			sql = sql + " and a.rejected = " + rejected;
			logger.info("++++++++++++++++ Added condition for rejected applications");
		}
		if(connected !=null) {
			sql = sql + " and a.areConnected = " + connected;	
			if(connected) {
				logger.info("++++++++++++++++ Added condition for connected applications");
			}
			else {
				logger.info("++++++++++++++++ Added condition for pending applications");
			}
		}
		if(expired !=null) {
			sql = sql + " and a.expired = " + expired;
			logger.info("++++++++++++++++ Added condition for expired applications");
		}
		
		if(commentable !=null) {
			sql = sql + " and a.commentable = " + commentable;
			logger.info("++++++++++++++++ Added condition for commentable applications");
		}
		
		Query query = em.createQuery(sql);
		logger.info("++++++++++++++++ Query created");
		Iterable<ApplyContactEntity> result = query.getResultList();
		logger.info("++++++++++++++++ Result of the query returned ok");
		return result;
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Iterable<ApplyContactEntity> findByQuery(Boolean rejected, Boolean connected, Boolean expired,Boolean commentable) {
		logger.info("++++++++++++++++ Service for finding applicaitons by params started");
		String sql = "select a from ApplyContactEntity a";
		Boolean firstParam = true;
		logger.info("++++++++++++++++ Basic query created");
		
		if(rejected !=null) {
			if(!firstParam) {
				sql = sql + " and";
			} else {
				sql = sql + " where";
				firstParam = false;
			}
			sql = sql + " a.rejected = " + rejected;
			logger.info("++++++++++++++++ Added condition for rejected applications");
		}
		if(connected !=null) {
			if(!firstParam) {
				sql = sql + " and";
			} else {
				sql = sql + " where";
				firstParam = false;
			}
			sql = sql + " a.areConnected = " + connected;	
			if(connected) {
				logger.info("++++++++++++++++ Added condition for connected applications");
			}
			else {
				logger.info("++++++++++++++++ Added condition for pending applications");
			}
		}
		if(expired !=null) {
			if(!firstParam) {
				sql = sql + " and";
			} else {
				sql = sql + " where";
				firstParam = false;
			}
			sql = sql + " and a.expired = " + expired;
			logger.info("++++++++++++++++ Added condition for expired applications");
		}
		
		if(commentable !=null) {
			if(!firstParam) {
				sql = sql + " and";
			} else {
				sql = sql + " where";
				firstParam = false;
			}
			sql = sql + " and a.commentable = " + commentable;
			logger.info("++++++++++++++++ Added condition for commentable applications");
		}
		
		Query query = em.createQuery(sql);
		logger.info("++++++++++++++++ Query created");
		Iterable<ApplyContactEntity> result = query.getResultList();
		logger.info("++++++++++++++++ Result of the query returned ok");
		return result;
		
	}
	
	
	@Override
	public void markApplyAsExpiredBySeek(JobSeekEntity seek) {
		logger.info("++++++++++++++++ mark all applies as expired by seek started");
		Iterable<ApplyContactEntity> applications = applyContactRepository.findBySeek(seek);
		if(Iterables.isEmpty(applications)){
			logger.info("++++++++++++++++ Applications not found");
		}
		else{
			logger.info("++++++++++++++++ Applications found");
			for (ApplyContactEntity app : applications) {
				if (!app.getAreConnected()) {
					logger.info("++++++++++++++++ Pending applicaitons found");
					app.setExpired(true);
					applyContactRepository.save(app);
					logger.info("++++++++++++++++ App marked as expired and saved in DB");
					
				}
			}
		}
	}
	
	@Override
	public void markApplyAsExpiredByOffer(JobOfferEntity offer) {
		logger.info("++++++++++++++++ mark all applies as expired by offer started");
		Iterable<ApplyContactEntity> applications = applyContactRepository.findByOffer(offer);
		if(Iterables.isEmpty(applications)){
			logger.info("++++++++++++++++ Applications not found");
		}
		else{
			logger.info("++++++++++++++++ Applications found");
			for (ApplyContactEntity app : applications) {
				if (!app.getAreConnected()) {
					logger.info("++++++++++++++++ Pending applicaitons found");
					app.setExpired(true);
					applyContactRepository.save(app);
					logger.info("++++++++++++++++ App marked as expired and saved in DB");
					
				}
			}
		}
	}
}
