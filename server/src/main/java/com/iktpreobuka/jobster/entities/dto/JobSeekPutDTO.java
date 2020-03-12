package com.iktpreobuka.jobster.entities.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class JobSeekPutDTO {
	
	@Size(min = 0, max = 250, message = "Text can have 250 character max.")
    @NotNull (message = "Details must be provided.")
	private String detailsLink;
	
	JobSeekPutDTO(){}

	public JobSeekPutDTO(
			@Size(min = 0, max = 250, message = "Text can have 250 character max.") @NotNull(message = "Details must be provided.") String detailsLink) {
		this.detailsLink = detailsLink;
	}

	public String getDetailsLink() {
		return detailsLink;
	}

	public void setDetailsLink(String detailsLink) {
		this.detailsLink = detailsLink;
	}

}
