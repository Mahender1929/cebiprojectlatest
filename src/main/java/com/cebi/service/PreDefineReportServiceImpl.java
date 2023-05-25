package com.cebi.service;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.internal.SessionImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import com.cebi.dao.AdminReportDao;
import com.cebi.entity.AppMessages;
import com.cebi.entity.ApplicationLabel;
import com.cebi.entity.ColumnNames;
import com.cebi.entity.PreDefineReport;
import com.cebi.entity.QueryData;
import com.cebi.entity.ReportQueueData;
import com.cebi.entity.TableMetaData;
import com.cebi.entity.TellerMaster;
import com.cebi.service.ApplicationLabelService;
import com.cebi.utility.Block;
import com.cebi.utility.Board;
import com.cebi.utility.CebiConstant;
import com.cebi.utility.MappingConstant;
import com.cebi.utility.Table;

@Transactional
@Service
public class PreDefineReportServiceImpl implements PredefineReportService {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	CebiConstant cebiConstant;

	@Autowired
	ApplicationLabelService applicationLabelService;

	@Autowired
	AdminReportDao adminReportDao;

	private static final Logger logger = Logger
			.getLogger(PreDefineReportServiceImpl.class);

	@Override
	public String saveDefReportObj(PreDefineReport PreDefineReport) {
		Session session = sessionFactory.openSession();
		Transaction tx1 = session.beginTransaction();
		int stId = (Integer) session.save(PreDefineReport);
		tx1.commit();
		return "Query Saved Successfully with QryID : [ " + stId + " ]";
	}

	@Override
	public List<PreDefineReport> getQryListByBank(String bankCode) {
		Session session = sessionFactory.openSession();
		Criteria crit = session.createCriteria(PreDefineReport.class);
		crit.add(Restrictions.eq("BankCod", bankCode));
		crit.add(Restrictions.eq("sts", "EXIST"));
		@SuppressWarnings("unchecked")
		List<PreDefineReport> results = crit.list();
		return results;

	}

