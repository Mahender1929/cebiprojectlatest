package com.cebi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cebi.dao.CreateStaticReportPdfDao;
import com.cebi.entity.BranchInfo;

@Service
public class CreateStaticReportPdfServiceImpl implements CreateStaticReportPdfService {
	@Autowired
	CreateStaticReportPdfDao createStaticReportPdfDao;

	@Override
	public byte[] createStaticReportPdf(BranchInfo branchInformation) {
		return createStaticReportPdfDao.createStaticReportPdf(branchInformation);
	}

}
