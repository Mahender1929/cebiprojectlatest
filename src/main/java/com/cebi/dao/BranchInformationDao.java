package com.cebi.dao;

import java.util.List;

import com.cebi.entity.BranchRep;
import com.cebi.entity.Ccdp010;

public interface BranchInformationDao {
	/*public List<Ccdp010> retriveBranchInfoDetails(Ccdp010 ccdp010,String brcdbankcd1, String branchcde,String acctno,String reportdate);*/
	public List<Ccdp010> retriveBranchInfoDetails(BranchRep ccdp010);

	public void updateBranchInformation(Ccdp010 dbDetails, Ccdp010 ccdp0102);
	/*public void updateBranchInformation(Ccdp010 ccdp0102);*/

	public Ccdp010 populateDbRecord(Ccdp010 ccdp010);

	public List<Object[]> createShowChart(String bankname);

	/*public boolean deleteBranchInfo(String branchCode, String accno);*/
	
	public boolean deleteBranchInfo(BranchRep ccdp0101);
	public List<Object[]> retrivechartLoanChartDetails(String bank);
	
}
