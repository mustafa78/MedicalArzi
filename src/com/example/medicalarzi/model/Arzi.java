package com.example.medicalarzi.model;

import java.io.Serializable;
import java.util.Date;

public class Arzi extends ArziHeader implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -3071236194000826029L;

	private Long reviewerItsNumber;

	private GregHijDate reviewDate;

	private GregHijDate statusChangeDate;

	public Arzi() {
		super();
	}

	public Long getReviewerItsNumber() {
		return reviewerItsNumber;
	}

	public void setReviewerItsNumber(Long reviewerItsNumber) {
		this.reviewerItsNumber = reviewerItsNumber;
	}

	public GregHijDate getReviewDate() {
		return reviewDate;
	}

	public void setReviewDate(GregHijDate reviewDate) {
		this.reviewDate = reviewDate;
	}

	public GregHijDate getStatusChangeDate() {
		return statusChangeDate;
	}

	public void setStatusChangeDate(GregHijDate statusChangeDate) {
		this.statusChangeDate = statusChangeDate;
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
