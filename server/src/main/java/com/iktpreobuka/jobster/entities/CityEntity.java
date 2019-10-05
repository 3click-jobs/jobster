package com.iktpreobuka.jobster.entities;

import java.util.ArrayList;
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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "cities")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class CityEntity {
	
	private static final Integer STATUS_INACTIVE = 0;
	private static final Integer STATUS_ACTIVE = 1;
	private static final Integer STATUS_ARCHIVED = -1;

	@JsonIgnore
	@OneToMany(mappedBy = "city", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH})
	private List<UserEntity> users = new ArrayList<>(); 
	
	@JsonIgnore
	@OneToMany(mappedBy = "city", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH})
	private List<JobOfferEntity> jobOffers = new ArrayList<>(); 
	
	@JsonIgnore
	@OneToMany(mappedBy = "city", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH})
	private List<JobSeekEntity> jobSeeks = new ArrayList<>(); 

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "region")
	@NotNull (message = "Country region must be provided.")
	private CountryRegionEntity region;
	
	@JsonIgnore
	@OneToMany(mappedBy = "toCity", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH}, orphanRemoval = true)
	private List<CityDistanceEntity> toDistances = new ArrayList<>();
	
	@JsonIgnore
	@OneToMany(mappedBy = "fromCity", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH}, orphanRemoval = true)
	private List<CityDistanceEntity> fromDistances = new ArrayList<>();

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	//@JsonView(Views.Parent.class)
	@Column(name="city_id")
	protected Integer id;
	//@JsonView(Views.Student.class)
	@Column(name="city_name")
	@Pattern(regexp = "^[A-Za-z]{2,}$", message="City name is not valid.")
	@NotNull (message = "City name must be provided.")
	protected String cityName;
	//@JsonView(Views.Student.class)
	@Column(name="longitude")
	@NotNull (message = "Longitude must be provided.")
	@Min(value=-180, message = "Longitude  must be {value} or higher!")
	@Max(value=180, message = "Longitude must be {value} or lower!")
	private Double longitude;
	//@JsonView(Views.Student.class)
	@Column(name="latitude")
	@NotNull (message = "Latitude must be provided.")
	@Min(value=-90, message = "Latitude  must be {value} or higher!")
	@Max(value=90, message = "Latitude must be {value} or lower!")
	private Double latitude;
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

	
	public CityEntity() {
		super();
	}
	
	public CityEntity(@NotNull(message = "Country region must be provided.") CountryRegionEntity region,
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "City name is not valid.") @NotNull(message = "City name must be provided.") String cityName,
			@NotNull(message = "Longitude must be provided.") @Min(value = -180, message = "Longitude  must be {value} or higher!") @Max(value = 180, message = "Longitude must be {value} or lower!") Double longitude,
			@NotNull(message = "Latitude must be provided.") @Min(value = -90, message = "Latitude  must be {value} or higher!") @Max(value = 90, message = "Latitude must be {value} or lower!") Double latitude,
			Integer createdById) {
		super();
		this.region = region;
		this.cityName = cityName;
		this.longitude = longitude;
		this.latitude = latitude;
		this.status = getStatusActive();
		this.createdById = createdById;
	}

	
	public List<UserEntity> getUsers() {
		return users;
	}

	public void setUsers(List<UserEntity> users) {
		this.users = users;
	}

	public CountryRegionEntity getRegion() {
		return region;
	}

	public void setRegion(CountryRegionEntity region) {
		this.region = region;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public List<CityDistanceEntity> getToDistances() {
		return toDistances;
	}

	public void setToDistances(List<CityDistanceEntity> toDistances) {
		this.toDistances = toDistances;
	}

	public List<CityDistanceEntity> getFromDistances() {
		return fromDistances;
	}

	public void setFromDistances(List<CityDistanceEntity> fromDistances) {
		this.fromDistances = fromDistances;
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
