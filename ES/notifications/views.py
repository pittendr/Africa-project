from django.shortcuts import render, redirect
from django.http import HttpResponse
import simplejson as json
from api.models import Recipe
from django.views.decorators.csrf import ensure_csrf_cookie
from django.http import Http404

#Render the recipe creation template and pass it the list of all recipes
@ensure_csrf_cookie
def index(request):
    if not request.user.is_authenticated():
        return redirect('/login')
    recipes = Recipe.objects.all()
    context = {'recipes': recipes }
	
    return render(request, 'notifications/index.html', context)

#Handle ajax request for the latest id	
def latest(request):
    if request.method == 'GET' and request.is_ajax():
        last = Recipe.objects.last();
        if last != None:
            last = last.id
        else:
            last = 0;
        return HttpResponse(last)
    else:
        raise Http404("Page not found")

		
