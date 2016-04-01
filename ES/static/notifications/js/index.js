var recipeCount = 1;
var csrftoken = getCookie('csrftoken');

$(document).ready(function(){
	$("#recipe-container").on("click", ".custom-dropdown", function(){
		$(this).toggleClass('active');
	});
	$("#recipe-container").on("click", "#vardrop .custom-drop li", function(){
		$(this).closest("#vardrop").find('span').text($(this).text());
		setUnits($(this).closest("#vardrop").find('span').text(), $(this).closest(".recipe-wrapper").find(".value_label"));
	});
	$("#recipe-container").on("click", "#alertdrop .custom-drop li", function(){
		$(this).closest("#alertdrop").find('span').text($(this).text());
	});
	$("#recipe-container").on("click", ".andCircle", function(){
		if(!$(this).hasClass("disabled")){
			newRecipe();
			$(this).addClass("disabled");
		}

	});
	
	$("#recipe-container").on('mouseleave', '.custom-drop', function(){
		$(this).parent().removeClass('active');
	});
	
	$("#recipe-container").on('click', '.circle', function(){
		$(this).parent().find('.circle').not($(this)).css("background","#CDD5F6");
		$(this).parent().find('.circle').not($(this)).css("color","#071857");
		$(this).parent().find('.circle').not($(this)).removeClass("clicked");
	    if($(this).hasClass("clicked")){
			$(this).css("background","#CDD5F6");
			$(this).css("color","#071857");
		}else{
			$(this).css("background","#071857");
			$(this).css("color","#CDD5F6");
		}
		$(this).toggleClass("clicked");
		
	});
	
	$('table').on('click', 'tr', function(){
		$(this).parent().find('tr').not($(this)).css("background","#3855BF");
		$(this).parent().find('tr').not($(this)).removeClass("clicked");
	   
		if($(this).hasClass("clicked")){
			$(this).css("background","#3855BF");
			$('.toolbar').css("visibility", "hidden");
			
		}else{
			$(this).css("background","#071857");
			$('.toolbar').css("visibility", "visible");
			$('.toolbar').css('top', $(this).position().top);
		}
		$(this).toggleClass("clicked");
	});
	var tabledata=null;
	$(document).on("click", "#saveButton", function(){
		var i=1;
		var badRecipes = checkRecipes();
		if(typeof badRecipes == 'undefined' || badRecipes.length ==0){
			ajaxCall(i);
		}else{
			if (badRecipes.length == 1){
				alert("Recipe "+badRecipes+" is not valid.");
			}else{
				alert("Recipe(s) "+badRecipes+" are not valid.");
			}
			
		}
		function ajaxCall(i){
			var multiple= null;
			var id = null;
			if(recipeCount==1){
				multiple = "null";
				id = "null"
			}else{
				id = latest;
				if(i==1){
					multiple = "start";
				}else if(i==recipeCount){
					multiple = "end";
				}else{
					multiple = "middle";
				}
			}
			$.ajaxSetup({
				beforeSend: function(xhr, settings) {
					if (!csrfSafeMethod(settings.type) && !this.crossDomain) {
						xhr.setRequestHeader("X-CSRFToken", csrftoken);
					}
				}
			});
			$.ajax({
				method: 'POST',
				url: '/data',
				data: {
					'variable' : $("#recipe"+(i)).find('#vardrop span').text(),
					'operator' : $("#recipe"+(i)).find(".circle.clicked").text(),
					'value' : $("#recipe"+(i)).find('.recipe_value').val(),
					'range' : $("#recipe"+(i)).find('.recipe_range').val(),
					'multiple' : multiple,
					'id' : id,
					'alert' : $("#recipe1").find('#alertdrop span').text()
				},
				success: function (data) {
					$('#tablebody').html(data);
					if (i<recipeCount){
						i++;
						ajaxCall(i);
					}else{
						clearRecipes();
						latest+=i;
					}
				},
				error: function (data) {
					 console.log("failure", data);
				}
			});
		}
	});
	
	$(document).on("click", ".exit", function(){
		$(this).parent().parent().remove();
		recipeCount--;
		$('#recipe'+recipeCount).find('.andCircle').removeClass("disabled");
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

function newRecipe(){
	recipeCount++;
	var container = document.getElementById('recipe-container');
	
	var wrapper = document.createElement('div');
	wrapper.setAttribute("class", "recipe-wrapper");
	wrapper.setAttribute("id", "recipe"+recipeCount);
	
	var row = document.createElement('div');
	row.setAttribute("class", "row");
	row.style.position = "relative";
	
	var exit = document.createElement('div');
	exit.setAttribute("class", "exit");
	exit.innerHTML="x";
	
	row.appendChild(exit);
	
	var col1 = document.createElement('div');
	col1.setAttribute("class", "col-sm-3 col-xs-5 text-center");
	col1.setAttribute("style", "margin-top:35px");
	col1.innerHTML="<div id='vardrop' class='custom-dropdown shadow'><span>Variables</span><ul class='custom-drop'><li><a>Elevation</a></li><li><a>Wind Speed</a></li><li><a>Wind Direction</a></li><li><a>Temperature</a></li><li><a>Humidity</a></li><li><a>Rain</a></li><li><a>Cloud Coverage</a></li></ul></div>"
	
	var col2 = document.createElement('div');
	col2.setAttribute("class", "col-sm-1 col-xs-2 text-center");
	col2.setAttribute("style", "margin-top:10px");
	col2.innerHTML='<div class="circle-wrapper"><div class="circle shadow"><p>></p></div><div class="circle shadow"><p>=</p></div><div class="circle shadow"><p><</p></div></div>'
	
	var col3 = document.createElement('div');
    col3.setAttribute("class", "col-sm-3 col-xs-5 text-center");
	col3.setAttribute("style", "margin-top:35px");
    col3.innerHTML='<input type="number" placeholder="Value" class="recipe_value shadow"><label class="value_label"></label>'
	
	var col4 = document.createElement('div');
	col4.setAttribute("class", "col-sm-3 col-xs-6 text-center");
	col4.setAttribute("style", "margin-top:35px");
	col4.innerHTML='<input type="number" placeholder="Range" class="recipe_range shadow"><label class="range_label">&nbsp;km</label>'
   
	row.appendChild(col1);
	row.appendChild(col2);
	row.appendChild(col3);
	row.appendChild(col4);
	
	wrapper.appendChild(row);
	
	var andCircle = document.createElement("div");
	andCircle.setAttribute("class", "andCircle shadow");
	andCircle.innerHTML="+";
	
	wrapper.appendChild(andCircle);
	
	container.appendChild(wrapper);
}
function clearRecipes(){
	for (var i=1; i< recipeCount;i++){
		$('#recipe'+(i+1)).remove();
	}
	$('#recipe1').find('.andCircle').removeClass("disabled");
	$('#recipe1').find('#vardrop span').text("Variables");
	$('#recipe1').find('#alertdrop span').text("Alert Type")
	$('#recipe1').find('.circle').css("background","#CDD5F6");
	$('#recipe1').find('.circle').css("color","#071857");
	$('#recipe1').find('.circle').removeClass("clicked");
	$('#recipe1').find('.recipe_value').val('');
	$('#recipe1').find('.recipe_range').val('');
	recipeCount = 1;
}
function checkRecipes(){
	var badRecipes = [];
	for (var i=0; i< recipeCount;i++){
		if ($('#recipe'+(i+1)).find('span').text()=="Variables" ||
		!$('#recipe'+(i+1)).find('.circle').hasClass("clicked") ||
		$('#recipe'+(i+1)).find('.recipe_value').val() == '' ||
		$('#recipe'+(i+1)).find('.recipe_range').val() == '' ||
		$('#recipe1').find('#alertdrop span').text()=="Alert Type"){
			badRecipes.push((i+1));
		}
	}
	return badRecipes;
}
function setUnits(text, label){
	if (text=="Elevation"){
		label.html("&nbsp;km");
	}
	if (text=="Wind Speed"){
		label.html("&nbsp;km/h");
	}
	if (text=="Wind Direction"){
		label.html("&nbsp;°");
	}
	if (text=="Temperature"){
		label.html("&nbsp;°F");
	}
	if (text=="Humidity"){
		label.html("&nbsp;%");
	}
	if (text=="Rain"){
		label.html("&nbsp;mm");
	}
	if (text=="Cloud Coverage"){
		label.html("&nbsp;%");
	}
}

