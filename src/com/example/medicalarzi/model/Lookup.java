package com.example.medicalarzi.model;

import java.io.Serializable;
import java.util.Date;

public class Lookup implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 1326243434342L;

    private Integer lookupId;

    private Integer sortOrder;

    private String lookupType;

    private String lookupValue;

    private String description;

    private String createdBy;

	private Date createdDate;

	private String updatedBy;

	private Date updatedDate;

	private String activeInd;

	/**
	 *
	 */
	public Lookup() {

	}

	/**
	 *
	 * @param lookupId
	 * @param lookupValue
	 */
	public Lookup(Integer lookupId, String lookupValue) {
		this.lookupId = lookupId;
		this.lookupValue = lookupValue;
	}

    public Integer getLookupId() {
        return lookupId;
    }

    public void setLookupId(Integer lookupId) {
        this.lookupId = lookupId;
    }

    public String getLookupType() {
        return lookupType;
    }

    public void setLookupType(String lookupType) {
        this.lookupType = lookupType == null ? null : lookupType.trim();
    }

    public String getLookupValue() {
        return lookupValue;
    }

    public void setLookupValue(String lookupValue) {
        this.lookupValue = lookupValue == null ? null : lookupValue.trim();
    }
    public String getName() {
		return getLookupValue();
	}

	public void setName(String name) {
		setLookupValue(name);
	}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
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

	public String getActiveInd() {
		return activeInd;
	}

	public void setActiveInd(String activeInd) {
		this.activeInd = activeInd;
	}

}