package com.iktpreobuka.jobster.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.jobster.enumerations.EGender;
import com.iktpreobuka.jobster.security.Views;

@Entity
@Table(name = "persons")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class PersonEntity extends UserEntity {
	
	@JsonView(Views.User.class)
	@Column(name="first_name")
	@Pattern(regexp = "^[A-Za-z]{2,}$", message="First name is not valid.")
	@NotNull (message = "First name must be provided.")
	protected String firstName;
	@JsonView(Views.User.class)
	@Column(name="last_name")
	@Pattern(regexp = "^[A-Za-z]{2,}$", message="Last name is not valid.")
	@NotNull (message = "Last name must be provided.")
	protected String lastName;
	@JsonView(Views.Admin.class)
	@Column(name="gender")
	@Enumerated(EnumType.STRING)
	@NotNull (message = "Gender must be provided.")
	protected EGender gender;
	@JsonView(Views.User.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	//@Pattern(regexp = "^([0][1-9]|[1|2][0-9]|[3][0|1])[./-]([0][1-9]|[1][0-2])[./-]([1-2][0-9]{3})$", message="Enrollment date is not valid, must be in dd-MM-yyyy format.")
	@Column(name="birth_date")
	@NotNull (message = "Birth date must be provided.")
	protected Date birthDate;
	
	
	public PersonEntity() {
		super();
	}

	public PersonEntity(
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "First name is not valid.") @NotNull(message = "First name must be provided.") String firstName,
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "Last name is not valid.") @NotNull(message = "Last name must be provided.") String lastName,
			@NotNull(message = "Birth date must be provided.") Date birthDate) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthDate = birthDate;
	}


	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public EGender getGender() {
		return gender;
	}

	public void setGender(EGender gender) {
		this.gender = gender;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}


}
