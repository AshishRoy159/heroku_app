/*------------------------------------------------------------
 * This function is used to send new bicycle details to server
 *------------------------------------------------------------*/
function addBicycle() {
	var chasisNo = $('#chasisNo').val();
	var pickUpPoint = $('#pickUpPoint').val();
	$('#add').text("Adding..");
	$("#add").prop("disabled", true);

	$.ajax({
		type : "POST",
		url : "/admin/addNewBicycle",
		data : 'chasisNo=' + chasisNo + '&pickUpPoint=' + pickUpPoint,
		success : function(response) {
			$('#info').show(200);
			$('#info').html(response);
			$('#chasisNo').val('');
			$('#pickUpPoint').val('');
			$('#add').text("Add bicycle");
			$('#add').prop("disabled", false);
			hideMessage();
		},
		error : function(xhr, status, e) {
			alert(xhr.responseText);
		}
	});
	return false;
}

/*-----------------------------------------------------------------
 * This function is used to send new pickup point details to server
 *-----------------------------------------------------------------*/
function addPickupPoint() {
	var location = $('#location').val();
	var maxCapacity = $('#maxCapacity').val();
	$('#add').text("Adding..");
	$("#add").prop("disabled", true);

	$.ajax({
		type : "POST",
		url : "/admin/addPickupPoint",
		data : 'location=' + location + '&maxCapacity=' + maxCapacity,
		success : function(response) {
			$('#info').show(200);
			$('#info').html(response);
			$('#location').val('');
			$('#maxCapacity').val('');
			$('#add').text("Add Pickup point");
			$('#add').prop("disabled", false);
			hideMessage();
		},
		error : function(xhr, status, e) {
			alert(xhr.responseText);
		}
	});
	return false;
}

/*---------------------------------------------------------------
 * This function is used to send wallet balance details to server
 *---------------------------------------------------------------*/
function addBalance() {
	var userId = $('#userId').val();
	var balance = $('#balance').val();
	$('#add').text("Adding..");
	$("#add").prop("disabled", true);

	$.ajax({
		type : "POST",
		url : "/manager/wallet",
		data : 'userId=' + userId + '&balance=' + balance,
		success : function(response) {
			$('#info').show(200);
			$('#info').html(response);
			$('#userId').val('');
			$('#balance').val('');
			$('#add').text("Add Balance");
			$('#add').prop("disabled", false);
			hideMessage();
		},
		error : function(xhr, status, e) {
			alert(xhr.responseText);
		}
	});
	return false;
}

/*---------------------------------------------------------------------
 * This function is used to send base rate update details to the server
 *---------------------------------------------------------------------*/
function updateBaseRate() {
	var groupType = $('#groupType').val();
	var baseRate = $('#baseRate').val();
	$('#update').text("Adding..");
	$("#update").prop("disabled", true);

	$.ajax({
		type : "POST",
		url : "/admin/updateBaseRate",
		data : 'groupType=' + groupType + '&baseRate=' + baseRate,
		success : function(response) {
			$('#info').show(200);
			$('#info').html(response);
			$('#baseRate').val('');
			$('#update').text("Update Rate Group");
			$('#update').prop("disabled", false);
			hideMessage();
		},
		error : function(xhr, status, e) {
			alert(xhr.responseText);
		}
	});
	return false;
}

/*-------------------------------------------------------------------
 * This function is used to send new rate group details to the server
 *-------------------------------------------------------------------*/
function addNewRateGroup() {
	var groupType = $('#groupType').val();
	var discount = $('#discount').val();
	var effectiveFrom = $('#datepicker').val();
	$('#add').text("Adding..");
	$("#add").prop("disabled", true);

	$.ajax({
		type : "POST",
		url : "/admin/addRateGroup",
		data : 'discount=' + discount + '&groupType=' + groupType
				+ '&effectiveFrom=' + effectiveFrom,
		success : function(response) {
			$('#info').show(200);
			$('#info').html(response);
			$('#groupType').val('');
			$('#discount').val('');
			$('#datepicker').val('');
			$('#add').text("Add New Rate Group");
			$('#add').prop("disabled", false);
			hideMessage();
		},
		error : function(xhr, status, e) {
			alert(xhr.status);
		}
	});
	return false;
};

/*----------------------------------------------------------------------
 * This function is used to send rate group update details to the server
 *----------------------------------------------------------------------*/
