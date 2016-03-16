#!esenv/bin/python
from flask import Flask, jsonify, request 
import MySQLdb

app = Flask(__name__)
conn = MySQLdb.connect(host = "localhost", user = "eis", passwd = "188esplanade", db="expertsystem")

@app.route('/data', methods=['POST'])
def create_task():
    if not request.json or not 'gps' in request.json or not 'phone' in request.json or not 'pests' in request.json or not 'aid' in request.json or not 'mac' in request.json or not 'serial' in request.json or not 'time' in request.json:
        abort(400)
    data = {
        'phone': request.json['phone'],
        'gps': request.json['gps'],
        'pests': request.json['pest'],
	'aid': request.json['aid'],
	'mac': request.json['mac'],
	'serial': request.json['serial'],
	'time': request.json['serial']
    }

    x=conn.cursor()

    try:
        x.execute("""INSERT INTO fia(phone, gps, pests, aid, mac, serial, time) VALUES (%s, %s, %s, %s, %s, %s, %s)""", (data.phone, data.gps, data.pests, data.aid, data.mac, data.serial, data.time))
        conn.commit()
    except:
        conn.rollback()

    conn.close()
    
    return jsonify({'data': data}), 201

if __name__ == '__main__':
    app.run("0.0.0.0",8080)
