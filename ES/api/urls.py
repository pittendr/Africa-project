from django.conf.urls import url
from api import views
from rest_framework.urlpatterns import format_suffix_patterns

urlpatterns = [
    url(r'^api/$', views.FIAList.as_view()),
	url(r'^api/(?P<pk>[0-9]+)/$', views.FIADetail.as_view()),	
]

urlpatterns = format_suffix_patterns(urlpatterns)