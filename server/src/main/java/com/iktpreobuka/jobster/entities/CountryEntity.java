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
@Table(name = "countries")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class CountryEntity {

	private static final Integer STATUS_INACTIVE = 0;
	private static final Integer STATUS_ACTIVE = 1;
	private static final Integer STATUS_ARCHIVED = -1;

	@JsonIgnore
	@OneToMany(mappedBy = "country", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH})
	private List<CountryRegionEntity> regions = new ArrayList<>();

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	//@JsonView(Views.Parent.class)
	@Column(name="coutry_id")
	protected Integer id;
	
	//@JsonView(Views.Student.class)
	@Column(name="coutry_name", unique=true)
	@Pattern(regexp = "^[A-Za-z\\s]{2,}$", message="Country name is not valid.")
	@NotNull (message = "Country name must be provided.")
	protected String countryName;
	
	//@JsonView(Views.Student.class)
	@Column(name="iso2", unique=true)
	@Pattern(regexp = "^[A-Za-z]{2,3}$", message="ISO2 code is not valid.")
	@NotNull (message = "ISO2 code must be provided.")
	protected String iso2Code;
	
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

	
	public CountryEntity() {
		super();
	}
	
	public CountryEntity(
			@Pattern(regexp = "^[A-Za-z\\s]{2,}$", message = "Country name is not valid.") @NotNull(message = "Country name must be provided.") String countryName,
			@Pattern(regexp = "^[A-Za-z]{2,3}$", message = "ISO2 code is not valid.") @NotNull(message = "ISO2 code must be provided.") String iso2Code,
			Integer createdById) {
		super();
		this.countryName = countryName;
		this.iso2Code = iso2Code;
		this.status = getStatusActive();
		this.createdById = createdById;
	}


	public List<CountryRegionEntity> getRegions() {
		return regions;
	}

	public void setRegions(List<CountryRegionEntity> regions) {
		this.regions = regions;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
