package com.cebi.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cebi.dao.BranchInformationDao;
import com.cebi.entity.BranchRep;
import com.cebi.entity.Ccdp010;
import com.cebi.entity.Chart;



@Service
public class BranchInformationServiceImpl implements BranchInformationService {

	@Autowired 
	BranchInformationDao branchInformationDao;
	
	/*@Override
	public  List<Ccdp010>retriveBranchInfoDetails(Ccdp010 ccdp010,String brcdbankcd1, String branchcde,String acctno,String reportdate) {
		return branchInformationDao.retriveBranchInfoDetails(ccdp010, brcdbankcd1, branchcde, acctno, reportdate);
	}
	*/
	/*@Override
	public  List<Ccdp010>retriveBranchInfoDetails(Ccdp010 ccdp010,String brcdbankcd1, String branchcde,String acctno,String reportdate) {
		return branchInformationDao.retriveBranchInfoDetails(ccdp010,brcdbankcd1,branchcde,acctno,reportdate);
	}*/
	@Override
	public  List<Ccdp010>retriveBranchInfoDetails(BranchRep ccdp010) {
		return branchInformationDao.retriveBranchInfoDetails(ccdp010);
	}
	@Override
	public Ccdp010 populateDbRecord(Ccdp010 ccdp010) {
		
		return branchInformationDao.populateDbRecord(ccdp010);
	}

	@Override
	public void updateBranchInformation(Ccdp010 dbDetails, Ccdp010 ccdp010) {
		branchInformationDao.updateBranchInformation(dbDetails,ccdp010);
	}
	/*@Override
	public void updateBranchInformation( Ccdp010 ccdp010) {
		branchInformationDao.updateBranchInformation(ccdp010);
	}*/
	
	@Override
	public List<Chart> showchartPage(String bankname) {
		return branchInformationDao.createShowChart(bankname).stream()
				.map(data -> new Chart((String) data[0], (BigDecimal) data[1]))
				.collect(Collectors.toList());
	}
	
	@Override
	public List<Chart> retrivechartLoanChartDetails(String bank) {
		return branchInformationDao.retrivechartLoanChartDetails(bank).stream()
				.map(data -> new Chart((String) data[0], (BigDecimal) data[1]))
				.collect(Collectors.toList());
	}
	
	
	/*@Override
	public boolean deleteBranchInfo(String branchCode,String accno) {
		return branchInformationDao.deleteBranchInfo(branchCode,accno);
	}*/
	@Override
	public boolean deleteBranchInfo(BranchRep ccdp0101) {
		return branchInformationDao.deleteBranchInfo(ccdp0101);
	}

}
