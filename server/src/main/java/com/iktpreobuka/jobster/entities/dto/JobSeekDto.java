package com.iktpreobuka.jobster.entities.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.iktpreobuka.jobster.entities.UserEntity;
import com.iktpreobuka.jobster.enumerations.EDay;

public class JobSeekDto {
	
		
		//@JsonView(Views.Admin.class)
		@Pattern(regexp = "^[A-Za-z\\s]{2,}$", message="City name is not valid.")
		@NotNull (message = "City name must be provided.")
		protected String cityName;
		
		//@JsonView(Views.Student.class)
		@Pattern(regexp = "^[A-Za-z]{2,}$", message="Job type name is not valid.")
		@NotNull (message = "Job type name must be provided.")
		protected String jobTypeName;
	
		//@JsonView(Views.Student.class)
		@NotNull (message = "Distance to job must be provided.") //??????????????????????
		@Pattern(regexp = "^[0-9]{1,5}$", message = "Only numbers are allowed.")
		@Min(value=0, message = "Distance to job must be {value} or higher!")
		private Integer distanceToJob;
		
		
		
		 // -----------------DATES-------------------
		
		//@JsonView(Views.Student.class)
		@NotNull (message = "Beginning date must be provided.") //?????????????????????????
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
		private Date beginningDate;
		
		//@JsonView(Views.Student.class)
		@NotNull (message = "End date must be provided.") //??????????????????????????????????
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
		private Date endDate;
		
		//@JsonView(Views.Admin.class)
		private Boolean flexibileDates;
		
		//@JsonView(Views.Admin.class)
		private Boolean flexibileDays; //------------------DAYS---------------------
		
		//@JsonView(Views.Student.class)
		@NotNull (message = "Price must be provided.") //?????????????????????????????????????
		@Min(value=0, message = "Price must be {value} or higher!")
		private Double price;
		
		//@JsonView(Views.Teacher.class)
		@NotNull (message = "Details must be provided.") //???????????????????????????????????
	    @Size(min = 0, max = 250, message = "Text can have 250 character max.")
		private String detailsLink;
		
		//@JsonView(Views.Admin.class)
		@NotNull (message = "List must be provided.")
		protected List<JobDayHoursDto> listJobDayHoursDto;
		
		public JobSeekDto() {}

		public JobSeekDto(
				@Pattern(regexp = "^[A-Za-z\\s]{2,}$", message = "City name is not valid.") @NotNull(message = "City name must be provided.") String cityName,
				@Pattern(regexp = "^[A-Za-z]{2,}$", message = "Job type name is not valid.") @NotNull(message = "Job type name must be provided.") String jobTypeName,
				@NotNull(message = "Distance to job must be provided.") @Pattern(regexp = "^[0-9]{1,5}$", message = "Only numbers are allowed.") @Min(value = 0, message = "Distance to job must be {value} or higher!") Integer distanceToJob,
				@NotNull(message = "Beginning date must be provided.") Date beginningDate,
				@NotNull(message = "End date must be provided.") Date endDate, Boolean flexibileDates,
				Boolean flexibileDays,
				@NotNull(message = "Price must be provided.") @Min(value = 0, message = "Price must be {value} or higher!") Double price,
				@NotNull(message = "Details must be provided.") @Size(min = 0, max = 250, message = "Text can have 250 character max.") String detailsLink,
				@NotNull(message = "List must be provided.") List<JobDayHoursDto> listJobDayHoursDto) {
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
			this.listJobDayHoursDto = listJobDayHoursDto;
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

		public List<JobDayHoursDto> getListJobDayHoursDto() {
			return listJobDayHoursDto;
		}

		public void setListJobDayHoursDto(List<JobDayHoursDto> listJobDayHoursDto) {
			this.listJobDayHoursDto = listJobDayHoursDto;
		};
		
		

}