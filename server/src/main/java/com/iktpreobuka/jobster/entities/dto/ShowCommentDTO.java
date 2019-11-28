package com.iktpreobuka.jobster.entities.dto;

import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.jobster.security.Views;

public class ShowCommentDTO {
	
	@NotNull (message = "Please add a title to the comment")
	private String commentTitle;
	private String commentContent;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date commentDate;
	@Max(value = 5, message = "Maximum rating is 5 stars")
	@Min(value = 1, message = "Minimum rating is 1 stars")
	private Integer rating;
	@Pattern(regexp = "^[A-Za-z]{2,}$", message="First name is not valid, can contain only letters and minimum is 2 letter.")
	private String commentPosterName;
	@Pattern(regexp = "^[A-Za-z\\s]+$", message="City name is not valid, can contain only letters and minimum 1 letter.")
	private String cityName;
	@Pattern(regexp = "^[A-Za-z\\s]+$", message="Job type is not valid, can contain only letters.")
	private String type;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date beginningDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date endDate;
	@JsonView(Views.User.class)
	private Double price;
	private Integer id;
	
	
	public ShowCommentDTO() {
		super();
		// TODO Auto-generated constructor stub
	}


	


	public Integer getId() {
		return id;
	}





	public void setId(Integer id) {
		this.id = id;
	}





	public ShowCommentDTO(@NotNull(message = "Please add a title to the comment") String commentTitle,
			String commentContent, Date commentDate,
			@Max(value = 5, message = "Maximum rating is 5 stars") @Min(value = 1, message = "Minimum rating is 1 stars") Integer rating,
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "First name is not valid, can contain only letters and minimum is 2 letter.") String commentPosterName,
			@Pattern(regexp = "^[A-Za-z\\s]+$", message = "City name is not valid, can contain only letters and minimum 1 letter.") String cityName,
			@Pattern(regexp = "^[A-Za-z\\s]+$", message = "Job type is not valid, can contain only letters.") String type,
			Date beginningDate, Date endDate, Double price, Integer id) {
		super();
		this.commentTitle = commentTitle;
		this.commentContent = commentContent;
		this.commentDate = commentDate;
		this.rating = rating;
		this.commentPosterName = commentPosterName;
		this.cityName = cityName;
		this.type = type;
		this.beginningDate = beginningDate;
		this.endDate = endDate;
		this.price = price;
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


	public String getCommentPosterName() {
		return commentPosterName;
	}


	public void setCommentPosterName(String commentPosterName) {
		this.commentPosterName = commentPosterName;
	}


	public String getCityName() {
		return cityName;
	}


	public void setCityName(String cityName) {
		this.cityName = cityName;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public Date getBeginningDate() {
		return beginningDate;
	}


	public void setBeginningDate(Date beginningDate) {
		this.beginningDate = beginningDate;
	}


	public Date getEndDate() {
		return endDate;
	}


	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}


	public Double getPrice() {
		return price;
	}


	public void setPrice(Double price) {
		this.price = price;
	}
	
	
	
}
