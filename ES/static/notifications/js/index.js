function DropDown(el) {
    this.dd = el;
    this.placeholder = this.dd.children('span');
    this.opts = this.dd.find('ul.dropdown > li');
    this.val = '';
    this.index = -1;
    this.initEvents();
}
DropDown.prototype = {
    initEvents : function() {
        var obj = this;

        obj.dd.on('click', function(event){
            $(this).toggleClass('active');
            return false;
        });

        obj.opts.on('click',function(){
            var opt = $(this);
            obj.val = opt.text();
            obj.index = opt.index();
            obj.placeholder.text(obj.val);
        });
    },
    getValue : function() {
        return this.val;
    },
    getIndex : function() {
        return this.index;
    }
}


$(document).ready(function(){
	var dd = new DropDown($('#variable_dropdown'));
	
	$("#recipe-container").on("click", ".andCircle", function(){
		if(!$(this).hasClass("disabled")){
			newRecipe();
			$(this).addClass("disabled");
		}

	});
	$(".row").on("click", ".exit", function(){
		alert("hello")
		$(this).parent().parent().remove();
	});
	
})

function newRecipe(){
	var container = document.getElementById('recipe-container');
	
	var wrapper = document.createElement('div');
	wrapper.setAttribute("class", "recipe-wrapper");
	
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
	col1.innerHTML="<div id='variable_dropdown' class='custom-dropdown'><span>Variables</span><ul class='custom-drop'><li><a>Test</a></li><li><a>Test</a></li><li><a>Test</a></li></ul></div>"
	
	var col2 = document.createElement('div');
	col2.setAttribute("class", "col-sm-1 col-xs-1 text-center");
	col2.setAttribute("style", "margin-top:10px");
	col2.innerHTML='<div class="circle-wrapper"><div class="circle"><p>></p></div><div class="circle"><p>=</p></div><div class="circle"><p><</p></div></div>'
	
	var col3 = document.createElement('div');
    col3.setAttribute("class", "col-sm-3 col-xs-3 text-center");
	col3.setAttribute("style", "margin-top:35px");
    col3.innerHTML='<input type="number" placeholder="Value" id="recipe_value">'
	
	var col4 = document.createElement('div');
	col4.setAttribute("class", "col-sm-3 col-xs-3 text-center");
	col4.setAttribute("style", "margin-top:35px");
	col4.innerHTML='<input type="number" placeholder="Range" id="recipe_range">'

	row.appendChild(col1);
	row.appendChild(col2);
	row.appendChild(col3);
	row.appendChild(col4);
	
	wrapper.appendChild(row);
	
	container.appendChild(wrapper);
	
	var andCircle = document.createElement("div");
	andCircle.setAttribute("class", "andCircle");
	andCircle.innerHTML="+";
	
	container.appendChild(andCircle);
	
}