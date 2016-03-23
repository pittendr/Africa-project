var recipeCount = 1;
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
	$(document).on("click", ".exit", function(){
		$(this).parent().parent().remove();
		recipeCount--;
		$('#recipe'+recipeCount).find('.andCircle').removeClass("disabled");
	});
	
})

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
	col1.innerHTML="<div id='variable_dropdown"+recipeCount+"' class='custom-dropdown'><span>Variables</span><ul class='custom-drop'><li><a>Elevation</a></li><li><a>Wind Speed</a></li><li><a>Wind Direction</a></li><li><a>Temperature</a></li><li><a>Humidity</a></li><li><a>Rain</a></li><li><a>Cloud Coverage</a></li></ul></div>"
	
	var col2 = document.createElement('div');
	col2.setAttribute("class", "col-sm-1 col-xs-1 text-center");
	col2.setAttribute("style", "margin-top:10px");
	col2.innerHTML='<div id="circle+'+recipeCount+'" class="circle-wrapper"><div class="circle"><p>></p></div><div class="circle"><p>=</p></div><div class="circle"><p><</p></div></div>'
	
	var col3 = document.createElement('div');
    col3.setAttribute("class", "col-sm-3 col-xs-3 text-center");
	col3.setAttribute("style", "margin-top:35px");
    col3.innerHTML='<input type="number" placeholder="Value" id="recipe_value'+recipeCount+'">'
	
	var col4 = document.createElement('div');
	col4.setAttribute("class", "col-sm-3 col-xs-3 text-center");
	col4.setAttribute("style", "margin-top:35px");
	col4.innerHTML='<input type="number" placeholder="Range" id="recipe_range'+recipeCount+'">'

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