#!/usr/bin/python

from scapy.all import *


ip = input("Introduce la dirección IP:\n")
print("Realizando scan ICMP...")

'''Para realizar un ICMP scan, envíamos un paquete ICMP y estudiamos la respuesta que nos ha facilitado el sistema'''

paquete=IP(dst=ip)/ICMP()

respuesta_icmp= sr1(paquete,timeout=1, verbose=0)

if respuesta_icmp == None:
	print("El host en",ip,"no está disponible o no existe\n")
else:
	print("El host en",ip,"está disponible\n")

	
'''una vez realizado el scan mediante icmp de la IP, pasamos a realizar un SYN Stealth scan o Syn Scan o Half scan. Nos basaremos en los 20 puertos TCP más usados que suele escanear NMAP'''

puertos=[80,23,443,21,22,25,3389,110,445,139,143,53,135,3306,8080,1723,111,995,993,5900]
abiertos=[]
filtrados=[]
cerrados=[]
if respuesta_icmp != None:
	print("Realizando SYN Stelath scan...")
	for puerto in puertos:
		p = IP(dst=ip)/TCP(sport=RandShort(), dport=puerto, flags='S')
		respuesta = sr1(p, timeout=2)
		if(respuesta is None):
			filtrados.append(puerto)
			continue;
		elif respuesta.haslayer(TCP):
			if respuesta.getlayer(TCP).flags == 0x14:
				cerrados.append(puerto)
				continue;
			elif respuesta.getlayer(TCP).flags == 0x12:
				abiertos.append(puerto)
				continue;            
			elif (int(respuesta.getlayer(ICMP).type)==3 and int(respuesta.getlayer(ICMP).code) in [1,2,3,9,10,13]):
				filtrados.append(puerto)
				continue;
else:
	print("Fin del scanner")

print("Los puertos abiertos son", abiertos)
print("Los puertos filtrados son", filtrados)
print("Los puertos cerrados son", cerrados)
	

