import mysql.connector
import requests

mydb = mysql.connector.connect(
    host='localhost',
    user='root',
    password='example',
    database='vaccine'
)


def check_table_exists(table):
    with mydb.cursor(prepared=True) as cursor:
        query = '''SELECT COUNT(*) FROM information_schema.tables
                   WHERE table_schema = "vaccine" AND table_name = %s;'''
        cursor.execute(query, (table,))
        n_tables, = cursor.fetchone()

        return n_tables


def create_centre(id, data):
    name = data['name']
    county = data['county']
    address = ','.join([data[f'address{i + 1}'] for i in range(3) if data[f'address{i + 1}']])
    eircode = data['eircode']

    with mydb.cursor(prepared=True) as cursor:
        query = '''INSERT INTO vaccination_centre(id, name, county, address, eircode)
                    VALUES (%s, %s, %s, %s, %s);'''
        cursor.execute(query, (id, name, county, address, eircode))

    print(f'Centre "{name}" created.')


def create_centres():
    with mydb.cursor() as cursor:
        query = '''SELECT next_val FROM hibernate_sequence;'''
        cursor.execute(query)
        next_id, = cursor.fetchone()

    r = requests.get('https://www2.hse.ie/api/services/vaccine-clinics/?format=json')
    for data in r.json():
        create_centre(next_id, data)
        next_id += 1

    with mydb.cursor(prepared=True) as cursor:
        query = '''UPDATE hibernate_sequence SET next_val = %s;'''
        cursor.execute(query, (next_id,))

    mydb.commit()


while not check_table_exists('vaccination_centre'):
    pass

create_centres()

while not check_table_exists('users'):
    pass

mydb.close()
