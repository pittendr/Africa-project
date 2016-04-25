var csrftoken = getCookie('csrftoken');

$(document).ready(function(){
	$('.list').on('click', 'li a', function(){
		var elem = $(this).parent().find(".block")
		if($(this).hasClass('clicked')){
			elem.css("display", "none");
		}else{
			elem.css("display", "block");
		}
		$(this).toggleClass("clicked");
	});
	$(document).on('click', '#panelhead', function(){
		var elem = $(this).parent().find("#panelfoot")
		if($(this).hasClass('clicked')){
			elem.css("display", "none");
		}else{
			elem.css("display", "block");
		}
		$(this).toggleClass("clicked");
	});
	$('.list').on('change', '.admincheck', function(){
		username = $(this).closest('li').find(">:first-child").text();
		changeAdmin(username)
	});
	$('.list').on('click', '.deleteUser', function(){
		username = $(this).closest('li').find(">:first-child").text();
		if(confirm("Are you sure you want to delete "+username+"'s account? This cannot be reversed.")){
			deleteUser(username);
		}
		
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
				method: 'PATCH',
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

function changeAdmin(user){
	$.ajaxSetup({
		beforeSend: function(xhr, settings) {
			if (!csrfSafeMethod(settings.type) && !this.crossDomain) {
				xhr.setRequestHeader("X-CSRFToken", csrftoken);
			}
		}
	});
	$.ajax({
		method: 'POST',
		url: '/change-admin/',
		data: {'user':user},
		success: function (data) {
			alert(user + "'s creator status has changed")
		},
		error: function () {
			alert("Unable to change "+user+"'s creator status")
		}
	});
}
function deleteUser(user){
	$.ajaxSetup({
		beforeSend: function(xhr, settings) {
			if (!csrfSafeMethod(settings.type) && !this.crossDomain) {
				xhr.setRequestHeader("X-CSRFToken", csrftoken);
			}
		}
	});
	$.ajax({
		method: 'POST',
		url: '/delete-user/',
		data: {'user':user},
		success: function (data) {
			alert(user + " has been deleted")
			$("#user-list").html(data);
		},
		error: function () {
			alert("Unable to delete "+user)
		}
	});
}
