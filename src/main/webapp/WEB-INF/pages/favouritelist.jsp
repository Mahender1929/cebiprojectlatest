<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>favourite List</title>
<%@ include file="header.jsp" %>
<!-- <link href="css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<link href="http://maxcdn.bootstrapcdn.com/font-awesome/4.1.0/css/font-awesome.min.css" rel="stylesheet">-->
<link href="css/style.css" rel="stylesheet" type="text/css" /> 
<style type="text/css">
 .hidebar{
display:none;
}
.container-fluid{
margin-top:8% !important;
}
</style>
</head>
<body>
	<div class="report_bg_patch">

		<div class="">
			<h2 style="color: black; font-size: 28px; text-align: left;">
					<i class="glyphicon glyphicon-star-empty"></i>&nbsp;&nbsp;User Favourite List
				</h2>
				<div class="scrollable1">
			<div class="report_bg_patch1" style="height:380px">
			<div class="tablebg">
				<table class="table" style="color: #000;">
					<thead>
						<tr>
							<th style="color:white;font-size:11px">Title</th>
							<th style="color:white;font-size:11px">Purpose</th>
							<th style="color:white;font-size:11px">Description</th>
							<th style="color:white;font-size:11px">Table</th>
							<th style="color:white;font-size:11px">Selected Columns</th>
							<th style="color:white;font-size:11px">Filter</th>
							<th style="color:white;font-size:11px">Date</th>
							<th style="color:white;font-size:11px">Action</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${favouriteLists}" var="table">
							<tr>
								<td>${table.qryTitle}</td>
								<td>${table.purpose}</td>
								<td>${table.description}</td>
								<td>${table.table}</td>
								<td class="parameter">${table.parameter}</td>
								<td>${table.query}</td>
								<td>${table.currentDate}</td>
								<td style="cursor: pointer">
									<form id="reportdetailsForm" style="padding: 0px" method="post" action="${pageContext.request.contextPath}/getReportDetails">
										<input type="hidden" name="id" value="${table.id}"/>
										<button class="getreportdetails" id="getreportdetails" style="width: 82px;padding: 0;font-size: 15px;" class="btn btn-success report-btn" type="submit">
											<span class="fa fa-edit"></span>&nbsp;Edit
										</button>
									</form>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				</div>
				</div>
			</div>
			<h2 style="color: white; font-size: 28px; text-align: center;">
				<b><c:out value='${nocontent}' /></b>
			</h2>
		</div>
	</div>
	<%@ include file="/WEB-INF/pages/Footer.jsp"%>
	<script type="text/javascript" src="js/jquery-3.4.1.min.js"></script>
	<script type="text/javascript" src="js/favouritelist.js"></script>
	<script type="text/javascript">
	
		$(document).ready(function() {
			
			$('.getreportdetails').click(function(e) {
				var parameter=$(this).closest('.table tr').children('td.parameter').text();
				localStorage.setItem("parameter", parameter);
				$('#reportdetailsForm').submit();
			});
		});
	</script>
</body>
</html>