package com.iktpreobuka.jobster.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "rejected_offers")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class RejectOfferEntity extends RejectEntity {

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "rejected_offer")
	@NotNull (message = "Job Offer must be provided.")
	private JobOfferEntity rejectedOffer;

	
	public RejectOfferEntity() {
		super();
	}

	public RejectOfferEntity(@NotNull(message = "Job Offer must be provided.") JobOfferEntity rejectedOffer) {
		super();
		this.rejectedOffer = rejectedOffer;
	}

	public RejectOfferEntity(@NotNull(message = "User must be provided.") UserEntity user,
			@NotNull(message = "Job Offer must be provided.") JobOfferEntity rejectedOffer,
			@NotNull(message = "Rejection date must be provided.") Date rejectionDate, 
			Integer createdById) {
		super();
		super.setUser(user);
		this.rejectedOffer = rejectedOffer;
		super.setRejectionDate(rejectionDate);
		super.setStatusActive();
		super.setCreatedById(createdById);
	}

	
	public JobOfferEntity getRejectedOffer() {
		return rejectedOffer;
	}

	public void setRejectedOffer(JobOfferEntity rejectedOffer) {
		this.rejectedOffer = rejectedOffer;
	}
	
}
