#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Wed Nov 18 08:49:20 2020

@author: trocsamas
"""
import socket, ssl
import pprint

context = ssl.SSLContext(ssl.PROTOCOL_TLSv1_2)
context.load_cert_chain(certfile="/home/trocsamas/certificate.pem", keyfile="/home/trocsamas/key.pem")

bindsocket = socket.socket()
bindsocket.bind(('127.0.0.1', 10023))
bindsocket.listen(5)

userDict = {"30256459M":"Hola_1234"}

def deal_with_client(connstream):
    
    message = "Hola Cliente, envíe su usuario\n".encode('utf-8')
    connstream.send(message)
    
    data = connstream.recv(1024)
    usuario = str(data,'utf-8').replace("\n","")
    print(usuario)
        
    message = ("Gracias "+ usuario +", envíe su contraseña\n").encode('utf-8')
    connstream.send(message)
    
    data = connstream.recv(1024)
    password= str(data,'utf-8').replace("\n","")
    print(password)
    
    if usuario in userDict and userDict[usuario] == password:
         while data:
             message = ("Gracias "+ usuario +", ¿que transacción desea realizar?\n").encode('utf-8')
             connstream.send(message)
             data = connstream.recv(1024)
             print(data)
             message = ("¿Desea realizar otra transacción?\n").encode('utf-8')
             connstream.send(message)
             data = connstream.recv(1024)
             print(data)
             if str(data,'utf-8').replace("\n","") == "no":
                 message = ("Cerrando conexión, muchas gracias\n").encode('utf-8')
                 connstream.send(message)
                 data = None
             
    else:
        message = ("Usuario o Contraseña incorrecto, cerrando conexión\n").encode('utf-8')
        connstream.send(message)

        

while True:
    print("Waiting for connections...")
    newsocket, fromaddr = bindsocket.accept()
    connstream = context.wrap_socket(newsocket, server_side=True)
    try:
        deal_with_client(connstream)
    finally:
        connstream.shutdown(socket.SHUT_RDWR)
        connstream.close()