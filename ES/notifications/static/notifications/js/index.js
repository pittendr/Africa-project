var recipeCount = 1;
var csrftoken = getCookie('csrftoken');
var recipe={};
var data=[];
recipeCount=0;
$(document).ready(function(){
	addOptions();
	
	
	var drag = dragula([document.querySelector('#variable-box'), document.querySelector('#drop-box')], {
		accepts: function (el, target, source, sibling) {
			if(target == document.querySelector('#variable-box')){
				return false;
			}
			return true;
		},   
		copy: true,                     
		copySortSource: false,
		removeOnSpill:true,
	});
	var dropbox = $("#drop-box");
	
	drag.on('drop', function(el, target, source, sibling){
		$(el).attr('id', recipeCount);
		recipeCount++;
		clicked(el, '.tile');
		clearValues();
		data[$(".tile.clicked").attr("id")]=
				{
					"logic":"",
					"value":"",
					"range":"",
				};
		console.log(data[$(".tile.clicked").attr("id")])
	});
	
	
	$("#drop-box").on('click', '.tile', function(){
		clicked(this, ".tile");
		fillValues($(this).attr('id'));
		
	});
	
	
	
	$("#value-box").on('click', "#delButton", function(){
		delete data[$(".tile.clicked").attr("id")];
		$(".tile.clicked").remove();
		clearValues();
	});
	
	$("#value-box").on('click', "#addButton", function(){
		data[$(".tile.clicked").attr("id")]=
				{
					"logic":$(".circle.clicked").text(),
					"value":$("#value-input").val(),
					"range":$("#range-input").val(),
				};
		var text = checkRecipe();
		if (text!=""){
			alert(text);
		}else{
			for (var i=0;i<data.length;i++){
				if(data[i]!=undefined){	
					console.log(i+": "+data[i]);
				}
			}
			
		}
	});
	
	$("#value-box").on('click', ".circle", function(){
		clicked(this, '.circle');
	});
	
	

	/*var tabledata=null;
	$(document).on("click", "#saveButton", function(){
		var i=1;
		var badRecipes = checkRecipes();
		if(typeof badRecipes == 'undefined' || badRecipes.length ==0){
			name = prompt("Please enter the name of your Recipe");
			if(name=="null"){
				name = "Recipe";
			}
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
			var alrt = $("#recipe1").find('#alertdrop span').text();
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
					'alert' : alrt,
					'name' : name,
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
	});*/
	
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

var variables = ["Elevation", "Temperature", "Wind Direction", "Wind Speed", "Humidity", "Rain", "Cloud Coverage", "Genotype"];
var logic = [">", "=", "<"]
var digits = ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9"];
function addOptions(){
	var box = document.getElementById("variable-box");
	for (i=0; i<variables.length;i++){
		var tile = document.createElement("div");
		tile.setAttribute("class", "tile shadow draggable drag-drop");
		tile.innerHTML = "&nbsp;"+variables[i]+"&nbsp;";
		box.appendChild(tile);
	}
}

function clicked(el, cl){
	var elem = $(el).parent().find(cl).not($(el))
	elem.css("background","#CDD5F6");
	elem.css("color","#071857");
	elem.removeClass("clicked");
	$(el).css("background","#071857");
	$(el).css("color","#CDD5F6");
	$(el).addClass("clicked");
}

function fillValues(id){
	if (data[id].logic == ">"){
		clicked("#greaterThan", ".circle");
	}else if(data[id].logic == "="){
		clicked("#equalTo", ".circle");
	}else if(data[id].logic == "<"){
		clicked("#lessThan", ".circle");
	}else{
	    $(".circle.clicked").css("background","#CDD5F6");
		$(".circle.clicked").css("color","#071857");
		$(".circle.clicked").removeClass("clicked");
	}
	$("#value-input").val(data[id].value);
	$("#range-input").val(data[id].range);
}
function clearValues(){
	$(".circle.clicked").css("background","#CDD5F6");
	$(".circle.clicked").css("color","#071857");
	$(".circle.clicked").removeClass("clicked");
	$('#value-input').val("");
	$('#range-input').val("");
}

function checkRecipe(){
	var badRecipe = false;
	var text = "Cannot add recipe:";
	if($(".circle.clicked").length<1){
		text+="\nNo logical operator selected"
		badRecipe=true;
	}
	if($('#value-input').val()=="" || $('#value-input').val()<0){
		text+="\nA positive integer is required for the Value input"
		badRecipe=true;
	}
	if($('#value-input').val()=="" || $('#value-input').val()<0){
		text+="\nA positive integer is required for the Range input"
		badRecipe=true;
	}
	if (!badRecipe){
		text ="";
	}
	return text;
			
}



