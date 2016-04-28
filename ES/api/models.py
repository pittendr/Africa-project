from __future__ import unicode_literals
from django.contrib.auth.models import User

from django.db import models

#Models for SQL db
class FIA(models.Model):
    phone = models.CharField(max_length=255, blank=True, default="unable to collect phone", null=True)
    gps = models.CharField(max_length=255, blank=True, null=True)
    aid = models.CharField(max_length=255, blank=True, null=True)
    mac = models.CharField(max_length=255, blank=True, null=True)
    serial = models.CharField(max_length=255, blank=True, null=True)
    pests = models.CharField(max_length=255, blank=True, null=True)
    time = models.CharField(max_length=255, blank=True, null=True)
    elevation = models.FloatField(blank=True, null=True)
    windspeed = models.FloatField(blank=True, null=True)
    winddir = models.FloatField(blank=True, null=True)
    temp = models.FloatField(blank=True, null=True)
    humidity = models.FloatField(blank=True, null=True)
    rain = models.FloatField(blank=True, null=True)
    clouds = models.FloatField(blank=True, null=True)

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