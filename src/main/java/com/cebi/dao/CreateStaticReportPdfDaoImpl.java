package com.cebi.dao;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.internal.SessionImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cebi.entity.BranchInfo;
import com.cebi.entity.BranchInformation;
import com.cebi.entity.Ccdp0010;
import com.cebi.utility.PdfUtils;
import com.cebi.utility.StaticReportConstant;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

@Repository
public class CreateStaticReportPdfDaoImpl extends PdfUtils implements CreateStaticReportPdfDao {

	private static final Logger logger = Logger.getLogger(CreateStaticReportPdfDaoImpl.class);

	private static DecimalFormat decimalFormat = new DecimalFormat(".##");
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/mm/yyyy");
	private static final String MAINHEADING = "     BRANCH  SUMMARY OF CLASSIFICATION OF ADVANCES AS PER 'IRAC' NORMS  AS ON 30/06/2018";

	@Autowired
	CreateStaticReportDao createStaticReportDao;

	@Autowired
	SessionFactory staticReportSessionFactory;

	@Transactional
	public byte[] createStaticReportPdf(BranchInfo branchInformation) {

		byte[] bytes = null;
		List<String> standards = createStaticReportDao.getAllStandards();
		try {
			if ("isSelectall".equalsIgnoreCase(branchInformation.getOptionvalue()) && branchInformation.getBrcdBankCd() != null) {
				bytes = populateAllResultData(branchInformation, standards);

			} else if ("isRangetrue".equalsIgnoreCase(branchInformation.getOptionvalue()) && branchInformation.getToBranch() != null && branchInformation.getBrcdBankCd() != null) {
				bytes = populateRangeDate(branchInformation, standards);

			} else if ("isConsolidated".equalsIgnoreCase(branchInformation.getOptionvalue()) && branchInformation.getBrcdBankCd() != null) {
				bytes = populateConsolitedResultData(branchInformation, standards);

			} else if (branchInformation.getBrcdBankCd() != null && branchInformation.getBrcdBranchCd() != null && !branchInformation.getBrcdBranchCd().isEmpty()) {
				bytes = populatePerticularData(branchInformation, standards);
			}

		} catch (DocumentException e) {
			logger.equals(e.getMessage());

		}
		return bytes;

	}

	private byte[] populateConsolitedResultData(BranchInfo branchInformation, List<String> standards) throws DocumentException {
		List<BranchInformation> lst = createStaticReportDao.populateConsolidatedata(branchInformation);
		Document document = new Document(PageSize.LEGAL_LANDSCAPE);
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		PdfWriter pdfWriter = null;
		PdfPTable table = null;
		pdfWriter = PdfWriter.getInstance(document, buffer);
		MyFooter1 event = new MyFooter1();
		pdfWriter.setPageEvent(event);
		document.open();
		if (!lst.isEmpty()) {
			BranchInformation info = lst.get(0);
			info.setConsolidated(true);// settting consolidated flag here
			info.setBrcdBranchName("");

			table = computePartOneVars(info, standards);
			document.add(table);
			table = computePartTwoVars(info, standards);
			document.add(table);
			document.newPage();

			table = computePartThreeVars(info, standards);
			document.add(table);
			table = computePartFourVars(info, standards);
			document.add(table);
			document.newPage();

			table = computePartFiveVars(info, standards);
			document.add(table);
			table = computePartSixVars(info);
			document.add(table);
			document.newPage();

			table = computePartSevenVars(info, standards);
			document.add(table);
			table = computePartEightVars(info, standards);
			document.add(table);
			document.newPage();

			table = computePartNineVars(info, standards);
			document.add(table);
			table = computePartTenVars(info, standards);
			document.add(table);
			document.newPage();

			table = computePartTwelveVars(info, standards);
			document.add(table);
			table = computePartElevenVars(info, standards);
			document.add(table);
			document.newPage();
		}
		document.close();
		return buffer.toByteArray();
	}

	protected byte[] populateRangeDate(BranchInfo branchInformation, List<String> standards) throws DocumentException {
		Map<String, List<BranchInformation>> branchs = createStaticReportDao.populateBetweenRangeBranchInformation(branchInformation);
		Document document = new Document(PageSize.LEGAL_LANDSCAPE);
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		PdfWriter pdfWriter = null;
		PdfPTable table = null;
		pdfWriter = PdfWriter.getInstance(document, buffer);
		MyFooter1 event = new MyFooter1();
		pdfWriter.setPageEvent(event);
		document.open();

		if (branchs.containsKey(branchInformation.getBrcdBankCd())) {
			List<BranchInformation> lsts = branchs.get(branchInformation.getBrcdBankCd());
			int tobranchNo = Integer.parseInt(branchInformation.getToBranch());
			for (BranchInformation info : lsts) {
				int branchno = Integer.parseInt(info.getId().getBrcdBranchCd());
				if (branchno <= tobranchNo) {

					table = computePartOneVars(info, standards);
					document.add(table);
					table = computePartTwoVars(info, standards);
					document.add(table);
					document.newPage();

					table = computePartThreeVars(info, standards);
					document.add(table);
					table = computePartFourVars(info, standards);
					document.add(table);
					document.newPage();

					table = computePartFiveVars(info, standards);
					document.add(table);
					table = computePartSixVars(info);
					document.add(table);
					document.newPage();

					table = computePartSevenVars(info, standards);
					document.add(table);
					table = computePartEightVars(info, standards);
					document.add(table);
					document.newPage();

					table = computePartNineVars(info, standards);
					document.add(table);
					table = computePartTenVars(info, standards);
					document.add(table);
					document.newPage();

					table = computePartTwelveVars(info, standards);
					document.add(table);
					table = computePartElevenVars(info, standards);
					document.add(table);
					document.newPage();

				}
			}
		}
		document.close();
		return buffer.toByteArray();
	}

	private byte[] populateAllResultData(BranchInfo branchInformation, List<String> standards) throws DocumentException {
		Document document = new Document(PageSize.LEGAL_LANDSCAPE);
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		PdfWriter pdfWriter = null;
		PdfPTable table = null;
		pdfWriter = PdfWriter.getInstance(document, buffer);
		MyFooter1 event = new MyFooter1();
		pdfWriter.setPageEvent(event);
		document.open();

		Map<String, List<BranchInformation>> branchs = createStaticReportDao.populateBranchInformation();
		if (branchs.containsKey(branchInformation.getBrcdBankCd())) {
			List<BranchInformation> lst = branchs.get(branchInformation.getBrcdBankCd());
			for (BranchInformation info : lst) {

				table = computePartOneVars(info, standards);
				document.add(table);
				table = computePartTwoVars(info, standards);
				document.add(table);
				document.newPage();

				table = computePartThreeVars(info, standards);
				document.add(table);
				table = computePartFourVars(info, standards);
				document.add(table);
				document.newPage();

				table = computePartFiveVars(info, standards);
				document.add(table);
				table = computePartSixVars(info);
				document.add(table);
				document.newPage();

				table = computePartSevenVars(info, standards);
				document.add(table);
				table = computePartEightVars(info, standards);
				document.add(table);
				document.newPage();

				table = computePartNineVars(info, standards);
				document.add(table);
				table = computePartTenVars(info, standards);
				document.add(table);
				document.newPage();

				table = computePartTwelveVars(info, standards);
				document.add(table);
				table = computePartElevenVars(info, standards);
				document.add(table);
				document.newPage();

			}
		}
		document.close();
		return buffer.toByteArray();

	}

