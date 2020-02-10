package com.iktpreobuka.jobster.entities.dto;

import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.jobster.security.Views;

public class JobTypeDTO {

	@JsonView(Views.Admin.class)
	@Pattern(regexp = "^[A-Za-z-, \\.\\/\\(\\)]{2,}$", message="Job type name is not valid.")
	protected String jobTypeName;

	public JobTypeDTO() {
		super();
	}

	public JobTypeDTO(@Pattern(regexp = "^[A-Za-z-, \\.\\/\\(\\)]{2,}$", message = "Job type name is not valid.") String jobTypeName) {
		super();
		this.jobTypeName = jobTypeName;
	}

	public String getJobTypeName() {
		return jobTypeName;
	}

	public void setJobTypeName(String jobTypeName) {
		this.jobTypeName = jobTypeName;
	}
	
}
