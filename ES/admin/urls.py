from django.conf.urls import url

from . import views

urlpatterns = [
    url(r'^admin/', views.index, name='index'),
    url(r'^change-admin/', views.change, name='change'),
    url(r'^delete-user/', views.delete, name='delete'),
]
