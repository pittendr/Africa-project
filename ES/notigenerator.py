import MySQLdb
import simplejson
import pyowm
import urllib
import json
import smtplib
import math

ID_COL = 0
VAR_COL = 1
LOG_COL = 2
VAL_COL = 3
RAN_COL = 4
MAT_COL = 6
ALT_COL = 7

def getRecipeList(recipes): 
    oldmatch = -1
    list = []
    matchingList = []
	
    for recipe in recipes:
        match = recipe[MAT_COL]
        if match == "null":
            if matchingList:
                list.append(matchingList)
                matchingList = []
            list.append(recipe)
        else:
            if match == oldmatch:
                matchingList.append(recipe)
            else:
                if matchingList:
                    list.append(matchingList)
                    matchingList = []
                matchingList.append(recipe)
		
        oldmatch = match
    if matchingList:
        list.append(matchingList)
        matchingList = []
    return list

def getFIAList(recipelist,x):
    fiaList = []
	
    for recipes in recipelist:
            fiaValues = getFIAValues(recipes,x)
            for values in fiaValues:
                fiaList.append(values)
				
    return fiaList
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
		
def getArrayIndex(name):
    if name == 'Elevation':
	    return 8
    elif name == 'Wind Speed':
        return 9
    elif name == 'Wind Direction':
        return 10
    elif name == 'Humidity':
        return 11
    elif name == 'Temperature':
        return 12
    elif name == 'Rain':
        return 13
    elif name == 'Cloud Coverage':
        return 14
    elif name == 'Alert':
        return 15
    elif name == 'Phone':
        return 1	
    elif name == 'GPS':
        return 2
    elif name == 'Range':
        return 16

def getFIAValues(recipe, x):
    count = len(recipe)
    sqlStatement = "SELECT * FROM api_fia WHERE"
    firstIter = True
    variable = None
    logic = None
    value = None
    alert = None
    range = None
    for item in recipe:
        variable = item[VAR_COL]
        logic = item[LOG_COL]
        value = item[VAL_COL]
        alert = item[ALT_COL]
        range = item[RAN_COL]
        if firstIter:
            sqlStatement += " "+ str(convertName(variable)+ " " + str(logic) + " " + str(value)) 
            firstIter = False
        else:
            sqlStatement += " and " + str(convertName(variable)+ " " + str(logic) + " " + str(value)) 
    
    x.execute(sqlStatement)

    array = []
    count = 0
    y=x.fetchall()
    while count < len(y):
        count2 = 0
        array2 = []
        while count2 < len(y[count]):
            array2.append(y[count][count2])
            count2+=1
        array2.append(alert)
        array2.append(range)		
        array.append(array2)
        count+=1
		
    return array

def getGPSAndAlert(list):
    array = []
    count = 0
    for item in list:
        array2 = []
        array2.append(item[getArrayIndex('GPS')])
        array2.append(item[getArrayIndex('Alert')])
        array2.append(item[getArrayIndex('Range')])
        array.append(array2)
    return array

def removeDuplicates(x):
    a = []
    for i in x:
        if i not in a:
            a.append(i)
    return a

def findFarms(list, x):
    farmList = []
    farmFeatures = []
    x.execute("""SELECT * FROM api_fia""")
    farms = x.fetchall()
    for item in list:
        range=item[2]
        gps1 = json.loads(item[0])
        lat1 = float(gps1['latitude'])
        lon1 = float(gps1['longitude'])
        for farm in farms:
            gps2 = json.loads(farm[2])
            lat2 = float(gps2['latitude'])
            lon2 = float(gps2['longitude'])
            R = 6371
            dLat = math.radians(lat2-lat1)
            dLon = math.radians(lon2-lon1)
            lat1 = math.radians(lat1)
            lat2 = math.radians(lat2)

            a = math.sin(dLat/2) * math.sin(dLat/2) + math.sin(dLon/2) * math.sin(dLon/2) * math.cos(lat1) * math.cos(lat2)
            c = 2 * math.atan2(math.sqrt(a),math.sqrt(1-a))
            d = R * c
            if d <= range:
                farmFeatures.append(farm[1])
                farmFeatures.append(item[1])
                farmList.append(farmFeatures)
                farmFeatures = []
                print farmList

    return removeDuplicates(farmList)
	
def sendSMS(list):
    #using google smtp
    server = smtplib.SMTP( "smtp.gmail.com", 587 )
    server.ehlo()
    server.starttls()
    server.ehlo()
    server.login( 'fianotificationgenerator@gmail.com', 'expertsystem' )
    for item in list:
        phone = item[0]
        alert = item[1]
        if alert == "Spray":
            server.sendmail( 'eis', phone+'@sms.fido.ca', 'You need to spray' )
        else: 
            server.sendmail( 'eis', phone+'@sms.fido.ca', 'You need to scout' )
   
if __name__ == '__main__':
    try:
        #connect to mysql db
        conn = MySQLdb.connect(host = "localhost", user = "eis", passwd = "188esplanade", db="expertsystem")
        x=conn.cursor()
        x.execute("""SELECT * FROM api_recipe""")
        recipevals = x.fetchall()
		
        recipeList = getRecipeList(recipevals)
        finalList = getFIAList(recipeList,x)
        finalList = removeDuplicates(finalList)
        finalList = getGPSAndAlert(finalList)
        print findFarms(finalList, x)
    except Exception, Argument:
        print Argument
    finally:
        conn.close()
