# -*- coding: utf-8 -*-
# Generated by Django 1.9.5 on 2016-04-27 17:38
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('api', '0011_auto_20160427_1722'),
    ]

    operations = [
        migrations.AlterField(
            model_name='fia',
            name='aid',
            field=models.CharField(blank=True, max_length=255, null=True),
        ),
        migrations.AlterField(
            model_name='fia',
            name='gps',
            field=models.CharField(blank=True, max_length=255, null=True),
        ),
        migrations.AlterField(
            model_name='fia',
            name='mac',
            field=models.CharField(blank=True, max_length=255, null=True),
        ),
        migrations.AlterField(
            model_name='fia',
            name='pests',
            field=models.CharField(blank=True, max_length=255, null=True),
        ),
        migrations.AlterField(
            model_name='fia',
            name='phone',
            field=models.CharField(blank=True, default='unable to collect phone', max_length=255, null=True),
        ),
        migrations.AlterField(
            model_name='fia',
            name='serial',
            field=models.CharField(blank=True, max_length=255, null=True),
        ),
        migrations.AlterField(
            model_name='fia',
            name='time',
            field=models.CharField(blank=True, max_length=255, null=True),
        ),
    ]
