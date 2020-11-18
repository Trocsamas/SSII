#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Wed Nov 18 08:49:20 2020

@author: trocsamas
"""
import socket, ssl
# import pprint

context = ssl.SSLContext(ssl.PROTOCOL_TLSv1_2)
context.load_cert_chain(certfile="/home/trocsamas/certificate.pem", keyfile="/home/trocsamas/key.pem")

bindsocket = socket.socket()
bindsocket.bind(('127.0.0.1', 10023))
bindsocket.listen(5)

def deal_with_client(connstream):
    data = connstream.recv(1024)
    # empty data means the client is finished with us
    while data:
        print(data)
        data = connstream.recv(1024)

while True:
    print("Waiting for connections...")
    newsocket, fromaddr = bindsocket.accept()
    connstream = context.wrap_socket(newsocket, server_side=True)
    try:
        deal_with_client(connstream)
    finally:
        connstream.shutdown(socket.SHUT_RDWR)
        connstream.close()