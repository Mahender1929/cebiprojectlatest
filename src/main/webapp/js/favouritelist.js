	
	function retriveColumns() {
		columnbox();
		if($('#table_select').val()){
			$.ajax({
				type : "POST",
				url : 'retriveColumns',
				dataType : "json",
				data : $('#table_select').val(),
				contentType : "application/json; charset=utf-8",
				error : function(xhr, status, error) {
				},
				success : function(response) {
					localStorage.setItem("dbColumns", JSON.stringify(response));
					$.each(response, function(i, value) {
			/*			if(isNumber(value)){
							$('#columns').append($("<li class = 'active'>").append($("<a data-toggle='collapse' aria-expanded='false'>").text(getAppLabel(response, value.name)).attr({
								href:'#'+value.name,
								value : value.name,
							})).append($("<ul class='collapse list-unstyled'>").attr('id',value.name)
									.append($("<li>")
											.append($("<a class='btn btn-success'>").text(getAppLabel(response, value.name)).attr({value:value.name,onclick :'moveButton(this)'})
											))
											.append($("<li>")
													.append($("<a class='btn btn-success'>").text('AVG ('+getAppLabel(response, value.name)+')').attr({value:'AVG('+value.name+') As '+value.name,onclick :'moveButton(this)'})

													))
													.append($("<li>")
															.append($("<a class='btn btn-success'>").text('MIN ('+getAppLabel(response, value.name)+')').attr({value:'MIN('+value.name+') As '+value.name,onclick :'moveButton(this)'})

															))		
															.append($("<li>")
																	.append($("<a class='btn btn-success'>").text('MAX ('+getAppLabel(response, value.name)+')').attr({value:'MAX('+value.name+') As '+value.name,onclick :'moveButton(this)'})

																	))	
							));
						}else{*/
							$('#columns').append($("<li class = 'active'>").append($("<a class='btn btn-success' data-toggle='collapse'>").text(getAppLabel(response, value.name)).attr({
								href:'#'+value.name,
								value : value.name,
								onclick :'moveButton(this)',
								id : "drag" + i,
							})));

						/*}*/

						// ADD ALL COLUMNS TO THE SELECT OPTIONS FOR CRITERIA
						if(isRequired(value)){
							$('#s1').append($('<option></option>').text(getAppLabel(response, value.name)).attr({
								value : value.name,
								style	:'color:red;',
							}));
						}else if(isString(value)){
							$('#s1').append($('<option></option>').text(getAppLabel(response, value.name)).attr({
								value : value.name,
								/*style	:'color:#919be6;'*/
							}));
						}else{
							$('#s1').append($('<option></option>').text(getAppLabel(response, value.name)).attr({
								value : value.name,
							}));
						}
					});
					validateSelectedValue();
					openNav();
					filterColumns(response);
				},
				beforeSend : function() {
					// openModal();
				},
			});
		}
	}

	
	function filterColumns(response) {
	var parameter=localStorage.getItem("parameter");
	var parameterObj = parameter.split(',');
	$.each(parameterObj, function(key, value) {
		$("#columns li a").each(function() {
			var text = $(this).attr("value");
			if (value == text) {
				$(this).detach().appendTo('#drag');
			}
		});

	});
	localStorage.setItem("parameter", "");
}
	
