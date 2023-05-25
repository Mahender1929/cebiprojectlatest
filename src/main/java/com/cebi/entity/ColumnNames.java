package com.cebi.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ColumnNames  {

	public ColumnNames(String name, String dataType) {
		this.name = name;
		this.dataType = dataType;
	}

	private String name;
	private String dataType;
	private String fieldName;
	@Transient
	private String required;
	List<ApplicationLabel>appLabels;

	public String getName() {
		return name;
	}

	public ColumnNames() {
		super();
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getField() {
		return fieldName;
	}

	public void setField(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public List<ApplicationLabel> getAppLabels() {
		return appLabels;
	}

	public void setAppLabels(List<ApplicationLabel> appLabels) {
		this.appLabels = appLabels;
	}

	public String getRequired() {
		return required;
	}

	public void setRequired(String required) {
		this.required = required;
	}
}
