package com.iktpreobuka.jobster.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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

@Entity
@Table(name = "city_distances")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class CityDistanceEntity {
	
	
	private static final Integer STATUS_INACTIVE = 0;
	private static final Integer STATUS_ACTIVE = 1;
	private static final Integer STATUS_ARCHIVED = -1;


	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name = "fromCity", referencedColumnName = "city_id", nullable=false)
	//@OneToOne(mappedBy = "fromCity")
	@NotNull (message = "From city must be provided.")
	private CityEntity fromCity;
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name = "toCity", referencedColumnName = "city_id", nullable=false)
	//@OneToOne(mappedBy = "toCity")
	@NotNull (message = "To city must be provided.")
	private CityEntity toCity;

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	//@JsonView(Views.Parent.class)
	@Column(name="city_distance_id")
	protected Integer id;
	//@JsonView(Views.Student.class)
	@Column(name="km")
	@NotNull (message = "Distance in kilometres must be provided.")
	@Min(value=0, message = "Distance in kilometres must be {value} or higher!")
	private Double kmDistance;
	//@JsonView(Views.Student.class)
	@Column(name="mi")
	@NotNull (message = "Distance in miles must be provided.")
	@Min(value=0, message = "Distance in miles  must be {value} or higher!")
	private Double miDistance;
	//@JsonView(Views.Admin.class)
	@Max(1)
    @Min(-1)
    @Column(name = "status", nullable = false)
	private Integer status;
	//@JsonView(Views.Admin.class)
    @Column(name = "created_by", updatable = false)
	private Integer createdById;
    //@JsonView(Views.Admin.class)
    @Column(name = "updated_by")
    private Integer updatedById;
	@JsonIgnore
	@Version
	private Integer version;

	
	public CityDistanceEntity() {
		super();
	}
	
	public CityDistanceEntity(@NotNull(message = "From city must be provided.") CityEntity fromCity,
			@NotNull(message = "To city must be provided.") CityEntity toCity,
			@NotNull(message = "Distance in kilometres must be provided.") @Min(value = 0, message = "Distance in kilometres must be {value} or higher!") Double kmDistance,
			@NotNull(message = "Distance in miles must be provided.") @Min(value = 0, message = "Distance in miles  must be {value} or higher!") Double miDistance,
			Integer createdById) {
		super();
		this.fromCity = fromCity;
		this.toCity = toCity;
		this.kmDistance = kmDistance;
		this.miDistance = miDistance;
		this.status = getStatusActive();
		this.createdById = createdById;
	}

	public CityDistanceEntity(@NotNull(message = "From city must be provided.") CityEntity fromCity,
			@NotNull(message = "To city must be provided.") CityEntity toCity,
			@NotNull(message = "Distance in kilometres must be provided.") @Min(value = 0, message = "Distance in kilometres must be {value} or higher!") Double kmDistance,
			@NotNull(message = "Distance in miles must be provided.") @Min(value = 0, message = "Distance in miles  must be {value} or higher!") Double miDistance) {
		super();
		this.fromCity = fromCity;
		this.toCity = toCity;
		this.kmDistance = kmDistance;
		this.miDistance = miDistance;
		this.status = getStatusActive();
	}


	public Integer getId() {
		return id;
	}

	public CityEntity getFrom() {
		return fromCity;
	}

	public void setFrom(CityEntity fromCity) {
		this.fromCity = fromCity;
	}

	public CityEntity getTo() {
		return toCity;
	}

	public void setTo(CityEntity toCity) {
		this.toCity = toCity;
	}

	public Double getKmDistance() {
		return kmDistance;
	}

	public void setKmDistance(Double kmDistance) {
		this.kmDistance = kmDistance;
	}

	public Double getMiDistance() {
		return miDistance;
	}

	public void setMiDistance(Double miDistance) {
		this.miDistance = miDistance;
	}

	public void setId(Integer id) {
		this.id = id;
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
