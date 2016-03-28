from rest_framework import serializers
from api.models import FIA  

class FIASerializer(serializers.ModelSerializer):
    class Meta:
        model = FIA
        fields = ('id', 'phone', 'gps', 'aid', 'mac', 'serial', 'pests', 'time', 'elevation', 'windspeed', 'winddir', 'temp', 'humidity', 'rain', 'clouds')
		
    def create(self, validated_data):
        return FIA.objects.create(**validated_data)

    def update(self, instance, validated_data):
        instance.phone = validated_data.get('phone', instance.phone)
        instance.gps = validated_data.get('gps', instance.gps)
        instance.aid = validated_data.get('aid', instance.aid)
        instance.mac = validated_data.get('mac', instance.mac)
        instance.serial = validated_data.get('serial', instance.serial)
        instance.pests = validated_data.get('pests', instance.pests)
        instance.time = validated_data.get('time', instance.time)
        instance.elevation = validated_data.get('elevation', instance.elevation)
        instance.windspeed = validated_data.get('windspeed', instance.windspeed)
        instance.winddir = validated_data.get('winddir', instance.winddir)
        instance.temp = validated_data.get('temp', instance.temp)
        instance.humidity = validated_data.get('humidity', instance.humidity)
        instance.rain = validated_data.get('rain', instance.rain)
        instance.clouds = validated_data.get('clouds', instance.clouds)
        instance.save()
        return instance