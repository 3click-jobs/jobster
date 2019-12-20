package com.iktpreobuka.jobster.entities.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class POSTCountryDTO {
	
	@Pattern(regexp = "^[A-Za-z\\s]{2,}$", message="Country name is not valid.")
	@NotNull (message = "Country name must be provided.")
	protected String countryName;
	
	@Pattern(regexp = "^[A-Za-z]{2,2}$", message="ISO2 code is not valid.")
	@NotNull (message = "ISO2 code must be provided.")
	protected String iso2Code;

	public POSTCountryDTO() {
		super();
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
	
	

}