	protected byte[] populatePerticularData(BranchInfo branchInformation, List<String> standards) throws DocumentException {
		Map<String, List<BranchInformation>> branchs = createStaticReportDao.populatePerticularBranchInformation(branchInformation);
		Document document = new Document(PageSize.LEGAL_LANDSCAPE);
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		PdfWriter pdfWriter = null;
		PdfPTable table = null;
		pdfWriter = PdfWriter.getInstance(document, buffer);
		MyFooter1 event = new MyFooter1();
		pdfWriter.setPageEvent(event);
		document.open();
		if (branchs.containsKey(branchInformation.getBrcdBankCd())) {
			List<BranchInformation> lsts = branchs.get(branchInformation.getBrcdBankCd());
			for (BranchInformation info : lsts) {
				table = computePartOneVars(info, standards);
				document.add(table);
				table = computePartTwoVars(info, standards);
				document.add(table);
				document.newPage();

				table = computePartThreeVars(info, standards);
				document.add(table);
				table = computePartFourVars(info, standards);
				document.add(table);
				document.newPage();

				table = computePartFiveVars(info, standards);
				document.add(table);
				table = computePartSixVars(info);
				document.add(table);
				document.newPage();

				table = computePartSevenVars(info, standards);
				document.add(table);
				table = computePartEightVars(info, standards);
				document.add(table);
				document.newPage();

				table = computePartNineVars(info, standards);
				document.add(table);
				table = computePartTenVars(info, standards);
				document.add(table);
				document.newPage();

				table = computePartTwelveVars(info, standards);
				document.add(table);
				table = computePartElevenVars(info, standards);
				document.add(table);
				document.newPage();
			}
		}
		document.close();
		return buffer.toByteArray();
	}

	protected PdfPTable computePartElevenVars(BranchInformation branchInformation, List<String> standards) {
		logger.info("Inside computePartElevenVars():::::::::::" + branchInformation.getId().getBrcdBranchCd());
		Ccdp0010 ccdp0010 = null;
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;
		String[] columns = { "TANGIBLE", "DICGC", "BANK", "UNSECURED" };
		String[] grandTotalColumns = { "TOTAL_TANG", "DICGC", "BNK_GOV_ECG", "UNSECURED" };
		float[] a = { 16, 6, 6, 6, 6, 6 };
		PdfPTable table = new PdfPTable(a);
		table.setWidthPercentage(100);

		String text = "PART - I  SEGMENTWISE CLASSIFICATION";
		table = populateTableHeader(table, branchInformation, text, a);
		table.addCell(getStateCell("STAT DESC"));
		for (String column : columns) {
			table.addCell(getCell(column));
		}
		table.addCell(getCell("Total"));
		Session session = staticReportSessionFactory.getCurrentSession();
		try {
			BigDecimal grandTotal = BigDecimal.ZERO;
			BigDecimal grandTotalOfTotal = BigDecimal.ZERO;
			Map<String, BigDecimal> grandTotalMap = new HashMap<>();
			Map<String, Ccdp0010> inner = new HashMap<>();
			Connection connection = ((SessionImpl) session).connection();
			if (!branchInformation.isConsolidated()) {
				prepareStatement = connection
						.prepareStatement("SELECT a.ASSET_SUBCLASS, c.asset_dec, SUM((b.TOTAL_TANG / 100 * PROV_SECURED )) AS PROV_TOTAL_TANG , SUM((b.DICGC / 100 * PROV_SECURED )) AS PROV_DICGC, SUM(((b.BANK_SECU + b.SECURE_ECGC + b.SECU_CENT_GOVT + b.SECU_STATE_GOVT) / 100 * PROV_SECURED )) AS BNK_GOV_ECG, SUM(CASE WHEN(a.tot_os - b.TOTAL_TANG) >= 1 THEN ((a.tot_os - b.TOTAL_TANG)/100* PROV_UNSECURED) ELSE 0 END )AS PROV_UNSEC_AMT , COUNT(*) AS NO_OF_AC FROM ccdp010 a, ccdp020 b, ccdp001 c WHERE a.BRCD_BANK_CD = b.BRCD_BANK_CD AND a.acct_no = b.acct_no AND c.sub_asset = a.ASSET_SUBCLASS AND a.BRCD_BANK_CD = ? AND a.BRANCH_CODE = ? GROUP BY a.ASSET_SUBCLASS, c.asset_dec ORDER BY 1 ,2");
				prepareStatement.setString(1, branchInformation.getId().getBrcdBankCd());
				prepareStatement.setString(2, branchInformation.getId().getBrcdBranchCd());
			} else {
				prepareStatement = connection
						.prepareStatement("SELECT a.ASSET_SUBCLASS, c.asset_dec, SUM((b.TOTAL_TANG / 100 * PROV_SECURED )) AS PROV_TOTAL_TANG , SUM((b.DICGC / 100 * PROV_SECURED )) AS PROV_DICGC, SUM(((b.BANK_SECU + b.SECURE_ECGC + b.SECU_CENT_GOVT + b.SECU_STATE_GOVT) / 100 * PROV_SECURED )) AS BNK_GOV_ECG, SUM(CASE WHEN(a.tot_os - b.TOTAL_TANG) >= 1 THEN ((a.tot_os - b.TOTAL_TANG)/100* PROV_UNSECURED) ELSE 0 END )AS PROV_UNSEC_AMT , COUNT(*) AS NO_OF_AC FROM ccdp010 a, ccdp020 b, ccdp001 c WHERE a.BRCD_BANK_CD = b.BRCD_BANK_CD AND a.acct_no = b.acct_no AND c.sub_asset = a.ASSET_SUBCLASS AND a.BRCD_BANK_CD = ? GROUP BY a.ASSET_SUBCLASS, c.asset_dec ORDER BY 1 ,2");
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
				table.addCell(getStateCell(standard));
				BigDecimal total = BigDecimal.ZERO;

				if (inner.containsKey(standard)) {
					ccdp0010 = inner.get(standard.trim());
					table.addCell(getCell(ccdp0010.getTotalOs()));
					table.addCell(getCell(ccdp0010.getDicgc()));
					table.addCell(getCell(ccdp0010.getBnkgovecg()));
					table.addCell(getCell(ccdp0010.getUnsecured()));
					total = total.add(new BigDecimal(ccdp0010.getTotalOs().trim()).add(new BigDecimal(ccdp0010.getDicgc().trim()))
							.add(new BigDecimal(ccdp0010.getBnkgovecg().trim())).add(new BigDecimal(ccdp0010.getUnsecured().trim())).add(new BigDecimal(ccdp0010.getNoOFAc().trim())));
					grandTotalOfTotal = grandTotalOfTotal.add(total);
				} else {
					table.addCell(getCell("0.00"));
					table.addCell(getCell("0.00"));
					table.addCell(getCell("0.00"));
					table.addCell(getCell("0.00"));

				}
				table.addCell(getCell(decimalFormat.format(total)));
				grandTotalOfTotal = grandTotalOfTotal.add(total);

			}
			populateMisAndTotal(table, grandTotalMap, grandTotalColumns);
			table.addCell(getCell(decimalFormat.format(grandTotalOfTotal)));
		} catch (Exception e) {
			logger.error(e.getMessage());
		}finally {
			closeAllConnection(resultSet, prepareStatement);
		}
		return table;
	}

