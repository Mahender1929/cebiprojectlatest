package com.cebi.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.internal.SessionImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebi.dao.StaticReportDaoImpl;
import com.cebi.entity.AuditHistory;
import com.cebi.entity.Banks;
import com.cebi.entity.StaticReports;
import com.cebi.entity.TableMetaData;
import com.cebi.entity.TellerMaster;
import com.cebi.utility.CebiConstant;
import com.cebi.utility.MappingConstant;

@Service
public class StaticReportAccServiceImpl implements StaticReportAccService {

	@Autowired
	CebiConstant cebiConstant;

	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	StaticReportDaoImpl staticReportDaoimpl;

	private static final Logger logger = Logger
			.getLogger(StaticReportAccServiceImpl.class);
	 LocalTime time = null;

	@Override
	public List<StaticReports> getDetailByAccountNo(
			StaticReports staticReports, String bankCode,TellerMaster tellerMaster) {
		logger.info("ENTER into getDetailByAccountNo():"+LocalTime.now().toString());
		Connection connection = null;
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;
		Session session = null;
		staticReports.setAccNum(staticReports.getAccNum().trim().substring(0, staticReports.getAccNum().length() - 1));
		staticReports.setAccNum(populateData(staticReports.getAccNum(), 16, 0));
		String query = MappingConstant.QUERYSCRIPT.replaceAll("XXXXXX",
				staticReports.getAccNum());
		query = query.replaceAll("ZZZZZZ", staticReports.getFrDate());
		query = query.replaceAll("YYYYYY", staticReports.getToDate());
		List<StaticReports> data = new ArrayList<>();
		try {
			session = cebiConstant.getCurrentSession(bankCode);
			connection = ((SessionImpl) session).connection();
			connection.setAutoCommit(false);
			SimpleDateFormat formatter = new SimpleDateFormat(
					"dd/MM/yyyy HH:mm:ss");
			Date date = new Date();
			String[] timDate = formatter.format(date).split(" ");
			
			prepareStatement = connection.prepareStatement(query);
			resultSet = prepareStatement.executeQuery();
			
			
			while (resultSet.next()) {
				StaticReports strpo = new StaticReports();
				strpo.setAccNum(resultSet.getString("ACCT_NO"));
				strpo.setTxCode(resultSet.getString("TRNCODE"));
				strpo.setBalance(resultSet.getString("BALANCE"));
				strpo.setCheqNo(resultSet.getString("CHQNO"));
				strpo.setMobNO(resultSet.getString("MOB_NO"));
				if (resultSet.getString("TRNDRCR").trim()
						.equalsIgnoreCase("CR")) {
					strpo.setCrdr("Credit");
					strpo.setTxAmount(resultSet.getString("TRNAMT"));
					strpo.setInstNo("");
				} else {
					strpo.setCrdr("Debit");
					strpo.setInstNo(resultSet.getString("TRNAMT"));
					strpo.setTxAmount("");
				}
				strpo.setNarration(resultSet.getString("NARRATION")
						.replace("    ", " ").replace("   ", " ")
						.replace("  ", " ").replace("   ", " ")
						.replace("  ", " "));
				strpo.setCurrBalace(resultSet.getString("CURR_BAL"));
				strpo.setTxDate(resultSet.getString("TRAN_DATE").substring(0,
						10));
				strpo.setPostTime(resultSet.getString("POST_DATE").substring(0,
						10));
				strpo.setTlrBranch(resultSet.getString("BRANCH_NO"));
				strpo.setCustName(resultSet.getString("CUSTOMER_NAME"));
				strpo.setCustNo(resultSet.getString("CUSTOMER_NO"));
				strpo.setPhBusNo(resultSet.getString("PHONE_NO_BUS"));
				strpo.setJournalNo(resultSet.getString("BRANCH_DETAILS"));
				strpo.setAddress(resultSet.getString("ADD1")
						.replace("    ", " ").replace("   ", " ")
						.replace("  ", " ").replace("   ", " ")
						.replace("  ", " "));
				strpo.setCurrDate(timDate[0]);
				strpo.setCurrTime(timeConversion(timDate[1]));
				data.add(strpo);
			}
			if (!(data.size() == 0)) {
				float totalcredit = 0;
				float totaldebit = 0;
				int countCr = 0;
				int countDr = 0;
				for (StaticReports staticReport : data) {
					if (!staticReport.getTxAmount().equalsIgnoreCase("")) {
						totalcredit = totalcredit
								+ Float.parseFloat(staticReport.getTxAmount()
										.replaceAll("-", "").trim());
						countCr++;
					} else if (!staticReport.getInstNo().equalsIgnoreCase("")) {
						totaldebit = totaldebit
								+ Float.parseFloat(staticReport.getInstNo()
										.replaceAll("-", "").trim());
						countDr++;
					}
				}
				long totlcr=(long)totalcredit;
				long totldr=(long)totaldebit;
				for (StaticReports staticReport : data) {
					
					staticReport.setTotalCr(Long.toString(totlcr));
					staticReport.setTotalDr(Long.toString(totldr));
					staticReport.setCrCount(Integer.toString(countCr));
					staticReport.setDrCount(Integer.toString(countDr));
					if (staticReport.getMobNO().trim().length() < 5)
						staticReport.setMobNO("XXXXXXXXXX");
				}
			}

		} catch (Exception e) {
			TableMetaData tableDataErr = new TableMetaData();
			tableDataErr.setName("Error : " + e.getMessage());
			logger.info(e.getMessage());
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) { /* ignored */
				}
			}
			if (prepareStatement != null) {
				try {
					prepareStatement.close();
				} catch (SQLException e) { /* ignored */
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) { /* ignored */
				}
			}
		}
		
			
			staticReportDaoimpl.populateAuditHistory( "OLD@RRBINCT",  tellerMaster,  query);
		
		logger.info("Exit From getDetailByAccountNo():"+LocalTime.now().toString());
		return data;

	}

	public String populateData(String value, int size, Object val) {
		int dataSize = value.length();
		int totalsize = size - dataSize;
		String result = "";
		for (int i = 1; i <= totalsize; i++) {
			result += val;
		}
		if (val instanceof Integer) {
			return result + value;
		} else {
			return value + result;
		}

	}

	public static String timeConversion(String _24HourTime) {
		try {
			/* String _24HourTime = "22:15"; */
			SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm:ss");
			SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
			Date _24HourDt = _24HourSDF.parse(_24HourTime);
			_24HourTime = _12HourSDF.format(_24HourDt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return _24HourTime;

	}

	@Transactional
	@SuppressWarnings("unchecked")
	public List<Banks> getBankNames(String code) {
		logger.info("ENTER into getBankNames():"+LocalTime.now().toString());
		List<Banks> banks = new ArrayList<>();
		Query query = sessionFactory.getCurrentSession().createQuery(
				"FROM Banks WHERE bankCode= :bankCode");
		query.setParameter("bankCode", code);
		banks = (List<Banks>) query.list();
		logger.info("Exit From getBankNames():"+LocalTime.now().toString());
		return banks;
	}

	@Override
	public List<StaticReports> getDepositStmtDetailyByAccountNo(
			StaticReports staticReports, String bankCode,
			TellerMaster tellerMaster) {

		logger.info("ENTER into getDepositStmtDetailyByAccountNo():"+LocalTime.now().toString());
		Connection connection = null;
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;
		Session session = null;
		staticReports.setAccNum(staticReports.getAccNum().trim().substring(0, staticReports.getAccNum().length() - 1));
		staticReports.setAccNum(populateData(staticReports.getAccNum(), 16, 0));
		String query = MappingConstant.DEPOSITSTMTQUERYSCRIPT.replaceAll("XXXXXX",
				staticReports.getAccNum());
		query = query.replaceAll("ZZZZZZ", staticReports.getFrDate());
		query = query.replaceAll("YYYYYY", staticReports.getToDate());
		List<StaticReports> data = new ArrayList<>();
		try {
			session = cebiConstant.getCurrentSession(bankCode);
			connection = ((SessionImpl) session).connection();
			connection.setAutoCommit(false);
			SimpleDateFormat formatter = new SimpleDateFormat(
					"dd/MM/yyyy HH:mm:ss");
			Date date = new Date();
			String[] timDate = formatter.format(date).split(" ");
			logger.info("INCT query : "+query);
			prepareStatement = connection.prepareStatement(query);
			resultSet = prepareStatement.executeQuery();
			
			
			while (resultSet.next()) {
				StaticReports strpo = new StaticReports();
				strpo.setAccNum(resultSet.getString("ACCT_NO"));
				strpo.setTxCode(resultSet.getString("TRNCODE"));
				strpo.setBalance(resultSet.getString("BALANCE"));
				strpo.setCheqNo(resultSet.getString("CHQNO"));
				strpo.setMobNO(resultSet.getString("MOB_NO"));
				if (resultSet.getString("TRNDRCR").trim()
						.equalsIgnoreCase("CR")) {
					strpo.setCrdr("Credit");
					strpo.setTxAmount(resultSet.getString("TRNAMT"));
					strpo.setInstNo("");
				} else {
					strpo.setCrdr("Debit");
					strpo.setInstNo(resultSet.getString("TRNAMT"));
					strpo.setTxAmount("");
				}
				strpo.setNarration(resultSet.getString("NARRATION")
						.replace("    ", " ").replace("   ", " ")
						.replace("  ", " ").replace("   ", " ")
						.replace("  ", " "));
				strpo.setCurrBalace(resultSet.getString("CURR_BAL"));
				strpo.setTxDate(resultSet.getString("TRAN_DATE").substring(0,
						10));
				strpo.setPostTime(resultSet.getString("POST_DATE").substring(0,
						10));
				strpo.setTlrBranch(resultSet.getString("BRANCH_NO"));
				strpo.setCustName(resultSet.getString("CUSTOMER_NAME"));
				strpo.setCustNo(resultSet.getString("CUSTOMER_NO"));
				strpo.setPhBusNo(resultSet.getString("PHONE_NO_BUS"));
				strpo.setJournalNo(resultSet.getString("BRANCH_DETAILS"));
				strpo.setAddress(resultSet.getString("ADD1")
						.replace("    ", " ").replace("   ", " ")
						.replace("  ", " ").replace("   ", " ")
						.replace("  ", " "));
				strpo.setCurrDate(timDate[0]);
				strpo.setCurrTime(timeConversion(timDate[1]));
				data.add(strpo);
			}
			if (!(data.size() == 0)) {
				float totalcredit = 0;
				float totaldebit = 0;
				int countCr = 0;
				int countDr = 0;
				for (StaticReports staticReport : data) {
					if (!staticReport.getTxAmount().equalsIgnoreCase("")) {
						logger.info("txn amt"+staticReport.getTxAmount());
						System.out.println("txn amt"+staticReport.getTxAmount());
						totalcredit = totalcredit
								+ Float.parseFloat(staticReport.getTxAmount()
										.replaceAll("-", "").trim());
						countCr++;
					} else if (!staticReport.getInstNo().equalsIgnoreCase("")) {
						logger.info("txn amt"+staticReport.getInstNo());
						System.out.println("txn amt"+staticReport.getInstNo());
						totaldebit = totaldebit
								+ Float.parseFloat(staticReport.getInstNo()
										.replaceAll("-", "").trim());
						countDr++;
					}
				}
				for (StaticReports staticReport : data) {
					staticReport.setTotalCr(Float.toString(totalcredit));
					staticReport.setTotalDr(Float.toString(totaldebit));
					staticReport.setCrCount(Integer.toString(countCr));
					staticReport.setDrCount(Integer.toString(countDr));
					if (staticReport.getMobNO().trim().length() < 5)
						staticReport.setMobNO("XXXXXXXXXX");
				}
			}

		} catch (Exception e) {
			TableMetaData tableDataErr = new TableMetaData();
			tableDataErr.setName("Error : " + e.getMessage());
			logger.info(e.getMessage());
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) { /* ignored */
				}
			}
			if (prepareStatement != null) {
				try {
					prepareStatement.close();
				} catch (SQLException e) { /* ignored */
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) { /* ignored */
				}
			}
		}
		
			
			staticReportDaoimpl.populateAuditHistory( "OLD@RRBINCT",  tellerMaster,  query);
		
		logger.info("Exit From getDepositStmtDetailyByAccountNo():"+LocalTime.now().toString());
		return data;

	
	}
	
	

}
