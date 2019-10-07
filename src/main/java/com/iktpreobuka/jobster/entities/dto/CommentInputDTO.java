package com.iktpreobuka.jobster.entities.dto;

import javax.persistence.Column;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class CommentInputDTO {
	
	@Column(name="comment_title")
	@NotNull (message = "Please add a title to the comment")
	protected String commentTitle;
	
	@Column(name="comment_content")
	protected String commentContent;
	
	@Max(value=5,message ="Maximum rating is 5 stars")
    @Min(value=1,message ="Minimum rating is 1 stars")
    @Column(name = "rating", nullable = false)
	private Integer rating;

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

	

	public CommentInputDTO(@NotNull(message = "Please add a title to the comment") String commentTitle,
			String commentContent,
			@Max(value = 5, message = "Maximum rating is 5 stars") @Min(value = 1, message = "Minimum rating is 1 stars") Integer rating) {
		super();
		this.commentTitle = commentTitle;
		this.commentContent = commentContent;
		this.rating = rating;
	}

	public CommentInputDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
}
