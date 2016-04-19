from django.shortcuts import render, redirect
from django.http import HttpResponse
import simplejson as json
from api.models import Recipe
from django.views.decorators.csrf import ensure_csrf_cookie
from django.http import Http404

@ensure_csrf_cookie
def index(request):
    if not request.user.is_authenticated():
        return redirect('/login')
    recipes = Recipe.objects.all()
    context = {'recipes': recipes }
	
    return render(request, 'notifications/index.html', context)
	
def latest(request):
    if request.method == 'GET' and request.is_ajax():
        last = Recipe.objects.last();
        if last != None:
            last = last.id
        else:
            last = 0;
        context = {'latest':last}
        return render(request, 'notifications/latest.html', context)
    else:
        raise Http404("Page not found")

		