package com.cebi.dao;

import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;




import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.internal.SessionImpl;
import org.springframework.aop.ThrowsAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cebi.entity.AppMessages;
import com.cebi.entity.ApplicationLabel;
import com.cebi.entity.AuditHistory;
import com.cebi.entity.Banks;
import com.cebi.entity.ColumnNames;
import com.cebi.entity.QueryData;
import com.cebi.entity.ReportQueueData;
import com.cebi.entity.RequiredField;
import com.cebi.entity.TableMetaData;
import com.cebi.entity.TellerMaster;
import com.cebi.entity.ViewInfo;
import com.cebi.service.ApplicationLabelService;
import com.cebi.service.GenerateCheckDigitService;
import com.cebi.utility.Block;
import com.cebi.utility.Board;
import com.cebi.utility.CebiConstant;
import com.cebi.utility.Constants;
import com.cebi.utility.PdfUtils;
import com.cebi.utility.Table;

@Repository
@Transactional
public class AdminReportDaoImpl extends PdfUtils implements AdminReportDao {

    @Autowired
    ApplicationLabelService applicationLabelService;

    @Autowired
    ApplicationLabelDao applicationLabelDao;

    @Autowired
    GenerateCheckDigitService generateCheckDigitService;

    @Autowired
    CebiConstant cebiConstant;

    @Autowired
    SessionFactory sessionFactory;

    private static final Logger logger = Logger.getLogger(AdminReportDaoImpl.class);

