from rest_framework.response import Response
from rest_framework import status
from rest_framework.views import APIView
from rest_framework import generics
from api.models import FIA, Recipe
from api.serializers import FIASerializer, RecipeSerializer, UserSerializer
from django.http import Http404
import json
import pyowm
import urllib
from django.contrib.auth.models import User
from django.shortcuts import render

#API implented using djangorestframework, further details can be found at http://www.django-rest-framework.org/

#Handles POST and GET requests
class FIAList(APIView):
    def get(self, request, format=None):
        fia = FIA.objects.all()
        serializer = FIASerializer(fia, many=True)
        return Response(serializer.data)
		
    #Takes GPS values from POST request and makes API calls to owm and googleapis for weather and GIS data. Stores this data in db.
    def post(self, request):
        req = request.data
        #Check the received POST values
        if req["mac"] == "null" or req["mac"] is None:
            req["mac"] = None
        if req["aid"] == "null" or req["aid"] is None:
            req["aid"] = None			
        if req["time"] == "time" or req["time"] is None:
            req["time"] = None
        if req["phone"] == "null" or req["phone"] is None:
            req["phone"] = None
        if req["serial"] == "null" or req["serial"] is None:
            req["serial"] = None
        if req["phone"] == "null" or req["phone"] is None:
            req["phone"] = None
           
        if gps is None or gps == "null" or pests is None or pests == "null":
           return Response("No gps data", status=status.HTTP_201_CREATED) 
        gps = json.dumps(req['gps'])
        gps = json.loads(gps)
        gps = json.loads(gps)
        pests = json.dumps(req['pests'])
        pests = json.loads(pests)
        pests = json.loads(pests)
        
        longitude = gps['longitude']
        latitude = gps['latitude']

	#Google API for elevation details
        url = 'https://maps.googleapis.com/maps/api/elevation/json?locations='+str(latitude)+','+str(longitude)+'&key=AIzaSyBaIP0AbBO1GgS8q9i28FQGd8HopCMNES0'
        resp = json.load(urllib.urlopen(url))
        elevationarray = []
        for resultset in resp['results']:
            elevationarray.append(resultset['elevation'])
        elevation = elevationarray[0]

        #OpenWeatherMap API for weather details. Uses pyowm api python wrapper. (RAIN AND WINDDIR VALUES NOT WORKING ANYMORE, MAKE SURE IT IS FIXED BEFORE RELEASE)
        owm = pyowm.OWM('cdb2f4f903237bca966049d773d2e06e')
        observation = owm.weather_at_coords(float(latitude), float(longitude))
        weather = observation.get_weather()
        windspeed = weather.get_wind()['speed']
        winddir = None#weather.get_wind()['deg']
        humidity = weather.get_humidity()
        rain = None#weather.get_rain()
        temp = weather.get_temperature('celsius')['temp']
        clouds = weather.get_clouds()

        req['elevation']=float(elevation)
        req['windspeed']=float(windspeed)
        req['winddir']=winddir
        req['humidity']=float(humidity)
        req['temp']=float(temp)
        req['rain']=rain
        req['clouds']=float(clouds)

        req = json.dumps(req)
        req = json.loads(req)		
        print req		
        serializer = FIASerializer(data=req)
        if serializer.is_valid():
            serializer.save()	
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

#Handles PUT, PATCH, OPTIONS requests
class FIADetail(generics.RetrieveAPIView):    
    queryset = FIA.objects.all()
    serializer_class = FIASerializer

#Handles POST and GET requests 
class RecipeList(generics.ListCreateAPIView):
        queryset = Recipe.objects.all()
        serializer_class = RecipeSerializer
        def post(self, request, format=None):
            serializer = RecipeSerializer(data=request.data)
            req = request.data
            #Validate POST data
            if req['recipe_variable'] is None or req['logic_operator'] is None or req['recipe_limit'] is None or req['recipe_match'] is None or req['recipe_range'] is None or req['recipe_alert'] is None or req['recipe_name'] is None or req['owner'] is None or req['multiple'] is None:
                return Response("Empty variable", status=status.HTTP_400_BAD_REQUEST)
            
            if req['recipe_variable'] == "Temperature" or req['recipe_variable'] == "Elevation" or req['recipe_variable'] == "Wind Direction" or req['recipe_variable'] == "Wind Speed" or req['recipe_variable'] == "Rain" or req['recipe_variable'] == "Humidity" or req['recipe_variable'] == "Cloud Coverage" or req['recipe_variable'] == "Genotype":  
                print "valid"
            else:
                print req['recipe_variable']
                return Response("Variable Error", status=status.HTTP_400_BAD_REQUEST)
            if req['logic_operator'] == ">" or req['logic_operator'] =="<" or req['logic_operator'] == "=":
                print "valid"
            else:
                return Response("Logic Operator Error", status=status.HTTP_400_BAD_REQUEST)
	        
            try:
                val = int(req['recipe_limit'])
            except ValueError:
                return Response("Recipe value is not an Integer", status=status.HTTP_400_BAD_REQUEST)
            else:
                if val < 0:
                    return Response("Recipe value is not positive", status=status.HTTP_400_BAD_REQUEST)				

            try:
                val = int(req['recipe_range'])
            except ValueError:
                return Response("Recipe Range is not an Integer", status=status.HTTP_400_BAD_REQUEST)
            else:
                if val < 0:
                    return Response("Recipe Range is not positive", status=status.HTTP_400_BAD_REQUEST)				
			
            if req['recipe_match'] != "null":			
                try:
                    print req['recipe_match']
                    val = int(req['recipe_match'])
                except ValueError:
                    return Response("Recipe id is not an Integer", status=status.HTTP_400_BAD_REQUEST)
                else:
                    if val < 0:
                        return Response("Recipe id is not positive", status=status.HTTP_400_BAD_REQUEST)				


            if req['recipe_alert'] == "Spray" or req['recipe_alert'] == "Scout":
                print "valid"
            else:
                return Response("Recipe alert is not spray or scout", status=status.HTTP_400_BAD_REQUEST)
			
            if req['multiple'] == "start" or req['multiple'] == "end" or req['multiple'] == "middle" or req['multiple'] == "null":
                print "valid"
            else:
                return Response("Recipe multiple is not start or end or middle", status=status.HTTP_400_BAD_REQUEST)
				
            if serializer.is_valid():
                recipes = Recipe.objects.all()
                context = {'recipes': recipes}
                serializer.save(owner=self.request.user)	
                return render(request, 'notifications/table.html', context)
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
            
#Handles PUT, PATCH, DELETE, OPTIONS	
class RecipeDetail(generics.RetrieveUpdateDestroyAPIView):
    queryset = Recipe.objects.all()
    serializer_class = RecipeSerializer
#Handles POST and GET requests
class UserList(generics.ListAPIView):
    queryset = User.objects.all()
    serializer_class = UserSerializer
#Handles PUT, PATCH, OPTIONS
class UserDetail(generics.RetrieveAPIView):
    queryset = User.objects.all()
    serializer_class = UserSerializer

		