function updateRateGroup() {
	var rateGroupId = $('#rateGroupId').val();
	var groupType = $('#groupType').val();
	var discount = $('#discount').val();
	var effectiveFrom = $('#datepicker').val();
	$('#update').text("Adding..");
	$("#update").prop("disabled", true);

	$.ajax({
		type : "POST",
		url : "/admin/updatedRateGroup",
		data : 'discount=' + discount + '&groupType=' + groupType
				+ '&effectiveFrom=' + effectiveFrom,
		success : function(response) {
			$('#info').show(200);
			$('#info').html(response);
			$('#discount').val('');
			$('#effectiveFrom').val('');
			$('#update').text("Update Rate Group");
			$('#update').prop("disabled", false);
			hideMessage();
		},
		error : function(xhr, status, e) {
			alert(xhr.responseText);
		}
	});
	return false;
}

/*------------------------------------------------------------------
 * This function is used to send close booking details to the server
 *------------------------------------------------------------------*/
function closeBooking() {
	var bookingId = $('#bookingId').val();
	$('#close').text("Closing..");
	$("#close").prop("disabled", true);

	$.ajax({
		type : "POST",
		url : "/manager/closeBooking",
		data : 'bookingId=' + bookingId,
		success : function(response) {
			$('#info').show(200);
			$('#info').html(response);
			$('#bookingId').val('');
			$('#close').text("Close");
			$('#close').prop("disabled", false);
			hideMessage();
		},
		error : function(xhr, status, e) {
			alert(xhr.responseText);
		}
	});
	return false;
}

function hideMessage() {
	setTimeout(function() {
		$("#info").hide(500);
	}, 2500);
}

/*------------------------------------------------------------
 * This function is used to approve/disapprove user using ajax
 *------------------------------------------------------------*/
function approveUser(param) {
	var result = bootbox.confirm("Are you sure?", function(result) {
		if (result) {
			var userDetails = $(param).attr('href');
			var currentText = $(param).text();
			$(param).text("Updating..");
			$(param).prop("disabled", true);
			$.ajax({
				type : "GET",
				url : userDetails,
				success : function(response) {
					if (response == "approved") {
						response = "User Approved Successfully";
						$(param).text("Approved");
						$(param).attr("class", "btn btn-success btn-xs");
					} else if (response == "disapproved") {
						response = "User Disapproved Successfully";
						$(param).text("Disapproved");
						$(param).attr("class", "btn btn-danger btn-xs");
					} else {
						$(param).text(currentText);
					}
					$('#info').show(200);
					$(param).prop("disabled", false);
					$('#info').html(response);

					hideMessage();
				},
				error : function(xhr, status, e) {
					$(param).text(currentText);
					$(param).prop("disabled", false);
					alert(xhr.responseText);
				}
			});
		}
	});
	return false;
}

/*-------------------------------------------------------------
 * This function is used to activate/deactivate user using ajax
 *-------------------------------------------------------------*/
function enableUser(param) {
	var result = bootbox.confirm("Are you sure?", function(result) {
		if (result) {
			var userDetails = $(param).attr('href');
			var currentText = $(param).text();
			$(param).text("Updating..");
			$(param).prop("disabled", true);
			$.ajax({
				type : "GET",
				url : userDetails,
				success : function(response) {
					if (response == "enabled") {
						response = "User Enabled Successfully";
						$(param).text("Active");
						$(param).attr("class", "btn btn-success btn-xs");
					} else if (response == "disabled") {
						response = "User Disabled Successfully"
						$(param).text("Inactive");
						$(param).attr("class", "btn btn-danger btn-xs");
					} else {
						$(param).text(currentText);
					}
					$(param).prop("disabled", false);
					$('#info').show(200);
					$('#info').html(response);

					hideMessage();
				},
				error : function(xhr, status, e) {
					$(param).text(currentText);
					$(param).prop("disabled", false);
					alert(xhr.responseText);
				}
			});
		}
	});
	return false;
}

/*------------------------------------------------------------------
 * This function is used to send transfer request using ajax
 *------------------------------------------------------------------*/
function transferRequest() {
	var quantity = $('#quantity').val();
	$('#send').text("Sending..");
	$("#send").prop("disabled", true);

	$.ajax({
		type : "POST",
		url : "/manager/sendRequest",
		data : 'quantity=' + quantity,
		success : function(response) {
			$('#info').show(200);
			$('#info').html(response);
			$('#quantity').val('');
			$('#send').text("Send Request");
			$('#send').prop("disabled", false);
			hideMessage();
		},
		error : function(xhr, status, e) {
			alert(xhr.responseText);
		}
	});
	return false;
}


function hideMessage() {
	setTimeout(function() {
		$("#info").hide(500);
	}, 2500);
}
