package com.iktpreobuka.jobster.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.iktpreobuka.jobster.enumerations.EUserType;

@Entity
@Table(name = "applies_contacts")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class ApplyContactEntity {

	private static final Integer STATUS_INACTIVE = 0;
	private static final Integer STATUS_ACTIVE = 1;
	private static final Integer STATUS_ARCHIVED = -1;


	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "offer")
	@NotNull (message = "Job Offer must be provided.")
	private JobOfferEntity offer;
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "seek")
	@NotNull (message = "Job Seek must be provided.")
	private JobSeekEntity seek;
	
	@JsonIgnore
	@OneToMany(mappedBy = "application", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH})
	private List<CommentEntity> comments = new ArrayList<>();

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	//@JsonView(Views.Parent.class)
	@Column(name="apply_contact_id")
	protected Integer id;
	//@JsonView(Views.Student.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@NotNull (message = "Contact date must be provided.")
	@Column(name="contact_date")
	private Date contactDate;
	@Column(name="first_step")
	//@JsonView(Views.Admin.class)
	@Enumerated(EnumType.STRING)
	@NotNull (message = "First step must be provided.")
	private EUserType firstStep;
	@Column(name="are_connected")
	//@JsonView(Views.Admin.class)
	private Boolean areConnected;
	//@JsonView(Views.Student.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@Column(name="connection_date")
	private Date connectionDate;
	@Column(name="rejected")
	//@JsonView(Views.Admin.class)
	private Boolean rejected;
	//@JsonView(Views.Admin.class)
	private Boolean expired;
	//@JsonView(Views.Admin.class)
	private Boolean commentable;
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
	
	
	public ApplyContactEntity() {
		super();
	}

	








	public ApplyContactEntity(@NotNull(message = "Job Offer must be provided.") JobOfferEntity offer,
			@NotNull(message = "Job Seek must be provided.") JobSeekEntity seek, List<CommentEntity> comments,
			Integer id, @NotNull(message = "Contact date must be provided.") Date contactDate,
			@NotNull(message = "First step must be provided.") EUserType firstStep, Boolean areConnected,
			Date connectionDate, Boolean rejected, Boolean expired, Boolean commentable,
			@Max(1) @Min(-1) Integer status, Integer createdById, Integer updatedById, Integer version) {
		super();
		this.offer = offer;
		this.seek = seek;
		this.comments = comments;
		this.id = id;
		this.contactDate = contactDate;
		this.firstStep = firstStep;
		this.areConnected = areConnected;
		this.connectionDate = connectionDate;
		this.rejected = rejected;
		this.expired = expired;
		this.commentable = commentable;
		this.status = status;
		this.createdById = createdById;
		this.updatedById = updatedById;
		this.version = version;
	}

	public Boolean getExpired() {
		return expired;
	}

	public void setExpired(Boolean expired) {
		this.expired = expired;
	}



	public JobOfferEntity getOffer() {
		return offer;
	}

	public void setOffer(JobOfferEntity offer) {
		this.offer = offer;
	}

	public JobSeekEntity getSeek() {
		return seek;
	}

	public void setSeek(JobSeekEntity seek) {
		this.seek = seek;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getContactDate() {
		return contactDate;
	}

	public void setContactDate(Date contactDate) {
		this.contactDate = contactDate;
	}

	public EUserType getFirstStep() {
		return firstStep;
	}

	public void setFirstStep(EUserType firstStep) {
		this.firstStep = firstStep;
	}

	public Boolean getAreConnected() {
		return areConnected;
	}

	public void setAreConnected(Boolean areConnected) {
		this.areConnected = areConnected;
	}

	public Date getConnectionDate() {
		return connectionDate;
	}

	public void setConnectionDate(Date connectionDate) {
		this.connectionDate = connectionDate;
	}

	public Boolean getRejected() {
		return rejected;
	}

	public void setRejected(Boolean rejected) {
		this.rejected = rejected;
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

	public List<CommentEntity> getComments() {
		return comments;
	}

	public void setComments(List<CommentEntity> comments) {
		this.comments = comments;
	}

	public Boolean getCommentable() {
		return commentable;
	}

	public void setCommentable(Boolean commentable) {
		this.commentable = commentable;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
	
}
