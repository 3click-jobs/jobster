package com.iktpreobuka.jobster.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Table(name = "job_seeks")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class JobSeekEntity {
	
	
	private static final Integer STATUS_INACTIVE = 0;
	private static final Integer STATUS_ACTIVE = 1;
	private static final Integer STATUS_ARCHIVED = -1;


	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "employee")
	@NotNull (message = "Employee must be provided.")
	private UserEntity employee;
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "city")
	@NotNull (message = "City must be provided.")
	private CityEntity city;
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "type")
	@NotNull (message = "Job type must be provided.")
	private JobTypeEntity type;
	
	@JsonIgnore
	@OneToMany(mappedBy = "seek", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH})
	private List<JobDayHoursEntity> daysAndHours = new ArrayList<>();
	
	@JsonIgnore
	@OneToMany(mappedBy = "seek", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH})
	private List<ApplyContactEntity> contacts = new ArrayList<>(); //STA JE I GDE JE OVO????????????????????????SLOBO
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	//@JsonView(Views.Parent.class)
	@Column(name="seek_id")
	protected Integer id;
	
	//@JsonView(Views.Student.class)
	@Column(name="distance_to_job")
	@NotNull (message = "Distance to job must be provided.")
	//@Pattern(regexp = "^[0-9]{1,5}$", message = "Only numbers are allowed.")
	@Min(value=0, message = "Distance to job must be {value} or higher!")
	private Integer distanceToJob;
	
	//@JsonView(Views.Student.class)
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@NotNull (message = "Beginning date must be provided.")
	@Column(name="beginning_date")
	private Date beginningDate;
	
	//@JsonView(Views.Student.class)
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@NotNull (message = "End date must be provided.")
	@Column(name="end_date")
	private Date endDate;
	
	@Column(name="flexibile_dates")
	//@JsonView(Views.Admin.class)
	private Boolean flexibileDates;
	
	//@JsonView(Views.Student.class)
	@Column(name="price")
	@NotNull (message = "Price must be provided.")
	@Min(value=0, message = "Price must be {value} or higher!")
	private Double price;
	
	//@JsonView(Views.Teacher.class)
	@Column(name="details_link")
	@NotNull (message = "Details must be provided.")
	private String detailsLink;
	
	@Column(name="flexibile_days")
	//@JsonView(Views.Admin.class)
	private Boolean flexibileDays;
	
	//@JsonView(Views.Admin.class)
	@Max(1)
    @Min(-1)
    @Column(name = "status", nullable = false)
	private Integer status;
	
	//@JsonView(Views.Admin.class)
	@Max(1)
    @Min(0)
    @Column(name = "elapse", nullable = false)
	private Integer elapse;
	
	//@JsonView(Views.Admin.class)
    @Column(name = "created_by", nullable = false, updatable = false)
	private Integer createdById;
    
    //@JsonView(Views.Admin.class)
    @Column(name = "updated_by")
    private Integer updatedById;
    
	@JsonIgnore
	@Version
	private Integer version;


	public JobSeekEntity() {
		super();
	}

	public JobSeekEntity(@NotNull(message = "Employee must be provided.") UserEntity employee,
			@NotNull(message = "City must be provided.") CityEntity city,
			@NotNull(message = "Job type must be provided.") JobTypeEntity type, List<JobDayHoursEntity> daysAndHours,
			@NotNull(message = "Distance to job must be provided.") /*@Pattern(regexp = "^[0-9]{1,5}$", message = "Only numbers are allowed.")*/ @Min(value = 0, message = "Distance to job must be {value} or higher!") Integer distanceToJob,
			@NotNull(message = "Beginning date must be provided.") Date beginningDate,
			@NotNull(message = "End date must be provided.") Date endDate, Boolean flexibileDates,
			@NotNull(message = "Price must be provided.") @Min(value = 0, message = "Price must be {value} or higher!") Double price,
			@NotNull(message = "Details must be provided.") String detailsLink, Boolean flexibileDays,
			Integer createdById) {
		super();
		this.employee = employee;
		this.city = city;
		this.type = type;
		this.daysAndHours = daysAndHours;
		this.distanceToJob = distanceToJob;
		this.beginningDate = beginningDate;
		this.endDate = endDate;
		this.flexibileDates = flexibileDates;
		this.price = price;
		this.detailsLink = detailsLink;
		this.flexibileDays = flexibileDays;
		this.status = getStatusActive();
		this.elapse = getStatusActive();
		this.createdById = createdById;
	}

	
	public List<ApplyContactEntity> getContacts() {
		return contacts;
	}

	public void setContacts(List<ApplyContactEntity> contacts) {
		this.contacts = contacts;
	}

	public UserEntity getEmployee() {
		return employee;
	}

	public void setEmployee(UserEntity employee) {
		this.employee = employee;
	}

	public Integer getDistanceToJob() {
		return distanceToJob;
	}

	public void setDistanceToJob(Integer distanceToJob) {
		this.distanceToJob = distanceToJob;
	}

	public CityEntity getCity() {
		return city;
	}

	public void setCity(CityEntity city) {
		this.city = city;
	}

	public JobTypeEntity getType() {
		return type;
	}

	public void setType(JobTypeEntity type) {
		this.type = type;
	}

	public List<JobDayHoursEntity> getDaysAndHours() {
		return daysAndHours;
	}

	public void setDaysAndHours(List<JobDayHoursEntity> daysAndHours) {
		this.daysAndHours = daysAndHours;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Boolean getFlexibileDays() {
		return flexibileDays;
	}

	public void setFlexibileDays(Boolean flexibileDays) {
		this.flexibileDays = flexibileDays;
	}

	public Integer getCreatedById() {
		return createdById;
	}

	public void setCreatedById(Integer createdById) {
		this.createdById = createdById;
	}

	public Integer getUpdatedById() {
		return updatedById;
	}

	public void setUpdatedById(Integer updatedById) {
		this.updatedById = updatedById;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Integer getElapse() {
		return elapse;
	}

	public void setElapseInactive() {
		this.elapse = getStatusInactive();
	}
	
	public void setElapseActive() {
		this.elapse = getStatusActive();
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatusInactive() {
		this.status = getStatusInactive();
	}

	public void setStatusActive() {
		this.status = getStatusActive();
	}

	public void setStatusArchived() {
		this.status = getStatusArchived();
	}
	
	public static Integer getStatusInactive() {
		return STATUS_INACTIVE;
	}

	public static Integer getStatusActive() {
		return STATUS_ACTIVE;
	}

	public static Integer getStatusArchived() {
		return STATUS_ARCHIVED;
	}

}