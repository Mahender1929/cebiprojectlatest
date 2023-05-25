package com.cebi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cebi.dao.CreateStaticReportDao;
import com.cebi.entity.BranchInfo;

@Service
public class CreateStaticReportServiceImpl implements CreateStaticReportService{
	
	@Autowired
	CreateStaticReportDao createStaticReportDao;

	@Override
	public byte[] createReportSA(BranchInfo branchInformation) {
		
		return createStaticReportDao.createReportSA(branchInformation);
	}

}
