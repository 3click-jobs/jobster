package com.iktpreobuka.jobster.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "persons")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class PersonEntity extends UserEntity {
	
	//@JsonView(Views.Student.class)
	@Column(name="first_name")
	@Pattern(regexp = "^[A-Za-z]{2,}$", message="First name is not valid.")
	@NotNull (message = "First name must be provided.")
	protected String firstName;
	//@JsonView(Views.Student.class)
	@Column(name="last_name")
	@Pattern(regexp = "^[A-Za-z]{2,}$", message="Last name is not valid.")
	@NotNull (message = "Last name must be provided.")
	protected String lastName;
	
	
	public PersonEntity() {
		super();
	}


	public PersonEntity(
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "First name is not valid.") @NotNull(message = "First name must be provided.") String firstName,
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "Last name is not valid.") @NotNull(message = "Last name must be provided.") String lastName) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
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


}
