from django.shortcuts import redirect, render
from django.contrib.auth import authenticate, login as auth_login
from notifications.models import Recipe

def login(request):
    username = password = ''
    next = ""
    if request.GET:  
        next = request.GET['next']
    if request.POST:
        username = request.POST['username']
        password = request.POST['password']
        user = authenticate(username=username, password=password)
        if user is not None:
            if user.is_active:
                auth_login(request, user)
                recipes = Recipe.objects.all()
                if next == "":
                    last = Recipe.objects.last();
                    if last != None:
                        last = last.id
                    else:
                        last = 0
                    context = {'recipes': recipes , 'latest' : last}
                    return redirect('/../', context)
                else:
                    context = {'recipes': recipes}
                    return redirect(next)
    return render(request, 'login/login.html', {'next':next})