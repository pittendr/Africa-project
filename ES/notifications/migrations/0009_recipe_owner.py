# -*- coding: utf-8 -*-
# Generated by Django 1.9.5 on 2016-04-08 20:21
from __future__ import unicode_literals

from django.conf import settings
from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        migrations.swappable_dependency(settings.AUTH_USER_MODEL),
        ('notifications', '0008_auto_20160408_1852'),
    ]

    operations = [
        migrations.AddField(
            model_name='recipe',
            name='owner',
            field=models.ForeignKey(default=1, on_delete=django.db.models.deletion.CASCADE, related_name='fia', to=settings.AUTH_USER_MODEL),
            preserve_default=False,
        ),
    ]
