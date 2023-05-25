$(document).ready(function() {
	/* $('.saveqry:contains("SELECT")').css('color', 'green'); */
	$("#txtarea").empty();
	$('#csvbtn').attr('disabled','disabled');
	$('#ptxtbtn').attr('disabled','disabled');
	$('#pxlsbtn').attr('disabled','disabled');
	$('#txtarea').prop('readonly', false);
	$('#tabledata').hide();
	
});

function submitDetailsForm() {
    $("#saveform").submit();
 }


$(".report-btn").on("click", function() {
	saveValu();
});

$(".transmitbtn").on("click", function() {
	transval();
});

$("#csvbtn").on("click", function() {
	downloadCSV();
});


$("#ptxtbtn").on("click", function() {
	downloadTXT();
});

$("#pxlsbtn").on("click", function() {
	downloadXLS();
});



$('#saveQry').click(function(e) {
	//getQueryList(e);
});

function saveFavouriteQuery(e) {
	var obj = JSON.parse($('#pdf').val());
	obj.qryTitle = $('#qryTitle').val();
	obj.purpose = $('#purpose').val();
	obj.description = $('#description').val();
	var json = JSON.stringify(obj);
	$.ajax({
		type : 'POST',
		url : 'favourite',
		data : json,
		dataType : "json",
		contentType : "application/json; charset=utf-8",
		error : function(xhr, status, error) {
			console.log(xhr.responseText);
			var err = xhr.responseText;
			if (err.toLowerCase().indexOf("session_timed_out") >= 0) {
				window.location = "login.html?statusCheck=SessionExpired";
			}
		},
		beforeSend : function() {

		},
		success : function(response) {
			alert(response.saved);
			$('#saveFavourite').hide();
			$('.modal-backdrop').hide();
		},
		complete : function() {
		}
	});
}

function transval() {
	var res = "";
	var qry = document.getElementById('txtarea').value;
	 qry=qry.toUpperCase();
	if (trimAll(qry) === '') {
		alert('Empty !!');
	} else {
		var s = validateQry(qry);
		if(s.length == 0){
			var n = qry.startsWith("SELECT")&& qry.includes("FROM")&& !qry.startsWith("CREATE")&& !qry.startsWith("DROP")&& !qry.startsWith("UPDATE");
	      if (n == true) {
		 getTableData();
     	} else {
		  alert('Not Valid SQL Statement');
         	}
		}else{
			var res = "";
			for (j = 0; j < s.length; j++) { 
				res= res.concat(s[j]).concat(","); 
				}
			alert(res =  res + " Not Allowed"); 
			
		}
		
	}
}

function validateQry(qry) {
	var filterstrings = new Array('ALTER', 'TRUNCATE',
			'COMMENT', 'RENAME', 'INSERT', 'UPDATE', 'DELETE', 'COMMIT',
			'ROLLBACK', 'SAVEPOINT', 'SET TRANSACTION', 'DROP', 'CALL',
			'AUDIT', 'SHOW', 'MODIFY', 'ORDER BY', 'ASC');
	var notAllowed = [];
	for (i = 0; i < filterstrings.length; i++) {
		if (qry.includes(filterstrings[i])) {
			notAllowed.push(filterstrings[i]);
		}
	}
	
 return notAllowed;
}

function saveValu() {
	if (trimAll(document.getElementById('txtarea').value) === '') {
		alert('Empty !!');
	} else {
		var str = document.getElementById('txtarea').value;
		var n = (str.startsWith("SELECT") || str.startsWith("select"))
				&& (str.includes("FROM") || str.includes("from"));
		if (n == true) {
			getQueryList();
		} else {
			$('#myModal').hide();
			alert('Not Valid SQL Statement');
		}
	}
}

function trimAll(qry) {
	while (qry.substring(0, 1) == ' ') {
		qry = qry.substring(1, sString.length);
	}
	while (qry.substring(qry.length - 1, qry.length) == ' ') {
		qry = qry.substring(0, qry.length - 1);
	}
	var n = qry.startsWith("SELECT");
	if (n == true) {
		return qry;
	}

}

