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
public class Patient implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -241043149012578605L;

	@NotNull(message="Please enter the ITS number.")
	private Long itsNumber;

	private Lookup ptntTitle;

	@Size(min=1, max=30)
	@NotEmpty(message="Please enter your first name.")
	private String firstName;

	private Lookup ptntMiddleNmTitle;

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

	@NotEmpty(message="Please enter the password.")
	private String password;

	private String createdBy;

	private Date createdDate;

	private String updatedBy;

	private Date updatedDate;

	private String doctorsRecommendations;

	private String additionalComments;

	private Condition condition;

	private Procedure procedure;

	private MedicalHistory medicalHistory;

	private List<SecurityRole> roles;

	//Only for UI purpose
	private String passString;

	/**
	 *
	 */
	public Patient() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the itsNumber
	 */
	public Long getItsNumber() {
		return itsNumber;
	}

	/**
	 * @param itsNumber
	 *            the itsNumber to set
	 */
	public void setItsNumber(Long itsNumber) {
		this.itsNumber = itsNumber;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the middleName
	 */
	public String getMiddleName() {
		return middleName;
	}

	/**
	 * @param middleName
	 *            the middleName to set
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the genderInd
	 */
	public String getGenderInd() {
		return genderInd;
	}

	/**
	 * @param genderInd
	 *            the genderInd to set
	 */
	public void setGenderInd(String genderInd) {
		this.genderInd = genderInd;
	}

	/**
	 * @return the dob
	 */
	public Date getDob() {
		return dob;
	}

	/**
	 * @param dob
	 *            the dob to set
	 */
	public void setDob(Date dob) {
		this.dob = dob;
	}

	/**
	 * @return the phoneNum
	 */
	public String getPhoneNum() {
		return phoneNum;
	}

	/**
	 * @param phoneNum
	 *            the phoneNum to set
	 */
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	/**
	 * @return the faxNum
	 */
	public String getFaxNum() {
		return faxNum;
	}

	/**
	 * @param faxNum
	 *            the faxNum to set
	 */
	public void setFaxNum(String faxNum) {
		this.faxNum = faxNum;
	}

	/**
	 * @return the cellNum
	 */
	public String getCellNum() {
		return cellNum;
	}

	/**
	 * @param cellNum
	 *            the cellNum to set
	 */
	public void setCellNum(String cellNum) {
		this.cellNum = cellNum;
	}

	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * @param emailAddress
	 *            the emailAddress to set
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	/**
	 * @return the homeAddress1
	 */
	public String getHomeAddress1() {
		return homeAddress1;
	}

	/**
	 * @param homeAddress1
	 *            the homeAddress1 to set
	 */
	public void setHomeAddress1(String homeAddress1) {
		this.homeAddress1 = homeAddress1;
	}

	/**
	 * @return the homeAddress2
	 */
	public String getHomeAddress2() {
		return homeAddress2;
	}

	/**
	 * @param homeAddress2
	 *            the homeAddress2 to set
	 */
	public void setHomeAddress2(String homeAddress2) {
		this.homeAddress2 = homeAddress2;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the zip
	 */
	public String getZip() {
		return zip;
	}

	/**
	 * @param zip
	 *            the zip to set
	 */
	public void setZip(String zip) {
		this.zip = zip;
	}

	/**
	 * @return the doctorsRecommendations
	 */
	public String getDoctorsRecommendations() {
		return doctorsRecommendations;
	}

	/**
	 * @param doctorsRecommendations
	 *            the doctorsRecommendations to set
	 */
	public void setDoctorsRecommendations(String doctorsRecommendations) {
		this.doctorsRecommendations = doctorsRecommendations;
	}

	/**
	 * @return the additionalComments
	 */
	public String getAdditionalComments() {
		return additionalComments;
	}

	/**
	 * @param additionalComments
	 *            the additionalComments to set
	 */
	public void setAdditionalComments(String additionalComments) {
		this.additionalComments = additionalComments;
	}

	/**
	 * @return the condition
	 */
	public Condition getCondition() {
		return condition;
	}

	/**
	 * @param condition
	 *            the condition to set
	 */
	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	/**
	 * @return the procedure
	 */
	public Procedure getProcedure() {
		return procedure;
	}

	/**
	 * @param procedure
	 *            the procedure to set
	 */
	public void setProcedure(Procedure procedure) {
		this.procedure = procedure;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public MedicalHistory getMedicalHistory() {
		return medicalHistory;
	}

	public void setMedicalHistory(MedicalHistory medicalHistory) {
		this.medicalHistory = medicalHistory;
	}

	/**
	 * @return the roles
	 */
	public List<SecurityRole> getRoles() {
		return roles;
	}

	/**
	 * @param roles the roles to set
	 */
	public void setRoles(List<SecurityRole> roles) {
		this.roles = roles;
	}

	/**
	 * @return the ptntTitle
	 */
	public Lookup getPtntTitle() {
		return ptntTitle;
	}

	/**
	 * @param ptntTitle the ptntTitle to set
	 */
	public void setPtntTitle(Lookup ptntTitle) {
		this.ptntTitle = ptntTitle;
	}

	/**
	 * @return the ptntMiddleNmTitle
	 */
	public Lookup getPtntMiddleNmTitle() {
		return ptntMiddleNmTitle;
	}

	/**
	 * @param ptntMiddleNmTitle the ptntMiddleNmTitle to set
	 */
	public void setPtntMiddleNmTitle(Lookup ptntMiddleNmTitle) {
		this.ptntMiddleNmTitle = ptntMiddleNmTitle;
	}

	public String getPassString() {
		return passString;
	}

	public void setPassString(String passString) {
		this.passString = passString;
	}

}
