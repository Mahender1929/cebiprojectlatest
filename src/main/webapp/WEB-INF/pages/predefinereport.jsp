<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="header.jsp"%>
<link rel="stylesheet" href="css/style.css">


<style type="text/css">
.btn-primary-new, .transmitbtn{
background:transparent;color:#000;
border-color:#357ebd;}
.btn-primary-new:HOVER{background: #337ab7;color:#fff;}
.transmitbtn:HOVER{background: #337ab7;color:#fff;}
.btn-group button {
	background-color: #4CAF50; /* Green background */
	border: 1px solid green; /* Green border */
	color: white; /* White text */
	padding: 10px 24px; /* Some padding */
	cursor: pointer; /* Pointer/hand icon */
	width: 50%; /* Set a width if needed */
	display: block; /* Make the buttons appear below each other */
}

.btn-group button:not(:last-child)
{
border-bottom:none; /* Prevent double borders */
}

/* Add a background color on hover */
.btn-group button:hover {
	background-color: #3e8e41;
}

.select2-results li {
	color: black;
	font-weight: 500;
	font-size: 12px;
}

.round-button {
	display: block;
	width: 80px;
	height: 80px;
	line-height: 80px;
	border: 2px solid #f5f5f5;
	border-radius: 50%;
	color: #f5f5f5;
	text-align: center;
	text-decoration: none;
	background: #f9f9f9;
	box-shadow: 0 0 3px gray;
	font-size: 20px;
	font-weight: bold;
}

.round-button:hover {
	background: #777555;
}

table {
	table-layout: fixed;
}

.result {
	font-family: -webkit-body;
	margin-top: 0%;
	color: red;
	background: aliceblue;
}

#errmsg {
	color: red;
}

#serverValidation {
	color: red;
	margin-top: 48px;
}

#message {
	margin-left: 85px;
	color: red;
}

.table_select_error {
	border-color: red;
}

.acc_table {
	width: 100%;
	color: gainsboro;
	border: solid;
	margin-bottom: 7%;
}

.scrollit {
/* 	overflow: scroll; */
	height: 643px;
}

.acc_table thead {
	background: linear-gradient(to bottom, rgba(28, 90, 169, 1) 0%,
		rgba(20, 113, 187, 1) 100%);
	color: #fff;
}

.acc_table td {
	text-align: center;
	padding: 1px;
	border: 1px solid #ccc;
	background-color: #f1f1f100;;
}

.acc_table td input {
	float: none;
	display: inline;
	width: 100%
}

#drag_message {
	color: red;
}

.iptext_message {
	color: red !important;
}

ul>li, ol>li {
	line-height: 1.4;
	padding-left: 0;
	border-bottom: 1px solid #ccc;
}

.btn {
	text-align: left;
}

.table_field a {
	margin-right: 5px;
	margin-bottom: 5px;
}
/* .btn-success:focus{background-color: #E94057;border-color: #E94057;} */
.btn-success:hover {
	background-color: #339bb7 !important;
	border-color: #339bb7 !important;
}

.scrollable {
	overflow-y: auto;
	-webkit-overflow-scrolling: touch;
	padding-top: 0px;
}

.header-color {
	line-height: 10px !important;
}

.select2-container .select2-choice {
	padding: 0 0 0 5px;
	background: none;
	border: none;
}

tbody {
	height: 100px;
	overflow-y: auto;
	overflow-x: hidden;
}

.select2-container .select2-choice .select2-arrow {
	background: none;
	border: 0;
	border-radius: 0;
}

