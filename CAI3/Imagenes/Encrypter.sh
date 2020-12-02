#!/bin/bash

mkdir "$1"/"$2"
key="$3"
iv="$4"
for file in "$1"*
do
    if [ -f "${file}" ] ; then
        nombre=$(basename "$file" .bmp)
        openssl enc $2 -e -in "${file}" -out "$1"/"$2"/"$nombre""$2".bmp -K "$key" -iv "$iv"
        
    fi
done
