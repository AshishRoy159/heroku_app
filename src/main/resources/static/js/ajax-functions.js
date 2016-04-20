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

function hideMessage() {
	setTimeout(function() {
		$("#info").hide(500);
	}, 3000);
}