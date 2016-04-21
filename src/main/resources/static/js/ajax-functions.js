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
			$('#info').show();
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
			$('#info').show();
			$('#info').html(response);
			$('#location').val('');
			$('#maxCapacity').val('');
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
 * This function is used to send rate group details to the server
 *-----------------------------------------------------------------*/
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
			$('#info').show();
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
			$('#info').show();
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
			$('#info').show();
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

function hideMessage() {
	setTimeout(function() {
		$("#info").hide(500);
	}, 3000);
}
