#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Wed Nov 18 08:49:20 2020

@author: Jaime Emilio Sala Mascort
"""
import socket, ssl
import pprint

context = ssl.SSLContext(ssl.PROTOCOL_TLSv1_2)

# Idealmente los certificados se almacenan en un lugar seguro
context.load_cert_chain(certfile="./certificate.pem", keyfile="./key.pem")

bindsocket = socket.socket()
bindsocket.bind(('127.0.0.1', 7070))
bindsocket.listen(5)

# En este diccionario se encuentran los clientes del Banco y sus contraseñas
userDict = {"21439092Y":"9nyup23r@|saf","89125124Q":"52_k~#jbñAS"}

def comunicacionCliente(connstream):
    
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
        comunicacionCliente(connstream)
    except:
        print ("Se ha producido un fallo en la comunicacion con el cliente")
    finally:
        connstream.shutdown(socket.SHUT_RDWR)
        connstream.close()
