/*-------------------------------------------------
 * This function is used to create users data table
 --------------------------------------------------*/
$('#usertable').ready(function() {
	var table = $('table#users').DataTable({
		'ajax' : '/data/userList',
		'serverSide' : true,
		columns : [ {
			data : 'userId'
		}, {
			data : 'firstName',
			render: function ( data, type, row, meta ) {
				if($('#currentUserRole').val() == 'ADMIN'){
					return '<a href="/user/userProfile/' + row.userId + '">'+row.firstName+" "+row.lastName+'</a>';
				}
				return row.firstName+" "+row.lastName;
		    }
		}, {
			data : 'userAddress'
		}, {
			data : 'mobileNo'
		}, {
			data : 'registrationTime',
			render: function ( data, type, row ) {
		        if ( type === 'display' || type === 'filter' ) {
		            var d = new Date( data );
		            return d.getDate() +'-'+ (d.getMonth()+1) +'-'+ d.getFullYear() + " "+ d.getHours() + ":"+d.getMinutes();
		        }
		        return data;
		    }
		}, {
			data : 'role.userRole',
			render : function(data, type, row) {
				if (row.role) {
					if($('#currentUserRole').val() == 'ADMIN'){
						return '<a href="/admin/manageRole/' + row.userId + '">'+row.role.userRole+'</a>';
					} else {
						return row.role.userRole;
					}
				}
				return '';
			}
		}, {
			data : 'isApproved',
			render: function(data, type, row) {
				if(row.isApproved){
					return '<a class="btn btn-success btn-xs" href="/user/userApproval/'+row.userId+'" onclick="return approveUser(this)" data-toggle="tooltip"	title="Change approval status">Approved</a>';
				} else {
					return '<a class="btn btn-danger btn-xs" href="/user/userApproval/'+row.userId+'" onclick="return approveUser(this)" data-toggle="tooltip"	title="Change approval status">Disapproved</a>';
				}
			}
		}, {
			data : 'enabled',
			render: function(data, type, row) {
				if(row.enabled){
					return '<a class="btn btn-success btn-xs" href="/user/userEnable/'+row.userId+'" onclick="return enableUser(this)" data-toggle="tooltip" title="Change active status">Active</a>';
				} else {
					return '<a class="btn btn-danger btn-xs" href="/user/userEnable/'+row.userId+'" onclick="return enableUser(this)" data-toggle="tooltip"	title="Change active status">Inactive</a>';
				}
			}
		} ]
	});
});

/*----------------------------------------------------
 * This function is used to create bookings data table
 -----------------------------------------------------*/
$('#bookingtable').ready(function() {
	var table = $('table#bookings').DataTable({
		'ajax' : '/data/usedBookings',
		'serverSide' : true,
		columns : [ {
			data : 'bookingId'
		}, {
			data : 'user.userId',
			render : function(data, type, row) {
				if (row.user) {
					return row.user.userId;
				}
				return '';
			}
		}, {
			data : 'user.firstName',
			render : function(data, type, row) {
				if (row.user) {
					return row.user.firstName;
				}
				return '';
			}
		}, {
			data : 'bookingTime',
			render: function ( data, type, row ) {
		        if ( type === 'display' || type === 'filter' ) {
		            var d = new Date( data );
		            return d.getDate() +'-'+ (d.getMonth()+1) +'-'+ d.getFullYear() + " "+ d.getHours() + ":"+d.getMinutes();
		        }
		        return data;
		    }
		}, {
			data : 'pickedUpFrom.location',
			render : function(data, type, row) {
				if (row.pickedUpFrom) {
					return row.pickedUpFrom.location;
				}
				return '';
			}
		}, {
			data : 'returnedAt.location',
			render : function(data, type, row) {
				if (row.returnedAt) {
					return row.returnedAt.location;
				}
				return '';
			}
		}, {
			data : 'actualOut',
			render: function ( data, type, row ) {
		        if ( type === 'display' || type === 'filter' ) {
		            var d = new Date( data );
		            return d.getDate() +'-'+ (d.getMonth()+1) +'-'+ d.getFullYear() + " "+ d.getHours() + ":"+d.getMinutes();
		        }
		        return data;
		    }
		}, {
			data : 'actualIn',
			render: function ( data, type, row ) {
		        if ( type === 'display' || type === 'filter' ) {
		            var d = new Date( data );
		            return d.getDate() +'-'+ (d.getMonth()+1) +'-'+ d.getFullYear() + " "+ d.getHours() + ":"+d.getMinutes();
		        }
		        return data;
		    }
		}, {
			data : 'biCycleId.chasisNo',
			render : function(data, type, row) {
				if (row.biCycleId) {
					return row.biCycleId.chasisNo;
				}
				return '';
			}
		} ]
	});
});

/*---------------------------------------------------
 * This function is used to fetch bookings of an user
 ----------------------------------------------------*/
