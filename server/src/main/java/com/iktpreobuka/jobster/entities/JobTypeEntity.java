package com.iktpreobuka.jobster.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "job_types")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class JobTypeEntity {

	private static final Integer STATUS_INACTIVE = 0;
	private static final Integer STATUS_ACTIVE = 1;
	private static final Integer STATUS_ARCHIVED = -1;

	@JsonIgnore
	@OneToMany(mappedBy = "type", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH})
	private List<JobOfferEntity> offers = new ArrayList<>();
	
	@JsonIgnore
	@OneToMany(mappedBy = "type", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH})
	private List<JobSeekEntity> seeks = new ArrayList<>();

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	//@JsonView(Views.Parent.class)
	@Column(name="job_type_id")
	protected Integer id;
	//@JsonView(Views.Student.class)
	@Column(name="job_type_name")
	@Pattern(regexp = "^[A-Za-z]{2,}$", message="Job type name is not valid.")
	@NotNull (message = "Job type name must be provided.")
	protected String jobTypeName;
	//@JsonView(Views.Admin.class)
	@Max(1)
    @Min(-1)
    @Column(name = "status", nullable = false)
	private Integer status;
	//@JsonView(Views.Admin.class)
    @Column(name = "created_by", nullable = false, updatable = false)
	private Integer createdById;
    //@JsonView(Views.Admin.class)
    @Column(name = "updated_by")
    private Integer updatedById;
	@JsonIgnore
	@Version
	private Integer version;
	

	public JobTypeEntity() {
		super();
	}

	public JobTypeEntity(
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "Job type name is not valid.") @NotNull(message = "Job type name must be provided.") String jobTypeName,
			@Max(1) @Min(-1) Integer status, Integer createdById) {
		super();
		this.jobTypeName = jobTypeName;
		this.status = getStatusActive();
		this.createdById = createdById;
	}


	public List<JobSeekEntity> getSeeks() {
		return seeks;
	}

	public void setSeeks(List<JobSeekEntity> seeks) {
		this.seeks = seeks;
	}

	public List<JobOfferEntity> getOffers() {
		return offers;
	}

	public void setOffers(List<JobOfferEntity> offers) {
		this.offers = offers;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getJobTypeName() {
		return jobTypeName;
	}

	public void setJobTypeName(String jobTypeName) {
		this.jobTypeName = jobTypeName;
	}

	public Integer getCreatedById() {
		return createdById;
	}

	public void setCreatedById(Integer createdById) {
		this.createdById = createdById;
	}

	public Integer getUpdatedById() {
		return updatedById;
	}

	public void setUpdatedById(Integer updatedById) {
		this.updatedById = updatedById;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatusInactive() {
		this.status = getStatusInactive();
	}

	public void setStatusActive() {
		this.status = getStatusActive();
	}

	public void setStatusArchived() {
		this.status = getStatusArchived();
	}
	
	public static Integer getStatusInactive() {
		return STATUS_INACTIVE;
	}

	public static Integer getStatusActive() {
		return STATUS_ACTIVE;
	}

	public static Integer getStatusArchived() {
		return STATUS_ARCHIVED;
	}

}
