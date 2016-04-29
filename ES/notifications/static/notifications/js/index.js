
var csrftoken = getCookie('csrftoken');
var data={};
var recipeCount=0;
var latest = null;
$(document).ready(function(){
	//Get Latest ID from db 
	getLatest();
	
	clicked(document.getElementById("sprayButton"), '.alert');
	
	//Drag and drop initializaion
	var drag = dragula([document.querySelector('#variable-zone'), document.querySelector('#drop-zone')], {
		accepts: function (el, target, source, sibling) {
			if(target == document.querySelector('#variable-zone')){
				return false;
			}
			return true;
		},   
		copy: true,                     
		copySortSource: false,
		removeOnSpill:true,
	});
	
	//When the tile is dropped in the drop-zone
	drag.on('drop', function(el, target, source, sibling){
		//Set ID and add + sign before or after dropped element depending on location
		$(el).attr('id', "recipe"+recipeCount);
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
		//Initialize the associated key in the data object 
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
				
		setUnits($(".tile.clicked").text(), document.getElementById("value-input"));
	});
	
	//Show the values associated with the dropped key
	$("#drop-zone").on('click', '.tile', function(){
		clicked(this, ".tile");
		fillValues($(this).attr('id'));
		setUnits($(".tile.clicked").text(), document.getElementById("value-input"));
	});
	
	//Spray or Scout alerts
	$("#recipe-footer").on('click','#sprayButton',function(){
		clicked(document.getElementById("sprayButton"),'.alert');
	});
	$("#recipe-footer").on('click','#scoutButton',function(){
		clicked(document.getElementById("scoutButton"),'.alert');
	});
	
	//When the delete button is clicked for a tile
	$("#value-box").on('click', "#delButton", function(){
		if($(".tile.clicked").length){
			console.log("deleting");
			//Delete the associated key in the data object
			deleteData($(".tile.clicked").attr('id'));
			//Remove the element from the drop-zone
			if($(".tile.clicked").next('span').length > 0 && $(".tile.clicked").prev('span').length>0){
				$(".tile.clicked").next().remove();
			}else if($(".tile.clicked").next('span').length > 0 && $(".tile.clicked").prev('span').length<=0){
				$(".tile.clicked").next().remove();
			}else if($(".tile.clicked").next('span').length <= 0 && $(".tile.clicked").prev('span').length>0){
				$(".tile.clicked").prev().remove();
			}
			
			$(".tile.clicked").remove();
			clearValues();
		}
		
	});
	
	//When add button is clicked for a tile
	$("#value-box").on('click', "#addButton", function(){
		//Check that the recipe is valid
		var text = checkRecipe();
		//Add the values to the associated data object key
		//check value is set to true to flag the recipe as filled 
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
			$(".tile.clicked").css("background","rgba(100,100,100,0.87)");
			$(".tile.clicked").css("color","rgba(255,255,255,0.87)");
			$(".tile.clicked").css("border-color","rgba(100,100,100,0.87)");
			$(".tile.clicked").append("&#10004;");
			$(".tile.clicked").addClass("tt");
			$(".tile.clicked").attr("tool", $(".tile.clicked").text() + $(".circle.clicked").text() +" "+$("#value-input").val() + ", \nRange: " + $("#range-input").val());
			$(".tile.clicked").addClass("added");
			$(".tile.clicked").removeClass("clicked");
			
			clearValues();
			
		}
	});
	
	$("#value-box").on('click', ".circle", function(){
		clicked(this, '.circle');
	});
	
	//When save button is clicked for a recipe
	var tabledata=null;
	$(document).on("click", "#saveButton", function(){
		var i=1;
		var sendData=[]
		//fill the sendData array with the associated key values
		for (var key in data){
			if (data.hasOwnProperty(key)){
				console.log(data[key]);
				if(data[key]!=undefined){
					sendData.push(data[key]);
				}
			}
		}
		//Make sure the recipe is valid and permorm ajax call
		if(!badRecipes(sendData)){
			name = prompt("Please enter the name of your Recipe");
			if(name=="null"){
				name = "Recipe";
			}			
			ajaxCall(i, sendData);
		}else{
			alert("Could not save recipes. Please ensure that all recipes have the required information.")
		}
	});
	
});


function setUnits(text, el){
	if (text=="Elevation"){
		el.placeholder = "Value (km)";
	}else if (text=="Wind Speed"){
		el.placeholder="Value (km/h)";
	}else if (text=="Wind Direction"){
		el.placeholder="Value (°)";
	}else if (text=="Temperature"){
		el.placeholder="Value (°C)";
	}else if (text=="Humidity"){
		el.placeholder="Value(%)";
	}else if (text=="Rain"){
		el.placeholder="Value (mm)";
	}else if (text=="Cloud Coverage"){
		el.placeholder="Value (%)";
	}else if (text=="Genotype"){
		el.placeholder="Value";
	}
}

