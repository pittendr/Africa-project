from django.shortcuts import render, redirect
from django.http import HttpResponse
import simplejson as json
from notifications.models import Recipe
from django.views.decorators.csrf import ensure_csrf_cookie

@ensure_csrf_cookie
def index(request):
    if not request.user.is_authenticated():
        return redirect('%s?next=%s' % ('../login', request.path))
    recipes = Recipe.objects.all()
    last = Recipe.objects.last();
    if last != None:
        last = last.id
    else:
        last = 0;
    context = {'recipes': recipes , 'latest' : last}
	
    return render(request, 'notifications/index.html', context)
	

def data(request):
    if request.method == 'POST' and request.is_ajax():
        variable = request.POST['variable']
        operator = request.POST['operator']
        value = request.POST['value']
        range = request.POST['range']
        multiple = request.POST['multiple']
        id = request.POST['id']
        alert = request.POST['alert']     
        name = request.POST['name']
	   
        recipe = Recipe.objects.create(recipe_variable = variable, logic_operator = operator, recipe_limit = value, recipe_range = range, multiple = multiple, recipe_match = id, recipe_alert=alert, recipe_name=name)
        recipes = Recipe.objects.all()
        context = {'recipes': recipes}		
        return render(request, 'notifications/table.html', context)
    else:
        return HttpResponse("Failed")
		