package com.cebi.service;

import com.cebi.entity.BranchInfo;

public interface CreateStaticReportService {

	public byte[] createReportSA(BranchInfo branchInformation);
}
