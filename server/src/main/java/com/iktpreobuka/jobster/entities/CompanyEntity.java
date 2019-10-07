package com.iktpreobuka.jobster.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.jobster.security.Views;

@Entity
@Table(name = "companies")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class CompanyEntity extends UserEntity {
	

	@JsonView(Views.User.class)
	@Column(name="company_name")
	@Pattern(regexp = "^[A-Za-z]{2,}$", message="Company name is not valid.")
	@NotNull (message = "Company name must be provided.")
	protected String companyName;
	@JsonView(Views.User.class)
	@Column(name="company_id", unique=true, length=13, nullable=false)
	@Pattern(regexp = "^[0-9]{13,13}$", message="Company ID is not valid, can contain only numbers and must be exactly 13 numbers long.")
	@NotNull (message = "Company ID must be provided.")
	protected String companyId;
	
	
	public CompanyEntity() {
		super();
	}
	public CompanyEntity(String companyName, String companyId) {
		super();
		this.companyName = companyName;
		this.companyId = companyId;
	}
	
	
	public String getCompanyName() {
		return companyName;
	}
	
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	public String getCompanyId() {
		return companyId;
	}
	
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

}