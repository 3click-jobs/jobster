package com.iktpreobuka.jobster.entities.dto;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

public class JobSeekPutDto {

	@Pattern(regexp = "^[A-Za-z\\s]{2,}$", message="City name is not valid.")
	protected String cityName;
	
	@Pattern(regexp = "^[A-Za-z]{2,}$", message="Job type name is not valid.")
	protected String jobTypeName;

	@Pattern(regexp = "^[0-9]{1,5}$", message = "Only numbers are allowed.")
	@Min(value=0, message = "Distance to job must be {value} or higher!")
	private Integer distanceToJob;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date beginningDate;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date endDate;
			
	private Boolean flexibileDates;
			
	private Boolean flexibileDays;
	
	@Min(value=0, message = "Price must be {value} or higher!")
	private Double price;
	
    @Size(min = 0, max = 250, message = "Text can have 250 character max.")
	private String detailsLink;
	
	protected List<JobDayHoursPutDto> listJobDayHoursPutDto;
	
	@Pattern(regexp = "^[A-Za-z\\s]{2,}$", message="Country name is not valid.")
	private String countryName;
	
	@Pattern(regexp = "^[A-Za-z]{2,3}$", message="ISO2 code is not valid.")
	protected String iso2Code;
	
	@Pattern(regexp = "^[A-Za-z\\s]{0,}$", message="Country region name is not valid.")
	private String countryRegionName;
	
	@Min(value=-180, message = "Longitude  must be {value} or higher!")
	@Max(value=180, message = "Longitude must be {value} or lower!")
	private Double longitude;
	
	@Min(value=-90, message = "Latitude  must be {value} or higher!")
	@Max(value=90, message = "Latitude must be {value} or lower!")
	private Double latitude;
	
	public JobSeekPutDto() {}

	public JobSeekPutDto(@Pattern(regexp = "^[A-Za-z\\s]{2,}$", message = "City name is not valid.") String cityName,
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "Job type name is not valid.") String jobTypeName,
			@Pattern(regexp = "^[0-9]{1,5}$", message = "Only numbers are allowed.") @Min(value = 0, message = "Distance to job must be {value} or higher!") Integer distanceToJob,
			Date beginningDate, Date endDate, Boolean flexibileDates, Boolean flexibileDays,
			@Min(value = 0, message = "Price must be {value} or higher!") Double price,
			@Size(min = 0, max = 250, message = "Text can have 250 character max.") String detailsLink,
			List<JobDayHoursPutDto> listJobDayHoursPutDto,
			@Pattern(regexp = "^[A-Za-z\\s]{2,}$", message = "Country name is not valid.") String countryName,
			@Pattern(regexp = "^[A-Za-z]{2,3}$", message = "ISO2 code is not valid.") String iso2Code,
			@Pattern(regexp = "^[A-Za-z\\s]{0,}$", message = "Country region name is not valid.") String countryRegionName,
			@Min(value = -180, message = "Longitude  must be {value} or higher!") @Max(value = 180, message = "Longitude must be {value} or lower!") Double longitude,
			@Min(value = -90, message = "Latitude  must be {value} or higher!") @Max(value = 90, message = "Latitude must be {value} or lower!") Double latitude) {
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
		this.listJobDayHoursPutDto = listJobDayHoursPutDto;
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

	public List<JobDayHoursPutDto> getListJobDayHoursPutDto() {
		return listJobDayHoursPutDto;
	}

	public void setListJobDayHoursPutDto(List<JobDayHoursPutDto> listJobDayHoursPutDto) {
		this.listJobDayHoursPutDto = listJobDayHoursPutDto;
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
