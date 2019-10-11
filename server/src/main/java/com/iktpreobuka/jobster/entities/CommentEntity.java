package com.iktpreobuka.jobster.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.jobster.security.Views;

@Entity
@Table(name = "apply_comments")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class CommentEntity {

	private static final Integer STATUS_INACTIVE = 0;
	private static final Integer STATUS_ACTIVE = 1;
	private static final Integer STATUS_ARCHIVED = -1;
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "application")
	@NotNull (message = "Comment needs to be connected to a specific job apply")
	private ApplyContactEntity application;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="apply_comment_id")
	protected Integer id;
	
	@Column(name="comment_title")
	@NotNull (message = "Please add a title to the comment")
	protected String commentTitle;
	
	@Column(name="comment_content")
	protected String commentContent;
	
	@Max(1)
    @Min(-1)
    @Column(name = "status", nullable = false)
	private Integer status;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@NotNull (message = "Comment date must be provided.")
	@Column(name="comment_date")
	private Date commentDate;

	
	@Max(value=5,message ="Maximum rating is 5 stars")
    @Min(value=1,message ="Minimum rating is 1 stars")
    @Column(name = "rating", nullable = false)
	private Integer rating;
	
	@JsonView(Views.Admin.class)
    @Column(name = "comment_receiver", nullable = false, updatable = false)
	private Integer commentReceiver;
	
	@Column(name="edited")
	private Boolean edited;
	
	@JsonView(Views.Admin.class)
    @Column(name = "created_by", nullable = false, updatable = false)
	private Integer createdById;
    
    @JsonView(Views.Admin.class)
    @Column(name = "updated_by")
    private Integer updatedById;
    
	@JsonIgnore
	@Version
	private Integer version;

	public ApplyContactEntity getApplication() {
		return application;
	}

	public void setApplication(ApplyContactEntity application) {
		this.application = application;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCommentTitle() {
		return commentTitle;
	}

	public void setCommentTitle(String commentTitle) {
		this.commentTitle = commentTitle;
	}

	public String getCommentContent() {
		return commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCommentDate() {
		return commentDate;
	}

	public void setCommentDate(Date commentDate) {
		this.commentDate = commentDate;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public Integer getCommentReceiver() {
		return commentReceiver;
	}

	public void setCommentReceiver(Integer commentReceiver) {
		this.commentReceiver = commentReceiver;
	}

	public Boolean getEdited() {
		return edited;
	}

	public void setEdited(Boolean edited) {
		this.edited = edited;
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

	public static Integer getStatusInactive() {
		return STATUS_INACTIVE;
	}

	public static Integer getStatusActive() {
		return STATUS_ACTIVE;
	}

	public static Integer getStatusArchived() {
		return STATUS_ARCHIVED;
	}

	public CommentEntity(
			@NotNull(message = "Comment needs to be connected to a specific job apply") ApplyContactEntity application,
			Integer id, @NotNull(message = "Please add a title to the comment") String commentTitle,
			String commentContent, @Max(1) @Min(-1) Integer status,
			@NotNull(message = "Comment date must be provided.") Date commentDate,
			@Max(value = 5, message = "Maximum rating is 5 stars") @Min(value = 1, message = "Minimum rating is 1 stars") Integer rating,
			Integer commentReceiver, Boolean edited, Integer createdById, Integer updatedById, Integer version) {
		super();
		this.application = application;
		this.id = id;
		this.commentTitle = commentTitle;
		this.commentContent = commentContent;
		this.status = status;
		this.commentDate = commentDate;
		this.rating = rating;
		this.commentReceiver = commentReceiver;
		this.edited = edited;
		this.createdById = createdById;
		this.updatedById = updatedById;
		this.version = version;
	}

	public CommentEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	
	
}
