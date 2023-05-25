package com.cebi.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cebi.entity.BranchRep;
import com.cebi.entity.Ccdp010;
import com.cebi.utility.CebiConstant;

@Repository
public class BranchInformationDaoImpl implements BranchInformationDao {
	private static final Logger logger = Logger.getLogger(BranchInformationDaoImpl.class);

	@Autowired
	SessionFactory staticReportSessionFactory;
	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	CebiConstant cebiConstant;


	@Transactional
	public boolean deleteBranchInfo(BranchRep ccdp0101) {

		Session session = staticReportSessionFactory.getCurrentSession();
		Query query = session.createSQLQuery("delete from ccdp010 where BRCD_BANK_CD= :brcdbankcd AND ACCT_NO= :accno AND BRANCH_CODE= :branchCode");
		//Query query = session.createQuery("delete from Ccdp010 c where c.id.brcdBankCd= :brcdbankcd AND c.id.acctNo= :accno AND c.id.branchCode= :branchcode");
		query.setParameter("brcdbankcd",ccdp0101.getBrcdBankCd());
		query.setParameter("accno",ccdp0101.getAcctNo());
		query.setParameter("branchCode",ccdp0101.getBranchCode());
		int result = query.executeUpdate();
		if (result > 0) {
			return true;
		} else{
			return false;
		}
	}

	/* INFO BRANCH DATA IMPLEMENTATION */

	@Override
	@Transactional
	public List<Ccdp010> retriveBranchInfoDetails(BranchRep ccdp010) {
		logger.info("inside createStaticDao start time :: " + System.currentTimeMillis());
		Session session = staticReportSessionFactory.getCurrentSession();
		Query query = null;
		String accNo = ccdp010.getAcctNo();
		if (accNo != null && accNo != "" && !accNo.isEmpty()) {
			//query = session.createQuery("From Ccdp010 c where c.id.brcdBankCd = :brcd_bank_cd and c.id.reportDate= :report_date and c.id.branchCode= :branch_code and c.id.acctNo= :acct_no and REC_STATUS NOT IN('deleted')");
			query = session.createSQLQuery("From Ccdp010 c where c.id.brcdBankCd = :brcd_bank_cd and c.id.reportDate= :report_date and c.id.branchCode= :branch_code and c.id.acctNo= :acct_no");
			query.setParameter("brcd_bank_cd", ccdp010.getBrcdBankCd());
			query.setParameter("report_date", ccdp010.getReportDate());
			query.setParameter("branch_code", ccdp010.getBranchCode());
			query.setParameter("acct_no", ccdp010.getAcctNo());
			List<Ccdp010> list = query.list();
			return list;
		} else {
			//query = session.createQuery("From Ccdp010 c where c.id.brcdBankCd = :brcd_bank_cd and c.id.reportDate= :report_date and c.id.branchCode= :branch_code and REC_STATUS NOT IN('deleted')");
			query = session.createQuery("From Ccdp010 c where c.id.brcdBankCd = :brcd_bank_cd and c.id.reportDate= :report_date and c.id.branchCode= :branch_code");
			query.setParameter("brcd_bank_cd", ccdp010.getBrcdBankCd());
			query.setParameter("report_date", ccdp010.getReportDate());
			query.setParameter("branch_code", ccdp010.getBranchCode());

			List<Ccdp010> list = query.list();
			return list;
		}

	}

	/* EDIT CODE IMPLIMENTATION */

	@Transactional
	public void updateBranchInformation(Ccdp010 dbDetails, Ccdp010 ccdp010) {
		logger.info("inside updateBranchInformation start time :: " + System.currentTimeMillis());
		Session session = staticReportSessionFactory.getCurrentSession();
		session.update(ccdp010);
	}

	@Transactional
	public Ccdp010 populateDbRecord(Ccdp010 ccdp010) {
		logger.info("inside populateDbRecord start time :: " + System.currentTimeMillis());
		Session session = staticReportSessionFactory.getCurrentSession();
		Query query = session.createQuery("From  Ccdp010 c where c.id.brcdBankCd= :brcd_bank_cd and c.id.acctNo=:acct_no");
		query.setParameter("brcd_bank_cd", ccdp010.getId().getBrcdBankCd());
		query.setParameter("acct_no", ccdp010.getId().getAcctNo());
		List<Ccdp010> list = query.list();
		return list.get(0);
	}
	
	
	/* SHOW CHART CODE IMPLEMENTATION */

	@Transactional
	public List<Object[]> createShowChart(String bank) {
		logger.info("Inside populateConsolidatedata start time ::" + System.currentTimeMillis());
		Session session = cebiConstant.getCurrentSession(bank);
		Query query = session.createSQLQuery("select acct_type,count(*) as count from invm group by acct_type");
		List<Object[]> list = query.list();
		return list;

	}

	@Override
	public List<Object[]> retrivechartLoanChartDetails(String bank) {
		logger.info("Inside populateConsolidatedata start time ::" + System.currentTimeMillis());
		Session session = cebiConstant.getCurrentSession(bank);
		Query query = session.createSQLQuery("select act_type,count(*) as count from borm group by act_type");
		List<Object[]> list = query.list();
		return list;
	}

	/*
	 * public static void main(String[] args) { CreateStaticReportDaoImple
	 * createStaticReportDaoImple = new CreateStaticReportDaoImple(); BranchInfo
	 * branchInformation = new BranchInfo();
	 * branchInformation.setBrcdBranchCd("00002");
	 * branchInformation.setBrcdBankCd("001");
	 * createStaticReportDaoImple.createReportSA(branchInformation); }
	 */

}
