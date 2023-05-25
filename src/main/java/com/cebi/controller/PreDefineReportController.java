package com.cebi.controller;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cebi.entity.PreDefineReport;
import com.cebi.entity.QueryData;
import com.cebi.entity.TableMetaData;
import com.cebi.entity.TellerMaster;
import com.cebi.service.AdminReportService;
import com.cebi.service.PredefineReportService;
import com.cebi.utility.CebiConstant;
import com.cebi.utility.MappingConstant;

@Controller
public class PreDefineReportController {

	@Autowired
	PredefineReportService predefineReportService;
	
	@Autowired
	AdminReportService adminReportService;

	private static final Logger logger = Logger
			.getLogger(PreDefineReportController.class);
	
	@RequestMapping(value = MappingConstant.PREDEFINE_REPORT_PAGE, method = RequestMethod.GET)
	public ModelAndView preDefineReport(ModelAndView model,
			HttpServletRequest request) {
		logger.info("ENTER into predefinereportPage : " + LocalTime.now().toString());
		ModelAndView modelAndView = new ModelAndView();
		HttpSession session = request.getSession();
		String bankCode = (String) session.getAttribute(CebiConstant.BANK_CODE);
		List<PreDefineReport> list = predefineReportService
				.getQryListByBank(bankCode);
		modelAndView.addObject("list", list);
		TellerMaster tellerMaster = new TellerMaster();
		if (session.getAttribute("user") != null) {
			String user= (String) session.getAttribute("user");
			if(user.equals("501")){
			modelAndView.setViewName("predefinereport");
			}else{
				modelAndView.setViewName("landing");
			}
		} else {
			modelAndView.setViewName(CebiConstant.LOGIN);
			modelAndView.addObject("loginForm", tellerMaster);
			modelAndView.addObject("SESSION_LOGOUT",
					CebiConstant.SESSION_LOGOUT);
		}
		logger.info("Exit from predefinereportPage : " + LocalTime.now().toString());
		return modelAndView;
	}
	
	@RequestMapping(value = MappingConstant.GET_TABLE_DATA, method = RequestMethod.POST)
	public @ResponseBody List<TableMetaData> preDefineReportTable(@RequestBody PreDefineReport preDefineReport,HttpServletRequest request) {
		logger.info("ENTER into preDefineReportTable : " + LocalTime.now().toString());
		List<TableMetaData> dataList = new ArrayList<TableMetaData>();
		HttpSession session = request.getSession();
		String bankCode = (String) session.getAttribute(CebiConstant.BANK_CODE);
		String tableName = predefineReportService.getQueryString(preDefineReport.getSaveQuery());
		logger.info(preDefineReport.getSaveQuery()+"-" + LocalTime.now().toString());
		dataList = predefineReportService.getTableDataListByBank(preDefineReport,bankCode);   
		logger.info("Exit from preDefineReportTable : " + LocalTime.now().toString());
			return dataList;
	}
	
	

