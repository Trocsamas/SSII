#!/bin/bash

mkdir "$1"/"$2"/DECRYPT/
for file in "$1"/"$2"/*
do
    if [ -f "${file}" ] ; then
        nombre=$(basename "$file" .bmp)
        openssl enc $2 -d -in "${file}" -out "$1"/"$2"/DECRYPT/"$nombre"DECRYPT"$2".bmp  -K 1234567812345678123456781234567812345678123456781234567812345678 -iv 00000000000000000000000000000000
        
    fi
done