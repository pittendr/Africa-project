var csrftoken = getCookie('csrftoken');

$(document).ready(function(){
	$('#recipe-list').on('click', 'li a', function(){
		if($(this).hasClass('clicked')){
			$(this).parent().find(".recipe-block").css("display", "none");
		}else{
			$(this).parent().find(".recipe-block").css("display", "block");
		}
		$(this).toggleClass("clicked");
	});
	$(document).on('click', '#panelhead', function(){
		if($(this).hasClass('clicked')){
			$(this).parent().find("#panelfoot").css("display", "none");
		}else{
			$(this).parent().find("#panelfoot").css("display", "block");
		}
		$(this).toggleClass("clicked");
	});
	$("li").on("click", ".custom-dropdown", function(){
		$(this).toggleClass('active');
		if($(".custom-dropdown").not($(this)).hasClass("active")){
			$(".custom-dropdown").not($(this)).removeClass("active");
		}
	});
	$("li").on("click", "#vardrop .custom-drop li", function(){
		$(this).closest("#vardrop").find('span').text($(this).text());
	});
	$("li").on("click", "#alertdrop .custom-drop li", function(){
		$(this).closest("#alertdrop").find('span').text($(this).text());
	});
	$("li").on("click", "#logicdrop .custom-drop li", function(){
		$(this).closest("#logicdrop").find('span').text($(this).text());
	});
	$("li").on("click", '.save', function(){
		var recipeId =[];
		$(this).parent().find('.row').each(function(index, element){
			recipeId.push($(element).attr('id'));
		});
		var i = 0
		ajaxPut(i);
		function ajaxPut(i){
			
			id = recipeId[i];
			$.ajaxSetup({
				beforeSend: function(xhr, settings) {
					if (!csrfSafeMethod(settings.type) && !this.crossDomain) {
						xhr.setRequestHeader("X-CSRFToken", csrftoken);
					}
				}
			});
			$.ajax({
				method: 'PUT',
				url: '/recipe/'+id+'/',
				data: {
					"recipe_variable" : $('#'+id).find('#vardrop span').text(),
					"logic_operator" : $('#'+id).find('#logicdrop span').text(),
					"recipe_limit" : $('#'+id).find('.recipe_value').val(),
					"recipe_range" : $('#'+id).find('.recipe_range').val(),
					"recipe_alert" : $('#'+id).parent().find('#alertdrop span').text(),
				},
				success: function (data) {
					console.log("success", data);
					if (i<recipeId.length-1){
						i++;
						ajaxPut(i);
					}else{
						alert("success");
					}
				},
				error: function (data) {
					 alert("failure");
					 console.log("failure", data);
				}
			});
				
		}
	});
	$("li").on("click", '.delete', function(){
		var recipeId =[];
		$(this).parent().find('.row').each(function(index, element){
			recipeId.push($(element).attr('id'));
		});
		var block = $(this).closest('li');
		var i = 0
		ajaxDelete(i);
		function ajaxDelete(i){
			
			id = recipeId[i];
			$.ajaxSetup({
				beforeSend: function(xhr, settings) {
					if (!csrfSafeMethod(settings.type) && !this.crossDomain) {
						xhr.setRequestHeader("X-CSRFToken", csrftoken);
					}
				}
			});
			$.ajax({
				method: 'DELETE',
				url: '/recipe/'+id+'/',
				success: function () {
					console.log("success");
					if (i<recipeId.length-1){
						i++;
						ajaxDelete(i);
					}else{							
						alert("success");
						block.remove();
					}
				},
				error: function () {
					 alert("failure");
					 console.log("failure");
				}
			});
				
		}
	});
});

function csrfSafeMethod(method) {
    // these HTTP methods do not require CSRF protection
    return (/^(GET|HEAD|OPTIONS|TRACE)$/.test(method));
}

function getCookie(name) {
    var cookieValue = null;
    if (document.cookie && document.cookie != '') {
        var cookies = document.cookie.split(';');
        for (var i = 0; i < cookies.length; i++) {
            var cookie = jQuery.trim(cookies[i]);
            if (cookie.substring(0, name.length + 1) == (name + '=')) {
                cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
                break;
            }
        }
    }
    return cookieValue;
}
