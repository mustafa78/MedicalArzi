/**
 *
 */
package com.example.medicalarzi.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.RecursiveToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * @author mkanchwa
 *
 */
public class BodyPart implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 4094424170718884351L;

	private Long bodyPartId;

	private String bodyPartPrefixCd;

	private String bodyPartName;

	private Boolean activeInd;

	private String createdBy;

	private Date createdDate;

	private String updatedBy;

	private Date updatedDate;

	/**
	 *
	 */
	public BodyPart() {
		// TODO Auto-generated constructor stub
	}

	public Long getBodyPartId() {
		return bodyPartId;
	}

	public void setBodyPartId(Long bodyPartId) {
		this.bodyPartId = bodyPartId;
	}

	public String getBodyPartName() {
		return bodyPartName;
	}

	public void setBodyPartName(String bodyPartName) {
		this.bodyPartName = bodyPartName;
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

	public String getBodyPartPrefixCd() {
		return bodyPartPrefixCd;
	}

	public void setBodyPartPrefixCd(String bodyPartPrefixCd) {
		this.bodyPartPrefixCd = bodyPartPrefixCd;
	}

	public Boolean getActiveInd() {
		return activeInd;
	}

	public void setActiveInd(Boolean activeInd) {
		this.activeInd = activeInd;
	}

	@Override
	public String toString() {
		return (new ReflectionToStringBuilder(this,
				RecursiveToStringStyle.MULTI_LINE_STYLE)).toString();
	}
	
	@Override
	public int hashCode() {
        return bodyPartId.intValue();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof BodyPart)) {
			return false;
		}
		BodyPart other = (BodyPart) obj;
		if (bodyPartId == null) {
			if (other.bodyPartId != null) {
				return false;
			}
		} else if (!bodyPartId.equals(other.bodyPartId)) {
			return false;
		}
		return true;
	}	
	
}
