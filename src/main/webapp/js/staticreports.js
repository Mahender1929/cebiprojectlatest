$( document ).ready(function() {
	$("#print").attr("disabled", true);
    $("#table3").hide();
});

function getData() {
	   $('#modal').show();
	   var table = $('#qry_table').DataTable();
	   table.destroy();
	var url = "getReportDataByAcc";
	var frDate = $('#datepicker-13').val();
	var toDate = $('#datepicker').val();
	var accNum = $('#accNum').val();
	var instNo = "";
	var journalNo = "";
	var crdr = "";
	var txCode = "";
	var tlrBranch = "";
	var narration = "";
	var txAmount = "";
	var balance = "";
	var postTime = "";
	var txDate = "";
	var cheqNo = "";
	var intCat = "";
	var custName = "";
	var custNo = "";
	var phBusNo = "";
	var mobNO = "";
	var currBalace = "";
	var totalDr = "";
	var totalCr = "";
	var crCount = "";
	var drCount = "";
	var currTime = "";
	var currDate = "";
	var staticReports = {
		"accNum" : accNum,
		"toDate" : toDate,
		"frDate" : frDate,
		"instNo" : "",
		"journalNo" : "",
		"crdr" : "",
		"txCode" : "",
		"tlrBranch" : "",
		"narration" : "",
		"txAmount" : "",
		"balance" : "",
		"postTime" : "",
		"txDate" : "",
		"cheqNo" : "",
		"intCat" : "",
		"custName" : "",
		"custNo" : "",
		"phBusNo" : "",
		"mobNO" : "",
		"currBalace" : "",
		"totalDr" : "",
		"totalCr" : "",
		"crCount" : "",
		"drCount" : "",
		"currTime" : "",
		"currDate" : "",

	}

	$.ajax({
		type : "POST",
		data : JSON.stringify(staticReports),
		dataType : "json",
		contentType : "application/json; charset=utf-8",
		url : url,
		error : function(xhr, status, error) {
			$('#modal').hide();
			alert("Error");
			console.log(xhr.responseText);
			var err = xhr.responseText;
			if (err.toLowerCase().indexOf("session_timed_out") >= 0) {
				window.location = "login.html?statusCheck=SessionExpired";
			}
		},
		beforeSend : function() {
			$("#trnsmit").attr("disabled", true);
		},
		success : function(response) {
			$('#modal').hide();
			if (response.error === 1) {
				$("#trnsmit").attr("disabled", false);
				$('.result').empty();
				var err = "Dates Must be within 1 Year";
				$(".result").html(err);
			}else if (response.error === 2 ){
				$('.result').empty();
				$("#trnsmit").attr("disabled", false);
				$("#accNum").prop("readonly", false);
				$("#datepicker-13").prop("readonly", false);
				$("#datepicker").prop("readonly", false);
				$("#print").attr("disabled", true);
				var err  ="No Data Found with this Account Number";
		        $(".result").html(err);
			}else if (response.error === 3 ){
				$('.result').empty();
				$("#trnsmit").attr("disabled", false);
				$("#accNum").prop("readonly", false);
				$("#datepicker-13").prop("readonly", false);
				$("#datepicker").prop("readonly", false);
				$("#print").attr("disabled", true);
				var err  ="Please Enter A/C no,From Date and To Date";
		        $(".result").html(err);
			}else {
				 $("#form2").show();
				    $("#table3").show();
				    $("#trnsmit").attr("disabled", false);
				    $("#print").attr("disabled", false);
				//$("#accNum").prop("readonly", true);
				//$("#datepicker-13").prop("readonly", true);
			//	$("#datepicker").prop("readonly", true);
				//$("#print").attr("disabled", false);
				$('#custno').empty();
				$('#custName').empty();
				$('#address').empty();
				$('#mobNo').empty();
				$('#date').empty();
				$('#branch').empty();
				$('#bal').empty();
				$('#time').empty();
				$('#tdr').empty();
				$('#tcr').empty();
				$('#qrytbl').empty();
				$('.result').empty();
				var list = response.list;
				document.getElementById('custno').setAttribute('value',
						list[0].custNo);
				document.getElementById('custName').setAttribute('value',
						list[0].custName);
				document.getElementById('address').setAttribute('value',
						list[0].address);
				document.getElementById('mobNo').setAttribute('value',
						list[0].mobNO);
				document.getElementById('date').setAttribute('value',
						list[0].currDate);
				document.getElementById('branch').setAttribute('value',
						list[0].tlrBranch);
				document.getElementById('time').setAttribute('value',
						list[0].currTime);
				document.getElementById('bal').setAttribute('value',
						list[0].currBalace);
				document.getElementById('tdr').setAttribute('value',
						list[0].totalDr);
				document.getElementById('tcr').setAttribute('value',
						list[0].totalCr);

				var sr = 1
				$.each(response.list, function(index, value) {
					var tr = '<tr>'
					tr += "<td >" + value.postTime + "</td>";
					tr += "<td>" + value.txDate + "</td>";
					tr += "<td>" + value.narration + "</td>";
					tr += "<td>" + value.cheqNo + "</td>";
					tr += "<td>" + value.instNo + "</td>";
					tr += "<td>" + value.txAmount + "</td>";
					tr += "<td>" + value.balance + "</td>";
					$('#qrytbl').append(tr);
					sr++;

				});
				var table = $('#qry_table').dataTable({
					"bPaginate" : true,
					"bFilter" : false,
					"bInfo" : false,
					  "columns": [
						             { "width": "13%" },
						             { "width": "13%" },
						             { "width": "35%" },
						             { "width": "10%" },
						             { "width": "10%" },
						             { "width": "10%" },
						             { "width": "10%" }
						           ]
				});

			}
		}
	});
}