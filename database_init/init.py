import sys
import mysql.connector
import requests
import time

host = sys.argv[1] if len(sys.argv) > 1 else 'localhost'
while True:
    try:
        print('Trying', host)
        with mysql.connector.connect(host=host,user='root',password='example') as mydb:
            print('Creating db')
            with mydb.cursor() as cursor:
                cursor.execute('CREATE DATABASE IF NOT EXISTS vaccine')
            mydb.commit()
            mydb.close()
        break
    except Exception as e:
        print(e)
        continue


def check_table_exists(table):
    with mysql.connector.connect(
    host=host,
    user='root',
    password='example',
    database='vaccine'
    ) as mydb:
        with mydb.cursor(prepared=True) as cursor:
            query = '''SELECT COUNT(*) FROM information_schema.tables
                       WHERE table_schema = "vaccine" AND table_name = %s;'''
            cursor.execute(query, (table,))
            n_tables, = cursor.fetchone()

            return n_tables


def create_centre(db, id, data):
    name = data['name']
    county = data['county']
    address = ','.join([data[f'address{i + 1}'] for i in range(3) if data[f'address{i + 1}']])
    eircode = data['eircode']

    with db.cursor(prepared=True) as cursor:
        query = '''INSERT INTO vaccination_centre(id, name, county, address, eircode)
                    VALUES (%s, %s, %s, %s, %s);'''
        cursor.execute(query, (id, name, county, address, eircode))

    print(f'Centre "{name}" created.')


def create_centres():
    with mysql.connector.connect(
            host=host,
            user='root',
            password='example',
            database='vaccine') as mydb:
        with mydb.cursor() as cursor:
            query = '''SELECT next_val FROM hibernate_sequence;'''
            cursor.execute(query)
            next_id, = cursor.fetchone()

        r = requests.get('https://www2.hse.ie/api/services/vaccine-clinics/?format=json')
        for data in r.json():
            create_centre(mydb, next_id, data)
            next_id += 1

        with mydb.cursor(prepared=True) as cursor:
            query = '''UPDATE hibernate_sequence SET next_val = %s;'''
            cursor.execute(query, (next_id,))

        mydb.commit()


while not check_table_exists('vaccination_centre'):
    print('Waiting for centre table')
    time.sleep(1)
    pass

create_centres()

while not check_table_exists('users'):
    pass

mydb.close()
