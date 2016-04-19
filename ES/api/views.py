from rest_framework.response import Response
from rest_framework import status
from rest_framework.views import APIView
from rest_framework import generics
from api.models import FIA, Recipe
from api.serializers import FIASerializer, RecipeSerializer, UserSerializer
from django.http import Http404
import simplejson as json
import pyowm
import urllib
from django.contrib.auth.models import User
from django.shortcuts import render
		
class FIAList(APIView):
    def get(self, request, format=None):
        fia = FIA.objects.all()
        serializer = FIASerializer(fia, many=True)
        return Response(serializer.data)
    def post(self, request, format=None):
        req = request.data
		
        latitude = str(json.loads(request.data['gps'])['latitude'])
        longitude = str(json.loads(request.data['gps'])['longitude'])
		
        url = 'https://maps.googleapis.com/maps/api/elevation/json?locations='+str(latitude)+','+str(longitude)+'&key=AIzaSyBaIP0AbBO1GgS8q9i28FQGd8HopCMNES0'
        resp = json.load(urllib.urlopen(url))
        elevationarray = []
        for resultset in resp['results']:
            elevationarray.append(resultset['elevation'])
        elevation = elevationarray[0]

        owm = pyowm.OWM('cdb2f4f903237bca966049d773d2e06e')
        observation = owm.weather_at_coords(float(latitude), float(longitude))
        weather = observation.get_weather()
        windspeed = weather.get_wind()['speed']
        winddir = weather.get_wind()['deg']
        humidity = weather.get_humidity()
        rain = weather.get_rain()
        temp = weather.get_temperature('fahrenheit')['temp']
        clouds = weather.get_clouds()

        req['elevation']=elevation
        req['windspeed']=windspeed
        req['winddir']=winddir
        req['humidity']=humidity
        req['temp']=temp
        req['rain']=rain
        req['clouds']=clouds
		
		
        serializer = FIASerializer(data=req)
        if serializer.is_valid():
            serializer.save()	
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

class FIADetail(generics.RetrieveAPIView):    
    queryset = FIA.objects.all()
    serializer_class = FIASerializer
	
class RecipeList(generics.ListCreateAPIView):
    queryset = Recipe.objects.all()
    serializer_class = RecipeSerializer
    def post(self, request, format=None):
        serializer = RecipeSerializer(data=request.data)
        if serializer.is_valid():
            recipes = Recipe.objects.all()
            context = {'recipes': recipes}
            serializer.save()	
            return render(request, 'notifications/table.html', context)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

        		
        
    
    def perform_create(self, serializer):
        serializer.save(owner=self.request.user)
		
class RecipeDetail(generics.RetrieveUpdateDestroyAPIView):
    queryset = Recipe.objects.all()
    serializer_class = RecipeSerializer
	
class UserList(generics.ListAPIView):
    queryset = User.objects.all()
    serializer_class = UserSerializer


class UserDetail(generics.RetrieveAPIView):
    queryset = User.objects.all()
    serializer_class = UserSerializer

		