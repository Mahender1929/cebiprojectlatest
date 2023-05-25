<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="header.jsp" %>  
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Admin</title>
<link href="css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="css/style.css"> 
<style type="text/css">
.col-3-width{width:16%;padding:0 5px;}
.header-color{line-height: 10px;}
.container {
	padding: 0;
}
.static-btn {
	margin: 22% 0 0;
}

.panel-red {
    border-color: #d82d0d;
    background-color: #ea0707;
}
.zoom {

    transition: transform .7s;  
}
/* .row{margin: 0;} */
.zoom:hover {
    -ms-transform: scale(1.1); /* IE 9 */
    -webkit-transform: scale(1.1); /* Safari 3-8 */
    transform: scale(1.1); 
}
.hidebar{
display:none;
}
.homebtn{
display:none;
}
.btn-success:hover{background-color: #339bb7 !important;border-color: #339bb7 !important;}
.panel .panel-heading{padding: 8px 10px; min-height: auto;}
</style>
</head>
<body onload="checkbankcode()">
	
		<div id="page-wrapper" style="min-height: 575px;">
            <div class="row" style="margin: 0;padding: 0;">
                <div class="col-lg-12">
                   <h3 class="page-header" style="margin-top:10px">Dashboard  <p style="float:right">${bnkname}</p> </h3> 
                </div>
            </div> 
           
            <div  style="width:85%;margin: 0 auto;">
           
                <div class="col-md-3 col-3-width" id="createreport">
                <div class="zoom">
                    <div class="panel panel-primary">
                        <div class="panel-heading">
                            <div class="row">
                                <div class="col-xs-3">
                                    <i class="fa fa-clipboard fa-lg" style="line-height:1.2;"></i>
                                </div>
                                <div class="col-xs-9 text-left">
                                    <div style="color:white;font-size:14px;font-weight:600;">Create Report</div>
                            </div>
                            </div>
                        </div>
                        <a href="${pageContext.request.contextPath}/report"> 
                            <div class="panel-footer">
                                <span class="pull-left" style="font-weight:500;">View Details</span>
                                <span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
                              
                                <div class="clearfix"></div>
                            </div>
                       </a>
                        
                </div>
                </div>
              </div>
           <c:choose>
			<c:when test="${sessionScope.isCCdp=='true'}">
              
                 <div class="col-md-3 col-3-width" id="cpa">
                 <div class="zoom">
                    <div class="panel panel-green">
                        <div class="panel-heading">
                            <div class="row">
                                <div class="col-xs-3">
                                    <i class="fa fa-file fa-lg" style="line-height:1.2;"></i>
                                </div>
                                <div class="col-xs-9 text-left">
                                    <div style="color:white;font-size:14px;font-weight:600;">CPA</div>
                              </div>
                            </div>
                        </div>
                        <a href="${pageContext.request.contextPath}/staticreport">
                            <div class="panel-footer">
                                <span class="pull-left"  style="font-weight:500;">View Details</span>
                                <span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
                                <div class="clearfix"></div>
                            </div>
                        </a>
                    </div>
                </div>
                </div>
            </c:when>
				</c:choose>
				<c:choose>
			<c:when test="${sessionScope.isCCdp=='false'}">
              <div class="col-md-3 col-3-width">
                  <div class="zoom">
                    <div class="panel panel-green">
                        <div class="panel-heading">
                            <div class="row">
                                <div class="col-xs-3">
                                    <i class="fa fa-file fa-lg" style="line-height:1.2;" ></i>
                                </div>
                                <div class="col-xs-9 text-left">
                                    <div style="color:white;font-size:14px;font-weight:600;">CPA</div>
                              </div>
                            </div>
                        </div>
                        <a href="#">
                            <div class="panel-footer">
                                <span class="pull-left"  style="font-weight:500;">View Details</span>
                                <span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
                                <div class="clearfix"></div>
                            </div>
                        </a>
                    </div>
                </div>
                </div>
            </c:when>
				</c:choose>
				
                
                <div class="col-md-3 col-3-width" id="showcart">
                	<div class="zoom">
                    <div class="panel panel-yellow">
                        <div class="panel-heading">
                            <div class="row">
                                <div class="col-xs-3">
                                    <i class="fa fa-bar-chart fa-lg" style="line-height:1.2;"></i>
                                </div>
                                <div class="col-xs-9 text-left">
                                    <div style="color:white;font-size:14px;font-weight:600;">Show Chart</div>
                              </div>
                            </div>
                        </div>
                        <a href="${pageContext.request.contextPath}/showchartDetails">
                            <div class="panel-footer">
                                <span class="pull-left"  style="font-weight:500;">View Details</span>
                                <span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
                                <div class="clearfix"></div>
                            </div>
                        </a>
                    </div>
                
                </div>
                </div>
                <!-- Deposit statement start here -->
				<div class="col-md-3 col-3-width" id="dipoststmtdivid">
				<div class="zoom">
					<div class="panel panel-primary">
						<div class="panel-heading">
							<div class="row">
								<div class="col-xs-3">
									<i class="fa fa-bar-chart fa-lg" style="line-height:1.2;"></i>
								</div>
								<div class="text-left">
									<div style="color: white; font-size:14px;font-weight:600;">Deposit Statement</div>
								</div>
							</div>
						</div>
						<a href="${pageContext.request.contextPath}/getDepositstmt">
							<div class="panel-footer">
								<span class="pull-left" style="font-weight: 500;">View
									Details</span> <span class="pull-right"><i
									class="fa fa-arrow-circle-right"></i></span>
								<div class="clearfix"></div>
							</div>
						</a>
					</div>
			</div>
			

				</div>

				<!-- Deposit Statement ends here -->                
                <div class="col-md-3 col-3-width" id="oldstmt">
				<div class="zoom">
					<div class="panel panel-green">
						<div class="panel-heading">
							<div class="row">
								<div class="col-xs-3">
									<i class="fa fa-bar-chart fa-lg" style="line-height:1.2;"></i>
								</div>
								<div class="col-xs-9 text-left">
									<div style="color: white; font-size:14px;font-weight:600;">Old Statement</div>
								</div>
							</div>
						</div>
						<a href="${pageContext.request.contextPath}/getReportByAcc">
							<div class="panel-footer">
								<span class="pull-left" style="font-weight: 500;">View
									Details</span> <span class="pull-right"><i
									class="fa fa-arrow-circle-right"></i></span>
								<div class="clearfix"></div>
							</div>
						</a>
					</div>
			</div>
			

				</div>
				<div class="col-md-3 col-3-width" id="quaryrunner">
				<div class="zoom">
					<div class="panel panel-yellow">
						<div class="panel-heading" style="background: crimson;">
							<div class="row">
								<div class="col-xs-2">
									<i class="fa fa-bar-chart fa-lg" style="line-height:1.2;"></i>
								</div>
								<div class="col-xs-10 text-left">
									<div style="color: white; font-size:14px;font-weight:600;">Quary Runner</div>
								</div>
							</div>
						</div>
						<a href="${pageContext.request.contextPath}/predefinereports">
							<div class="panel-footer">
								<span class="pull-left" style="font-weight: 500;">View
									Details</span> <span class="pull-right"><i
									class="fa fa-arrow-circle-right"></i></span>
								<div class="clearfix"></div>
							</div>
						</a>
					</div>
</div>
				</div>
				<input type="hidden" value="${user }" id="userid"/>
				<input type="hidden" id="tellertype" value=${sessionScope.tellertype}>
                </div>
              <!-------------------------------------------------------------------------------------------------------- -->
                  <!--  <div class="zoom"> -->
                   <div style="width:85%;margin: 0 auto;">
                <div class="col-md-12">
                    <div class="">
                        <div class="">
                           
                        </div>
                        <!-- /.panel-heading -->
                 <div style="width:100%; overflow:hidden;">
                         	<div id="invmchart" class="col-md-6" style="height:400px;"></div>
                         	<div id="loanchart" class="col-md-6" style="height:400px;"></div>
                         	</div>
                         	</div>
                         	
                         	</div>
                </div>
                </div>
                      <%@ include file="/WEB-INF/pages/Footer.jsp"%>    
                
     <script type="text/javascript" charset="utf-8">
     
		$(window).on("load", function() {
			$.ajax({
				type : "POST",
				url : 'showchartResult',
				data : '',
				processData : false,
				contentType : "application/json; charset=utf-8",
				error : function(xhr, status, error) {
				},
				success : function(response) {
					var list = response;

					var chart = new CanvasJS.Chart("invmchart", {
						exportEnabled: false,
						animationEnabled: true,
						theme: "light1", // "light1", "light2", "dark1", "dark2"
						title:{
							text: "Deposit - Product and Accounts",
						        fontSize: 20,
						        padding: 10  
						},
						axisY: {
							title: "Number of Transactions"
						},
						axisX: {
							title: "List of Banks"
						},
						data: [{        
					        
							type: "pie",
							startAngle: 20,
							toolTipContent: "<b>Product {label} </b>No of count: {y}",
							legendText: "{label}",
							indexLabelFontSize: 12,
							indexLabel: "{label} - {y}",
							dataPoints:list
						}]
					});
					
					chart.render();
					if($('.canvasjs-chart-credit'))
					$('.canvasjs-chart-credit').addClass('hide');
				},
				beforeSend : function() {

				},
			});
			$.ajax({
				type : "POST",
				url : 'loanchartData',
				data : '',
				processData : false,
				contentType : "application/json; charset=utf-8",
				error : function(xhr, status, error) {
				},
				success : function(response) {
					var list = response;

					var chart = new CanvasJS.Chart("loanchart", {
						exportEnabled: false,
						animationEnabled: true,
						theme: "light1", // "light1", "light2", "dark1", "dark2"
						title:{
							text: "Loan - Product and Accounts",
							fontSize: 20,
					        padding: 10
						},
						axisY: {
							title: "Number of Transactions"
						},
						axisX: {
							title: "List of Banks"
						},
						data: [{        
					        
							type: "pie",
							startAngle: 20,
							toolTipContent: "<b>Product {label} </b>No of count: {y}",
							legendText: "{label}",
							indexLabelFontSize: 12,
							indexLabel: "{label} - {y}",
							dataPoints:list
						}]
					});
					
					chart.render();
					if($('.canvasjs-chart-credit'))
					$('.canvasjs-chart-credit').addClass('hide');
					/* $("#chartContainer").CanvasJSChart(chart); */
				},
				beforeSend : function() {

				},
			});
			
			/* var userid=$("#userid").val();
			var tellertype=$("#tellertype").val()
			if(userid=='501')
				{
					$('#dipoststmtdivid').show();
					$('#quaryrunner').show();
				}
			else
				{
					$('#dipoststmtdivid').hide();
					$('#quaryrunner').hide();
				}
			if(tellertype=='20')
				{
					$('#createreport').hide();
					$('#cpa').hide();
					$('#showcart').hide();
					$('#quaryrunner').hide();
					$('#dipoststmtdivid').hide();
				}
			
				{
					$('#createreport').show();
					$('#cpa').show();
					$('#showcart').show();
					$('#dipoststmtdivid').show();		
					$('#quaryrunner').show();
				} */
				
			$('#dipoststmtdivid').hide();		
			$('#quaryrunner').hide();
				
		});
		 /* $( document ).ready(function() { */
			/*  function checkbankcode()
		 {
				 var ul=window.location.href;
					if(ul.endsWith("/landing")||ul.endsWith("/report"))
						{
							 var  bnkcode=$("#bnkcode").val();
							 $.ajax({
									type : 'POST',
									url : 'chkbankcode.json',
									dataType : "json",
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

									if(bnkcode==response.list)
										{
											alert("Logged in with "+response.list)
										}
									else
										{
											alert("Logged in with incorrect bank "+response.list)
										}
										
									},
									complete : function() {
									}
								});
						 }  
		 } */
			 
			   
			/* });  */
	</script>
</body>
</html>