package com.iktpreobuka.jobster.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.iktpreobuka.jobster.entities.ApplyContactEntity;

@Service
public class ApplyContactDaoImp implements ApplyContactDao{
	
	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
	
	//Vraca ID druge strane u apply
	public Integer otherPartyFromApply(Integer idIn,ApplyContactEntity apply) {
		logger.info("get other party's ID from apply started");
		if(apply.getOffer().getEmployer().getId()==idIn) {
			return apply.getSeek().getEmployee().getId();}
		if(apply.getSeek().getEmployee().getId()==idIn) {
			return apply.getOffer().getEmployer().getId();}
		else {return null;}
	}
}
