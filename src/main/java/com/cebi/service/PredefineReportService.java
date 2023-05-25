package com.cebi.service;

import java.util.List;

import com.cebi.entity.PreDefineReport;
import com.cebi.entity.QueryData;
import com.cebi.entity.ReportQueueData;
import com.cebi.entity.TableMetaData;
import com.cebi.entity.TellerMaster;

public interface PredefineReportService {
	public String saveDefReportObj(PreDefineReport PreDefineReport);

	public List<PreDefineReport> getQryListByBank(String bankCode);

	public boolean isQuerySaved(PreDefineReport preDefineReport);

	public String deleteThisObject(PreDefineReport PreDefineReport);

	public List<TableMetaData> getTableDataListByBank(PreDefineReport preDefineReport, String bankCode);
	
	Object getDataIntoFile(QueryData getTableData, String bank);
	
	public 	String  getQueryString(String qry);
	
	public boolean getTableNameIsExist(List<TableMetaData> table ,String tableName);

}