//Initialize the variable box
var variables = ["Elevation", "Temperature", "Wind Direction", "Wind Speed", "Humidity", "Rain", "Cloud Coverage", "Genotype"];
function addOptions(){
	var box = document.getElementById("variable-zone");
	for (i=0; i<variables.length;i++){
		var tile = document.createElement("div");
		tile.setAttribute("class", "tile shadow");
		tile.innerHTML = "&nbsp;"+variables[i]+"&nbsp;";
		box.appendChild(tile);
	}
}

function clicked(el, cl){
	var elem = $(el).parent().find(cl).not($(el))
	elem.each(function(){
		if($(this).hasClass("added")){
			$(this).css("background","rgba(100,100,100,0.87)");
			$(this).css("color","rgba(255,255,255,0.87)");
			$(this).css("border-color","rgba(100,100,100,0.87)");
			$(this).removeClass("clicked");
		}else{
			$(this).css("background","rgba(255,255,255,0.87)");
			$(this).css("color","rgba(4,4,4,0.87)");
			$(this).css("border-color","rgba(255,255,255,0.87)");
			$(this).removeClass("clicked");
			
		}
	});
	$(el).css("background","rgba(4,4,4,0.87)");
	$(el).css("color","rgba(255,255,255,0.87)");
    $(el).css("border-color","rgba(255,255,255,0.87)");
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
	    $(".circle.clicked").css("background","rgba(255,255,255,0.87)");
		$(".circle.clicked").css("color","rgba(4,4,4,0.87)");
		$(".circle.clicked").removeClass("clicked");
	}
	$("#value-input").val(data[id].value);
	$("#range-input").val(data[id].range);
}
function clearValues(){
	$(".circle.clicked").css("background","rgba(255,255,255,0.87)");
	$(".circle.clicked").css("color","rgba(4,4,4,0.87)");
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
	if($('#range-input').val()=="" || $('#range-input').val()<0){
		text+="\nA positive integer is required for the Range input"
		badRecipe=true;
	}
	if (!badRecipe){
		text ="";
	}
	return text;
}

function badRecipes(recipeData){
	for(var i=0;i<recipeData.length; i++){
		console.log(recipeData[i]);
		if (!recipeData[i].check){
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
		for (var key in data){
			if (data.hasOwnProperty(key)){
				delete data[key];
			}
		}
	}else{
		delete data[id];
	}
}

function ajaxCall(i, recipeData){
	var multiple= "null";
	var id = "null";
	//Multiple key defines the recipes location for table formatting
	if(recipeData.length!=1){
		id = latest;
		if(i==1){
			multiple = "start";
		}else if(i==recipeData.length){
			multiple = "end";
		}else{
			multiple = "middle";
		}
	}
	//get the csrf token for safe ajax post
	$.ajaxSetup({
		beforeSend: function(xhr, settings) {
			if (!csrfSafeMethod(settings.type) && !this.crossDomain) {
				xhr.setRequestHeader("X-CSRFToken", csrftoken);
			}
		}
	});
	console.log(i);
	//multiple is to keep track of table formatting, recipe_match is the id of the first recipe in a multi-recipe input. 
	//The remaining recipes all have the same recipe_match as the first recipe in the list.
	$.ajax({
		method: 'POST',
		url: '/recipe/',
		data: {
			'recipe_variable' : recipeData[i-1].variable,
			'logic_operator' : recipeData[i-1].logic,
			'recipe_limit' : recipeData[i-1].value,
			'recipe_range' : recipeData[i-1].range,
			'multiple' : multiple,
			'recipe_match' : id,
			'recipe_alert' : $(".alert.clicked").text(),
			'recipe_name' : name,
			'owner' : user,
		},
		success: function (d) {
			
			console.log("success", i);
			//Recursively call ajaxCall until all elements in recipe are POSted, then fill the table
			if (i==recipeData.length){
				latest+=i;
				$('#tablebody').html(d);
				clearValues();	
				clearRecipes();
			}else{
				i++;
				ajaxCall(i, recipeData);
			}
		},
		error: function (d) {
			 console.log("failure", d);
		}
	});
}

//Get the csrf cookie
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

//Get the latest ID in the database
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
			setLatest(d);
		},
		error: function (d) {
			 console.log("failure", d);
		}
	});
}
function setLatest(d){
	latest = parseInt(d);
}




