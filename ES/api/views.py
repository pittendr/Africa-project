from rest_framework.response import Response
from rest_framework import status
from rest_framework.views import APIView
from api.models import FIA
from api.serializers import FIASerializer
from django.http import Http404
import simplejson as json
import pyowm
import urllib
		
class FIAList(APIView):
    def get(self, request, format=None):
        fia = FIA.objects.all()
        serializer = FIASerializer(fia, many=True)
        return Response(serializer.data)
    def post(self, request, format=None):
        req =request.data
		
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

class FIADetail(APIView):
    def get_object(self, pk):
        try:
            fia = FIA.objects.get(pk=pk)
        except fia.DoesNotExist:
            return Response(status=status.HTTP_404_NOT_FOUND)
			
    def get(self, request, pk, format=None):
        fia = self.get_object(pk)
        serializer = FIASerializer(fia)
        return Response(serializer.data)

    def put(self, request, pk, format=None):
        fia = self.get_object(pk)
        serializer = FIASerializer(fia, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
    
    def delete(self, request, pk, format=None):
        fia = self.get_object(pk)	
        fia.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)
		

		