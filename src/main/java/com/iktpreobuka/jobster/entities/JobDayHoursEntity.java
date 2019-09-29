package com.iktpreobuka.jobster.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.iktpreobuka.jobster.enumerations.EDay;

@Entity
@Table (name = "working_days_hours")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class JobDayHoursEntity {

	private static final Integer STATUS_INACTIVE = 0;
	private static final Integer STATUS_ACTIVE = 1;
	private static final Integer STATUS_ARCHIVED = -1;
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "offer")
	//@NotNull (message = "Job offer must be provided.")
	private JobOfferEntity offer;	
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "seek")
	//@NotNull (message = "Job seek must be provided.")
	private JobSeekEntity seek;	

	
	//@JsonView(Views.Admin.class)
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="days_hours_id")
	private Integer id;
	@Column(name="working_day")
	//@JsonView(Views.Admin.class)
	@Enumerated(EnumType.STRING)
	@NotNull (message = "Day must be provided.")
	private EDay day;
	@Column(name="from_min_hour")
	//@JsonView(Views.Admin.class)
	@NotNull (message = "From/minimum hour/s must be provided.")
	@Min(value=0, message = "From/minimum hour/s must be {value} or higher!")
	@Max(value=24, message = "From/minimum hour/s must be {value} or lower!")
	private Integer fromHour;
	@Column(name="to_max_hour")
	//@JsonView(Views.Admin.class)
	@NotNull (message = "To/maximum hour/s must be provided.")
	@Min(value=0, message = "To/maximum hour/s must be {value} or higher!")
	@Max(value=24, message = "To/maximum hour/s must be {value} or lower!")
	private Integer toHour;
	@Column(name="is_min_max")
	//@JsonView(Views.Admin.class)
	private Boolean isMinMax;
	@Column(name="flexibile_hours")
	//@JsonView(Views.Admin.class)
	private Boolean flexibileHours;
	//@JsonView(Views.Admin.class)
	@Max(1)
    @Min(-1)
    @Column(name = "status", nullable = false)
	private Integer status;
	//@JsonView(Views.Admin.class)
    @Column(name = "created_by", nullable = false, updatable = false)
	private Integer createdById;
    //@JsonView(Views.Admin.class)
    @Column(name = "updated_by")
    private Integer updatedById;
	@JsonIgnore
	@Version
	private Integer version;
	

	public JobDayHoursEntity() {
		super();
	}
	
	public JobDayHoursEntity(@NotNull(message = "Job offer must be provided.") JobOfferEntity offer,
			@NotNull(message = "Day must be provided.") EDay day,
			@NotNull(message = "From/minimum hour/s must be provided.") @Min(value = 0, message = "From/minimum hour/s must be {value} or higher!") @Max(value = 24, message = "From/minimum hour/s must be {value} or lower!") Integer fromHour,
			@NotNull(message = "To/maximum hour/s must be provided.") @Min(value = 0, message = "To/maximum hour/s must be {value} or higher!") @Max(value = 24, message = "To/maximum hour/s must be {value} or lower!") Integer toHour,
			Boolean isMinMax, Boolean isFlexibile, Integer createdById) {
		super();
		this.offer = offer;
		this.day = day;
		this.fromHour = fromHour;
		this.toHour = toHour;
		this.isMinMax = isMinMax;
		this.flexibileHours = isFlexibile;
		this.status = getStatusActive();;
		this.createdById = createdById;
	}
	
	
	public JobOfferEntity getOffer() {
		return offer;
	}

	public void setOffer(JobOfferEntity offer) {
		this.offer = offer;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public EDay getDay() {
		return day;
	}

	public void setDay(EDay day) {
		this.day = day;
	}

	public Integer getFromHour() {
		return fromHour;
	}

	public void setFromHour(Integer fromHour) {
		this.fromHour = fromHour;
	}

	public Integer getToHour() {
		return toHour;
	}

	public void setToHour(Integer toHour) {
		this.toHour = toHour;
	}

	public Boolean getIsMinMax() {
		return isMinMax;
	}

	public void setIsMinMax(Boolean isMinMax) {
		this.isMinMax = isMinMax;
	}

	public Boolean getFlexibileHours() {
		return flexibileHours;
	}

	public void setFlexibileHours(Boolean isFlexibile) {
		this.flexibileHours = isFlexibile;
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
