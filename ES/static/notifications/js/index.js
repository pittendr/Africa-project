var recipeCount = 1;
var csrftoken = getCookie('csrftoken');

$(document).ready(function(){
	$("#recipe-container").on("click", ".custom-dropdown", function(){
		$(this).toggleClass('active');
	});
	$("#recipe-container").on("click", ".custom-drop li", function(){
		$(this).parent().parent().find('span').text($(this).text());
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
		$(this).parent().find('.circle').not($(this)).css("background","white");
		$(this).parent().find('.circle').not($(this)).removeClass("clicked");
	    if($(this).hasClass("clicked")){
			$(this).css("background","white");
		}else{
			$(this).css("background","gray");
		}
		$(this).toggleClass("clicked");
	});
	var data=null;
	$(document).on("click", "#saveButton", function(){
		for(var i=0; i<recipeCount;i++){
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
					'variable' : $("#recipe"+(i+1)).find('span').text(),
					'operator' : $("#recipe"+(i+1)).find(".circle.clicked").text(),
					'value' : $("#recipe"+(i+1)).find('#recipe_value').val(),
					'range' : $("#recipe"+(i+1)).find('#recipe_range').val(),
				},
				success: function (data) {
					 $('#tablebody').html(data);
				},
				error: function (data) {
					 alert(data);
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
	col1.setAttribute("class", "col-sm-3 col-xs-3 col-xs-offset-1 text-center");
	col1.setAttribute("style", "margin-top:35px");
	col1.innerHTML="<div class='custom-dropdown'><span>Variables</span><ul class='custom-drop'><li><a>Elevation</a></li><li><a>Wind Speed</a></li><li><a>Wind Direction</a></li><li><a>Temperature</a></li><li><a>Humidity</a></li><li><a>Rain</a></li><li><a>Cloud Coverage</a></li></ul></div>"
	
	var col2 = document.createElement('div');
	col2.setAttribute("class", "col-sm-1 col-xs-1 text-center");
	col2.setAttribute("style", "margin-top:10px");
	col2.innerHTML='<div class="circle-wrapper"><div class="circle"><p>></p></div><div class="circle"><p>=</p></div><div class="circle"><p><</p></div></div>'
	
	var col3 = document.createElement('div');
    col3.setAttribute("class", "col-sm-3 col-xs-3 text-center");
	col3.setAttribute("style", "margin-top:35px");
    col3.innerHTML='<input type="number" placeholder="Value" class="recipe_value">'
	
	var col4 = document.createElement('div');
	col4.setAttribute("class", "col-sm-3 col-xs-3 text-center");
	col4.setAttribute("style", "margin-top:35px");
	col4.innerHTML='<input type="number" placeholder="Range" class="recipe_range">'

	row.appendChild(col1);
	row.appendChild(col2);
	row.appendChild(col3);
	row.appendChild(col4);
	
	wrapper.appendChild(row);
	
	var andCircle = document.createElement("div");
	andCircle.setAttribute("class", "andCircle");
	andCircle.innerHTML="+";
	
	wrapper.appendChild(andCircle);
	
	container.appendChild(wrapper);
	
	
	
}

