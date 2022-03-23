import psycopg2
from sshtunnel import SSHTunnelForwarder
from console import console

username = "mn9765"
password = "Faust_SW73114"
dbName = "p320_16"


try:
    with SSHTunnelForwarder(('starbug.cs.rit.edu', 22),
                            ssh_username=username,
                            ssh_password=password,
                            remote_bind_address=('localhost', 5432)) as server:
        server.start()
        print("SSH tunnel established")
        params = {
            'database': dbName,
            'user': username,
            'password': password,
            'host': 'localhost',
            'port': server.local_bind_port
        }
        conn = psycopg2.connect(**params)
        curs = conn.cursor()
        print("Database connection established")
        console(conn)
        conn.close()
except Exception as e:
    print("Connection failed: ", e)
    conn.close()

