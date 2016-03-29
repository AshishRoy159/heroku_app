/* jQuery Preloader
 -----------------------------------------------*/
$(window).load(function() {
	$('.preloader').fadeOut(1000); // set duration in brackets
});

$(document).ready(function() {

	/*
	 * Hide mobile menu after clicking on a link
	 * -----------------------------------------------
	 */
	$('.navbar-collapse a').click(function() {
		$(".navbar-collapse").collapse('hide');
	});

	/*
	 * jQuery to collapse the navbar on scroll
	 * -----------------------------------------------
	 */
	$(window).scroll(function() {
		if ($(".navbar").offset().top > 50) {
			$(".navbar-fixed-top").addClass("top-nav-collapse");
		} else {
			$(".navbar-fixed-top").removeClass("top-nav-collapse");
		}
	});

	/*
	 * BxSlider -----------------------------------------------
	 */
	(function(window, $) {
		'use strict';

		// Cache document for fast access.
		var document = window.document;

		function mainSlider() {
			$('.bxslider').bxSlider({
				pagerCustom : '#bx-pager',
				mode : 'fade',
				nextText : '',
				prevText : ''
			});
		}

		mainSlider();

		var $links = $(".bx-wrapper .bx-controls-direction a, #bx-pager a");
		$links.click(function() {
			$(".slider-caption").removeClass('animated fadeInLeft');
			$(".slider-caption").addClass('animated fadeInLeft');
		});

		$(".bx-controls").addClass('container');
		$(".bx-next").addClass('fa fa-angle-right');
		$(".bx-prev").addClass('fa fa-angle-left');

	})(window, jQuery);

	/*
	 * Owl Carousel -----------------------------------------------
	 */
	$(document).ready(function() {
		$("#owl-work").owlCarousel({
			autoPlay : 3000,
			items : 3,
			itemsDesktop : [ 1199, 3 ],
			itemsDesktopSmall : [ 979, 3 ],
		});
	});

	/*
	 * Parallax section -----------------------------------------------
	 */
	function initParallax() {
		$('#work').parallax("100%", 0.3);
		$('#about').parallax("100%", 0.2);
		$('#team').parallax("100%", 0.3);
		$('#portfolio').parallax("100%", 0.1);
		$('#plan').parallax("100%", 0.3);
		$('#contact').parallax("100%", 0.2);
	}
	initParallax();

	/*
	 * Nivo lightbox -----------------------------------------------
	 */
	$('#portfolio .col-md-4 a').nivoLightbox({
		effect : 'fadeScale',
	});

	/*
	 * wow -------------------------------
	 */
	new WOW({
		mobile : false
	}).init();

});

/*
 * Match password and confirm password
 */
function passwordFunction(form) {
	var pass = document.getElementById("password").value;
	var cnfPass = document.getElementById("password_confirm").value;
	var ok = true;
	if (pass != cnfPass) {
		document.getElementById("error_id").innerHTML = "Passwords doesn't match.";
		ok = false;
	}
	return ok;
}

/*
 * Match email and confirm email
 */
function emailFunction(form) {
	var email = document.getElementById("emailId").value;
	var cnfEmail = document.getElementById("email_confirm").value;
	var ok = true;
	if (email != cnfEmail) {
		document.getElementById("email_error_id").innerHTML = "Emails doesn't match.";
		ok = false;
	}
	return ok;
}

/*
 * Set amount as per chosen package
 */
function payment() {
	var x = document.getElementById("option").value;
	if (x == "Starter") {
		document.getElementById("amount").value = 100;
	} else if (x == "Regular") {
		document.getElementById("amount").value = 220;
	} else if (x == "Explorer") {
		document.getElementById("amount").value = 345;
	}
}

function hideInvalidMessage() {
	document.getElementById("invalidPasswordMessage").style.visibility = "hidden";
}