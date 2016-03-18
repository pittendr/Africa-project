#!../../esenv/bin/python
from flask import Flask, jsonify, request 
import MySQLdb
import simplejson
import pyowm
import urllib
import json

app = Flask(__name__)

googleapikey = 'AIzaSyBaIP0AbBO1GgS8q9i28FQGd8HopCMNES0'
weatherapikey = 'cdb2f4f903237bca966049d773d2e06e'

@app.route('/data', methods=['POST'])
def create_task():
    if not request.json or not 'gps' in request.json or not 'phone' in request.json or not 'pests' in request.json or not 'aid' in request.json or not 'mac' in request.json or not 'serial' in request.json or not 'time' in request.json:
        abort(400)

    latitude = str(json.loads(request.json['gps'])['latitude'])
    longitude = str(json.loads(request.json['gps'])['longitude'])

    url = 'https://maps.googleapis.com/maps/api/elevation/json?locations='+str(latitude)+','+str(longitude)+'&key=AIzaSyBaIP0AbBO1GgS8q9i28FQGd8HopCMNES0'
    resp = simplejson.load(urllib.urlopen(url))
    elevationarray = []
    for resultset in resp['results']:
        elevationarray.append(resultset['elevation'])
    elevation = elevationarray[0]

    owm = pyowm.OWM('cdb2f4f903237bca966049d773d2e06e')
    observation = owm.weather_at_coords(float(latitude), float(longitude))
    weather = observation.get_weather()
    windspeed = weather.get_wind()['speed']
    winddir = weather.get_wind()['deg']
    humidity = weather.get_humidity()
    temp = weather.get_temperature('fahrenheit')['temp']
    clouds = weather.get_clouds()

    data = {
        'phone': request.json['phone'],
        'gps': request.json['gps'],
        'pests': request.json['pests'],
        'aid': request.json['aid'],
        'mac': request.json['mac'],
        'serial': request.json['serial'],
        'time': request.json['time'],
        'elevation':elevation,
        'windspeed': windspeed,
        'winddir': winddir,
        'humidity': humidity,
        'temp': temp,
        'clouds': clouds
    }

    try:
        conn = MySQLdb.connect(host = "localhost", user = "eis", passwd = "188esplanade", db="expertsystem")
        x=conn.cursor()
        x.execute("""INSERT INTO fia(phone, gps, pests, aid, mac, serial, time, elevation, windspeed, winddir, humidity, temp, clouds) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)""", (data['phone'],data['gps'], data['pests'], data['aid'], data['mac'], data['serial'], data['time'], elevation, windspeed, winddir, humidity, temp, clouds))
        conn.commit()
    except Exception, Argument:
        print Argument
        conn.rollback()
    finally:
        conn.close()
    
    return jsonify({'data':data}),201 

if __name__ == '__main__':
    app.run("0.0.0.0",8080)
