package com.cebi.service;

import java.util.List;

import com.cebi.entity.Banks;
import com.cebi.entity.StaticReports;
import com.cebi.entity.TellerMaster;

public interface StaticReportAccService {
	
 public	List<StaticReports> getDetailByAccountNo(StaticReports staticReports,String bankCode,TellerMaster tellerMaster);
 public	List<StaticReports> getDepositStmtDetailyByAccountNo(StaticReports staticReports,String bankCode,TellerMaster tellerMaster);
 public List<Banks> getBankNames(String bankCode);

}
