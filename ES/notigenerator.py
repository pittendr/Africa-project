#!../../esenv/bin/python
import MySQLdb
import simplejson
import pyowm
import urllib
import json
import smtplib

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
    fiaValues = []
    fiaList = []
	
    for recipes in recipelist:
        if isinstance(recipes, list):
            firstIter = True			
            for recipe in recipes:
                count=0
                if not firstIter:
                    variable = recipe[VAR_COL]
                    logic = recipe[LOG_COL]
                    value = recipe[VAL_COL]
                    while count < len(fiaValues):					
                        expression = str(fiaValues[count][getArrayIndex(variable)]) + str(logic) + str(value)
                        if not eval(expression):
                            del fiaValues[count]
                        count+=1								
                else:
                    fiaValues = getFIAValues(recipe,x)
                    if len(fiaValues)==0:
                        break;
                    firstIter=False
            for values in fiaValues:
                fiaList.append(values)
        else:
            singlefiaValues = getFIAValues(recipes, x)
            for values in singlefiaValues:
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

def getFIAValues(recipe, x):
    variable = recipe[VAR_COL]
    logic = recipe[LOG_COL]
    value = recipe[VAL_COL]
    alert = recipe[ALT_COL]
    if logic == '>':
        x.execute("""SELECT * FROM api_fia WHERE """+convertName(variable)+""" > '%s'""", [float(value)])    
    elif logic == '<':
        x.execute("""SELECT * FROM api_fia WHERE """+convertName(variable)+""" < '%s'""", [float(value)])
    elif logic =='=':
        x.execute("""SELECT * FROM api_fia WHERE """+convertName(variable)+""" = '%s'""", [float(value)])
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
        array.append(array2)
        count+=1
    return array

def getNumberAndAlert(list):
    array = []
    count = 0
    for item in list:
        array2 = []
        array2.append(item[getArrayIndex('Phone')])
        array2.append(item[getArrayIndex('Alert')])
        array.append(array2)
    return array

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
        x.execute("""SELECT * FROM notifications_recipe""");
        recipevals = x.fetchall()
		
        recipeList = getRecipeList(recipevals)
        finalList = getFIAList(recipeList,x)
		#have to remove duplicate phone numbers
        sendSMS(getNumberAndAlert(finalList))
    except Exception, Argument:
        print Argument
    finally:
        conn.close()
