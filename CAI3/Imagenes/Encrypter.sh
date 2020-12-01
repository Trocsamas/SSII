#!/bin/bash

mkdir "$1"/"$2"
for file in "$1"*
do
    if [ -f "${file}" ] ; then
        nombre=$(basename "$file" .bmp)
        openssl enc $2 -e -in "${file}" -out "$1"/"$2"/"$nombre""$2".bmp  -K 1234567812345678123456781234567812345678123456781234567812345678 -iv 00000000000000000000000000000000
        
    fi
done