function getTableData() {
	var url = "getdatatable";
	var saveQuery = $("#txtarea").val();
	var sts = $("#sts").val();
	var id = $("#Id").val();
	var bankCode = $("#bankCode").val();
	var title  = $("#qryTitle").val();
	var descp  = $("#description").val();
	var purpose = $("#purpose").val();
	 $("#csvquery").val(saveQuery);
	 $("#txtquery").val(saveQuery);
	 $("#xlsquery").val(saveQuery);
	var preDefineReport = {
		"saveQuery" : saveQuery,
		"sts" : sts,
		"id" : id,
		"title" : title,
		"descp" : descp,
		"id" : id,
		"purpose" : purpose,
	}
	if (true) {
		$.ajax({
			type : "POST",
			data : JSON.stringify(preDefineReport),
			dataType : "json",
			contentType : 'application/json; charset=utf-8',
			url : url,
			error : function(xhr, status, error) {
			},
			success : function(response) {
				$("#errmsg").empty();
				 $("#menu4").empty();
				 closeModal();
			      var err  =response[0].name;
			        $(".result").html(err);
				   CreateTableFromJSON(response);   
				$('.report_field_table').DataTable({
					'scrollX' : true,
					dom : 'Bfrtip',
					buttons : [
					/*
					 * { text : '<i class="fa fa-file-excel-o"></i> Excel',
					 * action : function( e, dt, node, config) { e
					 * .preventDefault(); $("#excelForm") .submit(); } }, { text : '<i
					 * class="fa fa-file-text-o"></i> CSV', action : function(
					 * e, dt, node, config) { e .preventDefault(); $("#csvForm")
					 * .submit(); } }, { text : '<i class="fa fa-file-text-o"></i>
					 * CSV PIPE', action : function( e, dt, node, config) { e
					 * .preventDefault(); $( "#pipecsvForm") .submit(); } }, {
					 * text : '<i class="fa fa-file-pdf-o"></i> PDF', action :
					 * function( e, dt, node, config) { e .preventDefault();
					 * $("#pdfForm") .submit(); } }, { extend : 'copyHtml5',
					 * text : '<i class="fa fa-file"></i> Copy', titleAttr :
					 * 'Copy' }
					 */]
				});

			},
			beforeSend : function() {
				$("#drag_message").html("");
				if ($("#drag").hasClass("table_select_error"))
					$("#drag").removeClass("table_select_error");
				if ($("#table_select").hasClass("table_select_error"))
					$("#table_select").removeClass("table_select_error");
				openModal();
				
			},
		});

	} else {
		if (!$('#table_select').val()) {
		} else if (!$('#drag').text()) {
			$("#drag_message").html("Please select the columns");
			$("#drag").addClass("table_select_error");
		}
	}
}
var col = [], colCode = [];

function CreateTableFromJSON(response) {
	// var columns = JSON.parse(localStorage.getItem("dbColumns"));
	col = [];
	$.each(response[0].names, function(i, names) {
		if (col.indexOf(names.name) === -1) {
			col.push(names.name);
			// colCode.push(getAppLabelCode(response[0].appLabels,names.name));
		}
	});

	// CREATE DYNAMIC TABLE.
	var table = document.createElement("table");
	var thead = table.createTHead();
	table.classList.add("report_field_table", "display", "nowrap");

	var tblBody = document.createElement("tbody");
	table.appendChild(tblBody);

	var tr = thead.insertRow(-1);
	for (var i = 0; i < col.length; i++) {
		var th = document.createElement("th"); // TABLE HEADER.
		th.innerHTML = col[i];
		tr.appendChild(th);
	}

	for (var i = 0; i < response[0].names.length; i++) {
		tr = tblBody.insertRow(-1);
		for (var j = 0; j < col.length; j++) {
			var tabCell = tr.insertCell(-1);
			if ((col.length - 1) != j)
				tabCell.innerHTML = response[0].names[i++].field;
			if ((col.length - 1) == j)
				tabCell.innerHTML = response[0].names[i].field;
		}
	}

	// FINALLY ADD THE NEWLY CREATED TABLE WITH JSON DATA TO A CONTAINER.
	var divContainer = document.getElementById("menu4");
	divContainer.innerHTML = "";
	divContainer.appendChild(table);
	$('#tabledata').show();
	$('#csvbtn').removeAttr('disabled');
	$('#ptxtbtn').removeAttr('disabled');
	$('#pxlsbtn').removeAttr('disabled');
	$('#txtarea').prop('readonly', true);
}

$('#qry_table').on('click', '.get', function() {
	var $row = $(this).closest("tr");
	var val = $row.find("td:nth-child(2)").text();
	$("#txtarea").text(val);
	$('#myModale').hide();
	
});

function clrtxt(){
	 $("#txtarea").empty();
	 $("#menu4").empty();
	    $('#csvbtn').attr('disabled','disabled');
		$('#ptxtbtn').attr('disabled','disabled');
		$('#pxlsbtn').attr('disabled','disabled');
		$('#txtarea').prop('readonly', false);
		$('#tabledata').hide();
		
}

