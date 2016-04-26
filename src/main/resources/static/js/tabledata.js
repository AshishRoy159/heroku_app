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
			data : 'user',
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