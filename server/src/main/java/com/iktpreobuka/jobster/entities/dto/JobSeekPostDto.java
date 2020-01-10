package com.iktpreobuka.jobster.entities.dto;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

public class JobSeekPostDto {
	
		
		@Pattern(regexp = "^[A-Za-z\\s]{2,}$", message="City name is not valid.")
		@NotNull (message = "City name must be provided.")
		protected String cityName;
		
		@Pattern(regexp = "^[A-Za-z]{2,}$", message="Job type name is not valid.")
		@NotNull (message = "Job type name must be provided.")
		protected String jobTypeName;
	
		@NotNull (message = "Distance to job must be provided.")
		//@Pattern(regexp = "^[0-9]{1,5}$", message = "Only numbers are allowed.")
		@Min(value=0, message = "Distance to job must be {value} or higher!")
		private Integer distanceToJob;
	
		@NotNull (message = "Beginning date must be provided.")
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
		private Date beginningDate;
		
		@NotNull (message = "End date must be provided.")
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
		private Date endDate;
				
		private Boolean flexibileDates;
				
		private Boolean flexibileDays;
		
		@NotNull (message = "Price must be provided.")
		@Min(value=0, message = "Price must be {value} or higher!")
		private Double price;
		
	    @Size(min = 0, max = 250, message = "Text can have 250 character max.")
	    @NotNull (message = "Details must be provided.")
		private String detailsLink;
		
		@NotNull (message = "List DayHours must be provided.")
		protected List<JobDayHoursPostDto> listJobDayHoursPostDto;
		
		@NotNull (message = "Contry name must be provided.")
		@Pattern(regexp = "^[A-Za-z\\s]{2,}$", message="Country name is not valid.")
		private String countryName;
		
		@Pattern(regexp = "^[A-Za-z]{2,3}$", message="ISO2 code is not valid.")
		@NotNull (message = "Contry Iso2Code must be provided.")
		protected String iso2Code;
		
		@Pattern(regexp = "^[A-Za-z\\s]{0,}$", message="Country region name is not valid.")
		private String countryRegionName;
		
		@Min(value=-180, message = "Longitude  must be {value} or higher!")
		@Max(value=180, message = "Longitude must be {value} or lower!")
		@NotNull (message = "City longitude must be provided.")
		private Double longitude;
		
		@Min(value=-90, message = "Latitude  must be {value} or higher!")
		@Max(value=90, message = "Latitude must be {value} or lower!")
		@NotNull (message = "City latitude must be provided.")
		private Double latitude;
		
		public JobSeekPostDto() {}

		
		public JobSeekPostDto(
				@Pattern(regexp = "^[A-Za-z\\s]{2,}$", message = "City name is not valid.") @NotNull(message = "City name must be provided.") String cityName,
				@Pattern(regexp = "^[A-Za-z]{2,}$", message = "Job type name is not valid.") @NotNull(message = "Job type name must be provided.") String jobTypeName,
				@NotNull(message = "Distance to job must be provided.") @Min(value = 0, message = "Distance to job must be {value} or higher!") Integer distanceToJob,
				@NotNull(message = "Beginning date must be provided.") Date beginningDate,
				@NotNull(message = "End date must be provided.") Date endDate, Boolean flexibileDates,
				Boolean flexibileDays,
				@NotNull(message = "Price must be provided.") @Min(value = 0, message = "Price must be {value} or higher!") Double price,
				@Size(min = 0, max = 250, message = "Text can have 250 character max.") @NotNull(message = "Details must be provided.") String detailsLink,
				@NotNull(message = "List DayHours must be provided.") List<JobDayHoursPostDto> listJobDayHoursPostDto,
				@NotNull(message = "Contry name must be provided.") @Pattern(regexp = "^[A-Za-z\\s]{2,}$", message = "Country name is not valid.") String countryName,
				@Pattern(regexp = "^[A-Za-z]{2,3}$", message = "ISO2 code is not valid.") @NotNull(message = "Contry Iso2Code must be provided.") String iso2Code,
				@Pattern(regexp = "^[A-Za-z\\s]{0,}$", message = "Country region name is not valid.") String countryRegionName,
				@Min(value = -180, message = "Longitude  must be {value} or higher!") @Max(value = 180, message = "Longitude must be {value} or lower!") @NotNull(message = "City longitude must be provided.") Double longitude,
				@Min(value = -90, message = "Latitude  must be {value} or higher!") @Max(value = 90, message = "Latitude must be {value} or lower!") @NotNull(message = "City latitude must be provided.") Double latitude) {
			super();
			this.cityName = cityName;
			this.jobTypeName = jobTypeName;
			this.distanceToJob = distanceToJob;
			this.beginningDate = beginningDate;
			this.endDate = endDate;
			this.flexibileDates = flexibileDates;
			this.flexibileDays = flexibileDays;
			this.price = price;
			this.detailsLink = detailsLink;
			this.listJobDayHoursPostDto = listJobDayHoursPostDto;
			this.countryName = countryName;
			this.iso2Code = iso2Code;
			this.countryRegionName = countryRegionName;
			this.longitude = longitude;
			this.latitude = latitude;
		}



		public String getCityName() {
			return cityName;
		}

		public void setCityName(String cityName) {
			this.cityName = cityName;
		}

		public String getJobTypeName() {
			return jobTypeName;
		}

		public void setJobTypeName(String jobTypeName) {
			this.jobTypeName = jobTypeName;
		}

		public Integer getDistanceToJob() {
			return distanceToJob;
		}

		public void setDistanceToJob(Integer distanceToJob) {
			this.distanceToJob = distanceToJob;
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

		public Boolean getFlexibileDates() {
			return flexibileDates;
		}

		public void setFlexibileDates(Boolean flexibileDates) {
			this.flexibileDates = flexibileDates;
		}

		public Boolean getFlexibileDays() {
			return flexibileDays;
		}

		public void setFlexibileDays(Boolean flexibileDays) {
			this.flexibileDays = flexibileDays;
		}

		public Double getPrice() {
			return price;
		}

		public void setPrice(Double price) {
			this.price = price;
		}

		public String getDetailsLink() {
			return detailsLink;
		}

		public void setDetailsLink(String detailsLink) {
			this.detailsLink = detailsLink;
		}

		public List<JobDayHoursPostDto> getListJobDayHoursPostDto() {
			return listJobDayHoursPostDto;
		}

		public void setListJobDayHoursPostDto(List<JobDayHoursPostDto> listJobDayHoursPostDto) {
			this.listJobDayHoursPostDto = listJobDayHoursPostDto;
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

		public String getCountryRegionName() {
			return countryRegionName;
		}

		public void setCountryRegionName(String countryRegionName) {
			this.countryRegionName = countryRegionName;
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
	

}