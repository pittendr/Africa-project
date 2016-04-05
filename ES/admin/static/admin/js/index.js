$(document).ready(function(){
	$('ul').on('click', 'li', function(){
		if($(this).hasClass('clicked')){
			$(this).find(".recipe-block").css("display", "none");
		}else{
			$(this).find(".recipe-block").css("display", "block");
		}
	});
	$(document).on('click', '#panelhead', function(){
		if($(this).hasClass('clicked')){
			$(this).parent().find("#panelfoot").css("display", "none");
		}else{
			$(this).parent().find("#panelfoot").css("display", "block");
		}
		$(this).toggleClass("clicked");
	})
});