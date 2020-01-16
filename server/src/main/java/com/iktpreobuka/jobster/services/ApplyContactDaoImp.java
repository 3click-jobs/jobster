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
import com.iktpreobuka.jobster.util.StringDateFormatValidator;



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
	public Iterable<ApplyContactEntity> findByQueryForLoggedInUser(Integer loggedInUserId, Integer status, 
			Boolean rejected, Boolean connected, Boolean expired,Boolean commentable) {
		logger.info("++++++++++++++++ Service for finding applicaitons for a logged in user by params started");
		String sql = "select a from ApplyContactEntity a where a.createdById = " + loggedInUserId;
		logger.info("++++++++++++++++ Basic query created");
		if(status !=null) {
			sql = sql + " and a.status = " + status;
			logger.info("++++++++++++++++ Added condition for rejected applications");
		}
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
	public Iterable<ApplyContactEntity> findByQuery(Integer status, Boolean rejected, Boolean connected, Boolean expired,Boolean commentable) {
		logger.info("++++++++++++++++ Service for finding applicaitons by params started");
		String sql = "select a from ApplyContactEntity a";
		Boolean firstParam = true;
		logger.info("++++++++++++++++ Basic query created");
		if(status != null) {
			if(!firstParam) {
				sql = sql + " and";
			} else {
				sql = sql + " where";
				firstParam = false;
			}
			sql = sql + " a.status = " + status;
			logger.info("++++++++++++++++ Added condition for rejected applications");
		}
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
			sql = sql + " a.expired = " + expired;
			logger.info("++++++++++++++++ Added condition for expired applications");
		}
		
		if(commentable !=null) {
			if(!firstParam) {
				sql = sql + " and";
			} else {
				sql = sql + " where";
				firstParam = false;
			}
			sql = sql + " a.commentable = " + commentable;
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
	public Iterable<ApplyContactEntity> findByQuery(Integer status, Boolean rejected, Boolean connected,
			Boolean expired, Boolean commentable, String connectionDateBottom, String connectionDateTop,
			String contactDateBottom, String contactDateTop) {
		logger.info("++++++++++++++++ Service for finding applicaitons by params started - date params included");
		String sql = "select a from ApplyContactEntity a";
		Boolean firstParam = true;
		logger.info("++++++++++++++++ Basic query created");
		if(status != null) {
			if(!firstParam) {
				sql = sql + " and";
			} else {
				sql = sql + " where";
				firstParam = false;
			}
			sql = sql + " a.status = " + status;
			logger.info("++++++++++++++++ Added condition for rejected applications");
		}
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
			sql = sql + " a.expired = " + expired;
			logger.info("++++++++++++++++ Added condition for expired applications");
		}
		
		if(commentable !=null) {
			if(!firstParam) {
				sql = sql + " and";
			} else {
				sql = sql + " where";
				firstParam = false;
			}
			sql = sql + " a.commentable = " + commentable;
			logger.info("++++++++++++++++ Added condition for commentable applications");
		}
		
		if(connectionDateBottom !=null) {
			if(!firstParam) {
				sql = sql + " and";
			} else {
				sql = sql + " where";
				firstParam = false;
			}
			sql = sql + " a.connectionDate >= " + connectionDateBottom;
			logger.info("++++++++++++++++ Added condition for all applications where connection is younger than" + connectionDateBottom);
		}
		
		if(connectionDateTop !=null) {
			if(!firstParam) {
				sql = sql + " and";
			} else {
				sql = sql + " where";
				firstParam = false;
			}
			sql = sql + " a.connectionDate <= " + connectionDateTop;
			logger.info("++++++++++++++++ Added condition for all applications where conncetion is older than" + connectionDateTop);
		}
		
		if(contactDateTop !=null) {
			if(!firstParam) {
				sql = sql + " and";
			} else {
				sql = sql + " where";
				firstParam = false;
			}
			sql = sql + " a.contactDate <= " + contactDateTop;
			logger.info("++++++++++++++++ Added condition for all applications where contact is older than " + contactDateTop);
		}
		
		if(contactDateBottom !=null) {
			if(!firstParam) {
				sql = sql + " and";
			} else {
				sql = sql + " where";
				firstParam = false;
			}
			sql = sql + " a.contactDate >= " + contactDateBottom;
			logger.info("++++++++++++++++ Added condition for all applications where contact is younger than " + contactDateBottom);
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

	
	//checks if the provided string is correct data format applicable to use in query
	@Override
	public boolean stringDateFormatNotCorrect(String connectionDateBottom, String connectionDateTop,
			String contactDateBottom, String contactDateTop) {
		if(connectionDateBottom != null) {
			if(!StringDateFormatValidator.checkIfStringIsValidDateFormat(connectionDateBottom)) {
				return true;
			}
		}
		if(connectionDateTop != null) {
			if(!StringDateFormatValidator.checkIfStringIsValidDateFormat(connectionDateBottom)) {
				return true;
			}
		}
		if(contactDateBottom != null) {
			if(!StringDateFormatValidator.checkIfStringIsValidDateFormat(connectionDateBottom)) {
				return true;
			}
		}
		if(contactDateTop != null) {
			if(!StringDateFormatValidator.checkIfStringIsValidDateFormat(connectionDateBottom)) {
				return true;
			}
		}
		return false;
	}



}
