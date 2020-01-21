package com.iktpreobuka.jobster.entities.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class POSTCountryDTO {
	
	@Pattern(regexp = "^[a-zA-Z]{2,}+( [a-zA-Z_]+)*$", message="Country name is not valid.")
	@NotNull (message = "Country name must be provided.")
	protected String countryName;
	
	@Pattern(regexp = "^[A-Za-z]{2,2}$", message="ISO2 code is not valid.")
	@NotNull (message = "ISO2 code must be provided.")
	protected String iso2Code;
	
	//@JsonView(Views.Admin.class)
	@Max(1)
	@Min(-1)
	private Integer status;

	public POSTCountryDTO() {
		super();
	}
	
	public POSTCountryDTO(
			@Pattern(regexp = "^[a-zA-Z]{2,}+( [a-zA-Z_]+)*$", message = "Country name is not valid.") @NotNull(message = "Country name must be provided.") String countryName,
			@Pattern(regexp = "^[A-Za-z]{2,2}$", message = "ISO2 code is not valid.") @NotNull(message = "ISO2 code must be provided.") String iso2Code,
			@Max(1) @Min(-1)  Integer status) {
	super();
	this.countryName = countryName;
	this.iso2Code = iso2Code;
	this.status = status;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getIso2Code() {
		return iso2Code;
	}

	public void setIso2Code(String iso2Code) {
		this.iso2Code = iso2Code;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	

}
