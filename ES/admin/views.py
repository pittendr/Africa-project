from django.shortcuts import render, redirect
from notifications.models import Recipe

def index(request):
    if not request.user.is_authenticated():
        return redirect('%s?next=%s' % ('../login', request.path))
		
    recipes = Recipe.objects.all()
    context = {'recipes' : recipes}
    return render(request, 'admin/index.html', context)
