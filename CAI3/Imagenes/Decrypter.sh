#!/bin/bash

mkdir "$1"/"$2"/DECRYPT/
key="$3"
iv="$4"
for file in "$1"/"$2"/*
do
    if [ -f "${file}" ] ; then
        nombre=$(basename "$file" .bmp)
        openssl enc $2 -d -in "${file}" -out "$1"/"$2"/DECRYPT/"$nombre"DECRYPT"$2".bmp  -K "$key" -iv "$iv"
        
    fi
done
