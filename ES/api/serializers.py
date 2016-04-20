from rest_framework import serializers
from api.models import FIA, Recipe 
from django.contrib.auth.models import User

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
		
class UserSerializer(serializers.ModelSerializer):
    recipes = serializers.PrimaryKeyRelatedField(many=True, queryset=Recipe.objects.all())

    class Meta:
        model = User
        fields = ('id', 'username', 'recipes')
		
class RecipeSerializer(serializers.ModelSerializer):
    owner = serializers.ReadOnlyField(source='owner.username')
    class Meta:
        model = Recipe
        fields = ('id', 'recipe_variable', 'logic_operator', 'recipe_limit', 'recipe_range', 'recipe_alert','multiple','recipe_match', 'recipe_name', 'owner')
		
		
    def create(self, validated_data):
        return Recipe.objects.create(**validated_data)

    def update(self, instance, validated_data):
        instance.recipe_variable = validated_data.get('recipe_variable', instance.recipe_variable)
        instance.logic_operator = validated_data.get('logic_operator', instance.logic_operator)
        instance.recipe_limit = validated_data.get('recipe_limit', instance.recipe_limit)
        instance.recipe_range = validated_data.get('recipe_range', instance.recipe_range)
        instance.recipe_alert = validated_data.get('recipe_alert', instance.recipe_alert)
        instance.multiple = validated_data.get('multiple', instance.multiple)
        instance.recipe_match = validated_data.get('recipe_match', instance.recipe_match)
        instance.recipe_name = validated_data.get('recipe_name', instance.recipe_name)
        instance.save()
        return instance
