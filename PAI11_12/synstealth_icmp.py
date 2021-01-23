#!/usr/bin/python

from scapy.all import *

print("Realizando scan ICMP...\n")
ip = input("Introduce la dirección IP:\n")


'''Para realizar un ICMP scan, envíamos un paquete ICMP y estudiamos la respuesta que nos ha facilitado el sistema'''

paquete=IP(dst=ip)/ICMP()

respuesta_icmp= sr1(paquete,timeout=1, verbose=0)

if respuesta_icmp == None:
	print("El host en",ip,"no está disponible o no existe")
else:
	print("El host en",ip,"está disponible")

	
'''una vez realizado el scan mediante icmp de la IP, pasamos a realizar un SYN Stealth scan o Syn Scan o Half scan. Nos basaremos en los 20 puertos TCP más usados que suele escanear NMAP'''

puertos=[80,23,443,21,22,25,3389,110,445,139,143,53,135,3306,8080,1723,111,995,993,5900]
print("Realizando SYN Stelath scan...\n")


respuesta = sr(IP(dst=ip)/TCP(sport=RandShort(),dport=puertos,flags="S"))

print(respuesta)
