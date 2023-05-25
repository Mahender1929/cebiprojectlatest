package com.cebi.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cebi.entity.BranchInfo;
import com.cebi.entity.Ccdp010;
import com.cebi.entity.TellerMaster;
import com.cebi.service.CreateStaticReportPdfService;
import com.cebi.service.CreateStaticReportService;
import com.cebi.utility.CebiConstant;
import com.cebi.utility.MappingConstant;

@Controller
public class StaticReportController {

	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");

	@Autowired
	CreateStaticReportService createStaticReportService;
	
	@Autowired
	CreateStaticReportPdfService createStaticReportPdfService;
	
	@RequestMapping(value = MappingConstant.STATIC_REPORT, method = RequestMethod.GET)
	public ModelAndView staticReportPage(HttpServletRequest request, HttpServletResponse response) {
		BranchInfo bankBranchInformation = new BranchInfo();
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession();
		if (session.getAttribute("user") != null) {
			String bankcode = (String) session.getAttribute("bankCode");
			bankBranchInformation.setBrcdBankCd(bankcode);
			model.addObject("staticForm", bankBranchInformation);
			model.addObject("branchRep", new Ccdp010());
			model.setViewName("staticreport");
		} else {
			model.setViewName(CebiConstant.LOGIN);
			model.addObject("loginForm", new TellerMaster());
			model.addObject("SESSION_LOGOUT", CebiConstant.SESSION_LOGOUT);
		}
		return model;
	}

	@RequestMapping(value = MappingConstant.SA_5, method = RequestMethod.POST)
	public ResponseEntity<byte[]> downloadSA(@ModelAttribute("staticForm") BranchInfo branchInformation, Model model, HttpServletResponse response) {
		byte[] pdfBytes = null;
		ResponseEntity<byte[]> pdfResponse = null;
		HttpHeaders headers = new HttpHeaders();
		if(branchInformation.getFileType().equalsIgnoreCase("pdf")){
		pdfBytes = createStaticReportPdfService.createStaticReportPdf(branchInformation);
		response.setContentType("application/pdf");
		headers.setContentDispositionFormData("inline", simpleDateFormat.format(new Date()) + ".pdf");
		}else if(branchInformation.getFileType().equalsIgnoreCase("text")){
		pdfBytes = createStaticReportService.createReportSA(branchInformation);
		response.setContentType("application/text");
		headers.setContentDispositionFormData("inline", simpleDateFormat.format(new Date()) + ".text");
		}else{
			pdfBytes = createStaticReportService.createReportSA(branchInformation);
			response.setContentType("application/text");
			headers.setContentDispositionFormData("inline", simpleDateFormat.format(new Date()) + ".text");
		}
		pdfResponse = new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
		return pdfResponse;
	}
}
