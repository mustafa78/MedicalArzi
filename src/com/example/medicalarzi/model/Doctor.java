/**
 * 
 */
package com.example.medicalarzi.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author mkanchwa
 *
 */
public class Doctor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4455720315025256808L;
	
	@NotNull(message="Please enter the ITS number.")
	private Long itsNumber;

	private Lookup drTitle; //title in Daawat

	@Size(min=1, max=30)
	@NotEmpty(message="Please enter your first name.")
	private String firstName;

	private Lookup drMiddleNmTitle; //title in Daawat

	@Size(min=0, max=30)
	private String middleName;

	@Size(min=1, max=30)
	@NotEmpty(message="Please enter your last name/ surname.")
	private String lastName;

	private String genderInd;

	@Past(message="The DOB must be in the past.")
	private Date dob;

	private String phoneNum;

	private String faxNum;

	private String cellNum;

	@NotEmpty(message="Please enter a email address.")
	@Email(message="Please enter a valid email address.")
	private String emailAddress;

	private String homeAddress1;

	private String homeAddress2;

	private String city;

	private String state;

	private String zip;

	private String jamaatName;

	private Boolean activeInd;

	private String workPhoneNum;
	
	private String workAddress1;
	
	private String workAddress2;
	
	private String workCity;
	
	private String workState;
	
	private String workZip;

	private String createdBy;

	private Date createdDate;

	private String updatedBy;

	private Date updatedDate;

	private MedicalSpeciality medicalSpeciality;

	private List<SecurityRole> roles;	

	/**
	 * 
	 */
	public Doctor() {
		// TODO Auto-generated constructor stub
	}

	public Long getItsNumber() {
		return itsNumber;
	}

	public void setItsNumber(Long itsNumber) {
		this.itsNumber = itsNumber;
	}

	public Lookup getDrTitle() {
		return drTitle;
	}

	public void setDrTitle(Lookup drTitle) {
		this.drTitle = drTitle;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public Lookup getDrMiddleNmTitle() {
		return drMiddleNmTitle;
	}

	public void setDrMiddleNmTitle(Lookup drMiddleNmTitle) {
		this.drMiddleNmTitle = drMiddleNmTitle;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getGenderInd() {
		return genderInd;
	}

	public void setGenderInd(String genderInd) {
		this.genderInd = genderInd;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getFaxNum() {
		return faxNum;
	}

	public void setFaxNum(String faxNum) {
		this.faxNum = faxNum;
	}

	public String getCellNum() {
		return cellNum;
	}

	public void setCellNum(String cellNum) {
		this.cellNum = cellNum;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getHomeAddress1() {
		return homeAddress1;
	}

	public void setHomeAddress1(String homeAddress1) {
		this.homeAddress1 = homeAddress1;
	}

	public String getHomeAddress2() {
		return homeAddress2;
	}

	public void setHomeAddress2(String homeAddress2) {
		this.homeAddress2 = homeAddress2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getJamaatName() {
		return jamaatName;
	}

	public void setJamaatName(String jamaatName) {
		this.jamaatName = jamaatName;
	}

	public Boolean getActiveInd() {
		return activeInd;
	}

	public void setActiveInd(Boolean activeInd) {
		this.activeInd = activeInd;
	}

	public String getWorkPhoneNum() {
		return workPhoneNum;
	}

	public void setWorkPhoneNum(String workPhoneNum) {
		this.workPhoneNum = workPhoneNum;
	}

	public String getWorkAddress1() {
		return workAddress1;
	}

	public void setWorkAddress1(String workAddress1) {
		this.workAddress1 = workAddress1;
	}

	public String getWorkAddress2() {
		return workAddress2;
	}

	public void setWorkAddress2(String workAddress2) {
		this.workAddress2 = workAddress2;
	}

	public String getWorkCity() {
		return workCity;
	}

	public void setWorkCity(String workCity) {
		this.workCity = workCity;
	}

	public String getWorkState() {
		return workState;
	}

	public void setWorkState(String workState) {
		this.workState = workState;
	}

	public String getWorkZip() {
		return workZip;
	}

	public void setWorkZip(String workZip) {
		this.workZip = workZip;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public MedicalSpeciality getMedicalSpeciality() {
		return medicalSpeciality;
	}

	public void setMedicalSpeciality(MedicalSpeciality medicalSpeciality) {
		this.medicalSpeciality = medicalSpeciality;
	}

	public List<SecurityRole> getRoles() {
		return roles;
	}

	public void setRoles(List<SecurityRole> roles) {
		this.roles = roles;
	}

}