function getQueryList() {
	var url = "predefinerepo";
	var saveQuery = $("#txtarea").val();
	var sts = $("#sts").val();
	var id = $("#Id").val();
	var bankCode = $("#bankCode").val();
	var title  = $("#qryTitle").val();
	var descp  = $("#description").val();
	var purpose = $("#purp").val();

	var preDefineReport = {
		"saveQuery" : saveQuery,
		"sts" : sts,
		"id" : id,
		"title" : title,
		"descp" : descp,
		"id" : id,
		"purpose" : purpose,
	}

	if (true) {
		debugger;
		$.ajax({
			type : "POST",
			data : JSON.stringify(preDefineReport),
			dataType : "json",
			contentType : 'application/json; charset=utf-8',
			url : url,
			error : function(xhr, status, error) {

			},
			success : function(response) {
				$('#myModal').hide();
				alert(response.msg);
				location.reload();
			},
			complete : function() {
				/* $.unblockUI(); */
			}
		});
	}

}

$('#qry_table').on('click', '.deletebtn', function() {
	var $row = $(this).closest("tr");
	var saveQuery = $row.find("td:nth-child(2)").text().trim();
	var id = $row.find("td:nth-child(1)").text().trim();
	var url = "deletesaveqry";
	var sts = "DELETE";
	var bankCode = "";

	var title  = $("#qryTitle").val();
	var descp  = $("#description").val();
	var purpose = $("#purpose").val();

	var preDefineReport = {
		"saveQuery" : saveQuery,
		"sts" : sts,
		"id" : id,
		"title" : title,
		"descp" : descp,
		"id" : id,
		"purpose" : purpose,
	}
	
	if (true) {
		debugger;
		$.ajax({
			type : "POST",
			data : JSON.stringify(preDefineReport),
			dataType : "json",
			contentType : 'application/json; charset=utf-8',
			url : url,
			error : function(xhr, status, error) {

			},
			success : function(response) {
				alert(response.msg);
				location.reload();
			},
			complete : function() {
				/* $.unblockUI(); */
			}
		});
	}
});


function downloadCSV() {
	var url = "prcsvdownload";
	var saveQuery = $("#txtarea").val();
	var sts = $("#sts").val();
	var id = $("#Id").val();
	var bankCode = $("#bankCode").val();
	var title  = $("#qryTitle").val();
	var descp  = $("#description").val();
	var purpose = $("#purpose").val();

	var preDefineReport = {
		"saveQuery" : saveQuery,
		"sts" : sts,
		"id" : id,
		"title" : title,
		"descp" : descp,
		"id" : id,
		"purpose" : purpose,
	}
	$.ajax({
		type : "POST",
		data : JSON.stringify(preDefineReport),
		dataType : "json",
		contentType : 'application/json; charset=utf-8',
		url : url,
		error : function(xhr, status, error) {

		},
		success : function(response) {
			alert(response.msg);
		},
		complete : function() {
			/* $.unblockUI(); */
		}
	});
}


function downloadTXT() {
	var url = "prtxtdownload";
	var saveQuery = $("#txtarea").val();
	var sts = $("#sts").val();
	var id = $("#Id").val();
	var bankCode = $("#bankCode").val();
	var title  = $("#qryTitle").val();
	var descp  = $("#description").val();
	var purpose = $("#purpose").val();

	var preDefineReport = {
		"saveQuery" : saveQuery,
		"sts" : sts,
		"id" : id,
		"title" : title,
		"descp" : descp,
		"id" : id,
		"purpose" : purpose,
	}
	$.ajax({
		type : "POST",
		data : JSON.stringify(preDefineReport),
		dataType : "json",
		contentType : 'application/json; charset=utf-8',
		url : url,
		error : function(xhr, status, error) {

		},
		success : function(response) {
			alert(response.msg);
		},
		complete : function() {
			/* $.unblockUI(); */
		}
	});
}

function downloadXLS() {
	var url = "prxlsdownload";
	var saveQuery = $("#txtarea").val();
	var sts = $("#sts").val();
	var id = $("#Id").val();
	var bankCode = $("#bankCode").val();
	var title  = $("#qryTitle").val();
	var descp  = $("#description").val();
	var purpose = $("#purpose").val();

	var preDefineReport = {
		"saveQuery" : saveQuery,
		"sts" : sts,
		"id" : id,
		"title" : title,
		"descp" : descp,
		"id" : id,
		"purpose" : purpose,
	}
	$.ajax({
		type : "POST",
		data : JSON.stringify(preDefineReport),
		dataType : "json",
		contentType : 'application/json; charset=utf-8',
		url : url,
		error : function(xhr, status, error) {

		},
		success : function(response) {
			alert(response.msg);
		},
		complete : function() {
			/* $.unblockUI(); */
		}
	});
}
	
