# -*- coding: utf-8 -*-
# Generated by Django 1.9.4 on 2016-03-24 18:36
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('notifications', '0002_auto_20160324_0023'),
    ]

    operations = [
        migrations.AddField(
            model_name='recipe',
            name='multiple',
            field=models.CharField(blank=True, max_length=40, null=True),
        ),
    ]