	protected PdfPTable computePartTwelveVars(BranchInformation branchInformation, List<String> standards) {
		logger.info("Inside computePartTwelveVars():::::::::::" + branchInformation.getId().getBrcdBranchCd());
		Ccdp0010 ccdp0010 = null;
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;
		Map<String, Map<String, Ccdp0010>> outer = new HashMap<>();
		String[] columns = { "CC/RA/ACC/OD/DL", "TL/ATL", "BILLS" };
		float[] a = { 16, 8, 6, 6, 6 };
		PdfPTable table = new PdfPTable(a);
		table.setWidthPercentage(100);

		String text = "PART - III (A) FACILITYWISE SUMMARY OF PROVISION";
		table = populateTableHeader(table, branchInformation, text, a);
		table.addCell(getStateCell("STAT DESC"));
		for (String column : columns) {
			table.addCell(getCell(column));
		}
		table.addCell(getCell("TOTAL"));
		Session session = staticReportSessionFactory.getCurrentSession();
		try {
			BigDecimal grandTotalOfTotal = BigDecimal.ZERO;
			Map<String, BigDecimal> grandTotalMap = new HashMap<>();
			Map<String, Ccdp0010> inner = new HashMap<>();
			Connection connection = ((SessionImpl) session).connection();

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
				table.addCell(getStateCell(standard));
				boolean flag = true;
				BigDecimal mistotal = BigDecimal.ZERO;
				for (String assets : columns) {
					BigDecimal total = BigDecimal.ZERO;
					BigDecimal grandTotal = BigDecimal.ZERO;
					inner = outer.get(standard.trim());
					if (inner != null) {
						String[] temp = assets.split("/");
						if (temp.length == 1) {
							if (inner.containsKey(assets)) {
								ccdp0010 = inner.get(assets);

								if (ccdp0010.getStateDesc().trim().equalsIgnoreCase(standard.trim()))
									table.addCell(getCell(ccdp0010.getTotalOs()));

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
						table.addCell(getCell(total.toString()));
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
						table.addCell(getCell("0.00"));
					}
				}
				table.addCell(getCell(decimalFormat.format(mistotal)));
				grandTotalOfTotal = grandTotalOfTotal.add(mistotal);

			}
			populateMisAndTotal(table, grandTotalMap, columns);
			table.addCell(getCell(decimalFormat.format(grandTotalOfTotal)));
		} catch (SQLException e) {
			logger.info(e.getMessage());
		} finally {
			closeAllConnection(resultSet, prepareStatement);
		}
		return table;
	}

	protected PdfPTable computePartTenVars(BranchInformation branchInformation, List<String> standards) {
		logger.info("Inside computePartTenVars():::::::::::" + branchInformation.getId().getBrcdBranchCd());
		Ccdp0010 ccdp0010 = null;
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;
		Map<String, Map<String, Ccdp0010>> outer = new HashMap<>();
		String[] columns = { "CC", "ACC", "OD", "DL", "TL", "ATL", "BI", "RA" };
		float[] a = { 20, 6, 6, 6, 6, 6, 6, 6, 6, 6 };
		PdfPTable table = new PdfPTable(a);
		table.setWidthPercentage(100);

		String text = "PART - III  FACILITYWISE CLASSIFICATION PROVISION ";
		table = populateTableHeader(table, branchInformation, text, a);
		table.addCell(getStateCell("STAT DESC"));
		for (String column : columns) {
			table.addCell(getCell(column));
		}
		table.addCell(getCell("TOTAL"));
		Session session = staticReportSessionFactory.getCurrentSession();
		try {
			BigDecimal grandTotal = BigDecimal.ZERO;
			BigDecimal grandAllTotal = BigDecimal.ZERO;
			Map<String, BigDecimal> grandTotalMap = new HashMap<>();
			Map<String, Ccdp0010> inner = new HashMap<>();
			Connection connection = ((SessionImpl) session).connection();

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
				table.addCell(getStateCell(standard));
				BigDecimal total = BigDecimal.ZERO;
				boolean flag = true;
				for (String assets : columns) {
					inner = outer.get(standard.trim());
					if (inner != null) {
						if (inner.containsKey(assets)) {
							ccdp0010 = inner.get(assets);
							if (ccdp0010.getStateDesc().trim().equalsIgnoreCase(standard.trim()))
								table.addCell(getCell(ccdp0010.getTotalOs()));
							total = total.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
						} else {
							table.addCell(getCell("0.00"));
						}
						if (flag) {
							flag = false;
						}
					} else {
						table.addCell(getCell("0.00"));
					}
				}
				table.addCell(getCell(decimalFormat.format(total)));
				grandAllTotal = grandAllTotal.add(total);

			}
			populateMisAndTotal(table, grandTotalMap, columns);
			table.addCell(getCell(decimalFormat.format(grandAllTotal)));

		} catch (Exception e) {
			logger.info(e.getMessage());
		} finally {
			closeAllConnection(resultSet, prepareStatement);
		}
		return table;
	}

