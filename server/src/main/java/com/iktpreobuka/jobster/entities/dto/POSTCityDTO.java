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
		@Pattern(regexp = "^[A-Za-z\\s]{2,}$", message="City name is not valid.")
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
		
		@NotNull (message = "Country region must be provided.")
		private Integer region;

		
	public POSTCityDTO() {
		super();
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

	public Integer getRegion() {
		return region;
	}

	public void setRegion(Integer region) {
		this.region = region;
	}
   
}
