from django.conf.urls import url
from api import views
from rest_framework.urlpatterns import format_suffix_patterns

urlpatterns = [
    url(r'^fia/$', views.FIAList.as_view()),
	url(r'^fia/(?P<pk>[0-9]+)/$', views.FIADetail.as_view()),
    url(r'^recipe/$', views.RecipeList.as_view()),
	url(r'^recipe/(?P<pk>[0-9]+)/$', views.RecipeDetail.as_view()),	
]

urlpatterns = format_suffix_patterns(urlpatterns)