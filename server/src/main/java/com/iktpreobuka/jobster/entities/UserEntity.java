
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
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import javax.validation.constraints.Size;

import org.hibernate.annotations.DiscriminatorOptions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.jobster.security.Views;

@Entity
@Table (name = "users"/*, uniqueConstraints=@UniqueConstraint(columnNames= {"phone", "e_mail"})*/)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorOptions(force = true)
public class UserEntity {
	
	private static final Integer STATUS_INACTIVE = 0;
	private static final Integer STATUS_ACTIVE = 1;
	private static final Integer STATUS_ARCHIVED = -1;
    
	//@JsonView(Views.Admin.class)
	/*@JsonIgnore
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH})
	private List<UserAccountEntity> accounts = new ArrayList<>();*/
	
	//@JsonView(Views.Admin.class)
	/*@OneToOne
	@JoinColumn(name="user_account")
    protected UserAccountEntity userAccount;*/
	
	@JsonView(Views.User.class)
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "city")
	@NotNull (message = "City must be provided.")
	private CityEntity city;
	
	@JsonIgnore
	@OneToMany(mappedBy = "employer", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH})
	private List<JobOfferEntity> jobOffers = new ArrayList<>(); 
	
	@JsonIgnore
	@OneToMany(mappedBy = "employee", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH})
	private List<JobSeekEntity> jobSeeks = new ArrayList<>(); 

	@JsonIgnore
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH})
	private List<RejectEntity> rejections = new ArrayList<>(); 

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonView(Views.User.class)
	@Column(name="user_id")
	protected Integer id;
	@JsonView(Views.User.class)
	@Column(name="mobile_phone")
	@NotNull (message = "Phone number must be provided.")
	@Pattern(regexp = "^([\\(]{0,1}[\\+]{0,1}[\\(]{0,1}([3][8][1]){0,1}[\\)]{0,1}[- \\.\\/]{0,1}[\\(]{0,1}[0]{0,1}[\\)]{0,1}[6]{1,1}([0-6]|[9]){1,1}[\\)]{0,1}[- \\.\\/]{0,1}(([0-9]{6,7})|([0-9]{2,3}[- \\.\\/]{0,1}[0-9]{2,4}[- \\.\\/]{0,1}[0-9]{0,3})))$", message="Mobile phone number is not valid.")
	private String mobilePhone;
	@JsonView(Views.User.class)
	@Column(name="e_mail", unique=true, length=50)
	@Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message="Email is not valid.")
	@NotNull (message = "E-mail must be provided.")
	private String email;
	@JsonView(Views.User.class)
	@Column(name="about")
	@Size(max=255, message = "About must be maximum {max} characters long.")
	//@NotNull (message = "About must be provided.")
	private String about;
	@JsonView(Views.User.class)
	@Max(5)
    @Min(0)
    @Column(name = "rating", nullable = false)
	private Double rating;
	@JsonView(Views.User.class)
    @Min(0)
    @Column(name = "number_of_ratings", nullable = false)
	private Integer numberOfRatings;
	@JsonView(Views.User.class)
	@Max(1)
    @Min(-1)
    @Column(name = "status", nullable = false)
	private Integer status;
	@JsonView(Views.Admin.class)
    @Column(name = "created_by"/*, updatable = false*/)
	private Integer createdById;
    @JsonView(Views.Admin.class)
    @Column(name = "updated_by")
    private Integer updatedById;
	@JsonIgnore
	@Version
	private Integer version;
	
	
	public UserEntity() { 
		super();
	}
	
	public UserEntity(@NotNull(message = "City must be provided.") CityEntity city,
			@NotNull(message = "Phone number must be provided.") @Pattern(regexp = "^([\\(]{0,1}[\\+]{0,1}[\\(]{0,1}([3][8][1]){0,1}[\\)]{0,1}[- \\.\\/]{0,1}[\\(]{0,1}[0]{0,1}[\\)]{0,1}[6]{1,1}([0-6]|[9]){1,1}[\\)]{0,1}[- \\.\\/]{0,1}(([0-9]{6,7})|([0-9]{2,3}[- \\.\\/]{0,1}[0-9]{2,4}[- \\.\\/]{0,1}[0-9]{0,3})))$", message = "Mobile phone number is not valid.") String mobilePhoneNumber,
			@Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = "Email is not valid.") @NotNull(message = "E-mail must be provided.") String email,
			@Size(max=255, message = "About must be maximum {max} characters long.") String about,
			Integer createdById) {
		super();
		this.city = city;
		this.mobilePhone = mobilePhoneNumber;
		this.email = email;
		this.about = about;
		this.status = getStatusActive();
		this.rating = 0.0;
		this.numberOfRatings = 0;
		this.createdById = createdById;
		}

	public UserEntity(@NotNull(message = "City must be provided.") CityEntity city,
			@NotNull(message = "Phone number must be provided.") @Pattern(regexp = "^([\\(]{0,1}[\\+]{0,1}[\\(]{0,1}([3][8][1]){0,1}[\\)]{0,1}[- \\.\\/]{0,1}[\\(]{0,1}[0]{0,1}[\\)]{0,1}[6]{1,1}([0-6]|[9]){1,1}[\\)]{0,1}[- \\.\\/]{0,1}(([0-9]{6,7})|([0-9]{2,3}[- \\.\\/]{0,1}[0-9]{2,4}[- \\.\\/]{0,1}[0-9]{0,3})))$", message = "Mobile phone number is not valid.") String mobilePhoneNumber,
			@Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = "Email is not valid.") @NotNull(message = "E-mail must be provided.") String email,
			@Size(max=255, message = "About must be maximum {max} characters long.") String about) {
		super();
		this.city = city;
		this.mobilePhone = mobilePhoneNumber;
		this.email = email;
		this.about = about;
		this.status = getStatusActive();
		this.rating = 0.0;
		this.numberOfRatings = 0;
		}

/*	public UserAccountEntity getAccount() {
		return userAccount;
	}

	public void setAccount(UserAccountEntity userAccount) {
		this.userAccount = userAccount;
	}*/


	public CityEntity getCity() {
		return city;
	}

	protected void setStatus(Integer status) {
		this.status = status;
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

	public List<JobOfferEntity> getJobOffers() {
		return jobOffers;
	}

	public void setJobOffers(List<JobOfferEntity> jobOffers) {
		this.jobOffers = jobOffers;
	}

	public List<JobSeekEntity> getJobSeeks() {
		return jobSeeks;
	}

	public void setJobSeeks(List<JobSeekEntity> jobSeeks) {
		this.jobSeeks = jobSeeks;
	}

	public void setCity(CityEntity city) {
		this.city = city;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
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
	
	protected static Integer getStatusInactive() {
		return STATUS_INACTIVE;
	}

	protected static Integer getStatusActive() {
		return STATUS_ACTIVE;
	}

	protected static Integer getStatusArchived() {
		return STATUS_ARCHIVED;
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

	
}