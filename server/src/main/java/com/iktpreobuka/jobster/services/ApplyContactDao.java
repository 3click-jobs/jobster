package com.iktpreobuka.jobster.services;

import com.iktpreobuka.jobster.entities.ApplyContactEntity;

public interface ApplyContactDao {
	public Integer otherPartyFromApply(Integer idIn,ApplyContactEntity apply);
}
