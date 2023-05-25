package com.cebi.dao;

import com.cebi.entity.QueryData;
import com.cebi.entity.ReportQueueData;

public interface CreateCsvDao {

	public int addReportQueueData(ReportQueueData datQueueData);

	ReportQueueData getReportQueueData(int id);

	public byte[] downloadCsv(QueryData getTableData, String bank);

	public byte[] downloadCsvpipe(QueryData getTableData, String bank);
}
