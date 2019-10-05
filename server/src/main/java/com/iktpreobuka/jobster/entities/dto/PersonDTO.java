package com.iktpreobuka.jobster.entities.dto;

import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.jobster.security.Views;

public class PersonDTO {

	@JsonView(Views.User.class)
	@Pattern(regexp = "^[A-Za-z]{2,}$", message="First name is not valid, can contain only letters and minimum is 2 letter.")
	private String firstName;
	@JsonView(Views.User.class)
	@Pattern(regexp = "^[A-Za-z]{2,}$", message="Last name is not valid, can contain only letters and minimum is 2 letter.")
	private String lastName;
	@JsonView(Views.Admin.class)
	@Pattern(regexp="^(GENDER_MALE|GENDER_FEMALE)$",message="Gender is not valid, must be GENDER_MALE or GENDER_FEMALE")
	private String gender;
	@JsonView(Views.User.class)
	//@Pattern(regexp = "^([0][1-9]|[1|2][0-9]|[3][0|1])[./-]([0][1-9]|[1][0-2])[./-]([1-2][0-9]{3})$", message="Enrollment date is not valid, must be in dd-MM-yyyy format.")
	private String birthDate;
	@JsonView(Views.User.class)
	@Pattern(regexp = "^([\\(]{0,1}[\\+]{0,1}[\\(]{0,1}([3][8][1]){0,1}[\\)]{0,1}[- \\.\\/]{0,1}[\\(]{0,1}[0]{0,1}[\\)]{0,1}[6]{1,1}([0-6]|[9]){1,1}[\\)]{0,1}[- \\.\\/]{0,1}(([0-9]{6,7})|([0-9]{2,3}[- \\.\\/]{0,1}[0-9]{2,4}[- \\.\\/]{0,1}[0-9]{0,3})))$", message="Mobile phone number is not valid.")
	private String mobilePhone;
	@JsonView(Views.User.class)
	@Size(max=50, message = "E-mail must be maximum {max} characters long.")
	@Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message="Email is not valid.")
	private String email;
	@JsonView(Views.User.class)
	@Pattern(regexp = "^[0-9]+$", message="City Id is not valid, can contain only numbers and minimum 1 number.")
	private String city;
	@JsonView(Views.User.class)
	private String detailsLink;
	@JsonView(Views.User.class)
	@Max(5)
    @Min(0)
	private Double rating;
	@JsonView(Views.User.class)
    @Min(0)
	private Integer numberOfRatings;
	@JsonView(Views.User.class)
	//@Pattern(regexp = "^[0-9]+$", message="Subjects Id is not valid, can contain only numbers and minimum 1 number.")
	private List<String> jobOffers;
	@JsonView(Views.User.class)
	//@Pattern(regexp = "^[0-9]+$", message="Subjects Id is not valid, can contain only numbers and minimum 1 number.")
	private List<String> jobSeeks;

	@JsonView(Views.Admin.class)
	@Size(min=5, max=20, message = "Username must be between {min} and {max} characters long.")
	private String username;
	@JsonView(Views.Admin.class)
	@Pattern(regexp="^(ROLE_ADMIN|ROLE_TEACHER|ROLE_PARENT|ROLE_STUDENT)$",message="Role is not valid, must be ROLE_ADMIN, ROLE_TEACHER, ROLE_PARENT or ROLE_STUDENT")
	private String accessRole;
	@Size(min=5, message = "Password must be {min} characters long or higher.")
	@Pattern(regexp = "^[A-Za-z0-9]*$", message="Password is not valid, must contin only letters and numbers.")
	private String password;
	@Size(min=5, message = "Password must be {min} characters long or higher.")
	@Pattern(regexp = "^[A-Za-z0-9]*$", message="Password is not valid, must contin only letters and numbers.")
	private String confirmedPassword;
	
	
	public PersonDTO() {
		super();
	}
	
	public PersonDTO(
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "First name is not valid, can contain only letters and minimum is 2 letter.") String firstName,
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "Last name is not valid, can contain only letters and minimum is 2 letter.") String lastName,
			String birthDate,
			@Pattern(regexp = "^([\\(]{0,1}[\\+]{0,1}[\\(]{0,1}([3][8][1]){0,1}[\\)]{0,1}[- \\.\\/]{0,1}[\\(]{0,1}[0]{0,1}[\\)]{0,1}[6]{1,1}([0-6]|[9]){1,1}[\\)]{0,1}[- \\.\\/]{0,1}(([0-9]{6,7})|([0-9]{2,3}[- \\.\\/]{0,1}[0-9]{2,4}[- \\.\\/]{0,1}[0-9]{0,3})))$", message = "Mobile phone number is not valid.") String mobilePhone,
			@Size(max = 50, message = "E-mail must be maximum {max} characters long.") @Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = "Email is not valid.") String email,
			@Pattern(regexp = "^[0-9]+$", message = "City Id is not valid, can contain only numbers and minimum 1 number.") String city,
			String detailsLink, @Max(5) @Min(0) Double rating, @Min(0) Integer numberOfRatings, List<String> jobOffers,
			List<String> jobSeeks,
			@Size(min = 5, max = 20, message = "Username must be between {min} and {max} characters long.") String username,
			@Pattern(regexp = "^(ROLE_ADMIN|ROLE_TEACHER|ROLE_PARENT|ROLE_STUDENT)$", message = "Role is not valid, must be ROLE_ADMIN, ROLE_TEACHER, ROLE_PARENT or ROLE_STUDENT") String accessRole,
			@Size(min = 5, message = "Password must be {min} characters long or higher.") @Pattern(regexp = "^[A-Za-z0-9]*$", message = "Password is not valid, must contin only letters and numbers.") String password,
			@Size(min = 5, message = "Password must be {min} characters long or higher.") @Pattern(regexp = "^[A-Za-z0-9]*$", message = "Password is not valid, must contin only letters and numbers.") String confirmedPassword) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthDate = birthDate;
		this.mobilePhone = mobilePhone;
		this.email = email;
		this.city = city;
		this.detailsLink = detailsLink;
		this.rating = rating;
		this.numberOfRatings = numberOfRatings;
		this.jobOffers = jobOffers;
		this.jobSeeks = jobSeeks;
		this.username = username;
		this.accessRole = accessRole;
		this.password = password;
		this.confirmedPassword = confirmedPassword;
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
	
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBirthDate() {
		return birthDate;
	}
	
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	
	public String getMobilePhone() {
		return mobilePhone;
	}
	
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getDetailsLink() {
		return detailsLink;
	}
	
	public void setDetailsLink(String detailsLink) {
		this.detailsLink = detailsLink;
	}
	
	public Double getRating() {
		return rating;
	}
	
	public void setRating(Double rating) {
		this.rating = rating;
	}
	
	public Integer getNumberOfRatings() {
		return numberOfRatings;
	}
	
	public void setNumberOfRatings(Integer numberOfRatings) {
		this.numberOfRatings = numberOfRatings;
	}
	
	public List<String> getJobOffers() {
		return jobOffers;
	}
	
	public void setJobOffers(List<String> jobOffers) {
		this.jobOffers = jobOffers;
	}
	
	public List<String> getJobSeeks() {
		return jobSeeks;
	}
	
	public void setJobSeeks(List<String> jobSeeks) {
		this.jobSeeks = jobSeeks;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getAccessRole() {
		return accessRole;
	}
	
	public void setAccessRole(String accessRole) {
		this.accessRole = accessRole;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getConfirmedPassword() {
		return confirmedPassword;
	}
	
	public void setConfirmedPassword(String confirmedPassword) {
		this.confirmedPassword = confirmedPassword;
	}

}
