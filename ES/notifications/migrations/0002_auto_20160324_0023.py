# -*- coding: utf-8 -*-
# Generated by Django 1.9.4 on 2016-03-24 00:23
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('notifications', '0001_initial'),
    ]

    operations = [
        migrations.AlterField(
            model_name='recipe',
            name='logic_operator',
            field=models.CharField(max_length=40),
        ),
    ]