    public Object populateDataTable(QueryData getTableData, String bank, TellerMaster master1) throws Exception {
	String parameter = "";
	String criteria = "";
	Connection connection = null;
	PreparedStatement prepareStatement = null;
	Statement statement = null;
	ResultSet resultSet = null;
	Session session = null;
	String query = null;
	ColumnNames field;
	List<ColumnNames> names = new ArrayList<>();
	List<AppMessages> appMessages = new ArrayList<>();
	List<TableMetaData> data = new ArrayList<>();
	TableMetaData tableMetaData = new TableMetaData();
	List<ApplicationLabel> labels = null;
	String filename = null;

	try {
	    session = cebiConstant.getCurrentSession(bank);
	    connection = ((SessionImpl) session).connection();
	    parameter = getTableData.getParameter().trim().length() > 0 ? getTableData.getParameter() : "";
	    criteria = getTableData.getQuery().trim().length() > 0 ? getTableData.getQuery() : "";
	    connection.setAutoCommit(false);
	    labels = applicationLabelService.retrieveAllLabels();
	    SimpleDateFormat formatter1 = new SimpleDateFormat("ddMMyyyy HH:mm:ss");
	    Date date1 = new Date();
	    logger.info("start ---" + formatter1.format(date1));
	    if (tableMetaData.getAppMessage() == null || tableMetaData.getAppMessage().size() == 0) {

		if ("Simple".equalsIgnoreCase(getTableData.getReporttype())) {
		    query = populateQuery(getTableData, parameter, criteria);
		    logger.info("query  --- >  " + query);
		    if (criteria != null && !criteria.isEmpty()) {
			validateTableCriteria(criteria, getTableData, tableMetaData, appMessages);
		    }
		    prepareStatement = connection.prepareStatement(query);
		    resultSet = prepareStatement.executeQuery();

		    // populateAuditHistory(getTableData.getTable(), master,
		    // query);
		    while (resultSet.next()) {
			tableMetaData = new TableMetaData();
			ResultSetMetaData rsmd = resultSet.getMetaData();
			int columnCount = rsmd.getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
			    String label = rsmd.getColumnName(i);
			    label = label.contains("(") && label.contains(")") ? label.substring(label.indexOf('(') + 1, label.indexOf(')')) : label;
			    field = new ColumnNames();
			    if (resultSet.getString(label) == null || resultSet.getString(label) == "")
				field.setField("");
			    else
				field.setField(resultSet.getString(label));
			    field.setName(addApplicationLabels(label, labels));
			    names.add(field);
			    tableMetaData.setNames(names);
			}
		    }
		} else if (Constants.CSV.equalsIgnoreCase(getTableData.getReporttype())) {
		    query = super.populateQuery(getTableData, parameter, criteria);
		    logger.info("query  --- >  " + query);
		    if (criteria != null && !criteria.isEmpty()) {
			validateTableCriteria(criteria, getTableData, tableMetaData, appMessages);
		    }
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
		    populateAuditHistory(getTableData.getTable(), master1, query);
		    return String.valueOf(bw).getBytes();
		} else if (Constants.TEXT.equalsIgnoreCase(getTableData.getReporttype())) {

		    int lgth = 0;
		    List<String> stringList = new ArrayList<>();
		    query = super.populateQuery(getTableData, parameter, criteria);
		    logger.info("query  --- >  " + query);
		    if (criteria != null && !criteria.isEmpty()) {
			validateTableCriteria(criteria, getTableData, tableMetaData, appMessages);
		    }
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
		    populateAuditHistory(getTableData.getTable(), master1, query);
		    return output;
		} else if (Constants.CSVPIPE.equalsIgnoreCase(getTableData.getReporttype())) {
			
		    query = super.populateQuery(getTableData, parameter, criteria);
		    logger.info("query  --- >  " + query);
		    if (criteria != null && !criteria.isEmpty()) {
			validateTableCriteria(criteria, getTableData, tableMetaData, appMessages);
		    }
		    statement = (Statement) connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, // or
			    ResultSet.CONCUR_READ_ONLY);
		    statement.setFetchSize(5000);
		    resultSet = statement.executeQuery(query);
		    StringBuilder buffer = new StringBuilder();
		    ResultSetMetaData rsmd = resultSet.getMetaData();
		    int columnCount = rsmd.getColumnCount();
		    List<String> colummnName = new ArrayList<String>();
		    for (int i = 1; i <= columnCount; i++) {
			buffer.append(rsmd.getColumnName(i) + "|");
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
				bw.append(StringUtils.rightPad(CebiConstant.EMPTY_SPACE, label.length())).append("|");
			    } else
				bw.append(StringUtils.rightPad(resultSet.getString(label).trim(), resultSet.getString(label).trim().length() - label.length())).append("|");
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
		    populateAuditHistory(getTableData.getTable(), master1, query);
		    return String.valueOf(bw).getBytes();

		} else {

		    query = super.populateQuery(getTableData, parameter, criteria);
		    logger.info("query  --- >  " + query);
		    if (criteria != null && !criteria.isEmpty()) {
			validateTableCriteria(criteria, getTableData, tableMetaData, appMessages);
		    }
		    statement = (Statement) connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, // or
			    ResultSet.CONCUR_READ_ONLY);
		    statement.setFetchSize(5000);
		    int colNum = 0;
		    int rowcnt = 1;
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
		    populateAuditHistory(getTableData.getTable(), master1, query);
		    return outArray;
		}
	    }
	    tableMetaData.setAppLabels(labels);
	    data.add(tableMetaData);
	    validateTableData(data, appMessages);
	} catch (Exception e) {
	    logger.info(e.getMessage());
	    e.printStackTrace();
	    throw e;
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
	populateAuditHistory(getTableData.getTable(), master1, query);
	return data;
    }

    public String populateQuery(QueryData table, String parameter, String criteria) {
	String sql = "SELECT";
	String parameterS = "  ";
	if (parameter.trim().length() > 0) {
	    parameter = parameter.trim();
	    if (parameter.lastIndexOf(",") == (parameter.length() - 1))
		parameterS += parameter.substring(0, (parameter.length() - 1)) + " ";
	    else
		parameterS += parameter.substring(0, (parameter.length())) + " ";
	} else {
	    parameterS += "  * ";
	}

	if (criteria.trim().length() > 0) {
	    sql = sql + parameterS + CebiConstant.QRY_FROM + table.getTable() + CebiConstant.QRY_WHERE;
	    sql += criteria;
	    sql += CebiConstant.QRY_ROWNUM;
	} else {
	    sql = sql + parameterS + " from " + table.getTable() + CebiConstant.WHERE_ROWNUM;
	}
	if (table.getGroupby() != null && table.getGroupby().trim().length() > 0) {
	    String groups = table.getGroupby().substring(0, (table.getGroupby().length() - 1));
	    sql = sql + "GROUP BY " + groups;

	}
	return sql;
    }

    protected void populateAuditHistory(String table, TellerMaster master, String query) {
    	logger.info("populateAuditHistory");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
	AuditHistory audit = new AuditHistory();
	audit.setTellerId(Integer.parseInt(master.getTellerid()));
	audit.setBranchId(master.getBranchid());
	audit.setBankCode(master.getBankCode());
	audit.setQuery(query);
	audit.setTable(table);
	audit.setAudDate(simpleDateFormat.format(new Date()));
	audit.setDburl(master.getIp());
	sessionFactory.getCurrentSession().save(audit);
    }

    protected void validateTableCriteria(String criteria, QueryData getTableData, TableMetaData tableMetaData, List<AppMessages> appMessages) {
	addValidationError(criteria, tableMetaData, appMessages, populateFields(getTableData.getTable()));
    }

    @SuppressWarnings("unchecked")
    public RequiredField populateFields(String table) {
	List<RequiredField> field = new ArrayList<>();
	RequiredField requiredField = new RequiredField();
	Session session = sessionFactory.getCurrentSession();
	Query query = session.createQuery("FROM RequiredField R WHERE R.tabel=:tbl_name");
	query.setParameter("tbl_name", table);
	field = (List<RequiredField>) query.list();
	if (field.size() >= 1)
	    requiredField = field.get(0);
	return requiredField;
    }

    private void addValidationError(String criteriaList, TableMetaData tableMetaData, List<AppMessages> appMessages, RequiredField required) {
	if (required.getFiled() != null) {
	    String mandate = required.getFiled();
	    for (String str : mandate.split(",")) {
		if (criteriaList.contains(str)) {
		    continue;
		}
		appMessages.add(new AppMessages(str, str.toLowerCase() + " is Required"));
	    }

	    tableMetaData.setAppMessage(appMessages);
	}
    }

    protected String addApplicationLabels(String label, List<ApplicationLabel> labels) {
	for (ApplicationLabel lbl : labels) {
	    if (lbl.getLabelCode().equalsIgnoreCase(label)) {
		label = lbl.getAppLabel();
		break;
	    }
	}
	return label;
    }

    private void validateTableData(List<TableMetaData> data, List<AppMessages> appMessages) {
	for (TableMetaData tableMetaData : data) {
	    // tableMetaData.setAppLabels(applicationLabelDao.retrieveAllLabels());
	    if (tableMetaData.getNames() == null || tableMetaData.getNames().isEmpty()) {
		appMessages.add(new AppMessages("NO_RESULTS", CebiConstant.NO_RESULT));
		tableMetaData.setAppMessage(appMessages);
	    }
	}
    }

    public void saveFavouriteQuery(QueryData data) {
	String parameter = "";
	String criteria = "";
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
	parameter = data.getParameter().trim().length() > 0 ? data.getParameter() : "";
	criteria = data.getQuery().trim().length() > 0 ? data.getQuery() : "";
	String query = populateQuery(data, parameter, criteria);
	data.setFinalQry(query);
	data.setCurrentDate(simpleDateFormat.format(new Date()));
	sessionFactory.getCurrentSession().save(data);
    }

    @Override
    public List<QueryData> retrieveFavouriteList(String bankCode) {
	List<QueryData> lists = new ArrayList<QueryData>();
	Query query = sessionFactory.getCurrentSession().createQuery("FROM QueryData qd WHERE qd.bankCode=:code");
	query.setParameter("code", bankCode);
	lists = (List<QueryData>) query.list();
	return lists;

    }

    @Override
    public List<QueryData> getReportDetails(int id) {
	List<QueryData> lists = new ArrayList<>();
	Query query = sessionFactory.getCurrentSession().createQuery("FROM QueryData qd WHERE qd.id=:id");
	query.setParameter("id", id);
	lists = (List<QueryData>) query.list();
	return lists;

    }

    @Override
    public List<Banks> retreiveBankNames() {
	List<Banks> banks = new ArrayList<>();
	Query query = sessionFactory.getCurrentSession().createQuery("FROM Banks ORDER BY bankName");
	banks = (List<Banks>) query.list();
	return banks;
    }

    @Override
    public Banks retreiveDbConnection(String bank) {
	List<Banks> banks = new ArrayList<>();
	Query query = sessionFactory.getCurrentSession().createQuery("FROM Banks WHERE bankCode = :code");
	query.setParameter("code", bank);
	banks = (List<Banks>) query.list();
	return banks.get(0);
    }

    @Override
    public String populateBankType(String bankcode) {
	Query query = sessionFactory.getCurrentSession().createSQLQuery("SELECT bank_type FROM cesys004 WHERE bank_code=:code");
	query.setParameter("code", bankcode);
	String type = (String) query.list().get(0);
	return type;
    }

    @Override
    public List<ViewInfo> retreiveViewDetails(String type) {
	List<ViewInfo> viewInfos = new ArrayList<>();
	Query query = null;
	if (type != null && type.equalsIgnoreCase("NB")) {
	    query = sessionFactory.getCurrentSession().createQuery("FROM ViewInfo WHERE access NOT IN (2) AND STATUS NOT IN('deactive')");
	} else {
	    query = sessionFactory.getCurrentSession().createQuery("FROM ViewInfo WHERE access NOT IN (0) AND STATUS NOT IN('deactive')");
	}
	viewInfos = (List<ViewInfo>) query.list();
	viewInfos.get(0).setAppLabels(applicationLabelDao.retrieveAllLabels());
	return viewInfos;
    }

    @Override
    public int addReportQueueData(ReportQueueData reportQueueData) {
	return (int) sessionFactory.getCurrentSession().save(reportQueueData);
    }

    @Override
    public void updateReportQueueData(ReportQueueData reportQueueData) {
	sessionFactory.getCurrentSession().update(reportQueueData);
    }

    @Override
    @Transactional
    public ReportQueueData getReportQueueData(int id) {
	return (ReportQueueData) sessionFactory.getCurrentSession().get(ReportQueueData.class, id);
    }

    @Override
    public void updatereportStatus(int id, String inProcess) {
	Query query = sessionFactory.getCurrentSession().createSQLQuery("UPDATE `reportqueuetable` SET `status`= :rStatus WHERE `id` = :rid");
	query.setParameter("rStatus", inProcess);
	query.setParameter("rid", id);
	query.executeUpdate();
    }

    @Override
    public List<Object[]> replaceCriteriaField(String table) {
	Query query = sessionFactory.getCurrentSession().createSQLQuery("SELECT `columnname`,`replacecriteriaColumn` FROM `statictablemetadata`  WHERE `tablename` =:tableName");
	query.setParameter("tableName", table);
	return query.list();
    }
}
