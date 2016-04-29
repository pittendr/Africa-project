#Expert System Set Up and Operation Guide

This guide assumes the user is using a newly created server droplet

The following guides were used to help with the set up:
https://www.digitalocean.com/community/tutorials/how-to-serve-django-applications-with-apache-and-mod_wsgi-on-ubuntu-14-04
https://www.digitalocean.com/community/tutorials/how-to-use-mysql-or-mariadb-with-your-django-application-on-ubuntu-14-04

**Creating new sudo user**
For security reasons, the project will be set up under a new sudo user rather than the default root user
To create a new sudo user:

```
adduser eis
visudo
```
Under the line root add eis as follows:
```
root    ALL=(ALL:ALL) ALL
eis     ALL=(ALL:ALL) ALL
```

The following sections assume you are working under the eis user. If not, make sure that any paths, usernames, etc are correct for the user you are working under

**Setting up Virtual Environment**
Install pip, virtualenv, and create the project directory:
```
sudo apt-get update
sudo apt-get install python-pip 
sudo apt-get install get
sudo pip install virtualenv
mkdir ~/es
cd ~/es
```
Create a new virtual environment:
```
virtualenv esenv
```
And activate it using:
```
source esenv/bin/activate
```
At this point you are working under the virtual environment. You're shell should look something like this:
(esenv) eis@ExpertSystem:~/es/
Install python packages under virtual environment:
```
sudo pip install django
sudo pip install pyowm
sudo pip install simplejson
sudo pip install djangorestframework
```

**Cloning the project repository**
```
cd ~/es
git clone https://github.com/pittendr/Africa-project.git
```

**Installing mysql server**
```
sudo apt-get update
sudo apt-get install python-dev mysql-server libmysqlclient-dev
sudo mysql_install_db
sudo mysql_secure_installation (answer yes to everything except changing root password)
```
Now open the mysql shell
```
mysql -u root -p
```
Create the project database:
```
CREATE DATABASE expertsystem CHARACTER SET UTF8;
CREATE USER eis@localhost IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON expertsystem.* TO eis@localhost;
FLUSH PRIVILEGES;
exit
```
And install the python mysqlclient(Make sure you are still working under the virtual environment):
```
pip install mysqlclient
```
Now make sure that the django settings are correct:
```
nano ~/es/Africa-project/ES/expertsystem/settings.py
```
Make sure the DATABASES key is as below:
```
DATABASES = 
    'default': {
        'ENGINE': 'django.db.backends.mysql',
        'NAME': 'expertsystem',
        'USER': 'eis',
        'PASSWORD': 'password',
        'HOST': 'localhost',
        'PORT': '',
    }
}
```
And the ALLOWED_HOSTS key is as below:
```
ALLOWED_HOSTS = ['localhost', '127.0.0.1', 'yourIPaddress']
```
Then migrate the settings using:
```
cd ~/es/Africa-project/ES/
python manage.py makemigrations
python manage.py migrate
```

**Installing apache and mod_wsgi**
Install apache and mod_wsgi (mod_wsgi is an Apache HTTP Server module that provides a WSGI compliant interface for hosting Python based web applications under Apache):
```
sudo apt-get update
sudo apt-get install apache2 libapache2-mod-wsgi
```
Set up the Virtual Host for the project by modifying the 000-default.conf file:
```
sudo nano /etc/apache2/sites-available/000-default.conf
```
Make sure that the 000-default.conf file looks like this:
```
<VirtualHost *:80>
  ServerAdmin webmaster@localhost
  DocumentRoot /var/www/html
  ErrorLog ${APACHE_LOG_DIR}/error.log
  CustomLog ${APACHE_LOG_DIR}/access.log combined
  Alias /static /home/eis/es/Africa-project/ES/static
  <Directory /home/eis/es/Africa-project/ES/static>
    Require all granted
  </Directory>
  <Directory /home/eis/es/Africa-project/ES/expertsystem>
    <Files wsgi.py>
      Require all granted
    </Files>
  </Directory>
  WSGIDaemonProcess expertsystem python-path=/home/eis/es/:/home/eis/es/esenv/lib/python2.7/site-packages 
  WSGIProcessGroup expertsystem
  WSGIScriptAlias / /home/eis/es/Africa-project/ES/expertsystem/wsgi.py
</VirtualHost>
```
Note that if you are not using the eis user, that you will have to modify the paths above.
Grant permissions to your project directory:
```
sudo chown :www-data ~/es
```
Restart the apache2 server:
```
sudo service apache2 restart
```
At this point you should be able to access the expertsystem by pointing your browser to your servers IP address.

If you are getting Forbidden 403 error then most likely the path to the project is incorrect in  /etc/apache2/sites-available/000-default.conf. If you are sure the path is correct then double check that your project directory has the required permissions.  
If you are getting Bad Request 400 then most likely you are missing your servers IP address in the ALLOWED_HOSTS key in the django project's settings.py
If any errors persist then check the apache log files from the root user for further insight into the source of the problem:
```
sudo -i
nano /var/log/apache2/error.log
```

**Creating Django project superuser/Giving website permissions**
To create the django superuser:
```
cd ~/es/Africa-project/ES/
python manage.py createsuperuser
```
And follow the onscreen instructions. The following section assumes the superuser is named "admin".
To create the website permissions and give them to the superuser we must use the django shell:
```
python manage.py shell
```
From the shell:
```
>>> from django.contrib.auth.models import User, Permission
>>> from django.contrib.contenttypes.models import ContentType
>>> user = User.objects.get(username="admin")
>>> content_type = ContentType.objects.get_for_model(User)
>>> permission = Permission.objects.create(codename='is_creator', name='Can Create', content_type=content_type)
>>> user.user_permissions.add(permission)
>>> permission = Permission.objects.create(codename='is_admin', name='Is Admin', content_type=content_type)
>>> user.user_permissions.add(permission)
```
And check that the permissions were succesfully assigned:
```
>>> user.user_permissions.all()
[<Permission: auth | user | Is Admin>, <Permission: auth | user | Can Create>]
```
To create a new user and add existing permissions:
```
python manage.py shell
>>> from django.contrib.auth.models import User, Permission
>>> user = User.objects.create_user(username="test", password="test")
>>> permission = Permission.objects.get(codename="is_creator")
>>> user.user_permissions.add(permission)
>>> user.user_permissions.all()
[<Permission: auth | user | Can Create>]
```

