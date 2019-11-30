
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
	@Pattern(regexp = "^[_A-Za-z0-9-]{2,}$", message="Company name is not valid.")
	@NotNull (message = "Company name must be provided.")
	protected String companyName;
	@JsonView(Views.User.class)
	@Column(name="company_registration_number", unique=true, length=14, nullable=false)
	@Pattern(regexp = "^[0-9]{14,14}$", message="Company registration number is not valid, can contain only numbers and must be exactly 14 numbers long.")
	@NotNull (message = "Company registration number must be provided.")
	protected String companyRegistrationNumber;
	
	
	public CompanyEntity() {
		super();
	}
	
	public CompanyEntity(
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "Company name is not valid.") @NotNull(message = "Company name must be provided.") String companyName,
			@Pattern(regexp = "^[0-9]{13,13}$", message = "Company ID is not valid, can contain only numbers and must be exactly 13 numbers long.") @NotNull(message = "Company ID must be provided.") String companyId) {
		super();
		this.companyName = companyName;
		this.companyRegistrationNumber = companyId;
	}

	public CompanyEntity(@NotNull(message = "City must be provided.") CityEntity city,
			@NotNull(message = "Phone number must be provided.") @Pattern(regexp = "^([\\(]{0,1}[\\+]{0,1}[\\(]{0,1}([3][8][1]){0,1}[\\)]{0,1}[- \\.\\/]{0,1}[\\(]{0,1}[0]{0,1}[\\)]{0,1}[6]{1,1}([0-6]|[9]){1,1}[\\)]{0,1}[- \\.\\/]{0,1}(([0-9]{6,7})|([0-9]{2,3}[- \\.\\/]{0,1}[0-9]{2,4}[- \\.\\/]{0,1}[0-9]{0,3})))$", message = "Mobile phone number is not valid.") String mobilePhoneNumber,
			@Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = "Email is not valid.") @NotNull(message = "E-mail must be provided.") String email,
			@NotNull(message = "Details must be provided.") String about,
			Integer createdById,
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "Company name is not valid.") @NotNull(message = "Company name must be provided.") String companyName,
			@Pattern(regexp = "^[0-9]{13,13}$", message = "Company ID is not valid, can contain only numbers and must be exactly 13 numbers long.") @NotNull(message = "Company ID must be provided.") String companyId) {
		super();
		super.setCity(city);
		super.setMobilePhone(mobilePhoneNumber);
		super.setEmail(email);
		super.setAbout(about);
		super.setStatus(getStatusActive());
		super.setRating(0.0);
		super.setNumberOfRatings(0);
		super.setCreatedById(createdById);
		this.companyName = companyName;
		this.companyRegistrationNumber = companyId;
	}

	public CompanyEntity(@NotNull(message = "City must be provided.") CityEntity city,
			@NotNull(message = "Phone number must be provided.") @Pattern(regexp = "^([\\(]{0,1}[\\+]{0,1}[\\(]{0,1}([3][8][1]){0,1}[\\)]{0,1}[- \\.\\/]{0,1}[\\(]{0,1}[0]{0,1}[\\)]{0,1}[6]{1,1}([0-6]|[9]){1,1}[\\)]{0,1}[- \\.\\/]{0,1}(([0-9]{6,7})|([0-9]{2,3}[- \\.\\/]{0,1}[0-9]{2,4}[- \\.\\/]{0,1}[0-9]{0,3})))$", message = "Mobile phone number is not valid.") String mobilePhoneNumber,
			@Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = "Email is not valid.") @NotNull(message = "E-mail must be provided.") String email,
			@NotNull(message = "Details must be provided.") String about,
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "Company name is not valid.") @NotNull(message = "Company name must be provided.") String companyName,
			@Pattern(regexp = "^[0-9]{13,13}$", message = "Company ID is not valid, can contain only numbers and must be exactly 13 numbers long.") @NotNull(message = "Company ID must be provided.") String companyId) {
		super();
		super.setCity(city);
		super.setMobilePhone(mobilePhoneNumber);
		super.setEmail(email);
		super.setAbout(about);
		super.setStatus(getStatusActive());
		super.setRating(0.0);
		super.setNumberOfRatings(0);
		this.companyName = companyName;
		this.companyRegistrationNumber = companyId;
	}

	
	public String getCompanyName() {
		return companyName;
	}
	
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	public String getCompanyRegistrationNumber() {
		return companyRegistrationNumber;
	}
	
	public void setCompanyRegistrationNumber(String companyId) {
		this.companyRegistrationNumber = companyId;
	}

}
