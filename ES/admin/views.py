from django.shortcuts import render, redirect
from notifications.models import Recipe
from django.views.decorators.csrf import ensure_csrf_cookie

@ensure_csrf_cookie
def index(request):
    if not request.user.is_authenticated():
        return redirect('%s?next=%s' % ('../login', request.path))
		
    recipes = Recipe.objects.all()
    context = {'recipes' : recipes}
    return render(request, 'admin/index.html', context)
