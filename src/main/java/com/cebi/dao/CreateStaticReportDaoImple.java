package com.cebi.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.internal.SessionImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cebi.entity.BranchInfo;
import com.cebi.entity.BranchInformation;
import com.cebi.entity.Ccdp0010;
import com.cebi.utility.DatabaseConnection;
import com.cebi.utility.StaticReportConstant;

@Repository
public class CreateStaticReportDaoImple implements CreateStaticReportDao {

	private static final Logger logger = Logger.getLogger(CreateStaticReportDaoImple.class);

	private static DecimalFormat decimalFormat = new DecimalFormat(".##");

	private static final String MAINHEADING = "BRANCH  SUMMARY OF CLASSIFICATION OF ADVANCES AS PER 'IRAC' NORMS  AS ON 30/06/2018";

	@Autowired
	SessionFactory staticReportSessionFactory;

	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
	Calendar cal = Calendar.getInstance();

	@Override
	@Transactional
	public byte[] createReportSA(BranchInfo branchInformation) {

		StringBuilder buffer = new StringBuilder();
		byte[] outArray = null;
		List<String> standards = getAllStandards();
		if ("isSelectall".equalsIgnoreCase(branchInformation.getOptionvalue()) && branchInformation.getBrcdBankCd() != null) {
			populateAllResultData(branchInformation, buffer, standards);

		} else if ("isRangetrue".equalsIgnoreCase(branchInformation.getOptionvalue()) && branchInformation.getToBranch() != null && branchInformation.getBrcdBankCd() != null) {
			populateRangeDate(branchInformation, buffer, standards);

		} else if ("isConsolidated".equalsIgnoreCase(branchInformation.getOptionvalue()) && branchInformation.getBrcdBankCd() != null) {
			populateConsolitedResultData(branchInformation, buffer, standards);

		} else if (branchInformation.getBrcdBankCd() != null && branchInformation.getBrcdBranchCd() != null && !branchInformation.getBrcdBranchCd().isEmpty()) {
			populatePerticularData(branchInformation, buffer, standards);

		}

		outArray = buffer.toString().getBytes();
		return outArray;
	}

