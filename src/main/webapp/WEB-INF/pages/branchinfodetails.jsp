<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="header.jsp" %>
<title>static report page</title>
<link rel="stylesheet" href="css/style.css">
<style type="text/css">
.container-fluid{
margin-top:20% !important;
}
 .form-control{
    width: 70%;
    padding: 12px 20px;
    margin: 8px 0;
    display: inline-block;
    border: 1px solid #ccc;
    box-sizing: border-box;
    border-radius: 5px;
    padding-right: 15px;
    padding-left: 15px;
} 
.modal-dialog2 {
    width: 600px;
    margin: 140px auto;
}
.header-color {
    line-height: 50px !important;
    }
    .hidebar{
display:none;
}
.btn-success:hover{background-color: #339bb7 !important;border-color: #339bb7 !important;}
#updatebtn:hover{background-color: #339bb7 !important;border-color: #339bb7 !important;}
.cancelbtn2:hover{background-color: #ff6666 !important;border-color: #ff6666 !important;}
</style>

</head>

<body>
	<center>
	<!-- <h1>Branch Details</h1> -->
		<div class="scrollable">
		<div class="b-b b-r b-l b-t b-grey loan_label" style="width:1270px; padding: 5px 10px 40px; background: #fff; box-shadow: 0 0 8px rgba(0, 0, 0, 0.5);">
		<h4 class="text-center bold " style="height: 45px; background: #fff; border-radius: 1px; position:absolute;text-align: left;  padding-left: 50px; ">
					<span class="glyphicon">&#xe086; </span>&nbsp;<span> BranchInformation</span>
				
		</h4>
				 	
			<div class="report_bg_patchbranch">
			
				<table id="example" style="width:100%" border="2px">
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

					<c:if test="${not empty lists}">
						<ul>
							<tbody id="del">
								<c:forEach var="listValue" items="${lists}">
									<tr>
										<td>${listValue.id.brcdBankCd}</td>
										<td>${listValue.id.reportDate}</td>
										<td>${listValue.id.branchCode}</td>
										<td>${listValue.id.acctNo}</td>
										<td style="text-align: center; color: blue">
											<button onclick="document.getElementById('id02').style.display='block'" type="button" class="btn-details btn btn-info btn-sm">
												<span class="glyphicon glyphicon-pencil"></span>
											</button>
                                        </td>
                                       <td style="text-align: center;">
										<button type="button" class=" btn btn-info btn-sm" style="background-color: #660066; border: 1px solid transparent;" onclick="m1('${listValue.id.acctNo}','${listValue.id.branchCode}')">
											<span class="glyphicon glyphicon-trash"></span>
										</button>
										</td>
								<td class="hide">${listValue.module}</td>
								<td class="hide">${listValue.circle}</td>
								<td class="hide">${listValue.region}</td>
								<td class="hide">${listValue.branchName}</td>
								<td class="hide">${listValue.segment}</td>
								<td class="hide">${listValue.cifNo}</td>
								<td class="hide">${listValue.sector}</td>
								<td class="hide">${listValue.facilityType}</td>
								<td class="hide">${listValue.borrName}</td>
								<td class="hide">${listValue.limitAmt}</td>
								<td class="hide">${listValue.dp}</td>
								<td class="hide">${listValue.irregularAmt}</td>
								<td class="hide">${listValue.natOfBills}</td>
								<td class="hide">${listValue.securityAmt}</td>
								<td class="hide">${listValue.chargesCreated}</td>
								<td class="hide">${listValue.primaryTang }</td>
								<td class="hide">${listValue.collateralTang }</td>
								<td class="hide">${listValue.totalTang }</td>
								<td class="hide">${listValue.constituent }</td>
								<td class="hide">${listValue.allocated }</td>
								<td class="hide">${listValue.inttRate }</td>
								<td class="hide">${listValue.crSumm }</td>
								<td class="hide">${listValue.irregularDate}</td>
								<td class="hide">${listValue.lastCrDate}</td>
								<td class="hide">${listValue.crr }</td>
								<td class="hide">${listValue.insuDate }</td>
								<td class="hide">${listValue.insuAmt }</td>
								<td class="hide">${listValue.receEcgc}</td>
								<td class="hide">${listValue.sancDate}</td>
								<td class="hide">${listValue.stockStmntDate}</td>
								<td class="hide">${listValue.totOs }</td>
								<td class="hide">${listValue.secuTangAsset}</td>
								<td class="hide">${listValue.bankSecu }</td>
								<td class="hide">${listValue.secureEcgc}</td>
								<td class="hide">${listValue.unsecured}</td>
								<td class="hide">${listValue.assetSubclass}</td>
								<td class="hide">${listValue.pbRd }</td>
								<td class="hide">${listValue.unrealisedIntt}</td>
								<td class="hide">${listValue.npaDate }</td>
								<td class="hide">${listValue.inca }</td>
								<td class="hide">${listValue.provAmt}</td>
								<td class="hide">${listValue.provUnsec}</td>
								<td class="hide">${listValue.inttSusp}</td>
								<td class="hide">${listValue.adjUnsecuPortion}</td>
								<td class="hide">${listValue.secuPortion}</td>
								<td class="hide">${listValue.rstrctrProv }</td>
								<td class="hide">${listValue.rstrctrDate}</td>
								<td class="hide">${listValue.oneNpa }</td>
								<%-- <td class="hide">${listValue.REC_STATUS}</td> --%>
					            </tr>
								</c:forEach>
						</ul>
					</c:if>
					<span style="color:red">${nocontent}</span>
					</tbody>
					<tfoot style="display: table-row-group; background: #white;">
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
					</tfoot>
				</table>
			</div>
			</div>
		</div>
	</center>
<!-- 	<script type="text/javascript" src="js/jquery-1.11.1.js"></script>
	<script type="text/javascript" src="js/bootstrap.min.js"></script>
	<script type="text/javascript" src="js/datatables.min.js"></script>
	<script type="text/javascript" src="js/BrancInfo.js"></script> -->

<script type="text/javascript" src="js/datatables.min.js"></script>
	<script>
		$(document).ready(function() {
			$('#example').DataTable({});
		});
	</script>
</body>

<!-- --Delete popup start -->

<div class="modal fade" id="myModal" role="dialog">
	<div class="modal-dialog2">
		<!-- Modal content-->
		<div class="modal-content3 animate">
			<div class="modal-header">
			
				<h4 class="modal-title"> <span style="font-weight:500;font-size:17px;">Do you want to delete?</span> </h4>
			</div><br>
			
					 <div>
						<form method="post" action="delete" id="e1">
							&nbsp;&nbsp;<input type="hidden" id="accNo" value="" name="accNo"> <input type="hidden" id="branchCode1" value="" name="branchCode"> <input type="submit"
								class="btn btn-danger btn-sm" id="deletebtn1" value="Delete" />&nbsp;&nbsp;&nbsp;
						<input type="button" id="btn1" onclick="document.getElementById('myModal').style.display='none'" class="btn btn-danger btn-sm" data-dismiss="modal" value="No" />
						</form>
					</div>
					
		
		
				
			<div class="modal-footer">
				<button type="button" onclick="document.getElementById('myModal').style.display='none'" class="btn btn-default" data-dismiss="modal">Close</button> 
			</div>
		</div>
	</div>
</div>
<!-- --Delete popup end -->
<script>
    var modal = document.getElementById('myModal');
	window.onclick = function(event) {
		if (event.target == modal) {
			modal.style.display = "none";
		}
	}

	function m1(accNo, branchCode) {
		$('#myModal').show();
		$("#accNo").val(accNo);
		$("#branchCode1").val(branchCode);
	}
</script>

                                             
                                              <!-- Edit POPUP Code Starts Here -->
                                              
                                              
         <div id="id02" class="modal2" role="dialog">
				<div class="modal-content2 animate">
					<div class="Edit" style="font-size: 28px;">
						<span class="glyphicon glyphicon-pencil" style="font-size: 17px !important;"></span>&nbsp;Edit Information
					</div><hr>
					<form:form action="displaymodify" method="POST" commandName="ccdp010" id="reportForm">
						
						<div class="col-xs-4">
						<label>Bank Cd</label><br>
						<form:input path="id.brcdBankCd" type="text" id="BankCd" placeholder="Enter Branch Code" readonly="true" class="form-control"/>
						</div>
						<div class="col-xs-4" >
						<label>Report Date</label><br>
						<form:input path="id.reportDate" type="text" id="reportdate" placeholder="Enter reportdate" readonly="true" class="form-control"/>
						</div>
						<div class="col-xs-4" >
						<label>Branch Code</label><br>
						<form:input path="id.branchCode" type="text" id="branchcode" placeholder="Enter BranchCode" readonly="true" class="form-control"/>
						</div> 
						<div class="col-xs-4" >
						<label class=>Module</label><br>
						<form:input path="module" type="text" id="module" placeholder="Enter Module" class="form-control"/>
						</div>
						<div class="col-xs-4" >
						<label>Circle</label><br>
						<form:input path="circle" type="text" id="circle" placeholder="Enter Circle" class="form-control"/>
						</div>
						<div class="col-xs-4" >
						<label>Region</label><br>
						<form:input path="region" type="text" id="region" placeholder="Enter Region" class="form-control"/>
						</div>
						<div class="col-xs-4">
						<label>branch Name</label><br>
						<form:input path="branchName" type="text" id="branchname" placeholder="Enter Branch name" class="form-control"/>
						</div>
						<div class="col-xs-4" >
						<label>Segment</label><br>
						<form:input path="segment" type="text" id="segment" placeholder="Enter Segment" class="form-control"/>
						</div>
						<div class="col-xs-4" >
						<label>Cif No</label><br>
						<form:input path="cifNo" type="text" id="cifno" placeholder="Enter CifNo" class="form-control"/>
						</div>
						<div class="col-xs-4" >
						<label>Sector</label><br>
						<form:input path="sector" type="text" id="sector" placeholder="Enter Sector" class="form-control"/>
						</div>
						<div class="col-xs-4" > 
						<label>Facility Type</label><br>
						<form:input path="facilityType" type="text" id="facility Type" placeholder="Enter Facility Type" class="form-control"/>
						</div>
						<div class="col-xs-4" >
						<label>Acc No</label><br>
						<form:input path="id.acctNo" type="text" id="accno" placeholder="Enter Region" readonly="true" class="form-control"/>
						</div>
						<div class="col-xs-4">
						<label>Borr path</label><br>
						<form:input path="borrName" type="text" id="BorrName" placeholder="Enter AccNo" class="form-control"/>
						</div>
						<div class="col-xs-4" >
						<label>Limit Amt</label><br>
						<form:input path="limitAmt" type="text" id="Limitamt" placeholder="Enter Limit Amt" class="form-control"/>
						</div>
						<div class="col-xs-4" >
						<label>Dp</label><br>
						<form:input path="dp" type="text" id="dp" placeholder="Enter DP" class="form-control"/>
						</div>
						<div class="col-xs-4" >
						<label>Irregular Amt</label><br>
						<form:input path="irregularAmt" type="text" id="irregularamt" placeholder="Enter Irregular_Amt:" class="form-control"/>
						</div>
						<div class="col-xs-4" >
						<label>Nat Of Bills</label><br>
						<form:input path="natOfBills" type="text" id="natOfbills" placeholder="Enter NAT_Of_Bills" class="form-control"/>
						</div>
						<div class=" col-xs-4" >
						<label>Security Amt</label><br>
						<form:input path="securityAmt" type="text" id="securityamt" placeholder="Enter SecurityAmt" class="form-control"/>
						</div>
						<div class="col-xs-4">
						<label>Charges Created</label><br>
						<form:input path="chargesCreated" type="text" id="chargescreated" placeholder="Enter Charges Created" class="form-control"/>
						</div>
						<div class="col-xs-4" >
						<label >Primary Tang</label><br>
						<form:input path="primaryTang" type="text" id="primaryTang" placeholder="Enter Primary Tang" class="form-control"/>
						</div>
						<div class="col-xs-4" >
						<label>Collateral Tang</label><br>
						<form:input path="collateralTang" type="text" id="collateralTang" placeholder="Enter Collateral Tang" class="form-control"/>
						</div>
						<div class="col-xs-4" >
						<label>Total Tang</label><br>
						<form:input path="totalTang" type="text" id="totalTang" placeholder="Enter Total Tang" class="form-control"/>
						</div>
						<div class="col-xs-4" >
						<label>Constituent</label><br>
						<form:input path="constituent" type="text" id="constituent" placeholder="Enter Constituent" class="form-control"/>
						</div>
						<div class="col-xs-4" >
						<label>Allocated</label><br>
						<form:input path="allocated" type="text" id="allocated" placeholder="Enter Allocated" class="form-control"/>
						</div>
						<div class="col-xs-4">
						<label>Init Rate</label><br>
						<form:input path="inttRate" type="text" id="inttrate" placeholder="Enter InitRate" class="form-control"/>
						</div>
						<div class="col-xs-4" >
						<label>Cr Sum</label><br>
						<form:input path="crSumm" type="text" id="crsumm" placeholder="Enter CR_SUM" class="form-control"/>
						</div>
						<div class="col-xs-4" >
						<label>Irregular Date</label><br>
						<form:input path="irregularDate" type="text" id="irregulardate" placeholder="Enter BranchCode" class="form-control"/>
						</div>
						<div class="col-xs-4" >
						<label>Last CrDate</label><br>
						<form:input path="lastCrDate" type="text" id="lastcrdate" placeholder="Enter IrregularDate" class="form-control"/>
						</div>
						<div class="col-xs-4" >
						<label>Crr</label><br>
						<form:input path="crr" type="text" id="crr" placeholder="Enter CRR" class="form-control"/>
						</div>
						<div class="col-xs-4" >
						<label>Insu_Date</label><br>
						<form:input path="insuDate" type="text" id="insudate" placeholder="Enter Insu_Date" class="form-control"/>
						</div>
						<div class="col-xs-4">
						<label>Insu Amt</label><br>
						<form:input path="insuAmt" type="text" id="insuamt" placeholder="Enter Insu_Amt" class="form-control"/>
						</div>
						<div class="col-xs-4" >
						<label>Rece Ecgc</label><br>
						<form:input path="receEcgc" type="text" id="receccgc" placeholder="Enter Rece_ecgc" class="form-control"/>
						</div>
						<div class="col-xs-4" >
						<label>Sanc_Date</label><br>
						<form:input path="sancDate" type="text" id="sancDate" placeholder="Enter Sanc_Date" class="form-control"/>
						</div>
						<div class="col-xs-4" >
						<label>Stock StmtDate</label><br>
						<form:input path="stockStmntDate" type="text" id="stockstmtdate" placeholder="Enter StmtDate" class="form-control"/>
						</div>
						<div class="col-xs-4" >
						<label>Tot Os</label><br>
						<form:input path="totOs" type="text" id="totos" placeholder="Enter TotOs" class="form-control"/>
						</div>
						<div class="col-xs-4" >
						<label>SecuTand Asset</label><br>
						<form:input path="secuTangAsset" type="text" id="secutandasset" placeholder="Enter SecuTand Asset" class="form-control"/>
						</div>
						<div class="col-xs-4">
						<label>Bank Secu</label><br>
						<form:input path="bankSecu" type="text" id="BankSecu" placeholder="Enter BankSecu" class="form-control"/>
						</div>
						<div class="col-xs-4" >
						<label>Secu Ecgc</label><br>
						<form:input path="secureEcgc" type="text" id="SecuEcgc" placeholder="Enter SecuEcgc" class="form-control"/>
						</div>
						<div class="col-xs-4" >
						<label>Unsecured</label><br>
						<form:input path="Unsecured" type="text" id="unsecured" placeholder="Enter Unsecured" class="form-control"/>
						</div>
						<div class="col-xs-4" >
						<label>Asset SubClass</label><br>
						<form:input path="assetSubclass" type="text" id="assetsubclass" placeholder="Enter AssetSubClass" class="form-control"/>
						</div>
						<div class="col-xs-4" >
						<label>Pb Rd</label><br>
						<form:input path="pbRd" type="text" id="pbRd" placeholder="Enter PB_RD" class="form-control"/>
						</div>
						<div class="col-xs-4" >
						<label>Unrealised Init</label><br>
						<form:input path="unrealisedIntt" type="text" id="UnrealisedIntt" placeholder="Enter UnrealisedInit" class="form-control"/>
						</div>
						<div class="col-xs-4">
						<label>Npa Date</label><br>
						<form:input path="npaDate" type="text" id="npaDate" placeholder="Enter NPA_DATE" class="form-control"/>
						</div>
						<div class="col-xs-4" >
						<label >INCA</label><br>
						<form:input path="inca" type="text" id="inca" placeholder="Enter INCA" class="form-control"/>
						</div>
						<div class="col-xs-4" >
						<label>Prov Amt</label><br>
						<form:input path="provAmt" type="text" id="provamt" placeholder="Enter ProvAmt" class="form-control"/>
						</div>
						<div class="col-xs-4" >
						<label>Prov Unsec</label><br>
						<form:input path="provUnsec" type="text" id="provUnsec" placeholder="Enter ProvUnsec" class="form-control"/>
						</div>
						<div class="col-xs-4" >
						<label>Init Susp</label><br>
						<form:input path="inttSusp" type="text" id="initsusp" placeholder="Enter InitSusp" class="form-control"/>
						</div>
						<div class="col-xs-4" >
						<label>AdjUnsecu Portion</label><br>
						<form:input path="adjUnsecuPortion" type="text" id="adjunsecuportion" placeholder="Enter AdjUnsecuPortion" class="form-control"/>
						</div>
						<div class="col-xs-4" >
						<label>Secu Portion</label><br>
						<form:input path="secuPortion" type="text" id="secuportion" placeholder="Enter SecuPortion" class="form-control"/>
						</div>
						<div class="col-xs-4" >
						<label>RSTRCTR Prov</label><br>
						<form:input path="rstrctrProv" type="text" id="rstrctrprov" placeholder="Enter RSTRCTRProv" class="form-control"/>
						</div>
						<div class="col-xs-4" >
						<label>RSTRCTR Date</label><br>
						<form:input path="rstrctrDate" type="text" id="rstrctrdate" placeholder="Enter RSTRCTRDate" class="form-control"/>
						</div>
						<div class="col-xs-4" >
						<label>One Npa</label><br>
						<form:input path="oneNpa" type="text" id="onenpa" placeholder="Enter OneNpa" class="form-control"/>
						</div>
						<%-- <div class="col-xs-4" >
						<label  >Rec Status</label><br>
						<form:input path="REC_STATUS" type="text" id="recstatus" placeholder="Enter rec status" class="form-control"/>
						</div> --%><br>
						
						<div class="text-center">
						<form:button type="submit" class="button1" id="updatebtn">Update</form:button>&nbsp; &nbsp;
      					
      					<button type="button" onclick="document.getElementById('id02').style.display='none'" class="cancelbtn2">Cancel</button> 
 						</div>
 							
					</form:form>

				</div>
			</div> 
	<%@ include file="/WEB-INF/pages/Footer.jsp"%>
			<script>
			$(document).keydown(function(event) {
				if (event.keyCode == 27) {
					$('.modal').hide();

				}
			});
			$(document).keydown(function(event) {
				if (event.keyCode == 27) {
					$('.modal2').hide();

				}
			});
		</script>

			<script>
		var modal = document.getElementById('id02'); //When the user clicks anywhere outside of the modal, close it 
		window.onclick = function(event) { if (event.target == modal) {
			modal.style.display ="none";
			} 
		}
			var bankcod = $("#brcCode").val();
			$("#BankCd").val(bankcod);
			
			$('#example').on('click', '.btn-details', function(e) {
				midifyPopup(e, this);
			});
			
			/* $("#updatebtn").on("click",function(e){
				updateticketdetails(e);
	   }); */
	 
			
		</script>









</html>