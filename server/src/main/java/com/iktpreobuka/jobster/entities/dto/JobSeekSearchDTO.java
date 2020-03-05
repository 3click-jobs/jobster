package com.iktpreobuka.jobster.entities.dto;

import java.sql.Date;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonFormat;

public class JobSeekSearchDTO {
	
	@Min(value=1, message = "Employee Id must be {value} or higher!")
	protected Integer employeeId;
	
	@Pattern(regexp = "^[A-Za-z\\s]{2,}$", message="City name is not valid.")
	protected String cityName;
	
	@Pattern(regexp = "^[A-Za-z\\s]{0,}$", message="Country region name is not valid.")
	private String countryRegionName;
	
	@Pattern(regexp = "^[A-Za-z\\s]{2,}$", message="Country name is not valid.")
	private String countryName;
	
	@Min(value=1, message = "Job type Id must be {value} or higher!")
	protected Integer typeId;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date beginningDate;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date endDate;
			
	private Boolean flexibileDates;
			
	private Boolean flexibileDays;
	
	@Min(value=0, message = "Price must be {value} or higher!")
	private Double price;
	
	protected List<JobDayHoursDTO> jobDayHours;

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCountryRegionName() {
		return countryRegionName;
	}

	public void setCountryRegionName(String countryRegionName) {
		this.countryRegionName = countryRegionName;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
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

	public List<JobDayHoursDTO> getJobDayHours() {
		return jobDayHours;
	}

	public void setJobDayHours(List<JobDayHoursDTO> jobDayHours) {
		this.jobDayHours = jobDayHours;
	}
	
	public JobSeekSearchDTO() {};

	public JobSeekSearchDTO(@Min(value = 1, message = "Employee Id must be {value} or higher!") Integer employeeId,
			@Pattern(regexp = "^[A-Za-z\\s]{2,}$", message = "City name is not valid.") String cityName,
			@Pattern(regexp = "^[A-Za-z\\s]{0,}$", message = "Country region name is not valid.") String countryRegionName,
			@Pattern(regexp = "^[A-Za-z\\s]{2,}$", message = "Country name is not valid.") String countryName,
			@Min(value = 1, message = "Job type Id must be {value} or higher!") Integer typeId, Date beginningDate,
			Date endDate, Boolean flexibileDates, Boolean flexibileDays,
			@Min(value = 0, message = "Price must be {value} or higher!") Double price,
			List<JobDayHoursDTO> jobDayHours) {
		super();
		this.employeeId = employeeId;
		this.cityName = cityName;
		this.countryRegionName = countryRegionName;
		this.countryName = countryName;
		this.typeId = typeId;
		this.beginningDate = beginningDate;
		this.endDate = endDate;
		this.flexibileDates = flexibileDates;
		this.flexibileDays = flexibileDays;
		this.price = price;
		this.jobDayHours = jobDayHours;
	}
	
}