	protected void populateCcdpDetails(BranchInformation branchInformation, StringBuilder buffer) {
		logger.info("Inside populateCcdpDetails():::::::::::");
		int size = 770;
		String[] str = { "CIF_NO", "SEGMENT", "SECTOR", "FACILITY_TYPE", "ACCT_NO", "BORR_NAME", "LIMIT_AMT", "SANC_DATE", "DP", "IRREGULAR_AMT", "CONSTITUENT", "INTT_RATE", "IRREGULAR_DATE", "LAST_CR_DATE", "SECURITY_AMT", "CHARGES_CREATED", "COLLATERAL_TANG", "TOTAL_TANG", "SECU_TANG_ASSET", "INSU_AMT", "RECE_ECGC", "TOT_OS", "ASSET_SUBCLASS", "ASSET_DESC", "PB_RD", "NPA_DATE", "INCA",
				"BANK_SECU", "SECURE_ECGC", "SECU_CENT_GOVT", "SECU_STATE_GOVT", "UNSECURED", "PROV_AMT", "PROV_UNSEC", "DICGC", "WRITE_OFF" };
		String mainheading = MAINHEADING;
		String tableheading = "FINAL REPORT";
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String line = new String(new char[size]).replace('\0', '-');
		new StaticReportConstant().addHeader(branchInformation, size, str, buffer, mainheading, tableheading);
		buffer.append(System.getProperty(StaticReportConstant.LINE) + "|");
		buffer.append(line + "|");
		try {
			Session session = staticReportSessionFactory.getCurrentSession();
			Connection connection = ((SessionImpl) session).connection();
			preparedStatement = connection
					.prepareStatement("SELECT a.CIF_NO, a.SEGMENT, a.SECTOR, a.FACILITY_TYPE, a.ACCT_NO, TRIM(a.BORR_NAME) AS BORR_NAME, a.LIMIT_AMT, b.SANC_DATE, a.DP, a.IRREGULAR_AMT, TRIM(a.CONSTITUENT) AS CONSTITUENT, a.INTT_RATE, a.IRREGULAR_DATE, a.LAST_CR_DATE, TRIM(a.SECURITY_AMT) AS SECURITY_AMT, TRIM(a.CHARGES_CREATED) AS CHARGES_CREATED, a.COLLATERAL_TANG, b.TOTAL_TANG, a.SECU_TANG_ASSET, a.INSU_AMT, a.RECE_ECGC, a.TOT_OS, a.ASSET_SUBCLASS, RPAD(c.ASSET_DEC, 35, ' ') AS ASSET_DESC, a.PB_RD, a.NPA_DATE, a.INCA, b.BANK_SECU, b.SECURE_ECGC, b.SECU_CENT_GOVT, b.SECU_STATE_GOVT, b.UNSECURED, b.PROV_AMT, b.PROV_UNSEC, b.DICGC, b.WRITE_OFF FROM ccdp010 a, ccdp020 b, ccdp001 c WHERE a.acct_no = b.acct_no AND c.sub_asset = a.ASSET_SUBCLASS AND a.BRCD_BANK_CD = ?");
			preparedStatement.setString(1, branchInformation.getId().getBrcdBankCd());
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				buffer.append(System.getProperty(StaticReportConstant.LINE) + "|");
				for (int i = 0; i < str.length; i++) {
					buffer.append(StringUtils.leftPad(resultSet.getString(str[i]), 20) + "|");
				}
				buffer.append(System.getProperty(StaticReportConstant.LINE) + "|");
				buffer.append(line + "|");
			}
			new StaticReportConstant().addFooter(size, buffer);

			buffer.append(System.getProperty(StaticReportConstant.LINE) + "|");
			buffer.append(line + "|");
		} catch (Exception e) {
			logger.info(e.getMessage());
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					logger.info(e.getMessage());
				}
			}
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					logger.info(e.getMessage());
				}
			}
		}
	}

	private void populateConsolitedResultData(BranchInfo branchInformation, StringBuilder buffer, List<String> standards) {
		List<BranchInformation> lst = populateConsolidatedata(branchInformation);
		if (!lst.isEmpty()) {
			BranchInformation info = lst.get(0);
			info.setConsolidated(true);// settting consolidated flag here
			info.setBrcdBranchName("");
			computePartOneVars(info, buffer, standards);
			computePartTwoVars(info, buffer, standards);
			computePartThreeVars(info, buffer, standards);
			computePartFourVars(info, buffer, standards);
			computePartFiveVars(info, buffer, standards);
			computePartSixVars(info, buffer);
			computePartSevenVars(info, buffer, standards);
			computePartEightVars(info, buffer, standards);
			computePartNineVars(info, buffer, standards);
			computePartTenVars(info, buffer, standards);
			computePartTwelveVars(info, buffer, standards);
			computePartElevenVars(info, buffer, standards);
		}
	}

	public List<BranchInformation> populateConsolidatedata(BranchInfo info) {
		logger.info("Inside populateConsolidatedata start time ::" + System.currentTimeMillis());
		Session session = staticReportSessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM BranchInformation where id.brcdBankCd=:brcdBankCd");
		query.setParameter("brcdBankCd", info.getBrcdBankCd());
		List<BranchInformation> list = (List<BranchInformation>) query.list();
		return list;
	}

	protected void populatePerticularData(BranchInfo branchInformation, StringBuilder buffer, List<String> standards) {
		Map<String, List<BranchInformation>> branchs = populatePerticularBranchInformation(branchInformation);
		if (branchs.containsKey(branchInformation.getBrcdBankCd())) {
			List<BranchInformation> lsts = branchs.get(branchInformation.getBrcdBankCd());
			for (BranchInformation info : lsts) {
				computePartOneVars(info, buffer, standards);
				computePartTwoVars(info, buffer, standards);
				computePartThreeVars(info, buffer, standards);
				computePartFourVars(info, buffer, standards);
				computePartFiveVars(info, buffer, standards);
				computePartSixVars(info, buffer);
				computePartSevenVars(info, buffer, standards);
				computePartEightVars(info, buffer, standards);
				computePartNineVars(info, buffer, standards);
				computePartTenVars(info, buffer, standards);
				computePartTwelveVars(info, buffer, standards);
				computePartElevenVars(info, buffer, standards);
				 
			}
		}

	}

	protected void computePartTwelveVars(BranchInformation branchInformation, StringBuilder buffer, List<String> standards) {

		logger.info("Inside computePartTwelveVars()::::::::::" + branchInformation.getId().getBrcdBranchCd());
		int size = 123;
		String[] str = { "CC/RA/ACC/OD/DL", "TL/ATL", "BILLS" };
		String mainheading = MAINHEADING;
		ResultSet resultSet = null;
		PreparedStatement prepareStatement = null;
		String tableheading = "PART - III (A) FACILITYWISE SUMMARY OF PROVISION";
		Ccdp0010 ccdp0010 = null;
		Map<String, Map<String, Ccdp0010>> outer = new HashMap<>();
		Map<String, Ccdp0010> inner = null;
		String line = new String(new char[size]).replace('\0', '-');

		new StaticReportConstant().addHeader(branchInformation, size, str, buffer, mainheading, tableheading);

		buffer.append(StringUtils.center(StaticReportConstant.TOTAL, 20) + "|");
		buffer.append(System.getProperty(StaticReportConstant.LINE) + "|");
		buffer.append(line + "|");

		Session session = staticReportSessionFactory.getCurrentSession();
		Connection connection = ((SessionImpl) session).connection();
		try {
			BigDecimal grandTotalOfTotal = BigDecimal.ZERO;
			Map<String, BigDecimal> grandTotalMap = new HashMap<>();
			
			if (!branchInformation.isConsolidated()) {
				prepareStatement = connection.prepareStatement("SELECT a.FACILITY_TYPE , a.ASSET_SUBCLASS, c.asset_dec, SUM(a.tot_os) AS TOT_OS, ( SUM((b.TOTAL_TANG / 100 * PROV_SECURED)) + SUM( CASE WHEN (a.tot_os - b.TOTAL_TANG) >= 1 THEN ( (a.tot_os - b.TOTAL_TANG) / 100 * PROV_UNSECURED ) ELSE 0 END ) ) AS PROVISION FROM ccdp010 a, ccdp020 b, ccdp001 c WHERE a.BRCD_BANK_CD = b.BRCD_BANK_CD AND a.acct_no = b.acct_no AND c.sub_asset = a.ASSET_SUBCLASS AND a.BRCD_BANK_CD = ? AND a.BRANCH_CODE = ? GROUP BY a.FACILITY_TYPE, a.ASSET_SUBCLASS, c.asset_dec ORDER BY 1 ,2");
				prepareStatement.setString(1, branchInformation.getId().getBrcdBankCd());
				prepareStatement.setString(2, branchInformation.getId().getBrcdBranchCd());
			} else {
				prepareStatement = connection.prepareStatement("SELECT a.FACILITY_TYPE , a.ASSET_SUBCLASS, c.asset_dec, SUM(a.tot_os) AS TOT_OS, ( SUM((b.TOTAL_TANG / 100 * PROV_SECURED)) + SUM( CASE WHEN (a.tot_os - b.TOTAL_TANG) >= 1 THEN ( (a.tot_os - b.TOTAL_TANG) / 100 * PROV_UNSECURED ) ELSE 0 END ) ) AS PROVISION FROM ccdp010 a, ccdp020 b, ccdp001 c WHERE a.BRCD_BANK_CD = b.BRCD_BANK_CD AND a.acct_no = b.acct_no AND c.sub_asset = a.ASSET_SUBCLASS AND a.BRCD_BANK_CD = ? GROUP BY a.FACILITY_TYPE, a.ASSET_SUBCLASS, c.asset_dec ORDER BY 1 ,2");
				prepareStatement.setString(1, branchInformation.getId().getBrcdBankCd());
			}
			resultSet = prepareStatement.executeQuery();
			while (resultSet.next()) {
				BigDecimal totos = BigDecimal.ZERO;
				ccdp0010 = new Ccdp0010();
				ccdp0010.setSegment(resultSet.getString(StaticReportConstant.FACILITY_TYPE));
				ccdp0010.setStateDesc(resultSet.getString(StaticReportConstant.ASSET_DEC));
				ccdp0010.setAssetSubclass(resultSet.getString(StaticReportConstant.ASSET_SUBCLASS));
				totos = new BigDecimal(resultSet.getString(StaticReportConstant.TOT_OS));
				ccdp0010.setTotalOs(decimalFormat.format(totos));
				if (outer.containsKey(resultSet.getString(StaticReportConstant.ASSET_DEC).trim())) {
					inner = outer.get(resultSet.getString(StaticReportConstant.ASSET_DEC).trim());
					inner.put(resultSet.getString(StaticReportConstant.FACILITY_TYPE).trim(), ccdp0010);
					outer.put(ccdp0010.getStateDesc().trim(), inner);
				} else {
					inner = new HashMap<>();
					inner.put(resultSet.getString(StaticReportConstant.FACILITY_TYPE).trim(), ccdp0010);
					outer.put(ccdp0010.getStateDesc().trim(), inner);
				}
			}

			for (String standard : standards) {
				BigDecimal mistotal = BigDecimal.ZERO;
				buffer.append(System.getProperty(StaticReportConstant.LINE) + "|");
				buffer.append(StringUtils.rightPad(standard, 40));
				boolean flag = true;
				for (String assets : str) {
					BigDecimal total = BigDecimal.ZERO;
					BigDecimal grandTotal = BigDecimal.ZERO;
					inner = outer.get(standard.trim());
					if (inner != null) {
						String[] temp = assets.split("/");
						if (temp.length == 1) {
							if (inner.containsKey(assets)) {
								ccdp0010 = inner.get(assets);
								if (ccdp0010.getStateDesc().trim().equalsIgnoreCase(standard.trim()))
									buffer.append(StringUtils.leftPad(ccdp0010.getTotalOs(), 20) + "|");
								total = total.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
								mistotal = mistotal.add(total);
							}
						} else {
							for (String innerassets : temp) {
								if (inner.containsKey(innerassets)) {
									ccdp0010 = inner.get(innerassets);
									mistotal = mistotal.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
									total = total.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
								}
							}
						}
						buffer.append(StringUtils.leftPad(total.toString(), 20) + "|");
						grandTotal = new BigDecimal(total.toString());
						if (grandTotalMap.containsKey(assets.trim())) {
							BigDecimal data = grandTotalMap.get(assets.trim());
							grandTotalMap.put(assets.trim(), data.add(grandTotal));
						} else {
							grandTotalMap.put(assets.trim(), grandTotal);
						}
						if (flag) {
							flag = false;
						}
					} else {
						buffer.append(StringUtils.leftPad("0.00", 20) + "|");
					}
				}
				buffer.append(StringUtils.leftPad(decimalFormat.format(mistotal), 20) + "|");
				grandTotalOfTotal = grandTotalOfTotal.add(mistotal);
				buffer.append(System.getProperty(StaticReportConstant.LINE) + "|");
				buffer.append(line + "|");

			}
			populateMisAndTotal(buffer, grandTotalMap, str);
			buffer.append(StringUtils.leftPad(decimalFormat.format(grandTotalOfTotal), 20) + "|");
			buffer.append(System.getProperty(StaticReportConstant.LINE) + "|");
			buffer.append(line + "|");
			
			new StaticReportConstant().addFooter(size, buffer);
		} catch (SQLException e) {
			logger.info(e.getMessage());
		} finally {
			closeAllConnection(resultSet, prepareStatement);
		}

	}

	private void computePartElevenVars(BranchInformation branchInformation, StringBuilder buffer, List<String> standards) {

		logger.info("Inside computePartElevenVars():::::::::::" + branchInformation.getId().getBrcdBranchCd());
		int size = 144;
		String[] str = { "TANGIBLE ASSETS", "DICGC", "BANK", "UNSECURED" };
		String[] grandTotalColumns = { "TOTAL_TANG", "DICGC", "BNK_GOV_ECG", "UNSECURED" };
		String mainheading = MAINHEADING;
		String tableheading = "PART - IV PROVISION  CLASSIFICATION";
		Ccdp0010 ccdp0010 = null;
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;
		Map<String, Ccdp0010> inner = new HashMap<>();
		String line = new String(new char[size]).replace('\0', '-');

		new StaticReportConstant().addHeader(branchInformation, size, str, buffer, mainheading, tableheading);
		buffer.append(StringUtils.center(StaticReportConstant.TOTAL, 20) + "|");
		buffer.append(System.getProperty(StaticReportConstant.LINE) + "|");
		buffer.append(line + "|");
		Session session = staticReportSessionFactory.getCurrentSession();
		Connection connection = ((SessionImpl) session).connection();
		try {
			BigDecimal grandTotal = BigDecimal.ZERO;
			BigDecimal grandTotalOfTotal = BigDecimal.ZERO;
			Map<String, BigDecimal> grandTotalMap = new HashMap<>();
			if (!branchInformation.isConsolidated()) {
				prepareStatement = connection.prepareStatement("SELECT a.ASSET_SUBCLASS, c.asset_dec, SUM((b.TOTAL_TANG / 100 * PROV_SECURED )) AS PROV_TOTAL_TANG , SUM((b.DICGC / 100 * PROV_SECURED )) AS PROV_DICGC, SUM(((b.BANK_SECU + b.SECURE_ECGC + b.SECU_CENT_GOVT + b.SECU_STATE_GOVT) / 100 * PROV_SECURED )) AS BNK_GOV_ECG, SUM(CASE WHEN(a.tot_os - b.TOTAL_TANG) >= 1 THEN ((a.tot_os - b.TOTAL_TANG)/100* PROV_UNSECURED) ELSE 0 END )AS PROV_UNSEC_AMT , COUNT(*) AS NO_OF_AC FROM ccdp010 a, ccdp020 b, ccdp001 c WHERE a.BRCD_BANK_CD = b.BRCD_BANK_CD AND a.acct_no = b.acct_no AND c.sub_asset = a.ASSET_SUBCLASS AND a.BRCD_BANK_CD = ? AND a.BRANCH_CODE = ? GROUP BY a.ASSET_SUBCLASS, c.asset_dec ORDER BY 1 ,2");
				prepareStatement.setString(1, branchInformation.getId().getBrcdBankCd());
				prepareStatement.setString(2, branchInformation.getId().getBrcdBranchCd());
			} else {
				prepareStatement = connection.prepareStatement("SELECT a.ASSET_SUBCLASS, c.asset_dec, SUM((b.TOTAL_TANG / 100 * PROV_SECURED )) AS PROV_TOTAL_TANG , SUM((b.DICGC / 100 * PROV_SECURED )) AS PROV_DICGC, SUM(((b.BANK_SECU + b.SECURE_ECGC + b.SECU_CENT_GOVT + b.SECU_STATE_GOVT) / 100 * PROV_SECURED )) AS BNK_GOV_ECG, SUM(CASE WHEN(a.tot_os - b.TOTAL_TANG) >= 1 THEN ((a.tot_os - b.TOTAL_TANG)/100* PROV_UNSECURED) ELSE 0 END )AS PROV_UNSEC_AMT , COUNT(*) AS NO_OF_AC FROM ccdp010 a, ccdp020 b, ccdp001 c WHERE a.BRCD_BANK_CD = b.BRCD_BANK_CD AND a.acct_no = b.acct_no AND c.sub_asset = a.ASSET_SUBCLASS AND a.BRCD_BANK_CD = ? GROUP BY a.ASSET_SUBCLASS, c.asset_dec ORDER BY 1 ,2");
				prepareStatement.setString(1, branchInformation.getId().getBrcdBankCd());
			}
			resultSet = prepareStatement.executeQuery();
			while (resultSet.next()) {
				ccdp0010 = new Ccdp0010();
				ccdp0010.setAssetSubclass(resultSet.getString(StaticReportConstant.ASSET_SUBCLASS));
				ccdp0010.setStateDesc(resultSet.getString(StaticReportConstant.ASSET_DEC));
				ccdp0010.setTotalOs(decimalFormat.format(new BigDecimal(resultSet.getString("PROV_TOTAL_TANG"))));
				ccdp0010.setDicgc(resultSet.getString("PROV_DICGC"));
				ccdp0010.setBnkgovecg(resultSet.getString("BNK_GOV_ECG"));
				ccdp0010.setUnsecured(decimalFormat.format(new BigDecimal(resultSet.getString("PROV_UNSEC_AMT"))));
				ccdp0010.setNoOFAc(decimalFormat.format(new BigDecimal(resultSet.getString("NO_OF_AC"))));
				inner.put(resultSet.getString(StaticReportConstant.ASSET_DEC), ccdp0010);
				grandTotalMap = populateGrandMap(grandTotalMap, grandTotal, ccdp0010);
			}

			for (String standard : standards) {
				BigDecimal total = BigDecimal.ZERO;
				buffer.append(System.getProperty(StaticReportConstant.LINE) + "|");
				buffer.append(StringUtils.rightPad(standard, 40));
				if (inner.containsKey(standard)) {
					ccdp0010 = inner.get(standard.trim());
					buffer.append(StringUtils.leftPad(ccdp0010.getTotalOs(), 20) + "|");
					buffer.append(StringUtils.leftPad(ccdp0010.getDicgc(), 20) + "|");
					buffer.append(StringUtils.leftPad(ccdp0010.getBnkgovecg(), 20) + "|");
					buffer.append(StringUtils.leftPad(ccdp0010.getUnsecured(), 20) + "|");
					total = total.add(new BigDecimal(ccdp0010.getTotalOs().trim()).add(new BigDecimal(ccdp0010.getDicgc().trim())).add(new BigDecimal(ccdp0010.getBnkgovecg().trim())).add(new BigDecimal(ccdp0010.getUnsecured().trim())));
					grandTotalOfTotal = grandTotalOfTotal.add(total);
				} else {
					buffer.append(StringUtils.leftPad("0.00", 20) + "|");
					buffer.append(StringUtils.leftPad("0.00", 20) + "|");
					buffer.append(StringUtils.leftPad("0.00", 20) + "|");
					buffer.append(StringUtils.leftPad("0.00", 20) + "|");

				}
				buffer.append(StringUtils.leftPad(decimalFormat.format(total), 20) + "|");
				buffer.append(System.getProperty(StaticReportConstant.LINE) + "|");
				buffer.append(line + "|");

			}
			populateMisAndTotal(buffer, grandTotalMap, grandTotalColumns);
			buffer.append(StringUtils.leftPad(decimalFormat.format(grandTotalOfTotal), 20) + "|");
			buffer.append(System.getProperty(StaticReportConstant.LINE) + "|");
			buffer.append(line + "|");
			
			new StaticReportConstant().addFooter(size, buffer);
		} catch (SQLException e) {
			logger.info(e.getMessage());
		} finally {
			closeAllConnection(resultSet, prepareStatement);
		}

	}

	private void computePartTenVars(BranchInformation branchInformation, StringBuilder buffer, List<String> standards) {

		logger.info("inside computePartTenVars():::::::::::" + branchInformation.getId().getBrcdBranchCd());
		int size = 228;
		String[] str = { "CC", "ACC", "OD", "DL", "TL", "ATL", "BI", "RA" };
		String mainheading = MAINHEADING;
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;
		String tableheading = "PART - III  FACILITYWISE CLASSIFICATION PROVISION ";

		Ccdp0010 ccdp0010 = null;
		Map<String, Map<String, Ccdp0010>> outer = new HashMap<>();
		Map<String, Ccdp0010> inner = null;
		String line = new String(new char[size]).replace('\0', '-');

		new StaticReportConstant().addHeader(branchInformation, size, str, buffer, mainheading, tableheading);
		buffer.append(StringUtils.center(StaticReportConstant.TOTAL, 20) + "|");
		buffer.append(System.getProperty(StaticReportConstant.LINE) + "|");
		buffer.append(line + "|");
		Session session = staticReportSessionFactory.getCurrentSession();
		Connection connection = ((SessionImpl) session).connection();
		try {
			BigDecimal grandTotal = BigDecimal.ZERO;
			BigDecimal grandAllTotal = BigDecimal.ZERO;
			Map<String, BigDecimal> grandTotalMap = new HashMap<>();
			
			if (!branchInformation.isConsolidated()) {
				prepareStatement = connection.prepareStatement("SELECT a.FACILITY_TYPE , a.ASSET_SUBCLASS, c.asset_dec, SUM(a.tot_os) AS TOT_OS, ( SUM((b.TOTAL_TANG / 100 * PROV_SECURED)) + SUM( CASE WHEN (a.tot_os - b.TOTAL_TANG) >= 1 THEN ( (a.tot_os - b.TOTAL_TANG) / 100 * PROV_UNSECURED ) ELSE 0 END ) ) AS PROVISION FROM ccdp010 a, ccdp020 b, ccdp001 c WHERE a.BRCD_BANK_CD = b.BRCD_BANK_CD AND a.acct_no = b.acct_no AND c.sub_asset = a.ASSET_SUBCLASS AND a.BRCD_BANK_CD = ? AND a.BRANCH_CODE = ? GROUP BY a.FACILITY_TYPE, a.ASSET_SUBCLASS, c.asset_dec ORDER BY 1 ,2");
				prepareStatement.setString(1, branchInformation.getId().getBrcdBankCd());
				prepareStatement.setString(2, branchInformation.getId().getBrcdBranchCd());
			} else {
				prepareStatement = connection.prepareStatement("SELECT a.FACILITY_TYPE , a.ASSET_SUBCLASS, c.asset_dec, SUM(a.tot_os) AS TOT_OS, ( SUM((b.TOTAL_TANG / 100 * PROV_SECURED)) + SUM( CASE WHEN (a.tot_os - b.TOTAL_TANG) >= 1 THEN ( (a.tot_os - b.TOTAL_TANG) / 100 * PROV_UNSECURED ) ELSE 0 END ) ) AS PROVISION FROM ccdp010 a, ccdp020 b, ccdp001 c WHERE a.BRCD_BANK_CD = b.BRCD_BANK_CD AND a.acct_no = b.acct_no AND c.sub_asset = a.ASSET_SUBCLASS AND a.BRCD_BANK_CD = ? GROUP BY a.FACILITY_TYPE, a.ASSET_SUBCLASS, c.asset_dec ORDER BY 1 ,2");
				prepareStatement.setString(1, branchInformation.getId().getBrcdBankCd());
			}
			resultSet = prepareStatement.executeQuery();
			while (resultSet.next()) {
				BigDecimal totos = BigDecimal.ZERO;
				ccdp0010 = new Ccdp0010();
				ccdp0010.setSegment(resultSet.getString(StaticReportConstant.FACILITY_TYPE));
				ccdp0010.setStateDesc(resultSet.getString(StaticReportConstant.ASSET_DEC));
				ccdp0010.setAssetSubclass(resultSet.getString(StaticReportConstant.ASSET_SUBCLASS));
				totos = new BigDecimal(resultSet.getString(StaticReportConstant.TOT_OS));
				ccdp0010.setTotalOs(decimalFormat.format(totos));
				if (outer.containsKey(resultSet.getString(StaticReportConstant.ASSET_DEC).trim())) {
					inner = outer.get(resultSet.getString(StaticReportConstant.ASSET_DEC).trim());
					inner.put(resultSet.getString(StaticReportConstant.FACILITY_TYPE).trim(), ccdp0010);
					outer.put(ccdp0010.getStateDesc().trim(), inner);
				} else {
					inner = new HashMap<>();
					inner.put(resultSet.getString(StaticReportConstant.FACILITY_TYPE).trim(), ccdp0010);
					outer.put(ccdp0010.getStateDesc().trim(), inner);
				}
				
				grandTotal = new BigDecimal(ccdp0010.getTotalOs().trim());
				if (grandTotalMap.containsKey(ccdp0010.getSegment().trim())) {
					BigDecimal data = grandTotalMap.get(ccdp0010.getSegment().trim());
					grandTotalMap.put(ccdp0010.getSegment().trim(), data.add(grandTotal));
				} else {
					grandTotalMap.put(ccdp0010.getSegment().trim(), grandTotal);
				}
			}

			for (String standard : standards) {
				buffer.append(System.getProperty(StaticReportConstant.LINE) + "|");
				buffer.append(StringUtils.rightPad(standard, 40));
				BigDecimal total = BigDecimal.ZERO;
				boolean flag = true;
				for (String assets : str) {
					inner = outer.get(standard.trim());
					if (inner != null) {
						if (inner.containsKey(assets)) {
							ccdp0010 = inner.get(assets);
							if (ccdp0010.getStateDesc().trim().equalsIgnoreCase(standard.trim()))
								buffer.append(StringUtils.leftPad(ccdp0010.getTotalOs(), 20) + "|");
							total = total.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
						} else {
							buffer.append(StringUtils.leftPad("0.00", 20) + "|");
						}
						if (flag) {
							flag = false;
						}
					} else {
						buffer.append(StringUtils.leftPad("0.00", 20) + "|");
					}
				}
				grandAllTotal = grandAllTotal.add(total);
				buffer.append(StringUtils.leftPad(decimalFormat.format(total), 20) + "|");
				buffer.append(System.getProperty("line.separator") + "|");
				buffer.append(line + "|");

			}
			populateMisAndTotal(buffer, grandTotalMap, str);
			buffer.append(StringUtils.leftPad(decimalFormat.format(grandAllTotal), 20) + "|");
			buffer.append(System.getProperty(StaticReportConstant.LINE) + "|");
			buffer.append(line + "|");
			
			new StaticReportConstant().addFooter(size, buffer);
		} catch (SQLException e) {
			logger.info(e.getMessage());
		} finally {
			closeAllConnection(resultSet, prepareStatement);
		}

	}

	private void computePartNineVars(BranchInformation branchInformation, StringBuilder buffer, List<String> standards) {

		logger.info("Inside computePartNineVars():::::::::::" + branchInformation.getId().getBrcdBranchCd());
		int size = 165;
		String[] str = { StaticReportConstant.PUBLIC, StaticReportConstant.PRIORITY, "BANK", "OTHERS", "PUBLIC SEC A/Cs" };
		String mainheading = MAINHEADING;
		ResultSet resultSet = null;
		PreparedStatement prepareStatement = null;
		String tableheading = "PART - II SECTORWISE CLASSIFICATION PROVISION";
		Ccdp0010 ccdp0010 = null;
		Map<String, Map<String, Ccdp0010>> outer = new HashMap<>();
		Map<String, Ccdp0010> inner = null;
		String line = new String(new char[size]).replace('\0', '-');

		new StaticReportConstant().addHeader(branchInformation, size, str, buffer, mainheading, tableheading);
		buffer.append(StringUtils.center(StaticReportConstant.TOTAL, 20) + "|");
		buffer.append(System.getProperty(StaticReportConstant.LINE) + "|");
		buffer.append(line + "|");
		Session session = staticReportSessionFactory.getCurrentSession();
		Connection connection = ((SessionImpl) session).connection();
		try {
			BigDecimal grandTotal = BigDecimal.ZERO;
			BigDecimal grandMisTotal = BigDecimal.ZERO;
			BigDecimal grandAllTotal = BigDecimal.ZERO;
			Map<String, BigDecimal> grandTotalMap = new HashMap<>();
			//changes done by kiran
			if (!branchInformation.isConsolidated()) {
				prepareStatement = connection.prepareStatement("SELECT CASE WHEN (a.SECTOR ='NA' OR a.SECTOR =' ') THEN 'OTHERS1' ELSE a.SECTOR END AS SECTOR , a.ASSET_SUBCLASS, c.asset_dec, SUM(a.tot_os) AS TOT_OS, ( SUM((b.TOTAL_TANG / 100 * PROV_SECURED)) + SUM( CASE WHEN (a.tot_os - b.TOTAL_TANG) >= 1 THEN ( (a.tot_os - b.TOTAL_TANG) / 100 * PROV_UNSECURED ) ELSE 0 END ) ) AS PROVISION FROM ccdp010 a, ccdp020 b, ccdp001 c WHERE a.BRCD_BANK_CD = b.BRCD_BANK_CD AND a.acct_no = b.acct_no AND c.sub_asset = a.ASSET_SUBCLASS AND a.BRCD_BANK_CD = ? AND a.BRANCH_CODE = ? GROUP BY a.SECTOR, a.ASSET_SUBCLASS, c.asset_dec ORDER BY 1 ,2");
				prepareStatement.setString(1, branchInformation.getId().getBrcdBankCd());
				prepareStatement.setString(2, branchInformation.getId().getBrcdBranchCd());
			} else {
				prepareStatement = connection.prepareStatement("SELECT CASE WHEN (A.SECTOR ='NA' OR A.SECTOR =' ') THEN 'OTHERS1' ELSE A.SECTOR END AS SECTOR , a.ASSET_SUBCLASS, c.asset_dec, SUM(a.tot_os) AS TOT_OS, ( SUM((b.TOTAL_TANG / 100 * PROV_SECURED)) + SUM( CASE WHEN (a.tot_os - b.TOTAL_TANG) >= 1 THEN ( (a.tot_os - b.TOTAL_TANG) / 100 * PROV_UNSECURED ) ELSE 0 END ) ) AS PROVISION FROM ccdp010 a, ccdp020 b, ccdp001 c WHERE a.BRCD_BANK_CD = b.BRCD_BANK_CD AND a.acct_no = b.acct_no AND c.sub_asset = a.ASSET_SUBCLASS AND a.BRCD_BANK_CD = ?  GROUP BY a.SECTOR, a.ASSET_SUBCLASS, c.asset_dec ORDER BY 1 ,2");
				prepareStatement.setString(1, branchInformation.getId().getBrcdBankCd());
			}
			resultSet = prepareStatement.executeQuery();
			while (resultSet.next()) {
				BigDecimal totos = BigDecimal.ZERO;
				ccdp0010 = new Ccdp0010();
				ccdp0010.setSegment(resultSet.getString(StaticReportConstant.SECTOR));
				ccdp0010.setStateDesc(resultSet.getString(StaticReportConstant.ASSET_DEC));
				ccdp0010.setAssetSubclass(resultSet.getString(StaticReportConstant.ASSET_SUBCLASS));
				totos = new BigDecimal(resultSet.getString(StaticReportConstant.TOT_OS));
				ccdp0010.setTotalOs(decimalFormat.format(totos));
				if (outer.containsKey(resultSet.getString(StaticReportConstant.ASSET_DEC).trim())) {
					inner = outer.get(resultSet.getString(StaticReportConstant.ASSET_DEC).trim());
					inner.put(resultSet.getString(StaticReportConstant.SECTOR).trim(), ccdp0010);
					outer.put(ccdp0010.getStateDesc().trim(), inner);
				} else {
					inner = new HashMap<>();
					inner.put(resultSet.getString(StaticReportConstant.SECTOR).trim(), ccdp0010);
					outer.put(ccdp0010.getStateDesc().trim(), inner);
				}
				grandTotal = new BigDecimal(ccdp0010.getTotalOs().trim());
				if (grandTotalMap.containsKey(ccdp0010.getSegment().trim())) {
					BigDecimal data = grandTotalMap.get(ccdp0010.getSegment().trim());
					grandTotalMap.put(ccdp0010.getSegment().trim(), data.add(grandTotal));
				} else {
					grandTotalMap.put(ccdp0010.getSegment().trim(), grandTotal);
				}

			}

			for (String standard : standards) {
				buffer.append(System.getProperty(StaticReportConstant.LINE) + "|");
				buffer.append(StringUtils.rightPad(standard, 40));
				BigDecimal mistotal = BigDecimal.ZERO;
				BigDecimal total = BigDecimal.ZERO;
				boolean flag = true;
				for (String assets : str) {
					inner = outer.get(standard.trim());
					if (inner != null) {
						if (inner.containsKey(assets)) {
							ccdp0010 = inner.get(assets);
							if (ccdp0010.getStateDesc().trim().equalsIgnoreCase(standard.trim()))
								buffer.append(StringUtils.leftPad(ccdp0010.getTotalOs(), 20) + "|");
							total = total.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
						} else {
							buffer.append(StringUtils.leftPad("0.00", 20) + "|");
						}
						if (flag) {
							if (inner.containsKey("PRIVATE")) {
								ccdp0010 = inner.get("PRIVATE");
								mistotal = mistotal.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
								total = total.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
							}
							if (inner.containsKey("OTHERS1")) {
								ccdp0010 = inner.get("OTHERS1");
								mistotal = mistotal.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
								total = total.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
							}
							/*if (inner.containsKey(StaticReportConstant.PUBLIC)) {
								ccdp0010 = inner.get(StaticReportConstant.PUBLIC);
								//mistotal = mistotal.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
								//total = total.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
							}
							if (inner.containsKey(StaticReportConstant.PRIORITY)) {
								ccdp0010 = inner.get(StaticReportConstant.PRIORITY);
								//mistotal = mistotal.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
								//total = total.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
							}
							if (inner.containsKey("BANK")) {
								ccdp0010 = inner.get("BANK");
								//mistotal = mistotal.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
								//total = total.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
							}*/
							flag = false;
						}
					} else {
						buffer.append(StringUtils.leftPad("0.00", 20) + "|");
					}
				}
				grandMisTotal = grandMisTotal.add(mistotal);
				grandAllTotal = grandAllTotal.add(total);

				buffer.append(StringUtils.leftPad(decimalFormat.format(total), 20) + "|");
				buffer.append(System.getProperty(StaticReportConstant.LINE) + "|");

				buffer.append(line + "|");

			}
			populateMisAndTotal(buffer, grandTotalMap, str);
			//buffer.append(StringUtils.leftPad(decimalFormat.format(grandMisTotal), 20) + "|");
			buffer.append(StringUtils.leftPad(decimalFormat.format(grandAllTotal), 20) + "|");
			buffer.append(System.getProperty(StaticReportConstant.LINE) + "|");
			buffer.append(line + "|");

			new StaticReportConstant().addFooter(size, buffer);
		} catch (SQLException e) {
			logger.info(e.getMessage());
		} finally {
			closeAllConnection(resultSet, prepareStatement);
		}

	}

	private void computePartEightVars(BranchInformation branchInformation, StringBuilder buffer, List<String> standards) {

		logger.info("Inside computePartEightVars():::::::::::" + branchInformation.getId().getBrcdBranchCd());
		int size = 186;
		String[] str = { "SSI", "SBF", "AGR", "C&I", "PER" };
		String mainheading = MAINHEADING;
		String tableheading = "PART - I  SEGMENTWISE CLASSIFICATION PROVISION";
		String line = new String(new char[size]).replace('\0', '-');
		Ccdp0010 ccdp0010 = null;
		Map<String, Map<String, Ccdp0010>> outer = new HashMap<>();
		Map<String, Ccdp0010> inner = null;
		ResultSet resultSet = null;
		Session session = staticReportSessionFactory.getCurrentSession();
		new StaticReportConstant().addHeader(branchInformation, size, str, buffer, mainheading, tableheading);
		buffer.append(StringUtils.center("MIS", 20) + "|");
		buffer.append(StringUtils.center(StaticReportConstant.TOTAL, 20) + "|");
		buffer.append(System.getProperty(StaticReportConstant.LINE) + "|");
		buffer.append(line + "|");
		PreparedStatement prepareStatement = null;
		try {
			BigDecimal grandTotal = BigDecimal.ZERO;
			BigDecimal grandMisTotal = BigDecimal.ZERO;
			BigDecimal grandTotalOfTotal = BigDecimal.ZERO;
			Map<String, BigDecimal> grandTotalMap = new HashMap<>();
			Connection connection = ((SessionImpl) session).connection();
			if (!branchInformation.isConsolidated()) {
				prepareStatement = connection.prepareStatement("SELECT a.SEGMENT, a.ASSET_SUBCLASS, c.asset_dec AS asset_dec, SUM(a.tot_os) AS TOT_OS, ( SUM((b.TOTAL_TANG / 100 * PROV_SECURED)) + SUM( CASE WHEN (a.tot_os - b.TOTAL_TANG) >= 1 THEN ( (a.tot_os - b.TOTAL_TANG) / 100 * PROV_UNSECURED ) ELSE 0 END ) ) AS PROVISION FROM ccdp010 a, ccdp020 b, ccdp001 c WHERE a.BRCD_BANK_CD = b.BRCD_BANK_CD AND a.acct_no = b.acct_no AND c.sub_asset = a.ASSET_SUBCLASS AND a.BRCD_BANK_CD = ? AND a.BRANCH_CODE = ? GROUP BY a.SEGMENT, a.ASSET_SUBCLASS, c.asset_dec ORDER BY 1 ,2");
				prepareStatement.setString(1, branchInformation.getId().getBrcdBankCd());
				prepareStatement.setString(2, branchInformation.getId().getBrcdBranchCd());
			} else {
				prepareStatement = connection.prepareStatement("SELECT a.SEGMENT, a.ASSET_SUBCLASS, c.asset_dec AS asset_dec, SUM(a.tot_os) AS TOT_OS, ( SUM((b.TOTAL_TANG / 100 * PROV_SECURED)) + SUM( CASE WHEN (a.tot_os - b.TOTAL_TANG) >= 1 THEN ( (a.tot_os - b.TOTAL_TANG) / 100 * PROV_UNSECURED ) ELSE 0 END ) ) AS PROVISION FROM ccdp010 a, ccdp020 b, ccdp001 c WHERE a.BRCD_BANK_CD = b.BRCD_BANK_CD AND a.acct_no = b.acct_no AND c.sub_asset = a.ASSET_SUBCLASS AND a.BRCD_BANK_CD = ? GROUP BY a.SEGMENT, a.ASSET_SUBCLASS, c.asset_dec ORDER BY 1 ,2");
				prepareStatement.setString(1, branchInformation.getId().getBrcdBankCd());
			}
			resultSet = prepareStatement.executeQuery();
			while (resultSet.next()) {
				BigDecimal totos = BigDecimal.ZERO;
				ccdp0010 = new Ccdp0010();
				ccdp0010.setSegment(resultSet.getString(StaticReportConstant.SEGMENT));
				ccdp0010.setStateDesc(resultSet.getString(StaticReportConstant.ASSET_DEC));
				ccdp0010.setAssetSubclass(resultSet.getString(StaticReportConstant.ASSET_SUBCLASS));
				totos = new BigDecimal(resultSet.getString(StaticReportConstant.TOT_OS));
				ccdp0010.setTotalOs(decimalFormat.format(totos));
				if (outer.containsKey(resultSet.getString(StaticReportConstant.ASSET_DEC).trim())) {
					inner = outer.get(resultSet.getString(StaticReportConstant.ASSET_DEC).trim());
					inner.put(resultSet.getString(StaticReportConstant.SEGMENT).trim(), ccdp0010);
					outer.put(ccdp0010.getStateDesc().trim(), inner);
				} else {
					inner = new HashMap<>();
					inner.put(resultSet.getString(StaticReportConstant.SEGMENT).trim(), ccdp0010);
					outer.put(ccdp0010.getStateDesc().trim(), inner);
				}
				grandTotal = new BigDecimal(ccdp0010.getTotalOs().trim());
				if (grandTotalMap.containsKey(ccdp0010.getSegment().trim())) {
					BigDecimal data = grandTotalMap.get(ccdp0010.getSegment().trim());
					grandTotalMap.put(ccdp0010.getSegment().trim(), data.add(grandTotal));
				} else {
					grandTotalMap.put(ccdp0010.getSegment().trim(), grandTotal);
				}
			}
			for (String standard : standards) {
				buffer.append(System.getProperty(StaticReportConstant.LINE) + "|");
				buffer.append(StringUtils.rightPad(standard, 40));
				BigDecimal mistotal = BigDecimal.ZERO;
				BigDecimal total = BigDecimal.ZERO;
				boolean flag = true;
				for (String assets : str) {
					inner = outer.get(standard.trim());
					if (inner != null) {
						if (inner.containsKey(assets)) {
							ccdp0010 = inner.get(assets);
							if (ccdp0010.getStateDesc().trim().equalsIgnoreCase(standard.trim()))
								buffer.append(StringUtils.leftPad(ccdp0010.getTotalOs(), 20) + "|");
							total = total.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
						} else {
							buffer.append(StringUtils.leftPad("0.00", 20) + "|");
						}
						if (flag) {
							if (inner.containsKey("IND")) {
								ccdp0010 = inner.get("IND");
								mistotal = mistotal.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
								total = total.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
							}
							if (inner.containsKey("STF")) {
								ccdp0010 = inner.get("STF");
								mistotal = mistotal.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
								total = total.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
							}
							if (inner.containsKey("MIS")) {
								ccdp0010 = inner.get("MIS");
								mistotal = mistotal.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
								total = total.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
							}
							flag = false;
						}
					} else {
						buffer.append(StringUtils.leftPad("0.00", 20) + "|");
					}
				}
				grandMisTotal = grandMisTotal.add(mistotal);
				grandTotalOfTotal = grandTotalOfTotal.add(total);
				
				// MIS COLUMN
				buffer.append(StringUtils.leftPad(decimalFormat.format(mistotal), 20) + "|");
				buffer.append(StringUtils.leftPad(decimalFormat.format(total), 20) + "|");
				buffer.append(System.getProperty(StaticReportConstant.LINE) + "|");
				buffer.append(line + "|");

			}
			
			populateMisAndTotal(buffer, grandTotalMap, str);
			buffer.append(StringUtils.leftPad(decimalFormat.format(grandMisTotal), 20) + "|");
			buffer.append(StringUtils.leftPad(decimalFormat.format(grandTotalOfTotal), 20) + "|");
			buffer.append(System.getProperty(StaticReportConstant.LINE) + "|");
			buffer.append(line + "|");
			
			new StaticReportConstant().addFooter(size, buffer);
		} catch (Exception e) {
			logger.info(e.getMessage());
		} finally {
			closeAllConnection(resultSet, prepareStatement);
		}

	}

	protected void populateRangeDate(BranchInfo branchInformation, StringBuilder buffer, List<String> standards) {
		Map<String, List<BranchInformation>> branchs = populateBetweenRangeBranchInformation(branchInformation);
		if (branchs.containsKey(branchInformation.getBrcdBankCd())) {
			List<BranchInformation> lsts = branchs.get(branchInformation.getBrcdBankCd());
			int tobranchNo = Integer.parseInt(branchInformation.getToBranch());
			for (BranchInformation info : lsts) {
				int branchno = Integer.parseInt(info.getId().getBrcdBranchCd());
				if (branchno <= tobranchNo) {
					computePartOneVars(info, buffer, standards);
					computePartTwoVars(info, buffer, standards);
					computePartThreeVars(info, buffer, standards);
					computePartFourVars(info, buffer, standards);
					computePartFiveVars(info, buffer, standards);
					computePartSixVars(info, buffer);
					computePartSevenVars(info, buffer, standards);
					computePartEightVars(info, buffer, standards);
					computePartNineVars(info, buffer, standards);
					computePartTenVars(info, buffer, standards);
					computePartTwelveVars(info, buffer, standards);
					computePartElevenVars(info, buffer, standards);
				}
			}
			// populateCcdpDetails(lsts.get(0), buffer);
		}

	}

	private void populateAllResultData(BranchInfo branchInformation, StringBuilder buffer, List<String> standards) {

		Map<String, List<BranchInformation>> branchs = populateBranchInformation();
		if (branchs.containsKey(branchInformation.getBrcdBankCd())) {
			List<BranchInformation> lst = branchs.get(branchInformation.getBrcdBankCd());
			for (BranchInformation info : lst) {
				computePartOneVars(info, buffer, standards);
				computePartTwoVars(info, buffer, standards);
				computePartThreeVars(info, buffer, standards);
				computePartFourVars(info, buffer, standards);
				computePartFiveVars(info, buffer, standards);
				computePartSixVars(info, buffer);
				computePartSevenVars(info, buffer, standards);
				computePartEightVars(info, buffer, standards);
				computePartNineVars(info, buffer, standards);
				computePartTenVars(info, buffer, standards);
				computePartTwelveVars(info, buffer, standards);
				computePartElevenVars(info, buffer, standards);

			}
			// populateCcdpDetails(lst.get(0),buffer);
		}

	}

	protected void computePartOneVars(BranchInformation branchInformation, StringBuilder buffer, List<String> standards) {
		logger.info("Inside computePartOneVars():::::::::::" + branchInformation.getId().getBrcdBranchCd());
		int size = 186;
		String[] str = { "C&I", "SSI", "SBF", "AGR", "PER" };
		String mainheading = MAINHEADING;
		String tableheading = "PART - I  SEGMENTWISE CLASSIFICATION";
		String line = new String(new char[size]).replace('\0', '-');
		Ccdp0010 ccdp0010 = null;
		Map<String, Map<String, Ccdp0010>> outer = new HashMap<>();
		Map<String, Ccdp0010> inner = null;
		ResultSet resultSet = null;
		Session session = staticReportSessionFactory.getCurrentSession();
		new StaticReportConstant().addHeader(branchInformation, size, str, buffer, mainheading, tableheading);
		buffer.append(StringUtils.center("MIS", 20) + "|");
		buffer.append(StringUtils.center(StaticReportConstant.TOTAL, 20) + "|");
		buffer.append(System.getProperty(StaticReportConstant.LINE) + "|");
		buffer.append(line + "|");
		PreparedStatement prepareStatement = null;
		try {
			BigDecimal grandTotal = BigDecimal.ZERO;
			BigDecimal grandMisTotal = BigDecimal.ZERO;
			BigDecimal grandAllTotal = BigDecimal.ZERO;
			Map<String, BigDecimal> grandTotalMap = new HashMap<>();

			Connection connection = ((SessionImpl) session).connection();
			if (!branchInformation.isConsolidated()) {
				prepareStatement = connection.prepareStatement("SELECT a.SEGMENT, a.ASSET_SUBCLASS, asset_dec, SUM(a.tot_os) AS TOT_OS, COUNT(*) AS NO_OF_AC FROM ccdp010 a, ccdp020 b, ccdp001 c WHERE a.BRCD_BANK_CD = b.BRCD_BANK_CD AND a.acct_no = b.acct_no AND c.sub_asset = a.ASSET_SUBCLASS AND a.BRCD_BANK_CD =? AND a.BRANCH_CODE =? GROUP BY SEGMENT, ASSET_SUBCLASS ,asset_dec ORDER BY 1,2");
				prepareStatement.setString(1, branchInformation.getId().getBrcdBankCd());
				prepareStatement.setString(2, branchInformation.getId().getBrcdBranchCd());
			} else {
				prepareStatement = connection.prepareStatement("SELECT a.SEGMENT, a.ASSET_SUBCLASS, asset_dec, SUM(a.tot_os) AS TOT_OS, COUNT(*) AS NO_OF_AC FROM ccdp010 a, ccdp020 b, ccdp001 c WHERE a.BRCD_BANK_CD = b.BRCD_BANK_CD AND a.acct_no = b.acct_no AND c.sub_asset = a.ASSET_SUBCLASS AND a.BRCD_BANK_CD =? GROUP BY SEGMENT, ASSET_SUBCLASS ,asset_dec ORDER BY 1,2");
				prepareStatement.setString(1, branchInformation.getId().getBrcdBankCd());
			}
			resultSet = prepareStatement.executeQuery();
			while (resultSet.next()) {
				BigDecimal totos = BigDecimal.ZERO;
				ccdp0010 = new Ccdp0010();
				ccdp0010.setSegment(resultSet.getString(StaticReportConstant.SEGMENT));
				ccdp0010.setStateDesc(resultSet.getString(StaticReportConstant.ASSET_DEC));
				ccdp0010.setAssetSubclass(resultSet.getString(StaticReportConstant.ASSET_SUBCLASS));
				totos = new BigDecimal(resultSet.getString(StaticReportConstant.TOT_OS));
				ccdp0010.setTotalOs(decimalFormat.format(totos));
				if (outer.containsKey(resultSet.getString(StaticReportConstant.ASSET_DEC).trim())) {
					inner = outer.get(resultSet.getString(StaticReportConstant.ASSET_DEC).trim());
					inner.put(resultSet.getString(StaticReportConstant.SEGMENT).trim(), ccdp0010);
					outer.put(ccdp0010.getStateDesc().trim(), inner);
				} else {
					inner = new HashMap<>();
					inner.put(resultSet.getString(StaticReportConstant.SEGMENT).trim(), ccdp0010);
					outer.put(ccdp0010.getStateDesc().trim(), inner);
				}

				grandTotal = new BigDecimal(ccdp0010.getTotalOs().trim());
				if (grandTotalMap.containsKey(ccdp0010.getSegment().trim())) {
					BigDecimal data = grandTotalMap.get(ccdp0010.getSegment().trim());
					grandTotalMap.put(ccdp0010.getSegment().trim(), data.add(grandTotal));
				} else {
					grandTotalMap.put(ccdp0010.getSegment().trim(), grandTotal);
				}
			}
			for (String standard : standards) {
				buffer.append(System.getProperty(StaticReportConstant.LINE) + "|");
				buffer.append(StringUtils.rightPad(standard, 40));
				BigDecimal mistotal = BigDecimal.ZERO;
				BigDecimal total = BigDecimal.ZERO;
				boolean flag = true;
				for (String assets : str) {
					inner = outer.get(standard.trim());
					if (inner != null) {
						if (inner.containsKey(assets)) {
							ccdp0010 = inner.get(assets);
							if (ccdp0010.getStateDesc().trim().equalsIgnoreCase(standard.trim()))
								buffer.append(StringUtils.leftPad(ccdp0010.getTotalOs(), 20) + "|");
							total = total.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
						} else {
							buffer.append(StringUtils.leftPad("0.00", 20) + "|");
						}
						if (flag) {
							if (inner.containsKey("IND")) {
								ccdp0010 = inner.get("IND");
								mistotal = mistotal.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
								total = total.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
							}
							if (inner.containsKey("STF")) {
								ccdp0010 = inner.get("STF");
								mistotal = mistotal.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
								total = total.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
							}
							if (inner.containsKey("OTH")) {
								ccdp0010 = inner.get("OTH");
								mistotal = mistotal.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
								total = total.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
							}
							if (inner.containsKey("MIS")) {
								ccdp0010 = inner.get("MIS");
								mistotal = mistotal.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
								total = total.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
							}
							flag = false;
						}
					} else {
						buffer.append(StringUtils.leftPad("0.00", 20) + "|");
					}
				}

				grandMisTotal = grandMisTotal.add(mistotal);
				grandAllTotal = grandAllTotal.add(total);

				// MIS COLUMN
				buffer.append(StringUtils.leftPad(decimalFormat.format(mistotal), 20) + "|");
				buffer.append(StringUtils.leftPad(decimalFormat.format(total), 20) + "|");
				buffer.append(System.getProperty(StaticReportConstant.LINE) + "|");
				buffer.append(line + "|");

			}

			populateMisAndTotal(buffer, grandTotalMap, str);
			buffer.append(StringUtils.leftPad(decimalFormat.format(grandMisTotal), 20) + "|");
			buffer.append(StringUtils.leftPad(decimalFormat.format(grandAllTotal), 20) + "|");
			buffer.append(System.getProperty(StaticReportConstant.LINE) + "|");
			buffer.append(line + "|");

			new StaticReportConstant().addFooter(size, buffer);
		} catch (Exception e) {
			logger.info(e.getMessage());
		} finally {
			closeAllConnection(resultSet, prepareStatement);
		}

	}

	private void populateMisAndTotal(StringBuilder buffer, Map<String, BigDecimal> grandTotalMap, String[] columns) {
		buffer.append(System.getProperty(StaticReportConstant.LINE) + "|");
		buffer.append(StringUtils.rightPad("Grand Total", 38) + " |");
		for (String column : columns) {
			if (grandTotalMap != null) {
				if (grandTotalMap.get(column) != null) {
					buffer.append(StringUtils.leftPad(decimalFormat.format(grandTotalMap.get(column)), 20) + "|");
				} else {
					buffer.append(StringUtils.leftPad("0.00", 20) + "|");
				}
			}
		}
	}

	protected void computePartFiveVars(BranchInformation branchInformation, StringBuilder buffer, List<String> standards) {
		logger.info("Inside computePartFiveVars():::::::::::" + branchInformation.getId().getBrcdBranchCd());
		int size = 144;
		String[] str = { "TOT TANGIBLE", "DICGC", "BANKS/GOVT/ECGC", "UNSECURED" };
		String[] grandTotalColumns = { "TOTAL_TANG", "DICGC", "BNK_GOV_ECG", "UNSECURED" };
		String mainheading = MAINHEADING;
		String tableheading = "PART - IV SECURITYWISE CLASSIFICATION";
		Ccdp0010 ccdp0010 = null;
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;
		Map<String, Ccdp0010> inner = new HashMap<>();
		String line = new String(new char[size]).replace('\0', '-');

		new StaticReportConstant().addHeader(branchInformation, size, str, buffer, mainheading, tableheading);
		buffer.append(StringUtils.center(StaticReportConstant.TOTAL, 20) + "|");
		buffer.append(System.getProperty(StaticReportConstant.LINE) + "|");
		buffer.append(line + "|");
		Session session = staticReportSessionFactory.getCurrentSession();
		Connection connection = ((SessionImpl) session).connection();
		BigDecimal grandTotalOfTotal = BigDecimal.ZERO;
		try {
			Map<String, BigDecimal> grandTotalMap = new HashMap<>();
			if (!branchInformation.isConsolidated()) {
				prepareStatement = connection.prepareStatement("SELECT b.ASSET_SUBCLASS, asset_dec, SUM(b.TOTAL_TANG) AS TOTAL_TANG, SUM(b.DICGC) AS DICGC, SUM(b.BANK_SECU + b.SECURE_ECGC + b.SECU_CENT_GOVT+ b.SECU_STATE_GOVT) AS BNK_GOV_ECG, SUM(b.UNSECURED) AS UNSECURED , COUNT(*) AS NO_OF_AC FROM ccdp010 a, ccdp020 b, ccdp001 c WHERE a.BRCD_BANK_CD = b.BRCD_BANK_CD AND a.acct_no = b.acct_no AND c.sub_asset = b.ASSET_SUBCLASS AND a.BRCD_BANK_CD = ? AND a.BRANCH_CODE = ? GROUP BY b.ASSET_SUBCLASS, asset_dec ORDER BY 1, 2");
				prepareStatement.setString(1, branchInformation.getId().getBrcdBankCd());
				prepareStatement.setString(2, branchInformation.getId().getBrcdBranchCd());
			} else {
				prepareStatement = connection.prepareStatement("SELECT b.ASSET_SUBCLASS, asset_dec, SUM(b.TOTAL_TANG) AS TOTAL_TANG, SUM(b.DICGC) AS DICGC, SUM(b.BANK_SECU + b.SECURE_ECGC + b.SECU_CENT_GOVT+ b.SECU_STATE_GOVT) AS BNK_GOV_ECG, SUM(b.UNSECURED) AS UNSECURED , COUNT(*) AS NO_OF_AC FROM ccdp010 a, ccdp020 b, ccdp001 c WHERE a.BRCD_BANK_CD = b.BRCD_BANK_CD AND a.acct_no = b.acct_no AND c.sub_asset = b.ASSET_SUBCLASS AND a.BRCD_BANK_CD = ?  GROUP BY b.ASSET_SUBCLASS, asset_dec ORDER BY 1, 2");
				prepareStatement.setString(1, branchInformation.getId().getBrcdBankCd());
			}
			resultSet = prepareStatement.executeQuery();
			while (resultSet.next()) {
				BigDecimal grandTotal = BigDecimal.ZERO;
				ccdp0010 = new Ccdp0010();
				ccdp0010.setAssetSubclass(resultSet.getString(StaticReportConstant.ASSET_SUBCLASS));
				ccdp0010.setStateDesc(resultSet.getString(StaticReportConstant.ASSET_DEC));
				ccdp0010.setTotalOs(decimalFormat.format(new BigDecimal(resultSet.getString("TOTAL_TANG"))));
				ccdp0010.setDicgc(resultSet.getString("DICGC"));
				ccdp0010.setBnkgovecg(resultSet.getString("BNK_GOV_ECG"));
				ccdp0010.setUnsecured(decimalFormat.format(new BigDecimal(resultSet.getString("UNSECURED"))));
				ccdp0010.setNoOFAc(decimalFormat.format(new BigDecimal(resultSet.getString("NO_OF_AC"))));
				inner.put(resultSet.getString(StaticReportConstant.ASSET_DEC), ccdp0010);
				grandTotalMap = populateGrandMap(grandTotalMap, grandTotal, ccdp0010);
			}

			for (String standard : standards) {
				BigDecimal total = BigDecimal.ZERO;
				buffer.append(System.getProperty(StaticReportConstant.LINE) + "|");
				buffer.append(StringUtils.rightPad(standard, 40));
				if (inner.containsKey(standard)) {
					ccdp0010 = inner.get(standard.trim());
					buffer.append(StringUtils.leftPad(ccdp0010.getTotalOs(), 20) + "|");
					buffer.append(StringUtils.leftPad(ccdp0010.getDicgc(), 20) + "|");
					buffer.append(StringUtils.leftPad(ccdp0010.getBnkgovecg(), 20) + "|");
					buffer.append(StringUtils.leftPad(ccdp0010.getUnsecured(), 20) + "|");
					total = total.add(new BigDecimal(ccdp0010.getTotalOs().trim()).add(new BigDecimal(ccdp0010.getDicgc().trim())).add(new BigDecimal(ccdp0010.getBnkgovecg().trim()))
							.add(new BigDecimal(ccdp0010.getUnsecured().trim())).add(new BigDecimal(ccdp0010.getNoOFAc().trim())));
					grandTotalOfTotal = grandTotalOfTotal.add(total);
				} else {
					buffer.append(StringUtils.leftPad("0.00", 20) + "|");
					buffer.append(StringUtils.leftPad("0.00", 20) + "|");
					buffer.append(StringUtils.leftPad("0.00", 20) + "|");
					buffer.append(StringUtils.leftPad("0.00", 20) + "|");

				}
				buffer.append(StringUtils.leftPad(decimalFormat.format(total), 20) + "|");
				buffer.append(System.getProperty(StaticReportConstant.LINE) + "|");
				buffer.append(line + "|");

			}
			populateMisAndTotal(buffer, grandTotalMap, grandTotalColumns);
			buffer.append(StringUtils.leftPad(decimalFormat.format(grandTotalOfTotal), 20) + "|");
			buffer.append(System.getProperty(StaticReportConstant.LINE) + "|");
			buffer.append(line + "|");

			new StaticReportConstant().addFooter(size, buffer);
		} catch (SQLException e) {
			logger.info(e.getMessage());
		} finally {
			closeAllConnection(resultSet, prepareStatement);
		}
	}

	protected void computePartSevenVars(BranchInformation branchInformation, StringBuilder buffer, List<String> standards) {

		logger.info("Inside computePartSevenVars():::::::::::" + branchInformation.getId().getBrcdBranchCd());
		int size = 188;
		String[] str = { "C&I", "SSI", "SBF", "AGR", "PER" };
		String mainheading = MAINHEADING;
		String tableheading = "PART - VI NO OF ACCOUNTS (SEGMENTWISE)";
		Ccdp0010 ccdp0010 = null;
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;
		Map<String, Map<String, Ccdp0010>> outer = new HashMap<>();
		Map<String, Ccdp0010> inner = null;
		String line = new String(new char[size]).replace('\0', '-');

		new StaticReportConstant().addHeader(branchInformation, size, str, buffer, mainheading, tableheading);
		buffer.append(StringUtils.center("MIS", 22) + "|");
		buffer.append(StringUtils.center(StaticReportConstant.TOTAL, 20) + "|");
		buffer.append(System.getProperty(StaticReportConstant.LINE) + "|");
		buffer.append(line + "|");
		Session session = staticReportSessionFactory.getCurrentSession();
		Connection connection = ((SessionImpl) session).connection();
		try {
			BigDecimal grandTotal = BigDecimal.ZERO;
			BigDecimal grandMisTotal = BigDecimal.ZERO;
			BigDecimal grandAllTotal = BigDecimal.ZERO;
			Map<String, BigDecimal> grandTotalMap = new HashMap<>();

			if (!branchInformation.isConsolidated()) {
				prepareStatement = connection
						.prepareStatement(" SELECT a.SEGMENT,a.ASSET_SUBCLASS, asset_dec, SUM(a.tot_os) AS TOT_OS, COUNT(*) AS NO_OF_AC FROM ccdp010 a, ccdp020 b, ccdp001 c WHERE a.BRCD_BANK_CD = b.BRCD_BANK_CD AND a.acct_no = b.acct_no AND c.sub_asset = a.ASSET_SUBCLASS AND a.BRCD_BANK_CD = ? AND a.BRANCH_CODE = ? GROUP BY a.SEGMENT ,a.ASSET_SUBCLASS, asset_dec ORDER BY 1, 2");
				prepareStatement.setString(1, branchInformation.getId().getBrcdBankCd());
				prepareStatement.setString(2, branchInformation.getId().getBrcdBranchCd());
			} else {
				prepareStatement = connection.prepareStatement(" SELECT a.SEGMENT,a.ASSET_SUBCLASS, asset_dec, SUM(a.tot_os) AS TOT_OS, COUNT(*) AS NO_OF_AC FROM ccdp010 a, ccdp020 b, ccdp001 c WHERE a.BRCD_BANK_CD = b.BRCD_BANK_CD AND a.acct_no = b.acct_no AND c.sub_asset = a.ASSET_SUBCLASS AND a.BRCD_BANK_CD = ? GROUP BY a.SEGMENT ,a.ASSET_SUBCLASS, asset_dec ORDER BY 1, 2");
				prepareStatement.setString(1, branchInformation.getId().getBrcdBankCd());
			}
			resultSet = prepareStatement.executeQuery();
			while (resultSet.next()) {
				BigDecimal totos = BigDecimal.ZERO;
				ccdp0010 = new Ccdp0010();
				ccdp0010.setSegment(resultSet.getString(StaticReportConstant.SEGMENT).trim());
				ccdp0010.setStateDesc(resultSet.getString(StaticReportConstant.ASSET_DEC));
				ccdp0010.setAssetSubclass(resultSet.getString(StaticReportConstant.ASSET_SUBCLASS));
				totos = new BigDecimal(resultSet.getString(StaticReportConstant.NO_OF_AC));
				ccdp0010.setTotalOs(String.valueOf(totos.intValueExact()));
				if (outer.containsKey(resultSet.getString(StaticReportConstant.ASSET_DEC).trim())) {
					inner = outer.get(resultSet.getString(StaticReportConstant.ASSET_DEC).trim());
					inner.put(resultSet.getString(StaticReportConstant.SEGMENT).trim(), ccdp0010);
					outer.put(ccdp0010.getStateDesc().trim(), inner);
				} else {
					inner = new HashMap<>();
					inner.put(resultSet.getString(StaticReportConstant.SEGMENT).trim(), ccdp0010);
					outer.put(ccdp0010.getStateDesc().trim(), inner);
				}
				grandTotal = new BigDecimal(ccdp0010.getTotalOs().trim());
				if (grandTotalMap.containsKey(ccdp0010.getSegment().trim())) {
					BigDecimal data = grandTotalMap.get(ccdp0010.getSegment().trim());
					grandTotalMap.put(ccdp0010.getSegment().trim(), data.add(grandTotal));
				} else {
					grandTotalMap.put(ccdp0010.getSegment().trim(), grandTotal);
				}
			}

			for (String standard : standards) {
				buffer.append(System.getProperty(StaticReportConstant.LINE) + "|");
				buffer.append(StringUtils.rightPad(standard, 40));
				BigDecimal mistotal = BigDecimal.ZERO;
				BigDecimal total = BigDecimal.ZERO;
				boolean flag = true;
				for (String assets : str) {
					inner = outer.get(standard.trim());
					if (inner != null) {
						if (inner.containsKey(assets)) {
							ccdp0010 = inner.get(assets);
							if (ccdp0010.getStateDesc().trim().equalsIgnoreCase(standard.trim()))
								buffer.append(StringUtils.leftPad(ccdp0010.getTotalOs(), 20) + "|");
							total = total.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
						} else {
							buffer.append(StringUtils.leftPad("   0", 20) + "|");
						}
						if (flag) {
							if (inner.containsKey("IND")) {
								ccdp0010 = inner.get("IND");
								mistotal = mistotal.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
								total = total.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
							}
							if (inner.containsKey("STF")) {
								ccdp0010 = inner.get("STF");
								mistotal = mistotal.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
								total = total.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
							}
							if (inner.containsKey("OTH")) {
								ccdp0010 = inner.get("OTH");
								mistotal = mistotal.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
								total = total.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
							}
							if (inner.containsKey("MIS")) {
								ccdp0010 = inner.get("MIS");
								mistotal = mistotal.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
								total = total.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
							}
							flag = false;
						}
					} else {
						buffer.append(StringUtils.leftPad("   0", 20) + "|");
					}
				}
				grandMisTotal = grandMisTotal.add(mistotal);
				grandAllTotal = grandAllTotal.add(total);

				// MIS COLUMN
				buffer.append(StringUtils.leftPad(String.valueOf(mistotal.intValueExact()), 20) + "|");
				buffer.append(StringUtils.leftPad(String.valueOf(total.intValueExact()), 22) + "|");
				buffer.append(System.getProperty(StaticReportConstant.LINE) + "|");
				buffer.append(line + "|");

			}
			populateMisAndTotal(buffer, grandTotalMap, str);
			buffer.append(StringUtils.leftPad(decimalFormat.format(grandMisTotal), 20) + "|");
			buffer.append(StringUtils.leftPad(decimalFormat.format(grandAllTotal), 22) + "|");
			buffer.append(System.getProperty(StaticReportConstant.LINE) + "|");
			buffer.append(line + "|");

			new StaticReportConstant().addFooter(size, buffer);
		} catch (SQLException e) {
			logger.info(e.getMessage());
		} finally {
			closeAllConnection(resultSet, prepareStatement);
		}

	}

	protected void computePartSixVars(BranchInformation branchInformation, StringBuilder buffer) {
		logger.info("Inside computePartSixVars():::::::::::" + branchInformation.getId().getBrcdBankCd());
		int size = 155;
		String[] str = { "NO OF AC", "OUTSTANDING", "TANGIBLE", "  DICGC/BNK", "UNSECURED" };
		String[] assets = { "SSI", "SBF", "AGR", "C&I", "PER", "MIS" };
		String[] stds = { "Doubtful Asset – Less than 1 Year", "Doubtful Asset  = > 1 Year but < 3 Years", "Doubtful Asset  = > 3 Years" };

		String mainheading = MAINHEADING;
		String tableheading = "PART - V SEGMENTWISE/AGEWISE/SECURITYWISE CLASSIFICATION OF DOUBTFUL ASSETS ";
		Ccdp0010 ccdp0010 = null;
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;
		Map<String, Map<String, Ccdp0010>> outer = new HashMap<>();
		Map<String, Ccdp0010> inner = null;
		String headerline = new String(new char[size]).replace('\0', '=');

		new StaticReportConstant().addHeader(branchInformation, size, str, buffer, mainheading, tableheading);
		buffer.append(System.getProperty(StaticReportConstant.LINE) + "|");
		buffer.append(headerline + "|");
		Session session = staticReportSessionFactory.getCurrentSession();
		Connection connection = ((SessionImpl) session).connection();
		try {
			if (!branchInformation.isConsolidated()) {
				prepareStatement = connection
						.prepareStatement("SELECT CASE b.SEGMENT WHEN 01 THEN 'SSI' WHEN 02 THEN 'BSF' WHEN 03 THEN 'AGR' WHEN 04 THEN 'C&I ' WHEN 05 THEN 'PER' WHEN 06 THEN 'MIS' ELSE 'MIS' END AS SEGMENT , b.ASSET_SUBCLASS, asset_dec, COUNT(*) AS NO_OF_AC ,SUM(b.TOT_OS) AS OOUTSTANDINGS, SUM(b.TOTAL_TANG) AS TANGIBLE, SUM(b.BANK_SECU + b.SECURE_ECGC + b.SECU_STATE_GOVT + b.SECU_CENT_GOVT + b.DICGC ) AS BNKGOV, SUM(a.UNSECURED) AS UNSECURED FROM ccdp010 a, ccdp020 b, ccdp001 c WHERE a.BRCD_BANK_CD = b.BRCD_BANK_CD AND a.acct_no = b.acct_no AND c.sub_asset = b.ASSET_SUBCLASS AND b.ASSET_SUBCLASS IN ('05','06','07') AND a.BRCD_BANK_CD = ? AND a.BRANCH_CODE = ? GROUP BY b.SEGMENT ,b.ASSET_SUBCLASS, asset_dec ORDER BY 1, 2");
				prepareStatement.setString(1, branchInformation.getId().getBrcdBankCd());
				prepareStatement.setString(2, branchInformation.getId().getBrcdBranchCd());
			} else {
				prepareStatement = connection
						.prepareStatement("SELECT CASE b.SEGMENT WHEN 01 THEN 'SSI' WHEN 02 THEN 'BSF' WHEN 03 THEN 'AGR' WHEN 04 THEN 'C&I ' WHEN 05 THEN 'PER' WHEN 06 THEN 'MIS' ELSE 'MIS' END AS SEGMENT , b.ASSET_SUBCLASS, asset_dec, COUNT(*) AS NO_OF_AC ,SUM(b.TOT_OS) AS OOUTSTANDINGS, SUM(b.TOTAL_TANG) AS TANGIBLE, SUM(b.BANK_SECU + b.SECURE_ECGC + b.SECU_STATE_GOVT + b.SECU_CENT_GOVT + b.DICGC ) AS BNKGOV, SUM(a.UNSECURED) AS UNSECURED FROM ccdp010 a, ccdp020 b, ccdp001 c WHERE a.BRCD_BANK_CD = b.BRCD_BANK_CD AND a.acct_no = b.acct_no AND c.sub_asset = b.ASSET_SUBCLASS AND b.ASSET_SUBCLASS IN ('05','06','07') AND a.BRCD_BANK_CD = ? GROUP BY b.SEGMENT ,b.ASSET_SUBCLASS, asset_dec ORDER BY 1, 2");
				prepareStatement.setString(1, branchInformation.getId().getBrcdBankCd());
			}
			resultSet = prepareStatement.executeQuery();
			while (resultSet.next()) {
				ccdp0010 = new Ccdp0010();
				ccdp0010.setSegment(resultSet.getString(StaticReportConstant.SEGMENT).trim());
				ccdp0010.setStateDesc(resultSet.getString("ASSET_DEC"));
				ccdp0010.setAssetSubclass(resultSet.getString(StaticReportConstant.ASSET_SUBCLASS));
				ccdp0010.setTotalOs(resultSet.getString("OOUTSTANDINGS"));
				ccdp0010.setNoOFAc(resultSet.getString("NO_OF_AC"));
				ccdp0010.setUnsecured(resultSet.getString("UNSECURED"));
				ccdp0010.setBnkgovecg(resultSet.getString("BNKGOV"));
				ccdp0010.setOutstandings(resultSet.getString("OOUTSTANDINGS"));

				if (outer.containsKey(resultSet.getString(StaticReportConstant.SEGMENT).trim())) {
					inner = outer.get(resultSet.getString(StaticReportConstant.SEGMENT).trim());
					inner.put(ccdp0010.getStateDesc().trim(), ccdp0010);
					outer.put(resultSet.getString(StaticReportConstant.SEGMENT), inner);
				} else {
					inner = new HashMap<>();
					inner.put(ccdp0010.getStateDesc().trim(), ccdp0010);
					outer.put(resultSet.getString(StaticReportConstant.SEGMENT).trim(), inner);
				}
			}
			for (String asset : assets) {
				buffer.append(System.getProperty(StaticReportConstant.LINE) + "|");
				buffer.append(StringUtils.center(asset, 9) + "|");
				BigDecimal sumofnoofac = BigDecimal.ZERO;
				BigDecimal sumofoutstanding = BigDecimal.ZERO;
				BigDecimal sumoftangible = BigDecimal.ZERO;
				BigDecimal sumbnkgvt = BigDecimal.ZERO;
				BigDecimal sumunsecured = BigDecimal.ZERO;
				if (outer.containsKey(asset)) {
					inner = outer.get(asset);
					if (inner != null) {
						populateContainData(buffer, inner, stds, size, ccdp0010, sumofnoofac, sumofoutstanding, sumoftangible, sumbnkgvt, sumunsecured);
					}
				} else {
					for (String std : stds) {
						buffer.append(StringUtils.rightPad(std, 40) + "|");
						buffer.append(StringUtils.leftPad("0.00", 20) + "|");
						buffer.append(StringUtils.leftPad("0.00", 20) + "|");
						buffer.append(StringUtils.leftPad("0.00", 20) + "|");
						buffer.append(StringUtils.leftPad("0.00", 20) + "|");
						buffer.append(StringUtils.leftPad("0.00", 20) + "|");
						buffer.append(System.getProperty(StaticReportConstant.LINE) + "|");
						buffer.append(StringUtils.center("   ", 9) + "|");
					}
					addSubTotal(buffer, size, sumofnoofac, sumofoutstanding, sumoftangible, sumbnkgvt, sumunsecured);
				}
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
		} finally {
			closeAllConnection(resultSet, prepareStatement);
		}
	}

	private void populateContainData(StringBuilder buffer, Map<String, Ccdp0010> inner, String[] stds, int size, Ccdp0010 ccdp0010, BigDecimal sumofnoofac, BigDecimal sumofoutstanding, BigDecimal sumoftangible, BigDecimal sumbnkgvt, BigDecimal sumunsecured) {
		for (String std : stds) {
			if (inner.containsKey(std)) {
				ccdp0010 = inner.get(std);
				sumofnoofac = sumofnoofac.add(new BigDecimal(ccdp0010.getNoOFAc()));
				sumofoutstanding = sumofoutstanding.add(new BigDecimal(ccdp0010.getOutstandings()));
				sumoftangible = sumoftangible.add(new BigDecimal(ccdp0010.getTotalOs()));
				sumbnkgvt = sumbnkgvt.add(new BigDecimal(ccdp0010.getBnkgovecg()));
				sumunsecured = sumunsecured.add(new BigDecimal(ccdp0010.getUnsecured()));
				buffer.append(StringUtils.rightPad(std, 40) + "|");
				buffer.append(StringUtils.leftPad(decimalFormat.format(new BigDecimal(ccdp0010.getNoOFAc())), 20) + "|");
				buffer.append(StringUtils.leftPad(decimalFormat.format(new BigDecimal(ccdp0010.getOutstandings())), 20) + "|");
				buffer.append(StringUtils.leftPad(decimalFormat.format(new BigDecimal(ccdp0010.getTotalOs())), 20) + "|");
				buffer.append(StringUtils.leftPad(decimalFormat.format(new BigDecimal(ccdp0010.getBnkgovecg())), 20) + "|");
				buffer.append(StringUtils.leftPad(decimalFormat.format(new BigDecimal(ccdp0010.getUnsecured())), 20) + "|");
				buffer.append(System.getProperty(StaticReportConstant.LINE) + "|");
				buffer.append(StringUtils.center("   ", 9) + "|");
			} else {
				buffer.append(StringUtils.rightPad(std, 40) + "|");
				buffer.append(StringUtils.leftPad("0.00", 20) + "|");
				buffer.append(StringUtils.leftPad("0.00", 20) + "|");
				buffer.append(StringUtils.leftPad("0.00", 20) + "|");
				buffer.append(StringUtils.leftPad("0.00", 20) + "|");
				buffer.append(System.getProperty(StaticReportConstant.LINE) + "|");
				buffer.append(StringUtils.center("   ", 9) + "|");
			}
		}

		addSubTotal(buffer, size, sumofnoofac, sumofoutstanding, sumoftangible, sumbnkgvt, sumunsecured);

	}

	private void addSubTotal(StringBuilder buffer, int size, BigDecimal sumofnoofac, BigDecimal sumofoutstanding, BigDecimal sumoftangible, BigDecimal sumbnkgvt, BigDecimal sumunsecured) {
		String line = new String(new char[145]).replace('\0', '-');
		String headerline = new String(new char[size]).replace('\0', '=');
		buffer.append(line + "|");
		buffer.append(System.getProperty(StaticReportConstant.LINE) + "|");
		buffer.append(StringUtils.leftPad("SUB TOTAL", 9) + "|");
		buffer.append(StringUtils.leftPad(" ", 40) + "|");
		buffer.append(StringUtils.leftPad(sumofnoofac.toString(), 20) + "|");
		buffer.append(StringUtils.leftPad(sumofoutstanding.toString(), 20) + "|");
		buffer.append(StringUtils.leftPad(sumoftangible.toString(), 20) + "|");
		buffer.append(StringUtils.leftPad(sumbnkgvt.toString(), 20) + "|");
		buffer.append(StringUtils.leftPad(sumunsecured.toString(), 20) + "|");
		buffer.append(System.getProperty(StaticReportConstant.LINE) + "|");
		buffer.append(headerline + "|");

	}

	protected void computePartTwoVars(BranchInformation branchInformation, StringBuilder buffer, List<String> standards) {
		logger.info("Inside computePartTwoVars():::::::::::" + branchInformation.getId().getBrcdBranchCd());
		int size = 144;
		String[] str = { StaticReportConstant.PUBLIC, StaticReportConstant.PRIORITY, "BANK" };
		String mainheading = MAINHEADING;
		ResultSet resultSet = null;
		PreparedStatement prepareStatement = null;
		String tableheading = "PART - II SECTORWISE CLASSIFICATION";
		Ccdp0010 ccdp0010 = null;
		Map<String, Map<String, Ccdp0010>> outer = new HashMap<>();
		Map<String, Ccdp0010> inner = null;
		String line = new String(new char[size]).replace('\0', '-');

		new StaticReportConstant().addHeader(branchInformation, size, str, buffer, mainheading, tableheading);
		buffer.append(StringUtils.center("OTHERS", 20) + "|");
		buffer.append(StringUtils.center(StaticReportConstant.TOTAL, 20) + "|");
		buffer.append(System.getProperty(StaticReportConstant.LINE) + "|");
		buffer.append(line + "|");
		Session session = staticReportSessionFactory.getCurrentSession();
		Connection connection = ((SessionImpl) session).connection();
		try {
			BigDecimal grandTotal = BigDecimal.ZERO;
			BigDecimal grandMisTotal = BigDecimal.ZERO;
			BigDecimal grandAllTotal = BigDecimal.ZERO;
			Map<String, BigDecimal> grandTotalMap = new HashMap<>();
			if (!branchInformation.isConsolidated()) {
				prepareStatement = connection
						.prepareStatement("SELECT CASE WHEN (a.SECTOR = 'NA' OR a.SECTOR = ' ') THEN 'OTHERS1' ELSE a.SECTOR END AS SECTOR, a.ASSET_SUBCLASS, asset_dec, SUM(a.tot_os) AS TOT_OS, COUNT(*) AS NO_OF_AC FROM ccdp010 a, ccdp020 b, ccdp001 c WHERE a.BRCD_BANK_CD = b.BRCD_BANK_CD AND a.acct_no = b.acct_no AND c.sub_asset = a.ASSET_SUBCLASS AND a.BRCD_BANK_CD = ? AND a.BRANCH_CODE = ? GROUP BY a.SECTOR, a.ASSET_SUBCLASS, asset_dec ORDER BY 2, 1");
				prepareStatement.setString(1, branchInformation.getId().getBrcdBankCd());
				prepareStatement.setString(2, branchInformation.getId().getBrcdBranchCd());
			} else {
				prepareStatement = connection
						.prepareStatement("SELECT CASE WHEN (a.SECTOR ='NA' OR a.SECTOR =' ') THEN 'OTHERS' ELSE a.SECTOR END AS SECTOR , a.ASSET_SUBCLASS, asset_dec, SUM(a.tot_os) AS TOT_OS, COUNT(*) AS NO_OF_AC FROM ccdp010 a, ccdp020 b, ccdp001 c WHERE a.BRCD_BANK_CD = b.BRCD_BANK_CD AND a.acct_no = b.acct_no AND c.sub_asset = a.ASSET_SUBCLASS AND a.BRCD_BANK_CD =?  GROUP BY a.SECTOR,a.ASSET_SUBCLASS , asset_dec ORDER BY 2,1");
				prepareStatement.setString(1, branchInformation.getId().getBrcdBankCd());
			}
			resultSet = prepareStatement.executeQuery();
			while (resultSet.next()) {
				BigDecimal totos = BigDecimal.ZERO;
				ccdp0010 = new Ccdp0010();
				ccdp0010.setSegment(resultSet.getString(StaticReportConstant.SECTOR));
				ccdp0010.setStateDesc(resultSet.getString(StaticReportConstant.ASSET_DEC));
				ccdp0010.setAssetSubclass(resultSet.getString(StaticReportConstant.ASSET_SUBCLASS));
				totos = new BigDecimal(resultSet.getString(StaticReportConstant.TOT_OS));
				ccdp0010.setTotalOs(decimalFormat.format(totos));
				if (outer.containsKey(resultSet.getString(StaticReportConstant.ASSET_DEC).trim())) {
					inner = outer.get(resultSet.getString(StaticReportConstant.ASSET_DEC).trim());
					inner.put(resultSet.getString(StaticReportConstant.SECTOR).trim(), ccdp0010);
					outer.put(ccdp0010.getStateDesc().trim(), inner);
				} else {
					inner = new HashMap<>();
					inner.put(resultSet.getString(StaticReportConstant.SECTOR).trim(), ccdp0010);
					outer.put(ccdp0010.getStateDesc().trim(), inner);
				}

				grandTotal = new BigDecimal(ccdp0010.getTotalOs().trim());
				if (grandTotalMap.containsKey(ccdp0010.getSegment().trim())) {
					BigDecimal data = grandTotalMap.get(ccdp0010.getSegment().trim());
					grandTotalMap.put(ccdp0010.getSegment().trim(), data.add(grandTotal));
				} else {
					grandTotalMap.put(ccdp0010.getSegment().trim(), grandTotal);
				}
			}

			for (String standard : standards) {
				buffer.append(System.getProperty(StaticReportConstant.LINE) + "|");
				buffer.append(StringUtils.rightPad(standard, 40));
				BigDecimal mistotal = BigDecimal.ZERO;
				BigDecimal total = BigDecimal.ZERO;
				boolean flag = true;
				for (String assets : str) {
					inner = outer.get(standard.trim());
					if (inner != null) {
						if (inner.containsKey(assets)) {
							ccdp0010 = inner.get(assets);
							if (ccdp0010.getStateDesc().trim().equalsIgnoreCase(standard.trim()))
								buffer.append(StringUtils.leftPad(ccdp0010.getTotalOs(), 20) + "|");
							total = total.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
						} else {
							buffer.append(StringUtils.leftPad("0.00", 20) + "|");
						}
						if (flag) {
							/*if (inner.containsKey(StaticReportConstant.PUBLIC)) {
								ccdp0010 = inner.get(StaticReportConstant.PUBLIC);
								//mistotal = mistotal.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
								//total = total.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
							}
							if (inner.containsKey(StaticReportConstant.PRIORITY)) {
								ccdp0010 = inner.get(StaticReportConstant.PRIORITY);
								//mistotal = mistotal.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
								//total = total.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
							}
							if (inner.containsKey("BANK")) {
								ccdp0010 = inner.get("BANK");
								//mistotal = mistotal.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
								//total = total.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
							}*/
							if (inner.containsKey("OTHERS")) {
								ccdp0010 = inner.get("OTHERS");
								mistotal = mistotal.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
								total = total.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
							}
							if (inner.containsKey("PRIVATE")) {
								ccdp0010 = inner.get("PRIVATE");
								mistotal = mistotal.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
								total = total.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
							}
							if (inner.containsKey("OTHERS1")) {
								ccdp0010 = inner.get("OTHERS1");
								mistotal = mistotal.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
								total = total.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
							}
							
							flag = false;
						}
					} else {
						buffer.append(StringUtils.leftPad("0.00", 20) + "|");
					}
				}

				grandMisTotal = grandMisTotal.add(mistotal);
				grandAllTotal = grandAllTotal.add(total);

				buffer.append(StringUtils.leftPad(decimalFormat.format(mistotal), 20) + "|");
				buffer.append(StringUtils.leftPad(decimalFormat.format(total), 20) + "|");
				buffer.append(System.getProperty(StaticReportConstant.LINE) + "|");

				buffer.append(line + "|");

			}
			populateMisAndTotal(buffer, grandTotalMap, str);
			buffer.append(StringUtils.leftPad(decimalFormat.format(grandMisTotal), 20) + "|");
			buffer.append(StringUtils.leftPad(decimalFormat.format(grandAllTotal), 20) + "|");
			buffer.append(System.getProperty(StaticReportConstant.LINE) + "|");
			buffer.append(line + "|");

			new StaticReportConstant().addFooter(size, buffer);
		} catch (SQLException e) {
			logger.info(e.getMessage());
		} finally {
			closeAllConnection(resultSet, prepareStatement);
		}

	}

	protected void computePartThreeVars(BranchInformation branchInformation, StringBuilder buffer, List<String> standards) {
		logger.info("Inside computePartThreeVars():::::::::::" + branchInformation.getId().getBrcdBranchCd());
		int size = 270;
		String[] str = { "CC", "ACC", "OD", "TL", "ATL", "DL", "OD", "BI", "PB", "RD" };
		String mainheading = MAINHEADING;
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;
		String tableheading = "PART - III  FACILITYWISE CLASSIFICATION";

		Ccdp0010 ccdp0010 = null;
		Map<String, Map<String, Ccdp0010>> outer = new HashMap<>();
		Map<String, Ccdp0010> inner = null;
		String line = new String(new char[size]).replace('\0', '-');

		new StaticReportConstant().addHeader(branchInformation, size, str, buffer, mainheading, tableheading);
		buffer.append(StringUtils.center(StaticReportConstant.TOTAL, 20) + "|");
		buffer.append(System.getProperty(StaticReportConstant.LINE) + "|");
		buffer.append(line + "|");
		Session session = staticReportSessionFactory.getCurrentSession();
		Connection connection = ((SessionImpl) session).connection();
		try {
			BigDecimal grandTotal = BigDecimal.ZERO;
			BigDecimal grandTotalOfTotal = BigDecimal.ZERO;
			Map<String, BigDecimal> grandTotalMap = new HashMap<>();

			if (!branchInformation.isConsolidated()) {
				//chages done by kiran
				prepareStatement = connection
						.prepareStatement("SELECT a.FACILITY_TYPE, a.ASSET_SUBCLASS, asset_dec, SUM(a.tot_os) AS TOT_OS, COUNT(*) AS NO_OF_AC FROM ccdp010 a, ccdp020 b, ccdp001 c WHERE a.BRCD_BANK_CD = b.BRCD_BANK_CD AND a.acct_no = b.acct_no AND c.sub_asset = a.ASSET_SUBCLASS AND a.BRCD_BANK_CD = ? AND a.BRANCH_CODE = ? GROUP BY a.FACILITY_TYPE, a.ASSET_SUBCLASS, asset_dec ORDER BY 2, 1");
				prepareStatement.setString(1, branchInformation.getId().getBrcdBankCd());
				prepareStatement.setString(2, branchInformation.getId().getBrcdBranchCd());
			} else {
				prepareStatement = connection.prepareStatement("SELECT A.FACILITY_TYPE, A.ASSET_SUBCLASS, asset_dec, SUM(A.tot_os) AS TOT_OS, COUNT(*) AS NO_OF_AC FROM ccdp010 a, ccdp020 b, ccdp001 c WHERE a.BRCD_BANK_CD = b.BRCD_BANK_CD AND a.acct_no = b.acct_no AND c.sub_asset = a.ASSET_SUBCLASS AND a.BRCD_BANK_CD = ? GROUP BY A.FACILITY_TYPE, A.ASSET_SUBCLASS, asset_dec ORDER BY 2, 1");
				prepareStatement.setString(1, branchInformation.getId().getBrcdBankCd());
			}
			resultSet = prepareStatement.executeQuery();
			while (resultSet.next()) {
				BigDecimal totos = BigDecimal.ZERO;
				ccdp0010 = new Ccdp0010();
				ccdp0010.setSegment(resultSet.getString(StaticReportConstant.FACILITY_TYPE));
				ccdp0010.setStateDesc(resultSet.getString(StaticReportConstant.ASSET_DEC));
				ccdp0010.setAssetSubclass(resultSet.getString(StaticReportConstant.ASSET_SUBCLASS));
				totos = new BigDecimal(resultSet.getString(StaticReportConstant.TOT_OS));
				ccdp0010.setTotalOs(decimalFormat.format(totos));
				if (outer.containsKey(resultSet.getString(StaticReportConstant.ASSET_DEC).trim())) {
					inner = outer.get(resultSet.getString(StaticReportConstant.ASSET_DEC).trim());
					inner.put(resultSet.getString(StaticReportConstant.FACILITY_TYPE).trim(), ccdp0010);
					outer.put(ccdp0010.getStateDesc().trim(), inner);
				} else {
					inner = new HashMap<>();
					inner.put(resultSet.getString(StaticReportConstant.FACILITY_TYPE).trim(), ccdp0010);
					outer.put(ccdp0010.getStateDesc().trim(), inner);
				}
				grandTotal = new BigDecimal(ccdp0010.getTotalOs().trim());
				if (grandTotalMap.containsKey(ccdp0010.getSegment().trim())) {
					BigDecimal data = grandTotalMap.get(ccdp0010.getSegment().trim());
					grandTotalMap.put(ccdp0010.getSegment().trim(), data.add(grandTotal));
				} else {
					grandTotalMap.put(ccdp0010.getSegment().trim(), grandTotal);
				}
			}

			for (String standard : standards) {
				buffer.append(System.getProperty(StaticReportConstant.LINE) + "|");
				buffer.append(StringUtils.rightPad(standard, 40));
				BigDecimal total = BigDecimal.ZERO;
				boolean flag = true;
				for (String assets : str) {
					inner = outer.get(standard.trim());
					if (inner != null) {
						if (inner.containsKey(assets)) {
							ccdp0010 = inner.get(assets);
							if (ccdp0010.getStateDesc().trim().equalsIgnoreCase(standard.trim()))
								buffer.append(StringUtils.leftPad(ccdp0010.getTotalOs(), 20) + "|");
							total = total.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
						} else {
							buffer.append(StringUtils.leftPad("0.00", 20) + "|");
						}
						if (flag) {
							flag = false;
						}
					} else {
						buffer.append(StringUtils.leftPad("0.00", 20) + "|");
					}
				}
				grandTotalOfTotal = grandTotalOfTotal.add(total);
				buffer.append(StringUtils.leftPad(decimalFormat.format(total), 20) + "|");
				buffer.append(System.getProperty("line.separator") + "|");

				buffer.append(line + "|");

			}

			populateMisAndTotal(buffer, grandTotalMap, str);
			buffer.append(StringUtils.leftPad(decimalFormat.format(grandTotalOfTotal), 20) + "|");
			buffer.append(System.getProperty(StaticReportConstant.LINE) + "|");
			buffer.append(line + "|");

			new StaticReportConstant().addFooter(size, buffer);
		} catch (SQLException e) {
			logger.info(e.getMessage());
		} finally {
			closeAllConnection(resultSet, prepareStatement);
		}

	}

	protected void computePartFourVars(BranchInformation branchInformation, StringBuilder buffer, List<String> standards) {
		logger.info("Inside computePartFourVars():::::::::::" + branchInformation.getId().getBrcdBranchCd());
		int size = 123;
		String[] str = { "CC/RA/ACC/OD/DL", "TL/ATL", "PB" };
		String mainheading = MAINHEADING;
		ResultSet resultSet = null;
		PreparedStatement prepareStatement = null;
		String tableheading = "PART - III (A) SUMMARY OF FACILITYWISE CLASSIFICATION";
		Ccdp0010 ccdp0010 = null;
		Map<String, Map<String, Ccdp0010>> outer = new HashMap<>();
		Map<String, Ccdp0010> inner = null;
		String line = new String(new char[size]).replace('\0', '-');

		new StaticReportConstant().addHeader(branchInformation, size, str, buffer, mainheading, tableheading);

		buffer.append(StringUtils.center(StaticReportConstant.TOTAL, 20) + "|");
		buffer.append(System.getProperty(StaticReportConstant.LINE) + "|");
		buffer.append(line + "|");

		Session session = staticReportSessionFactory.getCurrentSession();
		Connection connection = ((SessionImpl) session).connection();
		try {
			BigDecimal grandTotalOfTotal = BigDecimal.ZERO;
			Map<String, BigDecimal> grandTotalMap = new HashMap<>();
			//changes done by kiran
			prepareStatement = connection
					.prepareStatement("SELECT a.FACILITY_TYPE, a.ASSET_SUBCLASS, asset_dec, SUM(a.tot_os) AS TOT_OS, COUNT(*) AS NO_OF_AC FROM ccdp010 a, ccdp020 b, ccdp001 c WHERE a.BRCD_BANK_CD = b.BRCD_BANK_CD AND a.acct_no = b.acct_no AND c.sub_asset = a.ASSET_SUBCLASS AND a.BRCD_BANK_CD = ? AND a.BRANCH_CODE = ? GROUP BY a.FACILITY_TYPE, a.ASSET_SUBCLASS, asset_dec ORDER BY 2, 1");
			prepareStatement.setString(1, branchInformation.getId().getBrcdBankCd());
			prepareStatement.setString(2, branchInformation.getId().getBrcdBranchCd());
			resultSet = prepareStatement.executeQuery();
			while (resultSet.next()) {
				BigDecimal totos = BigDecimal.ZERO;
				ccdp0010 = new Ccdp0010();
				ccdp0010.setSegment(resultSet.getString(StaticReportConstant.FACILITY_TYPE));
				ccdp0010.setStateDesc(resultSet.getString(StaticReportConstant.ASSET_DEC));
				ccdp0010.setAssetSubclass(resultSet.getString(StaticReportConstant.ASSET_SUBCLASS));
				totos = new BigDecimal(resultSet.getString(StaticReportConstant.TOT_OS));
				ccdp0010.setTotalOs(decimalFormat.format(totos));
				if (outer.containsKey(resultSet.getString(StaticReportConstant.ASSET_DEC).trim())) {
					inner = outer.get(resultSet.getString(StaticReportConstant.ASSET_DEC).trim());
					inner.put(resultSet.getString(StaticReportConstant.FACILITY_TYPE).trim(), ccdp0010);
					outer.put(ccdp0010.getStateDesc().trim(), inner);
				} else {
					inner = new HashMap<>();
					inner.put(resultSet.getString(StaticReportConstant.FACILITY_TYPE).trim(), ccdp0010);
					outer.put(ccdp0010.getStateDesc().trim(), inner);
				}
			}

			for (String standard : standards) {
				buffer.append(System.getProperty(StaticReportConstant.LINE) + "|");
				buffer.append(StringUtils.rightPad(standard, 40));
				BigDecimal mistotal = BigDecimal.ZERO;
				boolean flag = true;
				for (String assets : str) {
					BigDecimal total = BigDecimal.ZERO;
					BigDecimal grandTotal = BigDecimal.ZERO;
					inner = outer.get(standard.trim());
					if (inner != null) {
						String[] temp = assets.split("/");
						if (temp.length == 1) {
							if (inner.containsKey(assets)) {
								ccdp0010 = inner.get(assets);
								if (ccdp0010.getStateDesc().trim().equalsIgnoreCase(standard.trim()))
									buffer.append(StringUtils.leftPad(ccdp0010.getTotalOs(), 20) + "|");
								total = total.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
								mistotal = mistotal.add(total);
							}
						} else {
							for (String innerassets : temp) {
								if (inner.containsKey(innerassets)) {
									ccdp0010 = inner.get(innerassets);
									mistotal = mistotal.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
									total = total.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
								}
							}
						}
						buffer.append(StringUtils.leftPad(total.toString(), 20) + "|");
						grandTotal = new BigDecimal(total.toString());
						if (grandTotalMap.containsKey(assets.trim())) {
							BigDecimal data = grandTotalMap.get(assets.trim());
							grandTotalMap.put(assets.trim(), data.add(grandTotal));
						} else {
							grandTotalMap.put(assets.trim(), grandTotal);
						}
						if (flag) {
							flag = false;
						}
					} else {
						buffer.append(StringUtils.leftPad("0.00", 20) + "|");
					}
				}
				buffer.append(StringUtils.leftPad(decimalFormat.format(mistotal), 20) + "|");
				grandTotalOfTotal = grandTotalOfTotal.add(mistotal);
				buffer.append(System.getProperty(StaticReportConstant.LINE) + "|");
				buffer.append(line + "|");

			}
			populateMisAndTotal(buffer, grandTotalMap, str);
			buffer.append(StringUtils.leftPad(decimalFormat.format(grandTotalOfTotal), 20) + "|");
			buffer.append(System.getProperty(StaticReportConstant.LINE) + "|");
			buffer.append(line + "|");

			new StaticReportConstant().addFooter(size, buffer);
		} catch (SQLException e) {
			logger.info(e.getMessage());
		} finally {
			closeAllConnection(resultSet, prepareStatement);
		}

	}

	public List<String> getAllStandards() {

		Session session = staticReportSessionFactory.getCurrentSession();
		SQLQuery query = session.createSQLQuery("SELECT ASSET_DEC FROM ccdp001");
		return (List<String>) query.list();

		
		  /*List<String> list = new ArrayList<String>(); Connection connection =DatabaseConnection.getConnection();
		  try { 
			  PreparedStatement preparedStatement = connection.prepareStatement("SELECT STAT_DESC FROM ccdp001"); 
			  ResultSet resultSet = preparedStatement.executeQuery();
		  
		  while (resultSet.next()) {
		  list.add(resultSet.getString("STAT_DESC")); } } catch (SQLException
		  e) { e.printStackTrace(); } return list;*/
		 
	}

	public Map<String, List<BranchInformation>> populateBranchInformation() {
		logger.info("Inside populateBranchInformation start time :: " + System.currentTimeMillis());

		/*
		 * Map<String, List<BranchInformation>> map = new HashMap<>();
		 * List<BranchInformation> branchInformations = null; BranchInformation
		 * branchInformation = null; BranchInformationId branchInformationId =
		 * null;
		 * 
		 * List<BranchInformation> list = new ArrayList<BranchInformation>();
		 * Connection connection = DatabaseConnection.getConnection(); try {
		 * PreparedStatement preparedStatement = connection.prepareStatement(
		 * "SELECT BRCD_BANK_CD ,BRCD_BRANCH_CD,BRCD_BRANCH_NAME ,BRCD_REGIONAL_ID ,BRCD_ZONAL_ID from branch_information where BRCD_BRANCH_CD=00002"
		 * ); ResultSet resultSet = preparedStatement.executeQuery();
		 * 
		 * while (resultSet.next()) { branchInformation = new
		 * BranchInformation(); branchInformationId = new BranchInformationId();
		 * branchInformationId
		 * .setBrcdBankCd(resultSet.getString("BRCD_BANK_CD"));
		 * branchInformationId
		 * .setBrcdBranchCd(resultSet.getString("BRCD_BRANCH_CD"));
		 * branchInformation
		 * .setBrcdBranchName(resultSet.getString("BRCD_BRANCH_NAME"));
		 * branchInformation
		 * .setBrcdRegionalId(resultSet.getString("BRCD_REGIONAL_ID"));
		 * branchInformation
		 * .setBrcdZonalId(resultSet.getString("BRCD_ZONAL_ID"));
		 * branchInformation.setId(branchInformationId);
		 * list.add(branchInformation); } } catch (SQLException e) {
		 * logger.info(e.getMessage()); }
		 * 
		 * for (BranchInformation info : list) { if
		 * (map.containsKey(info.getId().getBrcdBankCd())) { branchInformations
		 * = map.get(info.getId().getBrcdBankCd());
		 * branchInformations.add(info); map.put(info.getId().getBrcdBankCd(),
		 * branchInformations); } else { branchInformations = new ArrayList<>();
		 * branchInformations.add(info); map.put(info.getId().getBrcdBankCd(),
		 * branchInformations); }
		 * 
		 * } logger.info("inside populateBranchInformation end time :: " +
		 * System.currentTimeMillis()); return map;
		 */

		Session session = staticReportSessionFactory.getCurrentSession();
		List<BranchInformation> branchInformations = null;
		Query query = session.createQuery("FROM BranchInformation");
		branchInformations = query.list();
		Map<String, List<BranchInformation>> map = populateResultData(branchInformations);
		return map;

	}

	public Map<String, List<BranchInformation>> populatePerticularBranchInformation(BranchInfo info) {
		logger.info("inside populateBranchInformation start time :: " + System.currentTimeMillis());
		Session session = staticReportSessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM BranchInformation where id.brcdBankCd=:brcdBankCd and id.brcdBranchCd=:brcdBranchCd order by  id.brcdBankCd,id.brcdBranchCd ");
		query.setParameter("brcdBankCd", info.getBrcdBankCd());
		query.setParameter("brcdBranchCd", info.getBrcdBranchCd());
		List<BranchInformation> list = (List<BranchInformation>) query.list();

		/*
		 * Connection connection = DatabaseConnection.getConnection();
		 * BranchInformation branchInformation = null; BranchInformationId
		 * branchInformationId = null; List<BranchInformation> list = new
		 * ArrayList<BranchInformation>(); try { PreparedStatement
		 * preparedStatement = connection.prepareStatement(
		 * "SELECT BRCD_BANK_CD ,BRCD_BRANCH_CD,BRCD_BRANCH_NAME ,BRCD_REGIONAL_ID ,BRCD_ZONAL_ID from branch_information where BRCD_BRANCH_CD=00002"
		 * ); ResultSet resultSet = preparedStatement.executeQuery();
		 * 
		 * while (resultSet.next()) { branchInformation = new
		 * BranchInformation(); branchInformationId = new BranchInformationId();
		 * branchInformationId
		 * .setBrcdBankCd(resultSet.getString("BRCD_BANK_CD"));
		 * branchInformationId
		 * .setBrcdBranchCd(resultSet.getString("BRCD_BRANCH_CD"));
		 * branchInformation
		 * .setBrcdBranchName(resultSet.getString("BRCD_BRANCH_NAME"));
		 * branchInformation
		 * .setBrcdRegionalId(resultSet.getString("BRCD_REGIONAL_ID"));
		 * branchInformation
		 * .setBrcdZonalId(resultSet.getString("BRCD_ZONAL_ID"));
		 * branchInformation.setId(branchInformationId);
		 * list.add(branchInformation); } } catch (SQLException e) {
		 * logger.info(e.getMessage()); }
		 */

		Map<String, List<BranchInformation>> map = populateResultData(list);
		return map;

	}

	public Map<String, List<BranchInformation>> populateBetweenRangeBranchInformation(BranchInfo info) {
		logger.info("inside populateBranchInformation start time :: " + System.currentTimeMillis());
		info.setEndbrcdBranchCd("00006");
		Session session = staticReportSessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM BranchInformation where id.brcdBankCd=:brcdBankCd and id.brcdBranchCd BETWEEN :startbrcdBranchCd AND :endbrcdBranchCd");
		query.setParameter("brcdBankCd", info.getBrcdBankCd());
		query.setParameter("startbrcdBranchCd", info.getBrcdBranchCd());
		query.setParameter("endbrcdBranchCd", info.getToBranch());

		List<BranchInformation> list = (List<BranchInformation>) query.list();
		Map<String, List<BranchInformation>> map = populateResultData(list);
		return map;

	}

	private Map<String, List<BranchInformation>> populateResultData(List<BranchInformation> list) {
		List<BranchInformation> branchInformations = null;
		Map<String, List<BranchInformation>> map = new HashMap<String, List<BranchInformation>>();
		for (BranchInformation branchInformation : list) {
			if (map.containsKey(branchInformation.getId().getBrcdBankCd())) {
				branchInformations = map.get(branchInformation.getId().getBrcdBankCd());
				branchInformations.add(branchInformation);
				map.put(branchInformation.getId().getBrcdBankCd(), branchInformations);
			} else {
				branchInformations = new ArrayList<>();
				branchInformations.add(branchInformation);
				map.put(branchInformation.getId().getBrcdBankCd(), branchInformations);
			}

		}
		logger.info("inside populateBranchInformation end time :: " + System.currentTimeMillis());
		return map;

	}

	private Map<String, BigDecimal> populateGrandMap(Map<String, BigDecimal> grandTotalMap, BigDecimal grandTotal, Ccdp0010 ccdp0010) {

		if (grandTotalMap.containsKey("TOTAL_TANG")) {
			grandTotal = new BigDecimal(ccdp0010.getTotalOs().trim());
			BigDecimal data = grandTotalMap.get("TOTAL_TANG");
			grandTotalMap.put("TOTAL_TANG", data.add(grandTotal));
		} else {
			grandTotal = new BigDecimal(ccdp0010.getTotalOs().trim());
			grandTotalMap.put("TOTAL_TANG", grandTotal);
		}
		if (grandTotalMap.containsKey("DICGC")) {
			grandTotal = new BigDecimal(ccdp0010.getDicgc().trim());
			BigDecimal data = grandTotalMap.get("DICGC");
			grandTotalMap.put("DICGC", data.add(grandTotal));
		} else {
			grandTotal = new BigDecimal(ccdp0010.getDicgc().trim());
			grandTotalMap.put("DICGC", grandTotal);
		}
		if (grandTotalMap.containsKey("BNK_GOV_ECG")) {
			grandTotal = new BigDecimal(ccdp0010.getBnkgovecg().trim());
			BigDecimal data = grandTotalMap.get("BNK_GOV_ECG");
			grandTotalMap.put("BNK_GOV_ECG", data.add(grandTotal));
		} else {
			grandTotal = new BigDecimal(ccdp0010.getBnkgovecg().trim());
			grandTotalMap.put("BNK_GOV_ECG", grandTotal);
		}
		if (grandTotalMap.containsKey("UNSECURED")) {
			grandTotal = new BigDecimal(ccdp0010.getUnsecured().trim());
			BigDecimal data = grandTotalMap.get("UNSECURED");
			grandTotalMap.put("UNSECURED", data.add(grandTotal));
		} else {
			grandTotal = new BigDecimal(ccdp0010.getUnsecured().trim());
			grandTotalMap.put("UNSECURED", grandTotal);
		}
		return grandTotalMap;
	}

	private void closeAllConnection(ResultSet resultSet, PreparedStatement prepareStatement) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				logger.info(e.getMessage());
			}
		}
		if (prepareStatement != null) {
			try {
				prepareStatement.close();
			} catch (SQLException e) {
				logger.info(e.getMessage());
			}
		}
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
