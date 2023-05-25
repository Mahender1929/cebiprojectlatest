package com.cebi.service;

import java.util.List;

import com.cebi.entity.BranchRep;
import com.cebi.entity.Ccdp010;
import com.cebi.entity.Chart;


public interface BranchInformationService {
	
	/*public 	List<Ccdp010> retriveBranchInfoDetails(Ccdp010 branchReport,String brcdbankcd1, String branchcde,String acctno,String reportdate);*/
	public 	List<Ccdp010> retriveBranchInfoDetails(BranchRep ccdp010);
	public Ccdp010 populateDbRecord(Ccdp010 ccdp010);
    public void updateBranchInformation(Ccdp010 dbdata, Ccdp010 ccdp010);
	/*public void updateBranchInformation(Ccdp010 ccdp010);*/
	public List<Chart> showchartPage(String bank);
	/*public boolean deleteBranchInfo(String branchCode,String accno);*/
	public boolean deleteBranchInfo(BranchRep ccdp0101);
	public List<Chart> retrivechartLoanChartDetails(String bank);

}

