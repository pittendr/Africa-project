from django.shortcuts import render
from django.http import HttpResponse
import simplejson as json
from django.views.decorators.csrf import requires_csrf_token
from notifications.models import Recipe

@requires_csrf_token
def index(request):
    recipes = Recipe.objects.all()
    context = {'recipes': recipes}
    return render(request, 'notifications/index.html', context)
	

def data(request):
    if request.method == 'POST' and request.is_ajax():
        variable = request.POST['variable']
        operator = request.POST['operator']
        value = request.POST['value']
        range = request.POST['range']
        
        recipe = Recipe.objects.create(recipe_variable=variable, logic_operator = operator, recipe_limit = value, recipe_range = range)
        recipes = Recipe.objects.all()
        context = {'recipes': recipes}		
        return render(request, 'notifications/table.html', context)
    else:
        return HttpResponse("Failed")
		