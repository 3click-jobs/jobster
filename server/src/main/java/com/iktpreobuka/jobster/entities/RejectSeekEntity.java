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
@Table(name = "rejected_seeks")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class RejectSeekEntity extends RejectEntity {

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "rejected_seek")
	@NotNull (message = "Job Seek must be provided.")
	private JobSeekEntity rejectedSeek;

	
	public RejectSeekEntity() {
		super();
	}

	public RejectSeekEntity(@NotNull(message = "Job Seek must be provided.") JobSeekEntity rejectedSeek) {
		super();
		this.rejectedSeek = rejectedSeek;
	}

	public RejectSeekEntity(@NotNull(message = "User must be provided.") UserEntity user,
			@NotNull(message = "Job Seek must be provided.") JobSeekEntity rejectedSeek,
			@NotNull(message = "Rejection date must be provided.") Date rejectionDate, 
			Integer createdById) {
		super();
		super.setUser(user);
		this.rejectedSeek = rejectedSeek;
		super.setRejectionDate(rejectionDate);
		super.setStatusActive();
		super.setCreatedById(createdById);
	}

	
	public JobSeekEntity getRejectedSeek() {
		return rejectedSeek;
	}

	public void setRejectedSeek(JobSeekEntity rejectedSeek) {
		this.rejectedSeek = rejectedSeek;
	}
	
}
