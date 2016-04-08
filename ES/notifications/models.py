from __future__ import unicode_literals

from django.db import models

class Recipe(models.Model):
    recipe_variable = models.CharField(max_length=255)
    logic_operator = models.CharField(max_length=255)
    recipe_limit = models.PositiveIntegerField()
    recipe_range = models.PositiveIntegerField()
    multiple = models.CharField(max_length=255)
    recipe_match = models.CharField(max_length=255)
    recipe_alert = models.CharField(max_length=255)
    recipe_name = models.CharField(max_length=255)
# Create your models here.
