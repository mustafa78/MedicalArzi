/**
 * 
 */
package com.example.medicalarzi.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author mkanchwa
 *
 */
public class GregHijDate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8538792575554680291L;
	
	private Long gregorianDateId;
	
	private Date gregorianCalDate;
	
	private Integer gregorianCalYear;
	
	private Integer gregorianCalMnthNbr;
	
	private String gregorianCalMnthName;
	
	private Long hijriDateId;
	
	private String hijriCalDate;
	
	private Integer hijriCalYear;
	
	private Integer hijriCalMnthNbr;
	
	private String hijriCalMnthName;
	
	private String miqaatMnthDay;
	
	private String createdBy;

	private Date createdDate;

	private String updatedBy;

	private Date updatedDate;
	

	/**
	 * 
	 */
	public GregHijDate() {
		// TODO Auto-generated constructor stub
	}


	public Long getGregorianDateId() {
		return gregorianDateId;
	}


	public void setGregorianDateId(Long gregorianDateId) {
		this.gregorianDateId = gregorianDateId;
	}


	public Date getGregorianCalDate() {
		return gregorianCalDate;
	}


	public void setGregorianCalDate(Date gregorianCalDate) {
		this.gregorianCalDate = gregorianCalDate;
	}


	public Integer getGregorianCalYear() {
		return gregorianCalYear;
	}


	public void setGregorianCalYear(Integer gregorianCalYear) {
		this.gregorianCalYear = gregorianCalYear;
	}


	public Integer getGregorianCalMnthNbr() {
		return gregorianCalMnthNbr;
	}


	public void setGregorianCalMnthNbr(Integer gregorianCalMnthNbr) {
		this.gregorianCalMnthNbr = gregorianCalMnthNbr;
	}


	public String getGregorianCalMnthName() {
		return gregorianCalMnthName;
	}


	public void setGregorianCalMnthName(String gregorianCalMnthName) {
		this.gregorianCalMnthName = gregorianCalMnthName;
	}


	public Long getHijriDateId() {
		return hijriDateId;
	}


	public void setHijriDateId(Long hijriDateId) {
		this.hijriDateId = hijriDateId;
	}


	public String getHijriCalDate() {
		return hijriCalDate;
	}


	public void setHijriCalDate(String hijriCalDate) {
		this.hijriCalDate = hijriCalDate;
	}


	public Integer getHijriCalYear() {
		return hijriCalYear;
	}


	public void setHijriCalYear(Integer hijriCalYear) {
		this.hijriCalYear = hijriCalYear;
	}


	public Integer getHijriCalMnthNbr() {
		return hijriCalMnthNbr;
	}


	public void setHijriCalMnthNbr(Integer hijriCalMnthNbr) {
		this.hijriCalMnthNbr = hijriCalMnthNbr;
	}


	public String getHijriCalMnthName() {
		return hijriCalMnthName;
	}


	public void setHijriCalMnthName(String hijriCalMnthName) {
		this.hijriCalMnthName = hijriCalMnthName;
	}


	public String getMiqaatMnthDay() {
		return miqaatMnthDay;
	}


	public void setMiqaatMnthDay(String miqaatMnthDay) {
		this.miqaatMnthDay = miqaatMnthDay;
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

}
