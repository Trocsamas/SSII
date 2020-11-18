#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Wed Nov 18 10:22:22 2020

@author: trocsamas
"""
import socket, ssl, pprint
import tkinter



context = ssl.SSLContext(ssl.PROTOCOL_TLSv1_2)
context.load_cert_chain(certfile="/home/trocsamas/certificate-client.pem", keyfile="/home/trocsamas/key-client.pem",password="PASSWORD2")
conn = context.wrap_socket(socket.socket(socket.AF_INET),server_hostname="127.0.0.1")
conn.connect(("127.0.0.1", 10023))



data = conn.recv(1024)
pprint.pprint(data.decode('utf-8'))

conn.send(b"30256459M")
data = conn.recv(1024)
pprint.pprint(data.decode('utf-8'))

conn.send(b"Hola_1234")
data = conn.recv(1024)
pprint.pprint(data.decode('utf-8'))

conn.send(b"12 12 1200")
data = conn.recv(1024)
pprint.pprint(data.decode('utf-8'))

conn.send(b"si")
data = conn.recv(1024)
pprint.pprint(data.decode('utf-8'))

conn.send(b"13 12 1200")
data = conn.recv(1024)
pprint.pprint(data.decode('utf-8'))

conn.send(b"no")
data = conn.recv(1024)
pprint.pprint(data.decode('utf-8'))


