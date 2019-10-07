package com.iktpreobuka.jobster.entities.dto;

import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonFormat;

public class CommentOutputDTO {
	
	
	/*Moram da dodam i delove od posla... posle :) */
	

	private String commentTitle;
	private String commentContent;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date commentDate;
	
	@Max(value=5,message ="Maximum rating is 5 stars")
    @Min(value=1,message ="Minimum rating is 1 stars")
	private Integer rating;
	
    private String commentCreatorFirstName;
    
    private String commentCreatorLastName;
    
    

}
