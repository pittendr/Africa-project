from django.shortcuts import render, redirect
from api.models import Recipe
from django.contrib.auth.models import User, Permission
from django.views.decorators.csrf import ensure_csrf_cookie
from django.http import HttpResponseForbidden, HttpResponse
import json

@ensure_csrf_cookie
def index(request):
    if not request.user.is_authenticated():
        return redirect('%s?next=%s' % ('../login/', request.path))
    recipes = Recipe.objects.all()
    users = User.objects.all()
    permissions = Permission.objects.get(codename="is_admin")
    context = {'recipes' : recipes, 'users' : users, 'permissions':permissions}
    return render(request, 'admin/index.html', context)

def change(request):
    if request.method == 'POST' and request.is_ajax():
        username = request.POST['user']
        user = User.objects.get(username=username)
        permission = Permission.objects.get(codename='is_creator')
        if user.has_perm('auth.is_creator'):
            user.user_permissions.remove(permission)
        else:
            user.user_permissions.add(permission)
        users = User.objects.all()
        context = {'users' : users}
        return render(request, 'admin/users.html', context) 
    else:
        raise Http404("Page not found")

def delete(request):
    if request.method == 'POST' and request.is_ajax():
        username = request.POST['user']
        User.objects.get(username=username).delete()
        
        users = User.objects.all()
        context = {'users' : users}
        return render(request, 'admin/users.html', context) 
    else:
        raise Http404("Page not found")

		