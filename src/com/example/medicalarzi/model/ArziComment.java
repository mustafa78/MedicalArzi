/**
 * 
 */
package com.example.medicalarzi.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.lang3.builder.RecursiveToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * @author Mkanchwa
 *
 */
public class ArziComment implements Serializable {

	private static final long serialVersionUID = -4124728542357171692L;
	
	private Long arziId;
	
	private Long commentorItsNumber;
	
	private GregHijDate commentDate = new GregHijDate();
	
	private Timestamp commentTimestamp;
	
	private String commentText;
	
	private String createdBy;

	private Date createdDate;

	private String updatedBy;

	private Date updatedDate;

	/**
	 * Constructor
	 */
	public ArziComment() {

	}

	public Long getArziId() {
		return arziId;
	}

	public void setArziId(Long arziId) {
		this.arziId = arziId;
	}

	public Long getCommentorItsNumber() {
		return commentorItsNumber;
	}

	public void setCommentorItsNumber(Long commentorItsNumber) {
		this.commentorItsNumber = commentorItsNumber;
	}

	public GregHijDate getCommentDate() {
		return commentDate;
	}

	public void setCommentDate(GregHijDate commentDate) {
		this.commentDate = commentDate;
	}

	public Timestamp getCommentTimestamp() {
		return commentTimestamp;
	}

	public void setCommentTimestamp(Timestamp commentTimestamp) {
		this.commentTimestamp = commentTimestamp;
	}

	public String getCommentText() {
		return commentText;
	}

	public void setCommentText(String commentText) {
		this.commentText = commentText;
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
	
	@Override
	public String toString() {
		return (new ReflectionToStringBuilder(this,
				RecursiveToStringStyle.MULTI_LINE_STYLE)).toString();
	}	
}
