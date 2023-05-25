package com.cebi.dao;

import com.cebi.entity.BranchInfo;

public interface CreateStaticReportPdfDao {
	public byte[] createStaticReportPdf(BranchInfo branchInformation);
}
