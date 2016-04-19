
var csrftoken = getCookie('csrftoken');
var data=[];
var recipeCount=0;
$(document).ready(function(){
	getLatest();
	
	var drag = dragula([document.querySelector('#variable-box'), document.querySelector('#drop-zone')], {
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
	
	drag.on('drop', function(el, target, source, sibling){
		$(el).attr('id', recipeCount);
		$(el).css('margin-bottom','10px');
		var span = document.createElement('span');
		span.setAttribute("class","text-center");
		span.innerHTML="+";
		elDropped = $("#drop-zone").find(el);
		if(elDropped.next('span').length <=0){
			if(!elDropped.is(':last-child')){
				elDropped.after(span);
			}
		}
		if(elDropped.prev('span').length <=0){
			if(!elDropped.is(':first-child')){
				elDropped.before(span);
			}
			
		}
		recipeCount++;
		clicked(el, '.tile');
		clearValues();
		data[$(".tile.clicked").attr("id")]=
				{
					"variable":$(".tile.clicked").text(),
					"logic":"",
					"value":"",
					"range":"",
					"check": false,
				};
	});
	
	
	$("#drop-zone").on('click', '.tile', function(){
		clicked(this, ".tile");
		fillValues($(this).attr('id'));
	});
	
	
	
	$("#value-box").on('click', "#delButton", function(){
		deleteData($(".tile.clicked").attr('id'));
		if($(".tile.clicked").next('span').length > 0 && $(".tile.clicked").prev('span').length>0){
			$(".tile.clicked").next().remove();
		}else if($(".tile.clicked").next('span').length > 0 && $(".tile.clicked").prev('span').length<=0){
			$(".tile.clicked").next().remove();
		}else if($(".tile.clicked").next('span').length <= 0 && $(".tile.clicked").prev('span').length>0){
			$(".tile.clicked").prev().remove();
		}
		
		$(".tile.clicked").remove();
		clearValues();
		
		for(var i=0;i<data.length;i++){
			console.log(data[i]);
		}
		
	});
	
	$("#value-box").on('click', "#addButton", function(){
		var text = checkRecipe();
		if (text!=""){
			alert(text);
		}else{
			data[$(".tile.clicked").attr("id")]=
				{
					"variable":$(".tile.clicked").text(),
					"logic":$(".circle.clicked").text(),
					"value":$("#value-input").val(),
					"range":$("#range-input").val(),
					"check": true,
				};
			$(".tile.clicked").css("background","#CDD5F6");
			$(".tile.clicked").css("color","#071857");
			$(".tile.clicked").addClass("tt");
			$(".tile.clicked").attr("tool", $(".tile.clicked").text() + $(".circle.clicked").text() +" "+$("#value-input").val() + "\nRange: " + $("#range-input").val());
			
			$(".tile.clicked").removeClass("clicked");
			
			clearValues();
			
		}
	});
	
	$("#value-box").on('click', ".circle", function(){
		clicked(this, '.circle');
	});
	
	

	var tabledata=null;
	$(document).on("click", "#saveButton", function(){
		var i=1;	
		if(!badRecipes()){
			name = prompt("Please enter the name of your Recipe");
			if(name=="null"){
				name = "Recipe";
			}			
			ajaxCall(i);
		}else{
			alert("Could not save recipes. Please ensure that all recipes have the required information.")
		}
	});
	
});


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
function addOptions(){
	var box = document.getElementById("variable-box");
	for (i=0; i<variables.length;i++){
		var tile = document.createElement("div");
		tile.setAttribute("class", "tile shadow");
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

function badRecipes(){
	for(var i=0;i<data.length; i++){
		console.log(data[i]);
		if (!data[i].check){
			return true;
		}
	}
	return false;
}
function clearRecipes(){
	$("#drop-zone").empty();
	deleteData("all");
	recipeCount = 0;
}

function deleteData(id){
	if(id=="all"){
		data.splice(0,data.length)
	}else{
		delete data[id];
		for(var i=0;i<data.length;i++){
			if(data[i]==undefined){
				data.splice(i,1)
			}
		}
	}
}

function ajaxCall(i){
	var multiple= "null";
	var id = "null";
	var alrt = "Spray"
	if(data.length!=1){
		id = latest;
		if(i==1){
			multiple = "start";
		}else if(i==data.length){
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
	console.log(i);
	$.ajax({
		method: 'POST',
		url: '/recipe/',
		data: {
			'recipe_variable' : data[i-1].variable,
			'logic_operator' : data[i-1].logic,
			'recipe_limit' : data[i-1].value,
			'recipe_range' : data[i-1].range,
			'multiple' : multiple,
			'recipe_match' : id,
			'recipe_alert' : alrt,
			'recipe_name' : name,
		},
		success: function (d) {
			
			console.log("success", i);
			if (i==data.length){
				latest+=i;
				$('#tablebody').html(d);
				clearValues();	
				clearRecipes();
			}else{
				i++;
				ajaxCall(i);
			}
		},
		error: function (d) {
			 console.log("failure", d);
		}
	});
}

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

function getLatest(){
	$.ajaxSetup({
		beforeSend: function(xhr, settings) {
			if (!csrfSafeMethod(settings.type) && !this.crossDomain) {
				xhr.setRequestHeader("X-CSRFToken", csrftoken);
			}
		}
	});
	$.ajax({
		method: 'GET',
		url: '/latest',
		success: function (d) {
			latest = d
		},
		error: function (d) {
			 console.log("failure", d);
		}
	});
}




