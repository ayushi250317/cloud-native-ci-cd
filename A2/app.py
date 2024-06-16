from flask import Flask, request, jsonify, Response
import mysql.connector
import json
from collections import OrderedDict
from db_config import db_config

app = Flask(__name__)

def get_db_connection():
    conn = mysql.connector.connect(**db_config)
    return conn

@app.route('/store-products', methods=['POST'])
def insert_data():
    try:
        data = request.get_json()
        product_list=data.get('products')
        conn = get_db_connection()
        for item in product_list:
            name = item["name"]
            price = item["price"]
            availability=item["availability"]
            cursor = conn.cursor()

            insert_query = "INSERT INTO products (name, price, availability) VALUES (%s, %s,%s)"
            avail=int(availability)
            values = (name, price,avail)

            cursor.execute(insert_query, values)
        
        conn.commit()

        return jsonify({'message': 'Success.'}), 200
    except Exception as e:
        return jsonify({'error': str(e)}), 500
    finally:
        cursor.close()
        conn.close()
@app.route('/list-products', methods=['GET'])
def get_products():
    try:
        conn = get_db_connection()
        cursor = conn.cursor(dictionary=True) 
        query = "SELECT name, price, availability FROM products"
        cursor.execute(query)
        products = cursor.fetchall()
        product_list = []
        for product in products:
            product_dict = OrderedDict([
                ('name', product['name']),
                ('price', product['price']),
                ('availability', bool(product['availability']))
            ])
            product_list.append(product_dict)

        response_json = json.dumps({'products': product_list}, sort_keys=False)
        return Response(response_json, mimetype='application/json')
    except Exception as e:
        return jsonify({'error': str(e)}), 500
    finally:
        cursor.close()
        conn.close()

if __name__ == '__main__':
    app.run(debug=True, port=8000)