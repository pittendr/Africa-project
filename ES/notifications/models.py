from __future__ import unicode_literals

from django.db import models

class Recipe(models.Model):
    recipe_variable = models.CharField(max_length=40)
    logic_operator = models.NullBooleanField()
    recipe_limit = models.PositiveIntegerField()
    recipe_range = models.PositiveIntegerField()

# Create your models here.
