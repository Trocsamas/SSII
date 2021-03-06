#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Wed Nov 18 13:31:54 2020

@author: Jaime Emilio Sala Mascort
"""
import socket, ssl

# La librería tkinter está en las librería básicas de python
import tkinter as tk


root = tk.Tk()
root.title('Entidad bancaria')
root.geometry("480x100")


lbl = tk.Label(root, text="Bienvenido a tu App Bancaria")
lbl.pack()
textEntry = tk.Entry(root, width=50)



def botonEnviar(conn,si,no):
    if si==None:
        buttonEnviar = tk.Button(root,text="Enviar",command=lambda:enviar(conn, buttonEnviar))
        buttonEnviar.pack()
    else:
        message = bytes("Si", 'utf-8')
        conn.send(message)
        si.destroy()
        no.destroy()
        buffer = conn.recv(1024)
        data = str(buffer,'utf-8').replace("\n","")
        lbl.configure(text = data)
        buttonEnviar = tk.Button(root,text="Enviar",command=lambda:enviar(conn, buttonEnviar))
        buttonEnviar.pack()
    return 0

def aceptar():
    try:    
        # context = ssl.SSLContext(ssl.PROTOCOL_TLSv1_2) Esto es para especificar el uso de tls 1.2 en el cliente
        context = ssl.SSLContext(ssl.PROTOCOL_TLS)
        conn = inicializarConexion(context)
    except:
        popUp = tk.Toplevel()
        popUp.wm_title("Error de conexión")
        errorText = tk.Label(popUp,text="Se ha producido un error al realizar la conexión")
        errorText.pack()
        b = tk.Button(popUp, text="Vale", command=popUp.destroy)
        b.pack()
    else:
        textEntry.pack()
        buttonAceptar.destroy()
        botonEnviar(conn,None,None)


def inicializarConexion(context):
     conn = context.wrap_socket(socket.socket(socket.AF_INET),server_hostname="127.0.0.1")
     conn.connect(("127.0.0.1", 7070))
     buffer = conn.recv(1024)
     data = str(buffer,'utf-8').replace("\n","")
     lbl.configure(text = data)
     return conn
 
def cerrarConexion(conn):
    conn.send(b'no')
    root.destroy()

def enviar(conn, buttonEnviar):
    message = bytes(textEntry.get(), 'utf-8')
    conn.send(message)
    buffer = conn.recv(1024)
    data = str(buffer,'utf-8').replace("\n","")
    lbl.configure(text = data)
    if "incorrecto" in data:
        buttonEnviar.destroy()
        tk.Button(root,text="Ok", command = lambda :cerrarConexion(conn)).pack()
        
    if "otra" in data:
        buttonNo = tk.Button(root,text="No", command = lambda :cerrarConexion(conn))
        buttonNo.pack()
        buttonSi = tk.Button(root,text="Si", command = lambda :botonEnviar(conn,buttonSi,buttonNo))
        buttonSi.pack()
        buttonEnviar.destroy()

buttonAceptar = tk.Button(root, text="Entrar", command=aceptar)
buttonAceptar.pack()

root.mainloop()


