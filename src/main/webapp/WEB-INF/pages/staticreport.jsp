<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="header.jsp"%>
<title>static report page</title>
<link href="css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<style type="text/css">
.staticformdiv {
	max-width: 600px !important;
	margin-top: 30px;
	margin-left: 380px;
	padding: 34px 0;
	border-style: solid;
	border-width: 8.5px;
	border-color: #d2cccc;
}

.container-fluid {
	margin-top: 0% !important;
}

.form-control1 {
	display: block;
	width: 78%;
	height: 34px;
	padding: 10px 28px;
	font-size: 14px;
	line-height: 1.42857143;
	color: #555;
	background-color: #fff;
	background-image: none;
	border: 1px solid #ccc;
	border-radius: 4px;
	-webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075);
	box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075);
	-webkit-transition: border-color ease-in-out .15s, -webkit-box-shadow
		ease-in-out .15s;
	-o-transition: border-color ease-in-out .15s, box-shadow ease-in-out
		.15s;
	transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s;
}

.formWrapper {
	margin-left: 134px;
}
/* #retrive{

		    margin-left: 253px; 
   width: 790px;
    height: 94%; 

} */
.req {
	color: red;
}

hr {
	margin-top: 7px;
	margin-left: -84px;
}

label {
	font-weight: 200;
}

.form-check-label {
	margin-bottom: 0;
}

.form-check-input {
	float: left;
	margin: 4px 7px 0 !important;
}

.container {
	/* width: 1070px;
    margin-left: 177px; */
	width: 1070px;
	margin-left: 401px;
	height: 291px;
	padding-top: -3px;
}

.header-color {
	line-height: 50px !important;
}

.hidebar {
	display: none;
}

.form-check-label span {
	float: left;
}

.btn-success:hover {
	background-color: #339bb7 !important;
	border-color: #339bb7 !important;
}

.footer {
	bottom: 0;
	position: absolute;
	width: 100%;
}

.btn-success, .btn-success:focus {
	color: #ffffff;
	background-color: #337ab7;
	border-color: #337ab7;
}

a:hover, a:focus, a:active {
	text-decoration: none;
}

.hidebar {
	display: none;
}

.panel-heading {
	padding: 5px 15px !important;
	color: #000 !important;
}

.container-fluid {
	margin-top: 6.5% !important;
}

.example_length {
	margin-left: 19px;
}

.panel-body {
	padding: 15px !important;
}

p {
	color: #626262;
}

.nav-tabs ~.tab-content {
	overflow: hidden;
	padding: 15px;
	border: 1px solid #ddd;
	margin-top: -1px;
}

.help_header {
	color: black;
	font-size: 18px;
	text-align: left;
	margin: 0px auto 10px;
	border-bottom: 1px solid #ddd;
	padding: 0 0 10px;
}

.panel-title {
	text-transform: capitalize !important;
}

.panel-heading
 
a
:not
 
(
.btn
 
){
color
:
 
#000
 
!
important
;


}
.panel-group .panel-heading .panel-title>a {
	font-size: 15px;
}

.panel.panel-default {
	border: 1px solid rgba(0, 0, 0, 0.07) !important;
}
/* .form-control{
width: 395px !important;
} */
.table thead tr th {
	color: #fff;
	text-align: center;
}

.table tbody tr td {
	font-size: 12px;
	border: 1px solid #ddd;
}

.text-center {
	margin-top: -104px;
	margin-left: -60px;
}

.Edit {
	text-align: left !important;
	padding-top: 0px;
	height: 10%;
	margin-left: 0px;
}
</style>
</head>

