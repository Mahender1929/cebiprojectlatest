<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="header.jsp"%>
<link rel="stylesheet" href="css/style.css">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
<style type="text/css">
.btn-group button {
	background-color: #4CAF50; /* Green background */
	border: 1px solid green; /* Green border */
	color: white; /* White text */
	padding: 10px 24px; /* Some padding */
	cursor: pointer; /* Pointer/hand icon */
	width: 50%; /* Set a width if needed */
	display: block; /* Make the buttons appear below each other */
}

.btn-group


button

.sorting_1{
    height: 10px;
}


:not

.vl {
	border-left: 6px solid green;
	height: 500px;
}

(
:last-child








)
{
border-bottom







:







none









; /* Prevent double borders */
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
	color: #000;
	margin-bottom: 7%;
}

.scrollit {
	overflow: scroll;
	height: 425px;
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

.field {
	margin: 0;
	width: 100%;
}
</style>
</head>
<body>
	<div id="main">
		<!-- <div class="header-color"><span style="font-size:25px;cursor:pointer" onclick="openNav()">&#9776;</span></div> -->
		<div>
			<div class="report_bg_patch">
				<div class="row text-center">
					<h3 style="width: 100%;">Old Statement</h3>
				</div>
				<div class="col-md-3 b-b b-r b-l b-t b-grey loan_label"
					style="padding-bottom: 253px;background-color: honeydew;">
					<div class="row text-center">
					<h5 style="width: 100%;">Get Old Statement</h5>
				</div>
					<form id="form" method="POST" action="printdatabyacc"
						modelAttribute="MAFORMDATA">
						<div class="form-group">
							<div class="row">
								<div class="col-md-12">
									<label class="new_for_label"> Account No. </label> <input
										type="text" class="form-control new_for_text"
										required="required" id="accNum" name="accNum" style="color:black;font-weight: bold;" 
										onkeypress="javascript:return isNumber(event)" path="accNum"
										autocomplete="off" placeholder="A/c No." />
								</div>
							</div>
							<div class="row">
								<div class="col-md-12">
									<label class="new_for_label"> From Date </label> <input
										type="text" class="form-control datepicker new_for_text"
										required="required" autocomplete="off" id="datepicker-13" style="color:black;font-weight: bold;"
										name="frDate" path="frDate" placeholder="DD/MM/YYYY" />
								</div>
							</div>
							<div class="row">
								<div class="col-md-12">
									<label class="new_for_label"> Date To </label> <input
										type="text" class="form-control datepicker new_for_text"
										required="required" autocomplete="off" id="datepicker" style="color:black;font-weight: bold;"
										name="toDate" path="toDate" placeholder="DD/MM/YYYY" />
								</div>
							</div>
							<input type="hidden" name="instNo" path="instNo"> <input
								type="hidden" name="journalNo" path="journalNo"> <input
								type="hidden" name="crdr" path="crdr"> <input
								type="hidden" name="txCode" path="txCode"> <input
								type="hidden" name="tlrBranch" path="tlrBranch"> <input
								type="hidden" name="narration" path="narration"> <input
								type="hidden" name="txAmount" path="txAmount"> <input
								type="hidden" name="balance" path="balance"> <input
								type="hidden" name="postTime" path="postTime"> <input
								type="hidden" name="txDate" path="txDate"> <input
								type="hidden" name="cheqNo" path="cheqNo"> <input
								type="hidden" name="intCat" path="intCat"> <input
								type="hidden" name="custName" path="custName"> <input
								type="hidden" name="custNo" path="custNo"> <input
								type="hidden" name="phBusNo" path="phBusNo"> <input
								type="hidden" name="mobNO" path="mobNO"> <input
								type="hidden" name="address" path="address"> <input
								type="hidden" name="currBalace" path="currBalace"> <input
								type="hidden" name="totalDr" path="totalDr"> <input
								type="hidden" name="totalCr" path="totalCr"> <input
								type="hidden" name="crCount" path="crCount"> <input
								type="hidden" name="drCount" path="drCount"> <input
								type="hidden" name="currTime" path="currTime"> <input
								type="hidden" name="currDate" path="currDate">
							<div class="col-md-12">
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
							</div>
						</div>

					</form>
				</div>
				<div id="form2" class="col-md-9 b-b b-r b-l b-t b-grey loan_label " style="background-color: #fff6f6;">
				<h4 style="margin-left: 42%; /* align-content: center; *//* align-items: center; */">Customer Details</h4>
					<div class="form-group">
						<div class="row">
							<div class="col-md-3">
								<label class="new_for_label"> Customer No. </label> <input
									type="text" class="form-control new_for_text field"
									readonly="readonly" id="custno" name="custNo" />
							</div>
							<div class="col-md-6">
								<label class="new_for_label">Customer Name </label> <input
									type="text" class="form-control new_for_text field"
									readonly="readonly" id="custName" name="custName" />
							</div>
							<div class="col-md-3">
								<label class="new_for_label">Branch</label> <input type="text"
									class="form-control new_for_text field" id="branch"
									readonly="readonly" name="branch" />
							</div>
						</div>
						<div class="row" style="margin-top: -48px;">
							<div class="col-md-6">
								<label class="new_for_label"> Address </label> <input
									type="text" class="form-control new_for_text field"
									readonly="readonly" id="address" name="address" />
							</div>
							<div class="col-md-3">
								<label class="new_for_label">Mobile No</label> <input
									type="text" class="form-control new_for_text field" id="mobNo"
									readonly="readonly" name="mobNo" />
							</div>
							<div class="col-md-3">
								<label class="new_for_label">Date</label> <input type="text"
									class="form-control new_for_text field" id="date"
									readonly="readonly" name="date" />
							</div>
						</div>
						<div class="row" style="margin-top: 5px;">
							<div class="col-md-3">
								<label class="new_for_label"> Current Balance </label> <input
									type="text" class="form-control new_for_text field"
									readonly="readonly" id="bal" name="bal" />
							</div>
							<div class="col-md-3">
								<label class="new_for_label"> Total Credit </label> <input
									type="text" class="form-control new_for_text field"
									readonly="readonly" id="tcr" name="tcr" />
							</div>
							<div class="col-md-3">
								<label class="new_for_label"> Total Debit </label> <input
									type="text" class="form-control new_for_text field"
									readonly="readonly" id="tdr" name="tdr" />
							</div>
							<div class="col-md-3">
								<label class="new_for_label">Time</label> <input type="text"
									class="form-control new_for_text field" id="time"
									readonly="readonly" name="time" />
							</div>
						</div>
					</div>
				</div>
				<div class="result" id="errmsg" style="padding-left: 2%;"></div>
			<div class="col-md-9 b-b b-r b-l b-t b-grey loan_label scrollit " id="table3" style="padding-bottom: 292px;display: flex;">
					<div style="padding: -2px;margin-top: -5%;">
						<table class="acc_table scrollit" id="qry_table">
							<thead>
								<tr>
									<td style="background-color: cadetblue;">POST DATE</td>
									<td style="background-color: cadetblue;">Value Date</td>
									<td style="background-color: cadetblue;">Details</td>
									<td style="background-color: cadetblue;">Cheque</td>
									<td style="background-color: cadetblue;">Debit</td>
									<td style="background-color: cadetblue;">Credit</td>
									<td style="background-color: cadetblue;">Balance</td>
								</tr>
							</thead>
							<tbody id="qrytbl">

							</tbody>

							<tfoot></tfoot>
						</table>
					</div>
				</div>
			</div>
			<div></div>
		</div>


	</div>


	<div id="fade"></div>
	<div id="modal"
		style="height: auto; width: auto; padding: 0; border-radius: 0;">
		<img id="loader" style="width: 150px;" src="images/loading5.gif" />
	</div>

	<div id="dialog" style="display: none">

		<div style="margin-top: -3%;">
			<%@ include file="/WEB-INF/pages/Footer.jsp"%>
		</div>
		<!-------------------------------- Full Height Modal Right------------------------ -->


		<input type="hidden" id="jsonBom" value='${data}' />
		<script>
		$(function() {
			$('.datepicker').datepicker({
			    dateFormat: 'dd/mm/yy',
			    changeMonth: true,
			      changeYear: true,
			      yearRange: "c-20:c+20"
			});
		});

	
		
		 $("refresh").click(function(){
	            location.reload(true);
	        });
	</script>
		<script>
		var List = $('#jsonBom').val();
	</script>

		<script type="text/javascript" src="js/staticreports.js"></script>
		<script>
    // WRITE THE VALIDATION SCRIPT.
    function isNumber(evt) {
        var iKeyCode = (evt.which) ? evt.which : evt.keyCode
        if (iKeyCode != 46 && iKeyCode > 31 && (iKeyCode < 48 || iKeyCode > 57))
            return false;

        return true;
    }  
      
</script>
</body>
</html>