package com.cebi.dao;

import java.util.List;
import java.util.Map;

import com.cebi.entity.BranchInfo;
import com.cebi.entity.BranchInformation;

public interface CreateStaticReportDao {


	public byte[] createReportSA(BranchInfo branchInformation);
	public List<String> getAllStandards();
	public Map<String, List<BranchInformation>> populatePerticularBranchInformation(BranchInfo info);
	public Map<String, List<BranchInformation>> populateBranchInformation();
	public Map<String, List<BranchInformation>> populateBetweenRangeBranchInformation(BranchInfo info);
	public List<BranchInformation> populateConsolidatedata(BranchInfo info);

}
