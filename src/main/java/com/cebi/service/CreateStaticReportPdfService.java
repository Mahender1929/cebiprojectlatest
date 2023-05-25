package com.cebi.service;

import com.cebi.entity.BranchInfo;

public interface CreateStaticReportPdfService {
	
	public byte[] createStaticReportPdf(BranchInfo branchInformation);

}
