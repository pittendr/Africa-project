from django.conf.urls import url
from django.contrib.auth import views as auth_views

from . import views
#Most of these urls point to built-in django views. Templates are in the template directory.
urlpatterns = [
    url('^signup/', views.signup),
    url('^login/', auth_views.login),
	url('^logout/',
        auth_views.logout,
        {'next_page': '/login/'}
    ),
	url('^change-password/', auth_views.password_change),
	url('^password-change-done/', 
        auth_views.password_change_done,
		name = 'password_change_done',
    ),
	url('^password-reset-done/', 
        auth_views.password_reset_done,
		name = 'password_reset_done',
    ),
    url(r'^reset-password/(?P<uidb64>[0-9A-Za-z_\-]+)/(?P<token>[0-9A-Za-z]{1,13}-[0-9A-Za-z]{1,20})/$',
        'django.contrib.auth.views.password_reset_confirm',
        name='password_reset_confirm'
	),	
	url(r'^reset-password/', 'django.contrib.auth.views.password_reset'),
	url('^password-reset-complete/', 
        auth_views.password_reset_complete,
		name = 'password_reset_complete',
    ),
	
]