</head>
<body>

	<!-- <div class="scrollable1"> -->
	<!-- <div class="report_bg_patch1"> -->

	<ul class="nav nav-tabs" id="myTab" role="tablist">
		<li class="active"><a data-toggle="tab" role="tab"
			href="#branchinfo">Branch Information</a></li>
		<li class="infolist"><a data-toggle="tab" role="tab" href="#info1">Information</a></li>
		<!-- <li><a class="file_action" href="info">Product Info</a></li> -->
		<li class="prolist"><a href="#productinfo" role="tab" data-toggle="tab">Product
				Info</a></li>
	</ul>


	<div class="tab-content" role="tab-content" style="margin-bottom: 42px;">
		<!-- first tab content -->
		<div id="branchinfo" style="" class="tab-pane fade in active">
			<div class="bg-contact100"
				style="background-image: url('images/bg-01.jpg');">
				<!-- <div class="container-contact100"> -->
				<!-- <div class="b-b b-r b-l b-t b-grey loan_label" style="background: #fff; box-shadow: 0 0 8px rgba(0, 0, 0, 0.5);"> -->
				<div class="wrap-contact100">
					<div style="width: 390px; margin-right: 65px;">

						<div class="contact100-pic js-tilt">
							<img src="images/img-01.png" alt="IMG">
						</div>
					</div>

					<div class="vl" style="padding-left: 140px">
						<form:form id="staticformid" method="POST"
							commandName="staticForm" class="contact100-form validate-form"
							action="${pageContext.request.contextPath}/sa-5">
							<span class="contact100-form-title"> Branch Information </span>

							<div class="wrap-input100 validate-input">
								<form:input path="brcdBankCd" type="text"
									class="input100 form-control" id="bankcd"
									placeholder="Enter bank" readonly="true" required="required" />
								<span class="focus-input100"></span> <span
									class="symbol-input100"> <i class="fa fa-user"
									aria-hidden="true"></i>
								</span>
							</div>

							<div class="branchdiv">
								<div class="wrap-input100 validate-input"
									style="margin-bottom: 15px;">
									<form:input path="brcdBranchCd" type="text"
										class="form-control input100" id="branchcd" autocomplete="off"
										placeholder="Enter branch" />
									<span class="focus-input100"></span> <span
										class="symbol-input100"> <i class="fa fa-envelope"
										aria-hidden="true"></i>
									</span>
								</div>

								<div id="tobranchcddiv" class="wrap-input100 validate-input"
									style="display: none; margin-bottom: 15px;">
									<form:input path="toBranch" type="text"
										class="form-control input100" id="tobranchcd"
										autocomplete="off" placeholder="Enter to branch" />
									<span class="focus-input100"></span> <span
										class="symbol-input100"> <i class="fa fa-envelope"
										aria-hidden="true"></i>
									</span>
								</div>
							</div>

							<div class="form-check">
								<label class="form-check-label container1"> <form:radiobutton
										path="optionvalue" value="isRangetrue" id="rangeid"
										class="form-check-input" /> <span class="checkmark"></span><span>Range</span>
								</label> <label style="margin-left: 25px;"
									class="form-check-label container1"> <form:radiobutton
										path="optionvalue" value="isSelectall" id="selectall"
										class="form-check-input" /> <span class="checkmark"></span><span>All</span></label>
								<label style="margin-left: 25px;"
									class="form-check-label container1"> <form:radiobutton
										path="optionvalue" value="isConsolidated" id="consolidated"
										class="form-check-input" /> <span class="checkmark"></span><span>Consolidated</span>
								</label>
								<div>
									<p
										style="color: #97e26f; margin: 0; padding: 0 10px; float: left; font-weight: 600;">File
										Format:</p>
									<div style="float: left;">
										<label class="form-check-label container1"> <form:radiobutton
												path="fileType" value="text" id="textid"
												class="form-check-input" /> <span class="checkmark"></span><span>Text</span>
										</label> <label style="margin-left: 15px;"
											class="form-check-label container1"> <form:radiobutton
												path="fileType" value="pdf" id="pdfid"
												class="form-check-input" /> <span class="checkmark"></span><span>Pdf</span>
										</label>
									</div>
								</div>
							</div>
							<div class="clearfix"></div>
							<div class="container-contact100-form-btn"
								style="margin: 5px 0 0;">
								<button id="process" type="button" class="contact100-form-btn">
									<span class="glyphicon glyphicon-log-in"></span> &nbsp; Process
								</button>
							</div>
							<p id="errMessage" class="req"></p>

						</form:form>
					</div>
					<!-- </div> -->
					<!-- </div> -->
					<div id="fade"></div>
					<div id="modal"
						style="height: auto; width: auto; padding: 0; border-radius: 0;">
						<img id="loading5" style="width: 150px;" src="images/loading5.gif" />
					</div>
				</div>
				<ul class="hide">
					<li><a href="${pageContext.request.contextPath}/sa-5"
						class="btn btn-default">SA-5</a></li>
				</ul>

			</div>

			<script type="text/javascript">
				$(document).ready(function() {
					$('#process').click(function(e) {
						submitstaticForm(e);
					});
					$('#selectall').click(function(e) {
						hideBranchDiv(e);
					});
					$('#consolidated').click(function(e) {
						hideBranchDiv(e);
					});
					$('#rangeid').click(function(e) {
						addRangeDiv(e);
					});
				});
			</script>
		</div>

		<div id="info1" class="tab-pane fade">


			<!-- <div class="container-contact100"> -->
			<!-- <div class="b-b b-r b-l b-t b-grey loan_label" style="background: #fff; box-shadow: 0 0 8px rgba(0, 0, 0, 0.5);"> -->
			<!-- <div class="wrap-contact100"> -->



			<form action="retriveBranchInfo" id="formvalidate" method="POST">


				<div class="Edit"
					style="font-size: 30px; padding: 0px 25px; margin-left: -98px; margin-top: -17px;">
					<span style="font-size: 17px;" class="fa fa-user"></span>&nbsp;<span
						style="font-size: 17px !important;" id="noteheader"></span>
				</div>
				<hr>
				<!-- <div class="contact100-pic js-tilt">
								<img src="images/img-01.png" alt="IMG">
							</div>
					<div> -->
				<h6>
					<span class="NoteMessage" style="color: red">Note:* Shows
						Fields are Mandatory</span>
				</h6>

				<br> <br>
				<div class="container">
					<div class="form-inline">
						<div class="col-xs-4">
							<label class="col-sm-4">Bancd</label> <input name="brcdBankCd"
								type="text" id="Bancd" class="form-control" readonly="true" />
						</div>
					</div>
					<br>
					<br>
					<div class="form-inline">
						<div class="col-xs-4">
							<label class="col-sm-4"> BranchCode<span class="required">*</span></label>
							<input name="branchCode" type="text" id="BranchCode"
								class="form-control" autocomplete="off"
								placeholder="Branch Code" />
							<h6 id="BranchKode" class="req"></h6>
						</div>
					</div>
					<br>
					<br>
					<div class="form-inline ">
						<div class="col-xs-4">
							<label class="col-sm-4">AcctNo</label> <input name="acctNo"
								type="text" id="Number" class="form-control" autocomplete="off"
								placeholder="Acct No" />
						</div>
					</div>
					<br>
					<br>

					<div class="form-inline">
						<div class="col-xs-4">
							<label class="col-sm-4">ReportDates<span class="required">*</span></label>
							<input name="reportDate" type="text" id="ReportDate"
								class="form-control" placeholder="dd/mm/yyyy" autocomplete="off" />
							<h6 id="ReportdateZ" class="req"></h6>
						</div>
					</div>
				</div>
				<br> <br>

				<div class="clearfix">
					<div class="text-center">
						<button type="button" class="btn btn-primary btn-cons"
							id="button4" value="show">Submit</button>
						&nbsp;&nbsp;&nbsp;
						<button type="reset" class="btn btn-danger btn-cons "
							class="cancelbtn">Reset</button>
					</div>
				</div>

			</form>
			<!-- </div> -->
			<!--  </div> -->
			<!-- </div> -->
		</div>



		<div id="productinfo" class="tab-pane fade">
			<!-- <li><a href="#content_notify"></a></li> -->
			<div class="scrollable" id="div3" style="overflow: hidden;">
				<div class="b-b b-r b-l b-t b-grey loan_label"
					style="width: 1301px; padding: 5px 69px 40px; background: #fff; box-shadow: 0 0 8px rgba(0, 0, 0, 0.5);">
					<!-- <h4 class="text-center bold " style="height: 45px; background: #fff; border-radius: 1px; position:absolute;text-align: left;  padding-left: 50px; ">
					<span class="glyphicon">&#xe086; </span>&nbsp;<span> BranchInformation</span>
				
		</h4> -->

					<div class="report_bg_patchbranch" style="margin-right: -97px;">

						<table id="example" class="table1" style="width: 100%"
							border="2px">
							<thead>
								<tr>
									<th><b>Bank Cd</b></th>
									<th><b>Report Date</b></th>
									<th><b>Branch Code</b></th>
									<th><b>Acct No</b></th>
									<th style="cell-padding: 5px">Modify</th>
									<th>Delete</th>
									<th class="hide">Module</th>
									<th class="hide">Circle</th>
									<th class="hide">region</th>
									<th class="hide">Branch Name</th>
									<th class="hide">Segment</th>
									<th class="hide">Cif No</th>
									<th class="hide">Sector</th>
									<th class="hide">Facility Type</th>
									<th class="hide">Borr Name</th>
									<th class="hide">Limit Amt</th>
									<th class="hide">Dp</th>
									<th class="hide">Irregular Amt</th>
									<th class="hide">NatOfBills</th>
									<th class="hide">Security Amt</th>
									<th class="hide">Charges Created</th>
									<th class="hide">Primary Tag</th>
									<th class="hide">Collateral Tang</th>
									<th class="hide">Total Tang</th>
									<th class="hide">Constituent</th>
									<th class="hide">Allocated</th>

									<th class="hide">Intt Rate</th>
									<th class="hide">Cr Summ</th>

									<th class="hide">Irregular Date</th>
									<th class="hide">Last Cr Date</th>
									<th class="hide">Crr</th>
									<th class="hide">InsuDate</th>
									<th class="hide">Insu Amt</th>
									<th class="hide">ReceEcgc</th>
									<th class="hide">SancDate</th>
									<th class="hide">StockStmntDate</th>

									<th class="hide">TotOs</th>
									<th class="hide">SecuTangAsset</th>
									<th class="hide">BankSecu</th>
									<th class="hide">SecureEcgc</th>
									<th class="hide">Unsecured</th>
									<th class="hide">AssetSubClass</th>

									<th class="hide">PbRd</th>
									<th class="hide">UnRealisedIntt</th>

									<th class="hide">Npa Date</th>
									<th class="hide">Inca</th>
									<th class="hide">Prov Amt</th>
									<th class="hide">Prov Unsec</th>
									<th class="hide">Intt Susp</th>
									<th class="hide">AdjUnsecuPortion</th>
									<th class="hide">SecuPortion</th>
									<th class="hide">RstrctrProv</th>

									<th class="hide">rstrctrDate</th>
									<th class="hide">OneNpa</th>
									<!-- <th class="hide">Record Status</th> -->
								</tr>
							</thead>

							<tbody id="datainfo" class="tbodyinfo">
							</tbody>

						</table>
						<!-------------------- Delete popup  -->
						<div class="modal fade" id="myModal" role="dialog">
							<div class="modal-dialog2">
								<!-- Modal content-->
								<div class="modal-content3 animate" style="width: 40%;">
									<div class="modal-header">

										<h4 class="modal-title">
											<span style="font-weight: 500; font-size: 17px;">Do
												you want to delete?</span>
										</h4>
									</div>
									<br>

									<div>
										<form method="post" action="delete" id="deleteform">
											&nbsp;&nbsp;<input type="hidden" id="brcdbancd" value=""
												name="brcdBankCd"><input type="hidden" id="repdate"
												value="" name="reportDate"> <input type="hidden"
												id="AccNo" value="" name="id.acctNo"> <input
												type="hidden" id="branchCode1" value="" name="id.branchCode">
											<input type="button" class="btn btn-danger btn-sm"
												id="deletebtn1" value="Delete" />&nbsp;&nbsp;&nbsp; <input
												type="button" id="btn1"
												onclick="document.getElementById('myModal').style.display='none'"
												class="btn btn-danger btn-sm" data-dismiss="modal"
												value="No" />
										</form>
									</div>




									<div class="modal-footer">
										<button type="button"
											onclick="document.getElementById('myModal').style.display='none'"
											class="btn btn-default" data-dismiss="modal">Close</button>
									</div>
								</div>
							</div>
						</div>
						<div id="id02" class="modal2" role="dialog">
							<div class="modal-content2 animate">
								<div class="Edit" style="font-size: 28px;">
									<span class="glyphicon glyphicon-pencil"
										style="font-size: 17px !important;"></span>&nbsp;Edit
									Information
								</div>
								<hr style="margin-top: -11px;">
								<form action="displaymodify" method="POST" id="reportForm">

									<div class="col-xs-4">
										<label>Bank Cd</label><br> <input name="id.brcdBankCd"
											type="text" id="BankCd" placeholder="Enter Branch Code"
											readonly="true" class="form-control1" />
									</div>
									<div class="col-xs-4">
										<label>Report Date</label><br> <input
											name="id.reportDate" type="text" id="reportdate"
											placeholder="Enter reportdate" readonly="true"
											class="form-control1" />
									</div>
									<div class="col-xs-4">
										<label>Branch Code</label><br> <input
											name="id.branchCode" type="text" id="branchcode"
											placeholder="Enter BranchCode" readonly="true"
											class="form-control1" />
									</div>
									<div class="col-xs-4">
										<label class=>Module</label><br> <input name="module"
											type="text" id="module" placeholder="Enter Module"
											class="form-control1" />
									</div>
									<div class="col-xs-4">
										<label>Circle</label><br> <input name="circle"
											type="text" id="circle" placeholder="Enter Circle"
											class="form-control1" />
									</div>
									<div class="col-xs-4">
										<label>Region</label><br> <input name="region"
											type="text" id="region" placeholder="Enter Region"
											class="form-control1" />
									</div>
									<div class="col-xs-4">
										<label>branch Name</label><br> <input name="branchName"
											type="text" id="branchname" placeholder="Enter Branch name"
											class="form-control1" />
									</div>
									<div class="col-xs-4">
										<label>Segment</label><br> <input name="segment"
											type="text" id="segment" placeholder="Enter Segment"
											class="form-control1" />
									</div>
									<div class="col-xs-4">
										<label>Cif No</label><br> <input name="cifNo" type="text"
											id="cifno" placeholder="Enter CifNo" class="form-control1" />
									</div>
									<div class="col-xs-4">
										<label>Sector</label><br> <input name="sector"
											type="text" id="sector" placeholder="Enter Sector"
											class="form-control1" />
									</div>
									<div class="col-xs-4">
										<label>Facility Type</label><br> <input
											name="facilityType" type="text" id="facility Type"
											placeholder="Enter Facility Type" class="form-control1" />
									</div>
									<div class="col-xs-4">
										<label>Acc No</label><br> <input name="id.acctNo"
											type="text" id="accno" placeholder="Enter Region"
											readonly="true" class="form-control1" />
									</div>
									<div class="col-xs-4">
										<label>Borr path</label><br> <input name="borrName"
											type="text" id="BorrName" placeholder="Enter AccNo"
											class="form-control1" />
									</div>
									<div class="col-xs-4">
										<label>Limit Amt</label><br> <input name="limitAmt"
											type="text" id="Limitamt" placeholder="Enter Limit Amt"
											class="form-control1" />
									</div>
									<div class="col-xs-4">
										<label>Dp</label><br> <input name="dp" type="text"
											id="dp" placeholder="Enter DP" class="form-control1" />
									</div>
									<div class="col-xs-4">
										<label>Irregular Amt</label><br> <input
											name="irregularAmt" type="text" id="irregularamt"
											placeholder="Enter Irregular_Amt:" class="form-control1" />
									</div>
									<div class="col-xs-4">
										<label>Nat Of Bills</label><br> <input name="natOfBills"
											type="text" id="natOfbills" placeholder="Enter NAT_Of_Bills"
											class="form-control1" />
									</div>
									<div class=" col-xs-4">
										<label>Security Amt</label><br> <input name="securityAmt"
											type="text" id="securityamt" placeholder="Enter SecurityAmt"
											class="form-control1" />
									</div>
									<div class="col-xs-4">
										<label>Charges Created</label><br> <input
											name="chargesCreated" type="text" id="chargescreated"
											placeholder="Enter Charges Created" class="form-control1" />
									</div>
									<div class="col-xs-4">
										<label>Primary Tang</label><br> <input
											name="primaryTang" type="text" id="primaryTang"
											placeholder="Enter Primary Tang" class="form-control1" />
									</div>
									<div class="col-xs-4">
										<label>Collateral Tang</label><br> <input
											name="collateralTang" type="text" id="collateralTang"
											placeholder="Enter Collateral Tang" class="form-control1" />
									</div>
									<div class="col-xs-4">
										<label>Total Tang</label><br> <input name="totalTang"
											type="text" id="totalTang" placeholder="Enter Total Tang"
											class="form-control1" />
									</div>
									<div class="col-xs-4">
										<label>Constituent</label><br> <input name="constituent"
											type="text" id="constituent" placeholder="Enter Constituent"
											class="form-control1" />
									</div>
									<div class="col-xs-4">
										<label>Allocated</label><br> <input name="allocated"
											type="text" id="allocated" placeholder="Enter Allocated"
											class="form-control1" />
									</div>
									<div class="col-xs-4">
										<label>Init Rate</label><br> <input name="inttRate"
											type="text" id="inttrate" placeholder="Enter InitRate"
											class="form-control1" />
									</div>
									<div class="col-xs-4">
										<label>Cr Sum</label><br> <input name="crSumm"
											type="text" id="crsumm" placeholder="Enter CR_SUM"
											class="form-control1" />
									</div>
									<div class="col-xs-4">
										<label>Irregular Date</label><br> <input
											name="irregularDate" type="text" id="irregulardate"
											placeholder="Enter BranchCode" class="form-control1" />
									</div>
									<div class="col-xs-4">
										<label>Last CrDate</label><br> <input name="lastCrDate"
											type="text" id="lastcrdate" placeholder="Enter IrregularDate"
											class="form-control1" />
									</div>
									<div class="col-xs-4">
										<label>Crr</label><br> <input name="crr" type="text"
											id="crr" placeholder="Enter CRR" class="form-control1" />
									</div>
									<div class="col-xs-4">
										<label>Insu_Date</label><br> <input name="insuDate"
											type="text" id="insudate" placeholder="Enter Insu_Date"
											class="form-control1" />
									</div>
									<div class="col-xs-4">
										<label>Insu Amt</label><br> <input name="insuAmt"
											type="text" id="insuamt" placeholder="Enter Insu_Amt"
											class="form-control1" />
									</div>
									<div class="col-xs-4">
										<label>Rece Ecgc</label><br> <input name="receEcgc"
											type="text" id="receccgc" placeholder="Enter Rece_ecgc"
											class="form-control1" />
									</div>
									<div class="col-xs-4">
										<label>Sanc_Date</label><br> <input name="sancDate"
											type="text" id="sancDate" placeholder="Enter Sanc_Date"
											class="form-control1" />
									</div>
									<div class="col-xs-4">
										<label>Stock StmtDate</label><br> <input
											name="stockStmntDate" type="text" id="stockstmtdate"
											placeholder="Enter StmtDate" class="form-control1" />
									</div>
									<div class="col-xs-4">
										<label>Tot Os</label><br> <input name="totOs" type="text"
											id="totos" placeholder="Enter TotOs" class="form-control1" />
									</div>
									<div class="col-xs-4">
										<label>SecuTand Asset</label><br> <input
											name="secuTangAsset" type="text" id="secutandasset"
											placeholder="Enter SecuTand Asset" class="form-control1" />
									</div>
									<div class="col-xs-4">
										<label>Bank Secu</label><br> <input name="bankSecu"
											type="text" id="BankSecu" placeholder="Enter BankSecu"
											class="form-control1" />
									</div>
									<div class="col-xs-4">
										<label>Secu Ecgc</label><br> <input name="secureEcgc"
											type="text" id="SecuEcgc" placeholder="Enter SecuEcgc"
											class="form-control1" />
									</div>
									<div class="col-xs-4">
										<label>Unsecured</label><br> <input name="Unsecured"
											type="text" id="unsecured" placeholder="Enter Unsecured"
											class="form-control1" />
									</div>
									<div class="col-xs-4">
										<label>Asset SubClass</label><br> <input
											name="assetSubclass" type="text" id="assetsubclass"
											placeholder="Enter AssetSubClass" class="form-control1" />
									</div>
									<div class="col-xs-4">
										<label>Pb Rd</label><br> <input name="pbRd" type="text"
											id="pbRd" placeholder="Enter PB_RD" class="form-control1" />
									</div>
									<div class="col-xs-4">
										<label>Unrealised Init</label><br> <input
											name="unrealisedIntt" type="text" id="UnrealisedIntt"
											placeholder="Enter UnrealisedInit" class="form-control1" />
									</div>
									<div class="col-xs-4">
										<label>Npa Date</label><br> <input name="npaDate"
											type="text" id="npaDate" placeholder="Enter NPA_DATE"
											class="form-control1" />
									</div>
									<div class="col-xs-4">
										<label>INCA</label><br> <input name="inca" type="text"
											id="inca" placeholder="Enter INCA" class="form-control1" />
									</div>
									<div class="col-xs-4">
										<label>Prov Amt</label><br> <input name="provAmt"
											type="text" id="provamt" placeholder="Enter ProvAmt"
											class="form-control1" />
									</div>
									<div class="col-xs-4">
										<label>Prov Unsec</label><br> <input name="provUnsec"
											type="text" id="provUnsec" placeholder="Enter ProvUnsec"
											class="form-control1" />
									</div>
									<div class="col-xs-4">
										<label>Init Susp</label><br> <input name="inttSusp"
											type="text" id="initsusp" placeholder="Enter InitSusp"
											class="form-control1" />
									</div>
									<div class="col-xs-4">
										<label>AdjUnsecu Portion</label><br> <input
											name="adjUnsecuPortion" type="text" id="adjunsecuportion"
											placeholder="Enter AdjUnsecuPortion" class="form-control1" />
									</div>
									<div class="col-xs-4">
										<label>Secu Portion</label><br> <input name="secuPortion"
											type="text" id="secuportion" placeholder="Enter SecuPortion"
											class="form-control1" />
									</div>
									<div class="col-xs-4">
										<label>RSTRCTR Prov</label><br> <input name="rstrctrProv"
											type="text" id="rstrctrprov" placeholder="Enter RSTRCTRProv"
											class="form-control1" />
									</div>
									<div class="col-xs-4">
										<label>RSTRCTR Date</label><br> <input name="rstrctrDate"
											type="text" id="rstrctrdate" placeholder="Enter RSTRCTRDate"
											class="form-control1" />
									</div>
									<div class="col-xs-4">
										<label>One Npa</label><br> <input name="oneNpa"
											type="text" id="onenpa" placeholder="Enter OneNpa"
											class="form-control1" />
									</div>
									</br>
									</br>
									</br>
									<!-- <div class="col-xs-4" >
						<label  >Rec Status</label><br>
						<input path="REC_STATUS" type="text" id="recstatus" placeholder="Enter rec status" class="form-control"/>
						</div> -->

									<div class="text-center">
										<button type="button" class="button1" id="updatebtn">Update</button>
										&nbsp; &nbsp;

										<button type="button"
											onclick="document.getElementById('id02').style.display='none'"
											class="cancelbtn2">Cancel</button>
									</div>

								</form>

							</div>
						</div>
					</div>

				</div>
			</div>
		</div>
	</div>

	<%@ include file="/WEB-INF/pages/Footer.jsp"%>
	<script type="text/javascript">
		$(document).ready(function() {

			$('#BranchCode').keyup(function() {
				BranchKode_check();
			});

			/* $('#ReportDate').keyup(function() {
				ReportDate_check();

			}); */

			$('#SubmitValues').click(function() {
				openModal();
				var Branchkode_err = BranchKode_check();
				var Reportdate_err = ReportDate_check();

				if ((Branchkode_err == true) && (Reportdate_err == true)) {
					return true;
					$("#formvalidate").submit();

				} else {
					closeModal();
					return false;
				}
			});

		});
		$('#BranchCode')
				.keypress(
						function(event) {
							var keycode = event.which;
							if (!(event.shiftKey == false && (keycode == 46
									|| keycode == 8 || keycode == 37
									|| keycode == 39 || (keycode >= 48 && keycode <= 57)))) {
								event.preventDefault();
							}
						});

		$('#Number')
				.keypress(
						function(event) {
							var keycode = event.which;
							if (!(event.shiftKey == false && (keycode == 46
									|| keycode == 8 || keycode == 37
									|| keycode == 39 || (keycode >= 48 && keycode <= 57)))) {
								event.preventDefault();
							}
						});
		$(document).keydown(function(event) {
			if (event.keyCode == 27) {
				$('.modal').hide();

			}
		});

		var modal = document.getElementById('id02');
		//When the user clicks anywhere outside of the modal, close it
		window.onclick = function(event) {
			if (event.target == modal) {
				modal.style.display = "none";
			}
		}

		// Get the modal
		var modal = document.getElementById('id01');
		// When the user clicks anywhere outside of the modal, close it
		window.onclick = function(event) {
			if (event.target == modal) {
				modal.style.display = "none";
			}
		}
	</script>
	<!-- <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>  -->
	<script type="text/javascript">
		var bankcod = $("#bankcd").val();
		$("#Bancd").val(bankcod);
		$("#noteheader").text(bankcod);

		$(function datepicker() {
			$('#ReportDate').datepicker({
				dateFormat : "dd/mm/yy"
			}).val()

		}); 
		$(document).ready(function()

		{
			$("#productcode").attr('maxlength', '4');
		});
		$("#productcode").on("input", function() {
			var regexp = /[^a-zA-Z]/g;
			if ($(this).val().match(regexp)) {
				$(this).val($(this).val().replace(regexp, ''));
			}
		});
		$("#accnttype").on("input", function() {
			var regexp = /[^a-zA-Z]/g;
			if ($(this).val().match(regexp)) {
				$(this).val($(this).val().replace(regexp, ''));
			}
		});
		$(document).ready(function() {

			$("#btn").click(function() {

				$('#myModal').modal('show');
			});
		});

		$('#button4').one('click',function(e) {
					retriveData();
					e.preventDefault();
					
				});

		$('#example').on('click', '.btn-details', function(e) {
			midifyPopup(e, this);

		});
		
		 $("#updatebtn").on("click", function(e) {
			updateticketdetails(e);
		}); 
		$("#deletebtn1").on("click", function(e) {
			deleteticketdetails(e);
		});
		
		
		
		
	</script>
	<%-- </center> --%>

</body>
</html>






