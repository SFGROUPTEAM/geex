$(function(){
	$('#lanPos').css('top',$('.hover').offset().top-280);
		$('.col-md-2 .row .panel-group .panel ul li').hover(function(){
			$('#lanPos').css('top',$(this).offset().top-280);
			},function(){
				$('#lanPos').css('top',$('.hover').offset().top-280);
				})
	})