$('#userClosedBookingHistory').ready(function() {
	var id = $("#userId").val();
	var url = '/data/userBookings/'+id;
	var table = $('table#userBookingHistory').DataTable({
		'ajax' : url,
		'serverSide' : true,
		columns : [ {
			data : 'bookingId'
		}, {
			data : 'bookingTime',
			render: function ( data, type, row ) {
		        if ( type === 'display' || type === 'filter' ) {
		            var d = new Date( data );
		            return d.getDate() +'-'+ (d.getMonth()+1) +'-'+ d.getFullYear() + " "+ d.getHours() + ":"+d.getMinutes();
		        }
		        return data;
		    }
		}, {
			data : 'actualOut',
			render: function ( data, type, row ) {
		        if ( type === 'display' || type === 'filter' ) {
		            var d = new Date( data );
		            return d.getDate() +'-'+ (d.getMonth()+1) +'-'+ d.getFullYear() + " "+ d.getHours() + ":"+d.getMinutes();
		        }
		        return data;
		    }
		}, {
			data : 'actualIn',
			render: function ( data, type, row ) {
		        if ( type === 'display' || type === 'filter' ) {
		            var d = new Date( data );
		            return d.getDate() +'-'+ (d.getMonth()+1) +'-'+ d.getFullYear() + " "+ d.getHours() + ":"+d.getMinutes();
		        }
		        return data;
		    }
		},	{
			data : 'pickedUpFrom.location',
			render : function(data, type, row) {
				if (row.pickedUpFrom) {
					return row.pickedUpFrom.location;
				}
				return '';
			}
		}, {
			data : 'returnedAt.location',
			render : function(data, type, row) {
				if (row.returnedAt) {
					return row.returnedAt.location;
				}
				return '';
			}
		}, {
			data : 'fare',
			render : function(data, type, row) {
				if (row.fare) {
					return "Rs. "+row.fare;
				}
				return '';
			}
		}]
	});
});

/*------------------------------------------------------
 * This function is used to fetch all approved transfers
 -------------------------------------------------------*/
$('#approvedRequestTable').ready(function() {
	var table = $('table#approvedRequests').DataTable({
		'ajax' : "/data/approvedRequests",
		'serverSide' : true,
		columns : [ {
			data : 'requestId'
		}, {
			data : 'pickUpPoint.location',
			render : function(data, type, row) {
				if (row.pickUpPoint) {
					return row.pickUpPoint.location;
				}
				return '';
			}
		}, {
			data : 'manager.firstName',
			render: function ( data, type, row ) {
				return row.manager.firstName+" "+row.manager.lastName;
		    }
		}, {
			data : 'requestedOn',
			render: function ( data, type, row ) {
		        if ( type === 'display' || type === 'filter' ) {
		            var d = new Date( data );
		            return d.getDate() +'-'+ (d.getMonth()+1) +'-'+ d.getFullYear() + " "+ d.getHours() + ":"+d.getMinutes();
		        }
		        return data;
		    }
		}, {
			data : 'quantity'
		}, {
			data : 'approvedQuantity'
		}]
	});
});

/*----------------------------------------------------
 * This function is used to fetch all closed transfers
 -----------------------------------------------------*/
$('#closedTransferTable').ready(function() {
	var table = $('table#closedTransfers').DataTable({
		'ajax' : "/data/closedTransfers",
		'serverSide' : true,
		columns : [ {
			data : 'transferId'
		}, {
			data : 'transferredFrom.location',
			render : function(data, type, row) {
				if (row.transferredFrom) {
					return row.transferredFrom.location;
				}
				return '';
			}
		}, {
			data : 'transferredTo.location',
			render : function(data, type, row) {
				if (row.transferredTo) {
					return row.transferredTo.location;
				}
				return '';
			}
		}, {
			data : 'quantity',
			render : function(data, type, row, meta) {
				return '<button class="btn btn-primary btn-sm" data-toggle="modal" data-target="#bicyclesTransferred" title="Show Bicycles Transferred" onclick = "return showBicycles('+row.transferId+');">'+row.quantity+'</button>';
			}
		}, {
			data : 'dispatchedAt',
			render: function ( data, type, row ) {
		        if ( type === 'display' || type === 'filter' ) {
		            var d = new Date( data );
		            return d.getDate() +'-'+ (d.getMonth()+1) +'-'+ d.getFullYear() + " "+ d.getHours() + ":"+d.getMinutes();
		        }
		        return data;
		    }
		}, {
			data : 'arrivedOn',
			render: function ( data, type, row ) {
		        if ( type === 'display' || type === 'filter' ) {
		            var d = new Date( data );
		            return d.getDate() +'-'+ (d.getMonth()+1) +'-'+ d.getFullYear() + " "+ d.getHours() + ":"+d.getMinutes();
		        }
		        return data;
		    }
		}, {
			data : 'vehicleNo'
		}]
	});
});

function showBicycles(param){
	var id = param;
	$.ajax({
		type : "GET",
		url : "/admin/viewTransfer/"+id,
		dataType: "json",
		success: function(data){
			var bicycles = "";
			$.each(data, function(index, value) {
				bicycles = bicycles + "<span class = 'label label-info'>"+value.chasisNo+"</span> ";
	            console.log(value.chasisNo+" ");
	            });
			$("#bicycleChasisNo").html(bicycles);
	        },
	        error: function(e) {
	            console.log(e.message);
	        }
		});
}