package com.iktpreobuka.jobster.entities.dto;

import javax.persistence.Column;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class AddCommentDTO {

	protected String commentTitle;

	protected String commentContent;

	@Max(value = 5, message = "Maximum rating is 5 stars")
	@Min(value = 1, message = "Minimum rating is 1 stars")
	@Column(name = "rating", nullable = false)
	private Integer rating;

	private Integer applyId;

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

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public Integer getApplyId() {
		return applyId;
	}

	public void setApplyId(Integer applyId) {
		this.applyId = applyId;
	}

	public AddCommentDTO(String commentTitle, String commentContent,@Max(value = 5, message = "Maximum rating is 5 stars") @Min(value = 1, message = "Minimum rating is 1 stars") Integer rating,
			Integer applyId) {
		super();
		this.commentTitle = commentTitle;
		this.commentContent = commentContent;
		this.rating = rating;
		this.applyId = applyId;
	}

	public AddCommentDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

}