	@RequestMapping(value = MappingConstant.SAVE_REPORT, method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveDefRepoQry(@RequestBody PreDefineReport preDefineReport,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		logger.info("ENTER into saveDefRepoQry : " + LocalTime.now().toString());
		HttpSession session = request.getSession();
		preDefineReport.setId(null);
		preDefineReport.setSaveQuery(preDefineReport.getSaveQuery());
		String bankCode = (String) session.getAttribute(CebiConstant.BANK_CODE);
		preDefineReport.setBankCod(bankCode);
		preDefineReport.setSts("EXIST");
		boolean isExist = predefineReportService.isQuerySaved(preDefineReport);
		String msg = null;
		if (isExist == true) {
			msg = predefineReportService.saveDefReportObj(preDefineReport);
		} else {
			msg = "Query is Already Exist..!";
		}
		map.put("msg", msg);
		logger.info("Exit from saveDefRepoQry : " + LocalTime.now().toString());
		return map;
	}
	
	@RequestMapping(value = MappingConstant.DELETE_SAVED_QRY, method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> deleteSavedQry(
			@RequestBody PreDefineReport preDefineReport,
			HttpServletRequest request) {
		logger.info("ENTER into deleteSavedQry : " + LocalTime.now().toString());
		Map<String, Object> map = new HashMap<String, Object>();
		String msg = predefineReportService.deleteThisObject(preDefineReport);
		map.put("msg", msg);
		logger.info("Exit from deleteSavedQry : " + LocalTime.now().toString());
		return map;
	}
	
	@RequestMapping(value = MappingConstant.DOWNLOAD_PR_CSV, method = RequestMethod.POST)
	public ResponseEntity<byte[]> exportDataToCSV( HttpServletRequest request , HttpServletResponse response)throws IOException {
		ResponseEntity<byte[]> csvResponse = null;
		logger.info("ENTER into exportDataToCSV : " + LocalTime.now().toString());
		String qry = request.getParameter("csvquery").replace(";", "");
		HttpSession session = request.getSession();
		QueryData queryData = new QueryData();
		logger.info(qry + LocalTime.now().toString());
		queryData.setFinalQry(qry);
		queryData.setReporttype("csvpr");
		String bank = (String) session.getAttribute("bank");
		queryData.setBankCode(bank);
		byte[] csvByt =	(byte[])predefineReportService.getDataIntoFile(queryData, bank);
		HttpHeaders headers = new HttpHeaders();
		response.setContentType("text/csv");
		headers.setContentDispositionFormData(CebiConstant.INLINE, "cebi.csv");
		csvResponse = new ResponseEntity<>(csvByt, headers, HttpStatus.OK);
		logger.info("Exit from exportDataToCSV : " + LocalTime.now().toString());
		return csvResponse;
	}

	@RequestMapping(value = MappingConstant.DOWNLOAD_PR_TXT, method = RequestMethod.POST)
	public ResponseEntity<byte[]> exportDataToTXT(HttpServletRequest request,HttpServletResponse response)throws IOException {
		ResponseEntity<byte[]> txtResponse = null;
		logger.info("ENTER into exportDataToTXT : " + LocalTime.now().toString());
		HttpSession session = request.getSession();
		String qry = request.getParameter("txtquery").replace(";", "");
		QueryData queryData = new QueryData();
		logger.info(qry + LocalTime.now().toString());
		queryData.setFinalQry(qry);
		queryData.setReporttype("txtpr");
		String bank = (String) session.getAttribute("bank");
		queryData.setBankCode(bank);
		byte[] txtByt =	(byte[])predefineReportService.getDataIntoFile(queryData, bank);
		HttpHeaders headers = new HttpHeaders();
		response.setContentType("application/txt");
		headers.setContentDispositionFormData(CebiConstant.INLINE, "cebi.txt");
		txtResponse = new ResponseEntity<>(txtByt, headers, HttpStatus.OK);
		logger.info("Exit from exportDataToTXT : " + LocalTime.now().toString());
		return txtResponse;
		//	return adminReportService.csvDownloadQueue(queryData, txtquery);
	}
	
	@RequestMapping(value = MappingConstant.DOWNLOAD_PR_XLS, method = RequestMethod.POST)
	public ResponseEntity<byte[]> exportDataToXLS( HttpServletRequest request,HttpServletResponse response)throws IOException {
		ResponseEntity<byte[]> xlsResponse = null;
		logger.info("ENTER into exportDataToXLS : " + LocalTime.now().toString());
		HttpSession session = request.getSession();
		String qry = request.getParameter("xlsquery").replace(";", "");
		logger.info(qry + LocalTime.now().toString());
		QueryData queryData = new QueryData();
		queryData.setFinalQry(qry);
		queryData.setReporttype("xlspr");
		String bank = (String) session.getAttribute("bank");
		queryData.setBankCode(bank);
		byte[] xlsByt =	(byte[])predefineReportService.getDataIntoFile(queryData, bank);
		HttpHeaders headers = new HttpHeaders();
		response.setContentType("application/ms-excel");
		headers.setContentDispositionFormData(CebiConstant.INLINE, "cebi.xlsx");
		headers.setContentLength(xlsByt.length);
		xlsResponse = new ResponseEntity<>(xlsByt, headers, HttpStatus.OK);
		logger.info("Exit from exportDataToXLS : " + LocalTime.now().toString());
		return xlsResponse;
		//return adminReportService.csvDownloadQueue(queryData, bank);
	}
	
	
	

}