.select2-results .select2-highlighted .select2-result-label {
	color: #fff !important;
}
</style>
</head>
<body>
	<div id="main">
		<!-- <div class="header-color"><span style="font-size:25px;cursor:pointer" onclick="openNav()">&#9776;</span></div> -->
		<div>
			<div class="report_bg_patch scrollit">
				<!-- <div class="row">
					<h4 class="text-center bold b-b report_head m-t-0"
						style="height: 20px; background: #ff9797; border-radius: 37;">PreDefine
						Reports</h4>
				</div> -->
				<div class="row">
					<div class="col-md-12">
						<div class="col-md-10 no-padding">
							<div class="clearfix"></div>
							<span id="message"></span>
						</div>
						<div class="clearfix"></div>
						<div style="display: none;" id='table_field' class="drag_drop_div">
							<label class="field_head">Select Reports Fields</label>&nbsp;&nbsp;<span
								class="fs-12">(Please click on field)</span>
							<div class="clearfix"></div>
							<div class="hide col-md-6 p-r-5 p-l-0" id="div1"
								ondrop="drop(event)" ondragover="allowDrop(event)">
								<div class="table_field b-grey" id="drop"></div>
							</div>
							<div class="col-md-12 p-l-5 p-r-0" id="div2" ondrop="drop(event)"
								ondragover="allowDrop(event)">
								<div class="table_field b-grey" id="drag">
									<%-- <c:out value="${favouriteLists.parameter}"></c:out> --%>
								</div>
								<span id="drag_message"></span>
							</div>
						</div>
					</div>
				</div>
				<div class="row"><p style="color:#000;margin: 5px 0px 5px 15px;">Query Runner</p></div>
				<div class="row">
					<div class="col-md-12" style="padding-bottom: 1%;">
						<form:form modelAttribute="MAPREREPO" action="" method="post" id="saveform">
							<div>
								<textarea placeholder="Enter Query here...!" id="txtarea" rows="7" path="saveQuery" style="width:100%;"
									required="required" name="saveQuery"></textarea>
							</div>
							<div ><p>'WHERE' condition is mandatory</p></div>
							<input type="hidden" id="Id" path="id" name="id"
								autocomplete="off" placeholder="" />
							<input type="hidden" id="sts" path="sts" name="sts"
								autocomplete="off" placeholder="" />
							<input type="hidden" id="bankCode" path="bankCode"
								name="bankCode" autocomplete="off" placeholder="" />
								
	</form:form>
					</div>
					<div class="col-md-4" style="padding-bottom: 1%;">
						<div class="row">
								<button id="transmit" type="button"
									class="btn transmitbtn">
									<i class="fa fa-play"></i> <span>Run</span>
								</button>
								<button class="btn btn-primary-new" id="clrbtn" onclick="clrtxt()">
									<i class="glyphicon glyphicon-remove"></i> <span>Clear</span>
								</button>
								<button class="btn btn-primary-new" id="saveQry" data-toggle="modal"
									data-target="#myModal">
									<i class="glyphicon glyphicon-save"></i> <span>Save</span>
								</button>
								<button class="btn btn-primary-new" data-toggle="modal" href="#myModale">
									<i class="glyphicon glyphicon-saved"></i> <span>Saved Queries</span>
								</button>
							
							
						
							<!--			
							
						</form:form>	
							
								<div class="row" style="padding: 1%; padding-left: 35%;">
							<button type="button" id="saveQry" class="btn btn-primary "
									style="background-color: green;" data-toggle="modal"
									data-target="#myModal">
									<span class="glyphicon glyphicon-save"></span>Save
								</button>
								
								<button type="button" id="clrbtn" class="btn btn-primary"
									style="background-color: #ff4646;" onclick="clrtxt()">
									<span class="glyphicon glyphicon-remove"></span>Clear
								</button>
								
									
								<button type="button" class="btn btn-primary"
									data-toggle="modal" href="#myModale"
									style="background-color: #080808;">
									<span class="glyphicon glyphicon-saved"></span>Saved Queries
								</button>
								
							</div> -->
						</div>
					</div>
					<div class="col-md-6" style="padding-bottom: 1%;">
					<div class="result" id="errmsg" style="margin-left:2%;margin-top: 1%;"></div>
					</div>
					
				</div>
				<div class="row" id="tabledata" style="margin: 2px 0;">
					<div class="col-md-12" style="padding-bottom: 2%;">
							<div>
								<div id="menu4"></div>
								<div>
									
							<!-- 		<form id="csvForm" method="post" 
											action="/prcsvdownload">
											<button type="submit" id="csvbtn"
										class="btn btn-default btn-sm"
										style="color: white; background: #884d7b;">
										<span class="glyphicon glyphicon-save-file"></span> CSV
									</button> 
										</form> -->
										<div style="float: left;">
										<form id="csvForm" method="post"
											action="${pageContext.request.contextPath}/prcsvdownload">
											<input type="hidden" id="csvquery" name="csvquery" class="csv" >
											<button class="btn btn-primary csv" type="submit"><span class="glyphicon glyphicon-save-file"></span> CSV</button>
										</form>
										</div>
										<div style="float: left;margin-left: 5px;">
										<form id="txtForm" method="post"
											action="${pageContext.request.contextPath}/prtxtdownload">
											<input type="hidden" id="txtquery" name="txtquery" class="csv" >
											<button class="csv btn btn-primary" type="submit"><span class="glyphicon glyphicon-save-file"></span> TEXT</button>
										</form>
										</div>
										<div style="float: left;margin-left: 5px;">
											<form id="txtForm" method="post"
											action="${pageContext.request.contextPath}/prxlsdownload">
											<input type="hidden" id="xlsquery" name="xlsquery" class="csv" >
											<button class="csv btn btn-primary" type="submit"><span class="glyphicon glyphicon-save-file"></span> EXCEL</button>
										</form>
										</div>
										
									
									
							 	 <!-- <button type="button" id=""
										class="btn btn-default btn-sm" 
										style="color: white; background: #884d7b;">
										<span class="glyphicon glyphicon-save-file"></span> CSV
									</button>   -->
									<!-- <button type="button" id="ptxtbtn"
										class="btn btn-default btn-sm"
										style="color: white; background: #435f2a;">
										<span class="glyphicon glyphicon-save-file"></span> TEXT
									</button>
									<button type="button" id="pxlsbtn"
										class="btn btn-default btn-sm"
										style="color: white; background: #a29f35;">
										<span class="glyphicon glyphicon-save-file"></span> EXCEL
									</button> -->
								</div>
							</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	</div>
	</div>
	</div>
	<div id="fade"></div>
	<div id="modal"
		style="height: auto; width: auto; padding: 0; border-radius: 0;">
		<img id="loader" style="width: 150px;" src="images/loading5.gif" />
	</div>



	<!-- -----------------------Add To  popup----------------------------------- -->

	<!-- Modal -->
	<div class="modal" id="myModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content animate">
				<!-- Modal Header -->
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span> <span class="sr-only">Close</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">Save Query with
						Details</h4>
				</div>

				<!-- Modal Body -->
				<div class="modal-body">

					<form id="formdata" role="form">
						<div class="form-group">
							<label for="qryTitle">Query Title</label> <input type="text"
								name="qryTitle" class="form-control" id="qryTitle"
								placeholder="Enter query title" />
						</div>
						<div class="form-group">
							<label for="purpose">Description</label>
							<textarea name="description" class="form-control"
								id="description" placeholder="Enter description"></textarea>
						</div>
						<div class="form-group">
							<label for="purpose">Purpose</label>
							<textarea name="purpose" class="form-control" id="purp"
								placeholder="Enter purpose"></textarea>
						</div>
						<button id="favouriteForm" style="width: 80px" type="button"
							class="btn btn-success report-btn">
							<span class="glyphicon glyphicon-saved"></span></span>Save
						</button>
						<button id="resetbtn" style="background: #fd1616; width: 80px;"
							type="reset" class="btn btn-success report-btn">
							<span class="glyphicon glyphicon-remove"></span>Reset
						</button>

					</form>
				</div>

				<!-- <div class="col-md-12">
								<div class="row" style=" padding: 13px; ">
								<button type="button" style=" background-color: yellowgreen; "  class="btn btn-primary btn-block"
									onclick="getData()" 
									id="trnsmit">
									<span class="glyphicon glyphicon-send" style=" margin-left: 36%; "> </span> Transmit
								</button>
								</div>
								<div class="row" style=" padding: 13px; ">
								<button type="button" class="btn btn-primary btn-block"
									onclick="location.reload(true);">
									<span class="glyphicon glyphicon-refresh" style=" margin-left: 36%; "> </span> Refresh
								</button>
								</div>
									<div class="row" style=" padding: 13px; ">
								<button type="submit" class="btn btn-primary btn-block" style=" background-color: darkmagenta; "  id="print"
								 margin-top: 5%;">
									<span class="glyphicon glyphicon-list-alt" style=" margin-left: 36%; "> </span> Print
								</button>
								</div>
							</div> -->


				<!-- Modal Footer -->
				<div class="modal-footer">
					<button type="button" class="btn btn-success report-btn"
						data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>

	</div>

	<!-- -----------------------Add To table popup----------------------------------- -->


	<div class="modal fade" id="myModale">
		<div
			class="modal-modal-dialog  modal-fluid modal-full-width modal-right"
			style="width: 118%; padding-left: -4%; margin-left: -10%; margin-top: -6%;">
			<div class="modal-content">
				<div class="modal-body" style="background-color: aliceblue;">
					<table class="acc_table" id="qry_table">
						<thead>
							<tr>
								<td style="">QID</td>
								<td>SavedQuery</td>
								<td>QryTitle</td>
								<td>Description</td>
								<td>Purpose</td>
								<td>Get</td>
								<td>Delete</td>
							</tr>
						</thead>
						<tbody id="qrytbl">
							<c:set var="counter" value="0" />
							<c:forEach var="predefine" items="${list}">
								<tr>
									<c:set var="counter" value="${predefine.id}" />
									<c:set var="string1" value="${predefine.saveQuery}" />
									<td
										style="font-style: normal; font: bolder; color: #d824d1; width: 3%"
										align="center">${counter}</td>

									<td data-toggle="tooltip" title="${string1}"
										style="font-size: smaller; font-style: normal; text-align: -webkit-left; color: #bd098d; font-family: auto;">${string1}</td>

									<td id="title" style="color: red; font-style: normal;">${predefine.title}</td>
									<td id="descp" style="color: blue; font-style: black;">${predefine.descp}</td>

									<td id="purpose" style="color: black; font-style: normal;">${predefine.purpose}</td>


									<td data-toggle="
													tooltip" title="Get this Query""><button
											type="button" value="${predefine.id}"
											class="btn btn-default btn-sm get"
											style="padding-bottom: 13%; background: #43b912f5;">
											<span style="color: white;" class="glyphicon glyphicon-edit"></span>
										</button></td>
									<td data-toggle="tooltip" title="Delete"">
										<button type="button" class="btn btn-default btn-sm deletebtn"
											id="detbtn" value="${predefine.id}"
											style="padding-bottom: 13%; background: #d03920;">
											<span style="color: white;"
												class="glyphicon glyphicon-remove-sign"></span>
										</button>
									</td>
								</tr>
							</c:forEach>
						</tbody>

						<tfoot></tfoot>
					</table>
				</div>
				<div class="modal-footer" style="background-color: aliceblue;">
					<button type="button" style="background-color: red; color: white;"
						class="btn btn-default" data-dismiss="modal">Close</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- /.modal -->

	<!-- -----------------------Add To table popup----------------------------------- -->

	<div id="" class="modal fade">
		<div class="modal-dialog modal-confirm">
			<div class="modal-content">
				<div class="modal-header">
					<div class="icon-box">
						<i class="material-icons">&#xE5CD;</i>
					</div>
					<h4 class="modal-title">Are you sure?</h4>
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
				</div>
				<div class="modal-body">
					<p>Do you really want to delete these records? This process
						cannot be undone.</p>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-info" data-dismiss="modal">Cancel</button>
					<button type="button" class="btn btn-danger">Delete</button>
				</div>
			</div>
		</div>
	</div>

	<div style="margin-top: -3%;">
		<%@ include file="/WEB-INF/pages/Footer.jsp"%>
	</div>
	<!-- -------------------------------Add To Favourite popup End---------------------------- -->
	
	<!-- <script language="javascript" type="text/javascript">
    function submitDetailsForm() {
       $("#saveform").submit();
    }
</script> -->
	
	
	<script type="text/javascript">
		$(document).ready(function() {
			$('#qry_table').DataTable({
				columnDefs : [ {
					width : 815,
					targets : 1
				}, {
					width : 15,
					targets : 0
				}, {
					width : 35,
					targets : 2
				}, {
					width : 80,
					targets : 3
				}, {
					width : 70,
					targets : 4
				}, {
					width : 35,
					targets : 5
				}, {
					width : 35,
					targets : 6
				}

				],
				fixedColumns : true
			});

		});
	</script>
	<script type="text/javascript" src="js/predefinerepoort.js"></script>
	<script type="text/javascript" src="js/jquery.validate.js"></script>
</body>
</html>