package com.cebi.entity;

import java.util.List;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TableMetaData {

	private String tableName;
	private String name;
	List<AppMessages> appMessage;
	private List<ColumnNames> names;
	private List<ApplicationLabel> appLabels;

	public TableMetaData() {
	}

	public TableMetaData(String tableName, String name) {
		super();
		this.tableName = tableName;
		if (tableName.equalsIgnoreCase("INCT"))
			this.name = "Deposit Transaction";

		else if (tableName.equalsIgnoreCase("GLIF"))
			this.name = "General Ledger Interface";
		else if (tableName.equalsIgnoreCase("INCT@RRBINCT"))
			this.name = "OLD Deposit Transaction ";
		

	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<ColumnNames> getNames() {
		return names;
	}

	public void setNames(List<ColumnNames> names) {
		this.names = names;
	}

	public List<AppMessages> getAppMessage() {
		return appMessage;
	}

	public void setAppMessage(List<AppMessages> appMessage) {
		this.appMessage = appMessage;
	}

	public List<ApplicationLabel> getAppLabels() {
		return appLabels;
	}

	public void setAppLabels(List<ApplicationLabel> appLabels) {
		this.appLabels = appLabels;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