	protected PdfPTable computePartNineVars(BranchInformation branchInformation, List<String> standards) {
		logger.info("Inside computePartNineVars():::::::::::" + branchInformation.getId().getBrcdBranchCd());
		Ccdp0010 ccdp0010 = null;
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;
		Map<String, Map<String, Ccdp0010>> outer = new HashMap<>();
		String[] columns = { StaticReportConstant.PUBLIC, StaticReportConstant.PRIORITY, "BANK", "OTHERS", "PUBLIC SEC A/Cs" };
		float[] a = { 20, 6, 6, 6, 6, 6, 6, 6 };
		PdfPTable table = new PdfPTable(a);
		table.setWidthPercentage(100);

		String text = " PART - II SECTORWISE CLASSIFICATION PROVISION ";
		table = populateTableHeader(table, branchInformation, text, a);
		table.addCell(getStateCell("STAT DESC"));
		for (String column : columns) {
			table.addCell(getCell(column));
		}
		table.addCell(getCell("MIS"));
		table.addCell(getCell("TOTAL"));
		Session session = staticReportSessionFactory.getCurrentSession();
		try {
			BigDecimal grandTotal = BigDecimal.ZERO;
			BigDecimal grandMisTotal = BigDecimal.ZERO;
			BigDecimal grandAllTotal = BigDecimal.ZERO;
			Map<String, BigDecimal> grandTotalMap = new HashMap<>();
			Map<String, Ccdp0010> inner = new HashMap<>();
			Connection connection = ((SessionImpl) session).connection();

			if (!branchInformation.isConsolidated()) {
				prepareStatement = connection
						.prepareStatement("SELECT CASE WHEN (A.SECTOR ='NA' OR A.SECTOR =' ') THEN 'OTHERS1' ELSE A.SECTOR END AS SECTOR , a.ASSET_SUBCLASS, c.asset_dec, SUM(a.tot_os) AS TOT_OS, ( SUM((b.TOTAL_TANG / 100 * PROV_SECURED)) + SUM( CASE WHEN (a.tot_os - b.TOTAL_TANG) >= 1 THEN ( (a.tot_os - b.TOTAL_TANG) / 100 * PROV_UNSECURED ) ELSE 0 END ) ) AS PROVISION FROM ccdp010 a, ccdp020 b, ccdp001 c WHERE a.BRCD_BANK_CD = b.BRCD_BANK_CD AND a.acct_no = b.acct_no AND c.sub_asset = a.ASSET_SUBCLASS AND a.BRCD_BANK_CD = ? AND a.BRANCH_CODE = ? GROUP BY a.SECTOR, a.ASSET_SUBCLASS, c.asset_dec ORDER BY 1 ,2");
				prepareStatement.setString(1, branchInformation.getId().getBrcdBankCd());
				prepareStatement.setString(2, branchInformation.getId().getBrcdBranchCd());
			} else {
				prepareStatement = connection
						.prepareStatement("SELECT CASE WHEN (A.SECTOR ='NA' OR A.SECTOR =' ') THEN 'OTHERS1' ELSE A.SECTOR END AS SECTOR , a.ASSET_SUBCLASS, c.asset_dec, SUM(a.tot_os) AS TOT_OS, ( SUM((b.TOTAL_TANG / 100 * PROV_SECURED)) + SUM( CASE WHEN (a.tot_os - b.TOTAL_TANG) >= 1 THEN ( (a.tot_os - b.TOTAL_TANG) / 100 * PROV_UNSECURED ) ELSE 0 END ) ) AS PROVISION FROM ccdp010 a, ccdp020 b, ccdp001 c WHERE a.BRCD_BANK_CD = b.BRCD_BANK_CD AND a.acct_no = b.acct_no AND c.sub_asset = a.ASSET_SUBCLASS AND a.BRCD_BANK_CD = ?  GROUP BY a.SECTOR, a.ASSET_SUBCLASS, c.asset_dec ORDER BY 1 ,2");
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
				table.addCell(getStateCell(standard));
				BigDecimal mistotal = BigDecimal.ZERO;
				BigDecimal total = BigDecimal.ZERO;
				boolean flag = true;
				for (String assets : columns) {
					inner = outer.get(standard.trim());
					if (inner != null) {
						if (inner.containsKey(assets)) {
							ccdp0010 = inner.get(assets);
							if (ccdp0010.getStateDesc().trim().equalsIgnoreCase(standard.trim()))
								table.addCell(getCell(ccdp0010.getTotalOs()));
							total = total.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
						} else {
							table.addCell(getCell("0.00"));
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
								mistotal = mistotal.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
								total = total.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
							}
							if (inner.containsKey(StaticReportConstant.PRIORITY)) {
								ccdp0010 = inner.get(StaticReportConstant.PRIORITY);
								mistotal = mistotal.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
								total = total.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
							}
							if (inner.containsKey("BANK")) {
								ccdp0010 = inner.get("BANK");
								mistotal = mistotal.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
								total = total.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
							}*/
							flag = false;
						}
					} else {
						table.addCell(getCell("0.00"));
					}
				}

				grandMisTotal = grandMisTotal.add(mistotal);
				grandAllTotal = grandAllTotal.add(total);

				// MIS COLUMN
				table.addCell(getCell(decimalFormat.format(mistotal)));
				table.addCell(getCell(decimalFormat.format(total)));
			}
			populateMisAndTotal(table, grandTotalMap, columns);
			table.addCell(getCell(decimalFormat.format(grandMisTotal)));
			table.addCell(getCell(decimalFormat.format(grandAllTotal)));

		} catch (Exception e) {
			logger.info(e.getMessage());
		} finally {
			closeAllConnection(resultSet, prepareStatement);
		}
		return table;
	}

	protected PdfPTable computePartEightVars(BranchInformation branchInformation, List<String> standards) {

		logger.info("Inside computePartEightVars():::::::::::" + branchInformation.getId().getBrcdBranchCd());
		Ccdp0010 ccdp0010 = null;
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;
		Map<String, Map<String, Ccdp0010>> outer = new HashMap<>();
		String[] columns = { "C&I", "SSI", "SBF", "AGR", "PER" };
		float[] a = { 20, 6, 6, 6, 6, 6, 6, 6 };
		PdfPTable table = new PdfPTable(a);
		table.setWidthPercentage(100);

		String text = " PART - I  SEGMENTWISE CLASSIFICATION PROVISION";
		table = populateTableHeader(table, branchInformation, text, a);
		table.addCell(getStateCell("STAT DESC"));
		for (String column : columns) {
			table.addCell(getCell(column));
		}
		table.addCell(getCell("MIS"));
		table.addCell(getCell("TOTAL"));
		Session session = staticReportSessionFactory.getCurrentSession();

		try {
			BigDecimal grandTotal = BigDecimal.ZERO;
			BigDecimal grandMisTotal = BigDecimal.ZERO;
			BigDecimal grandAllTotal = BigDecimal.ZERO;
			Map<String, BigDecimal> grandTotalMap = new HashMap<>();
			Map<String, Ccdp0010> inner = new HashMap<>();
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
				table.addCell(getStateCell(standard));
				BigDecimal mistotal = BigDecimal.ZERO;
				BigDecimal total = BigDecimal.ZERO;
				boolean flag = true;
				for (String assets : columns) {
					inner = outer.get(standard.trim());
					if (inner != null) {
						if (inner.containsKey(assets)) {
							ccdp0010 = inner.get(assets);
							if (ccdp0010.getStateDesc().trim().equalsIgnoreCase(standard.trim()))
								table.addCell(getCell(ccdp0010.getTotalOs()));
							total = total.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
						} else {
							table.addCell(getCell("0.00"));
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
						table.addCell(getCell("0.00"));
					}
				}

				grandMisTotal = grandMisTotal.add(mistotal);
				grandAllTotal = grandAllTotal.add(total);

				// MIS COLUMN
				table.addCell(getCell(decimalFormat.format(mistotal)));
				table.addCell(getCell(decimalFormat.format(total)));
			}
			populateMisAndTotal(table, grandTotalMap, columns);
			table.addCell(getCell(decimalFormat.format(grandMisTotal)));
			table.addCell(getCell(decimalFormat.format(grandAllTotal)));

		} catch (Exception e) {
			logger.info(e.getMessage());
		} finally {
			closeAllConnection(resultSet, prepareStatement);
		}

		return table;
	}

	protected PdfPTable computePartSevenVars(BranchInformation branchInformation, List<String> standards) {
		logger.info("Inside computePartSevenVars():::::::::::" + branchInformation.getId().getBrcdBranchCd());
		Ccdp0010 ccdp0010 = null;
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;
		Map<String, Map<String, Ccdp0010>> outer = new HashMap<>();
		String[] columns = { "C&I", "SSI", "SBF", "AGR", "PER" };
		float[] a = { 20, 6, 6, 6, 6, 6, 6, 6 };
		PdfPTable table = new PdfPTable(a);
		table.setWidthPercentage(100);

		String text = " PART - VI NO OF ACCOUNTS (SEGMENTWISE)";
		table = populateTableHeader(table, branchInformation, text, a);
		table.addCell(getStateCell("STAT DESC"));
		for (String column : columns) {
			table.addCell(getCell(column));
		}
		table.addCell(getCell("MIS"));
		table.addCell(getCell("Total"));
		Session session = staticReportSessionFactory.getCurrentSession();
		try {
			BigDecimal grandTotal = BigDecimal.ZERO;
			BigDecimal grandMisTotal = BigDecimal.ZERO;
			BigDecimal grandAllTotal = BigDecimal.ZERO;
			Map<String, BigDecimal> grandTotalMap = new HashMap<>();
			Map<String, Ccdp0010> inner = new HashMap<>();
			Connection connection = ((SessionImpl) session).connection();

			if (!branchInformation.isConsolidated()) {
				if (!branchInformation.isConsolidated()) {
					prepareStatement = connection.prepareStatement(" SELECT a.SEGMENT,a.ASSET_SUBCLASS, asset_dec, SUM(a.tot_os) AS TOT_OS, COUNT(*) AS NO_OF_AC FROM ccdp010 a, ccdp020 b, ccdp001 c WHERE a.BRCD_BANK_CD = b.BRCD_BANK_CD AND a.acct_no = b.acct_no AND c.sub_asset = a.ASSET_SUBCLASS AND a.BRCD_BANK_CD = ? AND a.BRANCH_CODE = ? GROUP BY a.SEGMENT ,a.ASSET_SUBCLASS, asset_dec ORDER BY 1, 2");
					prepareStatement.setString(1, branchInformation.getId().getBrcdBankCd());
					prepareStatement.setString(2, branchInformation.getId().getBrcdBranchCd());
				} else {
					prepareStatement = connection.prepareStatement(" SELECT a.SEGMENT,a.ASSET_SUBCLASS, asset_dec, SUM(a.tot_os) AS TOT_OS, COUNT(*) AS NO_OF_AC FROM ccdp010 a, ccdp020 b, ccdp001 c WHERE a.BRCD_BANK_CD = b.BRCD_BANK_CD AND a.acct_no = b.acct_no AND c.sub_asset = a.ASSET_SUBCLASS AND a.BRCD_BANK_CD = ? GROUP BY a.SEGMENT ,a.ASSET_SUBCLASS, asset_dec ORDER BY 1, 2");
					prepareStatement.setString(1, branchInformation.getId().getBrcdBankCd());
				}
				resultSet = prepareStatement.executeQuery();
			}
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
				table.addCell(getStateCell(standard));
				BigDecimal mistotal = BigDecimal.ZERO;
				BigDecimal total = BigDecimal.ZERO;
				boolean flag = true;
				for (String assets : columns) {
					inner = outer.get(standard.trim());
					if (inner != null) {
						if (inner.containsKey(assets)) {
							ccdp0010 = inner.get(assets);
							if (ccdp0010.getStateDesc().trim().equalsIgnoreCase(standard.trim()))
								table.addCell(getCell(ccdp0010.getTotalOs()));
							total = total.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
						} else {
							table.addCell(getCell("0.00"));
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
						table.addCell(getCell("0.00"));
					}
				}

				grandMisTotal = grandMisTotal.add(mistotal);
				grandAllTotal = grandAllTotal.add(total);

				// MIS COLUMN
				table.addCell(getCell(decimalFormat.format(mistotal)));
				table.addCell(getCell(decimalFormat.format(total)));
			}
			populateMisAndTotal(table, grandTotalMap, columns);
			table.addCell(getCell(decimalFormat.format(grandMisTotal)));
			table.addCell(getCell(decimalFormat.format(grandAllTotal)));

		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
		} finally {
			closeAllConnection(resultSet, prepareStatement);
		}

		return table;
	}

	protected PdfPTable computePartSixVars(BranchInformation branchInformation) {

		logger.info("Inside computePartSixVars():::::::::::" + branchInformation.getId().getBrcdBranchCd());
		Ccdp0010 ccdp0010 = null;
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;
		Map<String, Map<String, Ccdp0010>> outer = new HashMap<>();
		Map<String, Ccdp0010> inner = new HashMap<>();
		String[] str = { "NO OF AC", "OUTSTANDING", "TANGIBLE", "  DICGC/BNK", "UNSECURED" };
		String[] assets = { "SSI", "SBF", "AGR", "C&I", "PER", "MIS" };
		String[] stds = { "Doubtful Asset – Less than 1 Year", "Doubtful Asset  = > 1 Year but < 3 Years", "Doubtful Asset  = > 3 Years" };

		float[] a = { 10, 12, 6, 6, 6, 6, 6 };
		PdfPTable table = new PdfPTable(a);
		table.setWidthPercentage(100);

		String text = "PART - V SEGMENTWISE/AGEWISE/SECURITYWISE CLASSIFICATION OF DOUBTFUL ASSETS ";
		table = populateTableHeader(table, branchInformation, text, a);
		table.addCell(getStateCell("ASSETS"));
		table.addCell(getStateCell("STAT DESC"));
		for (String column : str) {
			table.addCell(getCell(column));
		}
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
				BigDecimal sumofnoofac = BigDecimal.ZERO;
				BigDecimal sumofoutstanding = BigDecimal.ZERO;
				BigDecimal sumoftangible = BigDecimal.ZERO;
				BigDecimal sumbnkgvt = BigDecimal.ZERO;
				BigDecimal sumunsecured = BigDecimal.ZERO;
				if (outer.containsKey(asset)) {
					inner = outer.get(asset);
					if (inner != null) {
						populateContainData(asset, table, inner, stds, ccdp0010, sumofnoofac, sumofoutstanding, sumoftangible, sumbnkgvt, sumunsecured);
					}
				} else {
					for (String std : stds) {
						table.addCell(getStateCell(asset));
						table.addCell(getStateCell(std));
						table.addCell(getCell("0.00"));
						table.addCell(getCell("0.00"));
						table.addCell(getCell("0.00"));
						table.addCell(getCell("0.00"));
						table.addCell(getCell("0.00"));
					}
					addSubTotal(table, sumofnoofac, sumofoutstanding, sumoftangible, sumbnkgvt, sumunsecured);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}finally {
			closeAllConnection(resultSet, prepareStatement);
		}
		return table;
	}

	private void populateContainData(String asset, PdfPTable table, Map<String, Ccdp0010> inner, String[] stds, Ccdp0010 ccdp0010, BigDecimal sumofnoofac, BigDecimal sumofoutstanding, BigDecimal sumoftangible, BigDecimal sumbnkgvt, BigDecimal sumunsecured) {
		for (String std : stds) {
			if (inner.containsKey(std)) {
				ccdp0010 = inner.get(std);
				sumofnoofac = sumofnoofac.add(new BigDecimal(ccdp0010.getNoOFAc()));
				sumofoutstanding = sumofoutstanding.add(new BigDecimal(ccdp0010.getOutstandings()));
				sumoftangible = sumoftangible.add(new BigDecimal(ccdp0010.getTotalOs()));
				sumbnkgvt = sumbnkgvt.add(new BigDecimal(ccdp0010.getBnkgovecg()));
				sumunsecured = sumunsecured.add(new BigDecimal(ccdp0010.getUnsecured()));
				table.addCell(getStateCell(asset));
				table.addCell(getStateCell(std));
				table.addCell(getCell(decimalFormat.format(new BigDecimal(ccdp0010.getNoOFAc()))));
				table.addCell(getCell(decimalFormat.format(new BigDecimal(ccdp0010.getOutstandings()))));
				table.addCell(getCell(decimalFormat.format(new BigDecimal(ccdp0010.getTotalOs()))));
				table.addCell(getCell(decimalFormat.format(new BigDecimal(ccdp0010.getBnkgovecg()))));
				table.addCell(getCell(decimalFormat.format(new BigDecimal(ccdp0010.getUnsecured()))));
			} else {
				table.addCell(getStateCell(asset));
				table.addCell(getStateCell(std));
				table.addCell(getCell("0.00"));
				table.addCell(getCell("0.00"));
				table.addCell(getCell("0.00"));
				table.addCell(getCell("0.00"));
				table.addCell(getCell("0.00"));
			}
		}

		addSubTotal(table, sumofnoofac, sumofoutstanding, sumoftangible, sumbnkgvt, sumunsecured);

	}

	private void addSubTotal(PdfPTable table, BigDecimal sumofnoofac, BigDecimal sumofoutstanding, BigDecimal sumoftangible, BigDecimal sumbnkgvt, BigDecimal sumunsecured) {
		table.addCell(getStateCell("SUB TOTAL"));
		table.addCell(getCell("--"));
		table.addCell(getCell(decimalFormat.format(sumofnoofac)));
		table.addCell(getCell(decimalFormat.format(sumofoutstanding)));
		table.addCell(getCell(decimalFormat.format(sumoftangible)));
		table.addCell(getCell(decimalFormat.format(sumbnkgvt)));
		table.addCell(getCell(decimalFormat.format(sumunsecured)));
	}

	protected PdfPTable computePartFiveVars(BranchInformation branchInformation, List<String> standards) {
		logger.info("Inside computePartFiveVars():::::::::::" + branchInformation.getId().getBrcdBranchCd());
		Ccdp0010 ccdp0010 = null;
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;
		String[] columns = { "TOT TANGIBLE", "DICGC", "BANKS/GOVT/ECGC", "UNSECURED" };
		String[] grandTotalColumns = { "TOTAL_TANG", "DICGC", "BNK_GOV_ECG", "UNSECURED" };
		float[] a = { 16, 6, 6, 6, 6, 6 };
		PdfPTable table = new PdfPTable(a);
		table.setWidthPercentage(100);

		String text = "PART - IV SECURITYWISE CLASSIFICATION ";
		table = populateTableHeader(table, branchInformation, text, a);
		table.addCell(getStateCell("ASSETS"));
		for (String column : columns) {
			table.addCell(getCell(column));
		}
		table.addCell(getCell("TOTAL"));
		BigDecimal grandTotalOfTotal = BigDecimal.ZERO;
		try {
			Map<String, BigDecimal> grandTotalMap = new HashMap<>();
			Map<String, Ccdp0010> inner = new HashMap<>();
			Session session = staticReportSessionFactory.getCurrentSession();
			Connection connection = ((SessionImpl) session).connection();
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
				table.addCell(getStateCell(standard));
				BigDecimal total = BigDecimal.ZERO;
				if (inner.containsKey(standard)) {
					ccdp0010 = inner.get(standard.trim());
					table.addCell(getCell(ccdp0010.getTotalOs()));
					table.addCell(getCell(ccdp0010.getDicgc()));
					table.addCell(getCell(ccdp0010.getBnkgovecg()));
					table.addCell(getCell(ccdp0010.getUnsecured()));
					total = total.add(new BigDecimal(ccdp0010.getTotalOs().trim()).add(new BigDecimal(ccdp0010.getDicgc().trim())).add(new BigDecimal(ccdp0010.getBnkgovecg().trim()))
							.add(new BigDecimal(ccdp0010.getUnsecured().trim())).add(new BigDecimal(ccdp0010.getNoOFAc().trim())));
					grandTotalOfTotal = grandTotalOfTotal.add(total);
				} else {
					table.addCell(getCell("0.00"));
					table.addCell(getCell("0.00"));
					table.addCell(getCell("0.00"));
					table.addCell(getCell("0.00"));

				}
				table.addCell(getCell(decimalFormat.format(total)));
			}
			populateMisAndTotal(table, grandTotalMap, grandTotalColumns);
			table.addCell(getCell(decimalFormat.format(grandTotalOfTotal)));

		} catch (Exception e) {
			logger.error(e.getMessage());
		}finally {
			closeAllConnection(resultSet, prepareStatement);
		}
		return table;
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

	protected PdfPTable computePartFourVars(BranchInformation branchInformation, List<String> standards) {
		logger.info("Inside computePartFourVars():::::::::::" + branchInformation.getId().getBrcdBranchCd());
		Ccdp0010 ccdp0010 = null;
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;
		Map<String, Map<String, Ccdp0010>> outer = new HashMap<>();
		String[] columns = { "CC/RA/ACC/OD/DL", "TL/ATL", "PB" };
		float[] a = { 16, 6, 6, 6, 6 };
		PdfPTable table = new PdfPTable(a);
		table.setWidthPercentage(100);

		String text = "PART - III (A) SUMMARY OF FACILITYWISE CLASSIFICATION";
		table = populateTableHeader(table, branchInformation, text, a);
		table.addCell(getStateCell("ASSETS"));
		for (String column : columns) {
			table.addCell(getCell(column));
		}
		table.addCell(getCell("TOTAL"));

		try {
			BigDecimal grandTotalOfTotal = BigDecimal.ZERO;
			Map<String, BigDecimal> grandTotalMap = new HashMap<>();
			Map<String, Ccdp0010> inner = new HashMap<>();
			Session session = staticReportSessionFactory.getCurrentSession();
			Connection connection = ((SessionImpl) session).connection();

			if (!branchInformation.isConsolidated()) {
				prepareStatement = connection.prepareStatement("SELECT A.FACILITY_TYPE, A.ASSET_SUBCLASS, asset_dec, SUM(A.tot_os) AS TOT_OS, COUNT(*) AS NO_OF_AC FROM ccdp010 a, ccdp020 b, ccdp001 c WHERE a.BRCD_BANK_CD = b.BRCD_BANK_CD AND a.acct_no = b.acct_no AND c.sub_asset = a.ASSET_SUBCLASS AND a.BRCD_BANK_CD = ? AND a.BRANCH_CODE = ? GROUP BY A.FACILITY_TYPE, A.ASSET_SUBCLASS, asset_dec ORDER BY 2, 1");
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

			}
			for (String standard : standards) {
				table.addCell(getStateCell(standard));
				boolean flag = true;
				BigDecimal mistotal = BigDecimal.ZERO;
				for (String assets : columns) {
					BigDecimal total = BigDecimal.ZERO;
					BigDecimal grandTotal = BigDecimal.ZERO;
					inner = outer.get(standard.trim());
					if (inner != null) {
						String[] temp = assets.split("/");
						if (temp.length == 1) {
							if (inner.containsKey(assets)) {
								ccdp0010 = inner.get(assets);

								if (ccdp0010.getStateDesc().trim().equalsIgnoreCase(standard.trim()))
									table.addCell(getCell(ccdp0010.getTotalOs()));

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
						table.addCell(getCell(total.toString()));
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
						table.addCell(getCell("0.00"));
					}
				}
				table.addCell(getCell(decimalFormat.format(mistotal)));
				grandTotalOfTotal = grandTotalOfTotal.add(mistotal);

			}
			populateMisAndTotal(table, grandTotalMap, columns);
			table.addCell(getCell(decimalFormat.format(grandTotalOfTotal)));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}finally {
			closeAllConnection(resultSet, prepareStatement);
		}
		return table;
	}

	protected PdfPTable computePartThreeVars(BranchInformation branchInformation, List<String> standards) throws DocumentException {
		logger.info("Inside computePartThreeVars():::::::::::" + branchInformation.getId().getBrcdBranchCd());

		Ccdp0010 ccdp0010 = null;
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;
		Map<String, Map<String, Ccdp0010>> outer = new HashMap<>();
		String[] columns = { "CC", "ACC", "OD", "TL", "ATL", "DL", "OD", "BI", "PB", "RD" };
		float[] a = { 20, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6 };
		PdfPTable table = new PdfPTable(a);
		table.setWidthPercentage(100);

		String text = "PART - III  FACILITYWISE CLASSIFICATION";
		table = populateTableHeader(table, branchInformation, text, a);
		table.addCell(getStateCell("ASSETS"));
		for (String column : columns) {
			table.addCell(getCell(column));
		}
		table.addCell(getCell("TOTAL"));
		try {
			BigDecimal grandTotal = BigDecimal.ZERO;
			BigDecimal grandTotalOfTotal = BigDecimal.ZERO;
			Map<String, BigDecimal> grandTotalMap = new HashMap<>();
			Map<String, Ccdp0010> inner = new HashMap<>();
			Session session = staticReportSessionFactory.getCurrentSession();
			Connection connection = ((SessionImpl) session).connection();

			if (!branchInformation.isConsolidated()) {
				prepareStatement = connection.prepareStatement("SELECT A.FACILITY_TYPE, A.ASSET_SUBCLASS, asset_dec, SUM(A.tot_os) AS TOT_OS, COUNT(*) AS NO_OF_AC FROM ccdp010 a, ccdp020 b, ccdp001 c WHERE a.BRCD_BANK_CD = b.BRCD_BANK_CD AND a.acct_no = b.acct_no AND c.sub_asset = a.ASSET_SUBCLASS AND a.BRCD_BANK_CD = ? AND a.BRANCH_CODE = ? GROUP BY A.FACILITY_TYPE, A.ASSET_SUBCLASS, asset_dec ORDER BY 2, 1");
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
				table.addCell(getStateCell(standard));
				BigDecimal total = BigDecimal.ZERO;
				boolean flag = true;
				for (String assets : columns) {
					inner = outer.get(standard.trim());
					if (inner != null) {
						if (inner.containsKey(assets)) {
							ccdp0010 = inner.get(assets);
							if (ccdp0010.getStateDesc().trim().equalsIgnoreCase(standard.trim()))
								table.addCell(getCell(ccdp0010.getTotalOs()));
							total = total.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
						} else {
							table.addCell(getCell("0.00"));
						}
						if (flag) {
							flag = false;
						}
					} else {
						table.addCell(getCell("0.00"));
					}
				}
				table.addCell(getCell(decimalFormat.format(total)));
				grandTotalOfTotal = grandTotalOfTotal.add(total);
			}
			populateMisAndTotal(table, grandTotalMap, columns);
			table.addCell(getCell(decimalFormat.format(grandTotalOfTotal)));
		} catch (Exception e) {
			logger.info(e.getMessage());
		} finally {
			closeAllConnection(resultSet, prepareStatement);
		}

		return table;
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

	protected PdfPTable computePartOneVars(BranchInformation branchInformation, List<String> standards) throws DocumentException {
		logger.info("Inside computePartOneVars():::::::::::" + branchInformation.getId().getBrcdBranchCd());
		Ccdp0010 ccdp0010 = null;
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;
		Map<String, Map<String, Ccdp0010>> outer = new HashMap<>();
		String[] columns = { "C&I", "SSI", "SBF", "AGR", "PER" };
		float[] a = { 20, 6, 6, 6, 6, 6, 6, 6 };
		PdfPTable table = new PdfPTable(a);
		table.setWidthPercentage(100);

		String text = "PART - I  SEGMENTWISE CLASSIFICATION";
		table = populateTableHeader(table, branchInformation, text, a);
		table.addCell(getStateCell("STAT DESC"));
		for (String column : columns) {
			table.addCell(getCell(column));
		}
		table.addCell(getCell("MIS"));
		table.addCell(getCell("Total"));
		Session session = staticReportSessionFactory.getCurrentSession();

		try {
			BigDecimal grandTotal = BigDecimal.ZERO;
			BigDecimal grandMisTotal = BigDecimal.ZERO;
			BigDecimal grandAllTotal = BigDecimal.ZERO;
			Map<String, BigDecimal> grandTotalMap = new HashMap<>();

			Map<String, Ccdp0010> inner = new HashMap<>();
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
				table.addCell(getStateCell(standard));
				BigDecimal mistotal = BigDecimal.ZERO;
				BigDecimal total = BigDecimal.ZERO;
				boolean flag = true;
				for (String assets : columns) {
					inner = outer.get(standard.trim());
					if (inner != null) {
						if (inner.containsKey(assets)) {
							ccdp0010 = inner.get(assets);
							if (ccdp0010.getStateDesc().trim().equalsIgnoreCase(standard.trim()))
								table.addCell(getCell(ccdp0010.getTotalOs()));
							total = total.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
						} else {
							table.addCell(getCell("0.00"));
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
						table.addCell(getCell("0.00"));
					}
				}

				grandMisTotal = grandMisTotal.add(mistotal);
				grandAllTotal = grandAllTotal.add(total);

				// MIS COLUMN
				table.addCell(getCell(decimalFormat.format(mistotal)));
				table.addCell(getCell(decimalFormat.format(total)));
			}
			populateMisAndTotal(table, grandTotalMap, columns);
			table.addCell(getCell(decimalFormat.format(grandMisTotal)));
			table.addCell(getCell(decimalFormat.format(grandAllTotal)));

		} catch (Exception e) {
			logger.info(e.getMessage());
		} finally {
			closeAllConnection(resultSet, prepareStatement);
		}
		return table;

	}

	protected void populateMisAndTotal(PdfPTable table, Map<String, BigDecimal> grandTotalMap, String[] columns) {
		table.addCell(getStateCell("Grand Total"));
		for (String column : columns) {
			if (grandTotalMap != null) {
				if (grandTotalMap.get(column) != null) {
					table.addCell(getCell(decimalFormat.format(grandTotalMap.get(column))));
				} else {
					table.addCell(getCell("0.00"));
				}
			}
		}
	}

	protected PdfPTable computePartTwoVars(BranchInformation branchInformation, List<String> standards) {
		logger.info("Inside computePartTwoVars():::::::::::" + branchInformation.getId().getBrcdBranchCd());

		Ccdp0010 ccdp0010 = null;
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;
		Map<String, Map<String, Ccdp0010>> outer = new HashMap<>();
		String[] columns = { StaticReportConstant.PUBLIC, StaticReportConstant.PRIORITY, "BANK" };
		float[] a = { 16, 6, 6, 6, 6, 6 };
		PdfPTable table = new PdfPTable(a);
		table.setWidthPercentage(100);

		String text = "PART - II SECTORWISE CLASSIFICATION";
		table = populateTableHeader(table, branchInformation, text, a);
		table.addCell(getStateCell("STAT DESC"));
		for (String column : columns) {
			table.addCell(getCell(column));
		}
		table.addCell(getCell("OTHERS"));
		table.addCell(getCell("TOTAL"));
		Session session = staticReportSessionFactory.getCurrentSession();
		Connection connection = ((SessionImpl) session).connection();
		try {
			BigDecimal grandTotal = BigDecimal.ZERO;
			BigDecimal grandMisTotal = BigDecimal.ZERO;
			BigDecimal grandAllTotal = BigDecimal.ZERO;
			Map<String, BigDecimal> grandTotalMap = new HashMap<>();
			Map<String, Ccdp0010> inner = new HashMap<>();
			if (!branchInformation.isConsolidated()) {
				prepareStatement = connection.prepareStatement("SELECT CASE WHEN (a.SECTOR = 'NA' OR a.SECTOR = ' ') THEN 'OTHERS1' ELSE a.SECTOR END AS SECTOR, a.ASSET_SUBCLASS, asset_dec, SUM(a.tot_os) AS TOT_OS, COUNT(*) AS NO_OF_AC FROM ccdp010 a, ccdp020 b, ccdp001 c WHERE a.BRCD_BANK_CD = b.BRCD_BANK_CD AND a.acct_no = b.acct_no AND c.sub_asset = a.ASSET_SUBCLASS AND a.BRCD_BANK_CD = ? AND a.BRANCH_CODE = ? GROUP BY a.SECTOR, a.ASSET_SUBCLASS, asset_dec ORDER BY 2, 1");
				prepareStatement.setString(1, branchInformation.getId().getBrcdBankCd());
				prepareStatement.setString(2, branchInformation.getId().getBrcdBranchCd());
			} else {
				prepareStatement = connection.prepareStatement("SELECT CASE WHEN (a.SECTOR ='NA' OR a.SECTOR =' ') THEN 'OTHERS1' ELSE a.SECTOR END AS SECTOR , a.ASSET_SUBCLASS, asset_dec, SUM(a.tot_os) AS TOT_OS, COUNT(*) AS NO_OF_AC FROM ccdp010 a, ccdp020 b, ccdp001 c WHERE a.BRCD_BANK_CD = b.BRCD_BANK_CD AND a.acct_no = b.acct_no AND c.sub_asset = a.ASSET_SUBCLASS AND a.BRCD_BANK_CD =?  GROUP BY a.SECTOR,a.ASSET_SUBCLASS , asset_dec ORDER BY 2,1");
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
				table.addCell(getStateCell(standard));
				BigDecimal mistotal = BigDecimal.ZERO;
				BigDecimal total = BigDecimal.ZERO;
				boolean flag = true;
				for (String assets : columns) {
					inner = outer.get(standard.trim());
					if (inner != null) {
						if (inner.containsKey(assets)) {
							ccdp0010 = inner.get(assets);
							if (ccdp0010.getStateDesc().trim().equalsIgnoreCase(standard.trim()))
								table.addCell(getCell(ccdp0010.getTotalOs()));
							total = total.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
						} else {
							table.addCell(getCell("0.00"));
						}
						if (flag) {
							/*if (inner.containsKey(StaticReportConstant.PUBLIC)) {
								ccdp0010 = inner.get(StaticReportConstant.PUBLIC);
								mistotal = mistotal.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
								total = total.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
							}
							if (inner.containsKey(StaticReportConstant.PRIORITY)) {
								ccdp0010 = inner.get(StaticReportConstant.PRIORITY);
								mistotal = mistotal.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
								total = total.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
							}
							if (inner.containsKey("BANK")) {
								ccdp0010 = inner.get("BANK");
								mistotal = mistotal.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
								total = total.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
							}
							if (inner.containsKey("OTHERS")) {
								ccdp0010 = inner.get("OTHERS");
								mistotal = mistotal.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
								total = total.add(new BigDecimal(ccdp0010.getTotalOs().trim()));
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
						table.addCell(getCell("0.00"));
					}
				}

				grandMisTotal = grandMisTotal.add(mistotal);
				grandAllTotal = grandAllTotal.add(total);

				// MIS COLUMN
				table.addCell(getCell(decimalFormat.format(mistotal)));
				table.addCell(getCell(decimalFormat.format(total)));
			}
			populateMisAndTotal(table, grandTotalMap, columns);
			table.addCell(getCell(decimalFormat.format(grandMisTotal)));
			table.addCell(getCell(decimalFormat.format(grandAllTotal)));

		} catch (Exception e) {
			logger.info(e.getMessage());
		} finally {
			closeAllConnection(resultSet, prepareStatement);
		}
		return table;

	}

	protected PdfPTable populateTableHeader(PdfPTable table, BranchInformation branchInformation, String text, float[] a) {
		String branchname = branchInformation.getBrcdBranchName() != null ? branchInformation.getBrcdBranchName() : " ";
		String region = branchInformation.getBrcdRegionalId() != null ? branchInformation.getBrcdRegionalId() : " ";
		String zone = branchInformation.getBrcdZonalId() != null ? branchInformation.getBrcdZonalId() : " ";
		String branccd = branchInformation.getId().getBrcdBranchCd() != null ? branchInformation.getId().getBrcdBranchCd().trim() : " ";
		table.addCell(addTableHeader(StringUtils.rightPad("", 45),a.length, PdfPCell.NO_BORDER));
		table.addCell(addTableHeader(StringUtils.rightPad("SA - 5", 45) + StringUtils.leftPad("BRANCH NAME: " + branchname.trim() + "    REGION:  " + region + "     ZONE: " + zone + "   CIRCLE: XXXXXX ", 40), a.length, PdfPCell.NO_BORDER));
		table.addCell(addTableHeader("BRANCH:" + branccd, a.length, PdfPCell.NO_BORDER));
		table.addCell(addTableHeader(MAINHEADING, a.length, PdfPCell.NO_BORDER));
		table.addCell(getTableHeaderCell(text, a.length));
		return table;
	}

}

class MyFooter1 extends PdfPageEventHelper {
	Font ffont = new Font(Font.FontFamily.UNDEFINED, 5, Font.ITALIC);
	String bankName = null;

	@Override
	public void onEndPage(PdfWriter writer, Document document) {
		Phrase header = null;
		PdfContentByte cb = writer.getDirectContent();
		Phrase footer = new Phrase("Page " + writer.getPageNumber(), ffont);
		ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, header, (document.right() - document.left()) / 2 + document.leftMargin(), document.top() + 10, 0);
		ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, footer, (document.right() - document.left()) / 2 + document.leftMargin(), document.bottom() - 10, 0);

	}
}
