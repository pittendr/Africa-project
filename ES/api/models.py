from __future__ import unicode_literals
from django.contrib.auth.models import User

from django.db import models
    
class FIA(models.Model):
    phone = models.CharField(max_length=255, blank=True, default="unable to collect phone")
    gps = models.CharField(max_length=255)
    aid = models.CharField(max_length=255)
    mac = models.CharField(max_length=255)
    serial = models.CharField(max_length=255)
    pests = models.CharField(max_length=255)
    time = models.CharField(max_length=255)
    elevation = models.FloatField(blank=True)
    windspeed = models.FloatField(blank=True)
    winddir = models.FloatField(blank=True)
    temp = models.FloatField(blank=True)
    humidity = models.FloatField(blank=True)
    rain = models.FloatField(blank=True)
    clouds = models.FloatField(blank=True)

class Recipe(models.Model):
    owner = models.ForeignKey(User, related_name='recipes')
    recipe_variable = models.CharField(max_length=255)
    logic_operator = models.CharField(max_length=255)
    recipe_limit = models.PositiveIntegerField()
    recipe_range = models.PositiveIntegerField()
    multiple = models.CharField(max_length=255)
    recipe_match = models.CharField(max_length=255)
    recipe_alert = models.CharField(max_length=255)
    recipe_name = models.CharField(max_length=255)
	
    def save(self, *args, **kwargs):
        super(Recipe, self).save(*args, **kwargs)