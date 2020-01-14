package com.iktpreobuka.jobster.entities.dto;

import javax.persistence.Column;
import javax.persistence.Version;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iktpreobuka.jobster.entities.CountryRegionEntity;

public class POSTCityDTO {
	

	//@JsonView(Views.Student.class)
		@Pattern(regexp = "^[a-zA-Z]{2,}+( [a-zA-Z_]+)*$", message="City name is not valid.")
		@NotNull (message = "City name must be provided.")
		protected String cityName;
		
		//@JsonView(Views.Student.class)
		@NotNull (message = "Longitude must be provided.")
		@Min(value=-180, message = "Longitude  must be {value} or higher!")
		@Max(value=180, message = "Longitude must be {value} or lower!")
		private Double longitude;
		
		//@JsonView(Views.Student.class)
		@NotNull (message = "Latitude must be provided.")
		@Min(value=-90, message = "Latitude  must be {value} or higher!")
		@Max(value=90, message = "Latitude must be {value} or lower!")
		private Double latitude;
		
		@Pattern(regexp = "^[a-zA-Z]{2,}+( [a-zA-Z_]+)*$", message="Country region name is not valid.")
		private String region;
		
		@Pattern(regexp = "^[a-zA-Z]{2,}+( [a-zA-Z_]+)*$", message="Country name is not valid.")
		@NotNull (message = "Country must be provided.")
		private String country;
		
		@Pattern(regexp = "^[A-Za-z]{2,2}$", message="ISO2 code is not valid.")
		@NotNull (message = "ISO2 code must be provided.")
		protected String iso2Code;

		
	public POSTCityDTO() {
		super();
	}
	
	public POSTCityDTO(
			@Pattern(regexp = "^[a-zA-Z]{2,}+( [a-zA-Z_]+)*$", message = "City name is not valid.") @NotNull(message = "City name must be provided.") String cityName,
			@NotNull(message = "Longitude must be provided.") @Min(value = -180, message = "Longitude  must be {value} or higher!") @Max(value = 180, message = "Longitude must be {value} or lower!") Double longitude,
			@NotNull(message = "Latitude must be provided.") @Min(value = -90, message = "Latitude  must be {value} or higher!") @Max(value = 90, message = "Latitude must be {value} or lower!") Double latitude,
			@Pattern(regexp = "^[a-zA-Z]{2,}+( [a-zA-Z_]+)*$", message="Country region name is not valid.") String region,
			@Pattern(regexp = "^[a-zA-Z]{2,}+( [a-zA-Z_]+)*$", message="Country name is not valid.") @NotNull (message = "Country must be provided.") String country,
			@Pattern(regexp = "^[A-Za-z]{2,2}$", message="ISO2 code is not valid.") @NotNull (message = "ISO2 code must be provided.") String iso2Code)
	{
		super();
		this.cityName = cityName;
		this.longitude = longitude;
		this.latitude = latitude;
		this.region = region;
		this.country = country;
		this.iso2Code = iso2Code;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getIso2Code() {
		return iso2Code;
	}

	public void setIso2Code(String iso2Code) {
		this.iso2Code = iso2Code;
	}
   
}
