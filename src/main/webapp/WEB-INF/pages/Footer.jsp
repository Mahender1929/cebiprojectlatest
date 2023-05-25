<div style="margin-top:4%" class="container-fluid container-fixed-lg footer no-padding">
	<div class="row bg-warning-light no-margin no-padding responseBar"
		style="display: block;">
		<div class="text-left col-md-7 p-t-10 p-b-10">
			<b class="left">POWEREdge Release 2.0</b>
		</div>
		<div class="col-md-5 p-t-10 p-b-10 text-right">
			<b class="small no-margin sm-pull-reset mob_center">
				<span class=""> © 2018 </span> <span><span>c</span><!-- <span
					style="font-size: 17px;">&#x3b1;</span><span>NC</span> --><span
					style="text-transform: uppercase; font-size: 10px;"> - Edge</span>.</span>
				<span class="">All rights reserved. &nbsp;</span> <span
					class="sm-block"><a style="color:#fff;" href="#" class="m-l-10 m-r-10">Terms
						of use</a> | <a style="color:#fff;" href="#" class="m-l-10">Privacy Policy</a></span>
			</b>
			<div class="clearfix"></div>
		</div>
	</div>
	<script>
	 function checkbankcode()
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

								if(bnkcode!=response.list)
									{
										alert("Logged in with incorrect bank "+response.list)
									}
								},
								complete : function() {
								}
							});
					 }  
	 }
	document.onkeydown = function(e) { 
		  if(e.ctrlKey && e.shiftKey && e.keyCode == 'I'.charCodeAt(0)) {
		     return false;
		  }
		}
	</script>
</div>

