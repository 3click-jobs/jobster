package com.iktpreobuka.jobster.entities.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

public class AddCommentDTO {

	@Size(max=255, message = "Comment title is to long, maximum is {max}")
	protected String commentTitle;

	@Size(max=255, message = "Comment content is to long, maximum is {max}")
	protected String commentContent;

	@Max(value = 5, message = "Maximum rating is 5 stars")
	@Min(value = 1, message = "Minimum rating is 1 stars")
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
