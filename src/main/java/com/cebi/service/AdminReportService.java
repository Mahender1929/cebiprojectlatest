package com.cebi.service;

import java.sql.SQLSyntaxErrorException;
import java.util.List;
import java.util.Map;

import com.cebi.entity.Banks;
import com.cebi.entity.QueryData;
import com.cebi.entity.TableMetaData;
import com.cebi.entity.TellerMaster;
import com.cebi.entity.ViewInfo;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;


public interface AdminReportService {

	public List<TableMetaData> getTableData(QueryData getTableData,String bank,TellerMaster tellerMaster) throws Exception;
	public List<TableMetaData> populateDbTables(String bank);
	public List<Banks> retreiveBankNames();
	public Map<String,List<String>> populateBankDbDetails(List<Banks> banks);
	public Banks populateBankDbDetail(String bank);
	public  List<ViewInfo> retreiveViewDetails(String type);
	Object buildSqlQuery(QueryData getTableData, String bank, TellerMaster tellerMaster) throws Exception;
	public void updatereportStatus(int id, String inProcess);
	byte[] downloadExcel(QueryData queryData, String bank, TellerMaster master) throws Exception;
	byte[] downloadCsvPipeSeperator(QueryData queryData, String bank, TellerMaster master) throws Exception;
	byte[] downloadPdf(QueryData queryData, String bank, TellerMaster master) throws Exception;
	public byte[] csvDownloadQueue(QueryData queryData, String bank, TellerMaster master) throws Exception;
}
