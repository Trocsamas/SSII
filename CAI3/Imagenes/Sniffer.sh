#!/bin/bash

mkdir "$1"/SNIFFED/
for file in "$1"*
do
    if [ -f "${file}" ] ; then
        nombre=$(basename "$file" .bmp)
        
        head -c 54 $2 > header
        tail -c +55 $file > body_cbc
        cat header body_cbc > "$1"/SNIFFED/"$nombre"Sniff.bmp
        
        rm header
        rm body_cbc
    fi
done