	@Override
	public boolean isQuerySaved(PreDefineReport preDefineReport) {
		Session session = sessionFactory.openSession();
		Criteria crit = session.createCriteria(PreDefineReport.class);
		crit.add(Restrictions.eq("BankCod", preDefineReport.getBankCod()));
		crit.add(Restrictions.eq("sts", "EXIST"));
		@SuppressWarnings("unchecked")
		List<PreDefineReport> objList = crit.list();
		List<Boolean> isExistList = new ArrayList<Boolean>();
		for (PreDefineReport pdr : objList) {
			if (pdr.getSaveQuery().trim()
					.equals(preDefineReport.getSaveQuery().trim())) {
				isExistList.add(false);
			}
		}
		int i = isExistList.size();
		if (i == 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String deleteThisObject(PreDefineReport preDefineReport) {
		Session session = sessionFactory.openSession();
		PreDefineReport pdfr = (PreDefineReport) session.load(
				PreDefineReport.class, preDefineReport.getId());
		pdfr.setSts(preDefineReport.getSts());
		Transaction tx1 = session.beginTransaction();
		session.saveOrUpdate(pdfr);
		tx1.commit();
		return "| " + preDefineReport.getSaveQuery() + " |"
				+ " : Query is Removed...!";
	}

	@Override
	public List<TableMetaData> getTableDataListByBank(
			PreDefineReport preDefineReport, String bankCode) {
		Connection connection = null;
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;
		Session session = null;
		String query = null;
		ColumnNames field;
		List<ColumnNames> names = new ArrayList<>();
		List<AppMessages> appMessages = new ArrayList<>();
		List<TableMetaData> data = new ArrayList<>();
		TableMetaData tableMetaData = new TableMetaData();
		List<ApplicationLabel> labels = null;
		try {
			session = cebiConstant.getCurrentSession(bankCode);
			connection = ((SessionImpl) session).connection();
			connection.setAutoCommit(false);
			labels = applicationLabelService.retrieveAllLabels();
			SimpleDateFormat formatter1 = new SimpleDateFormat(
					"ddMMyyyy HH:mm:ss");
			Date date1 = new Date();
			logger.info("start ---" + formatter1.format(date1));
			if (tableMetaData.getAppMessage() == null
					|| tableMetaData.getAppMessage().size() == 0) {
				query = preDefineReport.getSaveQuery().replace(";", "").concat("AND ROWNUM<=100");
				logger.info(query +LocalTime.now().toString());
				prepareStatement = connection.prepareStatement(query);
				resultSet = prepareStatement.executeQuery();
				while (resultSet.next()) {
					tableMetaData = new TableMetaData();
					ResultSetMetaData rsmd = resultSet.getMetaData();
					int columnCount = rsmd.getColumnCount();
					for (int i = 1; i <= columnCount; i++) {
						String label = rsmd.getColumnName(i);
						label = label.contains("(") && label.contains(")") ? label
								.substring(label.indexOf('(') + 1,
										label.indexOf(')')) : label;
						field = new ColumnNames();
						if (resultSet.getString(label) == null
								|| resultSet.getString(label) == "")
							field.setField("");
						else
							field.setField(resultSet.getString(label));
						field.setName(addApplicationLabels(label, labels));
						names.add(field);
						tableMetaData.setNames(names);
					}
				}

				tableMetaData.setAppLabels(labels);
				data.add(tableMetaData);
				validateTableData(data, appMessages);
			}
		} catch (Exception e) {
			TableMetaData tableDataErr = new TableMetaData();
			tableDataErr.setName("Error : " + e.getMessage());
			data.add(tableDataErr);
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
		return data;
	}

	protected String addApplicationLabels(String label,
			List<ApplicationLabel> labels) {
		for (ApplicationLabel lbl : labels) {
			if (lbl.getLabelCode().equalsIgnoreCase(label)) {
				label = lbl.getAppLabel();
				break;
			}
		}
		return label;

	}

	private void validateTableData(List<TableMetaData> data,
			List<AppMessages> appMessages) {
		for (TableMetaData tableMetaData : data) {
			if (tableMetaData.getNames() == null
					|| tableMetaData.getNames().isEmpty()) {
				appMessages.add(new AppMessages("NO_RESULTS",
						CebiConstant.NO_RESULT));
				tableMetaData.setAppMessage(appMessages);
			}
		}
	}

	@Override
	public String getQueryString(String qry) {
		Pattern p = Pattern
				.compile(
						"from\\s+(?:\\w+\\.)*(\\w+)($|\\s+[WHERE,JOIN,START\\s+WITH,ORDER\\s+BY,GROUP\\s+BY])",
						Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(qry);
		String tableName = null;
		while (m.find())
			tableName = m.group(1);
		return tableName;
	}

	@Override
	public boolean getTableNameIsExist(List<TableMetaData> table,
			String tableName) {
		List<String> tableList = new ArrayList<String>();
		boolean sts = false;
		for (TableMetaData tableMetaData : table) {
			tableList.add(tableMetaData.getTableName());
		}
		for (String tblName : tableList) {
			if (tblName.equalsIgnoreCase(tableName)) {
				sts = true;
				break;
			}
		}
		return sts;
	}

	public Object getDataIntoFile(QueryData getTableData,
			String bank) {

		Connection connection = null;
		PreparedStatement prepareStatement = null;
		Statement statement = null;
		ResultSet resultSet = null;
		Session session = null;
		String query = null;
		List<AppMessages> appMessages = new ArrayList<>();
		List<TableMetaData> data = new ArrayList<>();
		TableMetaData tableMetaData = new TableMetaData();
		List<ApplicationLabel> labels = null;
		String filename = null;
		File fzip = null;
		try {
			session = cebiConstant.getCurrentSession(bank);
			connection = ((SessionImpl) session).connection();
			connection.setAutoCommit(false);
			labels = applicationLabelService.retrieveAllLabels();
			SimpleDateFormat formatter1 = new SimpleDateFormat(
					"ddMMyyyy HH:mm:ss");
			Date date1 = new Date();
			logger.info("start ---" + formatter1.format(date1));
			query = getTableData.getFinalQry();
			getTableData.setTable(getQueryString(query));
			if ("csvpr".equalsIgnoreCase(getTableData.getReporttype())) {
			    logger.info("query  --- >  " + query);
			    statement = (Statement) connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, // or
				    ResultSet.CONCUR_READ_ONLY);
			    statement.setFetchSize(5000);
			    resultSet = statement.executeQuery(query);
			    StringBuilder buffer = new StringBuilder();
			    ResultSetMetaData rsmd = resultSet.getMetaData();
			    int columnCount = rsmd.getColumnCount();
			    List<String> colummnName = new ArrayList<String>();
			    for (int i = 1; i <= columnCount; i++) {
				buffer.append(rsmd.getColumnName(i) + " ,  ");
				colummnName.add(rsmd.getColumnName(i));
			    }
			    int i = 0;
			    int j = 1;
			    // BufferedWriter bw = null;
			    FileWriter fw = null;

			    SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
			    Date date = new Date();
			    filename = getTableData.getReportDataId() + formatter.format(date);
			    // bw = new BufferedWriter(fw);
			    StringBuffer bw = new StringBuffer();
			    bw.append(buffer.toString());
			    while (resultSet.next()) {
				bw.append(CebiConstant.NEW_LINE);
				for (int k = 0; k < colummnName.size(); k++) {
				    String label = colummnName.get(k);
				    label = label.contains("(") && label.contains(")") ? label.substring(label.indexOf('(') + 1, label.indexOf(')')) : label;
				    if (resultSet.getString(label) == null || resultSet.getString(label).isEmpty()) {
					bw.append(StringUtils.rightPad(CebiConstant.EMPTY_SPACE, label.length())).append(",");
				    } else
					bw.append(StringUtils.rightPad(resultSet.getString(label).trim(), resultSet.getString(label).trim().length() - label.length())).append(",");
				    ;
				}
				i++;
				if (i % (j * 10000) == 0) {
				    j++;
				}
			    }
			    /*
			     * bw.close(); Date enddate = new Date(); ReportQueueData
			     * reportQueueData =
			     * getReportQueueData(getTableData.getReportDataId());
			     * reportQueueData.setFileName(filename);
			     * reportQueueData.setTimecomplete(enddate);
			     * reportQueueData.setTimetake(enddate.getTime() -
			     * date1.getTime() + "");
			     * reportQueueData.setStatus(CebiConstant.COMPLETED);
			     * reportQueueData.setTotalCount(i + "");
			     * updateReportQueueData(reportQueueData);
			     */
			//    populateAuditHistory(getTableData.getTable(), master1, query);
			    return String.valueOf(bw).getBytes();
			} else if ("txtpr".equalsIgnoreCase(getTableData.getReporttype())) {

			    int lgth = 0;
			    List<String> stringList = new ArrayList<>();
			    logger.info("query  --- >  " + query);
			    statement = (Statement) connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, // or
				    ResultSet.CONCUR_READ_ONLY);
			    statement.setFetchSize(5000);
			    resultSet = statement.executeQuery(query);
			    StringBuilder buffer = new StringBuilder();
			    ResultSetMetaData rsmd = resultSet.getMetaData();
			    int columnCount = rsmd.getColumnCount();
			    List<String> colummnName = new ArrayList<String>();
			    List<Integer> colTitleSize = new ArrayList<>();
			    for (int i = 1; i <= columnCount; i++) {
				colummnName.add(rsmd.getColumnName(i));
				colTitleSize.add(rsmd.getColumnName(i).length());
			    }

			    final List<List<String>> rowList = new LinkedList<List<String>>();
			    int i = 0;
			    List<Integer> arrySize = new ArrayList<>();

			    while (resultSet.next()) {
				final List<String> columnList = new LinkedList<String>();
				rowList.add(columnList);

				for (int column = 1; column <= columnCount; ++column) {
				    final Object value = resultSet.getObject(column);
				    columnList.add(String.valueOf(value).trim());
				}
				i++;
			    }

			    Map<String, List<Integer>> sizeMap = new HashMap<>();

			    for (int column = 0; column < columnCount; ++column) {
				List<Integer> len = new ArrayList<Integer>();
				for (int row = 0; row < rowList.size(); ++row) {
				    List<String> al = rowList.get(row);
				    String size = al.get(column).trim();
				    len.add(size.length());
				}
				sizeMap.put(colummnName.get(column), len);

			    }

			    for (int column = 0; column < columnCount; ++column) {
				List<Integer> sz = sizeMap.get(colummnName.get(column));
				int maxSizeCol = Collections.max(sz);
				arrySize.add(maxSizeCol);
			    }

			    List<Integer> colWd1 = new ArrayList<>();
			    for (int column = 0; column < columnCount; ++column) {
				if (arrySize.get(column) < colTitleSize.get(column)) {
				    colWd1.add(colTitleSize.get(column) + 1);
				} else {
				    colWd1.add(arrySize.get(column) + 1);
				}

			    }
			    Board board = new Board(1600);
			    Table table = new Table(board, 1600, colummnName, rowList);
			    table.setGridMode(Table.GRID_COLUMN);
			    List<Integer> colAl = new ArrayList<>();
			    for (int column = 1; column <= columnCount; ++column) {
				colAl.add(Block.DATA_TOP_LEFT);
			    }
			    // List<Integer> colWidthsList = Arrays.asList(20, 14, 13,
			    // 14, 14);
			    // List<Integer> colAlignList =
			    // Arrays.asList(Block.DATA_CENTER, Block.DATA_CENTER,
			    // Block.DATA_CENTER, Block.DATA_CENTER, Block.DATA_CENTER);
			    table.setColWidthsList(colWd1);
			    table.setColAlignsList(colAl);

			    Block tableBlock = table.tableToBlocks();
			    board.setInitialBlock(tableBlock);
			    board.build();
			    String tableString = board.getPreview();
			    String tabStr = tableString.replaceAll("null", "----");
			    /*
			     * System.out.println(tableString.length());
			     * System.out.println(tableString);
			     */
			    String[] lines = tabStr.split("\\s*\\r?\\n\\s*");
			    List al = Arrays.asList(lines);

			    StringBuilder buff = new StringBuilder();
			    for (int s = 0; s < al.size(); s++) {
				buff.append(al.get(s));
				buff.append("\r\n");
			    }
			    byte[] output = String.valueOf(buff).getBytes();
			 //   populateAuditHistory(getTableData.getTable(), master1, query);
			    return output;
			} else {
			    logger.info("query  --- >  " + query);
			    int colNum = 0;
			    int rowcnt = 1;
			    statement = (Statement) connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, // or
					    ResultSet.CONCUR_READ_ONLY);
				    statement.setFetchSize(5000);
			    resultSet = statement.executeQuery(query);
			    List<String> dbColumns = new ArrayList<String>();
			    ResultSetMetaData rsmd = resultSet.getMetaData();
			    int columnCount = rsmd.getColumnCount();
			    ByteArrayOutputStream byteq = new ByteArrayOutputStream();
			    Workbook workbook = new XSSFWorkbook(); // new
								    // HSSFWorkbook()
								    // for generating
								    // `.xls` file

			    /*
			     * CreationHelper helps us create instances of various
			     * things like DataFormat, Hyperlink, RichTextString etc, in
			     * a format (HSSF, XSSF) independent way
			     */
			    CreationHelper createHelper = workbook.getCreationHelper();
			    Font headerFont = workbook.createFont();
			    headerFont.setFontHeightInPoints((short) 14);
			    headerFont.setColor(IndexedColors.RED.getIndex());

			    // Create a CellStyle with the font
			    CellStyle headerCellStyle = workbook.createCellStyle();
			    headerCellStyle.setFont(headerFont);
			    Sheet sheet = workbook.createSheet("Employee");
			    Row headerRow = sheet.createRow(0);
			    dbColumns = new ArrayList<String>();
			    for (int i = 1; i <= columnCount; i++) {
				String labelName = rsmd.getColumnName(i);
				Cell cell = headerRow.createCell(colNum);
				cell.setCellStyle(headerCellStyle);
				cell.setCellValue(labelName);
				++colNum;
				dbColumns.add(labelName);
			    }
			    while (resultSet.next()) {
				Row row = null;
				Cell cell = null;
				colNum = 0;
				row = sheet.createRow(rowcnt);
				for (String label : dbColumns) {
				    label = label.contains("(") && label.contains(")") ? label.substring(label.indexOf('(') + 1, label.indexOf(')')) : label;
				    cell = row.createCell(colNum);
				    if (resultSet.getString(label) == null || resultSet.getString(label).isEmpty()) {
					cell.setCellValue("");
					System.out.print("");
				    } else {
					cell.setCellValue(resultSet.getString(label));
					System.out.print(resultSet.getString(label));
				    }
				    ++colNum;
				}
				++rowcnt;
				System.out.println();
			    }
			    workbook.write(byteq);
			    byte[] outArray = null;
			    outArray = byteq.toByteArray();
			 //   populateAuditHistory(getTableData.getTable(), master1, query);
			    return outArray;
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
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
		return  data;
	}

}
