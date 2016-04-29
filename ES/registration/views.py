from django.shortcuts import redirect, render
from django.contrib.auth.models import User

#validate the signup form data. Should be a little more robust in username/password validation
def signup(request):
    username = password = ''
    if request.POST:
        username = request.POST['username']
        password = request.POST['password']
        email = request.POST['email']
        confemail = request.POST['confirmemail']
        confpassword = request.POST['confirmpassword']
		
        if email != confemail:
            return render(request, 'registration/signup.html', {'error': 'E-mails did not match'})
		
        if password != confpassword:
            return render(request, 'registration/signup.html', {'error': 'Passwords did not match'})
   		
        if User.objects.filter(username = username).exists():
            return render(request, 'registration/signup.html', {'error': 'Username already in use'})
        elif User.objects.filter(email = email).exists():
            return render(request, 'registration/signup.html', {'error': 'E-mail already in use'})		
        else:
            user = User.objects.create_user(username, email, password)
            return render(request, 'registration/login.html')		
    return render(request, 'registration/signup.html')
