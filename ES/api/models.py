from __future__ import unicode_literals

from django.db import models
    
class FIA(models.Model):
    phone = models.CharField(max_length=255, blank=True, default="unable to collect phone")
    gps = models.CharField(max_length=255)
    aid = models.CharField(max_length=255)
    mac = models.CharField(max_length=255)
    serial = models.CharField(max_length=255)
    pests = models.CharField(max_length=255)
    time = models.CharField(max_length=255)
    elevation = models.CharField(max_length=255, blank=True)
    windspeed = models.CharField(max_length=255, blank=True)
    winddir = models.CharField(max_length=255, blank=True)
    temp = models.CharField(max_length=255, blank=True)
    humidity = models.CharField(max_length=255, blank=True)
    rain = models.CharField(max_length=255, blank=True)
    clouds = models.CharField(max_length=255, blank=True)
