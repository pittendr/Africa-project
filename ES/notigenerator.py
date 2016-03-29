#!../../esenv/bin/python
import MySQLdb
import simplejson
import pyowm
import urllib
import json

ID_COL = 0
VAR_COL = 1
LOG_COL = 2
VAL_COL = 3
RAN_COL = 4
MAT_COL = 6

def getRecipes():
    try:
        conn = MySQLdb.connect(host = "localhost", user = "eis", passwd = "188esplanade", db="expertsystem")
        x=conn.cursor()
        x.execute("""SELECT * FROM notifications_recipe""");
        recipes = x.fetchall()
      
        for recipe in recipes:
            variable = recipe[VAR_COL]
            logic = recipe[LOG_COL]
            value = recipe[VAL_COL]
            range = recipe[RAN_COL]
            match = recipe[MAT_COL]
            if logic == '>':
                x.execute("""SELECT * FROM api_fia WHERE %s > %s""", [convertName(variable).replace("'",""), float(value)])
            elif logic == '<':
                x.execute("""SELECT * FROM api_fia WHERE %s < %s""", [convertName(variable).replace("'",""), float(value)])
            elif logic =='=':
                x.execute("""SELECT * FROM api_fia WHERE  = %s""", [convertName(variable), float(value)])
            print x.fetchall()
    except Exception, Argument:
        print Argument
    finally:
        conn.close()
		
def convertName(name):
    if name == 'Elevation':
	    return 'elevation'
    elif name == 'Wind Speed':
        return 'windspeed'
    elif name == 'Wind Direction':
        return 'winddir'
    elif name == 'Humidity':
        return 'humidity'
    elif name == 'Temperature':
        return 'temp'
    elif name == 'Rain':
        return 'rain'
    elif name == 'Cloud Coverage':
        return 'clouds'
		
if __name__ == '__main__':
    getRecipes()
